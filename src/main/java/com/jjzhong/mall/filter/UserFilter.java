package com.jjzhong.mall.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjzhong.mall.common.CommonResponse;
import com.jjzhong.mall.common.Constant;
import com.jjzhong.mall.exception.MallExceptionEnum;
import com.jjzhong.mall.model.pojo.User;
import com.jjzhong.mall.util.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 用户登陆过滤器，判断是否是合法用户
 */
public class UserFilter implements Filter {
    public static ThreadLocal<User> userThreadLocal = ThreadLocal.withInitial(User::new);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 若为预检请求，则直接放行
        if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name()))
            chain.doFilter(request, response);
        else {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            String token = httpServletRequest.getHeader(Constant.JWT_TOKEN);
            CommonResponse commonResponse = null;
            if (!StringUtils.hasText(token)) {
                commonResponse = CommonResponse.error(MallExceptionEnum.NEED_LOGIN);
            } else {
                // 获取 token 中的用户信息
                try {
                    JwtUtils.getUserFromToken(token, userThreadLocal.get());
                } catch (TokenExpiredException e) {
                    // token 过期
                    commonResponse = CommonResponse.error(MallExceptionEnum.TOKEN_EXPIRED);
                } catch (Exception e) {
                    // 无效的 token
                    commonResponse = CommonResponse.error(MallExceptionEnum.TOKEN_INVALID);
                }
            }
            if (commonResponse != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(commonResponse));
            } else {
                chain.doFilter(request, response);
            }
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        userThreadLocal.remove();
    }
}
