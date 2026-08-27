/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.handler;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.weixin.entity.WxAutoReply;
import com.dingyangmall.weixin.entity.WxUser;
import com.dingyangmall.weixin.handler.AbstractHandler;
import com.dingyangmall.weixin.handler.MsgHandler;
import com.dingyangmall.weixin.mapper.WxUserMapper;
import com.dingyangmall.weixin.service.WxAutoReplyService;
import com.dingyangmall.weixin.service.WxMsgService;
import com.dingyangmall.weixin.utils.LocalDateTimeUtils;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SubscribeHandler
extends AbstractHandler {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(SubscribeHandler.class);
    private final WxAutoReplyService wxAutoReplyService;
    private final WxUserMapper wxUserMapper;
    private final WxMsgService wxMsgService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService, WxSessionManager sessionManager) {
        log.info("\u65b0\u5173\u6ce8\u7528\u6237 OPENID: " + wxMessage.getFromUser());
        try {
            WxMpUser userWxInfo = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                WxUser wxUser = (WxUser)this.wxUserMapper.selectOne((Wrapper)Wrappers.lambdaQuery().eq(WxUser::getOpenId, userWxInfo.getOpenId()));
                if (wxUser == null) {
                    wxUser = new WxUser();
                    wxUser.setSubscribeNum(1);
                    SubscribeHandler.setWxUserValue(wxUser, userWxInfo);
                    this.wxUserMapper.insert(wxUser);
                } else {
                    wxUser.setSubscribeNum(wxUser.getSubscribeNum() + 1);
                    SubscribeHandler.setWxUserValue(wxUser, userWxInfo);
                    this.wxUserMapper.updateById(wxUser);
                }
                List<WxAutoReply> listWxAutoReply = this.wxAutoReplyService.list((Wrapper)Wrappers.query().lambda().eq(WxAutoReply::getType, "1"));
                WxMpXmlOutMessage wxMpXmlOutMessage = MsgHandler.getWxMpXmlOutMessage(wxMessage, listWxAutoReply, wxUser, this.wxMsgService);
                return wxMpXmlOutMessage;
            }
        }
        catch (Exception e) {
            log.error("\u7528\u6237\u5173\u6ce8\u51fa\u9519\uff1a" + e.getMessage());
        }
        return null;
    }

    public static void setWxUserValue(WxUser wxUser, WxMpUser userWxInfo) {
        wxUser.setAppType("2");
        wxUser.setSubscribe("1");
        wxUser.setSubscribeScene(userWxInfo.getSubscribeScene());
        if (null != userWxInfo.getSubscribeTime()) {
            wxUser.setSubscribeTime(LocalDateTimeUtils.timestamToDatetime(userWxInfo.getSubscribeTime() * 1000L));
        }
        wxUser.setOpenId(userWxInfo.getOpenId());
        wxUser.setLanguage(userWxInfo.getLanguage());
        wxUser.setRemark(userWxInfo.getRemark());
        wxUser.setUnionId(userWxInfo.getUnionId());
        wxUser.setGroupId(JSONUtil.toJsonStr(userWxInfo.getGroupId()));
        wxUser.setTagidList(userWxInfo.getTagIds());
        wxUser.setQrSceneStr(userWxInfo.getQrSceneStr());
    }

    @Generated
    public SubscribeHandler(WxAutoReplyService wxAutoReplyService, WxUserMapper wxUserMapper, WxMsgService wxMsgService) {
        this.wxAutoReplyService = wxAutoReplyService;
        this.wxUserMapper = wxUserMapper;
        this.wxMsgService = wxMsgService;
    }
}

