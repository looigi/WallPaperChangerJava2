package com.looigi.wallpaperchanger2.Player.scan;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.Player.UtilityPlayer;
import com.looigi.wallpaperchanger2.Player.db_dati_player;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanBraniNonPresentiSuDB {
    private int maxId  = 0;
    private String PathImmagini;
    private int AggiuntiBrani = 0;
    private int AggiunteImmagini = 0;

    public void controllaCanzoniNonSalvateSuDB(Context context, boolean MostraPopup) {
        if (MostraPopup) {
            UtilityPlayer.getInstance().Attesa(true);
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                PathImmagini = context.getFilesDir() + "/Player/ImmaginiMusica/";
                String path = context.getFilesDir() + "/Player/Brani";

                db_dati_player db = new db_dati_player(context);
                maxId = db.RitornaMaxIdBrano();

                maxId += 65000;
                AggiuntiBrani = 0;
                AggiunteImmagini = 0;

                File rootPrincipale = new File(path);
                if (!rootPrincipale.exists()) {
                    rootPrincipale.mkdir();
                }
                walk(db, rootPrincipale, "BRANI");

                rootPrincipale = new File(PathImmagini);
                if (!rootPrincipale.exists()) {
                    rootPrincipale.mkdir();
                }
                walk(db, rootPrincipale, "IMMAGINI");

                if (MostraPopup) {
                    UtilityPlayer.getInstance().Attesa(false);
                    UtilitiesGlobali.getInstance().ApreToast(context,
                            "Brani aggiunti a DB: " + AggiuntiBrani + "\n" +
                            "\nImmagini aggiunte a DB: " + AggiunteImmagini);
                }

                ScanBraniNonPresentiSuSD s = new ScanBraniNonPresentiSuSD();
                s.EsegueOperazione(context, MostraPopup);

                db.ChiudeDB();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }

    private void walk(db_dati_player db, File root, String Cosa) {
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                walk(db, f, Cosa);
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
                if (Cosa.equals("BRANI")) {
                    if (Nome.toUpperCase().contains(".MP3") || Nome.toUpperCase().contains(".WMA")) {
                        String[] Parti = Filetto.split("/");

                        String Artista = Parti[8];
                        String Album = Parti[9];
                        String Brano = Parti[10];
                        String Anno = "0000";
                        String Traccia = "00";
                        String Estensione = "";

                        if (Album.contains("-")) {
                            String[] a = Album.split("-");
                            Anno = a[0];
                            Album = a[1];
                        }
                        if (Brano.contains("-")) {
                            String[] a = Brano.split("-");
                            Traccia = a[0];
                            Brano = a[1];
                        }
                        if (Nome.contains(".")) {
                            String[] n = Nome.split("\\.");
                            Estensione = "." + n[n.length - 1];
                            Brano = Brano.replace(Estensione, "");
                        }

                        boolean esiste = db.EsisteBrano(Artista, Album, Brano);
                        if (!esiste) {
                            String Data = String.valueOf(Files.getInstance().DataFile(Filetto));
                            long Dimensione = Files.getInstance().DimensioniFile(Filetto);

                            StrutturaBrano sb = new StrutturaBrano();
                            sb.setIdBrano(maxId);
                            sb.setQuantiBrani(-1);
                            sb.setArtista(Artista);
                            sb.setAlbum(Album);
                            sb.setBrano(Brano);
                            sb.setAnno(Anno);
                            sb.setTraccia(Traccia);
                            sb.setEstensione(Estensione);
                            sb.setAscoltata(0);
                            sb.setBellezza(-2);
                            sb.setData(Data);
                            sb.setDimensione(Dimensione);
                            sb.setTesto("");
                            sb.setTestoTradotto("");
                            sb.setUrlBrano("");
                            sb.setPathBrano(Filetto);
                            sb.setCartellaBrano("");
                            sb.setTags("");
                            sb.setTipoBrano(-1);

                            String sPathImmagini1 = PathImmagini +
                                    Artista + "/" + Anno + "-" + Album;
                            List<StrutturaImmagini> lista1 = RitornaImmaginiBrano(Anno + "-" + Album, sPathImmagini1);

                            String sPathImmagini2 = PathImmagini +
                                    Artista + "/ZZZ-ImmaginiArtista";
                            List<StrutturaImmagini> lista2 = RitornaImmaginiBrano(Anno + "-" + Album, sPathImmagini2);

                            List<StrutturaImmagini> lista = new ArrayList<>();
                            lista.addAll(lista1);
                            lista.addAll(lista2);

                            sb.setImmagini(lista);

                            db.ScriveBrano(sb);
                            db.ScriveImmaginiBrano(sb);

                            maxId++;

                            AggiuntiBrani++;
                        }
                    }
                } else {
                    if (Nome.toUpperCase().contains(".JPG") || Nome.toUpperCase().contains(".JPEG") ||
                            Nome.toUpperCase().contains(".BMP") || Nome.toUpperCase().contains(".PNG") ||
                            Nome.toUpperCase().contains(".GIF")) {

                        String NomeImmagine = Filetto.replace(PathImmagini, "");
                        String[] n = NomeImmagine.split("/");

                        StrutturaImmagini s = new StrutturaImmagini();
                        s.setArtista(n[0]);
                        s.setCartellaImmagine(n[1]);
                        s.setNomeImmagine(n[2]);
                        s.setAlbum(n[1]);
                        s.setPathImmagine(Filetto);
                        s.setUrlImmagine(Filetto);

                        if (!db.EsisteImmagineBrano(s)) {
                            db.ScriveImmagineBrano(n[0], s);

                            AggiunteImmagini++;
                        }
                    }
                }
            }
        }
    }

    private List<StrutturaImmagini> RitornaImmaginiBrano(String Album, String Path) {
        File root = new File(Path);
        File[] list = root.listFiles();

        if (list == null) {
            return new ArrayList<>();
        }

        List<StrutturaImmagini> lista = new ArrayList<>();

        for (File f : list) {
            String pathImm = f.getAbsoluteFile().getPath();
            String nomeFile = f.getAbsoluteFile().getName();

            StrutturaImmagini si = new StrutturaImmagini();
            si.setPathImmagine(pathImm);
            si.setAlbum(Album);
            si.setNomeImmagine(nomeFile);
            si.setUrlImmagine(pathImm);
            si.setCartellaImmagine("");

            lista.add(si);
        }

        return lista;
    }
}
