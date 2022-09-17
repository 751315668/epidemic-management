package com.epidemicmanagement.config;

import com.epidemicmanagement.aspect.MethodArgumentResolver;
import com.epidemicmanagement.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 自定义配置类
 *
 * @author XUE
 */
@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MethodArgumentResolver argumentResolver;

//    @Autowired
//    private TokenInterceptor tokenInterceptor;

    // 添加自定义的参数解析器
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(argumentResolver);
    }

    // 添加自定义拦截器
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**")
//                .excludePathPatterns("/login")
//                .excludePathPatterns("/photo/**")
//                .excludePathPatterns("/newsimg/**")
//                .excludePathPatterns("/supplyimg/**")
//                .excludePathPatterns("/css/**")
//                .excludePathPatterns("/js/**")
//                .excludePathPatterns("/favicon/**");
//
//    }

    // 静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/newsimg/**", "/photo/**", "/supplyimg/**") // 访问静态资源的请求路径
                .addResourceLocations("file:C:\\Users\\hp\\IdeaProjects\\epidemic-management\\src\\main\\resources\\static\\newsimg\\",
                        "file:C:\\Users\\hp\\IdeaProjects\\epidemic-management\\src\\main\\resources\\static\\photo\\",
                        "file:C:\\Users\\hp\\IdeaProjects\\epidemic-management\\src\\main\\resources\\static\\supplyimg\\");
    }
}
