package com.zyx1011.mobilesafe002;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_protect_set3)
public class ProtectActivity3 extends BaseProtectActivity {

	@ViewInject(R.id.et_protect_set3_safenum)
	private EditText mEtSafeNum;

	@ViewInject(R.id.tv_protect_set3_selectnum)
	private TextView mTvSelectNum;

	private String mSafeNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		mSafeNum = PreferenceUtils.getString(this, Constants.PROTECT_PHONE.SAFE_PHONE);
		initData();
	}

	/**
	 * 初始化显示数据
	 */
	private void initData() {
		mEtSafeNum.setText(mSafeNum); // 初始化显示安全号码
		mEtSafeNum.setSelection(mSafeNum.length()); // 移动光标
	}

	@Override
	public boolean performPre() {
		Intent intent = new Intent(this, ProtectActivity2.class);
		startActivity(intent);
		return false;
	}

	@Override
	public boolean performNext() {
		String num = mEtSafeNum.getText().toString().trim();
		if (!TextUtils.isEmpty(num)) {
			PreferenceUtils.putString(this, Constants.PROTECT_PHONE.SAFE_PHONE, num);
			Intent intent = new Intent(this, ProtectActivity4.class);
			startActivity(intent);
		} else {
			Toast.makeText(this, "安全手机号码不能为空!", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	@OnClick(R.id.tv_protect_set3_selectnum)
	public void selectClick(View v) {
		Intent intent = new Intent(ProtectActivity3.this, SelectPhoneActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				if (data != null) { // 在选择联系人界面可能按back返回而导致data为null
					String number = data.getStringExtra(Constants.PROTECT_PHONE.TELEPHONE);
					mEtSafeNum.setText(number);
					mEtSafeNum.setSelection(number.length());
				}
			}
			break;

		default:
			break;
		}
	}

}
