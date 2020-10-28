package com.mario.library.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


import com.mario.library.R;

import androidx.annotation.Nullable;

public class LoadArrowView extends View {


    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作
    private Paint mDefaultPaint;
    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;
    private Path path;
    private PathMeasure pathMeasure;
    private float mAnimatorValue;
    public LoadArrowView(Context context) {
        this(context, null);
    }

    public LoadArrowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;       // 缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, options);
        mMatrix = new Matrix();

        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(Color.RED);
        mDefaultPaint.setStrokeWidth(5);
        mDefaultPaint.setStyle(Paint.Style.STROKE);

        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        path = new Path();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        path.addCircle(mViewWidth / 2, mViewHeight / 2, 200, Path.Direction.CW);
        pathMeasure = new PathMeasure(path, false);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取动画进行的百分比
                mAnimatorValue = (float) animation.getAnimatedValue();
                postInvalidate(); // 更新界面
            }
        });
        // 设置动画的属性
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        pathMeasure.getPosTan(pathMeasure.getLength() * mAnimatorValue, pos, tan);
        //Path.Direction.CW顺时针方向

        float angle = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        mMatrix.reset();
        mMatrix.postRotate(angle,mBitmap.getWidth()/2,mBitmap.getHeight()/2);

        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2,pos[1] - mBitmap.getHeight() / 2);

        canvas.drawPath(path,mDefaultPaint);

        canvas.drawBitmap(mBitmap,mMatrix,mPaint);


    }
}
