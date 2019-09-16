package com.j13.poppy.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
/**
 * 在token过期的时候强制不抛出16的错误码，适用于非登录状态的首页显示等
 */
public @interface TokenExpireDontThrow16 {
}
