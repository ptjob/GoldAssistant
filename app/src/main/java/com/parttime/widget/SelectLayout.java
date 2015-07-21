package com.parttime.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cjz on 2015/7/20.
 */
public class SelectLayout extends CommonShowItemLayout implements View.OnClickListener{
    private Context context;
    private boolean initExecuted;

    private int textSize;

    private ColorStateList textColorList;
    private int textColor;

//    private ColorStateList itemBgColorList;
//    private Drawable itemBgDrawable;
    private int itemBgColor;
    private int itemBgResId;

    private int maxCount = 5;

//    private List<Integer> selectedIndexes = new ArrayList<Integer>();
//    private List<String> selecteds = new ArrayList<String>();
    private Set<Integer> selectedIndexes = new LinkedHashSet<Integer>();
    private Map<Integer, String> valueMap = new HashMap<Integer, String>();

    public SelectLayout(Context context) {
        super(context);
        if(!initExecuted){
            initExecuted = true;
            init(context, null);
        }
    }

    public SelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public SelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    public SelectLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(!initExecuted){
            initExecuted = true;
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        Resources res = context.getResources();
        textSize = (int) (res.getDisplayMetrics().density * 15);
        textColor = 0xff333333;
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectLayout);
            if(typedArray != null){
                textColorList = typedArray.getColorStateList(R.styleable.SelectLayout_sl_textColor);
                if(textColorList == null){
                    textColor = typedArray.getColor(R.styleable.SelectLayout_sl_textColor, textColor);
                }
                textSize = typedArray.getDimensionPixelSize(R.styleable.SelectLayout_sl_textSize, textSize);
//                itemBgResId = typedArray.getResourceId(R.styleable.SelectLayout_sl_itemBackground, 0);
//                ColorStateList colorStateList = typedArray.getColorStateList(R.styleable.SelectLayout_sl_itemBackground);
//                typedArray.get
//                itemBgDrawable = typedArray.getDrawable(R.styleable.SelectLayout_sl_itemBackground);
                itemBgResId = typedArray.getResourceId(R.styleable.SelectLayout_sl_itemBackground, 0);
//                int color = typedArray.getColor(R.styleable.SelectLayout_sl_itemBackground, 0);
                maxCount = typedArray.getInt(R.styleable.SelectLayout_sl_maxSelectCount, maxCount);
            }
        }
    }

    public void add(String itemValue){
        TextView si = new TextView(context);
        si.setOnClickListener(this);
        si.setGravity(Gravity.CENTER);
        if(itemBgResId > 0){
            si.setBackgroundResource(itemBgResId);
        }
        si.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        if(textColorList != null) {
            si.setTextColor(textColorList);
        }else {
            si.setTextColor(textColor);
        }
        si.setText(itemValue);
        addView(si);
        valueMap.put(indexOfChild(si), itemValue);
    }

    @Override
    public void onClick(View v) {
        if(v.isSelected()){
            v.setSelected(false);
            selectedIndexes.remove(indexOfChild(v));
        }else {
            if(selectedIndexes.size() >= maxCount){
                return;
            }else {
                v.setSelected(true);
                selectedIndexes.add(indexOfChild(v));
            }
        }
    }

    public List<Integer> getSelectedIndexes(){

        return new ArrayList<Integer>(selectedIndexes);
    }

    public List<String> getSelectedValues(){
        return new ArrayList<String>(valueMap.values());
    }
}
