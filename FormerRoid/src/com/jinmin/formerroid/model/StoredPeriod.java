package com.jinmin.formerroid.model;

import android.content.ContentValues;

import com.jinmin.formerroid.helpers.SQLHelper;

public class StoredPeriod
{
	private long _id;
	private String title;
	private String startTime;
	private String finishTime;
	private String weeks;
	private int ring_mode;
	private int ring_mode_option;

	public StoredPeriod(String title, String startTime, String finishTime, String weeks, int ring_mode, int ring_mode_option)
	{
		this.title = title;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.weeks = weeks;
		this.ring_mode = ring_mode;
		this.ring_mode_option = ring_mode_option;
	}

	public StoredPeriod(long _id, String title, String startTime, String finishTime, String weeks, int ring_mode, int ring_mode_option)
	{
		this._id = _id;
		this.title = title;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.weeks = weeks;
		this.ring_mode = ring_mode;
		this.ring_mode_option = ring_mode_option;
	}

	public ContentValues toContentValues()
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(SQLHelper.STORE_PERIOD_TABLE.TITLE, title);
		contentValues.put(SQLHelper.STORE_PERIOD_TABLE.START_TIME, startTime);
		contentValues.put(SQLHelper.STORE_PERIOD_TABLE.FINISH_TIME, finishTime);
		contentValues.put(SQLHelper.STORE_PERIOD_TABLE.WEEKS, weeks);
		contentValues.put(SQLHelper.STORE_PERIOD_TABLE.RING_MODE, ring_mode);
		contentValues.put(SQLHelper.STORE_PERIOD_TABLE.RING_MODE_OPTION, ring_mode_option);
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

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getFinishTime()
	{
		return finishTime;
	}

	public void setFinishTime(String finishTime)
	{
		this.finishTime = finishTime;
	}

	public String getWeeks()
	{
		return weeks;
	}

	public void setWeeks(String weeks)
	{
		this.weeks = weeks;
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
