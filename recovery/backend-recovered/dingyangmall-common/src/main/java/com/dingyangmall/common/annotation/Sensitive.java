/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.annotation;

import com.dingyangmall.common.config.serializer.SensitiveJsonSerializer;
import com.dingyangmall.common.enums.DesensitizedType;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
@JacksonAnnotationsInside
@JsonSerialize(using=SensitiveJsonSerializer.class)
public @interface Sensitive {
    public DesensitizedType desensitizedType();
}

