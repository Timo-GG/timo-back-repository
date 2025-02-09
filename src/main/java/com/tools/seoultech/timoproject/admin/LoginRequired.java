package com.tools.seoultech.timoproject.admin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) // 메서드 및 클래스에 사용 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
public @interface LoginRequired {
}