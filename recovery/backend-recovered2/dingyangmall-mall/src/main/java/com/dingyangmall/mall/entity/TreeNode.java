/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class TreeNode {
    protected String id;
    protected String parentId;
    private Integer sort;
    protected List<TreeNode> children = new ArrayList<TreeNode>();

    public void addChildren(TreeNode treeNode) {
        this.children.add(treeNode);
    }

    public List<TreeNode> getChildren() {
        if (this.children.size() <= 0) {
            return null;
        }
        return this.children;
    }

    @Generated
    public TreeNode() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getParentId() {
        return this.parentId;
    }

    @Generated
    public Integer getSort() {
        return this.sort;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Generated
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Generated
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TreeNode)) {
            return false;
        }
        TreeNode other = (TreeNode)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$sort = this.getSort();
        Integer other$sort = other.getSort();
        if (this$sort == null ? other$sort != null : !((Object)this$sort).equals(other$sort)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$parentId = this.getParentId();
        String other$parentId = other.getParentId();
        if (this$parentId == null ? other$parentId != null : !this$parentId.equals(other$parentId)) {
            return false;
        }
        List<TreeNode> this$children = this.getChildren();
        List<TreeNode> other$children = other.getChildren();
        return !(this$children == null ? other$children != null : !((Object)this$children).equals(other$children));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TreeNode;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $sort = this.getSort();
        result = result * 59 + ($sort == null ? 43 : ((Object)$sort).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $parentId = this.getParentId();
        result = result * 59 + ($parentId == null ? 43 : $parentId.hashCode());
        List<TreeNode> $children = this.getChildren();
        result = result * 59 + ($children == null ? 43 : ((Object)$children).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "TreeNode(id=" + this.getId() + ", parentId=" + this.getParentId() + ", sort=" + this.getSort() + ", children=" + String.valueOf(this.getChildren()) + ")";
    }
}

