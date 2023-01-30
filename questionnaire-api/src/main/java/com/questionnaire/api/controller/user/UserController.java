package com.questionnaire.api.controller.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.questionnaire.common.aspect.annotation.LimitRequestFrequency;
import com.questionnaire.common.core.result.FailEnum;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.entity.Users;
import com.questionnaire.db.excellistener.UserExcelListener;
import com.questionnaire.db.service.IUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: ahui
 * @Description: 用户信息Controller
 * @DateTime: 2022/5/25 - 14:37
 **/
@Slf4j
@Tag(name = "用户模块")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class UserController {
    private final IUsersService usersService; // 用户 Service

    /**
     * 用户信息列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示条数
     * @return List<Users> 用户列表
     */
    @Parameters(value = {
            @Parameter(name = "currentPage", description = "当前页"),
            @Parameter(name = "pageSize", description = "显示条数")
    })
    @Operation(summary = "查询用户列表", description = "查询用户信息并以分页列表展示")
    @LimitRequestFrequency(count = 10)
    @GetMapping("/list/{currentPage}/{pageSize}/{condition}")
    public Result<Object> queryUserList(
            @PathVariable(required = false) Integer currentPage,
            @PathVariable(required = false) Integer pageSize,
            @PathVariable String condition) {

        return usersService.queryUsersList(currentPage, pageSize, condition);
    }

    /**
     * 获取单个用户信息
     *
     * @param userId 用户id
     * @return Result
     */
    @Parameter(name = "userId", description = "用户ID", required = true)
    @Operation(summary = "查询用户信息", description = "查询单个用户详情信息")
    @LimitRequestFrequency
    @GetMapping("/get/{userId}")
    public Result<Object> queryUserInfo(@PathVariable String userId) {

        return usersService.queryUserInfo(userId);
    }

    /**
     * 修改密码
     *
     * @return result
     */
    @Parameter(name = "parameter", description = "需要修改的密码与旧密码")
    @Operation(summary = "修改用户密码")
    @PutMapping("/update/password")
    public Result<Object> queryUserPassword(@RequestBody Map<String, String> parameter) {

        return usersService.modifyingUserPassword(parameter.get("oldPassword"), parameter.get("newPassword"));
    }

    /**
     * 更新用户信息
     *
     * @param users 用户信息
     * @return Result
     */
    @Parameter(name = "users", description = "用户对象实体", required = true)
    @Operation(summary = "更新用户信息", description = "更新用户信息")
    @LimitRequestFrequency
    @PutMapping("/update")
    public Result<Object> modifyUserInfo(@RequestBody Users users) {

        if (StrUtil.isNotBlank(users.getPhoneNumber()) && users.getPhoneNumber().length() > 11) {
            return Result.fail(FailEnum.PHONE_NUMBER_INCORRECT);
        }

        return usersService.modifyingUserInfo(users);
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @return Result
     */
    @Parameter(name = "userId", description = "用户ID", required = true)
    @Operation(summary = "删除用户")
    @LimitRequestFrequency
    @DeleteMapping("/delete/{userId}")
    public Result<Object> removeUserInfo(@PathVariable String userId) {
        return usersService.removeUserInfo(userId);
    }

    /**
     * 导入用户excel
     *
     * @param file 文件
     * @return Result
     */
    @Parameter(name = "file", description = "文件", required = true)
    @Operation(summary = "导入用户信息", description = "通过excel表导入用户信息")
    @LimitRequestFrequency
    @PostMapping("/importExcel")
    public Result<Object> importExcel(@NotNull MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.fail(">>>>>>>>>>>>>>>>> 文件为空 >>>>>>>>>>>>>>>>>");
        }
        //读
        EasyExcel.read(
                file.getInputStream(),
                Users.class,
                new UserExcelListener(usersService)
        ).sheet().autoTrim(true).doRead();
        return Result.ok();
    }

    /**
     * 导出用户excel
     *
     * @param response 响应
     * @return Result
     */
    @Operation(summary = "导出用户信息", description = "以excel表的方式导出用户信息")
    @GetMapping("/exportExcel")
    public void exportUserExcel(HttpServletResponse response, String fileName) throws IOException {
        // 处理中文乱码
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        //设置响应内容文件类型
        response.setContentType("application/vnd.ms-excel"); // 改成输出excel文件
        response.setCharacterEncoding("utf-8");
        // 添加响应头信息
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx"); //设置头
        response.setHeader("Cache-Control", "No-cache");//不缓存
        response.setHeader("Pragma", "No-cache");

        // 写
        EasyExcel.write(response.getOutputStream(), Users.class).sheet("用户信息").doWrite(usersService.lambdaQuery().list());
        log.info(">>>>>>>>>>>>>>>>> 导出成功 >>>>>>>>>>>>>>>>>");
    }

}
