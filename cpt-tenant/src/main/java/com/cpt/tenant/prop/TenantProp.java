package com.cpt.tenant.prop;

import lombok.Data;

/**
 * @author lw
 * @since 2021/1/11
 **/
@Data
public class TenantProp {
    /**
     * taskId
     */
    private Long taskId;

    /**
     * tenantId
     */
    private Long tenantId;
}
