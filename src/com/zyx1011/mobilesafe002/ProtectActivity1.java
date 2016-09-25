package com.zyx1011.mobilesafe002;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;

import android.content.Intent;
import android.os.Bundle;

@ContentView(R.layout.activity_protect_set1)
public class ProtectActivity1 extends BaseProtectActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
	}

	@Override
	public boolean performPre() {
		return false;
	}

	@Override
	public boolean performNext() {
		Intent intent = new Intent(this, ProtectActivity2.class);
		startActivity(intent);
		return false;
	}

}
