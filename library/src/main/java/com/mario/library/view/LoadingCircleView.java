package com.mario.library.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class LoadingCircleView extends View {

    private Path path;
    private PathMeasure pathMeasure;

    private Path dst; // 被截取的路径

    private Paint mPaint; // 画笔

    // View的宽高
    private int height;
    private int width;

    private float radius; // 空心圆的半径
    private float mLength; // path路径的长度

    ValueAnimator valueAnimator; // 属性动画
    private float mAnimatorValue; // 属性动画返回的百分比

    private float stop; // 截取路径时的stopD值
    private float start; // 截取路径时的startD值

    public LoadingCircleView(Context context) {
        this(context,null);
    }

    public LoadingCircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 21, getContext().getResources().getDisplayMetrics()); // 初始化半径
        path = new Path();
        dst = new Path();
        mPaint = new Paint();
        pathMeasure = new PathMeasure();

        // 设置画笔属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xbfe46d32);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.5f, getContext().getResources().getDisplayMetrics()));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        // 勾勒空心圆
        path.reset();
        path.addCircle(width / 2, height / 2, radius, Path.Direction.CW);
        // 生成pathMeasure对象
        pathMeasure.setPath(path, true);

        // 获取path的长度
        mLength = pathMeasure.getLength();

        // 通过属性动画取得百分比值，并更新View
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取动画进行的百分比
                mAnimatorValue = (float) animation.getAnimatedValue();
                postInvalidate(); // 更新界面
            }
        });
        // 设置动画的属性
        valueAnimator.setDuration(2100);
        valueAnimator.setRepeatCount(1000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

         //为加强动画效果，每次对画布旋转不同角度
        canvas.rotate(360.0f * mAnimatorValue - 45.0f, width / 2, height / 2);
         //初始化截取的路径
        dst.reset();
        dst.lineTo(0, 0); // 消除硬件加速的影响
        // 更新截取的开始值和结束值：当mAnimatorValue为0或1时，两个值相等
        stop = mAnimatorValue * mLength;
        start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * mLength));
        // 截取路径后，并绘制路径
        pathMeasure.getSegment(start, stop, dst, true);
        canvas.drawPath(dst, mPaint);
//        canvas.translate(width/2,height/2);
//        Path mPath = new Path();
//        Path mDst = new Path();
//        PathMeasure mPathMeasure = new PathMeasure();
//// 顺时针画 半径为400px的圆
//        mPath.addCircle(0,0, 400, Path.Direction.CW);
//        mPathMeasure.setPath(mPath, false);
//
//// 画直线
//        mDst.moveTo(110, 0);
//        mDst.lineTo(200, 300);
//
//// 截取 0.25 到 0.5 距离的圆弧放置dst中
//        mPathMeasure.getSegment(mPathMeasure.getLength() * 0.25f,
//                mPathMeasure.getLength() * 0.5f,
//                mDst,
//                true);

//        canvas.drawPath(mDst, paint);
    }
}
