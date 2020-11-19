package com.qinbin.p2p.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.qinbin.p2p.R;

/**
 * Created by teacher on 2016/4/25.
 */
public class RingProgress extends View {
    public static final int DEFAULT_STROKE_WIDTH_IN_PX = 10;
    public static final int DEFAULT_TEXT_SIZE_IN_SP = 16;
    private Paint textPaint;


    public RingProgress(Context context) {
        super(context);
        init(null);
    }

    public RingProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    float progress = 0f;

    Paint grayPaint;
    private Paint redPaint;
    int strokeWidth = DEFAULT_STROKE_WIDTH_IN_PX;
    int textSize;

    private void init(AttributeSet attrs) {
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE_IN_SP, getResources().getDisplayMetrics());

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RingProgress);
            strokeWidth = typedArray.getDimensionPixelSize(R.styleable.RingProgress_stroke_width, DEFAULT_STROKE_WIDTH_IN_PX);
            textSize = (int) typedArray.getDimension(R.styleable.RingProgress_text_size, textSize);
            progress = typedArray.getFloat(R.styleable.RingProgress_progress,0f);
            typedArray.recycle();
        }

        grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        grayPaint.setColor(Color.GRAY);
        grayPaint.setStyle(Paint.Style.STROKE);
        grayPaint.setStrokeWidth(strokeWidth);

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(Color.RED);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFF0094FF);
        textPaint.setTextSize(textSize);
    }

    public void setStrokeWidth(int strokeWidthInPx) {
        this.strokeWidth = strokeWidthInPx;
        grayPaint.setStrokeWidth(strokeWidthInPx);
        redPaint.setStrokeWidth(strokeWidthInPx);
        invalidate();
    }

    public void setTextSize(int textSizeInPx) {
        this.textSize = textSize;
        textPaint.setTextSize(textSizeInPx);
        invalidate();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        computeValues();

        drawBackGroundRing(canvas);
        drawProgress(canvas);
        drawText(canvas);

    }

    PointF center = new PointF();
    float radius = 0;
    RectF rect = new RectF();

    private void computeValues() {
        center.x = getWidth() / 2;
        center.y = getHeight() / 2;
        radius = Math.min(getWidth() / 2, getHeight() / 2) - strokeWidth / 2 - 1;

        rect.set(center.x - radius, center.y - radius, center.x + radius, center.y + radius);

    }


    private void drawBackGroundRing(Canvas canvas) {
        // 如果要画环，需要给画笔指定style为Paint.Style.STROKE
        // 并对画笔指定描边的宽度setStrokeWidth(strokeWidth);
        // 那radius参数就是环的中间的位置，在半径的内外两侧各有strokeWidth/2的像素
        canvas.drawCircle(center.x, center.y, radius, grayPaint);
    }


    private void drawProgress(Canvas canvas) {
        // 第一个参数，矩形，指的是弧的外切圆
        // 第二个参数，起始角度，是360进制的，0，代表3点钟方向
        // 第三个参数，扫过的角度，也是360进制的，如果是正值，就是顺时针；负值，就是逆时针
        // 第四个参数，boolean，如果是false，就是弧，如果是true，就是扇
        // 第五个参数，画笔，略
        canvas.drawArc(rect, -90, 360.0f * progress, false, redPaint);
    }


    Rect textBound = new Rect();

    private void drawText(Canvas canvas) {
        String text = String.format("%.2f%%", progress * 100);
        // 第二、第三个参数，指定的位置是文字区域的左下角
        textPaint.getTextBounds(text, 0, text.length(), textBound);
        canvas.drawText(text, center.x - textBound.width() / 2, center.y + textBound.height() / 2, textPaint);
    }
}
