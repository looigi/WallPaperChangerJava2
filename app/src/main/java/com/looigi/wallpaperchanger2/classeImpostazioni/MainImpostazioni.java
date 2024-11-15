package com.looigi.wallpaperchanger2.classeImpostazioni;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeFetekkie.VariabiliStaticheMostraImmaginiFetekkie;
import com.looigi.wallpaperchanger2.classeFetekkie.db_dati_fetekkie;
import com.looigi.wallpaperchanger2.classeFetekkie.webservice.ChiamateWSFET;
import com.looigi.wallpaperchanger2.classeFilms.UtilityFilms;
import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.classeFilms.db_dati_films;
import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;
import com.looigi.wallpaperchanger2.classeGps.GestioneGPS;
import com.looigi.wallpaperchanger2.classeGps.GestioneNotificaGPS;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classeDetector.Impostazioni;
import com.looigi.wallpaperchanger2.classeDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeDetector.db_dati_detector;
import com.looigi.wallpaperchanger2.classeGps.GestioneMappa;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classeGps.db_dati_gps;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePennetta.webservice.ChiamateWSPEN;
import com.looigi.wallpaperchanger2.classeVideo.UtilityVideo;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classeVideo.db_dati_video;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePennetta.db_dati_pennetta;
import com.looigi.wallpaperchanger2.classePlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.MainPlayer;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;
import com.looigi.wallpaperchanger2.utilities.RichiestaPathImmaginiLocali;
import com.looigi.wallpaperchanger2.classeAvvio.db_debug;
import com.looigi.wallpaperchanger2.classeWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.classeWallpaper.ScannaDiscoPerImmaginiLocali;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.db_dati_wallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;

