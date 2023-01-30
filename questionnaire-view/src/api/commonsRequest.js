import {customRequest, request} from "@/api/api";


// TODO 文件上传
export function fileUpload(file) {
    return request({
        url: "/common/file/ossUpload",
        method: "put",
        data: {file},
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

// TODO 登录
export function login(account, password, isAdmin) {
    return request({
        url: "/auth/" + account + "/" + password + "/" + isAdmin,
        method: "get"
    });
}

// TODO 注册
export function register(users) {
    return request({
        url: "/auth/",
        method: "post",
        data: users,
        type: "json"
    });
}

// TODO 注销
export function logout() {
    return request({
        url: "/auth/logout",
        method: "get"
    })
}

// TODO 获取验证码
export function captcha() {
    return request({
        url: "/common/captcha/",
        method: "get"
    });
}

// TODO 校验验证码
export function verifyCaptcha(key, captcha) {
    return request({
        url: "/common/captcha/" + key + "/" + captcha,
        method: "get"
    });
}

// TODO 获取每日一言
export function obtainRemark() {
    return customRequest({
        url: "https://saying.api.azwcl.com/saying/get",
        method: "get"
    })
}