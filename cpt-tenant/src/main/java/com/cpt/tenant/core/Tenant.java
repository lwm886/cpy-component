package com.cpt.tenant.core;

import java.lang.annotation.*;

/**
 * @author lw
 * @since 2021/1/11
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface Tenant {
    TenantMode value() default TenantMode.JOB;
}
