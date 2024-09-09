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
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivita.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classiAttivita.ScannaDiscoPerImmaginiLocali;
import com.looigi.wallpaperchanger2.classiAttivita.db_dati;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import java.io.File;

import static androidx.core.content.ContextCompat.startActivity;

public class InizializzaMaschera {
    private static final String NomeMaschera = "INITMASCHERA";

    public void inizializzaMaschera(Context context, Activity view) {
        Utility.getInstance().ScriveLog(context, NomeMaschera, "Inizializzo maschera");

        TextView txtQuante = (TextView) view.findViewById(R.id.txtQuanteImmagini);
        VariabiliStaticheServizio.getInstance().setTxtQuanteImmagini(txtQuante);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Apro db");
        db_dati db = new db_dati(context);
        Utility.getInstance().ScriveLog(context, NomeMaschera,"Creo tabelle");
        db.CreazioneTabelle();
        Utility.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
        db.LeggeImpostazioni();

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
    }

    private void ImpostaOggetti(Context context, Activity view) {
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
        GestioneNotifiche.getInstance().AggiornaNotifica(
                immagine,
                prossimo
        );

        Utility.getInstance().ScriveLog(context, NomeMaschera,"Prossimo cambio: " +
                VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                quantiGiri);

        Button btnMenoMinuti = (Button) view.findViewById(R.id.btnMenoMinuti);
        Button btnPiuMinuti = (Button) view.findViewById(R.id.btnPiuMinuti);
        TextView edtMinuti = (TextView) view.findViewById(R.id.txtMinuti);
        edtMinuti.setText(Integer.toString(minuti));

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
                GestioneNotifiche.getInstance().AggiornaNotifica(
                        immagine,
                        prossimo
                );

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
                GestioneNotifiche.getInstance().AggiornaNotifica(
                        immagine,
                        prossimo
                );

                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();
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
                   Toast.makeText(context, VariabiliStaticheServizio.channelName + ": File di log eliminato", Toast.LENGTH_SHORT).show();
               }
           }
       });

        Button btnInviaLog = (Button) view.findViewById(R.id.btnInviaLog);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                String path = VariabiliStaticheServizio.getInstance().getPercorsoDIRLog() + "/" +
                        VariabiliStaticheServizio.getInstance().getNomeFileDiLog();

                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                File fileWithinMyDir = new File(path);

                if(fileWithinMyDir.exists()) {
                    intentShareFile.setType("text/plain");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Invio file di log " + VariabiliStaticheServizio.channelName);

                    context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
            }
        });

        TextView txtPath = (TextView) view.findViewById(R.id.txtPath);
        VariabiliStaticheServizio.getInstance().setTxtPath(txtPath);
        txtPath.setText(VariabiliStaticheServizio.getInstance().getPercorsoIMMAGINI());
        Button btnCambioPath = (Button) view.findViewById(R.id.btnCambiaPath);

        btnCambioPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                view.startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
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
                        if (VariabiliStaticheServizio.getInstance().getTxtQuanteImmagini() != null &&
                                VariabiliStaticheServizio.getInstance().getTxtQuanteImmagini().length() > 0) {
                            int q = VariabiliStaticheServizio.getInstance().getTxtQuanteImmagini().length();
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

                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();
            }
        });

        Switch swcBlur = (Switch) view.findViewById(R.id.switchBlur);
        swcBlur.setChecked(VariabiliStaticheServizio.getInstance().isBlur());
        swcBlur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheServizio.getInstance().setBlur(isChecked);

                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();

                VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                // ChangeWallpaper c = new ChangeWallpaper(context);
                // c.setWallpaperLocale(VariabiliStaticheServizio.getInstance().getUltimaImmagine());
            }
        });

        Switch switchScriveTesto = (Switch) view.findViewById(R.id.switchScriveTesto);
        switchScriveTesto.setChecked(VariabiliStaticheServizio.getInstance().isScriveTestoSuImmagine());
        switchScriveTesto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheServizio.getInstance().setScriveTestoSuImmagine(isChecked);

                db_dati db = new db_dati(context);
                db.ScriveImpostazioni();

                VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
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
        } else {
            btnMenoMinuti.setEnabled(false);
            btnPiuMinuti.setEnabled(false);
            // btnCambioPath.setEnabled(false);
            swcBlur.setEnabled(false);
            swcOffline.setEnabled(false);
            switchScriveTesto.setEnabled(false);
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
                } else {
                    btnMenoMinuti.setEnabled(false);
                    btnPiuMinuti.setEnabled(false);
                    // btnCambioPath.setEnabled(false);
                    swcBlur.setEnabled(false);
                    swcOffline.setEnabled(false);
                    switchScriveTesto.setEnabled(false);
                }

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

        ImageView imgCaricamento = (ImageView) view.findViewById(R.id.imgCaricamento);
        imgCaricamento.setVisibility(LinearLayout.GONE);
        VariabiliStaticheServizio.getInstance().setImgCaricamento(imgCaricamento);

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgCaricamento.setVisibility(LinearLayout.VISIBLE);
                btnMenoMinuti.setEnabled(false);
                btnPiuMinuti.setEnabled(false);
                // btnCambioPath.setEnabled(false);
                swcBlur.setEnabled(false);
                swcOffline.setEnabled(false);
                switchScriveTesto.setEnabled(false);
                imgRefresh.setVisibility(LinearLayout.GONE);
                imgRefreshLocale.setVisibility(LinearLayout.GONE);
                btnCambioPath.setEnabled(false);

                Runnable runTimer;
                Handler handlerTimer;

                handlerTimer = new Handler(Looper.getMainLooper());
                handlerTimer.postDelayed(runTimer = new Runnable() {
                    @Override
                    public void run() {
                        VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                        if (VariabiliStaticheServizio.getInstance().isScreenOn()) {
                            VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                            Utility.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine Manuale---");
                            ChangeWallpaper c = new ChangeWallpaper(context);
                            if (!VariabiliStaticheServizio.getInstance().isOffline()) {
                                boolean fatto = c.setWallpaper(null);
                                Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente: " + fatto + "---");
                            } else {
                                int numeroRandom = Utility.getInstance().GeneraNumeroRandom(VariabiliStaticheServizio.getInstance().getListaImmagini().size() - 1);
                                if (numeroRandom > -1) {
                                    boolean fatto = c.setWallpaper(VariabiliStaticheServizio.getInstance().getListaImmagini().get(numeroRandom));
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
                        imgCaricamento.setVisibility(LinearLayout.GONE);
                    }
                }, 500);
            }
        });

        ImageView imgUscita = (ImageView) view.findViewById(R.id.imgUscita);
        imgUscita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Context context = getApplicationContext();
                PackageManager packageManager = context.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                context.startActivity(mainIntent);
                Runtime.getRuntime().exit(0); */

                Utility.getInstance().ScriveLog(context, NomeMaschera,"Applicazione terminata");
                GestioneNotifiche.getInstance().RimuoviNotifica();
                // MainActivity.getAppActivity().stopService(new Intent(MainActivity.getAppActivity(),
                //         ServizioBackground.class));

                System.exit(0);
            }
        });

        if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
            Bitmap ultima = BitmapFactory.decodeFile(VariabiliStaticheServizio.getInstance().getUltimaImmagine().getPathImmagine());
            imgImpostata.setImageBitmap(ultima);
        }
    }
}
