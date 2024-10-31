package com.looigi.wallpaperchanger2.classeGps;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaAccensioneGPS;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaPuntiSpegnimento;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class db_dati_gps {
    private static final String NomeMaschera = "DB_Dati_GPS";
    private String PathDB = "";
    private SQLiteDatabase myDB;
    private Context context;
    private boolean Riprova = false;

    public boolean DbAperto() {
        if (myDB != null) {
            return true;
        } else {
            return false;
        }
    }

    public db_dati_gps(Context context) {
        this.context = context;
        if (context == null) {
            this.context = UtilitiesGlobali.getInstance().tornaContextValido();
        }
        PathDB = this.context.getFilesDir() + "/DB/";

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception e) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore costruttore: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
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
        try {
            String nomeDB = "dati_gps.db";

            db = context.openOrCreateDatabase(
                    PathDB + nomeDB, MODE_PRIVATE, null);
        } catch (Exception e) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Errore nell'apertura del db: " +
                            UtilityWallpaper.getInstance().PrendeErroreDaException(e));
        }

        return  db;
    }

    public boolean CreazioneTabelle() {
        try {
            // SQLiteDatabase myDB = ApreDB();
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "posizioni "
                        + " (data VARCHAR, ora VARCHAR, latitudine VARCHAR, longitudine VARCHAR, speed VARCHAR, " +
                        "altitude VARCHAR, accuracy VARCHAR, distanza VARCHAR, wifi VARCHAR, livelloSegnale VARCHAR, " +
                        "tipoConnessione VARCHAR, livello VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS " +
                        "Accensioni " +
                        "(Domenica VARCHAR, Lunedi VARCHAR, Martedi VARCHAR, Mercoledi VARCHAR," +
                        "Giovedi VARCHAR, Venerdi VARCHAR, Sabato VARCHAR, " +
                        "OraAccDomenica VARCHAR, OraSpegnDomenica VARCHAR, " +
                        "OraAccLunedi VARCHAR, OraSpegnLunedi VARCHAR, " +
                        "OraAccMartedi VARCHAR, OraSpegnMartedi VARCHAR, " +
                        "OraAccMercoledi VARCHAR, OraSpegnMercoledi VARCHAR, " +
                        "OraAccGiovedi VARCHAR, OraSpegnGiovedi VARCHAR, " +
                        "OraAccVenerdi VARCHAR, OraSpegnVenerdi VARCHAR, " +
                        "OraAccSabato VARCHAR, OraSpegnSabato VARCHAR, " +
                        "GPSAttivo VARCHAR)";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni"
                        + " (Segue VARCHAR, PathSegnale VARCHAR, PathPercorso VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS " +
                        "puntiDiSpegnimento (" +
                        "lat VARCHAR, lon VARCHAR, Nome VARCHAR" +
                        ")";

                myDB.execSQL(sql);

                return true;
            } else {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "DB Non Valido");

                return false;
            }
        } catch (Exception e) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore creazione tabelle: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));

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
                        + "'" + (VariabiliStaticheGPS.getInstance().isSegue() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().isMostraSegnale() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().isMostraPercorso() ? "S" : "N") + "' "
                        + ") ";
                myDB.execSQL(sql);

                return true;
            } catch (SQLException e) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                // PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                // CreazioneTabelle();
                return false;
            }
        } else {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return false;
        }
    }

    public void ImpostaValoriDiDefault() {
        VariabiliStaticheGPS.getInstance().setSegue(true);
        VariabiliStaticheGPS.getInstance().setMostraSegnale(true);
        VariabiliStaticheGPS.getInstance().setMostraPercorso(true);
    }

    public int CaricaImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheGPS.getInstance().setSegue(c.getString(0).equals("S"));
                        VariabiliStaticheGPS.getInstance().setMostraSegnale(c.getString(1).equals("S"));
                        VariabiliStaticheGPS.getInstance().setMostraPercorso(c.getString(2).equals("S"));

                        return 0;
                    } catch (Exception e) {
                        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Errore try db carica impostazioni: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));

                        return -4;
                    }
                } else {
                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");

                    return -3;
                }
            } catch (Exception e) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -2;
            }
        } else {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
            return -1;
        }
    }

    public boolean ScrivePuntoDiSpegnimento(String Nome, Location l) {
        if (myDB != null) {
            try {
                String sql = "INSERT INTO"
                        + " puntiDiSpegnimento"
                        + " VALUES ("
                        + "'" + l.getLatitude() + "', "
                        + "'" + l.getLongitude() + "', "
                        + "'" + Nome.replace("'", "''") + "' "
                        + ") ";
                myDB.execSQL(sql);

                StrutturaPuntiSpegnimento s = new StrutturaPuntiSpegnimento();
                s.setLoc(l);
                s.setNome(Nome);

                List<StrutturaPuntiSpegnimento> ll = VariabiliStaticheGPS.getInstance().getListaPuntiDiSpegnimento();
                ll.add(s);
                VariabiliStaticheGPS.getInstance().setListaPuntiDiSpegnimento(ll);
            } catch (SQLException e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db punto di spegnimento: " + e.getMessage());
                PulisceDatiPS(context);
                CreazioneTabelle();
                ScrivePuntoDiSpegnimento(Nome, l);

                return false;
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public void EliminaPuntoDiSpegnimento(String Nome) {
        if (myDB != null) {
            try {
                String sql = "Delete From "
                        + " puntiDiSpegnimento"
                        + " Where Nome = '" + Nome.replace("'", "''") + "' ";
                myDB.execSQL(sql);

                CompattaDB();

                List<StrutturaPuntiSpegnimento> ll = new ArrayList<>();
                for (StrutturaPuntiSpegnimento s : VariabiliStaticheGPS.getInstance().getListaPuntiDiSpegnimento()) {
                    if (!s.getNome().equals(s.getNome())) {
                        ll.add(s);
                    }
                }
                VariabiliStaticheGPS.getInstance().setListaPuntiDiSpegnimento(ll);
            } catch (Exception e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore eliminazione db per punti di spegnimento: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiPS(context);
                CreazioneTabelle();
                EliminaPuntoDiSpegnimento(Nome);
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public int CaricaPuntiDiSpegnimento() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM puntiDiSpegnimento", null);
                if (c.getCount() > 0) {
                    List<StrutturaPuntiSpegnimento> lista = new ArrayList<>();

                    c.moveToFirst();
                    do {
                        StrutturaPuntiSpegnimento s = new StrutturaPuntiSpegnimento();

                        Location targetLocation = new Location("");//provider name is unnecessary
                        targetLocation.setLatitude(Double.parseDouble(c.getString(0)));
                        targetLocation.setLongitude(Double.parseDouble(c.getString(1)));
                        s.setLoc(targetLocation);
                        s.setNome(c.getString(2));

                        lista.add(s);
                    } while (c.moveToNext());

                    VariabiliStaticheGPS.getInstance().setListaPuntiDiSpegnimento(lista);

                    return 0;
                } else {
                    VariabiliStaticheGPS.getInstance().setListaPuntiDiSpegnimento(new ArrayList<>());

                    return -3;
                }
            } catch (Exception e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db per punti di spegnimento: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                // PulisceDatiPS(context);
                // CreazioneTabelle();
                // CaricaPuntiDiSpegnimento();

                return -2; // "Tabella creata di nuovo: " + e.getMessage();
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            VariabiliStaticheGPS.getInstance().setListaPuntiDiSpegnimento(new ArrayList<>());
            return -1; // "Db Non Valido";
        }
    }

    public Boolean ScriveAccensioni(Context context) {
        if (VariabiliStaticheGPS.getInstance().getAccensioneGPS() == null) {
            return false;
        }

        if (myDB != null) {
            try {
                String Imm = "";
                myDB.execSQL("Delete From Accensioni");

                String sql = "INSERT INTO"
                        + " Accensioni"
                        + " VALUES ("
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().isSpegnimentoAttivoDomenica() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().isSpegnimentoAttivoLunedi() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().isSpegnimentoAttivoMartedi() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().isSpegnimentoAttivoMercoledi() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().isSpegnimentoAttivoGiovedi() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().isSpegnimentoAttivoVenerdi() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().isSpegnimentoAttivoSabato() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraDisattivazioneDomenica()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraRiattivazioneDomenica()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraDisattivazioneLunedi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraRiattivazioneLunedi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraDisattivazioneMartedi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraRiattivazioneMartedi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraDisattivazioneMercoledi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraRiattivazioneMercoledi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraDisattivazioneGiovedi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraRiattivazioneGiovedi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraDisattivazioneVenerdi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraRiattivazioneVenerdi()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraDisattivazioneSabato()) + "', "
                        + "'" + (VariabiliStaticheGPS.getInstance().getAccensioneGPS().getOraRiattivazioneSabato()) + "', "
                        // + "'" + (VariabiliStaticheGPS.getInstance().isGpsAttivo() ? "S" : "N") + "' "
                        + "'-' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db accensioni: " + e.getMessage());
                // Log.getInstance().ScriveLog("ERRORE Su scrittura impostazioni: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                // Log.getInstance().ScriveLog("Pulizia tabelle");
                PulisceDatiAcc();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                ScriveAccensioni(context);

                return false;
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public boolean CaricaAccensioni(Context context) {
        // UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Controllo apertura db");
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Accensioni", null);
                if (c.getCount() > 0) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per accensioni");
                    c.moveToFirst();

                    try {
                        StrutturaAccensioneGPS s = new StrutturaAccensioneGPS();
                        s.setSpegnimentoAttivoDomenica(c.getString(0).equals("S"));
                        s.setSpegnimentoAttivoLunedi(c.getString(1).equals("S"));
                        s.setSpegnimentoAttivoMartedi(c.getString(2).equals("S"));
                        s.setSpegnimentoAttivoMercoledi(c.getString(3).equals("S"));
                        s.setSpegnimentoAttivoGiovedi(c.getString(4).equals("S"));
                        s.setSpegnimentoAttivoVenerdi(c.getString(5).equals("S"));
                        s.setSpegnimentoAttivoSabato(c.getString(6).equals("S"));
                        s.setOraDisattivazioneDomenica(c.getString(7));
                        s.setOraRiattivazioneDomenica(c.getString(8));
                        s.setOraDisattivazioneLunedi(c.getString(9));
                        s.setOraRiattivazioneLunedi(c.getString(10));
                        s.setOraDisattivazioneMartedi(c.getString(11));
                        s.setOraRiattivazioneMartedi(c.getString(12));
                        s.setOraDisattivazioneMercoledi(c.getString(13));
                        s.setOraRiattivazioneMercoledi(c.getString(14));
                        s.setOraDisattivazioneGiovedi(c.getString(15));
                        s.setOraRiattivazioneGiovedi(c.getString(16));
                        s.setOraDisattivazioneVenerdi(c.getString(17));
                        s.setOraRiattivazioneVenerdi(c.getString(18));
                        s.setOraDisattivazioneSabato(c.getString(19));
                        s.setOraRiattivazioneSabato(c.getString(20));
                        // VariabiliStaticheGPS.getInstance().setGpsAttivo(c.getString(21).equals("S"));
                        VariabiliStaticheGPS.getInstance().setAccensioneGPS(s);

                        return true; // "Impostazioni caricate correttamente. Risoluzione: " + VariabiliStatiche.getInstance().getRisoluzione();
                    } catch (Exception e) {
                        PulisceDati();
                        CreazioneTabelle();
                        CaricaAccensioni(context);

                        return false; //  "ERROR: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
                    }
                } else {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db accensione. Imposto default");

                    StrutturaAccensioneGPS s = new StrutturaAccensioneGPS();
                    s.setSpegnimentoAttivoDomenica(true);
                    s.setSpegnimentoAttivoLunedi(true);
                    s.setSpegnimentoAttivoMartedi(true);
                    s.setSpegnimentoAttivoMercoledi(true);
                    s.setSpegnimentoAttivoGiovedi(true);
                    s.setSpegnimentoAttivoVenerdi(true);
                    s.setSpegnimentoAttivoSabato(true);
                    s.setOraDisattivazioneDomenica("08:00");
                    s.setOraRiattivazioneDomenica("16:00");
                    s.setOraDisattivazioneLunedi("08:00");
                    s.setOraRiattivazioneLunedi("16:00");
                    s.setOraDisattivazioneMartedi("08:00");
                    s.setOraRiattivazioneMartedi("16:00");
                    s.setOraDisattivazioneMercoledi("08:00");
                    s.setOraRiattivazioneMercoledi("16:00");
                    s.setOraDisattivazioneGiovedi("08:00");
                    s.setOraRiattivazioneGiovedi("16:00");
                    s.setOraDisattivazioneVenerdi("08:00");
                    s.setOraRiattivazioneVenerdi("16:00");
                    s.setOraDisattivazioneSabato("08:00");
                    s.setOraRiattivazioneSabato("16:00");
                    VariabiliStaticheGPS.getInstance().setGpsAttivo(true);
                    VariabiliStaticheGPS.getInstance().setAccensioneGPS(s);

                    return true;
                }
            } catch (Exception e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db accensioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiAcc();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                // CaricaAccensioni(context);

                return false; // "Tabella creata di nuovo: " + e.getMessage();
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
            return false; // "Db Non Valido";
        }
    }

    public Boolean AggiungePosizione(StrutturaGps s) {
        if (myDB != null) {
            try {
                String sql = "";
                sql = "INSERT INTO"
                        + " posizioni"
                        + " (data, ora, latitudine, longitudine, speed, altitude, accuracy, "
                        + "distanza, wifi, livelloSegnale, tipoConnessione, livello)"
                        + " VALUES ("
                        + "'" + s.getData() + "', "
                        + "'" + s.getOra() + "', "
                        + "'" + s.getLat() + "', "
                        + "'" + s.getLon() + "', "
                        + "'" + s.getSpeed() + "', "
                        + "'" + s.getAltitude() + "', "
                        + "'" + s.getAccuracy() + "', "
                        + "'" + s.getDistanza() + "', "
                        + "'" + (s.isWifi()  ? "S" : "N") + "', "
                        + "'" + s.getLivelloSegnale() + "', "
                        + "'" + s.getTipoSegnale() + "', "
                        + "'" + s.getLevel() + "' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Errore aggiunta posizione: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e) +
                        " Riprova: " + Riprova);

                PulisceDati();
                CreazioneTabelle();
                if (!Riprova) {
                    Riprova = true;
                    AggiungePosizione(s);
                }
                Riprova = false;

                return false;
            }
        }

        return true;
    }

    public void EliminaPosizioni(String Data) {
        if (myDB != null) {
            try {
                String sql = "DELETE FROM posizioni Where data = '" + Data + "'";
                myDB.execSQL(sql);

                CompattaDB();
            } catch (Exception e) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Errore eliminazione dati: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public List<StrutturaGps> RitornaPosizioni(String Data) {
        List<StrutturaGps> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM posizioni Where data = '" + Data + "' Order By ora", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        StrutturaGps s = new StrutturaGps();
                        s.setData(c.getString(0));
                        s.setOra(c.getString(1));
                        s.setLat(Double.parseDouble(c.getString(2)));
                        s.setLon(Double.parseDouble(c.getString(3)));
                        s.setSpeed(Float.parseFloat(c.getString(4)));
                        s.setAltitude(Float.parseFloat(c.getString(5)));
                        s.setAccuracy(Float.parseFloat(c.getString(6)));
                        s.setDistanza(Float.parseFloat(c.getString(7)));
                        s.setWifi(c.getString(8).equals("S"));
                        s.setLivelloSegnale(c.getInt(9));
                        s.setTipoSegnale(c.getString(10));
                        s.setLevel(c.getInt(11));

                        lista.add(s);
                    } while (c.moveToNext());
                } else {
                }
                c.close();
            } catch (Exception e) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Errore ritorno dati: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e) +
                        " Riprova: " + Riprova);

                PulisceDati();
                CreazioneTabelle();

                if (!Riprova) {
                    Riprova = true;

                    return RitornaPosizioni(Data);
                } else {
                    return lista;
                }
            }
        } else {
            return lista;
        }

        return lista;
    }

    public StrutturaGps RitornaUltimaPosizione(String Data) {
        StrutturaGps lista = null;

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM posizioni Where data = '" + Data + "' Order By ora desc", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    StrutturaGps s = new StrutturaGps();
                    s.setData(c.getString(0));
                    s.setOra(c.getString(1));
                    s.setLat(Double.parseDouble(c.getString(2)));
                    s.setLon(Double.parseDouble(c.getString(3)));
                    s.setSpeed(Float.parseFloat(c.getString(4)));
                    s.setAltitude(Float.parseFloat(c.getString(5)));
                    s.setAccuracy(Float.parseFloat(c.getString(6)));
                    s.setDistanza(Float.parseFloat(c.getString(7)));

                    lista = s;
                }
                c.close();
            } catch (Exception e) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Errore ritorno ultima posizione: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e) +
                                " Riprova: " + Riprova);

                PulisceDati();
                CreazioneTabelle();

                if (!Riprova) {
                    Riprova = true;

                    return RitornaUltimaPosizione(Data);
                } else {
                    return lista;
                }
            }
        } else {
            return lista;
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
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table posizioni");

                return true;
            } catch (Exception ignored) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void PulisceDatiAcc() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table Accensioni");
            } catch (Exception ignored) {

            }
        }
    }

    public void PulisceDatiIMP() {
        if (myDB != null) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");
            } catch (Exception ignored) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());
            }
        }
    }

    public void PulisceDatiPS(Context context) {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db punti di spegnimento");
            try {
                myDB.execSQL("Drop Table puntiDiSpegnimento");
            } catch (Exception ignored) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db: " + ignored.getMessage());
            }
        }
    }
}
