package com.zyx1011.mobilesafe002;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.engine.SoftwareProvider;
import com.zyx1011.mobilesafe002.entity.SoftwareInfo;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_lockscreen)
public class LockScreenActivity extends Activity {

	@ViewInject(R.id.iv_lockscreen_icon)
	private ImageView mIvIcon;

	@ViewInject(R.id.tv_lockscreen_name)
	private TextView mTvName;

	@ViewInject(R.id.et_lockscreen_pass)
	private EditText mEtPass;

	private String mPackageName;
	private SoftwareInfo mInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initData();
		initEvent();
	}

	private void initEvent() {
		if (mInfo != null) {
			mIvIcon.setImageDrawable(mInfo.getIco());
			mTvName.setText(mInfo.getName());
		}
	}

	private void initData() {
		Intent intent = getIntent();
		if (intent != null) {
			mPackageName = intent.getStringExtra("PACKAGE_NAME");
			mInfo = SoftwareProvider.getInfo(getApplicationContext(), mPackageName);
		}
	}

	public void passClick(View v) {
		String pass = mEtPass.getText().toString().trim();
		if (!TextUtils.isEmpty(pass)) {
			if (PreferenceUtils.getString(getApplicationContext(), Constants.PROTECT_PHONE.PASSWORD).equals(pass)) {
				Intent intent = new Intent();
				intent.setAction(Constants.BROADCAST_ACTION);
				intent.putExtra("PACKAGE_NAME", mPackageName);
				sendBroadcast(intent);

				this.finish();
			} else {
				editAnim();
				mEtPass.setText(""); // 清空输入框
				Toast.makeText(getApplicationContext(), "密码错误,请重试!", Toast.LENGTH_SHORT).show();
			}
		} else {
			editAnim();
			Toast.makeText(getApplicationContext(), "密码不能为空!", Toast.LENGTH_SHORT).show();
		}
	}

	private void editAnim() {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		mEtPass.startAnimation(shake);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true; // 自身消费
		}
		return super.onKeyDown(keyCode, event);
	}

}
