package com.zyx1011.mobilesafe002;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

@ContentView(R.layout.activity_protect_set5)
public class ProtectActivity5 extends BaseProtectActivity {

	@ViewInject(R.id.cb_protect_set5_active)
	private CheckBox mCbAvtive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initData();
	}

	/**
	 * 初始化显示数据
	 */
	private void initData() {
		if (PreferenceUtils.getBoolean(this, Constants.PROTECT_PHONE.FLAG, false)) {
			mCbAvtive.setChecked(true);
		} else {
			mCbAvtive.setChecked(false);
		}
	}

	@Override
	public boolean performPre() {
		Intent intent = new Intent(this, ProtectActivity4.class);
		startActivity(intent);
		return false;
	}

	@Override
	public boolean performNext() {
		if (!mCbAvtive.isChecked()) {
			Toast.makeText(this, "请勾选开启防盗保护!", Toast.LENGTH_SHORT).show();
			return true;
		}
		Toast.makeText(this, "设置成功!", Toast.LENGTH_SHORT).show();
		PreferenceUtils.putBoolean(this, Constants.PROTECT_PHONE.FLAG, true);

		Intent intent = new Intent(this, PhoneProtectActivity.class);
		startActivity(intent);
		return false;
	}

}
