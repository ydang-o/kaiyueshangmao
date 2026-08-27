/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.weixin.constant.MyReturnCode;
import com.dingyangmall.weixin.entity.ThirdSession;
import com.dingyangmall.weixin.utils.ThirdSessionHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

@Component
public class ThirdSessionInterceptor
implements AsyncHandlerInterceptor {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ThirdSessionInterceptor.class);
    private final RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object thirdSessionObj;
        String thirdSessionHeader = request.getHeader("third-session");
        if (StrUtil.isNotBlank(thirdSessionHeader)) {
            String key = "wx:ma:3rd_session:" + thirdSessionHeader;
            thirdSessionObj = this.redisTemplate.opsForValue().get(key);
            if (thirdSessionObj == null) {
                AjaxResult r = AjaxResult.error(MyReturnCode.ERR_60001.getCode(), MyReturnCode.ERR_60001.getMsg());
                this.writerPrint(response, r);
                return Boolean.FALSE;
            }
        } else {
            AjaxResult r = AjaxResult.error(MyReturnCode.ERR_60002.getCode(), MyReturnCode.ERR_60002.getMsg());
            this.writerPrint(response, r);
            return Boolean.FALSE;
        }
        String thirdSessionStr = String.valueOf(thirdSessionObj);
        ThirdSession thirdSession = JSONUtil.toBean(thirdSessionStr, ThirdSession.class);
        ThirdSessionHolder.setThirdSession(thirdSession);
        return Boolean.TRUE;
    }

    private void writerPrint(HttpServletResponse response, AjaxResult r) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.print(JSONUtil.parseObj(r));
        if (writer != null) {
            writer.close();
        }
    }

    @Generated
    public ThirdSessionInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}

