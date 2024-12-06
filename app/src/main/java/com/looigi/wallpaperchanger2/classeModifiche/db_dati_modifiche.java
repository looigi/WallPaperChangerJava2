package com.looigi.wallpaperchanger2.classeModifiche;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Modifiche;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Moduli;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Progetti;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Sezioni;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Stati;
import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class db_dati_modifiche {
    private static final String NomeMaschera = "DB_Immagini_MOD";

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

    public db_dati_modifiche(Context context) {
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
            String nomeDB = "dati_modifiche.db";
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
                        + "Progetti "
                        + "(idProgetto NUMERIC, Progetto VARCHAR, Eliminato VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Moduli "
                        + "(idProgetto NUMERIC, idModulo NUMERIC, Modulo VARCHAR, Eliminato VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Sezioni "
                        + "(idProgetto NUMERIC, idModulo NUMERIC, idSezione NUMERIC, Sezione VARCHAR, Eliminato VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Modifiche "
                        + "(idProgetto NUMERIC, idModulo NUMERIC, idSezione NUMERIC, idModifica NUMERIC, "
                        + "idStato NUMERIC, Modifica VARCHAR, Eliminato VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Stati "
                        + "(idStato NUMERIC, Stato VARCHAR);";
                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void InserisceNuovoProgetto(String Progetto) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Coalesce(Max(idProgetto), 0) + 1 FROM Progetti", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    int idProgetto = c.getInt(0);
                    c.close();

                    String sql = "Insert Into Progetti Values (" +
                            " " + idProgetto + "," +
                            "'" + Progetto.replace("'", "''") + "'," +
                            "'N')";
                    myDB.execSQL(sql);
                }
            } catch (SQLException e) {
                int a = 0;
            }
        } else {
        }
    }

    public void ModificaProgetto(int idProgetto, String Progetto) {
        if (myDB != null) {
            try {
                String sql = "Update Progetti Set " +
                        "Progetto='" + Progetto.replace("'", "''") + "' " +
                        "Where idProgetto=" + idProgetto;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void EliminaProgetto(int idProgetto) {
        if (myDB != null) {
            try {
                String sql = "Update Progetti Set " +
                        "Eliminato='S' " +
                        "Where idProgetto=" + idProgetto;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public List<Progetti> RitornaProgetti() {
        List<Progetti> lista = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Progetti Order By Progetto", null);
                c.moveToFirst();
                do {
                    try {
                        Progetti p = new Progetti();
                        p.setIdProgetto(c.getInt(0));
                        p.setProgetto(c.getString(1));

                        lista.add(p);
                    } catch (Exception ignored) {

                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
            }
        } else {
        }

        return lista;
    }

    public void InserisceNuovoModulo(int idProgetto, String Modulo) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Coalesce(Max(idModulo), 0) + 1 FROM Moduli " +
                        "Where idProgetto=" + idProgetto, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    int idModulo = c.getInt(0);
                    c.close();

                    String sql = "Insert Into Moduli Values (" +
                            " " + idProgetto + "," +
                            " " + idModulo + "," +
                            "'" + Modulo.replace("'", "''") + "'," +
                            "'N')";
                    myDB.execSQL(sql);
                }
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void ModificaModulo(int idProgetto, int idModulo, String Modulo) {
        if (myDB != null) {
            try {
                String sql = "Update Moduli Set " +
                        "Modulo='" + Modulo.replace("'", "''") + "' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void EliminaModulo(int idProgetto, int idModulo) {
        if (myDB != null) {
            try {
                String sql = "Update Moduli Set " +
                        "Eliminato='S' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public List<Moduli> RitornaModuli(int idProgetto) {
        List<Moduli> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Moduli Where idProgetto=" + idProgetto + " " +
                        "Order By Modulo", null);
                c.moveToFirst();
                do {
                    c.moveToFirst();
                    do {
                        try {
                            Moduli p = new Moduli();
                            p.setIdProgetto(c.getInt(0));
                            p.setIdModulo(c.getInt(1));
                            p.setModulo(c.getString(2));

                            lista.add(p);
                        } catch (Exception ignored) {

                        }
                    } while(c.moveToNext());
                } while(c.moveToNext());
            } catch (SQLException e) {
            }
        } else {
        }

        return lista;
    }

    public void InserisceNuovaSezione(int idProgetto, int idModulo, String Sezione) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Coalesce(Max(idSezione), 0) + 1 FROM Sezioni " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    int idSezione = c.getInt(0);
                    c.close();

                    String sql = "Insert Into Sezioni Values (" +
                            " " + idProgetto + "," +
                            " " + idModulo + "," +
                            " " + idSezione + "," +
                            "'" + Sezione.replace("'", "''") + "'," +
                            "'N')";
                    myDB.execSQL(sql);
                }
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void ModificaSezione(int idProgetto, int idModulo, int idSezione, String Sezione) {
        if (myDB != null) {
            try {
                String sql = "Update Sezioni Set " +
                        "Sezione='" + Sezione.replace("'", "''") + "' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo + " " +
                        "And idSezione=" + idSezione;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void EliminaSezione(int idProgetto, int idModulo, int idSezione) {
        if (myDB != null) {
            try {
                String sql = "Update Sezioni Set " +
                        "Eliminato='S' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo + " " +
                        "And idSezione=" + idSezione;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public List<Sezioni> RitornaSezioni(int idProgetto, int idModulo) {
        List<Sezioni> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Sezioni Where idProgetto=" + idProgetto + " " +
                        "And idModulo=" + idModulo + " Order By Sezione", null);
                c.moveToFirst();
                do {
                    try {
                        Sezioni p = new Sezioni();
                        p.setIdProgetto(c.getInt(0));
                        p.setIdModulo(c.getInt(1));
                        p.setIdSezione(c.getInt(2));
                        p.setSezione(c.getString(3));

                        lista.add(p);
                    } catch (Exception ignored) {

                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
            }
        } else {
        }

        return lista;
    }

    public void InserisceNuovaModifica(int idProgetto, int idModulo, int idSezione, String Modifica) {
        if (myDB != null) {
            try {
                String sql = "";

                Cursor c1 = myDB.rawQuery("SELECT * FROM Stati", null);
                if (c1.getCount() == 0) {
                    sql = "Insert Into Stati Values(0, 'Aperta')";
                    myDB.execSQL(sql);

                    sql = "Insert Into Stati Values(1, 'Da Controllare')";
                    myDB.execSQL(sql);

                    sql = "Insert Into Stati Values(2, 'Chiusa')";
                    myDB.execSQL(sql);

                    sql = "Insert Into Stati Values(3, 'In Dubbio')";
                    myDB.execSQL(sql);
                }
                c1.close();

                Cursor c = myDB.rawQuery("SELECT Coalesce(Max(idModifica), 0) + 1 FROM Modifiche " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo + " " +
                        "And idSezione=" + idSezione, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    int idModifica = c.getInt(0);
                    c.close();

                    sql = "Insert Into Modifiche Values (" +
                            " " + idProgetto + "," +
                            " " + idModulo + "," +
                            " " + idSezione + "," +
                            " " + idModifica + "," +
                            "'" + Modifica.replace("'", "''") + "'," +
                            "0," +
                            "'N')";
                    myDB.execSQL(sql);
                }
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void ModificaModifica(int idProgetto, int idModulo, int idSezione, int idModifica,
                                 String Modifica, int idStato) {
        if (myDB != null) {
            try {
                String sql = "Update Modifiche Set " +
                        "Modifica='" + Modifica.replace("'", "''") + "', " +
                        "idStato=" + idStato + ", " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo +
                        " And idSezione=" + idSezione + " And idModifica=" + idModifica;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void EliminaModifica(int idProgetto, int idModulo, int idSezione, int idModifica) {
        if (myDB != null) {
            try {
                String sql = "Update Modifiche Set " +
                        "Eliminato='S' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo +
                        " And idSezione=" + idSezione + " And idModifica=" + idModifica;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public List<Modifiche> RitornaModifiche(int idProgetto, int idModulo, int idSezione) {
        List<Modifiche> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                String Where = "";

                if (VariabiliStaticheModifiche.getInstance().getSwcSoloAperti().isChecked()) {
                    Where = "Where idStato = 0";
                }

                Cursor c = myDB.rawQuery("SELECT * FROM Modifiche Where " +
                        "idProgetto=" + idProgetto + " And idModulo=" + idModulo +
                        " And idSezione=" + idSezione + " " + Where + " Order By idModifica", null);
                c.moveToFirst();
                do {
                    try {
                        Modifiche p = new Modifiche();
                        p.setIdProgetto(c.getInt(0));
                        p.setIdModulo(c.getInt(1));
                        p.setIdSezione(c.getInt(2));
                        p.setIdModifica(c.getInt(3));
                        p.setModifica(c.getString(4));
                        p.setIdStato(c.getInt(5));

                        lista.add(p);
                    } catch (Exception ignored) {

                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
            }
        } else {
        }

        return lista;
    }

    public void InserisceNuovoStato(String Stato) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Coalesce(Max(idStato), 0) + 1 FROM Stati", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    int idStato = c.getInt(0);
                    c.close();

                    String sql = "Insert Into Stati Values (" +
                            " " + idStato + "," +
                            "'" + Stato.replace("'", "''") + "'," +
                            "'N')";
                    myDB.execSQL(sql);
                }
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void ModificaStato(int idStato, String Stato) {
        if (myDB != null) {
            try {
                String sql = "Update Stati Set " +
                        "Stato='" + Stato.replace("'", "''") + "' " +
                        "Where idStato=" + idStato;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public void EliminaStati(int idStato) {
        if (myDB != null) {
            try {
                String sql = "Update Stati Set " +
                        "Eliminato='S' " +
                        "Where idStato=" + idStato;
                myDB.execSQL(sql);
            } catch (SQLException e) {
            }
        } else {
        }
    }

    public List<Stati> RitornaStati() {
        List<Stati> lista = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Stati Order By Stato", null);
                c.moveToFirst();
                do {
                    try {
                        Stati p = new Stati();
                        p.setIdStato(c.getInt(0));
                        p.setStato(c.getString(1));

                        lista.add(p);
                    } catch (Exception ignored) {

                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
                int a = 0;
            }
        } else {
        }

        return lista;
    }
}
