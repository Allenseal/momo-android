package com.cs.app.momo;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * �O��j�a�B�����e��
	 */
	public static int viewWidth = 300;

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
		final ImageButton feed = (ImageButton) findViewById(R.id.w_feed);
		final ImageButton cost = (ImageButton) findViewById(R.id.w_cost);

        //set button view
        feed.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            feed.setBackgroundResource(R.drawable.widget_feed_button_push);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            feed.setBackgroundResource(R.drawable.widget_feed_button_release);
                            MyWindowManager.removeBigWindow(context);
                            MyWindowManager.removeSmallWindow(context);

                            //my revise
                            Intent intent = new Intent(getContext(), Feed.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);
                        }
                        return false;
                    }
                }
        );

        cost.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            cost.setBackgroundResource(R.drawable.widget_cost_button_push);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            cost.setBackgroundResource(R.drawable.widget_cost_button_release);
                            MyWindowManager.removeBigWindow(context);
                            MyWindowManager.removeSmallWindow(context);

                            //my revise
                            Intent intent = new Intent(getContext(), Cost.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);
                        }
                        return false;
                    }
                }
        );

        final ImageButton returnBtn = (ImageButton) findViewById(R.id.return_button);
        returnBtn.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            returnBtn.setImageResource(R.drawable.ic_action_back_down);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            returnBtn.setImageResource(R.drawable.ic_action_back);
                            MyWindowManager.removeBigWindow(context);
                            //MyWindowManager.removeSmallWindow(context);
                        }
                        return false;
                    }
                }
        );

	}
}
