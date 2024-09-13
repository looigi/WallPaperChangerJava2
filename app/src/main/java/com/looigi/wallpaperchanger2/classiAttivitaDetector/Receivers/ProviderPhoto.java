package com.looigi.wallpaperchanger2.classiAttivitaDetector.Receivers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;

public class ProviderPhoto extends AppWidgetProvider {
	private static final String NomeMaschera = "PROVIDERPHOTO";
	private int appId = 111;
	public static String CLICKPHOTO = "CLICK_PHOTO";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {
		UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "onUpdate ProviderPhoto 1");

		VariabiliStaticheDetector.getInstance().setContext(context);

		/* RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_photo);
		Intent configIntent = new Intent(context, Photo.class);
		// configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "onUpdate ProviderPhoto 2");

		PendingIntent configPendingIntent = PendingIntent.getActivity(
				context,
				0,
				configIntent,
				PendingIntent.FLAG_IMMUTABLE);

		remoteViews.setOnClickPendingIntent(R.id.imgScatta, configPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,
				"onUpdate ProviderPhoto 3"); */

		Intent intent = new Intent(context, Photo.class);
		intent.setAction(CLICKPHOTO);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context,
				0,
				intent,
				PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
		);

		// Get the layout for the widget and attach an onClick listener to
		// the button.
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_photo);
		views.setOnClickPendingIntent(R.id.imgScatta, pendingIntent);

		// Tell the AppWidgetManager to perform an update on the current app
		// widget.
		appWidgetManager.updateAppWidget(appId, views);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (intent.getAction().equals(CLICKPHOTO)) {
			//do some really cool stuff here
		}
	}
}
