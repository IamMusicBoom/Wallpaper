package com.wma.wallpaperstudy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_WALLPAPER = 0;
    private ImageView mIvFan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
        mIvFan = findViewById(R.id.iv_fan);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(mIvFan, "translationX", 500);



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_wallpaper) {
            if (!WallpaperUtils.isLiveWallpaperRunning(this.getApplicationContext(), getPackageName())) {
                startLiveWallpaperPreView(getPackageName(), LiveWallpaperService.class.getName());
            }
        } else if (v.getId() == R.id.cancel_wallpaper) {
            WallpaperManager instance = WallpaperManager.getInstance(MainActivity.this.getApplicationContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                instance.clearWallpaper();
            } else {
                try {
                    instance.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void startLiveWallpaperPreView(String packageName, String classFullName) {
        ComponentName componentName = new ComponentName(packageName, classFullName);
        Intent intent;
        intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
        intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
        startActivityForResult(intent, REQUEST_WALLPAPER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_WALLPAPER && resultCode == RESULT_OK) {
        }
    }
}