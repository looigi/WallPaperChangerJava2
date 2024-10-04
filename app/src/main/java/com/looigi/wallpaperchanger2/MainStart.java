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
import com.looigi.wallpaperchanger2.classiDetector.InizializzaMascheraDetector;
import com.looigi.wallpaperchanger2.classiDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classiDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiPlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classiPlayer.MainPlayer;
import com.looigi.wallpaperchanger2.classiPlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classiWallpaper.InizializzaMascheraWallpaper;
import com.looigi.wallpaperchanger2.classiWallpaper.MainWallpaper;
import com.looigi.wallpaperchanger2.classiStandard.Permessi;
import com.looigi.wallpaperchanger2.classiStandard.ServizioInterno;
import com.looigi.wallpaperchanger2.classiGps.GestioneGPS;
import com.looigi.wallpaperchanger2.classiGps.GestioneMappa;
import com.looigi.wallpaperchanger2.classiGps.Mappa;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classiGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classiGps.db_dati_gps;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainStart  extends Activity {
    private static String NomeMaschera = "MAINSTART";
    private Context context;
    private Activity act;

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
            Intent intent1 = new Intent(MainStart.this, RunServiceOnBoot.class);
            startService(intent1);

            VariabiliStaticheStart.getInstance().setContext(this);
            VariabiliStaticheStart.getInstance().setMainActivity(this);

            // UtilityWallpaper.getInstance().generaPath(this);

            Permessi pp = new Permessi();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                VariabiliStaticheWallpaper.getInstance().setCiSonoPermessi(pp.ControllaPermessi(this));
            }

            if (VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {
                StartActivities();
            }
        }

        LinearLayout laySplash = findViewById(R.id.laySplash);

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                laySplash.setVisibility(LinearLayout.GONE);
                impostaSchermata();
            }
        };
        handlerTimer.postDelayed(rTimer, 5000);
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

        TextView txtLabelDet = findViewById(R.id.txtStartLabelDet);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            imgD.setVisibility(LinearLayout.VISIBLE);
            txtLabelDet.setVisibility(LinearLayout.VISIBLE);
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

        ImageView imgP = findViewById(R.id.imgStartPlayer);
        imgP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
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

        ImageView imgM = findViewById(R.id.imgStartMappa);
        TextView txtLabelMap = findViewById(R.id.txtStartLabelMap);
        if (VariabiliStaticheStart.getInstance().isDetector()) {
            imgM.setVisibility(LinearLayout.VISIBLE);
            txtLabelMap.setVisibility(LinearLayout.VISIBLE);
        }
        imgM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStart.setVisibility(LinearLayout.GONE);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, Mappa.class);
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

        if (VariabiliStaticheStart.getInstance().isDetector()) {
            db_dati_gps db = new db_dati_gps(context);
            db.CaricaAccensioni(context);

            // VariabiliStaticheGPS.getInstance().setGpsAttivo(true);

            GestioneGPS g = new GestioneGPS();
            VariabiliStaticheGPS.getInstance().setGestioneGPS(g);
            g.AbilitaTimer(context);
            g.AbilitaGPS(context);
        }




        /* Intent iP = new Intent(context, MainPlayer.class);
        iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(iP);

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

        Handler handlerTimer = new Handler();
        Runnable rTimer = new Runnable() {
            public void run() {
                StartActivities();
            }
        };
        handlerTimer.postDelayed(rTimer, 3000);
    }

    private void StartActivities() {
        VariabiliStaticheWallpaper.getInstance().setServizioForeground(new Intent(this, ServizioInterno.class));
        startForegroundService(VariabiliStaticheWallpaper.getInstance().getServizioForeground());

        VariabiliStaticheStart.getInstance().setGiaPartito(true);
        // WALLPAPER Parte nel servizio

        // DETECTOR PARTE IN WALLPAPER DOPO LA LETTURA DELLE VARIABILI SALVATE PER CAPIRE
        // SE SI DEVE APRIRE (InizializzaMaschera)

        this.finish();
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