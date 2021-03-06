package com.jinmin.formerroid;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jinmin.formerroid.adapters.ContactListArrayAdapter;
import com.jinmin.formerroid.dao.ContactService;
import com.jinmin.formerroid.model.StoredContact;

public class ContactPageFragment extends Fragment
{

	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final int CONTACT_REQUEST_CODE = 99;
	public static final int EDIT_TEXT_CONTACT_NAME = 1;
	public static final int EDIT_TEXT_CONTACT_NUMBER = 2;

	public static final String POSITION = "position";
	private Context _context;
	private ContactListArrayAdapter arrayAdapter;
	ContactService storedContactService;
	private Dialog regContactFormDialog;

	static ProgressDialog progressDialogLoading;

	private Handler loadingListViewHandler;

	public ContactPageFragment(Context _context)
	{
		this._context = _context;
	}

	public static Fragment newInstance(Context _context, int position)
	{
		Fragment f = new ContactPageFragment(_context);
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d("onCreateView", "called");
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.contact_layout, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// if (savedInstanceState != null) {
		// if (savedInstanceState.containsKey(AppConstants.SAVE_INSTANCE_STATE_CONTACT_SERVICE_KEY)) {
		// storedContactService = (ContactService)savedInstanceState.get(AppConstants.SAVE_INSTANCE_STATE_CONTACT_SERVICE_KEY);
		// }
		// if (savedInstanceState.containsKey("arrayAdapter")) {
		// ((ListView)getActivity().findViewById(R.id.listView)).setAdapter((ContactListArrayAdapter)savedInstanceState.get("arrayAdapter"));
		// }
		// }
		// else {
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
				displayListView();
			}
		});

		// }

		refreshListView();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Log.d("@", " onResume called");
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.d("@", "onPause called");
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		Log.d("@", "onDestroyView called");
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		Log.d("@", "onSaveInstanceState called");
	}

	@Override
	public void onDestroy()
	{
		Log.d("@", "onDestroy called");
		super.onDestroy();
	}

	public List<StoredContact> getListViewData()
	{
		if (storedContactService == null)
			storedContactService = new ContactService(_context);
		return storedContactService.getStoredContactList();
	}

	List<StoredContact> list = null;

	public void displayListView()
	{
		list = storedContactService.getStoredContactList();
		// set arrayAdapter
		arrayAdapter = new ContactListArrayAdapter(_context, R.layout.contact_listview_row, list);
		ListView listView = (ListView)getActivity().findViewById(R.id.listView);
		listView.setAdapter(arrayAdapter);
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				final int selectedPosition = position;
				StoredContact storedContact = (StoredContact)parent.getAdapter().getItem(selectedPosition);

				AlertDialog.Builder confirmDeleteAlert = new AlertDialog.Builder(parent.getContext()).setMessage("[ " + storedContact.getName() + " ]을 삭제하시겠습니까?").setPositiveButton("삭제", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						StoredContact item = arrayAdapter.getItem(selectedPosition);
						if (item != null) {
							if (storedContactService.removeContact(item.getId())) {
								arrayAdapter.remove(item);
								Toast.makeText(_context, "삭제 성공", Toast.LENGTH_SHORT).show();
							}
							else {
								Toast.makeText(_context, "삭제 실패", Toast.LENGTH_SHORT).show();
							}

						}
					}
				}).setNegativeButton("취소", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				confirmDeleteAlert.show();
			}

		});
		// }

		// 등록 버튼
		Button registBtn = (Button)getView().findViewById(R.id.insertBtn);
		registBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (regContactFormDialog == null)
					regContactFormDialog = createRegistDialog();

				regContactFormDialog.show();
			}
		});
		// loadingListViewHandler.post(new Runnable()
		// {
		// @Override
		// public void run()
		// {
		// if (progressDialogLoading != null && progressDialogLoading.isShowing()) {
		// progressDialogLoading.dismiss();
		// }
		// }
		// });

	}

	private void setProgressDialogLoading()
	{
		if (progressDialogLoading == null) {
			progressDialogLoading = new ProgressDialog(getActivity());
			progressDialogLoading.setMessage("데이터 로딩중..");
			progressDialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
	}

	private AlertDialog createRegistDialog()
	{

		final View innerView = getLayoutInflater(getArguments()).inflate(R.layout.contact_add_layout, null);

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
				EditText contactNameEt = getEditText(innerView, EDIT_TEXT_CONTACT_NAME);
				EditText contactNumberEt = getEditText(innerView, EDIT_TEXT_CONTACT_NUMBER);

				String name = contactNameEt.getText().toString();
				String tel = contactNumberEt.getText().toString().replace("-", "");

				if (name.trim().length() == 0) {
					Toast.makeText(innerView.getContext(), "이름을 확인해주세요.", Toast.LENGTH_SHORT).show();
				}
				else if (tel.trim().length() == 0 || (tel.trim().length() < 9)) {
					Toast.makeText(innerView.getContext(), "전화번호를 확인해주세요.(지역번호 포함)", Toast.LENGTH_SHORT).show();
				}
				else {
					int duplicated = storedContactService.checkDuplicatedStoredContact(name, tel);
					if (duplicated > 0) {
						Toast.makeText(_context, "이미 존재하는 데이터입니다.", Toast.LENGTH_SHORT).show();
						clearRegistFormDialog(innerView);
					}
					else {
						RadioGroup radioGroup = ((RadioGroup)innerView.findViewById(R.id.ring_mode));
						RadioButton radioButton = (RadioButton)radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
						int ring_mode = radioGroup.indexOfChild(radioButton);
						int ring_mode_option = 0;

						StoredContact storedContact = new StoredContact(name, tel, ring_mode, ring_mode_option);
						if (storedContactService.insertContact(storedContact)) {
							arrayAdapter.add(storedContact);
							refreshListView();
							Toast.makeText(_context, "등록 성공.", Toast.LENGTH_SHORT).show();
							clearRegistFormDialog(innerView);
						}
						else {
							Toast.makeText(_context, "등록 실패", Toast.LENGTH_SHORT).show();
						}
					}
				}

			}
		});

		Button contactBtn = (Button)innerView.findViewById(R.id.searchBtn);
		contactBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
				getActivity().startActivityForResult(intent, CONTACT_REQUEST_CODE);
			}
		});
		return dialogBuilder.create();

	}

	private EditText getEditText(View innerView, int target)
	{

		if (innerView == null)
			return null;

		EditText editText = null;
		switch (target) {
			case EDIT_TEXT_CONTACT_NAME:
				editText = (EditText)innerView.findViewById(R.id.contactName);
				break;
			case EDIT_TEXT_CONTACT_NUMBER:
				editText = (EditText)innerView.findViewById(R.id.contactNumber);
				break;
		}

		return editText;
	}

	private void clearRegistFormDialog(View innerView)
	{
		getEditText(innerView, EDIT_TEXT_CONTACT_NAME).setText("");
		getEditText(innerView, EDIT_TEXT_CONTACT_NUMBER).setText("");
		((RadioButton)innerView.findViewById(R.id.ring_mode_0)).setChecked(true);
	}

	public void setContactData(String _name, String _phoneNumber)
	{

		if (regContactFormDialog != null) {
			if (_name != null) {
				((EditText)regContactFormDialog.findViewById(R.id.contactName)).setText(_name);
			}
			if (_phoneNumber != null) {
				((EditText)regContactFormDialog.findViewById(R.id.contactNumber)).setText(_phoneNumber.replace("-", ""));
			}
		}
	}

	public void refreshListView()
	{
		if (arrayAdapter != null) {
			arrayAdapter.notifyDataSetChanged();
		}

	}

	public void addArrayAdapter(StoredContact storedContact)
	{
		arrayAdapter.add(storedContact);
	}

}