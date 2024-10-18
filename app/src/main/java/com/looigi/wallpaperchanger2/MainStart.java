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
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFilms.MainMostraFilms;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeDetector.InizializzaMascheraDetector;
import com.looigi.wallpaperchanger2.classeDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeOnomastici.MainOnomastici;
import com.looigi.wallpaperchanger2.classePlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.MainPlayer;
import com.looigi.wallpaperchanger2.classeWallpaper.InizializzaMascheraWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.MainWallpaper;
import com.looigi.wallpaperchanger2.classeStandard.Permessi;
import com.looigi.wallpaperchanger2.classeStandard.ServizioInterno;
import com.looigi.wallpaperchanger2.classeGps.GestioneMappa;
import com.looigi.wallpaperchanger2.classeGps.MainMappa;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.utilities.CaricaSettaggi;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainStart  extends Activity {
    private static String NomeMaschera = "Main_Start";
    private Context context;
    private Activity act;
    private LinearLayout laySplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

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
            }
        }
    }

    private void impostaSchermata() {
        LinearLayout layStart = findViewById(R.id.layStart);

        if (VariabiliStaticheStart.getInstance().isDetector()) {
            GestioneMappa m = new GestioneMappa(this);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
            String dataOdierna = sdfD.format(calendar.getTime());
            m.LeggePunti(dataOdierna);
            VariabiliStaticheGPS.getInstance().setMappa(m);
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
        }); */

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
        this.startActivity(myIntent); */
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
            VariabiliStaticheWallpaper.getInstance().setServizioForeground(new Intent(this, ServizioInterno.class));
            startForegroundService(VariabiliStaticheWallpaper.getInstance().getServizioForeground());

            VariabiliStaticheStart.getInstance().setGiaPartito(true);
            // WALLPAPER Parte nel servizio

            // DETECTOR PARTE IN WALLPAPER DOPO LA LETTURA DELLE VARIABILI SALVATE PER CAPIRE
            // SE SI DEVE APRIRE (InizializzaMaschera)

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

}