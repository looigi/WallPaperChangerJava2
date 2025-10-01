package com.looigi.wallpaperchanger2.Pazzia;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.Detector.UtilityDetector;

import java.io.File;

public class db_dati_pazzia {
    private static final String NomeMaschera = "DB_Pazzia";

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

    public db_dati_pazzia(Context context) {
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
            String nomeDB = "dati_pazzia.db";
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
                        + "Impostazioni "
                        + "(CategoriaPEN VARCHAR, CategoriaIMM VARCHAR, CategoriaVID VARCHAR," +
                        "FiltroPEN VARCHAR, FiltroIMM VARCHAR, FiltroVID VARCHAR);";
                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void SalvaImpostazioni() {
        if (myDB != null) {
            try {
                String sql = "Delete From Impostazioni";
                myDB.execSQL(sql);

                sql = "Insert Into Impostazioni Values(" +
                        "'" + VariabiliStatichePazzia.getInstance().getCategoriaPennetta() + "'," +
                        "'" + VariabiliStatichePazzia.getInstance().getCategoriaImmagini() + "'," +
                        "'" + VariabiliStatichePazzia.getInstance().getCategoriaVideo() + "'," +
                        "'" + VariabiliStatichePazzia.getInstance().getFiltroPEN() + "'," +
                        "'" + VariabiliStatichePazzia.getInstance().getFiltroIMM() + "'," +
                        "'" + VariabiliStatichePazzia.getInstance().getFiltroVID() + "' " +
                        ")";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int i = 0;
            }
        }
    }

    public void LeggeImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                boolean ok = false;
                if (c.moveToFirst()) {
                    do {
                        ok = true;

                        VariabiliStatichePazzia.getInstance().setCategoriaPennetta(c.getString(0));
                        VariabiliStatichePazzia.getInstance().setCategoriaImmagini(c.getString(1));
                        VariabiliStatichePazzia.getInstance().setCategoriaVideo(c.getString(2));
                        VariabiliStatichePazzia.getInstance().setFiltroPEN(c.getString(3));
                        VariabiliStatichePazzia.getInstance().setFiltroIMM(c.getString(4));
                        VariabiliStatichePazzia.getInstance().setFiltroVID(c.getString(5));
                    } while (c.moveToNext());
                }
                c.close();

                if (!ok) {
                    VariabiliStatichePazzia.getInstance().setCategoriaImmagini("");
                    VariabiliStatichePazzia.getInstance().setCategoriaPennetta("");
                    VariabiliStatichePazzia.getInstance().setCategoriaVideo("");
                }
            } catch (SQLException e) {
                VariabiliStatichePazzia.getInstance().setCategoriaImmagini("");
                VariabiliStatichePazzia.getInstance().setCategoriaPennetta("");
                VariabiliStatichePazzia.getInstance().setCategoriaVideo("");
            }
        }
    }
}
