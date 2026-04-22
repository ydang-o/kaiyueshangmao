(global["webpackJsonp"] = global["webpackJsonp"] || []).push([["pages/home/index"],{

/***/ 38:
/*!**************************************************************************************************************!*\
  !*** D:/work_boss/kaiyueshangmao/dingyangmall-wx/dingyangmall-wx-ma/main.js?{"page":"pages%2Fhome%2Findex"} ***!
  \**************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(wx, createPage) {

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ 4);
__webpack_require__(/*! uni-pages */ 26);
var _vue = _interopRequireDefault(__webpack_require__(/*! vue */ 25));
var _index = _interopRequireDefault(__webpack_require__(/*! ./pages/home/index.vue */ 39));
// @ts-ignore
wx.__webpack_require_UNI_MP_PLUGIN__ = __webpack_require__;
createPage(_index.default);
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/wx.js */ 1)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["createPage"]))

/***/ }),

/***/ 39:
/*!*******************************************************************************************!*\
  !*** D:/work_boss/kaiyueshangmao/dingyangmall-wx/dingyangmall-wx-ma/pages/home/index.vue ***!
  \*******************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./index.vue?vue&type=template&id=71e217db&scoped=true& */ 40);
/* harmony import */ var _index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./index.vue?vue&type=script&lang=js& */ 42);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_1__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_1__[key]; }) }(__WEBPACK_IMPORT_KEY__));
/* harmony import */ var _index_vue_vue_type_style_index_0_id_71e217db_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./index.vue?vue&type=style&index=0&id=71e217db&scoped=true&lang=css& */ 44);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/runtime/componentNormalizer.js */ 36);

var renderjs





/* normalize component */

var component = Object(_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_runtime_componentNormalizer_js__WEBPACK_IMPORTED_MODULE_3__["default"])(
  _index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_1__["default"],
  _index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__["render"],
  _index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__["staticRenderFns"],
  false,
  null,
  "71e217db",
  null,
  false,
  _index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__["components"],
  renderjs
)

component.options.__file = "pages/home/index.vue"
/* harmony default export */ __webpack_exports__["default"] = (component.exports);

/***/ }),

/***/ 40:
/*!**************************************************************************************************************************************!*\
  !*** D:/work_boss/kaiyueshangmao/dingyangmall-wx/dingyangmall-wx-ma/pages/home/index.vue?vue&type=template&id=71e217db&scoped=true& ***!
  \**************************************************************************************************************************************/
