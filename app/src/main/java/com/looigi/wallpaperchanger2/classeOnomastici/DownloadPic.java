package com.looigi.wallpaperchanger2.classeOnomastici;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.looigi.wallpaperchanger2.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class DownloadPic {
	private String Directory = "";
	private String NomeFiletto="";
	private String FileDaDownloadare="";
	private GestioneDB GestDB;
	private GestioneRubrica GestRubr;
	private ContentResolver Rubrica;
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

		DownloadFile downloadFile = new DownloadFile(this);
		downloadFile.execute(FileDaDownloadare);
	}

	public void updateWidget() {
		WidgetOnomastici w=new WidgetOnomastici();
		Intent intent=act.getIntent();
		w.onReceive(VariabiliStaticheOnomastici.getInstance().getContext(), intent);
    }

	public void setImmagine(ImageView NomeImm) {
		VariabiliStaticheOnomastici.getInstance().setImgView(NomeImm);
	}
	    
	public void setScarica(boolean SiNo) {
		ScaricaImmagine=SiNo;
	}
	
	public void setRubrica(ContentResolver Rubr) {
		Rubrica=Rubr;
	}
	
	public void setGestioneRubrica(GestioneRubrica Rubrica) {
		GestRubr=Rubrica;
	}
    
	public void setGestioneDB(GestioneDB NomeDb) {
		GestDB=NomeDb;
	}
	
	public void setDirectory(String NomeDir) {
		Directory = NomeDir;
	}
	
	public void setFileDaDown(String NomeFileURL) {
		FileDaDownloadare = NomeFileURL;
	}
	
	public void setFiletto(String NomeFile) {
		NomeFiletto = NomeFile;
	}
	
	private class DownloadFile extends AsyncTask<String, Integer, String> {
		private DownloadPic d;

		public DownloadFile(DownloadPic d) {
			this.d = d;
		}

		@Override
	    protected String doInBackground(String... sUrl) {
	    	if (ScaricaImmagine) {
		        try {
		            URL url = new URL(sUrl[0]);
		            URLConnection connection = url.openConnection();
		            connection.connect();
		            // this will be useful so that you can show a typical 0-100% progress bar
		            int fileLength = connection.getContentLength();
	
		            // download the file
		            InputStream input = new BufferedInputStream(url.openStream());
		            OutputStream output = new FileOutputStream(Directory+"/"+NomeFiletto);
	
		            byte data[] = new byte[1024];
		            long total = 0;
		            int count;
		            while ((count = input.read(data)) != -1) {
		                total += count;
		                // publishing the progress....
		                publishProgress((int) (total * 100 / fileLength));
		                output.write(data, 0, count);
		            }
	
		            output.flush();
		            output.close();
		            input.close();
		        } catch (Exception ignored) {
		        }
	    	}

			d.updateWidget();

	        // Scarica la lista dei nomi per farla leggere al widget
	        int Quanti=0;
	        
	        List<String> Lista=GestRubr.RitornaTuttiINomi(Rubrica);
	    	SQLiteDatabase myDB= GestDB.ApreDB();
	        myDB.execSQL("Create Table If Not Exists Rubrica(Nome Varchar(100));");
	        try {
	            String Sql="SELECT Count(*) FROM Rubrica;";
	    		Cursor c = myDB.rawQuery(Sql , null);
	    		c.moveToFirst();
	    		Quanti=c.getInt(0);
	    		c.close();
		        if (Quanti!=Lista.size()) {
			    	myDB.execSQL("Delete From Rubrica;");
			        for (int i=0;i<Lista.size();i++) {
			        	myDB.execSQL("Insert Into Rubrica Values ('"+Lista.get(i).replace("'", "''")+"');");
			        }
		        }
		    } catch (Exception ignored) {
	        	
	        }

	        GestDB.ChiudeDB(myDB);
	        // Scarica la lista dei nomi per farla leggere al widget
	        
	        return null;
	    }
	
	    /* @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressDialog.show();
	    } */
	
	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        
	        try {
		        PrendeSantoClass ps=new PrendeSantoClass();
		        if (ps.fileExistsInSD(NomeFiletto, Directory)) {
					VariabiliStaticheOnomastici.getInstance().getImgView().setImageBitmap(BitmapFactory.decodeFile(Directory+"/"+NomeFiletto));
					// Widgets1.settaImmagineWidget();
		        }
	        } catch (Exception ignored) {
	        	
	        }
	    }
	}
    
}
