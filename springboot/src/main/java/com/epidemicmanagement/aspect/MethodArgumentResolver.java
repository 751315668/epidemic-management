package com.epidemicmanagement.aspect;

import com.epidemicmanagement.entity.User;
import com.epidemicmanagement.utils.Token;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自定义参数解析器：获取当前登录人的信息
 *
 * @author XUE
 */
@Component
public class MethodArgumentResolver implements HandlerMethodArgumentResolver {

    // 执行参数解析的条件：1.标注了@TokenHandler；2.参数类型为User
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenHandler.class) &&
                parameter.getParameterType().isAssignableFrom(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("user_token");
        Integer userId = Token.getUserId(token);
        User user = new User();
        user.setId(userId);
        return user;
    }
}
