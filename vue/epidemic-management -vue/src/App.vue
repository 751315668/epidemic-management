<template>
  <div id="app">
    <!-- backtop按钮 -->
    <el-backtop :visibility-height="20"></el-backtop>
    <router-view />
  </div>
</template>

<script>
  export default {
    name: 'App',
  }
</script>