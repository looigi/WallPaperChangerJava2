package com.looigi.wallpaperchanger2.classeImpostazioni;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
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

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classiDetector.Impostazioni;
import com.looigi.wallpaperchanger2.classiDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiDetector.db_dati_detector;
import com.looigi.wallpaperchanger2.classiGps.GestioneGPS;
import com.looigi.wallpaperchanger2.classiGps.GestioneMappa;
import com.looigi.wallpaperchanger2.classiGps.MainMappa;
import com.looigi.wallpaperchanger2.classiGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classiPlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classiPlayer.MainPlayer;
import com.looigi.wallpaperchanger2.classiPlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classiStandard.RichiestaPathImmaginiLocali;
import com.looigi.wallpaperchanger2.classiStandard.db_debug;
import com.looigi.wallpaperchanger2.classiWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.classiWallpaper.ScannaDiscoPerImmaginiLocali;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classiWallpaper.db_dati_wallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainImpostazioni extends Activity {
    private static String NomeMaschera = "MAINIMPOSTAZIONI";
    private Context context;
    private Activity act;
    private int qualeSchermata = 0;
    private boolean segue = true;
    private Long controlloLongPress = null;
    private LinearLayout layAprePlayer;
    private LinearLayout layChiudePlayer;
    // private LinearLayout laySettingsImpo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_impostazioni);

        context = this;
        act = this;

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
            default:
                qualeSchermata = 0;
                break;
        }

        Button btnWallpaper = act.findViewById(R.id.btnSettingsWallpaper);
        Button btnPlayer = act.findViewById(R.id.btnSettingsPlayer);
        Button btnDebug = act.findViewById(R.id.btnSettingsDebug);

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

        ImpostaSchermataWallpaper(act);
        ImpostaSchermataDetector(act);
        ImpostaSchermataMappa(act);
        ImpostaSchermataPlayer(act);
        ImpostaSchermataDebug(act);
        ImpostaSchermataImmagini(act);
        ImpostaSchermataVideo(act);

        VisualizzaSchermata(act);
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

        layWallaper.setVisibility(LinearLayout.GONE);
        layDetector.setVisibility(LinearLayout.GONE);
        layMappa.setVisibility(LinearLayout.GONE);
        layPlayer.setVisibility(LinearLayout.GONE);
        layDebug.setVisibility(LinearLayout.GONE);
        layImmagini.setVisibility(LinearLayout.GONE);
        layVideo.setVisibility(LinearLayout.GONE);

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

                        // GestioneNotifiche.getInstance().AggiornaNotifica();
                        if (isD) {
                            Handler handlerTimer = new Handler(Looper.getMainLooper());
                            Runnable rTimer = new Runnable() {
                                public void run() {
                                    txtDetector.setVisibility(LinearLayout.VISIBLE);

                                    GestioneGPS g = new GestioneGPS();
                                    VariabiliStaticheGPS.getInstance().setGestioneGPS(g);
                                    g.AbilitaTimer(context);
                                    g.AbilitaGPS();

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

                            VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("LONG CLICK");
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
                GestioneNotificheWP.getInstance().AggiornaNotifica();

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
                GestioneNotificheWP.getInstance().AggiornaNotifica();

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        SwitchCompat switchHome = (SwitchCompat) act.findViewById(R.id.switchHome);
        switchHome.setChecked(VariabiliStaticheWallpaper.getInstance().isHome());
        switchHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setHome(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
            }
        });

        SwitchCompat switchLock = (SwitchCompat) act.findViewById(R.id.switchLock);
        switchLock.setChecked(VariabiliStaticheWallpaper.getInstance().isLock());
        switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheWallpaper.getInstance().setLock(isChecked);

                db_dati_wallpaper db = new db_dati_wallpaper(context);
                db.ScriveImpostazioni();
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
                ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(context);
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

        SwitchCompat swcBlur = (SwitchCompat) act.findViewById(R.id.switchBlur);
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

        SwitchCompat switchScriveTesto = act.findViewById(R.id.switchScriveTesto);
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
        SwitchCompat sFotoPower = (SwitchCompat) act.findViewById(R.id.sPower);
        sFotoPower.setChecked(VariabiliStaticheDetector.getInstance().isFotoSuPower());
        sFotoPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setFotoSuPower(sFotoPower.isChecked());

                db_dati_detector db = new db_dati_detector(context);
                db.ScriveImpostazioni(context);
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
                db.ScriveImpostazioni(context);

                if (VariabiliStaticheGPS.getInstance().getGestioneGPS() != null) {
                    VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("INIT 1");
                    VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS();
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

        EditText etGpsMs = (EditText) act.findViewById(R.id.edtGpsMs);
        etGpsMs.setText(Integer.toString(VariabiliStaticheDetector.getInstance().getGpsMs()));
        etGpsMs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    {
                        VariabiliStaticheDetector.getInstance().setGpsMs(Integer.parseInt(etGpsMs.getText().toString()));

                        db_dati_detector db = new db_dati_detector(context);
                        db.ScriveImpostazioni(context);

                        VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("FOCUS CHANGE 1");
                        VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS();
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
                        db.ScriveImpostazioni(context);

                        VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("FOCUS CHANGE 2");
                        VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS();
                    }
                }
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
        SwitchCompat sSegue = act.findViewById(R.id.sSegue);
        sSegue.setChecked(true);
        segue = true;
        sSegue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segue = sSegue.isChecked();

                MainMappa.segue = segue;
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

    private void ImpostaSchermataVideo(Activity act) {
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
}