public class MainImpostazioni extends Activity {
    private static String NomeMaschera = "Main_Impostazioni";
    private Context context;
    private Activity act;
    private int qualeSchermata = 0;
    private boolean segue = true;
    private Long controlloLongPress = null;
    private LinearLayout layAprePlayer;
    private LinearLayout layChiudePlayer;
    private GifImageView imgAttesa;
    // private LinearLayout laySettingsImpo;
    private Intent intentGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_impostazioni);

        context = this;
        act = this;

        imgAttesa = findViewById(R.id.imgCaricamentoSettings);
        imgAttesa.setVisibility(LinearLayout.GONE);

        Bundle b = getIntent().getExtras();
        String value = "";
        if (b != null) {
            value = b.getString("qualeSettaggio");
        }
        switch (value) {
            case "WALLPAPER":
                qualeSchermata = 0;
                break;
            case "DETECTOR":
                qualeSchermata = 1;
                break;
            case "PLAYER":
                qualeSchermata = 2;
                break;
            case "MAPPA":
                qualeSchermata = 3;
                break;
            case "IMMAGINI":
                qualeSchermata = 5;
                break;
            case "VIDEO":
                qualeSchermata = 6;
                break;
            case "PENNETTA":
                qualeSchermata = 7;
                break;
            case "FILMS":
                qualeSchermata = 8;
                break;
            case "FETEKKIE":
                qualeSchermata = 9;
                break;
            default:
                qualeSchermata = 0;
                break;
        }

        Button btnWallpaper = act.findViewById(R.id.btnSettingsWallpaper);
        Button btnPlayer = act.findViewById(R.id.btnSettingsPlayer);
        Button btnDebug = act.findViewById(R.id.btnSettingsDebug);
        Button btnFilms = act.findViewById(R.id.btnSettingsFilms);

        Button btnDetector = act.findViewById(R.id.btnSettingsDetector);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            btnDetector.setVisibility(LinearLayout.VISIBLE);
        } else {
            btnDetector.setVisibility(LinearLayout.GONE);
        }

        Button btnMappa = act.findViewById(R.id.btnSettingsMappa);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            btnMappa.setVisibility(LinearLayout.VISIBLE);
        } else {
            btnMappa.setVisibility(LinearLayout.GONE);
        }

        Button btnImmagini = act.findViewById(R.id.btnSettingsImmagini);
        if (VariabiliStaticheStart.getInstance().isDetector() &&
                VariabiliStaticheStart.getInstance().isVisibileImmagini()) {
            btnImmagini.setVisibility(LinearLayout.VISIBLE);
        } else {
            btnImmagini.setVisibility(LinearLayout.GONE);
        }

        Button btnVideo = act.findViewById(R.id.btnSettingsVideo);
        if (VariabiliStaticheStart.getInstance().isDetector() &&
                VariabiliStaticheStart.getInstance().isVisibileVideo()) {
            btnVideo.setVisibility(LinearLayout.VISIBLE);
        } else {
            btnVideo.setVisibility(LinearLayout.GONE);
        }

        Button btnPennetta = act.findViewById(R.id.btnSettingsPennetta);
        if (VariabiliStaticheStart.getInstance().isDetector() &&
                VariabiliStaticheStart.getInstance().isVisibilePennetta()) {
            btnPennetta.setVisibility(LinearLayout.VISIBLE);
        } else {
            btnPennetta.setVisibility(LinearLayout.GONE);
        }

        Button btnFetekkie = act.findViewById(R.id.btnSettingsFetekkie);

        btnWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 0;
                VisualizzaSchermata(act);
            }
        });

        btnDetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 1;
                VisualizzaSchermata(act);
            }
        });

        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 2;
                VisualizzaSchermata(act);
            }
        });

        btnMappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 3;
                VisualizzaSchermata(act);
            }
        });

        btnDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 4;
                VisualizzaSchermata(act);
            }
        });

        btnImmagini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 5;
                VisualizzaSchermata(act);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 6;
                VisualizzaSchermata(act);
            }
        });

        btnPennetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 7;
                VisualizzaSchermata(act);
            }
        });

        btnFilms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 8;
                VisualizzaSchermata(act);
            }
        });

        btnFetekkie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 9;
                VisualizzaSchermata(act);
            }
        });

        ImpostaSchermataWallpaper(act);
        ImpostaSchermataDetector(act);
        ImpostaSchermataMappa(act);
        ImpostaSchermataPlayer(act);
        ImpostaSchermataDebug(act);
        ImpostaSchermataImmagini(act);
        ImpostaSchermataVideo(act);
        ImpostaSchermataPennetta(act);
        ImpostaSchermataFetekkie(act);
        ImpostaSchermataFilms(act);

        VisualizzaSchermata(act);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (intentGPS != null) {
            stopService(intentGPS);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }

    private void VisualizzaSchermata(Activity act) {
        RelativeLayout layWallaper = act.findViewById(R.id.layImpostazioniWP);
        LinearLayout layDetector = act.findViewById(R.id.lay_impostazioni_detector);
        LinearLayout layMappa = act.findViewById(R.id.layImpostazioniMappa);
        LinearLayout layPlayer = act.findViewById(R.id.layImpostazioniPlayer);
        LinearLayout layDebug = act.findViewById(R.id.layImpostazioniDebug);
        LinearLayout layImmagini = act.findViewById(R.id.layImpostazioniImmagini);
        LinearLayout layVideo = act.findViewById(R.id.layImpostazioniVideo);
        LinearLayout layPennetta = act.findViewById(R.id.layImpostazioniPennetta);
        LinearLayout layFilms = act.findViewById(R.id.layImpostazioniFilms);
        LinearLayout layFetekkie = act.findViewById(R.id.layImpostazioniFetekkie);

        layWallaper.setVisibility(LinearLayout.GONE);
        layDetector.setVisibility(LinearLayout.GONE);
        layMappa.setVisibility(LinearLayout.GONE);
        layPlayer.setVisibility(LinearLayout.GONE);
        layDebug.setVisibility(LinearLayout.GONE);
        layImmagini.setVisibility(LinearLayout.GONE);
        layVideo.setVisibility(LinearLayout.GONE);
        layPennetta.setVisibility(LinearLayout.GONE);
        layFilms.setVisibility(LinearLayout.GONE);
        layFetekkie.setVisibility(LinearLayout.GONE);

        switch(qualeSchermata) {
            case 0:
                layWallaper.setVisibility(LinearLayout.VISIBLE);
                break;
            case 1:
                layDetector.setVisibility(LinearLayout.VISIBLE);
                break;
            case 2:
                layPlayer.setVisibility(LinearLayout.VISIBLE);
                break;
            case 3:
                layMappa.setVisibility(LinearLayout.VISIBLE);
                break;
            case 4:
                layDebug.setVisibility(LinearLayout.VISIBLE);
                break;
            case 5:
                layImmagini.setVisibility(LinearLayout.VISIBLE);
                break;
            case 6:
                layVideo.setVisibility(LinearLayout.VISIBLE);
                break;
            case 7:
                layPennetta.setVisibility(LinearLayout.VISIBLE);
                break;
            case 8:
                layFilms.setVisibility(LinearLayout.VISIBLE);
                break;
            case 9:
                layFetekkie.setVisibility(LinearLayout.VISIBLE);
                break;
        }
    }

    private void ImpostaSchermataWallpaper(Activity act) {
        int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();

        TextView txtPath = (TextView) act.findViewById(R.id.txtPathImpost);
        VariabiliStaticheWallpaper.getInstance().setTxtPath(txtPath);
        txtPath.setText(VariabiliStaticheWallpaper.getInstance().getPercorsoIMMAGINI());

        TextView txtDetector = act.findViewById(R.id.txtDetector);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            txtDetector.setVisibility(LinearLayout.VISIBLE);
        } else {
            txtDetector.setVisibility(LinearLayout.GONE);
        }

        Button btnMenoMinuti = (Button) act.findViewById(R.id.btnMenoMinuti);
        Button btnPiuMinuti = (Button) act.findViewById(R.id.btnPiuMinuti);
        TextView edtMinuti = (TextView) act.findViewById(R.id.txtMinuti);
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
                        db.ChiudeDB();

                        // GestioneNotifiche.getInstance().AggiornaNotifica();
                        if (isD) {
                            Handler handlerTimer = new Handler(Looper.getMainLooper());
                            Runnable rTimer = new Runnable() {
                                public void run() {
                                    txtDetector.setVisibility(LinearLayout.VISIBLE);

                                    /* GestioneGPS g = new GestioneGPS();
                                    VariabiliStaticheGPS.getInstance().setGestioneGPS(g);
                                    g.AbilitaTimer(context);
                                    g.AbilitaGPS(); */

                                    intentGPS = new Intent(context, GestioneGPS.class);
                                    startForegroundService(intentGPS);

                                    // VariabiliStaticheStart.getInstance().setServizioForegroundGPS(new Intent(context, ServizioDiAvvioGPS.class));
                                    // startForegroundService(VariabiliStaticheStart.getInstance().getServizioForegroundGPS());

                                    GestioneMappa m = new GestioneMappa(context);
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                                    String dataOdierna = sdfD.format(calendar.getTime());
                                    m.LeggePunti(dataOdierna);
                                    VariabiliStaticheGPS.getInstance().setMappa(m);
                                    GestioneNotificaGPS.getInstance().AggiornaNotifica();

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

                                        GestioneNotificheTasti.getInstance().AggiornaNotifica();

                                        UtilitiesGlobali.getInstance().ApreToast(context, "Detector Partito");
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

                            GestioneNotificaGPS.getInstance().RimuoviNotifica();

                            // VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("LONG CLICK");
                            // VariabiliStaticheGPS.getInstance().getGestioneGPS().ChiudeMaschera();
                            // VariabiliStaticheGPS.getInstance().setGestioneGPS(null);
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
                GestioneNotificheWP.getInstance().AggiornaNotifica();

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
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
                GestioneNotificheWP.getInstance().AggiornaNotifica();

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat switchHome = (SwitchCompat) act.findViewById(R.id.switchHome);
        switchHome.setChecked(VariabiliStaticheWallpaper.getInstance().isHome());
        switchHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setHome(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat switchLock = (SwitchCompat) act.findViewById(R.id.switchLock);
        switchLock.setChecked(VariabiliStaticheWallpaper.getInstance().isLock());
        switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setLock(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat swcOffline = (SwitchCompat) act.findViewById(R.id.switchOffline);
        swcOffline.setChecked(VariabiliStaticheWallpaper.getInstance().isOffline());
        LinearLayout layOffline = (LinearLayout) act.findViewById(R.id.layOffline);
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
                            ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context, imgAttesa);
                            bckLeggeImmaginiLocali.execute();
                        }
                    } else {
                        VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText(
                                "Immagini online: " + VariabiliStaticheWallpaper.getInstance().getImmaginiOnline());
                    }
                }

                // VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(true);
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        Button btnCambioPath = (Button) act.findViewById(R.id.btnCambiaPath);
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

        ImageView imgRefreshLocale = (ImageView) act.findViewById(R.id.imgRefreshLocale);
        imgRefreshLocale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgAttesa.setVisibility(LinearLayout.VISIBLE);

                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context, imgAttesa);
                bckLeggeImmaginiLocali.execute();
            }
        });

        SwitchCompat swcEspansa = (SwitchCompat) act.findViewById(R.id.switchEspansa);
        SwitchCompat swcSoloVolti = act.findViewById(R.id.switchSoloVolto);
        SwitchCompat swcEffetti = (SwitchCompat) act.findViewById(R.id.switchEffetti);
        swcEffetti.setChecked(VariabiliStaticheWallpaper.getInstance().isEffetti());
        if (VariabiliStaticheWallpaper.getInstance().isEffetti()) {
            swcSoloVolti.setChecked(false);
            swcSoloVolti.setEnabled(false);
            swcEspansa.setChecked(false);
            swcEspansa.setEnabled(false);
        } else {
            swcSoloVolti.setEnabled(true);
            swcSoloVolti.setChecked(VariabiliStaticheWallpaper.getInstance().isSoloVolti());
            swcEspansa.setEnabled(true);
            swcEspansa.setChecked(VariabiliStaticheWallpaper.getInstance().isEffetti());
            if (swcEspansa.isChecked()) {
                swcSoloVolti.setVisibility(LinearLayout.VISIBLE);
            } else {
                swcSoloVolti.setVisibility(LinearLayout.GONE);
            }
        }
        swcEffetti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setEffetti(isChecked);
                if (VariabiliStaticheWallpaper.getInstance().isEffetti()) {
                    swcSoloVolti.setChecked(false);
                    swcSoloVolti.setEnabled(false);
                    swcEspansa.setChecked(false);
                    swcEspansa.setEnabled(false);
                } else {
                    swcSoloVolti.setEnabled(true);
                    swcSoloVolti.setChecked(VariabiliStaticheWallpaper.getInstance().isSoloVolti());
                    swcEspansa.setEnabled(true);
                    swcEspansa.setChecked(VariabiliStaticheWallpaper.getInstance().isEffetti());
                    if (swcEspansa.isChecked()) {
                        swcSoloVolti.setVisibility(LinearLayout.VISIBLE);
                    } else {
                        swcSoloVolti.setVisibility(LinearLayout.GONE);
                    }
                }

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        swcSoloVolti.setChecked(VariabiliStaticheWallpaper.getInstance().isSoloVolti());
        swcSoloVolti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setSoloVolti(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
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
                db.ChiudeDB();
            }
        });

        SwitchCompat swcBlur = (SwitchCompat) act.findViewById(R.id.switchBlur);
        swcBlur.setChecked(VariabiliStaticheWallpaper.getInstance().isBlur());
        swcBlur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setBlur(isChecked);

                // VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(true);
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();

                // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                // ChangeWallpaper c = new ChangeWallpaper(context);
                // c.setWallpaperLocale(VariabiliStaticheServizio.getInstance().getUltimaImmagine());
            }
        });

        SwitchCompat switchScriveTesto = act.findViewById(R.id.switchScriveTesto);
        switchScriveTesto.setChecked(VariabiliStaticheWallpaper.getInstance().isScriveTestoSuImmagine());
        switchScriveTesto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setScriveTestoSuImmagine(isChecked);

                // VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(true);
                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();

                // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                // ChangeWallpaper c = new ChangeWallpaper(context);
                // c.setWallpaperLocale(VariabiliStaticheServizio.getInstance().getUltimaImmagine());
            }
        });

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogWP);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogWP);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogWP);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "WALLPAPER");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "WALLPAPER");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "WALLPAPER");
            }
        });
    }

    private void ImpostaSchermataDetector(Activity act) {
        SwitchCompat sTriploTastoCuffieFoto = (SwitchCompat) act.findViewById(R.id.sTriploTastoCuffie);
        sTriploTastoCuffieFoto.setChecked(VariabiliStaticheDetector.getInstance().isFotoSuTriploTastoCuffie());
        sTriploTastoCuffieFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setFotoSuTriploTastoCuffie(sTriploTastoCuffieFoto.isChecked());

                db_dati_detector db = new db_dati_detector(context);
                db.ScriveImpostazioni(context, "SET TRIPLO TASTO CUFFIE");
                db.ChiudeDB();
            }
        });
        SwitchCompat sVibrazione = (SwitchCompat) act.findViewById(R.id.sVibrazione);
        SwitchCompat sToast = (SwitchCompat) act.findViewById(R.id.sToast);
        SwitchCompat sGpsPreciso = (SwitchCompat) act.findViewById(R.id.sGpsPreciso);

        sVibrazione.setChecked(VariabiliStaticheDetector.getInstance().isVibrazione());
        sVibrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setVibrazione(sVibrazione.isChecked());

                Impostazioni i = new Impostazioni();
                i.ImpostaVibrazione(context, sVibrazione.isChecked());
            }
        });

        sGpsPreciso.setChecked(VariabiliStaticheDetector.getInstance().isGpsPreciso());
        sGpsPreciso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean gps = VariabiliStaticheDetector.getInstance().isGpsPreciso();
                gps = !gps;
                VariabiliStaticheDetector.getInstance().setGpsPreciso(gps);

                db_dati_detector db = new db_dati_detector(context);
                db.ScriveImpostazioni(context, "SET GPS PRECISO");
                db.ChiudeDB();

                if (VariabiliStaticheGPS.getInstance().getGestioneGPS() != null) {
                    VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("INIT 1");
                    VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS("Imposta Schermata Detector");
                }
            }
        });

        sToast.setChecked(VariabiliStaticheDetector.getInstance().isVisualizzaToast());
        sToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setVisualizzaToast(sToast.isChecked());

                Impostazioni i = new Impostazioni();
                i.ImpostaVisualizzaToast(context, sToast.isChecked());
            }
        });

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogDT);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogDT);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogDT);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "DETECTOR");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "DETECTOR");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "DETECTOR");
            }
        });
    }

    private void ImpostaSchermataMappa(Activity act) {
        EditText etGpsMs = (EditText) act.findViewById(R.id.edtGpsMs);
        etGpsMs.setText(Integer.toString(VariabiliStaticheDetector.getInstance().getGpsMs()));
        etGpsMs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        VariabiliStaticheDetector.getInstance().setGpsMs(Integer.parseInt(etGpsMs.getText().toString()));

                        db_dati_detector db = new db_dati_detector(context);
                        db.ScriveImpostazioni(context, "SET GPS MS");
                        db.ChiudeDB();

                        VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("FOCUS CHANGE 1");
                        VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS("Imposta schermata mappa 1");
                    }
                }
            }
        });

        EditText etGpsMeters = (EditText) act.findViewById(R.id.edtGpsMeters);
        etGpsMeters.setText(Integer.toString(VariabiliStaticheDetector.getInstance().getGpsMeters()));
        etGpsMeters.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        VariabiliStaticheDetector.getInstance().setGpsMeters(Integer.parseInt(etGpsMeters.getText().toString()));

                        db_dati_detector db = new db_dati_detector(context);
                        db.ScriveImpostazioni(context, "SET GPS METERS");
                        db.ChiudeDB();

                        VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("FOCUS CHANGE 2");
                        VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS("Imposta schermata mappa 2");
                    }
                }
            }
        });

        EditText etDistanzaPS = (EditText) act.findViewById(R.id.edtMetriPS);
        etDistanzaPS.setText(Integer.toString(VariabiliStaticheGPS.getInstance().getDistanzaMetriPerPS()));
        etDistanzaPS.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        VariabiliStaticheGPS.getInstance().setDistanzaMetriPerPS(Integer.parseInt(etDistanzaPS.getText().toString()));

                        db_dati_detector db = new db_dati_detector(context);
                        db.ScriveImpostazioni(context, "SET METRI PS");
                        db.ChiudeDB();
                    }
                }
            }
        });

        SwitchCompat sSegnale = act.findViewById(R.id.sSegnale);
        sSegnale.setChecked(true);
        sSegnale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheGPS.getInstance().setMostraSegnale(sSegnale.isChecked());

                db_dati_gps db = new db_dati_gps(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat sPercorso = act.findViewById(R.id.sPercorso);
        sPercorso.setChecked(true);
        sPercorso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheGPS.getInstance().setMostraPercorso(sPercorso.isChecked());

                db_dati_gps db = new db_dati_gps(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat sPS = act.findViewById(R.id.sPuntiDiSospensione);
        sPS.setChecked(VariabiliStaticheGPS.getInstance().isPuntiSospensioneAttivi());
        sPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheGPS.getInstance().setPuntiSospensioneAttivi(sPS.isChecked());

                db_dati_gps db = new db_dati_gps(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat sWifi = act.findViewById(R.id.sWifi);
        sWifi.setChecked(VariabiliStaticheGPS.getInstance().isBloccoPerWifi());
        sWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheGPS.getInstance().setBloccoPerWifi(sWifi.isChecked());

                db_dati_gps db = new db_dati_gps(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();

                VariabiliStaticheGPS.getInstance().getGestioneGPS().ControlloAccSpegn(context);
            }
        });

        SwitchCompat sAccuracy = act.findViewById(R.id.sAccuracy);
        sAccuracy.setChecked(VariabiliStaticheGPS.getInstance().isAccuracyAttiva());
        sAccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheGPS.getInstance().setAccuracyAttiva(sAccuracy.isChecked());

                db_dati_gps db = new db_dati_gps(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogMP);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogMP);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogMP);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "MAPPA");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "MAPPA");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "MAPPA");
            }
        });
    }

    private void impostaTastiPlayer() {
        if (VariabiliStaticheStart.getInstance().isPlayerAperto()) {
            layAprePlayer.setVisibility(LinearLayout.GONE);
            layChiudePlayer.setVisibility(LinearLayout.VISIBLE);
            // laySettingsImpo.setVisibility(LinearLayout.VISIBLE);
        } else {
            layAprePlayer.setVisibility(LinearLayout.VISIBLE);
            layChiudePlayer.setVisibility(LinearLayout.GONE);
            // laySettingsImpo.setVisibility(LinearLayout.GONE);
        }

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogPL);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogPL);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogPL);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "PLAYER");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "PLAYER");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "PLAYER");
            }
        });
    }

    private void ImpostaSchermataImmagini(Activity act) {
        // db_dati_immagini db = new db_dati_immagini(context);
        // db.CaricaImpostazioni();
        SwitchCompat swcRandom = (SwitchCompat) act.findViewById(R.id.sRandomIMM);
        swcRandom.setChecked(VariabiliStaticheMostraImmagini.getInstance().getRandom().equals("S"));
        swcRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmagini.getInstance().setRandom(swcRandom.isChecked() ? "S" : "N");

                db_dati_immagini db = new db_dati_immagini(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat swcRicercaVisuaVideo = act.findViewById(R.id.switchCercaVisua);
        swcRicercaVisuaVideo.setChecked(VariabiliStaticheMostraImmagini.getInstance().isRicercaPerVisua());
        swcRicercaVisuaVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmagini.getInstance().setRicercaPerVisua(swcRicercaVisuaVideo.isChecked());

                db_dati_immagini db = new db_dati_immagini(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        ImageView imgRefresh = act.findViewById(R.id.imgRefreshImmagini);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgAttesa.setVisibility(LinearLayout.VISIBLE);

                ChiamateWSMI ws = new ChiamateWSMI(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Tipo aggiornamento");
                builder.setPositiveButton("Categoria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshImmagini(String.valueOf(VariabiliStaticheMostraImmagini.getInstance().getIdCategoria()));
                    }
                });
                builder.setNegativeButton("Tutto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshImmagini("");
                        // dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        EditText edtSecondi = act.findViewById(R.id.edtTempoSlideShow);
        String limite = String.valueOf(VariabiliStaticheMostraImmagini.getInstance().getSecondiAttesa());
        edtSecondi.setText(limite);
        edtSecondi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        String lim = edtSecondi.getText().toString();
                        VariabiliStaticheMostraImmagini.getInstance().setSecondiAttesa(Integer.parseInt(lim));

                        db_dati_immagini db = new db_dati_immagini(context);
                        db.ScriveImpostazioni();
                        db.ChiudeDB();
                    }
                }
            }
        });

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogIM);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogIM);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogIM);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "IMMAGINI");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "IMMAGINI");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "IMMAGINI");
            }
        });
    }

    private void ImpostaSchermataFetekkie(Activity act) {
        // db_dati_immagini db = new db_dati_immagini(context);
        // db.CaricaImpostazioni();
        SwitchCompat swcRandom = (SwitchCompat) act.findViewById(R.id.sRandomFET);
        swcRandom.setChecked(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getRandom().equals("S"));
        swcRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setRandom(swcRandom.isChecked() ? "S" : "N");

                db_dati_fetekkie db = new db_dati_fetekkie(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat swcRicercaVisuaVideo = act.findViewById(R.id.switchCercaVisuaFet);
        swcRicercaVisuaVideo.setChecked(VariabiliStaticheMostraImmaginiFetekkie.getInstance().isRicercaPerVisua());
        swcRicercaVisuaVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setRicercaPerVisua(swcRicercaVisuaVideo.isChecked());

                db_dati_fetekkie db = new db_dati_fetekkie(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        EditText edtSecondi = act.findViewById(R.id.edtTempoSlideShowFET);
        String limite = String.valueOf(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSecondiAttesa());
        edtSecondi.setText(limite);
        edtSecondi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        String lim = edtSecondi.getText().toString();
                        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSecondiAttesa(Integer.parseInt(lim));

                        db_dati_fetekkie db = new db_dati_fetekkie(context);
                        db.ScriveImpostazioni();
                        db.ChiudeDB();
                    }
                }
            }
        });

        ImageView imgRefresh = act.findViewById(R.id.imgRefreshFetekkie);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgAttesa.setVisibility(LinearLayout.VISIBLE);

                ChiamateWSFET ws = new ChiamateWSFET(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Tipo aggiornamento");
                builder.setPositiveButton("Categoria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshImmagini(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoria());
                    }
                });
                builder.setNegativeButton("Tutto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshImmagini("");
                        // dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogFET);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogFET);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogFET);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "FETEKKIE");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "FETEKKIE");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "FETEKKIE");
            }
        });
    }

    private void ImpostaSchermataPennetta(Activity act) {
        // db_dati_immagini db = new db_dati_immagini(context);
        // db.CaricaImpostazioni();
        SwitchCompat swcRandom = (SwitchCompat) act.findViewById(R.id.sRandomPEN);
        swcRandom.setChecked(VariabiliStaticheMostraImmaginiPennetta.getInstance().getRandom().equals("S"));
        swcRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setRandom(swcRandom.isChecked() ? "S" : "N");

                db_dati_pennetta db = new db_dati_pennetta(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat swcRicercaVisuaVideo = act.findViewById(R.id.switchCercaVisuaPen);
        swcRicercaVisuaVideo.setChecked(VariabiliStaticheMostraImmaginiPennetta.getInstance().isRicercaPerVisua());
        swcRicercaVisuaVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setRicercaPerVisua(swcRicercaVisuaVideo.isChecked());

                db_dati_pennetta db = new db_dati_pennetta(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        EditText edtSecondi = act.findViewById(R.id.edtTempoSlideShowPen);
        String limite = String.valueOf(VariabiliStaticheMostraImmaginiPennetta.getInstance().getSecondiAttesa());
        edtSecondi.setText(limite);
        edtSecondi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        String lim = edtSecondi.getText().toString();
                        VariabiliStaticheMostraImmaginiPennetta.getInstance().setSecondiAttesa(Integer.parseInt(lim));

                        db_dati_pennetta db = new db_dati_pennetta(context);
                        db.ScriveImpostazioni();
                        db.ChiudeDB();
                    }
                }
            }
        });

        ImageView imgRefresh = act.findViewById(R.id.imgRefreshPennetta);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgAttesa.setVisibility(LinearLayout.VISIBLE);

                ChiamateWSPEN ws = new ChiamateWSPEN(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Tipo aggiornamento");
                builder.setPositiveButton("Categoria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshImmagini(VariabiliStaticheMostraImmaginiPennetta.getInstance().getCategoria());
                    }
                });
                builder.setNegativeButton("Tutto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshImmagini("");
                        // dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogPen);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogPen);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogPen);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "PENNETTA");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "PENNETTA");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "PENNETTA");
            }
        });
    }

    private void ImpostaSchermataVideo(Activity act) {
        SwitchCompat swcRandom = (SwitchCompat) act.findViewById(R.id.sRandomV);
        swcRandom.setChecked(VariabiliStaticheVideo.getInstance().getRandom().equals("S"));
        swcRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheVideo.getInstance().setRandom(swcRandom.isChecked() ? "S" : "N");

                db_dati_video db = new db_dati_video(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        EditText edtNumFrames = (EditText) act.findViewById(R.id.edtNumFramesV);
        edtNumFrames.setText(Integer.toString(VariabiliStaticheVideo.getInstance().getNumeroFrames()));
        edtNumFrames.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        VariabiliStaticheVideo.getInstance().setNumeroFrames(Integer.parseInt(edtNumFrames.getText().toString()));

                        db_dati_video db = new db_dati_video(context);
                        db.ScriveImpostazioni();
                        db.ChiudeDB();
                    }
                }
            }
        });

        ImageView imgRefresh = act.findViewById(R.id.imgRefreshVideo);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgAttesa.setVisibility(LinearLayout.VISIBLE);

                ChiamateWSV ws = new ChiamateWSV(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Tipo aggiornamento");
                builder.setPositiveButton("Categoria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshVideo(VariabiliStaticheVideo.getInstance().getCategoria());
                    }
                });
                builder.setNegativeButton("Tutto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshVideo("");
                        // dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        SwitchCompat swcBarraVideo = act.findViewById(R.id.switchBarraVideo);
        swcBarraVideo.setChecked(VariabiliStaticheVideo.getInstance().isBarraVisibile());
        swcBarraVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheVideo.getInstance().setBarraVisibile(swcBarraVideo.isChecked());

                if (swcBarraVideo.isChecked()) {
                    // BARRA sempre visibile
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(
                            new Runnable() {
                                public void run() {
                                    VariabiliStaticheVideo.getInstance().getVideoView().stopPlayback();
                                    VariabiliStaticheVideo.getInstance().getVideoView().clearAnimation();
                                    VariabiliStaticheVideo.getInstance().getVideoView().suspend(); // clears media player
                                    VariabiliStaticheVideo.getInstance().getVideoView().setVideoURI(null);
                                    VariabiliStaticheVideo.getInstance().setVideoView(null);

                                    UtilityVideo.getInstance().ImpostaVideo();
                                }
                            }, 100);
                }

                db_dati_video db = new db_dati_video(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat swcRicercaVisuaVideo = act.findViewById(R.id.switchCercaVisua);
        swcRicercaVisuaVideo.setChecked(VariabiliStaticheVideo.getInstance().isRicercaPerVisua());
        swcRicercaVisuaVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheVideo.getInstance().setRicercaPerVisua(swcRicercaVisuaVideo.isChecked());

                db_dati_video db = new db_dati_video(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogVD);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogVD);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogVD);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "VIDEO");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "VIDEO");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "VIDEO");
            }
        });
    }

    private void ImpostaSchermataPlayer(Activity act) {
        layAprePlayer = act.findViewById(R.id.layAprePlayer);
        layChiudePlayer = act.findViewById(R.id.layChiudePlayer);
        // laySettingsImpo = act.findViewById(R.id.laySettingsPlayer);

        // db_dati_player db = new db_dati_player(context);
        // db.CaricaImpostazioni();

        EditText edtLimiteGB = act.findViewById(R.id.edtLimiteGiga);
        String limite = String.valueOf(VariabiliStatichePlayer.getInstance().getLimiteInGb());
        edtLimiteGB.setText(limite);
        edtLimiteGB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        String lim = edtLimiteGB.getText().toString();
                        VariabiliStatichePlayer.getInstance().setLimiteInGb(Float.parseFloat(lim));

                        db_dati_player db = new db_dati_player(context);
                        db.ScriveImpostazioni();
                        db.ChiudeDB();
                    }
                }
            }
        });

        Button btnAprePlayer = (Button) act.findViewById(R.id.btnAprePlayer);
        btnAprePlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheStart.getInstance().setPlayerAperto(true);
                impostaTastiPlayer();

                Intent iP = new Intent(context, MainPlayer.class);
                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iP);

                Notification notificaPlayer = GestioneNotifichePlayer.getInstance().StartNotifica(context);
                if (notificaPlayer != null) {
                    // startForeground(VariabiliStatichePlayer.NOTIFICATION_CHANNEL_ID, notificaPlayer);

                    GestioneNotifichePlayer.getInstance().AggiornaNotifica("Titolo Canzone");

                    UtilitiesGlobali.getInstance().ApreToast(context, "Player Partito");
                }

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                    }
                }, 500);
            }
        });

        Button btnChiudePlayer = (Button) act.findViewById(R.id.btnChiudePlayer);
        btnChiudePlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheStart.getInstance().setPlayerAperto(false);
                impostaTastiPlayer();

                GestioneNotifichePlayer.getInstance().RimuoviNotifica();

                UtilityPlayer.getInstance().PressionePlay(context, false);
                UtilityPlayer.getInstance().ChiudeActivity(true);
            }
        });

        SwitchCompat swcChiacchiera = (SwitchCompat) act.findViewById(R.id.sChiacchiera);
        swcChiacchiera.setChecked(VariabiliStatichePlayer.getInstance().isChiacchiera());
        swcChiacchiera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setChiacchiera(swcChiacchiera.isChecked());

                db_dati_player db = new db_dati_player(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        impostaTastiPlayer();
    }

    private void ImpostaSchermataDebug(Activity act) {
        SwitchCompat swcAttivaDebug = (SwitchCompat) act.findViewById(R.id.switchDebug);
        swcAttivaDebug.setChecked(VariabiliStaticheStart.getInstance().isLogAttivo());
        swcAttivaDebug.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheStart.getInstance().setLogAttivo(swcAttivaDebug.isChecked());

                db_debug db = new db_debug(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        Button btnPulisceLog = (Button) act.findViewById(R.id.btnPulisceLog);
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "");
            }
        });

        Button btnInviaLog = (Button) act.findViewById(R.id.btnInviaLog);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "");
            }
        });
    }

    private void ImpostaSchermataFilms(Activity act) {
        SwitchCompat swcRandom = (SwitchCompat) act.findViewById(R.id.sRandomF);
        swcRandom.setChecked(VariabiliStaticheVideo.getInstance().getRandom().equals("S"));
        swcRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheFilms.getInstance().setRandom(swcRandom.isChecked() ? "S" : "N");

                db_dati_films db = new db_dati_films(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        EditText edtNumFrames = (EditText) act.findViewById(R.id.edtNumFramesF);
        edtNumFrames.setText(Integer.toString(VariabiliStaticheFilms.getInstance().getNumeroFrames()));
        edtNumFrames.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        VariabiliStaticheFilms.getInstance().setNumeroFrames(Integer.parseInt(edtNumFrames.getText().toString()));

                        db_dati_films db = new db_dati_films(context);
                        db.ScriveImpostazioni();
                        db.ChiudeDB();
                    }
                }
            }
        });

        SwitchCompat swcBarraFilm = act.findViewById(R.id.switchBarraFilm);
        swcBarraFilm.setChecked(VariabiliStaticheFilms.getInstance().isBarraVisibile());
        swcBarraFilm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheFilms.getInstance().setBarraVisibile(swcBarraFilm.isChecked());

                if (swcBarraFilm.isChecked()) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(
                            new Runnable() {
                                public void run() {
                                    VariabiliStaticheFilms.getInstance().getFilmsView().stopPlayback();
                                    VariabiliStaticheFilms.getInstance().getFilmsView().clearAnimation();
                                    VariabiliStaticheFilms.getInstance().getFilmsView().suspend(); // clears media player
                                    VariabiliStaticheFilms.getInstance().getFilmsView().setVideoURI(null);
                                    VariabiliStaticheFilms.getInstance().setFilmsView(null);

                                    UtilityFilms.getInstance().ImpostaFilms();
                                }
                            }, 100);
                }

                db_dati_films db = new db_dati_films(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        SwitchCompat swcRicercaVisuaVideo = act.findViewById(R.id.switchCercaVisua);
        swcRicercaVisuaVideo.setChecked(VariabiliStaticheFilms.getInstance().isRicercaPerVisua());
        swcRicercaVisuaVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheFilms.getInstance().setRicercaPerVisua(swcRicercaVisuaVideo.isChecked());

                db_dati_films db = new db_dati_films(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        ImageView imgRefresh = act.findViewById(R.id.imgRefreshFilms);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgAttesa.setVisibility(LinearLayout.VISIBLE);

                ChiamateWSF ws = new ChiamateWSF(context);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Tipo aggiornamento");
                builder.setPositiveButton("Categoria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshFilms(VariabiliStaticheFilms.getInstance().getCategoria());
                    }
                });
                builder.setNegativeButton("Tutto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ws.ImpostaImgAttesa(imgAttesa);
                        ws.RefreshFilms("");
                        // dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        Button btnInviaLog = act.findViewById(R.id.btnInviaLogF);
        Button btnPulisceLog = act.findViewById(R.id.btnPulisceLogF);
        Button btnVisualizzaLog = act.findViewById(R.id.btnVisualizzaLogF);
        btnInviaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().CondividiLogs(context, "FILMS");
            }
        });
        btnPulisceLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().EliminaLogs(context, "FILMS");
            }
        });
        btnVisualizzaLog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().VisualizzaLogs(context, "FILMS");
            }
        });
    }
}
