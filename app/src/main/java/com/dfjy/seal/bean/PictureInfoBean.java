package com.dfjy.seal.bean;

import java.io.Serializable;

/**
 * Project：SealCop
 * User: dongxf(dongxf@orient-it.com)
 * Date: 2015-07-13
 * Time: 09:59
 */
public class PictureInfoBean implements Serializable {
    String picId;
    String fileId;
    byte[] picInfo;
    public String getPicId() {
        return picId;
    }
    public void setPicId(String picId) {
        this.picId = picId;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public byte[] getPicInfo() {
        return picInfo;
    }
    public void setPicInfo(byte[] picInfo) {
        this.picInfo = picInfo;
    }
}
