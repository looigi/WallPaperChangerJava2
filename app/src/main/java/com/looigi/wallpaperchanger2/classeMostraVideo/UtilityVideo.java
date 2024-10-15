package com.looigi.wallpaperchanger2.classeMostraVideo;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;

import com.looigi.wallpaperchanger2.classeMostraVideo.webservice.ChiamateWSV;
import com.looigi.wallpaperchanger2.classeStandard.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class UtilityVideo {
    private static UtilityVideo instance = null;

    private UtilityVideo() {
    }

    public static UtilityVideo getInstance() {
        if (instance == null) {
            instance = new UtilityVideo();
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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("VIDEO", Maschera,  Log);
            // }
        } else {

        }
    }

    public void ImpostaVideo() {
        Context context = VariabiliStaticheVideo.getInstance().getContext();
        if (context == null) {
            context = UtilitiesGlobali.getInstance().tornaContextValido();
        }
        try {
            String link = VariabiliStaticheVideo.getInstance().getUltimoLink();

            VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(VariabiliStaticheVideo.getInstance().getVideoView());
            Context finalContext = context;
            mediaController.setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Handle next click here
                    ChiamateWSV ws = new ChiamateWSV(finalContext);
                    ws.RitornaProssimoVideo();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Handle previous click here
                }
            });
            Uri video = Uri.parse(link);
            VariabiliStaticheVideo.getInstance().getVideoView().setMediaController(mediaController);
            VariabiliStaticheVideo.getInstance().getVideoView().setVideoURI(video);
            VariabiliStaticheVideo.getInstance().getVideoView().start();
            VariabiliStaticheVideo.getInstance().getVideoView().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);

                    return false;
                }
            });
            VariabiliStaticheVideo.getInstance().getVideoView().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // mediaController.show(0);

                    VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            // Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }
}