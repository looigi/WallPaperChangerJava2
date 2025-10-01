package com.looigi.wallpaperchanger2.Onomastici;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.Onomastici.db.GestioneDB;
import com.looigi.wallpaperchanger2.Onomastici.strutture.CampiRitornoSanti;
import com.looigi.wallpaperchanger2.Onomastici.web.DownloadPic;

import java.io.File;
import java.util.Calendar;

public class PrendeSantoClass {
	private ImageView imgView=null;
	private String[] Giorni=new String[8];
    private String NomeMese[]=new String[13];
        
    public CampiRitornoSanti PrendeSanto(Activity act, Context context, String Modalita) {
    	String Immagine="";
    	
		/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
		    Giorni[1]=new String("Sunday");
		    Giorni[2]=new String("Monday");
		    Giorni[3]=new String("Tuesday");
		    Giorni[4]=new String("Wednesday");
		    Giorni[5]=new String("Thursay");
		    Giorni[6]=new String("Friday");
		    Giorni[7]=new String("Saturday");

		    NomeMese[1]=new String("January");
		    NomeMese[2]=new String("February");
		    NomeMese[3]=new String("March");
		    NomeMese[4]=new String("April");
		    NomeMese[5]=new String("May");
		    NomeMese[6]=new String("June");
		    NomeMese[7]=new String("July");
		    NomeMese[8]=new String("August");
		    NomeMese[9]=new String("September");
		    NomeMese[10]=new String("October");
		    NomeMese[11]=new String("November");
		    NomeMese[12]=new String("December");
		} else { */
		    Giorni[1]=new String("Domenica");
		    Giorni[2]=new String("Lunedi");
		    Giorni[3]=new String("Martedi");
		    Giorni[4]=new String("Mercoledi");
		    Giorni[5]=new String("Giovedi");
		    Giorni[6]=new String("Venerdi");
		    Giorni[7]=new String("Sabato");

		    NomeMese[1]=new String("Gennaio");
		    NomeMese[2]=new String("Febbraio");
		    NomeMese[3]=new String("Marzo");
		    NomeMese[4]=new String("Aprile");
		    NomeMese[5]=new String("Maggio");
		    NomeMese[6]=new String("Giugno");
		    NomeMese[7]=new String("Luglio");
		    NomeMese[8]=new String("Agosto");
		    NomeMese[9]=new String("Settembre");
		    NomeMese[10]=new String("Ottobre");
		    NomeMese[11]=new String("Novembre");
		    NomeMese[12]=new String("Dicembre");
		// }
	    
	    CampiRitornoSanti Ritorno=new CampiRitornoSanti();
    	
        GestioneDB varDB=new GestioneDB(context);
   	
		SQLiteDatabase myDB= varDB.ApreDB();
	    if (myDB!=null) {
	    	String Sql="";
	    	String Santo="";
	    	String Sinonimo="";
	    	String DescSanto="";
	    	String DescSantoIngl="";
	    	Calendar Oggi = Calendar.getInstance();
	        int Giorno=Oggi.get(Calendar.DAY_OF_MONTH);
	        int Mese=Oggi.get(Calendar.MONTH)+1;
	        int Anno=Oggi.get(Calendar.YEAR);
	        int GiornoSett=Oggi.get(Calendar.DAY_OF_WEEK);
	        int NumSettimana=Oggi.get(Calendar.WEEK_OF_YEAR);
	        int NumGiorno=Oggi.get(Calendar.DAY_OF_YEAR);
	    	
	        // myDB = this.openOrCreateDatabase("Onomastici", MODE_PRIVATE, null);
			Cursor c ;
	        try {
			   	Sql="SELECT Santo, Sinonimo FROM Onomastici Where Giorno="+ Giorno + " And Mese=" + Mese + ";";
			   	c= myDB.rawQuery(Sql , null);
				c.moveToFirst();
				Santo=c.getString(0);
				// Santo=Santo.replace("/", "\n");
				Sinonimo=c.getString(1);
				if (Sinonimo==null) {
					Sinonimo="";
				}
				Sinonimo=Sinonimo.replace("'","''");
				c.close();
	        } catch (Exception e) {
	        	Santo="";
	        	Sinonimo="";
	        }

	        try {
		        Sql="SELECT Descrizione, DescrInglese FROM DatiSanti Where Giorno="+ Giorno + " And Mese=" + Mese + ";";
				c = myDB.rawQuery(Sql , null);
				c.moveToFirst();
				DescSanto=c.getString(0);
				DescSanto=DescSanto.replace("  ", " ");
				DescSanto=DescSanto.replace("s.", "***^^^***");
				DescSanto=DescSanto.replace("S.", "***^^^***");
				DescSanto=DescSanto.replace(". ", ".\n\n")+"\n   ";
				DescSanto=DescSanto.replace("***^^^***","S.");

				DescSantoIngl=c.getString(1);
				DescSantoIngl=DescSantoIngl.replace("  ", " ");
				DescSantoIngl=DescSantoIngl.replace("s.", "***^^^***");
				DescSantoIngl=DescSantoIngl.replace("S.", "***^^^***");
				DescSantoIngl=DescSantoIngl.replace(". ", ".\n\n")+"\n   ";
				DescSantoIngl=DescSantoIngl.replace("***^^^***","S.");
				c.close();
	        } catch (Exception e) {
				DescSanto="";
			}
		
			varDB.ChiudeDB(myDB);
	
			String StringaGiorno;
			String Dati;
			
			/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
				StringaGiorno=Giorni[GiornoSett]+ " " + Giorno + " " + NomeMese[Mese] + " " + Anno;
				Dati="Week: " + NumSettimana + "\nDay of the year: " + NumGiorno;
			} else { */
				StringaGiorno=Giorni[GiornoSett]+ " " + Giorno + " " + NomeMese[Mese] + " " + Anno;
				Dati="Settimana: " + NumSettimana + "\nGiorno dell'anno: " + NumGiorno;
			// }

			if (Modalita==null) {
				Immagine=ControllaEsistenzaImmagine(act, context, Giorno, Mese);
			}
			
			Ritorno.setCampo1(Santo);
			/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
				Ritorno.setCampo2(DescSantoIngl);
			} else { */
				Ritorno.setCampo2(DescSanto);
			// }
			Ritorno.setCampo3(StringaGiorno);
			Ritorno.setCampo4(Dati);
			Ritorno.setCampo5(Immagine);
			Ritorno.setCampo6(Sinonimo);
	    } else {
			/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
		    	Ritorno.setCampo1("ERROR");
			} else { */
		    	Ritorno.setCampo1("ERRORE");
			// }
	    }
        
		return Ritorno;
    }
    
