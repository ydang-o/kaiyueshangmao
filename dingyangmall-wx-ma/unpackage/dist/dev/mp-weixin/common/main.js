(global["webpackJsonp"] = global["webpackJsonp"] || []).push([["common/main"],{

/***/ 0:
/*!**********************************************************************!*\
  !*** D:/work_boss/dingyangMall/JooLun-wx/dingyangmall-wx-ma/main.js ***!
  \**********************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(wx, createApp) {

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ 4);
var _defineProperty2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/defineProperty */ 11));
__webpack_require__(/*! uni-pages */ 26);
var _vue = _interopRequireDefault(__webpack_require__(/*! vue */ 25));
var _App = _interopRequireDefault(__webpack_require__(/*! ./App.vue */ 27));
var _imageUrl = __webpack_require__(/*! ./utils/imageUrl */ 264);
function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); enumerableOnly && (symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; })), keys.push.apply(keys, symbols); } return keys; }
function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = null != arguments[i] ? arguments[i] : {}; i % 2 ? ownKeys(Object(source), !0).forEach(function (key) { (0, _defineProperty2.default)(target, key, source[key]); }) : Object.getOwnPropertyDescriptors ? Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)) : ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } return target; }
// @ts-ignore
wx.__webpack_require_UNI_MP_PLUGIN__ = __webpack_require__;
console.log('[如囍优选] main.js 已加载');
_vue.default.config.productionTip = false;
_vue.default.prototype.$imgUrl = _imageUrl.fullImageUrl;
_App.default.mpType = 'app';
var app = new _vue.default(_objectSpread({}, _App.default));
createApp(app).$mount();
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/wx.js */ 1)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["createApp"]))

/***/ }),

/***/ 27:
/*!**********************************************************************!*\
  !*** D:/work_boss/dingyangMall/JooLun-wx/dingyangmall-wx-ma/App.vue ***!
  \**********************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./App.vue?vue&type=script&lang=js& */ 28);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
/* harmony import */ var _App_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./App.vue?vue&type=style&index=0&lang=css& */ 32);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/runtime/componentNormalizer.js */ 34);
var render, staticRenderFns, recyclableRender, components
var renderjs





/* normalize component */

var component = Object(_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_2__["default"])(
  _App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__["default"],
  render,
  staticRenderFns,
  false,
  null,
  null,
  null,
  false,
  components,
  renderjs
)

component.options.__file = "App.vue"
/* harmony default export */ __webpack_exports__["default"] = (component.exports);

/***/ }),

/***/ 28:
/*!***********************************************************************************************!*\
  !*** D:/work_boss/dingyangMall/JooLun-wx/dingyangmall-wx-ma/App.vue?vue&type=script&lang=js& ***!
  \***********************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/babel-loader/lib!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./App.vue?vue&type=script&lang=js& */ 29);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 29:
