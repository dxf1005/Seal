package com.dfjy.seal.util;

import android.app.Application;

/**
 * Project:seal
 * User: dongxf(dongxf@orient-it.com)
 * Date: 2015-07-01
 * Time: 09:47
 */
public class MyApplication extends Application {
    private  String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
