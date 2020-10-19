package com.mario.library.drawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DouYinLoadingDrawable extends Drawable implements Animatable {

    private Paint leftBallPaint, rightBallPaint, coincideBallPaint;

    private Path leftBallPath, rightBallPath, coincideBallPath;

    private float translate, scale;

    private Direction mCurrentDirection;

    private AnimatorSet animatorSet;

    private float mWidth, mHeight;
    private float centerX, centerY;
    private float radius = 20f;

    public DouYinLoadingDrawable() {
        leftBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        leftBallPaint.setAntiAlias(true);
        leftBallPaint.setColor(Color.parseColor("#FF00EEEE"));
        leftBallPaint.setStyle(Paint.Style.FILL);

        rightBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rightBallPaint.setAntiAlias(true);
        rightBallPaint.setColor(Color.parseColor("#FFFF4040"));
        rightBallPaint.setStyle(Paint.Style.FILL);

        coincideBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coincideBallPaint.setAntiAlias(true);
        coincideBallPaint.setColor(Color.BLACK);
        coincideBallPaint.setStyle(Paint.Style.FILL);

        leftBallPath = new Path();
        rightBallPath = new Path();
        coincideBallPath = new Path();

        mCurrentDirection = Direction.LEFT;
    }


    @Override
    public void start() {
        startAnimation();
    }

    @Override
    public void stop() {
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.end();
        }
    }

    @Override
    public boolean isRunning() {
        return animatorSet.isRunning();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mCurrentDirection == Direction.LEFT) {
            canvas.save();
            leftBallPath.reset();
            leftBallPath.addCircle(centerX - radius + translate, centerY, radius, Path.Direction.CCW);
            canvas.drawPath(leftBallPath, leftBallPaint);
            canvas.restore();


            canvas.save();
            rightBallPath.reset();
            rightBallPath.addCircle(centerX + radius - translate, centerY, radius * scale, Path.Direction.CCW);
            canvas.drawPath(rightBallPath, rightBallPaint);
            canvas.restore();

            canvas.save();
            coincideBallPath.reset();
            coincideBallPath.op(leftBallPath, rightBallPath, Path.Op.INTERSECT);
            canvas.drawPath(coincideBallPath, coincideBallPaint);
            canvas.restore();
        } else if (mCurrentDirection == Direction.RIGHT) {
            canvas.save();
            rightBallPath.reset();
            rightBallPath.addCircle(centerX - radius + translate, centerY, 20, Path.Direction.CCW);
            canvas.drawPath(rightBallPath, rightBallPaint);
            canvas.restore();

            canvas.save();
            leftBallPath.reset();
            leftBallPath.addCircle(centerX + radius - translate, centerY, 20 * scale, Path.Direction.CCW);
            canvas.drawPath(leftBallPath, leftBallPaint);
            canvas.restore();

            canvas.save();
            coincideBallPath.reset();
            coincideBallPath.op(leftBallPath, rightBallPath, Path.Op.INTERSECT);
            canvas.drawPath(coincideBallPath, coincideBallPaint);
            canvas.restore();
        }


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
        mWidth = bounds.width();
        mHeight = bounds.height();
        centerX = mWidth / 2;
        centerY = mHeight / 2;
    }

    private void startAnimation() {
        leftAnimation();
    }


    private void leftAnimation() {
        final ValueAnimator translateAnimator = ValueAnimator.ofFloat(0, 2 * radius);
        translateAnimator.setStartDelay(200);
        translateAnimator.setDuration(350);
        translateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                translate = (float) translateAnimator.getAnimatedValue();
                invalidateSelf();
            }
        });

        final ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1, 0.5f, 1);
        scaleAnimator.setStartDelay(200);
        scaleAnimator.setDuration(350);
        scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (float) scaleAnimator.getAnimatedValue();
                invalidateSelf();
            }
        });

        translateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCurrentDirection == Direction.LEFT)
                    mCurrentDirection = Direction.RIGHT;
                else
                    mCurrentDirection = Direction.LEFT;
                translate = 0;
                leftAnimation();
            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateAnimator, scaleAnimator);
        animatorSet.start();

    }

    public enum Direction {
        LEFT,
        RIGHT
    }


}
