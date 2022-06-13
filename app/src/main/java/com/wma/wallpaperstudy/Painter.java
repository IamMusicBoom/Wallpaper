package com.wma.wallpaperstudy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Created by WMA on 2022/6/10.
 */
public class Painter {
    private Context mContext;
    private Paint mPaint;// 画笔
    private Bitmap mFanBp;//风车
    private int mRadius;//半径
    private Rect mDistRect;//目标区域
    private Point mCenterP = new Point();//圆心
    public Point mCanvasCenterP = new Point();//画布原点
    private int[] mScreenSize;//屏幕尺寸
    LinearGradient mShader;//渐变色
    public RectF mRect;//背景圆角矩形
    Path mPath;//圆角矩形
    float mDistance = 30;//水平移动距离
    public RectF mRealRect;//点击区域

    public Painter(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFanBp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ac);
        mRadius = UISizeUtil.dpToPx(mContext, 37 / 2);
        mScreenSize = UISizeUtil.getScreenSize(mContext);
        mCanvasCenterP.x = mScreenSize[0] - (UISizeUtil.dpToPx(mContext, 45) / 2);
        mCanvasCenterP.y = mScreenSize[1] * 2 / 3 + (UISizeUtil.dpToPx(mContext, 45) / 2);
        mCenterP.x = 0;
        mCenterP.y = 0;
        mShader = new LinearGradient(mCenterP.x, mCenterP.y - mRadius, mCenterP.x, mCenterP.y + mRadius, mContext.getResources().getColor(R.color.color_B3FFC3), mContext.getResources().getColor(R.color.color_34DC75), Shader.TileMode.MIRROR);
        mDistRect = new Rect(mCenterP.x - (mFanBp.getWidth() / 2), mCenterP.y - (mFanBp.getHeight() / 2), mCenterP.x + (mFanBp.getWidth() / 2), mCenterP.y + (mFanBp.getHeight() / 2));
        mRect = new RectF(mCenterP.x - (UISizeUtil.dpToPx(mContext, 45) / 2), mCenterP.y - (UISizeUtil.dpToPx(mContext, 45) / 2),
                mCenterP.x + (UISizeUtil.dpToPx(mContext, 45) / 2), mCenterP.y + (UISizeUtil.dpToPx(mContext, 45) / 2));

        mRealRect = new RectF(mCanvasCenterP.x - (UISizeUtil.dpToPx(mContext, 45) / 2), mCanvasCenterP.y - (UISizeUtil.dpToPx(mContext, 45) / 2)
                , mCanvasCenterP.x + (UISizeUtil.dpToPx(mContext, 45) / 2), mCanvasCenterP.y + (UISizeUtil.dpToPx(mContext, 45) / 2));
        mPath = new Path();
    }


    public void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(mFanBp, null, mDistRect, mPaint);
    }

    public void drawOriginRect(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setShader(null);
        mPath.addRoundRect(mRect, new float[]{50, 50, 0, 0, 0, 0, 50, 50},// 四个顶点的圆角x,y圆角，左上，右上，右下，左下
                Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
    }

    public void drawCircle(Canvas canvas) {
        mPaint.setShader(mShader);
        canvas.drawCircle(mCenterP.x, mCenterP.y, mRadius, mPaint);
    }

    public void recycle() {
        if(mFanBp != null){
            mFanBp.recycle();
        }
    }
}
