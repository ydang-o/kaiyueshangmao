package com.dingyangmall.weixin.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeUtils {
    private static final DateTimeFormatter WECHAT_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private LocalDateTimeUtils() {}

    public static LocalDateTime parse(String value) {
        if (value == null || value.isBlank()) return null;
        return LocalDateTime.parse(value, WECHAT_TIME);
    }
}
