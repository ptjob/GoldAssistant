package com.parttimejob.swipe;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SwipeItem extends ViewGroup {

    public static final int TYPE_SCROLL_LEFT = 1;
    public static final int TYPE_SCROLL_RIGHT = 2;
    private Scroller mScroller;
    private ViewGroup mMainView;
    private ViewGroup mLeftView;
    private int mLeftViewWidth = 0;
    private ViewGroup mRightView;
    private int mRightViewWidth = 0;
    private int mMainViewWidth = 0;
    private int height;
    private LayoutInflater mInflater;
    private onScrollCompleteListener listener;

    public SwipeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        TypedArray array = getContext().obtainStyledAttributes(attrs,
                R.styleable.swipeitem);
        int leftRes = array.getResourceId(R.styleable.swipeitem_swipeview_left,
                -1);
        if (leftRes != -1) {
            mLeftView = (ViewGroup) mInflater.inflate(leftRes, null);
            String tagStr = array.getString(R.styleable.swipeitem_left_layout);
            if ("match".equals(tagStr)) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                mLeftView.setLayoutParams(params);
            } else if ("wrap".equals(tagStr)) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mLeftView.setLayoutParams(params);
            }
            addView(mLeftView);
        }
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        int mainRes = array.getResourceId(R.styleable.swipeitem_mainview, -1);
        if (mainRes != -1) {
            mMainView = (ViewGroup) mInflater.inflate(mainRes, null);
            LayoutParams params2 = new LayoutParams(rect.right,
                    LayoutParams.WRAP_CONTENT);
            mMainView.setLayoutParams(params2);
            addView(mMainView);
        } else
            throw new IllegalStateException(
                    "the xml should set swipe:mainview tag");

        int rightRes = array.getResourceId(
                R.styleable.swipeitem_swipeview_right, -1);
        if (rightRes != -1) {
            mRightView = (ViewGroup) mInflater.inflate(rightRes, null);
            String tagStr2 = array
                    .getString(R.styleable.swipeitem_right_layout);
            if ("match".equals(tagStr2)) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                mRightView.setLayoutParams(params);
            } else if ("wrap".equals(tagStr2)) {
                LayoutParams params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mRightView.setLayoutParams(params);
            }
            addView(mRightView);
        }

        init();
    }

    private void init() {
        mScroller = new Scroller(this.getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mMainView != null) {
            measureChild(mMainView, widthMeasureSpec, heightMeasureSpec);
            height = mMainView.getMeasuredHeight();
            if (mMainViewWidth == 0) {
                mMainViewWidth = mMainView.getMeasuredWidth();
            }
        }

        if (mLeftView != null) {
            measureChild(mLeftView, widthMeasureSpec,
                    mMainView.getMeasuredHeight());

            if (mLeftViewWidth == 0)
                mLeftViewWidth = mLeftView.getMeasuredWidth();
        }

        if (mRightView != null) {
            measureChild(mRightView, widthMeasureSpec,
                    mMainView.getMeasuredHeight());

            if (mRightViewWidth == 0) {
                mRightViewWidth = mRightView.getMeasuredWidth();
            }
        }

        setMeasuredDimension(mLeftViewWidth + mMainViewWidth + mRightViewWidth,
                height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (mMainView != null) {
            mMainView.layout(0, 0, mMainViewWidth, height);
        }

        if (mLeftView != null) {
            mLeftView.layout(-mLeftViewWidth, 0, 0, height);

        }

        if (mRightView != null) {
            mRightView.layout(mMainViewWidth, 0, mMainViewWidth
                    + mRightViewWidth, height);
        }

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public int getmLeftViewWidth() {
        return mLeftViewWidth;
    }

    public void setmLeftViewWidth(int mLeftViewWidth) {
        this.mLeftViewWidth = mLeftViewWidth;
    }

    public int getmRightViewWidth() {
        return mRightViewWidth;
    }

    public void setmRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }

    public void showLeft() {
        mScroller.startScroll(getScrollX(), 0,
                -(mLeftViewWidth + getScrollX()), 0);
        invalidate();
    }

    public void showRight() {
        mScroller.startScroll(getScrollX(), 0, mRightViewWidth - getScrollX(),
                0);
        invalidate();
    }

    public void restore() {
        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
        invalidate();
    }

    public void restoreNoAnimation() {
        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 0);
        invalidate();
    }

    public void setOnScrollCompleteListener(onScrollCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (l < 0 && Math.abs(l) == mLeftViewWidth) {
            if (listener != null) {
                listener.onScrollComplete(TYPE_SCROLL_RIGHT);
                setPressed(false);
                if (getParent() != null)
                    ((SwipeListView) getParent()).setPressed(false);
            }
        } else if (l > 0 && Math.abs(l) == mRightViewWidth) {

            if (listener != null) {
                listener.onScrollComplete(TYPE_SCROLL_LEFT);
                setPressed(false);
                if (getParent() != null)
                    ((SwipeListView) getParent()).setPressed(false);
            }

        }

    }

    public View getMainView() {
        return mMainView;
    }

    public View getLeftView() {
        return mLeftView;
    }

    public View getRightView() {
        return mRightView;
    }
    public interface onScrollCompleteListener {
        public void onScrollComplete(int type);
    }

}
