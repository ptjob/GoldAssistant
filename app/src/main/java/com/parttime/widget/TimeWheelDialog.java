package com.parttime.widget;

import android.content.Context;
import android.util.Log;

import com.parttime.widget.wheel.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 公共时间选择器
 * 支持年、月、日、时、分自由组合选择。
 * 可对年份上下限和分钟间隔进行设定。
 *
 * Created by wyw on 2015/6/3.
 */
public class TimeWheelDialog extends WheelDialog {


    private enum FlagType {
        YEAR, MONTH, DATE, HOUR, MINUTE;
    }

    /**
     * 可选年
     */
    public static final int FLAG_YEAR = 1 << 5;

    /**
     * 可选月
     */
    public static final int FLAG_MONTH = 1 << 4;

    /**
     * 可选日
     */
    public static final int FLAG_DATE = 1 << 3;

    /**
     * 可选小时
     */
    public static final int FLAG_HOUR = 1 << 2;

    /**
     * 可选分钟
     */
    public static final int FLAG_MINUTE = 1 << 1;

    private boolean showYear;
    private boolean showMonth;
    private boolean showDate;
    private boolean showHour;
    private boolean showMinute;
    private Calendar current;
    private List<String> yearList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();
    private List<String> hourList = new ArrayList<>();
    private List<String> minuteList = new ArrayList<>();


    private List<FlagType> posTypeMap = new ArrayList<>();
    private List<Integer> currentPosMap = new ArrayList<>();
    private int beginYear;
    private int endYear;

    /**
     * 分钟的时间间隔（只有当FLAG_MINUTE有值时有效，默认为1分钟）
     */
    private int timespan = 1;

    private TimeWheelDialog(Context context, String[]... txts) {
        super(context, txts);
    }

    /**
     * 构造函数（分钟间隔默认为1，不循环显示，年份范围默认是当前年前后50年）
     *
     * @param context
     * @param flags 时间选择器类型，多个FLAG用|相隔（如选择时分，FLAG_HOUR|FLAG_MINUTE）
     * @param currentCalendar 当前时间的Calendar
     */
    public TimeWheelDialog(Context context, int flags, Calendar currentCalendar) {
        this(context, flags, currentCalendar, false);
    }

    /**
     * 构造函数（分钟间隔默认为1，年份范围默认是当前年前后50年）
     *
     * @param context
     * @param flags 时间选择器类型，多个FLAG用|相隔（如选择时分，FLAG_HOUR|FLAG_MINUTE）
     * @param currentCalendar 当前时间的Calendar
     * @param isCycle 是否循环显示
     */
    public TimeWheelDialog(Context context, int flags, Calendar currentCalendar, boolean isCycle) {
        this(context, flags, currentCalendar, isCycle, 1);
    }

