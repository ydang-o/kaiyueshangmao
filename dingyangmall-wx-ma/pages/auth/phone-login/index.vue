<template>
  <view class="login-page padding-xl">
    <view class="login-card bg-white radius shadow-lg padding-xl">
      <view class="text-center margin-bottom-xl"><view class="text-xxl text-bold">{{ isRegister ? '注册账号' : '手机号登录' }}</view><view class="text-gray margin-top-sm">如囍优选</view></view>
      <view class="cu-form-group margin-top"><view class="title">手机号</view><input type="number" maxlength="11" v-model="phone" placeholder="请输入手机号" /></view>
      <view class="cu-form-group"><view class="title">验证码</view><input type="number" maxlength="6" v-model="code" placeholder="请输入验证码" /><button class="cu-btn line-green" :disabled="smsDisabled" @tap="sendSms">{{ smsText }}</button></view>
      <view v-if="isRegister" class="cu-form-group"><view class="title">邀请码</view><input v-model="inviteCode" placeholder="选填" /></view>
      <view v-if="isRegister" class="padding-top text-sm text-gray"><checkbox :checked="agreed" @tap="toggleAgree" /> 我已阅读并同意 <text class="text-blue" @tap="showUserAgreement">用户协议</text> 和 <text class="text-blue" @tap="showPrivacyPolicy">隐私政策</text></view>
      <button class="cu-btn bg-red block margin-top-xl" :loading="loading" :disabled="isRegister && !agreed" @tap="isRegister ? doRegister() : doLogin">{{ isRegister ? '注册' : '登录' }}</button>
      <view class="text-center margin-top text-blue" @tap="toggleMode">{{ isRegister ? '已有账号，去登录' : '没有账号，去注册' }}</view>
    </view>
  </view>
</template>
<script>
export default {
  name: 'PhoneLoginPage',
  data() { return { phone:'', code:'', loading:false, smsText:'获取验证码', smsDisabled:false, timer:null, _destroyed:false, isRegister:false, inviteCode:'', agreed:false } },
  onUnload() { this._destroyed=true; this.clearTimer() }, beforeDestroy() { this._destroyed=true; this.clearTimer() },
  methods: {
    getApi() { const app=typeof getApp==='function'?getApp():null; return (app&&app.api)||{} },
    clearTimer() { if(this.timer){clearInterval(this.timer);this.timer=null} },
    sendSms() { if(this.smsDisabled)return; if(!this.phone)return uni.showToast({title:'请输入手机号',icon:'none'}); if(!/^1[3-9]\d{9}$/.test(this.phone))return uni.showToast({title:'手机号格式不正确',icon:'none'}); const api=this.getApi(); if(!api||typeof api.sendSmsCode!=='function')return uni.showToast({title:'接口未就绪',icon:'none'}); uni.showLoading({title:'发送中...'}); api.sendSmsCode(this.phone).then(res=>{uni.hideLoading();if(!res||(res.code!==0&&res.code!==200))return uni.showToast({title:(res&&res.msg)||'发送失败',icon:'none'});uni.showToast({title:'验证码已发送',icon:'none'});this.startTimer()}).catch(()=>{uni.hideLoading();uni.showToast({title:'发送失败，请稍后重试',icon:'none'})}) },
    startTimer(){let n=60;this.smsDisabled=true;this.smsText=n+'s';this.clearTimer();this.timer=setInterval(()=>{if(this._destroyed)return this.clearTimer();n--;if(n<=0){this.clearTimer();this.smsDisabled=false;this.smsText='获取验证码'}else this.smsText=n+'s'},1000)},
    toggleMode(){this.isRegister=!this.isRegister;this.phone='';this.code='';this.inviteCode='';this.agreed=false}, toggleAgree(){this.agreed=!this.agreed},
    showUserAgreement(){this.showPolicy('4','用户协议','用户注册即视为同意本平台服务规则。')}, showPrivacyPolicy(){this.showPolicy('3','隐私政策','如囍优选重视用户隐私保护。')},
    showPolicy(type,title,fallback){const app=getApp();const base=app&&app.globalData&&app.globalData.config&&app.globalData.config.basePath||'https://kaiyueshangmao.xyz';uni.request({url:base+'/api/public/ma/notice/type/'+type,method:'GET',success:r=>{const d=r.data||{};const content=d.data&&d.data.noticeContent?d.data.noticeContent.replace(/<[^>]+>/g,'').replace(/&nbsp;/g,' ').trim():fallback;uni.showModal({title:d.data&&d.data.noticeTitle||title,content,showCancel:false})},fail:()=>uni.showModal({title,content:fallback,showCancel:false})})},
    doLogin(){if(!this.phone)return uni.showToast({title:'请输入手机号',icon:'none'});if(!/^1[3-9]\d{9}$/.test(this.phone))return uni.showToast({title:'手机号格式不正确',icon:'none'});if(!this.code)return uni.showToast({title:'请输入验证码',icon:'none'});const api=this.getApi();if(!api||typeof api.loginBySms!=='function')return uni.showToast({title:'接口未就绪',icon:'none'});this.loading=true;api.loginBySms({phone:this.phone,code:this.code}).then(res=>{this.loading=false;const d=res&&res.data||res||{};if(!res||(res.code!==0&&res.code!==200)||!d.token)return uni.showToast({title:(res&&res.msg)||'登录失败',icon:'none'});const app=getApp();app.globalData.wxToken=d.token;app.globalData.thirdSession=d.token;app.globalData.wxUser=d;uni.setStorageSync('wx_token',d.token);uni.setStorageSync('wx_third_session',d.token);uni.showToast({title:'登录成功',icon:'success'});setTimeout(()=>uni.switchTab({url:'/pages/home/index'}),1000)}).catch(()=>{this.loading=false;uni.showToast({title:'登录失败，请稍后重试',icon:'none'})})},
    doRegister(){if(!this.phone||!/^1[3-9]\d{9}$/.test(this.phone))return uni.showToast({title:'请输入正确的手机号',icon:'none'});if(!this.code)return uni.showToast({title:'请输入验证码',icon:'none'});if(!this.agreed)return uni.showToast({title:'请先同意用户协议',icon:'none'});const api=this.getApi();if(!api||typeof api.registerBySms!=='function')return uni.showToast({title:'接口未就绪',icon:'none'});this.loading=true;api.registerBySms({phone:this.phone,code:this.code,inviteCode:this.inviteCode}).then(res=>{this.loading=false;if(!res||(res.code!==0&&res.code!==200))return uni.showToast({title:(res&&res.msg)||'注册失败',icon:'none'});uni.showToast({title:'注册成功，请登录',icon:'success'});setTimeout(()=>{this.isRegister=false;this.code=''},1500)}).catch(()=>{this.loading=false;uni.showToast({title:'注册失败，请稍后重试',icon:'none'})})}
  }
}
</script>
<style scoped>
.login-page{min-height:100vh;background:#f3f4f7;padding-top:120rpx}.login-card{min-height:720rpx}.block{display:block;width:100%}
</style>
