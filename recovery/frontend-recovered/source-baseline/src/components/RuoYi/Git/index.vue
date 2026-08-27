<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script setup>
const url = ref("https://gitee.com/dingyangmall/dingyangmall-wx");

function goto() {
  window.open(url.value);
}
</script>

