package com.looigi.wallpaperchanger2.classePlayer.scan;

import android.content.Context;
import android.icu.util.UniversalTimeScale;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanBraniPerLimite {
    private static final String NomeMaschera = "Scan_Brani";

    private long Spazio = 0;
    private List<String> listaBrani;
    private List<String> listaImmagini;

    public void controllaSpazioOccupato(Context context) {
        Spazio = 0;
        listaBrani = new ArrayList<>();
        listaImmagini = new ArrayList<>();

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
                walk(rootPrincipale, "BRANI");

                String path2 = context.getFilesDir() + "/Player/ImmaginiMusica";

                File rootPrincipale2 = new File(path2);
                if (!rootPrincipale2.exists()) {
                    rootPrincipale2.mkdir();
                }
                walk(rootPrincipale2, "IMMAGINI");

                float lim = VariabiliStatichePlayer.getInstance().getLimiteInGb();
                long L = (long) (lim * 1024 * 1024 * 1024);
                double ll = Math.round(L * .8);
                long Limite = (long) ll;

                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                        "Scan spazio.\n" +
                        "Occupato: " + Spazio + "\n" +
                        "Limite: " + Limite + "\n" +
                        "Brani: " + (listaBrani.size() - 1) + "\n" +
                        "Immagini: " + (listaImmagini.size() - 1));

                int pe = Math.round(((float) Spazio / Limite) * 100);
                String perc = pe + "%";
                VariabiliStatichePlayer.getInstance().getTxtPercentuale().setText(perc);

                if (Spazio > Limite) {
                    // PULIZIA.. Troppo spazio occupato
                    PuliziaBraniEccedentiLoSpazio p = new PuliziaBraniEccedentiLoSpazio();
                    p.pulisceBrani(context, Spazio, listaBrani, listaImmagini);
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

    private void walk(File root, String Cosa) {
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }

        for (File f : list) {
            if (f.isDirectory()) {
                walk(f, Cosa);
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
                boolean ok = false;
                if (Cosa.equals("BRANI")) {
                    if (Nome.toUpperCase().contains(".MP3") || Nome.toUpperCase().contains(".WMA")) {
                        ok = true;
                    }
                } else {
                    if (Nome.toUpperCase().contains(".JPG") || Nome.toUpperCase().contains(".JPEG")
                            || Nome.toUpperCase().contains(".BMP") || Nome.toUpperCase().contains(".PNG")) {
                        ok = true;
                    }
                }
                if (ok) {
                    long dimensioni = Files.getInstance().DimensioniFile(Filetto);

                    if (Cosa.equals("BRANI")) {
                        listaBrani.add(Filetto);
                    } else {
                        listaImmagini.add(Filetto);
                    }
                    Spazio += dimensioni;
                }
            }
        }
    }
}
