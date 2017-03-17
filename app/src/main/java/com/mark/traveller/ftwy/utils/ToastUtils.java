package com.mark.traveller.ftwy.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * 工具类:Toast
 * Created by Mark on 2016/11/14 0014.
 */

public class ToastUtils {

    public static void showToast(final Activity context, final String str) {
        if (Thread.currentThread().getName().equals("main")) {
            // 主线程
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        } else {
            // 其他线程
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
