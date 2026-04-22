package com.dingyangmall.web.api;

import com.dingyangmall.common.core.domain.AjaxResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 微信素材 API
 *
 * @author dingyangmall
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wxmaterial")
public class WxMaterialApi {

    /**
     * 上传临时素材
     */
    @PostMapping("/upload")
    public AjaxResult uploadMaterial(@RequestParam("file") MultipartFile file,
                                      @RequestParam("type") String type) {
        // TODO: 实现上传素材逻辑
        return AjaxResult.success("素材上传成功");
    }

    /**
     * 获取素材列表
     */
    @GetMapping("/list")
    public AjaxResult getMaterialList(@RequestParam(value = "type", defaultValue = "image") String type,
                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        // TODO: 实现获取素材列表逻辑
        return AjaxResult.success();
    }

    /**
     * 删除素材
     */
    @PostMapping("/delete/{mediaId}")
    public AjaxResult deleteMaterial(@PathVariable String mediaId) {
        // TODO: 实现删除素材逻辑
        return AjaxResult.success("素材删除成功");
    }
}
