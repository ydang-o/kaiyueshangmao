/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import cn.hutool.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class MenuButton
implements Serializable {
    private String type;
    private String name;
    private String key;
    private String url;
    private String media_id;
    private String appid;
    private String pagepath;
    private List<MenuButton> sub_button = new ArrayList<MenuButton>();
    private JSONObject content;
    private String repContent;
    private String repType;
    private String repName;
    private String repDesc;
    private String repUrl;
    private String repHqUrl;
    private String repThumbMediaId;
    private String repThumbUrl;
    private String article_id;

    @Generated
    public MenuButton() {
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getKey() {
        return this.key;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public String getMedia_id() {
        return this.media_id;
    }

    @Generated
    public String getAppid() {
        return this.appid;
    }

    @Generated
    public String getPagepath() {
        return this.pagepath;
    }

    @Generated
    public List<MenuButton> getSub_button() {
        return this.sub_button;
    }

    @Generated
    public JSONObject getContent() {
        return this.content;
    }

    @Generated
    public String getRepContent() {
        return this.repContent;
    }

    @Generated
    public String getRepType() {
        return this.repType;
    }

    @Generated
    public String getRepName() {
        return this.repName;
    }

    @Generated
    public String getRepDesc() {
        return this.repDesc;
    }

    @Generated
    public String getRepUrl() {
        return this.repUrl;
    }

    @Generated
    public String getRepHqUrl() {
        return this.repHqUrl;
    }

    @Generated
    public String getRepThumbMediaId() {
        return this.repThumbMediaId;
    }

    @Generated
    public String getRepThumbUrl() {
        return this.repThumbUrl;
    }

    @Generated
    public String getArticle_id() {
        return this.article_id;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setKey(String key) {
        this.key = key;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    @Generated
    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Generated
    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    @Generated
    public void setSub_button(List<MenuButton> sub_button) {
        this.sub_button = sub_button;
    }

    @Generated
    public void setContent(JSONObject content) {
        this.content = content;
    }

    @Generated
    public void setRepContent(String repContent) {
        this.repContent = repContent;
    }

    @Generated
    public void setRepType(String repType) {
        this.repType = repType;
    }

    @Generated
    public void setRepName(String repName) {
        this.repName = repName;
    }

    @Generated
    public void setRepDesc(String repDesc) {
        this.repDesc = repDesc;
    }

    @Generated
    public void setRepUrl(String repUrl) {
        this.repUrl = repUrl;
    }

    @Generated
    public void setRepHqUrl(String repHqUrl) {
        this.repHqUrl = repHqUrl;
    }

    @Generated
    public void setRepThumbMediaId(String repThumbMediaId) {
        this.repThumbMediaId = repThumbMediaId;
    }

    @Generated
    public void setRepThumbUrl(String repThumbUrl) {
        this.repThumbUrl = repThumbUrl;
    }

    @Generated
    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MenuButton)) {
            return false;
        }
        MenuButton other = (MenuButton)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$key = this.getKey();
        String other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) {
            return false;
        }
        String this$media_id = this.getMedia_id();
        String other$media_id = other.getMedia_id();
        if (this$media_id == null ? other$media_id != null : !this$media_id.equals(other$media_id)) {
            return false;
        }
        String this$appid = this.getAppid();
        String other$appid = other.getAppid();
        if (this$appid == null ? other$appid != null : !this$appid.equals(other$appid)) {
            return false;
        }
        String this$pagepath = this.getPagepath();
        String other$pagepath = other.getPagepath();
        if (this$pagepath == null ? other$pagepath != null : !this$pagepath.equals(other$pagepath)) {
            return false;
        }
        List<MenuButton> this$sub_button = this.getSub_button();
        List<MenuButton> other$sub_button = other.getSub_button();
        if (this$sub_button == null ? other$sub_button != null : !((Object)this$sub_button).equals(other$sub_button)) {
            return false;
        }
        JSONObject this$content = this.getContent();
        JSONObject other$content = other.getContent();
        if (this$content == null ? other$content != null : !((Object)this$content).equals(other$content)) {
            return false;
        }
        String this$repContent = this.getRepContent();
        String other$repContent = other.getRepContent();
        if (this$repContent == null ? other$repContent != null : !this$repContent.equals(other$repContent)) {
            return false;
        }
        String this$repType = this.getRepType();
        String other$repType = other.getRepType();
        if (this$repType == null ? other$repType != null : !this$repType.equals(other$repType)) {
            return false;
        }
        String this$repName = this.getRepName();
        String other$repName = other.getRepName();
        if (this$repName == null ? other$repName != null : !this$repName.equals(other$repName)) {
            return false;
        }
        String this$repDesc = this.getRepDesc();
        String other$repDesc = other.getRepDesc();
        if (this$repDesc == null ? other$repDesc != null : !this$repDesc.equals(other$repDesc)) {
            return false;
        }
        String this$repUrl = this.getRepUrl();
        String other$repUrl = other.getRepUrl();
        if (this$repUrl == null ? other$repUrl != null : !this$repUrl.equals(other$repUrl)) {
            return false;
        }
        String this$repHqUrl = this.getRepHqUrl();
        String other$repHqUrl = other.getRepHqUrl();
        if (this$repHqUrl == null ? other$repHqUrl != null : !this$repHqUrl.equals(other$repHqUrl)) {
            return false;
        }
        String this$repThumbMediaId = this.getRepThumbMediaId();
        String other$repThumbMediaId = other.getRepThumbMediaId();
        if (this$repThumbMediaId == null ? other$repThumbMediaId != null : !this$repThumbMediaId.equals(other$repThumbMediaId)) {
            return false;
        }
        String this$repThumbUrl = this.getRepThumbUrl();
        String other$repThumbUrl = other.getRepThumbUrl();
        if (this$repThumbUrl == null ? other$repThumbUrl != null : !this$repThumbUrl.equals(other$repThumbUrl)) {
            return false;
        }
        String this$article_id = this.getArticle_id();
        String other$article_id = other.getArticle_id();
        return !(this$article_id == null ? other$article_id != null : !this$article_id.equals(other$article_id));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof MenuButton;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $media_id = this.getMedia_id();
        result = result * 59 + ($media_id == null ? 43 : $media_id.hashCode());
        String $appid = this.getAppid();
        result = result * 59 + ($appid == null ? 43 : $appid.hashCode());
        String $pagepath = this.getPagepath();
        result = result * 59 + ($pagepath == null ? 43 : $pagepath.hashCode());
        List<MenuButton> $sub_button = this.getSub_button();
        result = result * 59 + ($sub_button == null ? 43 : ((Object)$sub_button).hashCode());
        JSONObject $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : ((Object)$content).hashCode());
        String $repContent = this.getRepContent();
        result = result * 59 + ($repContent == null ? 43 : $repContent.hashCode());
        String $repType = this.getRepType();
        result = result * 59 + ($repType == null ? 43 : $repType.hashCode());
        String $repName = this.getRepName();
        result = result * 59 + ($repName == null ? 43 : $repName.hashCode());
        String $repDesc = this.getRepDesc();
        result = result * 59 + ($repDesc == null ? 43 : $repDesc.hashCode());
        String $repUrl = this.getRepUrl();
        result = result * 59 + ($repUrl == null ? 43 : $repUrl.hashCode());
        String $repHqUrl = this.getRepHqUrl();
        result = result * 59 + ($repHqUrl == null ? 43 : $repHqUrl.hashCode());
        String $repThumbMediaId = this.getRepThumbMediaId();
        result = result * 59 + ($repThumbMediaId == null ? 43 : $repThumbMediaId.hashCode());
        String $repThumbUrl = this.getRepThumbUrl();
        result = result * 59 + ($repThumbUrl == null ? 43 : $repThumbUrl.hashCode());
        String $article_id = this.getArticle_id();
        result = result * 59 + ($article_id == null ? 43 : $article_id.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "MenuButton(type=" + this.getType() + ", name=" + this.getName() + ", key=" + this.getKey() + ", url=" + this.getUrl() + ", media_id=" + this.getMedia_id() + ", appid=" + this.getAppid() + ", pagepath=" + this.getPagepath() + ", sub_button=" + String.valueOf(this.getSub_button()) + ", content=" + String.valueOf(this.getContent()) + ", repContent=" + this.getRepContent() + ", repType=" + this.getRepType() + ", repName=" + this.getRepName() + ", repDesc=" + this.getRepDesc() + ", repUrl=" + this.getRepUrl() + ", repHqUrl=" + this.getRepHqUrl() + ", repThumbMediaId=" + this.getRepThumbMediaId() + ", repThumbUrl=" + this.getRepThumbUrl() + ", article_id=" + this.getArticle_id() + ")";
    }
}

