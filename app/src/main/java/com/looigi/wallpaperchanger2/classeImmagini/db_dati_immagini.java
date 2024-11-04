package com.looigi.wallpaperchanger2.classeImmagini;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFilms.UtilityFilms;
import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeVideo.UtilityVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                        + " (LimiteInBytes VARCHAR, Random VARCHAR, SettingsAperto VARCHAR, "
                        + "FiltroCategoria VARCHAR, Filtro VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Categorie "
                        + "(idCategoria VARCHAR, Categoria VARCHAR, Alias VARCHAR, Tag VARCHAR);";

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

    public void EliminaCategorie() {
        if (myDB != null) {
            try {
                String sql = "Delete From Categorie";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per eliminazione categorie: " + e.getMessage());
            }
        } else {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public List<StrutturaImmaginiCategorie> LeggeCategorie() {
        List<StrutturaImmaginiCategorie> l = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Categorie", null);
                c.moveToFirst();
                do {
                    StrutturaImmaginiCategorie sic = new StrutturaImmaginiCategorie();
                    sic.setIdCategoria(Integer.parseInt(c.getString(0)));
                    sic.setCategoria(c.getString(1));
                    sic.setAlias(c.getString(2));
                    sic.setTag(c.getString(3));

                    l.add(sic);
                } while(c.moveToNext());
            } catch (Exception e) {
                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db categorie: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
        return l;
    }

    public void ScriveCategoria(StrutturaImmaginiCategorie C) {
        if (myDB != null) {
            try {
                String sql = "INSERT INTO "
                        + "Categorie "
                        + " VALUES ("
                        + "'" + C.getIdCategoria() + "',"
                        + "'" + C.getCategoria().replace("'", "''") + "',"
                        + "'" + C.getAlias().replace("'", "''") + "',"
                        + "'" + C.getTag().replace("'", "''") + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per categoria: " + e.getMessage());
            }
        } else {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
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
                        + "'" + (VariabiliStaticheMostraImmagini.getInstance().isSettingsAperto() ? "S" : "N") + "', "
                        + "'" + VariabiliStaticheMostraImmagini.getInstance().getFiltroCategoria().replace("'", "''") + "',"
                        + "'" + VariabiliStaticheMostraImmagini.getInstance().getFiltro().replace("'", "''") + "'"
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
        VariabiliStaticheMostraImmagini.getInstance().setFiltroCategoria("");
        VariabiliStaticheMostraImmagini.getInstance().setFiltro("");
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
                        VariabiliStaticheMostraImmagini.getInstance().setFiltroCategoria(c.getString(3));
                        VariabiliStaticheMostraImmagini.getInstance().setFiltro(c.getString(4));

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
