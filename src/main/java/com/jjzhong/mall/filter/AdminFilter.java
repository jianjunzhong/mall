package com.jjzhong.mall.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.IOException;

/**
 * 管理员用户过滤器
 */
public class AdminFilter implements Filter {
    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 预检请求直接放行
        if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name()))
            chain.doFilter(request, response);
        else {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            User currentUser = UserFilter.userThreadLocal.get();
            ObjectMapper objectMapper = new ObjectMapper();
            if (currentUser == null) {
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(CommonResponse.error(MallExceptionEnum.NEED_LOGIN)));
            } else if (!userService.checkAdmin(currentUser)) {
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(CommonResponse.error(MallExceptionEnum.NEED_ADMIN)));
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
