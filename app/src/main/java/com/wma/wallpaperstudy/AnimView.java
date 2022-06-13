package com.wma.wallpaperstudy;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

/**
 * Created by WMA on 2022/6/10.
 */
public class AnimView extends View {
    private Context mContext;
    Painter mWpPainter;
    ValueAnimator mRotateAnim;
    ValueAnimator mTranslateAnim;
    float mDegrees;//宣传角度


    public AnimView(Context context) {
        this(context, null);

    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        mWpPainter = new Painter(mContext);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mWpPainter.mCanvasCenterP.x + mWpPainter.mDistance, mWpPainter.mCanvasCenterP.y);
        mWpPainter.drawOriginRect(canvas);
        mWpPainter.drawCircle(canvas);
        canvas.rotate(mDegrees);
        mWpPainter.drawBitmap(canvas);
        canvas.restore();

    }


    public void drawTranslate(){

        mTranslateAnim = ValueAnimator.ofInt(30, 0);
        mTranslateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWpPainter.mDistance = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mTranslateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                drawRotateBitmap();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mTranslateAnim.setInterpolator(new LinearInterpolator());
        mTranslateAnim.setDuration(300);
        mTranslateAnim.start();
    }


    public void drawRotateBitmap() {

        mRotateAnim = ValueAnimator.ofFloat(0, 3600);
        mRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDegrees = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mRotateAnim.setInterpolator(new LinearInterpolator());
        mRotateAnim.setDuration(8000);
        mRotateAnim.start();
    }

    public void recycle(){
        if(mRotateAnim != null && mRotateAnim.isRunning()){
            mRotateAnim.cancel();
        }
        mWpPainter.recycle();
    }
}
