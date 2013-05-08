package com.jinmin.formerroid.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jinmin.formerroid.handler.JmExceptionHandler;
import com.jinmin.formerroid.helpers.SQLHelper;
import com.jinmin.formerroid.model.StoredContact;
import com.jinmin.formerroid.model.StoredPeriod;

public class StoredPeriodService
{

	private SQLHelper sqlHelper;

	public StoredPeriodService(Context _context)
	{
		sqlHelper = new SQLHelper(_context);
	}

	public List<StoredPeriod> getStoredPeriodList()
	{

		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		List<StoredPeriod> list = new ArrayList<StoredPeriod>();
		try {
			Cursor cursor = db.rawQuery("select _id," + SQLHelper.STORE_PERIOD_TABLE.TITLE + "," + SQLHelper.STORE_PERIOD_TABLE.START_TIME + ", " + SQLHelper.STORE_PERIOD_TABLE.FINISH_TIME + ", " + SQLHelper.STORE_PERIOD_TABLE.WEEKS + ", " + SQLHelper.STORE_PERIOD_TABLE.RING_MODE + ", " + ", " + SQLHelper.STORE_PERIOD_TABLE.RING_MODE_OPTION + " from " + SQLHelper.STORED_PERIOD_TB, null);
			while (cursor.moveToNext()) {
				list.add(new StoredPeriod(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
			}
		}
		catch (Exception e) {
			Log.e("StoredPeriodService.getStoredPeriodList", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return list;
	}

	public int checkDuplicatedStoredPeriod(String startTime, String finishTime, String weeks)
	{

		int result = 0;
		// SQLiteDatabase db = sqlHelper.getReadableDatabase();
		// try {
		// Cursor cursor = db.rawQuery("select count(*) cnt  from " + SQLHelper.STORED_CONTACT_TB + " where tel = '" + tel + "'", null);
		// while (cursor.moveToNext()) {
		// result = cursor.getInt(0);
		// }
		// }
		// catch (Exception e) {
		// Log.e("StoredContactService.checkDuplicatedStoredContact", JmExceptionHandler.getStackTraceToString(e));
		// }
		// finally {
		// db.close();
		// }
		return result;
	}

	public boolean insertPeriod(StoredPeriod storePeriod)
	{
		if (storePeriod == null) {
			return false;
		}
		boolean isSuccessed = false;
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		long result = 0l;
		try {
			result = db.insert(SQLHelper.STORED_PERIOD_TB, null, storePeriod.toContentValues());
			if (result > 0) {
				Log.d("@@@", "result ==> " + result);
				storePeriod.setId(result);
				isSuccessed = true;
			}
		}
		catch (Exception e) {
			Log.e("StoredPeriodService.insertPeriod", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return isSuccessed;
	}

	public boolean updatePeroid(StoredContact storedContact)
	{
		if (storedContact == null) {
			return false;
		}
		boolean isSuccessed = false;
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		try {
			if (storedContact != null) {
				isSuccessed = db.update(SQLHelper.STORED_PERIOD_TB, storedContact.toContentValues(), " _id =  " + storedContact.getId(), null) > 0 ? true : false;
			}
		}
		catch (Exception e) {
			Log.e("StoredPeriodService.updatePeroid", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return isSuccessed;
	}

	public boolean removePeriod(long id)
	{
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		long result = 0l;
		try {
			String[] params = new String[]{ String.valueOf(id) };
			result = db.delete(SQLHelper.STORED_PERIOD_TB, " _id = ? ", params);
		}
		catch (Exception e) {
			Log.e("StoredPeriodService.removePeriod", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return result > 0 ? true : false;
	}

}
