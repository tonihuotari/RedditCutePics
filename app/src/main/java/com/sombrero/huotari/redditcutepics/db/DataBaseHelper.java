package com.sombrero.huotari.redditcutepics.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "RedditCutePics.db";

	private static DataBaseHelper mInstance;

	public static DataBaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DataBaseHelper(context);
		}
		return mInstance;
	}

	public static void closeInstance() {
		if (mInstance != null) {
			mInstance.close();
		}
	}

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(RedditItemContract.SQL_CREATE_ENTRIES);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Simply deleting old database on upgrade
		db.execSQL(RedditItemContract.SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}
