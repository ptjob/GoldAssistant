package com.parttimejob.swipe;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;

public class SwipeListTouchListener implements OnTouchListener {

    public static final String TAG = "SwipeListTouchListener";

    public static final int STATE_TOUCH_NONE = 0;
    private int currentState = STATE_TOUCH_NONE;
    public static final int STATE_TOUCH_X = 1;
    public static final int STATE_TOUCH_Y = 2;
    public static final int STATE_SWIPE_LEFT = 3;
    public static final int STATE_SWIPE_NORMAL = 4;
    private int mOpendState = STATE_SWIPE_NORMAL;
    public static final int STATE_SWIPE_RIGHT = 5;
    private boolean isClick = true;
    private boolean isTouchRestore = false;
    private int lastMotionX;
    private int lastMotionY;
    private SwipeItem currentItem;
    private int currentPosition = -1;
    private int lastPosition = -1;
    private Rect rect = new Rect();

    private SwipeListView mListView;
    private SwipeListView.OnScrollDeleteListener listener;

    private int lastDeltaX = 0;

    private int hasScrollX = 0;

    private int touchSlop;

    public SwipeListTouchListener(SwipeListView swipeListView) {
        this.mListView = swipeListView;
        init();
    }

    public void init() {
        ViewConfiguration configuration = ViewConfiguration.get(mListView
                .getContext());
        touchSlop = configuration.getScaledTouchSlop();
    }

