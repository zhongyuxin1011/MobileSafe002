package com.zyx1011.mobilesafe002.receiver;

import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.service.AddressService;
import com.zyx1011.mobilesafe002.service.InterceptService;
import com.zyx1011.mobilesafe002.service.ProtectService;
import com.zyx1011.mobilesafe002.service.WatchDogService1;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;
import com.zyx1011.mobilesafe002.utils.ServiceStatusUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootReceiver extends BroadcastReceiver {

	private TelephonyManager mTm;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// 手机防盗
			if (PreferenceUtils.getBoolean(context, Constants.PROTECT_PHONE.FLAG, false)) {
				mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				String serialNumber = mTm.getSimSerialNumber();
				if (!PreferenceUtils.getString(context, Constants.PROTECT_PHONE.SIM_SERIAL_NUMBER)
						.equals(serialNumber)) {
					SmsManager.getDefault().sendTextMessage(
							PreferenceUtils.getString(context, Constants.PROTECT_PHONE.SAFE_PHONE), null,
							"Attention:Your mobile phone is change SIM card!--from MobileSafe002", null, null);
				}
			}

			// 骚扰拦截
			bootService(context, Constants.MAIN_MENU.INTERCEPT_HARRY, InterceptService.class);

			// 来电归属地显示
			bootService(context, Constants.MAIN_MENU.PHONE_AREA, AddressService.class);

			// 状态栏显示
			bootService(context, Constants.PROTECT_SERVICE, ProtectService.class);
			
			// 电子狗服务1
			bootService(context, Constants.WATCH_DOG1, WatchDogService1.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据文件中存储的设置信息启动服务
	 * 
	 * @param context
	 * @param key
	 * @param clazz
	 */
	private void bootService(Context context, String key, Class<? extends Service> clazz) {
		Intent service = new Intent(context, clazz);
		boolean runningService = ServiceStatusUtils.isRunningService(context, clazz);
		if (PreferenceUtils.getBoolean(context, key)) {
			if (!runningService) {
				context.startService(service);
			}
		} else {
			if (runningService) {
				context.stopService(service);
			}
		}
	}

}
