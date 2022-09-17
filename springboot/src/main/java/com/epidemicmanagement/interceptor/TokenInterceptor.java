package com.epidemicmanagement.interceptor;

import com.epidemicmanagement.aspect.PassInterceptor;
import com.epidemicmanagement.config.Result;
import com.epidemicmanagement.utils.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author XUE
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        StringBuffer requestURL = request.getRequestURL();
//        if (requestURL.indexOf("newsimg") > 0) {
//            return true;
//        }
        // 标注@PassInterceptor的方法放行
        // 若controller中有对应的请求时
        if (handler instanceof HandlerMethod) {
            boolean isPass = ((HandlerMethod) handler).getMethodAnnotation(PassInterceptor.class) != null;
            if (isPass) {
                return true;
            } else {
                boolean verify = Token.verify(request.getHeader("user_token"));
                if (verify) {
                    return true;
                } else {
                    returnJson(response);
                    return false;
                }
            }
            // 否则请求会被当做对静态资源的请求
        } else return handler instanceof ResourceHttpRequestHandler;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

    private void returnJson(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            Result result = Result.fail(501, "token无效");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
