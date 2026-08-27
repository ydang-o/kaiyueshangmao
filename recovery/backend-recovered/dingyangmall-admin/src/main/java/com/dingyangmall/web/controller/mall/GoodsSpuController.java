/*
 * Decompiled with CFR.
 */
package com.dingyangmall.web.controller.mall;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.core.controller.BaseController;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.exception.file.InvalidExtensionException;
import com.dingyangmall.common.utils.file.FileUploadUtils;
import com.dingyangmall.common.utils.file.MimeTypeUtils;
import com.dingyangmall.mall.entity.GoodsSpu;
import com.dingyangmall.mall.service.GoodsSpuService;
import com.dingyangmall.web.utils.AdminImageUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value={"/goodsspu", "/dev-api/goodsspu"})
public class GoodsSpuController
extends BaseController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(GoodsSpuController.class);
    private final GoodsSpuService goodsSpuService;
    private static final String UPLOAD_SUBDIR_GOODS = "upload/goods";

    @GetMapping(value={"/page"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodsspu:index')")
    public AjaxResult getGoodsSpuPage(Page page, GoodsSpu goodsSpu) {
        IPage<GoodsSpu> result = this.goodsSpuService.page1(page, goodsSpu);
        result.getRecords().forEach(AdminImageUtils::normalizeGoodsSpuPicUrls);
        return AjaxResult.success(result);
    }

    @GetMapping(value={"/list"})
    public List<GoodsSpu> getList(GoodsSpu goodsSpu) {
        return this.goodsSpuService.list(Wrappers.query(goodsSpu).lambda().select(GoodsSpu::getId, GoodsSpu::getName));
    }

    @GetMapping(value={"/count"})
    public AjaxResult getCount(GoodsSpu goodsSpu) {
        return AjaxResult.success(this.goodsSpuService.count(Wrappers.query(goodsSpu)));
    }

    @GetMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodsspu:get')")
    public AjaxResult getById(@PathVariable(value="id") String id) {
        GoodsSpu spu = this.goodsSpuService.getById1(id);
        if (spu != null) {
            AdminImageUtils.normalizeGoodsSpuPicUrls(spu);
        }
        return AjaxResult.success(spu);
    }

    @PostMapping(value={"/upload"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodsspu:add') or @ss.hasPermi('mall:goodsspu:edit')")
    public AjaxResult uploadImage(MultipartFile file) throws IOException, InvalidExtensionException {
        String path = this.saveGoodsImageToLocal(file);
        if (path == null) {
            return AjaxResult.error("\u4e0a\u4f20\u8def\u5f84\u672a\u914d\u7f6e");
        }
        return AjaxResult.success("\u4e0a\u4f20\u6210\u529f").put("url", (Object)path).put("fileName", (Object)path);
    }

    @PostMapping(value={"/uploads"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodsspu:add') or @ss.hasPermi('mall:goodsspu:edit')")
    public AjaxResult uploadImages(@RequestParam(value="files") List<MultipartFile> files) throws IOException, InvalidExtensionException {
        if (files == null || files.isEmpty()) {
            return AjaxResult.success().put("urls", (Object)List.of());
        }
        ArrayList<String> urls = new ArrayList<String>();
        for (MultipartFile file : files) {
            String path;
            if (file.isEmpty() || (path = this.saveGoodsImageToLocal(file)) == null) continue;
            urls.add(path);
        }
        return AjaxResult.success("\u4e0a\u4f20\u6210\u529f").put("urls", (Object)urls);
    }

    private String saveGoodsImageToLocal(MultipartFile file) throws IOException, InvalidExtensionException {
        String profile = DingyangmallConfig.getProfile();
        if (profile == null || profile.trim().isEmpty()) {
            return null;
        }
        Object baseDir = profile.replace("\\", "/").trim();
        if (!((String)baseDir).endsWith("/")) {
            baseDir = (String)baseDir + "/";
        }
        baseDir = (String)baseDir + UPLOAD_SUBDIR_GOODS;
        new File((String)baseDir).mkdirs();
        return FileUploadUtils.upload((String)baseDir, file, MimeTypeUtils.IMAGE_EXTENSION);
    }

    @PostMapping
    @PreAuthorize(value="@ss.hasPermi('mall:goodsspu:add')")
    public AjaxResult save(@RequestBody GoodsSpu goodsSpu) {
        return AjaxResult.success(this.goodsSpuService.save1(goodsSpu));
    }

    @PutMapping
    @PreAuthorize(value="@ss.hasPermi('mall:goodsspu:edit')")
    public AjaxResult updateById(@RequestBody GoodsSpu goodsSpu) {
        return AjaxResult.success(this.goodsSpuService.updateById1(goodsSpu));
    }

    @PutMapping(value={"/shelf"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodsspu:edit')")
    public AjaxResult updateById(@RequestParam(value="shelf") String shelf, @RequestParam(value="ids") String ids) {
        GoodsSpu goodsSpu = new GoodsSpu();
        goodsSpu.setShelf(shelf);
        return AjaxResult.success(this.goodsSpuService.update(goodsSpu, (Wrapper)Wrappers.lambdaQuery().in(GoodsSpu::getId, Convert.toList(ids))));
    }

    @DeleteMapping(value={"/{id}"})
    @PreAuthorize(value="@ss.hasPermi('mall:goodsspu:del')")
    public AjaxResult removeById(@PathVariable String id) {
        return AjaxResult.success(this.goodsSpuService.removeById((Serializable)((Object)id)));
    }

    @Generated
    public GoodsSpuController(GoodsSpuService goodsSpuService) {
        this.goodsSpuService = goodsSpuService;
    }
}

