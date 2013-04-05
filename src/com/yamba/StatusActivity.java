package com.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;



public class StatusActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "Status Activit";
	private Button updateButton;
	private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener((android.view.View.OnClickListener) this);
        editText = (EditText) findViewById(R.id.editText);
    }

	public void onClick(View arg0) {
		Log.d(TAG, "onClick: " + editText.getText().toString());
	}

}
