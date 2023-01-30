package com.questionnaire.db.excellistener;

import com.questionnaire.db.entity.Users;
import com.questionnaire.db.service.IUsersService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @Author: ahui
 * @Description: 用户信息导出导入excel监听器
 * @DateTime: 2022/5/30 - 16:33
 **/
@Slf4j
public class UserExcelListener implements ReadListener<Users> {

    /**
     * 每隔100条 清理list 方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    private final List<Users> cacheDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private final IUsersService userInfoService;

    public UserExcelListener (IUsersService service) {
        this.userInfoService = service;
    }

    @Override
    public void invoke(Users users, AnalysisContext analysisContext) {
        log.info("解析到一条数据: {}", JSON.toJSONString(users));
        cacheDataList.add(users);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if(cacheDataList.size() == BATCH_COUNT) {
            saveData();
            // 存储完清理 list
            cacheDataList.clear();
        }
    }

    /**
     * 处理完之后都会调用该方法
     * @param analysisContext context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    public void saveData() {
        log.info("{}条数据，开始存储数据库！", cacheDataList.size());
        userInfoService.saveBatch(cacheDataList);
    }


}
