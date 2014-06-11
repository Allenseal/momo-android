package com.cs.app.momo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class MyWindowManager {

	/**
	 * �p�a�B��View�����
	 */
	protected static FloatWindowSmallView smallWindow;

	/**
	 * �j�a�B��View�����
	 */
    protected static FloatWindowBigView bigWindow;

	/**
	 * ���b�o�g�x�����
	 */
    protected static RocketLauncher rocketLauncher;

	/**
	 * �p�a�B��View���Ѽ�
	 */
	private static LayoutParams smallWindowParams;

	/**
	 * �j�a�B��View���Ѽ�
	 */
	private static LayoutParams bigWindowParams;

	/**
	 * ���b�o�g�x���Ѽ�
	 */
	private static LayoutParams launcherParams;

	/**
	 * �Ω󱱨�b�ù��W�K�[�β����a�B��
	 */
	private static WindowManager mWindowManager;

	/**
	 * �Ω�������i�ΰO����
	 */
	private static ActivityManager mActivityManager;

    protected static boolean resetParamsSwitch = true;
	/**
	 * �Ыؤ@�Ӥp�a�B���C��l��m���ù����k�����W��m�C
	 */
	public static void createSmallWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (smallWindow == null) {
			smallWindow = new FloatWindowSmallView(context);
			if (resetParamsSwitch) {
				smallWindowParams = new LayoutParams();
				smallWindowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
				smallWindowParams.format = PixelFormat.RGBA_8888;
				smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.width = FloatWindowSmallView.windowViewWidth; //�ȥ�float_window_small.xml�M�w
				smallWindowParams.height = FloatWindowSmallView.windowViewHeight; //�ȥ�float_window_small.xml�M�w
				smallWindowParams.x = screenWidth /2;
				smallWindowParams.y = screenHeight / 2;
			}
			smallWindow.setParams(smallWindowParams);
			windowManager.addView(smallWindow, smallWindowParams);
		}
	}

	/**
	 * �N�p�a�B���q�ù��W�����C
	 */
	public static void removeSmallWindow(Context context) {
		if (smallWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(smallWindow);
			smallWindow = null;
		}
	}

	/**
	 * �Ыؤ@�Ӥj�a�B���C��m���ù��������C
	 */
	public static void createBigWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (bigWindow == null) {
			bigWindow = new FloatWindowBigView(context);
			if (bigWindowParams == null) {
				bigWindowParams = new LayoutParams();
				bigWindowParams.x = screenWidth / 2
						- FloatWindowBigView.viewWidth / 2;
				bigWindowParams.y = screenHeight / 2
						- FloatWindowBigView.viewHeight / 2;
				bigWindowParams.type = LayoutParams.TYPE_PHONE;
				bigWindowParams.format = PixelFormat.RGBA_8888;
				bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				bigWindowParams.width = FloatWindowBigView.viewWidth;
				bigWindowParams.height = FloatWindowBigView.viewHeight;
			}
			windowManager.addView(bigWindow, bigWindowParams);
		}
	}

	/**
	 * �N�j�a�B���q�ù��W�����C
	 */
	public static void removeBigWindow(Context context) {
		if (bigWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(bigWindow);
			bigWindow = null;
		}
	}

	/**
	 * �Ыؤ@�Ӥ��b�o�g�x�A��m���ù������C
	 */
	public static void createLauncher(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (rocketLauncher == null) {
			rocketLauncher = new RocketLauncher(context);
			if (resetParamsSwitch) {
				launcherParams = new LayoutParams();
				launcherParams.x = screenWidth / 2 - RocketLauncher.width / 2;
				launcherParams.y = screenHeight - RocketLauncher.height;
				launcherParams.type = LayoutParams.TYPE_PHONE;
				launcherParams.format = PixelFormat.RGBA_8888;
				launcherParams.gravity = Gravity.LEFT | Gravity.TOP;
				launcherParams.width = RocketLauncher.width;
				launcherParams.height = RocketLauncher.height;
			}
			windowManager.addView(rocketLauncher, launcherParams);
		}
	}

	/**
	 * �N���b�o�g�x�q�ù��W�����C
	 */
	public static void removeLauncher(Context context) {
		if (rocketLauncher != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(rocketLauncher);
			rocketLauncher = null;
		}
	}

	/**
	 * ��s���b�o�g�x����ܪ��A�C
	 */
	public static void updateLauncher() {
		if (rocketLauncher != null) {
			rocketLauncher.updateLauncherStatus(isReadyToLaunch());
		}
	}

	/**
	 * ��s�p�a�B����TextView�W����ơA��ܰO����ϥΪ��ʤ���C
	 * 
	 * @param context
	 *            �i�ǤJ���ε{���W�U��C
	 */
	public static void updateUsedPercent(Context context) {
		if (smallWindow != null) {
			TextView percentView = (TextView) smallWindow
					.findViewById(R.id.percent);
			percentView.setText(getUsedPercentValue(context));
		}
	}

	/**
	 * �O�_���a�B��(�]�A�p�a�B���M�j�a�B��)��ܦb�ù��W�C
	 * 
	 * @return ���a�B����ܦb�ୱ�W��^true�A�S�����ܪ�^false�C
	 */
	public static boolean isWindowShowing() {
		return smallWindow != null || bigWindow != null;
	}

	/**
	 * �P�_�p���b�O�_�ǳƦn�o�g�F�C
	 * 
	 * @return ����b�Q�o��o�g�O�W��^true�A�_�h��^false�C
	 */
	public static boolean isReadyToLaunch(){
		
		if ((smallWindowParams.x > (launcherParams==null? 0 : launcherParams.x) && smallWindowParams.x
				+ smallWindowParams.width < (launcherParams==null? 0 : launcherParams.x)
				+ (launcherParams==null? 0 : launcherParams.width))
				&& (smallWindowParams.y + smallWindowParams.height > (launcherParams==null? 0 : launcherParams.y))) {
			return true;
		}
		return false;
	}

	/**
	 * �p�GWindowManager�٥��ЫءA�h�Ыؤ@�ӷs��WindowManager��^�C�_�h��^��e�w�Ыت�WindowManager�C
	 * 
	 * @param context
	 *            ���������ε{����Context.
	 * @return WindowManager����ҡA�Ω󱱨�b�ù��W�K�[�β����a�B���C
	 */
	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	/**
	 * �p�GActivityManager�٥��ЫءA�h�Ыؤ@�ӷs��ActivityManager��^�C�_�h��^��e�w�Ыت�ActivityManager�C
	 * 
	 * @param context
	 *            �i�ǤJ���ε{���W�U��C
	 * @return ActivityManager����ҡA�Ω�������i�ΰO����C
	 */
	private static ActivityManager getActivityManager(Context context) {
		if (mActivityManager == null) {
			mActivityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}

	/**
	 * �p��w�ϥΰO���骺�ʤ���A�ê�^�C
	 * 
	 * @param context
	 *            �i�ǤJ���ε{���W�U��C
	 * @return �w�ϥΰO���骺�ʤ���A�H�r��Φ���^�C
	 */
	public static String getUsedPercentValue(Context context) {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine
					.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll(
					"\\D+", ""));
			long availableSize = getAvailableMemory(context) / 1024;
			int percent = (int) ((totalMemorySize - availableSize)
					/ (float) totalMemorySize * 100);
			return percent + "%";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "�a�B��";
	}

	/**
	 * ����e�i�ΰO����A��^��ƥH�줸�լ����C
	 * 
	 * @param context
	 *            �i�ǤJ���ε{���W�U��C
	 * @return ��e�i�ΰO����C
	 */
	private static long getAvailableMemory(Context context) {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(mi);
		return mi.availMem;
	}
	
	///////////////////////////////////////////////////////////
	//My adding

}
