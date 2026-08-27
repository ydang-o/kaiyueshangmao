/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.common.config.DingyangmallConfig
 *  com.dingyangmall.common.core.domain.AjaxResult
 *  com.dingyangmall.common.utils.StringUtils
 *  com.dingyangmall.common.utils.file.FileUtils
 *  com.dingyangmall.framework.config.ServerConfig
 *  com.dingyangmall.system.domain.SysUploadFile
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletResponse
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 */
package com.dingyangmall.web.controller.common;

import com.dingyangmall.common.config.DingyangmallConfig;
import com.dingyangmall.common.core.domain.AjaxResult;
import com.dingyangmall.common.utils.StringUtils;
import com.dingyangmall.common.utils.file.FileUtils;
import com.dingyangmall.framework.config.ServerConfig;
import com.dingyangmall.system.domain.SysUploadFile;
import com.dingyangmall.web.service.SysUploadFileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/common", "/dev-api/common"})
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private SysUploadFileService sysUploadFileService;
    private static final String FILE_DELIMETER = ",";

    @GetMapping(value={"/download"})
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload((String)fileName)) {
                throw new Exception(StringUtils.format((String)"\u6587\u4ef6\u540d\u79f0({})\u975e\u6cd5\uff0c\u4e0d\u5141\u8bb8\u4e0b\u8f7d\u3002 ", (Object[])new Object[]{fileName}));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = DingyangmallConfig.getDownloadPath() + fileName;
            response.setContentType("application/octet-stream");
            FileUtils.setAttachmentResponseHeader((HttpServletResponse)response, (String)realFileName);
            FileUtils.writeBytes((String)filePath, (OutputStream)response.getOutputStream());
            if (delete.booleanValue()) {
                FileUtils.deleteFile((String)filePath);
            }
        }
        catch (Exception e) {
            log.error("\u4e0b\u8f7d\u6587\u4ef6\u5931\u8d25", (Throwable)e);
        }
    }

    @PostMapping(value={"/upload"})
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            SysUploadFile entity = this.sysUploadFileService.save(file);
            String fileName = "/profile/file/" + entity.getFileId();
            String url = this.serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", (Object)url);
            ajax.put("fileName", (Object)fileName);
            ajax.put("newFileName", (Object)entity.getFileName());
            ajax.put("originalFilename", (Object)entity.getOriginalName());
            return ajax;
        }
        catch (Exception e) {
            return AjaxResult.error((String)e.getMessage());
        }
    }

    @PostMapping(value={"/uploads"})
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {
        try {
            ArrayList<CallSite> urls = new ArrayList<CallSite>();
            ArrayList<CallSite> fileNames = new ArrayList<CallSite>();
            ArrayList<String> newFileNames = new ArrayList<String>();
            ArrayList<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                SysUploadFile entity = this.sysUploadFileService.save(file);
                String fileName = "/profile/file/" + entity.getFileId();
                String url = this.serverConfig.getUrl() + fileName;
                urls.add((CallSite)((Object)url));
                fileNames.add((CallSite)((Object)fileName));
                newFileNames.add(entity.getFileName());
                originalFilenames.add(entity.getOriginalName());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", (Object)StringUtils.join(urls, (String)FILE_DELIMETER));
            ajax.put("fileNames", (Object)StringUtils.join(fileNames, (String)FILE_DELIMETER));
            ajax.put("newFileNames", (Object)StringUtils.join(newFileNames, (String)FILE_DELIMETER));
            ajax.put("originalFilenames", (Object)StringUtils.join(originalFilenames, (String)FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e) {
            return AjaxResult.error((String)e.getMessage());
        }
    }

    @GetMapping(value={"/download/resource"})
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if (!FileUtils.checkAllowDownload((String)resource)) {
                throw new Exception(StringUtils.format((String)"\u8d44\u6e90\u6587\u4ef6({})\u975e\u6cd5\uff0c\u4e0d\u5141\u8bb8\u4e0b\u8f7d\u3002 ", (Object[])new Object[]{resource}));
            }
            String localPath = DingyangmallConfig.getProfile();
            String downloadPath = localPath + StringUtils.substringAfter((String)resource, (String)"/profile");
            String downloadName = StringUtils.substringAfterLast((String)downloadPath, (String)"/");
            response.setContentType("application/octet-stream");
            FileUtils.setAttachmentResponseHeader((HttpServletResponse)response, (String)downloadName);
            FileUtils.writeBytes((String)downloadPath, (OutputStream)response.getOutputStream());
        }
        catch (Exception e) {
            log.error("\u4e0b\u8f7d\u6587\u4ef6\u5931\u8d25", (Throwable)e);
        }
    }
}

