package com.parttime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by cjz on 2015/7/31.
 */
public class SingleSelectLayout extends SelectLayout {
    public SingleSelectLayout(Context context) {
        super(context);
    }

    public SingleSelectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleSelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SingleSelectLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        maxCount = 1;

    }

    @Override
    public void onClick(View v) {
        if(selectedIndexes.size() > 0){
            View view;
            for(Integer i : selectedIndexes){
                if(i < getChildCount()) {
                    view = getChildAt(i);
                    if (view != null && view.isSelected()) {
                        view.setSelected(false);
                    }
                }
            }
            selectedIndexes.clear();
        }
        int i = indexOfChild(v);
        if(i >= 0){
            selectedIndexes.add(i);
            v.setSelected(true);
        }
    }

    public int getSelectedindex(){
        List<Integer> selectedIndexes = getSelectedIndexes();
        if(selectedIndexes != null && selectedIndexes.size() >= 1){
            return selectedIndexes.get(0);
        }
        return -1;
    }

    public String getSelectedValue(){
        List<String> selectedValues = getSelectedValues();
        if(selectedValues != null && selectedValues.size() >= 1){
            return selectedValues.get(0);
        }
        return null;
    }
}
