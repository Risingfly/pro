package com.genge.demo.configuration;

import com.genge.demo.interceptor.LoginRequiredInterceptor;
import com.genge.demo.interceptor.MyInterceptor;
import com.genge.demo.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
@Configuration
public class TouTiaoWebConfiguration implements WebMvcConfigurer{
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    MyInterceptor myInterceptor;

    /**
     * 此处真正注册拦截器bean,直接在拦截器类上加@Component,
     * 不能真正注入bean
     * @return
     */
    @Bean
    public PassportInterceptor passportInterceptor(){
        return new PassportInterceptor();
    }

    /**
     * 此处真正注册拦截器bean,直接在拦截器类上加@Component,
     * 不能真正注入bean
     * @return
     */
    @Bean
    public LoginRequiredInterceptor loginRequiredInterceptor(){
        return new LoginRequiredInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("注册拦截器开始");
        registry.addInterceptor(loginRequiredInterceptor()).addPathPatterns("/setting*");
        registry.addInterceptor(passportInterceptor());
    }
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        System.out.println("注册拦截器开始");
//        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
//        registry.addInterceptor(passportInterceptor);
//        super.addInterceptors(registry);
//    }

}
