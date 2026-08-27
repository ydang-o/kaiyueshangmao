/*
 * Decompiled with CFR.
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
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("\u6587\u4ef6\u540d\u79f0({})\u975e\u6cd5\uff0c\u4e0d\u5141\u8bb8\u4e0b\u8f7d\u3002 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = DingyangmallConfig.getDownloadPath() + fileName;
            response.setContentType("application/octet-stream");
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete.booleanValue()) {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e) {
            log.error("\u4e0b\u8f7d\u6587\u4ef6\u5931\u8d25", e);
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
            return AjaxResult.error(e.getMessage());
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
            ajax.put("urls", (Object)StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", (Object)StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", (Object)StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", (Object)StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping(value={"/download/resource"})
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("\u8d44\u6e90\u6587\u4ef6({})\u975e\u6cd5\uff0c\u4e0d\u5141\u8bb8\u4e0b\u8f7d\u3002 ", resource));
            }
            String localPath = DingyangmallConfig.getProfile();
            String downloadPath = localPath + StringUtils.substringAfter(resource, "/profile");
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType("application/octet-stream");
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e) {
            log.error("\u4e0b\u8f7d\u6587\u4ef6\u5931\u8d25", e);
        }
    }
}

