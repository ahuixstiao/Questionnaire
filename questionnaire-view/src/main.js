// 每个 Vue 应用都是通过 createApp 函数创建一个新的应用实例.
import { createApp } from 'vue'
// 根组件
import App from './App.vue'
// 路由
import router from './router'
// 全局组件状态管理器
import { createPinia } from 'pinia'
// ElementPlus UI
import elementPlus from 'element-plus'
// ElementPlus 汉化库
import zhCn from 'element-plus/es/locale/lang/zh-cn'
// ElementPlus icon图标库
import * as elementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
// 自定义全局css样式
import '@/assets/css/global.scss'
// 路由动画样式库
import 'animate.css';


// ============================================================

//构建vue实例 并注入其他插件
const app = createApp(App);
// 遍历elementPlusIconsVue
for (const [key, component] of Object.entries(elementPlusIconsVue)) {
    app.component(key, component);
}
// 导入pinia
app.use(createPinia());
// ElementPlus组件 本地化
app.use(elementPlus, {locale: zhCn});
// 添加路由到Vue实例中
app.use(router);
app.mount('#app');
