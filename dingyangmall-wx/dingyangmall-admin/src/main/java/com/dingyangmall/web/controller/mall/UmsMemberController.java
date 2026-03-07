package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.R;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.mall.entity.UmsMember;
import com.dingyangmall.mall.service.UmsMemberService;
import com.dingyangmall.web.entity.WxMaUser;
import com.dingyangmall.web.mapper.WxMaUserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/mall/member")
public class UmsMemberController extends BaseController {

    private final UmsMemberService umsMemberService;
    private final WxMaUserMapper wxMaUserMapper;

    /**
     * 会员分页：以 wx_user 为主表，按手机号关联 ums_member，每条记录带积分、余额、等级、会员码等。
     * 请求参数：current/size（或 pageNum/pageSize），phone、nickname 模糊筛选。
     */
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:member:index')")
    public R getPage(@RequestParam(value = "current", defaultValue = "1") long current,
                     @RequestParam(value = "size", defaultValue = "10") long size,
                     @RequestParam(value = "pageNum", required = false) Long pageNum,
                     @RequestParam(value = "pageSize", required = false) Long pageSize,
                     @RequestParam(required = false) String phone,
                     @RequestParam(required = false) String nickname) {
        if (pageNum != null && pageNum > 0) current = pageNum;
        if (pageSize != null && pageSize > 0) size = pageSize;
        long offset = (current - 1) * size;
        long total = wxMaUserMapper.countPage(phone, nickname);
        List<WxMaUser> wxList = wxMaUserMapper.selectPage(offset, size, phone, nickname);
        List<Map<String, Object>> records = new ArrayList<>();
        for (WxMaUser wx : wxList) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", wx.getId());
            row.put("openid", wx.getOpenid());
            row.put("nickname", wx.getNickname());
            row.put("avatarUrl", wx.getAvatarUrl());
            row.put("phone", wx.getPhone());
            row.put("createTime", wx.getCreateTime());
            row.put("updateTime", wx.getUpdateTime());
            UmsMember member = StringUtils.isNotEmpty(wx.getPhone())
                    ? umsMemberService.getByPhone(wx.getPhone()) : null;
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
        Page<Map<String, Object>> result = new Page<>(current, size, total);
        result.setRecords(records);
        return R.ok(result);
    }

    /**
     * 会员详情：优先按 wx_user 主键 id 查询并附带 ums_member 积分等；否则按 ums_member 的 id 查并附带 wx 信息。
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:member:get')")
    public R getById(@PathVariable("id") String id) {
        WxMaUser wx = wxMaUserMapper.selectById(id);
        if (wx != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", wx.getId());
            data.put("openid", wx.getOpenid());
            data.put("nickname", wx.getNickname());
            data.put("avatarUrl", wx.getAvatarUrl());
            data.put("phone", wx.getPhone());
            data.put("createTime", wx.getCreateTime());
            data.put("updateTime", wx.getUpdateTime());
            UmsMember member = StringUtils.isNotEmpty(wx.getPhone())
                    ? umsMemberService.getByPhone(wx.getPhone()) : null;
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
        UmsMember member = umsMemberService.getById(id);
        if (member == null) {
            return R.fail("会员不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("member", member);
        if (StringUtils.isNotEmpty(member.getPhone())) {
            wx = wxMaUserMapper.selectByPhone(member.getPhone());
            data.put("wxOpenid", wx != null ? wx.getOpenid() : null);
            data.put("wxAvatarUrl", wx != null ? wx.getAvatarUrl() : null);
            data.put("wxNickname", wx != null ? wx.getNickname() : null);
        }
        return R.ok(data);
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:member:add')")
    public R save(@RequestBody UmsMember umsMember) {
        return R.ok(umsMemberService.save(umsMember));
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:member:edit')")
    public R update(@RequestBody UmsMember umsMember) {
        return R.ok(umsMemberService.updateById(umsMember));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:member:del')")
    public R remove(@PathVariable("id") String id) {
        return R.ok(umsMemberService.removeById(id));
    }
}
