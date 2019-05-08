package com.genge.demo.configuration;

import com.genge.demo.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Component
public class TouTiaoWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassportInterceptor passportInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
    }
}
