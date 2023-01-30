<template>
  <div class="answer-container">
    <el-card class="answer-card">
      <el-table :data="data.answerQuestionnaireList" style="width: 100%; height: 100%">
        <el-table-column prop="title" align="center" label="问卷标题" width="auto"/>
        <el-table-column prop="status" align="center" label="问卷状态" width="auto">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="success">回收中</el-tag>
            <el-tag v-if="scope.row.status === 1" type="info">已暂停</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="author" align="center" label="发布者" width="auto"/>
        <el-table-column prop="time" align="center" label="填答时间" width="auto"/>
        <el-table-column align="center" label="操作" width="auto">
          <template #default="scope">
            <el-button @click="">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import {onMounted, reactive} from "vue";
import {queryAnswerQuestionnaireList} from "@/api/userRequest";
import {ElMessage} from "element-plus";

const data = reactive({
  // 当前页
  currentPage: 1,
  // 每页显示记录条数
  pageSize: 5,
  // 记录总数
  total: 0,
  answerQuestionnaireList: []
});

onMounted(()=>{
  queryAnswerQuestionnaireListHandler()
})

// TODO 查询已填问卷列表
const queryAnswerQuestionnaireListHandler = async () => {
  let res = await queryAnswerQuestionnaireList(data.currentPage, data.pageSize);
  console.log(res)
  if (res.status === 200) {
    data.answerQuestionnaireList = res.data
  }else {
    showMessage(res.message, 'e')
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

.answer-container {
  display: flex;
}

.answer-card {
  margin: 0 80px 0 80px;
  height: 100%;
  width: 100%;
}

</style>