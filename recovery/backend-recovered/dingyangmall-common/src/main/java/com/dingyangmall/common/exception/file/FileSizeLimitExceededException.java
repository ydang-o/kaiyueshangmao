/*
 * Decompiled with CFR.
 */
package com.dingyangmall.common.exception.file;

import com.dingyangmall.common.exception.file.FileException;

public class FileSizeLimitExceededException
extends FileException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}