    public void bindScrollDeleteListener(SwipeListView.OnScrollDeleteListener l) {
        listener = l;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mListView.getmCurrentMode() == 0) {
            return false;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastMotionX = (int) event.getRawX();
                lastMotionY = (int) event.getRawY();
                mListView.setPressed(false);
                getTouchView(event);
                if (currentPosition == -1 || mListView.getLimitPos() > currentPosition) {
                    if (currentItem != null) {
                        currentItem.restore();
                        hasScrollX = 0;
                        mOpendState = STATE_SWIPE_NORMAL;
                    }
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (currentPosition == -1 || mListView.getLimitPos() >= currentPosition) {
                    return false;
                }
                isClick = false;
                if (currentState == STATE_TOUCH_NONE) {
                    checkInMove(event);

                } else if (currentState == STATE_TOUCH_X) {
                    mListView.setClickable(false);
                    mListView.setPressed(false);
                    mListView.setFocusable(false);
                    currentItem.setPressed(false);
                    currentItem.setFocusable(false);
                    currentItem
                            .setOnScrollCompleteListener(new SwipeItem.onScrollCompleteListener() {

                                @Override
                                public void onScrollComplete(int type) {
                                    if (listener != null)
                                        listener.onScrollDelete(type,
                                                currentPosition);

                                }
                            });

                    int deltaX = (int) (event.getRawX() - lastMotionX);
                    if (currentItem.getmLeftViewWidth() > 0) {
                        int scrollX = hasScrollX - deltaX;
                        if (scrollX > 0) {//往右滑
                            if (scrollX > currentItem.getmRightViewWidth())
                                scrollX = currentItem.getmRightViewWidth();
                        } else {
                            if (-scrollX > currentItem.getmLeftViewWidth()) {
                                scrollX = currentItem.getmLeftViewWidth();
                            }
                        }
                        currentItem.scrollTo(scrollX, 0);
                        lastDeltaX = deltaX;
                    } else if (deltaX < 0) {
                        int scrollX = hasScrollX - deltaX;
                        if (scrollX > 0) {//往右滑
                            if (scrollX > currentItem.getmRightViewWidth())
                                scrollX = currentItem.getmRightViewWidth();
                        } else {
                            if (-scrollX > currentItem.getmLeftViewWidth()) {
                                scrollX = currentItem.getmLeftViewWidth();
                            }
                        }
                        currentItem.scrollTo(scrollX, 0);
                        lastDeltaX = deltaX;
                    } else {
                        lastDeltaX = 0;
                    }

                }
                return currentState == STATE_TOUCH_X;
            case MotionEvent.ACTION_UP:
                mListView.setPressed(false);
                mListView.setFocusable(false);
                if (currentPosition == -1 || mListView.getLimitPos() >= currentPosition) {
                    Log.i(TAG, "return false!");
                    return false;
                }
                currentItem.setPressed(false);
                currentItem.setFocusable(false);
                if (currentState == STATE_TOUCH_NONE
                        || currentState == STATE_TOUCH_Y) {
                    if (isTouchRestore) {
                        isClick = false;
                    } else {
                        isClick = true;
                    }

                } else {
                    isClick = false;
                    // 大于0 表示 X数据向右滑动
                    if (lastDeltaX > 0) {
                        switch (mOpendState) {
                            case STATE_SWIPE_LEFT:
                                if (currentItem.getmLeftViewWidth() > 0) {
                                    currentItem.showLeft();
                                    mOpendState = STATE_SWIPE_LEFT;
                                    hasScrollX = -currentItem.getmLeftViewWidth();
                                }
                            case STATE_SWIPE_NORMAL:
                                // 正常状态下 向右滑。显示左边
                                if (Math.abs(currentItem.getScrollX()) > currentItem
                                        .getmLeftViewWidth() / 2) {
                                    currentItem.showLeft();
                                    mOpendState = STATE_SWIPE_LEFT;
                                    hasScrollX = -currentItem.getmLeftViewWidth();
                                } else {
                                    currentItem.restore();
                                    hasScrollX = 0;
                                    mOpendState = STATE_SWIPE_NORMAL;
                                }
                                break;
                            case STATE_SWIPE_RIGHT:
                                int swipeLength = Math.abs(currentItem
                                        .getmRightViewWidth()
                                        - currentItem.getScrollX());
                                if (swipeLength <= currentItem.getmRightViewWidth() / 2) {
                                    currentItem.showRight();
                                    mOpendState = STATE_SWIPE_RIGHT;
                                    hasScrollX = currentItem.getmRightViewWidth();
                                } else if (swipeLength > currentItem
                                        .getmRightViewWidth() / 2
                                        && swipeLength < currentItem
                                        .getmRightViewWidth()) {
                                    currentItem.restore();
                                    mOpendState = STATE_SWIPE_NORMAL;
                                    hasScrollX = 0;
                                } else if (swipeLength > currentItem
                                        .getmRightViewWidth()
                                        && swipeLength < (currentItem
                                        .getmRightViewWidth() + currentItem
                                        .getmLeftViewWidth() / 2)) {
                                    currentItem.restore();
                                    mOpendState = STATE_SWIPE_NORMAL;
                                    hasScrollX = 0;

                                } else if (swipeLength > currentItem
                                        .getmRightViewWidth()
                                        + currentItem.getmLeftViewWidth() / 2) {
                                    currentItem.showLeft();
                                    mOpendState = STATE_SWIPE_LEFT;
                                    hasScrollX = -currentItem.getmLeftViewWidth();
                                }

                                break;
                        }

                    } else if (lastDeltaX < 0) {
                        // 向左

                        switch (mOpendState) {
                            case STATE_SWIPE_NORMAL:
                                if (Math.abs(lastDeltaX) > currentItem
                                        .getmRightViewWidth() / 2) {
                                    mOpendState = STATE_SWIPE_RIGHT;
                                    currentItem.showRight();
                                    hasScrollX = currentItem.getmRightViewWidth();
                                } else {
                                    mOpendState = STATE_SWIPE_NORMAL;
                                    currentItem.restore();
                                    hasScrollX = 0;
                                }
                                break;
                            case STATE_SWIPE_LEFT:
                                // 显示左边情况下向左滑
                                int swipeLength = currentItem.getmLeftViewWidth()
                                        + currentItem.getScrollX();
                                if (Math.abs(swipeLength) > currentItem
                                        .getmLeftViewWidth() / 2
                                        && Math.abs(swipeLength) < currentItem
                                        .getmLeftViewWidth()) {
                                    currentItem.restore();
                                    mOpendState = STATE_SWIPE_NORMAL;
                                    hasScrollX = 0;
                                } else if (Math.abs(swipeLength) <= currentItem
                                        .getmLeftViewWidth() / 2) {
                                    currentItem.showLeft();
                                    hasScrollX = -currentItem.getmLeftViewWidth();
                                    mOpendState = STATE_SWIPE_LEFT;
                                } else if (Math.abs(swipeLength) > currentItem
                                        .getmLeftViewWidth()
                                        && Math.abs(swipeLength) < (currentItem
                                        .getmRightViewWidth() / 2 + currentItem
                                        .getmLeftViewWidth())) {
                                    currentItem.restore();
                                    mOpendState = STATE_SWIPE_NORMAL;
                                    hasScrollX = 0;
                                } else if (Math.abs(swipeLength) > (currentItem
                                        .getmRightViewWidth() / 2 + currentItem
                                        .getmLeftViewWidth())) {
                                    currentItem.showRight();
                                    mOpendState = STATE_SWIPE_RIGHT;
                                    hasScrollX = currentItem.getmRightViewWidth();
                                }
                                break;

                            case STATE_SWIPE_RIGHT:
                                currentItem.showRight();
                                mOpendState = STATE_SWIPE_RIGHT;
                                hasScrollX = currentItem.getmRightViewWidth();
                                break;

                        }
                    } else {
                        currentItem.restore();
                        hasScrollX = 0;
                        mOpendState = STATE_SWIPE_NORMAL;
                    }
                }
                currentState = STATE_TOUCH_NONE;
                isTouchRestore = false;
                mListView.setClickable(true);
                break;
        }
        return !isClick;
    }

