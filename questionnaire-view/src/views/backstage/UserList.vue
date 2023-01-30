<template>
  <div style="height: 100%">
    <!-- 子组件 -->
    <BaseBreadcrumb :routeBreadcrumb="data.links"/>
    <!-- 卡片 -->
    <el-card class="head-item-card">
      <!-- 搜索栏 -->
      <el-row :gutter="20">
        <!-- el-col span一共为24 多个col一起分配这24列 -->
        <el-col :span="10">
          <el-input v-model="data.keyWord" @keyup.enter="queryUserListHandler" placeholder="要搜索的用户昵称">
            <template #append>
              <el-button type="primary" :icon="Search" @click="queryUserListHandler"/>
            </template>
          </el-input>
        </el-col>
        <!-- 添加用户按钮 -->
        <el-col :span="2">
          <el-button class="user-button" @click="data.dialogFormVisible = true">添加用户</el-button>
        </el-col>
        <!-- 导出用户按钮 -->
        <el-col :span="12">
          <el-button class="user-button" @click="exportUsersExcelHandler">导出用户</el-button>
        </el-col>
      </el-row>
      <!-- 列表 -->
      <el-row class="head-item-card-table-row">
        <el-table height="100%" :data="data.userList">
          <el-table-column align="center" prop="id" label="ID" width="90"/>
          <el-table-column align="center" prop="account" label="账号" width="130"/>
          <el-table-column align="center" prop="userType" label="用户类型" width="120">
            <template #default="scope">
              <el-tag v-if="scope.row.userType === 0">超级管理员</el-tag>
              <el-tag v-if="scope.row.userType === 1" type="success">管理员</el-tag>
              <el-tag v-if="scope.row.userType === 2" type="info">普通用户</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="phoneNumber" label="手机号码" width="130"/>
          <el-table-column align="center" prop="avatar" label="用户头像" width="80">
            <template #default="scope">
              <div class="table-column-img-avatar">
                <img v-if="scope.row.avatar" :src="scope.row.avatar" alt="头像">
                <img v-else class="table-column-img-avatar" :src="data.defaultAvatar" alt="头像">
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="nickName" label="用户昵称" width="140"/>
          <el-table-column align="center" prop="loginIp" label="最后登录IP" width="140"/>
          <el-table-column align="center" prop="loginDate" label="最后登录时间" width="110"/>
          <el-table-column align="center" label="操作" width="auto">
            <template #default="scope">
              <el-button v-if="scope.row.userType !== 0" @click="queryUserInfo(scope.row)">查看详情</el-button>
              <el-popconfirm v-if="scope.row.userType !== 0" title="确认删除"
                             @confirm="removeUserInfoHandler(scope.row.userId)"
                             confirm-button-type="danger">
                <template #reference>
                  <el-button type="danger">删除</el-button>
                </template>
              </el-popconfirm>
              <p v-else>无法操作</p>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <!-- 分页控制 -->
      <el-row class="head-item-card-page-row">
        <el-pagination
            background
            :total="data.total" v-model:current-page="data.currentPage" v-model:page-size="data.pageSize"
            :page-sizes="[5, 10, 20, 50, 100]" layout="sizes, prev, pager, next, jumper, ->"
            @current-change="queryUserListHandler" @size-change="queryUserListHandler"/>
      </el-row>
    </el-card>
    <!-- 添加用户 弹出框 -->
    <el-dialog center width="30%" v-model="data.dialogFormVisible" title="添加用户信息">
      <el-form label-position="left">
        <el-form-item label="账号:" :label-width="formLabelWidth">
          <el-input v-model="data.saveUsers.account"/>
        </el-form-item>
        <el-form-item label="密码:" :label-width="formLabelWidth">
          <el-input v-model="data.saveUsers.password"/>
        </el-form-item>
        <el-form-item label="用户类型:" :label-width="formLabelWidth">
          <el-select v-model="data.saveUsers.userType" placeholder="请选择用户类型">
            <el-option label="普通用户" value="2"/>
            <el-option label="管理员" value="1"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.dialogFormVisible = false">取消</el-button>
        <el-button type="primary" :loading="data.loading" @click="createUserHandler">保存</el-button>
      </span>
      </template>
    </el-dialog>
    <!-- 查看用户详情 弹出框 -->
    <el-dialog center width="35%" v-model="data.dialogFormUserInfo" title="用户详情">
      <el-form label-position="left">
        <el-form-item label="ID:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.id" disabled/>
        </el-form-item>
        <el-form-item label="账号:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.account"/>
        </el-form-item>
        <el-form-item label="用户类型:" :label-width="formLabelWidth">
          <el-select v-model="data.userInfo.userType" placeholder="请选择用户类型">
            <el-option label="普通用户" :value="2"/>
            <el-option label="管理员" :value="1"/>
            <el-option label="超级管理员" disabled/>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.nickName"/>
        </el-form-item>
        <el-form-item label="手机号码:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.phoneNumber"/>
        </el-form-item>
        <el-form-item label="个性签名:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.signature"/>
        </el-form-item>
        <el-form-item label="邮箱:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.email"/>
        </el-form-item>
        <el-form-item label="联系地址:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.address"/>
        </el-form-item>
        <el-form-item label="最后登录IP:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.loginIp" disabled/>
        </el-form-item>
        <el-form-item label="最后登录时间:" :label-width="formLabelWidth">
          <el-input v-model="data.userInfo.loginDate" disabled/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.dialogFormUserInfo = false">取消</el-button>
        <el-button type="primary" :loading="data.loading" @click="modifyingUserInfoByUserIdHandler">修改</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import BaseBreadcrumb from "@/components/base/BaseBreadcrumb.vue";
