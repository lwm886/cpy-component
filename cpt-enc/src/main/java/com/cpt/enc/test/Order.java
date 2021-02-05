package com.cpt.enc.test;

import com.cpt.enc.reflect.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author lw
 * @since 2021/2/5
 **/
@Data
public class Order {

    private String id;

    @Desensitized(mode = Mode.PHONE)
    private String phone="150100110001";

    private List<Goods> goodsList;

    private Map<String,Object> detailMap;
}
