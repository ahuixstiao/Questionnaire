package com.questionnaire.common.core.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisCacheTool {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 读取缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public void put(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入缓存 并设置时效时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间(单位 秒)
     * @return true成功 false失败
     */
    public void put(String key, Object value, Long expireTime) {
        try {
            redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     */
    public void hashPut(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建，并添加过期时间
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param expireTime  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public void hashPut(String key, String item, Object value, long expireTime) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hashGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hashGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * HashDelete
     * @param key 键
     * @param item 项
     * @return 影响条数
     */
    public long hashDel(String key, String item) {
        return redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断某个hash项是否存在
     * @param key 键
     * @param item 项
     * @return boolean
     */
    public boolean hashExists(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    // ========================= 其他 =========================

    /**
     * 指定缓存失效时间
     *
     * @param key        键
     * @param expireTime 时间(单位 秒)
     * @return true成功 false失败
     */
    public void expire(String key, Long expireTime) {
        try {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存长度
     *
     * @return 缓存长度
     */
    public long size() {
        try {
            return redisTemplate.getConnectionFactory().getConnection().dbSize();
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}
