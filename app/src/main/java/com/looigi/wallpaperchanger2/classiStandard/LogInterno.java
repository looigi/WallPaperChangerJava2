package com.looigi.wallpaperchanger2.classiStandard;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogInterno {
	private Context context;
	private List<String> lista;
	private Handler handler;
	private boolean Detector = false;

	public LogInterno(Context c, boolean Detector) {
		CreaCartella(VariabiliStaticheWallpaper.getInstance().getPercorsoDIRLog());

		this.Detector = Detector;
		context = c;
		lista = new ArrayList<>();
	}

	public void PulisceFileDiLog() {
		String Datella = "";
		Datella = PrendeDataAttuale() + " " + PrendeOraAttuale();

		String sBody = Datella + ": Inizio log\n";

        try {
			String path = tornaPath();

			File file = new File(path);
			if (!Utility.getInstance().EsisteFile(path)) {
				if (!file.createNewFile()) {
					/* Toast.makeText(context,
							VariabiliStatiche.channelName + ": Impossibile creare il file.",
							Toast.LENGTH_LONG).show(); */
					return;
				}
			}

			FileOutputStream fos = new FileOutputStream(file, false);
			fos.write(sBody.getBytes());
			fos.close();
        } catch (IOException e) {
			e.printStackTrace();
			/* Toast.makeText(context,
					VariabiliStatiche.channelName + ": Impossibile creare il file: " +
					Utility.getInstance().PrendeErroreDaException(e),
					Toast.LENGTH_LONG).show(); */
        }

        /* File gpxfile = new File(VariabiliStatiche.PercorsoDIRLog, NomeFile);
		FileWriter writer;
		try {
			writer = new FileWriter(gpxfile);
			writer.append(sBody);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// e.printStackTrace();
		} */
    }
    
	private String PrendeDataAttuale() {
		String Ritorno="";
		
		Calendar Oggi = Calendar.getInstance();
        int Giorno = Oggi.get(Calendar.DAY_OF_MONTH);
        int Mese = Oggi.get(Calendar.MONTH);
        int Anno = Oggi.get(Calendar.YEAR);
        String sGiorno = Integer.toString(Giorno).trim();
        String sMese = Integer.toString(Mese+1).trim();
        String sAnno = Integer.toString(Anno).trim();
        if (sGiorno.length() == 1) {
        	sGiorno = "0" + sGiorno;
        }
        if (sMese.length() == 1) {
        	sMese = "0" + sMese;
        }
        Ritorno = sGiorno + "/" + sMese + "/" + sAnno;
        
        return Ritorno;
	}
	
	private String PrendeOraAttuale() {
		String Ritorno="";
		
		Calendar Oggi = Calendar.getInstance();
        int Ore = Oggi.get(Calendar.HOUR_OF_DAY);
        int Minuti = Oggi.get(Calendar.MINUTE);
        int Secondi = Oggi.get(Calendar.SECOND);
        String sOre = Integer.toString(Ore).trim();
        String sMinuti = Integer.toString(Minuti).trim();
        String sSecondi = Integer.toString(Secondi).trim();
        if (sOre.length() == 1) {
        	sOre = "0" + sOre;
        }
        if (sMinuti.length() == 1) {
        	sMinuti = "0" + sMinuti;
        }
        if (sSecondi.length() == 1) {
        	sSecondi = "0" + sSecondi;
        }
        Ritorno = sOre + ":" + sMinuti + ":" + sSecondi;
        
        return Ritorno;
	}
   
    public void ScriveLog(String MessaggioLog) {
		if (lista == null) {
			lista = new ArrayList<>();
		}
		lista.add(MessaggioLog);

		if (handler == null) {
			handler = new Handler(Looper.getMainLooper());
			final Runnable r = new Runnable() {
				public void run() {
					ScriveLogInterno(lista.get(0));

					lista.remove(0);
					if (!lista.isEmpty()) {
						handler.postDelayed(this, 100);
					} else {
						handler.removeCallbacks(this);
						handler = null;
					}
				}
			};
			handler.postDelayed(r, 100);
		}
	}

	private String tornaPath() {
		String path = "";

		if (VariabiliStaticheWallpaper.getInstance().getPercorsoDIRLog().isEmpty() ||
				VariabiliStaticheWallpaper.getInstance().getNomeFileDiLog().isEmpty()) {
			Utility.getInstance().generaPath(context);
		}
		path = VariabiliStaticheWallpaper.getInstance().getPercorsoDIRLog() + "/" +
				VariabiliStaticheWallpaper.getInstance().getNomeFileDiLog();
		if (Detector) {
			if (VariabiliStaticheDetector.getInstance().getPercorsoDIRLog().isEmpty() ||
					VariabiliStaticheDetector.getInstance().getNomeFileDiLog().isEmpty()) {
				UtilityDetector.getInstance().generaPath(context);
			}
			path = VariabiliStaticheDetector.getInstance().getPercorsoDIRLog() + "/" +
					VariabiliStaticheDetector.getInstance().getNomeFileDiLog();
		}

		return path;
	}

	private void ScriveLogInterno(String MessaggioLog) {
		String Datella = "";
		Datella = PrendeDataAttuale() + " " + PrendeOraAttuale();

		String path = tornaPath();

		if (Utility.getInstance().EsisteFile(path)) {
			File gpxfile = new File(path);
			FileWriter writer;
			try {
				writer = new FileWriter(gpxfile, true);
				writer.append(Datella + ": " + MessaggioLog + "\n");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				// Toast.makeText(context,
				// 		VariabiliStatiche.channelName + ": Errore scrittura log " + Utility.getInstance().PrendeErroreDaException(e),
				// 		Toast.LENGTH_LONG).show();
			}
		}
    }

    private void CreaCartella(String Percorso) {
		try {
			File dDirectory = new File(Percorso);
			dDirectory.mkdirs();
		} catch (Exception ignored) {
			
		}  
	}
}
