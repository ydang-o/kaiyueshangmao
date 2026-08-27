/*
 * Decompiled with CFR.
 */
package com.dingyangmall.mall.util;

import com.dingyangmall.mall.entity.TreeNode;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public final class TreeUtil {
    public static <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {
        ArrayList<TreeNode> trees = new ArrayList<TreeNode>();
        for (TreeNode treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }
            for (TreeNode it : treeNodes) {
                if (!it.getParentId().equals(treeNode.getId())) continue;
                treeNode.addChildren(it);
            }
        }
        return trees;
    }

    public static <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
        ArrayList<TreeNode> trees = new ArrayList<TreeNode>();
        for (TreeNode treeNode : treeNodes) {
            if (!root.equals(treeNode.getParentId())) continue;
            trees.add(TreeUtil.findChildren(treeNode, treeNodes));
        }
        return trees;
    }

    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (TreeNode it : treeNodes) {
            if (treeNode.getId() != it.getParentId()) continue;
            if (treeNode.getChildren() == null) {
                treeNode.setChildren(new ArrayList<TreeNode>());
            }
            treeNode.addChildren(TreeUtil.findChildren(it, treeNodes));
        }
        return treeNode;
    }

    @Generated
    private TreeUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

