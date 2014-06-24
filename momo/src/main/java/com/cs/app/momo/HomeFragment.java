package com.cs.app.momo;

import android.annotation.TargetApi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Time;
import android.text.style.BackgroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import android.widget.*;
import de.passsy.holocircularprogressbar.HoloCircularProgressBar;

import java.text.NumberFormat;



public class HomeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private View rootView;

    private ImageButton feedBtn;
    private ImageButton costBtn;
    private TextView feedText;
    private TextView costText;
    private TextView powerText;
    private TextView monthText;
    private TextView totalText;
    private TextView petName;

    private ImageView monster, stage;

    private HoloCircularProgressBar mHoloCircularProgressBar;
    private ObjectAnimator mProgressBarAnimator;
    private com.nineoldandroids.animation.ObjectAnimator mProgressBarAnimatorOld;

    private int layoutWidth;
    private int layoutHeight;

    private int monsterNumber = 0;
    private String[] monsterName = {"Penguin", "Bear", "Seal"};

    private double percentFrom = 0;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public void  onResume() {
//        if(MyWindowManager.smallWindow != null) {
//            // 關閉懸浮窗，移除所有懸浮窗，並停止Service
//            MyWindowManager.removeLauncher(getActivity());
//            MyWindowManager.removeBigWindow(getActivity());
//            MyWindowManager.removeSmallWindow(getActivity());
//            Intent intent = new Intent(getActivity(), FloatWindowService.class);
//            getActivity().stopService(intent);
//        }

        setView();

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label1);
        //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER))+". Home");

        this.rootView = rootView;
        setView();



        return rootView;
    }

    protected void setView(){
        Log.d("refresh", "refresh status");
        //get moster name
        SharedPreferences settings = getActivity().getSharedPreferences("MOMO_SETTINGS",  0);
        monsterNumber = settings.getInt("MONSTER_NUMBER", 0);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutWidth = metrics.widthPixels;
        layoutHeight = metrics.heightPixels;

        //set button view
        feedBtn = (ImageButton) rootView.findViewById(R.id.feed_btn);
        feedBtn.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            feedBtn.setImageResource(R.drawable.home_feed_button_push);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            feedBtn.setImageResource(R.drawable.home_feed_button_release);
//                            testDialog("Feed");
                            Intent feed = new Intent (getActivity(), Feed.class);
                            startActivity(feed);
                            //getActivity().finish();
                        }
                        return false;
                    }
                }
        );


        costBtn = (ImageButton) rootView.findViewById(R.id.cost_btn);
        costBtn.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            costBtn.setImageResource(R.drawable.home_cost_button_push);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            costBtn.setImageResource(R.drawable.home_cost_button_release);
