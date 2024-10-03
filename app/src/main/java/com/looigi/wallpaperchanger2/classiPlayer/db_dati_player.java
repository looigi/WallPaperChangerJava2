package com.looigi.wallpaperchanger2.classiPlayer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaImmagini;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class db_dati_player {
    private static final String NomeMaschera = "DBPLAYER";

    // private final String PathDB = VariabiliStatiche.getInstance().getPercorsoDIR()+"/DB/";
    private String PathDB;
    private SQLiteDatabase myDB = null;
    private boolean Controlla = true;
    private Context context;

    public db_dati_player(Context context) {
        this.context = context;
        PathDB = UtilityDetector.getInstance().PrendePathDB(context);

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception ignored) {

        }
        myDB = ApreDB();
    }

    private SQLiteDatabase ApreDB() {
        SQLiteDatabase db = null;

        UtilityDetector.getInstance().CreaCartelle(PathDB, "DB");

        try {
            String nomeDB = "dati_player.db";
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
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "UltimoBrano"
                        + " (idBrano VARCHAR, quantiBrani VARCHAR, Artista VARCHAR, Album VARCHAR,"
                        + " Brano VARCHAR, Anno VARCHAR, Traccia VARCHAR, Estensione VARCHAR,"
                        + " Data VARCHAR, Dimensione VARCHAR, Ascoltata VARCHAR, Bellezza VARCHAR,"
                        + "Testo VARCHAR, TestoTradotto VARCHAR, UrlBrano VARCHAR, PathBrano VARCHAR,"
                        + "CartellaBrano VARCHAR, Tags VARCHAR, TipoBrano VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "ImmaginiUltimoBrano"
                        + " (Album VARCHAR,"
                        + " NomeImmagine VARCHAR,"
                        + " UrlImmagine VARCHAR,"
                        + " PathImmagine VARCHAR,"
                        + " CartellaImmagine VARCHAR"
                        + ");";

                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            // Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + UtilityDetector.getInstance().PrendeErroreDaException(ignored));
            int a = 0;
        }
    }

    public StrutturaBrano CaricaUltimoBrano() {
        // UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Controllo apertura db");
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM UltimoBrano", null);
                if (c.getCount() > 0) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per ultimo brano");
                    c.moveToFirst();

                    try {
                        StrutturaBrano sb = new StrutturaBrano();
                        sb.setIdBrano(Integer.valueOf(c.getString(0)));
                        sb.setQuantiBrani(Integer.valueOf(c.getString(1)));
                        sb.setArtista(c.getString(2));
                        sb.setAlbum(c.getString(3));
                        sb.setBrano(c.getString(4));
                        sb.setAnno(c.getString(5));
                        sb.setTraccia(c.getString(6));
                        sb.setEstensione(c.getString(7));
                        sb.setData(c.getString(8));
                        sb.setDimensione(Long.valueOf(c.getString(9)));
                        sb.setAscoltata(Integer.valueOf(c.getString(10)));
                        sb.setBellezza(Integer.valueOf(c.getString(11)));
                        sb.setTesto(c.getString(12));
                        sb.setTestoTradotto(c.getString(13));
                        sb.setUrlBrano(c.getString(14));
                        sb.setPathBrano(c.getString(15));
                        sb.setCartellaBrano(c.getString(16));
                        sb.setTags(c.getString(17));
                        sb.setTipoBrano(Integer.parseInt(c.getString(18)));

                        sb.setImmagini(caricaImmaginiUltimoBrano());

                        return sb;
                    } catch (Exception e) {
                        PulisceDatiUB();
                        CreazioneTabelle();
                        CaricaUltimoBrano();

                        return null; //  "ERROR: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
                    }
                } else {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per ultimo brano. Imposto default");

                    return null;
                }
            } catch (Exception e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db ultimo brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiUB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                CaricaUltimoBrano();

                return null;
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return null;
        }
    }

    private List<StrutturaImmagini> caricaImmaginiUltimoBrano() {
        List<StrutturaImmagini> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM ImmaginiUltimoBrano", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    try {
                        do {
                            StrutturaImmagini i = new StrutturaImmagini();
                            i.setAlbum(c.getString(0));
                            i.setNomeImmagine(c.getString(1));
                            i.setUrlImmagine(c.getString(2));
                            i.setPathImmagine(c.getString(3));
                            i.setCartellaImmagine(c.getString(4));

                            lista.add(i);
                        } while (c.moveToNext());
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {

            }
        }

        return lista;
    }

    public Boolean ScriveUltimoBrano(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From UltimoBrano");

                String sql = "INSERT INTO"
                        + " UltimoBrano"
                        + " VALUES ("
                        + "'" + sb.getIdBrano() + "', "
                        + "'" + sb.getQuantiBrani() + "', "
                        + "'" + sb.getArtista().replace("'", "''") + "', "
                        + "'" + sb.getAlbum().replace("'", "''") + "', "
                        + "'" + sb.getBrano().replace("'", "''") + "', "
                        + "'" + sb.getAnno() + "', "
                        + "'" + sb.getTraccia() + "', "
                        + "'" + sb.getEstensione() + "', "
                        + "'" + sb.getData() + "', "
                        + "'" + sb.getDimensione() + "', "
                        + "'" + sb.getAscoltata() + "', "
                        + "'" + sb.getBellezza() + "', "
                        + "'" + sb.getTesto().replace("'", "''") + "', "
                        + "'" + sb.getTestoTradotto().replace("'", "''") + "', "
                        + "'" + sb.getUrlBrano().replace("'", "''") + "', "
                        + "'" + sb.getPathBrano().replace("'", "''") + "', "
                        + "'" + sb.getCartellaBrano().replace("'", "''") + "', "
                        + "'" + sb.getTags().replace("'", "''") + "', "
                        + "'" + sb.getTipoBrano() + "' "
                        + ") ";
                myDB.execSQL(sql);

                ScriveImmaginiUltimoBrano(sb);
            } catch (SQLException e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo brano: " + e.getMessage());
                PulisceDatiUB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                ScriveUltimoBrano(sb);

                return false;
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    private void ScriveImmaginiUltimoBrano(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From ImmaginiUltimoBrano");

                List<StrutturaImmagini> lista = sb.getImmagini();
                List<StrutturaImmagini> nuovaLista = new ArrayList<>();
                for (StrutturaImmagini i : lista) {
                    String imm = i.getUrlImmagine().toUpperCase();
                    if (imm.contains(".JPG") || imm.contains(".JPEG") || imm.contains(".PNG")) {
                        nuovaLista.add(i);

                        String sql = "INSERT INTO"
                                + " ImmaginiUltimoBrano"
                                + " VALUES ("
                                + "'" + i.getAlbum().replace("'", "''") + "', "
                                + "'" + i.getNomeImmagine().replace("'", "''") + "', "
                                + "'" + i.getUrlImmagine().replace("'", "''") + "', "
                                + "'" + i.getPathImmagine().replace("'", "''") + "', "
                                + "'" + i.getCartellaImmagine().replace("'", "''") + "' "
                                + ") ";
                        myDB.execSQL(sql);
                    }
                }

                sb.setImmagini(nuovaLista);
            } catch (SQLException e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo brano: " + e.getMessage());
                PulisceDatiUBI();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                ScriveImmaginiUltimoBrano(sb);
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public void PulisceDatiUB() {
        if (myDB != null) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db Ultimo brano");
            try {
                myDB.execSQL("Drop Table UltimoBrano");
            } catch (Exception ignored) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db ultimo brano: " + ignored.getMessage());
            }
        }
    }

    public void PulisceDatiUBI() {
        if (myDB != null) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db Immagini Ultimo brano");
            try {
                myDB.execSQL("Drop Table ImmaginiUltimoBrano");
            } catch (Exception ignored) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db Immagini ultimo brano: " + ignored.getMessage());
            }
        }
    }
}
