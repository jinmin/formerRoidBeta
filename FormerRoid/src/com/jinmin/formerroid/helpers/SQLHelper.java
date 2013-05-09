package com.jinmin.formerroid.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jinmin.formerroid.handler.JmExceptionHandler;

public class SQLHelper extends SQLiteOpenHelper
{

	public final static String DB_NAME = "formerRoid.db";
	public final static int DB_VERSION = 4;

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
		try {
			db.execSQL("CREATE TABLE IF NOT EXISTS " + STORED_CONTACT_TB + " (" + STORE_CONTACT_TABLE.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + STORE_CONTACT_TABLE.NAME + " TEXT " + ", " + STORE_CONTACT_TABLE.TEL + " TEXT " + ", " + STORE_CONTACT_TABLE.RING_MODE + " INTEGER " + ", " + STORE_CONTACT_TABLE.RING_MODE_OPTION + " INTEGER );");
			db.execSQL("CREATE TABLE IF NOT EXISTS " + STORED_PERIOD_TB + " (" + STORE_PERIOD_TABLE.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + STORE_PERIOD_TABLE.TITLE + " TEXT " + ", " + STORE_PERIOD_TABLE.START_TIME + " TEXT " + ", " + STORE_PERIOD_TABLE.FINISH_TIME + " TEXT " + ", " + STORE_PERIOD_TABLE.WEEKS + " TEXT " + ", " + STORE_PERIOD_TABLE.RING_MODE + " INTEGER " + ", " + STORE_PERIOD_TABLE.RING_MODE_OPTION + " INTEGER );");
		}
		catch (Exception e) {
			JmExceptionHandler.getStackTraceToString(e);
		}
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
		// 만약 데이터 이전이 필요하면 Thread로 처리
		try {
			db.beginTransaction();
			String query = "SELECT name FROM sqlite_master WHERE type='table' AND name in ('" + STORED_CONTACT_TB + "', '" + STORED_PERIOD_TB + "')";

			Cursor c = db.rawQuery(query, null);
			while (c.moveToNext()) {
				if (STORED_CONTACT_TB.equals(c.getString(0))) {
					db.execSQL("DROP TABLE " + STORED_CONTACT_TB);
					Log.d("sql=execute", "DROP TABLE " + STORED_CONTACT_TB);
				}
				else if (STORED_PERIOD_TB.equals(c.getString(0))) {
					db.execSQL("DROP TABLE " + STORED_PERIOD_TB);
					Log.d("sql=execute", "DROP TABLE " + STORED_PERIOD_TB);
				}
			}

			onCreate(db);

			db.setTransactionSuccessful();
		}
		catch (Exception e) {
			JmExceptionHandler.getStackTraceToString(e);
		}
		finally {
			db.endTransaction();
		}
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