    /**
     * 构造函数（年份范围默认是当前年前后50年）
     *
     * @param context
     * @param flags           时间选择器类型，多个FLAG用|相隔（如选择时分，FLAG_HOUR|FLAG_MINUTE）
     * @param currentCalendar 当前时间的Calendar
     * @param isCycle         是否可以循环显示
     * @param timespan        分钟间隔
     */
    public TimeWheelDialog(Context context, int flags, Calendar currentCalendar, boolean isCycle, int timespan) {
        this(context, flags, currentCalendar, isCycle, timespan, 0, 0);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param flags           时间选择器类型，多个FLAG用|相隔（如选择时分，FLAG_HOUR|FLAG_MINUTE）
     * @param currentCalendar 当前时间的Calendar
     * @param isCycle         是否可以循环显示
     * @param timespan        分钟间隔
     * @param beginYear       起始年（设置了FLAG_YEAR 有效）
     * @param endYear         终止年（设置了FLAG_YEAR 有效）
     */
    public TimeWheelDialog(Context context, int flags, Calendar currentCalendar, boolean isCycle, int timespan, int beginYear, int endYear) {
        this(context, new String[]{});
        setCycle(isCycle);
        this.timespan = timespan;
        initShow(flags);
        current = currentCalendar;
        if (beginYear == 0) {
            beginYear = Calendar.getInstance(currentCalendar.getTimeZone()).get(Calendar.YEAR) - 50;
        }
        this.beginYear = beginYear;

        if (endYear == 0) {
            endYear = Calendar.getInstance(currentCalendar.getTimeZone()).get(Calendar.YEAR) + 49;
        }
        this.endYear = endYear;
        resetWheels();
    }

    private void initShow(int flags) {
        showYear = false;
        if ((flags & FLAG_YEAR) == FLAG_YEAR) {
            showYear = true;
        }
        showMonth = false;
        if ((flags & FLAG_MONTH) == FLAG_MONTH) {
            showMonth = true;
        }
        showDate = false;
        if ((flags & FLAG_DATE) == FLAG_DATE) {
            showDate = true;
        }
        showHour = false;
        if ((flags & FLAG_HOUR) == FLAG_HOUR) {
            showHour = true;
        }
        showMinute = false;
        if ((flags & FLAG_MINUTE) == FLAG_MINUTE) {
            showMinute = true;
        }
    }

    private void resetWheels() {
        currentPosMap.clear();
        posTypeMap.clear();
        List<String[]> dataList = new ArrayList<>();
        if (showYear) {
            int currentYear = current.get(Calendar.YEAR);
            yearList = new ArrayList<>();
            for (int i = beginYear; i <= endYear; ++i) {
                if (i == currentYear) {
                    currentPosMap.add(i - beginYear);
                }
                yearList.add(String.valueOf(i));
            }
            posTypeMap.add(FlagType.YEAR);
            String[] yearArray = new String[yearList.size()];
            yearList.toArray(yearArray);
            dataList.add(yearArray);
        }
        if (showMonth) {
            int currentMonth = current.get(Calendar.MONTH) + 1;
            monthList = new ArrayList<>();
            for (int i = 1; i <= 12; ++i) {
                if (i == currentMonth) {
                    currentPosMap.add(i - 1);
                }
                monthList.add(String.valueOf(i));
            }
            posTypeMap.add(FlagType.MONTH);
            String[] monthArray = new String[monthList.size()];
            monthList.toArray(monthArray);
            dataList.add(monthArray);
        }
        if (showDate) {
            int currentDate = current.get(Calendar.DATE);
            int dayOfMonth = getDateInMonth(current);
            dateList = new ArrayList<>();
            for (int i = 1; i <= dayOfMonth; ++i) {
                if (i == currentDate) {
                    currentPosMap.add(i - 1);
                }
                dateList.add(String.valueOf(i));
            }
            posTypeMap.add(FlagType.DATE);
            String[] dateArray = new String[dateList.size()];
            dateList.toArray(dateArray);
            dataList.add(dateArray);
        }
        if (showHour) {
            int currentHour = current.get(Calendar.HOUR_OF_DAY);
            hourList = new ArrayList<>();
            for (int i = 0; i <= 23; ++i) {
                if (i == currentHour) {
                    currentPosMap.add(i);
                }
                hourList.add(String.valueOf(i));
            }
            posTypeMap.add(FlagType.HOUR);
            String[] hourArray = new String[hourList.size()];
            hourList.toArray(hourArray);
            dataList.add(hourArray);
        }
        if (showMinute) {
            int currentMinute = current.get(Calendar.MINUTE);
            currentMinute = (int) (this.timespan * Math.round((double) currentMinute / this.timespan));
            current.set(Calendar.MINUTE, currentMinute);
            minuteList = new ArrayList<>();
            for (int i = 0; i < 60 / this.timespan; ++i) {
                int tick = this.timespan * i;
                if (currentMinute == tick) {
                    currentPosMap.add(i);
                }
                minuteList.add(String.valueOf(tick));
            }
            posTypeMap.add(FlagType.MINUTE);
            String[] minuteArray = new String[minuteList.size()];
            minuteList.toArray(minuteArray);
            dataList.add(minuteArray);
        }

        String[][] marrayData = new String[dataList.size()][];
        FlagType[] posTypeArray = new FlagType[posTypeMap.size()];
        posTypeMap.toArray(posTypeArray);
        int[] currentPosArray = new int[currentPosMap.size()];
        for (int i = 0; i < currentPosMap.size(); i++) {
            currentPosArray[i] = currentPosMap.get(i);
        }

        attachWheels(posTypeArray, dataList.toArray(marrayData));
        setCurrentPosition(currentPosArray);

        for (WheelView wheel : wheels) {
            wheel.addChangingListener(this);
        }
    }

    private void resetDateWheel() {
        if (showDate) {
            List<FlagType> posTypeMap = new ArrayList<>();
            List<Integer> currentPosMap = new ArrayList<>();

            List<String[]> dataList = new ArrayList<>();

            int currentDate = current.get(Calendar.DATE);
            int dayOfMonth = getDateInMonth(current);
            dateList = new ArrayList<>();
            for (int i = 1; i <= dayOfMonth; ++i) {
                if (i == currentDate) {
                    currentPosMap.add(i - 1);
                }
                dateList.add(String.valueOf(i));
            }
            posTypeMap.add(FlagType.DATE);
            String[] dateArray = new String[dateList.size()];
            dateList.toArray(dateArray);
            dataList.add(dateArray);

            String[][] marrayData = new String[dataList.size()][];
            FlagType[] posTypeArray = new FlagType[posTypeMap.size()];
            posTypeMap.toArray(posTypeArray);
            int currentPos = 0;
            for (int i = 0; i < currentPosMap.size(); i++) {
                currentPos = currentPosMap.get(i);
            }

            resetWheels(posTypeArray, dataList.toArray(marrayData));
            for(int i = 0; i < wheels.length; ++i){
                if (wheels[i].getTag() == FlagType.DATE) {
                    wheels[i].setCurrentItem(currentPos, false);
                }
            }
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        super.onChanged(wheel, oldValue, newValue);
        FlagType flagType = (FlagType) wheel.getTag();
        Log.i("TimeWheelDialog", "flagType: " + flagType + "; oldValue: " + oldValue + "; newValue: " + newValue);
        try {
            switch (flagType) {
                case YEAR:
                    int rollValue = Integer.valueOf(yearList.get(newValue)) - Integer.valueOf(yearList.get(oldValue));
                    current.roll(Calendar.YEAR, rollValue);
                    resetDateWheel();
                    break;
                case MONTH:
                    rollValue = Integer.valueOf(monthList.get(newValue)) - Integer.valueOf(monthList.get(oldValue));
                    current.roll(Calendar.MONTH, rollValue);
                    resetDateWheel();
                    break;
                case DATE:
                    oldValue = oldValue >= dateList.size() ? dateList.size() - 1 : oldValue;
                    rollValue = Integer.valueOf(dateList.get(newValue)) - Integer.valueOf(dateList.get(oldValue));
                    current.roll(Calendar.DATE, rollValue);
                    break;
                case HOUR:
                    rollValue = Integer.valueOf(hourList.get(newValue)) - Integer.valueOf(hourList.get(oldValue));
                    current.roll(Calendar.HOUR_OF_DAY, rollValue);
                    break;
                case MINUTE:
                    rollValue = Integer.valueOf(minuteList.get(newValue)) - Integer.valueOf(minuteList.get(oldValue));
                    current.roll(Calendar.MINUTE, rollValue);
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private int getDateInMonth(Calendar c) {
        Calendar temp = Calendar.getInstance(c.getTimeZone());
        temp.setTimeInMillis(c.getTimeInMillis());
        temp.set(Calendar.DATE, 1);
        temp.roll(Calendar.DATE, -1);
        return temp.get(Calendar.DATE);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public Calendar getTime() {
        return current;
    }
}
