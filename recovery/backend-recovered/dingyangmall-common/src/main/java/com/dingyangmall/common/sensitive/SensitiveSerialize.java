/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.sensitive;

import com.dingyangmall.common.sensitive.Sensitive;
import com.dingyangmall.common.sensitive.SensitiveTypeEnum;
import com.dingyangmall.common.utils.SensitiveUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;
import java.util.Objects;
import lombok.Generated;

public class SensitiveSerialize
extends JsonSerializer<String>
implements ContextualSerializer {
    private SensitiveTypeEnum type;

    @Override
    public void serialize(String originStr, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (this.type) {
            case CHINESE_NAME: {
                jsonGenerator.writeString(SensitiveUtils.chineseName(originStr));
                break;
            }
            case MOBILE_PHONE: {
                jsonGenerator.writeString(SensitiveUtils.mobilePhone(originStr));
                break;
            }
            case EMAIL: {
                jsonGenerator.writeString(SensitiveUtils.email(originStr));
                break;
            }
            case PASSWORD: {
                jsonGenerator.writeString(SensitiveUtils.password(originStr));
                break;
            }
            case KEY: {
                jsonGenerator.writeString(SensitiveUtils.key(originStr));
                break;
            }
            default: {
                throw new IllegalArgumentException("\u672a\u5b9a\u4e49\u7684\u654f\u611f\u4fe1\u606f\u679a\u4e3e\u7c7b" + String.valueOf((Object)this.type));
            }
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
                if (sensitive == null) {
                    sensitive = beanProperty.getContextAnnotation(Sensitive.class);
                }
                if (sensitive != null) {
                    return new SensitiveSerialize(sensitive.type());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }

    @Generated
    public SensitiveSerialize() {
    }

    @Generated
    public SensitiveSerialize(SensitiveTypeEnum type) {
        this.type = type;
    }
}

