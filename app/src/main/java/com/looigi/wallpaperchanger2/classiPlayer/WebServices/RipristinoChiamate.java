package com.looigi.wallpaperchanger2.classiPlayer.WebServices;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.looigi.wallpaperchanger2.classiPlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classiPlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;

import java.util.ArrayList;

public class RipristinoChiamate {
    private static final String NomeMaschera = "RIPRISTINOCHIAMATE";
    private Runnable r;
    private HandlerThread handlerThread;
    private Handler handler;
    private int tempoDiAttesa = 5000;
    private int tentativiEffettuati = 0;

    private static RipristinoChiamate instance = null;

    private RipristinoChiamate() {
    }

    public static RipristinoChiamate getInstance() {
        if (instance == null) {
            instance = new RipristinoChiamate();
        }

        return instance;
    }

    public int getTentativiEffettuati() {
        return tentativiEffettuati;
    }

    public void setTentativiEffettuati(int tentativiEffettuati) {
        this.tentativiEffettuati = tentativiEffettuati;
    }

    public void RimuoveTimer() {
        if (handler != null && r != null && handlerThread != null) {
            handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    public void AttivaTimerChiamate(Context context) {
        if (handler != null || VariabiliStatichePlayer.getInstance().getChiamate().isEmpty()) {
            return;
        }

        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Attivo timer");

        handlerThread = new HandlerThread("background-thread_" +
                VariabiliStaticheWallpaper.channelName + "_RC");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                StrutturaChiamateWSPlayer s = VariabiliStatichePlayer.getInstance().getChiamate().get(0);

                VariabiliStatichePlayer.getInstance().setRetePresente(true);
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Riprovo chiamata " + s.gettOperazione() +
                        "Tentativo: " + (tentativiEffettuati + 1));

                switch (s.gettOperazione()) {
                    case "RitornaProssimoBranoMobile":
                        if (VariabiliStatichePlayer.getInstance().getClasseChiamata() != null) {
                            VariabiliStatichePlayer.getInstance().getClasseChiamata().StoppaEsecuzione();
                        }

                        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, true);
                        VariabiliStatichePlayer.getInstance().setClasseChiamata(ws);
                        ws.RitornaBranoDaID(s.getBrano(), s.isPregresso());

                        tentativiEffettuati++;
                        tempoDiAttesa = tentativiEffettuati * 5000;
                        if (tentativiEffettuati > 5) {
                            tentativiEffettuati = 0;
                            VariabiliStatichePlayer.getInstance().setChiamate(new ArrayList<>());

                            RimuoveTimer();
                        }
                        break;
                }
            }
        };
        handler.postDelayed(r, tempoDiAttesa);
    }
}
