package com.questionnaire.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ahui
 * @Description: 实体类转换器
 * @DateTime: 2022/11/11 - 10:38
 **/
@SuppressWarnings("all")
public class BeanConvertUtil {

    /**
     * Dto ,Vo ,Entity 相互转换
     * 同：BeanUtils.copyProperties(dtoEntity, newInstance);
     * @param source    元对象 --Dto，Vo，Entity
     * @param target      新对象 --Dto，Vo，Entity
     * @return 新对象实例
     * @throws Exception
     */
    public static <T> T convert(Object source, Class<T> target) {
        if (ObjectUtil.isNull(source)){
            return null;
        }
        // 获取将被转换类的实例
        T newInstance = null;
        try {
            // 创建实例
            newInstance = target.getDeclaredConstructor().newInstance();
            // 复制对象
            BeanUtils.copyProperties(source, newInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newInstance;
    }

    /**
     * 转换List集合对象
     * @param sourceList 集合
     * @param clazz 目标类
     * @return List<T>
     */
    public static <T> List<T> convertList(List<T> sourceList, Class<T> clazz) {
        List<T> targetList = new ArrayList<>();
        for (T source : sourceList) {
            targetList.add(convert(source, clazz));
        }
        return targetList;
    }

    /**
     * Page<--Dto，Vo，Entity> 分页对象转 Page<--Dto，Vo，Entity>
     * @param page 元分页对象
     * @param newClass 新的泛型类型 --Dto，Vo，Entity
     * @return newPage<Vo>
     */
    public static <T, E> IPage<E> convertPage(Page<T> page, Class<E> newClass) {
        IPage<E> convert = page.convert(item -> {
            try {
                // 获取将被转换类的实例
                E newVo = convert(item, newClass);
                return newVo;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return convert;
    }

}