/*!******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/babel-loader/lib!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!D:/work_boss/dingyangMall/JooLun-wx/dingyangmall-wx-ma/App.vue?vue&type=script&lang=js& ***!
  \******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(uni, wx) {

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ 4);
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _defineProperty2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/defineProperty */ 11));
var _typeof2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/typeof */ 13));
var _api = _interopRequireDefault(__webpack_require__(/*! ./utils/api */ 30));
var _env = _interopRequireDefault(__webpack_require__(/*! ./config/env */ 31));
function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); enumerableOnly && (symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; })), keys.push.apply(keys, symbols); } return keys; }
function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = null != arguments[i] ? arguments[i] : {}; i % 2 ? ownKeys(Object(source), !0).forEach(function (key) { (0, _defineProperty2.default)(target, key, source[key]); }) : Object.getOwnPropertyDescriptors ? Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)) : ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } return target; }
var _default = {
  onLaunch: function onLaunch() {
    console.log('Hello World');
    console.log('[如囍优选] App.onLaunch 已执行');
    // 注意：onLaunch 时 getApp() 可能尚未可用，必须用 this 指向当前 App 实例
    this.api = _api.default;
    this.globalData = {
      __api: _api.default,
      wxToken: null,
      thirdSession: null,
      wxUser: null,
      profileSkipped: false,
      config: _env.default,
      StatusBar: 0,
      Custom: null,
      CustomBar: 0,
      shoppingCartCount: '0'
    };
    // 冷启动时从 storage 恢复令牌，后续请求带 X-Wx-Token
    try {
      var _u = typeof uni !== 'undefined' ? uni : wx;
      if (_u.getStorageSync) {
        var saved = _u.getStorageSync('wx_token');
        if (saved && typeof saved === 'string' && saved.length > 0) {
          this.globalData.wxToken = saved;
          this.globalData.thirdSession = saved;
          console.log('[App] 已从 storage 恢复 token');
        }
      }
    } catch (e) {
      console.warn('[App] 恢复 token 失败', e && e.message);
    }
    this.initPage = this.initPage.bind(this);
    this.doLogin = this.doLogin.bind(this);
    this.shoppingCartCount = this.shoppingCartCount.bind(this);
    this.getCurrentPageUrlWithArgs = this.getCurrentPageUrlWithArgs.bind(this);

    // 诊断：仅在有 token 时发起测试请求，不在启动时自动静默登录（避免未点「微信一键登录」就调登录接口、启动页不跳转）
    var that = this;
    setTimeout(function () {
      if ((that.globalData.wxToken || that.globalData.thirdSession) && that.api && typeof that.api.goodsCategoryGet === 'function') {
        console.log('[App] 诊断：发起测试请求', _env.default.basePath);
        that.api.goodsCategoryGet().then(function () {
          return console.log('[App] 诊断请求成功');
        }).catch(function (e) {
          return console.warn('[App] 诊断请求失败', e);
        });
      }
    }, 300);
    this.updateManager();
    var u = typeof uni !== 'undefined' ? uni : wx;
    u.getSystemInfo({
      success: function success(e) {
        that.globalData.StatusBar = e.statusBarHeight;
        var custom = u.getMenuButtonBoundingClientRect && u.getMenuButtonBoundingClientRect();
        if (custom) {
          that.globalData.Custom = custom;
          that.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
        }
      }
    });
  },
  onShow: function onShow() {},
  onHide: function onHide() {},
  methods: {
    updateManager: function updateManager() {
      var u = typeof uni !== 'undefined' ? uni : wx;
      if (!u.getUpdateManager) return;
      var updateManager = u.getUpdateManager();
      updateManager.onUpdateReady(function () {
        u.showModal({
          title: '更新提示',
          content: '新版本已经准备好，是否重启应用？',
          success: function success(res) {
            if (res.confirm) updateManager.applyUpdate();
          }
        });
      });
    },
    initPage: function initPage() {
      var that = getApp();
      var u = typeof uni !== 'undefined' ? uni : wx;
      return new Promise(function (resolve) {
        if (that.globalData.wxToken || that.globalData.thirdSession) {
          u.checkSession({
            success: function success() {
              return resolve('success');
            },
            fail: function fail() {
              that.globalData.wxToken = null;
              that.globalData.thirdSession = null;
              that.globalData.wxUser = null;
              try {
                if (u.removeStorageSync) u.removeStorageSync('wx_token');
                if (u.removeStorageSync) u.removeStorageSync('wx_third_session');
              } catch (e) {}
              resolve('success');
            }
          });
          return;
        }
        // 无 token 时不再自动静默登录，避免未点击「微信一键登录」就调登录接口；需用户主动点击登录
        resolve('success');
      });
    },
    _doSilentLogin: function _doSilentLogin() {
      // 单例：多接口同时 60002 时共享同一登录请求，避免疯狂重试
      var that = getApp();
      if (that._silentLoginPromise) return that._silentLoginPromise;
      var u = typeof uni !== 'undefined' ? uni : wx;
      // 微信环境下必须用官方 wx.login 取 code，后端据此调 code2Session
      var loginApi = typeof wx !== 'undefined' && wx.login ? wx : u;
      that._silentLoginPromise = new Promise(function (resolve, reject) {
        loginApi.login({
          success: function success(res) {
            if (!res.code) {
              reject(new Error('no code'));
              return;
            }
            _api.default.login({
              code: res.code,
              jsCode: res.code
            }).then(function (resp) {
              var raw = resp && (0, _typeof2.default)(resp) === 'object' ? resp : {};
              var data = raw.data && (0, _typeof2.default)(raw.data) === 'object' ? raw.data : raw;
              if (data && data.data && (0, _typeof2.default)(data.data) === 'object' && (data.data.token != null || data.data.openid != null)) data = data.data;
              var token = data && (data.token != null ? data.token : data.thirdSession || data.sessionKey) || (raw.token != null ? raw.token : null);
              var openid = data && (data.openid != null ? data.openid : data.userId != null ? data.userId : '');
              var userId = data && (data.userId != null ? data.userId : openid);
              if (!token) {
                reject(new Error('no token'));
                return;
              }
              that.globalData.wxToken = token;
              that.globalData.thirdSession = token;
              try {
                var _u2 = typeof uni !== 'undefined' ? uni : wx;
                if (_u2.setStorageSync) {
                  _u2.setStorageSync('wx_token', token);
                  _u2.setStorageSync('wx_third_session', token);
                }
              } catch (e) {}
              that.globalData.wxUser = _objectSpread({
                sessionKey: token,
                userId: userId || openid,
                openid: openid || userId,
                nickName: data && (data.nickname != null ? data.nickname : data.nickName) || '',
                headimgUrl: data && (data.avatarUrl != null ? data.avatarUrl : data.headimgUrl || data.avatar) || ''
              }, data || {});
              that._silentLoginPromise = null;
              that._loginFailedAt = 0;
              resolve();
            }).catch(function (err) {
              console.warn('[静默登录] 失败', err);
              that._silentLoginPromise = null;
              that._loginFailedAt = Date.now();
              reject(err);
            });
          },
          fail: function fail() {
            that._silentLoginPromise = null;
            that._loginFailedAt = Date.now();
            reject(new Error('wx.login fail'));
          }
        });
      });
      return that._silentLoginPromise;
    },
    doLogin: function doLogin() {
      var that = getApp();
      // 单例：防止用户快速多次点击或多处同时调用导致重复请求
      if (that._doLoginPromise) return that._doLoginPromise;
      var u = typeof uni !== 'undefined' ? uni : wx;
      u.showLoading({
        title: '登录中'
      });
      // 微信小程序必须走官方 wx.login 获取 code，后端据此调微信 code2Session
      var loginApi = typeof wx !== 'undefined' && wx.login ? wx : u;
      that._doLoginPromise = new Promise(function (resolve) {
        loginApi.login({
          success: function success(res) {
            if (res.code) {
              _api.default.login({
                code: res.code,
                jsCode: res.code
              }).then(function (resp) {
                u.hideLoading();
                var raw = resp && (0, _typeof2.default)(resp) === 'object' ? resp : {};
                var data = raw.data && (0, _typeof2.default)(raw.data) === 'object' ? raw.data : raw;
                if (data && data.data && (0, _typeof2.default)(data.data) === 'object' && (data.data.token != null || data.data.openid != null)) data = data.data;
                if (!(data && (data.token != null || data.userId != null || data.openid != null))) data = raw;
                var openid = data && (data.openid != null ? data.openid : data.userId != null ? data.userId : '');
                var userId = data && (data.userId != null ? data.userId : openid);
                var token = data && (data.token != null ? data.token : data.thirdSession || data.sessionKey) || (raw.token != null ? raw.token : null) || openid || userId;
                if (!token) {
                  console.warn('[Login] 响应中无 token', raw);
                  that._doLoginPromise = null;
                  resolve('fail');
                  return;
                }
                console.log('[Login] 成功，已保存 token', {
                  openid: openid ? openid.slice(0, 12) + '...' : '(空)',
                  tokenPrefix: token ? token.slice(0, 12) + '...' : '(空)'
                });
                if (_env.default.apiDebug) console.log('[Login] 解析结果', {
                  token: token.slice(0, 12) + '...',
                  userId: userId,
                  openid: openid,
                  hasNickname: !!(data.nickname || data.nickName),
                  hasAvatar: !!(data.avatarUrl || data.avatar)
                });
                that._loginFailedAt = 0;
                that.globalData.wxToken = token;
                that.globalData.thirdSession = token;
                try {
                  if (u.setStorageSync) {
                    u.setStorageSync('wx_token', token);
                    u.setStorageSync('wx_third_session', token);
                  }
                } catch (e) {}
                that.globalData.wxUser = _objectSpread({
                  sessionKey: token,
                  userId: userId,
                  openid: openid || userId,
                  nickName: data.nickname != null ? data.nickname : data.nickName || '',
                  headimgUrl: data.avatarUrl != null ? data.avatarUrl : data.headimgUrl || data.avatar || '',
                  avatarUrl: data.avatarUrl,
                  nickname: data.nickname,
                  unionid: data.unionid
                }, data);
                that._doLoginPromise = null;
                resolve('success');
              }).catch(function (err) {
                console.warn('[Login] 请求失败', err);
                u.hideLoading();
                that._doLoginPromise = null;
                that._loginFailedAt = Date.now();
                resolve('fail');
              });
            } else {
              u.hideLoading();
              that._doLoginPromise = null;
              that._loginFailedAt = Date.now();
              resolve('fail');
            }
          },
          fail: function fail() {
            u.hideLoading();
            that._doLoginPromise = null;
            that._loginFailedAt = Date.now();
            resolve('fail');
          }
        });
      });
      return that._doLoginPromise;
    },
    shoppingCartCount: function shoppingCartCount() {
      var that = getApp() || this;
      if (!that || !that.globalData) return;
      var hasToken = !!(that.globalData.wxToken || that.globalData.thirdSession);
      if (!hasToken) {
        that.globalData.shoppingCartCount = '0';
        var u = typeof uni !== 'undefined' ? uni : wx;
        if (u.setTabBarBadge) u.setTabBarBadge({
          index: 2,
          text: '0'
        }).catch(function () {});
        return;
      }
      var apiRef = that && that.api || that.globalData && that.globalData.__api || (typeof _api.default !== 'undefined' ? _api.default : null);
      if (!apiRef || typeof apiRef.shoppingCartCount !== 'function') return;
      apiRef.shoppingCartCount().then(function (res) {
        if (that && that.globalData) that.globalData.shoppingCartCount = (res.data != null ? res.data : res) + '';
        var u = typeof uni !== 'undefined' ? uni : wx;
        if (that && that.globalData && u.setTabBarBadge) u.setTabBarBadge({
          index: 2,
          text: that.globalData.shoppingCartCount + ''
        });
      }).catch(function () {
        if (that && that.globalData) that.globalData.shoppingCartCount = '0';
      });
    },
    getCurrentPageUrlWithArgs: function getCurrentPageUrlWithArgs() {
      var pages = getCurrentPages();
      var currentPage = pages[pages.length - 1];
      var route = currentPage.route || currentPage.$page && currentPage.$page.fullPath && currentPage.$page.fullPath.replace(/^\//, '').replace(/\?.*/, '');
      var options = currentPage.options || currentPage.$page && currentPage.$page.options || {};
      var urlWithArgs = "/".concat(route || '', "?");
      for (var key in options) {
        urlWithArgs += "".concat(key, "=").concat(options[key], "&");
      }
      return urlWithArgs.substring(0, urlWithArgs.length - 1);
    }
  }
};
exports.default = _default;
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/wx.js */ 1)["default"]))

