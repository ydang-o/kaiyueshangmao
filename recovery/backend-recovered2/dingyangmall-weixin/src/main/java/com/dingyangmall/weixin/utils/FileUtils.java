/*
 * Decompiled with CFR.
 */
package com.dingyangmall.weixin.utils;

import cn.hutool.core.lang.UUID;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
    public static File multipartFileToFile(MultipartFile mulFile) throws IOException {
        InputStream ins = mulFile.getInputStream();
        String fileName = mulFile.getOriginalFilename();
        String prefix = FileUtils.getFileNameNoEx(fileName) + String.valueOf(UUID.fastUUID());
        String suffix = "." + FileUtils.getExtensionName(fileName);
        File toFile = File.createTempFile(prefix, suffix);
        FileOutputStream os = new FileOutputStream(toFile);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            ((OutputStream)os).write(buffer, 0, bytesRead);
        }
        ((OutputStream)os).close();
        ins.close();
        return toFile;
    }

    public static String getExtensionName(String filename) {
        int dot;
        if (filename != null && filename.length() > 0 && (dot = filename.lastIndexOf(46)) > -1 && dot < filename.length() - 1) {
            return filename.substring(dot + 1);
        }
        return filename;
    }

    public static String getFileNameNoEx(String filename) {
        int dot;
        if (filename != null && filename.length() > 0 && (dot = filename.lastIndexOf(46)) > -1 && dot < filename.length()) {
            return filename.substring(0, dot);
        }
        return filename;
    }
}

