package com.cs.app.momo;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatWindowSmallView extends LinearLayout {

	/**
	 * �O��p�a�B�����e��
	 */
	public static int windowViewWidth;

	/**
	 * �O��p�a�B��������
	 */
	public static int windowViewHeight;

	/**
	 * �O��t�Ϊ��A�C������
	 */
	private static int statusBarHeight;

	/**
	 * �Ω��s�p�a�B������m
	 */
	private WindowManager windowManager;

	/**
	 * �p�a�B�����G��
	 */
	private LinearLayout smallWindowLayout;

	/**
	 * �p���b���
	 */
	private ImageView rocketImg;

	/**
	 * �p�a�B�����Ѽ�
	 */
	private WindowManager.LayoutParams mParams;

	/**
	 * �O���e����m�b�ù��W����Э�
	 */
	private float xInScreen;

	/**
	 * �O���e����m�b�ù��W���a���Э�
	 */
	private float yInScreen;

	/**
	 * �O������U�ɦb�ù��W����Ъ���
	 */
	private float xDownInScreen;

	/**
	 * �O������U�ɦb�ù��W���a���Ъ���
	 */
	private float yDownInScreen;

	/**
	 * �O������U�ɦb�p�a�B����View�W����Ъ���
	 */
	private float xInView;

	/**
	 * �O������U�ɦb�p�a�B����View�W���a���Ъ���
	 */
	private float yInView;

	/**
	 * �O��p���b���e��
	 */
	private int rocketWidth;

	/**
	 * �O��p���b������
	 */
	private int rocketHeight;

	/**
	 * �O���e���O�_���U
	 */
	private boolean isPressed;
	
	///////////////////
	//my new add
	private ImageView imgMonster;
	private float xInScreen_tmp, yInScreen_tmp;

	public FloatWindowSmallView(Context context) {
		super(context);
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		smallWindowLayout = (LinearLayout) findViewById(R.id.small_window_layout);
		windowViewWidth = smallWindowLayout.getLayoutParams().width;
		windowViewHeight = smallWindowLayout.getLayoutParams().height;
		rocketImg = (ImageView) findViewById(R.id.img2);

		rocketWidth = rocketImg.getLayoutParams().width;
		rocketHeight = rocketImg.getLayoutParams().height;
		//Log.d("tag", "rocketWidth="+rocketWidth);
		//Log.d("tag", "rocketHeight="+rocketHeight);

        imgMonster = (ImageView) findViewById(R.id.img1);

        SharedPreferences settings = context.getSharedPreferences("MOMO_SETTINGS",  0);;
        SharedPreferences.Editor PE = settings.edit();
        PE.putBoolean("WIDGET_SWITCH", true);
        PE.commit();

        String status = settings.getString("MONSTER_STATUS", "sleep");
        String monsterName = settings.getString("MONSTER_NAME", "penguin");
        Log.d("status", monsterName);
        Log.d("status", status);
        int id;
        if(status.equals("normal")){
            id = getResources().getIdentifier(monsterName + "_n", "drawable", "com.cs.app.momo");
        }else if(status.equals("hungry")){
            id = getResources().getIdentifier(monsterName + "_h", "drawable", "com.cs.app.momo");
        }else if(status.equals("death")){
            id = getResources().getIdentifier(monsterName + "_d", "drawable", "com.cs.app.momo");
        }else{
            id = getResources().getIdentifier(monsterName + "_s", "drawable", "com.cs.app.momo");
        }
        imgMonster.setImageResource(id);
        rocketImg.setImageResource(id);
        setImgAnimation(imgMonster);
		//TextView percentView = (TextView) findViewById(R.id.percent);
		//percentView.setText(MyWindowManager.getUsedPercentValue(context));
	}

	

	private boolean longTouch = false;
	//new add
	final Handler handler = new Handler(); 
	Runnable mLongPressed = new Runnable() { 
	    public void run() { 
	    	//Toast.makeText(getContext(), "Long press!", Toast.LENGTH_LONG).show();
	    	goStartAPP();
            longTouch = true;

	    }   
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isPressed = true;
            longTouch = false;

			// �����U�ɰO��n���,�a���Ъ��ȳ��ݭn��h���A�C����
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen_tmp = xInScreen;
			yInScreen_tmp = yInScreen;
			
			handler.postDelayed(mLongPressed, 500); //���0.4��
			setImgAnimation(imgMonster);
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();

			// �Y���ʽd���j, �h�P�w�ʧ@�����ʦӫD�Q���
			if(Math.abs(xInScreen_tmp-xInScreen)>15.0f || Math.abs(yInScreen_tmp-yInScreen)>15.0f) {
				handler.removeCallbacks(mLongPressed);
			}
			
			// ���ʪ��ɭԧ�s�p�a�B�������A�M��m
			updateViewStatus();
			updateViewPosition();

			break;
		case MotionEvent.ACTION_UP:
			isPressed = false;
			handler.removeCallbacks(mLongPressed);

			if (MyWindowManager.isReadyToLaunch()) {
				//launchRocket();

                // 關閉懸浮窗，移除所有懸浮窗，並停止Service
                MyWindowManager.removeLauncher(getContext());
                MyWindowManager.removeBigWindow(getContext());
                MyWindowManager.removeSmallWindow(getContext());
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                getContext().stopService(intent);

			} else {
				updateViewStatus();
				// �p�G������}�ù��ɡAxDownInScreen�MxInScreen�۵��A�ByDownInScreen�MyInScreen�۵��A�h��Ĳ�o�F���@�U�ƥ�C
				if (xDownInScreen == xInScreen && yDownInScreen == yInScreen && !longTouch) {
					openBigWindow();
				}
			}
            setImgAnimation(imgMonster);

			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * �N�p�a�B�����ѼƶǤJ�A�Ω��s�p�a�B������m�C
	 * 
	 * @param params
	 *            �p�a�B�����Ѽ�
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * �Ω�o�g�p���b�C
	 */
	private void launchRocket() {
		MyWindowManager.removeLauncher(getContext());
		new LaunchTask().execute();
	}

	/**
	 * ��s�p�a�B���b�ù�������m�C
	 */
	private void updateViewPosition() {
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
		MyWindowManager.updateLauncher();
	}

	/**
	 * ��sView����ܪ��A�A�P�_�O����a�B���٬O�p���b�C
	 */
	private void updateViewStatus() {
		if (isPressed && rocketImg.getVisibility() != View.VISIBLE) {
			mParams.width = windowViewWidth;
			//mParams.height = windowViewHeight; //180
			//mParams.width = rocketWidth;
			mParams.height = rocketHeight; //120
			
			Log.d("tag","windowViewWidth="+windowViewWidth);
			Log.d("tag","rocketHeight="+rocketHeight);
			windowManager.updateViewLayout(this, mParams);
			//smallWindowLayout.setVisibility(View.GONE);
			//imgMonster.setVisibility(View.GONE);
			//rocketImg.setVisibility(View.VISIBLE);
			MyWindowManager.createLauncher(getContext());
		} else if (!isPressed) {
			
			mParams.width = windowViewWidth;
			mParams.height = windowViewHeight;
			windowManager.updateViewLayout(this, mParams);
			//smallWindowLayout.setVisibility(View.VISIBLE);
			//imgMonster.setVisibility(View.VISIBLE);
			//rocketImg.setVisibility(View.GONE);
			MyWindowManager.removeLauncher(getContext());
		}
	}

	/**
	 * ���}�j�a�B���A�P�������p�a�B���C
	 */
	private void openBigWindow() {
		MyWindowManager.createBigWindow(getContext());
		//MyWindowManager.removeSmallWindow(getContext());
	}

	/**
	 * �Ω����A�C�����סC
	 * 
	 * @return ��^���A�C���ת��Ϥ��ȡC
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}
	
	
	
	/////////////////////////////////////////////////////////////////
	///my adding
	
	private void setImgAnimation(ImageView img) {
		//�Ĥ@�հʵe:����
		Animation am1 = new ScaleAnimation(1,1.1f,1,0.9f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		am1.setDuration(300);	// �ʵe�}�l�쵲�����ɶ��A1000=1��
		am1.setRepeatCount( 0 );	// �ʵe���Ц��� (-1��ܤ@�����СA0��ܤ����а���A�ҥH�u�|����@��)
		
		//�ĤG�հʵe:�Ⱚ
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

        //�ĤG�հʵe:�Ⱚ
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
	
	
	private void goStartAPP() {
		MyWindowManager.removeBigWindow(getContext());
		//MyWindowManager.removeSmallWindow(getContext());
		
		Intent intent = new Intent(getContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		getContext().startActivity(intent);
	}
	
	
	
	
	////////////////////////////////////////////////////////////////

	/**
	 * �}�l����o�g�p���b�����ȡC
	 * 
	 * @author guolin
	 */
	class LaunchTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// �b�o�̹�p���b����m�i����ܡA�q�Ӳ��ͤ��b�ɪŪ��ĪG
			while (mParams.y > 0) {
				mParams.y = mParams.y - 10;
				publishProgress();
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			windowManager.updateViewLayout(FloatWindowSmallView.this, mParams);
		}

		@Override
		protected void onPostExecute(Void result) {
			// ���b�ɪŵ�����A�^�k���a�B�����A
			updateViewStatus();
			mParams.x = (int) (xDownInScreen - xInView);
			mParams.y = (int) (yDownInScreen - yInView);
			windowManager.updateViewLayout(FloatWindowSmallView.this, mParams);
		}

	}
	
	

}
