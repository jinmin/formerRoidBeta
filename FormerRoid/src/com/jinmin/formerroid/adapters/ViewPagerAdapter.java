package com.jinmin.formerroid.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.jinmin.formerroid.ContactPageFragment;
import com.jinmin.formerroid.R;
import com.jinmin.formerroid.PeriodPageFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter
{
	public static final int VIEW_PAGE_COUNT = 2;

	private Context _context;

	private Fragment[] fragmentArr;

	public ViewPagerAdapter(Context context, FragmentManager fm)
	{
		super(fm);
		_context = context;
		fragmentArr = new Fragment[getCount()];
	}

	@Override
	public Fragment getItem(int position)
	{
		Fragment f = new Fragment();
		// Bundle bundle = new Bundle();
		Log.d("position == > ", String.valueOf(position));
		switch (position) {
			case 0:
				if (fragmentArr[position] == null) {
					fragmentArr[position] = ContactPageFragment.newInstance(_context, position);
				}
				f = fragmentArr[position];
				break;
			case 1:
				if (fragmentArr[position] == null) {
					fragmentArr[position] = PeriodPageFragment.newInstance(_context, position);
				}
				f = fragmentArr[position];
				break;
		// case 2:
		// break;
		}

		return f;
	}

	@Override
	public int getCount()
	{
		return VIEW_PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		switch (position) {
			case 0:
				return _context.getString(R.string.title_section1).toUpperCase();
			case 1:
				return _context.getString(R.string.title_section2).toUpperCase();
			case 2:
				return _context.getString(R.string.title_section3).toUpperCase();
		}
		return null;
	}
}
