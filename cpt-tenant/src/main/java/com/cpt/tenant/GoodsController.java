package com.cpt.tenant;

import com.cpt.tenant.core.Tenant;
import com.cpt.tenant.core.TenantMode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lw
 * @since 2021/1/11
 **/
@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Tenant(TenantMode.CONFIG)
    @GetMapping("/query")
    public String query(@RequestParam String param){
        return "test tenant";
    }

}
