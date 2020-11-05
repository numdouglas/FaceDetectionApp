package com.example.facedetectionapp.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic {

    private int RECT_COLOR = Color.BLUE;
    private float mStrokeWidth=4.0f;
    private Paint mRectPaint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;


    public RectOverlay(GraphicOverlay overlay, Rect rect) {
        super(overlay);
        this.rect=rect;
        this.graphicOverlay=overlay;
        mRectPaint=new Paint();
        mRectPaint.setColor(RECT_COLOR);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(mStrokeWidth);

        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF=new RectF(rect);
        rectF.left=translateX(rectF.left);
        rectF.right=translateX(rectF.right);
        rectF.top=translateY(rectF.top);
        rectF.bottom =translateY(rectF.bottom);

        canvas.drawRect(rectF,mRectPaint);
    }
}
