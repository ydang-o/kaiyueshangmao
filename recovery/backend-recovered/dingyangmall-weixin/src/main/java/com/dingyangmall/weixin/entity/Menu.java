/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import cn.hutool.json.JSONUtil;
import com.dingyangmall.weixin.entity.MenuButton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import me.chanjar.weixin.common.bean.menu.WxMenuRule;

public class Menu
implements Serializable {
    private static final long serialVersionUID = -7083914585539687746L;
    private List<MenuButton> button = new ArrayList<MenuButton>();
    private WxMenuRule matchrule;

    public static Menu fromJson(String json) {
        return JSONUtil.parseObj(json).toBean(Menu.class);
    }

    public String toJson() {
        return JSONUtil.toJsonStr(this);
    }

    public String toString() {
        return this.toJson();
    }

    @Generated
    public Menu() {
    }

    @Generated
    public List<MenuButton> getButton() {
        return this.button;
    }

    @Generated
    public WxMenuRule getMatchrule() {
        return this.matchrule;
    }

    @Generated
    public void setButton(List<MenuButton> button) {
        this.button = button;
    }

    @Generated
    public void setMatchrule(WxMenuRule matchrule) {
        this.matchrule = matchrule;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        Menu other = (Menu)o;
        if (!other.canEqual(this)) {
            return false;
        }
        List<MenuButton> this$button = this.getButton();
        List<MenuButton> other$button = other.getButton();
        if (this$button == null ? other$button != null : !((Object)this$button).equals(other$button)) {
            return false;
        }
        WxMenuRule this$matchrule = this.getMatchrule();
        WxMenuRule other$matchrule = other.getMatchrule();
        return !(this$matchrule == null ? other$matchrule != null : !((Object)this$matchrule).equals(other$matchrule));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Menu;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List<MenuButton> $button = this.getButton();
        result = result * 59 + ($button == null ? 43 : ((Object)$button).hashCode());
        WxMenuRule $matchrule = this.getMatchrule();
        result = result * 59 + ($matchrule == null ? 43 : ((Object)$matchrule).hashCode());
        return result;
    }
}

