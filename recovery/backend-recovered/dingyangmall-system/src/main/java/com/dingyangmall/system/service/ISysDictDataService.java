/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.service;

import com.dingyangmall.common.core.domain.entity.SysDictData;
import java.util.List;

public interface ISysDictDataService {
    public List<SysDictData> selectDictDataList(SysDictData var1);

    public String selectDictLabel(String var1, String var2);

    public SysDictData selectDictDataById(Long var1);

    public int deleteDictDataByIds(Long[] var1);

    public int insertDictData(SysDictData var1);

    public int updateDictData(SysDictData var1);
}

