package com.jinmin.formerroid.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper
{

	public final static String DB_NAME = "formerRoid.db";
	public final static int DB_VERSION = 2;

	public final static String STORED_CONTACT_TB = "STORED_CONTACT_TB";
	public final static String STORED_PERIOD_TB = "STORED_PERIOD_TB";

	public SQLHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * 생성시
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("create table " + STORED_CONTACT_TB + " (" + STORE_CONTACT_TABLE.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + STORE_CONTACT_TABLE.NAME + " TEXT " + ", " + STORE_CONTACT_TABLE.TEL + " TEXT " + ", " + STORE_CONTACT_TABLE.RING_MODE + " INTEGER " + ", " + STORE_CONTACT_TABLE.RING_MODE_OPTION + " INTEGER );");
		db.execSQL("create table " + STORED_PERIOD_TB + " (" + STORE_PERIOD_TABLE.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + STORE_PERIOD_TABLE.TITLE + " TEXT " + ", " + STORE_PERIOD_TABLE.START_TIME + " TEXT " + ", " + STORE_PERIOD_TABLE.FINISH_TIME + " TEXT " + ", " + STORE_PERIOD_TABLE.WEEKS + " TEXT " + ", " + STORE_PERIOD_TABLE.RING_MODE + " INTEGER " + ", " + STORE_PERIOD_TABLE.RING_MODE_OPTION + " INTEGER );");
	}

	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
	}

	/**
	 * 업그레이드 호출
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}

	public static class STORE_CONTACT_TABLE
	{
		public final static String ROW_ID = "_id";
		public final static String NAME = "name";
		public final static String TEL = "tel";
		public final static String RING_MODE = "ring_mode";
		public final static String RING_MODE_OPTION = "ring_mode_option";
	}

	public static class STORE_PERIOD_TABLE
	{
		public final static String ROW_ID = "_id";
		public final static String TITLE = "title";
		public final static String START_TIME = "start_time";
		public final static String FINISH_TIME = "finish_time";
		public final static String WEEKS = "weeks";
		public final static String RING_MODE = "ring_mode";
		public final static String RING_MODE_OPTION = "ring_mode_option";
	}

}
