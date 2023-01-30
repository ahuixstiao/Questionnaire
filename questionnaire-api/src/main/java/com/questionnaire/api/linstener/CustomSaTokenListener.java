package com.questionnaire.api.linstener;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: ahui
 * @Description: 自定义Sa-Token监听器
 * @DateTime: 2022/11/9 - 18:29
 **/
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class CustomSaTokenListener implements SaTokenListener {

    /**
     * 每次登录时触发
     *
     * @param loginType  账号类别
     * @param loginId    账号id
     * @param tokenValue 本次登录产生的 token 值
     * @param loginModel 登录参数
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {

    }

    /**
     * 每次注销时触发
     *
     * @param loginType  账号类别
     * @param loginId    账号id
     * @param tokenValue token值
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {

    }

    /**
     * 每次被踢下线时触发
     *
     * @param loginType  账号类别
     * @param loginId    账号id
     * @param tokenValue token值
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {

    }

    /**
     * 每次被顶下线时触发
     *
     * @param loginType  账号类别
     * @param loginId    账号id
     * @param tokenValue token值
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {

    }

    /**
     * 每次被封禁时触发
     *
     * @param loginType   账号类别
     * @param loginId     账号id
     * @param service     指定服务
     * @param level       封禁等级
     * @param disableTime 封禁时长，单位: 秒
     */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {

    }

    /**
     * 每次被解封时触发
     *
     * @param loginType 账号类别
     * @param loginId   账号id
     * @param service   指定服务
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {

    }

    /**
     * 每次创建Session时触发
     *
     * @param id SessionId
     */
    @Override
    public void doCreateSession(String id) {

    }

    /**
     * 每次注销Session时触发
     *
     * @param id SessionId
     */
    @Override
    public void doLogoutSession(String id) {

    }

    /**
     * 每次Token续期时触发
     *
     * @param tokenValue token 值
     * @param loginId    账号id
     * @param timeout    续期时间
     */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {

    }


    @Override
    public void doOpenSafe(String s, String s1, String s2, long l) {

    }

    @Override
    public void doCloseSafe(String s, String s1, String s2) {

    }
}
