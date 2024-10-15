package com.looigi.wallpaperchanger2.classeStandard;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.File;

public class db_debug {
    private static final String NomeMaschera = "DBDEBUG";

    // private final String PathDB = VariabiliStatiche.getInstance().getPercorsoDIR()+"/DB/";
    private String PathDB;
    private SQLiteDatabase myDB = null;
    private Context context;

    public db_debug(Context context) {
        this.context = context;
        PathDB = UtilityDetector.getInstance().PrendePathDB(context);

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception ignored) {

        }
        myDB = ApreDB();
    }

    private SQLiteDatabase ApreDB() {
        SQLiteDatabase db = null;

        UtilityDetector.getInstance().CreaCartelle(PathDB, "DB");

        try {
            String nomeDB = "dati_debug.db";
            db = context.openOrCreateDatabase(
                    PathDB + nomeDB, 0, null);
        } catch (Exception e) {
            // Log.getInstance().ScriveLog("ERRORE Nell'apertura del db: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
            int a = 0;
        }
        return  db;
    }

    public void CreazioneTabelle() {
        try {
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Debug"
                        + " (Attivo VARCHAR);";

                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            // Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + UtilityDetector.getInstance().PrendeErroreDaException(ignored));
            int a = 0;
        }
    }

    public void CaricaImpostazioni() {
        // UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Controllo apertura db");
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Debug", null);
                if (c.getCount() > 0) {
                    // UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Riga rilevata su db per impostazioni");
                    c.moveToFirst();
                    
                    VariabiliStaticheStart.getInstance().setLogAttivo(c.getString(0).equals("S"));
                }
            } catch (Exception ignored) {

            }
        }
    }
    
    public Boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Debug");

                String sql = "INSERT INTO"
                        + " Debug"
                        + " VALUES ("
                        + "'" + (VariabiliStaticheStart.getInstance().isLogAttivo() ? "S" : "N") + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db debug per impostazioni: " + e.getMessage());
                PulisceDati();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveImpostazioni();
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public void PulisceDati() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db debug");
            try {
                myDB.execSQL("Drop Table Debug");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db debug: " + ignored.getMessage());
            }
        }
    }
}
