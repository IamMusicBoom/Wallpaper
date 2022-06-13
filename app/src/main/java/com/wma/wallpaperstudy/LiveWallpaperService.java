package com.wma.wallpaperstudy;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by WMA on 2022/6/6.
 */
public class LiveWallpaperService extends WallpaperService {
    public static final String CHANGE_WALLPAPER = "change_wallpaper";
    private final String TAG = "WMA-WMA";

    private MyEngine mEngine;


    private SurfaceHolder mHolder;

    //执行动画

    private Handler handler;

    private Bitmap mWallpaperBp;

    private Rect mTipsBgRect;

    private int[] screenSize;

    private Paint mPaint;
    private Shader mBgShader;

    private Movie movie;
    private String mTitleText, mMessageText;
    private Rect mTextRect;
    private TextPaint mTextPaint;
    Painter mWpPainter;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service onCreate: ");
        handler = new Handler();
        init();
    }

    private void init() {
        mWpPainter = new Painter(getApplicationContext());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        screenSize = UISizeUtil.getScreenSize(getApplicationContext());
        mTipsBgRect = new Rect(0, 0, screenSize[0], screenSize[1]);
        mBgShader = new LinearGradient(screenSize[0] / 2, 0, screenSize[0] / 2, screenSize[1],
                new int[]{getResources().getColor(R.color.color_777777), getResources().getColor(R.color.white), getResources().getColor(R.color.color_777777)},
                null, Shader.TileMode.MIRROR);
        // gif
        try {
            InputStream stream = getApplicationContext().getAssets().open("test.gif");
            movie = Movie.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 文字
        mTextRect = new Rect();
        mTitleText = getApplicationContext().getString(R.string.test_title);
        mMessageText = getApplicationContext().getString(R.string.test_message);

        // 墙纸
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Drawable wallpaperBitmap = wallpaperManager.getDrawable();
            mWallpaperBp = drawableToBitmap(wallpaperBitmap);
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "service onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "service onUnbind: ");
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "service onDestroy: ");

    }

    // 实现WallpaperService必须实现的抽象方法
    public Engine onCreateEngine() {
        // 返回自定义的CameraEngine
        Log.d(TAG, "onCreateEngine: ");
        mEngine = new MyEngine();
        return mEngine;
    }


    class MyEngine extends Engine {
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            Log.d(TAG, "onCreate: ");
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);


        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d(TAG, "onDestroy: ");
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Log.d(TAG, "onSurfaceCreated: ");
            mHolder = getSurfaceHolder();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.d(TAG, "onSurfaceChanged: ");
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.d(TAG, "onSurfaceDestroyed: ");
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            Log.d(TAG, "onVisibilityChanged: visible = " + visible);
            if (visible) {
                if (WallpaperUtils.isLiveWallpaperRunning(getApplicationContext(), getPackageName())) {
                    if(runnable != null){
                        handler.removeCallbacks(runnable);
                        runnable = null;
                    }
                    Canvas canvas = mHolder.lockCanvas();
                    if (canvas == null) {
                        return;
                    }
                    canvas.save();
                    drawWallpaper(canvas);
                    canvas.restore();
                    //结束锁定画图，并提交改变,画画完成(解锁)
                    mHolder.unlockCanvasAndPost(canvas);
                } else {
                    handler.post(runnable);
                }
            }

        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            Log.d(TAG, "onOffsetsChanged: xOffset = " + xOffset + " yOffset = " + yOffset + " xOffsetStep = " + xOffsetStep
                    + " yOffsetStep = " + yOffsetStep + " xPixelOffset = " + xPixelOffset + " yPixelOffset = " + yPixelOffset);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();
                    if (mWpPainter.mRealRect.contains(((int) rawX), ((int) rawY))) {
                        EmptyActivity.start(getApplicationContext());
                    }
                    break;
            }

        }


    }


    //线程

    private Runnable runnable = new Runnable() {

        @Override

        public void run() {

            //获取画布(加锁)

            Canvas canvas = mHolder.lockCanvas();

            if (canvas == null) {
                return;
            }
            canvas.save();
            drawTips(canvas);
            canvas.restore();
            //结束锁定画图，并提交改变,画画完成(解锁)
            mHolder.unlockCanvasAndPost(canvas);
            handler.postDelayed(runnable, 50); //50ms表示每50ms绘制一帧

        }

    };

    private void drawTips(Canvas canvas) {
        drawTipsBg(canvas);
        drawGif(canvas);
        drawTitleText(canvas);
        drawMessageText(canvas);
    }

    private void drawTipsBg(Canvas canvas) {
        mPaint.setShader(mBgShader);
        canvas.drawRect(mTipsBgRect, mPaint);
    }

    private void drawMessageText(Canvas canvas) {
        canvas.save();
        mTextPaint.setShader(null);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(35);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.getTextBounds(mMessageText, 0, mMessageText.length(), mTextRect);
        StaticLayout layout = new StaticLayout(mMessageText, mTextPaint, 640, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
        canvas.translate(40, screenSize[1] / 2 + (mTextRect.bottom - mTextRect.top) / 2 + 60);
        layout.draw(canvas);
        canvas.restore();
//        canvas.drawText(mMessageText, 0, mMessageText.length(), screenSize[0] / 2 - (mTextRect.right - mTextRect.left) /2  , screenSize[1] / 2 + (mTextRect.bottom - mTextRect.top) / 2 + 60, mPaint);

    }

    private void drawTitleText(Canvas canvas) {
        mTextPaint.setShader(null);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(40);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextRect);
        canvas.drawText(mTitleText, 0, mTitleText.length(), screenSize[0] / 2 - (mTextRect.right - mTextRect.left) / 2, screenSize[1] / 2 + (mTextRect.bottom - mTextRect.top) / 2, mTextPaint);
    }

    private void drawGif(Canvas canvas) {
        //            canvas.scale(zoom, zoom); //x为水平方向的放大倍数，y为竖直方向的放大倍数。

        //绘制此gif的某一帧，并刷新本身

        movie.draw(canvas, screenSize[0] / 2 - movie.width() / 2, screenSize[1] / 2 - 500);

        //逐帧绘制图片(图片数量5)

        // 1 2 3 4 5 6 7 8 9 10

        // 1 2 3 4 0 1 2 3 4 0 循环

        movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
    }

    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(mWallpaperBp, 0, 0, mPaint);
    }

    private void drawWallpaper(Canvas canvas) {
        drawBitmap(canvas);
        canvas.save();
        canvas.translate(mWpPainter.mCanvasCenterP.x + mWpPainter.mDistance, mWpPainter.mCanvasCenterP.y);
        mWpPainter.drawOriginRect(canvas);
        mWpPainter.drawCircle(canvas);
        mWpPainter.drawBitmap(canvas);
        canvas.restore();
    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (drawable instanceof BitmapDrawable) {
                    return ((BitmapDrawable) drawable).getBitmap();
                } else if (drawable instanceof AdaptiveIconDrawable) {
                    Drawable[] drr = new Drawable[2];
                    drr[0] = ((AdaptiveIconDrawable) drawable).getBackground();
                    drr[1] = ((AdaptiveIconDrawable) drawable).getForeground();
                    LayerDrawable layerDrawable = new LayerDrawable(drr);
                    int width = layerDrawable.getIntrinsicWidth();
                    int height = layerDrawable.getIntrinsicHeight();

                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(bitmap);
                    layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    layerDrawable.draw(canvas);
                    return bitmap;
                }
            } else {
                return ((BitmapDrawable) drawable).getBitmap();
            }
            return ((BitmapDrawable) drawable).getBitmap();
        } catch (ClassCastException e) {
            Drawable defaultDrawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_launcher);
            return drawableToBitmap(defaultDrawable);
        }
    }
}