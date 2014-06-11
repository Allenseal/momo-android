package com.cs.app.momo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.util.Calendar;


public class Feed extends FragmentActivity implements DatePickerDialog.OnDateSetListener  {
    public static final String DATEPICKER_TAG = "datepicker";

    private Button dollar;
    private Button date;
    private Button cancel;
    private Button save;
    //private TextView show;
    //private Spinner info;
    private EditText comment;
    //private EditText dollarDialog;
    private int year;
    private int month;
    private int day;
    private String dollarInput = null;
    String text ="";
    //static final int DATE_DIALOG_ID = 999;
    //private Calendar calendar;
    //public static Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setCurrentDateOnView();

        //dollarDialog = new EditText(this);

        /*show = (TextView)findViewById(R.id.show);
        show.setText("0");
        show.setTextSize(20);*/

        comment = (EditText)findViewById(R.id.comment);

        dollar = (Button)findViewById(R.id.dollar);
        SpannableString content = new SpannableString("0");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        String showText = "$" + content.toString();
        dollar.setText(showText);
        dollar.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            dollar.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            dollar.setBackgroundResource(R.color.white);
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Feed.this);
                            /*final EditText input = new EditText(Feed.this);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            alertDialog.setView(input);*/
                            LayoutInflater inflater = LayoutInflater.from(Feed.this);

                            View calculator_view = inflater.inflate(R.layout.calculator,null);
                            alertDialog.setView(calculator_view);
                            final TextView display = (TextView)calculator_view.findViewById(R.id.display);
                            //String text = "";
                            final Button one = (Button)calculator_view.findViewById(R.id.one);
                            text = "";
                            one.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                one.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                one.setBackgroundResource(R.color.white);
                                                text = intDetect(1, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button two = (Button)calculator_view.findViewById(R.id.two);
                            two.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                two.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                two.setBackgroundResource(R.color.white);
                                                text = intDetect(2, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button three = (Button)calculator_view.findViewById(R.id.three);
                            three.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                three.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                three.setBackgroundResource(R.color.white);
                                                text = intDetect(3, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button four = (Button)calculator_view.findViewById(R.id.four);
                            four.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                four.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                four.setBackgroundResource(R.color.white);
                                                text = intDetect(4, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button five = (Button)calculator_view.findViewById(R.id.five);
                            five.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                five.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                five.setBackgroundResource(R.color.white);
                                                text = intDetect(5, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button six = (Button)calculator_view.findViewById(R.id.six);
                            six.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                six.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                six.setBackgroundResource(R.color.white);
                                                text = intDetect(6, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button seven = (Button)calculator_view.findViewById(R.id.seven);
                            seven.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                seven.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                seven.setBackgroundResource(R.color.white);
                                                text = intDetect(7, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button eight = (Button)calculator_view.findViewById(R.id.eight);
                            eight.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                eight.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                eight.setBackgroundResource(R.color.white);
                                                text = intDetect(8, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button nine = (Button)calculator_view.findViewById(R.id.nine);
                            nine.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                nine.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                nine.setBackgroundResource(R.color.white);
                                                text = intDetect(9, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button zero = (Button)calculator_view.findViewById(R.id.zero);
                            zero.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                zero.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                zero.setBackgroundResource(R.color.white);
                                                text = intDetect(0, text);
                                                display.setText(text);
                                            }
                                            return false;
                                        }
                                    }
                            );
                            final Button bspace = (Button)calculator_view.findViewById(R.id.bspace);
                            bspace.setOnTouchListener(
                                    new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                bspace.setBackgroundResource(R.color.gray);
                                            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                bspace.setBackgroundResource(R.color.white);
                                                text = "";
                                                display.setText("0");
                                            }
                                            return false;
                                        }
                                    }
                            );

                            alertDialog.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int which) {
                                            // Write your code here to execute after dialog
                                            //dollarInput = input.getText().toString();
                                            SpannableString content = new SpannableString(display.getText().toString());
                                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                            String showText = "$" + content.toString();
                                            dollar.setText(showText);
                                        }
                                    });
                            alertDialog.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to execute after dialog
                                            dialog.cancel();
                                        }
                                    });
                            alertDialog.show();
                        }
                        return false;
                    }
                }
        );

        date = (Button)findViewById(R.id.date);
        date.setText(year + "/" + (month+1) + "/" + day);
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, year, month, day, false);
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }
        }

        //info = (Spinner)findViewById(R.id.date);

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            cancel.setBackgroundResource(R.color.white);

                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            cancel.setBackgroundResource(R.color.gray);
                            finish();
                        }
                        return false;
                    }
                }
        );

        save = (Button)findViewById(R.id.save);
        save.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            save.setBackgroundResource(R.color.white);

                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            save.setBackgroundResource(R.color.gray);
                            DatabaseHandler db = new DatabaseHandler(Feed.this, String.valueOf(month), String.valueOf(year));
                            //if (!db.isExisted(String.valueOf(year)+String.valueOf(month))) {
                            //db.insert(new Statistics(String.valueOf(month), String.valueOf(month) + String.valueOf(), int type, int kind, int money,String comment, String year));
                            db.create(String.valueOf(month), String.valueOf(year));
                            //}
                            String getDollar = dollar.getText().toString().substring(1, dollar.getText().toString().length());
                            db.insert(new Statistics(String.valueOf(month), String.valueOf(day), 0, 10, Integer.parseInt(getDollar), comment.getText().toString(), String.valueOf(year)));
                            //Log.v("sum", String.valueOf(db.getSum()));

                            //refresh status
                            Calendar cal = Calendar.getInstance();
                            int nowMonth = cal.get(Calendar.MONTH);
                            int nowYear = cal.get(Calendar.YEAR);
                            db.create(String.valueOf(nowMonth), String.valueOf(nowYear));
                            double total = db.getSum(String.valueOf(year), String.valueOf(month));
                            double monthcost = db.getMonthCost(String.valueOf(year), String.valueOf(month));
                            Log.d("total", Double.toString(total));
                            Log.d("monthcost", Double.toString(monthcost));

                            final double percent;
                            if(monthcost != 0.0 && total != 0.0){
                                percent =  total / (total + monthcost);
                            }else if(monthcost == 0.0 && total > 0.0){
                                percent = 1.0 ;
                            }else{
                                percent = 0.0;
                            }
                            Log.d("percent", Double.toString(percent));

                            SharedPreferences settings = getSharedPreferences("MOMO_SETTINGS", 0);;
                            SharedPreferences.Editor PE = settings.edit();

                            PE.putBoolean("FIRST_COMMIT", true);
                            if (percent * 100 > 40) {
                                // normal
                                PE.putString("MONSTER_STATUS", "normal");
                            } else if (percent * 100 > 10) {
                                //hungry
                                PE.putString("MONSTER_STATUS", "hungry");
                            } else {
                                //death
                                PE.putString("MONSTER_STATUS", "death");
                            }
                            PE.commit();

                            if(MyWindowManager.smallWindow != null) {
                                MyWindowManager.resetParamsSwitch = false;
                                MyWindowManager.removeSmallWindow(Feed.this);
                                MyWindowManager.createSmallWindow(Feed.this);
                                MyWindowManager.resetParamsSwitch = true;
                            }

                            finish();
                        }
                        return false;
                    }
                }
        );



    }

    public void setCurrentDateOnView() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int syear, int smonth, int sday) {
        //Toast.makeText(getActivity(), "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
        year = syear;
        month = smonth;
        day = sday;
        date.setText(year + "/" + (month+1) + "/" + day);
    }

    private String intDetect(int num, String text){
        boolean flag = false;
        text = text + Integer.toString(num);
        if(text.length()==10){
            if(text.charAt(0) == 2){
                String tmp = String.valueOf(text.subSequence(1, text.length()));
                if(Integer.parseInt(tmp)>147483647){
                    flag = true;
                }
            }else if(num == 1) {
                flag = false;
            }else {
                flag = true;
            }
        }else if(text.length()>10){
            flag = true;
        }

        if(flag){
            return "2147483647";
        }else{
            return String.valueOf(Integer.parseInt(text));
        }
    }
}
