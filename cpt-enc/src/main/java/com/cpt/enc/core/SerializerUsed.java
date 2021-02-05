package com.cpt.enc.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.LabelFilter;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import static java.lang.Boolean.TRUE;

/**
 * @author lw
 * @since 2021/2/1
 **/
@Slf4j
public class SerializerUsed implements ObjectSerializer {

    /**
     * 手机号
     */
    private static final String MOBILE = "mobile";
    /**
     * 身份证号
     */
    private static final String ID_NO = "idNo";
    /**
     * 本地缓存区-类型
     */
    static ThreadLocal<String> typeCache = new InheritableThreadLocal<>();
    /**
     * 本地缓存区-激活状态位
     */
    static ThreadLocal<Boolean> activeCache = new InheritableThreadLocal<>();


    @Override
    public void write(JSONSerializer serializer, Object value, Object fieldName, Type type, int i) throws IOException {
        try {
            String desType = typeCache.get();
            log.debug("write#=================== print param type:{},fieldName:{},value:{},desType:{}", type, fieldName, value, desType);
            String stringType = "class java.lang.String";
            if (TRUE.equals(activeCache.get()) && stringType.equals(type.toString()) && Objects.nonNull(value)) {
                if (MOBILE.equals(desType)) {
                    value = DesensitizedUtils.mobilePhone(value.toString());
                } else if (ID_NO.equals(desType)) {
                    value = DesensitizedUtils.idCardNum(value.toString());
                }
            }
            serializer.write(value);
        } catch (Exception e) {
            log.warn("write#=================== write error ", e);
            serializer.write(value);
        }
    }

    /**
     * 清理缓存区
     */
    public static void clear() {
        typeCache.remove();
        activeCache.remove();
        log.debug("clear#=================== clear cache success");
    }

    /**
     * 序列化设置
     * @param object
     * @return
     */
    static String getSerializerOut(Object object) {
        return JSON.toJSONString(object, (LabelFilter) s -> {
            if (!StringUtils.isEmpty(s)) {
                typeCache.set(s);
                log.debug("getSerializerOut#=================== write type cache success");
            }
            return true;
        }, SerializerFeature.WriteMapNullValue);
    }
}
