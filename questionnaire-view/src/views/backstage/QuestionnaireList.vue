<template>
  <div>
    <BaseBreadcrumb :routeBreadcrumb="data.links"/>
    <h1>问卷列表</h1>
  </div>
</template>

<script setup>
import BaseBreadcrumb from "@/components/base/BaseBreadcrumb.vue";
import {reactive} from "vue";

const data = reactive({
  links: [
    {name: "问卷管理", path: ""},
    {name: "问卷列表", path: ""},
  ]
})

</script>

<style lang="scss" scoped>

</style>