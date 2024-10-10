package com.looigi.wallpaperchanger2.classiPlayer.scan;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classiPlayer.Files;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classiPlayer.db_dati_player;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanBraniNonPresentiSuDB {
    private int maxId  = -1;

    public void controllaCanzoniNonSalvateSuDB(Context context) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                String path = context.getFilesDir() + "/Player/Brani";
                db_dati_player db = new db_dati_player(context);
                maxId = db.RitornaMaxIdBrano();

                File rootPrincipale = new File(path);
                if (!rootPrincipale.exists()) {
                    rootPrincipale.mkdir();
                }
                walk(db, rootPrincipale);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }

    private void walk(db_dati_player db, File root) {
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                walk(db, f);
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
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
                        Estensione = n[n.length - 1];
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
                        sb.setBellezza(0);
                        sb.setData(Data);
                        sb.setDimensione(Dimensione);
                        sb.setTesto("");
                        sb.setTestoTradotto("");
                        sb.setUrlBrano("");
                        sb.setPathBrano(Filetto);
                        sb.setCartellaBrano("");
                        sb.setTags("");
                        sb.setTipoBrano(-1);

                        sb.setImmagini(new ArrayList<>());

                        db.ScriveBrano(sb);

                        maxId++;
                    }
                }
            }
        }
    }
}
