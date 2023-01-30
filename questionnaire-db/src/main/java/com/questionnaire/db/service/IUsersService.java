package com.questionnaire.db.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.entity.Users;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ahui
 * @since 2022-05-23
 */
public interface IUsersService extends IService<Users> {

    /**
     * 注册
     * @param users 用户信息
     * @return Result
     */
    Result<Object> userRegister(Users users);

    /**
     * 查询单个用户信息
     * @param userId 用户ID
     * @return users
     */
    Result<Object> queryUserInfo(String userId);

    /**
     * 查询用户列表，可指定条件
     *
     * @param currentPage 当前页
     * @param pageSize    页面显示条数
     * @param condition   筛选条件
     * @return List<UsersVo> 筛选后的用户列表
     */
    Result<Object> queryUsersList(Integer currentPage, Integer pageSize, String condition);

    /**
     * 用户登录
     * @param account 账户
     * @param password 密码
     * @param isAdmin 是否为管理员
     * @return Result
     */
    Result<Object> login(String account, String password, boolean isAdmin);

    /**
     * 修改
     * @param newUsers 用户信息
     * @return Result
     */
    Result<Object> modifyingUserInfo(Users newUsers);

    /**
     * 校验并修改用户密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return Result<Object>
     */
    Result<Object> modifyingUserPassword(String oldPassword, String newPassword);

    /**
     * 删除用户
     * @param userId 用户ID
     * @return Result
     */
    Result<Object> removeUserInfo(String userId);

}