//                            testDialog("Cost");
                            Intent cost = new Intent(getActivity(), Cost.class);
                            startActivity(cost);
                        }
                        return false;
                    }
                }
        );


        //set month text
        monthText = (TextView) rootView.findViewById(R.id.monthlycost);
        totalText = (TextView) rootView.findViewById(R.id.home_total);
        totalText.setText(" " + getResources().getString(R.string.total));
        setMonth();

        //money text view
        //TODO get data from database
        Time time = new Time("GMT+8");
        time.setToNow();
        int month = time.month;
        int year = time.year;
        Statistics now = new Statistics(Integer.toString(month), Integer.toString(year), 0);
        DatabaseHandler db = new DatabaseHandler(getActivity(), String.valueOf(month), String.valueOf(year));
        db.create(String.valueOf(month), String.valueOf(year));
        int total = db.getSum(String.valueOf(year), String.valueOf(month));
        int monthcost = db.getMonthCost(String.valueOf(year), String.valueOf(month));
        //Statistics total_month = db.getMonth(now);
        //int total = 0;
        //if(total_month != null){
        // total = total_month.getSum();
        //}

        feedText = (TextView) rootView.findViewById(R.id.feed_text);
        feedText.setText("$"+Integer.toString(total));
        costText = (TextView) rootView.findViewById(R.id.cost_text);
        costText.setText("$"+String.valueOf(monthcost));
        powerText = (TextView) rootView.findViewById(R.id.power_text);
        powerText.setText(DoubleFormat(percentFrom*100, 0));

        //set bar view
        //setMoneyNum("Initial", "0");
        mHoloCircularProgressBar = (HoloCircularProgressBar) rootView.findViewById(R.id.holoCircularProgressBar1);
        mHoloCircularProgressBar.setMarkerEnabled(false);
        //mHoloCircularProgressBar.setMarkerProgress(0);
        BarTask barTask = new BarTask("Initial", "0");
        barTask.execute();

        //set monster view
        monster = (ImageView) rootView.findViewById(R.id.monster_view);
        stage = (ImageView) rootView.findViewById(R.id.stage_view);
        petName = (TextView) rootView.findViewById(R.id.pet_name);
        petName.setText(monsterName[monsterNumber]);

        //TODO get monsterName from datapath

        changeMonsterStatus(percentFrom);

        monster.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            BarTask barTask = new BarTask("Initial", "0");
                            barTask.execute();
                            //Log.d("touch", "down");
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            setImgAnimation(monster);
                        }
                        return false;
                    }
                }
        );


    }

    private class BarTask extends AsyncTask<String, Void, Double> {
        private double feed, cost;
        public BarTask(String title, String moneyNum){
            feed = Double.parseDouble(feedText.getText().toString().replace("$", ""));
            cost = Double.parseDouble(costText.getText().toString().replace("$", ""));

            if(title.equals("Feed")){
                feed = feed + Double.parseDouble(moneyNum);
                feedText.setText("$"+ Integer.toString((int)feed));
            }else if(title.equals("Cost")){
                cost = cost + Double.parseDouble(moneyNum);
                feed = feed - Double.parseDouble(moneyNum);
                costText.setText("$" + Integer.toString((int)cost));
                feedText.setText("$"+ Integer.toString((int)feed));
            }


        }

        @Override
        protected Double doInBackground(String... param) {
            final double percent;
            if(cost != 0.0 && feed != 0.0){
                percent =  feed / (feed + cost);
            }else if(cost == 0.0 && feed > 0.0){
                percent = 1.0 ;
            }else{
                percent = 0.0;
            }
            //temporality
            percentFrom = percent;

            return percent;
        }

        @Override
        protected void onPostExecute(Double percent) {

            changeMonsterStatus(percentFrom);
            setImgAnimation(monster);

            if (Build.VERSION.SDK_INT >= 11) {
                //set progress
                if (mProgressBarAnimator != null) {
                    mProgressBarAnimator.cancel();
                }
                if (percentFrom < 0) {
                    animate(mHoloCircularProgressBar, null, (float) 0, 1000);
                    powerText.setText(DoubleFormat(0, 0));
                } else {
                    animate(mHoloCircularProgressBar, null, (float) percentFrom, 1000);
                    powerText.setText(DoubleFormat(percentFrom * 100, 0));
                }
            }else{
                //set progress
                if (mProgressBarAnimatorOld != null) {
                    mProgressBarAnimatorOld.cancel();
                }
                if (percentFrom < 0) {
                    animateOld(mHoloCircularProgressBar, null, (float) 0, 1000);
                    powerText.setText(DoubleFormat(0, 0));
                } else {
                    animateOld(mHoloCircularProgressBar, null, (float) percentFrom, 1000);
                    powerText.setText(DoubleFormat(percentFrom * 100, 0));
                }
            }

            if((percentFrom*100) <= 10){
                powerText.setTextColor(getResources().getColor(R.color.red));
            }else{
                powerText.setTextColor(getResources().getColor(R.color.gray));
            }
        }
    }


    private void setImgAnimation(ImageView img) {
        Animation am1 = new ScaleAnimation(1,1.1f,1,0.9f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        am1.setDuration(300);
        am1.setRepeatCount( 0 );


        Animation am2 = new ScaleAnimation(1,(1f/1.1f),1,(1f/0.9f),
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        am2.setStartOffset(300);

        am2.setDuration( 400 );
        am2.setRepeatCount(0);

        Animation am3 = new ScaleAnimation(1,1.05f,1,0.9f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        am3.setStartOffset(700);
        am3.setDuration( 300 );
        am3.setRepeatCount( 0 );

        Animation am4 = new ScaleAnimation(1,(1f/1.05f),1,(1f/0.9f),
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        am4.setStartOffset(1000);

        am4.setDuration( 400 );
        am4.setRepeatCount(0);

        //�]�w����ʵe���X
        AnimationSet am = new AnimationSet( true );
        am.addAnimation( am1 );
        am.addAnimation( am2 );
        am.addAnimation( am3 );
        am.addAnimation( am4 );

        img.setAnimation(am);// �Ϥ�t�m�ʵe
        am.startNow();// �ʵe�}�l
    }
    public void testDialog(final String title){
        // Dialog
        final EditText moneyNum = new EditText(getActivity());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setView(moneyNum);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        Button mBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (mBtn != null) {
            mBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    String mMoneyNum = moneyNum.getText().toString();

                    if (TextUtils.isEmpty(mMoneyNum)){
                        BackgroundColorSpan fgcspan = new BackgroundColorSpan(R.color.white);
                        SpannableStringBuilder mEBuilder = new SpannableStringBuilder(getString(R.string.error_field_required));
                        mEBuilder.setSpan(fgcspan, 0, getString(R.string.error_field_required).length(), 0);
                        moneyNum.setError(mEBuilder);
                        moneyNum.requestFocus();
                    }
                    else{
                        //setMoneyNum(title, mMoneyNum);
                        BarTask barTask = new BarTask(title, mMoneyNum);
                        barTask.execute();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void setMonth() {
        Time time = new Time("GMT+8");
        time.setToNow();
        int month = time.month;
        String monthWord="unknown";
        if(month==0) {
            monthWord = getResources().getString(R.string.jan);
        }else if(month==1){
            monthWord = getResources().getString(R.string.feb);
        }else if(month==2){
            monthWord = getResources().getString(R.string.mar);
        }else if(month==3){
            monthWord = getResources().getString(R.string.apr);
        }else if(month==4){
            monthWord = getResources().getString(R.string.may);
        }else if(month==5){
            monthWord = getResources().getString(R.string.jun);
        }else if(month==6){
            monthWord = getResources().getString(R.string.jul);
        }else if(month==7){
            monthWord = getResources().getString(R.string.aug);
        }else if(month==8){
            monthWord = getResources().getString(R.string.sep);
        }else if(month==9){
            monthWord = getResources().getString(R.string.oct);
        }else if(month==10){
            monthWord = getResources().getString(R.string.nov);
        }else if(month==11){
            monthWord = getResources().getString(R.string.dec);
        }
        monthText.setText(" "+ getResources().getString(R.string.monthly_cost) + "- " + monthWord);
    }

    private void changeMonsterStatus(double status) {
        int id ;
        SharedPreferences settings = getActivity().getSharedPreferences("MOMO_SETTINGS",  0);
        SharedPreferences.Editor PE = settings.edit();
        Boolean firstCommit = settings.getBoolean("FIRST_COMMIT", false);
        if(firstCommit) {
            if (status * 100 > 40) {
                // normal
                PE.putString("MONSTER_STATUS", "normal");
                id = getResources().getIdentifier(monsterName[monsterNumber].toLowerCase() + "_normal", "drawable", "com.cs.app.momo");
            } else if (status * 100 > 10) {
                //hungry
                PE.putString("MONSTER_STATUS", "hungry");
                id = getResources().getIdentifier(monsterName[monsterNumber].toLowerCase() + "_hungry", "drawable", "com.cs.app.momo");
            } else {
                //death
                PE.putString("MONSTER_STATUS", "death");
                id = getResources().getIdentifier(monsterName[monsterNumber].toLowerCase() + "_death", "drawable", "com.cs.app.momo");
            }
        }else{
            PE.putString("MONSTER_STATUS", "sleep");
            id = getResources().getIdentifier(monsterName[monsterNumber].toLowerCase() + "_sleep", "drawable", "com.cs.app.momo");
        }
        PE.commit();
        monster.setImageResource(id);
        id = getResources().getIdentifier(monsterName[monsterNumber].toLowerCase() + "_stage", "drawable", "com.cs.app.momo");
        stage.setImageResource(id);

        int mW = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int mH = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        monster.measure(mW, mH);
        int monsterHeight = monster.getMeasuredHeight();
        int monsterWidth = monster.getMeasuredWidth();

        FrameLayout.LayoutParams layoutmonster = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutmonster.gravity = Gravity.CENTER;
        double widthTmp = monsterWidth;
        double heightTmp = monsterHeight;
//        if(monsterHeight>((double)layoutHeight*7/12)) {
            heightTmp = ((double)layoutHeight*2/5);
            double scale = heightTmp /(double)monsterHeight;
            widthTmp = ((double)monsterWidth) * scale;
            layoutmonster.width = (int)widthTmp;
            layoutmonster.height = (int)heightTmp;
//        }
        monster.setLayoutParams(layoutmonster);
        stage.setLayoutParams(layoutmonster);


    }

    private String DoubleFormat(double number, int count){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(count);
        return nf.format(number);
    }

    private void animate(final HoloCircularProgressBar progressBar, final Animator.AnimatorListener listener,
                         final float progress, final int duration) {

        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);

        mProgressBarAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        //progressBar.setMarkerProgress(progress);
        mProgressBarAnimator.start();
    }

    private void animateOld(final HoloCircularProgressBar progressBar, final  com.nineoldandroids.animation.ObjectAnimator.AnimatorListener listener,
                         final float progress, final int duration) {

        mProgressBarAnimatorOld = com.nineoldandroids.animation.ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimatorOld.setDuration(duration);

        mProgressBarAnimatorOld.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(final com.nineoldandroids.animation.Animator animation) {
            }

            @Override
            public void onAnimationEnd(final com.nineoldandroids.animation.Animator animation) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(final com.nineoldandroids.animation.Animator animation) {
            }

            @Override
            public void onAnimationStart(final com.nineoldandroids.animation.Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimatorOld.addListener(listener);
        }
        mProgressBarAnimatorOld.reverse();
        mProgressBarAnimatorOld.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final com.nineoldandroids.animation.ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        //progressBar.setMarkerProgress(progress);
        mProgressBarAnimatorOld.start();
    }
}
