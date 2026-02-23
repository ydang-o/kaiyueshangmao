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
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;

/**
 * 从 MySQL 读取并输出文件（替代 OSS/磁盘）
 */
@RestController
public class FileViewController {

    private static final Logger log = LoggerFactory.getLogger(FileViewController.class);

    private final SysUploadFileService sysUploadFileService;

    public FileViewController(SysUploadFileService sysUploadFileService) {
        this.sysUploadFileService = sysUploadFileService;
    }

    @GetMapping("/profile/file/{id}")
    public void getFile(@PathVariable Long id, HttpServletResponse response) {
        try {
            SysUploadFile file = sysUploadFileService.getById(id);
            if (file == null || file.getContent() == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String contentType = file.getContentType();
            if (StringUtils.isNotEmpty(contentType)) {
                response.setContentType(contentType);
            } else {
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            }
            try (OutputStream out = response.getOutputStream()) {
                out.write(file.getContent());
                out.flush();
            }
        } catch (Exception e) {
            log.error("读取文件失败 fileId={}", id, e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ex) {
                log.error("发送错误响应失败", ex);
            }
        }
    }
}
