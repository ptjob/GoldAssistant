package com.parttime.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cjz on 2015/7/24.
 */
public class CountDownTimer {
    private Timer mTimer;
    private int mSecondsLeft;
    private TimeTick mCallback;
    private TimerTask mTask;

    public CountDownTimer(int howManySeconds, TimeTick callback) {
        mTimer = new Timer();
        mTask = new CountDownTask();
        mCallback = callback;
        mSecondsLeft = howManySeconds;
    }

    public void start() {
        if (mTimer == null) {
            throw new IllegalStateException("Each instance of CountDownTimer can only be used once");
        }
        mTimer.schedule(mTask, 1000, 1000);
    }

    public void cancel() {
        if (mTask != null) {
            mTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
        mCallback.cancelled();
    }

    public void pause() {
        if (mTask != null) {
            mTask.cancel();
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mCallback.paused();
    }

    public void goOn() {
        mTask = new CountDownTask();
        mTimer = new Timer();
        mTimer.schedule(mTask, 1000, 1000);
        mCallback.goOn();
    }

    public boolean isCounting() {
        return (mSecondsLeft > 0);
    }

    public int getSecondsLeft() {
        return mSecondsLeft;
    }

    public interface TimeTick {
        void ticking(int secondsLeft);

        void stoped();

        void paused();

        void cancelled();

        void goOn();
    }

    private class CountDownTask extends TimerTask {

        @Override
        public void run() {
            if (--mSecondsLeft <= 0) {
                mCallback.stoped();
                this.cancel();
                mTimer = null;
            } else {
                mCallback.ticking(mSecondsLeft);
            }
        }

    }
}
