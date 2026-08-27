/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.weixin.config.WxMaConfiguration;
import com.dingyangmall.weixin.entity.ThirdSession;
import com.dingyangmall.weixin.entity.WxOpenDataDTO;
import com.dingyangmall.weixin.entity.WxUser;
import com.dingyangmall.weixin.handler.SubscribeHandler;
import com.dingyangmall.weixin.mapper.WxUserMapper;
import com.dingyangmall.weixin.service.WxUserService;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Generated;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.api.WxMpUserTagService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WxUserServiceImpl
extends ServiceImpl<WxUserMapper, WxUser>
implements WxUserService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WxUserServiceImpl.class);
    private final WxMpService wxService;
    private final RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor={Exception.class})
    public boolean updateRemark(WxUser entity) throws WxErrorException {
        String id = entity.getId();
        String remark = entity.getRemark();
        String openId = entity.getOpenId();
        entity = new WxUser();
        entity.setId(id);
        entity.setRemark(remark);
        super.updateById(entity);
        WxMpUserService wxMpUserService = this.wxService.getUserService();
        wxMpUserService.userUpdateRemark(openId, remark);
        return true;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void tagging(String taggingType, Long tagId, String[] openIds) throws WxErrorException {
        List<Long> list;
        Long[] tagidList;
        WxUser wxUser;
        WxMpUserTagService wxMpUserTagService = this.wxService.getUserTagService();
        if ("tagging".equals(taggingType)) {
            for (String openId : openIds) {
                wxUser = (WxUser)((WxUserMapper)this.baseMapper).selectOne((Wrapper)Wrappers.lambdaQuery().eq(WxUser::getOpenId, openId));
                tagidList = wxUser.getTagidList();
                list = Arrays.asList(tagidList);
                if ((list = new ArrayList<Long>(list)).contains(tagId)) continue;
                list.add(tagId);
                tagidList = list.toArray(new Long[list.size()]);
                wxUser.setTagidList(tagidList);
                this.updateById(wxUser);
            }
            wxMpUserTagService.batchTagging(tagId, openIds);
        }
        if ("unTagging".equals(taggingType)) {
            for (String openId : openIds) {
                wxUser = (WxUser)((WxUserMapper)this.baseMapper).selectOne((Wrapper)Wrappers.lambdaQuery().eq(WxUser::getOpenId, openId));
                tagidList = wxUser.getTagidList();
                list = Arrays.asList(tagidList);
                if (!(list = new ArrayList<Long>(list)).contains(tagId)) continue;
                list.remove(tagId);
                tagidList = list.toArray(new Long[list.size()]);
                wxUser.setTagidList(tagidList);
                this.updateById(wxUser);
            }
            wxMpUserTagService.batchUntagging(tagId, openIds);
        }
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void synchroWxUser() throws WxErrorException {
        WxUser wxUser = new WxUser();
        wxUser.setSubscribe("0");
        ((WxUserMapper)this.baseMapper).update(wxUser, (Wrapper)Wrappers.lambdaQuery().eq(WxUser::getSubscribe, "1"));
        WxMpUserService wxMpUserService = this.wxService.getUserService();
        this.recursionGet(wxMpUserService, null);
    }

    void recursionGet(WxMpUserService wxMpUserService, String nextOpenid) throws WxErrorException {
        WxMpUserList userList = wxMpUserService.userList(nextOpenid);
        ArrayList listWxUser = new ArrayList();
        List<WxMpUser> listWxMpUser = this.getWxMpUserList(wxMpUserService, userList.getOpenids());
        listWxMpUser.forEach(wxMpUser -> {
            WxUser wxUser = (WxUser)((WxUserMapper)this.baseMapper).selectOne((Wrapper)Wrappers.lambdaQuery().eq(WxUser::getOpenId, wxMpUser.getOpenId()));
            if (wxUser == null) {
                wxUser = new WxUser();
                wxUser.setSubscribeNum(1);
            }
            SubscribeHandler.setWxUserValue(wxUser, wxMpUser);
            listWxUser.add(wxUser);
        });
        this.saveOrUpdateBatch(listWxUser);
        if (userList.getCount() >= 10000) {
            this.recursionGet(wxMpUserService, userList.getNextOpenid());
        }
    }

    private List<WxMpUser> getWxMpUserList(WxMpUserService wxMpUserService, List<String> openidsList) throws WxErrorException {
        int count = openidsList.size();
        if (count <= 0) {
            return new ArrayList<WxMpUser>();
        }
        ArrayList<WxMpUser> list = Lists.newArrayList();
        int a = count % 100 > 0 ? count / 100 + 1 : count / 100;
        for (int i = 0; i < a; ++i) {
            List<WxMpUser> followersInfoList;
            if (i + 1 < a) {
                log.debug("i:{},from:{},to:{}", i, i * 100, (i + 1) * 100);
                followersInfoList = wxMpUserService.userInfoList(openidsList.subList(i * 100, (i + 1) * 100));
                if (null == followersInfoList || followersInfoList.isEmpty()) continue;
                list.addAll(followersInfoList);
                continue;
            }
            log.debug("i:{},from:{},to:{}", i, i * 100, count - i * 100);
            followersInfoList = wxMpUserService.userInfoList(openidsList.subList(i * 100, count));
            if (null == followersInfoList || followersInfoList.isEmpty()) continue;
            list.addAll(followersInfoList);
        }
        log.debug("\u672c\u6279\u6b21\u83b7\u53d6\u5fae\u4fe1\u7c89\u4e1d\u6570\uff1a", (Object)list.size());
        return list;
    }

    @Override
    public WxUser getByOpenId(String openId) {
        return (WxUser)((WxUserMapper)this.baseMapper).selectOne((Wrapper)Wrappers.lambdaQuery().eq(WxUser::getOpenId, openId));
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public WxUser loginMa(String appId, String jsCode) throws WxErrorException {
        WxMaJscode2SessionResult jscode2session = WxMaConfiguration.getMaService(appId).jsCode2SessionInfo(jsCode);
        WxUser wxUser = this.getByOpenId(jscode2session.getOpenid());
        if (wxUser == null) {
            wxUser = new WxUser();
            wxUser.setAppType("1");
            wxUser.setOpenId(jscode2session.getOpenid());
            wxUser.setSessionKey(jscode2session.getSessionKey());
            wxUser.setUnionId(jscode2session.getUnionid());
            try {
                this.save(wxUser);
            }
            catch (DuplicateKeyException e) {
                if (e.getMessage().contains("uk_appid_openid")) {
                    wxUser = this.getByOpenId(wxUser.getOpenId());
                }
            }
        } else {
            wxUser.setAppType("1");
            wxUser.setOpenId(jscode2session.getOpenid());
            wxUser.setSessionKey(jscode2session.getSessionKey());
            wxUser.setUnionId(jscode2session.getUnionid());
            this.updateById(wxUser);
        }
        String thirdSessionKey = UUID.randomUUID().toString();
        ThirdSession thirdSession = new ThirdSession();
        thirdSession.setAppId(appId);
        thirdSession.setSessionKey(wxUser.getSessionKey());
        thirdSession.setWxUserId(wxUser.getId());
        thirdSession.setOpenId(wxUser.getOpenId());
        String key = "wx:ma:3rd_session:" + thirdSessionKey;
        this.redisTemplate.opsForValue().set((CallSite)((Object)key), JSONUtil.toJsonStr(thirdSession), 6L, TimeUnit.HOURS);
        wxUser.setSessionKey(thirdSessionKey);
        return wxUser;
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public WxUser saveOrUptateWxUser(WxOpenDataDTO wxOpenDataDTO) {
        WxMaUserService wxMaUserService = WxMaConfiguration.getMaService(wxOpenDataDTO.getAppId()).getUserService();
        WxMaUserInfo wxMaUserInfo = wxMaUserService.getUserInfo(wxOpenDataDTO.getSessionKey(), wxOpenDataDTO.getEncryptedData(), wxOpenDataDTO.getIv());
        WxUser wxUser = new WxUser();
        BeanUtil.copyProperties(wxMaUserInfo, wxUser);
        wxUser.setId(wxOpenDataDTO.getUserId());
        wxUser.setSex(wxMaUserInfo.getGender());
        wxUser.setHeadimgUrl(wxMaUserInfo.getAvatarUrl());
        ((WxUserMapper)this.baseMapper).updateById(wxUser);
        wxUser = (WxUser)((WxUserMapper)this.baseMapper).selectById((Serializable)((Object)wxUser.getId()));
        return wxUser;
    }

    @Generated
    public WxUserServiceImpl(WxMpService wxService, RedisTemplate redisTemplate) {
        this.wxService = wxService;
        this.redisTemplate = redisTemplate;
    }
}

