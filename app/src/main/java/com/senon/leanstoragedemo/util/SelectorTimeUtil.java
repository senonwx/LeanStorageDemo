package com.senon.leanstoragedemo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.senon.leanstoragedemo.R;
import com.senon.leanstoragedemo.util.timeselectorutils.ZJDateTimeSelector;
import com.senon.leanstoragedemo.util.timeselectorutils.ZJJudgeDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 选择时间
 */
public class SelectorTimeUtil {
    /**
     * 选择日期的方法
     * @param txtView
     */
    public static void choseDateTime(final TextView txtView, String time_type,Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LayoutInflater inflater = LayoutInflater.from(context);

        final View timepickerview = inflater.inflate(R.layout.datetimeselector, null);
        final ZJDateTimeSelector dateTimeSelector = new ZJDateTimeSelector(timepickerview, false);
        String time = txtView.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (ZJJudgeDate.isDate(time, "yyyy-MM-dd")) {
            try {
                calendar.setTime(dateFormat.parse(time));
            } catch (ParseException e) {
                Log.e("时间", "choseDateTime " + e.getMessage());
            }
        }
        dateTimeSelector.initDateTimePicker(calendar.getTime());
//        new AlertDialog.Builder(activity,R.style.AlertDialog)
        new AlertDialog.Builder(context)
                .setTitle(time_type == null ? "请选择日期":time_type)
                .setView(timepickerview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txtView.setText(dateTimeSelector.getTime());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
