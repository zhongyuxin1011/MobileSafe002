package com.zyx1011.mobilesafe002.receiver;

import java.util.Random;

import com.zyx1011.mobilesafe002.R;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.service.GPSService;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;

import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	private String mAddress;
	private String mBody;

	@Override
	public void onReceive(Context context, Intent intent) {

		// 手机防盗
		if (PreferenceUtils.getBoolean(context, Constants.PROTECT_PHONE.FLAG, false)) {

			DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			ComponentName who = new ComponentName(context, SafeAdminReceiver.class);

			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			if (objs != null) {

				for (Object obj : objs) {
					SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
					mAddress = message.getOriginatingAddress();
					mBody = message.getMessageBody();

					if (mAddress.equals(PreferenceUtils.getString(context, Constants.PROTECT_PHONE.SAFE_PHONE))) {
						if ("#*location*#".equals(mBody)) { // 定位
							abortBroadcast(); // 截断短信
							// 启动定位服务
							Intent location = new Intent(context, GPSService.class);
							context.startService(location);
						} else if ("#*lockscreen*#".equals(mBody)) { // 锁屏
							abortBroadcast();
							if (dpm.isAdminActive(who)) { // 判断设备管理员有没有激活
								Random random = new Random();
								StringBuilder sb = new StringBuilder();
								for (int i = 0; i < 6; i++) {
									sb.append(random.nextInt(10)); // 拼接产生6位0~9的伪随机数新密码
								}

								String key = sb.toString();
								dpm.resetPassword(key, 0); // 设置密码

								// 发送新的锁屏密码给安全手机
								SmsManager.getDefault().sendTextMessage(mAddress, null,
										"Screen is locked, and new password is: " + key, null, null);
							}
							dpm.lockNow();
						} else if ("#*wipedata*#".equals(mBody)) { // 清除数据
							abortBroadcast();
							if (dpm.isAdminActive(who)) {
								dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE); // 清除存储卡内容
								dpm.wipeData(DeviceAdminInfo.USES_POLICY_WIPE_DATA); // 恢复出厂设置
							}
						} else if ("#*music*#".equals(mBody)) { // 播放音乐
							abortBroadcast();

							MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
							player.setVolume(1.0f, 1.0f); // 音量
							player.setLooping(true); // 循环播放
							player.start();
						}
					}
				}
			}
		}
	}

}
