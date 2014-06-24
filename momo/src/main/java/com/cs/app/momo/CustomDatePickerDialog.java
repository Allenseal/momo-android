package com.cs.app.momo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

class CustomDatePickerDialog extends DatePickerDialog implements DatePicker.OnDateChangedListener {

    private DatePickerDialog mDatePicker;

    @SuppressLint("NewApi")
    public CustomDatePickerDialog(Context context, OnDateSetListener callBack,
                                  int year, int monthOfYear, int dayOfMonth) {
        super(context,callBack, year, monthOfYear, dayOfMonth);
        mDatePicker = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);

        if (Build.VERSION.SDK_INT >= 11) {
            mDatePicker.getDatePicker().init(year, monthOfYear, dayOfMonth, this);
        }
        updateTitle(year, monthOfYear);

    }
    public void onDateChanged(DatePicker view, int year,
                              int month, int day) {
        updateTitle(year, month);
    }
    private void updateTitle(int year, int month) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
//       mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mDatePicker.setTitle(getFormat().format(mCalendar.getTime()));

    }

    public DatePickerDialog getPicker(){

        return this.mDatePicker;
    }
    /*
     * the format for dialog tile,and you can override this method
     */
    public SimpleDateFormat getFormat(){
        return new SimpleDateFormat("MMM, yyyy");
    };
}