/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.weixin.entity.WxMenu;
import com.dingyangmall.weixin.entity.WxMsg;
import com.dingyangmall.weixin.entity.WxUser;
import com.dingyangmall.weixin.handler.AbstractHandler;
import com.dingyangmall.weixin.handler.SubscribeHandler;
import com.dingyangmall.weixin.mapper.WxMenuMapper;
import com.dingyangmall.weixin.mapper.WxMsgMapper;
import com.dingyangmall.weixin.mapper.WxUserMapper;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.builder.outxml.ImageBuilder;
import me.chanjar.weixin.mp.builder.outxml.MusicBuilder;
import me.chanjar.weixin.mp.builder.outxml.NewsBuilder;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import me.chanjar.weixin.mp.builder.outxml.VideoBuilder;
import me.chanjar.weixin.mp.builder.outxml.VoiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MenuHandler
extends AbstractHandler {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(MenuHandler.class);
    private final WxMenuMapper wxMenuMapper;
    private final WxUserMapper wxUserMapper;
    private final WxMsgMapper wxMsgMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService, WxSessionManager sessionManager) throws WxErrorException {
        WxUser wxUser;
        WxMenu wxMenu = null;
        if ("CLICK".equals(wxMessage.getEvent()) || "scancode_waitmsg".equals(wxMessage.getEvent())) {
            wxMenu = (WxMenu)this.wxMenuMapper.selectById((Serializable)((Object)wxMessage.getEventKey()));
            if (wxMenu == null) {
                return ((TextBuilder)((TextBuilder)new TextBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).content("\u975e\u5e38\u62b1\u6b49\uff0c\u8be5\u83dc\u5355\u5df2\u5220\u9664\uff01").build();
            }
        } else {
            wxMenu = new WxMenu();
        }
        if ((wxUser = (WxUser)this.wxUserMapper.selectOne((Wrapper)Wrappers.lambdaQuery().eq(WxUser::getOpenId, wxMessage.getFromUser()))) == null) {
            WxMpUser userWxInfo = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);
            wxUser = new WxUser();
            wxUser.setSubscribeNum(1);
            SubscribeHandler.setWxUserValue(wxUser, userWxInfo);
            this.wxUserMapper.insert(wxUser);
        }
        return this.getWxMpXmlOutMessage(wxMessage, wxMenu, wxUser);
    }

    public WxMpXmlOutMessage getWxMpXmlOutMessage(WxMpXmlMessage wxMessage, WxMenu wxMenu, WxUser wxUser) {
        WxMpXmlOutMessage wxMpXmlOutMessage = null;
        WxMsg wxMsg = new WxMsg();
        wxMsg.setWxUserId(wxUser.getId());
        wxMsg.setNickName(wxUser.getNickName());
        wxMsg.setHeadimgUrl(wxUser.getHeadimgUrl());
        wxMsg.setType("1");
        wxMsg.setRepEvent(wxMessage.getEvent());
        wxMsg.setRepType(wxMessage.getMsgType());
        wxMsg.setRepName(wxMenu.getName());
        if ("VIEW".equals(wxMessage.getEvent())) {
            wxMsg.setRepUrl(wxMessage.getEventKey());
        }
        if ("scancode_waitmsg".equals(wxMessage.getEvent())) {
            wxMsg.setRepContent(wxMessage.getScanCodeInfo().getScanResult());
        }
        wxMsg.setReadFlag("0");
        LocalDateTime now = LocalDateTime.now();
        wxMsg.setCreateTime(now);
        this.wxMsgMapper.insert(wxMsg);
        if ("click".equals(wxMenu.getType()) || "scancode_waitmsg".equals(wxMenu.getType())) {
            wxMsg = new WxMsg();
            wxMsg.setWxUserId(wxUser.getId());
            wxMsg.setNickName(wxUser.getNickName());
            wxMsg.setHeadimgUrl(wxUser.getHeadimgUrl());
            wxMsg.setCreateTime(now.plusSeconds(1L));
            wxMsg.setType("2");
            wxMsg.setRepType(wxMenu.getRepType());
            if ("text".equals(wxMenu.getRepType())) {
                wxMsg.setRepContent(wxMenu.getRepContent());
                wxMpXmlOutMessage = ((TextBuilder)((TextBuilder)new TextBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).content(wxMenu.getRepContent()).build();
            }
            if ("image".equals(wxMenu.getRepType())) {
                wxMsg.setRepName(wxMenu.getRepName());
                wxMsg.setRepUrl(wxMenu.getRepUrl());
                wxMsg.setRepMediaId(wxMenu.getRepMediaId());
                wxMpXmlOutMessage = ((ImageBuilder)((ImageBuilder)new ImageBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).mediaId(wxMenu.getRepMediaId()).build();
            }
            if ("voice".equals(wxMenu.getRepType())) {
                wxMsg.setRepName(wxMenu.getRepName());
                wxMsg.setRepUrl(wxMenu.getRepUrl());
                wxMsg.setRepMediaId(wxMenu.getRepMediaId());
                wxMpXmlOutMessage = ((VoiceBuilder)((VoiceBuilder)new VoiceBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).mediaId(wxMenu.getRepMediaId()).build();
            }
            if ("video".equals(wxMenu.getRepType())) {
                wxMsg.setRepName(wxMenu.getRepName());
                wxMsg.setRepDesc(wxMenu.getRepDesc());
                wxMsg.setRepUrl(wxMenu.getRepUrl());
                wxMsg.setRepMediaId(wxMenu.getRepMediaId());
                wxMpXmlOutMessage = ((VideoBuilder)((VideoBuilder)new VideoBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).mediaId(wxMenu.getRepMediaId()).title(wxMenu.getRepName()).description(wxMenu.getRepDesc()).build();
            }
            if ("music".equals(wxMenu.getRepType())) {
                wxMsg.setRepName(wxMenu.getRepName());
                wxMsg.setRepDesc(wxMenu.getRepDesc());
                wxMsg.setRepUrl(wxMenu.getRepUrl());
                wxMsg.setRepHqUrl(wxMenu.getRepHqUrl());
                wxMsg.setRepThumbMediaId(wxMenu.getRepThumbMediaId());
                wxMsg.setRepThumbUrl(wxMenu.getRepThumbUrl());
                wxMpXmlOutMessage = ((MusicBuilder)((MusicBuilder)new MusicBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).thumbMediaId(wxMenu.getRepThumbMediaId()).title(wxMenu.getRepName()).description(wxMenu.getRepDesc()).musicUrl(wxMenu.getRepUrl()).hqMusicUrl(wxMenu.getRepHqUrl()).build();
            }
            if ("news".equals(wxMenu.getRepType())) {
                ArrayList<WxMpXmlOutNewsMessage.Item> list = new ArrayList<WxMpXmlOutNewsMessage.Item>();
                List<JSONObject> listJSONObject = JSONUtil.toList(wxMenu.getContent().getJSONArray("articles"), JSONObject.class);
                for (JSONObject jSONObject : listJSONObject) {
                    WxMpXmlOutNewsMessage.Item t = new WxMpXmlOutNewsMessage.Item();
                    t.setTitle(jSONObject.getStr("title"));
                    t.setDescription(jSONObject.getStr("digest"));
                    t.setPicUrl(jSONObject.getStr("thumbUrl"));
                    t.setUrl(jSONObject.getStr("url"));
                    list.add(t);
                }
                wxMsg.setRepName(wxMenu.getRepName());
                wxMsg.setRepDesc(wxMenu.getRepDesc());
                wxMsg.setRepUrl(wxMenu.getRepUrl());
                wxMsg.setRepMediaId(wxMenu.getRepMediaId());
                wxMsg.setContent(wxMenu.getContent());
                wxMpXmlOutMessage = ((NewsBuilder)((NewsBuilder)new NewsBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).articles(list).build();
            }
            this.wxMsgMapper.insert(wxMsg);
        }
        return wxMpXmlOutMessage;
    }

    @Generated
    public MenuHandler(WxMenuMapper wxMenuMapper, WxUserMapper wxUserMapper, WxMsgMapper wxMsgMapper) {
        this.wxMenuMapper = wxMenuMapper;
        this.wxUserMapper = wxUserMapper;
        this.wxMsgMapper = wxMsgMapper;
    }
}

