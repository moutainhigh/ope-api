package com.xianjinxia.conf;

import com.xianjinxia.filter.GateWayInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AdapterConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private GateWayInterceptor gateWayInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        registry.addInterceptor(gateWayInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}