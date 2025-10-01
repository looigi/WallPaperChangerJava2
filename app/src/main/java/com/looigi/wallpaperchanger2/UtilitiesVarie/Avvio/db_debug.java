package com.looigi.wallpaperchanger2.UtilitiesVarie.Avvio;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.Detector.UtilityDetector;
import com.looigi.wallpaperchanger2.Player.UtilityPlayer;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import java.io.File;

public class db_debug {
    private static final String NomeMaschera = "DB_Debug";

    // private final String PathDB = VariabiliStatiche.getInstance().getPercorsoDIR()+"/DB/";
    private String PathDB;
    private SQLiteDatabase myDB = null;
    private Context context;

    public boolean DbAperto() {
        if (myDB != null) {
            return true;
        } else {
            return false;
        }
    }

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

    public void ChiudeDB() {
        if (myDB != null) {
            myDB.close();
            myDB = null;
        }
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

    public boolean CreazioneTabelle() {
        try {
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Debug"
                        + " (Attivo VARCHAR);";

                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception ignored) {
            // Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + UtilityDetector.getInstance().PrendeErroreDaException(ignored));
            return false;
        }
    }

    public void ImpostaValoriDiDefault() {
        VariabiliStaticheStart.getInstance().setLogAttivo(true);
    }

    public int CaricaImpostazioni() {
        // UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Controllo apertura db");
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Debug", null);
                if (c.getCount() > 0) {
                    // UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Riga rilevata su db per impostazioni");
                    c.moveToFirst();
                    
                    VariabiliStaticheStart.getInstance().setLogAttivo(c.getString(0).equals("S"));

                    return 0;
                } else {
                    return -3;
                }
            } catch (Exception ignored) {
                return -2;
            }
        } else {
            return -1;
        }
    }
    
    public boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Debug");

                String sql = "INSERT INTO"
                        + " Debug"
                        + " VALUES ("
                        + "'" + (VariabiliStaticheStart.getInstance().isLogAttivo() ? "S" : "N") + "'"
                        + ") ";
                myDB.execSQL(sql);

                return true;
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db debug per impostazioni: " + e.getMessage());
                // PulisceDati();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                // CreazioneTabelle();
                return false;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
            return false;
        }
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public boolean PulisceDati() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db debug");
            try {
                myDB.execSQL("Drop Table Debug");
                return true;
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db debug: " + ignored.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }
}
