package com.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener {
	
	private static String TAG = YambaApplication.class.getSimpleName();
	private Twitter twitter;
	private SharedPreferences preferences;
	
	@Override
	public void onCreate() {
		super.onCreate();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        Log.i(TAG, "onCreate");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminate");
	}



	public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d(TAG, "onSharedPreferenceChanged");
		twitter = null;
	}
	
	public synchronized Twitter getTwitter() {
		if (twitter == null) {
			String username, password, apiRoot;
			username = preferences.getString("userName", "");
			password = preferences.getString("passWord", "");
			apiRoot = preferences.getString("apiRoot", "http://yamba.marakana.com/api");
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(apiRoot);
		}
		return twitter;
	}

}
