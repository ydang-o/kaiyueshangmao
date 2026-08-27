/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.entity;

import lombok.Generated;

public class ImageManager {
    private static final long serialVersionUID = 1L;
    private String url;
    private String thumb;
    private String tag;
    private String name;
    private Integer id;

    @Generated
    public ImageManager() {
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public String getThumb() {
        return this.thumb;
    }

    @Generated
    public String getTag() {
        return this.tag;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public Integer getId() {
        return this.id;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Generated
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ImageManager)) {
            return false;
        }
        ImageManager other = (ImageManager)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$url = this.getUrl();
        String other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) {
            return false;
        }
        String this$thumb = this.getThumb();
        String other$thumb = other.getThumb();
        if (this$thumb == null ? other$thumb != null : !this$thumb.equals(other$thumb)) {
            return false;
        }
        String this$tag = this.getTag();
        String other$tag = other.getTag();
        if (this$tag == null ? other$tag != null : !this$tag.equals(other$tag)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        return !(this$name == null ? other$name != null : !this$name.equals(other$name));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ImageManager;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $url = this.getUrl();
        result = result * 59 + ($url == null ? 43 : $url.hashCode());
        String $thumb = this.getThumb();
        result = result * 59 + ($thumb == null ? 43 : $thumb.hashCode());
        String $tag = this.getTag();
        result = result * 59 + ($tag == null ? 43 : $tag.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ImageManager(url=" + this.getUrl() + ", thumb=" + this.getThumb() + ", tag=" + this.getTag() + ", name=" + this.getName() + ", id=" + this.getId() + ")";
    }
}

