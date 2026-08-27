/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.manager;

import com.dingyangmall.framework.manager.AsyncManager;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ShutdownManager {
    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    @PreDestroy
    public void destroy() {
        this.shutdownAsyncManager();
    }

    private void shutdownAsyncManager() {
        try {
            logger.info("====\u5173\u95ed\u540e\u53f0\u4efb\u52a1\u4efb\u52a1\u7ebf\u7a0b\u6c60====");
            AsyncManager.me().shutdown();
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}

