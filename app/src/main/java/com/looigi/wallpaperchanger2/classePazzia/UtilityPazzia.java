package com.looigi.wallpaperchanger2.classePazzia;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePennetta.webservice.ChiamateWSPEN;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;

import pl.droidsonroids.gif.GifImageView;

public class UtilityPazzia {
    private static UtilityPazzia instance = null;

    private UtilityPazzia() {
    }

    public static UtilityPazzia getInstance() {
        if (instance == null) {
            instance = new UtilityPazzia();
        }

        return instance;
    }

    public void CambiaImmaginePennetta(Context context) {
        ChiamateWSPEN ws = new ChiamateWSPEN(context);
        ws.RitornaProssimaImmagine(
                VariabiliStaticheMostraImmaginiPennetta.getInstance().getCategoria(),
                "PAZZIA"
        );
    }

    public void CambiaImmagineImmagine(Context context) {
        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaProssimaImmagine(
                VariabiliStaticheMostraImmagini.getInstance().getIdCategoria(),
                VariabiliStaticheMostraImmagini.getInstance().getIdImmagine(),
                VariabiliStaticheMostraImmagini.getInstance().getRandom(),
                "PAZZIA"
        );
    }

    public void CambiaVideo(Context context) {

    }

    public void ImpostaAttesaPazzia(GifImageView chi, boolean come) {
        if (chi != null) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    if (come) {
                        chi.setVisibility(LinearLayout.VISIBLE);
                    } else {
                        chi.setVisibility(LinearLayout.GONE);
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }
}
