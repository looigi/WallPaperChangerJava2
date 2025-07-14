package com.looigi.wallpaperchanger2.classeModificheCodice;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Modifiche;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Moduli;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Progetti;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Sezioni;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Stati;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.StrutturaConteggi;
import com.looigi.wallpaperchanger2.classeModificheCodice.webService.ChiamateWSModifiche;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class db_dati_modifiche_codice {
    private static final String NomeMaschera = "DB_Modifiche";

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

    public db_dati_modifiche_codice(Context context) {
        this.context = context;
        PathDB = UtilityDetector.getInstance().PrendePathDB(context);

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception ignored) {
            int i = 0;
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
                        + "(idStato NUMERIC, Stato VARCHAR, Eliminato VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "UltimeSelezioni "
                        + "(Progetto VARCHAR, Modulo VARCHAR, Sezione VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "StatiAttivi "
                        + "(Stati VARCHAR);";
                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void LeggeUltimeSelezioni() {
        if (myDB != null) {
            VariabiliStaticheModificheCodice.getInstance().setProgettoSelezionato("");
            VariabiliStaticheModificheCodice.getInstance().setModuloSelezionato("");
            VariabiliStaticheModificheCodice.getInstance().setSezioneSelezionata("");

            try {
                Cursor c = myDB.rawQuery("SELECT * FROM UltimeSelezioni", null);
                c.moveToFirst();
                do {
                    VariabiliStaticheModificheCodice.getInstance().setProgettoSelezionato(c.getString(0));
                    VariabiliStaticheModificheCodice.getInstance().setModuloSelezionato(c.getString(1));
                    VariabiliStaticheModificheCodice.getInstance().setSezioneSelezionata(c.getString(2));
                } while(c.moveToNext());
            } catch (Exception ignored) {
            }
        }
    }

    public void ModificaUltimeSelezioni() {
        if (myDB != null) {
            try {
                String sql = "Delete From UltimeSelezioni";
                myDB.execSQL(sql);

                sql = "Insert Into UltimeSelezioni Values(" +
                        "'" + VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato().replace("'", "''") + "'," +
                        "'" + VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato().replace("'", "''") + "'," +
                        "'" + VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata().replace("'", "''") + "'" +
                        ")";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        }
    }

    public void InserisceNuovoProgetto(String Progetto) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaProgetto("", Progetto);

        /* if (myDB != null) {
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
        } */
    }

    public void ModificaProgetto(int idProgetto, String Progetto) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaProgetto(String.valueOf(idProgetto), Progetto);

        /* if (myDB != null) {
            try {
                String sql = "Update Progetti Set " +
                        "Progetto='" + Progetto.replace("'", "''") + "' " +
                        "Where idProgetto=" + idProgetto;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void EliminaProgetto(int idProgetto) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.EliminaProgetto(String.valueOf(idProgetto));

        /* if (myDB != null) {
            try {
                String sql = "Update Progetti Set " +
                        "Eliminato='S' " +
                        "Where idProgetto=" + idProgetto;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void RitornaStatiAttivi() {
        if (myDB != null) {
            try {
                VariabiliStaticheModificheCodice.getInstance().setStatiAttivi("");
                Cursor c = myDB.rawQuery("SELECT * FROM StatiAttivi", null);
                if (c.moveToFirst()) {
                    do {
                        VariabiliStaticheModificheCodice.getInstance().setStatiAttivi(c.getString(0));
                    } while (c.moveToNext());
                }
                if (VariabiliStaticheModificheCodice.getInstance().getStatiAttivi().isEmpty()) {
                    int quanti = VariabiliStaticheModificheCodice.getInstance().getListaStati().size();
                    String s = "";
                    for (int i = 0; i < quanti; i++) {
                        s += "N";
                    }
                    VariabiliStaticheModificheCodice.getInstance().setStatiAttivi(s);
                }
                c.close();
            } catch (SQLException e) {
                int i = 0;
            }
        }
    }

    public void ScriveStatiAttivi() {
        if (myDB != null) {
            try {
                String sql = "Delete From StatiAttivi";
                myDB.execSQL(sql);

                sql = "Insert Into StatiAttivi(Stati) Values ('" + VariabiliStaticheModificheCodice.getInstance().getStatiAttivi() + "')";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        }
    }

    public void RitornaProgetti() {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.RitornaProgetti();

        /* List<Progetti> lista = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Progetti Where Eliminato='N' Order By Progetto", null);
                c.moveToFirst();
                do {
                    try {
                        Progetti p = new Progetti();
                        p.setIdProgetto(c.getInt(0));
                        p.setProgetto(c.getString(1));

                        lista.add(p);
                    } catch (Exception ignored) {
                        int i = 0;
                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
                int i = 0;
            }
        }

        return lista; */
    }

    public void InserisceNuovoModulo(int idProgetto, String Modulo) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaModulo(String.valueOf(idProgetto), "", Modulo);

        /* if (myDB != null) {
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
                int i = 0;
            }
        } */
    }

    public void ModificaModulo(int idProgetto, int idModulo, String Modulo) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaModulo(String.valueOf(idProgetto), String.valueOf(idModulo), Modulo);

        /* if (myDB != null) {
            try {
                String sql = "Update Moduli Set " +
                        "Modulo='" + Modulo.replace("'", "''") + "' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void EliminaModulo(int idProgetto, int idModulo) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.EliminaModulo(String.valueOf(idProgetto), String.valueOf(idModulo));

        /* if (myDB != null) {
            try {
                String sql = "Update Moduli Set " +
                        "Eliminato='S' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void RitornaModuli(int idProgetto) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.RitornaModuli(String.valueOf(idProgetto));

        /* List<Moduli> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Moduli Where idProgetto=" + idProgetto + " And Eliminato='N' " +
                        "Order By Modulo", null);
                c.moveToFirst();
                do {
                    try {
                        Moduli p = new Moduli();
                        p.setIdProgetto(c.getInt(0));
                        p.setIdModulo(c.getInt(1));
                        p.setModulo(c.getString(2));

                        lista.add(p);
                    } catch (Exception ignored) {
                        int i = 0;
                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
                int i = 0;
            }
        }

        return lista; */
    }

    public void InserisceNuovaSezione(int idProgetto, int idModulo, String Sezione) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaSezione(String.valueOf(idProgetto), String.valueOf(idModulo), "", Sezione);

        /* if (myDB != null) {
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
                int i = 0;
            }
        } */
    }

    public void ModificaSezione(int idProgetto, int idModulo, int idSezione, String Sezione) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaSezione(String.valueOf(idProgetto), String.valueOf(idModulo), String.valueOf(idSezione), Sezione);

        /* if (myDB != null) {
            try {
                String sql = "Update Sezioni Set " +
                        "Sezione='" + Sezione.replace("'", "''") + "' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo + " " +
                        "And idSezione=" + idSezione;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void EliminaSezione(int idProgetto, int idModulo, int idSezione) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.EliminaSezione(String.valueOf(idProgetto), String.valueOf(idModulo), String.valueOf(idSezione));

        /* if (myDB != null) {
            try {
                String sql = "Update Sezioni Set " +
                        "Eliminato='S' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo + " " +
                        "And idSezione=" + idSezione;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void RitornaSezioni(int idProgetto, int idModulo) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.RitornaSezioni(String.valueOf(idProgetto), String.valueOf(idModulo));

        /* List<Sezioni> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Sezioni Where idProgetto=" + idProgetto + " " +
                        "And idModulo=" + idModulo + " And Eliminato='N' Order By Sezione", null);
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
                        int i = 0;
                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
                int i = 0;
            }
        }

        return lista; */
    }

    public void InserisceNuovaModifica(int idProgetto, int idModulo, int idSezione, String Modifica) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaModifica(String.valueOf(idProgetto), String.valueOf(idModulo), String.valueOf(idSezione), "", Modifica, "0");

        /* if (myDB != null) {
            try {
                String sql = "";

                Cursor c1 = myDB.rawQuery("SELECT * FROM Stati", null);
                if (c1.getCount() == 0) {
                    sql = "Insert Into Stati Values(0, 'Aperta', 'N')";
                    myDB.execSQL(sql);

                    sql = "Insert Into Stati Values(1, 'Da Controllare', 'N')";
                    myDB.execSQL(sql);

                    sql = "Insert Into Stati Values(2, 'Chiusa', 'N')";
                    myDB.execSQL(sql);

                    sql = "Insert Into Stati Values(3, 'In Dubbio', 'N')";
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
                            "0," +
                            "'" + Modifica.replace("'", "''") + "'," +
                            "'N')";
                    myDB.execSQL(sql);
                }
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void ModificaModifica(int idProgetto, int idModulo, int idSezione, int idModifica,
                                 String Modifica, int idStato) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaModifica(String.valueOf(idProgetto), String.valueOf(idModulo), String.valueOf(idSezione), String.valueOf(idModifica), Modifica, String.valueOf(idStato));

        /* if (myDB != null) {
            try {
                String sql = "Update Modifiche Set " +
                        "Modifica='" + Modifica.replace("'", "''") + "', " +
                        "idStato=" + idStato + " " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo +
                        " And idSezione=" + idSezione + " And idModifica=" + idModifica;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void EliminaModifica(int idProgetto, int idModulo, int idSezione, int idModifica) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.EliminaModifica(String.valueOf(idProgetto), String.valueOf(idModulo), String.valueOf(idSezione), String.valueOf(idModifica));

        /* if (myDB != null) {
            try {
                String sql = "Update Modifiche Set " +
                        "Eliminato='S' " +
                        "Where idProgetto=" + idProgetto + " And idModulo=" + idModulo +
                        " And idSezione=" + idSezione + " And idModifica=" + idModifica;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void RitornaModifiche(int idProgetto, int idModulo, int idSezione) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.RitornaModifiche(String.valueOf(idProgetto), String.valueOf(idModulo), String.valueOf(idSezione));

        /* List<Modifiche> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                String Where = "";

                if (VariabiliStaticheModificheCodice.getInstance().getSwcSoloAperti().isChecked()) {
                    Where = "And idStato = 0";
                }

                String sql = "SELECT * FROM Modifiche Where " +
                        "idProgetto=" + idProgetto + " And idModulo=" + idModulo +
                        " And idSezione=" + idSezione + " " + Where + " And Eliminato='N' Order By idModifica";

                Cursor c = myDB.rawQuery(sql, null);
                c.moveToFirst();
                do {
                    try {
                        Modifiche p = new Modifiche();
                        p.setIdProgetto(c.getInt(0));
                        p.setIdModulo(c.getInt(1));
                        p.setIdSezione(c.getInt(2));
                        p.setIdModifica(c.getInt(3));
                        p.setIdStato(c.getInt(4));
                        p.setModifica(c.getString(5));

                        lista.add(p);
                    } catch (Exception ignored) {
                        int i = 0;
                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
                int i = 0;
            }
        }

        VariabiliStaticheModificheCodice.getInstance().setListaModifiche(lista);

        return lista; */
    }

    public void RitornaNumeroModificheTotali(int idProgetto, int idModulo, int idSezione) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.RitornaNumeroModificheTotali(String.valueOf(idProgetto), String.valueOf(idModulo), String.valueOf(idSezione));

        /* if (myDB != null) {
            try {
                String sql = "SELECT Coalesce(Count(*), 0) FROM Modifiche Where " +
                        "idProgetto=" + idProgetto + " And idModulo=" + idModulo +
                        " And idSezione=" + idSezione + " And Eliminato='N'";

                Cursor c = myDB.rawQuery(sql, null);
                c.moveToFirst();
                return c.getInt(0);
            } catch (SQLException e) {
                int i = 0;
            }
        }

        return 0; */
    }

    public void InserisceNuovoStato(String Stato) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaStato("", Stato, false);

        /* if (myDB != null) {
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
                int i = 0;
            }
        } */
    }

    public void ModificaStato(int idStato, String Stato) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.InserisceModificaStato(String.valueOf(idStato), Stato, false);

        /* if (myDB != null) {
            try {
                String sql = "Update Stati Set " +
                        "Stato='" + Stato.replace("'", "''") + "' " +
                        "Where idStato=" + idStato;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void EliminaStati(int idStato) {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.EliminaStato(String.valueOf(idStato), false);

        /* if (myDB != null) {
            try {
                String sql = "Update Stati Set " +
                        "Eliminato='S' " +
                        "Where idStato=" + idStato;
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        } */
    }

    public void RitornaStati() {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.RitornaStati(false);

        /* List<Stati> lista = new ArrayList<>();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Stati Where Eliminato='N' Order By Stato", null);
                c.moveToFirst();
                do {
                    try {
                        Stati p = new Stati();
                        p.setIdStato(c.getInt(0));
                        p.setStato(c.getString(1));

                        lista.add(p);
                    } catch (Exception ignored) {
                        int i = 0;
                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
                int a = 0;
            }
        }

        VariabiliStaticheModificheCodice.getInstance().setListaStati(lista);

        return lista; */
    }

    public void RitornaConteggi() {
        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.RitornaConteggi();

        /* List<StrutturaConteggi> lista = new ArrayList<>();
        if (myDB != null) {
            try {
                String sql = "Select A.idProgetto, A.idModulo, A.idSezione, B.Progetto, C.Modulo, D.Sezione,  Count(*) As Quanti From Modifiche A " +
                        "Left Join Progetti B On A.idProgetto = B.idProgetto " +
                        "Left Join Moduli C On A.idModulo = C.idModulo " +
                        "Left Join Sezioni D On A.idSezione = D.idSezione " +
                        "Where A.Eliminato = 'N' And A.idStato = 0 " +
                        "Group By A.idProgetto, A.idModulo, A.idSezione, B.Progetto, C.Modulo, D.Sezione";
                Cursor c = myDB.rawQuery(sql, null);
                c.moveToFirst();
                do {
                    try {
                        StrutturaConteggi p = new StrutturaConteggi();
                        p.setIdProgetto(c.getInt(0));
                        p.setIdModulo(c.getInt(1));
                        p.setIdSezione(c.getInt(2));
                        p.setProgetto(c.getString(3));
                        p.setModulo(c.getString(4));
                        p.setSezione(c.getString(5));
                        p.setQuante(c.getInt(6));

                        lista.add(p);
                    } catch (Exception ignored) {
                        int i = 0;
                    }
                } while(c.moveToNext());
            } catch (SQLException e) {
                int a = 0;
            }
        }

        VariabiliStaticheModificheCodice.getInstance().setListaConteggi(lista); */
    }
}
