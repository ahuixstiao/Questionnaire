package com.questionnaire.db.handler;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: ahui
 * @Description: MybatisPlus 字段填充处理器
 * @DateTime: 2022/10/15 - 18:22
 **/
@Slf4j
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(">>>>>>>>>>>>>>>>> 开始插入填充字段 >>>>>>>>>>>>>>>>>");
        String createdBy;
        if(StpUtil.isLogin()){
            createdBy = StpUtil.getExtra("nickName").toString();
        }else {
            createdBy = "default";
        }
        // 创建人
        this.strictInsertFill(metaObject, "createdBy", () -> createdBy, String.class);
        // 创建时间
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime::now, LocalDateTime.class);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(">>>>>>>>>>>>>>>>> 开始更新填充字段 >>>>>>>>>>>>>>>>>");
        // 此处获取的是当前登录用户的昵称 或 管理员的昵称
        String updatedBy;
        if(StpUtil.isLogin()){
            updatedBy = StpUtil.getExtra("nickName").toString();
        }else {
            updatedBy = "default";
        }
        // 修改人
        this.strictUpdateFill(metaObject, "updatedBy", () -> updatedBy, String.class);
        // 修改时间
        this.strictUpdateFill(metaObject, "updatedTime", LocalDateTime::now, LocalDateTime.class);
    }
}