/*! exports provided: render, staticRenderFns, recyclableRender, components */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--17-0!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/template.js!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-uni-app-loader/page-meta.js!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./index.vue?vue&type=template&id=71e217db&scoped=true& */ 41);
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "render", function() { return _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__["render"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "staticRenderFns", function() { return _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__["staticRenderFns"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "recyclableRender", function() { return _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__["recyclableRender"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "components", function() { return _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_templateLoader_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_17_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_template_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_uni_app_loader_page_meta_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_template_id_71e217db_scoped_true___WEBPACK_IMPORTED_MODULE_0__["components"]; });



/***/ }),

/***/ 41:
/*!**************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--17-0!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/template.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-uni-app-loader/page-meta.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!D:/work_boss/kaiyueshangmao/dingyangmall-wx/dingyangmall-wx-ma/pages/home/index.vue?vue&type=template&id=71e217db&scoped=true& ***!
  \**************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! exports provided: render, staticRenderFns, recyclableRender, components */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "render", function() { return render; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "staticRenderFns", function() { return staticRenderFns; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "recyclableRender", function() { return recyclableRender; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "components", function() { return components; });
var components
var render = function () {
  var _vm = this
  var _h = _vm.$createElement
  var _c = _vm._self._c || _h
  var g0 = _vm.swiperData.length
  var l0 = g0
    ? _vm.__map(_vm.swiperData, function (item, i) {
        var $orig = _vm.__get_orig(item)
        var m0 = _vm.$imgUrl(item.img)
        return {
          $orig: $orig,
          m0: m0,
        }
      })
    : null
  var g1 = _vm.noticeList.length
  _vm.$mp.data = Object.assign(
    {},
    {
      $root: {
        g0: g0,
        l0: l0,
        g1: g1,
      },
    }
  )
}
var recyclableRender = false
var staticRenderFns = []
render._withStripped = true



/***/ }),

/***/ 42:
/*!********************************************************************************************************************!*\
  !*** D:/work_boss/kaiyueshangmao/dingyangmall-wx/dingyangmall-wx-ma/pages/home/index.vue?vue&type=script&lang=js& ***!
  \********************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/babel-loader/lib!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./index.vue?vue&type=script&lang=js& */ 43);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_babel_loader_lib_index_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_13_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_script_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_script_lang_js___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 43:
/*!***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/babel-loader/lib!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--13-1!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/script.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!D:/work_boss/kaiyueshangmao/dingyangmall-wx/dingyangmall-wx-ma/pages/home/index.vue?vue&type=script&lang=js& ***!
  \***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(uni, wx) {

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ 4);
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _slicedToArray2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/slicedToArray */ 5));
var _toConsumableArray2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/toConsumableArray */ 18));
var _defineProperty2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/defineProperty */ 11));
var _typeof2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/typeof */ 13));
var _api = _interopRequireDefault(__webpack_require__(/*! @/utils/api */ 30));
var _util = _interopRequireDefault(__webpack_require__(/*! @/utils/util */ 32));
function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); enumerableOnly && (symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; })), keys.push.apply(keys, symbols); } return keys; }
function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = null != arguments[i] ? arguments[i] : {}; i % 2 ? ownKeys(Object(source), !0).forEach(function (key) { (0, _defineProperty2.default)(target, key, source[key]); }) : Object.getOwnPropertyDescriptors ? Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)) : ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } return target; }
var GoodsCardIndex = function GoodsCardIndex() {
  __webpack_require__.e(/*! require.ensure | components/goods-card-index/index */ "components/goods-card-index/index").then((function () {
    return resolve(__webpack_require__(/*! @/components/goods-card-index/index.vue */ 199));
  }).bind(null, __webpack_require__)).catch(__webpack_require__.oe);
};
var LoginBanner = function LoginBanner() {
  __webpack_require__.e(/*! require.ensure | components/login-banner/index */ "components/login-banner/index").then((function () {
    return resolve(__webpack_require__(/*! @/components/login-banner/index.vue */ 206));
  }).bind(null, __webpack_require__)).catch(__webpack_require__.oe);
};
var _default = {
  name: 'HomePage',
  components: {
    GoodsCardIndex: GoodsCardIndex,
    LoginBanner: LoginBanner
  },
  data: function data() {
    var app = getApp();
    var wxUser = app.globalData.wxUser || {};
    return {
      config: app.globalData.config || {},
      page: {
        searchCount: false,
        current: 1,
        size: 10
      },
      loadmore: true,
      goodsList: [],
      swiperData: [],
      navList: [],
      noticeList: [],
      promoList: [],
      isLoggedIn: !!app.globalData.thirdSession,
      hasPhone: !!(wxUser.phoneNumber || wxUser.phone),
      profileSkipped: !!app.globalData.profileSkipped,
      displayNickname: (wxUser.nickName || wxUser.nickname || '').trim(),
      displayAvatar: (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim(),
      displayPhone: (wxUser.phoneNumber || wxUser.phone || '').trim(),
      loginLoading: false
    };
  },
  onLoad: function onLoad() {
    var _this = this;
    console.log('Hello World');
    var app = getApp();
    var loadDataScheduled = false;
    var tryLoad = function tryLoad() {
      if (loadDataScheduled) return;
      loadDataScheduled = true;
      if (typeof _this.loadData === 'function') _this.loadData();
    };
    // 商品分页走 /api/ma，未登录也可请求，首页始终拉取商品列表
    var runLoad = function runLoad() {
      tryLoad();
    };
    if (!app.initPage) {
      console.warn('[Home] initPage 未挂载，延迟 500ms 再拉取');
      setTimeout(runLoad, 500);
      setTimeout(function () {
        if (!loadDataScheduled) runLoad();
      }, 2500);
    } else {
      app.initPage().then(runLoad).catch(function () {
        setTimeout(function () {
          app.initPage().then(runLoad).catch(runLoad);
        }, 300);
      });
    }
  },
  onShow: function onShow() {
    var app = getApp();
    this.isLoggedIn = !!app.globalData.thirdSession;
    this.profileSkipped = !!app.globalData.profileSkipped;
    var wxUser = app.globalData.wxUser || {};
    this.hasPhone = !!(wxUser.phoneNumber || wxUser.phone);
    this.displayNickname = (wxUser.nickName || wxUser.nickname || '').trim();
    this.displayAvatar = (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim();
    this.displayPhone = (wxUser.phoneNumber || wxUser.phone || '').trim();
    var pages = getCurrentPages();
    var page = pages[pages.length - 1];
    if (page && typeof page.getTabBar === 'function') {
      var tabBar = page.getTabBar();
      if (tabBar && tabBar.setData) tabBar.setData({
        selected: 0
      });
    }
    _util.default.updateCartBadge(app.globalData.shoppingCartCount || 0);
  },
  onPullDownRefresh: function onPullDownRefresh() {
    uni.showNavigationBarLoading();
    this.refresh();
    uni.hideNavigationBarLoading();
    uni.stopPullDownRefresh();
  },
  onReachBottom: function onReachBottom() {
    if (this.loadmore) {
      this.page.current++;
      this.goodsPage();
    }
  },
  onShareAppMessage: function onShareAppMessage() {
    return {
      title: '如囍优选',
      path: 'pages/home/index'
    };
  },
  methods: {
    change: function change() {},
    getApi: function getApi() {
      try {
        var app = typeof getApp === 'function' ? getApp() : null;
        var fromApp = app && app.api && (0, _typeof2.default)(app.api) === 'object';
        return (fromApp ? app.api : null) || (_api.default && (0, _typeof2.default)(_api.default) === 'object' ? _api.default : null) || {};
      } catch (e) {
        return _api.default || {};
      }
    },
    onProfileSkipOrConfirm: function onProfileSkipOrConfirm() {
      var app = getApp();
      if (app && app.globalData) app.globalData.profileSkipped = true;
      this.profileSkipped = true;
    },
    onLoginSuccess: function onLoginSuccess() {
      var app = getApp();
      this.isLoggedIn = !!(app.globalData.wxToken || app.globalData.thirdSession);
      var wxUser = app.globalData.wxUser || {};
      this.hasPhone = !!(wxUser.phoneNumber || wxUser.phone);
      this.displayNickname = (wxUser.nickName || wxUser.nickname || '').trim();
      this.displayAvatar = (wxUser.headimgUrl || wxUser.avatarUrl || wxUser.avatar || '').trim();
      this.displayPhone = (wxUser.phoneNumber || wxUser.phone || '').trim();
      if (this.isLoggedIn && typeof this.loadData === 'function') this.loadData();
    },
    wxLogin: function wxLogin() {
      var _this2 = this;
      uni.showModal({
        title: '微信授权登录',
        content: '为了更好的服务体验，将使用你的微信账号登录如囍优选。是否授权？',
        cancelText: '取消',
        confirmText: '授权登录',
        confirmColor: '#ff0036',
        success: function success(res) {
          if (!res.confirm) return;
          _this2.loginLoading = true;
          getApp().doLogin().then(function (result) {
            _this2.loginLoading = false;
            _this2.isLoggedIn = !!getApp().globalData.thirdSession;
            if (result === 'fail') {
              uni.showToast({
                title: '登录失败，请重试',
                icon: 'none'
              });
              return;
            }
            uni.showToast({
              title: '登录成功',
              icon: 'success'
            });
            _this2.fetchUserProfileAuth();
          });
        }
      });
    },
    fetchUserProfileAuth: function fetchUserProfileAuth() {
      var _this3 = this;
      if (typeof uni.getUserProfile !== 'function') return;
      uni.getUserProfile({
        desc: '用于完善会员资料、展示昵称与头像',
        success: function success(detail) {
          var api = _this3.getApi();
          if (!api || typeof api.wxUserSave !== 'function') return;
          api.wxUserSave(detail).then(function (res) {
            var data = res.data || res;
            var user = _objectSpread({
              headimgUrl: data.headimgUrl || data.avatar,
              nickName: data.nickName || data.nickname,
              userId: data.userId || data.id || data.openid
            }, data);
            getApp().globalData.wxUser = Object.assign(getApp().globalData.wxUser || {}, user);
          }).catch(function () {});
        },
        fail: function fail() {}
      });
    },
    goGoodsDetail: function goGoodsDetail(id) {
      if (id) uni.navigateTo({
        url: '/pages/goods/goods-detail/index?id=' + id
      });
    },
    loadData: function loadData() {
      var _this4 = this;
      var app = typeof getApp === 'function' ? getApp() : {};
      // 发请求前再次从 storage 同步 token，避免 GET 请求未带 X-Wx-Token
      if (app && app.globalData && !app.globalData.wxToken && !app.globalData.thirdSession) {
        try {
          var u = typeof uni !== 'undefined' ? uni : wx;
          if (u && u.getStorageSync) {
            var saved = u.getStorageSync('wx_token') || u.getStorageSync('wx_third_session');
            if (saved && typeof saved === 'string' && saved.length > 0) {
              app.globalData.wxToken = saved;
              app.globalData.thirdSession = saved;
              console.log('[Home] loadData 前已从 storage 恢复 token');
            }
          }
        } catch (e) {}
      }
      var api = app && app.api || app.globalData && app.globalData.__api || _api.default;
      var retryCount = this._loadDataRetryCount || 0;
      if (!api || typeof api.getHomePage !== 'function' && typeof api.goodsPage !== 'function') {
        if (retryCount >= 10) {
          console.error('[Home] api 始终未就绪，请确认用 HBuilderX「运行到微信开发者工具」并打开 unpackage/dist/dev/mp-weixin');
          return;
        }
        this._loadDataRetryCount = retryCount + 1;
        if (retryCount === 0) console.warn('[Home] api 未就绪，将重试最多 10 次');
        setTimeout(function () {
          return _this4.loadData();
        }, 200);
        return;
      }
      this._loadDataRetryCount = 0;
      console.log('[Home] loadData 开始请求');
      // 优先使用首页聚合接口（与各大购物平台一致，一次请求拿全首页数据）
      if (typeof api.getHomePage === 'function') {
        api.getHomePage().then(function (res) {
          var data = res && res.data || res || {};
          var bannerList = data.bannerList || [];
          var categoryTree = data.categoryTree || [];
          var noticeList = data.noticeList || [];
          var goodsList = data.goodsList || [];
          var promoList = data.promoList || [];
          _this4.buildSwiper(bannerList);
          _this4.buildNavList(categoryTree);
          _this4.noticeList = Array.isArray(noticeList) ? noticeList : [];
          _this4.goodsList = [].concat((0, _toConsumableArray2.default)(_this4.goodsList), (0, _toConsumableArray2.default)(Array.isArray(goodsList) ? goodsList : []));
          _this4.promoList = Array.isArray(promoList) ? promoList : [];
          _this4.loadmore = _this4.goodsList.length >= (_this4.page.size || 10);
          console.log('[Home] 首页聚合成功', '轮播:', bannerList.length, '分类:', categoryTree.length, '商品:', _this4.goodsList.length, '推荐:', _this4.promoList.length);
        }).catch(function (err) {
          console.warn('[Home] 首页聚合接口失败，回退到分接口', err);
          _this4._loadDataFallback(api);
        });
        return;
      }
      this._loadDataFallback(api);
    },
    _loadDataFallback: function _loadDataFallback(api) {
      var _this5 = this;
      Promise.all([api.goodsPage(this.page), api.goodsPage({
        searchCount: false,
        current: 1,
        size: 6
      })]).then(function (_ref) {
        var _ref2 = (0, _slicedToArray2.default)(_ref, 2),
          resPage = _ref2[0],
          resBanner = _ref2[1];
        var pageRecords = _this5._getRecords(resPage);
        if (!pageRecords.length && resPage) {
          try {
            console.warn('[Home] 商品分页返回空，请检查后端数据或响应格式。原始响应:', JSON.stringify(resPage).slice(0, 500));
          } catch (e) {}
        }
        _this5.goodsList = [].concat((0, _toConsumableArray2.default)(_this5.goodsList), (0, _toConsumableArray2.default)(pageRecords));
        if (!pageRecords.length || pageRecords.length < _this5.page.size) _this5.loadmore = false;
        _this5.buildSwiper(_this5._getRecords(resBanner));
        _this5.loadNotice();
        console.log('[Home] loadData 成功', '列表:', _this5.goodsList.length, '首页条数:', pageRecords.length);
      }).catch(function (err) {
        console.error('[Home] loadData 请求失败', err);
        _this5.loadmore = false;
      });
    },
    loadNotice: function loadNotice() {
      var _this6 = this;
      var app = typeof getApp === 'function' ? getApp() : null;
      var api = app && app.api || app && app.globalData && app.globalData.__api || _api.default;
      if (!api || typeof api.getNoticeList !== 'function') return;
      api.getNoticeList().then(function (res) {
        var raw = res && res.data || [];
        if (Array.isArray(raw)) _this6.noticeList = raw;else if (raw && raw.content && Array.isArray(raw.content)) _this6.noticeList = raw.content;else if (raw && raw.records && Array.isArray(raw.records)) _this6.noticeList = raw.records.map(function (r) {
          return r.content || r.title || r.name;
        });
      }).catch(function () {});
    },
    buildNavList: function buildNavList(categoryTree) {
      if (!categoryTree || !categoryTree.length) return;
      var list = [];
      for (var i = 0; i < categoryTree.length && list.length < 8; i++) {
        var cat = categoryTree[i];
        if (cat.children && cat.children.length) {
          cat.children.slice(0, 8 - list.length).forEach(function (c) {
            list.push({
              url: '/pages/goods/goods-list/index?categorySecond=' + (c.id || c.categoryId) + '&title=' + encodeURIComponent(c.name || ''),
              img: c.picUrl || '',
              text: c.name || ''
            });
          });
        } else {
          list.push({
            url: '/pages/goods/goods-list/index?categorySecond=' + (cat.id || cat.categoryId) + '&title=' + encodeURIComponent(cat.name || ''),
            img: cat.picUrl || '',
            text: cat.name || ''
          });
        }
      }
      this.navList = list;
    },
    buildSwiper: function buildSwiper(records) {
      this.swiperData = (records || []).map(function (item) {
        return {
          id: item.id,
          img: item.picUrls && item.picUrls[0] ? item.picUrls[0] : item.picUrl || ''
        };
      }).filter(function (item) {
        return item.img;
      });
    },
    goodsPage: function goodsPage() {
      var _this7 = this;
      var app = getApp();
      var api = app && app.api || app && app.globalData && app.globalData.__api || _api.default;
      if (!api || typeof api.goodsPage !== 'function') return;
      api.goodsPage(this.page).then(function (res) {
        var list = _this7._getRecords(res);
        _this7.goodsList = [].concat((0, _toConsumableArray2.default)(_this7.goodsList), (0, _toConsumableArray2.default)(list));
        if (list.length < _this7.page.size) _this7.loadmore = false;
      }).catch(function () {});
    },
    _getRecords: function _getRecords(r) {
      if (!r) return [];
      if (Array.isArray(r)) return r;
      if (r.records && Array.isArray(r.records)) return r.records;
      if (r.data && Array.isArray(r.data)) return r.data;
      if (r.data && r.data.records && Array.isArray(r.data.records)) return r.data.records;
      if (r.data && r.data.list && Array.isArray(r.data.list)) return r.data.list;
      if (r.data && r.data.rows && Array.isArray(r.data.rows)) return r.data.rows;
      if (r.data && r.data.content && Array.isArray(r.data.content)) return r.data.content;
      if (r.data && (0, _typeof2.default)(r.data) === 'object' && r.data.data && Array.isArray(r.data.data)) return r.data.data;
      if (r.list && Array.isArray(r.list)) return r.list;
      if (r.rows && Array.isArray(r.rows)) return r.rows;
      if (r.content && Array.isArray(r.content)) return r.content;
      if (r.result && Array.isArray(r.result)) return r.result;
      return [];
    },
    signIn: function signIn() {
      _util.default.requireLogin('请先登录后再签到').then(function (ok) {
        if (!ok) return;
        var app = getApp();
        var api = app && app.api || app && app.globalData && app.globalData.__api || _api.default;
        if (!api || typeof api.memberSignIn !== 'function') {
          uni.showToast({
            title: '功能暂不可用',
            icon: 'none'
          });
          return;
        }
        api.memberSignIn().then(function (res) {
          uni.showToast({
            title: res && res.msg || '签到成功',
            icon: 'success'
          });
        }).catch(function () {
          uni.showToast({
            title: '签到失败或今日已签到',
            icon: 'none'
          });
        });
      });
    },
    goCoupons: function goCoupons() {
      _util.default.requireLogin('请先登录后查看优惠券').then(function (ok) {
        if (!ok) return;
        uni.navigateTo({
          url: '/pages/coupon/my-coupons/index'
        });
      });
    },
    goLottery: function goLottery() {
      _util.default.requireLogin('请先登录后参与抽奖').then(function (ok) {
        if (!ok) return;
        uni.navigateTo({
          url: '/pages/lottery/index/index'
        });
      });
    },
    goPacket: function goPacket() {
      _util.default.requireLogin('请先登录后领取红包').then(function (ok) {
        if (!ok) return;
        uni.navigateTo({
          url: '/pages/integral/packet/index'
        });
      });
    },
    goIntegralShop: function goIntegralShop() {
      _util.default.requireLogin('请先登录后查看积分商城').then(function (ok) {
        if (!ok) return;
        uni.navigateTo({
          url: '/pages/integral/shop/index'
        });
      });
    },
    refresh: function refresh() {
      this.loadmore = true;
      this.page.current = 1;
      this.goodsList = [];
      this.swiperData = [];
      this.navList = [];
      this.promoList = [];
      this.loadData();
    }
  }
};
exports.default = _default;
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/index.js */ 2)["default"], __webpack_require__(/*! ./node_modules/@dcloudio/uni-mp-weixin/dist/wx.js */ 1)["default"]))

