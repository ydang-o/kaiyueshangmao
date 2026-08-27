/*
 * Decompiled with CFR.
 * 
 * Could not load the following classes:
 *  com.dingyangmall.system.domain.SysUploadFile
 *  com.dingyangmall.system.mapper.SysUploadFileMapper
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.web.multipart.MultipartFile
 */
package com.dingyangmall.web.service;

import com.dingyangmall.system.domain.SysUploadFile;
import com.dingyangmall.system.mapper.SysUploadFileMapper;
import java.io.Serializable;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SysUploadFileService {
    private final SysUploadFileMapper sysUploadFileMapper;

    public SysUploadFileService(SysUploadFileMapper sysUploadFileMapper) {
        this.sysUploadFileMapper = sysUploadFileMapper;
    }

    @Transactional(rollbackFor={Exception.class})
    public SysUploadFile save(MultipartFile file) throws Exception {
        SysUploadFile entity = new SysUploadFile();
        String originalName = file.getOriginalFilename();
        Object fileName = originalName != null ? System.currentTimeMillis() + "_" + originalName : String.valueOf(System.currentTimeMillis());
        entity.setFileName((String)fileName);
        entity.setOriginalName(originalName);
        entity.setContentType(file.getContentType());
        entity.setFileSize(Long.valueOf(file.getSize()));
        entity.setContent(file.getBytes());
        entity.setCreateTime(new Date());
        this.sysUploadFileMapper.insert((Object)entity);
        return entity;
    }

    public SysUploadFile getById(Long fileId) {
        return (SysUploadFile)this.sysUploadFileMapper.selectById((Serializable)fileId);
    }
}

