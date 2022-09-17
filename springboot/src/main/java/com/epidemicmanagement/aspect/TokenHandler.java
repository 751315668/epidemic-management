package com.epidemicmanagement.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解：被标注的方法将使用自定义参数解析器解析token并为User参数注入id属性
 *
 * @author XUE
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenHandler {
}
