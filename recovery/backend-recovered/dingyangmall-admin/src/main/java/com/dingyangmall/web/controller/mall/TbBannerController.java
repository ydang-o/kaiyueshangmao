/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.common.exception.file.InvalidExtensionException;
import com.dingyangmall.common.utils.file.FileUploadUtils;
import com.dingyangmall.common.utils.file.MimeTypeUtils;
import com.dingyangmall.mall.entity.TbBanner;
import com.dingyangmall.mall.service.TbBannerService;
import com.dingyangmall.web.utils.AdminImageUtils;
import java.io.File;
import java.io.IOException;
import lombok.Generated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/mall/banner", "/dev-api/mall/banner"})
public class TbBannerController
extends BaseController {
    private final TbBannerService bannerService;

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:banner:index')")
    public TableDataInfo getBannerPage(Page<TbBanner> page, TbBanner banner) {
        this.bannerService.page(page, (Wrapper)Wrappers.query(banner).lambda().orderByAsc(TbBanner::getSort));
        page.getRecords().forEach(b -> b.setPicUrl(AdminImageUtils.toLocalOrDefault(b.getPicUrl())));
        return this.getDataTable(page.getRecords());
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:banner:get')")
    public AjaxResult getById(@PathVariable Long id) {
        TbBanner banner = (TbBanner)this.bannerService.getById(id);
        if (banner != null) {
            banner.setPicUrl(AdminImageUtils.toLocalOrDefault(banner.getPicUrl()));
        }
        return AjaxResult.success(banner);
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:banner:add')")
    public AjaxResult save(@RequestBody TbBanner banner) {
        if (banner.getPicUrl() == null || banner.getPicUrl().trim().isEmpty()) {
            banner.setPicUrl("/profile/static/logo.png");
        }
        return this.toAjax(this.bannerService.save(banner));
    }

    @PostMapping(value={"/upload"})
    @PreAuthorize(value="@ss.hasPermi('mall:banner:add')")
    public AjaxResult saveWithFile(@RequestParam(value="file", required=false) MultipartFile file, @RequestParam(value="title", required=false) String title, @RequestParam(value="sort", required=false, defaultValue="0") Integer sort, @RequestParam(value="linkUrl", required=false) String linkUrl, @RequestParam(value="linkType", required=false, defaultValue="0") String linkType, @RequestParam(value="status", required=false, defaultValue="1") String status) throws Exception {
        String path;
        TbBanner banner = new TbBanner();
        banner.setTitle(title);
        banner.setSort(sort);
        banner.setLinkUrl(linkUrl);
        banner.setLinkType(linkType);
        banner.setStatus(status);
        banner.setPicUrl("/profile/static/logo.png");
        if (file != null && !file.isEmpty() && (path = this.saveBannerImageToLocal(file)) != null) {
            banner.setPicUrl(path);
        }
        return this.toAjax(this.bannerService.save(banner));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:banner:edit')")
    public AjaxResult update(@RequestBody TbBanner banner) {
        return this.toAjax(this.bannerService.updateById(banner));
    }

    @PostMapping(value={"/updateWithFile"})
    @PreAuthorize(value="@ss.hasPermi('mall:banner:edit')")
    public AjaxResult updateWithFile(@RequestParam(value="id") Long id, @RequestParam(value="file", required=false) MultipartFile file, @RequestParam(value="title", required=false) String title, @RequestParam(value="sort", required=false) Integer sort, @RequestParam(value="linkUrl", required=false) String linkUrl, @RequestParam(value="linkType", required=false) String linkType, @RequestParam(value="status", required=false) String status) throws Exception {
        String path;
        TbBanner banner = (TbBanner)this.bannerService.getById(id);
        if (banner == null) {
            return AjaxResult.error("\u8f6e\u64ad\u56fe\u4e0d\u5b58\u5728");
        }
        if (title != null) {
            banner.setTitle(title);
        }
        if (sort != null) {
            banner.setSort(sort);
        }
        if (linkUrl != null) {
            banner.setLinkUrl(linkUrl);
        }
        if (linkType != null) {
            banner.setLinkType(linkType);
        }
        if (status != null) {
            banner.setStatus(status);
        }
        if (file != null && !file.isEmpty() && (path = this.saveBannerImageToLocal(file)) != null) {
            banner.setPicUrl(path);
        }
        return this.toAjax(this.bannerService.updateById(banner));
    }

    private String saveBannerImageToLocal(MultipartFile file) throws IOException, InvalidExtensionException {
        String profile = DingyangmallConfig.getProfile();
        if (profile == null || profile.trim().isEmpty()) {
            return null;
        }
        Object bannerDir = profile.replace("\\", "/").trim();
        if (!((String)bannerDir).endsWith("/")) {
            bannerDir = (String)bannerDir + "/";
        }
        bannerDir = (String)bannerDir + "upload/banner";
        new File((String)bannerDir).mkdirs();
        return FileUploadUtils.upload((String)bannerDir, file, MimeTypeUtils.IMAGE_EXTENSION);
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:banner:del')")
    public AjaxResult remove(@PathVariable Long id) {
        return this.toAjax(this.bannerService.removeById(id));
    }

    @Generated
    public TbBannerController(TbBannerService bannerService) {
        this.bannerService = bannerService;
    }
}

