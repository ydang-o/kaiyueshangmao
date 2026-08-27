/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyangmall.weixin.entity.Menu;
import com.dingyangmall.weixin.entity.MenuButton;
import com.dingyangmall.weixin.entity.WxMenu;
import com.dingyangmall.weixin.mapper.WxMenuMapper;
import com.dingyangmall.weixin.service.WxMenuService;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WxMenuServiceImpl
extends ServiceImpl<WxMenuMapper, WxMenu>
implements WxMenuService {
    private final WxMpService wxService;

    @Override
    public String getWxMenuButton() {
        List listWxMenu = ((WxMenuMapper)this.baseMapper).selectList((Wrapper)((LambdaQueryWrapper)Wrappers.query().lambda().eq(WxMenu::getParentId, "0")).orderByAsc(WxMenu::getSort));
        Menu menu = new Menu();
        ArrayList<MenuButton> listMenuButton = new ArrayList<MenuButton>();
        if (listWxMenu != null && listWxMenu.size() > 0) {
            for (WxMenu wxMenu : listWxMenu) {
                MenuButton menuButton = new MenuButton();
                menuButton.setName(wxMenu.getName());
                String type = wxMenu.getType();
                if (StringUtils.isNotBlank(type)) {
                    menuButton.setType(type);
                    this.setButtonValue(menuButton, wxMenu);
                } else {
                    List listWxMenu1 = ((WxMenuMapper)this.baseMapper).selectList((Wrapper)((LambdaQueryWrapper)Wrappers.query().lambda().eq(WxMenu::getParentId, wxMenu.getId())).orderByAsc(WxMenu::getSort));
                    ArrayList<MenuButton> subButtons = new ArrayList<MenuButton>();
                    for (WxMenu wxMenu1 : listWxMenu1) {
                        MenuButton subButton = new MenuButton();
                        String type1 = wxMenu1.getType();
                        subButton.setName(wxMenu1.getName());
                        subButton.setType(type1);
                        this.setButtonValue(subButton, wxMenu1);
                        subButtons.add(subButton);
                    }
                    menuButton.setSub_button(subButtons);
                }
                listMenuButton.add(menuButton);
            }
        }
        menu.setButton(listMenuButton);
        return menu.toString();
    }

    void setButtonValue(MenuButton menuButton, WxMenu wxMenu) {
        menuButton.setKey(wxMenu.getId());
        menuButton.setUrl(wxMenu.getUrl());
        menuButton.setContent(wxMenu.getContent());
        menuButton.setRepContent(wxMenu.getRepContent());
        menuButton.setMedia_id(wxMenu.getRepMediaId());
        menuButton.setRepType(wxMenu.getRepType());
        menuButton.setRepName(wxMenu.getRepName());
        menuButton.setAppid(wxMenu.getMaAppId());
        menuButton.setPagepath(wxMenu.getMaPagePath());
        menuButton.setUrl(wxMenu.getUrl());
        menuButton.setRepUrl(wxMenu.getRepUrl());
        menuButton.setRepHqUrl(wxMenu.getRepHqUrl());
        menuButton.setRepDesc(wxMenu.getRepDesc());
        menuButton.setRepThumbMediaId(wxMenu.getRepThumbMediaId());
        menuButton.setRepThumbUrl(wxMenu.getRepThumbUrl());
    }

    @Override
    @Transactional(rollbackFor={Exception.class})
    public void saveAndRelease(String strWxMenu) throws WxErrorException {
        Menu menu = Menu.fromJson(strWxMenu);
        List<MenuButton> buttons = menu.getButton();
        ((WxMenuMapper)this.baseMapper).delete(Wrappers.query().lambda());
        WxMenu wxMenu = null;
        WxMenu wxMenu1 = null;
        int sort1 = 1;
        int sort2 = 1;
        for (MenuButton menuButton : buttons) {
            wxMenu = new WxMenu();
            this.setWxMenuValue(wxMenu, menuButton);
            wxMenu.setSort(sort1);
            wxMenu.setParentId("0");
            ((WxMenuMapper)this.baseMapper).insert(wxMenu);
            menuButton.setKey(wxMenu.getId());
            ++sort1;
            for (MenuButton menuButton1 : menuButton.getSub_button()) {
                wxMenu1 = new WxMenu();
                this.setWxMenuValue(wxMenu1, menuButton1);
                wxMenu1.setSort(sort2);
                wxMenu1.setParentId(wxMenu.getId());
                ((WxMenuMapper)this.baseMapper).insert(wxMenu1);
                menuButton1.setKey(wxMenu1.getId());
                ++sort2;
            }
        }
        this.wxService.getMenuService().menuCreate(menu.toString());
    }

    void setWxMenuValue(WxMenu wxMenu, MenuButton menuButton) {
        wxMenu.setId(menuButton.getKey());
        wxMenu.setType(menuButton.getType());
        wxMenu.setName(menuButton.getName());
        wxMenu.setUrl(menuButton.getUrl());
        wxMenu.setRepMediaId(menuButton.getMedia_id());
        wxMenu.setRepType(menuButton.getRepType());
        wxMenu.setRepName(menuButton.getRepName());
        wxMenu.setMaAppId(menuButton.getAppid());
        wxMenu.setMaPagePath(menuButton.getPagepath());
        wxMenu.setRepContent(menuButton.getRepContent());
        wxMenu.setContent(menuButton.getContent());
        wxMenu.setRepUrl(menuButton.getRepUrl());
        wxMenu.setRepHqUrl(menuButton.getRepHqUrl());
        wxMenu.setRepDesc(menuButton.getRepDesc());
        wxMenu.setRepThumbMediaId(menuButton.getRepThumbMediaId());
        wxMenu.setRepThumbUrl(menuButton.getRepThumbUrl());
        menuButton.setRepUrl(null);
        menuButton.setRepDesc(null);
        menuButton.setRepHqUrl(null);
        menuButton.setContent(null);
        menuButton.setRepContent(null);
        menuButton.setRepType(null);
        menuButton.setRepName(null);
        menuButton.setRepThumbMediaId(null);
        menuButton.setRepThumbUrl(null);
    }

    @Generated
    public WxMenuServiceImpl(WxMpService wxService) {
        this.wxService = wxService;
    }
}

