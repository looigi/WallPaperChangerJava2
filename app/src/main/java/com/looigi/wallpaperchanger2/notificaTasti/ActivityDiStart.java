package com.looigi.wallpaperchanger2.notificaTasti;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeBackup.MainBackup;
import com.looigi.wallpaperchanger2.classeDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeFetekkie.MainMostraFetekkie;
import com.looigi.wallpaperchanger2.classeFilms.MainMostraFilms;
import com.looigi.wallpaperchanger2.classeGps.MainMappa;
import com.looigi.wallpaperchanger2.classeImmagini.MainMostraImmagini;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeLazio.MainLazio;
import com.looigi.wallpaperchanger2.classeModificheCodice.MainModificheCodice;
import com.looigi.wallpaperchanger2.classeOnomastici.MainOnomastici;
import com.looigi.wallpaperchanger2.classeOrari.MainOrari;
import com.looigi.wallpaperchanger2.classePassword.MainPassword;
import com.looigi.wallpaperchanger2.classePennetta.MainMostraPennetta;
import com.looigi.wallpaperchanger2.classePlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.MainPlayer;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classeVideo.MainMostraVideo;
import com.looigi.wallpaperchanger2.classeWallpaper.MainWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class ActivityDiStart extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_di_start);

        Context context = this;
        Intent intent = getIntent();
        String id = intent.getStringExtra("DO");

        TextView t = findViewById(R.id.txtOperazione);
        t.setText("Apertura " + id);

        switch (id) {
                case "settings":
                    Intent iI = new Intent(context, MainImpostazioni.class);
                    iI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iI);
                    break;
                case "lazio":
                    Intent iL = new Intent(context, MainLazio.class);
                    iL.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iL);
                    break;
                case "modifiche":
                    Intent iM = new Intent(context, MainModificheCodice.class);
                    iM.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iM);
                    break;
                case "orari":
                    Intent iO = new Intent(context, MainOrari.class);
                    iO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iO);
                    break;
                case "detector":
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            VariabiliStaticheDetector.getInstance().setMascheraPartita(false);

                            Intent iD = new Intent(context, MainActivityDetector.class);
                            iD.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iD);

                            /* new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    InizializzaMascheraDetector i2 = new InizializzaMascheraDetector();
                                    i2.inizializzaMaschera(
                                            context,
                                            VariabiliStaticheDetector.getInstance().getMainActivity());
                                }
                            }, 100); */
                        }
                    }, 500);
                    break;
                case "onomastici":
                    Intent iOn = new Intent(context, MainOnomastici.class);
                    iOn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iOn);
                    break;
                case "fetekkie":
                    Intent iFE = new Intent(context, MainMostraFetekkie.class);
                    iFE.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iFE);
                    break;
                case "wallpaper":
                    Intent iW = new Intent(context, MainWallpaper.class);
                    iW.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iW);
                    break;
                case "backup":
                    Intent iB = new Intent(context, MainBackup.class);
                    iB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iB);
                    break;
                case "mappa":
                    Intent iMa = new Intent(context, MainMappa.class);
                    iMa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iMa);
                    break;
                case "password":
                    Intent iPa = new Intent(context, MainPassword.class);
                    iPa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iPa);
                    break;
                case "player":
                    if (!VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                        VariabiliStaticheStart.getInstance().setPlayerAperto(true);

                        Intent iP = new Intent(context, MainPlayer.class);
                        iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iP);

                        Notification notificaPlayer = GestioneNotifichePlayer.getInstance().StartNotifica(context);
                        if (notificaPlayer != null) {
                            // startForeground(VariabiliStatichePlayer.NOTIFICATION_CHANNEL_ID, notificaPlayer);

                            GestioneNotifichePlayer.getInstance().AggiornaNotifica("Titolo Canzone");
                            GestioneNotificheTasti.getInstance().AggiornaNotifica();

                            UtilitiesGlobali.getInstance().ApreToast(context, "Player Partito");
                        }
                    } else {
                        VariabiliStaticheStart.getInstance().setPlayerAperto(false);

                        UtilityPlayer.getInstance().PressionePlay(context, false);
                        GestioneNotificheTasti.getInstance().AggiornaNotifica();

                        Handler handlerTimer = new Handler(Looper.getMainLooper());
                        Runnable rTimer = new Runnable() {
                            public void run() {
                                GestioneNotifichePlayer.getInstance().RimuoviNotifica();

                                Handler handlerTimer = new Handler(Looper.getMainLooper());
                                Runnable rTimer = new Runnable() {
                                    public void run() {
                                        UtilityPlayer.getInstance().ChiudeActivity(true);
                                    }
                                };
                                handlerTimer.postDelayed(rTimer, 500);
                            }
                        };
                        handlerTimer.postDelayed(rTimer, 500);
                    }
                    break;
                case "immagini":
                    Intent iIm = new Intent(context, MainMostraImmagini.class);
                    iIm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iIm);
                    break;
                case "pennetta":
                    Intent iPe = new Intent(context, MainMostraPennetta.class);
                    iPe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iPe);
                    break;
                case "video":
                    Intent iVi = new Intent(context, MainMostraVideo.class);
                    iVi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iVi);
                    break;
                case "films":
                    Intent iF = new Intent(context, MainMostraFilms.class);
                    iF.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iF);
                    break;
                case "uscita":
                    UtilitiesGlobali.getInstance().ChiudeApplicazione(context);
                    break;
        }

        Activity act = this;

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                act.finish();
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);

    }
}
