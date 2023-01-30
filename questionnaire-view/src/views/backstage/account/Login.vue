<template>
  <div class="head">
    <video class="video-wrap-video" poster="https://qq-web.cdn-go.cn/zc.qq.com/f59c7d00/v3/img/img-bg@2x.png" src="https://qq-web.cdn-go.cn/zc.qq.com/f59c7d00/v3/img/bg-video.mp4" tabindex="-1" airplay="allow" x-webkit-airplay="true" playsinline="true" webkit-playsinline="true" x5-playsinline="true" x5-video-player-type="h5" x5-video-player-fullscreen="true" autoplay="" muted=""></video>
    <div class="head-item-warp">
      <TypeIt :values="['Welcome']" :cursor="true" :speed="200" :class-name="'head-item-welcome-text'" />
      <span class="head-item-remark-text">{{ data.remark }}</span>
      <span class="head-item-remark-text-author">{{ data.remarkAuthor }}</span>
      <el-form :inline="false" class="head-item-form">
        <el-form-item class="form-item">
          <el-input class="head-form-item-account" type="text" v-model="data.account" size="large" placeholder="账号" clearable/>
          <el-input class="head-form-item-password" type="password" @keyup.enter="adminLoginHandler" v-model="data.password" size="large" placeholder="密码" clearable/>
          <el-button class="head-form-button" type="primary" :loading="data.loading" @click="adminLoginHandler">登陆</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ElMessage} from "element-plus";
import {onMounted, reactive} from "vue";
import {useRouter} from "vue-router";
import {login, obtainRemark} from "@/api/commonsRequest";
import TypeIt from "@/assets/typeiter";


// 路由
const router = useRouter();

const data = reactive({
  account: "ahuiXstiao",
  password: "ahui0503",
  remark: "", // 每日一言
  remarkAuthor: "", // 每日一言作者
  captchaImage: "",       //验证码图片
  uuidKey: "",            //验证码UUID-Key
  captcha: "",            //验证码
  loading: false,         // false 正常 true 加载
})

// TODO 管理员登陆
const adminLoginHandler = async () => {
  data.loading = true;
  let res = await login(data.account, data.password, true);
  if (res.status === 200) {
    //保存token信息用以访问需要登陆的接口
    localStorage.setItem("tokenName", res.data.tokenName);
    localStorage.setItem("tokenValue", res.data.tokenValue);
    localStorage.setItem("id", res.data.userInfo.id);
    localStorage.setItem("userId", res.data.userInfo.userId);
    localStorage.setItem("account", res.data.userInfo.account);
    localStorage.setItem("avatar", res.data.userInfo.avatar);
    localStorage.setItem("nickName", res.data.userInfo.nickName);
    localStorage.setItem("phoneNumber", res.data.userInfo.phoneNumber);
    localStorage.setItem("signature", res.data.userInfo.signature);
    localStorage.setItem("email", res.data.userInfo.email);
    // 用户身份
    localStorage.setItem("userType", res.data.userInfo.userType);
    // 登录状态 1登录 0未登录
    localStorage.setItem("userStatus", "1");
    //跳转到后台主页
    await router.push("/admin");
  } else {
    showMessage(res.message, "e");
    data.loading = false;
  }
}

// TODO 获取每日一言
const obtainRemarkHandler = async () => {
  let res = await obtainRemark();
  if (res.code === 200) {
    data.remark = res.data.content;
    data.remarkAuthor = "--"+ res.data.author;
  }
}

onMounted(()=>{
  obtainRemarkHandler()
})

/**
 * TODO 封装message消息提示
 * @param message 消息内容
 * @param type 消息类型 success/warning/info/error
 */
const showMessage = (message, type) => {
  if (type === "s"){
    type = "success"
  }else if (type === "e") {
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
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}


.video-wrap-video {
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  width: 100%;
  height: 100%;
  -o-object-fit: cover;
  object-fit: cover;
  z-index: -1; // 将层级改到最底层
}

.head-item-warp {
  padding: 30px;
  width: 400px;
  margin-left: auto;
  margin-right: 20%;
}

.head-item-welcome-text {
  outline: 2px solid transparent;
  outline-offset: 2px;

  display: block;
  margin-bottom: 8px;
  line-height: 1.5;
  font-size: 45px;
  font-family: txFont, serif;
}

.head-item-remark-text {
  display: block;
  margin-bottom: 5px;
  font-size: 18px;
  line-height: 1.3;
  font-family: smileySansFont, serif;
}

.head-item-remark-text-author {
  display: block;
  margin-bottom: 32px;
  margin-left: 260px;
  font-size: 15px;
  line-height: 1.3;
  font-family: smileySansFont, serif;
}

.head-item-form {
  height: 100%;
  width: 100%;
}

.form-item {
  height: 100%;
  width: 100%;

}

.head-form-item-account {
  height: 45px;
}

.head-form-item-password {
  height: 45px;
  margin-top: 20px;
}

.head-form-item-captcha-image {
  width: 100px;
  height: 45px;
  margin-top: 20px
}

.head-form-item-captcha {
  width: 230px;
  height: 45px;
  margin-top: 20px;
  left: 10px;
}

.head-form-button {
  margin-top: 20px;
  width: 100%;
  height: 45px;
}

</style>