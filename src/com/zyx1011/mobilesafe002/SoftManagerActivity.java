package com.zyx1011.mobilesafe002;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zyx1011.mobilesafe002.engine.SoftwareProvider;
import com.zyx1011.mobilesafe002.entity.SoftwareInfo;
import com.zyx1011.mobilesafe002.utils.ScreenUtils;
import com.zyx1011.mobilesafe002.view.ProgressDes;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_softmanager)
public class SoftManagerActivity extends Activity {

	@ViewInject(R.id.pd_softmanager_data)
	private ProgressDes mPdData;

	@ViewInject(R.id.pd_softmanager_sd)
	private ProgressDes mPdSd;

	@ViewInject(R.id.pb_softmanager_loading)
	private LinearLayout mLoading;

	@ViewInject(R.id.lv_softmanager_info)
	private ListView mLvInfo;

	@ViewInject(R.id.tv_softmanager_head)
	private TextView mTvHead;

	private List<SoftwareInfo> mInfos;
	private List<SoftwareInfo> mUserInfos;
	private List<SoftwareInfo> mSystemInfos;
	private LayoutInflater mInflater;
	private AppAdapter mAdapter;
	private UninstallReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initView();
		initData();
		initEvent();
	}

	/**
	 * 注册监听卸载广播
	 */
	private void initEvent() {
		mReceiver = new UninstallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addDataScheme("package");
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) { // 取消广播监听
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	/**
	 * 设置监听器
	 */
	private void setListener() {
		mLvInfo.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem >= (mUserInfos.size() + 1)) {
					mTvHead.setText("系统程序: " + mSystemInfos.size() + "个");
				} else {
					mTvHead.setText("用户程序: " + mUserInfos.size() + "个");
				}
			}
		});

		mLvInfo.setOnItemClickListener(new OnItemClickListener() {

			private PopupWindow mWindow;
			private SoftwareInfo mInfo;
			private TextView mTvUninstall;
			private TextView mTvOpen;
			private TextView mTvShare;
			private TextView mTvMsg;
			private String mPackageName;

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				View convertView = View.inflate(getApplicationContext(), R.layout.popupwidow_softmanager, null);

				mTvUninstall = (TextView) convertView.findViewById(R.id.tv_popupwindow_uninstall);
				mTvOpen = (TextView) convertView.findViewById(R.id.tv_popupwindow_open);
				mTvShare = (TextView) convertView.findViewById(R.id.tv_popupwindow_share);
				mTvMsg = (TextView) convertView.findViewById(R.id.tv_popupwindow_msg);

				if (position < mUserInfos.size()) { // 用户程序
					mWindow = new PopupWindow(convertView, ScreenUtils.dp2px(getApplicationContext(), 200),
							view.getHeight(), true);

					mInfo = mUserInfos.get(position);

					mTvUninstall.setVisibility(View.VISIBLE);
					mTvShare.setVisibility(View.VISIBLE);
				} else { // 系统程序
					mWindow = new PopupWindow(convertView, ScreenUtils.dp2px(getApplicationContext(), 150),
							view.getHeight(), true); // 两个:110

					// -1:减去TextView占用的
					mInfo = mSystemInfos.get(position - mUserInfos.size() - 1);

					mTvUninstall.setVisibility(View.GONE);
					// mTvShare.setVisibility(View.GONE);
				}

				// 要在抛锚前设置,否则无效
				mWindow.setOutsideTouchable(true);
				mWindow.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

				mWindow.showAsDropDown(view, ScreenUtils.dp2px(getApplicationContext(), 50), -view.getHeight());

				mPackageName = mInfo.getPackageName();

				// 卸载
				mTvUninstall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!mPackageName.equals(getPackageName())) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.DELETE");
							intent.setData(Uri.parse("package:" + mPackageName));
							startActivity(intent);
						} else {
							Toast.makeText(getApplicationContext(), "不允许此操作!", Toast.LENGTH_SHORT).show();
						}
						mWindow.dismiss();
					}
				});

				// 打开
				mTvOpen.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						PackageManager pm = getPackageManager();
						// 不允许打开自身
						if (!getPackageName().equals(mPackageName)) {
							Intent intent = pm.getLaunchIntentForPackage(mPackageName);
							if (intent != null) {
								startActivity(intent);
							} else {
								Toast.makeText(getApplicationContext(), "无法打开该系统应用!", Toast.LENGTH_SHORT).show();
							}
						} else { // 打开本本身,不操作
							Toast.makeText(getApplicationContext(), "不允许此操作!", Toast.LENGTH_SHORT).show();
						}
						mWindow.dismiss();
					}
				});

				// 分享
				mTvShare.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						/*
						 * 短信分享
						 * Intent intent = new Intent(Intent.ACTION_SENDTO,
						 * Uri.parse("smsto:")); intent.putExtra("sms_body",
						 * "黑马手机卫士"); startActivity(intent);
						 */

						try { // APK分享
							PackageInfo packageInfo = getPackageManager().getPackageInfo(mInfo.getPackageName(), 0);
							String sourceDir = packageInfo.applicationInfo.sourceDir;
							File apk = new File(sourceDir);
							Intent shareIntent = new Intent();
							shareIntent.setAction(Intent.ACTION_SEND);
							shareIntent.setType("*/*");
							shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(apk));
							startActivity(shareIntent);
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "分享失败,无法完成!", Toast.LENGTH_SHORT).show();
						}
						mWindow.dismiss();
					}
				});

				// 信息
				mTvMsg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 不能打开自身
						if (!mPackageName.equals(getPackageName())) {
							Intent intent = new Intent();
							intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
							intent.setData(Uri.parse("package:" + mPackageName));
							startActivity(intent);
						} else {
							Toast.makeText(getApplicationContext(), "不允许此操作!", Toast.LENGTH_SHORT).show();
						}
						mWindow.dismiss();
					}
				});
			}
		});
	}

	/**
	 * 获取应用的集合
	 */
	private void doEvent() {
		if (mInfos != null && mInfos.size() > 0) {
			mSystemInfos = new ArrayList<>();
			mUserInfos = new ArrayList<>();

			for (SoftwareInfo info : mInfos) {
				if (info.isSystem()) {
					mSystemInfos.add(info);
				} else {
					mUserInfos.add(info);
				}
			}

			mTvHead.setText("用户程序: " + mUserInfos.size() + "个");
		}
		setListener();
	}

	/**
	 * 子线程获取所有应用的集合
	 */
	private void initData() {
		mInflater = LayoutInflater.from(this);

		new Thread(new Runnable() {

			public void run() {
				mInfos = SoftwareProvider.getAllInfos(getApplicationContext());

				runOnUiThread(new Runnable() { // 主线程更新UI
					public void run() {
						mLoading.setVisibility(View.GONE);
						if (mInfos != null && mInfos.size() > 0) { // 非空判断
							mTvHead.setVisibility(View.VISIBLE);
							mLvInfo.setVisibility(View.VISIBLE);

							doEvent();
						}

						// 设置适配器
						if (mAdapter == null) {
							mAdapter = new AppAdapter();
							mLvInfo.setAdapter(mAdapter);
						}
					}
				});
			}
		}).start();
	}

	/**
	 * 初始化控件显示
	 */
	private void initView() {
		// 控件的可见性
		mTvHead.setVisibility(View.GONE);
		mLoading.setVisibility(View.VISIBLE);
		mLvInfo.setVisibility(View.INVISIBLE);

		mPdData.setType("内存");
		File dataDir = Environment.getDataDirectory();

		long dataTotal = dataDir.getTotalSpace();
		long dataFree = dataDir.getFreeSpace();
		long dataUsed = dataTotal - dataFree;

		mPdData.setUsed(Formatter.formatFileSize(getApplicationContext(), dataUsed) + " used");
		mPdData.setFree(Formatter.formatFileSize(getApplicationContext(), dataFree) + " free");
		mPdData.setProgress(dataUsed, dataTotal);

		// SD卡存在
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			mPdSd.setType("SD");
			File sdDir = Environment.getExternalStorageDirectory();

			long sdTotal = sdDir.getTotalSpace();
			long sdFree = sdDir.getFreeSpace();
			long sdUsed = sdTotal - sdFree;

			mPdSd.setUsed(Formatter.formatFileSize(getApplicationContext(), sdUsed) + " used");
			mPdSd.setFree(Formatter.formatFileSize(getApplicationContext(), sdFree) + " free");
			mPdSd.setProgress(sdUsed, sdTotal);
		} else { // SD卡不存在
			mPdSd.setVisibility(View.GONE);
		}
	}

	/**
	 * 应用适配器,给ListView设置数据
	 * 
	 * @author zhongyuxin
	 */
	private class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mInfos.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return mInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null || convertView instanceof TextView) {
				convertView = mInflater.inflate(R.layout.listview_softmanager_item, parent, false);
				holder = new ViewHolder();

				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_softmanager_item_icon);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_softmanager_item_name);
				holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_softmanager_item_location);
				holder.tvSize = (TextView) convertView.findViewById(R.id.tv_softmanager_item_size);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 用户程序在前,系统程序在后
			if (position < mUserInfos.size()) {
				setValues(position, holder, mUserInfos);
			} else if (position == mUserInfos.size()) {
				TextView tv = new TextView(getApplicationContext());
				tv.setPadding(ScreenUtils.dp2px(getApplicationContext(), 10),
						ScreenUtils.dp2px(getApplicationContext(), 5), 0,
						ScreenUtils.dp2px(getApplicationContext(), 5));
				tv.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
				tv.setTextColor(Color.rgb(50, 50, 50));
				tv.setText("系统程序: " + mSystemInfos.size() + "个");
				return tv;
			} else {
				setValues(position - mUserInfos.size() - 1, holder, mSystemInfos);
			}

			return convertView;
		}

		@Override
		public boolean isEnabled(int position) {
			if (position == mUserInfos.size()) {
				return false;
			}
			return super.isEnabled(position);
		}

	}

	/**
	 * 给控件设置数值
	 * 
	 * @param position
	 * @param holder
	 * @param infos
	 */
	private void setValues(int position, ViewHolder holder, List<SoftwareInfo> infos) {
		holder.ivIcon.setImageDrawable(infos.get(position).getIco());
		holder.tvName.setText(infos.get(position).getName());
		holder.tvLocation.setText(infos.get(position).isInstallSD() ? "SD" : "手机内存");
		holder.tvSize.setText(Formatter.formatFileSize(getApplicationContext(), infos.get(position).getApkSize()));
	}

	private class ViewHolder {
		ImageView ivIcon;
		TextView tvName;
		TextView tvLocation;
		TextView tvSize;
	}

	/**
	 * 卸载监听广播
	 * 
	 * @author zhongyuxin
	 */
	private class UninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String dataString = intent.getDataString();
			String packageName = dataString.split(":")[1];

			Iterator<SoftwareInfo> iterator = mUserInfos.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getPackageName().equals(packageName)) {
					iterator.remove();
				}
			}

			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged(); // 更新ListView
			}
		}

	}

}
