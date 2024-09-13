package com.looigi.wallpaperchanger2.classiStandard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.InizializzaMascheraDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.AdapterListenerImmagini;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.ScannaDiscoPerImmaginiLocali;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.db_dati;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;
import com.looigi.wallpaperchanger2.webservice.ChiamateWS;

import java.io.File;
import java.io.IOException;

public class InizializzaMaschera {
    private static final String NomeMaschera = "INITMASCHERA";
    private Long controlloLongPress = null;

    public void inizializzaMaschera(Context context, Activity view) {
        Utility.getInstance().Attesa(true);

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                inizializzaMaschera2(context, view);
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);
    }

    private void inizializzaMaschera2(Context context, Activity view) {
        Utility.getInstance().ScriveLog(context, NomeMaschera, "Inizializzo maschera");

        VariabiliStaticheServizio.getInstance().setLayAttesa(view.findViewById(R.id.layAttesa));
        Utility.getInstance().Attesa(false);

        TextView txtQuante = (TextView) view.findViewById(R.id.txtQuanteImmagini);
        VariabiliStaticheServizio.getInstance().setTxtQuanteImmagini(txtQuante);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Apro db");
        db_dati db = new db_dati(context);
        Utility.getInstance().ScriveLog(context, NomeMaschera,"Creo tabelle");
        db.CreazioneTabelle();
        Utility.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
        boolean letto = db.LeggeImpostazioni();
        Utility.getInstance().ScriveLog(context, NomeMaschera,"Impostazioni lette: " + letto);
        VariabiliStaticheServizio.getInstance().setLetteImpostazioni(letto);

        if (VariabiliStaticheServizio.getInstance().isDetector() &&
                !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                VariabiliStaticheServizio.getInstance().isCiSonoPermessi()) {
            Intent intent = new Intent(context, MainActivityDetector.class);
            context.startActivity(intent);
        }

        if (!VariabiliStaticheServizio.getInstance().isLetteImpostazioni()) {
            Utility.getInstance().ScriveLog(context, NomeMaschera, "Mancanza di impostazioni");
            Utility.getInstance().ApreToast(context, VariabiliStaticheServizio.channelName + ": Mancanza di impostazioni");
        }

        ImpostaOggetti(context, view);

        if (!VariabiliStaticheServizio.getInstance().isePartito()) {
            Utility.getInstance().ScriveLog(context, NomeMaschera,"Carico immagini in locale");
            boolean letteImmagini = db.CaricaImmaginiInLocale();

            if (!letteImmagini) {
                // if (VariabiliGlobali.getInstance().isOffline()) {
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Immagini in locale non rilevate... Scanno...");
                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context);
                bckLeggeImmaginiLocali.execute();
                // }
            } else {
                if (VariabiliStaticheServizio.getInstance().isOffline()) {
                    int q = VariabiliStaticheServizio.getInstance().getListaImmagini().size();
                    VariabiliStaticheServizio.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + q);
                    Utility.getInstance().ScriveLog(context, NomeMaschera,"Immagini rilevate su disco: " + q);
                } else {
                    Utility.getInstance().ScriveLog(context, NomeMaschera,"Immagini rilevate su disco inutili: OnLine");
                }
            }
        }

        VariabiliStaticheServizio.getInstance().setePartito(false);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Maschera inizializzata");

        if (VariabiliStaticheServizio.getInstance().isStaPartendo() &&
            VariabiliStaticheServizio.getInstance().isCiSonoPermessi()) {
            VariabiliStaticheServizio.getInstance().setStaPartendo(false);
            view.moveTaskToBack(false);
        }
    }

    private void ImpostaOggetti(Context context, Activity view) {
        /* RelativeLayout layDetector = (RelativeLayout) view.findViewById(R.id.layDetector);
        if (VariabiliStaticheServizio.getInstance().isDetector()) {
            layDetector.setVisibility(LinearLayout.VISIBLE);
        } else {
            layDetector.setVisibility(LinearLayout.GONE);
        } */

        ImageView imgImpostata = (ImageView) view.findViewById(R.id.imgImpostata);
        VariabiliStaticheServizio.getInstance().setImgImpostata(imgImpostata);

        TextView txtTempoAlCambio = (TextView) view.findViewById(R.id.txtTempoAlCambio);
        VariabiliStaticheServizio.getInstance().setTxtTempoAlCambio(txtTempoAlCambio);
        VariabiliStaticheServizio.getInstance().setSecondiPassati(0);
        Utility.getInstance().ScriveLog(context, NomeMaschera,"Minuti al cambio: " +
                VariabiliStaticheServizio.getInstance().getMinutiAttesa());
        Utility.getInstance().ScriveLog(context, NomeMaschera,"Tempo timer: " +
                VariabiliStaticheServizio.secondiDiAttesaContatore);
        int minuti = VariabiliStaticheServizio.getInstance().getMinutiAttesa();
        int quantiGiri = (minuti * 60) / VariabiliStaticheServizio.secondiDiAttesaContatore;
        String prossimo = "Prossimo cambio: " +
                VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                quantiGiri;
        VariabiliStaticheServizio.getInstance().getTxtTempoAlCambio().setText(prossimo);
        String immagine = "";
        if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
            immagine = VariabiliStaticheServizio.getInstance().getUltimaImmagine().getImmagine();
        }
        GestioneNotifiche.getInstance().AggiornaNotifica();

        Utility.getInstance().ScriveLog(context, NomeMaschera,"Prossimo cambio: " +
                VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                quantiGiri);

        TextView txtQuanteRicerca = (TextView) view.findViewById(R.id.txtQuanteRicerca);
        txtQuanteRicerca.setText("");
        VariabiliStaticheServizio.getInstance().setTxtQuanteRicerca(txtQuanteRicerca);

        Button btnMenoMinuti = (Button) view.findViewById(R.id.btnMenoMinuti);
        Button btnPiuMinuti = (Button) view.findViewById(R.id.btnPiuMinuti);
        TextView edtMinuti = (TextView) view.findViewById(R.id.txtMinuti);
        edtMinuti.setText(Integer.toString(minuti));
        edtMinuti.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                if (controlloLongPress == null) {
                    edtMinuti.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

                    Handler handlerTimer;
                    Runnable rTimer;

                    controlloLongPress = System.currentTimeMillis();
                    Utility.getInstance().Vibra(context, 100);

                    handlerTimer = new Handler(Looper.getMainLooper());
                    rTimer = new Runnable() {
                        public void run() {
                            controlloLongPress = null;
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 2000);
                } else {
                    long diff = System.currentTimeMillis() - controlloLongPress;

                    edtMinuti.setTextColor(ContextCompat.getColor(context, R.color.black));

                    if (diff < 1950) {
                        controlloLongPress = null;

                        boolean isD = VariabiliStaticheServizio.getInstance().isDetector();
                        isD = !isD;
                        VariabiliStaticheServizio.getInstance().setDetector(isD);

                        db_dati db = new db_dati(context);
                        db.ScriveImpostazioni();

                        // GestioneNotifiche.getInstance().AggiornaNotifica();
                        if (isD) {
                            Handler handlerTimer = new Handler(Looper.getMainLooper());
                            Runnable rTimer = new Runnable() {
                                public void run() {
                                    /* LayoutInflater inflater = (LayoutInflater.from(context));
                                    View viewDetector = inflater.inflate(R.layout.barra_notifica_detector, null);

                                    InizializzaMascheraDetector id = new InizializzaMascheraDetector();
                                    id.inizializzaMaschera(context, view, viewDetector); */
                                    Intent intent = new Intent(context, MainActivityDetector.class);
                                    context.startActivity(intent);
                                }
                            };
                            handlerTimer.postDelayed(rTimer, 1000);
                        } else {
                            // Rimuove Notifica Detector
                            GestioneNotificheDetector.getInstance().RimuoviNotifica();
                        }
                    }
                }

                return false;
            }
        });

        btnMenoMinuti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int minuti = VariabiliStaticheServizio.getInstance().getMinutiAttesa();
                if (minuti > 1) {
                    minuti--;
                } else {
                    minuti = 1;
                }
                VariabiliStaticheServizio.getInstance().setMinutiAttesa(minuti);

                int quantiGiri = (minuti * 60) / VariabiliStaticheServizio.secondiDiAttesaContatore;
                edtMinuti.setText(Integer.toString(minuti));
                String prossimo = "Prossimo cambio: " +
                        VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                        quantiGiri;
                VariabiliStaticheServizio.getInstance().getTxtTempoAlCambio().setText(prossimo);

                String immagine = "";
                if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
                    immagine = VariabiliStaticheServizio.getInstance().getUltimaImmagine().getImmagine();
                }
                GestioneNotifiche.getInstance().AggiornaNotifica();

                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();
            }
        });
        btnPiuMinuti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int minuti = VariabiliStaticheServizio.getInstance().getMinutiAttesa();
                minuti++;
                VariabiliStaticheServizio.getInstance().setMinutiAttesa(minuti);

                /* VariabiliStaticheServizio.getInstance().setQuantiGiri(
                        VariabiliStaticheServizio.getInstance().getTempoTimer() /
                        VariabiliStaticheServizio.getInstance().getSecondiAlCambio()); */
                edtMinuti.setText(Integer.toString(minuti));
                int quantiGiri = (minuti * 60) / VariabiliStaticheServizio.secondiDiAttesaContatore;
                String prossimo = "Prossimo cambio: " +
                        VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                        quantiGiri;
                VariabiliStaticheServizio.getInstance().getTxtTempoAlCambio().setText(prossimo);

                String immagine = "";
                if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
                    immagine = VariabiliStaticheServizio.getInstance().getUltimaImmagine().getImmagine();
                }
                GestioneNotifiche.getInstance().AggiornaNotifica();

                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();
            }
        });

        Switch switchHome = (Switch) view.findViewById(R.id.switchHome);
        switchHome.setChecked(VariabiliStaticheServizio.getInstance().isHome());
        switchHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheServizio.getInstance().setHome(isChecked);

                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();
            }
        });

        Switch switchLock = (Switch) view.findViewById(R.id.switchLock);
        switchLock.setChecked(VariabiliStaticheServizio.getInstance().isLock());
        switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheServizio.getInstance().setLock(isChecked);

                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();
            }
        });

        VariabiliStaticheServizio.getInstance().setLstImmagini(view.findViewById(R.id.lstImmagini));

        RelativeLayout laySceltaImm = view.findViewById(R.id.laySceltaImmagine);
        laySceltaImm.setVisibility(LinearLayout.GONE);
        VariabiliStaticheServizio.getInstance().setLayScelta(laySceltaImm);
        ImageView imgRicerca = (ImageView) view.findViewById(R.id.imgRicerca);
        imgRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheServizio.getInstance().isOffline()) {
                    AdapterListenerImmagini customAdapterT = new AdapterListenerImmagini(context,
                            VariabiliStaticheServizio.getInstance().getListaImmagini());
                    VariabiliStaticheServizio.getInstance().getLstImmagini().setAdapter(customAdapterT);
                    VariabiliStaticheServizio.getInstance().setAdapterImmagini(customAdapterT);

                    laySceltaImm.setVisibility(LinearLayout.VISIBLE);
                } else {
                    VariabiliStaticheServizio.getInstance().setAdapterImmagini(null);

                    ChiamateWS c = new ChiamateWS(context);
                    c.TornaImmagini();
                }
            }
        });

        ImageView imgUscita = (ImageView) view.findViewById(R.id.imgUscita);
        imgUscita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utility.getInstance().ChiudeApplicazione(context);
            }
        });

        EditText edtFiltro = view.findViewById(R.id.edtFiltro);

        ImageView imgRicercaScelta = (ImageView) view.findViewById(R.id.imgRicercaScelta);
        imgRicercaScelta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheServizio.getInstance().setFiltroRicerca(edtFiltro.getText().toString());
                VariabiliStaticheServizio.getInstance().getAdapterImmagini().updateData(
                        VariabiliStaticheServizio.getInstance().getFiltroRicerca());
            }
        });
        ImageView imgChiudeRicerca = (ImageView) view.findViewById(R.id.imgChiudeScelta);
        imgChiudeRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySceltaImm.setVisibility(LinearLayout.GONE);
            }
        });

        Button btnPulisceLog = (Button) view.findViewById(R.id.btnPulisceLog);
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               String path = VariabiliStaticheServizio.getInstance().getPercorsoDIRLog() + "/" +
                       VariabiliStaticheServizio.getInstance().getNomeFileDiLog();
               if (Utility.getInstance().EsisteFile(path)) {
                    Utility.getInstance().EliminaFileUnico(path);
                    // Utility.getInstance().VisualizzaMessaggio("File di log eliminato");

                   String pathLog = UtilityDetector.getInstance().PrendePathLog(context);
                   String nomeFileLog = VariabiliStaticheDetector.channelName + ".txt";
                   if (Utility.getInstance().EsisteFile(pathLog + "/" + nomeFileLog)) {
                       Utility.getInstance().EliminaFileUnico(pathLog + "/" + nomeFileLog);
                   }
                   // Toast.makeText(context, VariabiliStaticheServizio.channelName + ": File di log eliminato", Toast.LENGTH_SHORT).show();
               }

               Utility.getInstance().ApreToast(context, "File di log eliminato");
           }
       });

        Button btnInviaLog = (Button) view.findViewById(R.id.btnInviaLog);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String path1 = VariabiliStaticheServizio.getInstance().getPercorsoDIRLog() + "/" +
                        VariabiliStaticheServizio.getInstance().getNomeFileDiLog();
                String pathLog = UtilityDetector.getInstance().PrendePathLog(context);
                String nomeFileLog = VariabiliStaticheDetector.channelName + ".txt";
                String path2 = pathLog + "/" + nomeFileLog;
                String pathDest = context.getFilesDir() + "/Appoggio";
                String destFile = pathDest + "/logs.zip";
                Utility.getInstance().CreaCartelle(pathDest);
                if (Utility.getInstance().EsisteFile(destFile)) {
                    Utility.getInstance().EliminaFileUnico(destFile);
                }
                if (!Utility.getInstance().EsisteFile(path1)) {
                    Utility.getInstance().ScriveLog(context, NomeMaschera, "Invio Log");
                }
                if (!Utility.getInstance().EsisteFile(path2)) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Invio Log");
                }
                String[] Files = { path1, path2 };
                try {
                    Utility.getInstance().zip(Files, destFile);
                    if (Utility.getInstance().EsisteFile(destFile)) {
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());

                        File f = new File(destFile);
                        Uri uri = FileProvider.getUriForFile(context,
                                context.getApplicationContext().getPackageName() + ".provider",
                                f);

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT,"logs.zip");
                        i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                        i.putExtra(Intent.EXTRA_STREAM,uri);
                        i.setType(Utility.getInstance().GetMimeType(context, uri));
                        context.startActivity(Intent.createChooser(i,"Share file di log"));
                    }
                } catch (IOException e) {
                    Utility.getInstance().ApreToast(context, "Errore nell'invio dei files di log");
                }
            }
        });

        RelativeLayout layDetector = view.findViewById(R.id.layDetectorMain);
        layDetector.setVisibility(LinearLayout.GONE);

        TextView txtPath = (TextView) view.findViewById(R.id.txtPath);
        VariabiliStaticheServizio.getInstance().setTxtPath(txtPath);
        txtPath.setText(VariabiliStaticheServizio.getInstance().getPercorsoIMMAGINI());
        /* txtPath.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (VariabiliStaticheServizio.getInstance().isDetector()) {
                    final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                    if (controlloLongPress == null) {
                        txtPath.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

                        Handler handlerTimer;
                        Runnable rTimer;

                        controlloLongPress = System.currentTimeMillis();
                        Utility.getInstance().Vibra(context, 100);

                        handlerTimer = new Handler(Looper.getMainLooper());
                        rTimer = new Runnable() {
                            public void run() {
                                controlloLongPress = null;
                            }
                        };
                        handlerTimer.postDelayed(rTimer, 2000);
                    } else {
                        long diff = System.currentTimeMillis() - controlloLongPress;

                        txtPath.setTextColor(ContextCompat.getColor(context, R.color.black));

                        if (diff < 1950) {
                            controlloLongPress = null;

                            layDetector.setVisibility(LinearLayout.VISIBLE);
                        }
                    }
                }

                return false;
            }
        }); */

        Button btnCambioPath = (Button) view.findViewById(R.id.btnCambiaPath);

        btnCambioPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                view.startActivityForResult(Intent.createChooser(i, "Scelta directory"), 9999);
            }
        });

        Switch swcOffline = (Switch) view.findViewById(R.id.switchOffline);
        swcOffline.setChecked(VariabiliStaticheServizio.getInstance().isOffline());
        LinearLayout layOffline = (LinearLayout) view.findViewById(R.id.layOffline);
        if (!VariabiliStaticheServizio.getInstance().isOffline()) {
            layOffline.setVisibility(LinearLayout.GONE);
        } else {
            layOffline.setVisibility(LinearLayout.VISIBLE);
        }
        swcOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheServizio.getInstance().setOffline(isChecked);

                if (!VariabiliStaticheServizio.getInstance().isOffline()) {
                    layOffline.setVisibility(LinearLayout.GONE);
                } else {
                    layOffline.setVisibility(LinearLayout.VISIBLE);
                    if(VariabiliStaticheServizio.getInstance().isOffline()) {
                        if (VariabiliStaticheServizio.getInstance().getListaImmagini() != null &&
                                VariabiliStaticheServizio.getInstance().getListaImmagini().size() > 0) {
                            int q = VariabiliStaticheServizio.getInstance().getListaImmagini().size();
                            VariabiliStaticheServizio.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + q);
                        } else {
                            ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context);
                            bckLeggeImmaginiLocali.execute();
                        }
                    } else {
                        VariabiliStaticheServizio.getInstance().getTxtQuanteImmagini().setText(
                                "Immagini online: " + VariabiliStaticheServizio.getInstance().getImmaginiOnline());
                    }
                }

                VariabiliStaticheServizio.getInstance().setLetteImpostazioni(true);
                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();
            }
        });

        Switch swcBlur = (Switch) view.findViewById(R.id.switchBlur);
        swcBlur.setChecked(VariabiliStaticheServizio.getInstance().isBlur());
        swcBlur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheServizio.getInstance().setBlur(isChecked);

                VariabiliStaticheServizio.getInstance().setLetteImpostazioni(true);
                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();

                // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                // ChangeWallpaper c = new ChangeWallpaper(context);
                // c.setWallpaperLocale(VariabiliStaticheServizio.getInstance().getUltimaImmagine());
            }
        });

        Switch switchScriveTesto = (Switch) view.findViewById(R.id.switchScriveTesto);
        switchScriveTesto.setChecked(VariabiliStaticheServizio.getInstance().isScriveTestoSuImmagine());
        switchScriveTesto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheServizio.getInstance().setScriveTestoSuImmagine(isChecked);

                VariabiliStaticheServizio.getInstance().setLetteImpostazioni(true);
                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();

                // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                // ChangeWallpaper c = new ChangeWallpaper(context);
                // c.setWallpaperLocale(VariabiliStaticheServizio.getInstance().getUltimaImmagine());
            }
        });

        ImageView imgRefresh = (ImageView) view.findViewById(R.id.imgRefresh);
        ImageView imgRefreshLocale = (ImageView) view.findViewById(R.id.imgRefreshLocale);
        imgRefreshLocale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context);
                bckLeggeImmaginiLocali.execute();
            }
        });


        Switch swcOnOff = (Switch) view.findViewById(R.id.switchOnOff);
        swcOnOff.setChecked(VariabiliStaticheServizio.getInstance().isOnOff());
        if (VariabiliStaticheServizio.getInstance().isOnOff()) {
            btnMenoMinuti.setEnabled(true);
            btnPiuMinuti.setEnabled(true);
            // btnCambioPath.setEnabled(true);
            swcBlur.setEnabled(true);
            swcOffline.setEnabled(true);
            switchScriveTesto.setEnabled(true);
            switchHome.setEnabled(true);
            switchLock.setEnabled(true);

            if (!VariabiliStaticheServizio.getInstance().isServizioAttivo()) {
                Esecuzione e = new Esecuzione(context);
                e.startServizio1();
            }
        } else {
            btnMenoMinuti.setEnabled(false);
            btnPiuMinuti.setEnabled(false);
            // btnCambioPath.setEnabled(false);
            swcBlur.setEnabled(false);
            swcOffline.setEnabled(false);
            switchScriveTesto.setEnabled(false);
            switchHome.setEnabled(false);
            switchLock.setEnabled(false);
        }
        swcOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheServizio.getInstance().setOnOff(isChecked);

                if (VariabiliStaticheServizio.getInstance().isOnOff()) {
                    btnMenoMinuti.setEnabled(true);
                    btnPiuMinuti.setEnabled(true);
                    // btnCambioPath.setEnabled(true);
                    swcBlur.setEnabled(true);
                    swcOffline.setEnabled(true);
                    switchScriveTesto.setEnabled(true);
                    switchHome.setEnabled(true);
                    switchLock.setEnabled(true);
                } else {
                    btnMenoMinuti.setEnabled(false);
                    btnPiuMinuti.setEnabled(false);
                    // btnCambioPath.setEnabled(false);
                    swcBlur.setEnabled(false);
                    swcOffline.setEnabled(false);
                    switchScriveTesto.setEnabled(false);
                    switchHome.setEnabled(false);
                    switchLock.setEnabled(false);
                }

                VariabiliStaticheServizio.getInstance().setLetteImpostazioni(true);
                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();
            }
        });

        /* Switch swcResize = (Switch) findViewById(R.id.switchResize);
        swcResize.setChecked(VariabiliGlobali.getInstance().isResize());
        swcResize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliGlobali.getInstance().setResize(isChecked);

                db_dati db = new db_dati();
                db.ScriveImpostazioni();

                ChangeWallpaper c = new ChangeWallpaper();
                c.setWallpaper(VariabiliGlobali.getInstance().getUltimaImmagine());
            }
        }); */

        /* ImageView imgCaricamento = (ImageView) view.findViewById(R.id.imgCaricamento);
        imgCaricamento.setVisibility(LinearLayout.GONE);
        VariabiliStaticheServizio.getInstance().setImgCaricamento(imgCaricamento); */

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // imgCaricamento.setVisibility(LinearLayout.VISIBLE);
                btnMenoMinuti.setEnabled(false);
                btnPiuMinuti.setEnabled(false);
                // btnCambioPath.setEnabled(false);
                swcBlur.setEnabled(false);
                swcOffline.setEnabled(false);
                switchScriveTesto.setEnabled(false);
                imgRefresh.setVisibility(LinearLayout.GONE);
                imgRefreshLocale.setVisibility(LinearLayout.GONE);
                btnCambioPath.setEnabled(false);
                switchHome.setEnabled(false);
                switchLock.setEnabled(false);

                Runnable runTimer;
                Handler handlerTimer;

                handlerTimer = new Handler(Looper.getMainLooper());
                handlerTimer.postDelayed(runTimer = new Runnable() {
                    @Override
                    public void run() {
                        // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                        if (VariabiliStaticheServizio.getInstance().isScreenOn()) {
                            // VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                            Utility.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine Manuale---");
                            ChangeWallpaper c = new ChangeWallpaper(context);
                            if (!VariabiliStaticheServizio.getInstance().isOffline()) {
                                boolean fatto = c.setWallpaper(context, null);
                                Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente: " + fatto + "---");
                            } else {
                                int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliStaticheServizio.getInstance().getListaImmagini().size() - 1);
                                if (numeroRandom > -1) {
                                    boolean fatto = c.setWallpaper(context, VariabiliStaticheServizio.getInstance().getListaImmagini().get(numeroRandom));
                                    Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente: " + fatto + "---");
                                } else {
                                    Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine NON cambiata manualmente: Caricamento immagini in corso---");
                                }
                            }
                            // } else {
                            //     Log.getInstance().ScriveLog("---Cambio Immagine posticipato per schermo spento---");
                            // VariabiliGlobali.getInstance().setImmagineDaCambiare(true);
                        }

                        btnMenoMinuti.setEnabled(true);
                        btnPiuMinuti.setEnabled(true);
                        // btnCambioPath.setEnabled(true);
                        swcBlur.setEnabled(true);
                        swcOffline.setEnabled(true);
                        switchScriveTesto.setEnabled(true);
                        imgRefresh.setVisibility(LinearLayout.VISIBLE);
                        imgRefreshLocale.setVisibility(LinearLayout.VISIBLE);
                        btnCambioPath.setEnabled(true);
                        // imgCaricamento.setVisibility(LinearLayout.GONE);
                        switchHome.setEnabled(true);
                        switchLock.setEnabled(true);
                    }
                }, 500);
            }
        });

        if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
            Bitmap ultima = BitmapFactory.decodeFile(VariabiliStaticheServizio.getInstance().getUltimaImmagine().getPathImmagine());
            imgImpostata.setImageBitmap(ultima);
        }
    }
}
