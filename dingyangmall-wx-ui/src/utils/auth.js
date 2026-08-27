import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token-Online'

export function getToken() {
  try {
    return localStorage.getItem(TokenKey) || Cookies.get(TokenKey)
  } catch (e) {
    return Cookies.get(TokenKey)
  }
}

export function setToken(token) {
  try {
    localStorage.setItem(TokenKey, token)
  } catch (e) {
    // Some privacy modes disable localStorage; keep the cookie fallback.
  }
  return Cookies.set(TokenKey, token, { path: '/' })
}

export function removeToken() {
  try {
    localStorage.removeItem(TokenKey)
  } catch (e) {
    // Ignore storage cleanup errors.
  }
  return Cookies.remove(TokenKey, { path: '/' })
}

