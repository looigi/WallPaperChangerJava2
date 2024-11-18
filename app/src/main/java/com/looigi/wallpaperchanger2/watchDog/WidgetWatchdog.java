package com.looigi.wallpaperchanger2.watchDog;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOnomastici.MainOnomastici;
import com.looigi.wallpaperchanger2.classeOnomastici.PrendeSantoClass;
import com.looigi.wallpaperchanger2.classeOnomastici.VariabiliStaticheOnomastici;
import com.looigi.wallpaperchanger2.classeOnomastici.db.DBLocaleOnomastici;
import com.looigi.wallpaperchanger2.classeOnomastici.db.GestioneDB;
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.CampiRitornoSanti;
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.DatiColori;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.Calendar;

public class WidgetWatchdog extends AppWidgetProvider {
    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                    final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        dostuff(context, appWidgetManager, appWidgetIds);
    }
	
    @Override
    public void onReceive (final Context context, Intent intent) {
    	super.onReceive(context, intent);

		VariabiliStaticheWatchdog.getInstance().setContext(context);

 		final AppWidgetManager awm=AppWidgetManager.getInstance(context);
		final int[] appWidgetIds=awm.getAppWidgetIds(new ComponentName(context, WidgetWatchdog.class));
		
		onUpdate(context, awm, appWidgetIds);
    }

    private void dostuff(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	// DatiColori dc = null;
    	
    	int N = appWidgetIds.length;

		VariabiliStaticheWatchdog.getInstance().ControllaServizi();

		for (int i = 0; i < N; i++) {
            int awID = appWidgetIds[i];

	        RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.widget_watchdog);

			rv.setTextViewText(R.id.txtInfo1, VariabiliStaticheWatchdog.getInstance().getInfo1());
			rv.setTextViewText(R.id.txtInfo2, VariabiliStaticheWatchdog.getInstance().getInfo2());
			rv.setTextViewText(R.id.txtInfo3, VariabiliStaticheWatchdog.getInstance().getInfo3());
			rv.setTextViewText(R.id.txtInfo4, VariabiliStaticheWatchdog.getInstance().getInfo4());

			rv.setTextViewText(R.id.txtInfo51, VariabiliStaticheWatchdog.getInstance().getServ1());
			rv.setTextViewText(R.id.txtInfo52, VariabiliStaticheWatchdog.getInstance().getServ2());
			rv.setTextViewText(R.id.txtInfo53, VariabiliStaticheWatchdog.getInstance().getServ3());
			rv.setTextViewText(R.id.txtInfo54, VariabiliStaticheWatchdog.getInstance().getServ4());

			if (VariabiliStaticheWatchdog.getInstance().getServ1().equals("On")) {
				rv.setTextColor(R.id.txtInfo51, Color.GREEN);
			} else {
				rv.setTextColor(R.id.txtInfo51, Color.RED);
			}
			if (VariabiliStaticheWatchdog.getInstance().getServ2().equals("On")) {
				rv.setTextColor(R.id.txtInfo52, Color.GREEN);
			} else {
				rv.setTextColor(R.id.txtInfo52, Color.RED);
			}
			if (VariabiliStaticheWatchdog.getInstance().getServ3().equals("On")) {
				rv.setTextColor(R.id.txtInfo53, Color.GREEN);
			} else {
				rv.setTextColor(R.id.txtInfo53, Color.RED);
			}
			if (VariabiliStaticheWatchdog.getInstance().getServ4().equals("On")) {
				rv.setTextColor(R.id.txtInfo54, Color.GREEN);
			} else {
				rv.setTextColor(R.id.txtInfo54, Color.RED);
			}

			appWidgetManager.updateAppWidget(awID, rv);
    	}
	}

	@Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}
