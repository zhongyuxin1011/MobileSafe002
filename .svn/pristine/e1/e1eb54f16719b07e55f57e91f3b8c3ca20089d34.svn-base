package com.zyx1011.mobilesafe002.receiver;

import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.service.CleanService;
import com.zyx1011.mobilesafe002.utils.PreferenceUtils;
import com.zyx1011.mobilesafe002.utils.ServiceStatusUtils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class WidgetReceiver extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		if (!ServiceStatusUtils.isRunningService(context, CleanService.class)) {
			Intent intent = new Intent(context, CleanService.class);
			context.startService(intent);

			PreferenceUtils.putInt(context, Constants.WIDGET_SERVICE, 1);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		if (ServiceStatusUtils.isRunningService(context, CleanService.class)) {
			Intent intent = new Intent(context, CleanService.class);
			context.stopService(intent);

			PreferenceUtils.putInt(context, Constants.WIDGET_SERVICE, 0);
		}
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

}
