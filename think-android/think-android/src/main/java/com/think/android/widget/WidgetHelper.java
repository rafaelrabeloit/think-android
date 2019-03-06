package com.think.android.widget;

import com.think.android.widget.OneRecentWidgetProvider_;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public final class WidgetHelper {
	
	/**
	 * To change things such as Theme based configurations.
	 * @param context
	 */
	public static void updateAllWidgets(Context context){

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		
		Intent intent = new Intent(context, OneRecentWidgetProvider_.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");

		// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
		// since it seems the onUpdate() is only fired on that:
		int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), OneRecentWidgetProvider_.class.getName()));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
		context.sendBroadcast(intent);
	}

	/**
	 * To change the Recent Quotes data set
	 * @param context
	 */
	public static void updateRecentQuotesWidgets(Context context){

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		
		Intent intent = new Intent(context, OneRecentWidgetProvider_.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");

		// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
		// since it seems the onUpdate() is only fired on that:
		int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), OneRecentWidgetProvider_.class.getName()));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
		context.sendBroadcast(intent);
	}
}
