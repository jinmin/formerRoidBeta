package com.jinmin.formerroid.adapters;

import java.util.List;

import android.content.Context;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinmin.formerroid.R;
import com.jinmin.formerroid.model.StoredContact;

public class StoredContactListArrayAdapter extends ArrayAdapter<StoredContact>
{

	private Context context;
	List<StoredContact> storedContactList;

	public StoredContactListArrayAdapter(Context context, int textViewResourceId, List<StoredContact> list)
	{
		super(context, textViewResourceId, list);
		this.context = context;
		this.storedContactList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// super.getView(position, convertView, parent);
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_row, parent, false);
		}
		StoredContact item = getItem(position);

		((TextView)view.findViewById(R.id.txtv_name)).setText(item.getName());
		((TextView)view.findViewById(R.id.txtv_tel)).setText("( " + item.getTel() + " )");

		ImageView ringModeImgv = (ImageView)view.findViewById(R.id.img_ring_mode);

		switch (item.getRing_mode()) {
			case AudioManager.RINGER_MODE_SILENT:
				ringModeImgv.setImageResource(R.drawable.mode_0_red);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				ringModeImgv.setImageResource(R.drawable.mode_1_green);
				break;
			case AudioManager.RINGER_MODE_NORMAL:
				ringModeImgv.setImageResource(R.drawable.mode_2_blue);
				break;
		}
		return view;
	}

	@Override
	public int getCount()
	{
		return super.getCount();
	}

	@Override
	public StoredContact getItem(int position)
	{
		return super.getItem(position);
	}

	@Override
	public void insert(StoredContact object, int index)
	{
		super.insert(object, index);
	}

	@Override
	public void notifyDataSetChanged()
	{
		super.notifyDataSetChanged();
	}

	@Override
	public void remove(StoredContact object)
	{
		super.remove(object);
	}

}
