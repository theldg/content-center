package com.ldg.app.contentcenter.annotation;

import java.lang.annotation.*;

/**
 * 登录检查注解
 *
 * @author ldg
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {
}
