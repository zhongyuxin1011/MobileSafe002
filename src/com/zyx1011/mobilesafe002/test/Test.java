package com.zyx1011.mobilesafe002.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.zyx1011.mobilesafe002.db.AddressDao;
import com.zyx1011.mobilesafe002.db.AntiVirusDao;
import com.zyx1011.mobilesafe002.db.AppLockDao;
import com.zyx1011.mobilesafe002.db.MDbDao;
import com.zyx1011.mobilesafe002.engine.ProcessProvider;
import com.zyx1011.mobilesafe002.engine.SmsProvider;
import com.zyx1011.mobilesafe002.engine.SmsProvider.SmsListener;
import com.zyx1011.mobilesafe002.entity.AddressInfo;
import com.zyx1011.mobilesafe002.entity.ContactInfo;
import com.zyx1011.mobilesafe002.entity.InterceptInfo;
import com.zyx1011.mobilesafe002.entity.ProcessInfo;
import com.zyx1011.mobilesafe002.service.GPSService;
import com.zyx1011.mobilesafe002.utils.ContactUtils;
import com.zyx1011.mobilesafe002.utils.Md5Utils;
import com.zyx1011.mobilesafe002.utils.ServiceStatusUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class Test extends AndroidTestCase {

	public void testGetAllContacts() {
		List<ContactInfo> allContact = ContactUtils.getAllContact(getContext());
		if (allContact != null) {
			for (ContactInfo contactInfo : allContact) {
				Log.d("11", contactInfo.toString());
			}
		}
	}

	public void testServiceStatusUtils() {
		boolean b = ServiceStatusUtils.isRunningService(getContext(), GPSService.class);
		Log.d("11", b + "");
	}

	public void testFindAll() {
		MDbDao dao = new MDbDao(getContext());
		List<InterceptInfo> list = dao.findAll();
		if (list != null) {
			for (InterceptInfo interceptInfo : list) {
				Log.d("11", interceptInfo.toString());
			}
		}
	}

	public void testModify() {
		MDbDao dao = new MDbDao(getContext());
		boolean b = dao.modify("13512300000", 2);
		Log.d("11", b + "");
	}

	public void testAdd() {
		MDbDao dao = new MDbDao(getContext());
		boolean b = dao.add(null, 1);
		Log.d("11", b + "");
	}

	public void testFindTypeByPhone() {
		MDbDao dao = new MDbDao(getContext());
		int type = dao.findTypeByPhone("10086");
		Log.d("11", type + "");
	}

	public void testIsExists() {
		MDbDao dao = new MDbDao(getContext());
		boolean b = dao.isExists("10086");
		Log.d("11", b + "");
	}

	public void testGetPartAddress() {
		AddressDao dao = new AddressDao(getContext());
		List<AddressInfo> address = dao.getPartAddress(100, 10);
		if (address != null) {
			for (AddressInfo addressInfo : address) {
				Log.d("11", addressInfo.toString());
			}
		}
	}

	public void testGetCardType() {
		AddressDao dao = new AddressDao(getContext());
		String type = dao.getSingelCardType("1369135");
		Log.d("11", type);
	}

	public void testGetMultilCardType() {
		AddressDao dao = new AddressDao(getContext());
		List<AddressInfo> address = dao.getMultilCardType("135", 0, 10);
		Log.d("11", "NULL");
		if (address != null) {
			for (AddressInfo addressInfo : address) {
				Log.d("11", addressInfo.toString());
			}
		}
	}

	public void testGetAllRunningProcessInfo() {
		List<ProcessInfo> allRunningProcessInfo = ProcessProvider.getAllRunningProcessInfo(getContext());
		if (allRunningProcessInfo != null) {
			for (ProcessInfo processInfo : allRunningProcessInfo) {
				Log.d("11", processInfo.toString());
			}
		} else {
			Log.d("11", "Null");
		}
	}

	public void testSmsBackup() {
		SmsProvider.smsBackup(getContext(), new SmsListener() {

			@Override
			public void onSuccess() {
			}

			@Override
			public void onProgress(int progress) {
			}

			@Override
			public void onMax(int max) {
			}

			@Override
			public void onFail(String errorDes) {
			}
		});
	}

	public void testIsLock() {
		AppLockDao dao = new AppLockDao(getContext());
		boolean b = dao.isLock("com.android.contacts");
		Log.d("11", b + "");
	}

	public void testIsVirus() {
		AntiVirusDao dao = new AntiVirusDao(getContext());
		boolean b = dao.isVirus("a2bd62c99207956348986bf1357dea01");
		Log.d("11", b + "");
	}

	public void testGetMd5() {
		InputStream in;
		try {
			in = getContext().getResources().getAssets().open("day26_test_03.apk");
			String md5 = Md5Utils.getMd5(in);
			Log.d("11", md5); 
			// 569209dae152aecc39922b2b71dfba2a
			// e5f0623ecc4961e38ac5da94d280040a
			// 18937c5959bc03c8b8d9a3e8576f6974
			// e5d8598dee6a373255e0eeb2443ab2a6
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testAddVirus() {
		AntiVirusDao dao = new AntiVirusDao(getContext());
		boolean b = dao.add("e5f0623ecc4961e38ac5da94d280040a");
		Log.d("11", b + "");
	}

}
