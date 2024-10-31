package com.looigi.wallpaperchanger2.classePlayer.scan;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanBraniNonPresentiSuSD {
    public void EsegueOperazione(Context context, boolean MostraPopup) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                int Eliminati = 0;

                db_dati_player db = new db_dati_player(context);
                List<StrutturaBrano> lista = db.CaricaTuttiIBraniLocali();
                for (StrutturaBrano l : lista) {
                    String Path = l.getPathBrano();
                    if (!Files.getInstance().EsisteFile(Path)) {
                        db.EliminaBrano(String.valueOf(l.getIdBrano()));
                        Eliminati++;
                    }
                }
                db.ChiudeDB();

                if (MostraPopup) {
                    UtilityDetector.getInstance().VisualizzaToast(context,
                            "Brani eliminati da SD: " + Eliminati,
                            true);
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
