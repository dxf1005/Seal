package com.dfjy.seal.bean;

import java.io.Serializable;

/**
 * Project:seal
 * User: dongxf(dongxf@orient-it.com)
 * Date: 2015-07-01
 * Time: 16:36
 */
public class FileTypeInfoBean implements Serializable {
    String fileTypeId;
    String fileTypeName;
    public String getFileTypeId() {
        return fileTypeId;
    }
    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }
    public String getFileTypeName() {
        return fileTypeName;
    }
    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }
}
