package com.parttime.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.qingmu.jianzhidaren.R;

/**
 * Created by cjz on 2015/7/18.
 */
public class CommonShowItemLayout extends ViewGroup{
    private int verticleInnerMargin, horizontalInnerMargin;
    private int columnCount;
    private int itemHeight;
    private int itemWidth;

    protected Context context;

    protected boolean initExecuted;

    public CommonShowItemLayout(Context context) {
        super(context);
        if(!initExecuted){
            initExecuted = true;
            init(context, null);
        }
    }

    public CommonShowItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public CommonShowItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommonShowItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    protected void init(Context context, AttributeSet attrs){
        this.context = context;
        Resources res = context.getResources();
        horizontalInnerMargin = res.getDimensionPixelSize(R.dimen.csil_horizontal_inner_margin_default);
        verticleInnerMargin = res.getDimensionPixelSize(R.dimen.csil_verticle_inner_margin_default);
        itemHeight = res.getDimensionPixelSize(R.dimen.csil_item_height_default);
        columnCount = 3;

        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonShowItemLayout);
            if(typedArray != null){
                horizontalInnerMargin = typedArray.getDimensionPixelSize(R.styleable.CommonShowItemLayout_csil_horizontalInnerMargin, horizontalInnerMargin);
                verticleInnerMargin = typedArray.getDimensionPixelSize(R.styleable.CommonShowItemLayout_csil_verticleInnerMargin, verticleInnerMargin);
                itemHeight = typedArray.getDimensionPixelSize(R.styleable.CommonShowItemLayout_csil_itemHeight, itemHeight);
                columnCount = typedArray.getInt(R.styleable.CommonShowItemLayout_csil_columnCount, columnCount);
            }
        }
    }

//    public void setAda

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        int x = paddingLeft;
        int y = paddingTop;

        int childCount = getChildCount();
        View child;
        for(int i = 0; i < childCount; ++i){
            child = getChildAt(i);
            child.layout(x, y, x + itemWidth, y + itemHeight);
            x += (itemWidth + horizontalInnerMargin);
            if((i + 1) % columnCount == 0){
                x = paddingLeft;
                y += (itemHeight + verticleInnerMargin);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureHeight(measuredWidth, heightMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        itemWidth = (measuredWidth - paddingLeft - paddingRight - (columnCount - 1) * horizontalInnerMargin) / columnCount;
        int cellWidthSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
        int cellHeightSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);

            childView.measure(cellWidthSpec, cellHeightSpec);
        }

        setMeasuredDimension(/*resolveSize(getMeasuredWidth(), widthMeasureSpec),
                resolveSize(getMeasuredHeight(), heightMeasureSpec)*/measuredWidth, measuredHeight);

    }

    private int measureHeight(int measureWidth, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
//
//
//
        int result;
//
//        if (specMode == MeasureSpec.AT_MOST){
//
//            result = specSize;
//        }
//        else if (specMode == MeasureSpec.EXACTLY){
//
//            result = specSize;
//        } else {
        if( specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            int childCount = getChildCount();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            result = getPaddingTop() + getPaddingBottom();
            int rowCount = childCount % columnCount > 0 ? childCount / columnCount + 1 : childCount / columnCount;
            result += rowCount * itemHeight;
            if(rowCount > 0){
                result += (rowCount - 1) * verticleInnerMargin;
            }
        }
        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 500;
        if (specMode == MeasureSpec.AT_MOST){

            result = specSize;
        }

        else if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }
        return result;
    }
}
