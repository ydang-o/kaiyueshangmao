package com.dingyangmall.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.core.page.TableDataInfo;
import com.dingyangmall.mall.entity.TbBanner;
import com.dingyangmall.mall.service.TbBannerService;
import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.exception.file.InvalidExtensionException;
import com.dingyangmall.common.utils.file.FileUploadUtils;
import com.dingyangmall.common.utils.file.MimeTypeUtils;
import com.dingyangmall.web.utils.AdminImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 首页轮播图管理 Controller
 *
 * @author dingyangmall
 */
@RestController
@AllArgsConstructor
@RequestMapping({"/mall/banner", "/dev-api/mall/banner"})
public class TbBannerController extends BaseController {

    private final TbBannerService bannerService;

    /**
     * 分页查询轮播图
     */
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:banner:index')")
    public TableDataInfo getBannerPage(Page<TbBanner> page, TbBanner banner) {
        bannerService.page(page, Wrappers.query(banner).lambda().orderByAsc(TbBanner::getSort));
        // 统一为管理端可加载的图片地址（/dev-api/profile/...）
        page.getRecords().forEach(b -> b.setPicUrl(AdminImageUtils.toLocalOrDefault(b.getPicUrl())));
        return getDataTable(page.getRecords());
    }

    /**
     * 获取轮播图详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:banner:get')")
    public AjaxResult getById(@PathVariable Long id) {
        TbBanner banner = bannerService.getById(id);
        if (banner != null) {
            banner.setPicUrl(AdminImageUtils.toLocalOrDefault(banner.getPicUrl()));
        }
        return AjaxResult.success(banner);
    }

    /**
     * 新增轮播图（JSON 格式）
     */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:banner:add')")
    public AjaxResult save(@RequestBody TbBanner banner) {
        if (banner.getPicUrl() == null || banner.getPicUrl().trim().isEmpty()) {
            banner.setPicUrl(com.dingyangmall.common.constant.Constants.RESOURCE_PREFIX + "/static/logo.png");
        }
        return toAjax(bannerService.save(banner));
    }

    /**
     * 新增轮播图（支持直接上传文件）
     */
    @PostMapping("/upload")
    @PreAuthorize("@ss.hasPermi('mall:banner:add')")
    public AjaxResult saveWithFile(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam(value = "title", required = false) String title,
                                  @RequestParam(value = "sort", required = false, defaultValue = "0") Integer sort,
                                  @RequestParam(value = "linkUrl", required = false) String linkUrl,
                                  @RequestParam(value = "linkType", required = false, defaultValue = "0") String linkType,
                                  @RequestParam(value = "status", required = false, defaultValue = "1") String status) throws Exception {
        TbBanner banner = new TbBanner();
        banner.setTitle(title);
        banner.setSort(sort);
        banner.setLinkUrl(linkUrl);
        banner.setLinkType(linkType);
        banner.setStatus(status);
        // 未上传图片时给默认值，避免 pic_url NOT NULL 报错
        banner.setPicUrl(com.dingyangmall.common.constant.Constants.RESOURCE_PREFIX + "/static/logo.png");

        if (file != null && !file.isEmpty()) {
            String path = saveBannerImageToLocal(file);
            if (path != null) banner.setPicUrl(path);
        }

        return toAjax(bannerService.save(banner));
    }

    /**
     * 修改轮播图（JSON 格式）
     */
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:banner:edit')")
    public AjaxResult update(@RequestBody TbBanner banner) {
        return toAjax(bannerService.updateById(banner));
    }

    /**
     * 修改轮播图（支持直接上传文件）
     */
    @PostMapping("/updateWithFile") // 注意：MultipartFile 在 PUT 下支持不佳，建议用 POST
    @PreAuthorize("@ss.hasPermi('mall:banner:edit')")
    public AjaxResult updateWithFile(@RequestParam("id") Long id,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "sort", required = false) Integer sort,
                                    @RequestParam(value = "linkUrl", required = false) String linkUrl,
                                    @RequestParam(value = "linkType", required = false) String linkType,
                                    @RequestParam(value = "status", required = false) String status) throws Exception {
        TbBanner banner = bannerService.getById(id);
        if (banner == null) {
            return AjaxResult.error("轮播图不存在");
        }
        if (title != null) banner.setTitle(title);
        if (sort != null) banner.setSort(sort);
        if (linkUrl != null) banner.setLinkUrl(linkUrl);
        if (linkType != null) banner.setLinkType(linkType);
        if (status != null) banner.setStatus(status);

        if (file != null && !file.isEmpty()) {
            String path = saveBannerImageToLocal(file);
            if (path != null) banner.setPicUrl(path);
        }

        return toAjax(bannerService.updateById(banner));
    }

    /**
     * 轮播图专用：保存到本地目录 profile/upload/banner/，返回访问路径（如 /profile/upload/banner/2026/03/05/xxx.png）。
     * 前端通过该路径直接访问图片，无需再调接口按 ID 查库。
     */
    private String saveBannerImageToLocal(MultipartFile file) throws IOException, InvalidExtensionException {
        String profile = DingyangmallConfig.getProfile();
        if (profile == null || profile.trim().isEmpty()) return null;
        String bannerDir = profile.replace("\\", "/").trim();
        if (!bannerDir.endsWith("/")) bannerDir += "/";
        bannerDir += "upload/banner";
        new File(bannerDir).mkdirs();
        return FileUploadUtils.upload(bannerDir, file, MimeTypeUtils.IMAGE_EXTENSION);
    }

    /**
     * 删除轮播图
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:banner:del')")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(bannerService.removeById(id));
    }
}
