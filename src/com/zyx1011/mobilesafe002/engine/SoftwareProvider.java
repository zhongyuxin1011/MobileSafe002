package com.zyx1011.mobilesafe002.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zyx1011.mobilesafe002.entity.SoftwareInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 提供应用相关的数据
 * 
 * @author zhongyuxin
 */
public class SoftwareProvider {

	/**
	 * 获取所有应用信息的集合
	 * 
	 * @param context
	 * @return
	 */
	public static List<SoftwareInfo> getAllInfos(Context context) {
		List<SoftwareInfo> infos = new ArrayList<>();

		PackageManager manager = context.getPackageManager();
		// 获取所有安装的包信息,可能返回null
		List<PackageInfo> packages = manager.getInstalledPackages(0);

		if (packages != null) {
			for (PackageInfo info : packages) {
				SoftwareInfo sInfo = new SoftwareInfo();
				sInfo.setPackageName(info.packageName); // 获取包名
				sInfo.setIco(info.applicationInfo.loadIcon(manager)); // 获取图标
				sInfo.setName(info.applicationInfo.loadLabel(manager).toString()); // 获取应用名
				sInfo.setApkSize(new File(info.applicationInfo.sourceDir).length()); // 获取应用大小

				int flag = info.applicationInfo.flags;
				// 获取应用类型,用户还是系统
				if ((flag & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
					sInfo.setSystem(true);
				} else {
					sInfo.setSystem(false);
				}

				// 获取应用安装的位置,内部存储还是SD卡
				if ((flag & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
					sInfo.setInstallSD(true);
				} else {
					sInfo.setInstallSD(false);
				}

				infos.add(sInfo);
			}
		}

		return infos; // 可能返回空集合
	}

	/**
	 * 获取一个应用信息的集合
	 * 
	 * @param context
	 * @return
	 */
	public static SoftwareInfo getInfo(Context context, String packageName) {

		PackageManager manager = context.getPackageManager();

		PackageInfo packageInfo;
		SoftwareInfo info = new SoftwareInfo();
		try {
			packageInfo = manager.getPackageInfo(packageName, 0);
			info.setIco(packageInfo.applicationInfo.loadIcon(manager));
			info.setName(packageInfo.applicationInfo.loadLabel(manager).toString());
			info.setApkSize(new File(packageInfo.applicationInfo.sourceDir).length());

			int flag = packageInfo.applicationInfo.flags;
			// 获取应用类型,用户还是系统
			if ((flag & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
				info.setSystem(true);
			} else {
				info.setSystem(false);
			}

			// 获取应用安装的位置,内部存储还是SD卡
			if ((flag & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
				info.setInstallSD(true);
			} else {
				info.setInstallSD(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return info;
	}

}
