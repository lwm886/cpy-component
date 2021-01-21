package com.cpt.enc.test;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lw
 * @since 2021/1/11
 **/
@Slf4j
@RequestMapping("/goods")
@RestController
public class GoodsController {

    @GetMapping("/query")
    public List<Goods> query(@RequestParam String param){
        Goods goods = new Goods();
        goods.setIdCard("100111191199010911");
        goods.setPhone("15510001111");
        goods.setSn("SN100000001");
        return Lists.newArrayList(goods);
    }
    @PostMapping("/save")
    public List<Goods> query(@RequestBody Goods goods){
        log.info("goods::{}",goods);
        return Lists.newArrayList(goods);
    }

}