/***/ }),

/***/ 32:
/*!*******************************************************************************************************!*\
  !*** D:/work_boss/dingyangMall/JooLun-wx/dingyangmall-wx-ma/App.vue?vue&type=style&index=0&lang=css& ***!
  \*******************************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/mini-css-extract-plugin/dist/loader.js??ref--6-oneOf-1-0!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/css-loader/dist/cjs.js??ref--6-oneOf-1-1!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--6-oneOf-1-2!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/postcss-loader/src??ref--6-oneOf-1-3!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./App.vue?vue&type=style&index=0&lang=css& */ 33);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_App_vue_vue_type_style_index_0_lang_css___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 33:
/*!***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/mini-css-extract-plugin/dist/loader.js??ref--6-oneOf-1-0!./node_modules/css-loader/dist/cjs.js??ref--6-oneOf-1-1!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--6-oneOf-1-2!./node_modules/postcss-loader/src??ref--6-oneOf-1-3!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!D:/work_boss/dingyangMall/JooLun-wx/dingyangmall-wx-ma/App.vue?vue&type=style&index=0&lang=css& ***!
  \***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin
    if(false) { var cssReload; }
  

/***/ })

},[[0,"common/runtime","common/vendor"]]]);
//# sourceMappingURL=../../.sourcemap/mp-weixin/common/main.js.map