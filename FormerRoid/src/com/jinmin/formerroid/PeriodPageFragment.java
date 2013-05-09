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
import android.app.TimePickerDialog;
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
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jinmin.formerroid.dao.PeriodService;
import com.jinmin.formerroid.model.StoredContact;
import com.jinmin.formerroid.model.StoredPeriod;

public class PeriodPageFragment extends Fragment
{

	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final int CONTACT_REQUEST_CODE = 99;
	public static final String POSITION = "position";

	private Context _context;
	private Handler loadingListViewHandler;

	PeriodService periodService;
	SharedPreferences pref;
	List<StoredContact> list = null;

	int SET_TIME_ID = 0;
	int[] times = new int[2];

	View.OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener()
	{
		@Override
		public void onFocusChange(View v, boolean hasFocus)
		{

			if (hasFocus) {

				if (timePickerDialog != null && !timePickerDialog.isShowing()) {
					SET_TIME_ID = ((EditText)v).getId();
					int[] textTimes = getTimes(((EditText)v).getText().toString());
					createTimePickerDialog(textTimes[0], textTimes[1]).show();
				}

			}
		}
	};

	View.OnClickListener onclickTimeEditText = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (timePickerDialog != null && !timePickerDialog.isShowing()) {
				SET_TIME_ID = ((EditText)v).getId();
				int[] textTimes = getTimes(((EditText)v).getText().toString());
				createTimePickerDialog(textTimes[0], textTimes[1]).show();
			}
		}
	};

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
		if (periodService == null) {
			periodService = new PeriodService(getActivity().getApplicationContext());
		}

		pref = ((FormerRoidActivity)getActivity()).getPref();
	}

	EditText periodTitle;
	EditText startTimeEdTxt;
	EditText finishTimeEdTxt;
	RadioGroup radioGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d("onCreateView", "called");
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.period_layout, container, false);

		timePickerDialog = createTimePickerDialog(0, 0);
		periodTitle = (EditText)view.findViewById(R.id.periodTitle);
		startTimeEdTxt = (EditText)view.findViewById(R.id.startPeriod);
		finishTimeEdTxt = (EditText)view.findViewById(R.id.finishPeriod);
		radioGroup = ((RadioGroup)view.findViewById(R.id.period_ring_mode));

		// add insert period event
		Button insertPeriodBtn = (Button)view.findViewById(R.id.insertPeriodBtn);
		insertPeriodBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				RadioButton radioButton = (RadioButton)radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
				int ring_mode = radioGroup.indexOfChild(radioButton);
				int ring_mode_option = 0;

				int[] checkBoxIds = { R.id.sun, R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat };

				String checked = "";
				for (int checkboxId: checkBoxIds) {
					checked += (((CheckBox)getView().findViewById(checkboxId)).isChecked() ? "1" : "0") + AppConstants.SEPARATOR_SYMBOL;
				}

				String[] startTimeArr = startTimeEdTxt.getText().toString().split(":");
				String[] finishTimeArr = finishTimeEdTxt.getText().toString().split(":");

				String startTimeTotal = String.valueOf((Integer.parseInt(startTimeArr[0]) * 60) + Integer.parseInt(startTimeArr[1]));
				String finishTimeTotal = String.valueOf((Integer.parseInt(finishTimeArr[0]) * 60) + Integer.parseInt(finishTimeArr[1]));

				checked = checked.substring(0, checked.length() - 1);
				StoredPeriod storedPeriod = new StoredPeriod(periodTitle.getText().toString(), startTimeTotal, finishTimeTotal, checked, ring_mode, ring_mode_option);
				periodService.insertPeriod(storedPeriod);

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
		});

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

		// add time event
		startTimeEdTxt.setOnFocusChangeListener(onFocusChangeListener);
		finishTimeEdTxt.setOnFocusChangeListener(onFocusChangeListener);
		startTimeEdTxt.setOnClickListener(onclickTimeEditText);
		finishTimeEdTxt.setOnClickListener(onclickTimeEditText);

		return view;
	}

	private int[] getTimes(String txt)
	{
		if (txt != null && txt.length() > 0) {
			times[0] = Integer.parseInt(txt.substring(0, 2));
			times[1] = Integer.parseInt(txt.substring(3, 5));
		}
		else {
			times[0] = 0;
			times[1] = 0;
		}
		return times;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// if (savedInstanceState != null) {
		// if (savedInstanceState.containsKey("storedContactService")) {
		// periodService = (PeriodService)savedInstanceState.get("storedContactService");
		// }
		// }
		// else {
		if (periodService == null) {
			periodService = new PeriodService(getActivity().getApplicationContext());
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
		// }
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

	private void clearRegistFormDialog(View innerView)
	{
	}

	TimePickerDialog timePickerDialog = null;

	private TimePickerDialog createTimePickerDialog(int hour, int minute)
	{
		// if (timePickerDialog == null) {
		TimePickerDialog timePickerDialog = new TimePickerDialog(_context, new TimePickerDialog.OnTimeSetListener()
		{
			@Override
			public void onTimeSet(TimePicker view, int hour, int minute)
			{
				String hourStr = hour > 9 ? String.valueOf(hour) : ("0" + hour);
				String minuteStr = minute > 9 ? String.valueOf(minute) : ("0" + minute);
				((EditText)getActivity().findViewById(SET_TIME_ID)).setText(hourStr + ":" + minuteStr);
			}
		}, hour, minute, true);
		timePickerDialog.setTitle("시간 선택 (24 시간 표기)");

		return timePickerDialog;
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
		displayChart(periodService.getStoredPeriodList());
	}

	private GraphicalView chartView;

	private void displayChart(List<StoredPeriod> periodList)
	{

		if (periodList == null || periodList.size() == 0) {

			// TODO 데이터 없는 표시 처리
			return;
		}

		// Pie차트 그리기
		LinearLayout incomePiechartLayout = (LinearLayout)getActivity().findViewById(R.id.chartLayout);

		CategorySeries series = new CategorySeries("!1");

		int[] colors = new int[]{ Color.BLUE, Color.GREEN, Color.RED, Color.BLACK, Color.CYAN };

		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(45);
		renderer.setLegendTextSize(25);
		renderer.setMargins(new int[]{ 20, 30, 15, 0 });

		SimpleSeriesRenderer r = null;
		int length = periodList.size();
		int total = 0;
		for (int i = 0; i < length; i++) {
			StoredPeriod storedPeriod = periodList.get(i);
			int startTotalMin = Integer.parseInt(storedPeriod.getStartTime());
			int finishTotalMin = Integer.parseInt(storedPeriod.getFinishTime());

			int startHour = Math.abs(startTotalMin / 60);
			int startMin = Math.abs(startTotalMin % 60);

			int finishHour = Math.abs(finishTotalMin / 60);
			int finishMin = finishTotalMin % 60;

			int value = (finishTotalMin - startTotalMin); // 시간 간격
			total += value;
			series.add(storedPeriod.getTitle() + "(" + (startHour + ":" + startMin) + "~" + (finishHour + ":" + finishMin) + ")", value);

			r = new SimpleSeriesRenderer();
			r.setColor(colors[i]);
			r.setChartValuesTextSize(25);
			r.setGradientEnabled(true);
			r.setStroke(BasicStroke.SOLID);
			renderer.addSeriesRenderer(r);

		}
		// 1439
		if (1440 > total) {
			series.add("기본(나머지 시간)", 1439 - total);
			r = new SimpleSeriesRenderer();
			r.setColor(colors[(series.getItemCount() - 1)]);
			r.setChartValuesTextSize(25);
			r.setGradientEnabled(true);
			r.setStroke(BasicStroke.SOLID);
			renderer.addSeriesRenderer(r);
		}

		renderer.setAxesColor(Color.BLACK);
		renderer.setZoomButtonsVisible(false);
		renderer.setZoomEnabled(true);
		renderer.setPanEnabled(false);
		renderer.setChartTitleTextSize(40);
		renderer.setLabelsColor(Color.BLUE);
		renderer.setScale(1.25f);
		chartView = ChartFactory.getPieChartView(_context, series, renderer);
		chartView.setFitsSystemWindows(true);

		chartView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			}
		});

		// mChartView.setBackgroundColor(Color.WHITE);
		incomePiechartLayout.addView(chartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}
}