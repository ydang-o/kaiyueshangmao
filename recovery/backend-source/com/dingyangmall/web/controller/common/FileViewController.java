/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.system.domain.SysUploadFile
 *  jakarta.servlet.ServletOutputStream
 *  jakarta.servlet.http.HttpServletResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.dingyangmall.web.controller.common;

import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.system.domain.SysUploadFile;
import com.dingyangmall.web.service.SysUploadFileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/profile", "/dev-api/profile"})
public class FileViewController {
    private static final Logger log = LoggerFactory.getLogger(FileViewController.class);
    private final SysUploadFileService sysUploadFileService;

    public FileViewController(SysUploadFileService sysUploadFileService) {
        this.sysUploadFileService = sysUploadFileService;
    }

    @GetMapping(value={"/file/{id}"})
    public void getFile(@PathVariable Long id, HttpServletResponse response) {
        try {
            SysUploadFile file = this.sysUploadFileService.getById(id);
            if (file == null || file.getContent() == null) {
                response.sendError(404);
                return;
            }
            String contentType = file.getContentType();
            if (StringUtils.isEmpty((String)contentType)) {
                contentType = "application/octet-stream";
            }
            response.setContentType(contentType);
            response.setHeader("Cache-Control", "max-age=2592000");
            try (ServletOutputStream out = response.getOutputStream();){
                out.write(file.getContent());
                out.flush();
            }
        }
        catch (Exception e) {
            log.error("\u8bfb\u53d6\u5e76\u5c55\u793a\u56fe\u7247\u5931\u8d25 fileId={}", (Object)id, (Object)e);
        }
    }
}

