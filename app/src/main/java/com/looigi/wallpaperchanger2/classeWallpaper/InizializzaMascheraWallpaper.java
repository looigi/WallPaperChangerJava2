package com.looigi.wallpaperchanger2.classeWallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeModificaImmagine.MainModificaImmagine;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.VariabiliStaticheRefresh;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.ChiamateWsWP;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class InizializzaMascheraWallpaper {
    private static final String NomeMaschera = "Init_Maschera_Wallpaper";
    private Long controlloLongPress = null;

    public void inizializzaMaschera(Context context, Activity view) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                inizializzaMaschera2(context, view);
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);
    }

    private void inizializzaMaschera2(Context context, Activity view) {
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                "Inizializzo maschera");

        VariabiliStaticheWallpaper.getInstance().setLayAttesa(view.findViewById(R.id.layAttesa));
        // UtilityWallpaper.getInstance().Attesa(false);

        TextView txtQuante = (TextView) view.findViewById(R.id.txtQuanteImmagini);
        VariabiliStaticheWallpaper.getInstance().setTxtQuanteImmagini(txtQuante);

        /* if (!VariabiliStaticheWallpaper.getInstance().isLetteImpostazioni()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Mancanza di impostazioni");
            UtilitiesGlobali.getInstance().ApreToast(context, VariabiliStaticheWallpaper.channelName + ": Mancanza di impostazioni");
        } */

        ImpostaOggetti(context, view);

        if (!VariabiliStaticheWallpaper.getInstance().isePartito()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Carico immagini in locale");

            db_dati_wallpaper db = new db_dati_wallpaper(context);
            boolean letteImmagini = db.CaricaImmaginiInLocale();

            if (!letteImmagini) {
                // if (VariabiliGlobali.getInstance().isOffline()) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                        "Immagini in locale non rilevate... Scanno...");
                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context, null);
                bckLeggeImmaginiLocali.execute();
                // }
            } else {
                switch (VariabiliStaticheWallpaper.getInstance().getModoRicercaImmagine()) {
                    case 0:
                        VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Immagini online");
                        break;
                    case 1:
                        int q = VariabiliStaticheWallpaper.getInstance().getListaImmagini().size();
                        VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText(
                                "Immagini rilevate su disco: " + q
                        );
                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                                "Immagini rilevate su disco: " + q);
                        break;
                    case 2:
                        VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Immagini da immagini");
                        break;
                }
            }
            db.ChiudeDB();
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
        ImageView imgImpostataFinale = (ImageView) view.findViewById(R.id.imgImpostataFinale);
        VariabiliStaticheWallpaper.getInstance().setImgImpostataFinale(imgImpostataFinale);

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
        GestioneNotificheWP.getInstance().AggiornaNotifica();

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Prossimo cambio: " +
                VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
                quantiGiri);

        TextView txtQuanteRicerca = (TextView) view.findViewById(R.id.txtQuanteRicerca);
        txtQuanteRicerca.setText("");
        VariabiliStaticheWallpaper.getInstance().setTxtQuanteRicerca(txtQuanteRicerca);

        VariabiliStaticheWallpaper.getInstance().setLstImmagini(view.findViewById(R.id.lstImmagini));

        RelativeLayout laySceltaImm = view.findViewById(R.id.laySceltaImmagine);
        laySceltaImm.setVisibility(LinearLayout.GONE);
        VariabiliStaticheWallpaper.getInstance().setLayScelta(laySceltaImm);
        ImageView imgRicerca = (ImageView) view.findViewById(R.id.imgRicerca);
        imgRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityWallpaper.getInstance().ApreRicerca(context);
            }
        });

        /* if (VariabiliStaticheWallpaper.getInstance().isApreRicerca()) {
            VariabiliStaticheWallpaper.getInstance().setApreRicerca(false);
            UtilityWallpaper.getInstance().ApreRicerca(context);
        }

        ImageView imgUscita = (ImageView) view.findViewById(R.id.imgUscita);
        imgUscita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityWallpaper.getInstance().ChiudeApplicazione(context);
            }
        }); */

        ImageView imgSettings = (ImageView) view.findViewById(R.id.imgSettings);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(context, MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "WALLPAPER");
                                iP.putExtras(b);
                                context.startActivity(iP);
                            }
                        }, 500);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });

        EditText edtFiltro = view.findViewById(R.id.edtFiltro);

        ImageView imgRefreshScelta = (ImageView) view.findViewById(R.id.imgRefreshScelta);
        imgRefreshScelta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWsWP ws = new ChiamateWsWP(context);
                ws.TornaImmagini(true);
            }
        });

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

        // RelativeLayout layDetector = view.findViewById(R.id.layDetectorMain);
        // layDetector.setVisibility(LinearLayout.GONE);

        /* ImageView imgImpoWP = view.findViewById(R.id.imgImpoWP);
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
        }); */

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

        VariabiliStaticheWallpaper.getInstance().setTxtAvanzamentoRefresh(view.findViewById(R.id.txtAvanzamentoRefresh));
        VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);

        /* ImageView imgRefreshImmagini = (ImageView) view.findViewById(R.id.imgRefreshIONOS);
        imgRefreshImmagini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Tipo aggiornamento");
                builder.setPositiveButton("Solo Locale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EsegueAggiornamento(context, 1);
                    }
                });
                builder.setNegativeButton("Solo IoNos", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EsegueAggiornamento(context, 2);
                    }
                });
                /* builder.setNeutralButton("Entrambi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EsegueAggiornamento(context, 3);
                    }
                }); * /

                builder.show();
            }
        }); */

        ImageView imgCondividi = (ImageView) view.findViewById(R.id.imgCondividi);
        imgCondividi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Datella = UtilitiesGlobali.getInstance().TornaNomeFileConData();
                String SoloNome = "Wallpaper_" + Datella + ".zip";
                String NomeFileZip = context.getFilesDir() + "/Appoggio/" + SoloNome;
                Files.getInstance().CreaCartelle(context.getFilesDir() + "/Appoggio");

                String[] NomeFile = { context.getFilesDir() + "/Download/Appoggio.jpg",
                        context.getFilesDir() + "/Download/AppoggioImpostato.jpg"};

                int BUFFER_SIZE = 1024;
                byte data[] = new byte[BUFFER_SIZE];

                BufferedInputStream origin = null;
                try {
                    ZipOutputStream out = new ZipOutputStream(
                            new BufferedOutputStream(
                                    new FileOutputStream(NomeFileZip)));
                    for (String filetto : NomeFile) {
                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Zip files: " + filetto);

                        File f = new File(filetto);
                        if (f.exists()) {
                            FileInputStream fi = new FileInputStream(f);
                            origin = new BufferedInputStream(fi, BUFFER_SIZE);
                            try {
                                ZipEntry entry = new ZipEntry(f.getAbsoluteFile().getName());
                                out.putNextEntry(entry);
                                int count;
                                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                                    out.write(data, 0, count);
                                }
                            } catch (IOException e) {
                                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                                        "Errore su zip files 1: " +
                                                UtilityDetector.getInstance().PrendeErroreDaException(e));
                            } finally {
                                origin.close();
                            }
                        } else {
                            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"NON esistente");
                        }
                    }
                    out.close();

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(NomeFileZip);
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, SoloNome);
                    i.putExtra(Intent.EXTRA_TEXT, "Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i, "Share wallpaper"));

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            Files.getInstance().EliminaFileUnico(NomeFileZip);
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 10000);
                } catch (IOException e) {
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                            "Errore su zip files 2: " +
                                    UtilityDetector.getInstance().PrendeErroreDaException(e));
                }
            }
        });

        ImageView imgCambiaSubito = (ImageView) view.findViewById(R.id.imgCambiaSubito);
        ImageView imgRefresh = (ImageView) view.findViewById(R.id.imgRefresh);

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

                // VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(true);
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
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

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Path = "";

                switch (VariabiliStaticheWallpaper.getInstance().getImmagineImpostataDaChi()) {
                    case "WALLPAPER":
                    case "":
                        Path = context.getFilesDir() + "/Download/Appoggio.jpg";
                        break;
                    case "PLAYER":
                        Path = context.getFilesDir() + "/Download/AppoggioPLA.jpg";
                        break;
                    case "PENNETTA":
                        Path = context.getFilesDir() + "/Download/AppoggioPEN.jpg";
                        break;
                    case "FETEKKIE":
                        Path = context.getFilesDir() + "/Download/AppoggioFET.jpg";
                        break;
                    case "IMMAGINI":
                        Path = context.getFilesDir() + "/Download/AppoggioMI.jpg";
                        break;
                }

                VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("WALLPAPER");
                VariabiliStaticheModificaImmagine.getInstance().setNomeImmagine(
                        Path
                );
                Intent i = new Intent(context, MainModificaImmagine.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheWallpaper.getInstance().setSecondiPassati(0);

                UtilityWallpaper.getInstance().Attesa(true);
                ChangeWallpaper c = new ChangeWallpaper(context, "WALLPAPER",
                        VariabiliStaticheWallpaper.getInstance().getUltimaImmagine());
                c.setWallpaperLocale(context,
                        VariabiliStaticheWallpaper.getInstance().getUltimaImmagine());
                UtilityWallpaper.getInstance().Attesa(false);
            }
        });

        imgCambiaSubito.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityWallpaper.getInstance().Attesa(true);

                // imgCaricamento.setVisibility(LinearLayout.VISIBLE);
                // btnMenoMinuti.setEnabled(false);
                // btnPiuMinuti.setEnabled(false);
                // btnCambioPath.setEnabled(false);
                // swcBlur.setEnabled(false);
                // swcEspansa.setEnabled(false);
                // swcOffline.setEnabled(false);
                // switchScriveTesto.setEnabled(false);
                imgCambiaSubito.setVisibility(LinearLayout.GONE);
                // imgRefreshLocale.setVisibility(LinearLayout.GONE);
                // btnCambioPath.setEnabled(false);
                // switchHome.setEnabled(false);
                // switchLock.setEnabled(false);

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

                        // btnMenoMinuti.setEnabled(true);
                        // btnPiuMinuti.setEnabled(true);
                        // btnCambioPath.setEnabled(true);
                        // swcBlur.setEnabled(true);
                        // swcEspansa.setEnabled(true);
                        // swcOffline.setEnabled(true);
                        // switchScriveTesto.setEnabled(true);
                        imgCambiaSubito.setVisibility(LinearLayout.VISIBLE);
                        // imgRefreshLocale.setVisibility(LinearLayout.VISIBLE);
                        // btnCambioPath.setEnabled(true);
                        // imgCaricamento.setVisibility(LinearLayout.GONE);
                        // switchHome.setEnabled(true);
                        // switchLock.setEnabled(true);

                        UtilityWallpaper.getInstance().Attesa(false);
                    }
                }, 500);
            }
        });

        if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
            Bitmap ultima = BitmapFactory.decodeFile(VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getPathImmagine());
            imgImpostata.setImageBitmap(ultima);

            String path1 = context.getFilesDir() + "/Download/AppoggioImpostato.jpg";
            if (UtilityWallpaper.getInstance().EsisteFile(path1)) {
                Bitmap ultimaFinale = BitmapFactory.decodeFile(path1);
                VariabiliStaticheWallpaper.getInstance().getImgImpostataFinale().setImageBitmap(ultimaFinale);
            }
        }
    }

    private void EsegueAggiornamento(Context context, int modalita) {
        switch (modalita) {
            case 1:
                VariabiliStaticheRefresh.getInstance().setAggiornaSoloLocale(true);
                VariabiliStaticheRefresh.getInstance().setAggiornaSoloIoNos(false);
                break;
            case 2:
                VariabiliStaticheRefresh.getInstance().setAggiornaSoloLocale(false);
                VariabiliStaticheRefresh.getInstance().setAggiornaSoloIoNos(true);
                break;
            case 3:
                VariabiliStaticheRefresh.getInstance().setAggiornaSoloLocale(true);
                VariabiliStaticheRefresh.getInstance().setAggiornaSoloIoNos(true);
                break;
        }
        VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(
                LinearLayout.VISIBLE
        );
        VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                "Lettura immagini IoNOS"
        );

        ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
        ws.RitornaListaImmaginiSfondiIoNOSPerRefresh();
    }
}
