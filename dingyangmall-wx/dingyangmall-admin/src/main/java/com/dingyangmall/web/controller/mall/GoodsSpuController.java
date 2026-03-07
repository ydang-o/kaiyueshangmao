/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.dingyangmall.com
 * 注意：
 * 本软件为www.dingyangmall.com开发研制，项目使用请保留此说明
 */
package com.dingyangmall.web.controller.mall;

import cn.hutool.core.convert.Convert;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * spu商品
 *
 * @author JL
 * @date 2019-08-12 16:25:10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping({"/goodsspu", "/dev-api/goodsspu"})
public class GoodsSpuController extends BaseController {

    private final GoodsSpuService goodsSpuService;

    /** 商品图片保存到本地目录 profile/upload/goods/，按路径直接访问 */
    private static final String UPLOAD_SUBDIR_GOODS = "upload/goods";

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsSpu spu商品
    * @return
    */
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:index')")
    public AjaxResult getGoodsSpuPage(Page page, GoodsSpu goodsSpu) {
		com.baomidou.mybatisplus.core.metadata.IPage<GoodsSpu> result = goodsSpuService.page1(page, goodsSpu);
		result.getRecords().forEach(AdminImageUtils::normalizeGoodsSpuPicUrls);
		return AjaxResult.success(result);
    }

	/**
	 * list查询
	 * @param goodsSpu
	 * @return
	 */
	@GetMapping("/list")
	public List<GoodsSpu> getList(GoodsSpu goodsSpu) {
		return goodsSpuService.list(Wrappers.query(goodsSpu).lambda()
						.select(GoodsSpu::getId,
								GoodsSpu::getName)
				);
	}

	/**
	 * 查询数量
	 * @param goodsSpu
	 * @return
	 */
	@GetMapping("/count")
	public AjaxResult getCount(GoodsSpu goodsSpu) {
		return AjaxResult.success(goodsSpuService.count(Wrappers.query(goodsSpu)));
	}

    /**
    * 通过id查询spu商品
    * @param id
    * @return AjaxResult
    */
    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:get')")
    public AjaxResult getById(@PathVariable("id") String id){
        GoodsSpu spu = goodsSpuService.getById1(id);
        if (spu != null) {
            AdminImageUtils.normalizeGoodsSpuPicUrls(spu);
        }
        return AjaxResult.success(spu);
    }

    /**
     * 商品图片上传（单张）：保存到 profile/upload/goods/，返回访问路径，用于商品主图/相册。
     */
    @PostMapping("/upload")
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:add') or @ss.hasPermi('mall:goodsspu:edit')")
    public AjaxResult uploadImage(MultipartFile file) throws IOException, InvalidExtensionException {
        String path = saveGoodsImageToLocal(file);
        if (path == null) return AjaxResult.error("上传路径未配置");
        return AjaxResult.success("上传成功").put("url", path).put("fileName", path);
    }

    /**
     * 商品图片上传（多张）：保存到 profile/upload/goods/，返回路径数组，用于商品多图。
     */
    @PostMapping("/uploads")
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:add') or @ss.hasPermi('mall:goodsspu:edit')")
    public AjaxResult uploadImages(@RequestParam("files") List<MultipartFile> files) throws IOException, InvalidExtensionException {
        if (files == null || files.isEmpty()) return AjaxResult.success().put("urls", List.of());
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            String path = saveGoodsImageToLocal(file);
            if (path != null) urls.add(path);
        }
        return AjaxResult.success("上传成功").put("urls", urls);
    }

    private String saveGoodsImageToLocal(MultipartFile file) throws IOException, InvalidExtensionException {
        String profile = DingyangmallConfig.getProfile();
        if (profile == null || profile.trim().isEmpty()) return null;
        String baseDir = profile.replace("\\", "/").trim();
        if (!baseDir.endsWith("/")) baseDir += "/";
        baseDir += UPLOAD_SUBDIR_GOODS;
        new File(baseDir).mkdirs();
        return FileUploadUtils.upload(baseDir, file, MimeTypeUtils.IMAGE_EXTENSION);
    }

    /**
    * 新增spu商品
    * @param goodsSpu spu商品
    * @return AjaxResult
    */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:add')")
    public AjaxResult save(@RequestBody GoodsSpu goodsSpu){
        return AjaxResult.success(goodsSpuService.save1(goodsSpu));
    }

    /**
    * 修改spu商品
    * @param goodsSpu spu商品
    * @return AjaxResult
    */
    @PutMapping
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:edit')")
    public AjaxResult updateById(@RequestBody GoodsSpu goodsSpu){
        return AjaxResult.success(goodsSpuService.updateById1(goodsSpu));
    }

	/**
	 * 商品上下架操作
	 * @param shelf
	 * @param ids
	 * @return AjaxResult
	 */
	@PutMapping("/shelf")
	@PreAuthorize("@ss.hasPermi('mall:goodsspu:edit')")
	public AjaxResult updateById(@RequestParam(value = "shelf") String shelf, @RequestParam(value = "ids") String ids){
		GoodsSpu goodsSpu = new GoodsSpu();
		goodsSpu.setShelf(shelf);
		return AjaxResult.success(goodsSpuService.update(goodsSpu,Wrappers.<GoodsSpu>lambdaQuery()
				.in(GoodsSpu::getId, Convert.toList(ids))));
	}

    /**
    * 通过id删除spu商品
    * @param id
    * @return AjaxResult
    */
    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('mall:goodsspu:del')")
    public AjaxResult removeById(@PathVariable String id){
        return AjaxResult.success(goodsSpuService.removeById(id));
    }

}

