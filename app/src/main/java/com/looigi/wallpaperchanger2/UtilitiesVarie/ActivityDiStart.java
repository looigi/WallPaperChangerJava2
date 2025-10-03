package com.looigi.wallpaperchanger2.UtilitiesVarie;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import com.looigi.wallpaperchanger2.Notifiche.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Backup.MainBackup;
import com.looigi.wallpaperchanger2.Detector.MainActivityDetector;
import com.looigi.wallpaperchanger2.Detector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.Fetekkie.MainMostraFetekkie;
import com.looigi.wallpaperchanger2.Films.MainMostraFilms;
import com.looigi.wallpaperchanger2.GoogleDrive.GoogleDrive;
import com.looigi.wallpaperchanger2.GoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.MainMappa;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.MainMostraImmagini;
import com.looigi.wallpaperchanger2.Impostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.Lazio.MainLazio;
import com.looigi.wallpaperchanger2.ModificheCodice.MainModificheCodice;
import com.looigi.wallpaperchanger2.Onomastici.MainOnomastici;
import com.looigi.wallpaperchanger2.Orari.MainOrari;
import com.looigi.wallpaperchanger2.Password.MainPassword;
import com.looigi.wallpaperchanger2.Pennetta.MainMostraPennetta;
import com.looigi.wallpaperchanger2.Player.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.Player.MainPlayer;
import com.looigi.wallpaperchanger2.Player.UtilityPlayer;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.MainRilevaOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.MainUtilityImmagini;
import com.looigi.wallpaperchanger2.UtilitiesVarie.InformazioniTelefono.PrendeModelloTelefono;
import com.looigi.wallpaperchanger2.Video.MainMostraVideo;
import com.looigi.wallpaperchanger2.Wallpaper.MainWallpaper;

public class ActivityDiStart extends FragmentActivity {
    private FragmentActivity act;
    private Context context;
    private String Cosa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_di_start);

        context = this;
        act = this;

        ImageView imgAttesa = findViewById(R.id.imgLoading);
        UtilitiesGlobali.getInstance().AttesaGif(context, imgAttesa, true);

        Intent intent = getIntent();
        String id = intent.getStringExtra("DO");

        TextView t = findViewById(R.id.txtOperazione);
        t.setText("Apertura " + id);

        boolean fingerPrint = false;

        switch (id) {
            /* case "allarme":
                SharedPreferences prefs = getSharedPreferences("START", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                if (!VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
                    UtilityAntifurto.getInstance().AttivaAntifurto(context, true);
                    editor.putString(
                            "AntifurtoAttivo",
                            "S");
                } else {
                    UtilityAntifurto.getInstance().AttivaAntifurto(context, false);
                    editor.putString(
                            "AntifurtoAttivo",
                            "N");
                }
                editor.apply();

                GestioneNotificheTasti.getInstance().AggiornaNotifica();
                break; */
            case "update":
                UtilitiesGlobali.getInstance().ControllaNuovaVersione(context);
                break;
            case "drive":
                VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("");
                Intent iDR = new Intent(context, GoogleDrive.class);
                iDR.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iDR);
                break;
            case "settings":
                Intent iI = new Intent(context, MainImpostazioni.class);
                iI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iI);
                break;
            case "controllo_immagini":
                fingerPrint = true;
                Cosa = id;
                ControlloFingerPrint();
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
                fingerPrint = true;
                Cosa = id;
                ControlloFingerPrint();
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
                fingerPrint = true;
                Cosa = id;
                ControlloFingerPrint();
                break;
            case "pennetta":
                fingerPrint = true;
                Cosa = id;
                ControlloFingerPrint();
                break;
            case "video":
                fingerPrint = true;
                Cosa = id;
                ControlloFingerPrint();
                break;
            case "films":
                Intent iF = new Intent(context, MainMostraFilms.class);
                iF.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iF);
                break;
            case "ocr":
                Intent ocr = new Intent(context, MainRilevaOCR.class);
                ocr.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ocr);
                break;
            case "uscita":
                UtilitiesGlobali.getInstance().ChiudeApplicazione(context);
                break;
        }

        if (!fingerPrint) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    UtilitiesGlobali.getInstance().AttesaGif(context, imgAttesa, false);

                    act.finish();
                }
            };
            handlerTimer.postDelayed(rTimer, 1000);
        }
    }

    private BiometricManagerSingleton bioManager;

    private void ControlloFingerPrint() {
        PrendeModelloTelefono p = new PrendeModelloTelefono();
        String modello = p.getDeviceName();
        if (!modello.contains("sdk_gphone64")) {
            bioManager = BiometricManagerSingleton.getInstance(context);

            int can = bioManager.canAuthenticate();
            if (can == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS) {
                bioManager.authenticate(act, "Accedi", "Autenticazione con impronta o volto", authCallback);
            } else {
                String msg;
                switch (can) {
                    case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        msg = "Dispositivo senza hardware biometrico";
                        break;
                    case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        msg = "Hardware biometrico non disponibile";
                        break;
                    case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        msg = "Nessuna impronta/biometria registrata. Registra una nella impostazioni.";
                        break;
                    default:
                        msg = "Impossibile usare biometria";
                }
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        } else {
            AutenticazioneOK();
        }
    }

    private final BiometricPrompt.AuthenticationCallback authCallback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            runOnUiThread(() -> {
                Toast.makeText(context, "Autenticazione OK", Toast.LENGTH_SHORT).show();

                AutenticazioneOK();
            });
            // Procedi con operazione protetta
        }

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            runOnUiThread(() -> Toast.makeText(context, "Errore: " + errString, Toast.LENGTH_SHORT).show());
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            runOnUiThread(() -> Toast.makeText(context, "Autenticazione fallita", Toast.LENGTH_SHORT).show());
        }
    };

    private void AutenticazioneOK() {
        switch(Cosa) {
            case "controllo_immagini":
                Intent ci = new Intent(context, MainUtilityImmagini.class);
                ci.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ci);
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
            case "immagini":
                Intent iIm = new Intent(context, MainMostraImmagini.class);
                iIm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iIm);
                break;
            case "password":
                Intent iPa = new Intent(context, MainPassword.class);
                iPa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iPa);
                break;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                act.finish();
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);
    }
}
