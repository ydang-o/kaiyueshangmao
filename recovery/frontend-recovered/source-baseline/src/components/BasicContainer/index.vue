<template>
  <div class="basic-container">
    <slot />
  </div>
</template>

<script>
export default {
  name: 'BasicContainer'
}
</script>

<style scoped>
.basic-container {
  padding: 16px;
  background: #fff;
  min-height: 100%;
  box-sizing: border-box;
}
</style>
