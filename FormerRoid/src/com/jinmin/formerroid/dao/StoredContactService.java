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

public class StoredContactService
{

	private SQLHelper sqlHelper;

	public StoredContactService(Context _context)
	{
		sqlHelper = new SQLHelper(_context);
	}

	public List<StoredContact> getStoredContactList()
	{

		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		List<StoredContact> list = new ArrayList<StoredContact>();
		try {
			Cursor cursor = db.rawQuery("select _id, name, tel, ring_mode, ring_mode_option  from " + SQLHelper.STORED_CONTACT_TB, null);
			while (cursor.moveToNext()) {
				list.add(new StoredContact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
			}
		}
		catch (Exception e) {
			Log.e("StoredContactService.getStoredContactList", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return list;
	}

	public int checkDuplicatedStoredContact(String name, String tel)
	{

		int result = 0;
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		try {
			Cursor cursor = db.rawQuery("select count(*) cnt  from " + SQLHelper.STORED_CONTACT_TB + " where tel = '" + tel + "'", null);
			while (cursor.moveToNext()) {
				result = cursor.getInt(0);
			}
		}
		catch (Exception e) {
			Log.e("StoredContactService.checkDuplicatedStoredContact", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return result;
	}

	public boolean insertContact(StoredContact storedContact)
	{
		if (storedContact == null) {
			return false;
		}
		boolean isSuccessed = false;
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		long result = 0l;
		try {
			if (storedContact != null) {
				result = db.insert(SQLHelper.STORED_CONTACT_TB, null, storedContact.toContentValues());
				if (result > 0) {
					Log.d("@@@", "result ==> " + result);
					storedContact.setId(result);
					isSuccessed = true;
				}
			}
		}
		catch (Exception e) {
			Log.e("StoredContactService.insertContact", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return isSuccessed;
	}

	public boolean updateContact(StoredContact storedContact)
	{
		if (storedContact == null) {
			return false;
		}
		boolean isSuccessed = false;
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		try {
			if (storedContact != null) {
				isSuccessed = db.update(SQLHelper.STORED_CONTACT_TB, storedContact.toContentValues(), " _id =  " + storedContact.getId(), null) > 0 ? true : false;
			}
		}
		catch (Exception e) {
			Log.e("StoredContactService.updateContact", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return isSuccessed;
	}

	public boolean removeContact(long id)
	{
		SQLiteDatabase db = sqlHelper.getWritableDatabase();
		long result = 0l;
		try {
			String[] params = new String[]{ String.valueOf(id) };
			result = db.delete(SQLHelper.STORED_CONTACT_TB, " _id = ? ", params);
		}
		catch (Exception e) {
			Log.e("StoredContactService.removeContact", JmExceptionHandler.getStackTraceToString(e));
		}
		finally {
			db.close();
		}
		return result > 0 ? true : false;
	}

}
