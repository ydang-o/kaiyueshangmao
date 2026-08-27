const IMAGE_BASE_URL = import.meta.env.VITE_IMAGE_BASE_URL || "https://kaiyueshangmao.xyz";
const API_BASE_URL = import.meta.env.VITE_APP_BASE_API || "";

export function resolveImageUrl(value) {
  if (!value) return "";
  const raw = String(value).trim();
  if (!raw) return "";
  if (/^https?:\/\//i.test(raw) || raw.startsWith("blob:") || raw.startsWith("data:")) {
    return raw;
  }

  // Remove only the local production prefix. Keep /dev-api because the online
  // deployment exposes uploaded files at /dev-api/profile/upload/*.
  let path = raw.startsWith("/") ? raw : `/${raw}`;
  path = path.replace(/^\/prod-api/i, "");
  if (/^\/(?:dev-api\/)?profile\//i.test(path)) {
    return `${IMAGE_BASE_URL}${path}`;
  }
  if (/^\d+$/.test(raw)) {
    return `${IMAGE_BASE_URL}/profile/file/${raw}`;
  }
  if (API_BASE_URL && API_BASE_URL !== "/" && path.startsWith(API_BASE_URL)) {
    return path;
  }
  return `${API_BASE_URL}${path}`;
}
export default resolveImageUrl;

