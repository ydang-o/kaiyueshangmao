/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.service;

import com.dingyangmall.system.domain.SysNotice;
import java.util.List;

public interface ISysNoticeService {
    public SysNotice selectNoticeById(Long var1);

    public List<SysNotice> selectNoticeList(SysNotice var1);

    public int insertNotice(SysNotice var1);

    public int updateNotice(SysNotice var1);

    public int deleteNoticeById(Long var1);

    public int deleteNoticeByIds(Long[] var1);
}

