package com.wma.wallpaperstudy;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Created by WMA on 2022/6/10.
 */
public class WallpaperUtils {

    public static boolean isLiveWallpaperRunning(Context context, String tagetPackageName) {

        // 得到壁纸管理器

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);

        // 如果系统使用的壁纸是动态壁纸话则返回该动态壁纸的信息,否则会返回null

        WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();

        if (wallpaperInfo != null) {

            // 如果是动态壁纸,则得到该动态壁纸的包名,并与想知道的动态壁纸包名做比较

            String currentLiveWallpaperPackageName = wallpaperInfo.getPackageName();

            if (currentLiveWallpaperPackageName.equals(tagetPackageName)) {

                return true;

            }

        }

        return false;

    }


    private Bitmap drawableToBitmap(Drawable drawable,Context context) {
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
            Drawable defaultDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
            return drawableToBitmap(defaultDrawable, context);
        }
    }

}
