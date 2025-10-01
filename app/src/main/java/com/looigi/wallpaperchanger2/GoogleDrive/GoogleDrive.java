package com.looigi.wallpaperchanger2.GoogleDrive;

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
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

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
        VariabiliStaticheGoogleDrive.getInstance().setTxtDettaglio(findViewById(R.id.txtDettaglio));

        UtilityGoogleDrive.getInstance().ImpostaAttesa(true, false);
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
        UtilityGoogleDrive.getInstance().ImpostaAttesa(true, false);
        Conta = 0;

        handlerTimerAF = new Handler(Looper.getMainLooper());
        rTimerAF = new Runnable() {
            public void run() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (VariabiliStaticheGoogleDrive.getInstance().isConnesso()) {
                            UtilityGoogleDrive.getInstance().ImpostaAttesa(false, false);
                            handlerTimerAF.removeCallbacksAndMessages(rTimerAF);

                            GestioneGoogleDrive g = new GestioneGoogleDrive();
                            switch (Modalita) {
                                case "LeggeFile":
                                    g.GestioneFileSuGoogleDrive(context,
                                            VariabiliStaticheGoogleDrive.getInstance().getPathOperazione(),
                                            VariabiliStaticheGoogleDrive.getInstance().getNomeFileApiFootball(),
                                            "LETTURA", false, act);
                                    break;
                                case "ScriveFile":
                                    g.GestioneFileSuGoogleDrive(context,
                                            VariabiliStaticheGoogleDrive.getInstance().getPathOperazione(),
                                            VariabiliStaticheGoogleDrive.getInstance().getNomeFileApiFootball(),
                                            "SCRITTURA", false, act);
                                    break;
                                case "AggiornaVersione":
                                    String pathDestinazione1 = context.getFilesDir() + "/GoogleDrive";

                                    Files.getInstance().CreaCartelle(pathDestinazione1);
                                    if (Files.getInstance().EsisteFile(pathDestinazione1 + "/" +
                                            VariabiliStaticheGoogleDrive.nomeFileAPK)) {
                                        Files.getInstance().EliminaFileUnico(pathDestinazione1 + "/" +
                                                VariabiliStaticheGoogleDrive.nomeFileAPK);
                                    }

                                    UtilityGoogleDrive.getInstance().ImpostaAttesa(true, true);
                                    VariabiliStaticheGoogleDrive.getInstance().setStaScaricandoFile(true);

                                    UtilityGoogleDrive.getInstance().dowload(
                                            VariabiliStaticheGoogleDrive.nuovaVersioneID, // Folder versioni
                                            VariabiliStaticheGoogleDrive.nomeFileAPK,
                                            pathDestinazione1 + "/" +
                                                    VariabiliStaticheGoogleDrive.nomeFileAPK
                                    );

                                    handlerThread = new HandlerThread("background-thread_DownloadUVAPK_" +
                                            VariabiliStaticheWallpaper.channelName);
                                    handlerThread.start();

                                    final int[] secondi = {0};
                                    final int[] decimi = {0};

                                    handler = new Handler(handlerThread.getLooper());
                                    r = new Runnable() {
                                        public void run() {
                                            decimi[0]++;
                                            if (decimi[0] > 9) {
                                                decimi[0] = 0;

                                                secondi[0]++;
                                                UtilityGoogleDrive.getInstance().ImpostaDettaglioGD("Download in corso " + secondi[0]);
                                            }

                                            if (!VariabiliStaticheGoogleDrive.getInstance().isStaScaricandoFile()) {
                                                UtilityGoogleDrive.getInstance().ImpostaAttesa(false, false);

                                                if (Files.getInstance().EsisteFile(pathDestinazione1 + "/" +
                                                        VariabiliStaticheGoogleDrive.nomeFileAPK)) {
                                                    if (VariabiliStaticheGoogleDrive.getInstance().getVersioneScaricata() == null || VariabiliStaticheGoogleDrive.getInstance().getVersioneScaricata().isEmpty()) {
                                                        UtilitiesGlobali.getInstance().ApreToast(context, "Versione non valida.");
                                                    } else {
                                                        java.io.File f = new File(pathDestinazione1 + "/" +
                                                                VariabiliStaticheGoogleDrive.nomeFileAPK);
                                                        Uri uri = FileProvider.getUriForFile(context,
                                                                context.getApplicationContext().getPackageName() + ".provider",
                                                                f
                                                        );
                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                        startActivity(intent);

                                                        if (Files.getInstance().EsisteFile(context.getFilesDir() + "/UltimaVersione.txt")) {
                                                            Files.getInstance().EliminaFileUnico(context.getFilesDir() + "/UltimaVersione.txt");
                                                        }
                                                        
                                                        Files.getInstance().ScriveFile(String.valueOf(
                                                                        context.getFilesDir()),
                                                                "UltimaVersione.txt",
                                                                VariabiliStaticheGoogleDrive.getInstance().getVersioneScaricata()
                                                        );
                                                    }
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
                                UtilityGoogleDrive.getInstance().ImpostaAttesa(true, false);
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
