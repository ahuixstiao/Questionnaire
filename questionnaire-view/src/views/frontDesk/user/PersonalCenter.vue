<template>
  <Header/>
    <el-container>
      <el-header class="personalCenter-header" height="80">
        <el-menu router mode="horizontal" :default-active="data.bindingPath">
          <el-menu-item index="/personalCenter/myAccount">我的账户</el-menu-item>
          <el-menu-item index="/personalCenter/answerQuestionnaire">我的已填问卷</el-menu-item>
        </el-menu>
      </el-header>

      <el-main>
        <router-view v-slot="{ Component }">
          <transition enter-active-class="animate__animated animate__backInRight">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
</template>

<script setup>
import Header from "@/components/base/BaseHeader.vue";
import {onMounted, reactive, watch} from "vue";
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
  userId: localStorage.getItem('userId'),
  bindingPath: "",
})

</script>

<style lang="scss" scoped>


.personalCenter-header {
  padding: 0 100px;
  display: flex;
  flex-direction: row;
}

.el-main{
  overflow: hidden; // 超出不隐藏
}

.head-item-path-name {
  a {
    text-decoration: none;
    margin-left: 50px;
    font-size: 17px;
    color: black;
    display: inline-block;
    &:hover {
      color: #2f8cdb;
      background-size: 100% 2px;
    }
  }
}

.el-container {
  padding-top: 100px;
}

.el-menu{
  width: 100%;
}
</style>