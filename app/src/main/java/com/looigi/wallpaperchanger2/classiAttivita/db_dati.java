package com.looigi.wallpaperchanger2.classiAttivita;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class db_dati {
    private static final String NomeMaschera = "DBDATI";
    private String PathDB = "";
    private final SQLiteDatabase myDB;
    private Context context;

    public db_dati(Context context) {
        this.context = context;
        PathDB = Environment.getExternalStorageDirectory() + "/" +
                Environment.DIRECTORY_DOWNLOADS + "/LooigiSoft/" + VariabiliStaticheServizio.channelName + "/DB/";

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception ignored) {

        }
        myDB = ApreDB();
    }

    private SQLiteDatabase ApreDB() {
        SQLiteDatabase db = null;
        try {
            String nomeDB = "dati.db";
            db = context.openOrCreateDatabase(
                    PathDB + nomeDB, MODE_PRIVATE, null);
        } catch (Exception e) {
            Utility.getInstance().ScriveLog(context, NomeMaschera, "ERRORE Nell'apertura del db: " +
                    Utility.getInstance().PrendeErroreDaException(e));
        }
        return  db;
    }

    public void CreazioneTabelle() {
        try {
            // SQLiteDatabase myDB = ApreDB();
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni "
                        + " (UltimaImmagineNome VARCHAR, UltimaImmaginePath VARCHAR, SecondiAlcambio VARCHAR, PathImmagini VARCHAR, Offline VARCHAR, " +
                        "Blur VARCHAR, Resize VARCHAR, ScriveTesto VARCHAR, OnOff VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "ListaImmaginiLocali "
                        + " (ImmagineNome VARCHAR, ImmaginePath VARCHAR, Data VARCHAR, Dimensione VARCHAR);";
                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE Nella creazione delle tabelle: " +
                    Utility.getInstance().PrendeErroreDaException(ignored));
        }
    }

    public boolean EliminaImmaginiInLocale() {
        if (myDB != null) {
            myDB.execSQL("Delete From Impostazioni");
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
                Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE Su scrittura immagini locali: " +
                        Utility.getInstance().PrendeErroreDaException(e));
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Pulizia tabelle");
                PulisceDatiIL();
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Creazione tabelle");
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

                    VariabiliStaticheServizio.getInstance().setListaImmagini(listaImmagini);
                } else {
                    return false;
                }
            } catch (Exception e) {
                Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE Su scrittura immagini locali: " +
                        Utility.getInstance().PrendeErroreDaException(e));
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Pulizia tabelle");
                PulisceDatiIL();
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Creazione tabelle");
                CreazioneTabelle();
                CaricaImmaginiInLocale();

                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public Boolean ScriveImpostazioni() {
        if (myDB != null) {
            try {
                String Imm = "";
                if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
                    Imm = VariabiliStaticheServizio.getInstance().getUltimaImmagine().getImmagine();
                }
                String PathImm = "";
                if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
                    PathImm = VariabiliStaticheServizio.getInstance().getUltimaImmagine().getPathImmagine();
                } else {
                    PathImm = "";
                }
                myDB.execSQL("Delete From Impostazioni");
                String sql = "INSERT INTO"
                        + " Impostazioni"
                        + " (UltimaImmagineNome, UltimaImmaginePath, SecondiAlCambio, PathImmagini, Offline, Blur, Resize, ScriveTesto, OnOff)"
                        + " VALUES ("
                        + "'" + (Imm) + "', "
                        + "'" + (PathImm) + "', "
                        + "'" + (VariabiliStaticheServizio.getInstance().getMinutiAttesa()) + "', "
                        + "'" + (VariabiliStaticheServizio.getInstance().getPercorsoIMMAGINI()) + "', "
                        + "'" + (VariabiliStaticheServizio.getInstance().isOffline() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheServizio.getInstance().isBlur() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheServizio.getInstance().isResize() ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheServizio.getInstance().isScriveTestoSuImmagine()  ? "S" : "N") + "', "
                        + "'" + (VariabiliStaticheServizio.getInstance().isOnOff() ? "S" : "N") + "' "
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                Utility.getInstance().ScriveLog(context, NomeMaschera, "ERRORE Su scrittura impostazioni: " +
                        Utility.getInstance().PrendeErroreDaException(e));
                Utility.getInstance().ScriveLog(context, NomeMaschera, "Pulizia tabelle");
                PulisceDati();
                Utility.getInstance().ScriveLog(context, NomeMaschera, "Creazione tabelle");
                CreazioneTabelle();

                return false;
            }
        }

        return true;
    }

    public boolean LeggeImpostazioni() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    StrutturaImmagine si = new StrutturaImmagine();
                    si.setImmagine(c.getString(0));
                    si.setPathImmagine(c.getString(1));
                    VariabiliStaticheServizio.getInstance().setUltimaImmagine(si);

                    VariabiliStaticheServizio.getInstance().setMinutiAttesa(Integer.parseInt(c.getString(2)));
                    VariabiliStaticheServizio.getInstance().setPercorsoIMMAGINI(c.getString(3));
                    VariabiliStaticheServizio.getInstance().setOffline(c.getString(4).equals("S"));
                    VariabiliStaticheServizio.getInstance().setBlur(c.getString(5).equals("S"));
                    VariabiliStaticheServizio.getInstance().setResize(c.getString(6).equals("S"));
                    VariabiliStaticheServizio.getInstance().setScriveTestoSuImmagine(c.getString(7).equals("S"));
                    VariabiliStaticheServizio.getInstance().setOnOff(c.getString(8).equals("S"));

                    Utility.getInstance().ScriveLog(context, NomeMaschera,"ON/OFF: " + VariabiliStaticheServizio.getInstance().isOnOff());
                    Utility.getInstance().ScriveLog(context, NomeMaschera,"Secondi al cambio: " + VariabiliStaticheServizio.getInstance().getMinutiAttesa());
                    Utility.getInstance().ScriveLog(context, NomeMaschera,"Percorso immagini: " + VariabiliStaticheServizio.getInstance().getPercorsoIMMAGINI());
                    Utility.getInstance().ScriveLog(context, NomeMaschera,"Offline: " + VariabiliStaticheServizio.getInstance().isOffline());
                    Utility.getInstance().ScriveLog(context, NomeMaschera,"Blur: " + VariabiliStaticheServizio.getInstance().isBlur());
                    Utility.getInstance().ScriveLog(context, NomeMaschera,"Resize: " + VariabiliStaticheServizio.getInstance().isResize());
                    Utility.getInstance().ScriveLog(context, NomeMaschera,"Scrive testo su immagine: " + VariabiliStaticheServizio.getInstance().isScriveTestoSuImmagine());
                } else {
                    return false;
                }
                c.close();
            } catch (Exception e) {
                Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE Nella lettura delle impostazioni: " + Utility.getInstance().PrendeErroreDaException(e));
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Pulizia tabelle");
                PulisceDati();
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Creazione tabelle");
                CreazioneTabelle();

                return false;
            }
        }

        return true;
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
                myDB.execSQL("Drop Table Impostazioni");
            } catch (Exception ignored) {

            }
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
}
