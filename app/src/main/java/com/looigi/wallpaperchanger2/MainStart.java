package com.looigi.wallpaperchanger2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.AutoStart.RunServiceOnBoot;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.InizializzaMascheraDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiStandard.InizializzaMaschera;
import com.looigi.wallpaperchanger2.classiStandard.Permessi;
import com.looigi.wallpaperchanger2.classiStandard.ServizioInterno;
import com.looigi.wallpaperchanger2.gps.GestioneGPS;
import com.looigi.wallpaperchanger2.gps.Mappa;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

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

        if (!VariabiliStaticheStart.getInstance().isGiaPartito()) {
            Intent intent1 = new Intent(MainStart.this, RunServiceOnBoot.class);
            startService(intent1);

            VariabiliStaticheStart.getInstance().setContext(this);
            VariabiliStaticheStart.getInstance().setMainActivity(this);

            Utility.getInstance().generaPath(this);

            Permessi pp = new Permessi();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                VariabiliStaticheWallpaper.getInstance().setCiSonoPermessi(pp.ControllaPermessi(this));
            }

            if (VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {
                StartActivities();
            }
        }

        impostaSchermata();
    }

    private void impostaSchermata() {
        ImageView imgD = findViewById(R.id.imgStartDetector);
        imgD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                act.finish();
                VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
                VariabiliStaticheStart.getInstance().ChiudeActivity(true);

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
                        }, 1000);
                    }
                }, 1000);
            }
        });

        ImageView imgW = findViewById(R.id.imgStartWallpaper);
        imgW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                act.finish();
                VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                VariabiliStaticheStart.getInstance().ChiudeActivity(true);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, MainWallpaper.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                InizializzaMaschera i = new InizializzaMaschera();
                                i.inizializzaMaschera(
                                        context,
                                        VariabiliStaticheWallpaper.getInstance().getMainActivity());
                            }
                        }, 1000);
                    }
                }, 1000);
            }
        });

        ImageView imgM = findViewById(R.id.imgStartMappa);
        imgM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                act.finish();
                VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                VariabiliStaticheStart.getInstance().ChiudeActivity(true);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(context, Mappa.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }, 1000);
            }
        });

        GestioneGPS g = new GestioneGPS();
        g.AbilitaGPS(context);
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

        Utility.getInstance().ScriveLog(this, NomeMaschera, "OnStop");
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