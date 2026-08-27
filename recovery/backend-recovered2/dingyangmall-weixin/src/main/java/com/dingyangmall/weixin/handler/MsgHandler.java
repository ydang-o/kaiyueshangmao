/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.handler;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyangmall.weixin.entity.WxAutoReply;
import com.dingyangmall.weixin.entity.WxMsg;
import com.dingyangmall.weixin.entity.WxUser;
import com.dingyangmall.weixin.handler.AbstractHandler;
import com.dingyangmall.weixin.mapper.WxUserMapper;
import com.dingyangmall.weixin.service.WxAutoReplyService;
import com.dingyangmall.weixin.service.WxMsgService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.builder.outxml.ImageBuilder;
import me.chanjar.weixin.mp.builder.outxml.MusicBuilder;
import me.chanjar.weixin.mp.builder.outxml.NewsBuilder;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import me.chanjar.weixin.mp.builder.outxml.VideoBuilder;
import me.chanjar.weixin.mp.builder.outxml.VoiceBuilder;
import org.springframework.stereotype.Component;

@Component
public class MsgHandler
extends AbstractHandler {
    private final WxAutoReplyService wxAutoReplyService;
    private final WxUserMapper wxUserMapper;
    private final WxMsgService wxMsgService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) {
        if (!wxMessage.getMsgType().equals("event")) {
            WxMpXmlOutMessage rs;
            List<WxAutoReply> listWxAutoReply;
            WxUser wxUser = (WxUser)this.wxUserMapper.selectOne((Wrapper)Wrappers.lambdaQuery().eq(WxUser::getOpenId, wxMessage.getFromUser()));
            if ("text".equals(wxMessage.getMsgType())) {
                listWxAutoReply = this.wxAutoReplyService.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.query().lambda().eq(WxAutoReply::getType, "3")).eq(WxAutoReply::getRepMate, "1")).eq(WxAutoReply::getReqKey, wxMessage.getContent()));
                if (listWxAutoReply != null && listWxAutoReply.size() > 0) {
                    rs = MsgHandler.getWxMpXmlOutMessage(wxMessage, listWxAutoReply, wxUser, this.wxMsgService);
                    if (rs != null) {
                        return rs;
                    }
                }
                if ((listWxAutoReply = this.wxAutoReplyService.list((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)Wrappers.query().lambda().eq(WxAutoReply::getType, "3")).eq(WxAutoReply::getRepMate, "2")).like(WxAutoReply::getReqKey, wxMessage.getContent()))) != null && listWxAutoReply.size() > 0) {
                    rs = MsgHandler.getWxMpXmlOutMessage(wxMessage, listWxAutoReply, wxUser, this.wxMsgService);
                    if (rs != null) {
                        return rs;
                    }
                }
            }
            listWxAutoReply = this.wxAutoReplyService.list((Wrapper)((LambdaQueryWrapper)Wrappers.query().lambda().eq(WxAutoReply::getType, "2")).eq(WxAutoReply::getReqType, wxMessage.getMsgType()));
            rs = MsgHandler.getWxMpXmlOutMessage(wxMessage, listWxAutoReply, wxUser, this.wxMsgService);
            return rs;
        }
        return null;
    }

    public static WxMpXmlOutMessage getWxMpXmlOutMessage(WxMpXmlMessage wxMessage, List<WxAutoReply> listWxAutoReply, WxUser wxUser, WxMsgService wxMsgService) {
        WxMpXmlOutMessage wxMpXmlOutMessage = null;
        WxMsg wxMsg = new WxMsg();
        wxMsg.setWxUserId(wxUser.getId());
        wxMsg.setNickName(wxUser.getNickName());
        wxMsg.setHeadimgUrl(wxUser.getHeadimgUrl());
        wxMsg.setType("1");
        wxMsg.setRepEvent(wxMessage.getEvent());
        wxMsg.setRepType(wxMessage.getMsgType());
        wxMsg.setRepMediaId(wxMessage.getMediaId());
        if ("text".equals(wxMessage.getMsgType())) {
            wxMsg.setRepContent(wxMessage.getContent());
        }
        if ("voice".equals(wxMessage.getMsgType())) {
            wxMsg.setRepName(wxMessage.getMediaId() + "." + wxMessage.getFormat());
            wxMsg.setRepContent(wxMessage.getRecognition());
        }
        if ("image".equals(wxMessage.getMsgType())) {
            wxMsg.setRepUrl(wxMessage.getPicUrl());
        }
        if ("link".equals(wxMessage.getMsgType())) {
            wxMsg.setRepName(wxMessage.getTitle());
            wxMsg.setRepDesc(wxMessage.getDescription());
            wxMsg.setRepUrl(wxMessage.getUrl());
        }
        if ("file".equals(wxMessage.getMsgType())) {
            wxMsg.setRepName(wxMessage.getTitle());
            wxMsg.setRepDesc(wxMessage.getDescription());
        }
        if ("video".equals(wxMessage.getMsgType())) {
            wxMsg.setRepThumbMediaId(wxMessage.getThumbMediaId());
        }
        if ("location".equals(wxMessage.getMsgType())) {
            wxMsg.setRepLocationX(wxMessage.getLocationX());
            wxMsg.setRepLocationY(wxMessage.getLocationY());
            wxMsg.setRepScale(wxMessage.getScale());
            wxMsg.setRepContent(wxMessage.getLabel());
        }
        wxMsg.setReadFlag("0");
        LocalDateTime now = LocalDateTime.now();
        wxMsg.setCreateTime(now);
        wxMsgService.save(wxMsg);
        String destination = "/weixin/wx_msg" + wxMsg.getWxUserId();
        if (listWxAutoReply != null && listWxAutoReply.size() > 0) {
            WxAutoReply wxAutoReply = listWxAutoReply.get(0);
            wxMsg = new WxMsg();
            wxMsg.setWxUserId(wxUser.getId());
            wxMsg.setNickName(wxUser.getNickName());
            wxMsg.setHeadimgUrl(wxUser.getHeadimgUrl());
            wxMsg.setCreateTime(now.plusSeconds(1L));
            wxMsg.setType("2");
            wxMsg.setRepType(wxAutoReply.getRepType());
            if ("text".equals(wxAutoReply.getRepType())) {
                wxMsg.setRepContent(wxAutoReply.getRepContent());
                wxMpXmlOutMessage = ((TextBuilder)((TextBuilder)new TextBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).content(wxAutoReply.getRepContent()).build();
            }
            if ("image".equals(wxAutoReply.getRepType())) {
                wxMsg.setRepName(wxAutoReply.getRepName());
                wxMsg.setRepUrl(wxAutoReply.getRepUrl());
                wxMsg.setRepMediaId(wxAutoReply.getRepMediaId());
                wxMpXmlOutMessage = ((ImageBuilder)((ImageBuilder)new ImageBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).mediaId(wxAutoReply.getRepMediaId()).build();
            }
            if ("voice".equals(wxAutoReply.getRepType())) {
                wxMsg.setRepName(wxAutoReply.getRepName());
                wxMsg.setRepUrl(wxAutoReply.getRepUrl());
                wxMsg.setRepMediaId(wxAutoReply.getRepMediaId());
                wxMpXmlOutMessage = ((VoiceBuilder)((VoiceBuilder)new VoiceBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).mediaId(wxAutoReply.getRepMediaId()).build();
            }
            if ("video".equals(wxAutoReply.getRepType())) {
                wxMsg.setRepName(wxAutoReply.getRepName());
                wxMsg.setRepDesc(wxAutoReply.getRepDesc());
                wxMsg.setRepUrl(wxAutoReply.getRepUrl());
                wxMsg.setRepMediaId(wxAutoReply.getRepMediaId());
                wxMpXmlOutMessage = ((VideoBuilder)((VideoBuilder)new VideoBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).mediaId(wxAutoReply.getRepMediaId()).title(wxAutoReply.getRepName()).description(wxAutoReply.getRepDesc()).build();
            }
            if ("music".equals(wxAutoReply.getRepType())) {
                wxMsg.setRepName(wxAutoReply.getRepName());
                wxMsg.setRepDesc(wxAutoReply.getRepDesc());
                wxMsg.setRepUrl(wxAutoReply.getRepUrl());
                wxMsg.setRepHqUrl(wxAutoReply.getRepHqUrl());
                wxMsg.setRepThumbMediaId(wxAutoReply.getRepThumbMediaId());
                wxMsg.setRepThumbUrl(wxAutoReply.getRepThumbUrl());
                wxMpXmlOutMessage = ((MusicBuilder)((MusicBuilder)new MusicBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).thumbMediaId(wxAutoReply.getRepThumbMediaId()).title(wxAutoReply.getRepName()).description(wxAutoReply.getRepDesc()).musicUrl(wxAutoReply.getRepUrl()).hqMusicUrl(wxAutoReply.getRepHqUrl()).build();
            }
            if ("news".equals(wxAutoReply.getRepType())) {
                ArrayList<WxMpXmlOutNewsMessage.Item> list = new ArrayList<WxMpXmlOutNewsMessage.Item>();
                List<JSONObject> listJSONObject = wxAutoReply.getContent().getJSONArray("articles").toList(JSONObject.class);
                for (JSONObject jSONObject : listJSONObject) {
                    WxMpXmlOutNewsMessage.Item t = new WxMpXmlOutNewsMessage.Item();
                    t.setTitle(jSONObject.getStr("title"));
                    t.setDescription(jSONObject.getStr("digest"));
                    t.setPicUrl(jSONObject.getStr("thumbUrl"));
                    t.setUrl(jSONObject.getStr("url"));
                    list.add(t);
                }
                wxMsg.setRepName(wxAutoReply.getRepName());
                wxMsg.setRepDesc(wxAutoReply.getRepDesc());
                wxMsg.setRepUrl(wxAutoReply.getRepUrl());
                wxMsg.setRepMediaId(wxAutoReply.getRepMediaId());
                wxMsg.setContent(wxAutoReply.getContent());
                wxMpXmlOutMessage = ((NewsBuilder)((NewsBuilder)new NewsBuilder().fromUser(wxMessage.getToUser())).toUser(wxMessage.getFromUser())).articles(list).build();
            }
            wxMsgService.save(wxMsg);
        }
        return wxMpXmlOutMessage;
    }

    @Generated
    public MsgHandler(WxAutoReplyService wxAutoReplyService, WxUserMapper wxUserMapper, WxMsgService wxMsgService) {
        this.wxAutoReplyService = wxAutoReplyService;
        this.wxUserMapper = wxUserMapper;
        this.wxMsgService = wxMsgService;
    }
}

