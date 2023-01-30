<template>
  <div class="head">
    <div class="head-item-warp">
      <div class="head-item">
        <router-link to="/" class="nav-icon">
          <img src="http://qiniu.huichat.icu/logo.png" alt="TeaCup问卷" class="icon-img"/>
          <div class="tx-font">TeaCup问卷</div>
        </router-link>
      </div>
      <!-- 遍历路由列表 -->
      <template v-if="data.isShow" v-for="[item, link] in data.links.entries()" :key="item">
        <div class="head-item head-item-path-name">
          <router-link :to="link.path">
            {{ link.name }}
          </router-link>
        </div>
      </template>
      <div class="head-item-flex"/>
      <div class="head-item">
        <!-- 登录按钮 -->
        <router-link to="/login">
          <el-button v-if="!data.userStatus" class="login-button">登陆</el-button>
        </router-link>
        <!-- 用户昵称 -->
        <span class="head-item-nickName" v-if="data.userStatus">{{data.nickName}}</span>
        <!-- 头像与下拉列表 -->
        <el-dropdown v-if="data.userStatus">
          <!-- 头像 -->
          <div class="head-item-avatar"><img :src="store.users.avatar"  alt="头像"></div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/personalCenter')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="logoutHandler" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup>

import {onMounted, reactive, watch} from "vue";
import {ElMessage} from "element-plus";
import {useRoute, useRouter} from "vue-router";
import {logout} from "@/api/commonsRequest";
import {userStore} from "@/store/userInfo";

//路由
const router = useRouter();
const route = useRoute();

// state
const store = userStore();

const data = reactive({
  links: [
    {name: '工作台', path: '/workbench'}
  ],
  isShow: true,
  userStatus: localStorage.getItem("userStatus") === "1",
  nickName: localStorage.getItem("nickName"),
  avatar: localStorage.getItem("avatar"),
})

onMounted(()=> {
  isShowPathHandler()
})

watch( ()=>route.path, ()=>{
  isShowPathHandler()
})

// TODO 是否显示header栏路由
const isShowPathHandler = ()=> {
  let notShowPath = route.path.split("/")[1];
  if(notShowPath.includes("admin")) {
    data.isShow = false;
  }
}

// TODO 注销
const logoutHandler = async () => {
  //发起请求
  let res = await logout();
  if (res.status === 200) {
    //设置登录状态为false
    data.loginStatus = false;
    await router.push("/")
  } else {
    showMessage(res.message, "e")
  }
  // 清除本地缓存
  localStorage.clear();
  //将用户状态的本地缓存初始化
  localStorage.setItem("userStatus", "0");
  // 刷新页面
  location.reload();
}

/**
 * TODO 封装message消息提示
 * @param message 消息内容
 * @param type 消息类型 success/warning/info/error
 */
const showMessage = (message, type) => {
  if (type === "s") {
    type = "success"
  } else if (type === "e") {
    type = "error"
  }
  ElMessage({
    //显示关闭按钮
    showClose: true,
    //显示多少毫秒
    duration: 1500,
    //显示信息
    message: message,
    //文字居中
    center: true,
    type: type
  });
}

</script>

<style lang="scss" scoped>

.head {
  background-color: #0052D9;
  position: fixed;
  align-items: center;
  z-index: 1000;
  height: 60px;
  width: 100%;
  margin: 0 auto;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.head-item-warp {
  padding: 0 30px;
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: space-between;
}

.nav-icon {
  display: flex;
  flex-direction: row;
  align-items: center;
  font-size: 21px;

  .icon-img {
    display: inline-block;
    vertical-align: bottom;
  }

  .tx-font {
    font-family: txFont, tahoma, arial, simsun, sans-serif;
    font-size: 20px;
    padding-left: 10px;
    color: aliceblue;
    display: inline-block;
  }
}

.head-item-nickName {
  position: absolute;
  color: white;
  top: 20px;
  right: 90px;
}

.head-item-avatar {
  height: 43px;
  width: 43px;
  box-sizing: border-box;
  overflow: hidden;
  border-radius: 50%;
  img{
    height: 100%;
    width: 100%;
    object-fit: cover;
  }
}

.head-item-path-name {
  padding-left: 35px;
  a {
    font-size: 19px;
    color: aliceblue;
    display: inline-block;
    &:hover {
      color: #2f8cdb;
      background-size: 100% 2px;
    }
  }
}

.login-button {
  color: #0052D9;
  height: 40px;
  width: 65px;
}

.head-item-flex {
  flex: 1;
}

</style>