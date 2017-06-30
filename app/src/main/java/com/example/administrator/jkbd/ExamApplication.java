package com.example.administrator.jkbd;

import android.app.Application;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ExamApplication extends Application {

    private ExamApplication instence;
    @Override
    public void onCreate() {
        super.onCreate();
        instence=this;
        initData();
    }

    private void initData() {

    }
}
