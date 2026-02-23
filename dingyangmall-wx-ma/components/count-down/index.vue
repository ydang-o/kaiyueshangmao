<!--
  Copyright (C) 2018-2019 www.dingyangmall.com
-->
<template>
  <text><text v-if="day">{{ day }}天</text><text v-if="hour">{{ hour }}小时</text><text v-if="minute">{{ minute }}分</text><text v-if="second">{{ second }}秒</text></text>
</template>

<script>
export default {
  name: 'CountDown',
  props: { outTime: { type: Number, default: 0 } },
  data() {
    return { day: null, hour: null, minute: null, second: null, timer: null, left: 0 }
  },
  watch: {
    outTime: { handler(v) { this.left = v; this.tick() }, immediate: true }
  },
  beforeDestroy() {
    if (this.timer) clearInterval(this.timer)
  },
  methods: {
    timeFormat(param) {
      return param < 10 ? '0' + param : param
    },
    tick() {
      if (this.timer) clearInterval(this.timer)
      this.timer = setInterval(() => {
        this.left -= 1000
        if (this.left <= 0) {
          clearInterval(this.timer)
          this.$emit('countDownDone')
          return
        }
        const t = this.left
        this.day = Math.floor(t / 1000 / 60 / 60 / 24)
        this.hour = Math.floor(t / 1000 / 60 / 60 % 24)
        this.minute = Math.floor(t / 1000 / 60 % 60)
        this.second = Math.floor(t / 1000 % 60)
        this.day = this.day > 0 ? this.timeFormat(this.day) : null
        this.hour = this.hour > 0 ? this.timeFormat(this.hour) : null
        this.minute = this.minute > 0 ? this.timeFormat(this.minute) : null
        this.second = this.second > 0 ? this.timeFormat(this.second) : null
      }, 1000)
    }
  }
}
</script>
