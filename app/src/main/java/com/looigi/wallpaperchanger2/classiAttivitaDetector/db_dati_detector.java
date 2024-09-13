package com.looigi.wallpaperchanger2.classiAttivitaDetector;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class db_dati_detector {
    private static final String NomeMaschera = "DBDATI";

    // private final String PathDB = VariabiliStatiche.getInstance().getPercorsoDIR()+"/DB/";
    private String PathDB;
    private SQLiteDatabase myDB = null;
    private boolean Controlla = true;

    public db_dati_detector(Context context) {
        PathDB = UtilityDetector.getInstance().PrendePathDB(context);

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception ignored) {

        }
        myDB = ApreDB(context);
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

    public void CreazioneTabelle() {
        try {
            // SQLiteDatabase myDB = ApreDB();
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni "
                        + " (FaiLog VARCHAR,  TipologiaScatto VARCHAR, Secondi VARCHAR, Fotocamera VARCHAR, " +
                        "Risoluzione VARCHAR, Estensione VARCHAR, Vibrazione VARCHAR, NumeroScatti VARCHAR, " +
                        "Anteprima VARCHAR, Orientamento VARCHAR, Lingua VARCHAR, DimensioniThumbs VARCHAR, DimensioniThumbsM VARCHAR, " +
                        "VisualizzaToast VARCHAR);";

                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            // Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + UtilityDetector.getInstance().PrendeErroreDaException(ignored));
            int a = 0;
        }
    }

    public boolean CaricaImpostazioni(Context context) {
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
                        VariabiliStaticheDetector.getInstance().setRisoluzione(c.getString(4));
                        VariabiliStaticheDetector.getInstance().setEstensione(Integer.parseInt(c.getString(5)));
                        VariabiliStaticheDetector.getInstance().setVibrazione(c.getString(6).equals("S"));
                        VariabiliStaticheDetector.getInstance().setNumeroScatti(Integer.parseInt(c.getString(7)));
                        VariabiliStaticheDetector.getInstance().setAnteprima(c.getString(8));
                        VariabiliStaticheDetector.getInstance().setOrientamento(Integer.parseInt(c.getString(9)));
                        VariabiliStaticheDetector.getInstance().setLingua(c.getString(10));
                        VariabiliStaticheDetector.getInstance().setDimensioniThumbs(Integer.parseInt(c.getString(11)));
                        VariabiliStaticheDetector.getInstance().setDimensioniThumbsM(Integer.parseInt(c.getString(12)));
                        VariabiliStaticheDetector.getInstance().setVisualizzaToast(c.getString(13).equals("S"));

                        return true; // "Impostazioni caricate correttamente. Risoluzione: " + VariabiliStatiche.getInstance().getRisoluzione();
                    } catch (Exception e) {
                        PulisceDati(context);
                        CreazioneTabelle();
                        CaricaImpostazioni(context);

                        return false; //  "ERROR: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
                    }
                } else {
                    // GBTakePictureNoPreview c2 = new GBTakePictureNoPreview();
                    // c2.ImpostaContext(VariabiliStatiche.getInstance().getContext());
                    // c2.setUseBackCamera();
                    // VariabiliStatiche.getInstance().Dimensioni = VariabiliStatiche.getInstance().getCamera().RitornaRisoluzioni();
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db. Imposto default");
                    if (VariabiliStaticheDetector.getInstance().Dimensioni == null) {
                        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Leggo risoluzioni");
                        if (VariabiliStaticheDetector.getInstance().getCamera() != null) {
                            VariabiliStaticheDetector.getInstance().getCamera().RitornaRisoluzioni();
                            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Risoluzioni Lette: " + VariabiliStaticheDetector.getInstance().Dimensioni.size());
                        } else {
                            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Risoluzioni NON Lette: Camera non rilevata");
                        }
                    }

                    String risol = "1024x768";

                    if (VariabiliStaticheDetector.getInstance().Dimensioni != null) {
                        UtilityDetector.getInstance().RitornaRisoluzioneMassima(VariabiliStaticheDetector.getInstance().Dimensioni);
                    }

                    VariabiliStaticheDetector.getInstance().setFaiLog(true);
                    VariabiliStaticheDetector.getInstance().setTipologiaScatto(2);
                    VariabiliStaticheDetector.getInstance().setSecondi(3);
                    VariabiliStaticheDetector.getInstance().setFotocamera(1);
                    VariabiliStaticheDetector.getInstance().setRisoluzione(risol);
                    VariabiliStaticheDetector.getInstance().setEstensione(2);
                    VariabiliStaticheDetector.getInstance().setVibrazione(true);
                    VariabiliStaticheDetector.getInstance().setNumeroScatti(3);
                    VariabiliStaticheDetector.getInstance().setAnteprima("S");
                    VariabiliStaticheDetector.getInstance().setOrientamento(0);
                    VariabiliStaticheDetector.getInstance().setLingua("ITALIANO");
                    VariabiliStaticheDetector.getInstance().setDimensioniThumbs(70);
                    VariabiliStaticheDetector.getInstance().setDimensioniThumbsM(50);
                    VariabiliStaticheDetector.getInstance().setVisualizzaToast(true);

                    Controlla = false;
                    ScriveImpostazioni(context);
                    Controlla = true;

                    return true; // "Impostazioni impostate da zero, correttamente. Risoluzione: " + risol;
                }
            } catch (Exception e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                // Log.getInstance().ScriveLog("ERRORE Su scrittura immagini locali: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                // Log.getInstance().ScriveLog("Pulizia tabelle");
                PulisceDati(context);
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                CaricaImpostazioni(context);

                return false; // "Tabella creata di nuovo: " + e.getMessage();
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
            return false; // "Db Non Valido";
        }
    }

    public Boolean ScriveImpostazioni(Context context) {
        if (Controlla && !VariabiliStaticheDetector.getInstance().isLetteImpostazioni()) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Impostazioni non lette. Non effettuo il salvataggio");
            return false;
        }

        if (myDB != null) {
            try {
                String Imm = "";
                myDB.execSQL("Delete From Impostazioni");

                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " VALUES ("
                        + "'" + (VariabiliStaticheDetector.getInstance().isFaiLog() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getTipologiaScatto()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getSecondi()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getFotocamera()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getRisoluzione()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getEstensione()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().isVibrazione() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getNumeroScatti()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getAnteprima()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getOrientamento()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getLingua()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getDimensioniThumbs()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().getDimensioniThumbsM()) + "', "
                        + "'" + (VariabiliStaticheDetector.getInstance().isVisualizzaToast() ? "S" : "N") + "' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db: " + e.getMessage());
                // Log.getInstance().ScriveLog("ERRORE Su scrittura impostazioni: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                // Log.getInstance().ScriveLog("Pulizia tabelle");
                PulisceDati(context);
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                ScriveImpostazioni(context);

                return false;
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public void PulisceDati(Context context) {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db");
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table Impostazioni");
            } catch (Exception ignored) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db: " + ignored.getMessage());
            }
        }
    }
}
