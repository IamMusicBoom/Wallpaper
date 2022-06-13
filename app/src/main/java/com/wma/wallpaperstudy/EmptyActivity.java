package com.wma.wallpaperstudy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by WMA on 2022/6/2.
 */
public class EmptyActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, EmptyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

//    private AnimView mAnim;
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mAnim.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mAnim.drawTranslate();
//            }
//        }, 200);
//
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        overridePendingTransition(0, 0);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_empty);
//        mAnim = findViewById(R.id.anim_view);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mAnim.recycle();
//
//    }


    private AnimViewGroup mAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        mAnim = findViewById(R.id.anim_view_group);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        mAnim.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnim.startAnim();
            }
        }, 200);
    }

}
