package com.looigi.wallpaperchanger2.classePlayer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaFiltroBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaArtisti;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaSalvataggi;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaTags;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class db_dati_player {
    private static final String NomeMaschera = "DB_Player";

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

    public db_dati_player(Context context) {
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
            String nomeDB = "dati_player.db";
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
                        + "UltimoBrano"
                        + " (idBrano VARCHAR);";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "listaBrani"
                        + " (idBrano VARCHAR, quantiBrani VARCHAR, Artista VARCHAR, Album VARCHAR,"
                        + " Brano VARCHAR, Anno VARCHAR, Traccia VARCHAR, Estensione VARCHAR,"
                        + " Data VARCHAR, Dimensione VARCHAR, Ascoltata VARCHAR, Bellezza VARCHAR,"
                        + "Testo VARCHAR, TestoTradotto VARCHAR, UrlBrano VARCHAR, PathBrano VARCHAR,"
                        + "CartellaBrano VARCHAR, Tags VARCHAR, TipoBrano VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Artisti"
                        + " (Artista VARCHAR, Tags VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Tags "
                        + "(idTag VARCHAR, Tag VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "ImmaginiBrano"
                        + " (Artista VARCHAR, Album VARCHAR,"
                        + " NomeImmagine VARCHAR,"
                        + " UrlImmagine VARCHAR,"
                        + " PathImmagine VARCHAR,"
                        + " CartellaImmagine VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni"
                        + " (LimiteInBytes VARCHAR, CambiaImmagini VARCHAR, TempoCambioImmagine VARCHAR, "
                        + "Chiacchera VARCHAR);";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "UltimoScanImmagini "
                        + " (Giorno VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Ricerche "
                        + "("
                        + "Random VARCHAR, Stelle VARCHAR, StelleSuperiori VARCHAR, MaiAscoltata VARCHAR, "
                        + "Testo VARCHAR, TestoNon VARCHAR, Preferiti VARCHAR, PreferitiElimina VARCHAR, "
                        + "AndOrPref VARCHAR, Tags VARCHAR, TagsElimina VARCHAR, AndOrTags VARCHAR, "
                        + "DataSuperiore VARCHAR, DataInferiore VARCHAR, DataSuperioreTesto VARCHAR, DataInferioreTesto VARCHAR "
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Salvataggi "
                        + "("
                        + "idSalvataggio VARCHAR, Salvataggio VARCHAR "
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "SalvataggiDefault "
                        + "("
                        + "idSalvataggio VARCHAR "
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "SalvataggiDettaglio "
                        + "("
                        + "idSalvataggio VARCHAR, Random VARCHAR, StelleDaRicercare VARCHAR, Stelle VARCHAR, StelleSuperiori VARCHAR, MaiAscoltata VARCHAR, "
                        + "RicercaTesto VARCHAR, Testo VARCHAR, TestoNon VARCHAR, RicercaPreferiti VARCHAR, Preferiti VARCHAR, PreferitiElimina VARCHAR, "
                        + "AndOrPref VARCHAR, RicercaTags VARCHAR, Tags VARCHAR, TagsElimina VARCHAR, AndOrTags VARCHAR, "
                        + "RicercaData VARCHAR, DataSuperiore VARCHAR, DataInferiore VARCHAR, DataSuperioreTesto VARCHAR, DataInferioreTesto VARCHAR "
                        + ");";

                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore creazione tabelle: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e));

            return false;
        }
    }

    public void EliminaSalvataggio(String idSalvataggio) {
        if (myDB != null) {
            myDB.execSQL("Delete From Salvataggi Where idSalvataggio='" + idSalvataggio + "'");
            myDB.execSQL("Delete From SalvataggiDettaglio Where idSalvataggio='" + idSalvataggio + "'");
        }
    }

    public void ScriveTags(List<StrutturaTags> lista) {
        if (myDB != null) {
            String sql = "Delete From Tags";
            myDB.execSQL(sql);

            for (StrutturaTags s : lista) {
                sql = "Insert Into Tags Values (" +
                        "'" + s.getIdTag() + "', " +
                        "'" + s.getTag().replace("'", "''") + "' " +
                        ")";
                myDB.execSQL(sql);
            }
        }
    }

    public List<StrutturaTags> RitornaTags() {
        List<StrutturaTags> lista = new ArrayList<>();
        if (myDB != null) {
            String sql = "Select * From Tags";
            Cursor c = myDB.rawQuery(sql, null);
            if (c.getCount() > 0) {
                c.moveToFirst();

                do {
                    StrutturaTags s = new StrutturaTags();
                    s.setIdTag(Integer.parseInt(c.getString(0)));
                    s.setTag(c.getString(1));

                    lista.add(s);
                } while (c.moveToNext());
            }
            c.close();
        }

        return lista;
    }

    public List<StrutturaSalvataggi> RitornaSalvataggi() {
        List<StrutturaSalvataggi> lista = new ArrayList<>();
        if (myDB != null) {
            String sql = "Select * From Salvataggi";
            Cursor c = myDB.rawQuery(sql, null);
            if (c.getCount() > 0) {
                c.moveToFirst();

                do {
                    StrutturaSalvataggi s = new StrutturaSalvataggi();
                    s.setIdSalvataggio(Integer.parseInt(c.getString(0)));
                    s.setSalvataggio(c.getString(1));

                    lista.add(s);
                } while (c.moveToNext());
            }
            c.close();
        }

        return lista;
    }

    public void SalvaSalvataggioDefault(String idSalvataggio) {
        if (myDB != null) {
            myDB.execSQL("Delete From SalvataggiDefault");
            myDB.execSQL("Insert Into SalvataggiDefault Values ('" + idSalvataggio + "')");
        }
    }

    public String RitornaSalvataggioDefault() {
        String id = "";
        if (myDB != null) {
            String sql = "Select * From SalvataggiDefault";
            Cursor c = myDB.rawQuery(sql, null);
            if (c.getCount() > 0) {
                c.moveToFirst();

                id = c.getString(0);
            }
            c.close();
        }

        return id;
    }

    public String EliminaSalvataggioDefault() {
        String id = "";
        if (myDB != null) {
            myDB.execSQL("Delete From SalvataggiDefault");
        }

        return id;
    }

    public void ModificaSalvataggioDettaglio(String idSalvataggio, String NomeSalvataggio) {
        if (myDB != null) {
            String sql = "Update SalvataggiDettaglio Set "
                    + "Random='" + (VariabiliStatichePlayer.getInstance().isRandom() ? "S" : "N") + "', "
                    + "StelleDaRicercare='" + (VariabiliStatichePlayer.getInstance().isRicercaStelle() ? "S" : "N") + "', "
                    + "Stelle='" + VariabiliStatichePlayer.getInstance().getStelleDaRicercare()  + "', "
                    + "StelleSuperiori='" + (VariabiliStatichePlayer.getInstance().isStelleSuperiori() ? "S" : "N") + "' , "
                    + "MaiAscoltata='" + (VariabiliStatichePlayer.getInstance().isRicercaMaiAscoltata() ? "S" : "N") + "' , "
                    + "RicercaTesto='" + (VariabiliStatichePlayer.getInstance().isRicercaTesto() ? "S" : "N") + "', "
                    + "Testo='" + VariabiliStatichePlayer.getInstance().getTestoDaRicercare().replace("'","''") + "', "
                    + "TestoNon='" + VariabiliStatichePlayer.getInstance().getTestoDaNonRicercare().replace("'","''") + "', "
                    + "RicercaPreferiti='" + (VariabiliStatichePlayer.getInstance().isRicercaPreferiti() ? "S" : "N") + "', "
                    + "Preferiti='" + VariabiliStatichePlayer.getInstance().getPreferiti().replace("'","''") + "', "
                    + "PreferitiElimina='" + VariabiliStatichePlayer.getInstance().getPreferitiElimina().replace("'","''") + "', "
                    + "AndOrPref='" + (VariabiliStatichePlayer.getInstance().isAndOrPref() ? "S" : "N")  + "', "
                    + "RicercaTags='" + (VariabiliStatichePlayer.getInstance().isRicercaTags() ? "S" : "N") + "', "
                    + "Tags='" + VariabiliStatichePlayer.getInstance().getPreferitiTags().replace("'","''") + "', "
                    + "TagsElimina='" + VariabiliStatichePlayer.getInstance().getPreferitiEliminaTags().replace("'","''") + "', "
                    + "AndOrTags='" + (VariabiliStatichePlayer.getInstance().isAndOrTags() ? "S" : "N")  + "', "
                    + "RicercaData='" + (VariabiliStatichePlayer.getInstance().isDate() ? "S" : "N") + "', "
                    + "DataSuperiore='" + (VariabiliStatichePlayer.getInstance().isDataSuperiore() ? "S" : "N")  + "', "
                    + "DataInferiore='" + (VariabiliStatichePlayer.getInstance().isDataInferiore() ? "S" : "N")  + "', "
                    + "DataSuperioreTesto='" + VariabiliStatichePlayer.getInstance().getsDataSuperiore()  + "', "
                    + "DataInferioreTesto='" + VariabiliStatichePlayer.getInstance().getsDataInferiore()  + "' "
                    + "Where idSalvataggio='" + idSalvataggio + "'";
            myDB.execSQL(sql);

            sql = "Update Salvataggi Set Salvataggio='" + NomeSalvataggio + "' Where idSalvataggio='" + idSalvataggio + "'";
            myDB.execSQL(sql);
        }
    }

    public void ScriveSalvataggioDettaglio(String Salvataggio) {
        if (myDB != null) {
            String idSalvataggio = "1";

            String sql = "Select * From Salvataggi Where Salvataggio='" + Salvataggio.replace("'", "''") + "'";
            Cursor c = myDB.rawQuery(sql, null);
            if (c.getCount() > 0) {
                c.moveToFirst();

                UtilitiesGlobali.getInstance().ApreToast(context, "Salvataggio già esistente");
                return;
            }
            c.close();

            sql = "Select Coalesce(Max(idSalvataggio), 0)+1 From Salvataggi";
            Cursor c2 = myDB.rawQuery(sql, null);
            if (c2.getCount() > 0) {
                c2.moveToFirst();

                idSalvataggio = c2.getString(0);
            }
            c2.close();

            sql = "Insert Into Salvataggi Values ("
                    + "'" + idSalvataggio + "', "
                    + "'" + Salvataggio.replace("'", "''") + "' "
                    + ")";
            myDB.execSQL(sql);

            sql = "Insert Into SalvataggiDettaglio Values ("
                + "'" + idSalvataggio + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isRandom() ? "S" : "N") + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isRicercaStelle() ? "S" : "N")  + "', "
                + "'" + VariabiliStatichePlayer.getInstance().getStelleDaRicercare()  + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isStelleSuperiori() ? "S" : "N") + "' , "
                + "'" + (VariabiliStatichePlayer.getInstance().isRicercaMaiAscoltata() ? "S" : "N") + "' , "
                + "'" + (VariabiliStatichePlayer.getInstance().isRicercaTesto() ? "S" : "N")  + "', "
                + "'" + VariabiliStatichePlayer.getInstance().getTestoDaRicercare().replace("'","''") + "', "
                + "'" + VariabiliStatichePlayer.getInstance().getTestoDaNonRicercare().replace("'","''") + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isRicercaPreferiti() ? "S" : "N") + "' , "
                + "'" + VariabiliStatichePlayer.getInstance().getPreferiti().replace("'","''")+ "', "
                + "'" + VariabiliStatichePlayer.getInstance().getPreferitiElimina().replace("'","''")  + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isAndOrPref() ? "S" : "N")  + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isRicercaTags() ? "S" : "N") + "' , "
                + "'" + VariabiliStatichePlayer.getInstance().getPreferitiTags().replace("'","''") + "', "
                + "'" + VariabiliStatichePlayer.getInstance().getPreferitiEliminaTags().replace("'","''")  + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isAndOrTags() ? "S" : "N")  + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isDate() ? "S" : "N")  + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isDataSuperiore() ? "S" : "N")  + "', "
                + "'" + (VariabiliStatichePlayer.getInstance().isDataInferiore() ? "S" : "N")  + "', "
                + "'" + VariabiliStatichePlayer.getInstance().getsDataSuperiore()  + "', "
                + "'" + VariabiliStatichePlayer.getInstance().getsDataInferiore()  + "' "
                + ")";
            myDB.execSQL(sql);
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido per scrive salvataggio dettaglio");
        }
    }

    public boolean CaricaSalvataggio(String idSalvataggio) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM SalvataggiDettaglio Where idSalvataggio=" + idSalvataggio, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    VariabiliStatichePlayer.getInstance().setRandom(c.getString(1).equals("S"));
                    VariabiliStatichePlayer.getInstance().setRicercaStelle(c.getString(2).equals("S"));
                    VariabiliStatichePlayer.getInstance().setStelleDaRicercare(Integer.parseInt(c.getString(3)));
                    VariabiliStatichePlayer.getInstance().setStelleSuperiori(c.getString(4).equals("S"));
                    VariabiliStatichePlayer.getInstance().setRicercaMaiAscoltata(c.getString(5).equals("S"));
                    VariabiliStatichePlayer.getInstance().setRicercaTesto(c.getString(6).equals("S"));
                    VariabiliStatichePlayer.getInstance().setTestoDaRicercare(c.getString(7));
                    VariabiliStatichePlayer.getInstance().setTestoDaNonRicercare(c.getString(8));
                    VariabiliStatichePlayer.getInstance().setRicercaPreferiti(c.getString(9).equals("S"));
                    VariabiliStatichePlayer.getInstance().setPreferiti(c.getString(10));
                    VariabiliStatichePlayer.getInstance().setPreferitiElimina(c.getString(11));
                    VariabiliStatichePlayer.getInstance().setAndOrPref(c.getString(12).equals("S"));
                    VariabiliStatichePlayer.getInstance().setRicercaTags(c.getString(13).equals("S"));
                    VariabiliStatichePlayer.getInstance().setPreferitiTags(c.getString(14));
                    VariabiliStatichePlayer.getInstance().setPreferitiEliminaTags(c.getString(15));
                    VariabiliStatichePlayer.getInstance().setAndOrTags(c.getString(16).equals("S"));
                    VariabiliStatichePlayer.getInstance().setDate(c.getString(17).equals("S"));
                    VariabiliStatichePlayer.getInstance().setDataSuperiore(c.getString(18).equals("S"));
                    VariabiliStatichePlayer.getInstance().setDataInferiore(c.getString(19).equals("S"));
                    VariabiliStatichePlayer.getInstance().setsDataSuperiore(c.getString(20));
                    VariabiliStatichePlayer.getInstance().setsDataInferiore(c.getString(21));

                    return true;
                } else {
                    return false;
                }
            } catch (Exception ignored) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void EliminaTutto() {
        if (myDB != null) {
            myDB.execSQL("Delete From listaBrani");
            myDB.execSQL("Delete From UltimoBrano");
            myDB.execSQL("Delete From ImmaginiBrano");
            myDB.execSQL("Delete From UltimoScanImmagini");
            myDB.execSQL("Delete From Salvataggi");
            myDB.execSQL("Delete From SalvataggiDefault");
            myDB.execSQL("Delete From SalvataggiDettaglio");
            myDB.execSQL("Delete From UltimoScanImmagini");

            CompattaDB();
        }
    }

    public void EliminaImmagineFisica(String Artista, String Immagine) {
        if (myDB != null) {
            myDB.execSQL("Delete From ImmaginiBrano Where Artista='" + Artista.replace("'", "''") + "' And NomeImmagine='" + Immagine.replace("'", "''") + "'");

            CompattaDB();
        }
    }

    public void ScriveUltimoGiornoControlloImmagini(String Giorno) {
        if (myDB != null) {
            myDB.execSQL("Delete From UltimoScanImmagini");
            myDB.execSQL("Insert Into UltimoScanImmagini Values ('" + Giorno + "')");
        }
    }

    public String CaricaUltimoGiornoControlloImmagini() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM UltimoScanImmagini", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    return c.getString(0);
                } else {
                    return "";
                }
            } catch (Exception ignored) {
                return "";
            }
        } else {
            return "";
        }
    }

    public int CaricaRicerche() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Ricerche", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    VariabiliStatichePlayer.getInstance().setRandom(c.getString(0).equals("S"));
                    VariabiliStatichePlayer.getInstance().setStelleDaRicercare(Integer.parseInt(c.getString(1)));
                    VariabiliStatichePlayer.getInstance().setStelleSuperiori(c.getString(2).equals("S"));
                    VariabiliStatichePlayer.getInstance().setRicercaMaiAscoltata(c.getString(3).equals("S"));
                    VariabiliStatichePlayer.getInstance().setTestoDaRicercare(c.getString(4));
                    VariabiliStatichePlayer.getInstance().setTestoDaNonRicercare(c.getString(5));
                    VariabiliStatichePlayer.getInstance().setPreferiti(c.getString(6));
                    VariabiliStatichePlayer.getInstance().setPreferitiElimina(c.getString(7));
                    VariabiliStatichePlayer.getInstance().setAndOrPref(c.getString(8).equals("S"));
                    VariabiliStatichePlayer.getInstance().setPreferitiTags(c.getString(9));
                    VariabiliStatichePlayer.getInstance().setPreferitiEliminaTags(c.getString(10));
                    VariabiliStatichePlayer.getInstance().setAndOrTags(c.getString(11).equals("S"));
                    VariabiliStatichePlayer.getInstance().setDataSuperiore(c.getString(12).equals("S"));
                    VariabiliStatichePlayer.getInstance().setDataInferiore(c.getString(13).equals("S"));
                    VariabiliStatichePlayer.getInstance().setsDataSuperiore(c.getString(14));
                    VariabiliStatichePlayer.getInstance().setsDataInferiore(c.getString(15));

                    return 0;
                } else {
                    VariabiliStatichePlayer.getInstance().setRandom(true);
                    VariabiliStatichePlayer.getInstance().setStelleDaRicercare(8);
                    VariabiliStatichePlayer.getInstance().setStelleSuperiori(true);
                    VariabiliStatichePlayer.getInstance().setRicercaMaiAscoltata(false);
                    VariabiliStatichePlayer.getInstance().setTestoDaRicercare("");
                    VariabiliStatichePlayer.getInstance().setTestoDaNonRicercare("");
                    VariabiliStatichePlayer.getInstance().setPreferiti("");
                    VariabiliStatichePlayer.getInstance().setPreferitiElimina("");
                    VariabiliStatichePlayer.getInstance().setAndOrPref(true);
                    VariabiliStatichePlayer.getInstance().setPreferitiTags("");
                    VariabiliStatichePlayer.getInstance().setPreferitiEliminaTags("");
                    VariabiliStatichePlayer.getInstance().setAndOrTags(true);
                    VariabiliStatichePlayer.getInstance().setDataSuperiore(false);
                    VariabiliStatichePlayer.getInstance().setDataInferiore(false);
                    VariabiliStatichePlayer.getInstance().setsDataSuperiore("");
                    VariabiliStatichePlayer.getInstance().setsDataInferiore("");

                    ScriveRicerca();

                    return 0;
                }
            } catch (Exception e) {
                PulisceDatiRic();
                CreazioneTabelle();
                return CaricaRicerche();
            }
        } else {
            return -1;
        }
    }

    public void ScriveArtisti(List<StrutturaArtisti> lista) {
        if (myDB != null) {
            String sql = "Delete From Artisti";
            myDB.execSQL(sql);

            for (StrutturaArtisti s : lista) {
                String Tags = "";
                for (String t : s.getTags()) {
                    Tags += t + ";";
                }
                sql = "Insert Into Artisti Values (" +
                        "'" + s.getNomeArtista().replace("'", "''") + "', " +
                        "'" + Tags.replace("'", "''") + "' " +
                        ")";
                myDB.execSQL(sql);
            }
        }
    }

    public List<StrutturaArtisti> CaricaArtisti() {
        if (myDB != null) {
            try {
                List<StrutturaArtisti> s = new ArrayList<>();

                String sql = "SELECT * FROM Artisti";
                Cursor c = myDB.rawQuery(sql, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    do {
                        StrutturaArtisti sa = new StrutturaArtisti();
                        sa.setNomeArtista(c.getString(0));
                        String Tags = c.getString(1);
                        List<String> t = new ArrayList<>();
                        String[] tt = Tags.split(";");
                        for (String ttt : tt) {
                            t.add(ttt);
                        }
                        sa.setTags(t);

                        s.add(sa);
                    } while (c.moveToNext());
                }

                return s;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public void ScriveRicerca() {
        if (myDB != null) {
            String sql = "Delete From Ricerche";
            myDB.execSQL(sql);

            sql = "Insert Into Ricerche Values ("
                    + "'" + (VariabiliStatichePlayer.getInstance().isRandom() ? "S" : "N") + "', "
                    + "'" + VariabiliStatichePlayer.getInstance().getStelleDaRicercare()  + "', "
                    + "'" + (VariabiliStatichePlayer.getInstance().isStelleSuperiori() ? "S" : "N") + "' , "
                    + "'" + (VariabiliStatichePlayer.getInstance().isRicercaMaiAscoltata() ? "S" : "N") + "' , "
                    + "'" + VariabiliStatichePlayer.getInstance().getTestoDaRicercare().replace("'","''")  + "', "
                    + "'" + VariabiliStatichePlayer.getInstance().getTestoDaNonRicercare().replace("'","''")  + "', "
                    + "'" + VariabiliStatichePlayer.getInstance().getPreferiti().replace("'","''")  + "', "
                    + "'" + VariabiliStatichePlayer.getInstance().getPreferitiElimina().replace("'","''")  + "', "
                    + "'" + (VariabiliStatichePlayer.getInstance().isAndOrPref() ? "S" : "N")  + "', "
                    + "'" + VariabiliStatichePlayer.getInstance().getPreferitiTags().replace("'","''")  + "', "
                    + "'" + VariabiliStatichePlayer.getInstance().getPreferitiEliminaTags().replace("'","''")  + "', "
                    + "'" + (VariabiliStatichePlayer.getInstance().isAndOrTags() ? "S" : "N")  + "', "
                    + "'" + (VariabiliStatichePlayer.getInstance().isDataSuperiore() ? "S" : "N")  + "', "
                    + "'" + (VariabiliStatichePlayer.getInstance().isDataInferiore() ? "S" : "N")  + "', "
                    + "'" + VariabiliStatichePlayer.getInstance().getsDataSuperiore()  + "', "
                    + "'" + VariabiliStatichePlayer.getInstance().getsDataInferiore()  + "' "
                    + ")";
            myDB.execSQL(sql);
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido per scrive ricerca");
        }
    }

    public List<StrutturaBrano> CaricaTuttiIBraniLocali() {
        List<StrutturaBrano> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM listaBrani Order By Artista, Album, Brano", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    try {
                        do {
                            StrutturaBrano sb = new StrutturaBrano();
                            sb.setIdBrano(Integer.valueOf(c.getString(0)));
                            sb.setQuantiBrani(Integer.valueOf(c.getString(1)));
                            sb.setArtista(c.getString(2));
                            sb.setAlbum(c.getString(3));
                            sb.setBrano(c.getString(4));
                            sb.setAnno(c.getString(5));
                            sb.setTraccia(c.getString(6));
                            sb.setEstensione(c.getString(7));
                            sb.setData(c.getString(8));
                            sb.setDimensione(Long.valueOf(c.getString(9)));
                            sb.setAscoltata(Integer.valueOf(c.getString(10)));
                            sb.setBellezza(Integer.valueOf(c.getString(11)));
                            sb.setTesto(c.getString(12));
                            sb.setTestoTradotto(c.getString(13));
                            sb.setUrlBrano(c.getString(14));
                            sb.setPathBrano(c.getString(15));
                            sb.setCartellaBrano(c.getString(16));
                            sb.setTags(c.getString(17));
                            sb.setTipoBrano(Integer.parseInt(c.getString(18)));

                            sb.setImmagini(CaricaImmaginiBrano(sb.getArtista().toString()));

                            lista.add(sb);
                        } while (c.moveToNext());

                        return lista;
                    } catch (Exception e) {
                        PulisceDatiSB();
                        CreazioneTabelle();
                        return CaricaTuttiIBraniLocali();
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica tutti i brani");

                    return lista;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica tutti i brani: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiSB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return CaricaTuttiIBraniLocali();
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return lista;
        }
    }

    public int PrendeBranoDaNumeroRiga(int numeroRiga) {
        int ritorno = -1;

        // UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Controllo apertura db");
        if (myDB != null) {
            try {
                StrutturaFiltroBrano s = UtilityPlayer.getInstance().CreaDatiFiltrobrani();

                String sql = "SELECT * FROM listaBrani " + s.getWhere();
                Cursor c = myDB.rawQuery(sql, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    int i = 0;
                    do {
                        i++;
                        if (i >= numeroRiga) {
                            ritorno = Integer.parseInt(c.getString(0));
                            break;
                        }
                    } while (c.moveToNext());
                }
            } catch (Exception e) {
            }
        }

        return ritorno;
    }

    public int PrendeBranoDaDati(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                String sql = "SELECT idBrano FROM listaBrani Where " +
                        "Artista='" + sb.getArtista().replace("'", "''") + "' And " +
                        "Album='" + sb.getAlbum().replace("'", "''") + "' And " +
                        "Brano='" + sb.getBrano().replace("'", "''") + "'";
                Cursor c = myDB.rawQuery(sql, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    return Integer.parseInt(c.getString(0));
                } else {
                    return -1;
                }
            } catch (Exception e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public StrutturaBrano CaricaBrano(String idBrano) {
        // UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Controllo apertura db");
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM listaBrani Where idBrano=" + idBrano, null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per ultimo brano");
                    c.moveToFirst();

                    try {
                        StrutturaBrano sb = new StrutturaBrano();
                        sb.setIdBrano(Integer.valueOf(c.getString(0)));
                        sb.setQuantiBrani(Integer.valueOf(c.getString(1)));
                        sb.setArtista(c.getString(2));
                        sb.setAlbum(c.getString(3));
                        sb.setBrano(c.getString(4));
                        sb.setAnno(c.getString(5));
                        sb.setTraccia(c.getString(6));
                        sb.setEstensione(c.getString(7));
                        sb.setData(c.getString(8));
                        sb.setDimensione(Long.valueOf(c.getString(9)));
                        sb.setAscoltata(Integer.valueOf(c.getString(10)));
                        sb.setBellezza(Integer.valueOf(c.getString(11)));
                        sb.setTesto(c.getString(12));
                        sb.setTestoTradotto(c.getString(13));
                        sb.setUrlBrano(c.getString(14));
                        sb.setPathBrano(c.getString(15));
                        sb.setCartellaBrano(c.getString(16));
                        sb.setTags(c.getString(17));
                        sb.setTipoBrano(Integer.parseInt(c.getString(18)));

                        sb.setImmagini(CaricaImmaginiBrano(sb.getArtista().toString()));

                        return sb;
                    } catch (Exception e) {
                        PulisceDatiSB();
                        CreazioneTabelle();
                        return CaricaBrano(idBrano);
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per ultimo brano. Imposto default");

                    return null;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db ultimo brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiSB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return CaricaBrano(idBrano);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return null;
        }
    }

    public List<StrutturaImmagini> CaricaImmaginiBrano(String Artista) {
        List<StrutturaImmagini> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM ImmaginiBrano Where Artista='" + Artista + "'", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    try {
                        do {
                            StrutturaImmagini i = new StrutturaImmagini();
                            i.setArtista(c.getString(0));
                            i.setAlbum(c.getString(1));
                            i.setNomeImmagine(c.getString(2));
                            i.setUrlImmagine(c.getString(3));
                            i.setPathImmagine(c.getString(4));
                            i.setCartellaImmagine(c.getString(5));

                            lista.add(i);
                        } while (c.moveToNext());
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {

            }
        }

        return lista;
    }

    public String CaricaUltimoBranoAscoltato() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Ultimobrano", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per ultimo brano");
                    c.moveToFirst();

                    try {
                        return c.getString(0);
                    } catch (Exception e) {
                        PulisceDatiUB();
                        CreazioneTabelle();
                        return CaricaUltimoBranoAscoltato();
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per ultimo brano. Imposto default");

                    return "";
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db ultimo brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiUB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return CaricaUltimoBranoAscoltato();
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return "";
        }
    }

    public int QuantiBraniInArchivio() {
        if (myDB != null) {
            try {
                StrutturaFiltroBrano s = UtilityPlayer.getInstance().CreaDatiFiltrobrani();
                String sql = "SELECT Count(*) FROM listaBrani " + s.getWhere();

                Cursor c = myDB.rawQuery(sql , null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per conteggio brani");
                    c.moveToFirst();

                    try {
                        return c.getInt(0);
                    } catch (Exception e) {
                        return -1;
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per conteggio brani");

                    return -1;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db conteggio brani: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -1;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return -1;
        }
    }

    public int RitornaMaxIdBrano() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT coalesce(max(idBrano),0) + 1 FROM listaBrani", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per max id brano");
                    c.moveToFirst();

                    try {
                        return c.getInt(0);
                    } catch (Exception e) {
                        return 0;
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per max id brano");

                    return -1;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db max id brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return 0;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return -1;
        }
    }

    public void ScriveStelleBrano(String Stelle) {
        if (myDB != null) {
            String idBrano = String.valueOf(VariabiliStatichePlayer.getInstance().getUltimoBrano().getIdBrano());
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Scrive stelle brano " + idBrano + ": " + Stelle);
            myDB.execSQL("Update listaBrani Set Bellezza='" + Stelle + "' Where idBrano=" + idBrano);
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido per scrive stelle brano");
        }
    }

    public boolean EsisteBrano(String Artista, String Album, String Brano) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM listaBrani Where Artista='" + Artista.replace("'", "''") + "' And Album='" + Album.replace("'", "''") + "' And Brano='" + Brano.replace("'", "''") + "'", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per esistenza brano");
                    c.moveToFirst();

                    try {
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per esistenza brano");

                    return false;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db esistenza brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return false;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return false;
        }
    }

    public Boolean ScriveUltimoBranoAscoltato(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From UltimoBrano");

                String sql = "INSERT INTO"
                        + " UltimoBrano"
                        + " VALUES ("
                        + "'" + sb.getIdBrano() + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo brano: " + e.getMessage());
                PulisceDatiUB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveBrano(sb);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Impostazioni");

                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " VALUES ("
                        + "'" + VariabiliStatichePlayer.getInstance().getLimiteInGb() + "',"
                        + "'" + (VariabiliStatichePlayer.getInstance().isCambiaImmagine() ? "S" : "N") + "',"
                        + "'" + VariabiliStatichePlayer.getInstance().getTempoCambioImmagine() + "', "
                        + "'" + (VariabiliStatichePlayer.getInstance().isChiacchiera() ? "S" : "N") + "'"
                        + ") ";
                myDB.execSQL(sql);

                return true;
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                // PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                // CreazioneTabelle();
                return false;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return false;
        }
    }

    public void ImpostaValoriDiDefault() {
        VariabiliStatichePlayer.getInstance().setLimiteInGb(1.5F);
        VariabiliStatichePlayer.getInstance().setCambiaImmagine(true);
        VariabiliStatichePlayer.getInstance().setTempoCambioImmagine(5000);
        VariabiliStatichePlayer.getInstance().setChiacchiera(false);
    }

    public int CaricaImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        VariabiliStatichePlayer.getInstance().setLimiteInGb(Float.parseFloat(c.getString(0)));
                        VariabiliStatichePlayer.getInstance().setCambiaImmagine(c.getString(1).equals("S"));
                        VariabiliStatichePlayer.getInstance().setTempoCambioImmagine(Integer.parseInt(c.getString(2)));
                        VariabiliStatichePlayer.getInstance().setChiacchiera(c.getString(3).equals("S"));

                        return 0;
                    } catch (Exception e) {
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));

                        return -4;
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");

                    return -3;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -2;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return -1;
        }
    }

    /* public void EliminaImmagine(StrutturaBrano sb, StrutturaImmagini si) {
        if (myDB != null) {
            String Sql = "Delete From ImmaginiBrano Where idBrano='" + sb.getIdBrano() + "' And " +
                    "PathImmagine = '" + si.getPathImmagine() + "'";
            myDB.execSQL(Sql);
        }
    } */

    public Boolean ScriveBrano(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                String sql = "INSERT INTO"
                        + " listaBrani"
                        + " VALUES ("
                        + "'" + sb.getIdBrano() + "', "
                        + "'" + sb.getQuantiBrani() + "', "
                        + "'" + sb.getArtista().replace("'", "''") + "', "
                        + "'" + sb.getAlbum().replace("'", "''") + "', "
                        + "'" + sb.getBrano().replace("'", "''") + "', "
                        + "'" + sb.getAnno() + "', "
                        + "'" + sb.getTraccia() + "', "
                        + "'" + sb.getEstensione() + "', "
                        + "'" + sb.getData() + "', "
                        + "'" + sb.getDimensione() + "', "
                        + "'" + sb.getAscoltata() + "', "
                        + "'" + sb.getBellezza() + "', "
                        + "'" + sb.getTesto().replace("'", "''") + "', "
                        + "'" + sb.getTestoTradotto().replace("'", "''") + "', "
                        + "'" + sb.getUrlBrano().replace("'", "''") + "', "
                        + "'" + sb.getPathBrano().replace("'", "''") + "', "
                        + "'" + sb.getCartellaBrano().replace("'", "''") + "', "
                        + "'" + sb.getTags().replace("'", "''") + "', "
                        + "'" + sb.getTipoBrano() + "' "
                        + ") ";
                myDB.execSQL(sql);

                // ScriveImmaginiBrano(sb);

                if (sb.getIdBrano() < 60000) {
                    sql = "Select * From listaBrani Where " +
                            "Artista ='" + sb.getArtista().replace("'", "''") + "' And " +
                            "Album ='" + sb.getAlbum().replace("'", "''") + "' And " +
                            "Brano ='" + sb.getBrano().replace("'", "''") + "' And " +
                            "Cast(idBrano AS INTEGER) >= 60000";
                    Cursor c = myDB.rawQuery(sql, null);
                    if (c.getCount() > 0) {
                        c.moveToFirst();

                        String idBrano = c.getString(0);
                        EliminaBrano(idBrano);
                    }
                }
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo brano: " + e.getMessage());
                PulisceDatiSB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveBrano(sb);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public List<StrutturaImmagini> RitornaTutteLeImmagini() {
        List<StrutturaImmagini> lista = new ArrayList<>();

        if (myDB != null) {
            String sql = "Select * From ImmaginiBrano";
            Cursor c = myDB.rawQuery(sql, null);
            String idBranoElim = "";
            if (c.getCount() > 0) {
                c.moveToFirst();

                do {
                    StrutturaImmagini s = new StrutturaImmagini();
                    s.setArtista(c.getString(0));
                    s.setAlbum(c.getString(1));
                    s.setNomeImmagine(c.getString(2));
                    s.setUrlImmagine(c.getString(3));
                    s.setPathImmagine(c.getString(4));
                    s.setCartellaImmagine(c.getString(5));

                    lista.add(s);
                } while(c.moveToNext());
            }
            c.close();
        }

        return lista;
    }

    public void EliminaImmagine(StrutturaImmagini s) {
        if (myDB != null) {
            String sql = "Delete From ImmaginiBrano " +
                    "Where Artista='" + s.getArtista().replace("'", "''") + "' " +
                    "And Album='" + s.getAlbum().replace("'", "''") + "' " +
                    "And NomeImmagine='" + s.getNomeImmagine().replace("'", "''") + "'";
            myDB.execSQL(sql);
        }
    }

    public void EliminaBrano(String idBrano) {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Elimino brano " + idBrano);

            /* String sql = "Select * From listaBrani Where idBrano = '" + idBrano + "'";
            Cursor c = myDB.rawQuery(sql, null);
            String idBranoElim = "";
            if (c.getCount() > 0) {
                c.moveToFirst();

                 idBranoElim = c.getString(0);
            }
            c.close(); */

            myDB.execSQL("Delete From listaBrani Where idBrano='" + idBrano + "'");
            /* if (!idBranoElim.isEmpty()) {
                myDB.execSQL("Delete From ImmaginiBrano Where idBrano='" + idBranoElim + "'");
            } */

            CompattaDB();
        }
    }

    public void ScriveImmaginiBrano(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                // myDB.execSQL("Delete From ImmaginiUltimoBrano");

                String Artista = sb.getArtista().toString();

                List<StrutturaImmagini> lista = sb.getImmagini();
                List<StrutturaImmagini> nuovaLista = new ArrayList<>();
                for (StrutturaImmagini i : lista) {
                    String imm = i.getUrlImmagine().toUpperCase();
                    if (imm.contains(".JPG") || imm.contains(".JPEG") || imm.contains(".PNG")) {
                        nuovaLista.add(i);

                        String sql = "INSERT INTO "
                                + "ImmaginiBrano "
                                + "VALUES ("
                                + "'" + Artista.replace("'", "''") + "', "
                                + "'" + i.getAlbum().replace("'", "''") + "', "
                                + "'" + i.getNomeImmagine().replace("'", "''") + "', "
                                + "'" + i.getUrlImmagine().replace("'", "''") + "', "
                                + "'" + i.getPathImmagine().replace("'", "''") + "', "
                                + "'" + i.getCartellaImmagine().replace("'", "''") + "' "
                                + ") ";
                        myDB.execSQL(sql);
                    }
                }

                sb.setImmagini(nuovaLista);
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per scrittura immagini: " + e.getMessage());
                PulisceDatiSBI();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                ScriveImmaginiBrano(sb);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public boolean EsisteImmagineBrano(StrutturaImmagini s) {
        if (myDB != null) {
            try {
                String imm = s.getUrlImmagine().toUpperCase();
                if (imm.contains(".JPG") || imm.contains(".JPEG") || imm.contains(".PNG")
                        || imm.contains(".BMP") || imm.contains(".GIF")) {
                    String sql = "Select * From "
                            + "ImmaginiBrano "
                            + "Where "
                            + "Artista='" + s.getArtista().replace("'","''") + "' And "
                            + "Album='" + s.getAlbum().replace("'", "''") + "' And "
                            + "NomeImmagine='" + s.getNomeImmagine().replace("'", "''") + "'";
                    Cursor c = myDB.rawQuery(sql, null);
                    if (c.getCount() > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (SQLException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void ScriveImmagineBrano(String Artista, StrutturaImmagini immagine) {
        if (myDB != null) {
            try {
                String imm = immagine.getUrlImmagine().toUpperCase();
                if (imm.contains(".JPG") || imm.contains(".JPEG") || imm.contains(".PNG")
                        || imm.contains(".BMP") || imm.contains(".GIF")) {
                    String sql = "INSERT INTO "
                            + "ImmaginiBrano "
                            + "VALUES ("
                            + "'" + Artista.replace("'", "''") + "', "
                            + "'" + immagine.getAlbum().replace("'", "''") + "', "
                            + "'" + immagine.getNomeImmagine().replace("'", "''") + "', "
                            + "'" + immagine.getUrlImmagine().replace("'", "''") + "', "
                            + "'" + immagine.getPathImmagine().replace("'", "''") + "', "
                            + "'" + immagine.getCartellaImmagine().replace("'", "''") + "' "
                            + ") ";
                    myDB.execSQL(sql);
                }
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per aggiunta immagine: " + e.getMessage());
                /* PulisceDatiSBI();
                CreazioneTabelle();
                ScriveImmagineBrano(Artista, immagine); */
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public void PulisceDatiSB() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db brani");
            try {
                myDB.execSQL("Drop Table listaBrani");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db brani: " + ignored.getMessage());
            }
        }
    }

    public boolean PulisceDati() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");

                return true;
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());

                return false;
            }
        } else {
            return false;
        }
    }

    public void PulisceDatiSBI() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db immagini brano");
            try {
                myDB.execSQL("Drop Table ImmaginiBrano");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db immagini brano: " + ignored.getMessage());
            }
        }
    }

    public void PulisceDatiRic() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db ricerche");
            try {
                myDB.execSQL("Drop Table Ricerche");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db ricerche: " + ignored.getMessage());
            }
        }
    }

    public void PulisceDatiUB() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db Ultimo brano");
            try {
                myDB.execSQL("Drop Table UltimoBrano");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db ultimo brano: " + ignored.getMessage());
            }
        }
    }
}
