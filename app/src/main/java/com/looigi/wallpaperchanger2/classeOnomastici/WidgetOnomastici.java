package com.looigi.wallpaperchanger2.classeOnomastici;

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
import com.looigi.wallpaperchanger2.classeOnomastici.db.DBLocaleOnomastici;
import com.looigi.wallpaperchanger2.classeOnomastici.db.GestioneDB;
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.CampiRitornoSanti;
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.DatiColori;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.Calendar;

public class WidgetOnomastici extends AppWidgetProvider {
    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                    final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        dostuff(context, appWidgetManager, appWidgetIds);
    }
	
    @Override
    public void onReceive (final Context context, Intent intent) {
    	super.onReceive(context, intent);

 		final AppWidgetManager awm=AppWidgetManager.getInstance(context);
		final int[] appWidgetIds=awm.getAppWidgetIds(new ComponentName(context, WidgetOnomastici.class));
		
		onUpdate(context, awm, appWidgetIds);
    }

    private void dostuff(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	// VariabiliStatiche.getInstance().setWidgetContext(context);
        // VariabiliStatiche.getInstance().setAppWidgetManager(appWidgetManager);
        // VariabiliStatiche.getInstance().setAppWidgetIds(appWidgetIds);
        // String Lingua=""+VariabiliStaticheOnomastici.getInstance().getLingua();
    	// if (Lingua.equals("")) {
    	// 	Lingua="ITALIANO";
    	// }
    	
    	DatiColori dc = null;
    	
		DBLocaleOnomastici dbl=new DBLocaleOnomastici();
		dbl.CreaDB();
		dc=dbl.PrendeOpzioni(context);
    	
    	String NomeSanto="";
    	String AppoSanto="";
    	String Ritorno="";
    	String Sinonimi="";
    	String QueryWhere="";
    	
    	int N = appWidgetIds.length;
        
		PrendeSantoClass Santo=new PrendeSantoClass();
		CampiRitornoSanti p = new CampiRitornoSanti();
		Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
		p=Santo.PrendeSanto(act, context,"NonCaricareImmagine");
		
		String Giorno=p.getGiorno();
		if (Giorno != null) {
			int Spazio = Giorno.indexOf(" ");
			String Datella = Giorno.substring(Spazio + 1, Giorno.length());
			Giorno = Giorno.substring(0, 3) + " " + Datella;
			NomeSanto = p.getNomeSanto();
			AppoSanto = NomeSanto;
			Sinonimi = p.getSinonimo();
		}

		int Quanti=0;
		
		GestioneDB GestDB=new GestioneDB(context);
		SQLiteDatabase myDB= GestDB.ApreDB();
		
		if (NomeSanto.contains("/")) {
			String Santo2="";
			while (NomeSanto.contains("/")) {
				Santo2=NomeSanto.substring(0,NomeSanto.indexOf("/")-1).trim().toUpperCase().replace("'", "''");
				QueryWhere+=" Nome Like '%"+Santo2+"%' Or ";
				NomeSanto=NomeSanto.substring(NomeSanto.indexOf("/")+1,NomeSanto.length());
			}
			QueryWhere+=" Nome Like '%"+NomeSanto.replace("'", "''")+"%' Or ";
		} else {
			QueryWhere+=" Nome Like '%"+NomeSanto.replace("'", "''")+"%' Or ";
		}
		if (Sinonimi.contains("/")) {
			String Santo2="";
			while (Sinonimi.contains("/")) {
				Santo2=Sinonimi.substring(0,Sinonimi.indexOf("/")-1).trim().toUpperCase().replace("'", "''");
				QueryWhere+=" Nome Like '%"+Santo2+"%' Or ";
				Sinonimi=Sinonimi.substring(Sinonimi.indexOf("/")+1,Sinonimi.length());
			}
			QueryWhere+=" Nome Like '%"+Sinonimi.replace("'", "''")+"%' Or ";
		} else {
			if (Sinonimi.length()>0) {
				QueryWhere+=" Nome Like '%"+Sinonimi.replace("'", "''")+"%' Or ";
			}
		}
		QueryWhere="Where "+QueryWhere.substring(0,QueryWhere.length()-4);
		
        /* try {
        	String Sql="SELECT Count(*) FROM Rubrica "+QueryWhere+";";
    		Cursor c = myDB.rawQuery(Sql , null);
    		c.moveToFirst();
    		Quanti=c.getInt(0);
    		c.close();
        } catch (Exception ignored) {
        	
        } */
		GestDB.ChiudeDB(myDB);
		
		for (int i = 0; i < N; i++) {
            int awID = appWidgetIds[i];
	        RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.widget_onomastici);

	        rv.setInt(R.id.layWidget, "setBackgroundColor", Color.argb(dc.getTrasparenza(), dc.getRosso(), dc.getVerde(), dc.getBlu()));
    		rv.setTextColor(R.id.txtWSanto, Color.argb(dc.getTrasparenzaT(), dc.getRossoT(), dc.getVerdeT(), dc.getBluT()));
    		rv.setTextColor(R.id.txtWData, Color.argb(dc.getTrasparenzaT(), dc.getRossoT(), dc.getVerdeT(), dc.getBluT()));
    		rv.setTextColor(R.id.txtWRilevati, Color.argb(dc.getTrasparenzaT(), dc.getRossoT(), dc.getVerdeT(), dc.getBluT()));

    		String s="";
    		if (AppoSanto.contains("/")) {
    			s="Ss. ";	
    		} else {
    			s="S. ";	
    		}
    		
    		rv.setTextViewText(R.id.txtWSanto, s+AppoSanto);
    		rv.setTextViewText(R.id.txtWData,  Giorno);
            if (Quanti>0) {
        		/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
        			Ritorno=" There are recurrences in address book: "+Quanti+" ";
        		} else { */
				String rico = "Ricorrenze rilevate: " + Quanti;
				if (!VariabiliStaticheOnomastici.getInstance().getCompleanni().isEmpty()) {
					rico += "\nCompleanni: " + VariabiliStaticheOnomastici.getInstance().getCompleanni().size();
				}

				Ritorno=rico; // " Ci sono ricorrenze in rubrica: "+Quanti+" ";
        		// }
            } else {
				String rico = "";
				if (!VariabiliStaticheOnomastici.getInstance().getCompleanni().isEmpty()) {
					rico = "Compleanni: " + VariabiliStaticheOnomastici.getInstance().getCompleanni().size();
				}
				Ritorno=rico;
			}
            rv.setTextViewText(R.id.txtWRilevati,  Ritorno);

			Calendar Oggi = Calendar.getInstance();
			int Mese=Oggi.get(Calendar.MONTH)+1;
			int Giorno2=Oggi.get(Calendar.DAY_OF_MONTH);

			String PercorsoDIR = context.getFilesDir() + "/Onomastici";
			String NomeFile="Imm_"+Giorno2+"_"+Mese;
			String EstensioneFile=".jpg";
			String sNomeFile = PercorsoDIR + "/" + NomeFile + EstensioneFile;

			if (Files.getInstance().EsisteFile(sNomeFile)) {
				Bitmap bmImg = BitmapFactory.decodeFile(sNomeFile);
				rv.setImageViewBitmap(R.id.imgSantoWidget, bmImg);
			} else {
				Bitmap imm = BitmapFactory.decodeResource(context.getResources(), R.drawable.onomastici);
				rv.setImageViewBitmap(R.id.imgSantoWidget, imm);
			}

			Intent intent = new Intent(context, MainOnomastici.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            rv.setOnClickPendingIntent(R.id.txtWSanto, pendingIntent);
            rv.setOnClickPendingIntent(R.id.txtWData, pendingIntent);
			// rv.setOnClickPendingIntent(R.id.imgSantoWidget, pendingIntent);
            // rv.setOnClickPendingIntent(R.id.txtWTitolo, pendingIntent);
            
	        appWidgetManager.updateAppWidget(awID, rv);
    	}

    	// settaImmagineWidget();
	}

	/* public static boolean settaImmagineWidget() {
    	boolean Ok = false;

    	if (VariabiliStatiche.getInstance().getWidgetContext() != null) {
			RemoteViews rv = new RemoteViews(VariabiliStatiche.getInstance().getWidgetContext().getPackageName(), R.layout.widget);
			String ns = VariabiliStatiche.getInstance().getNomeImmagineSantoPerWidget();
			if (ns != null) {
                if (!ns.isEmpty()) {
                    Bitmap b = BitmapFactory.decodeFile(ns);
                    rv.setBitmap(R.id.imgSantoWidget, "setImageBitmap", b);

                    int N = VariabiliStatiche.getInstance().getAppWidgetIds().length;
                    for (int i = 0; i < N; i++) {
                        int awID = VariabiliStatiche.getInstance().getAppWidgetIds()[i];

                        VariabiliStatiche.getInstance().getAppWidgetManager().updateAppWidget(awID, rv);
                    }

                    Ok = true;
                }
            }
		}

		return Ok;
	} */

	@Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

		/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
	        Toast.makeText(context, "It was nice... To the next", Toast.LENGTH_SHORT)
	        	.show();
		} else {
	        Toast.makeText(context, "E' stato bello... Alla prossima...", Toast.LENGTH_SHORT)
            	.show();
		} */
    }
}
