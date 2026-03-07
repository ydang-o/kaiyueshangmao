package com.dingyangmall.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.mall.entity.TbIntegralFlow;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.mapper.TbIntegralFlowMapper;
import com.dingyangmall.mall.mapper.UmsMemberMapper;
import com.dingyangmall.mall.service.TbIntegralFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * 积分流水 服务实现类
 *
 * @author dingyangmall
 * @date 2026-02-12
 */
@Service
public class TbIntegralFlowServiceImpl extends ServiceImpl<TbIntegralFlowMapper, TbIntegralFlow> implements TbIntegralFlowService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPoints(Long userId, Integer points, Integer type, String remark) {
        if (points == null || points == 0) return;
        
        // 更新用户积分
        UmsMember member = umsMemberMapper.selectById(userId);
        if (member != null) {
            // 初始化积分防止NPE
            if (member.getPoints() == null) member.setPoints(0);
            member.setPoints(member.getPoints() + points);
            umsMemberMapper.updateById(member);
            
            // 记录流水
            TbIntegralFlow flow = new TbIntegralFlow();
            flow.setUserId(userId);
            flow.setIntegralNum(points);
            flow.setOperType(type);
            flow.setRemark(remark);
            flow.setOperTime(LocalDateTime.now());
            flow.setCreateTime(LocalDateTime.now());
            flow.setCreateBy(String.valueOf(userId)); // create_by 为 NOT NULL，系统流水用 userId 标识
            save(flow);
        }
    }
}
