package com.zyx1011.mobilesafe002.view;

import com.zyx1011.mobilesafe002.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingItemView extends LinearLayout {

	private TextView mTvTitle;
	private ImageView mIvSwitch;

	private String mTitle;
	private boolean mIsCheck;
	private boolean mIsVisable;
	private int mBackgrund;
	private int mTitleColor;

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		View view = View.inflate(getContext(), R.layout.view_setting_item, null);

		addView(view); // 添加到SettingItemView

		mTvTitle = (TextView) view.findViewById(R.id.tv_setting_item_title);
		mIvSwitch = (ImageView) view.findViewById(R.id.iv_setting_item_switch);

		// 获取
		TypedArray osa = context.obtainStyledAttributes(attrs, R.styleable.SettingItemAttrs, defStyleAttr, 0);
		mTitle = osa.getString(R.styleable.SettingItemAttrs_sia_title);
		mIsVisable = osa.getBoolean(R.styleable.SettingItemAttrs_sia_visable, true);
		mIsCheck = osa.getBoolean(R.styleable.SettingItemAttrs_sia_check, false);
		mBackgrund = osa.getInt(R.styleable.SettingItemAttrs_sia_background, -1);
		mTitleColor = osa.getColor(R.styleable.SettingItemAttrs_sia_titleColor, Color.rgb(50, 50, 50));

		osa.recycle(); // 回收TypeArray

		setClickable(true);

		mTvTitle.setText(mTitle);
		setTitleColor(mTitleColor);

		check(mIsCheck);
		setVisable();
		setBackground();
	}

	public void setTitleColor(int color) {
		mTvTitle.setTextColor(color);
	}

	/**
	 * 设置背景图
	 * 
	 * @param backgrund
	 */
	public void setBackground() {
		switch (mBackgrund) {
		case 0:
			setBackgroundResource(R.drawable.iv_sia_start_selector);
			break;

		case 1:
			setBackgroundResource(R.drawable.iv_sia_middle_selector);
			break;

		case 2:
			setBackgroundResource(R.drawable.iv_sia_end_selector);
			break;

		default:
			setBackgroundResource(R.drawable.iv_sia_start_selector);
			break;
		}
	}

	/**
	 * 设置开关状态图标的可见性
	 * 
	 * @param isVisable
	 */
	public void setVisable() {
		mIvSwitch.setVisibility(mIsVisable ? View.VISIBLE : View.GONE);
	}

	/**
	 * 设置开关状态图标
	 * 
	 * @param isCheck
	 */
	public void check(boolean isCheck) {
		mIvSwitch.setImageResource(isCheck ? R.drawable.on : R.drawable.off);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingItemView(Context context) {
		this(context, null);
	}

}
