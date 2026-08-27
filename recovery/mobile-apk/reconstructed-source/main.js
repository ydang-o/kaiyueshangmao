import 'uni-pages';
// @ts-ignore
wx.__webpack_require_UNI_MP_PLUGIN__ = __webpack_require__;import Vue from 'vue'
import App from './App.vue'
import { fullImageUrl } from './utils/imageUrl'

console.log('[如囍优选] main.js 已加载')
Vue.config.productionTip = false
Vue.prototype.$imgUrl = fullImageUrl
App.mpType = 'app'

const app = new Vue({
  ...App
})
app.$mount()