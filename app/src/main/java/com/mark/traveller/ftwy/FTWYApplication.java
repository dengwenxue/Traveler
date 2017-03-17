package com.mark.traveller.ftwy;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.mark.traveller.ftwy.db.UserDao;

/**
 * Created by Mark on 2016/11/17.
 */

public class FTWYApplication extends Application {
    public static Context mContext;
    private static Handler mMainThreadHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        // 创建数据库
        UserDao dao = new UserDao(mContext);

        // 创建主线程
        mMainThreadHandler = new Handler();

        // 百度地图初始化
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // SDKInitializer.initialize(this);

    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

}
