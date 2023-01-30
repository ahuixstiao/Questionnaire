<template>
  <el-card class="recycleBin-card">
    <!-- 列表 -->
    <el-row class="recycleBin-card-table-row">
      <el-table height="100%" stripe :data="data.questionnaireList">
        <el-table-column align="center" prop="id" label="问卷ID" width="auto"/>
        <el-table-column align="center" prop="title" label="标题" width="auto"/>
        <el-table-column align="center" prop="recovery" label="回收统计" width="auto"/>
        <el-table-column align="center" label="操作" width="auto">
          <template #default="scope">
            <el-space :size="5" spacer="|">
              <el-button link type="primary" @click="recoveryQuestionnaireInfoHandler(scope.row.id)">恢复</el-button>
              <el-button link type="primary" @click="deleteQuestionnaireHandler(scope.row.id)">彻底删除</el-button>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <!-- 分页控制 -->
    <el-row class="recycleBin-card-page-row">
      <el-pagination background
                     :total="data.total" v-model:current-page="data.currentPage" v-model:page-size="data.pageSize"
                     :page-sizes="[5, 10, 20, 50, 100]" layout="sizes, prev, pager, next, jumper, ->"
                     @current-change="queryQuestionnaireRecycleBinHandler"
                     @size-change="queryQuestionnaireRecycleBinHandler"/>
    </el-row>
  </el-card>
</template>

<script setup>
import {onMounted, reactive} from "vue";
import {
  deleteQuestionnaireInfo,
  queryQuestionnaireRecycleBin, queryUserIssueQuestionnaireList,
  removeOrRecoveryQuestionnaireInfo
} from "@/api/userRequest";
import {ElMessage} from "element-plus";

const data = reactive({
  // 按钮加载状态
  loading: false,
  // 控制dialog对话框是否显示
  dialogVisible: false,
  // 当前页
  currentPage: 1,
  // 每页显示记录条数
  pageSize: 5,
  // 记录总数
  total: 0,
  // 问卷集合
  questionnaireList: []
})

onMounted(() => {
  queryQuestionnaireRecycleBinHandler()
})

// TODO 查询用户回收站的问卷列表
const queryQuestionnaireRecycleBinHandler = async () => {
  let res = await queryUserIssueQuestionnaireList(1, data.currentPage, data.pageSize);
  console.log(res);
  if (res.status === 200) {
    data.questionnaireList = res.data.records;
    data.total = res.data.total;
  } else {
    showMessage(res.message, 'e')
  }
}

// TODO 将用户问卷移出回收站
const recoveryQuestionnaireInfoHandler = async (id) => {
  let res = await removeOrRecoveryQuestionnaireInfo(id, 0);
  if (res.status === 200) {
    showMessage(res.message, 's');
    await queryQuestionnaireRecycleBinHandler();
  }else {
    showMessage(res.message, 'e');
  }
}

// TODO 彻底删除用户问卷信息
const deleteQuestionnaireHandler = async (id) => {
  let res = await deleteQuestionnaireInfo(id);
  if (res.status === 200) {
    showMessage(res.message, 's');
    await queryQuestionnaireRecycleBinHandler();
  }else {
    showMessage(res.message, 'e');
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
    // 消息类型
    type: type
  });
}

</script>

<style lang="scss" scoped>

.recycleBin-card {
  width: 100%;
  height: calc(100% - 40px);

  :deep(.el-card__body ) {
    height: 100%;
  }
}

.recycleBin-card-table-row {
  height: calc(100% - 60px);
}

.el-button {
  justify-content: center;
  align-items: center;
}

.recycleBin-card-page-row {
  display: flex;
  margin-top: 25px;
  justify-content: center;
  align-items: center;
}

</style>