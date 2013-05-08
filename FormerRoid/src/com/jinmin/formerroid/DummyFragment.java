package com.jinmin.formerroid;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DummyFragment extends Fragment
{
	public static final String ARG_SECTION_NUMBER = "section_number";

	public DummyFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		TextView textView = new TextView(getActivity());

		Bundle args = getArguments();
		textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
		return textView;
	}

}
