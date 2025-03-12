package com.looigi.wallpaperchanger2.classeBackup;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.CaricaSettaggi;
import com.looigi.wallpaperchanger2.utilities.log.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import pl.droidsonroids.gif.GifImageView;

public class UtilityBackup {
    private static final String NomeMaschera = "Utility_Backup";
    private static UtilityBackup instance = null;

    private UtilityBackup() {
    }

    public static UtilityBackup getInstance() {
        if (instance == null) {
            instance = new UtilityBackup();
        }

        return instance;
    }

    private TextView txtSelezionato;
    private StrutturaBackups nomeFileZipSelezionato;
    private GifImageView imgCaricamento;

    public void Attende(boolean Attesa) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (imgCaricamento != null) {
                    if (!Attesa) {
                        imgCaricamento.setVisibility(LinearLayout.GONE);
                    } else {
                        imgCaricamento.setVisibility(LinearLayout.VISIBLE);
                    }
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public TextView getTxtSelezionato() {
        return txtSelezionato;
    }

    public void setTxtSelezionato(TextView txtSelezionato) {
        this.txtSelezionato = txtSelezionato;
    }

    public StrutturaBackups getNomeFileZipSelezionato() {
        return nomeFileZipSelezionato;
    }

    public void setNomeFileZipSelezionato(StrutturaBackups nomeFileZipSelezionato) {
        this.nomeFileZipSelezionato = nomeFileZipSelezionato;
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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Backup", Maschera,  Log);
            // }
        } else {

        }
    }
}
