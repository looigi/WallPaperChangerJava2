package com.looigi.wallpaperchanger2.gps;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.UtilityWallpaper;

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
                        "altitude VARCHAR, accuracy VARCHAR, distanza VARCHAR);";
                myDB.execSQL(sql);
            }
        } catch (Exception e) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore creazione tabelle: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
        }
    }

    public Boolean AggiungePosizione(StrutturaGps s) {
        if (myDB != null) {
            try {
                String sql = "";
                sql = "INSERT INTO"
                        + " posizioni"
                        + " (data, ora, latitudine, longitudine, speed, altitude, accuracy, distanza)"
                        + " VALUES ("
                        + "'" + s.getData() + "', "
                        + "'" + s.getOra() + "', "
                        + "'" + s.getLat() + "', "
                        + "'" + s.getLon() + "', "
                        + "'" + s.getSpeed() + "', "
                        + "'" + s.getAltitude() + "', "
                        + "'" + s.getAccuracy() + "', "
                        + "'" + s.getDistanza() + "' "
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
}
