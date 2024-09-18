package com.looigi.wallpaperchanger2.classiAttivitaDetector.Receivers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classiAttivitaDetector.AndroidCameraApi;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class Photo extends Activity {
	private static String NomeMaschera = "PHOTO";

	// private static Context context;
	private Handler handlerTimer;
	private Runnable rTimer;
	private Handler handlerTimer2;
	private Runnable rTimer2;
	private Context context;
	private Activity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.camera);

		act = this;
		context = this; // VariabiliStatiche.getInstance().getContext();

		// InizializzaMascheraDetector id = new InizializzaMascheraDetector();
		// id.inizializzaMaschera(context, act);

		// VariabiliStaticheDetector.getInstance().setChiudiActivity(true);

		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent myIntent = new Intent(
						act,
						AndroidCameraApi.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				act.startActivity(myIntent);
			}
		}, 1000);

		VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
		VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
		VariabiliStaticheStart.getInstance().ChiudeActivity(true);

		/* handlerTimer = new Handler();
		rTimer = new Runnable() {
			public void run() {
				if (VariabiliStaticheStart.getInstance().getMainActivity() != null) {
					VariabiliStaticheStart.getInstance().getMainActivity().moveTaskToBack(true);
					VariabiliStaticheStart.getInstance().getMainActivity().finish();
				}
			}
		};
		handlerTimer.postDelayed(rTimer, 1000); */

		// finish();

		/* UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Scatto foto. Controllo esistenza act: " + (act != null));
		if (act == null) {
			Utility.getInstance().ApreToast(context, "ACT NULLA in Photo.class");

			UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Scatto foto. ACT Nulla. Riavvio tutto");

			/* Intent mStartActivity = new Intent(context, MainActivityDetector.class);
			mStartActivity.putExtra("SCATTAFOTO", "SI");
			int mPendingIntentId = 123451;
			PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
			AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
			System.exit(0); * /
		} else {
			TextureView textureView = (TextureView) act.findViewById(R.id.textureView);

			UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Scatto foto. Controllo esistenza TEXTURE VIEW: " + (textureView == null));

			if (textureView == null) {
				UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Scatto foto. TEXTURE VIEW inesistente. Riavvio applicazione per scatto automatico");

				Intent mStartActivity = new Intent(context, MainActivityDetector.class);
				mStartActivity.putExtra("SCATTAFOTO", "SI");
				int mPendingIntentId = 123451;
				PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
				AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
				System.exit(0);
			} else {
				textureView.setVisibility(LinearLayout.VISIBLE);
				UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Scatto foto. TEXTURE VIEW esistente. Proseguo normalmente");

				/* stopService(VariabiliImpostazioni.getInstance().getServiceIntent());
				VariabiliImpostazioni.getInstance().setServiceIntent(null);

				VariabiliImpostazioni.getInstance().setServiceIntent(new Intent(this, ServizioInterno.class));
				startService(VariabiliImpostazioni.getInstance().getServiceIntent()); * /

				handlerTimer = new Handler();
				rTimer = new Runnable() {
					public void run() {
						UtilityDetector.getInstance().ScattaFoto(context, "da WIDGET");

						handlerTimer2 = new Handler();
						rTimer2 = new Runnable() {
							public void run() {
								// finish();

								VariabiliStaticheDetector.getInstance().getMainActivity().moveTaskToBack(true);
								finish();

								handlerTimer2.removeCallbacks(rTimer2);
								rTimer2 = null;
							}
						};
						handlerTimer2.postDelayed(rTimer2, 100);

						handlerTimer.removeCallbacks(rTimer);
						rTimer = null;
					}
				};

				handlerTimer.postDelayed(rTimer, 500);
			}
		} */
	}
}
