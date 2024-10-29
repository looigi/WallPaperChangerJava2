package com.looigi.wallpaperchanger2.classePlayer.scan;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.Files.walk;

public class PuliziaBraniEccedentiLoSpazio {
    private static final String NomeMaschera = "Pulizia_Brani";

    private long Limite;
    private long SpazioUtilizzato;
    private List<String> listaFinaleBrani;
    private List<String> listaFinaleImmagini;

    public void pulisceBrani(Context context, long SpazioOccupato,
                             List<String> listaBrani, List<String> listaImmagini) {
        listaFinaleBrani = listaBrani;
        listaFinaleImmagini = listaImmagini;
        float lim = VariabiliStatichePlayer.getInstance().getLimiteInGb();
        long L = (long) (lim * 1024 * 1024 * 1024);
        double ll = Math.round(L * .8);
        Limite = (long) ll;
        SpazioUtilizzato = SpazioOccupato;

        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Pulizia brani.\n" +
                        "Spazio Occupato: " + SpazioOccupato + "\n" +
                        "Spazio Limite: " + Limite + "\n" +
                        "Brani in archivio: " + (listaBrani.size() - 1) + "\n" +
                        "Immagini ini archivio: " + (listaImmagini.size() - 1));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                int quanti = 0;
                int progressivoPerImmagini = 0;
                int progressivoPerBrani = 0;
                int modalita = 0;
                int erroriBrani = 0;
                int erroriImmagini= 0;

                while (SpazioUtilizzato > Limite) {
                    if (modalita == 0) {
                        // Eliminazione brani
                        int random = UtilityPlayer.getInstance().GeneraNumeroRandom(listaFinaleBrani.size() - 1);
                        long dimensione = Files.getInstance().DimensioniFile(listaFinaleBrani.get(random));

                        quanti++;
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                "Eliminazione brano " + quanti + ": " + listaFinaleBrani.get(random) + ". Dimensioni: " + dimensione);

                        Files.getInstance().EliminaFileUnico(listaFinaleBrani.get(random));
                        if (!Files.getInstance().EsisteFile(listaFinaleBrani.get(random))) {
                            List<String> l2 = new ArrayList<>();
                            for (String lll : listaFinaleBrani) {
                                if (!lll.equals(listaFinaleBrani.get(random))) {
                                    l2.add(lll);
                                }
                            }
                            listaFinaleBrani = l2;
                            SpazioUtilizzato -= dimensione;

                            erroriBrani = 0;
                            progressivoPerImmagini++;
                            if (progressivoPerImmagini > 5) {
                                progressivoPerImmagini = 0;
                                modalita = 1;
                            }
                        } else {
                            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                    "Brano NON eliminato");

                            erroriBrani++;
                            if (erroriBrani > 5) {
                                if (erroriImmagini > 5) {
                                    break;
                                }
                                modalita = 1;
                            }
                        }
                    } else {
                        // Eliminazione immagini
                        int random = UtilityPlayer.getInstance().GeneraNumeroRandom(listaFinaleImmagini.size() - 1);
                        long dimensione = Files.getInstance().DimensioniFile(listaFinaleImmagini.get(random));

                        quanti++;
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                "Eliminazione immagine " + quanti + ": " + listaFinaleImmagini.get(random) + ". Dimensioni: " + dimensione);

                        Files.getInstance().EliminaFileUnico(listaFinaleImmagini.get(random));
                        if (!Files.getInstance().EsisteFile(listaFinaleImmagini.get(random))) {
                            List<String> l2 = new ArrayList<>();
                            for (String lll : listaFinaleImmagini) {
                                if (!lll.equals(listaFinaleImmagini.get(random))) {
                                    l2.add(lll);
                                }
                            }
                            listaFinaleImmagini = l2;
                            SpazioUtilizzato -= dimensione;

                            erroriImmagini++;
                            progressivoPerBrani++;
                            if (progressivoPerBrani > 10) {
                                progressivoPerBrani = 0;
                                modalita = 0;
                            }
                        } else {
                            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                    "Immagine NON eliminata");

                            erroriImmagini++;
                            if (erroriImmagini > 5) {
                                if (erroriBrani > 5) {
                                    break;
                                }
                                modalita = 0;
                            }
                        }
                    }
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
}
