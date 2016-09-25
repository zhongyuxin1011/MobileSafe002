package com.zyx1011.mobilesafe002;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.engine.ProcessProvider;
import com.zyx1011.mobilesafe002.entity.ProcessInfo;
import com.zyx1011.mobilesafe002.service.LockScreenService;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;
import com.zyx1011.mobilesafe002.utils.ScreenUtils;
import com.zyx1011.mobilesafe002.utils.ServiceStatusUtils;
import com.zyx1011.mobilesafe002.view.ProgressDes;
import com.zyx1011.mobilesafe002.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

@SuppressWarnings({ "deprecation" })
@ContentView(R.layout.activity_processmanager)
public class ProcessManagerActivity extends Activity {

	@ViewInject(R.id.line_processmanager_btn)
	private View mVBtn;

	@ViewInject(R.id.pd_processmanager_process)
	private ProgressDes mPdProcess;

	@ViewInject(R.id.pd_processmanager_data)
	private ProgressDes mPdData;

	@ViewInject(R.id.lv_processmanager_info)
	private StickyListHeadersListView mLvInfo;

	@ViewInject(R.id.pb_processmanager_loading)
	private LinearLayout mLoading;

	@ViewInject(R.id.iv_processmanager_clean)
	private ImageView mIvClean;

	@ViewInject(R.id.sd_processmanager_slid)
	private SlidingDrawer mSdSetup;

	@ViewInject(R.id.iv_processmanager_sd_1)
	private ImageView mIvSd1;

	@ViewInject(R.id.siv_processmanager_showsystem)
	private SettingItemView mSivSystem;

	@ViewInject(R.id.siv_processmanager_lockscreen)
	private SettingItemView mSivLock;

	@ViewInject(R.id.iv_processmanager_sd_2)
	private ImageView mIvSd2;

