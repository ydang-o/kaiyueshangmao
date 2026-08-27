/*
 * Decompiled with CFR.
 */
package com.dingyangmall.system.domain;

import com.dingyangmall.common.annotation.Excel;
import com.dingyangmall.common.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SysConfig
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="\u53c2\u6570\u4e3b\u952e", cellType=Excel.ColumnType.NUMERIC)
    private Long configId;
    @Excel(name="\u53c2\u6570\u540d\u79f0")
    private String configName;
    @Excel(name="\u53c2\u6570\u952e\u540d")
    private String configKey;
    @Excel(name="\u53c2\u6570\u952e\u503c")
    private String configValue;
    @Excel(name="\u7cfb\u7edf\u5185\u7f6e", readConverterExp="Y=\u662f,N=\u5426")
    private String configType;

    public Long getConfigId() {
        return this.configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    @NotBlank(message="\u53c2\u6570\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a")
    @Size(min=0, max=100, message="\u53c2\u6570\u540d\u79f0\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26")
    public @NotBlank(message="\u53c2\u6570\u540d\u79f0\u4e0d\u80fd\u4e3a\u7a7a") @Size(min=0, max=100, message="\u53c2\u6570\u540d\u79f0\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26") String getConfigName() {
        return this.configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @NotBlank(message="\u53c2\u6570\u952e\u540d\u957f\u5ea6\u4e0d\u80fd\u4e3a\u7a7a")
    @Size(min=0, max=100, message="\u53c2\u6570\u952e\u540d\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26")
    public @NotBlank(message="\u53c2\u6570\u952e\u540d\u957f\u5ea6\u4e0d\u80fd\u4e3a\u7a7a") @Size(min=0, max=100, message="\u53c2\u6570\u952e\u540d\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc7100\u4e2a\u5b57\u7b26") String getConfigKey() {
        return this.configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    @NotBlank(message="\u53c2\u6570\u952e\u503c\u4e0d\u80fd\u4e3a\u7a7a")
    @Size(min=0, max=500, message="\u53c2\u6570\u952e\u503c\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc7500\u4e2a\u5b57\u7b26")
    public @NotBlank(message="\u53c2\u6570\u952e\u503c\u4e0d\u80fd\u4e3a\u7a7a") @Size(min=0, max=500, message="\u53c2\u6570\u952e\u503c\u957f\u5ea6\u4e0d\u80fd\u8d85\u8fc7500\u4e2a\u5b57\u7b26") String getConfigValue() {
        return this.configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigType() {
        return this.configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("configId", this.getConfigId()).append("configName", this.getConfigName()).append("configKey", this.getConfigKey()).append("configValue", this.getConfigValue()).append("configType", this.getConfigType()).append("createBy", this.getCreateBy()).append("createTime", this.getCreateTime()).append("updateBy", this.getUpdateBy()).append("updateTime", this.getUpdateTime()).append("remark", this.getRemark()).toString();
    }
}

