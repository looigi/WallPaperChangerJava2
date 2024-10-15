package com.looigi.wallpaperchanger2.classeDetector.Receivers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.looigi.wallpaperchanger2.R;

public class ProviderAudio extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

	    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_audio);
	    Intent configIntent = new Intent(context, Audio.class);

	    PendingIntent configPendingIntent = PendingIntent.getActivity(
				context,
				0,
				configIntent,
                PendingIntent.FLAG_IMMUTABLE);

	    remoteViews.setOnClickPendingIntent(R.id.imgAudio, configPendingIntent);
	    appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