import {onMounted, reactive} from "vue";
import {Search} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {
  exportUsersExcel,
  modifyingUserInfoByUserId,
  queryUserInfoByCondition,
  queryUserList,
  removeUserInfo
} from "@/api/userRequest";
import {register} from "@/api/commonsRequest";

// 组件完成初始渲染并创建 DOM 节点后运行代码
onMounted(() => {
  queryUserListHandler()
})

// 定义弹出的表单项宽度
const formLabelWidth = '100px';

const data = reactive({
  // 新增弹出框控制
  dialogFormVisible: false,
  // 详情弹出框控制
  dialogFormUserInfo: false,
  // 按钮加载控制 false 正常 true 加载
  loading: false,
  // 当前页
  currentPage: 1,
  // 每页显示记录条数
  pageSize: 5,
  // 记录总数
  total: 0,
  // 默认头像
  defaultAvatar: "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
  // 新增用户对象
  saveUsers: {
    account: "",
    password: "",
    userType: "",
  },
  // 搜索关键字
  keyWord: "",
  // 用户对象
  userInfo: {},
  // 用户集合
  userList: [],
  links: [
    {name: "用户管理", path: ""},
    {name: "用户列表", path: ""},
  ]
})

// TODO 创建用户
const createUserHandler = async () => {
  let res = await register(data.saveUsers);
  data.loading = true;
  if (res.status === 200) {
    data.dialogFormVisible = false;
    showMessage(res.message, 's');
    // 初始化输入框内容
    data.saveUsers.account = '';
    data.saveUsers.password = '';
    // 刷新一次用户列表
    await queryUserListHandler();
  } else {
    showMessage(res.message, 'e');
  }
  data.loading = false;
}

// TODO 删除用户
const removeUserInfoHandler = async (userId) => {
  let res = await removeUserInfo(userId);
  if (res.status === 200) {
    showMessage(res.message, 's');
    await queryUserListHandler();
  } else {
    showMessage(res.message, 'e');
  }
}

// TODO 查询用户信息
const queryUserInfo = async (users) => {
  data.userInfo = users;
  console.log(users)
  data.dialogFormUserInfo = true;
}

// TODO 修改用户信息
const modifyingUserInfoByUserIdHandler = async () => {
  let res = await modifyingUserInfoByUserId(data.userInfo);
  data.loading = true;
  if (res.status === 200) {
    data.dialogFormUserInfo = false;
    showMessage(res.message, 's');
    await queryUserListHandler();
  } else {
    showMessage(res.message, 'e');
  }
  data.loading = false;
}

// TODO 查询用户列表
const queryUserListHandler = async () => {
  let res;
  if(data.keyWord !== ""){
    res = await queryUserInfoByCondition(data.currentPage, data.pageSize, data.keyWord);
  }else {
    res = await queryUserList(data.currentPage, data.pageSize);
  }
  if (res.status === 200) {
    data.userList = res.data.records;
    data.total = res.data.total;
  } else {
    showMessage(res.message, 'e')
  }
}

// TODO 导出用户信息
const exportUsersExcelHandler = async ()=> {
  let fileName = "用户信息表";
  let res = await exportUsersExcel(fileName);
  const url = window.URL.createObjectURL(new Blob([res])) // 将返回的二进制流转换成浏览器可使用的URL
  const link = document.createElement("a") // 创建一个隐藏的 a标签
  link.href = url                          // 将上面转换的url赋值给a标签的href属性
  link.setAttribute("download", fileName+".xlsx") // 设置a标签的download属性，并指定下载时的文件名
  document.body.appendChild(link) // 将a标签添加到body中
  link.click() // 执行a标签的点击事件
  showMessage("成功", "s")
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

.head-item-card {
  height: calc(100% - 20px);
  :deep(.el-card__body ) {
    height: 100%;
  }
}

.head-item-card-table-row {
  height: calc(100% - 82px);
  padding-top: 20px;
}

.head-item-card-page-row {
  display: flex;
  height: 50px;
  margin-top: 10px;
  justify-content: center;
  align-items: center;
}

.table-column-img-avatar {
  vertical-align: middle;
  height: 45px;
  width: 90%;
  img{
    height: 100%;
    width: 100%;
    object-fit: cover;
  }
}

.user-button {
  background-color: #0052D9;
  color: aliceblue;
}

</style>