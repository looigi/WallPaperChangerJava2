package com.looigi.wallpaperchanger2.classeUtilityImmagini;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.StrutturaImmaginiUguali;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class db_dati_ui {
    private static final String NomeMaschera = "DB_UI";

    // private final String PathDB = VariabiliStatiche.getInstance().getPercorsoDIR()+"/DB/";
    private String PathDB;
    private SQLiteDatabase myDB = null;
    private boolean Controlla = true;
    private Context context;
    private boolean RiscritturaC = false;
    private boolean RiscritturaE = false;
    private boolean RiscritturaI = false;
    private boolean RiscritturaU = false;
    private boolean RiscritturaP = false;
    private boolean RiscritturaG = false;
    private boolean RiscritturaFC = false;

    public boolean DbAperto() {
        if (myDB != null) {
            return true;
        } else {
            return false;
        }
    }

    public db_dati_ui(Context context) {
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
            String nomeDB = "dati_ui.db";
            db = context.openOrCreateDatabase(
                    PathDB + nomeDB, 0, null);
            RiscritturaC = false;
            RiscritturaE = false;
            RiscritturaI = false;
            RiscritturaU = false;
            RiscritturaP = false;
            RiscritturaG = false;
            RiscritturaFC = false;
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
                        + "Controllo"
                        + " (idRiga int, Categoria VARCHAR, idCategoria int, Giuste int, "
                        + "Errate int, Piccole int, Inesistenti int, Grandi int"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Errate"
                        + " (idRiga int, Progressivo int, "
                        + "Errata VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Piccole"
                        + " (idRiga int, Progressivo int, "
                        + "Piccola VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Inesistenti"
                        + " (idRiga int, Progressivo int, "
                        + "Inesistente VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Uguali"
                        + " (idRiga int, Progressivo int, "
                        + "Tipo VARCHAR, Cosa VARCHAR, Filtro VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Grandi"
                        + " (idRiga int, Progressivo int, "
                        + "Grande VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "FuoriCategoria"
                        + " (idRiga int, Progressivo int, "
                        + "idImmagine, idCategoria int, Categoria VARCHAR, Alias VARCHAR, Tag VARCHAR, "
                        + "Cartella VARCHAR, NomeFile VARCHAR, DimensioneFile int, DataCreazione VARCHAR, "
                        + "DataModifica VARCHAR, DimensioniImmagine VARCHAR, UrlImmagine VARCHAR, "
                        + "PathImmagine VARCHAR, EsisteImmagine VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore su creazione tabelle: " + e.getMessage());

            return false;
        }
    }

    public void DropTabelle() {
        if (myDB != null) {
            String sql = "Drop Table "
                    + "Controllo";

            myDB.execSQL(sql);

            sql = "Drop Table "
                    + "Errate";

            myDB.execSQL(sql);

            sql = "Drop Table "
                    + "Piccole";

            myDB.execSQL(sql);

            sql = "Drop Table "
                    + "Inesistenti";

            myDB.execSQL(sql);

            sql = "Drop Table "
                    + "Uguali";

            myDB.execSQL(sql);

            sql = "Drop Table "
                    + "FuoriCategoria";

            myDB.execSQL(sql);

            sql = "Drop Table "
                    + "Grandi";

            myDB.execSQL(sql);
        }
    }

    public void PulisceTutto() {
        if (myDB != null) {
            String sql = "Delete From "
                    + "Controllo";

            myDB.execSQL(sql);

            sql = "Delete From "
                    + "Errate";

            myDB.execSQL(sql);

            sql = "Delete From "
                    + "Piccole";

            myDB.execSQL(sql);

            sql = "Delete From "
                    + "Inesistenti";

            myDB.execSQL(sql);

            sql = "Delete From "
                    + "Uguali";

            myDB.execSQL(sql);

            sql = "Delete From "
                    + "FuoriCategoria";

            myDB.execSQL(sql);

            sql = "Delete From "
                    + "Grandi";

            myDB.execSQL(sql);
        }
    }

    public StrutturaControlloImmagini LeggeDatiCategoria(String idCategoria) {
        StrutturaControlloImmagini s = new StrutturaControlloImmagini();
        if (myDB != null) {
            String sql = "";
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Controllo Where idCategoria=" + idCategoria, null);
                if (c.moveToFirst()) {
                    do {
                        int idRiga = c.getInt(0);
                        s.setCategoria(c.getString(1));
                        s.setIdCategoria(c.getInt(2));
                        s.setGiuste(c.getInt(3));
                        s.setPiccole(c.getInt(4));
                        s.setInesistenti(c.getInt(5));

                        List<String> errate = new ArrayList<>();
                        Cursor cE = myDB.rawQuery("SELECT * FROM Errate Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cE.moveToFirst()) {
                            do {
                                errate.add(cE.getString(2));
                            } while (cE.moveToNext());
                        }
                        cE.close();
                        s.setListaErrate(errate);

                        List<String> piccole = new ArrayList<>();
                        Cursor cP = myDB.rawQuery("SELECT * FROM Piccole Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cP.moveToFirst()) {
                            do {
                                piccole.add(cP.getString(2));
                            } while (cP.moveToNext());
                        }
                        cP.close();
                        s.setListaPiccole(piccole);

                        List<String> inesistenti = new ArrayList<>();
                        Cursor cI = myDB.rawQuery("SELECT * FROM Inesistenti Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cI.moveToFirst()) {
                            do {
                                inesistenti.add(cI.getString(2));
                            } while (cI.moveToNext());
                        }
                        cI.close();
                        s.setListaInesistenti(inesistenti);

                        List<StrutturaImmaginiUguali> uguali = new ArrayList<>();
                        Cursor cU = myDB.rawQuery("SELECT * FROM Uguali Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cU.moveToFirst()) {
                            do {
                                StrutturaImmaginiUguali sU = new StrutturaImmaginiUguali();
                                sU.setFiltro(cU.getString(0));
                                sU.setQuanti(cU.getInt(1));
                                sU.setTipo(cU.getString(2));

                                uguali.add(sU);
                            } while (cU.moveToNext());
                        }

                        List<String> grandi = new ArrayList<>();
                        Cursor cG = myDB.rawQuery("SELECT * FROM Grandi Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cG.moveToFirst()) {
                            do {
                                grandi.add(cG.getString(2));
                            } while (cG.moveToNext());
                        }
                        cG.close();
                        s.setListaGrandi(grandi);

                        cU.close();
                        s.setListaUguali(uguali);

                        List<StrutturaImmagineFuoriCategoria> fc = new ArrayList<>();
                        Cursor cFC = myDB.rawQuery("SELECT * FROM FuoriCategoria Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cFC.moveToFirst()) {
                            do {
                                StrutturaImmagineFuoriCategoria sU = new StrutturaImmagineFuoriCategoria();
                                sU.setIdImmagine(c.getInt(2));
                                sU.setIdCategoria(c.getInt(3));
                                sU.setCategoria(c.getString(4));
                                sU.setAlias(c.getString(5));
                                sU.setTag(c.getString(6));
                                sU.setCartella(c.getString(7));
                                sU.setNomeFile(c.getString(8));
                                sU.setDimensioneFile(c.getInt(9));
                                sU.setDataCreazione(c.getString(10));
                                sU.setDataModifica(c.getString(11));
                                sU.setDimensioniImmagine(c.getString(12));
                                sU.setUrlImmagine(c.getString(13));
                                sU.setPathImmagine(c.getString(14));
                                sU.setEsisteImmagine(c.getString(15).equals("S"));

                                fc.add(sU);
                            } while (cFC.moveToNext());
                        }
                        cFC.close();
                        s.setListaFC(fc);
                    } while (c.moveToNext());
                }
                c.close();
            } catch (Exception e) {
                // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Errore lettura db categorie: " +
                //         UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }

        return s;
    }

    public void LeggeDati() {
        if (myDB != null) {
            List<StrutturaControlloImmagini> lista = new ArrayList<>();
            String sql = "";
            String Cosa = "";
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Controllo", null);
                if (c.moveToFirst()) {
                    do {
                        int idRiga = c.getInt(0);
                        Cosa = "1-" + idRiga;
                        StrutturaControlloImmagini s = new StrutturaControlloImmagini();
                        s.setCategoria(c.getString(1));
                        s.setIdCategoria(c.getInt(2));
                        s.setGiuste(c.getInt(3));
                        s.setPiccole(c.getInt(4));
                        s.setInesistenti(c.getInt(5));
                        s.setGrandi(c.getInt(6));

                        Cosa = "2-" + idRiga;
                        List<String> errate = new ArrayList<>();
                        Cursor cE = myDB.rawQuery("SELECT * FROM Errate Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cE.moveToFirst()) {
                            do {
                                errate.add(cE.getString(2));
                            } while (cE.moveToNext());
                        }
                        cE.close();
                        s.setListaErrate(errate);

                        Cosa = "3-" + idRiga;
                        List<String> piccole = new ArrayList<>();
                        Cursor cP = myDB.rawQuery("SELECT * FROM Piccole Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cP.moveToFirst()) {
                            do {
                                piccole.add(cP.getString(2));
                            } while (cP.moveToNext());
                        }
                        cP.close();
                        s.setListaPiccole(piccole);

                        Cosa = "4-" + idRiga;
                        List<String> inesistenti = new ArrayList<>();
                        Cursor cI = myDB.rawQuery("SELECT * FROM Inesistenti Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cI.moveToFirst()) {
                            do {
                                inesistenti.add(cI.getString(2));
                            } while (cI.moveToNext());
                        }
                        cI.close();
                        s.setListaInesistenti(inesistenti);

                        Cosa = "5-" + idRiga;
                        List<String> grandi = new ArrayList<>();
                        Cursor cG = myDB.rawQuery("SELECT * FROM Grandi Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cG.moveToFirst()) {
                            do {
                                grandi.add(cG.getString(2));
                            } while (cG.moveToNext());
                        }
                        cG.close();
                        s.setListaGrandi(grandi);

                        Cosa = "6-" + idRiga;
                        List<StrutturaImmaginiUguali> uguali = new ArrayList<>();
                        Cursor cU = myDB.rawQuery("SELECT * FROM Uguali Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cU.moveToFirst()) {
                            do {
                                StrutturaImmaginiUguali sU = new StrutturaImmaginiUguali();
                                sU.setFiltro(cU.getString(0));
                                sU.setQuanti(cU.getInt(1));
                                sU.setTipo(cU.getString(2));

                                uguali.add(sU);
                            } while (cU.moveToNext());
                        }
                        cU.close();
                        s.setListaUguali(uguali);

                        Cosa = "7-" + idRiga;
                        List<StrutturaImmagineFuoriCategoria> fc = new ArrayList<>();
                        Cursor cFC = myDB.rawQuery("SELECT * FROM FuoriCategoria Where idRiga=" + idRiga + " Order By Progressivo", null);
                        if (cFC.moveToFirst()) {
                            int idRiga2 = 0;
                            do {
                                try {
                                    idRiga2++;
                                    StrutturaImmagineFuoriCategoria sU = new StrutturaImmagineFuoriCategoria();
                                    sU.setIdImmagine(cFC.getInt(2));
                                    sU.setIdCategoria(cFC.getInt(3));
                                    sU.setCategoria(cFC.getString(4));
                                    Cosa = "7-" + idRiga + "-1-" + idRiga2;
                                    sU.setAlias(cFC.getString(5));
                                    sU.setTag(cFC.getString(6));
                                    sU.setCartella(cFC.getString(7));
                                    sU.setNomeFile(cFC.getString(8));
                                    Cosa = "7-" + idRiga + "-2-" + idRiga2;
                                    sU.setDimensioneFile(cFC.getInt(9));
                                    sU.setDataCreazione(cFC.getString(10));
                                    sU.setDataModifica(cFC.getString(11));
                                    Cosa = "7-" + idRiga + "-3-" + idRiga2;
                                    sU.setDimensioniImmagine(cFC.getString(12));
                                    sU.setUrlImmagine(cFC.getString(13));
                                    sU.setPathImmagine(cFC.getString(14));
                                    Cosa = "7-" + idRiga + "-4-" + idRiga2;
                                    sU.setEsisteImmagine(cFC.getString(15).equals("S"));
                                    Cosa = "7-" + idRiga + "-5-" + idRiga2;

                                    fc.add(sU);
                                } catch (Exception e) {
                                    int a = 0;
                                }
                            } while (cFC.moveToNext());
                        }
                        cFC.close();
                        s.setListaFC(fc);

                        Cosa = "8-" + idRiga;
                        lista.add(s);
                    } while (c.moveToNext());
                }
                c.close();

                VariabiliStaticheUtilityImmagini.getInstance().setControlloImmagini(lista);
            } catch (Exception e) {
                int a = 0;
                // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Errore lettura db categorie: " +
                //         UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public void ScriveDati(StrutturaControlloImmagini s) {
        if (myDB != null) {
            String sql = "";
            boolean esiste = false;
            int idRiga = -1;
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Controllo Where idCategoria=" + s.getIdCategoria(), null);
                if (c.moveToFirst()) {
                    idRiga = c.getInt(0);
                    esiste = true;
                }
                c.close();
            } catch (Exception e) {
                // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db categorie: " +
                //         UtilityDetector.getInstance().PrendeErroreDaException(e));
            }

            if (!esiste) {
                try {
                    Cursor c = myDB.rawQuery("SELECT Coalesce(Max(idRiga),0)+1 FROM Controllo", null);
                    c.moveToFirst();
                    do {
                        idRiga = c.getInt(0);
                    } while (c.moveToNext());
                    c.close();
                } catch (Exception e) {
                    UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Errore lettura db categorie: " +
                            UtilityDetector.getInstance().PrendeErroreDaException(e));
                }

                sql = "Insert into Controllo Values (" +
                        " " + idRiga + ", " +
                        "'" + s.getCategoria().replace("'", "''") + "', " +
                        " " + s.getIdCategoria() + ", " +
                        " " + s.getGiuste() + ", " +
                        " " + s.getErrate() + ", " +
                        " " + s.getPiccole() + ", " +
                        " " + s.getInesistenti() + ", " +
                        " " + s.getGrandi() + " " +
                    ")";
            } else {
                sql = "update Controllo Set " +
                        "Giuste=" + s.getGiuste() + ", " +
                        "Errate=" + s.getErrate() + ", " +
                        "Piccole=" + s.getPiccole() + ", " +
                        "Grandi=" + s.getGrandi() + ", " +
                        "Inesistenti=" + s.getInesistenti() + " " +
                        "Where idRiga=" + idRiga;
            }
            try {
                myDB.execSQL(sql);
            } catch (SQLException e) {
                if (!RiscritturaC) {
                    RiscritturaC = true;
                    DropTabelle();
                    CreazioneTabelle();
                    ScriveDati(s);
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(
                            context,
                            "Problemi nella scrittua della tabella Controllo: " + e.getMessage()
                    );
                }
            }

            sql = "Delete From Errate Where idRiga=" + idRiga;
            try {
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int a = 0;
            }

            sql = "Delete From Piccole Where idRiga=" + idRiga;
            try {
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int a = 0;
            }

            sql = "Delete From Inesistenti Where idRiga=" + idRiga;
            try {
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int a = 0;
            }

            sql = "Delete From Uguali Where idRiga=" + idRiga;
            try {
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int a = 0;
            }

            sql = "Delete From Grandi Where idRiga=" + idRiga;
            try {
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int a = 0;
            }

            sql = "Delete From FuoriCategoria Where idRiga=" + idRiga;
            try {
                myDB.execSQL(sql);
            } catch (SQLException e) {
                int a = 0;
            }

            int Progressivo = 0;
            for (String s1: s.getListaErrate()) {
                sql = "Insert Into Errate Values (" +
                        " " + idRiga + ", " +
                        " " + Progressivo + ", " +
                        "'" + s1.replace("'", "''") + "' " +
                        ")";
                try {
                    myDB.execSQL(sql);
                } catch (SQLException e) {
                    if (!RiscritturaE) {
                        RiscritturaE = true;
                        DropTabelle();
                        CreazioneTabelle();
                        ScriveDati(s);
                    } else {
                        UtilitiesGlobali.getInstance().ApreToast(
                                context,
                                "Problemi nella scrittua della tabella Errate: " + e.getMessage()
                        );
                    }
                }

                Progressivo++;
            }

            Progressivo = 0;
            for (String s2: s.getListaPiccole()) {
                sql = "Insert Into Piccole Values (" +
                        " " + idRiga + ", " +
                        " " + Progressivo + ", " +
                        "'" + s2.replace("'", "''") + "' " +
                        ")";
                try {
                    myDB.execSQL(sql);
                } catch (SQLException e) {
                    if (!RiscritturaP) {
                        RiscritturaP = true;
                        DropTabelle();
                        CreazioneTabelle();
                        ScriveDati(s);
                    } else {
                        UtilitiesGlobali.getInstance().ApreToast(
                                context,
                                "Problemi nella scrittua della tabella Piccole: " + e.getMessage()
                        );
                    }
                }

                Progressivo++;
            }

            Progressivo = 0;
            for (String s3: s.getListaInesistenti()) {
                sql = "Insert Into Inesistenti Values (" +
                        " " + idRiga + ", " +
                        " " + Progressivo + ", " +
                        "'" + s3.replace("'", "''") + "' " +
                        ")";
                try {
                    myDB.execSQL(sql);
                } catch (SQLException e) {
                    if (!RiscritturaI) {
                        RiscritturaI = true;
                        DropTabelle();
                        CreazioneTabelle();
                        ScriveDati(s);
                    } else {
                        UtilitiesGlobali.getInstance().ApreToast(
                                context,
                                "Problemi nella scrittua della tabella Inesistenti: " + e.getMessage()
                        );
                    }
                }

                Progressivo++;
            }

            Progressivo = 0;
            for (String s5: s.getListaGrandi()) {
                sql = "Insert Into Grandi Values (" +
                        " " + idRiga + ", " +
                        " " + Progressivo + ", " +
                        "'" + s5.replace("'", "''") + "' " +
                        ")";
                try {
                    myDB.execSQL(sql);
                } catch (SQLException e) {
                    if (!RiscritturaG) {
                        RiscritturaG = true;
                        DropTabelle();
                        CreazioneTabelle();
                        ScriveDati(s);
                    } else {
                        UtilitiesGlobali.getInstance().ApreToast(
                                context,
                                "Problemi nella scrittua della tabella Grandi: " + e.getMessage()
                        );
                    }
                }

                Progressivo++;
            }

            Progressivo = 0;
            for (StrutturaImmaginiUguali s4: s.getListaUguali()) {
                sql = "Insert Into Uguali Values (" +
                        " " + idRiga + ", " +
                        " " + Progressivo + ", " +
                        "'" + s4.getTipo().replace("'", "''") + "', " +
                        " " + s4.getQuanti() + ", " +
                        "'" + s4.getFiltro().replace("'", "''") + "' " +
                        ")";
                try {
                    myDB.execSQL(sql);
                } catch (SQLException e) {
                    if (!RiscritturaU) {
                        RiscritturaU = true;
                        DropTabelle();
                        CreazioneTabelle();
                        ScriveDati(s);
                    } else {
                        UtilitiesGlobali.getInstance().ApreToast(
                                context,
                                "Problemi nella scrittua della tabella Uguali: " + e.getMessage()
                        );
                    }
                }

                Progressivo++;
            }

            Progressivo = 0;
            for (StrutturaImmagineFuoriCategoria s5: s.getListaFC()) {
                sql = "Insert Into FuoriCategoria Values (" +
                        " " + idRiga + ", " +
                        " " + Progressivo + ", " +
                        " " + s5.getIdImmagine() + ", " +
                        " " + s5.getIdCategoria() + ", " +
                        "'" + s5.getCategoria().replace("'", "''") + "', " +
                        "'" + s5.getAlias().replace("'", "''") + "', " +
                        "'" + s5.getTag().replace("'", "''") + "', " +
                        "'" + s5.getCartella().replace("'", "''") + "', " +
                        "'" + s5.getNomeFile().replace("'", "''") + "', " +
                        " " + s5.getDimensioneFile() + ", " +
                        "'" + s5.getDataCreazione().replace("'", "''") + "', " +
                        "'" + s5.getDataModifica().replace("'", "''") + "', " +
                        "'" + s5.getDimensioniImmagine().replace("'", "''") + "', " +
                        "'" + s5.getUrlImmagine().replace("'", "''") + "', " +
                        "'" + s5.getPathImmagine().replace("'", "''") + "', " +
                        "'" + (s5.isEsisteImmagine() ? "S" : "N") + "' " +
                        ")";
                try {
                    myDB.execSQL(sql);
                } catch (SQLException e) {
                    if (!RiscritturaFC) {
                        RiscritturaFC = true;
                        DropTabelle();
                        CreazioneTabelle();
                        ScriveDati(s);
                    } else {
                        UtilitiesGlobali.getInstance().ApreToast(
                                context,
                                "Problemi nella scrittua della tabella FC: " + e.getMessage()
                        );
                    }
                }

                Progressivo++;
            }
        } else {
            // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }
}
