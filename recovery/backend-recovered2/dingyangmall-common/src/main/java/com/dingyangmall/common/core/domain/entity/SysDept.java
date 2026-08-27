/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.core.domain.entity;

import com.dingyangmall.common.core.domain.BaseEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysDept
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long deptId;
    private Long parentId;
    private String ancestors;
    private String deptName;
    private Integer orderNum;
    private String leader;
    private String phone;
    private String email;
    private String status;
    private String delFlag;
    private String parentName;
    private List<SysDept> children = new ArrayList<SysDept>();

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return this.ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotBlank(message="\u90e8\u95e8\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Size(min=0, max=30, message="\u90e8\u95e8\u540d\u79f0\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc730\u4e2a\u5b57\u7b26")
    public @NotBlank(message="\u90e8\u95e8\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Size(min=0, max=30, message="\u90e8\u95e8\u540d\u79f0\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc730\u4e2a\u5b57\u7b26") String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @NotNull(message="\u663e\u793a\u987a\u5e8f\u4e0d\u80fd\u4e3a\u7a7a")
    public @NotNull(message="\u663e\u793a\u987a\u5e8f\u4e0d\u80fd\u4e3a\u7a7a") Integer getOrderNum() {
        return this.orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getLeader() {
        return this.leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Size(min=0, max=11, message="\u8054\u7cfb\u7535\u8bdd\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc711\u4e2a\u5b57\u7b26")
    public @Size(min=0, max=11, message="\u8054\u7cfb\u7535\u8bdd\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc711\u4e2a\u5b57\u7b26") String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Email(message="\u90ae\u7bb1\u683c\u5f0f\u4e0d\u6b63\u786e")
    @Size(min=0, max=50, message="\u90ae\u7bb1\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc750\u4e2a\u5b57\u7b26")
    public @Email(message="\u90ae\u7bb1\u683c\u5f0f\u4e0d\u6b63\u786e") @Size(min=0, max=50, message="\u90ae\u7bb1\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc750\u4e2a\u5b57\u7b26") String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysDept> getChildren() {
        return this.children;
    }

    public void setChildren(List<SysDept> children) {
        this.children = children;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("deptId", this.getDeptId()).append("parentId", this.getParentId()).append("ancestors", this.getAncestors()).append("deptName", this.getDeptName()).append("orderNum", this.getOrderNum()).append("leader", this.getLeader()).append("phone", this.getPhone()).append("email", this.getEmail()).append("status", this.getStatus()).append("delFlag", this.getDelFlag()).append("createBy", this.getCreateBy()).append("createTime", this.getCreateTime()).append("updateBy", this.getUpdateBy()).append("updateTime", this.getUpdateTime()).toString();
    }
}

