package com.lin_chen.filter;

import com.lin_chen.util.TokenUtils;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token验证拦截器
 **/
@Component
public class AccessTokenVerifyInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenVerifyInterceptor.class);

    @Autowired
    TokenVerifyProperties tokenVerifyProperties;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        response.setCharacterEncoding("UTF-8");
        if (tokenVerifyProperties.needVerify(method, requestURI)) {
            String token = request.getParameter("token");
            if (token != null) {
                Token tokenEntity;
                try {
                    tokenEntity = TokenUtils.decryptionToken(token);
                } catch (RuntimeException e) {
                    LOGGER.error("token解析异常，错误的token：{}", token);
                    response.sendError(403, "token解析异常，错误的token!");
                    return false;
                }
                String userId = tokenEntity.getUserId();
                request.setAttribute("userId", userId);
                return true;
            }
            response.sendError(403, "请上传token!");
            return false;
        }
        return true;
    }
}
