package com.questionnaire.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: ahui
 * @Description: TODO
 * @DateTime: 2022/12/7 - 09:24
 **/
@Slf4j
@Component
public class LocalStorageUtil {

    /**
     * http协议
     */
    private static final String HTTP = "http://";
    /**
     * https协议
     */
    private static final String HTTPS = "https://";

    /**
     * TODO 本地存储
     *
     * @param file       文件
     * @param folderPath 存放静态资源的文件夹
     * @param request    请求
     * @return String 访问路径
     * @throws IOException IO异常
     */
    public String localStorage(MultipartFile file, String folderPath, HttpServletRequest request) throws IOException {
        // 打印文件信息
        log.info("源文件: {} 文件大小: {}", file.getOriginalFilename(), file.getSize());
        // 项目相对路径
        ApplicationHome applicationHome = new ApplicationHome();
        String canonicalPath = applicationHome.getDir().getParentFile().getCanonicalPath();
        //检查并创建文件夹  /Users/ahui/WorkSpace/java_project/Questionnaire/files/
        var mkdir = FileUtil.mkdir(StrUtil.format("{}{}", canonicalPath, folderPath));
        // 新文件名 xxx.jpg
        var newFileName = StrUtil.format("{}.{}", IdUtil.simpleUUID(), FileNameUtil.getSuffix(file.getOriginalFilename()));
        //文件存储路径
        var filePath = StrUtil.format("{}/{}", mkdir.getPath(), newFileName);
        //写入文件到对应路径
        FileUtil.writeBytes(file.getBytes(), filePath);
        log.info(">>>>>>>>>>>>>>>>> 文件保存成功，PATH：{} >>>>>>>>>>>>>>>>>", filePath);
        //获取访问路径 localhost:8080/files/xxx.jpg
        var accessPath = StrUtil.format("{}{}:{}{}{}",
                HTTP, request.getServerName(), request.getServerPort(), folderPath, newFileName
        );
        log.info("访问路径: {}", accessPath);
        return accessPath;
    }


}
