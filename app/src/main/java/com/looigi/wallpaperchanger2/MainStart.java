package com.looigi.wallpaperchanger2;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.AutoStart.RunServiceOnBoot;
import com.looigi.wallpaperchanger2.classeBackup.MainBackup;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFetekkie.MainMostraFetekkie;
import com.looigi.wallpaperchanger2.classeFilms.MainMostraFilms;
import com.looigi.wallpaperchanger2.classeGoogleDrive.GoogleDrive;
import com.looigi.wallpaperchanger2.classeGoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.classeGps.GestioneNotificaGPS;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeDetector.InizializzaMascheraDetector;
import com.looigi.wallpaperchanger2.classeDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeLazio.MainLazio;
import com.looigi.wallpaperchanger2.classeModificheCodice.MainModificheCodice;
import com.looigi.wallpaperchanger2.classeOnomastici.MainOnomastici;
import com.looigi.wallpaperchanger2.classeOrari.MainOrari;
import com.looigi.wallpaperchanger2.classePassword.MainPassword;
import com.looigi.wallpaperchanger2.classePlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.MainPlayer;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.MainUtilityImmagini;
import com.looigi.wallpaperchanger2.classeWallpaper.InizializzaMascheraWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.MainWallpaper;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.Permessi;
import com.looigi.wallpaperchanger2.classeAvvio.ServizioInterno;
import com.looigi.wallpaperchanger2.classeGps.GestioneMappa;
import com.looigi.wallpaperchanger2.classeGps.MainMappa;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.utilities.CaricaSettaggi;
import com.looigi.wallpaperchanger2.utilities.PrendeModelloTelefono;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainStart extends Activity {
    private static String NomeMaschera = "Main_Start";
    private Context context;
    private Activity act;
    private LinearLayout laySplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_start);

        context = this;
        act = this;

        TextView txtTitolo = findViewById(R.id.txtStartTitolo);
        txtTitolo.setShadowLayer(
                15f,     // radius: The radius of the shadow
                0f,      // dx: The horizontal offset of the shadow
                0f,      // dy: The vertical offset of the shadow
                Color.BLUE // shadowColor: The color of the shadow
        );

        if (!VariabiliStaticheStart.getInstance().isGiaPartito()) {
            // UtilitiesGlobali.getInstance().InvioMail(context,
            //         "looigi@gmail.com",
            //         "Wallpaper Changer II",
            //         "Start Activity Principale.\nGià partito: " + VariabiliStaticheStart.getInstance().isGiaPartito());

            VariabiliStaticheStart.getInstance().setPlayerAperto(false);

            Intent intent1 = new Intent(MainStart.this, RunServiceOnBoot.class);
            startService(intent1);

            VariabiliStaticheStart.getInstance().setContext(this);
            VariabiliStaticheStart.getInstance().setMainActivity(this);

            // UtilityWallpaper.getInstance().generaPath(this);

            Permessi pp = new Permessi();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                VariabiliStaticheWallpaper.getInstance().setCiSonoPermessi(pp.ControllaPermessi(this));
            }

            laySplash = findViewById(R.id.laySplash);
            laySplash.setVisibility(LinearLayout.VISIBLE);

            String ritorno = CaricaSettaggi.getInstance().CaricaImpostazioniGlobali(context, "MAIN");
            if (!ritorno.equals("OK")) {
                UtilityDetector.getInstance().VisualizzaPOPUP(
                        context, ritorno, false, -1
                );
            } else {
                if (VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {
                    StartActivities();

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            laySplash.setVisibility(LinearLayout.GONE);

                            impostaSchermata();
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 3000);
                } else {
                    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        VariabiliStaticheWallpaper.getInstance().setCiSonoPermessi(pp.ControllaPermessi(this));
                    }
                    // }

                }
            }
        } else {
            impostaSchermata();
        }
    }

    private void impostaSchermata() {
        // new Chiacchierone(context, "App Quality Insights");

        VariabiliStaticheStart.getInstance().setOraEntrata(new Date());

        LinearLayout layStart = findViewById(R.id.layStart);

        if (VariabiliStaticheStart.getInstance().isDetector()) {
            GestioneMappa m = new GestioneMappa(this);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
            String dataOdierna = sdfD.format(calendar.getTime());
            m.LeggePunti(dataOdierna);
            VariabiliStaticheGPS.getInstance().setMappa(m);
            GestioneNotificaGPS.getInstance().AggiornaNotifica();
        }

        ImageView imgD = findViewById(R.id.imgStartDetector);
        LinearLayout layDetector = findViewById(R.id.layBarraDetector);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            layDetector.setVisibility(LinearLayout.VISIBLE);
        } else {
            layDetector.setVisibility(LinearLayout.GONE);
        }
        imgD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainActivityDetector.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InizializzaMascheraDetector i2 = new InizializzaMascheraDetector();
                                i2.inizializzaMaschera(
                                        context,
                                        VariabiliStaticheDetector.getInstance().getMainActivity());
                            }
                        }, 100);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // act.finish();
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                        act.finish();
                    }
                }, 100);
            }
        });

        ImageView imgCI = findViewById(R.id.imgStartContrImmagini);
        LinearLayout layCI = findViewById(R.id.layBarraCI);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            layCI.setVisibility(LinearLayout.VISIBLE);
        } else {
            layCI.setVisibility(LinearLayout.GONE);
        }
        imgCI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, MainUtilityImmagini.class);
                        intent.putExtra("idCategoria", "1");
                        startActivity(intent);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // act.finish();
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                        act.finish();
                    }
                }, 100);
            }
        });

        ImageView imgW = findViewById(R.id.imgStartWallpaper);
        imgW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainWallpaper.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InizializzaMascheraWallpaper i = new InizializzaMascheraWallpaper();
                                i.inizializzaMaschera(
                                        context,
                                        VariabiliStaticheWallpaper.getInstance().getMainActivity());
                            }
                        }, 500);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                        act.finish();
                    }
                }, 100);
            }
        });

        ImageView imgO = findViewById(R.id.imgStartOnomastici);
        imgO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VariabiliStaticheStart.getInstance().setPlayerAperto(true);

                        Intent iO = new Intent(context, MainOnomastici.class);
                        iO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iO);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                        act.finish();
                    }
                }, 100);
            }
        });

        ImageView imgF = findViewById(R.id.imgStartFilms);
        imgF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent iF = new Intent(context, MainMostraFilms.class);
                        iF.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iF);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                        act.finish();
                    }
                }, 100);
            }
        });

        ImageView imgP = findViewById(R.id.imgStartPlayer);
        imgP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VariabiliStaticheStart.getInstance().setPlayerAperto(true);

                        Intent iP = new Intent(context, MainPlayer.class);
                        iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iP);

                        Notification notificaPlayer = GestioneNotifichePlayer.getInstance().StartNotifica(context);
                        if (notificaPlayer != null) {
                            // startForeground(VariabiliStatichePlayer.NOTIFICATION_CHANNEL_ID, notificaPlayer);

                            GestioneNotifichePlayer.getInstance().AggiornaNotifica("Titolo Canzone");

                            UtilitiesGlobali.getInstance().ApreToast(context, "Player Partito");
                        }
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                        act.finish();
                    }
                }, 100);
            }
        });

        ImageView imgSettings = (ImageView) findViewById(R.id.imgStartSettings);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);

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

        ImageView imgFetekkie = (ImageView) findViewById(R.id.imgStartFetekkie);
        imgFetekkie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainMostraFetekkie.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgModifiche = (ImageView) findViewById(R.id.imgStartModifiche);
        imgModifiche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainModificheCodice.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgOrari = (ImageView) findViewById(R.id.imgStartOrari);
        imgOrari.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainOrari.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgLazio = (ImageView) findViewById(R.id.imgStartLazio);
        imgLazio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainLazio.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgM = findViewById(R.id.imgStartMappa);
        LinearLayout layMappa = findViewById(R.id.layBarraMappa);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            layMappa.setVisibility(LinearLayout.VISIBLE);
        } else {
            layMappa.setVisibility(LinearLayout.GONE);
        }
        imgM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainMappa.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgPass = findViewById(R.id.imgStartPassword);
        imgPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainPassword.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgDrive = findViewById(R.id.imgStartDrive);
        imgDrive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("");
                        Intent i = new Intent(context, GoogleDrive.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgBackup = findViewById(R.id.imgBackupSettings);
        imgBackup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainBackup.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        /* ImageView imgI = findViewById(R.id.imgStartImmagini);
        LinearLayout layImmagini = findViewById(R.id.layBarraImmagini);
        if (VariabiliStaticheStart.getInstance().isDetector() &&
            VariabiliStaticheStart.getInstance().isVisibileImmagini()) {
            layImmagini.setVisibility(LinearLayout.VISIBLE);
        } else {
            layImmagini.setVisibility(LinearLayout.GONE);
        }
        imgI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainMostraImmagini.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgPe = findViewById(R.id.imgStartPennetta);
        LinearLayout layPennetta = findViewById(R.id.layBarraPennetta);
        if (VariabiliStaticheStart.getInstance().isDetector() &&
                VariabiliStaticheStart.getInstance().isVisibilePennetta()) {
            layPennetta.setVisibility(LinearLayout.VISIBLE);
        } else {
            layPennetta.setVisibility(LinearLayout.GONE);
        }
        imgPe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainMostraPennetta.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        ImageView imgV = findViewById(R.id.imgStartVideo);
        LinearLayout layVideo = findViewById(R.id.layBarraVideo);
        if (VariabiliStaticheStart.getInstance().isDetector() &&
                VariabiliStaticheStart.getInstance().isVisibileVideo()) {
            layVideo.setVisibility(LinearLayout.VISIBLE);
        } else {
            layVideo.setVisibility(LinearLayout.GONE);
        }
        imgV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainMostraVideo.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 500);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                    }
                }, 100);
            }
        });

        Intent i = new Intent(context, MainPazzia.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i); */

        ImageView imgU = findViewById(R.id.imgStartUScita);
        imgU.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesGlobali.getInstance().ChiudeApplicazione(context);
            }
        });

        /* Intent iO = new Intent(context, MainOnomastici.class);
        iO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(iO);

        Notification notificaPlayer = GestioneNotifichePlayer.getInstance().StartNotifica(context);

        Intent myIntent = new Intent(
                this,
                MostraImmaginiLibrary.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(myIntent);

        Intent i = new Intent(context, MainOrari.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        String Operazione = "ApiFootball/Buttami1/Buttami2/Buttami3";
        String NomeFile = "Buttami.json";

        VariabiliStaticheGoogleDrive.getInstance().setOperazioneApiFootball(Operazione);
        VariabiliStaticheGoogleDrive.getInstance().setNomeFileApiFootball(NomeFile);
        VariabiliStaticheGoogleDrive.getInstance().setFileDiOrigine(
                context.getFilesDir() + "/ApiFootball/" +
                        "Fatte/Atalanta_2023.txt"
        );

        Intent i = new Intent(context, MainMMCategorieUguali.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("LeggeFileApiFootball");
        Intent apre = new Intent(context, GoogleDrive.class);
        apre.addCategory(Intent.CATEGORY_LAUNCHER);
        apre.setAction(Intent.ACTION_MAIN);
        apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        context.startActivity(apre);*/

        PrendeModelloTelefono p = new PrendeModelloTelefono();
        String modello = p.getDeviceName();
        if (!modello.contains("sdk_gphone64")) {
            String FileControllo = context.getFilesDir() + "/Controllo.txt";
            if (Files.getInstance().EsisteFile(FileControllo)) {
                UtilitiesGlobali.getInstance().ControllaNuovaVersione(context);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        int index = 0;

        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions) {
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                laySplash.setVisibility(LinearLayout.GONE);

                StartActivities();
            }
        };
        handlerTimer.postDelayed(rTimer, 3000);
    }

    private void StartActivities() {
        if (!VariabiliStaticheStart.getInstance().isGiaPartito()) {
            if (VariabiliStaticheStart.getInstance().getServizioForeground() == null) {
                VariabiliStaticheStart.getInstance().setServizioForeground(
                        new Intent(this, ServizioInterno.class));
                startForegroundService(VariabiliStaticheStart.getInstance().getServizioForeground());
            }
            VariabiliStaticheStart.getInstance().setGiaPartito(true);

            this.finish();
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 9999:
                if (data != null) {
                    Uri uri = data.getData();
                    Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                            DocumentsContract.getTreeDocumentId(uri));
                    ConverteNomeUri c = new ConverteNomeUri();
                    String path = c.getPath(this, docUri);

                    VariabiliStaticheWallpaper.getInstance().getTxtPath().setText(path);
                    VariabiliStaticheWallpaper.getInstance().setPercorsoIMMAGINI(path);

                    db_dati db = new db_dati(this);
                    db.ScriveImpostazioni();

                    ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(this);
                    bckLeggeImmaginiLocali.execute();

                    break;
                }
        }
    }
    */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        act.finish();

        super.onKeyDown(keyCode, event);

        /* Utility.getInstance().ScriveLog(this, NomeMaschera,
                "Tasto premuto: " + Integer.toString(keyCode));

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheStart.getInstance().ChiudeActivity(false);

                return false;
        } */

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "OnStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        VariabiliStaticheStart.getInstance().setMainActivity(this);
        VariabiliStaticheStart.getInstance().setContext(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key", "valore_importante");

        // UtilitiesGlobali.getInstance().InvioMail(context, "looigi@gmail.com", "Wallpaper Changer II", "On Save Instance State");

        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            String valore = savedInstanceState.getString("key");

            // UtilitiesGlobali.getInstance().InvioMail(context, "looigi@gmail.com", "Wallpaper Changer II", "On Restore Instance State");

            UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "onRestoreInstanceState");
        }
    }
}