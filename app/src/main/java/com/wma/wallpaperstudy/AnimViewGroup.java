package com.wma.wallpaperstudy;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by WMA on 2022/6/13.
 */
public class AnimViewGroup extends FrameLayout {
    private Context mContext;

    private Painter mWpPainter;
    private View mRoot;
    private View mFanIv;

    public AnimViewGroup(Context context) {
        this(context, null);


    }

    public AnimViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        mWpPainter = new Painter(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_anim_group, this);

        mRoot = view.findViewById(R.id.fl_root);
        mFanIv = view.findViewById(R.id.iv_fan);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        View childAt = getChildAt(0);
        childAt.layout(mWpPainter.mRealRect.left + mWpPainter.mDistance, mWpPainter.mRealRect.top, mWpPainter.mRealRect.right + mWpPainter.mDistance, mWpPainter.mRealRect.bottom);
    }


    public void startAnim() {
        Animator rotateAnimation = ObjectAnimator.ofFloat(mFanIv, "rotation", 0, 3600);
        rotateAnimation.setDuration(5000);
        Animator translateAnimation1 = ObjectAnimator.ofFloat(mRoot, "translationX", 0, -mWpPainter.mDistance);
        translateAnimation1.setDuration(500);
        Animator translateAnimation2 = ObjectAnimator.ofFloat(mRoot, "translationX", -mWpPainter.mDistance, 0);
        translateAnimation2.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.play(translateAnimation2).after(rotateAnimation).after(translateAnimation1);
        set.start();

    }
}
