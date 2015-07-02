package com.dfjy.seal.bean;

import java.io.Serializable;

/**
 * Project:seal
 * User: dongxf(dongxf@orient-it.com)
 * Date: 2015-07-01
 * Time: 16:36
 */
public class SealInfoBean implements Serializable {
    String sealId;
    String sealName;

    public String getSealId() {
        return sealId;
    }

    public void setSealId(String sealId) {
        this.sealId = sealId;
    }

    public String getSealName() {
        return sealName;
    }

    public void setSealName(String sealName) {
        this.sealName = sealName;
    }
}
