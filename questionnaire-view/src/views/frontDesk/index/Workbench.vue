<template>
  <Header/>
  <el-container class="workbench-container">
    <el-aside width="collapse">
      <el-menu router :default-active="data.bindingPath" :collapse="data.isCollapse" class="workbench-container-aside-menu">
        <el-menu-item index="/workbench/">
          <el-icon><RefreshLeft/></el-icon>
          <template #title>最近</template>
        </el-menu-item>
        <el-menu-item index="/workbench/mine">
          <el-icon><User/></el-icon>
          <template #title>个人问卷</template>
        </el-menu-item>
        <el-menu-item index="/workbench/recycleBin">
          <el-icon><Delete/></el-icon>
          <template #title>回收站</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-main>
      <div class="collapse-box">
        <!-- 点击展开收起导航和切换对应图标 -->
        <el-icon @click="isC" v-show="data.block"><Fold/></el-icon>
        <el-icon @click="isC" v-show="data.toggle"><Expand/></el-icon>
      </div>
      <router-view v-slot="{ Component }">
        <transition enter-active-class="animate__animated animate__backInRight">
          <component :is="Component" />
        </transition>
      </router-view>
    </el-main>

  </el-container>
</template>

<script setup>
import Header from '@/components/base/BaseHeader.vue'
import {onMounted, reactive, watch} from "vue";
import {Delete, Expand, Fold, RefreshLeft, User} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";

//路由
const route = useRoute();
// 组件完成初始渲染并创建 DOM 节点后运行代码
onMounted(()=>{
  data.bindingPath = route.path
})

// 监听器 当页面变化时将路径赋予bindingPath
watch(route, ()=>{
  data.bindingPath = route.path
})

const data = reactive({
  bindingPath: "",
  isCollapse: false,//展开或收起
  block: true,//默认显示第一个图标
  toggle: false,//第二个图标默认隐藏
})

const isC = ()=> {
  data.isCollapse = !data.isCollapse;
  data.toggle = !data.toggle;
  data.block = !data.block;
}


</script>

<style lang="scss" scoped>

.workbench-container {
  height: calc(100% - 10px);
  width: 100%;
  padding-top: 62px;
}
.el-main {
  padding: 0 10px 0 10px;
}
.el-menu { // 收起后的menu样式
  height: 100%;
}
.workbench-container-aside-menu:not(.el-menu--collapse) { // 展开后的menu样式
  width: 200px;
  height: 100%;
}

// 按钮样式
.collapse-box {
  height: 40px;
  display: flex;
  align-items: center;
  //justify-content: center;
  width: 40px;
  i {
    cursor: pointer;
    font-size: 25px;
  }
}

</style>