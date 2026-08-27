/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.mapper.TbIntegralFlowMapper;
import com.dingyangmall.mall.mapper.UmsMemberMapper;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TbIntegralFlowServiceImpl
extends ServiceImpl<TbIntegralFlowMapper, TbIntegralFlow>
implements TbIntegralFlowService {
    private static final Logger log = LoggerFactory.getLogger(TbIntegralFlowServiceImpl.class);
    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void addPoints(Long userId, Integer points, Integer type, String remark) {
        if (points == null || points == 0) {
            return;
        }
        log.info("[\u79ef\u5206\u64cd\u4f5c] \u5f00\u59cb\u6267\u884c addPoints: userId={}, points={}, type={}, remark={}", userId, points, type, remark);
        int affected = this.umsMemberMapper.updatePointsAtomic(userId, points);
        if (affected == 0) {
            if (points < 0) {
                UmsMember member = (UmsMember)this.umsMemberMapper.selectById(userId);
                int currentPoints = member != null && member.getPoints() != null ? member.getPoints() : 0;
                throw new RuntimeException("\u79ef\u5206\u4f59\u989d\u4e0d\u8db3\uff0c\u5f53\u524d\u4f59\u989d\uff1a" + currentPoints + "\uff0c\u9700\u8981\u6263\u51cf\uff1a" + -points.intValue());
            }
            throw new RuntimeException("\u4f1a\u5458\u4e0d\u5b58\u5728\u6216\u79ef\u5206\u66f4\u65b0\u5931\u8d25\uff0cuserId=" + userId);
        }
        log.info("[\u79ef\u5206\u64cd\u4f5c] \u539f\u5b50\u66f4\u65b0\u5b8c\u6210: userId={}, points={}", (Object)userId, (Object)points);
        TbIntegralFlow flow = new TbIntegralFlow();
        flow.setUserId(userId);
        flow.setIntegralNum(points);
        flow.setOperType(type);
        if (remark != null && remark.length() > 200) {
            remark = remark.substring(0, 200);
        }
        flow.setRemark(remark);
        flow.setOperTime(LocalDateTime.now());
        flow.setCreateTime(LocalDateTime.now());
        flow.setCreateBy(String.valueOf(userId));
        this.save(flow);
        log.info("[\u79ef\u5206\u64cd\u4f5c] \u6d41\u6c34\u8bb0\u5f55\u5df2\u4fdd\u5b58: userId={}, \u6d41\u6c34ID={}", (Object)userId, (Object)flow.getId());
    }
}

