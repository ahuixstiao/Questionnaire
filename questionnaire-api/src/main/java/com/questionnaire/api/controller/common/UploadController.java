package com.questionnaire.api.controller.common;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.questionnaire.common.aspect.annotation.LimitRequestFrequency;
import com.questionnaire.common.utils.KodoStorageUtil;
import com.questionnaire.common.core.result.FailEnum;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.common.utils.LocalStorageUtil;
import com.questionnaire.db.entity.Users;
import com.questionnaire.db.service.IUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: ahui
 * @Description: 文件上传下载控制器
 * @DateTime: 2022/5/28 - 11:27
 **/
@Slf4j
@Tag(name = "文件模块")
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@RestController
@RequestMapping("/common/file")
public class UploadController {

    /**
     * 文件夹名称
     */
    @Value("${web.static.folderPath}")
    private String folderPath;

    private final KodoStorageUtil kodoStorageUtil; // 七牛云文件上传工具

    private final LocalStorageUtil localStorageUtil; // 本地文件上传工具

    private final IUsersService usersService; // 用户 service

    /**
     * 本地上传
     *
     * @param file 文件
     * @return 上传结果
     * @throws IOException IO异常
     */
    @Operation(summary = "本地上传")
    @Parameter(name = "file", description = "上传的文件")
    @LimitRequestFrequency
    @PutMapping(value = "/localUpload")
    public Result<Object> localUpload(MultipartFile file, HttpServletRequest request) throws IOException {

        if (file.isEmpty() || ObjectUtil.isNull(file)) {
            return Result.fail(FailEnum.FILE_IS_EMPTY);
        }

        // 获取访问路径
        var accessPath = localStorageUtil.localStorage(file, folderPath, request);
        // 已登录且访问路径有返回
        if(StrUtil.isNotBlank(accessPath)){
            // 保存头像
            var uploadUserAvatar = usersService.lambdaUpdate()
                    .set(Users::getAvatar, accessPath)
                    .eq(Users::getUserId, StpUtil.getLoginId().toString()).update();
            if (uploadUserAvatar) {
                // 返回 URL
                return Result.ok(MapUtil.builder().put("avatarUrl", accessPath).build());
            }
        }else {
            // 返回提示未登录
            return Result.fail(FailEnum.NOT_LOGIN);
        }
        return Result.fail(FailEnum.FILE_UPLOAD_FAIL);
    }

    /**
     * 对象云存储上传
     * @param file
     * @return url
     */
    @Operation(summary = "对象云存储上传", description = "采用七牛云来作为云存储")
    @Parameter(name = "file", description = "上传的文件")
    @LimitRequestFrequency
    @PutMapping("/ossUpload")
    public Result<Object> ossUpload(MultipartFile file) throws IOException {
        // 获取访问路径
        var accessUrl = kodoStorageUtil.ossStorage(file);
        // 保存头像
        var uploadUserAvatar = usersService.lambdaUpdate()
                .set(Users::getAvatar, accessUrl)
                .eq(Users::getUserId, StpUtil.getLoginId().toString())
                .update();
        // 校验
        if (uploadUserAvatar) {
            // 返回 URL
            return Result.ok(MapUtil.builder().put("avatarUrl", accessUrl).build());
        }

        return Result.fail(FailEnum.FILE_UPLOAD_FAIL);
    }
}
