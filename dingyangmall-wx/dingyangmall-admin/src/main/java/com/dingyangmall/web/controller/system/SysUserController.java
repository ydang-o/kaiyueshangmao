package com.dingyangmall.web.controller.system;

import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.domain.entity.SysUser;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.constant.HttpStatus;
import com.dingyangmall.system.service.ISysUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户（账户）管理：一级/二级经销商列表，二级标明从属关系。
 * 与「会员管理」区分：会员管理用 wx_user + ums_member；本接口用 sys_user。
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService userService;

    /**
     * 用户列表：一级/二级经销商等，支持按 dealerLevel、parentDistributorId 筛选；
     * 二级经销商的从属关系通过 parentDistributorId、parentDistributorName 体现。
     * 请求参数：pageNum, pageSize, userName, phonenumber, status, dealerLevel（1=一级 2=二级）
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (SysUser u : list) {
            Map<String, Object> row = new HashMap<>();
            row.put("userId", u.getUserId());
            row.put("userName", u.getUserName());
            row.put("nickName", u.getNickName());
            row.put("phonenumber", u.getPhonenumber());
            row.put("status", u.getStatus());
            row.put("dealerLevel", u.getDealerLevel());
            row.put("parentDistributorId", u.getParentDistributorId());
            row.put("deptId", u.getDeptId());
            row.put("createTime", u.getCreateTime());
            if (u.getParentDistributorId() != null) {
                SysUser parent = userService.selectUserById(u.getParentDistributorId());
                row.put("parentDistributorName", parent != null ? parent.getNickName() : null);
            } else {
                row.put("parentDistributorName", null);
            }
            rows.add(row);
        }
        TableDataInfo dataInfo = new TableDataInfo();
        dataInfo.setCode(HttpStatus.SUCCESS);
        dataInfo.setMsg("查询成功");
        dataInfo.setRows(rows);
        dataInfo.setTotal(new PageInfo(list).getTotal());
        return dataInfo;
    }

    /**
     * 用户详情（含从属关系）
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        SysUser user = userService.selectUserById(userId);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        if (user.getParentDistributorId() != null) {
            SysUser parent = userService.selectUserById(user.getParentDistributorId());
            data.put("parentDistributorName", parent != null ? parent.getNickName() : null);
        }
        return AjaxResult.success(data);
    }
}
