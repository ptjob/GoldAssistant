package com.parttimejob.swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class SwipeListView extends ListView {

    public static final int SWIPE_MODE_NONE = 0;
    public static final int SWIPE_MODE_NORMAL = 1;
    private int mCurrentMode = SWIPE_MODE_NORMAL;
    SwipeListTouchListener listener = new SwipeListTouchListener(this);
    OnScrollDeleteListener listener2;
    private int mLimitPos = -1;

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public int getLimitPos() {
        return mLimitPos;
    }

    public void setLimitPos(int mLimitPos) {
        this.mLimitPos = mLimitPos;
    }

    public int getmCurrentMode() {
        return mCurrentMode;
    }

    public void setmCurrentMode(int mCurrentMode) {
        this.mCurrentMode = mCurrentMode;
    }

    public void init() {
        setOnTouchListener(listener);
        super.clearFocus();
        setRecyclerListener(new RecyclerListener() {

            @Override
            public void onMovedToScrapHeap(View view) {
                SwipeItem swipeItem = (SwipeItem) view
                        .findViewById(R.id.swipe_item);
                swipeItem.restoreNoAnimation();
//				restoreTouchState();
            }
        });
    }

    public void setOnScrollDeleteListener(OnScrollDeleteListener l) {
        listener.bindScrollDeleteListener(l);
        listener2 = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public void restoreTouchState() {
        listener.resetTouchState();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            //
        }

    }

    public interface OnScrollDeleteListener {
        public void onScrollDelete(int type, int position);
    }

    public interface onListViewItemClickListener {

        public void onListViewItem(int position);

    }
}
