package com.zyx1011.mobilesafe002.receiver;

import java.util.List;

import com.zyx1011.mobilesafe002.engine.ProcessProvider;
import com.zyx1011.mobilesafe002.entity.ProcessInfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.widget.Toast;

public class CleanReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int killCount = 0;
		long cleanMem = 0;

		List<ProcessInfo> infos = ProcessProvider.getAllRunningProcessInfo(context);

		if (infos != null) {
			for (ProcessInfo info : infos) {
				if (!info.getProcessName().equals(context.getPackageName())) {
					if (!info.isGodProcess()) {
						killCount++;
						ProcessProvider.killProcess(context, info.getProcessName());
						cleanMem += info.getUsedMem();
					}
				}
			}

			if (killCount > 0) {
				Toast.makeText(context,
						"Clean is done!" + killCount + " process is dead, and "
								+ Formatter.formatFileSize(context, cleanMem) + " memory is free.",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "Your mobile is health, hold on and usually clean it!", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

}
