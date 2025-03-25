package com.looigi.wallpaperchanger2.utilities.log;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogInterno {
	private Context context;
	private List<StrutturaLog> lista;
	private Handler handler;
	private boolean Detector = false;

	public LogInterno(Context c, boolean Detector) {
		this.Detector = Detector;
		context = c;
		lista = new ArrayList<>();
	}

	/*
	public void PulisceFileDiLog(String Path, String NomeFile) {
		String Datella = "";
		Datella = PrendeDataAttuale() + " " + PrendeOraAttuale();

		String sBody = Datella + ": Inizio log\n";

        try {
			File file = new File(Path + "/" + NomeFile);
			if (!UtilityWallpaper.getInstance().EsisteFile(Path + "/" + NomeFile)) {
				if (!file.createNewFile()) {
					/* Toast.makeText(context,
							VariabiliStatiche.channelName + ": Impossibile creare il file.",
							Toast.LENGTH_LONG).show(); * /
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
					Toast.LENGTH_LONG).show(); * /
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
		} * /
    }
    */

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
   
    public void ScriveLog(String Applicazione, String Maschera, String MessaggioLog) {
		if (!VariabiliStaticheStart.getInstance().isLogAttivo()) {
			return;
		}

		if (lista == null) {
			lista = new ArrayList<>();
		}

		if (Maschera == null) {
			Maschera = "Maschera_Nulla";
		}

		StrutturaLog s = new StrutturaLog();
		s.setApplicazione(Applicazione);
		s.setData(PrendeDataAttuale() + " " + PrendeOraAttuale());
		s.setNomeMaschera(Maschera);
		s.setLog(MessaggioLog);

		lista.add(s);

		if (handler == null) {
			handler = new Handler(Looper.getMainLooper());
			final Runnable r = new Runnable() {
				public void run() {
					if (!lista.isEmpty()) {
						ScriveLogInterno(lista.get(0));

						lista.remove(0);
						if (!lista.isEmpty()) {
							handler.postDelayed(this, 100);
						} else {
							handler.removeCallbacksAndMessages(this);
							handler.removeCallbacks(this);
							handler = null;
						}
					} else {
						if (handler != null) {
							handler.removeCallbacksAndMessages(this);
							handler.removeCallbacks(this);
							handler = null;
						}
					}
				}
			};
			handler.postDelayed(r, 100);
		}
	}

	public String PrendePathLog(Context context) {
		return context.getFilesDir() + "/Log/";
	}

	public void generaPath(Context context) {
        if (context != null) {
            String pathLog = PrendePathLog(context);
            VariabiliStaticheStart.getInstance().setPercorsoDIRLog(pathLog);
		}
    }

	private String tornaPath(StrutturaLog M) {
		String path = "";
		String NomeFileLog = M.getNomeMaschera();

		if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog() == null) {
			generaPath(context);
		}

		path = VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + M.getApplicazione();
		CreaCartella(path);

		path += "/" + NomeFileLog + ".txt";
		if (!EsisteFile(path)) {
			try {
				File f = new File(path);
				f.createNewFile();
			} catch (IOException e) {

			}
		}

		return path;
	}

	private boolean EsisteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	private void ScriveLogInterno(StrutturaLog M) {
		String path = tornaPath(M);

		if (EsisteFile(path)) {
			File gpxfile = new File(path);
			FileWriter writer;
			try {
				writer = new FileWriter(gpxfile, true);
				writer.append(M.getData() + ": " + M.getLog() + "\n");
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

	private void CreaCartella(String Path) {
		String[] Pezzetti = Path.split("/");
		String DaCreare = "";

		for (int i = 0; i < Pezzetti.length; i++) {
			if (!Pezzetti[i].isEmpty()) {
				DaCreare += "/" + Pezzetti[i];
				File newFolder = new File(DaCreare);
				if (!newFolder.exists()) {
					newFolder.mkdir();
				}
			}
		}
	}

}
