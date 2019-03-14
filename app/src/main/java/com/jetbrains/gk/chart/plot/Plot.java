package com.jetbrains.gk.chart.plot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jetbrains.gk.chart.R;

import java.util.ArrayList;

public class Plot extends View {
    private static final String TAG = Plot.class.getName();

    private BorderStyle borderStyle;
    private final float borderRadiusX = 15;
    private final float borderRadiusY = 15;
    private Paint backgroundPaint;
    private Paint borderPaint;
    private DisplayDimensions dimensions;

    private final ArrayList<OnClickListener> listeners;
    private Thread renderThread;

    {
        dimensions = new DisplayDimensions();
        borderStyle = BorderStyle.SQUARE;
        listeners = new ArrayList<>();
        borderPaint = new Paint();
        borderPaint.setColor(Color.GRAY);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1.0f);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    public Plot(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Plot(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Plot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected final void init(Context context, AttributeSet attrs, int defStyle) {
        if (context != null && attrs != null) {
            loadAttrs(attrs, defStyle);
        }
    }

    private void loadAttrs(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            Class styleableClass = R.styleable.class;
            TypedArray typedAttrs = null;

            try {
                int[] resIds = (int[]) styleableClass.getField(getClass().getSimpleName()).get(null);
                typedAttrs = getContext().obtainStyledAttributes(attrs, resIds, defStyle, 0);
            } catch (IllegalAccessException e) {
                //skip
            } catch (NoSuchFieldException e) {
                Log.d(TAG, "Styleable definition not found for: " + getClass().getSimpleName());
            } finally {
                if (typedAttrs != null) {
                    // apply derived class' attrs:
                    Attrs.getInstance()
                            .setup(typedAttrs);
                    typedAttrs.recycle();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null)
            return;
        if (backgroundPaint != null) {
            drawBackground(canvas, dimensions.marginatedRect);
        }

        if (borderPaint != null) {
            drawBorder(canvas, dimensions.marginatedRect);
        }
    }

    protected void drawBorder(Canvas canvas, RectF rectF) {
        drawRect(canvas, rectF, borderPaint);
    }

    protected void drawBackground(Canvas canvas, RectF rectF) {
        drawRect(canvas, rectF, backgroundPaint);
    }

    protected void drawRect(Canvas canvas, RectF dims, Paint paint) {
        switch (borderStyle) {
            case ROUNDED:
                canvas.drawRoundRect(dims, borderRadiusX, borderRadiusY, paint);
                break;
            case SQUARE:
            default:
                canvas.drawRect(dims, paint);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        RectF cRect = new RectF(0, 0, w, h);
        RectF mRect = new RectF(cRect.left + 10,
                cRect.top + 10,
                cRect.right - 10,
                cRect.bottom - 10);
        RectF pRect = new RectF(mRect.left + 5,
                mRect.top + 5,
                mRect.right - 5,
                mRect.bottom - 5);

        dimensions = new DisplayDimensions(cRect, mRect, pRect);
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
