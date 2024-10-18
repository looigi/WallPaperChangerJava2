package com.looigi.wallpaperchanger2.classeDetector.widgets;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;

public class ProviderPhoto extends AppWidgetProvider {
	private static final String NomeMaschera = "Provider_Photo";
	private int appId = 111;
	// public static String CLICKPHOTO = "CLICK_PHOTO";

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
				"onUpdate ProviderPhoto 3");

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
		appWidgetManager.updateAppWidget(appId, views); */


		// Construct the RemoteViews object which defines the view of out widget
		for (int appWidgetId : appWidgetIds) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_photo);
			// Instruct the widget manager to update the widget
			// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setRemoteAdapter(context, views);
			// } else {
			// 	setRemoteAdapterV11(context, views);
			// }
			/** PendingIntent to launch the MainActivity when the widget was clicked **/
			Intent launchMain = new Intent(context, Photo.class);
			PendingIntent pendingMainIntent = PendingIntent.getActivity(
					context,
					0,
					launchMain,
					PendingIntent.FLAG_IMMUTABLE);
			views.setOnClickPendingIntent(R.id.imgScatta, pendingMainIntent);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(R.id.imgScatta,
				new Intent(context, Photo.class));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
}
