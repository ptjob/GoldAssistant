package com.parttime.utils;

import android.content.Context;
import android.widget.TextView;

import com.parttime.widget.TimeWheelDialog;
import com.parttime.widget.WheelDialog;
import com.qingmu.jianzhidaren.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 动作触发帮助类
 * Created by wyw on 2015/7/25.
 */
public class ActionUtils {

    public static void selectDate(Context context, final TextView textView, Calendar current, String title) {
        final TimeWheelDialog timeWheelDialog = new TimeWheelDialog(context, TimeWheelDialog.FLAG_YEAR |
                TimeWheelDialog.FLAG_MONTH | TimeWheelDialog.FLAG_DATE, current, false);
        timeWheelDialog.setOnSubmitListener(new WheelDialog.OnSubmitListener() {
            @Override
            public void onSubmit(int... pos) {
                Calendar selectedCalendar = timeWheelDialog.getTime();
                SimpleDateFormat myFmt = new SimpleDateFormat(TimeUtils.DATE_FORMAT_YMD);
                String formatedDate = myFmt.format(selectedCalendar.getTime());
                textView.setText(formatedDate);
            }
        });
        timeWheelDialog.setTitle(title);

        timeWheelDialog.show();
    }

    public static void selectMeasurements(final Context context, final TextView textView) {

        final int minValue = context.getResources().getInteger(R.integer.measurements_min_value);
        int maxValue = context.getResources().getInteger(R.integer.measurements_max_value);

        String[][] itemsArrays = new String[3][maxValue - minValue + 1];

        for (int k = 0; k < itemsArrays.length; ++k) {
            for (int i = minValue; i <= maxValue; ++i) {
                itemsArrays[k][i - minValue] = String.valueOf(i);
            }
        }

        WheelDialog wheelDialog = new WheelDialog(context, itemsArrays);
        wheelDialog.setTitle(R.string.publish_job_measurements_dialog_title);
        wheelDialog.setOnSubmitListener(new WheelDialog.OnSubmitListener() {
            @Override
            public void onSubmit(int... pos) {
                textView.setText(context.getString(R.string.publish_job_measurements_format,
                        String.valueOf(minValue + pos[0]),
                        String.valueOf(minValue + pos[1]),
                        String.valueOf(minValue + pos[2])));
            }
        });
        wheelDialog.show();
    }
}
