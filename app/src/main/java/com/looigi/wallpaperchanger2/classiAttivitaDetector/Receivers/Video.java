package com.looigi.wallpaperchanger2.classiAttivitaDetector.Receivers;

import android.app.Activity;
import android.content.Context;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
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

public class Video extends Activity implements Callback {
	private Context context;
    private MediaRecorder recorder;
	private int StaRegistrando;
	private int Fotocamera;
	private List<String> Dimensioni;
	private Handler handlerTimer;
	private Runnable rTimer;
	private Activity act;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video);

		context = this;
		act = this;

		SurfaceView surfaceView=(SurfaceView) findViewById(R.id.surfaceView1);
		LinearLayout lsv=(LinearLayout) findViewById(R.id.laySV);
		LinearLayout.LayoutParams params = null;

		if (VariabiliStaticheDetector.getInstance().getAnteprima() == null) {
			VariabiliStaticheDetector.getInstance().setAnteprima("S");
		}
		if (VariabiliStaticheDetector.getInstance().getAnteprima().equals("S")) {
			params = new LinearLayout.LayoutParams(90, 90);
		} else {
			params = new LinearLayout.LayoutParams(5, 5);
		}
		lsv.setLayoutParams(params);
		
	    SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);

        StaRegistrando = 0;

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
        	holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    
        recorder = new MediaRecorder();

		try {
		    recorder.setPreviewDisplay(holder.getSurface());
	    } catch (Exception e) {
		    recorder=null;
	    }

		ImpostaVideo();
		
		Button cmdVideo = (Button) findViewById(R.id.cmdChiude);
		cmdVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	RimetteAPostoFilesAudioInSystem();

            	try {
					recorder.stop();
					recorder.reset();
					recorder.release();
				} catch(Exception ignored) {

				}

				finish();
            }
        });					        
		
		Button cmdAzione = (Button) findViewById(R.id.cmdAzione);
		cmdAzione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		        AzionaFermaVideo(context);
            }
        });

		RinominaFilesAudioInSystem();

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

	private void RinominaFilesAudioInSystem() {
//        SuoniTelefono s=new SuoniTelefono();
//        s.Imposta_suoni("mute");
//        
//		rootChecker r=new rootChecker();
//		String Origine="/system/media/audio/ui/Cam_Start.ogg";
//		String Destinazione="/system/media/audio/ui/Cam_Start.bak";
//		r.RinominaFilesRoot(Origine, Destinazione);
//		Origine="/system/media/audio/ui/Cam_Stop.ogg";
//		Destinazione="/system/media/audio/ui/Cam_Stop.bak";
//		r.RinominaFilesRoot(Origine, Destinazione);
	}
	
	private void RimetteAPostoFilesAudioInSystem() {
//        SuoniTelefono s=new SuoniTelefono();
//        s.Imposta_suoni("");
//        
//		rootChecker r=new rootChecker();
//		String Origine="/system/media/audio/ui/Cam_Start.bak";
//		String Destinazione="/system/media/audio/ui/Cam_Start.ogg";
//		r.RinominaFilesRoot(Origine, Destinazione);
//		Origine="/system/media/audio/ui/Cam_Stop.bak";
//		Destinazione="/system/media/audio/ui/Cam_Stop.ogg";
//		r.RinominaFilesRoot(Origine, Destinazione);
	}
	
	public void AzionaFermaVideo(Context context) {
		if (StaRegistrando == 0) {
			// u.VisualizzaToast(context, "Ok 1!", true);
			UtilityDetector.getInstance().Vibra(context, 500);

			Button cmdVideo=(Button) findViewById(R.id.cmdChiude);
			cmdVideo.setVisibility(LinearLayout.GONE);
			
			// Utility u=new Utility();
			// u.LeggeVibrazione(context);
			
			/* if (VariabiliStatiche.getInstance().isVibrazione()) {
			    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			    vibrator.vibrate(500);
			} */

			StaRegistrando=1;

			try {
				recorder.prepare();

				recorder.start();
			} catch (IllegalStateException e) {
			    recorder=null;
			} catch (IOException ignored) {
			    recorder=null;
			}
		} else {
			if (StaRegistrando == 1) {
				// u.VisualizzaToast(context, "Ok 2!", true);
				StaRegistrando=0;

				if (recorder != null) {
					try {
						recorder.stop();
						recorder.reset();
						recorder.release();
					} catch (Exception ignored) {

					}
				}

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
							"V"
					);
					VariabiliStatiche.getInstance().setProgressivoDBMM(proMM);
				}
				VariabiliStatiche.getInstance().ScriveDatiAVideo();

				if (VariabiliStatiche.getInstance().isVibrazione()) {
					Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(1000);
				} */
				UtilityDetector.getInstance().Vibra(context,1000);

				RimetteAPostoFilesAudioInSystem();

				finish();
			}
		}
	}

	private String PrendeRisoluzioni(int cameraImpostata) {
		String Ritorno = "";

		/* GBTakePictureNoPreview c = new GBTakePictureNoPreview();
	    c.ImpostaContext(context);
	    if (Fotocamera==1) {
		    c.setUseFrontCamera();
	    } else {
		    c.setUseBackCamera();
	    }
	    Dimensioni=c.RitornaRisoluzioniVideo(); */
		if (VariabiliStaticheDetector.getInstance().getDimensioni() == null) {
			UtilityDetector.getInstance().RitornaRisoluzioni(this, cameraImpostata);
		}
		Dimensioni = VariabiliStaticheDetector.getInstance().getDimensioni();

	    int maxX = 0;
	    int maxY = 0;
	    int quale = -1;
	    int conta = 0;
	    for (String d : Dimensioni) {
	    	String[] dd = d.split("x");
	    	int w = Integer.parseInt(dd[0]);
			int h = Integer.parseInt(dd[1]);
			if (w > maxX) {
				maxX = w;
				maxY = h;
				quale = conta;
			}
			if (h > maxY) {
				maxX = w;
				maxY = h;
				quale = conta;
			}
			conta++;
		}
	    Ritorno = Dimensioni.get(quale);

	    return Ritorno;
	}

	private void ImpostaVideo() {
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
		// String RisolX;
		// pos=Ritorno.indexOf("§");
		// RisolX=Ritorno.substring(0,pos);
		// pos=RisolX.indexOf("x");
		// pos=Ritorno.indexOf("§");
		// int Estensione = VariabiliStaticheDetector.getInstance().getEstensione(); // Integer.parseInt(Ritorno.substring(pos+1,Ritorno.length()));
		String sEstensione = "dbv";
		/* } else {
			sEstensione="mp4";
		} */

		String ris = PrendeRisoluzioni(cameraFronteRetro);
		int pos = ris.indexOf("x");
		int X = Integer.parseInt(ris.substring(0, pos));
		int Y = Integer.parseInt(ris.substring(pos+1,ris.length()));

	    // String Origine = Environment.getExternalStorageDirectory().getAbsolutePath();
	    // String Cartella=VariabiliStatiche.getInstance().PathApplicazione;
		// String Cartella = UtilityDetector.getInstance().PrendePath(context);

		String Cartella = UtilityDetector.getInstance().PrendePath(context);
		Utility.getInstance().CreaCartelle(Cartella);
		String fileName = Cartella + UtilityDetector.getInstance().PrendeNomeImmagine() +
				"." + sEstensione;

		UtilityDetector.getInstance().ControllaFileNoMedia(Cartella);

	    // String fileName = Origine + Cartella + UtilityDetector.getInstance().PrendeNomeImmagine()+"."+sEstensione;

	    recorder.setOutputFile(fileName);

	    recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
	    recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
	    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	    recorder.setVideoSize(X, Y);
	    // recorder.setVideoFrameRate(30);
		CamcorderProfile cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
		recorder.setVideoEncodingBitRate(cpHigh.videoBitRate);
		recorder.setVideoFrameRate(cpHigh.videoFrameRate);
	    recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
	    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		/* CamcorderProfile cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
		// recorder.setProfile(cpHigh);
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setVideoSize(VariabiliStatiche.getInstance().getDISPLAY_WIDTH(), VariabiliStatiche.getInstance().getDISPLAY_HEIGHT());
		recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setVideoEncodingBitRate(cpHigh.videoBitRate);
		recorder.setVideoFrameRate(cpHigh.videoFrameRate); */

//	    LeggeOrientamento(context);

//	    if (MainActivityDetector.Orient>0) {
//		    recorder.setOrientationHint(MainActivityDetector.Orient);
//	    }
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
//		try {
//			recorder.prepare();
//		} catch (IllegalStateException e) {
//		    recorder=null;
//		} catch (IOException e) {
//		    recorder=null;
//		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}

}
