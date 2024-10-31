package com.looigi.wallpaperchanger2.classeOnomastici.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeOnomastici.VariabiliStaticheOnomastici;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GestioneDB {
	// private String DB_PATH = "/data/data/com.looigi.onomastici4/databases/";
	// private String DB_NAME = "Onomastici.db";
	private Context context;

	public GestioneDB(Context context) {
		this.context = context;
	}

	public SQLiteDatabase ApreDB(TextView tRoutine, TextView tErrore, String tChiamante, TextView tChiamante2) {
		SQLiteDatabase db=null;

		String DB_PATH = context.getFilesDir() + "/DB/";
		String DB_NAME =  "dati_onomastici.db";

		try {
           db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
       } catch (Exception e) {
    	   db=null;
    	   tChiamante2.setText(tChiamante);
    	   tRoutine.setText("ApreDB");
    	   tErrore.setText(e.getMessage());
       }

       return db;
	}

	public SQLiteDatabase ApreDB() {
		SQLiteDatabase db=null;

		String DB_PATH = context.getFilesDir() + "/DB/";
		String DB_NAME =  "dati_onomastici.db";

		try {
           db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
       } catch (Exception e) {
    	   db=null;
       }
       
       return db;      
	}
	
	public boolean ChiudeDB(SQLiteDatabase db,TextView tRoutine, TextView tErrore, String tChiamante, TextView tChiamante2) {
		boolean Ok=true;
		
		try {
			db.close();
		} catch (Exception e) {
    	   tRoutine.setText("ChiudeDB");
    	   tErrore.setText(e.getMessage());
    	   tChiamante2.setText(tChiamante);
    	   Ok=false;
		}
		
		return Ok;
	}
	
	public boolean ChiudeDB(SQLiteDatabase db) {
		boolean Ok=true;
		
		try {
			db.close();
		} catch (Exception e) {
    	   Ok=false;
		}
		
		return Ok;
	}
	
	public boolean ControllaDB(TextView tRoutine, TextView tErrore, String tChiamante, TextView tChiamante2) {
		boolean Ok=true;
		
		boolean dbExist = checkDataBase(tRoutine, tErrore, tChiamante, tChiamante2);
		if(dbExist){
    		//do nothing - database already exist
    	}else{
			if (!copyDataBase(tRoutine, tErrore, tChiamante, tChiamante2)) {
				Ok=false;
			}
    	}
		
		return Ok;
	}
	
	private boolean checkDataBase(TextView tRoutine, TextView tErrore, String tChiamante, TextView tChiamante2){
    	SQLiteDatabase checkDB = null;

		String DB_PATH = context.getFilesDir() + "/DB/";
		String DB_NAME =  "dati_onomastici.db";

		try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
     	   tChiamante2.setText(tChiamante);
    	   tRoutine.setText("CheckDB");
     	   tErrore.setText(e.getMessage());
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }	
	
	private boolean copyDataBase(TextView tRoutine, TextView tErrore, String tChiamante, TextView tChiamante2) {
		boolean Ok=true;

		String DB_PATH = context.getFilesDir() + "/DB/";
		String DB_NAME = "dati_onomastici.db";

		try {
			File dDirectory = new File(DB_PATH);
			dDirectory.mkdirs();
		} catch (Exception ignored) {
			
		}
		
		try {
			InputStream myInput = VariabiliStaticheOnomastici.getInstance().getAssets().open("Onomastici.db");
	    	String outFileName = DB_PATH + DB_NAME;
	    	OutputStream myOutput = Files.newOutputStream(Paths.get(outFileName));
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
		} catch(IOException e) {
    	   tChiamante2.setText(tChiamante);
		   tRoutine.setText("CopiaDB1");
    	   tErrore.setText(e.getMessage());
    	   Ok=false;
		} catch(Exception e) {
    	   tChiamante2.setText(tChiamante);
		   tRoutine.setText("CopiaDB2");
    	   tErrore.setText(e.getMessage());
    	   Ok=false;
		}
		
		return Ok;
    }
	
	public boolean EliminaDB(TextView tRoutine, TextView tErrore, String tChiamante, TextView tChiamante2) {
		boolean Ok=true;

		String DB_PATH = context.getFilesDir() + "/DB/";
		String DB_NAME =  "dati_onomastici.db";

		try {
			VariabiliStaticheOnomastici.getInstance().getContext().deleteDatabase(DB_PATH + DB_NAME);
		} catch (Exception e) {
    	   tChiamante2.setText(tChiamante);
    	   tRoutine.setText("EliminaDB");
    	   tErrore.setText(e.getMessage());
    	   Ok=false;
		}
		
		return Ok;
	}
}
