package com.looigi.wallpaperchanger2.classeFilms;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFilms.UtilityFilms;
import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.classeVideo.UtilityVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                        + " (Random VARCHAR, SettingsAperto VARCHAR, BarraVisibile VARCHAR, "
                        + "NumeroFrames VARCHAR, FiltroCategorie VARCHAR, Filtro VARCHAR, RicercaPerVisura VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "UltimoFilms"
                        + " (Films VARCHAR, idFilm VARCHAR);";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Snapshots "
                        + "(id VARCHAR);";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Categorie "
                        + "(Categoria VARCHAR);";

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

    public List<String> LeggeCategorie() {
        List<String> l = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Categorie", null);
                c.moveToFirst();
                do {
                    l.add(c.getString(0));
                } while(c.moveToNext());
            } catch (Exception e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db categorie: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
        return l;
    }

    public void EliminaCategorie() {
        if (myDB != null) {
            try {
                String sql = "Delete From Categorie";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per eliminazione categorie: " + e.getMessage());
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public void ScriveCategoria(String Categoria) {
        if (myDB != null) {
            try {
                String sql = "INSERT INTO "
                        + "Categorie "
                        + " VALUES ("
                        + "'" + Categoria.replace("'", "''") + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per categoria: " + e.getMessage());
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public boolean VedeSnapshot(String id) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Snapshots Where id=" + id, null);
                if (c.getCount() > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db snapshots: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                return false;
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return false;
        }
    }

    public void ScriveSnapshot(String id) {
        if (myDB != null) {
            try {
                String sql = "INSERT INTO"
                        + "Snapshots "
                        + " VALUES ("
                        + "'" + id + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per snapshot: " + e.getMessage());
            }
        } else {
            UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
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
                        + "'" + VariabiliStaticheFilms.getInstance().getNumeroFrames() + "', "
                        + "'" + VariabiliStaticheFilms.getInstance().getFiltroCategoria().replace("'","''") + "', "
                        + "'" + VariabiliStaticheFilms.getInstance().getFiltro().replace("'","''") + "', "
                        + "'" + (VariabiliStaticheFilms.getInstance().isRicercaPerVisua() ? "S" : "N") + "' "
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
        VariabiliStaticheFilms.getInstance().setBarraVisibile(false);
        VariabiliStaticheFilms.getInstance().setNumeroFrames(10);
        VariabiliStaticheFilms.getInstance().setFiltroCategoria("");
        VariabiliStaticheFilms.getInstance().setFiltro("");
        VariabiliStaticheFilms.getInstance().setRicercaPerVisua(true);
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
                        VariabiliStaticheFilms.getInstance().setFiltroCategoria(c.getString(4));
                        VariabiliStaticheFilms.getInstance().setFiltro(c.getString(5));
                        VariabiliStaticheFilms.getInstance().setRicercaPerVisua(c.getString(6).equals("S"));

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
