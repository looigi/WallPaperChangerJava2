package com.looigi.wallpaperchanger2.classeWallpaper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class db_dati_wallpaper {
    private static final String NomeMaschera = "DB_Dati_Wallpaper";
    private String PathDB = "";
    private SQLiteDatabase myDB;
    private Context context;
    // private boolean Controlla = true;

    public boolean DbAperto() {
        if (myDB != null) {
            return true;
        } else {
            return false;
        }
    }

    public db_dati_wallpaper(Context context) {
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Instanzio db dati");

        this.context = context;
        // /data/user/0/com.looigi.wallpaperchanger2/files/LooigiSoft/wallpaperchanger2/DB/
        PathDB = context.getFilesDir() + "/DB/";

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cartella: " + PathDB);

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
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Apro DB");

        SQLiteDatabase db = null;
        try {
            String nomeDB = "dati_wallpaper.db";
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Nome DB: " + PathDB + nomeDB);

            db = context.openOrCreateDatabase(
                    PathDB + nomeDB, MODE_PRIVATE, null);
        } catch (Exception e) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "ERRORE Nell'apertura del db: " +
                    UtilityWallpaper.getInstance().PrendeErroreDaException(e));
            // Utility.getInstance().ApreToast(context, "Errore apertura DB: " +
            //         Utility.getInstance().PrendeErroreDaException(e));
        }

        return  db;
    }

    public boolean CreazioneTabelle() {
        try {
            // SQLiteDatabase myDB = ApreDB();
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni "
                        + " (UltimaImmagineNome VARCHAR, UltimaImmaginePath VARCHAR, SecondiAlcambio VARCHAR, PathImmagini VARCHAR, Offline VARCHAR, " +
                        "Blur VARCHAR, Resize VARCHAR, ScriveTesto VARCHAR, OnOff VARCHAR, Home VARCHAR, Lock VARCHAR, " +
                        "Detector VARCHAR, Espansa VARCHAR, SoloVolto VARCHAR, Effetti VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "ListaImmaginiLocali "
                        + " (ImmagineNome VARCHAR, ImmaginePath VARCHAR, Data VARCHAR, Dimensione VARCHAR);";
                myDB.execSQL(sql);

                return true;
            } else {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"DB Non valido");

                return false;
            }
        } catch (Exception e) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"ERRORE Nella creazione delle tabelle: " +
                    UtilityWallpaper.getInstance().PrendeErroreDaException(e));

            return false;
        }
    }

    public boolean EliminaImmaginiInLocale() {
        if (myDB != null) {
            myDB.execSQL("Delete From ListaImmaginiLocali");
        }

        return true;
    }

    public boolean ScriveImmagineInLocale(String Nome, String Path, String Data, String Dimensione) {
        if (myDB != null) {
            try {
                String sql = "INSERT INTO"
                        + " ListaImmaginiLocali"
                        + " (ImmagineNome, ImmaginePath, Data, Dimensione)"
                        + " VALUES ("
                        + "'" + Nome.replace("'","''") + "', "
                        + "'" + Path.replace("'","''") + "', "
                        + "'" + Data.replace("'","''") + "', "
                        + "'" + Dimensione.replace("'","''") + "' "
                        + ")";
                myDB.execSQL(sql);
            } catch (Exception e) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"ERRORE Su scrittura immagini locali: " +
                        UtilityWallpaper.getInstance().PrendeErroreDaException(e));
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Pulizia tabelle");
                PulisceDatiIL();
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Creazione tabelle");
                CreazioneTabelle();

                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean CaricaImmaginiInLocale() {
        if (myDB != null) {
            try {
                List<StrutturaImmagine> listaImmagini = new ArrayList<>();
                Cursor c = myDB.rawQuery("SELECT * FROM ListaImmaginiLocali", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        StrutturaImmagine s = new StrutturaImmagine();
                        s.setImmagine(c.getString(0));
                        s.setPathImmagine(c.getString(1));
                        s.setDataImmagine(c.getString(2));
                        s.setDimensione(c.getString(3));

                        listaImmagini.add(s);
                    } while (c.moveToNext());

                    VariabiliStaticheWallpaper.getInstance().setListaImmagini(listaImmagini);
                } else {
                    return false;
                }
            } catch (Exception e) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"ERRORE Su scrittura immagini locali: " +
                        UtilityWallpaper.getInstance().PrendeErroreDaException(e));
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Pulizia tabelle");
                PulisceDatiIL();
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Creazione tabelle");
                CreazioneTabelle();
                CaricaImmaginiInLocale();

                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean ScriveImpostazioni() {
        /* if (Controlla && !VariabiliStaticheWallpaper.getInstance().isLetteImpostazioni()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Impostazioni non lette. Non effettuo il salvataggio");
            return false;
        } */

        if (myDB != null) {
            try {
                String Imm = "";
                if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
                    Imm = VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getImmagine();
                }
                String PathImm = "";
                if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
                    PathImm = VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getPathImmagine();
                } else {
                    PathImm = "";
                }
                myDB.execSQL("Delete From Impostazioni");

                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " (UltimaImmagineNome, UltimaImmaginePath, SecondiAlCambio, PathImmagini, Offline, Blur, " +
                            "Resize, ScriveTesto, OnOff, Home, Lock, Detector, Espansa, SoloVolto, Effetti)"
                        + " VALUES ("
                        + "'" + (Imm) + "', "
                        + "'" + (PathImm) + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().getMinutiAttesa()) + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().getPercorsoIMMAGINI()) + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isOffline() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isBlur() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isResize() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isScriveTestoSuImmagine()  ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isOnOff() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isHome() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isLock() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheStart.getInstance().isDetector() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isEspansa() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isSoloVolti() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheWallpaper.getInstance().isEffetti() ? "S" : "N") + "' "
                        + ") ";
                myDB.execSQL(sql);

                return true;
            } catch (SQLException e) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "ERRORE Su scrittura impostazioni: " +
                        UtilityWallpaper.getInstance().PrendeErroreDaException(e));
                // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Pulizia tabelle");
                // PulisceDati();
                // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Creazione tabelle");
                // CreazioneTabelle();
                // ScriveImpostazioni();

                return false;
            }
        } else {
            return false;
        }
    }

    public int LeggeImpostazioni() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    StrutturaImmagine si = new StrutturaImmagine();
                    si.setImmagine(c.getString(0));
                    si.setPathImmagine(c.getString(1));
                    VariabiliStaticheWallpaper.getInstance().setUltimaImmagine(si);

                    VariabiliStaticheWallpaper.getInstance().setMinutiAttesa(Integer.parseInt(c.getString(2)));
                    VariabiliStaticheWallpaper.getInstance().setPercorsoIMMAGINI(c.getString(3));
                    VariabiliStaticheWallpaper.getInstance().setOffline(c.getString(4).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setBlur(c.getString(5).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setResize(c.getString(6).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setScriveTestoSuImmagine(c.getString(7).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setOnOff(c.getString(8).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setHome(c.getString(9).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setLock(c.getString(10).equals("S"));
                    VariabiliStaticheStart.getInstance().setDetector(c.getString(11).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setEspansa(c.getString(12).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setSoloVolti(c.getString(13).equals("S"));
                    VariabiliStaticheWallpaper.getInstance().setEffetti(c.getString(14).equals("S"));

                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"ON/OFF: " + VariabiliStaticheWallpaper.getInstance().isOnOff());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Secondi al cambio: " + VariabiliStaticheWallpaper.getInstance().getMinutiAttesa());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Percorso immagini: " + VariabiliStaticheWallpaper.getInstance().getPercorsoIMMAGINI());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Offline: " + VariabiliStaticheWallpaper.getInstance().isOffline());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Blur: " + VariabiliStaticheWallpaper.getInstance().isBlur());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Resize: " + VariabiliStaticheWallpaper.getInstance().isResize());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Scrive testo su immagine: " + VariabiliStaticheWallpaper.getInstance().isScriveTestoSuImmagine());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambia Home: " + VariabiliStaticheWallpaper.getInstance().isHome());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambia Lock: " + VariabiliStaticheWallpaper.getInstance().isLock());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Detector: " + VariabiliStaticheStart.getInstance().isDetector());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Immagine Espansa: " + VariabiliStaticheWallpaper.getInstance().isEspansa());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Solo Volti: " + VariabiliStaticheWallpaper.getInstance().isSoloVolti());
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Effetti: " + VariabiliStaticheWallpaper.getInstance().isEffetti());

                    c.close();

                    return 0;
                } else {
                    // Controlla = false;
                    // boolean scritti = ScriveImpostazioni();
                    // Controlla = true;
                    c.close();

                    return -3;
                }
            } catch (Exception e) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                        "ERRORE Nella lettura delle impostazioni: " +
                                UtilityWallpaper.getInstance().PrendeErroreDaException(e));
                // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Pulizia tabelle");
                // PulisceDati();
                // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Creazione tabelle");
                // CreazioneTabelle();

                return -2;
            }
        } else {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"ERRORE DB Non valido");

            return -1;
        }
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
                myDB.execSQL("Drop Table Impostazioni");

                return true;
            } catch (Exception ignored) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void PulisceDatiIL() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table ListaImmaginiLocali");
            } catch (Exception ignored) {

            }
        }
    }

    public void PulisceDatiIW() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            // myDB.execSQL("Delete From Utente");
            // myDB.execSQL("Delete From Ultima");
            try {
                myDB.execSQL("Drop Table ListaImmaginiWeb");
            } catch (Exception ignored) {

            }
        }
    }
}
