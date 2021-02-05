package com.cpt.enc.reflect;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author lw
 * @since 2021/2/4
 **/
public class TypeUtils {

    private static final List<String> baseTypeList = Lists.newArrayList(
            "byte", "short", "int", "long", "double", "float", "boolean",
            "class java.lang.Byte", "class java.lang.Short", "class java.lang.Long",
            "class java.lang.Integer", "class java.lang.Double", "class java.lang.Float",
            "class java.lang.Boolean", "class java.lang.String", "class java.time.LocalDateTime",
            "class java.time.LocalDate", "class java.time.LocalTime", "class java.util.Date", "class java.sql.Date",
            "class java.math.BigDecimal"
    );


    private static final List<String> basePkgList=Lists.newArrayList("class com.cpt.enc");

    private static final List<String> arrList=Lists.newArrayList("java.util.LinkedList","java.util.ArrayList","java.util.List");

    private static final List<String> mapList=Lists.newArrayList("java.util.Map","java.util.HashMap");

    /**
     * 基本类型
     * @param type
     * @return
     */
    public static boolean isBaeType(String type){
        return baseTypeList.contains(type);
    }

    /**
     * 自定义对象类型
     * @param type
     * @return
     */
    public static boolean isObjectType(String type){
        for(String pkg:basePkgList){
            if(type.contains(pkg)){
                return true;
            }
        }
        return false;
    }

    /**
     * List类型
     * @param type
     * @return
     */
    public static boolean isListType(String type){
        for(String arrType:arrList){
            if(type.contains(arrType)){
                return true;
            }
        }
        return false;
    }

    /**
     * Map类型
     * @param type
     * @return
     */
    public static boolean isMapType(String type){
        for(String arrType:mapList){
            if(type.contains(arrType)){
                return true;
            }
        }
        return false;
    }
}
