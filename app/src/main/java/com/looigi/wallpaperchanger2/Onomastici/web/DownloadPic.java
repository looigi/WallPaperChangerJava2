package com.looigi.wallpaperchanger2.Onomastici.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.Onomastici.VariabiliStaticheOnomastici;
import com.looigi.wallpaperchanger2.Onomastici.WidgetOnomastici;

public class DownloadPic {
	private String Directory = "";
	private String NomeFiletto = "";
	private String FileDaDownloadare = "";
	// private GestioneDB GestDB;
	// private GestioneRubrica GestRubr;
	// private ContentResolver Rubrica;
	private boolean ScaricaImmagine;
	private Context context;
	// private ProgressDialog mProgressDialog;
	private Activity act;

	public void startDownload(Activity act, Context context) {
		this.context = context;
		this.act = act;
		/* mProgressDialog = new ProgressDialog(DownloadPic.this);
		mProgressDialog.setMessage("Download immagine");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); */

		DownloadImmagineSanto d = new DownloadImmagineSanto();
		d.EsegueChiamata(
				context,
				this,
				ScaricaImmagine,
				FileDaDownloadare,
				Directory,
				NomeFiletto
		);
		// DownloadFile downloadFile = new DownloadFile(this);
		// downloadFile.execute(FileDaDownloadare);
	}

	public void updateWidget() {
		WidgetOnomastici w = new WidgetOnomastici();
		Intent intent = act.getIntent();
		w.onReceive(VariabiliStaticheOnomastici.getInstance().getContext(), intent);
	}

	public void setImmagine(ImageView NomeImm) {
		VariabiliStaticheOnomastici.getInstance().setImgView(NomeImm);
	}

	public void setScarica(boolean SiNo) {
		ScaricaImmagine = SiNo;
	}

	/* public void setRubrica(ContentResolver Rubr) {
		Rubrica = Rubr;
	}

	public void setGestioneRubrica(GestioneRubrica Rubrica) {
		GestRubr = Rubrica;
	}

	public void setGestioneDB(GestioneDB NomeDb) {
		GestDB = NomeDb;
	} */

	public void setDirectory(String NomeDir) {
		Directory = NomeDir;
	}

	public void setFileDaDown(String NomeFileURL) {
		FileDaDownloadare = NomeFileURL;
	}

	public void setFiletto(String NomeFile) {
		NomeFiletto = NomeFile;
	}
}