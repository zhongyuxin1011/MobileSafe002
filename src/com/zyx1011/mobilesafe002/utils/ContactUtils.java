package com.zyx1011.mobilesafe002.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.zyx1011.mobilesafe002.entity.ContactInfo;

/**
 * 操作内容内容解析者获取联系人数据
 * 
 * @author zhongyuxin
 * 
 */
public class ContactUtils {
	// 访问raw_contacts表的口令
	public static final Uri RAW_CONTACTS_URI = Uri.parse("content://com.android.contacts/raw_contacts");
	// 访问data表的口令
	public static final Uri DATA_URI = Uri.parse("content://com.android.contacts/data");

	/**
	 * 获取所有联系人的姓名和电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static List<ContactInfo> getAllContact(Context context) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver(); // 拿到内容解析者

		Cursor id = resolver.query(RAW_CONTACTS_URI, new String[] { "contact_id", "display_name" }, null, null, null);
		if (id != null) {
			while (id.moveToNext()) {
				ContactInfo info = new ContactInfo();
				info.setId(id.getInt(0)); // 获取contact_id
				info.setName(id.getString(1)); // 获取display_name

				Cursor phone = resolver.query(DATA_URI, new String[] { "mimetype", "data1" }, "raw_contact_id=?",
						new String[] { String.valueOf(info.getId()) }, null);
				if (phone != null) {
					while (phone.moveToNext()) {
						// 获取电话号码
						if ("vnd.android.cursor.item/phone_v2".equals(phone.getString(0))) {
							info.setPhone(phone.getString(1));
						}
					}
					contactInfos.add(info); // 添加到集合中
				}
				phone.close(); // 关闭资源
			}
			id.close();
		}

		return contactInfos;
	}

	/**
	 * 获取部分联系人信息
	 * 
	 * @param offset
	 * @param count
	 * @return
	 */
	public static List<ContactInfo> getPartPhone(Context context, int offset, int count) {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver(); // 拿到内容解析者

		Cursor id = resolver.query(RAW_CONTACTS_URI, new String[] { "contact_id", "display_name" },
				"contact_id>? and contact_id<=?",
				new String[] { String.valueOf(offset), String.valueOf(offset + count) }, null);
		if (id != null) {
			while (id.moveToNext()) {
				ContactInfo info = new ContactInfo();
				info.setId(id.getInt(0)); // 获取contact_id
				info.setName(id.getString(1)); // 获取display_name

				Cursor phone = resolver.query(DATA_URI, new String[] { "mimetype", "data1" }, "raw_contact_id=?",
						new String[] { String.valueOf(info.getId()) }, null);
				if (phone != null) {
					while (phone.moveToNext()) {
						// 获取电话号码
						if ("vnd.android.cursor.item/phone_v2".equals(phone.getString(0))) {
							info.setPhone(phone.getString(1));
						}
					}
					contactInfos.add(info); // 添加到集合中
				}
				phone.close(); // 关闭资源
			}
			id.close();
		}

		return contactInfos;
	}

	/**
	 * 获取联系人的图片
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static Bitmap getBitMapById(Context context, int id) {
		Bitmap bitmap = null;
		ContentResolver resolver = context.getContentResolver();

		Cursor bmBinary = resolver.query(DATA_URI, new String[] { "mimetype", "data15" }, "raw_contact_id=?",
				new String[] { String.valueOf(id) }, null);
		while (bmBinary.moveToNext()) {
			if ("vnd.android.cursor.item/photo".equals(bmBinary.getString(bmBinary.getColumnIndex("mimetype")))) {
				byte[] data = bmBinary.getBlob(bmBinary.getColumnIndex("data15")); // 获取bates数组数据
				// 通过bytes数组创建图片
				bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			}
		}
		bmBinary.close();
		return bitmap;
	}
}
