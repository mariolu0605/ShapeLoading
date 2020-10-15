package com.mario.library.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThreeBallsLoadingDrawable extends Drawable implements Animatable {


    private Paint mLeftPaint,mMiddlePaint,mRightPaint;


    private int mWidth,mHeight;

    private final static int RADIUS = 30;
    private final long ANIMATION_DURATION = 1000;

    private float translateX;

    public ThreeBallsLoadingDrawable(){
        mLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLeftPaint.setColor(Color.BLUE);
        mLeftPaint.setAntiAlias(true);
        mLeftPaint.setDither(true);

        mMiddlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddlePaint.setColor(Color.RED);
        mMiddlePaint.setAntiAlias(true);
        mMiddlePaint.setDither(true);

        mRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightPaint.setColor(Color.GREEN);
        mRightPaint.setAntiAlias(true);
        mRightPaint.setDither(true);
    }

    @Override
    public void start() {
        startAnimation();
    }



    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        canvas.save();
        canvas.translate(translateX,0);
        canvas.drawCircle(RADIUS,mHeight/2,RADIUS,mLeftPaint);
        canvas.restore();

        canvas.save();
        canvas.drawCircle(mWidth/2,mHeight/2,RADIUS,mMiddlePaint);
        canvas.restore();

        canvas.save();
        canvas.translate(-translateX,0);
        canvas.drawCircle(mWidth-RADIUS,mHeight/2,RADIUS,mRightPaint);
        canvas.restore();

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return PixelFormat.RGBA_8888;
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mHeight = bounds.height();
        mWidth = bounds.width();
    }

    private void startAnimation() {
        innerMove();
    }


    /**
     * 变换颜色方法
     */
    private void exchangeColor(){
        int mLeftPaintColor = mLeftPaint.getColor();
        int mRightPaintColor = mRightPaint.getColor();
        int mMiddlePaintColor = mMiddlePaint.getColor();
        mLeftPaint.setColor(mRightPaintColor);
        mRightPaint.setColor(mMiddlePaintColor);
        mMiddlePaint.setColor(mLeftPaintColor);
    }


    /**
     * 向内平移
     */
    private void innerMove() {
        final ValueAnimator translateXInnerAnimation = ValueAnimator.ofFloat(0,170);
        translateXInnerAnimation.setDuration(ANIMATION_DURATION);
        translateXInnerAnimation.setInterpolator(new AccelerateInterpolator());
        translateXInnerAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                translateX = (float) translateXInnerAnimation.getAnimatedValue();
                invalidateSelf();
            }
        });


        translateXInnerAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //变换颜色
                exchangeColor();
                expendMove();
            }
        });

        translateXInnerAnimation.start();
    }


    /**
     * 向外平移
     */
    private void expendMove(){
        final ValueAnimator translateXExpendAnimation = ValueAnimator.ofFloat(170,0);
        translateXExpendAnimation.setDuration(ANIMATION_DURATION);
        translateXExpendAnimation.setInterpolator(new DecelerateInterpolator());
        translateXExpendAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                translateX = (float) translateXExpendAnimation.getAnimatedValue();
                invalidateSelf();
            }
        });


        translateXExpendAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                innerMove();
            }
        });

        translateXExpendAnimation.start();
    }

}
