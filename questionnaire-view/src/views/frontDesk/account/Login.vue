<template>
  <div class="head">
    <video class="video-wrap-video" poster="https://qq-web.cdn-go.cn/zc.qq.com/f59c7d00/v3/img/img-bg@2x.png"
           src="https://qq-web.cdn-go.cn/zc.qq.com/f59c7d00/v3/img/bg-video.mp4" tabindex="-1" airplay="allow"
           x-webkit-airplay="true" playsinline="true" webkit-playsinline="true" x5-playsinline="true"
           x5-video-player-type="h5" x5-video-player-fullscreen="true" autoplay="" muted=""></video>
    <div class="head-item-warp">
      <TypeIt :values="['登录']" :cursor="true" :speed="200" :class-name="'head-item-welcome-text'" />
      <span class="head-item-remark-text">{{ data.remark }}</span>
      <span class="head-item-remark-text-author">{{ data.remarkAuthor }}</span>
      <el-form class="head-item-form" :model="data">
        <el-form-item prop="account" :rules="[{ validator: validateAccount, trigger: 'blur' }]">
          <el-input class="head-item-form-input" type="text" v-model="data.account" size="large" placeholder="账号" clearable/>
        </el-form-item>
        <el-form-item>
          <el-input class="head-item-form-input" type="password" v-model="data.password" size="large" placeholder="密码" clearable/>
        </el-form-item>
        <el-form-item>
          <el-image class="head-item-form-captcha-image" :src="data.captchaImage" @click="captchaHandler"/>
          <el-input class="head-item-form-captcha" size="large" @keyup.enter="verifyCaptchaHandler" type="text" v-model="data.captcha" placeholder="请输入验证码"/>
        </el-form-item>
        <el-form-item>
          <el-button class="head-form-button" type="primary" :loading="data.loading" @click="verifyCaptchaHandler">登陆</el-button>
        </el-form-item>
        <el-form-item>
          <router-link to="/register">
            <el-button class="head-form-button" type="primary">还没有账号？去注册</el-button>
          </router-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ElMessage} from "element-plus";
import {onMounted, reactive} from "vue";
import {useRouter} from "vue-router";
import {captcha, login, obtainRemark, verifyCaptcha} from "@/api/commonsRequest";
import TypeIt from "@/assets/typeiter";
import {userStore} from "@/store/userInfo";

// 路由
const router = useRouter();
const store = userStore();

onMounted(() => {
  obtainRemarkHandler()
  captchaHandler()
})

const data = reactive({
  account: "ahuiXstiao",
  password: "ahui0503",
  remark: "",         // 每日一言
  remarkAuthor: "",   // 每日一言作者
  captchaImage: "",   //验证码图片
  key: "",            //验证码Key
  captcha: "",        //验证码
  loading: false,     // false 正常 true 加载
})

// TODO 普通用户登陆
const ordinaryUsersLoginHandler = async () => {
  let res = await login(data.account, data.password, false);
  if (res.status === 200) {
    console.log(res);
    showMessage(res.message, "s");
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
    // 登陆时将参数给一次pinia否则会拿不到参数 因为localStorage和pinia的创建时机不一样
    store.users.avatar = res.data.userInfo.avatar;
    store.users.nickName = res.data.userInfo.nickName;
    // 用户身份
    localStorage.setItem("userType", res.data.userInfo.userType);
    // 登录状态 1登录 0未登录
    localStorage.setItem("userStatus", "1");
    //跳转到主页
    await router.push("/home");
  } else {
    showMessage(res.message, "e");
  }
}

// 定义验证函数
const validateAccount = async (rule, value, callback)=> {
  // 定义正则表达式
  let regex = /^[A-Za-z0-9]+$/;
  // 如果用户输入的内容不符合正则表达式，就触发错误提示
  if (!regex.test(value)) {
    callback(new Error('请输入大写或小写字母和数字'));
  } else {
    // 如果用户输入的内容符合正则表达式，就不会触发错误提示
    callback();
  }
}

//TODO 获取验证码
const captchaHandler = async () => {
  // 发起请求
  let res = await captcha();
  console.log(res);
  if (res.status === 200) {
    data.key = res.data.key;    // 保存验证码
    data.captchaImage = res.data.image; // 保存验证码图片
  } else {
    showMessage(res.message, "e");
  }
}

//TODO 校验验证码
const verifyCaptchaHandler = async () => {
  // 将按钮改为加载状态
  data.loading = true;
  let res = await verifyCaptcha(data.key, data.captcha);
  if (res.status === 200) {
    // 登录
    await ordinaryUsersLoginHandler();
  } else {
    // 验证失败
    showMessage(res.message, "e");
  }
  // 刷新验证码
  await captchaHandler();
  // 将按钮设为可点击
  data.loading = false;
}

// TODO 获取每日一言
const obtainRemarkHandler = async () => {
  let res = await obtainRemark();
  if (res.code === 200) {
    data.remark = res.data.content;
    data.remarkAuthor = "--" + res.data.author;
  }
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
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
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
  display: block;
  margin-bottom: 8px;
  line-height: 1.5;
  font-size: 45px;
  font-family: smileySansFont, serif;
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
  margin-bottom: 28px;
  margin-left: 260px;
  font-size: 15px;
  line-height: 1.3;
  font-family: smileySansFont, serif;
}

.head-item-form-input {
  height: 45px;
}

.head-item-form-captcha-image {
  width: 100px;
  height: 45px;
}

.head-item-form-captcha {
  width: 230px;
  height: 47px;
  left: 10px;
}

.head-form-button {
  width: 100%;
  height: 45px;
}

a {
  width: 100%;
}


</style>