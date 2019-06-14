package com.genge.focus.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;
@Component
@Aspect
public class LogAspect {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.genge.focus.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for (Object arg:
             joinPoint.getArgs()) {
            sb.append("arg:"+arg.toString()+"|");
        }
        LOGGER.info("BEGIN TIME:"+new Date());
        LOGGER.info("before Method:" + sb.toString());
    }
    @After("execution(* com.genge.focus.controller.*Controller.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        LOGGER.info("after Method:666");
    }
}
