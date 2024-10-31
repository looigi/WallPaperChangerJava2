package com.looigi.wallpaperchanger2.classeImmagini;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;

import java.io.File;

public class db_dati_immagini {
    private static final String NomeMaschera = "DB_Immagini";

    // private final String PathDB = VariabiliStatiche.getInstance().getPercorsoDIR()+"/DB/";
    private String PathDB;
    private SQLiteDatabase myDB = null;
    private boolean Controlla = true;
    private Context context;

    public boolean DbAperto() {
        if (myDB != null) {
            return true;
        } else {
            return false;
        }
    }

    public db_dati_immagini(Context context) {
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
            String nomeDB = "dati_immagini.db";
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
                        + "Impostazioni"
                        + " (LimiteInBytes VARCHAR, Random VARCHAR, SettingsAperto VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore su creazione tabelle: " + e.getMessage());

            return false;
        }
    }

    public Boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Impostazioni");

                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " VALUES ("
                        + "'" + VariabiliStaticheMostraImmagini.getInstance().getSecondiAttesa() + "',"
                        + "'" + VariabiliStaticheMostraImmagini.getInstance().getRandom() + "',"
                        + "'" + (VariabiliStaticheMostraImmagini.getInstance().isSettingsAperto() ? "S" : "N") + "'"
                        + ") ";
                myDB.execSQL(sql);

                return true;
            } catch (SQLException e) {
                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                // PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                // CreazioneTabelle();
                return false;
            }
        } else {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return false;
        }
    }

    public void ImpostaValoriDiDefault() {
        VariabiliStaticheMostraImmagini.getInstance().setSecondiAttesa(5000);
        VariabiliStaticheMostraImmagini.getInstance().setRandom("S");
        VariabiliStaticheMostraImmagini.getInstance().setSettingsAperto(true);
    }

    public int CaricaImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheMostraImmagini.getInstance().setSecondiAttesa(Integer.parseInt(c.getString(0)));
                        VariabiliStaticheMostraImmagini.getInstance().setRandom(c.getString(1));
                        VariabiliStaticheMostraImmagini.getInstance().setSettingsAperto(c.getString(2).equals("S"));

                        return 0;
                    } catch (Exception e) {
                        UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore try db carica impostazioni: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));

                        return -4;
                    }
                } else {
                    UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");

                    return -3;
                }
            } catch (Exception e) {
                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -2;
            }
        } else {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
            return -1;
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
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");

                return true;
            } catch (Exception ignored) {
                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());

                return false;
            }
        } else {
            return false;
        }
    }
}
