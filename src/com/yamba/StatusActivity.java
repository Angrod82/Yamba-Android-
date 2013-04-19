package com.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class StatusActivity extends Activity implements OnClickListener, TextWatcher {
	
	private static final String TAG = "Status Activity";
	private Button updateButton;
	private EditText editText;
	private TextView textCount;
	
	private class PostToTwitter extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				Twitter.Status status = ((YambaApplication) getApplication()).getTwitter().setStatus(params[0]);
				return status.text;
			}
			catch (TwitterException e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
				return "Failed to post";
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		}
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener((android.view.View.OnClickListener) this);
        editText = (EditText) findViewById(R.id.editText);
        textCount = (TextView) findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        textCount.addTextChangedListener((android.text.TextWatcher) this);        
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_status, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, PrefActivity.class));
			break;
		case R.id.itemServiceStart:
			startService(new Intent(this, UpdaterService.class));
			break;
		case R.id.itemServiceStop:
			stopService(new Intent(this, UpdaterService.class));
			break;
		}
		return true;
	}

	public void onClick(View arg0) {
		String status = editText.getText().toString();
		new PostToTwitter().execute(status);
	}

	public void afterTextChanged(Editable arg0) {
		Log.d(TAG, "afterTextChange");
		int count = 140 - arg0.length();
		String text = Integer.toString(count);
		textCount.setText(text);
		if (count > 10) {
			textCount.setTextColor(Color.GREEN);
		}
		else {
			if (count >= 1 && count <= 10) {
				textCount.setTextColor(Color.YELLOW);
			}
			else {
				textCount.setTextColor(Color.RED);
			}
		}
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	
}
