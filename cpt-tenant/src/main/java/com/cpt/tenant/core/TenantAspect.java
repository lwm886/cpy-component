package com.cpt.tenant.core;

import com.cpt.tenant.prop.TenantRegisterMap;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class TenantAspect {
    public final InheritableThreadLocal<Long> tenantIdMap = new InheritableThreadLocal<>();
    public final InheritableThreadLocal<TenantMode> tenantModeMap = new InheritableThreadLocal<>();
    public static final String SP_TEXT = ",";
    public static final int ZERO = 0;
    public static final int ONE = 1;

    @Autowired
    private TenantRegisterMap tenantRegisterMap;

    @Pointcut("@annotation(com.cpt.tenant.core.Tenant)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Tenant tenantAnnotation = method.getAnnotation(Tenant.class);
        if(tenantAnnotation.value()==TenantMode.NONE){
            tenantModeMap.set(TenantMode.NONE);
            log.debug("TENANT MODE : NONE");
            return;
        }else if(tenantAnnotation.value()==TenantMode.JOB){
            tenantModeMap.set(TenantMode.JOB);
            Object[] args = joinPoint.getArgs();
            if(Objects.nonNull(args) && args.length>ZERO){
                String[] array = args[ZERO].toString().split(SP_TEXT);
                tenantIdMap.set(Long.valueOf(array[array.length-ONE]));
            }
            log.debug("TENANT MODE : JOB : {}",tenantIdMap.get());
            return;
        }else if(tenantAnnotation.value()==TenantMode.CONFIG){
            Thread thread = Thread.currentThread();
            Class clazz=Class.forName("com.xxl.job.core.thread.JobThread");
            Field jobIdFeild = clazz.getField("jobId");
            int taskId = (int)jobIdFeild.get(thread);
            log.debug("TENANT MODE : CONFIG : TASK ID : {}",taskId);
            log.debug("TENANT MODE : CONFIG : REGISTER MAP : {}",tenantRegisterMap.getTenantRegisterMap());
            Long tenantId = tenantRegisterMap.getTenantRegisterMap().get(taskId);
            Objects.requireNonNull(tenantId);
            tenantIdMap.set(tenantId);
        }else{
            log.warn("SET TENANT MODE ERROR");
        }
    }

    @AfterReturning(returning = "response",pointcut = "pointCut()")
    public void doAfterReturning(Object response) {
        clear();
    }

    @AfterThrowing(throwing="ex", pointcut="pointCut()")
    public void doAfterThrowing(Throwable ex) {
        clear();
    }

    private void clear(){
        tenantIdMap.remove();
        tenantModeMap.remove();
        log.debug("TENANT MAP RELEASED");
    }
}
