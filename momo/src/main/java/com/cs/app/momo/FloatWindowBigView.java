package com.cs.app.momo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.util.Calendar;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * �O��j�a�B�����e��
	 */
	public static int viewWidth = 300;

    private Button dollar;
    private Button cancel;
    private Button save;
    private Spinner category;
    private int select = 0;
    private int year, month, day;
    private String text = "";
	/**
	 * �O��j�a�B��������
	 */
	public static int viewHeight = 133;
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) { 
		// TODO Auto-generated method stub 
		if (event.getAction() == KeyEvent.ACTION_DOWN 
		&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) { 
			//Log.e("tag", "222222");
			// �I����^���ɭԡA�����j�a�B���A�Ыؤp�a�B��
			MyWindowManager.removeBigWindow(getContext());
			MyWindowManager.createSmallWindow(getContext());
			//do something what you want 
			return true;//��^true�A��ƥ��O���A���|�~��ե�onBackPressed 
		} 
			return super.dispatchKeyEvent(event); 
		} 

	public FloatWindowBigView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.big_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;


        setCurrentDateOnView();
//        TextView title = (TextView)findViewById(R.id.quick_title);
//        title.setText(getResources().getString(R.string.quick_add) + " - " + year + "/" + (month+1) + "/" + day);
        /*show = (TextView)findViewById(R.id.show);
        show.setText("0");
        show.setTextSize(20);*/

        dollar = (Button)findViewById(R.id.dollar);
        SpannableString content = new SpannableString("0");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        String showText = "$" + content.toString();
        dollar.setText(showText);



        //String text = "";
        final Button one = (Button)findViewById(R.id.one);
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
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button two = (Button)findViewById(R.id.two);
        two.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            two.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            two.setBackgroundResource(R.color.white);
                            text = intDetect(2, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button three = (Button)findViewById(R.id.three);
        three.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            three.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            three.setBackgroundResource(R.color.white);
                            text = intDetect(3, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button four = (Button)findViewById(R.id.four);
        four.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            four.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            four.setBackgroundResource(R.color.white);
                            text = intDetect(4, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button five = (Button)findViewById(R.id.five);
        five.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            five.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            five.setBackgroundResource(R.color.white);
                            text = intDetect(5, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button six = (Button)findViewById(R.id.six);
        six.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            six.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            six.setBackgroundResource(R.color.white);
                            text = intDetect(6, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button seven = (Button)findViewById(R.id.seven);
        seven.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            seven.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            seven.setBackgroundResource(R.color.white);
                            text = intDetect(7, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button eight = (Button)findViewById(R.id.eight);
        eight.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            eight.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            eight.setBackgroundResource(R.color.white);
                            text = intDetect(8, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button nine = (Button)findViewById(R.id.nine);
        nine.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            nine.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            nine.setBackgroundResource(R.color.white);
                            text = intDetect(9, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button zero = (Button)findViewById(R.id.zero);
        zero.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            zero.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            zero.setBackgroundResource(R.color.white);
                            text = intDetect(0, text);
                            dollar.setText("$"+text);
                        }
                        return false;
                    }
                }
        );
        final Button bspace = (Button)findViewById(R.id.bspace);
        bspace.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            bspace.setBackgroundResource(R.color.gray);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            bspace.setBackgroundResource(R.color.white);
                            text = "";
                            dollar.setText("$"+"0");
                        }
                        return false;
                    }
                }
        );




        category = (Spinner)findViewById(R.id.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] {getResources().getString(R.string.title_activity_cost),
                        getResources().getString(R.string.title_activity_feed)});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        category.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                select = i==0? 1: 0 ;
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            cancel.setBackgroundResource(R.color.white);

                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            cancel.setBackgroundResource(R.color.gray);
                            MyWindowManager.removeBigWindow(context);
                        }
                        return false;
                    }
                }
        );

        save = (Button) view.findViewById(R.id.save);
        save.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            save.setBackgroundResource(R.color.white);

                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            save.setBackgroundResource(R.color.gray);
                            DatabaseHandler db = new DatabaseHandler(getContext(), String.valueOf(month), String.valueOf(year));
                            db.create(String.valueOf(month), String.valueOf(year));
                            String getDollar = dollar.getText().toString().substring(1, dollar.getText().toString().length());
                            db.insert(new Statistics(String.valueOf(month), String.valueOf(day), select, 3, Integer.parseInt(getDollar), "", String.valueOf(year)));

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

                            SharedPreferences settings = getContext().getSharedPreferences("MOMO_SETTINGS",  0);;
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
                            if(MyWindowManager.smallWindow != null){
                                MyWindowManager.resetParamsSwitch = false;
                                MyWindowManager.removeSmallWindow(getContext());
                                MyWindowManager.createSmallWindow(getContext());
                                MyWindowManager.resetParamsSwitch = true;
                            }


                            MyWindowManager.removeBigWindow(context);
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
