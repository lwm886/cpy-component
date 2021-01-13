package com.cpt.tenant.core;

/**
 * @author lw
 * @since 2021-01-11
 */
public enum TenantMode {
    /**
     * 手动模式
     */
    NONE,

    /**
     * JOB参数模式
     */
    JOB,

    /**
     * 配置文件模式
     */
    CONFIG;
}
