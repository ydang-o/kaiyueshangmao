package com.dingyangmall.web.service;

import com.dingyangmall.system.domain.SysUploadFile;
import com.dingyangmall.system.mapper.SysUploadFileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 上传文件服务（文件存于 MySQL，使用 system 模块 Mapper）
 */
@Service
public class SysUploadFileService {

    private final SysUploadFileMapper sysUploadFileMapper;

    public SysUploadFileService(SysUploadFileMapper sysUploadFileMapper) {
        this.sysUploadFileMapper = sysUploadFileMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public SysUploadFile save(MultipartFile file) throws Exception {
        SysUploadFile entity = new SysUploadFile();
        String originalName = file.getOriginalFilename();
        String fileName = originalName != null ? System.currentTimeMillis() + "_" + originalName : String.valueOf(System.currentTimeMillis());
        entity.setFileName(fileName);
        entity.setOriginalName(originalName);
        entity.setContentType(file.getContentType());
        entity.setFileSize(file.getSize());
        entity.setContent(file.getBytes());
        entity.setCreateTime(new Date());
        sysUploadFileMapper.insert(entity);
        return entity;
    }

    public SysUploadFile getById(Long fileId) {
        return sysUploadFileMapper.selectById(fileId);
    }
}
