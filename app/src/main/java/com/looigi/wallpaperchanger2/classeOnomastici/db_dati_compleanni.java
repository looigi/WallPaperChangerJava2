package com.looigi.wallpaperchanger2.classeOnomastici;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.StrutturaCompleanno;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class db_dati_compleanni {
    private static final String NomeMaschera = "DB_Immagini_COM";

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

    public db_dati_compleanni(Context context) {
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
            String nomeDB = "dati_compleanni.db";
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
                        + "Compleanni"
                        + " (id NUMBER, Giorno VARCHAR, MESE VARCHAR, Anno VARCHAR, Nome VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore su creazione tabelle: " + e.getMessage());

            return false;
        }
    }

    public void ScriveCompleanno(StrutturaCompleanno s) {
        if (myDB != null) {
            try {
                int id = 0;

                Cursor c = myDB.rawQuery("SELECT Coalesce(Max(id), 0)+1 FROM Compleanni", null);
                if (c.getCount() > 0) {
                    // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    id = c.getInt(0);
                }
                c.close();

                String sql = "INSERT INTO"
                        + " Compleanni"
                        + " VALUES ("
                        + " " + id + ", "
                        + "'" + s.getGiorno() + "',"
                        + "'" + s.getMese() + "',"
                        + "'" + s.getAnno() + "',"
                        + "'" + s.getNome().replace("'", "''") + "' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                PulisceDati();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                ScriveCompleanno(s);
            }
        } else {
            // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        // return true;
    }

    public void ModificaCompleanno(StrutturaCompleanno s) {
        if (myDB != null) {
            try {
                String sql = "Update "
                        + "Compleanni "
                        + "Set "
                        + "Giorno='" + s.getGiorno() + "',"
                        + "Mese='" + s.getMese() + "',"
                        + "Anno='" + s.getAnno() + "',"
                        + "Nome='" + s.getNome().replace("'", "''") + "' "
                        + "Where id=" + s.getId();
                myDB.execSQL(sql);
            } catch (SQLException e) {
                // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                // PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                // CreazioneTabelle();
                // return ScriveCompleanno(s);
            }
        } else {
            // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public void EliminaCompleanno(StrutturaCompleanno s) {
        if (myDB != null) {
            try {
                String sql = "Delete From "
                        + "Compleanni "
                        + "Where id=" + s.getId();
                myDB.execSQL(sql);
            } catch (SQLException e) {
                // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                // PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                // CreazioneTabelle();
                // return ScriveCompleanno(s);
            }
        } else {
            // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public List<StrutturaCompleanno> CaricaCompleannoDelGiorno(int Giorno, int Mese) {
        List<StrutturaCompleanno> lista = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Compleanni Where Giorno='" + Giorno + "' And Mese='" + Mese + "'", null);
                if (c.getCount() > 0) {
                    // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        do {
                            StrutturaCompleanno s = new StrutturaCompleanno();
                            s.setId(c.getInt(0));
                            s.setGiorno(Integer.parseInt(c.getString(1)));
                            s.setMese(Integer.parseInt(c.getString(2)));
                            s.setAnno(Integer.parseInt(c.getString(3)));
                            s.setNome(c.getString(4));

                            lista.add(s);
                        } while (c.moveToNext());
                    } catch (Exception e) {
                        // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore try db carica impostazioni: " +
                        //         UtilityDetector.getInstance().PrendeErroreDaException(e));
                    }
                } else {
                    // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");
                }
            } catch (Exception e) {
                // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                //         UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return lista;
    }

    public List<StrutturaCompleanno> CaricaCompleannoDalNome(String Nome) {
        List<StrutturaCompleanno> lista = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Compleanni Where Nome Like '%" + Nome + "%'", null);
                if (c.getCount() > 0) {
                    // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        do {
                            StrutturaCompleanno s = new StrutturaCompleanno();
                            s.setId(c.getInt(0));
                            s.setGiorno(Integer.parseInt(c.getString(1)));
                            s.setMese(Integer.parseInt(c.getString(2)));
                            s.setAnno(Integer.parseInt(c.getString(3)));
                            s.setNome(c.getString(4));

                            lista.add(s);
                        } while (c.moveToNext());
                    } catch (Exception e) {
                        // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore try db carica impostazioni: " +
                        //  UtilityDetector.getInstance().PrendeErroreDaException(e));
                    }
                } else {
                    // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");
                }
            } catch (Exception e) {
                // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                //         UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            // UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return lista;
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
                myDB.execSQL("Drop Table Compleanni");

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
