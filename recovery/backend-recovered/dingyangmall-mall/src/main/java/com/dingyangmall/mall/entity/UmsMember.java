/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.dingyangmall.common.annotation.Excel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Generated;

@TableName(value="ums_member")
public class UmsMember
extends Model<UmsMember> {
    private static final long serialVersionUID = 1L;
    @TableId(type=IdType.ASSIGN_ID)
    private Long id;
    @Excel(name="\u6635\u79f0")
    private String nickname;
    @Excel(name="\u771f\u5b9e\u59d3\u540d")
    private String realName;
    @Excel(name="\u624b\u673a\u53f7")
    private String phone;
    @Excel(name="\u8eab\u4efd\u7c7b\u578b")
    private String identityType;
    @Excel(name="\u4f1a\u5458\u7801")
    private String memberCode;
    @Excel(name="\u79ef\u5206")
    private Integer points;
    @Excel(name="\u4f59\u989d")
    private BigDecimal balance;
    @Excel(name="\u7b49\u7ea7")
    private Integer level;
    @Excel(name="\u5934\u50cf")
    private String avatar;
    @Excel(name="\u5bc6\u7801")
    private String password;
    @Excel(name="\u521b\u5efa\u65f6\u95f4")
    private LocalDateTime createTime;
    @Excel(name="\u66f4\u65b0\u65f6\u95f4")
    private LocalDateTime updateTime;
    @Excel(name="\u903b\u8f91\u5220\u9664\u6807\u8bb0")
    private String delFlag;

    @Generated
    public UmsMember() {
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getNickname() {
        return this.nickname;
    }

    @Generated
    public String getRealName() {
        return this.realName;
    }

    @Generated
    public String getPhone() {
        return this.phone;
    }

    @Generated
    public String getIdentityType() {
        return this.identityType;
    }

    @Generated
    public String getMemberCode() {
        return this.memberCode;
    }

    @Generated
    public Integer getPoints() {
        return this.points;
    }

    @Generated
    public BigDecimal getBalance() {
        return this.balance;
    }

    @Generated
    public Integer getLevel() {
        return this.level;
    }

    @Generated
    public String getAvatar() {
        return this.avatar;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Generated
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Generated
    public String getDelFlag() {
        return this.delFlag;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Generated
    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Generated
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Generated
    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    @Generated
    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    @Generated
    public void setPoints(Integer points) {
        this.points = points;
    }

    @Generated
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Generated
    public void setLevel(Integer level) {
        this.level = level;
    }

    @Generated
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Generated
    public void setPassword(String password) {
        this.password = password;
    }

    @Generated
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Generated
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Generated
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Generated
    public String toString() {
        return "UmsMember(id=" + this.getId() + ", nickname=" + this.getNickname() + ", realName=" + this.getRealName() + ", phone=" + this.getPhone() + ", identityType=" + this.getIdentityType() + ", memberCode=" + this.getMemberCode() + ", points=" + this.getPoints() + ", balance=" + String.valueOf(this.getBalance()) + ", level=" + this.getLevel() + ", avatar=" + this.getAvatar() + ", password=" + this.getPassword() + ", createTime=" + String.valueOf(this.getCreateTime()) + ", updateTime=" + String.valueOf(this.getUpdateTime()) + ", delFlag=" + this.getDelFlag() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UmsMember)) {
            return false;
        }
        UmsMember other = (UmsMember)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$points = this.getPoints();
        Integer other$points = other.getPoints();
        if (this$points == null ? other$points != null : !((Object)this$points).equals(other$points)) {
            return false;
        }
        Integer this$level = this.getLevel();
        Integer other$level = other.getLevel();
        if (this$level == null ? other$level != null : !((Object)this$level).equals(other$level)) {
            return false;
        }
        String this$nickname = this.getNickname();
        String other$nickname = other.getNickname();
        if (this$nickname == null ? other$nickname != null : !this$nickname.equals(other$nickname)) {
            return false;
        }
        String this$realName = this.getRealName();
        String other$realName = other.getRealName();
        if (this$realName == null ? other$realName != null : !this$realName.equals(other$realName)) {
            return false;
        }
        String this$phone = this.getPhone();
        String other$phone = other.getPhone();
        if (this$phone == null ? other$phone != null : !this$phone.equals(other$phone)) {
            return false;
        }
        String this$identityType = this.getIdentityType();
        String other$identityType = other.getIdentityType();
        if (this$identityType == null ? other$identityType != null : !this$identityType.equals(other$identityType)) {
            return false;
        }
        String this$memberCode = this.getMemberCode();
        String other$memberCode = other.getMemberCode();
        if (this$memberCode == null ? other$memberCode != null : !this$memberCode.equals(other$memberCode)) {
            return false;
        }
        BigDecimal this$balance = this.getBalance();
        BigDecimal other$balance = other.getBalance();
        if (this$balance == null ? other$balance != null : !((Object)this$balance).equals(other$balance)) {
            return false;
        }
        String this$avatar = this.getAvatar();
        String other$avatar = other.getAvatar();
        if (this$avatar == null ? other$avatar != null : !this$avatar.equals(other$avatar)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        LocalDateTime this$createTime = this.getCreateTime();
        LocalDateTime other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        LocalDateTime this$updateTime = this.getUpdateTime();
        LocalDateTime other$updateTime = other.getUpdateTime();
        if (this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime)) {
            return false;
        }
        String this$delFlag = this.getDelFlag();
        String other$delFlag = other.getDelFlag();
        return !(this$delFlag == null ? other$delFlag != null : !this$delFlag.equals(other$delFlag));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof UmsMember;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = super.hashCode();
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $points = this.getPoints();
        result = result * 59 + ($points == null ? 43 : ((Object)$points).hashCode());
        Integer $level = this.getLevel();
        result = result * 59 + ($level == null ? 43 : ((Object)$level).hashCode());
        String $nickname = this.getNickname();
        result = result * 59 + ($nickname == null ? 43 : $nickname.hashCode());
        String $realName = this.getRealName();
        result = result * 59 + ($realName == null ? 43 : $realName.hashCode());
        String $phone = this.getPhone();
        result = result * 59 + ($phone == null ? 43 : $phone.hashCode());
        String $identityType = this.getIdentityType();
        result = result * 59 + ($identityType == null ? 43 : $identityType.hashCode());
        String $memberCode = this.getMemberCode();
        result = result * 59 + ($memberCode == null ? 43 : $memberCode.hashCode());
        BigDecimal $balance = this.getBalance();
        result = result * 59 + ($balance == null ? 43 : ((Object)$balance).hashCode());
        String $avatar = this.getAvatar();
        result = result * 59 + ($avatar == null ? 43 : $avatar.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        LocalDateTime $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        LocalDateTime $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        String $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        return result;
    }
}

