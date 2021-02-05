package com.cpt.enc.reflect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cpt.enc.test.Detail;
import com.cpt.enc.test.Goods;
import com.cpt.enc.test.Order;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 对象遍历加密处理（支持像List<BO> Map<BO>等复杂对象属性）
 * @author lw
 * @since 2021/2/4
 **/
@Slf4j
public class Reflector {

    /**
     * 解析对象类型
     *
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static List<ReflectEntity> getSingleReflectEntity(Object object) throws IllegalAccessException {
        List<ReflectEntity> list = Lists.newArrayList();
        Field[] fields = getFields(object);
        if (fields == null) {
            return Lists.newArrayList();
        }
        for (Field field : fields) {
            field.setAccessible(true);
            String type = field.getGenericType().toString();

            if (TypeUtils.isBaeType(type)) {
                //基本类型
                if ("class java.lang.String".equals(type)) {
                    if (field.isAnnotationPresent(Desensitized.class)) {
                        list.add(new ReflectEntity(object, field, field.getAnnotation(Desensitized.class).mode()));
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } else if (TypeUtils.isObjectType(type)) {
                //自定义对象类型
                Object co = field.get(object);
                List<ReflectEntity> cList = getSingleReflectEntity(co);
                if (!CollectionUtils.isEmpty(cList)) {
                    list.addAll(cList);
                }
            } else if (TypeUtils.isListType(type)) {
                //List类型
                Object lo = field.get(object);
                if (Objects.isNull(lo)) {
                    continue;
                }
                List listObject = (List) lo;
                for (Object o : listObject) {
                    List<ReflectEntity> eList = getSingleReflectEntity(o);
                    if (!CollectionUtils.isEmpty(eList)) {
                        list.addAll(eList);
                    }
                }
            }else if(TypeUtils.isMapType(type)){
                //Map类型
                Object mo=field.get(object);
                if (Objects.isNull(mo)) {
                    continue;
                }
                Map mapObject = (Map) mo;
                Collection values = mapObject.values();
                for(Object co:values){
                    List<ReflectEntity> eList = getSingleReflectEntity(co);
                    if (!CollectionUtils.isEmpty(eList)) {
                        list.addAll(eList);
                    }
                }
            }

        }
        return list;
    }

    /**
     * 加密单元
     *
     * @param list
     * @throws IllegalAccessException
     */
    public static void encReflectEntity(List<ReflectEntity> list) throws IllegalAccessException {
        if (CollectionUtils.isEmpty(list)) {
            log.debug("encReflectEntity#====================== ReflectEntity is empty");
            return;
        }
        for (ReflectEntity reflectEntity : list) {
            Object object = reflectEntity.getObject();
            Field field = reflectEntity.getField();
            field.setAccessible(true);
            String encType = reflectEntity.getEncType();
            Object originVal = field.get(object);
            if (Objects.nonNull(originVal)) {
                field.set(object, originVal + "-" + encType+"被编辑");
            }
        }
    }

    private static Field[] getFields(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        Field[] declaredFields = object.getClass().getDeclaredFields();
        if (declaredFields == null || declaredFields.length == 0) {
            return null;
        }
        return declaredFields;
    }

    public static void main(String[] args) throws IllegalAccessException {
        Detail d1 = new Detail();
        Goods g1 = new Goods();
        g1.setGoodsDetail(Lists.newArrayList(d1));

        Order order = new Order();
        order.setId("1");
        order.setGoodsList(Lists.newArrayList(g1));

        Map map=new HashMap();
        Detail d3 = new Detail();
        map.put("goods",d3);
        order.setDetailMap(map);

        log.info("origin user++++++++:{}", JSON.toJSONString(order, SerializerFeature.PrettyFormat));
        List<ReflectEntity> list = getSingleReflectEntity(order);
        encReflectEntity(list);
        log.info("edited user++++++++:{}", JSON.toJSONString(order, SerializerFeature.PrettyFormat));
    }
}
