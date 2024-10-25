package com.looigi.wallpaperchanger2.classePlayer.scan;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanBraniPerLimite {
    private long Spazio = 0;
    private List<String> lista;

    public void controllaSpazioOccupato(Context context) {
        Spazio = 0;
        lista = new ArrayList<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                String path = context.getFilesDir() + "/Player/Brani";

                File rootPrincipale = new File(path);
                if (!rootPrincipale.exists()) {
                    rootPrincipale.mkdir();
                }
                walk(rootPrincipale);

                float lim = VariabiliStatichePlayer.getInstance().getLimiteInGb();
                long L = (long) (lim * 1024 * 1024 * 1024);
                double ll = Math.round(L * .8);
                long Limite = (long) ll;

                if (Spazio > Limite) {
                    // PULIZIA.. Troppo spazio occupato
                    PuliziaBraniEccedentiLoSpazio p = new PuliziaBraniEccedentiLoSpazio();
                    // p.pulisceBrani(context, Spazio, lista);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }

    private void walk(File root) {
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                walk(f);
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
                if (Nome.toUpperCase().contains(".MP3") || Nome.toUpperCase().contains(".WMA")) {
                    long dimensioni = Files.getInstance().DimensioniFile(Filetto);

                    lista.add(Filetto);
                    Spazio += dimensioni;
                }
            }
        }
    }
}
