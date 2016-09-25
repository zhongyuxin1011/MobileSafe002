package com.zyx1011.mobilesafe002.engine;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zyx1011.mobilesafe002.R;
import com.zyx1011.mobilesafe002.entity.ProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

/**
 * 提供与进程相关的数据,Android4.0
 * 
 * @author zhongyuxin
 */
public class ProcessProvider {

	/**
	 * 获取正在运行的进程数量
	 * 
	 * @param context
	 * @return
	 */
	public static int getRunningProcessCount(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

		if (runningAppProcesses != null) {
			return runningAppProcesses.size();
		}

		return -1; // 获取不到返回-1
	}

	/**
	 * 获取所有进程数量
	 * 
	 * @param context
	 * @return
	 */
	public static int getAllProcessCount(Context context) {
		PackageManager manager = context.getPackageManager();

		List<PackageInfo> packages = manager.getInstalledPackages(0);
		HashSet<String> process = new HashSet<>();

		for (PackageInfo info : packages) {
			process.add(info.applicationInfo.processName);

			// Activity
			ActivityInfo[] activities = info.activities;
			if (activities != null) {
				for (ActivityInfo activityInfo : activities) {
					process.add(activityInfo.processName);
				}
			}

			// Service
			ServiceInfo[] services = info.services;
			if (services != null) {
				for (ServiceInfo serviceInfo : services) {
					process.add(serviceInfo.processName);
				}
			}

			// Receiver
			ActivityInfo[] receivers = info.receivers;
			if (receivers != null) {
				for (ActivityInfo receiverInfo : receivers) {
					process.add(receiverInfo.processName);
				}
			}

			// Provider
			ProviderInfo[] providers = info.providers;
			if (providers != null) {
				for (ProviderInfo providerInfo : providers) {
					process.add(providerInfo.processName);
				}
			}
		}

		return process.size(); // 可能空集合
	}

	/**
	 * 获取已用内存大小
	 * 
	 * @param context
	 * @return
	 */
	public static long getUsedMen(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);

		return getTotalMen(context) - getAvailMen(context);
	}

	/**
	 * 获取可用内存大小
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailMen(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);

		return outInfo.availMem;
	}

	/**
	 * 获取全部内存空间
	 * 
	 * @param context
	 * @return
	 */
	public static long getTotalMen(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);

		return outInfo.totalMem;
	}

	/**
	 * 获取所有正在运行的进程的集合
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<ProcessInfo> getAllRunningProcessInfo(Context context) {
		List<ProcessInfo> infos = new CopyOnWriteArrayList<>();

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();

		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		if (runningAppProcesses != null) {
			for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
				ProcessInfo info = new ProcessInfo();
				info.setProcessName(runningAppProcessInfo.processName);

				android.os.Debug.MemoryInfo memoryInfo = am
						.getProcessMemoryInfo(new int[] { runningAppProcessInfo.pid })[0];
				info.setUsedMem(memoryInfo.getTotalPss() * 1024);

				try {
					PackageInfo packageInfo = pm.getPackageInfo(runningAppProcessInfo.processName, 0);

					info.setIcon(packageInfo.applicationInfo.loadIcon(pm));
					info.setApkName(packageInfo.applicationInfo.loadLabel(pm).toString());

					int flags = packageInfo.applicationInfo.flags;
					info.setSystem((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM);

					if (runningAppProcessInfo.importance < 300
							|| (runningAppProcessInfo.importance == 300 && info.isSystem())) {
						info.setGodProcess(true);
					} else {
						info.setGodProcess(false);
					}

				} catch (NameNotFoundException e) {
					e.printStackTrace();

					info.setApkName(info.getProcessName());
					info.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
					info.setSystem(true);
					info.setGodProcess(true);
				}
				infos.add(info);
			}
		}
		return infos;
	}

	/**
	 * 后台进程清理
	 * 
	 * @param context
	 * @param processName
	 */
	public static void killProcess(Context context, String processName) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		am.killBackgroundProcesses(processName);
	}

}
