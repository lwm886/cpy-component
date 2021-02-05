package com.cpt.enc.core;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.lang.Boolean.TRUE;

/**
 * @author lw
 * @since 2021/2/2
 **/
@Slf4j
@Component
@Aspect
public class DesensitizedAspect {

    @Pointcut("@annotation(com.cpt.enc.core.EnableDesensitized)")
    public void point() {
        log.debug("point#==================== point init");
    }

    @Around("point()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Exception {
        Object[] args = joinPoint.getArgs();
        Object res;
        try {
            res = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            log.error("doAround#==================== error", throwable);
            throw new Exception();
        }
        if (Objects.nonNull(res)) {
            SerializerUsed.activeCache.set(TRUE);
            String serializerOut = SerializerUsed.getSerializerOut(res);
            return JSON.parseObject(serializerOut, res.getClass());
        }
        SerializerUsed.clear();
        return null;
    }
}
