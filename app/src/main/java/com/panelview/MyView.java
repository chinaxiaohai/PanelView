package com.panelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by ThinkPad on 2016/6/20.
 */
public class MyView extends View {
    private int mWidth;
    private int mHeight;

    private Paint paintArc;
    private Paint paintArc1;
    private Paint paintArc2;
    private int arcColor;
    private int arcColor1;
    private int arcColor2;
    private int arcType;
    private int arcWidth;
    private int textSize;
    private String text;


    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PanleView, defStyleAttr, 0);
        arcColor = a.getColor(R.styleable.PanleView_arcColor, Color.parseColor("#5FB1ED"));
        arcColor1 = a.getColor(R.styleable.PanleView_arcColor, Color.parseColor("#5FB1ED"));
        arcColor2 = a.getColor(R.styleable.PanleView_arcColor, Color.parseColor("#5FB1ED"));
        arcType = a.getInt(R.styleable.PanleView_arcType, 0);
        arcWidth = a.getDimensionPixelSize(R.styleable.PanleView_arcWidth, 2);
        textSize = a.getDimensionPixelSize(spToPx(R.styleable.PanleView_android_textSize), 13);
        text = a.getString(R.styleable.PanleView_android_text);
        a.recycle();

        paintArc = new Paint();
        paintArc1 = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int strokeWidth = arcWidth;
        paintArc.reset();
        paintArc.setColor(arcColor);
        paintArc.setStrokeWidth(strokeWidth);
        paintArc.setAntiAlias(true);
        paintArc.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(strokeWidth, strokeWidth, mWidth - strokeWidth, mHeight - strokeWidth);
        canvas.drawArc(rectF, 145, 250, false, paintArc);
        paintArc1.setStrokeWidth(3);
        canvas.drawLine(mWidth / 2, strokeWidth, mWidth / 2, 40, paintArc1);
        int count = 50;
        float rAngle = 250f / count;
        for (int i = 0; i < count/2; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(mWidth / 2, strokeWidth, mWidth / 2, 40, paintArc1);
                paintArc1.setTextSize(20);
                canvas.drawText(i + "", (mWidth / 2)-5, 40 + 10, paintArc1);
                canvas.rotate(rAngle, mWidth / 2, mHeight / 2);
            } else {
                canvas.drawLine(mWidth / 2, strokeWidth, mWidth / 2, 20, paintArc1);
                canvas.rotate(rAngle, mWidth / 2, mHeight / 2);
            }

        }
        canvas.rotate((count * 5), mWidth / 2, mHeight / 2);

        for (int i = 0; i < count/2; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(mWidth / 2, strokeWidth, mWidth / 2, 40, paintArc1);
                paintArc1.setTextSize(20);
                canvas.drawText(i + "", (mWidth / 2)-5, 40 + 10, paintArc1);
                canvas.rotate(-rAngle, mWidth / 2, mHeight / 2);
            } else {
                canvas.drawLine(mWidth / 2, strokeWidth, mWidth / 2, 20, paintArc1);
                canvas.rotate(-rAngle, mWidth / 2, mHeight / 2);
            }

        }
        canvas.rotate(-(count * 5), mWidth / 2, mHeight / 2);
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
}