	private List<ProcessInfo> mInfos;
	private List<ProcessInfo> mUserInfos;
	private List<ProcessInfo> mSystemInfos;
	private ProcessAdapter mAdapter;
	private LayoutInflater mInflater;
	private boolean mShowSystemProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		// 防止抽屉触摸事件被ListView获取
		findViewById(R.id.sd_processmanager_content).setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		mLvInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ProcessInfo info = mInfos.get(position);
				info.setChecked(!info.isChecked());

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
		});

		mSdSetup.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				mVBtn.setVisibility(View.GONE); // 打开抽屉,让底部的两个按钮消失
				mIvSd1.setImageResource(R.drawable.drawer_arrow_down);
				mIvSd2.setImageResource(R.drawable.drawer_arrow_down);
			}
		});

		mSdSetup.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() { // 关闭抽屉,让底部按钮出现
				mVBtn.setVisibility(View.VISIBLE);
				mIvSd1.setImageResource(R.drawable.drawer_arrow_up);
				mIvSd2.setImageResource(R.drawable.drawer_arrow_up);
			}
		});
	}

	private void initData() {
		new Thread(new Runnable() {

			public void run() {
				mInfos = ProcessProvider.getAllRunningProcessInfo(getApplicationContext());

				if (mInfos.size() > 0) {

					for (ProcessInfo processInfo : mInfos) {
						if (processInfo.isSystem()) {
							mSystemInfos.add(processInfo);
						} else {
							mUserInfos.add(processInfo);
						}
					}

					runOnUiThread(new Runnable() {

						public void run() {
							if (mAdapter == null) {
								mAdapter = new ProcessAdapter();
								mLvInfo.setAdapter(mAdapter);
							}

							mLvInfo.setVisibility(View.VISIBLE);
							mLoading.setVisibility(View.GONE);

							// 清空集合中原来的数据,然后按顺序重新添加
							mInfos.clear();
							mInfos.addAll(mUserInfos);
							mInfos.addAll(mSystemInfos);
						}
					});
				}
			}
		}).start();
	}

	private void initView() {
		initAnim();

		mLvInfo.setVisibility(View.INVISIBLE);
		mInflater = LayoutInflater.from(this);
		mSystemInfos = new CopyOnWriteArrayList<>();
		mUserInfos = new CopyOnWriteArrayList<>();

		initHaed();

		// 是否显示系统进程,默认显示
		mShowSystemProcess = PreferenceUtils.getBoolean(getApplicationContext(), Constants.SHOW_SYSTEM);
		mSivSystem.check(mShowSystemProcess);

		// 锁屏清理显示状态
		mSivLock.check(ServiceStatusUtils.isRunningService(this, LockScreenService.class));
	}

	/**
	 * 初始化抽屉把手的动画效果
	 */
	private void initAnim() {
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(600);
		animation.setRepeatCount(AlphaAnimation.INFINITE);
		animation.setRepeatMode(AlphaAnimation.REVERSE);

		AlphaAnimation animation2 = new AlphaAnimation(0, 1);
		animation2.setDuration(600);
		animation2.setRepeatCount(AlphaAnimation.INFINITE);
		animation2.setRepeatMode(AlphaAnimation.REVERSE);

		mIvSd1.startAnimation(animation);
		mIvSd2.startAnimation(animation2);
	}

	/**
	 * 初始化进度条显示数据
	 */
	private void initHaed() {
		mPdProcess.setType("进程");
		int runningProcessCount = ProcessProvider.getRunningProcessCount(this);
		int allProcessCount = ProcessProvider.getAllProcessCount(this);

		mPdProcess.setUsed(runningProcessCount + " running");
		mPdProcess.setFree(allProcessCount + " all");
		mPdProcess.setProgress(runningProcessCount, allProcessCount);

		mPdData.setType("内存");
		long usedMen = ProcessProvider.getUsedMen(this);
		long availMen = ProcessProvider.getAvailMen(this);
		long totalMen = ProcessProvider.getTotalMen(this);

		mPdData.setUsed(Formatter.formatFileSize(this, usedMen) + " used");
		mPdData.setFree(Formatter.formatFileSize(this, availMen) + " avail");
		mPdData.setProgress(usedMen, totalMen);
	}

	private class ProcessAdapter extends BaseAdapter implements StickyListHeadersAdapter {

		@Override
		public int getCount() {
			return mShowSystemProcess ? mInfos.size() : mUserInfos.size();
		}

		@Override
		public Object getItem(int position) {
			if (position < mUserInfos.size()) {
				return mUserInfos.get(position);
			} else {
				return mSystemInfos.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.listview_processmanager_item, parent, false);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_processmanager_item_icon);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_processmanager_item_name);
				holder.tvMem = (TextView) convertView.findViewById(R.id.tv_processmanager_item_mem);
				holder.cbCheck = (CheckBox) convertView.findViewById(R.id.cb_processmanager_item_check);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ProcessInfo info = mInfos.get(position);

			holder.ivIcon.setImageDrawable(info.getIcon());
			holder.tvName.setText(info.getApkName());
			holder.tvMem.setText("used " + Formatter.formatFileSize(getApplicationContext(), info.getUsedMem()));
			holder.cbCheck.setChecked(info.isChecked());

			if (info.getProcessName().equals(getPackageName()) || info.isGodProcess()) {
				holder.cbCheck.setVisibility(View.GONE);
			} else {
				holder.cbCheck.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		private class ViewHolder {
			ImageView ivIcon;
			TextView tvName;
			TextView tvMem;
			CheckBox cbCheck;
		}

		@Override
		public View getHeaderView(int position, View convertView, ViewGroup parent) {
			ProcessInfo info = mInfos.get(position);

			TextView tv = new TextView(getApplicationContext());
			tv.setPadding(ScreenUtils.dp2px(getApplicationContext(), 10), ScreenUtils.dp2px(getApplicationContext(), 5),
					0, ScreenUtils.dp2px(getApplicationContext(), 5));
			tv.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
			tv.setTextColor(Color.rgb(50, 50, 50)); // 系统默认的字体颜色
			if (info.isSystem()) {
				tv.setText("系统进程(" + mSystemInfos.size() + "个)");
			} else {
				tv.setText("用户进程(" + mUserInfos.size() + "个)");
			}
			return tv;
		}

		@Override
		public long getHeaderId(int position) {
			ProcessInfo info = mInfos.get(position);
			return info.isSystem() ? 0 : 1;
		}

		@Override
		public boolean isEnabled(int position) {
			ProcessInfo info = mInfos.get(position);
			if (info.getProcessName().equals(getPackageName()) || info.isGodProcess()) {
				return false;
			}
			return true;
		}
	}

	public void btnClick(View v) {
		List<ProcessInfo> temp = mShowSystemProcess ? mInfos : mUserInfos;

		switch (v.getId()) {
		case R.id.btn_processmanager_checkall:
			if (temp != null) {
				for (ProcessInfo info : temp) {
					if (!info.getProcessName().equals(getPackageName())) {
						info.setChecked(true);
					}
				}

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
			break;

		case R.id.btn_processmanager_reversecheck:

			if (temp != null) {
				for (ProcessInfo info : temp) {
					if (!info.getProcessName().equals(getPackageName())) {
						info.setChecked(!info.isChecked());
					}
				}

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
			break;

		default:
			break;
		}
	}

	@OnClick(R.id.iv_processmanager_clean)
	public void cleanClick(View v) {
		int killCount = 0;
		long cleanMem = 0;

		List<ProcessInfo> temp = mShowSystemProcess ? mInfos : mUserInfos;

		if (temp != null) {
			for (ProcessInfo info : temp) {
				if (!info.getProcessName().equals(getPackageName())) {
					if (info.isChecked() && !info.isGodProcess()) {
						killCount++;
						ProcessProvider.killProcess(getApplicationContext(), info.getProcessName());
						cleanMem += info.getUsedMem();

						temp.remove(info);
						if (info.isSystem()) {
							mSystemInfos.remove(info);
						} else {
							mUserInfos.remove(info);
						}
					}
				}
			}

			initHaed();

			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}

			if (killCount > 0) {
				Toast.makeText(getApplicationContext(),
						"Clean is done!" + killCount + " process is dead, and "
								+ Formatter.formatFileSize(getApplicationContext(), cleanMem) + " memory is free.",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Your mobile is health, hold on and usually clean it!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 抽屉的功能选项点击响应事件
	 * 
	 * @param v
	 */
	public void onSetupClick(View v) {
		switch (v.getId()) {
		case R.id.siv_processmanager_showsystem:
			if (mShowSystemProcess) {
				mShowSystemProcess = false;
				PreferenceUtils.putBoolean(this, Constants.SHOW_SYSTEM, false);
			} else {
				mShowSystemProcess = true;
				PreferenceUtils.putBoolean(this, Constants.SHOW_SYSTEM, true);
			}
			mSivSystem.check(mShowSystemProcess);

			if (mAdapter != null) { // 更新数据显示
				mAdapter.notifyDataSetChanged();
			}
			break;

		case R.id.siv_processmanager_lockscreen:
			Intent service = new Intent(this, LockScreenService.class);
			if (ServiceStatusUtils.isRunningService(this, LockScreenService.class)) {
				stopService(service);
				mSivLock.check(false);
				PreferenceUtils.putBoolean(this, Constants.LOCK_SCREEN_CLEAN, false);
			} else {
				startService(service);
				mSivLock.check(true);
				PreferenceUtils.putBoolean(this, Constants.LOCK_SCREEN_CLEAN, true);
			}
			break;

		default:
			break;
		}
	}

}
