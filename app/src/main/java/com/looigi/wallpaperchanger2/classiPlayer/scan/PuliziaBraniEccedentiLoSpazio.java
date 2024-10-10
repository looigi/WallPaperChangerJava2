package com.looigi.wallpaperchanger2.classiPlayer.scan;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classiPlayer.Files;
import com.looigi.wallpaperchanger2.classiPlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classiPlayer.VariabiliStatichePlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.Files.walk;

public class PuliziaBraniEccedentiLoSpazio {
    private long Limite;
    private long SpazioUtilizzato;
    private List<String> listaFinale;

    public void pulisceBrani(Context context, long SpazioOccupato, List<String> lista) {
        listaFinale = lista;
        float lim = VariabiliStatichePlayer.getInstance().getLimiteInGb();
        long L = (long) (lim * 1024 * 1024 * 1024);
        double ll = Math.round(L * .8);
        Limite = (long) ll;
        SpazioUtilizzato = SpazioOccupato;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (SpazioUtilizzato > Limite) {
                    int random = UtilityPlayer.getInstance().GeneraNumeroRandom(listaFinale.size() - 1);
                    long dimensione = Files.getInstance().DimensioniFile(listaFinale.get(random));
                    Files.getInstance().EliminaFileUnico(listaFinale.get(random));
                    List<String> l2 = new ArrayList<>();
                    for (String lll : listaFinale) {
                        if (!lll.equals(listaFinale.get(random))) {
                            l2.add(lll);
                        }
                    }
                    listaFinale = l2;
                    SpazioUtilizzato -= dimensione;
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
