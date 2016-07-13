package com.panelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by ThinkPad on 2016/6/16.
 */
public class PanelView extends View {
    private int mWidth;
    private int mHeight;

    private int mPercent;

    //刻度宽度
    private float mTikeWidth;

    //第二个弧的宽度
    private int mScendArcWidth;

    //最小圆的半径
    private int mMinCircleRadius;

    //文字矩形的宽
    private int mRectWidth;

    //文字矩形的高
    private int mRectHeight;


    //文字内容
    private String mText = "";

    //文字的大小
    private int mTextSize;

    //设置文字颜色
    private int mTextColor;
    private int mArcColor;

    //小圆和指针颜色
    private int mMinCircleColor;

    //刻度的个数
    private int mTikeCount;

    private Context mContext;

    private static final int[] SECTION_COLORS = {Color.GREEN, Color.YELLOW, Color.RED};

    public PanelView(Context context) {
        this(context, null);
    }

    public PanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PanleView, defStyleAttr, 0);
        mArcColor = a.getColor(R.styleable.PanleView_arcColor, Color.parseColor("#5FB1ED"));
        mMinCircleColor = a.getColor(R.styleable.PanleView_pointerColor, Color.parseColor("#C9DEEE"));
        mTikeCount = a.getInt(R.styleable.PanleView_tikeCount, 12);
        mTextSize = a.getDimensionPixelSize(spToPx(R.styleable.PanleView_android_textSize), 13);
        mText = a.getString(R.styleable.PanleView_android_text);
        mScendArcWidth = 50;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        int strokeWidth = 3;
        p.setStrokeWidth(strokeWidth);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(mArcColor);
        //最外面线条
        canvas.drawArc(new RectF(strokeWidth, strokeWidth, mWidth - strokeWidth, mHeight - strokeWidth), 145, 250, false, p);


        /***
         *
         */
        RectF secondRectF = new RectF(strokeWidth + 40, strokeWidth + 40, mWidth - strokeWidth - 40, mHeight - strokeWidth - 40);
        float secondRectWidth = mWidth - strokeWidth - 50 - (strokeWidth + 50);
        float secondRectHeight = mHeight - strokeWidth - 50 - (strokeWidth + 50);
        float percent = mPercent / 100f;

        //充满的圆弧的度数    -5是大小弧的偏差
        float fill = 250 * percent;

        //空的圆弧的度数
        float empty = 250 - fill;
//        Log.e("wing", fill + "");
        //画粗弧突出部分左端
        p.setStrokeWidth(20);
        canvas.drawArc(secondRectF, 145, fill, false, p);


        p.setColor(mArcColor);
        //绘制第一条最上面的刻度
        mTikeWidth = 20;
        p.setStrokeWidth(3);

        canvas.drawLine(mWidth / 2, strokeWidth + 30, mWidth / 2, mTikeWidth + 30, p);
        //旋转的角度
        float rAngle = 250f / mTikeCount;
        //通过旋转画布 绘制右面的刻度
        for (int i = 0; i < mTikeCount / 2; i++) {
            canvas.rotate(rAngle, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2, strokeWidth + 30, mWidth / 2, mTikeWidth + 30, p);
        }
        //现在需要将将画布旋转回来
        canvas.rotate(-rAngle * mTikeCount / 2, mWidth / 2, mHeight / 2);

        //通过旋转画布 绘制左面的刻度
        for (int i = 0; i < mTikeCount / 2; i++) {
            canvas.rotate(-rAngle, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2, strokeWidth + 30, mWidth / 2, mTikeWidth + 30, p);
        }
        //现在需要将将画布旋转回来
        canvas.rotate(rAngle * mTikeCount / 2, mWidth / 2, mHeight / 2);

        p.setColor(mArcColor);
        //绘制小圆外圈
        p.setStrokeWidth(3);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 30, p);


        //绘制小圆内圈
        p.setColor(mMinCircleColor);
        p.setStrokeWidth(8);
        mMinCircleRadius = 15;
        canvas.drawCircle(mWidth / 2, mHeight / 2, mMinCircleRadius, p);


        //绘制指针
        p.setColor(mMinCircleColor);
        p.setStrokeWidth(4);


        //按照百分比绘制刻度
        canvas.rotate((250 * percent - 250 / 2), mWidth / 2, mHeight / 2);
        canvas.drawLine(mWidth / 2, (mHeight / 2 - secondRectHeight / 2) + mScendArcWidth / 2 + 2, mWidth / 2, mHeight / 2 - mMinCircleRadius, p);
        //将画布旋转回来
        canvas.rotate(-(250 * percent - 250 / 2), mWidth / 2, mHeight / 2);

        float rectBottomY = mHeight / 2 + secondRectHeight / 3 + mRectHeight;
        p.setTextSize(mTextSize);
        mTextColor = Color.GREEN;
        p.setColor(mTextColor);
        p.setStrokeWidth(1);
        p.setStyle(Paint.Style.FILL);
        float txtLength = p.measureText(mText);
        canvas.drawText(mText, (mWidth - txtLength) / 2, rectBottomY + 40, p);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = dpToPx(200);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = dpToPx(200);
        }
        setMeasuredDimension(mWidth, mHeight);

    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * 设置百分比
     *
     * @param percent
     */
    public void setPercent(int percent) {
        mPercent = percent;
        invalidate();
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        mText = text;
        invalidate();
    }

    /**
     * 设置圆弧颜色
     *
     * @param color
     */

    public void setArcColor(int color) {
        mArcColor = color;

        invalidate();
    }


    /**
     * 设置指针颜色
     *
     * @param color
     */
    public void setPointerColor(int color) {
        mMinCircleColor = color;

        invalidate();
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        mTextSize = size;

        invalidate();
    }

    /**
     * 设置粗弧的宽度
     *
     * @param width
     */
    public void setArcWidth(int width) {
        mScendArcWidth = width;

        invalidate();
    }

}
