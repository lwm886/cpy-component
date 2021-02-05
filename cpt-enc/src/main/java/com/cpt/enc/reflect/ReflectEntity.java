package com.cpt.enc.reflect;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author lw
 * @since 2021/2/4
 **/
@Data
public class ReflectEntity {
    private Object object;
    private Field field;
    private String encType;

    public ReflectEntity(Object object, Field field, String encType) {
        this.object = object;
        this.field = field;
        this.encType=encType;
    }
}