/***/ }),

/***/ 44:
/*!****************************************************************************************************************************************************!*\
  !*** D:/work_boss/kaiyueshangmao/dingyangmall-wx/dingyangmall-wx-ma/pages/home/index.vue?vue&type=style&index=0&id=71e217db&scoped=true&lang=css& ***!
  \****************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_style_index_0_id_71e217db_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! -!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/mini-css-extract-plugin/dist/loader.js??ref--6-oneOf-1-0!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/css-loader/dist/cjs.js??ref--6-oneOf-1-1!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--6-oneOf-1-2!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/postcss-loader/src??ref--6-oneOf-1-3!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!../../../../../../software/hbuilderx/HBuilderX.4.87.2025121004/HBuilderX/plugins/uniapp-cli/node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!./index.vue?vue&type=style&index=0&id=71e217db&scoped=true&lang=css& */ 45);
/* harmony import */ var _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_style_index_0_id_71e217db_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_style_index_0_id_71e217db_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0__);
/* harmony reexport (unknown) */ for(var __WEBPACK_IMPORT_KEY__ in _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_style_index_0_id_71e217db_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0__) if(["default"].indexOf(__WEBPACK_IMPORT_KEY__) < 0) (function(key) { __webpack_require__.d(__webpack_exports__, key, function() { return _software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_style_index_0_id_71e217db_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0__[key]; }) }(__WEBPACK_IMPORT_KEY__));
 /* harmony default export */ __webpack_exports__["default"] = (_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_mini_css_extract_plugin_dist_loader_js_ref_6_oneOf_1_0_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_loaders_stylePostLoader_js_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_webpack_preprocess_loader_index_js_ref_6_oneOf_1_2_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_vue_cli_plugin_uni_packages_vue_loader_lib_index_js_vue_loader_options_software_hbuilderx_HBuilderX_4_87_2025121004_HBuilderX_plugins_uniapp_cli_node_modules_dcloudio_webpack_uni_mp_loader_lib_style_js_index_vue_vue_type_style_index_0_id_71e217db_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0___default.a); 

/***/ }),

/***/ 45:
/*!********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** ./node_modules/mini-css-extract-plugin/dist/loader.js??ref--6-oneOf-1-0!./node_modules/css-loader/dist/cjs.js??ref--6-oneOf-1-1!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib/loaders/stylePostLoader.js!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/webpack-preprocess-loader??ref--6-oneOf-1-2!./node_modules/postcss-loader/src??ref--6-oneOf-1-3!./node_modules/@dcloudio/vue-cli-plugin-uni/packages/vue-loader/lib??vue-loader-options!./node_modules/@dcloudio/webpack-uni-mp-loader/lib/style.js!D:/work_boss/kaiyueshangmao/dingyangmall-wx/dingyangmall-wx-ma/pages/home/index.vue?vue&type=style&index=0&id=71e217db&scoped=true&lang=css& ***!
  \********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

// extracted by mini-css-extract-plugin
    if(false) { var cssReload; }
  

/***/ })

},[[38,"common/runtime","common/vendor"]]]);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/home/index.js.map