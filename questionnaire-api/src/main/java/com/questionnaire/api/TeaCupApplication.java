package com.questionnaire.api;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 启动类
 * @author ahui
 */
@MapperScan("com.questionnaire.db.mapper") //扫描Mapper路径
//@EsMapperScan("com.questionnaire.db.esmapper") // 扫描 EsMapper路径
@EnableTransactionManagement //启用事务管理
@SpringBootApplication(scanBasePackages = {"com.questionnaire.api", "com.questionnaire.db", "com.questionnaire.gen", "com.questionnaire.common"})
public class TeaCupApplication {

    public static void main(String[] args) {
        disableWarning();
        SpringApplication.run(TeaCupApplication.class, args);
        System.out.println("========================= SA-TOKEN STARTER SUCCESS =========================");
        System.out.println(SaManager.getConfig());

        System.out.println("========================= TeaCupApplication START SUCCESS =========================");
    }

    // 关闭警告
    public static void disableWarning() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore
        }
    }
}
