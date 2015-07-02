package com.dfjy.seal.bean;

import java.io.Serializable;

/**
 * Project:seal
 * User: dongxf(dongxf@orient-it.com)
 * Date: 2015-07-01
 * Time: 16:36
 */
public class MachInfoBean implements Serializable {
    String machID;
    String machName;

    public String getMachID() {
        return machID;
    }

    public void setMachID(String machID) {
        this.machID = machID;
    }

    public String getMachName() {
        return machName;
    }

    public void setMachName(String machName) {
        this.machName = machName;
    }
}
