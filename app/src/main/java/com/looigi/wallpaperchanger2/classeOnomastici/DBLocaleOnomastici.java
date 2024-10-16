package com.looigi.wallpaperchanger2.classeOnomastici;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeOnomastici.strutture.DatiColori;

public class DBLocaleOnomastici {
	
	public void CreaDB() {
    	SQLiteDatabase myDB= null;

    	try {
			myDB = VariabiliStaticheOnomastici.getInstance().getContext().openOrCreateDatabase("DatiLocali", 0, null);
			myDB.execSQL("CREATE TABLE IF NOT EXISTS Opzioni (Tipo text, Trasparenza int, Rosso int, Verde int, Blu int);");
			myDB.execSQL("CREATE TABLE IF NOT EXISTS Lingua (Lingua Text);");
			myDB.close();
    	} catch(Exception ignored) {

		} finally {
		   if (myDB != null){
			   myDB.close();
		   }
		}
	}
	   
	public void SalvaLingua(Context context, String Lingua) {
		SQLiteDatabase myDB= null;
    	
		myDB = context.openOrCreateDatabase("DatiLocali", 0, null);
	   	String Sql="Delete From Lingua;";
	   	myDB.execSQL(Sql);
	   	
	   	Sql="Insert Into Lingua Values ('" + Lingua + "');";
	   	myDB.execSQL(Sql);

	   	myDB.close();

		VariabiliStaticheOnomastici.getInstance().setLingua(Lingua);
	}

	public DatiColori PrendeOpzioni(Context context) {
		DatiColori dc=new DatiColori();
		
		SQLiteDatabase myDB= null;
		int Trasparenza;
		int Rosso;
		int Verde;
		int Blu;
		int TrasparenzaT;
		int RossoT;
		int VerdeT;
		int BluT;
		
		myDB = context.openOrCreateDatabase("DatiLocali", 0, null);
	   	String Sql="SELECT Tipo, Trasparenza, Rosso, Verde, Blu FROM Opzioni Where Tipo='BACKGROUND';";
		Cursor c = myDB.rawQuery(Sql , null);
		c.moveToFirst();
		try {
			Trasparenza=c.getInt(1);
			Rosso=c.getInt(2);
			Verde=c.getInt(3);
			Blu=c.getInt(4);
			c.close();
			
		   	Sql="SELECT Tipo, Trasparenza, Rosso, Verde, Blu FROM Opzioni Where Tipo='TESTO';";
			c = myDB.rawQuery(Sql , null);
			c.moveToFirst();
			TrasparenzaT=c.getInt(1);
			RossoT=c.getInt(2);
			VerdeT=c.getInt(3);
			BluT=c.getInt(4);
			
		} catch (Exception e) {
			Sql="Delete From Opzioni;";
			myDB.execSQL(Sql);

			Sql="Insert Into Opzioni Values ('BACKGROUND', 75,200,200,200);";
			myDB.execSQL(Sql);
			Sql="Insert Into Opzioni Values ('TESTO', 255 ,200, 0,0);";
			myDB.execSQL(Sql);

			Trasparenza=75;
			Rosso=200;
			Verde=200;
			Blu=200;

			TrasparenzaT=255;
			RossoT=200;
			VerdeT=0;
			BluT=0;
		}
		c.close();
		
		dc.setTrasparenza(Trasparenza);
		dc.setRosso(Rosso);
		dc.setVerde(Verde);
		dc.setBlu(Blu);
		
		dc.setTrasparenzaT(TrasparenzaT);
		dc.setRossoT(RossoT);
		dc.setVerdeT(VerdeT);
		dc.setBluT(BluT);
		
	   	Sql="SELECT * FROM Lingua;";
		c = myDB.rawQuery(Sql , null);
		c.moveToFirst();
		try {
			String Ritorno2;
			
			Ritorno2=c.getString(0);
			VariabiliStaticheOnomastici.getInstance().setLingua(Ritorno2);
		} catch (Exception e) {
		   	Sql="Delete From Lingua";
		   	myDB.execSQL(Sql);

		   	Sql="Insert Into Lingua Values ('INGLESE')";
		   	myDB.execSQL(Sql);

			VariabiliStaticheOnomastici.getInstance().setLingua("INGLESE");
		}
		c.close();

		myDB.close();

		return dc;
	}
	
	public void ScriveOpzioni(Context context, String Tipo, DatiColori dc) {
		SQLiteDatabase myDB= null;
		myDB = context.openOrCreateDatabase("DatiLocali", 0, null);
		
		int trasp;
		int rosso;
		int verde;
		int blu;
		
		if (Tipo.equals("BACKGROUND")) {
			trasp=dc.getTrasparenza();
			rosso=dc.getRosso();
			verde=dc.getVerde();
			blu=dc.getBlu();
		} else {
			trasp=dc.getTrasparenzaT();
			rosso=dc.getRossoT();
			verde=dc.getVerdeT();
			blu=dc.getBluT();
		}
		
	   	String Sql="Update Opzioni Set Trasparenza="+trasp+", Rosso="+rosso+", Verde="+verde+", Blu="+blu+" Where Tipo='"+Tipo+"';";
	   	myDB.execSQL(Sql);
	   	
	   	myDB.close();
	}
}
