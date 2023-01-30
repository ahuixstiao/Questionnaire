import axios from 'axios'

// 创建axios实例
export function request(config) {
    // 用户 token
    const token = localStorage.getItem('tokenValue');

    // 创建 request
    const _axios = axios.create({
        // 服务器基础路径
        baseURL: 'http://localhost:8080', //获取对应环境的路径
        timeout: 5000
    });

    // 添加请求拦截器
    _axios.interceptors.request.use(
        config => {
            if (token != null) {
                config.headers['QToken'] = token
            }
            return config
        }
    );

    // 添加响应拦截器
    _axios.interceptors.response.use(
        //响应
        response => {
            //判断用户Token是否已过期 过期则清除缓存
            if (response.data.status === 500) {
                let message = response.data.message;
                switch (message) {
                    case "未能读取到有效Token": {clear(); break}
                    case "Token无效": {clear(); break}
                    case "Token已过期": {clear(); break}
                    case "账号已被顶下线": {clear(); break}
                    case "账号已被踢下线": {clear(); break}
                }
            }
            return response.data
        },
        //错误
        error => {
            console.log('TCL: error', error)
            return Promise.reject(error)
        }
    );

    return _axios(config)
}

// 清除
const clear = () => {
    // 清除本地缓存
    localStorage.clear();
    //将用户状态的本地缓存初始化
    localStorage.setItem("userStatus", "0");
    // 刷新页面
    location.reload();
}

// 自定义请求
export function customRequest(config) {
    // 创建 request
    const _customAxios = axios.create({
        timeout: 5000
    });

    // 添加请求拦截器
    _customAxios.interceptors.request.use(
        config => {
            return config
        }
    );

    // 添加响应拦截器
    _customAxios.interceptors.response.use(
        //响应
        response => {
            return response.data
        },
        //错误
        error => {
            console.log('TCL: error', error)
            return Promise.reject(error)
        }
    );

    return _customAxios(config)

}

