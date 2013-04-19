package com.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	
	private static final String TAG = "UpdateService";
	private static final int DELAY = 6000;
	private boolean runFlag = true;
	private Update updater;
	
	private class Update extends Thread {
		
		private Update() {
			super("UpdaterService-Update");
		}
		
		@Override
		public void run() {
			UpdaterService updaterService = UpdaterService.this;
			while (updaterService.runFlag){
				Log . d(TAG, "Updater running");
				try {
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				}
				catch (InterruptedException e) {
				updaterService.runFlag = false;
				}
			}
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		updater = new Update();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		runFlag = false;
		updater.interrupt();
		updater = null;
		Log.d(TAG, "onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		runFlag = true ;
		updater.start();
		Log.d(TAG, "onStarted");
		return START_STICKY;
	}
	
}
