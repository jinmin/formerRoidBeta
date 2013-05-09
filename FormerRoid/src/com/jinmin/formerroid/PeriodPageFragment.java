package com.jinmin.formerroid;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;
import android.widget.Toast;

import com.jinmin.formerroid.dao.ContactService;
import com.jinmin.formerroid.model.StoredContact;

public class PeriodPageFragment extends Fragment
{

	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final int CONTACT_REQUEST_CODE = 99;
	public static final String POSITION = "position";

	private Context _context;
	private Handler loadingListViewHandler;

	ContactService storedContactService;
	SharedPreferences pref;
	List<StoredContact> list = null;

	public PeriodPageFragment(Context _context)
	{
		this._context = _context;
	}

	public static Fragment newInstance(Context _context, int position)
	{
		Fragment f = new PeriodPageFragment(_context);
		f.setRetainInstance(true);
		Bundle bundle = new Bundle();
		bundle.putInt(POSITION, position);
		f.setArguments(bundle);

		return f;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (storedContactService == null) {
			storedContactService = new ContactService(getActivity().getApplicationContext());
		}

		pref = ((FormerRoidActivity)getActivity()).getPref();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d("onCreateView", "called");
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.period_layout, container, false);

		// TimePicker startTimePicker = (TimePicker)view.findViewById(R.id.startTimePicker);
		// TimePicker finishTimePicker = (TimePicker)view.findViewById(R.id.finishTimePicker);
		// startTimePicker.setIs24HourView(true);
		// finishTimePicker.setIs24HourView(true);

		// add switch event
		Switch onOffSwitch = (Switch)view.findViewById(R.id.periodOnOffSwitch);
		onOffSwitch.setChecked(pref.getBoolean(AppConstants.PERIOD_ON_OFF_KEY, false));
		onOffSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				SharedPreferences.Editor sharedPrefEditor = pref.edit();
				sharedPrefEditor.putBoolean(AppConstants.PERIOD_ON_OFF_KEY, isChecked);
				sharedPrefEditor.commit();
				Toast.makeText(_context, getResources().getString(isChecked ? R.string.info_on_period : R.string.info_off_period), Toast.LENGTH_SHORT).show();
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("storedContactService")) {
				storedContactService = (ContactService)savedInstanceState.get("storedContactService");
			}
		}
		else {
			if (storedContactService == null) {
				storedContactService = new ContactService(getActivity().getApplicationContext());
			}

			loadingListViewHandler = new Handler();
			loadingListViewHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					// setProgressDialogLoading();
					// progressDialogLoading.show();
					displayView();
				}
			});

		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Log.d("PeriodPageFragment", " onResume called");
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.d("PeriodPageFragment", "onPause called");
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		Log.d("PeriodPageFragment", "onDestroyView called");
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.d("PeriodPageFragment", "onSaveInstanceState called");
	}

	@Override
	public void onDestroy()
	{
		Log.d("PeriodPageFragment", "onDestroy called");
		super.onDestroy();
	}

	public List<StoredContact> getListViewData()
	{
		if (storedContactService == null)
			storedContactService = new ContactService(_context);
		return storedContactService.getStoredContactList();
	}

	private void clearRegistFormDialog(View innerView)
	{
	}

	private AlertDialog createPeriodAddDialog()
	{

		final View innerView = getLayoutInflater(getArguments()).inflate(R.layout.period_add_layout, null);

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_context);// new AlertDialog.Builder(_context);
		// CustomAlertDialogBuilder dialogBuilder = new CustomAlertDialogBuilder(_context);// new AlertDialog.Builder(_context);
		dialogBuilder.setView(innerView);
		dialogBuilder.setTitle(R.string.insert_btn);
		dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				clearRegistFormDialog(innerView);
				dialog.dismiss();// 닫기
			}
		});
		dialogBuilder.setPositiveButton(R.string.insert_btn, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				EditText periodTitle = (EditText)innerView.findViewById(R.id.periodTitle);

				String title = periodTitle.getText().toString();
			}
		});

		return dialogBuilder.create();
	}

	public void displayView()
	{
		displayChart();
	}

	private GraphicalView chartView;

	private void displayChart()
	{

		// Pie차트 그리기
		LinearLayout incomePiechartLayout = (LinearLayout)getActivity().findViewById(R.id.chartLayout);

		// sereise
		double[] values = new double[]{ 12, 14, 11, 10, 19 };
		CategorySeries series = new CategorySeries("!1");
		for (double value: values) {
			series.add("Project " + 1, value);
		}

		// render
		int[] colors = new int[]{ Color.BLUE, Color.GREEN, Color.RED, Color.BLACK, Color.CYAN };
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(45);
		renderer.setLegendTextSize(25);
		renderer.setMargins(new int[]{ 20, 30, 15, 0 });
		for (int color: colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			r.setChartValuesTextSize(25);
			r.setGradientEnabled(true);
			r.setStroke(BasicStroke.SOLID);
			renderer.addSeriesRenderer(r);
		}

		renderer.setAxesColor(Color.BLACK);
		renderer.setZoomButtonsVisible(false);
		renderer.setZoomEnabled(false);
		renderer.setChartTitleTextSize(40);
		chartView = ChartFactory.getPieChartView(_context, series, renderer);
		chartView.setFitsSystemWindows(true);

		// mChartView.setBackgroundColor(Color.WHITE);
		incomePiechartLayout.addView(chartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}
}