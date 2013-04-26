package com.yamba;

import java.util.List;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	
	private static final String TAG = "UpdateService";
	private static final int DELAY = 60000;
	private boolean runFlag = true;
	private Updater updater;
	private YambaApplication yamba;
	private DBHelper dbHelper;
	
	private class Updater extends Thread {
		
		private List<Twitter.Status> timeLine;
		
		private Updater() {
			super("UpdaterService-Update");
		}
		
		@Override
		public void run() {
			UpdaterService updaterService = UpdaterService.this;
			Log.d(TAG, "Update run");
			while (updaterService.runFlag){
				try {
					try {
						timeLine = ((YambaApplication) getApplication()).getTwitter().getFriendsTimeline();
					}
					catch (TwitterException e) {
						Log.e(TAG, "Failed to connect to twitter service");
					}
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					ContentValues values = new ContentValues();
					for (Status status: timeLine) {
						values.clear();
						values.put(DBHelper.C_ID, status.id);
						values.put(DBHelper.C_CREATED_AT, status.createdAt.getTime());
						values.put(DBHelper.C_SOURCE, status.source);
						values.put(DBHelper.C_TEXT, status.text);
						values.put(DBHelper.C_USER, status.user.name);
						try {
							db.insertOrThrow(DBHelper.TABLE, null, values);
							Log.d(TAG, String.format( "%s:%s", status.user.name, status.text));
						}
						catch (SQLException e) {
							Log.e(TAG, e.toString());
							e.printStackTrace();
						}
					}
					db.close();
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
		yamba = (YambaApplication)getApplication();
		dbHelper = new DBHelper(yamba);
		updater = new Updater();
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
