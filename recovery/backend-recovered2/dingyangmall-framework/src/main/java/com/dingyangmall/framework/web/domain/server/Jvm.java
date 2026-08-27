/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.domain.server;

import com.dingyangmall.common.utils.Arith;
import com.dingyangmall.common.utils.DateUtils;
import java.lang.management.ManagementFactory;

public class Jvm {
    private double total;
    private double max;
    private double free;
    private String version;
    private String home;

    public double getTotal() {
        return Arith.div(this.total, 1048576.0, 2);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMax() {
        return Arith.div(this.max, 1048576.0, 2);
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getFree() {
        return Arith.div(this.free, 1048576.0, 2);
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getUsed() {
        return Arith.div(this.total - this.free, 1048576.0, 2);
    }

    public double getUsage() {
        return Arith.mul(Arith.div(this.total - this.free, this.total, 4), 100.0);
    }

    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHome() {
        return this.home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getStartTime() {
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.getServerStartDate());
    }

    public String getRunTime() {
        return DateUtils.timeDistance(DateUtils.getNowDate(), DateUtils.getServerStartDate());
    }

    public String getInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }
}

