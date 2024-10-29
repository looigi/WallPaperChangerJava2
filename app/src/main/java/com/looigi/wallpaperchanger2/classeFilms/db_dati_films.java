package com.looigi.wallpaperchanger2.classeFilms;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFilms.UtilityFilms;
import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;

import java.io.File;

public class db_dati_films {
    private static final String NomeMaschera = "DB_Films";

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

    public db_dati_films(Context context) {
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
            String nomeDB = "dati_Films.db";
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
                        + " (Random VARCHAR, SettingsAperto VARCHAR, BarraVisibile VARCHAR, NumeroFrames VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "UltimoFilms"
                        + " (Films VARCHAR, idFilm VARCHAR);";

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

    public String CaricaFilms() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM UltimoFilms", null);
                if (c.getCount() > 0) {
                    UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per ultimo Films");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheFilms.getInstance().setUltimoLink(c.getString(0));
                        VariabiliStaticheFilms.getInstance().setIdUltimoFilms(Integer.parseInt(c.getString(1)));

                        return VariabiliStaticheFilms.getInstance().getUltimoLink();
                    } catch (Exception e) {
                        PulisceDatiV();
                        CreazioneTabelle();
                        return CaricaFilms();
                    }
                } else {
                    UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per ultimo Films. Imposto default");

                    return "";
                }
            } catch (Exception e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db ultimo Films: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiV();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return CaricaFilms();
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return "";
        }
    }

    public Boolean ScriveUltimoFilms() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From UltimoFilms");

                String sql = "INSERT INTO"
                        + " UltimoFilms"
                        + " VALUES ("
                        + "'" + VariabiliStaticheFilms.getInstance().getUltimoLink().replace("'", "''") + "', "
                        + "'" + VariabiliStaticheFilms.getInstance().getIdUltimoFilms() + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo Films: " + e.getMessage());
                PulisceDatiV();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveUltimoFilms();
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public Boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Impostazioni");

                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " VALUES ("
                        + "'" + VariabiliStaticheFilms.getInstance().getRandom() + "',"
                        + "'" + (VariabiliStaticheFilms.getInstance().isSettingsAperto() ? "S" : "N") + "',"
                        + "'" + (VariabiliStaticheFilms.getInstance().isBarraVisibile() ? "S" : "N") + "', "
                        + "'" + VariabiliStaticheFilms.getInstance().getNumeroFrames() + "' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                // PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveImpostazioni();
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public void ImpostaValoriDiDefault() {
        VariabiliStaticheFilms.getInstance().setRandom("S");
        VariabiliStaticheFilms.getInstance().setSettingsAperto(true);
        VariabiliStaticheFilms.getInstance().setBarraVisibile(true);
        VariabiliStaticheFilms.getInstance().setNumeroFrames(10);
    }

    public int CaricaImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheFilms.getInstance().setRandom(c.getString(0));
                        VariabiliStaticheFilms.getInstance().setSettingsAperto(c.getString(1).equals("S"));
                        VariabiliStaticheFilms.getInstance().setBarraVisibile(c.getString(2).equals("S"));
                        VariabiliStaticheFilms.getInstance().setNumeroFrames(Integer.parseInt(c.getString(3)));

                        return 0;
                    } catch (Exception e) {
                        UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore try db carica impostazioni: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));

                        return -4;
                    }
                } else {
                    UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");

                    return -3;
                }
            } catch (Exception e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -2;
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return -1;
        }
    }

    public boolean PulisceDati() {
        if (myDB != null) {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");

                return true;
            } catch (Exception ignored) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());

                return false;
            }
        } else {
            return false;
        }
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public void PulisceDatiV() {
        if (myDB != null) {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db ultimo Films");
            try {
                myDB.execSQL("Drop Table UltimoFilms");
            } catch (Exception ignored) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db ultimo Films: " + ignored.getMessage());
            }
        }
    }
}
