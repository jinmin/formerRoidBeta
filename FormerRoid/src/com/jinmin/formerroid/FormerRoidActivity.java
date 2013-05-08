package com.jinmin.formerroid;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.jinmin.formerroid.adapters.ViewPagerAdapter;

public class FormerRoidActivity extends FragmentActivity implements ActionBar.TabListener
{

	ViewPagerAdapter viewPagerAdapter;

	ViewPager viewPager;

	int currentPosition = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.former_roid_layout);

		viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setDisplayShowTitleEnabled(false);

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager)findViewById(R.id.pager);
		viewPager.setAdapter(viewPagerAdapter);

		// addChangeTabListener
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				currentPosition = position;
				actionBar.setSelectedNavigationItem(position);
			}

		});

		// set new tab name from viewPagerAdapter
		for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(viewPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
			case ListPageFragment.CONTACT_REQUEST_CODE:
				if (resultCode == Activity.RESULT_OK) {

					Log.d("LIST_PAGE IDX ==>", String.valueOf(currentPosition));
					Cursor cursor = getContentResolver().query(intent.getData(), new String[]{ ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);
					cursor.moveToFirst();
					if (cursor != null) {
						ListPageFragment fragment = (ListPageFragment)viewPagerAdapter.getItem(currentPosition);
						Log.d("CONTACT NAME ==>", cursor.getString(0));
						Log.d("CONTACT Phone Number ==>", cursor.getString(1));
						String name = cursor.getString(0);
						String phoneNumber = cursor.getString(1);
						cursor.close();

						fragment.setContactData(name, phoneNumber);
					}
				}
				break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// getMenuInflater().inflate(R.menu.former_roid_layout, menu);
		return false;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
	}

	/****************************
	 * Exit
	 */
	private static final int MSG_TIMER_EXPIRED = 1;
	private static final int BACKEY_TIMEOUT = 2000;
	private boolean mIsBackKeyPressed = false;
	private long mCurrentTimeInMillis = 0;

	@Override
	public void onBackPressed()
	{
		if (mIsBackKeyPressed == false) {
			mIsBackKeyPressed = true;
			mCurrentTimeInMillis = Calendar.getInstance().getTimeInMillis();
			Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
			startTimer();
		}
		else {
			mIsBackKeyPressed = false;

			if (Calendar.getInstance().getTimeInMillis() <= (mCurrentTimeInMillis + (BACKEY_TIMEOUT))) {
				finish();
			}
		}
	}

	private void startTimer()
	{
		mTimerHander.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKEY_TIMEOUT);
	}

	private Handler mTimerHander = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
				case MSG_TIMER_EXPIRED:
					mIsBackKeyPressed = false;
					break;
			}
		}
	};
}
