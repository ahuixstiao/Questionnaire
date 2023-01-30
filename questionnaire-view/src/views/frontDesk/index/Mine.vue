<template>
  <el-card class="mine-card">
    <!-- 统计 -->
    <el-row :gutter="20">
      <el-col style="font-size: 15px">个人问卷({{ data.total }})</el-col>
    </el-row>
    <!-- 列表 -->
    <el-row class="mine-card-table-row">
      <el-table height="100%" stripe :data="data.questionnaireList">
        <el-table-column align="center" prop="id" label="问卷ID" width="100"/>
        <el-table-column align="center" prop="title" label="标题" width="250"/>
        <el-table-column align="center" prop="recovery" label="回收统计" width="200"/>
        <el-table-column align="center" prop="status" label="问卷状态" width="200">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="success">答题中</el-tag>
            <el-tag v-if="scope.row.status === 1" type="danger">已过期</el-tag>
            <el-tag v-if="scope.row.status === 2" type="info">已暂停</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="auto">
          <template #default="scope">
            <el-space :size="5" spacer="|" wrap>
              <el-button link type="primary" @click="">允许答题</el-button>
              <el-button link type="primary" @click="">编辑</el-button>
              <el-button link type="primary" @click="">统计</el-button>
              <el-dropdown trigger="click" ref="myDropdown" :hide-on-click="false">
                <el-button link type="primary">更多</el-button>
                <template #dropdown>
                  <el-dropdown-menu slot='dropdown'>
                    <el-dropdown-item>
                      <el-button link>投放问卷</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item divided>
                      <el-button link @click="data.dialogVisible = true">移动到回收站</el-button>
                      <el-dialog v-model="data.dialogVisible" title="移动到回收站" width="35%" :center="true"
                                 :modal="false">
                        <el-divider/>
                        <span>确定要将问卷《{{ scope.row.title }}》移动到回收站吗?</span>
                        <el-divider/>
                        <template #footer>
                        <span>
                          <el-button @click="data.dialogVisible = false">取消</el-button>
                          <el-button
                              type="primary"
                              :loading="data.loading"
                              @click="removeQuestionnaireInfoHandler(scope.row.id)">
                            确定
                          </el-button>
                        </span>
                        </template>
                      </el-dialog>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-space>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <!-- 分页控制 -->
    <el-row class="mine-card-page-row">
      <el-pagination background
                     :total="data.total" v-model:current-page="data.currentPage" v-model:page-size="data.pageSize"
                     :page-sizes="[5, 10, 20, 50, 100]" layout="sizes, prev, pager, next, jumper, ->"
                     @current-change="queryUserIssueQuestionnaireListHandler"
                     @size-change="queryUserIssueQuestionnaireListHandler"/>
    </el-row>
  </el-card>
</template>

<script setup>
import {onMounted, reactive, ref} from "vue";
import {queryUserIssueQuestionnaireList, removeOrRecoveryQuestionnaireInfo} from "@/api/userRequest";
import {ElDivider, ElMessage} from "element-plus";

const myDropdown = ref();

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
  queryUserIssueQuestionnaireListHandler()
})

// TODO 查询用户发布的问卷列表
const queryUserIssueQuestionnaireListHandler = async () => {
  let res = await queryUserIssueQuestionnaireList(0, data.currentPage, data.pageSize);
  if (res.status === 200) {
    data.questionnaireList = res.data.records;
    data.total = res.data.total;
  } else {
    showMessage(res.message, 'e')
  }
}

// TODO 将用户问卷移到回收站
const removeQuestionnaireInfoHandler = async (id) => {
  data.loading = true;
  let res = await removeOrRecoveryQuestionnaireInfo(id, 1);
  if (res.status === 200) {
    showMessage(res.message, 's')
    await queryUserIssueQuestionnaireListHandler()
  } else {
    showMessage(res.message, 'e')
  }
  myDropdown.value.handleClose();
  data.loading = false;
  data.dialogVisible = false;
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

.mine-card {
  width: 100%;
  height: calc(100% - 40px);

  :deep(.el-card__body ) {
    height: 100%;
  }
}

.mine-card-table-row {
  margin-top: 20px;
  height: calc(100% - 90px);
}

.el-button {
  justify-content: center;
  align-items: center;
}

.mine-card-page-row {
  display: flex;
  height: 40px;
  margin-top: 15px;
  justify-content: center;
  align-items: center;
}

</style>