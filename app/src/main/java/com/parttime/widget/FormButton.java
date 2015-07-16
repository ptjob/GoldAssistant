package com.parttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/16.
 */
public class FormButton extends TextView {
    private boolean initExecuted;
    private Context context;

    private boolean topLineShown = true;
    private boolean bottomLineShown = true;
    private int lineColor;
    private int lineWidth;
    private Paint paint;
    public FormButton(Context context) {
        super(context);
        if(!initExecuted){
            initExecuted = true;
            init(context, null);
        }
    }

    public FormButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public FormButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        lineColor = context.getResources().getColor(R.color.divider_color);
        lineWidth = context.getResources().getDimensionPixelSize(R.dimen.divider_width);
        lineWidth = lineWidth < 1 ? 1 : lineWidth;
        paint = new Paint();
        paint.setStrokeWidth(lineWidth);
        paint.setColor(lineColor);
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FormButton);
            if(typedArray != null){
                topLineShown = typedArray.getBoolean(R.styleable.FormButton_fb_topLine_shown, topLineShown);
                bottomLineShown = typedArray.getBoolean(R.styleable.FormButton_fb_bottomLine_show, bottomLineShown);
                lineColor = typedArray.getColor(R.styleable.FormButton_fb_line_color, lineColor);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if(topLineShown) {
            canvas.drawLine(0, 0, measuredWidth, 0, paint);
        }
        if(bottomLineShown) {
            canvas.drawLine(0, measuredHeight, measuredWidth, measuredHeight, paint);
        }

    }
}
