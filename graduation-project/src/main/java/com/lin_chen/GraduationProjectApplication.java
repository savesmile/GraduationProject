package com.lin_chen;

import com.lin_chen.filter.AccessTokenVerifyInterceptor;
import com.lin_chen.filter.TokenVerifyProperties;
import com.lin_chen.filter.UserIdMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@SpringBootApplication
@Import(TokenVerifyProperties.class)
public class GraduationProjectApplication extends WebMvcConfigurationSupport {

    public static void main(String[] args) {
        SpringApplication.run(GraduationProjectApplication.class, args);
    }

    @Autowired
    AccessTokenVerifyInterceptor accessTokenVerifyInterceptor;

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // 注册UserIdMethodArgumentResolver的参数分解器
        argumentResolvers.add(new UserIdMethodArgumentResolver());
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessTokenVerifyInterceptor);
    }


}
