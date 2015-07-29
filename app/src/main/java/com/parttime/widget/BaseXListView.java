package com.parttime.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.maxwin.view.XListView;

/**
 * 基础分页列表
 * Created by wyw on 2015/7/28.
 */
public class BaseXListView extends XListView {
    public BaseXListView(Context context) {
        super(context);
        init();
    }

    public BaseXListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        setBackgroundColor(Color.WHITE);
        setFadingEdgeLength(0);
        setScrollingCacheEnabled(false);
        setCacheColorHint(0);
        setPullRefreshEnable(true);
        setPullLoadEnable(true);
    }

    public void updateRefreshTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm ss");
        setRefreshTime(simpleDateFormat.format(new Date()));
    }
}
