package com.looigi.wallpaperchanger2.classiAttivitaWallpaper;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.MainActivityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiStandard.Esecuzione;
import com.looigi.wallpaperchanger2.classiStandard.GestioneNotifiche;
import com.looigi.wallpaperchanger2.classiStandard.RichiestaPathImmaginiLocali;
import com.looigi.wallpaperchanger2.gps.GestioneGPS;
import com.looigi.wallpaperchanger2.gps.GestioneMappa;
import com.looigi.wallpaperchanger2.gps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;
import com.looigi.wallpaperchanger2.webservice.ChiamateWS;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InizializzaMascheraWallpaper {
    private static final String NomeMaschera = "INITMASCHERA";
    private Long controlloLongPress = null;

    public void inizializzaMaschera(Context context, Activity view) {
        UtilityWallpaper.getInstance().Attesa(true);

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                inizializzaMaschera2(context, view);
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);
    }

    private void inizializzaMaschera2(Context context, Activity view) {
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Inizializzo maschera");

        VariabiliStaticheWallpaper.getInstance().setLayAttesa(view.findViewById(R.id.layAttesa));
        UtilityWallpaper.getInstance().Attesa(false);

        TextView txtQuante = (TextView) view.findViewById(R.id.txtQuanteImmagini);
        VariabiliStaticheWallpaper.getInstance().setTxtQuanteImmagini(txtQuante);

        if (!VariabiliStaticheWallpaper.getInstance().isLetteImpostazioni()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Mancanza di impostazioni");
            UtilityWallpaper.getInstance().ApreToast(context, VariabiliStaticheWallpaper.channelName + ": Mancanza di impostazioni");
        }

        ImpostaOggetti(context, view);

        if (!VariabiliStaticheWallpaper.getInstance().isePartito()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Carico immagini in locale");
            db_dati_wallpaper db = new db_dati_wallpaper(context);
            boolean letteImmagini = db.CaricaImmaginiInLocale();

            if (!letteImmagini) {
                // if (VariabiliGlobali.getInstance().isOffline()) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Immagini in locale non rilevate... Scanno...");
                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context);
                bckLeggeImmaginiLocali.execute();
                // }
            } else {
                if (VariabiliStaticheWallpaper.getInstance().isOffline()) {
                    int q = VariabiliStaticheWallpaper.getInstance().getListaImmagini().size();
                    VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + q);
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Immagini rilevate su disco: " + q);
                } else {
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Immagini rilevate su disco inutili: OnLine");
                }
            }
        }

        VariabiliStaticheWallpaper.getInstance().setePartito(false);

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Maschera inizializzata");

        if (VariabiliStaticheWallpaper.getInstance().isStaPartendo() &&
            VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {
            VariabiliStaticheWallpaper.getInstance().setStaPartendo(false);
            VariabiliStaticheWallpaper.getInstance().ChiudeActivity(false);
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
        VariabiliStaticheWallpaper.getInstance().setImgImpostata(imgImpostata);

        TextView txtTempoAlCambio = (TextView) view.findViewById(R.id.txtTempoAlCambio);
        VariabiliStaticheWallpaper.getInstance().setTxtTempoAlCambio(txtTempoAlCambio);
        VariabiliStaticheWallpaper.getInstance().setSecondiPassati(0);
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Minuti al cambio: " +
                VariabiliStaticheWallpaper.getInstance().getMinutiAttesa());
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Tempo timer: " +
                VariabiliStaticheWallpaper.secondiDiAttesaContatore);
        int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();
        int quantiGiri = (minuti * 60) / VariabiliStaticheWallpaper.secondiDiAttesaContatore;
        String prossimo = "Prossimo cambio: " +
                VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
                quantiGiri;
        VariabiliStaticheWallpaper.getInstance().getTxtTempoAlCambio().setText(prossimo);
        String immagine = "";
        if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
            immagine = VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getImmagine();
        }
        GestioneNotifiche.getInstance().AggiornaNotifica();

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Prossimo cambio: " +
                VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
                quantiGiri);

        TextView txtQuanteRicerca = (TextView) view.findViewById(R.id.txtQuanteRicerca);
        txtQuanteRicerca.setText("");
        VariabiliStaticheWallpaper.getInstance().setTxtQuanteRicerca(txtQuanteRicerca);

        TextView txtDetector = view.findViewById(R.id.txtDetector);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            txtDetector.setVisibility(LinearLayout.VISIBLE);
        } else {
            txtDetector.setVisibility(LinearLayout.GONE);
        }
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
                    UtilityWallpaper.getInstance().Vibra(context, 100);

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

                        boolean isD = VariabiliStaticheStart.getInstance().isDetector();
                        isD = !isD;
                        VariabiliStaticheStart.getInstance().setDetector(isD);

                        db_dati_wallpaper db = new db_dati_wallpaper(context);
                        db.ScriveImpostazioni();

                        // GestioneNotifiche.getInstance().AggiornaNotifica();
                        if (isD) {
                            Handler handlerTimer = new Handler(Looper.getMainLooper());
                            Runnable rTimer = new Runnable() {
                                public void run() {
                                    txtDetector.setVisibility(LinearLayout.VISIBLE);

                                    GestioneGPS g = new GestioneGPS();
                                    VariabiliStaticheGPS.getInstance().setGestioneGPS(g);
                                    g.AbilitaTimer(context);
                                    g.AbilitaGPS(context);

                                    GestioneMappa m = new GestioneMappa(context);
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                                    String dataOdierna = sdfD.format(calendar.getTime());
                                    m.LeggePunti(dataOdierna);
                                    VariabiliStaticheGPS.getInstance().setMappa(m);

                                    /* LayoutInflater inflater = (LayoutInflater.from(context));
                                    View viewDetector = inflater.inflate(R.layout.barra_notifica_detector, null);

                                    InizializzaMascheraDetector id = new InizializzaMascheraDetector();
                                    id.inizializzaMaschera(context, view, viewDetector); */

                                    VariabiliStaticheDetector.getInstance().setMascheraPartita(false);

                                    Intent intent = new Intent(context, MainActivityDetector.class);
                                    context.startActivity(intent);

                                    Notification notificaDetector = GestioneNotificheDetector.getInstance().StartNotifica(context);
                                    if (notificaDetector != null) {
                                        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");

                                        UtilityDetector.getInstance().ContaFiles(context);

                                        UtilityWallpaper.getInstance().ApreToast(context, "Detector Partito");
                                    }
                                }
                            };
                            handlerTimer.postDelayed(rTimer, 1000);
                        } else {
                            // Rimuove Notifica Detector
                            txtDetector.setVisibility(LinearLayout.GONE);

                            GestioneNotificheDetector.getInstance().RimuoviNotifica();

                            VariabiliStaticheGPS.getInstance().getMappa().ChiudeMaschera();
                            VariabiliStaticheGPS.getInstance().setMappa(null);

                            VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS();
                            VariabiliStaticheGPS.getInstance().getGestioneGPS().ChiudeMaschera();
                            VariabiliStaticheGPS.getInstance().setGestioneGPS(null);
                        }
                    }
                }

                return false;
            }
        });

        btnMenoMinuti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();
                if (minuti > 1) {
                    minuti--;
                } else {
                    minuti = 1;
                }
                VariabiliStaticheWallpaper.getInstance().setMinutiAttesa(minuti);

                int quantiGiri = (minuti * 60) / VariabiliStaticheWallpaper.secondiDiAttesaContatore;
                edtMinuti.setText(Integer.toString(minuti));
                String prossimo = "Prossimo cambio: " +
                        VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
                        quantiGiri;
                VariabiliStaticheWallpaper.getInstance().getTxtTempoAlCambio().setText(prossimo);

                String immagine = "";
                if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
                    immagine = VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getImmagine();
                }
                GestioneNotifiche.getInstance().AggiornaNotifica();

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });
        btnPiuMinuti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();
                minuti++;
                VariabiliStaticheWallpaper.getInstance().setMinutiAttesa(minuti);

                /* VariabiliStaticheServizio.getInstance().setQuantiGiri(
                        VariabiliStaticheServizio.getInstance().getTempoTimer() /
                        VariabiliStaticheServizio.getInstance().getSecondiAlCambio()); */
                edtMinuti.setText(Integer.toString(minuti));
                int quantiGiri = (minuti * 60) / VariabiliStaticheWallpaper.secondiDiAttesaContatore;
                String prossimo = "Prossimo cambio: " +
                        VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
                        quantiGiri;
                VariabiliStaticheWallpaper.getInstance().getTxtTempoAlCambio().setText(prossimo);

                String immagine = "";
                if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
                    immagine = VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getImmagine();
                }
                GestioneNotifiche.getInstance().AggiornaNotifica();

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        SwitchCompat switchHome = (SwitchCompat) view.findViewById(R.id.switchHome);
        switchHome.setChecked(VariabiliStaticheWallpaper.getInstance().isHome());
        switchHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setHome(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        SwitchCompat switchLock = (SwitchCompat) view.findViewById(R.id.switchLock);
        switchLock.setChecked(VariabiliStaticheWallpaper.getInstance().isLock());
        switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setLock(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        VariabiliStaticheWallpaper.getInstance().setLstImmagini(view.findViewById(R.id.lstImmagini));

        RelativeLayout laySceltaImm = view.findViewById(R.id.laySceltaImmagine);
        laySceltaImm.setVisibility(LinearLayout.GONE);
        VariabiliStaticheWallpaper.getInstance().setLayScelta(laySceltaImm);
        ImageView imgRicerca = (ImageView) view.findViewById(R.id.imgRicerca);
        imgRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheWallpaper.getInstance().isOffline()) {
                    AdapterListenerImmagini customAdapterT = new AdapterListenerImmagini(context,
                            VariabiliStaticheWallpaper.getInstance().getListaImmagini());
                    VariabiliStaticheWallpaper.getInstance().getLstImmagini().setAdapter(customAdapterT);
                    VariabiliStaticheWallpaper.getInstance().setAdapterImmagini(customAdapterT);

                    laySceltaImm.setVisibility(LinearLayout.VISIBLE);
                } else {
                    VariabiliStaticheWallpaper.getInstance().setAdapterImmagini(null);

                    ChiamateWS c = new ChiamateWS(context);
                    c.TornaImmagini();
                }
            }
        });

        ImageView imgUscita = (ImageView) view.findViewById(R.id.imgUscita);
        imgUscita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityWallpaper.getInstance().ChiudeApplicazione(context);
            }
        });

        EditText edtFiltro = view.findViewById(R.id.edtFiltro);

        ImageView imgRicercaScelta = (ImageView) view.findViewById(R.id.imgRicercaScelta);
        imgRicercaScelta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheWallpaper.getInstance().setFiltroRicerca(edtFiltro.getText().toString());
                VariabiliStaticheWallpaper.getInstance().getAdapterImmagini().updateData(
                        VariabiliStaticheWallpaper.getInstance().getFiltroRicerca());
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
               UtilityWallpaper.getInstance().EliminaLogs(context);
           }
       });

        Button btnInviaLog = (Button) view.findViewById(R.id.btnInviaLog);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityWallpaper.getInstance().CondividiLogs(context);
                /* String path1 = VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                        VariabiliStaticheWallpaper.getInstance().getNomeFileDiLog();
                String pathLog = UtilityDetector.getInstance().PrendePathLog(context);
                String nomeFileLog = VariabiliStaticheDetector.channelName + ".txt";
                String path2 = pathLog + "/" + nomeFileLog;
                String pathDest = context.getFilesDir() + "/Appoggio";
                String destFile = pathDest + "/logs.zip";
                UtilityWallpaper.getInstance().CreaCartelle(pathDest);
                if (UtilityWallpaper.getInstance().EsisteFile(destFile)) {
                    UtilityWallpaper.getInstance().EliminaFileUnico(destFile);
                }
                if (!UtilityWallpaper.getInstance().EsisteFile(path1)) {
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Invio Log");
                }
                if (!UtilityWallpaper.getInstance().EsisteFile(path2)) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Invio Log");
                }
                String[] Files = { path1, path2 };
                try {
                    UtilityWallpaper.getInstance().zip(Files, destFile);
                    if (UtilityWallpaper.getInstance().EsisteFile(destFile)) {
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
                        i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
                        context.startActivity(Intent.createChooser(i,"Share file di log"));
                    }
                } catch (IOException e) {
                    UtilityWallpaper.getInstance().ApreToast(context, "Errore nell'invio dei files di log");
                } */
            }
        });

        // RelativeLayout layDetector = view.findViewById(R.id.layDetectorMain);
        // layDetector.setVisibility(LinearLayout.GONE);

        ImageView imgImpoWP = view.findViewById(R.id.imgImpoWP);
        RelativeLayout layImpoWP = view.findViewById(R.id.layImpostazioniWP);
        layImpoWP.setVisibility(LinearLayout.GONE);
        imgImpoWP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layImpoWP.setVisibility(LinearLayout.VISIBLE);
            }
        });
        ImageView imgChiudeImpoWP = view.findViewById(R.id.imgChiudeImpoWP);
        imgChiudeImpoWP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layImpoWP.setVisibility(LinearLayout.GONE);
            }
        });

        TextView txtPath = (TextView) view.findViewById(R.id.txtPath);
        VariabiliStaticheWallpaper.getInstance().setTxtPath(txtPath);
        txtPath.setText(VariabiliStaticheWallpaper.getInstance().getPercorsoIMMAGINI());
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
                /* Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                view.startActivityForResult(Intent.createChooser(i, "Scelta directory"), 9999); */
                Intent myIntent = new Intent(
                        VariabiliStaticheWallpaper.getInstance().getMainActivity(),
                        RichiestaPathImmaginiLocali.class);
                VariabiliStaticheWallpaper.getInstance().getMainActivity().startActivity(myIntent);
            }
        });

        SwitchCompat swcOffline = (SwitchCompat) view.findViewById(R.id.switchOffline);
        swcOffline.setChecked(VariabiliStaticheWallpaper.getInstance().isOffline());
        LinearLayout layOffline = (LinearLayout) view.findViewById(R.id.layOffline);
        if (!VariabiliStaticheWallpaper.getInstance().isOffline()) {
            layOffline.setVisibility(LinearLayout.GONE);
        } else {
            layOffline.setVisibility(LinearLayout.VISIBLE);
        }
        swcOffline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setOffline(isChecked);

                if (!VariabiliStaticheWallpaper.getInstance().isOffline()) {
                    layOffline.setVisibility(LinearLayout.GONE);
                } else {
                    layOffline.setVisibility(LinearLayout.VISIBLE);
                    if(VariabiliStaticheWallpaper.getInstance().isOffline()) {
                        if (VariabiliStaticheWallpaper.getInstance().getListaImmagini() != null &&
                                VariabiliStaticheWallpaper.getInstance().getListaImmagini().size() > 0) {
                            int q = VariabiliStaticheWallpaper.getInstance().getListaImmagini().size();
                            VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + q);
                        } else {
                            ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context);
                            bckLeggeImmaginiLocali.execute();
                        }
                    } else {
                        VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText(
                                "Immagini online: " + VariabiliStaticheWallpaper.getInstance().getImmaginiOnline());
                    }
                }

                VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(true);
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        SwitchCompat swcBlur = (SwitchCompat) view.findViewById(R.id.switchBlur);
        swcBlur.setChecked(VariabiliStaticheWallpaper.getInstance().isBlur());
        swcBlur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setBlur(isChecked);

                VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(true);
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();

                // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                // ChangeWallpaper c = new ChangeWallpaper(context);
                // c.setWallpaperLocale(VariabiliStaticheServizio.getInstance().getUltimaImmagine());
            }
        });

        SwitchCompat switchScriveTesto = view.findViewById(R.id.switchScriveTesto);
        switchScriveTesto.setChecked(VariabiliStaticheWallpaper.getInstance().isScriveTestoSuImmagine());
        switchScriveTesto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setScriveTestoSuImmagine(isChecked);

                VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(true);
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();

                // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                // ChangeWallpaper c = new ChangeWallpaper(context);
                // c.setWallpaperLocale(VariabiliStaticheServizio.getInstance().getUltimaImmagine());
            }
        });

        ImageView imgCambiaSubito = (ImageView) view.findViewById(R.id.imgCambiaSubito);
        ImageView imgRefresh = (ImageView) view.findViewById(R.id.imgRefresh);
        ImageView imgRefreshLocale = (ImageView) view.findViewById(R.id.imgRefreshLocale);
        imgRefreshLocale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context);
                bckLeggeImmaginiLocali.execute();
            }
        });

        SwitchCompat swcEspansa = (SwitchCompat) view.findViewById(R.id.switchEspansa);
        SwitchCompat swcSoloVolti = view.findViewById(R.id.switchSoloVolto);
        SwitchCompat swcEffetti = (SwitchCompat) view.findViewById(R.id.switchEffetti);
        swcEffetti.setChecked(VariabiliStaticheWallpaper.getInstance().isEffetti());
        swcEffetti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setEffetti(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        SwitchCompat swcOnOff = (SwitchCompat) view.findViewById(R.id.switchOnOff);
        swcOnOff.setChecked(VariabiliStaticheWallpaper.getInstance().isOnOff());
        if (VariabiliStaticheWallpaper.getInstance().isOnOff()) {
            swcOnOff.setText("ON");
            /* btnMenoMinuti.setEnabled(true);
            btnPiuMinuti.setEnabled(true);
            // btnCambioPath.setEnabled(true);
            swcBlur.setEnabled(true);
            swcEspansa.setEnabled(true);
            swcSoloVolti.setEnabled(true);
            swcOffline.setEnabled(true);
            switchScriveTesto.setEnabled(true);
            switchHome.setEnabled(true);
            switchLock.setEnabled(true);

            if (!VariabiliStaticheWallpaper.getInstance().isServizioAttivo()) {
                Esecuzione e = new Esecuzione(context);
                e.startServizio1();
            } */
        } else {
            swcOnOff.setText("OFF");
            /* btnMenoMinuti.setEnabled(false);
            btnPiuMinuti.setEnabled(false);
            // btnCambioPath.setEnabled(false);
            swcBlur.setEnabled(false);
            swcEspansa.setEnabled(false);
            swcSoloVolti.setEnabled(false);
            swcOffline.setEnabled(false);
            switchScriveTesto.setEnabled(false);
            switchHome.setEnabled(false);
            switchLock.setEnabled(false); */
        }
        swcOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setOnOff(isChecked);

                if (VariabiliStaticheWallpaper.getInstance().isOnOff()) {
                    swcOnOff.setText("ON");
                    /* btnMenoMinuti.setEnabled(true);
                    btnPiuMinuti.setEnabled(true);
                    // btnCambioPath.setEnabled(true);
                    swcBlur.setEnabled(true);
                    swcEspansa.setEnabled(true);
                    swcOffline.setEnabled(true);
                    switchScriveTesto.setEnabled(true);
                    switchHome.setEnabled(true);
                    switchLock.setEnabled(true); */
                } else {
                    swcOnOff.setText("OFF");
                    /* btnMenoMinuti.setEnabled(false);
                    btnPiuMinuti.setEnabled(false);
                    // btnCambioPath.setEnabled(false);
                    swcBlur.setEnabled(false);
                    swcEspansa.setEnabled(false);
                    swcOffline.setEnabled(false);
                    switchScriveTesto.setEnabled(false);
                    switchHome.setEnabled(false);
                    switchLock.setEnabled(false); */
                }

                VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(true);
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        /* SwitchCompat swcResize = (SwitchCompat) findViewById(R.id.switchResize);
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
                UtilityWallpaper.getInstance().Attesa(true);
                ChangeWallpaper c = new ChangeWallpaper(context);
                c.setWallpaperLocale(context,
                        VariabiliStaticheWallpaper.getInstance().getUltimaImmagine());
                UtilityWallpaper.getInstance().Attesa(false);
            }
        });

        swcSoloVolti.setChecked(VariabiliStaticheWallpaper.getInstance().isSoloVolti());
        swcSoloVolti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setSoloVolti(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        swcEspansa.setChecked(VariabiliStaticheWallpaper.getInstance().isEspansa());
        if (VariabiliStaticheWallpaper.getInstance().isEspansa()) {
            swcSoloVolti.setVisibility(LinearLayout.VISIBLE);
        } else {
            swcSoloVolti.setVisibility(LinearLayout.GONE);
        }
        swcEspansa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setEspansa(isChecked);
                if (isChecked) {
                    swcSoloVolti.setVisibility(LinearLayout.VISIBLE);
                } else {
                    swcSoloVolti.setVisibility(LinearLayout.GONE);
                }
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        imgCambiaSubito.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityWallpaper.getInstance().Attesa(true);

                // imgCaricamento.setVisibility(LinearLayout.VISIBLE);
                btnMenoMinuti.setEnabled(false);
                btnPiuMinuti.setEnabled(false);
                // btnCambioPath.setEnabled(false);
                swcBlur.setEnabled(false);
                swcEspansa.setEnabled(false);
                swcOffline.setEnabled(false);
                switchScriveTesto.setEnabled(false);
                imgCambiaSubito.setVisibility(LinearLayout.GONE);
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
                        UtilityWallpaper.getInstance().CambiaImmagine(context);

                        /* if (VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
                            // VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine Manuale---");
                            ChangeWallpaper c = new ChangeWallpaper(context);
                            if (!VariabiliStaticheWallpaper.getInstance().isOffline()) {
                                c.setWallpaper(context, null);
                                // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente");
                            } else {
                                int numeroRandom = UtilityWallpaper.getInstance().GeneraNumeroRandom(VariabiliStaticheWallpaper.getInstance().getListaImmagini().size() - 1);
                                if (numeroRandom > -1) {
                                    c.setWallpaper(context, VariabiliStaticheWallpaper.getInstance().getListaImmagini().get(numeroRandom));
                                    // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente");
                                } else {
                                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine NON cambiata manualmente: Caricamento immagini in corso---");
                                }
                            }
                            // } else {
                            //     Log.getInstance().ScriveLog("---Cambio Immagine posticipato per schermo spento---");
                            // VariabiliGlobali.getInstance().setImmagineDaCambiare(true);
                        } */

                        btnMenoMinuti.setEnabled(true);
                        btnPiuMinuti.setEnabled(true);
                        // btnCambioPath.setEnabled(true);
                        swcBlur.setEnabled(true);
                        swcEspansa.setEnabled(true);
                        swcOffline.setEnabled(true);
                        switchScriveTesto.setEnabled(true);
                        imgCambiaSubito.setVisibility(LinearLayout.VISIBLE);
                        imgRefreshLocale.setVisibility(LinearLayout.VISIBLE);
                        btnCambioPath.setEnabled(true);
                        // imgCaricamento.setVisibility(LinearLayout.GONE);
                        switchHome.setEnabled(true);
                        switchLock.setEnabled(true);

                        UtilityWallpaper.getInstance().Attesa(false);
                    }
                }, 500);
            }
        });

        if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
            Bitmap ultima = BitmapFactory.decodeFile(VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getPathImmagine());
            imgImpostata.setImageBitmap(ultima);
        }
    }
}
