<template>
  <div class="myAccount-container">
    <el-card class="user-card">
      <!-- 头像 -->
      <el-upload class="avatar-uploader" :show-file-list="false" :auto-upload="false" :on-change="avatarFileUpload">
        <el-avatar v-if="data.users.avatar" shape="square" :size="178" fit="cover" :src="data.users.avatar" />
        <el-icon v-else class="avatar-uploader-icon"><Plus/></el-icon>
      </el-upload>
      <!-- 信息展示 -->
      <el-descriptions :column="1" size="large" direction="horizontal" title="账户信息">
        <el-descriptions-item label-class-name="my-label" label="账号:">{{ data.users.account }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="昵称:">{{ data.users.nickName }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="邮箱:">{{ data.users.email }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="手机号码:">{{ data.users.phoneNumber }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="个性签名:">{{ data.users.signature }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    <el-card class="edit-card">
      <!-- 基本资料 -->
      <el-tabs v-model="data.activeName">
        <el-tab-pane label="基本资料" name="1">
          <el-form class="edit-card-form" label-position="left" label-width="auto" :model="data.newUserInfo">
            <el-form-item label="用户昵称:">
              <el-input v-model="data.newUserInfo.nickName" />
            </el-form-item>
            <el-form-item label="手机号码:" prop="phoneNumber" :rules="[{ validator: validatePhoneNumber, trigger: 'blur' }]">
              <el-input v-model="data.newUserInfo.phoneNumber" />
            </el-form-item>
            <el-form-item label="用户邮箱:">
              <el-input v-model="data.newUserInfo.email" />
            </el-form-item>
            <el-form-item label="个性签名:">
              <el-input v-model="data.newUserInfo.signature" rows="5" type="textarea"/>
            </el-form-item>
            <el-form-item>
              <el-button class="get-started-btn" :loading="data.loading" @click="modifyUserInfo">保存</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="修改密码" name="2">
          <el-form label-position="left" label-width="auto" class="edit-card-form" :model="data">
            <el-form-item label="原密码:" prop="oldPassword" :rules="[{ validator: validatePasswordIsNull, trigger: 'blur' }]">
              <el-input v-model="data.oldPassword" type="password" show-password/>
            </el-form-item>
            <el-form-item label="新密码:" prop="newPassword" :rules="[{ validator: validatePasswordIsNull, trigger: 'blur' }]">
              <el-input v-model="data.newPassword" type="password" show-password/>
            </el-form-item>
            <el-form-item label="确认密码:" prop="confirmPassword" :rules="[{ validator: validateConfirmPassword, trigger: 'blur' }]">
              <el-input v-model="data.confirmPassword" type="password" show-password/>
            </el-form-item>
            <el-form-item>
              <el-button class="get-started-btn" :loading="data.loading" @click="modifyUserPasswordHandler">修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>

</template>

<script setup>
import {reactive} from "vue";
import {Plus} from "@element-plus/icons-vue";
import {fileUpload} from "@/api/commonsRequest";
import {ElMessage} from "element-plus";
import {modifyingUserInfoByUserId, modifyingUserPassword, queryUserInfoByUserId} from "@/api/userRequest";
import {userStore} from "@/store/userInfo";
import {useRouter} from "vue-router";

const router = useRouter();
const store = userStore();

const data = reactive({
  users: {
    id: localStorage.getItem("id"),
    userId: localStorage.getItem("userId"),
    account: localStorage.getItem("account"),
    avatar: localStorage.getItem("avatar"),
    nickName: localStorage.getItem("nickName"),
    phoneNumber: localStorage.getItem("phoneNumber"),
    email: localStorage.getItem("email"),
    signature: localStorage.getItem("signature"),
  },
  newUserInfo: {
    id: localStorage.getItem("id"),
    userId: localStorage.getItem("userId"),
    nickName: localStorage.getItem("nickName"),
    phoneNumber: localStorage.getItem("phoneNumber"),
    email: localStorage.getItem("email"),
    signature: localStorage.getItem("signature"),
  },
  loading: false,
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
  activeName: "1",
})

// TODO 更改用户信息
const modifyUserInfo = async ()=> {
  data.loading = true;
  let res = await modifyingUserInfoByUserId(data.newUserInfo);
  if (res.status === 200) {
    showMessage(res.message, 's')
    // 更新后查询一遍用户信息
    await queryUserInfoHandler();
  }else {
    showMessage(res.message, 'e')
  }
  data.loading = false;
}


// TODO 修改用户密码
const modifyUserPasswordHandler = async ()=> {
  if(data.newPassword === data.confirmPassword) {
    let res = await modifyingUserPassword(data.oldPassword, data.newPassword);
    if (res.status === 200) {
      showMessage(res.message, "s")
      // 清除本地缓存
      localStorage.clear();
      //将用户状态的本地缓存初始化
      localStorage.setItem("userStatus", "0");
      store.users = Object();
      //showMessage("修改成功，去重新登陆", "s")
      ElMessage.success("修改成功，去重新登陆")
      // 跳转
      await router.push("/");
    }else {
      showMessage(res.message, "e")
    }
  }else {
    showMessage("密码不一致", "e")
  }
}

// TODO 查询用户信息
const queryUserInfoHandler = async ()=> {
  let res = await queryUserInfoByUserId(data.users.userId);
  if (res.status === 200) {
    data.users = res.data;
    localStorage.setItem("id", res.data.id);
    localStorage.setItem("userId", res.data.userId);
    localStorage.setItem("account", res.data.account);
    localStorage.setItem("avatar", res.data.avatar);
    localStorage.setItem("nickName", res.data.nickName);
    localStorage.setItem("phoneNumber", res.data.phoneNumber);
    localStorage.setItem("signature", res.data.signature);
    localStorage.setItem("email", res.data.email);
    store.users.nickName = res.data.nickName;
    // 用户身份
    localStorage.setItem("userType", res.data.userType);
  }else {
    showMessage(res.message, 'e')
  }
}

//TODO 头像上传
const avatarFileUpload = async (file) => {
  console.log(file.size)
  // 上传之前校验一下是否小于10MB
  if (file.size / (1024 * 100 * 10) <= 10) {
    let res = await fileUpload(file.raw);
    if (res.status === 200) {
      localStorage.setItem("avatar", res.data.avatarUrl);
      data.users.avatar = res.data.avatarUrl;
      // 让pinia实时更改header栏头像
      store.users.avatar = res.data.avatarUrl;
    }
  } else {
    showMessage("头像文件过大", "e")
  }
}

// TODO 定义验证函数
const validatePhoneNumber = async (rule, value, callback)=> {
  // 定义正则表达式
  let regex = /^\d{11}$/;
  // 如果用户输入的内容不符合正则表达式，就触发错误提示
  if (!regex.test(value)) {
    callback(new Error('请输入数字长度为11位的手机号码'));
  } else {
    // 如果用户输入的内容符合正则表达式，就不会触发错误提示
    callback();
  }
}

// TODO 校验密码是否为空
const validatePasswordIsNull = async (rule, value, callback)=> {
  if (value === null || value === "") {
    callback(new Error('密码不能为空'));
  } else {
    // 如果用户输入的内容符合正则表达式，就不会触发错误提示
    callback();
  }
}

// TODO 校验密码是否一致
const validateConfirmPassword = async (rule, value, callback)=> {
  if (data.newPassword !== value) {
    callback(new Error('密码不一致'));
  } else {
    // 如果用户输入的内容符合正则表达式，就不会触发错误提示
    callback();
  }
}

/**
 * 封装message消息提示
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


.myAccount-container {
  display: flex;
}

.user-card {
  margin: 0 30px 0 80px;
  width: 254px;
}

.edit-card {
  flex: 1;
  margin-right: 80px;
}

.edit-card-form {
  max-width: 460px;
  margin: 40px 0 0 0;
}

:deep(.avatar-uploader .el-upload) {
  margin-left: 15px;
  margin-bottom: 15px;
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  img{
    height: 100%;
    width: 100%;
  }
}

:deep(.avatar-uploader .el-upload:hover) {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

.get-started-btn {
  background-color: #0052D9;
  color: aliceblue;
  height: 36px;
  width: 50%;
  border-radius: 7px;
  font-size: 16px;
}

</style>