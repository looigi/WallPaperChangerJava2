package com.looigi.wallpaperchanger2.classeFetekkie;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiCategorieFE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class db_dati_fetekkie {
    private static final String NomeMaschera = "DB_Immagini_PEN";

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

    public db_dati_fetekkie(Context context) {
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
            String nomeDB = "dati_Fetekkie.db";
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
                        + "FiltroCategoria VARCHAR, Filtro VARCHAR, RicercaPerVisura VARCHAR "
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
            UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Errore su creazione tabelle: " + e.getMessage());

            return false;
        }
    }

    public void EliminaCategorie() {
        if (myDB != null) {
            try {
                String sql = "Delete From Categorie";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per eliminazione categorie: " + e.getMessage());
            }
        } else {
            UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public List<StrutturaImmaginiCategorieFE> LeggeCategorie() {
        List<StrutturaImmaginiCategorieFE> l = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Categorie", null);
                c.moveToFirst();
                do {
                    StrutturaImmaginiCategorieFE sic = new StrutturaImmaginiCategorieFE();
                    sic.setIdCategoria(Integer.parseInt(c.getString(0)));
                    sic.setCategoria(c.getString(1));
                    sic.setAlias(c.getString(2));
                    sic.setTag(c.getString(3));

                    l.add(sic);
                } while(c.moveToNext());
            } catch (Exception e) {
                UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db categorie: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
        return l;
    }

    public void ScriveCategoria(StrutturaImmaginiCategorieFE C) {
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
                UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per categoria: " + e.getMessage());
            }
        } else {
            UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public Boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Impostazioni");

                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " VALUES ("
                        + "'" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSecondiAttesa() + "',"
                        + "'" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getRandom() + "',"
                        + "'" + (VariabiliStaticheMostraImmaginiFetekkie.getInstance().isSettingsAperto() ? "S" : "N") + "',"
                        + "'" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getFiltroCategoria().replace("'", "''") + "',"
                        + "'" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getFiltro().replace("'", "''") + "',"
                        + "'" + (VariabiliStaticheMostraImmaginiFetekkie.getInstance().isRicercaPerVisua() ? "S" : "N") + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                // PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveImpostazioni();
            }
        } else {
            UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public void ImpostaValoriDiDefault() {
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSecondiAttesa(5000);
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setRandom("S");
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSettingsAperto(true);
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setFiltroCategoria("");
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setFiltro("");
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setRicercaPerVisua(true);
    }

    public int CaricaImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSecondiAttesa(Integer.parseInt(c.getString(0)));
                        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setRandom(c.getString(1));
                        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSettingsAperto(c.getString(2).equals("S"));
                        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setFiltroCategoria(c.getString(3));
                        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setFiltro(c.getString(4));
                        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setRicercaPerVisua(c.getString(5).equals("S"));

                        return 0;
                    } catch (Exception e) {
                        UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Errore try db carica impostazioni: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));

                        return -4;
                    }
                } else {
                    UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");

                    return -3;
                }
            } catch (Exception e) {
                UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -2;
            }
        } else {
            UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

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
            UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");

                return true;
            } catch (Exception ignored) {
                UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());

                return false;
            }
        } else {
            return false;
        }
    }
}
