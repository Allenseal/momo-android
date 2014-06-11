package com.cs.app.momo;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RocketLauncher extends LinearLayout {

	/**
	 * �O����b�o�g�x���e��
	 */
	public static int width = 0;

	/**
	 * �O����b�o�g�x������
	 */
	public static int height = 100;

	/**
	 * ���b�o�g�x���I���Ϥ�
	 */
	private ImageView launcherImg;

	public RocketLauncher(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.launcher, this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout1);
		launcherImg = (ImageView) findViewById(R.id.imgClose);
		//width = launcherImg.getLayoutParams().width;
		//height = launcherImg.getLayoutParams().height;
		
		
		
		//��o�ù��e��
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth();  // deprecated
		//height = display.getHeight();  // deprecated
		height = layout.getLayoutParams().height;
		
		
		
		Log.d("tag", "width1="+width);
		//height = launcherImg.getLayoutParams().height;
	}

	/**
	 * ��s���b�o�g�x����ܪ��A�C�p�G�p���b�Q�����b�o�g�O�W�A�N��ܵo�g�C
	 */
	public void updateLauncherStatus(boolean isReadyToLaunch) {
		if (isReadyToLaunch) {
			launcherImg.setImageResource(R.drawable.close_circle_choose);
		} else {
			launcherImg.setImageResource(R.drawable.close_circle);
		}
	}

}
