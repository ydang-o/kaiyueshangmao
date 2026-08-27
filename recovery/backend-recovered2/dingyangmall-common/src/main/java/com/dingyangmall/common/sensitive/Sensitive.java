/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.sensitive;

import com.dingyangmall.common.sensitive.SensitiveSerialize;
import com.dingyangmall.common.sensitive.SensitiveTypeEnum;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD})
@JacksonAnnotationsInside
@JsonSerialize(using=SensitiveSerialize.class)
public @interface Sensitive {
    public SensitiveTypeEnum type();
}

