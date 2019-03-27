package com.lin_chen.filter;

import com.lin_chen.util.StringUtils;
import com.lin_chen.util.TokenUtils;
import com.lin_chen.util.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * userId的参数解释器
 **/
public class UserIdMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    AccessTokenVerifyInterceptor accessTokenVerifyInterceptor;

    public static final String PARAMS_UID = "userId";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String userId = (String) webRequest.getAttribute(PARAMS_UID, 0);
        if (StringUtils.isEmpty(userId) && parameter.getParameterAnnotation(UserId.class).required()) {
            throw new RuntimeException("UserId不能为空 或者 权限不够。");
        }
        return userId;
    }
}