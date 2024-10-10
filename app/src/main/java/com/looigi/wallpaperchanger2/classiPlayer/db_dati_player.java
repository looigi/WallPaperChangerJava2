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
                        + " (idBrano VARCHAR);";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "listaBrani"
                        + " (idBrano VARCHAR, quantiBrani VARCHAR, Artista VARCHAR, Album VARCHAR,"
                        + " Brano VARCHAR, Anno VARCHAR, Traccia VARCHAR, Estensione VARCHAR,"
                        + " Data VARCHAR, Dimensione VARCHAR, Ascoltata VARCHAR, Bellezza VARCHAR,"
                        + "Testo VARCHAR, TestoTradotto VARCHAR, UrlBrano VARCHAR, PathBrano VARCHAR,"
                        + "CartellaBrano VARCHAR, Tags VARCHAR, TipoBrano VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "ImmaginiBrano"
                        + " (idBrano VARCHAR, Album VARCHAR,"
                        + " NomeImmagine VARCHAR,"
                        + " UrlImmagine VARCHAR,"
                        + " PathImmagine VARCHAR,"
                        + " CartellaImmagine VARCHAR"
                        + ");";

                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Impostazioni"
                        + " (LimiteInBytes VARCHAR"
                        + ");";

                myDB.execSQL(sql);
            }
        } catch (Exception ignored) {
            // Log.getInstance().ScriveLog("ERRORE Nella creazione delle tabelle: " + UtilityDetector.getInstance().PrendeErroreDaException(ignored));
            int a = 0;
        }
    }

    public StrutturaBrano CaricaBrano(String idBrano) {
        // UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Controllo apertura db");
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM listaBrani Where idBrano=" + idBrano, null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per ultimo brano");
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

                        sb.setImmagini(CaricaImmaginiBrano(sb.getIdBrano().toString()));

                        return sb;
                    } catch (Exception e) {
                        PulisceDatiSB();
                        CreazioneTabelle();
                        return CaricaBrano(idBrano);
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per ultimo brano. Imposto default");

                    return null;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db ultimo brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiSB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return CaricaBrano(idBrano);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return null;
        }
    }

    private List<StrutturaImmagini> CaricaImmaginiBrano(String idBrano) {
        List<StrutturaImmagini> lista = new ArrayList<>();

        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM ImmaginiBrano Where idBrano=" + idBrano, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();

                    try {
                        do {
                            StrutturaImmagini i = new StrutturaImmagini();
                            i.setAlbum(c.getString(1));
                            i.setNomeImmagine(c.getString(2));
                            i.setUrlImmagine(c.getString(3));
                            i.setPathImmagine(c.getString(4));
                            i.setCartellaImmagine(c.getString(5));

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

    public String CaricaUltimoBranoAscoltato() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Ultimobrano", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per ultimo brano");
                    c.moveToFirst();

                    try {
                        return c.getString(0);
                    } catch (Exception e) {
                        PulisceDatiUB();
                        CreazioneTabelle();
                        return CaricaUltimoBranoAscoltato();
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per ultimo brano. Imposto default");

                    return "";
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db ultimo brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
                PulisceDatiUB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return CaricaUltimoBranoAscoltato();
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return "";
        }
    }

    public int QuantiBraniInArchivio() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT Count(*) FROM listaBrani", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per conteggio brani");
                    c.moveToFirst();

                    try {
                        return c.getInt(0);
                    } catch (Exception e) {
                        return -1;
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per conteggio brani");

                    return -1;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db conteggio brani: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -1;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return -1;
        }
    }

    public int RitornaMaxIdBrano() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT coalesce(max(idBrano),0) + 1 FROM listaBrani", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per max id brano");
                    c.moveToFirst();

                    try {
                        return c.getInt(0);
                    } catch (Exception e) {
                        return -1;
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per max id brano");

                    return -1;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db max id brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return -1;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return -1;
        }
    }

    public boolean EsisteBrano(String Artista, String Album, String Brano) {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM listaBrani Where Artista='" + Artista + "' And Album='" + Album + "' And Brano='" + Brano + "'", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per esistenza brano");
                    c.moveToFirst();

                    try {
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per esistenza brano");

                    return false;
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db esistenza brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                return false;
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");

            return false;
        }
    }

    public Boolean ScriveUltimoBranoAscoltato(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                myDB.execSQL("Delete From UltimoBrano");

                String sql = "INSERT INTO"
                        + " UltimoBrano"
                        + " VALUES ("
                        + "'" + sb.getIdBrano() + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo brano: " + e.getMessage());
                PulisceDatiUB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveBrano(sb);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
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
                        + "'" + VariabiliStatichePlayer.getInstance().getLimiteInGb() + "'"
                        + ") ";
                myDB.execSQL(sql);
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per impostazioni: " + e.getMessage());
                PulisceDatiIMP();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveImpostazioni();
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    public void CaricaImpostazioni() {
        if (myDB != null) {
            try {
                Cursor c = myDB.rawQuery("SELECT * FROM Impostazioni", null);
                if (c.getCount() > 0) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga rilevata su db per carica impostazioni");
                    c.moveToFirst();

                    try {
                        VariabiliStatichePlayer.getInstance().setLimiteInGb(Float.parseFloat(c.getString(0)));
                    } catch (Exception e) {
                        VariabiliStatichePlayer.getInstance().setLimiteInGb(1.5F);
                    }
                } else {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Riga non rilevata su db per carica impostazioni");
                }
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore lettura db carica impostazioni: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public Boolean ScriveBrano(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                String sql = "INSERT INTO"
                        + " listaBrani"
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

                ScriveImmaginiBrano(sb);
            } catch (SQLException e) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo brano: " + e.getMessage());
                PulisceDatiSB();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                return ScriveBrano(sb);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }

        return true;
    }

    private void ScriveImmaginiBrano(StrutturaBrano sb) {
        if (myDB != null) {
            try {
                // myDB.execSQL("Delete From ImmaginiUltimoBrano");

                String idBrano = sb.getIdBrano().toString();

                List<StrutturaImmagini> lista = sb.getImmagini();
                List<StrutturaImmagini> nuovaLista = new ArrayList<>();
                for (StrutturaImmagini i : lista) {
                    String imm = i.getUrlImmagine().toUpperCase();
                    if (imm.contains(".JPG") || imm.contains(".JPEG") || imm.contains(".PNG")) {
                        nuovaLista.add(i);

                        String sql = "INSERT INTO"
                                + " ImmaginiBrano"
                                + " VALUES ("
                                + "'" + idBrano + "', "
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
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore su scrittura db per ultimo brano: " + e.getMessage());
                PulisceDatiSBI();
                // Log.getInstance().ScriveLog("Creazione tabelle");
                CreazioneTabelle();
                ScriveImmaginiBrano(sb);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Db non valido");
        }
    }

    public void CompattaDB() {
        // SQLiteDatabase myDB = ApreDB();
        if (myDB != null) {
            myDB.execSQL("VACUUM");
        }
    }

    public void PulisceDatiSB() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db brani");
            try {
                myDB.execSQL("Drop Table listaBrani");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db brani: " + ignored.getMessage());
            }
        }
    }

    public void PulisceDatiIMP() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db impostazioni");
            try {
                myDB.execSQL("Drop Table Impostazioni");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db impostazioni: " + ignored.getMessage());
            }
        }
    }

    public void PulisceDatiSBI() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db immagini brano");
            try {
                myDB.execSQL("Drop Table ImmaginiBrano");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db immagini brano: " + ignored.getMessage());
            }
        }
    }

    public void PulisceDatiUB() {
        if (myDB != null) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Pulizia dati db Ultimo brano");
            try {
                myDB.execSQL("Drop Table UltimoBrano");
            } catch (Exception ignored) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore pulizia dati db ultimo brano: " + ignored.getMessage());
            }
        }
    }
}
