package com.uniking.androidwm2;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
    public static void show(Context context, String text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }
}
