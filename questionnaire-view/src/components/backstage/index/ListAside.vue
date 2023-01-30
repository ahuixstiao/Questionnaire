<template>
  <el-container class="head-aside-container">
    <el-aside width="200px">
      <el-menu router mode="vertical" :default-active="data.bindingPath" :default-openeds="['1', '1']">
        <el-sub-menu index="1">
          <template #title>
            <el-icon><user/></el-icon>用户管理
          </template>
          <el-menu-item index="/admin/userList">用户列表</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon><icon-menu/></el-icon>问卷管理
          </template>
          <el-menu-item index="/admin/questionnaireList">问卷列表</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>
            <el-icon><setting/></el-icon>系统管理
          </template>
          <el-menu-item index="/admin/quartz">定时任务</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <!-- 列表主体 -->
    <el-main> <!-- enter进入 leave离开 -->
      <router-view v-slot="{ Component }">
        <transition enter-active-class="animate__animated animate__bounceInDown">
          <component :is="Component" />
        </transition>
      </router-view>
    </el-main>
  </el-container>
</template>

<script setup>
import {Menu as IconMenu, Setting, User} from '@element-plus/icons-vue'
import {onMounted, reactive, watch} from "vue";
import {useRoute} from "vue-router";

//路由
const route = useRoute();

const data =reactive({
  bindingPath: ""
})

// 组件完成初始渲染并创建 DOM 节点后运行代码
onMounted(()=>{
  data.bindingPath = route.path
})

// 监听器 当页面变化时将路径赋予bindingPath
watch(route, ()=>{
  data.bindingPath = route.path
})

</script>

<style lang="scss" scoped>

.head-aside-container {
  height: calc(100% - 5px);
  width: 100%;
  padding-top: 62px;
}

.el-menu {
  height: 100%;
}

.el-main {
  overflow: hidden;
}


</style>