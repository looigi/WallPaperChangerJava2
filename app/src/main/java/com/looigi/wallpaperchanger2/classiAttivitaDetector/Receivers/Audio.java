package com.looigi.wallpaperchanger2.classiAttivitaDetector.Receivers;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.IOException;
import java.util.List;

// import static com.looigi.detector.MainActivityDetector.main;

public class Audio extends Activity  {
	private static Context context;
    MediaRecorder recorder;
	boolean StaRegistrando=false;
	private Handler handlerTimer;
	private Runnable rTimer;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);

		context=this;

		LinearLayout lsv=(LinearLayout) findViewById(R.id.laySV);

		LinearLayout.LayoutParams params = null;

		params = new LinearLayout.LayoutParams(5, 5);
		lsv.setLayoutParams(params);

        recorder = new MediaRecorder();

		Button cmdChiude = (Button) findViewById(R.id.cmdChiude);
		cmdChiude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    			finish();
            }
        });					        
		
		Button cmdAzione = (Button) findViewById(R.id.cmdAzione);
		cmdAzione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		        AzionaParteFermaAudio(context);
            }
        });

		ImpostaAudio();

		handlerTimer = new Handler();
		rTimer = new Runnable() {
			public void run() {
				VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
				VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
				VariabiliStaticheStart.getInstance().ChiudeActivity(true);

				handlerTimer.removeCallbacks(rTimer);
				rTimer = null;
			}
		};
		handlerTimer.postDelayed(rTimer, 100);
	}

	public void AzionaParteFermaAudio(Context context) {
		if (!StaRegistrando) {
			// Toast toast=Toast.makeText(context, "Ok 1!" ,Toast.LENGTH_LONG);
			// toast.show();
			UtilityDetector.getInstance().VisualizzaToast(context, "Ok 1!", true);

			Button cmdVideo=(Button) findViewById(R.id.cmdChiude);
			cmdVideo.setVisibility(LinearLayout.GONE);
			
			// Utility u=new Utility();
			// u.LeggeVibrazione(context);
			
			/* if (VariabiliImpostazioni.getInstance().isVibrazione()) {
			    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			    vibrator.vibrate(500);
			} */
			UtilityDetector.getInstance().Vibra(context, 500);

			try {
				recorder.prepare();
				recorder.start();
			} catch (IOException ignored) {

			}

			StaRegistrando=true;
		} else {
			UtilityDetector.getInstance().VisualizzaToast(context, "Ok 2!", true);

			StaRegistrando=false;
			
    		recorder.stop();
    		recorder.reset();
    		recorder.release();
    		
    		// Utility u=new Utility();
 			// if (VariabiliStatiche.getInstance().Vibrazione==null) {
			// 	u.LeggeVibrazione(context);
			// }

			/* Location l = VariabiliStatiche.getInstance().getLocGPS();

			if (l!=null) {
				Utility u = new Utility();
				Date todayDate = Calendar.getInstance().getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String oggi = formatter.format(todayDate);

				int proMM = VariabiliStatiche.getInstance().getProgressivoDBMM() + 1;
				VariabiliStatiche.getInstance().getDbGpsPos().aggiungiMultimedia(
						oggi,
						Integer.toString(proMM),
						Double.toString(l.getLatitude()),
						Double.toString(l.getLongitude()),
						Double.toString(l.getAltitude()),
						u.PrendeNomeImmagine(),
						"A"
				);
				VariabiliStatiche.getInstance().setProgressivoDBMM(proMM);
			}
			VariabiliStatiche.getInstance().ScriveDatiAVideo(); */

			/* if (VariabiliImpostazioni.getInstance().isVibrazione()) {
				Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(1000);
			} */
			UtilityDetector.getInstance().Vibra(context,1000);

			finish();
		}
	}

	int Fotocamera;
	List<String> Dimensioni;

	private void ImpostaAudio() {
		// GestioneDB gdb=new GestioneDB();
		// String Ritorno=gdb.LeggeValori(context);
		// int pos=Ritorno.indexOf("*");
		// Ritorno=Ritorno.substring(pos+1,Ritorno.length());
		// pos=Ritorno.indexOf("@");
		// Ritorno=Ritorno.substring(pos+1,Ritorno.length());
		// pos=Ritorno.indexOf("§");
		int cameraFronteRetro;

		int cameraID = VariabiliStaticheDetector.getInstance().getFotocamera();
		if (cameraID == 0) {
			cameraFronteRetro = 0;
		} else {
			cameraFronteRetro = 1;
		}

		// Ritorno=Ritorno.substring(pos+1,Ritorno.length());
		// // String RisolX;
		// pos=Ritorno.indexOf("§");
		// String RisolX=Ritorno.substring(0,pos);
		// pos=RisolX.indexOf("x");
		// pos=Ritorno.indexOf("§");
		// int Estensione = VariabiliStaticheDetector.getInstance().getEstensione(); // Integer.parseInt(Ritorno.substring(pos+1,Ritorno.length()));
		String sEstensione ="dba";

		// String Origine=Environment.getExternalStorageDirectory().getAbsolutePath();
		// String Cartella=VariabiliStatiche.getInstance().PathApplicazione;
		// String Cartella = UtilityDetector.getInstance().PrendePath(context);

		String Cartella = UtilityDetector.getInstance().PrendePath(context);
		Utility.getInstance().CreaCartelle(Cartella);
		String fileName = Cartella + UtilityDetector.getInstance().PrendeNomeImmagine() +
				"." + sEstensione;

		UtilityDetector.getInstance().ControllaFileNoMedia(Cartella);

		// String fileName = Origine + Cartella + UtilityDetector.getInstance().PrendeNomeImmagine()+"."+sEstensione;

		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		recorder.setOutputFile(fileName);

//	    LeggeOrientamento(context);

//	    if (MainActivityDetector.Orient>0) {
//		    recorder.setOrientationHint(MainActivityDetector.Orient);
//	    }
	}

}
