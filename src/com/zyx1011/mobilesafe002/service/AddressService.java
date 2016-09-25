package com.zyx1011.mobilesafe002.service;

import com.zyx1011.mobilesafe002.db.AddressDao;
import com.zyx1011.mobilesafe002.view.AddrToast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 来电归属地显示服务
 * 
 * @author zhongyuxin
 */
public class AddressService extends Service {

	private TelephonyManager mTm;
	private AddrListener mListener;
	private AddressDao mDao;
	private AddrToast mToast;
	private OutgoingReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDao = new AddressDao(this);

		mToast = new AddrToast(this);
		mTm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mListener = new AddrListener();
		mTm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE); // 注册监听

		mReceiver = new OutgoingReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mListener != null) { // 取消监听
			mTm.listen(mListener, PhoneStateListener.LISTEN_NONE);
			mListener = null;
		}

		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	/**
	 * 来电归属地监听器
	 * 
	 * @author zhongyuxin
	 */
	private class AddrListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				mToast.hide();
				break;

			case TelephonyManager.CALL_STATE_RINGING:
				String cardType = mDao.getSingelCardType(incomingNumber);
				mToast.show(cardType);
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 去电归属地监听器
	 * 
	 * @author zhongyuxin
	 */
	private class OutgoingReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String phone = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			if (!TextUtils.isEmpty(phone)) {
				String cardType = mDao.getSingelCardType(phone);
				mToast.show(cardType);
			}
		}
	}

}
