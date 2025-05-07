package com.looigi.wallpaperchanger2.classeGoogleDrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;

public class GoogleDrive extends Activity {
    private Context context;
    private Activity act;
    private String Modalita;
    private LinearLayout cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_google_drive);

        cont = findViewById(R.id.layContenitore);
        cont.setVisibility(LinearLayout.GONE);

        VariabiliStaticheGoogleDrive.getInstance().setImgAttesa(findViewById(R.id.imgCaricamentoGoogleDrive));
        UtilityGoogleDrive.getInstance().ImpostaAttesa(true);
        VariabiliStaticheGoogleDrive.getInstance().setLstFolders(findViewById(R.id.lstFolders));
        VariabiliStaticheGoogleDrive.getInstance().setLstFiles(findViewById(R.id.lstFiles));

        // Intent intent = getIntent();
        // String id = intent.getStringExtra("DO");
        // if (id != null) {
        //     Modalita = id;
        // } else {
        if (VariabiliStaticheGoogleDrive.getInstance().getOperazioneDaEffettuare() != null) {
            if (VariabiliStaticheGoogleDrive.getInstance().getOperazioneDaEffettuare().isEmpty()) {
                Modalita = "";
            } else {
                Modalita = VariabiliStaticheGoogleDrive.getInstance().getOperazioneDaEffettuare();
            }
        } else {
            Modalita = "";
        }

        context = this;
        act = this;

        VariabiliStaticheGoogleDrive.getInstance().setAct(this);

        VariabiliStaticheGoogleDrive.getInstance().setConnesso(false);
        VariabiliStaticheGoogleDrive.getInstance().setErroreConnessione("");

        VariabiliStaticheGoogleDrive.getInstance().setDriveHelper(new GoogleDriveHelper(this, context));
        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().signIn(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().handleSignInResult(requestCode, resultCode, data);

        AttendeConnessione();
    }

    private Handler handlerTimerAF;
    private Runnable rTimerAF;
    private int Conta = 0;
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;

    public void AttendeConnessione() {
        UtilityGoogleDrive.getInstance().ImpostaAttesa(true);
        Conta = 0;

        handlerTimerAF = new Handler(Looper.getMainLooper());
        rTimerAF = new Runnable() {
            public void run() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (VariabiliStaticheGoogleDrive.getInstance().isConnesso()) {
                            UtilityGoogleDrive.getInstance().ImpostaAttesa(false);
                            handlerTimerAF.removeCallbacksAndMessages(rTimerAF);

                            GestioneGoogleDrive g = new GestioneGoogleDrive();
                            switch (Modalita) {
                                case "LeggeFile":
                                    g.GestioneFileSuGoogleDrive(context,
                                            VariabiliStaticheGoogleDrive.getInstance().getPathOperazione(),
                                            VariabiliStaticheGoogleDrive.getInstance().getNomeFileApiFootball(),
                                            "LETTURA", false);
                                    break;
                                case "ScriveFile":
                                    g.GestioneFileSuGoogleDrive(context,
                                            VariabiliStaticheGoogleDrive.getInstance().getPathOperazione(),
                                            VariabiliStaticheGoogleDrive.getInstance().getNomeFileApiFootball(),
                                            "SCRITTURA", false);
                                    break;
                                case "AggiornaVersione":
                                    String pathDestinazione1 = context.getFilesDir() + "/GoogleDrive";

                                    Files.getInstance().CreaCartelle(pathDestinazione1);
                                    if (Files.getInstance().EsisteFile(pathDestinazione1 + "/Wallpaper Changer II.apk")) {
                                        Files.getInstance().EliminaFileUnico(pathDestinazione1 + "/Wallpaper Changer II.apk");
                                    }

                                    UtilityGoogleDrive.getInstance().dowload(
                                            VariabiliStaticheGoogleDrive.nuovaVersioneID, // Folder versioni
                                            "Wallpaper Changer II.apk",
                                            pathDestinazione1 + "/Wallpaper Changer II.apk"
                                    );

                                    VariabiliStaticheGoogleDrive.getInstance().setStaScaricandoFile(true);

                                    handlerThread = new HandlerThread("background-thread_DownloadUVAPK_" +
                                            VariabiliStaticheWallpaper.channelName);
                                    handlerThread.start();

                                    handler = new Handler(handlerThread.getLooper());
                                    r = new Runnable() {
                                        public void run() {
                                            if (!VariabiliStaticheGoogleDrive.getInstance().isStaScaricandoFile()) {
                                                if (Files.getInstance().EsisteFile(pathDestinazione1 + "/Wallpaper Changer II.apk")) {
                                                    java.io.File f = new File(pathDestinazione1 + "/Wallpaper Changer II.apk");
                                                    Uri uri = FileProvider.getUriForFile(context,
                                                            context.getApplicationContext().getPackageName() + ".provider",
                                                            f
                                                    );
                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    startActivity(intent);
                                                } else {
                                                    UtilitiesGlobali.getInstance().ApreToast(context, "Problemi nell'aggiornamento della versione");
                                                }

                                                act.finish();
                                            } else {
                                                if (handler != null) {
                                                    handler.postDelayed(this, 100);
                                                }
                                            }
                                        }
                                    };
                                    handler.postDelayed(r, 100);

                                    break;
                                case "RilevaVersione":
                                    String pathDestinazione = context.getFilesDir() + "/GoogleDrive";

                                    Files.getInstance().CreaCartelle(pathDestinazione);

                                    UtilityGoogleDrive.getInstance().dowload(
                                            VariabiliStaticheGoogleDrive.nuovaVersioneID, // Folder versioni
                                            "UltimaVersione.txt",
                                            pathDestinazione + "/UltimaVersione.txt"
                                    );

                                    VariabiliStaticheGoogleDrive.getInstance().setStaScaricandoFile(true);

                                    handlerThread = new HandlerThread("background-thread_DownloadUV_" +
                                            VariabiliStaticheWallpaper.channelName);
                                    handlerThread.start();

                                    handler = new Handler(handlerThread.getLooper());
                                    r = new Runnable() {
                                        public void run() {
                                            if (!VariabiliStaticheGoogleDrive.getInstance().isStaScaricandoFile()) {
                                                UtilitiesGlobali.getInstance().ControllaNuovaVersione2(context);

                                                act.finish();
                                            } else {
                                                if (handler != null) {
                                                    handler.postDelayed(this, 100);
                                                }
                                            }
                                        }
                                    };
                                    handler.postDelayed(r, 100);

                                    break;
                                default:
                                    cont.setVisibility(LinearLayout.VISIBLE);

                                    if (VariabiliStaticheGoogleDrive.getInstance().getErroreConnessione().isEmpty()) {
                                        UtilityGoogleDrive.getInstance().listaRootFolder(context);
                                    } else {
                                        UtilitiesGlobali.getInstance().ApreToast(context,
                                                VariabiliStaticheGoogleDrive.getInstance().getErroreConnessione()
                                        );
                                    }
                                    break;
                            }
                        } else {
                            if (Conta > 20) {
                                UtilityGoogleDrive.getInstance().ImpostaAttesa(true);
                                handlerTimerAF.removeCallbacksAndMessages(rTimerAF);

                                UtilitiesGlobali.getInstance().ApreToast(context,
                                        "Timeout nella connessione"
                                );
                            } else {
                                handlerTimerAF.postDelayed(rTimerAF, 500);
                            }
                        }
                    }
                }, 500);
            }
        };
        handlerTimerAF.postDelayed(rTimerAF, 500);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /* if (handlerTimer != null) {
                    handlerTimer.removeCallbacks(rTimer);
                    rTimer = null;
                    handlerTimer = null;
                } */
                this.finish();

                return false;
        }

        super.onKeyDown(keyCode, event);

        return false;
    }
}
