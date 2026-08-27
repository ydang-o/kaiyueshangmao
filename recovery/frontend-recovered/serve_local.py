from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import urlsplit
from urllib.request import Request, urlopen
from urllib.error import HTTPError, URLError
import os

STATIC_ROOT = Path(__file__).resolve().parent / "dist"
LOCAL_BACKEND = "http://127.0.0.1:7500"
ONLINE_BACKEND = "https://kaiyueshangmao.xyz"
PROXY_PREFIXES = ("/dev-api", "/prod-api")
ONLINE_RESOURCE_PREFIXES = (
    "/profile/", "/dev-api/profile/", "/prod-api/profile/", "/prod-api/dev-api/profile/"
)
ONLINE_AUTH_PATHS = {"/login", "/logout", "/getInfo", "/getRouters", "/captchaImage"}

class Handler(SimpleHTTPRequestHandler):
    protocol_version = "HTTP/1.1"

    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=str(STATIC_ROOT), **kwargs)

    def _proxy(self):
        parsed = urlsplit(self.path)
        prefix = next((p for p in PROXY_PREFIXES if parsed.path == p or parsed.path.startswith(p + "/")), None)
        remote_resource = next((p for p in ONLINE_RESOURCE_PREFIXES if parsed.path.startswith(p)), None)
        if prefix is None and remote_resource is None:
            return False

        if remote_resource:
            resource_path = parsed.path
            if resource_path.startswith("/dev-api/"):
                resource_path = resource_path[len("/dev-api"):]
            elif resource_path.startswith("/prod-api/"):
                resource_path = resource_path[len("/prod-api"):]
            target = ONLINE_BACKEND + resource_path
        else:
            backend_path = parsed.path[len(prefix):] or "/"
            use_online = self.command in {"GET", "HEAD"} or backend_path in ONLINE_AUTH_PATHS
            target = (ONLINE_BACKEND if use_online else LOCAL_BACKEND) + backend_path

        if parsed.query:
            target += "?" + parsed.query
        length = int(self.headers.get("Content-Length", "0"))
        body = self.rfile.read(length) if length else None
        blocked_headers = {"host", "content-length", "connection"}
        if remote_resource:
            blocked_headers.update({"authorization", "cookie"})
        headers = {k: v for k, v in self.headers.items() if k.lower() not in blocked_headers}
        headers["Host"] = urlsplit(target).netloc
        headers["Accept-Encoding"] = "identity"
        request = Request(target, data=body, headers=headers, method=self.command)
        try:
            response = urlopen(request, timeout=30)
            status = response.status
            data = response.read()
            response_headers = response.headers
        except HTTPError as error:
            status = error.code
            data = error.read()
            response_headers = error.headers
        except URLError as error:
            data = ("Remote resource unavailable: " + str(error)).encode("utf-8")
            status = 502
            response_headers = {}
        self.send_response(status)
        for key, value in response_headers.items():
            if key.lower() not in {"connection", "transfer-encoding", "content-length", "content-encoding"}:
                self.send_header(key, value)
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)
        return True

    def do_GET(self):
        if self._proxy():
            return
        path = (STATIC_ROOT / urlsplit(self.path).path.lstrip("/")).resolve()
        if not str(path).startswith(str(STATIC_ROOT.resolve())) or (not path.exists() and not path.is_file()):
            self.path = "/index.html"
        super().do_GET()

    def do_POST(self):
        if not self._proxy():
            self.send_error(405)

    def do_PUT(self):
        if not self._proxy():
            self.send_error(405)

    def do_DELETE(self):
        if not self._proxy():
            self.send_error(405)

    def end_headers(self):
        self.send_header("Cache-Control", "no-store, no-cache, must-revalidate")
        super().end_headers()

    def log_message(self, format, *args):
        print("[frontend] " + format % args, flush=True)

if __name__ == "__main__":
    port = int(os.environ.get("PORT", "5173"))
    print(f"Serving {STATIC_ROOT} at http://127.0.0.1:{port}", flush=True)
    ThreadingHTTPServer(("127.0.0.1", port), Handler).serve_forever()
