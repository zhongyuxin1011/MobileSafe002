package com.zyx1011.mobilesafe002;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zyx1011.mobilesafe002.entity.CacheInfo;
import com.zyx1011.mobilesafe002.view.ProgressDes;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_cacheclean)
public class CacheCleanActivity extends Activity {

	@ViewInject(R.id.rl_cacheclaen_scan)
	private View mScan;

	@ViewInject(R.id.fl_cacheclean_result)
	private View mResult;

	@ViewInject(R.id.iv_cacheclean_icon)
	private ImageView mIvIcon;

	@ViewInject(R.id.iv_cacheclean_line)
	private ImageView mIvLine;

	@ViewInject(R.id.tv_cacheclean_name)
	private TextView mTvName;

	@ViewInject(R.id.tv_cacheclean_packagename)
	private TextView mTvPackage;

	@ViewInject(R.id.tv_cacheclean_result)
	private TextView mTvResult;

	@ViewInject(R.id.pd_cacheclean_progress)
	private ProgressDes mPb;

	@ViewInject(R.id.lv_cacheclean_info)
	private ListView mLvInfos;

	@ViewInject(R.id.btn_cacheclean_reset)
	private Button mBtnReset;

	@ViewInject(R.id.btn_cacheclean_clean)
	private Button mBtnClean;

	private PackageManager mPm;
	private boolean mIsRun;
	private List<CacheInfo> mInfos;
	private CacheAdapter mAdapter;
	private int cacheCount;
	private long cacheSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		mPb.enableType(false);
		mPm = getPackageManager();

		mInfos = new ArrayList<>();
		mAdapter = new CacheAdapter();
		mLvInfos.setAdapter(mAdapter);

		initData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mIsRun = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mIsRun) {
			mIsRun = false;
			initData(); // 获得焦点重新扫描
		}
	}

	private void initData() {
		new AsyncTask<Void, CacheInfo, Void>() {

			private int mMax;
			private int mProgress;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mResult.setVisibility(View.GONE);
				mScan.setVisibility(View.VISIBLE);
				mBtnClean.setVisibility(View.GONE);
				mBtnReset.setEnabled(false);

				mInfos.clear(); // 清空集合
				cacheCount = 0; // 计数器清零
				cacheSize = 0;

				setAnim();
			}

			@Override
			protected Void doInBackground(Void... params) {

				List<PackageInfo> packages = mPm.getInstalledPackages(0);
				if (packages != null) {
					mMax = packages.size();

					for (PackageInfo packageInfo : packages) {
						if (mIsRun) {
							continue; // 失去焦点后不执行之后的代码
						}

						final CacheInfo info = new CacheInfo();
						info.packageName = packageInfo.packageName;
						info.name = packageInfo.applicationInfo.loadLabel(mPm).toString();
						info.icon = packageInfo.applicationInfo.loadIcon(mPm);

						Class<? extends PackageManager> cls = mPm.getClass();
						try {
							Method method = cls.getMethod("getPackageSizeInfo", String.class,
									IPackageStatsObserver.class);
							final IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {

								@Override
								public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
										throws RemoteException {
									info.cacheSize = pStats.cacheSize;
								}

							};
							method.invoke(mPm, info.packageName, mStatsObserver);
						} catch (Exception e) {
							e.printStackTrace();
						}
						mProgress++;
						publishProgress(info);

						SystemClock.sleep(100);
					}

				}
				return null;
			}

			@Override
			protected void onProgressUpdate(CacheInfo... values) {
				mPb.setProgress(mProgress, mMax);

				super.onProgressUpdate(values);
				CacheInfo info = values[0];
				mInfos.add(info);

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
					mLvInfos.smoothScrollToPosition(mAdapter.getCount());
				}

				if (info.cacheSize > 0) {
					cacheCount++;
					cacheSize += info.cacheSize;
				}

				mIvIcon.setImageDrawable(info.icon);
				mTvName.setText(info.name);
				mTvPackage.setText(info.packageName);
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				mResult.setVisibility(View.VISIBLE);
				mScan.setVisibility(View.GONE);
				if (cacheCount > 0) { // 有缓存才需要"一键清理"
					mBtnClean.setVisibility(View.VISIBLE);
				}
				mBtnReset.setEnabled(true);

				mTvResult.setText(
						"缓存:" + cacheCount + "个, 共" + Formatter.formatFileSize(getApplicationContext(), cacheSize));
			}
		}.execute();
	}

	/**
	 * 设置扫描的动画效果
	 */
	private void setAnim() {
		TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 0, TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 1);
		animation.setDuration(800);
		animation.setRepeatCount(TranslateAnimation.INFINITE);
		animation.setRepeatMode(TranslateAnimation.REVERSE);
		mIvLine.startAnimation(animation);
	}

	/**
	 * 快速扫面按钮点击响应事件
	 * 
	 * @param v
	 */
	public void rescanClick(View v) {
		initData();
	}

	/**
	 * 一键清理按钮点击响应事件
	 * 
	 * @param v
	 */
	public void clsClick(View v) {
		Class<? extends PackageManager> cls = mPm.getClass();
		try {
			IPackageDataObserver.Stub observer = new IPackageDataObserver.Stub() {

				@Override
				public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
					runOnUiThread(new Runnable() { // 不管成功与否,都提示成功并重扫
						public void run() {
							Toast.makeText(getApplicationContext(), "清理成功!", Toast.LENGTH_SHORT).show();
							initData(); // 重新加载扫描方法
						}
					});
				}

			};
			Method method = cls.getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
			method.invoke(mPm, Long.MAX_VALUE, observer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ListView适配器
	 * 
	 * @author zhongyuxin
	 */
	private class CacheAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mInfos.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			CacheInfo info = mInfos.get(position);

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = View.inflate(getApplicationContext(), R.layout.listview_cacheclean_item, null);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_cacheclean_item_icon);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_cacheclean_item_name);
				holder.tvCache = (TextView) convertView.findViewById(R.id.tv_cacheclean_item_cache);
				holder.ivClean = (ImageView) convertView.findViewById(R.id.iv_cacheclean_item_clean);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.ivIcon.setImageDrawable(info.icon);
			holder.tvName.setText(info.name);
			holder.tvCache.setText("缓存:" + Formatter.formatFileSize(getApplicationContext(), info.cacheSize));

			if (info.cacheSize > 0) {
				holder.tvCache.setTextColor(Color.RED); // 有缓存显示红色字体
				holder.ivClean.setVisibility(View.VISIBLE);
				holder.ivClean.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) { // 跳转自行清理
						Intent intent = new Intent();
						intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						intent.setData(Uri.parse("package:" + mInfos.get(position).packageName));
						startActivity(intent); // 隐式意图
					}
				});
			} else {
				holder.tvCache.setTextColor(Color.GREEN); // 无缓存显示绿色字体
				holder.ivClean.setVisibility(View.GONE);
			}

			return convertView;
		}

	}

	private class ViewHolder {
		ImageView ivIcon;
		TextView tvName;
		TextView tvCache;
		ImageView ivClean;
	}

}
