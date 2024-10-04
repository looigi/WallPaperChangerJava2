package com.looigi.wallpaperchanger2.classiGps;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class db_dati_gps {
    private static final String NomeMaschera = "DBDATI";
    private String PathDB = "";
    private final SQLiteDatabase myDB;
    private Context context;
    private boolean Riprova = false;

    public db_dati_gps(Context context) {
        this.context = context;
        PathDB = context.getFilesDir() + "/DB/";

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception e) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore costruttore: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
        }

        myDB = ApreDB();
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

    public void CreazioneTabelle() {
        if (myDB == null) {
            ApreDB();
        }

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
            }
        } catch (Exception e) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore creazione tabelle: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
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
        if (myDB == null) {
            ApreDB();
        }

        if (myDB != null) {
            try {
                String sql = "DELETE FROM posizioni Where data = '" + Data + "'";
                myDB.execSQL(sql);
            } catch (Exception e) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Errore eliminazione dati: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public List<StrutturaGps> RitornaPosizioni(String Data) {
        if (myDB == null) {
            ApreDB();
        }

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
        if (myDB == null) {
            ApreDB();
        }

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

    public void PulisceDati() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table posizioni");
            } catch (Exception ignored) {

            }
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
}
