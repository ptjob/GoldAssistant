package com.parttime.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

import me.maxwin.view.XListView;

/**
 * 基础列表
 * Created by wyw on 2015/7/17.
 */
public class BaseListView extends ListView {
    public BaseListView(Context context) {
        super(context);
        init();
    }

    public BaseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        setBackgroundColor(Color.WHITE);
        setFadingEdgeLength(0);
        setScrollingCacheEnabled(false);
        setCacheColorHint(0);
    }
}
