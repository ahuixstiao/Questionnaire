package com.questionnaire.db.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.questionnaire.common.core.cache.CacheKey;
import com.questionnaire.common.core.cache.RedisCacheTool;
import com.questionnaire.common.core.result.FailEnum;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.common.utils.BeanConvertUtil;
import com.questionnaire.common.utils.Flag;
import com.questionnaire.common.utils.Role;
import com.questionnaire.common.utils.SaltStr;
import com.questionnaire.db.entity.Users;
import com.questionnaire.db.entity.vo.UsersVo;
import com.questionnaire.db.mapper.UsersMapper;
import com.questionnaire.db.service.IUserRolesService;
import com.questionnaire.db.service.IUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ahui
 * @since 2022-05-23
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    private final IUserRolesService userRolesService; // 用户角色 service

    private final RedisCacheTool cache; // redis工具

    private final UsersMapper usersMapper; // 用户mapper

    /**
     * 用户登录
     *
     * @param account  账户
     * @param password 密码
     * @param isAdmin  是否为管理员
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<Object> login(String account, String password, boolean isAdmin) {
        // MD5加盐加密
        var md5BySaltPassword = SaSecureUtil.md5BySalt(password, SaltStr.SALT);
        // 校验账号密码是否正确，并获取用户信息
        var users = lambdaQuery()
                .eq(Users::getAccount, account)
                .eq(Users::getPassword, md5BySaltPassword)
                .one();

        //判断该账号是否已被禁用
        if(ObjectUtil.isNotNull(users.getFlag()) && users.getFlag().equals(1)) {
            return Result.fail(FailEnum.ACCOUNT_IN_DISABLE);
        }
        // 账号不存在则返回提示
        if (ObjectUtil.isNull(users) || ObjectUtil.isEmpty(users)) {
            return Result.fail(FailEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        // 账号被封禁则返回提示
        if (StpUtil.isDisable(users.getUserId())) {
            return Result.fail(FailEnum.ACCOUNT_IN_DISABLE);
        }

        // 判断是否为管理员登录
        if (isAdmin) {
            //判断该用户是否为管理员
            if (!users.getUserType().equals(0) && !users.getUserType().equals(1)) {
                return Result.fail("无权限");
            }
        }
        // 会话登录 若配置文件中is-read-cookie配置项设为true时会自动通过Response将token回写到客户端的Cookie中.
        StpUtil.login(
                users.getUserId(), // 设置用户ID为登录ID
                SaLoginConfig.setExtra("nickName", users.getNickName()) // 自定义单个或多个额外信息 JWT Payload
        );
        //打印日志 用户token信息
        log.info(">>>>>>>>>>>>>>>>> 登录用户Token信息: {} >>>>>>>>>>>>>>>>>", StpUtil.getTokenInfo());
        // 设置用户登录IP与时间
        var updateLoginIpAndDate = lambdaUpdate()
                .set(Users::getLoginIp, NetUtil.getLocalhostStr())
                .set(Users::getLoginDate, LocalDateTime.now())
                .eq(Users::getUserId, users.getUserId())
                .update();
        if (!updateLoginIpAndDate) {
            log.error(">>>>>>>>>>>>>>>>> 设置用户登录IP与登录时间失败 >>>>>>>>>>>>>>>>>");
        }
        // 转换对象
        var usersVo = BeanConvertUtil.convert(users, UsersVo.class);
        //返回 参数
        return Result.ok(
                MapUtil.builder()
                        .put("tokenName", StpUtil.getTokenName())
                        .put("tokenValue", StpUtil.getTokenValue())
                        .put("userInfo", usersVo)
                        .build()
        );
    }

    /**
     * 用户注册
     *
     * @param users 用户信息
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public synchronized Result<Object> userRegister(Users users) {
        //加盐加密
        users.setPassword(SaSecureUtil.md5BySalt(users.getPassword(), SaltStr.SALT));
        //通过account查询用户是否存在
        var exists = lambdaQuery().eq(Users::getAccount, users.getAccount()).one();
        // 用户存在则返回提示
        if (StrUtil.isNotBlank(exists.getUserId())) {
            // 用户是否已被禁用
            if(ObjectUtil.isNotNull(exists.getFlag()) && exists.getFlag().equals(Flag.FLAG_DISABLE)){
                log.info(">>>>>>>>>>>>>>>>> 用户已被禁用 >>>>>>>>>>>>>>>>>");
                return Result.fail(FailEnum.ACCOUNT_IN_DISABLE);
            }
            log.info(">>>>>>>>>>>>>>>>> 用户已存在 >>>>>>>>>>>>>>>>>");
            return Result.fail(FailEnum.ACCOUNT_HAS_BEEN_REGISTER);
        }

        // 生成UserID
        users.setUserId(IdUtil.simpleUUID());
        // 若不传用户类型则默认为普通用户和普通角色
        var userRoleId = Role.ROLE_USER_ID;
        users.setUserType(Optional.ofNullable(users.getUserType()).orElse(2));
        // 为管理员则将用户角色设为管理员
        if (users.getUserType() == 1) {
            userRoleId = Role.ROLE_ADMIN_ID;
        }
        // 为用户生成默认昵称
        users.setNickName(Optional.ofNullable(users.getNickName())
                .orElse("默认昵称" + RandomUtil.randomString(5)));
        // 为用户设置默认头像
        users.setAvatar(Optional.ofNullable(users.getAvatar()).orElse("http://rlzg9o6wf.hn-bkt.clouddn.com/0fc7d20532fdaf769a25683617711png.png"));
        // 注册
        var insertStatus = save(users);
        // 验证注册是否成功
        if (!insertStatus) {
            log.error(">>>>>>>>>>>>>>>>> 注册失败 >>>>>>>>>>>>>>>>>");
            return Result.fail(FailEnum.ACCOUNT_REGISTER_FAIL);
        }
        // 设置用户角色
        userRolesService.bindUserRole(users.getUserId(), userRoleId);
        // 打印日志 用户注册信息
        log.info(">>>>>>>>>>>>>>>>> 注册成功: {} >>>>>>>>>>>>>>>>>", users);
        return Result.ok();
    }

    /**
     * 查询用户信息
     * @param userId 用户ID
     * @return users
     */
    public Result<Object> queryUserInfo(String userId) {
        // 判断当前查询的用户是否为查询自己而非通过userId去查询别的用户
        if (StpUtil.getLoginId().equals(userId)) {
            Users users = lambdaQuery().eq(Users::getUserId, userId).one();
            if (ObjectUtil.isNull(users)) {
                return Result.fail("用户不存在");
            }
            return Result.ok(users);
        }
        log.error(">>>>>>>>>>>>>>>>> 用户所查询的UserID与当前用户不一致 >>>>>>>>>>>>>>>>>");
        return Result.fail();
    }

    /**
     * 查询用户列表，可指定查询条件
     *
     * @param currentPage 当前页
     * @param pageSize    页面显示条数
     * @param condition   筛选条件
     * @return List<UsersVo> 筛选后的用户列表
     */
    @Override
    public Result<Object> queryUsersList(Integer currentPage, Integer pageSize, String condition) {
        // 分页查询配置 通过Java8 新增的Optional类来 判断参数是否为空 为空则使用默认值
        Page<Users> page = new Page<>(
                Optional.ofNullable(currentPage).orElse(1),
                Optional.ofNullable(pageSize).orElse(10)
        );
        //查询条件构造器
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        // 第一个布尔值表示启用筛选条件 第二个布尔值表示是否启用 ASC 升序排序 false表示采用 DESC 降序排序
        // 最后一个字段为排序字段 可以设置多个排序字段采用逗号分隔
        queryWrapper.orderBy(true, false, Users::getId);
        // 条件不为空时加入查询条件
        if (StrUtil.isNotBlank(condition)) {
            queryWrapper.like(true, Users::getNickName, condition);
        }
        // 保存查询结果
        page = usersMapper.selectPage(page, queryWrapper);
        // 深拷贝对象 转换为 UsersVo
        IPage<UsersVo> usersVoIPage = BeanConvertUtil.convertPage(page, UsersVo.class);
        if (ObjectUtil.isNull(usersVoIPage)) {
            return Result.fail();
        }
        return Result.ok(usersVoIPage);
    }

    /**
     * 修改用户信息
     *
     * @param newUsers 新的用户信息
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<Object> modifyingUserInfo(Users newUsers) {
        log.info("即将要更新的用户信息: {}", newUsers);

        // 保存更新前数据
        Users oldUser = lambdaQuery().eq(Users::getUserId, newUsers.getUserId()).one();

        // 更新数据
        var updateStatus = updateById(newUsers);
        if (!updateStatus) {
            log.info(">>>>>>>>>>>>>>>>> 更新失败 >>>>>>>>>>>>>>>>>");
            return Result.fail();
        }
        // 用户身份不等于空且产生变动
        if (ObjectUtil.isNotNull(newUsers.getUserType()) && !oldUser.getUserType().equals(newUsers.getUserType())) {
            var userRole = 0;
            // 判断本次要变更的身份
            switch (newUsers.getUserType()) {
                case 1: {
                    userRole = Role.ROLE_ADMIN_ID;
                    break;
                }
                case 2: {
                    userRole = Role.ROLE_USER_ID;
                    break;
                }
            }
            // 再加一层判断防止 非法数据
            if (userRole != 0) {
                // 删除数据库数据
                userRolesService.removeUserRoleByUserId(newUsers.getUserId());
                // 删除之前的身份缓存
                cache.hashDel(CacheKey.USER_ROLE_KEY, newUsers.getUserId());
                // 变更用户身份
                userRolesService.bindUserRole(newUsers.getUserId(), userRole);
            }
        }
        log.info(">>>>>>>>>>>>>>>>> 更新成功 >>>>>>>>>>>>>>>>>");
        return Result.ok();
    }

    /**
     * 校验并修改用户密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return Result<Object>
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<Object> modifyingUserPassword(String oldPassword, String newPassword) {
        // 校验参数
        if (StrUtil.isBlank(oldPassword) || StrUtil.isBlank(newPassword)) {
            log.info(">>>>>>>>>>>>>>>>> 参数为空 >>>>>>>>>>>>>>>>>");
            return Result.fail(FailEnum.PARAMETER_IS_NULL);
        }
        // 加密密码
        String md5OldPassword = SaSecureUtil.md5BySalt(oldPassword, SaltStr.SALT);
        String md5NewPassword = SaSecureUtil.md5BySalt(newPassword, SaltStr.SALT);
        // 查询
        Users users = lambdaQuery().eq(Users::getUserId, StpUtil.getLoginId().toString()).one();
        // 校验旧密码是否正确
        if (!users.getPassword().equals(md5OldPassword)) {
            return Result.fail(FailEnum.ACCOUNT_PASSWORD_IS_INCORRECT);
        }
        // 修改
        boolean status = lambdaUpdate().set(Users::getPassword, md5NewPassword).eq(Users::getUserId, StpUtil.getLoginId()).update();
        if (!status) {
            log.info(">>>>>>>>>>>>>>>>> 更新失败 >>>>>>>>>>>>>>>>>");
            return Result.fail();
        }
        return Result.ok();
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<Object> removeUserInfo(String userId) {
        // 执行
        var deleteStatus = lambdaUpdate()
                .set(true, Users::getFlag, Flag.FLAG_DISABLE)
                .eq(Users::getUserId, userId)
                .eq(Users::getFlag, Flag.FLAG_NORMAL).update();

        if (!deleteStatus) {
            log.info(">>>>>>>>>>>>>>>>> 删除用户失败，用户ID: {} >>>>>>>>>>>>>>>>>", userId);
            return Result.fail();
        }

        //删除完毕后需要清除该用户的token并将其踢下线
        StpUtil.logout(userId);

        log.info(">>>>>>>>>>>>>>>>> 删除用户成功，用户ID: {} >>>>>>>>>>>>>>>>>", userId);
        return Result.ok();
    }
}
