package com.jinmin.formerroid.model;

import android.content.ContentValues;

import com.jinmin.formerroid.helpers.SQLHelper;

public class StoredContact
{
	private long _id;
	private String name;
	private String tel;
	private int ring_mode;
	private int ring_mode_option;

	public StoredContact(String name, String tel, int ring_mode, int ring_mode_option)
	{
		this.name = name;
		this.tel = tel;
		this.ring_mode = ring_mode;
		this.ring_mode_option = ring_mode_option;
	}

	public StoredContact(long _id, String name, String tel, int ring_mode, int ring_mode_option)
	{
		this._id = _id;
		this.name = name;
		this.tel = tel;
		this.ring_mode = ring_mode;
		this.ring_mode_option = ring_mode_option;
	}

	public ContentValues toContentValues()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(SQLHelper.STORE_CONTACT_TABLE.NAME, name);
		contentValues.put(SQLHelper.STORE_CONTACT_TABLE.TEL, tel);
		contentValues.put(SQLHelper.STORE_CONTACT_TABLE.RING_MODE, ring_mode);
		contentValues.put(SQLHelper.STORE_CONTACT_TABLE.RING_MODE_OPTION, ring_mode_option);
		return contentValues;
	}

	public long getId()
	{
		return _id;
	}

	public void setId(long _id)
	{
		this._id = _id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTel()
	{
		return tel;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public int getRing_mode()
	{
		return ring_mode;
	}

	public void setRing_mode(int ring_mode)
	{
		this.ring_mode = ring_mode;
	}

	public int getRing_mode_option()
	{
		return ring_mode_option;
	}

	public void setRing_mode_option(int ring_mode_option)
	{
		this.ring_mode_option = ring_mode_option;
	}

}
