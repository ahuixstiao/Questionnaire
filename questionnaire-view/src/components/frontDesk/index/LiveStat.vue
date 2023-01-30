<template>
  <div class="live-stat">
    <div class="stat">
      当前已经为广大用户完成
      <span class="user-count">
        {{ data.userCount }}
      </span>
      份回收
    </div>
    <el-button class="get-started-btn">立即使用</el-button>
  </div>
</template>

<script setup>

import {reactive} from "vue";

const data = reactive({
  userCount: 100
})

</script>

<style lang="scss" scoped>

.live-stat {
  position: center;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  height: 142px;
  margin: 0 auto;
  border-bottom: 1px solid #ddd;
}

.stat {
  margin-left: 150px;
  color: #999;
  font-size: 14px;
}

.user-count {
  position: center;
  color: #0052D9;
  font-size: 30px;
  margin: 0 5px;
}

.get-started-btn {
  background-color: #0052D9;
  color: aliceblue;
  height: 48px;
  margin-right: 150px;
  padding: 0 41px;
  border-radius: 7px;
  font-size: 18px;
}

</style>