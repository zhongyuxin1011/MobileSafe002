package com.zyx1011.mobilesafe002;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zyx1011.mobilesafe002.db.AppLockDao;
import com.zyx1011.mobilesafe002.engine.SoftwareProvider;
import com.zyx1011.mobilesafe002.entity.SoftwareInfo;
import com.zyx1011.mobilesafe002.service.WatchDogService1;
import com.zyx1011.mobilesafe002.service.WatchDogService2;
import com.zyx1011.mobilesafe002.utils.ServiceStatusUtils;
import com.zyx1011.mobilesafe002.view.SegmentView;
import com.zyx1011.mobilesafe002.view.SegmentView.onSelectedListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_applock)
public class AppLockActivity extends Activity {

	@ViewInject(R.id.tv_applock_item)
	private TextView mTvItem;

	@ViewInject(R.id.sv_applock_switch)
	private SegmentView mSvSwitch;

	@ViewInject(R.id.line_applock_loading)
	private View mLoading;

	@ViewInject(R.id.lv_applock_list)
	private ListView mLvInfo;

	private List<SoftwareInfo> mInfos;
	private List<SoftwareInfo> mLockInfo;
	private List<SoftwareInfo> mUnLockInfo;
	private AppLockDao mDao;
	private AppAdapter mAdapter;
	private boolean mIsLock;
	private boolean mAnimFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initView();
		initData();
		initEvent();
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		// ListView的item点击事件
		mLvInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mAnimFlag) {
					return; // 上一次动画未切换完,直接返回
				}

				View tmp = view; // 记录临时变量,以设置动画效果

				if (mIsLock) { // 加锁
					SoftwareInfo info = mLockInfo.get(position);

					mDao.delete(info.getPackageName()); // 从数据库删除记录

					mLockInfo.remove(info);
					mUnLockInfo.add(0, info);

					mTvItem.setText("已加锁" + mLockInfo.size() + "个");
				} else { // 无锁
					SoftwareInfo info = mUnLockInfo.get(position);

					mDao.add(info.getPackageName());

					mUnLockInfo.remove(info);
					mLockInfo.add(0, info);

					mTvItem.setText("未加锁" + mUnLockInfo.size() + "个");
				}

				setAnim(tmp); // 启动动画
			}
		});

		// 分段View的点击事件
		mSvSwitch.setOnSelectedListener(new onSelectedListener() {

			@Override
			public void OnSelected(boolean isLeft) {
				if (mAnimFlag) {
					return; // 上一次动画未加载完不允许重复加载
				}

				mIsLock = !mIsLock;

				// 动画展示效果
				RotateAnimation animation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
						RotateAnimation.RELATIVE_TO_SELF, 0.5f);
				animation.setDuration(500);
				mLvInfo.startAnimation(animation);

				// 动画监听事件
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						mAnimFlag = true;
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						mAnimFlag = false;

						if (mIsLock) {
							mTvItem.setText("已加锁" + mLockInfo.size() + "个");
						} else {
							mTvItem.setText("未加锁" + mUnLockInfo.size() + "个");
						}

						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		});
	}

	/**
	 * 设置ListView的item点击动画
	 * 
	 * @param tmp
	 */
	private void setAnim(View tmp) {
		TranslateAnimation animation;
		if (mIsLock) {
			animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, -1.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, 0);
		} else {
			animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, 1.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, 0);
		}
		animation.setDuration(300);
		tmp.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mAnimFlag = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				sortList(); // 重新对集合排序
				mAnimFlag = false;

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void initData() {
		// 异步加载
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// 显示正在加载
				mLoading.setVisibility(View.VISIBLE);

				mDao = new AppLockDao(getApplicationContext());
				mLockInfo = new ArrayList<>();
				mUnLockInfo = new ArrayList<>();
			}

			@Override
			protected Void doInBackground(Void... params) {
				mInfos = SoftwareProvider.getAllInfos(getApplicationContext());
				if (mInfos != null && mInfos.size() > 0) {
					for (SoftwareInfo info : mInfos) {
						if (mDao.isLock(info.getPackageName())) {
							mLockInfo.add(info); // 有锁
						} else {
							mUnLockInfo.add(info); // 无锁
						}
					}

					sortList();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				// 更新UI
				mLoading.setVisibility(View.GONE);
				mLvInfo.setVisibility(View.VISIBLE);

				// 设置适配器
				mTvItem.setText("未加锁" + mUnLockInfo.size() + "个");
				mIsLock = false; // 默认显示的是未上锁的
				mAdapter = new AppAdapter();
				mLvInfo.setAdapter(mAdapter);
			};

		}.execute();
	}

	/**
	 * 对集合重新排序
	 */
	private void sortList() {
		// 对集合重新整理排序:无锁在前,有锁在后
		mInfos.clear();
		mInfos.addAll(mUnLockInfo);
		mInfos.addAll(mLockInfo);
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		mLvInfo.setVisibility(View.GONE);

		// 电子狗服务已关闭提醒
		if (!ServiceStatusUtils.isRunningService(this, WatchDogService1.class)) {
			if (!ServiceStatusUtils.isRunningService(this, WatchDogService2.class)) {
				Toast.makeText(this, "请开启电子狗服务1或电子狗服务2!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 适配数据给Listview
	 * 
	 * @author zhongyuxin
	 */
	private class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mIsLock ? mLockInfo.size() : mUnLockInfo.size();
		}

		@Override
		public Object getItem(int position) {
			return mIsLock ? mLockInfo.get(position) : mUnLockInfo.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = View.inflate(getApplicationContext(), R.layout.listview_applock_item, null);
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_applock_item_pic);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_applock_item_name);
				holder.ivLock = (ImageView) convertView.findViewById(R.id.iv_applock_item_lock);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (mIsLock) {
				SoftwareInfo info = mLockInfo.get(position); // 从有锁集合取数据

				holder.ivPic.setImageDrawable(info.getIco());
				holder.tvName.setText(info.getName());
				holder.ivLock.setBackgroundResource(R.drawable.iv_applock_unlock_selecctor);
			} else {
				SoftwareInfo info = mUnLockInfo.get(position); // 从无锁集合取数据

				holder.ivPic.setImageDrawable(info.getIco());
				holder.tvName.setText(info.getName());
				holder.ivLock.setBackgroundResource(R.drawable.iv_applock_lock_selecctor);
			}

			return convertView;
		}
	}

	private class ViewHolder {
		ImageView ivPic;
		TextView tvName;
		ImageView ivLock;
	}

}
