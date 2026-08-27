/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.manager;

import com.dingyangmall.common.utils.Threads;
import com.dingyangmall.common.utils.spring.SpringUtils;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncManager {
    private final int OPERATE_DELAY_TIME = 10;
    private ScheduledExecutorService executor;
    private static AsyncManager me = new AsyncManager();

    private AsyncManager() {
    }

    public static AsyncManager me() {
        return me;
    }

    public void execute(TimerTask task) {
        if (this.executor == null) {
            this.executor = (ScheduledExecutorService)SpringUtils.getBean("scheduledExecutorService");
        }
        this.executor.schedule(task, 10L, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        if (this.executor != null) {
            Threads.shutdownAndAwaitTermination(this.executor);
        }
    }
}

