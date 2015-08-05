package com.parttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/12.
 */
public class CountingEditText extends EditText {
    private boolean initExecuted;

    protected Context context;
    protected EditText et;
    protected TextView tvCount;

    private int maxLen;
    private int countTextColor;
    private int countTextSize;
    private int countBarHeight;

    private Paint paint;
    private int paddingBottom;
    private int paddingRight;

    private int spaceInBar;

    private boolean paddingSet;

    public CountingEditText(Context context) {
        super(context);
        if(!initExecuted){
            initExecuted = true;
            init(context, null);
        }
    }

    public CountingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public CountingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CountingEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        countTextColor = context.getResources().getColor(R.color.txt_color_gray);
        countTextSize = context.getResources().getDimensionPixelSize(R.dimen.counting_text_default_size);
        countBarHeight = context.getResources().getDimensionPixelSize(R.dimen.counting_bar_default_height);

        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountingEditText);
            maxLen = typedArray.getInt(R.styleable.CountingEditText_cet_maxLen, Integer.MAX_VALUE);
            countTextColor = typedArray.getColor(R.styleable.CountingEditText_cet_countTextColor, countTextColor);
            countTextSize = typedArray.getDimensionPixelSize(R.styleable.CountingEditText_cet_countTextSize, countTextSize);
            countBarHeight = typedArray.getDimensionPixelSize(R.styleable.CountingEditText_cet_countBarHeight, countBarHeight);
        }
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(countTextColor);
        paint.setTextSize(countTextSize);

        paddingBottom = getPaddingBottom();
        paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();

        spaceInBar = (countBarHeight - countTextSize) / 2;
        paddingSet = true;

        super.setPadding(paddingLeft, paddingTop, paddingRight, countBarHeight + paddingBottom);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    @Override
    public int getPaddingBottom() {
        return super.getPaddingBottom()  - (paddingSet ? countBarHeight : 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.translate(0, -countBarHeight);
        super.onDraw(canvas);
//        canvas.translate(0, countBarHeight);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        String text = getText().length() + "/" + maxLen;
        float txtWidth = paint.measureText(text);
        canvas.drawText(text, measuredWidth - paddingRight - txtWidth, measuredHeight - paddingBottom - spaceInBar + getScrollY(), paint);
    }

}
