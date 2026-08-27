/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.service.impl;

import com.dingyangmall.system.domain.SysLogininfor;
import com.dingyangmall.system.mapper.SysLogininforMapper;
import com.dingyangmall.system.service.ISysLogininforService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogininforServiceImpl
implements ISysLogininforService {
    @Autowired
    private SysLogininforMapper logininforMapper;

    @Override
    public void insertLogininfor(SysLogininfor logininfor) {
        this.logininforMapper.insertLogininfor(logininfor);
    }

    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor) {
        return this.logininforMapper.selectLogininforList(logininfor);
    }

    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        return this.logininforMapper.deleteLogininforByIds(infoIds);
    }

    @Override
    public void cleanLogininfor() {
        this.logininforMapper.cleanLogininfor();
    }
}

