package com.looigi.wallpaperchanger2.classeVideo;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFilms.UtilityFilms;

import java.io.File;

public class db_dati_video {
    private static final String NomeMaschera = "DB_Video";

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

    public db_dati_video(Context context) {
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
            String nomeDB = "dati_video.db";
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
                        + " (Random VARCHAR, SettingsAperto VARCHAR, BarraVisibiòe VARCHAR, NumeroFrames VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "UltimoVideo"
                        + " (video VARCHAR, idvideo VARCHAR);";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Snapshots "
                        + "(id VARCHAR);";

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
                String sql = "INSERT INTO "
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

    public String CaricaVideo() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM UltimoVideo", null);
                if (c.getCount() > 0) {
                    UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per ultimo video");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheVideo.getInstance().setUltimoLink(c.getString(0));
                        VariabiliStaticheVideo.getInstance().setIdUltimoVideo(Integer.parseInt(c.getString(1)));

                        return VariabiliStaticheVideo.getInstance().getUltimoLink();
                    } catch (Exception e) {
                        PulisceDatiV();
                        CreazioneTabelle();
                        return CaricaVideo();
                    }
                } else {
                    UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per ultimo video. Imposto default");

                    return "";
                }
            } catch (Exception e) {
                UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db ultimo video: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiV();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return CaricaVideo();
            }
        } else {
            UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return "";
        }
    }

    public Boolean ScriveUltimoVideo() {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From UltimoVideo");

                String sql = "INSERT INTO"
                        + " UltimoVideo"
                        + " VALUES ("
                        + "'" + VariabiliStaticheVideo.getInstance().getUltimoLink().replace("'", "''") + "', "
                        + "'" + VariabiliStaticheVideo.getInstance().getIdUltimoVideo() + "' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo video: " + e.getMessage());
                PulisceDatiV();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveUltimoVideo();
            }
        } else {
            UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
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
                        + "'" + VariabiliStaticheVideo.getInstance().getRandom() + "',"
                        + "'" + (VariabiliStaticheVideo.getInstance().isSettingsAperto() ? "S" : "N") + "',"
                        + "'" + (VariabiliStaticheVideo.getInstance().isBarraVisibile() ? "S" : "N") + "', "
                        + "'" + VariabiliStaticheVideo.getInstance().getNumeroFrames() + "' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,
                        "Errore su scrittura db per impostazioni: " + e.getMessage());
                // PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveImpostazioni();
            }
        } else {
            UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public void ImpostaValoriDiDefault() {
        VariabiliStaticheVideo.getInstance().setRandom("S");
        VariabiliStaticheVideo.getInstance().setSettingsAperto(true);
        VariabiliStaticheVideo.getInstance().setBarraVisibile(true);
        VariabiliStaticheVideo.getInstance().setNumeroFrames(10);
    }

    public int CaricaImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        VariabiliStaticheVideo.getInstance().setRandom(c.getString(0));
                        VariabiliStaticheVideo.getInstance().setSettingsAperto(c.getString(1).equals("S"));
                        VariabiliStaticheVideo.getInstance().setBarraVisibile(c.getString(2).equals("S"));
                        VariabiliStaticheVideo.getInstance().setNumeroFrames(Integer.parseInt(c.getString(3)));

                        return 0;
                    } catch (Exception e) {
                        UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Errore try db carica impostazioni: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));

                        return -4;
                    }
                } else {
                    UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");

                    return -3;
                }
            } catch (Exception e) {
                UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -2;
            }
        } else {
            UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return -1;
        }
    }

    public boolean PulisceDati() {
        if (myDB != null) {
            UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");

                return true;
            } catch (Exception ignored) {
                UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());

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
            UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db ultimo video");
            try {
                myDB.execSQL("Drop Table UltimoVideo");
            } catch (Exception ignored) {
                UtilityVideo.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db ultimo video: " + ignored.getMessage());
            }
        }
    }
}
