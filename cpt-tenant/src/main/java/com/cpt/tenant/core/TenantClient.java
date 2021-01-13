package com.cpt.tenant.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lw
 * @since 2021/1/11
 **/
@Component
public class TenantClient {

    @Autowired
    private TenantAspect tenantAspect;


    /**
     * 查询租户ID
     * @return Long
     */
    public Long queryTenantId(){
        return tenantAspect.tenantIdMap.get();
    }

    /**
     * 请求过滤
     * @return boolean
     */
    public boolean isFiltered() {
        return tenantAspect.tenantModeMap.get() == TenantMode.NONE;
    }
}
