package com.jjzhong.mall.config;

import com.jjzhong.mall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置
 */
@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // admin 管理员页面资源映射
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/");
        // 图片/上传资源映射
        registry.addResourceHandler("/" + Constant.FILE_UPLOAD_IMAGE_CONTEXT + "/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR + Constant.FILE_UPLOAD_IMAGE_CONTEXT + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 跨域设置
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
                .maxAge(3600);
    }
}