    public void checkInMove(MotionEvent event) {
        int xDiff = (int) (event.getRawX() - lastMotionX);
        int yDiff = (int) (event.getRawY() - lastMotionY);

        if (xDiff == 0 && yDiff == 0) {
            return;
        }

//		Log.i(TAG, "xdiff:"+xDiff+" ydiff:"+yDiff);

        if (Math.abs(xDiff) > touchSlop || Math.abs(yDiff) > touchSlop) {
            if (Math.abs(xDiff) > Math.abs(yDiff)) {
                currentState = STATE_TOUCH_X;
//				Log.i(TAG, "x scroll!!");
            } else {
//				Log.i(TAG, "y scroll!！");
                currentState = STATE_TOUCH_Y;
            }
        }

    }

    public void getTouchView(MotionEvent event) {
        int childCount = mListView.getChildCount();
        int[] listViewCoords = new int[2];
        mListView.getLocationOnScreen(listViewCoords);
        int x = (int) event.getRawX() - listViewCoords[0];
        int y = (int) event.getRawY() - listViewCoords[1];
        View child;
        for (int i = 0; i < childCount; i++) {
            child = mListView.getChildAt(i);
            child.getHitRect(rect);
            int childPosition = mListView.getPositionForView(child);
            boolean allowSwipe = false;
            try {
                allowSwipe = mListView.getAdapter()
                        .isEnabled(childPosition)
                        && mListView.getAdapter().getItemViewType(childPosition) >= 0;
            } catch (IndexOutOfBoundsException e) {
                allowSwipe = false;
            }
            if (allowSwipe && rect.contains(x, y)) {
                currentPosition = childPosition;

                if (lastPosition == -1) {
                    lastPosition = childPosition;
                } else if (lastPosition != currentPosition) {
                    currentItem.restore();
                    hasScrollX = 0;
                    mOpendState = STATE_SWIPE_NORMAL;
                    lastPosition = currentPosition;

                } else {
                    if (mOpendState == STATE_SWIPE_LEFT
                            || mOpendState == STATE_SWIPE_RIGHT) {
                        currentItem.restore();
                        hasScrollX = 0;
                        mOpendState = STATE_SWIPE_NORMAL;
                        isClick = false;
                        isTouchRestore = true;
                    }

                }
                currentItem = (SwipeItem) child.findViewById(R.id.swipe_item);
                if (currentItem != null) {
                    currentItem.setOnScrollCompleteListener(new SwipeItem.onScrollCompleteListener() {

                        @Override
                        public void onScrollComplete(int type) {
//							mListView.setPressed(false);
//							mListView.setFocusable(false);
//							currentItem.setPressed(false);
//							currentItem.setPressed(false);
                        }
                    });
                }
                return;
            } else {
                currentPosition = -1;
            }
        }
    }

    public void resetTouchState() {
        Log.i(TAG, "resetTouchState");
        currentState = STATE_TOUCH_NONE;
        mOpendState = STATE_SWIPE_NORMAL;
        hasScrollX = 0;
    }

    public void stateRestore() {
        Log.i(TAG, "stateRestore");
        currentState = STATE_TOUCH_NONE;
        mOpendState = STATE_SWIPE_NORMAL;
        hasScrollX = 0;
    }

}
