import {createRouter, createWebHistory} from 'vue-router'
import App from "@/App.vue";

const routes = [
    // 首先进入的路由不能使用懒加载，否则将不能顺利加载组件
    {path: '/', component: App, redirect: "/home", meta: {auth: false}},

    {path: '/home', component: () => import("@/views/frontDesk/index/Home.vue"), meta: {auth: false}},
    {path: '/login', component: () => import("@/views/frontDesk/account/Login.vue"), meta: {auth: false}},
    {path: '/register', component: () => import("@/views/frontDesk/account/Register.vue"), meta: {auth: false}},
    {path: '/workbench',
        component: () => import("@/views/frontDesk/index/Workbench.vue"),
        meta: {auth: true},
        redirect: '/workbench/mine',
        children: [
            {path: 'mine', component: () => import("@/views/frontDesk/index/Mine.vue"), meta: {auth: true}},
            {path: 'recycleBin', component: ()=> import("@/views/frontDesk/index/RecycleBin.vue"), meta: {auth: true}}
        ]
    },


    // 个人中心
    {path: '/personalCenter',
        component: () => import("@/views/frontDesk/user/PersonalCenter.vue"),
        redirect: '/personalCenter/myAccount',
        meta: {auth: true},
        children: [
            {path: 'myAccount', component: ()=> import("@/views/frontDesk/user/MyAccount.vue"), meta: {auth: true}},
            {path: 'answerQuestionnaire', component: ()=> import("@/views/frontDesk/user/AnswerQuestionnaire.vue"), meta: {auth: true}},
        ]
    },

    /* 后台路由 */

    //后台首页
    {
        path: '/admin',
        component: () => import("@/views/backstage/index/Home.vue"),
        redirect: "/admin/userList",
        meta: {
            Oauth: true
        },
        children: [
            {path: "userList", component: () => import("@/views/backstage/UserList.vue"), meta: {Oauth: true}},
            {path: "questionnaireList", component: () => import("@/views/backstage/QuestionnaireList.vue"), meta: {Oauth: true}},
        ],
    },

    //后台登录页
    {path: '/admin_login', component: () => import("@/views/backstage/account/Login.vue"), meta: {Oauth: false}},

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

// 全局前置导航钩子 beforeEach 会在路由即将改变前触发
// to要跳转的路由 from目标路由 next不作任何拦截直接放行到目标路由
router.beforeEach((to, from) => {
    // 是否登录
    let isLogin = localStorage.getItem('userStatus') === "1";
    // 判断是否为管理员
    let isAdmin = localStorage.getItem('userType'); // 1 管理员 0 超级管理员

    // TODO 后台
    if (to.meta.Oauth) { // 验证访问的路由是否需要登陆验证
        // 是否登录
        if (isLogin) {
            // 判断是否是管理员
            if (isAdmin !== null && isAdmin !== '' && isAdmin !== '2') {
                //管理员
                return true;
            } else {
                return from.path;
            }
        } else {
            return from.path;
        }
    }

    // TODO 前台
    if (to.meta.auth) { //是否需要验证登录状态
        if (isLogin) { // 是否登录
            return true //直接放行
        } else {
            // 如果没有携带有效token且访问登录or注册页面则直接放行
            if (to.path === '/login' || to.path === '/register') {
                return true //跳转登录或注册页面
            } else {
                return '/login' // 既不访问登录注册页面也未登录 则跳到登录页
            }
        }
    }

    return true
})

export default router
