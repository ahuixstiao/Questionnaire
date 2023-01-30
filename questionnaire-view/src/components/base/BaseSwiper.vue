<template>
  <div class="swiper-container">
    <el-carousel trigger="click" height="503px">
      <el-carousel-item  v-for="item in data.bannerImages" :key="item">
        <img :src="item" alt="banner"/>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup>
import {reactive} from "vue";

const data = reactive({
  bannerImages: [
    "http://qiniu.huichat.icu/IMG_0383%2820201220-101256%29.JPG",
    "http://qiniu.huichat.icu/IMG_0384%2820201220-101314%29.JPG",
    "http://qiniu.huichat.icu/IMG_0385%2820201220-101427%29.JPG",
    "http://qiniu.huichat.icu/IMG_0386%2820201220-101542%29.JPG",
    "http://qiniu.huichat.icu/IMG_0389%2820201220-101812%29.JPG"
  ]
})


</script>

<style lang="scss" scoped>

.swiper-container {
  padding-top: 60px;
}

img{
  height: 100%;
  width: 100%;
  object-fit: cover;
}

</style>