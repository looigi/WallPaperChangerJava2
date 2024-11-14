package com.looigi.wallpaperchanger2.classeWallpaper.provider;

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
import com.looigi.wallpaperchanger2.classeDetector.widgets.Photo;
import com.looigi.wallpaperchanger2.notificaTasti.ActivityDiStart;

public class ProviderWallpaper extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		for (int appWidgetId : appWidgetIds) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_wallpaper);

			Intent cambia = new Intent(context, WallpaperPerWidget.class);
			cambia.addCategory(Intent.CATEGORY_LAUNCHER);
			cambia.setAction(Intent.ACTION_MAIN );
			cambia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP);
			cambia.putExtra("DO", "cambia");
			PendingIntent pCambia = PendingIntent.getActivity(context, 950, cambia,
					PendingIntent.FLAG_IMMUTABLE);
			views.setOnClickPendingIntent(R.id.imgCambia, pCambia);

			Intent refresh = new Intent(context, WallpaperPerWidget.class);
			refresh.addCategory(Intent.CATEGORY_LAUNCHER);
			refresh.setAction(Intent.ACTION_MAIN );
			refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP);
			refresh.putExtra("DO", "refresh");
			PendingIntent pRefresh = PendingIntent.getActivity(context, 951, refresh,
					PendingIntent.FLAG_IMMUTABLE);
			views.setOnClickPendingIntent(R.id.imgRefresh, pRefresh);

			setRemoteAdapter(context, views);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
		views.setRemoteAdapter(R.id.imgCambia,
				new Intent(context, WallpaperPerWidget.class));

		views.setRemoteAdapter(R.id.imgRefresh,
				new Intent(context, WallpaperPerWidget.class));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
}
