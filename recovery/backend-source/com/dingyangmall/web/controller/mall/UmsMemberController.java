/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.baomidou.mybatisplus.extension.plugins.pagination.Page
 *  com.dingyangmall.common.core.controller.BaseController
 *  com.dingyangmall.common.core.domain.R
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.mall.entity.UmsMember
 *  com.dingyangmall.mall.service.UmsMemberService
 *  lombok.Generated
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.R;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.web.entity.WxMaUser;
import com.dingyangmall.web.mapper.WxMaUserMapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Generated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/mall/member"})
public class UmsMemberController
extends BaseController {
    private final UmsMemberService umsMemberService;
    private final WxMaUserMapper wxMaUserMapper;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:member:index')")
    public R getPage(@RequestParam(value="current", defaultValue="1") long current, @RequestParam(value="size", defaultValue="10") long size, @RequestParam(value="pageNum", required=false) Long pageNum, @RequestParam(value="pageSize", required=false) Long pageSize, @RequestParam(required=false) String phone, @RequestParam(required=false) String nickname) {
        if (pageNum != null && pageNum > 0L) {
            current = pageNum;
        }
        if (pageSize != null && pageSize > 0L) {
            size = pageSize;
        }
        long offset = (current - 1L) * size;
        long total = this.wxMaUserMapper.countPage(phone, nickname);
        List<WxMaUser> wxList = this.wxMaUserMapper.selectPage(offset, size, phone, nickname);
        ArrayList records = new ArrayList();
        for (WxMaUser wx : wxList) {
            UmsMember member;
            HashMap<String, Object> row = new HashMap<String, Object>();
            row.put("id", wx.getId());
            row.put("openid", wx.getOpenid());
            row.put("nickname", wx.getNickname());
            row.put("avatarUrl", wx.getAvatarUrl());
            row.put("phone", wx.getPhone());
            row.put("createTime", wx.getCreateTime());
            row.put("updateTime", wx.getUpdateTime());
            UmsMember umsMember = member = StringUtils.isNotEmpty((String)wx.getPhone()) ? this.umsMemberService.getByPhone(wx.getPhone()) : null;
            if (member != null) {
                row.put("memberId", member.getId());
                row.put("points", member.getPoints());
                row.put("balance", member.getBalance());
                row.put("level", member.getLevel());
                row.put("memberCode", member.getMemberCode());
            } else {
                row.put("memberId", null);
                row.put("points", null);
                row.put("balance", null);
                row.put("level", null);
                row.put("memberCode", null);
            }
            records.add(row);
        }
        Page result = new Page(current, size, total);
        result.setRecords(records);
        return R.ok((Object)result);
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:member:get')")
    public R getById(@PathVariable(value="id") String id) {
        WxMaUser wx = this.wxMaUserMapper.selectById(id);
        if (wx != null) {
            UmsMember member;
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("id", wx.getId());
            data.put("openid", wx.getOpenid());
            data.put("nickname", wx.getNickname());
            data.put("avatarUrl", wx.getAvatarUrl());
            data.put("phone", wx.getPhone());
            data.put("createTime", wx.getCreateTime());
            data.put("updateTime", wx.getUpdateTime());
            UmsMember umsMember = member = StringUtils.isNotEmpty((String)wx.getPhone()) ? this.umsMemberService.getByPhone(wx.getPhone()) : null;
            if (member != null) {
                data.put("memberId", member.getId());
                data.put("points", member.getPoints());
                data.put("balance", member.getBalance());
                data.put("level", member.getLevel());
                data.put("memberCode", member.getMemberCode());
            } else {
                data.put("memberId", null);
                data.put("points", null);
                data.put("balance", null);
                data.put("level", null);
                data.put("memberCode", null);
            }
            return R.ok(data);
        }
        UmsMember member = (UmsMember)this.umsMemberService.getById((Serializable)((Object)id));
        if (member == null) {
            return R.fail((String)"\u4f1a\u5458\u4e0d\u5b58\u5728");
        }
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("member", member);
        if (StringUtils.isNotEmpty((String)member.getPhone())) {
            wx = this.wxMaUserMapper.selectByPhone(member.getPhone());
            data.put("wxOpenid", wx != null ? wx.getOpenid() : null);
            data.put("wxAvatarUrl", wx != null ? wx.getAvatarUrl() : null);
            data.put("wxNickname", wx != null ? wx.getNickname() : null);
        }
        return R.ok(data);
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:member:add')")
    public R save(@RequestBody UmsMember umsMember) {
        return R.ok((Object)this.umsMemberService.save((Object)umsMember));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:member:edit')")
    public R update(@RequestBody UmsMember umsMember) {
        return R.ok((Object)this.umsMemberService.updateById((Object)umsMember));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:member:del')")
    public R remove(@PathVariable(value="id") String id) {
        return R.ok((Object)this.umsMemberService.removeById((Serializable)((Object)id)));
    }

    @Generated
    public UmsMemberController(UmsMemberService umsMemberService, WxMaUserMapper wxMaUserMapper) {
        this.umsMemberService = umsMemberService;
        this.wxMaUserMapper = wxMaUserMapper;
    }
}

