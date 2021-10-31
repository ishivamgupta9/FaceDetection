package com.example.facedetection.helper;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;


public class RectOverlay extends GraphicOverlay.Graphic{
    private final Rect rect;
    private int mRectangleColor=  Color.GREEN;
    private float strokewidth=7.0f;
    private Paint mRectPaint;
    private GraphicOverlay graphicOverlay;


//    @SuppressLint("ResourceAsColor")
    @SuppressLint("ResourceAsColor")
    public RectOverlay(GraphicOverlay graphicOverlay, Rect rect) {
        super(graphicOverlay);

        mRectPaint=new Paint();
        mRectPaint.setColor(mRectangleColor);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(strokewidth);

        this.graphicOverlay=graphicOverlay;
        this.rect=rect;


      postInvalidate();


    }

    @Override
    public void draw(Canvas canvas) {

        RectF rectF=new RectF(rect);
        rectF.left=translateX(rectF.left);
        rectF.right=translateX(rectF.right);
        rectF.top=translateX(rectF.top);
        rectF.bottom=translateX(rectF.bottom);

        canvas.drawRect(rectF,mRectPaint);

    }
}
