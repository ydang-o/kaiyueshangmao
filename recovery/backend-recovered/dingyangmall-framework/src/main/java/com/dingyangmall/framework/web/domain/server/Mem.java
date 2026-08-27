/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.web.domain.server;

import com.dingyangmall.common.utils.Arith;

public class Mem {
    private double total;
    private double used;
    private double free;

    public double getTotal() {
        return Arith.div(this.total, 1.073741824E9, 2);
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double getUsed() {
        return Arith.div(this.used, 1.073741824E9, 2);
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public double getFree() {
        return Arith.div(this.free, 1.073741824E9, 2);
    }

    public void setFree(long free) {
        this.free = free;
    }

    public double getUsage() {
        return Arith.mul(Arith.div(this.used, this.total, 4), 100.0);
    }
}

