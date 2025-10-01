package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.Detector.UtilityDetector;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.strutture.StrutturaCategorieSpostamento;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class db_dati_spostamento {
    private static final String NomeMaschera = "DB_Spostamento";

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

    public db_dati_spostamento(Context context) {
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
            String nomeDB = "dati_spostamento.db";
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
                        + "Preferiti"
                        + " (Preferito VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Categorie"
                        + " (idCategoria int, Categoria VARCHAR, Immagini int"
                        + ");";

                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore su creazione tabelle: " + e.getMessage());

            return false;
        }
    }

    public String LeggeCategorie() {
        String Ritorno = "";
        List<StrutturaCategorieSpostamento> Categorie = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Categorie", null);
                c.moveToFirst();
                do {
                    StrutturaCategorieSpostamento s = new StrutturaCategorieSpostamento();
                    s.setIdCategoria(c.getInt(0));
                    s.setCategoria(c.getString(1));
                    s.setImmaginiContenute(c.getInt(2));

                    Categorie.add(s);
                } while(c.moveToNext());
                VariabiliStaticheSpostamento.getInstance().setListaCategorie(Categorie);
            } catch (Exception e) {
                // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db categoria: " +
                //         UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return Ritorno;
    }

    public String LeggePreferiti() {
        String Ritorno = "";
        List<String> Preferiti = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Preferiti", null);
                c.moveToFirst();
                do {
                    Preferiti.add(c.getString(0));
                } while(c.moveToNext());
                VariabiliStaticheSpostamento.getInstance().setPreferiti(Preferiti);
            } catch (Exception e) {
                // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db categoria: " +
                //         UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return Ritorno;
    }

    public void ScriveCategorie() {
        if (myDB != null) {
            String sql = "Delete from Categorie";
            myDB.execSQL(sql);

            for (StrutturaCategorieSpostamento s: VariabiliStaticheSpostamento.getInstance().getListaCategorie()) {
                try {
                    sql = "INSERT INTO "
                            + "Categorie "
                            + " VALUES ("
                            + " " + s.getIdCategoria() + ","
                            + "'" + s.getCategoria().replace("'", "''") + "',"
                            + " " + s.getImmaginiContenute() + " " +
                            ") ";
                    myDB.execSQL(sql);
                } catch (SQLException e) {
                    // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per categoria: " + e.getMessage());
                }
            }
        } else {
            // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public void ScrivePreferito(String Categoria) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Preferiti Where preferito='" + Categoria + "'", null);
                if (c.moveToFirst()) {
                    // Esiste almeno una riga
                    UtilitiesGlobali.getInstance().ApreToast(context, "Preferito già esistente");
                    c.close();
                    return;
                }
                c.close();
            } catch (Exception e) {
                // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db categoria: " +
                //         UtilityDetector.getInstance().PrendeErroreDaException(e));
            }

            try {
                String sql = "INSERT INTO "
                        + "Preferiti "
                        + " VALUES ("
                        + "'" + Categoria.replace("'", "''") + "'" +
                        ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per categoria: " + e.getMessage());
            }
        } else {
            // // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public void EliminaPreferito(String Categoria) {
        if (myDB != null) {
            try {
                String sql = "Delete From Preferiti Where Preferito='" + Categoria + "'";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per eliminazione categorie: " + e.getMessage());
            }
        } else {
            // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
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
            // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");

                return true;
            } catch (Exception ignored) {
                // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());

                return false;
            }
        } else {
            return false;
        }
    }
}
