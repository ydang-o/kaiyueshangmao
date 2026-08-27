/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.xss;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.xss.Xss;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XssValidator
implements ConstraintValidator<Xss, String> {
    private static final String HTML_PATTERN = "<(\\S*?)[^>]*>.*?|<.*? />";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return !XssValidator.containsHtml(value);
    }

    public static boolean containsHtml(String value) {
        StringBuilder sHtml = new StringBuilder();
        Pattern pattern = Pattern.compile(HTML_PATTERN);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            sHtml.append(matcher.group());
        }
        return pattern.matcher(sHtml).matches();
    }
}

