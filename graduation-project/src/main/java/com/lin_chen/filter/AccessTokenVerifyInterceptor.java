package com.lin_chen.filter;

import com.lin_chen.util.TokenUtils;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
        setOrigin(response);
        if (tokenVerifyProperties.needVerify(method, requestURI)) {
            String tokenOfHeader = request.getHeader("token");
            String token = tokenOfHeader == null ? request.getParameter("token") : tokenOfHeader;
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

    /**
     * 解决跨域问题
     */
    private void setOrigin(HttpServletResponse httpServletResponse) {
        //这里填写你允许进行跨域的主机ip
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        //允许的访问方法
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        //Access-Control-Max-Age 用于 CORS 相关配置的缓存
        httpServletResponse.setHeader("Access-Control-Max-Age", "18000L");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
    }
}
