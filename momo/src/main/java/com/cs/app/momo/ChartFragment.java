package com.cs.app.momo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.echo.holographlibrary.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;


public class ChartFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    static final int DATE_DIALOG_ID = 1;

    private int position;
    private View rootView;
    private Button dateButton;

    private int year, month, day;

    public static final String DATEPICKER_TAG = "datepicker";

    public static ChartFragment newInstance(int position) {
        ChartFragment f = new ChartFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        setCurrentDateOnView();

        final DatePickerDialog datePickerDialog = customDatePicker();

        dateButton = (Button) rootView.findViewById(R.id.dateButton);
        dateButton.setText(year + "-" + (month+1));
        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = customDatePicker();
                datePickerDialog.setTitle("Set Month");
                if (Build.VERSION.SDK_INT >= 11) {
                    datePickerDialog.getDatePicker().setCalendarViewShown(false);
                    //datePickerDialog.getDatePicker().setSpinnersShown(false);
                }
                datePickerDialog.show();
            }
        });

        ArrayList<Bar> points = new ArrayList<Bar>();
        Bar d = new Bar();
        d.setColor(Color.parseColor("#99CC00"));
        d.setName("Test1");
        d.setValue(10);
        Bar d2 = new Bar();
        d2.setColor(Color.parseColor("#FFBB33"));
        d2.setName("Test2");
        d2.setValue(20);

        points.add(d);
        points.add(d2);

        BarGraph g = (BarGraph) rootView.findViewById(R.id.graph);
        g.setBars(points);

        return rootView;
    }

    DatePickerDialog.OnDateSetListener mDateSetListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int syear, int smonthOfYear,
                              int sdayOfMonth) {
            year = syear;
            month = smonthOfYear;
            day = sdayOfMonth;
            dateButton.setText(year + "-" + (month+1));
        }
    };

    public void setCurrentDateOnView() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    private DatePickerDialog customDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListner,
                year, month, day);
        try {
            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField
                            .get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType()
                            .getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName())
                                || "mDaySpinner".equals(datePickerField
                                .getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }
}
