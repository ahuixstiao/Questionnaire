package com.questionnaire.common.utils;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: ahui
 * @Description: 七牛云文件上传工具
 * @DateTime: 2022/11/12 - 12:15
 **/
@Slf4j
@Component
public class KodoStorageUtil {
    @Value("${qiniuoss.accessKey}")
    private String accessKey; // 访问密钥
    @Value("${qiniuoss.secretKey}")
    private String secretKey; // 密钥
    @Value("${qiniuoss.bucketName}")
    private String bucketName;
    @Value("${qiniuoss.domain}")
    private String domain;


    /**
     * TODO 七牛云对象云存储
     * @param file 文件
     * @return URL
     * @throws IOException
     */
    public String ossStorage(MultipartFile file) throws IOException {
        // 1、构建上传配置 华南地区
        Configuration config = new Configuration(Region.huanan());
        // 指定分片上传版本
        config.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        // 请求协议为https
        config.useHttpsDomains = false;
        // 2、构建上传管理器
        UploadManager uploadManager = new UploadManager(config);
        // 3、认证
        Auth auth = Auth.create(accessKey, secretKey);
        // 获取对应对象存储空间的令牌
        String uploadToken = auth.uploadToken(bucketName);
        // 文件名
        String fileName = StrUtil.format("{}.{}", IdUtil.simpleUUID(), FileNameUtil.getSuffix(file.getOriginalFilename()));
        // 4、通过字节码方式上传文件
        Response response = uploadManager.put(file.getBytes(), fileName, uploadToken);
        // 5、解析上传结果
        DefaultPutRet defaultPutRet = new DefaultPutRet();
        try {
            // 5、解析上传结果
            defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException qiniuException) {
            Response re = qiniuException.response;
            log.error(re.toString());
        }
        // 构建文件访问URL
        DownloadUrl url = new DownloadUrl(domain, false, defaultPutRet.key);
        // 通过指定文件名来让用户访问URL时是下载而不是预览
        //url.setAttname(fileName);
        // 生成一张480x360大小的视频第一帧略缩图
        //url.setFop("vframe/jpg/offset/1/w/480/h/360");
        // 生成一张480x320大小的缩略图
        url.setFop("imageView2/2/w/480/h/480");
        log.info("访问路径: {}",url.buildURL());
        // 返回访问URL
        return url.buildURL();
    }

    /**
     * 删除文件
     * @param fileUrl 文件链接
     * @throws QiniuException 七牛云异常
     */
    public void ossDeleteFile(String fileUrl) throws QiniuException {
        // 切割文件名
        String key = fileUrl.split(domain + "/")[1].split("\\?")[0];
        // 1、构建配置 华南地区
        Configuration config = new Configuration(Region.huanan());
        // 请求协议为https
        config.useHttpsDomains = false;
        // 2、认证
        Auth auth = Auth.create(accessKey, secretKey);
        // 3、构建Bucket管理器
        BucketManager bucketManager = new BucketManager(auth, config);
        // 4、删除文件
        bucketManager.delete(bucketName, key);
    }

}
