package com.looigi.wallpaperchanger2.classeFilms;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;

import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;
import com.looigi.wallpaperchanger2.classeStandard.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class UtilityFilms {
    private static UtilityFilms instance = null;

    private UtilityFilms() {
    }

    public static UtilityFilms getInstance() {
        if (instance == null) {
            instance = new UtilityFilms();
        }

        return instance;
    }

    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Films", Maschera,  Log);
            // }
        } else {

        }
    }

    public void ImpostaFilms() {
        Context context = VariabiliStaticheFilms.getInstance().getContext();
        if (context == null) {
            context = UtilitiesGlobali.getInstance().tornaContextValido();
        }
        try {
            String link = VariabiliStaticheFilms.getInstance().getUltimoLink();

            VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(VariabiliStaticheFilms.getInstance().getFilmsView());
            Context finalContext = context;
            mediaController.setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Handle next click here
                    ChiamateWSF ws = new ChiamateWSF(finalContext);
                    ws.RitornaProssimoFilms();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Handle previous click here
                }
            });
            Uri Films = Uri.parse(link);
            VariabiliStaticheFilms.getInstance().getFilmsView().setMediaController(mediaController);
            VariabiliStaticheFilms.getInstance().getFilmsView().setVideoURI(Films);
            VariabiliStaticheFilms.getInstance().getFilmsView().start();
            VariabiliStaticheFilms.getInstance().getFilmsView().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);

                    return false;
                }
            });
            VariabiliStaticheFilms.getInstance().getFilmsView().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // mediaController.show(0);

                    VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            // Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }
}
