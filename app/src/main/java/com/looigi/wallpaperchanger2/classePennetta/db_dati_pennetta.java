package com.looigi.wallpaperchanger2.classePennetta;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;

import java.io.File;

public class db_dati_pennetta {
    private static final String NomeMaschera = "DBIMMAGINIPEN";

    // private final String PathDB = VariabiliStatiche.getInstance().getPercorsoDIR()+"/DB/";
    private String PathDB;
    private SQLiteDatabase myDB = null;
    private boolean Controlla = true;
    private Context context;

    public db_dati_pennetta(Context context) {
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
            String nomeDB = "dati_pennetta.db";
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
                        + "Impostazioni"
                        + " (LimiteInBytes VARCHAR"
                        + ");";

                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            // Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + UtilityDetector.getInstance().PrendeErroreDaException(ignored));
            int a = 0;
        }
    }

    public Boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Impostazioni");

                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " VALUES ("
                        + "'" + VariabiliStaticheMostraImmaginiPennetta.getInstance().getSecondiAttesa() + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveImpostazioni();
            }
        } else {
            UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public void CaricaImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheMostraImmaginiPennetta.getInstance().setSecondiAttesa(Integer.parseInt(c.getString(0)));
                    } catch (Exception e) {
                        VariabiliStaticheMostraImmaginiPennetta.getInstance().setSecondiAttesa(5000);
                    }
                } else {
                    UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");
                }
            } catch (Exception e) {
                UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public void PulisceDatiIMP() {
        if (myDB != null) {
            UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");
            } catch (Exception ignored) {
                UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());
            }
        }
    }
}
