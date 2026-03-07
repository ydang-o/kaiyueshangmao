package com.dingyangmall.web.controller.common;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.system.domain.SysUploadFile;
import com.dingyangmall.web.service.SysUploadFileService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;

/**
 * 从 MySQL 读取并输出文件（替代 OSS/磁盘）
 */
@RestController
@RequestMapping({"/profile", "/dev-api/profile"})
public class FileViewController {

    private static final Logger log = LoggerFactory.getLogger(FileViewController.class);

    private final SysUploadFileService sysUploadFileService;

    public FileViewController(SysUploadFileService sysUploadFileService) {
        this.sysUploadFileService = sysUploadFileService;
    }

    @GetMapping("/file/{id}")
    public void getFile(@PathVariable Long id, HttpServletResponse response) {
        try {
            SysUploadFile file = sysUploadFileService.getById(id);
            if (file == null || file.getContent() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            // 1. 设置内容类型
            String contentType = file.getContentType();
            if (StringUtils.isEmpty(contentType)) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            response.setContentType(contentType);

            // 2. 增加浏览器缓存支持，提高加载速度
            response.setHeader("Cache-Control", "max-age=2592000"); // 缓存30天

            // 3. 输出图片流
            try (OutputStream out = response.getOutputStream()) {
                out.write(file.getContent());
                out.flush();
            }
        } catch (Exception e) {
            log.error("读取并展示图片失败 fileId={}", id, e);
        }
    }
}
