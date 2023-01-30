<template>
  <div class="head-breadcrumb">
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
      <template v-for="[idx, link] in props" :key="idx">
        <el-breadcrumb-item>{{ idx.name }}</el-breadcrumb-item>
        <el-breadcrumb-item>{{ link.name }}</el-breadcrumb-item>
      </template>
    </el-breadcrumb>
  </div>
</template>

<script setup>
import {ArrowRight} from '@element-plus/icons-vue'
const props =  defineProps(["routeBreadcrumb"])
</script>

<style lang="scss" scoped>
.head-breadcrumb {
  padding-bottom: 20px
}

</style>