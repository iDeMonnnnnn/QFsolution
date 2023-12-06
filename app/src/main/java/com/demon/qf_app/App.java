package com.demon.qf_app;

import android.app.Application;

import com.demon.qfsolution.QFHelper;

/**
 * @author DeMon
 * Created on 2022/3/7.
 * E-mail idemon_liu@qq.com
 * Desc:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         *考虑到不同项目中FileProvider的authorities可能不一样
         *因此这里改成可以根据自己项目FileProvider的authorities自由设置
         *如:android:authorities="${applicationId}.file.provider",你只需要传入“file.provider”即可
         */
        QFHelper.init(this, false, "file.provider");
        QFHelper.initImgLoader(new GlideLoader());
    }
}