    public boolean fileExistsInSD(String sFileName, String Percorso){
        String sFolder = Percorso ;
        String sFile=sFolder+"/"+sFileName;
        File file = new File(sFile);
        
        return file.exists();
    }

	public void setImmagine(ImageView NomeImm) {
		imgView=NomeImm;
	}
	
    private String ControllaEsistenzaImmagine(Activity act, Context context, int Giorno, int Mese) {
		String PercorsoDIR = context.getFilesDir() + "/Onomastici";
    	String NomeFile="Imm_"+Giorno+"_"+Mese;
    	String EstensioneFile=".jpg";
    	String EstensioneWeb=".Txt";
		boolean Esiste=true;
		
    	try {
			File dDirectory = new File(PercorsoDIR);
			dDirectory.mkdirs();
		} catch (Exception ignored) {
			
		}  
    	
    	if (!fileExistsInSD(NomeFile+EstensioneFile, PercorsoDIR)) {
    		Esiste=false;
    	} 
    	
		DownloadPic a = new DownloadPic();

		a.setDirectory(PercorsoDIR);
		a.setFiletto(NomeFile+EstensioneFile);
		a.setFileDaDown(VariabiliStaticheOnomastici.UrlImmagini + "Onomastici/"+NomeFile+EstensioneWeb);
		// a.setGestioneDB(varDB);
		// a.setGestioneRubrica(Rubr);
		// a.setRubrica(VariabiliStaticheOnomastici.getInstance().getRubrica());
		a.setImmagine(imgView);
    	if (!Esiste) {
    		a.setScarica(true);
    	} else {
    		a.setScarica(false);
            // VariabiliStatiche.getInstance().setNomeImmagineSantoPerWidget(PercorsoDIR+"/"+NomeFile+EstensioneFile);
            // Widgets1.settaImmagineWidget();
		}
		a.startDownload(act, context);
    	
    	return PercorsoDIR+"/"+NomeFile+EstensioneFile;
    }
}
