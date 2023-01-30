import {request} from "@/api/api";

// TODO 查询用户列表
export function queryUserList(currentPage, pageSize) {
    return request({
        url: "/user/list/" + currentPage + "/" + pageSize,
        method: "get",
    });
}

// TODO 按昵称查询用户列表
export function queryUserInfoByCondition(currentPage, pageSize, nickName) {
    return request({
        url: "/user/list/" + currentPage + "/" + pageSize + "/" + nickName,
        method: "get"
    })
}

// TODO 获取用户信息
export function queryUserInfoByUserId(userId) {
    return request({
        url: "/user/get/" + userId,
        method: "get",
    })
}

// TODO 修改用户密码
export function modifyingUserPassword(oldPassword, newPassword) {
    return request({
        url: "/user/update/password",
        method: "put",
        data: {oldPassword, newPassword},
        type: "json"
    })
}

// TODO 修改用户信息
export function modifyingUserInfoByUserId(users) {
    return request({
        url: "/user/update",
        method: "put",
        data: users,
        type: "json",
    });
}

// TODO 删除用户
export function removeUserInfo(userId) {
    return request({
        url: "/user/delete/" + userId,
        method: "delete",
    })
}

// TODO 导出用户
export function exportUsersExcel(fileName) {
    return request({
        url: "/user/exportExcel",
        method: "get",
        responseType: "blob", //后台响应类型为二进制流
        params: {fileName},
    })
}

// TODO 用户问卷列表
export function queryUserIssueQuestionnaireList(flag, currentPage, pageSize) {
    return request({
        url: "/user/questionnaire/list/" + flag + "/" + currentPage + "/" + pageSize,
        method: "get"
    })
}

// TODO 将用户问卷移出/移入回收站
export function removeOrRecoveryQuestionnaireInfo(id, flag) {
    return request({
        url: "/user/questionnaire/operation/" + id + "/" + flag,
        method: "put",
    });
}

// TODO 彻底删除用户问卷信息
export function deleteQuestionnaireInfo(id) {
    return request({
        url: "/user/questionnaire/delete/" + id,
        method: "delete"
    })
}

// TODO 用户已填问卷列表
export function queryAnswerQuestionnaireList(currentPage, pageSize) {
    return request({
        url: "/user/answer/list/" + currentPage + "/" + pageSize,
        method: "get",
    });
}

