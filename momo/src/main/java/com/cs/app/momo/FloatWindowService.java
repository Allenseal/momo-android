package com.cs.app.momo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

public class FloatWindowService extends Service {

	/**
	 * �Ω�b�����Ыةβ����a�B���C
	 */
	private Handler handler = new Handler();
    private static Context mContext;
	/**
	 * �p�ɾ��A�w�ɶi���˴��e���ӳЫ��٬O�����a�B���C
	 */
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// �}�ҭp�ɾ��A�C�j0.5���s�@��
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
		}
		return super.onStartCommand(intent, flags, startId);
	}

    @Override
    public void onCreate() {
        Log.d("TAG", "onCreate()");

        mContext = getApplicationContext();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BCAST_CONFIGCHANGED);
        this.registerReceiver(mBroadcastReceiver, filter);
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service�Q�פ�P�ɤ]����p�ɾ��~��B��
		timer.cancel();
		timer = null;
        this.unregisterReceiver(mBroadcastReceiver);
	}

	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			// ��e�����O�ୱ�A�B�S���a�B����ܡA�h�Ы��a�B���C
			if (isHome() && !MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.createSmallWindow(getApplicationContext());
					}
				});
			}
			// ��e�������O�ୱ�A�B���a�B����ܡA�h�����a�B���C
			else if (!isHome() && MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						//MyWindowManager.removeSmallWindow(getApplicationContext());
						//MyWindowManager.removeBigWindow(getApplicationContext());
					}
				});
			}
			// ��e�����O�ୱ�A�B���a�B����ܡA�h��s�O�����ơC
			else if (isHome() && MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						//MyWindowManager.updateUsedPercent(getApplicationContext());
					}
				});
			}
		}

	}

	/**
	 * �P�_��e�����O�_�O�ୱ
	 */
	private boolean isHome() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return getHomes().contains(rti.get(0).topActivity.getPackageName());
	}

	/**
	 * ��o�ݩ�ୱ�����Ϊ����Υ]�W��
	 * 
	 * @return ��^�]�t�Ҧ��]�W���r��M��
	 */
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}

    private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";

    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent myIntent) {

            if ( myIntent.getAction().equals( BCAST_CONFIGCHANGED ) ) {

                Log.d("TAG", "received->" + BCAST_CONFIGCHANGED);


                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    // it's Landscape
                    Log.d("TAG", "LANDSCAPE");
                    if (MyWindowManager.rocketLauncher != null) {
                        MyWindowManager.removeLauncher(context);
                        MyWindowManager.createLauncher(context);
                    }
                    if (MyWindowManager.smallWindow != null) {
                        MyWindowManager.removeSmallWindow(context);
                        MyWindowManager.removeSmallWindow(context);
                    }
                }
                else {
                    Log.d("TAG", "PORTRAIT");
                    if (MyWindowManager.rocketLauncher != null) {
                        MyWindowManager.removeLauncher(context);
                        MyWindowManager.createLauncher(context);
                    }
                    if (MyWindowManager.smallWindow != null) {
                        MyWindowManager.removeSmallWindow(context);
                        MyWindowManager.removeSmallWindow(context);
                    }
                }
            }
        }
    };
}
