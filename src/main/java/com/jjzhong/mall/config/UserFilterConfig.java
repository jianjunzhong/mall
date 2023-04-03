package com.jjzhong.mall.config;

import com.jjzhong.mall.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户登陆过滤器配置
 */
@Configuration
public class UserFilterConfig {
    @Bean
    public UserFilter userFilter() {
        return new UserFilter();
    }

    @Bean(name = "userFilterConf")
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(userFilter());
        filterRegistrationBean.addUrlPatterns("/order/*");
        filterRegistrationBean.addUrlPatterns("/cart/*");
        filterRegistrationBean.addUrlPatterns("/user/logout");
        filterRegistrationBean.addUrlPatterns("/user/update");
        filterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterRegistrationBean.addUrlPatterns("/admin/order/*");
        filterRegistrationBean.setName("userFilterConf");
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }
}
