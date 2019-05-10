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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@Component
public class TouTiaoWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        registry.addInterceptor(passportInterceptor);

    }

}
