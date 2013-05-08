package com.jinmin.formerroid.listeners;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.jinmin.formerroid.dao.StoredContactService;
import com.jinmin.formerroid.helpers.SQLHelper;
import com.jinmin.formerroid.model.StoredContact;

public class CopyOfCallStatusListener extends BroadcastReceiver
{
	private AudioManager mAudioManager;

	private SQLHelper sqlHelper;

	public String TAG = getClass().getSimpleName();

	static int currVolumnVal = 0;
	static int currRingMode = -1;

	StoredContactService storedContactService;

	@Override
	public void onReceive(Context context, Intent received)
	{
		String action = received.getAction();
		Bundle bundle = received.getExtras();

		if (storedContactService == null) {
			storedContactService = new StoredContactService(context);
		}

		if (mAudioManager == null) {
			mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		}

		if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			// ComponentName cName = new ComponentName(context.getPackageName(),
			// FormerRoidActivity );
			// ComponentName svcName = context.startService(new
			// Intent().setComponent(cName);
			// if (svc == null) {
			// Log.e(TAG, "Could not start service " + cName.toString());
			// }
		}
		else if (action.equals("android.intent.action.PHONE_STATE")) {
			String state = bundle.getString(TelephonyManager.EXTRA_STATE);

			Toast.makeText(context, state, Toast.LENGTH_SHORT).show();
			if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) { // ringing
				if (bundle != null) {
					String calledNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
					List<StoredContact> list = storedContactService.getStoredContactList();

					currRingMode = mAudioManager.getRingerMode();

					if (currRingMode == AudioManager.RINGER_MODE_NORMAL) {
						// mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
						// mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
					}

					for (StoredContact item: list) {
						if (calledNumber.equals(item.getTel())) {
							currRingMode = mAudioManager.getRingerMode();

							switch (item.getRing_mode()) {
								case AudioManager.RINGER_MODE_NORMAL:
									Toast.makeText(context, "현재 : " + currRingMode + " => 변경 : 링모드", Toast.LENGTH_SHORT).show();
									volumnUP();
									mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

									break;
								case AudioManager.RINGER_MODE_VIBRATE:
									Toast.makeText(context, "현재 : " + currRingMode + " => 변경 : 진동모드", Toast.LENGTH_SHORT).show();
									// mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
									mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
									// mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
									// Vibrator vibe = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
									// vibe.vibrate(700);
									break;
								case AudioManager.RINGER_MODE_SILENT:
									Toast.makeText(context, "현재 : " + currRingMode + " => 변경 : 무음모드", Toast.LENGTH_SHORT).show();
									mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
									mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
									// mAudioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
									break;
							}
							break;
						}
					}

				}
			}
			else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) { // hang up
				if (currRingMode >= 0) {
					// mAudioManager.setRingerMode(currRingMode);
				}
			}
		}
	}

	public void volumnUP()
	{
		if (mAudioManager != null) {
			currVolumnVal = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
			int maxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
			if (currVolumnVal < maxVol) {
				mAudioManager.setStreamVolume(AudioManager.STREAM_RING, maxVol, AudioManager.FLAG_PLAY_SOUND);
			}
		}
	}
}
