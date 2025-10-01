package com.looigi.wallpaperchanger2.Detector;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.Mappe.MappeEGps.VariabiliStaticheGPS;

import java.io.File;

public class db_dati_detector {
    private static final String NomeMaschera = "DB_Dati_Detector";

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

    public db_dati_detector(Context context) {
        this.context = context;
        PathDB = UtilityDetector.getInstance().PrendePathDB(context);

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception ignored) {

        }
        myDB = ApreDB(context);
    }

    public void ChiudeDB() {
        if (myDB != null) {
            myDB.close();
            myDB = null;
        }
    }

    private SQLiteDatabase ApreDB(Context context) {
        SQLiteDatabase db = null;

        UtilityDetector.getInstance().CreaCartelle(PathDB, "DB");

        try {
            String nomeDB = "dati_detector.db";
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
            // SQLiteDatabase myDB = ApreDB();
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni "
                        + " (FaiLog VARCHAR,  TipologiaScatto VARCHAR, Secondi VARCHAR, Fotocamera VARCHAR, " +
                        "Risoluzione VARCHAR, Estensione VARCHAR, Vibrazione VARCHAR, NumeroScatti VARCHAR, " +
                        "Anteprima VARCHAR, Orientamento VARCHAR, Lingua VARCHAR, DimensioniThumbs VARCHAR, DimensioniThumbsM VARCHAR, " +
                        "VisualizzaToast VARCHAR, GpsPreciso VARCHAR, GpsMS VARCHAR, GPSMeters VARCHAR, FotoPower VARCHAR, " +
                        "MetriPS VARCHAR, QuantiPunti VARCHAR, ModalitaGPS VARCHAR);";

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
                        "OraAccSabato VARCHAR, OraSpegnSabato VARCHAR " +
                        ")";

                myDB.execSQL(sql);

                return true;
            } else {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,
                        "DB Non Valido");

                return false;
            }
        } catch (Exception e) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore su impostazione valori: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e));

            // Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + UtilityDetector.getInstance().PrendeErroreDaException(ignored));
            return false;
        }
    }

    public void ImpostaValoriDiDefault() {
        VariabiliStaticheDetector.getInstance().setFaiLog(true);
        VariabiliStaticheDetector.getInstance().setTipologiaScatto(2);
        VariabiliStaticheDetector.getInstance().setSecondi(3);
        VariabiliStaticheDetector.getInstance().setFotocamera(1);
        // VariabiliStaticheDetector.getInstance().setRisoluzione(risol);
        VariabiliStaticheDetector.getInstance().setRisoluzione("");
        VariabiliStaticheDetector.getInstance().setEstensione(2);
        VariabiliStaticheDetector.getInstance().setVibrazione(true);
        VariabiliStaticheDetector.getInstance().setNumeroScatti(3);
        VariabiliStaticheDetector.getInstance().setAnteprima("S");
        VariabiliStaticheDetector.getInstance().setOrientamento(0);
        VariabiliStaticheDetector.getInstance().setLingua("ITALIANO");
        VariabiliStaticheDetector.getInstance().setDimensioniThumbs(70);
        VariabiliStaticheDetector.getInstance().setDimensioniThumbsM(50);
        VariabiliStaticheDetector.getInstance().setVisualizzaToast(true);
        VariabiliStaticheDetector.getInstance().setGpsPreciso(false);
        VariabiliStaticheDetector.getInstance().setGpsMs(10000);
        VariabiliStaticheDetector.getInstance().setGpsMeters(5);
        VariabiliStaticheDetector.getInstance().setFotoSuTriploTastoCuffie(true);
        VariabiliStaticheGPS.getInstance().setDistanzaMetriPerPS(75);
        VariabiliStaticheGPS.getInstance().setQuantiPuntiSumappa(2000);
        VariabiliStaticheDetector.getInstance().setModalitaGps(true);
    }

    public int CaricaImpostazioni() {
        // UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Controllo apertura db");
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheDetector.getInstance().setFaiLog(c.getString(0).equals("S"));
                        VariabiliStaticheDetector.getInstance().setTipologiaScatto(Integer.parseInt(c.getString(1)));
                        VariabiliStaticheDetector.getInstance().setSecondi(Integer.parseInt(c.getString(2)));
                        VariabiliStaticheDetector.getInstance().setFotocamera(Integer.parseInt(c.getString(3)));
                        // VariabiliStaticheDetector.getInstance().setRisoluzione(c.getString(4));
                        VariabiliStaticheDetector.getInstance().setRisoluzione("");
                        // VariabiliStaticheDetector.getInstance().setEstensione(Integer.parseInt(c.getString(5)));
                        VariabiliStaticheDetector.getInstance().setEstensione(2);
                        VariabiliStaticheDetector.getInstance().setVibrazione(c.getString(6).equals("S"));
                        VariabiliStaticheDetector.getInstance().setNumeroScatti(Integer.parseInt(c.getString(7)));
                        VariabiliStaticheDetector.getInstance().setAnteprima(c.getString(8));
                        VariabiliStaticheDetector.getInstance().setOrientamento(Integer.parseInt(c.getString(9)));
                        VariabiliStaticheDetector.getInstance().setLingua(c.getString(10));
                        VariabiliStaticheDetector.getInstance().setDimensioniThumbs(Integer.parseInt(c.getString(11)));
                        VariabiliStaticheDetector.getInstance().setDimensioniThumbsM(Integer.parseInt(c.getString(12)));
                        VariabiliStaticheDetector.getInstance().setVisualizzaToast(c.getString(13).equals("S"));
                        VariabiliStaticheDetector.getInstance().setGpsPreciso(c.getString(14).equals("S"));
                        VariabiliStaticheDetector.getInstance().setGpsMs(Integer.parseInt(c.getString(15)));
                        VariabiliStaticheDetector.getInstance().setGpsMeters(Integer.parseInt(c.getString(16)));
                        VariabiliStaticheDetector.getInstance().setFotoSuTriploTastoCuffie(c.getString(17).equals("S"));
                        VariabiliStaticheGPS.getInstance().setDistanzaMetriPerPS(Integer.parseInt(c.getString(18)));
                        VariabiliStaticheGPS.getInstance().setQuantiPuntiSumappa(Integer.parseInt(c.getString(19)));
                        VariabiliStaticheDetector.getInstance().setModalitaGps(c.getString(20).equals("S"));

                        return 0; // "Impostazioni caricate correttamente. Risoluzione: " + VariabiliStatiche.getInstance().getRisoluzione();
                    } catch (Exception e) {
                        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore su impostazione valori: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));
                        // PulisceDati(context, daDove);
                        // CreazioneTabelle();
                        // CaricaImpostazioni(context, daDove);

                        return -4; //  "ERROR: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
                    }
                } else {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db. Imposto default");

                    return -3;
                }
                /* else {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db. Imposto default");

                    VariabiliStaticheDetector.getInstance().setFaiLog(true);
                    VariabiliStaticheDetector.getInstance().setTipologiaScatto(2);
                    VariabiliStaticheDetector.getInstance().setSecondi(3);
                    VariabiliStaticheDetector.getInstance().setFotocamera(1);
                    // VariabiliStaticheDetector.getInstance().setRisoluzione(risol);
                    VariabiliStaticheDetector.getInstance().setRisoluzione("");
                    VariabiliStaticheDetector.getInstance().setEstensione(2);
                    VariabiliStaticheDetector.getInstance().setVibrazione(true);
                    VariabiliStaticheDetector.getInstance().setNumeroScatti(3);
                    VariabiliStaticheDetector.getInstance().setAnteprima("S");
                    VariabiliStaticheDetector.getInstance().setOrientamento(0);
                    VariabiliStaticheDetector.getInstance().setLingua("ITALIANO");
                    VariabiliStaticheDetector.getInstance().setDimensioniThumbs(70);
                    VariabiliStaticheDetector.getInstance().setDimensioniThumbsM(50);
                    VariabiliStaticheDetector.getInstance().setVisualizzaToast(true);
                    VariabiliStaticheDetector.getInstance().setGpsPreciso(true);
                    VariabiliStaticheDetector.getInstance().setGpsMs(1000);
                    VariabiliStaticheDetector.getInstance().setGpsMeters(5);
                    VariabiliStaticheDetector.getInstance().setFotoSuPower(true);
                    VariabiliStaticheGPS.getInstance().setDistanzaMetriPerPS(50);

                    Controlla = false;
                    ScriveImpostazioni(context, daDove);
                    Controlla = true;

                    return true; // "Impostazioni impostate da zero, correttamente. Risoluzione: " + risol;
                } */
            } catch (Exception e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                // Log.getInstance().ScriveLog("ERRORE Su scrittura immagini locali: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                // Log.getInstance().ScriveLog("Pulizia tabelle");
                // PulisceDati(context, daDove);
                // Log.getInstance().ScriveLog("Creazione tabelle");
                // CreazioneTabelle();
                // CaricaImpostazioni(context, daDove);

                return -2; // "Tabella creata di nuovo: " + e.getMessage();
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return -1; // "Db Non Valido";
        }
    }

    public boolean ScriveImpostazioni(Context context, String daDove) {
        /* if (Controlla && !VariabiliStaticheDetector.getInstance().isLetteImpostazioni()) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Impostazioni non lette. Non effettuo il salvataggio");
            return false;
        } */

        if (myDB != null) {
            try {
                myDB.execSQL("Delete From Impostazioni");

                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " VALUES ("
                        + "'" + (VariabiliStaticheDetector.getInstance().isFaiLog() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getTipologiaScatto()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getSecondi()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getFotocamera()) + "', "
                        // + "'" + (VariabiliStaticheDetector.getInstance().getRisoluzione()) + "', "
                        + "'', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getEstensione()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().isVibrazione() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getNumeroScatti()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getAnteprima()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getOrientamento()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getLingua()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getDimensioniThumbs()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getDimensioniThumbsM()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().isVisualizzaToast() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().isGpsPreciso() ? "S" : "N") + "', "
                        + " " + VariabiliStaticheDetector.getInstance().getGpsMs() + ", "
                        + " " + VariabiliStaticheDetector.getInstance().getGpsMeters() + ", "
                        + "'" + (VariabiliStaticheDetector.getInstance().isFotoSuTriploTastoCuffie() ? "S" : "N") + "', "
                        + "'" + VariabiliStaticheGPS.getInstance().getDistanzaMetriPerPS() + "', "
                        + "'" + VariabiliStaticheGPS.getInstance().getQuantiPuntiSumappa() + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getModalitaGps() ? "S" : "N") + "' "
                        + ") ";
                myDB.execSQL(sql);

                return true;
            } catch (SQLException e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db: " + e.getMessage());
                // Log.getInstance().ScriveLog("ERRORE Su scrittura impostazioni: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                // Log.getInstance().ScriveLog("Pulizia tabelle");
                // PulisceDati(context, daDove);
                // Log.getInstance().ScriveLog("Creazione tabelle");
                // CreazioneTabelle();
                // ScriveImpostazioni(context, daDove);

                return false;
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return false;
        }
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public boolean PulisceDati(Context context, String daDove) {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db");
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table Impostazioni");

                CompattaDB();

                // UtilityDetector.getInstance().VisualizzaPOPUP(
                //         context, "DATI DETECTOR Eliminati.\nDa operazione " + daDove, false, 0
                // );

                return true;
            } catch (Exception ignored) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db: " + ignored.getMessage());

                return false;
            }
        } else {
            return false;
        }
    }
}
