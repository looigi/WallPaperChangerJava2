package com.looigi.wallpaperchanger2.classeOnomastici;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RicercaSanto {
	
	public int EffettuaRicercaSanto(Context context, TextView tRitorno, EditText rSanto) {
    	int Quanti=0;
    	String NomeMese[]=new String[13];

		/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
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

		String Ricerca = rSanto.getText().toString();
		if (Ricerca.length()==0) {
			Toast toast;
			/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
				toast=Toast.makeText(VariabiliStaticheOnomastici.getInstance().getContext(),"Insert a name to search",Toast.LENGTH_LONG);
			} else { */
				toast=Toast.makeText(VariabiliStaticheOnomastici.getInstance().getContext(),"Immettere un nome da ricercare",Toast.LENGTH_LONG);
			// }
			toast.show();			
		} else {
	        GestioneDB varDB=new GestioneDB(context);
	       	
	    	SQLiteDatabase myDB= varDB.ApreDB();
	    	if (myDB!=null) {
		    	String Sql="";
		    	String Ritorno="";
		    	String Mese="";
		    	String Nome="";
		    	Cursor c = null ;
		    	
	    		try {
	    			Sql="Select Giorno, Mese, Santo From Onomastici Where Santo Like '%" + Ricerca + "%' Order By Mese, Giorno;";
		    		c= myDB.rawQuery(Sql , null);
		    		c.moveToFirst();
		    		Quanti=0;
	    			Mese= NomeMese[c.getInt(c.getColumnIndex("Mese"))];
	    			Nome=c.getString(c.getColumnIndex("Santo"));
	    			Ritorno+=Nome + ": " +c.getInt(c.getColumnIndex("Giorno")) + " " + Mese + "\n";
	    			Quanti++;
	    		} catch (Exception ignored) {
	    			
	    		}
	    		try {
	    			while (c.moveToNext()) {
		    			Quanti++;
		    			Mese= NomeMese[c.getInt(c.getColumnIndex("Mese"))];
		    			Nome=c.getString(c.getColumnIndex("Santo"));
		    			Ritorno+=Nome + ": " +c.getInt(c.getColumnIndex("Giorno")) + " " + Mese + "\n";
		            }; 
	    		} catch (Exception ignored) {
	    			
	    		}
				if (c != null) {
					c.close();
				}
	    		
	    		if (Quanti==0) {
		    		tRitorno.setText("");
	    		} else {
		    		tRitorno.setText(Ritorno);
	    		}
	    		
	    		varDB.ChiudeDB(myDB);
	    	}
		}
		return Quanti;
	}
	
	public int RicercaSantoPerData(Context context, TextView tRitorno, int Giorno, int Mese1) {
    	int Quanti=0;
    	String NomeMese[]=new String[13];

		/* if (VariabiliStaticheOnomastici.getInstance().getLingua().equals("INGLESE")) {
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

        GestioneDB varDB=new GestioneDB(context);
       	
    	SQLiteDatabase myDB= varDB.ApreDB();
    	if (myDB!=null) {
	    	String Sql="";
	    	String Ritorno="";
	    	String Mese="";
	    	String Nome="";
	    	Cursor c = null ;
	    	
    		try {
    			Sql="Select Giorno, Mese, Santo From Onomastici Where Giorno="+Giorno+" And Mese="+Mese1+";";
	    		c= myDB.rawQuery(Sql , null);
	    		c.moveToFirst();
	    		Quanti=0;
    			Mese= NomeMese[c.getInt(c.getColumnIndex("Mese"))];
    			Nome=c.getString(c.getColumnIndex("Santo"));
    			Ritorno+=Nome + ": " +c.getInt(c.getColumnIndex("Giorno")) + " " + Mese + "\n";
    			Quanti++;
    		} catch (Exception ignored) {
    			
    		}
    		try {
    			while (c.moveToNext()) {
	    			Quanti++;
	    			Mese= NomeMese[c.getInt(c.getColumnIndex("Mese"))];
	    			Nome=c.getString(c.getColumnIndex("Santo"));
	    			Ritorno+=Nome + ": " +c.getInt(c.getColumnIndex("Giorno")) + " " + Mese + "\n";
	            }; 
    		} catch (Exception ignored) {
    			
    		}
    		c.close();
    		
    		if (Quanti==0) {
	    		tRitorno.setText("");
    		} else {
	    		tRitorno.setText(Ritorno);
    		}
    		
    		varDB.ChiudeDB(myDB);
    	}

		return Quanti;
	}
}
