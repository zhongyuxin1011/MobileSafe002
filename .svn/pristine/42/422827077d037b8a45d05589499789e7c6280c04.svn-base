package com.zyx1011.mobilesafe002;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.db.MDbDao;
import com.zyx1011.mobilesafe002.entity.InterceptInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_addintercept)
public class AddInerceptActivity extends Activity {

	@ViewInject(R.id.et_addintercept_phone)
	private EditText mEtPhone;

	@ViewInject(R.id.rg_addintercept_type)
	private RadioGroup mRgType;

	@ViewInject(R.id.rb_addintercept_call)
	private RadioButton mRbCall;

	@ViewInject(R.id.rb_addintercept_message)
	private RadioButton mRbMessage;

	@ViewInject(R.id.rb_addintercept_all)
	private RadioButton mRbAll;

	@ViewInject(R.id.tv_addintercept_selectnum)
	private TextView mTvSelect;

	@ViewInject(R.id.tv_addintercept_title)
	private TextView mTvTilte;

	@ViewInject(R.id.btn_addinercept_ok)
	private Button mBtnOk;

	private MDbDao mDao;
	private InterceptInfo mInfo;
	private Bundle mBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initData();

		initView();
	}

	/**
	 * 初始化:是新增还是更新
	 */
	private void initView() {
		Intent intent = getIntent();
		if (intent != null) {
			mBundle = intent.getExtras();
			if (mBundle != null) {
				mTvSelect.setVisibility(View.GONE);
				mTvTilte.setText("更新黑名单");
				mBtnOk.setText("更新");

				mInfo = (InterceptInfo) mBundle.getSerializable("INFO");

				String phone = mInfo.getPhone();
				mEtPhone.setText(phone);
				mEtPhone.setEnabled(false);

				int type = mInfo.getType();
				switch (type) {
				case 0:
					mRbCall.setChecked(true);
					break;

				case 1:
					mRbMessage.setChecked(true);
					break;

				case 2:
					mRbAll.setChecked(true);
					break;

				default:
					break;
				}
			}
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mDao = new MDbDao(this);
	}

	@OnClick(R.id.tv_addintercept_selectnum)
	public void numClick(View v) {
		Intent intent = new Intent(this, SelectPhoneActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				if (data != null) { // 在选择联系人界面可能按back返回而导致data为null
					// 因为是复用手机防盗选择安全手机的界面,所以key名未改
					String number = data.getStringExtra(Constants.PROTECT_PHONE.TELEPHONE);
					mEtPhone.setText(number);
					mEtPhone.setSelection(number.length());
				}
			}
			break;

		default:
			break;
		}
	}

	@OnClick(R.id.btn_addinercept_ok)
	public void saveClick(View v) {
		String phone = mEtPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "电话号码不能为空!", Toast.LENGTH_SHORT).show();
		} else {
			int type = getType();
			if (type != -1) {
				if (mDao.isExists(phone)) { // 更新
					if (mBundle != null) {
						if (mInfo.getType() != type) { // 未 更改类型不需要更新数据库
							if (mDao.modify(phone, type)) {
								setResult(RESULT_OK);
								Toast.makeText(this, "更新成功 !", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(this, "更新失败 !", Toast.LENGTH_SHORT).show();
							}
						}
					} else {
						Toast.makeText(this, "该电话号码已存在,请重新输入!", Toast.LENGTH_SHORT).show();
						return;
					}
				} else {
					if (mDao.add(phone, type)) { // 新增
						setResult(RESULT_OK);
						Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "保存失败!", Toast.LENGTH_SHORT).show();
					}
				}
			}
			this.finish();
		}
	}

	/**
	 * 获取拦截类型
	 * 
	 * @return
	 */
	private int getType() {
		int count = mRgType.getChildCount();
		for (int i = 0; i < count; i++) {
			RadioButton rb = (RadioButton) mRgType.getChildAt(i);
			if (rb.isChecked()) {
				switch (rb.getId()) {
				case R.id.rb_addintercept_call:
					return 0;

				case R.id.rb_addintercept_message:
					return 1;

				case R.id.rb_addintercept_all:
					return 2;

				default:
					break;
				}
			}
		}
		return -1;
	}

	@OnClick(R.id.btn_addintercept_cancel)
	public void offClick(View v) {
		finish(); // 关闭Activity
	}

}
