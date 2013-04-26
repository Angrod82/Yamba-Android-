package com.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	static final String TAG = DBHelper.class.getSimpleName();
	static final String DB_NAME = "timeline.db";
	static final int DB_VERSION = 1 ;
	static final String TABLE = "timeline";
	static final String C_ID = "id";
	static final String C_CREATED_AT = "createdat";
	static final String C_SOURCE = "source";
	static final String C_TEXT = "txt";
	static final String C_USER = "user";
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = " create table " + TABLE + " (" + C_ID + " int primary key, " + C_CREATED_AT + " int, " + C_SOURCE + " text, " + C_USER + " text, " + C_TEXT + " text)";
		db.execSQL(sql);
		Log.d(TAG, "onCrate:" + sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL ( " drop table if exists " + TABLE);
		Log.d(TAG, "onUpdated" );
		onCreate(db);

	}

}
