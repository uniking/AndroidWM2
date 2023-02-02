package com.uniking.androidwm2;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.WatermarkDetector;
import com.watermark.androidwm.listener.BuildFinishListener;
import com.watermark.androidwm.listener.DetectFinishListener;
import com.watermark.androidwm.task.DetectionReturnValue;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.bt_add_dark_watermark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdcard = Os.getSdard();
//                DarkWatermark darkWatermark = new DarkWatermark(
//                        new File(sdcard+"/Pictures/Screenshots/1.png"),
//                        new File(sdcard+"/Pictures/Screenshots/2.png"));
//                darkWatermark.addWatermark(sdcard+"/Pictures/Screenshots/0.png");

                Bitmap backgroundBitmap = BitmapFactory.decodeFile(sdcard+"/Pictures/Screenshots/1.png");
                WatermarkBuilder
                        .create(getApplication(), backgroundBitmap, false)
                        .loadWatermarkText("HelloWorld")
                        .setInvisibleWMListener(true, new BuildFinishListener<Bitmap>() {
                            @Override
                            public void onSuccess(Bitmap object) {
                                if (object != null) {
                                    // do something...
                                    String sdcard = Os.getSdard();
                                    Os.save(object, new File(sdcard+"/Pictures/Screenshots/0.png"), Bitmap.CompressFormat.PNG, true);
                                    ToastUtils.show(MainActivity.this, "添加水印成功");
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                // do something...
                                ToastUtils.show(MainActivity.this, "添加水印失败");
                            }
                        });
            }
        });

        findViewById(R.id.bt_get_dark_watermark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadPool.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        String sdcard = Os.getSdard();
//                        DarkWatermark darkWatermark = new DarkWatermark(
//                                new File(sdcard+"/Pictures/Screenshots/0.png"));
//                        darkWatermark.abstractWatermark(sdcard+"/Pictures/Screenshots/00.png");
                        Bitmap inputBitmap = BitmapFactory.decodeFile(sdcard+"/Pictures/Screenshots/0.png");
                        WatermarkDetector
                                .create(inputBitmap, true)
                                .detect(new DetectFinishListener() {
                                    @Override
                                    public void onSuccess(DetectionReturnValue returnValue) {
                                        //Bitmap watermarkImage = returnValue.getWatermarkBitmap();
                                        String watermarkString = returnValue.getWatermarkString();
                                        ToastUtils.show(MainActivity.this, "提取成功:"+watermarkString);
                                    }

                                    @Override
                                    public void onFailure(String message) {
                                        // do something...
                                        ToastUtils.show(MainActivity.this, "提取失败");
                                    }
                                });
                    }
                });
            }
        });


        getPermission();
    }

    private void getPermission(){
        ArrayList<String> permissions = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
            }
        }
    }
}