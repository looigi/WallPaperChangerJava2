package com.looigi.wallpaperchanger2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.AutoStart.RunServiceOnBoot;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.ConverteNomeUri;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.ScannaDiscoPerImmaginiLocali;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.db_dati;
import com.looigi.wallpaperchanger2.classiStandard.Permessi;
import com.looigi.wallpaperchanger2.classiStandard.ServizioInterno;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import java.util.HashMap;
import java.util.Map;

public class MainStart  extends Activity {
    private static String NomeMaschera = "MAINSTART";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        context = this;

        Intent intent1 = new Intent(MainStart.this, RunServiceOnBoot.class);
        startService(intent1);

        VariabiliStaticheServizio.getInstance().setMainActivity(this);

        Utility.getInstance().generaPath(this);

        Permessi pp = new Permessi();
        VariabiliStaticheServizio.getInstance().setCiSonoPermessi(pp.ControllaPermessi(this));

        if (VariabiliStaticheServizio.getInstance().isCiSonoPermessi()) {
            StartActivities();
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

        Handler handlerTimer = new Handler();
        Runnable rTimer = new Runnable() {
            public void run() {
                StartActivities();
            }
        };
        handlerTimer.postDelayed(rTimer, 3000);
    }

    private void StartActivities() {
        VariabiliStaticheServizio.getInstance().setServizioForeground(new Intent(this, ServizioInterno.class));
        startForegroundService(VariabiliStaticheServizio.getInstance().getServizioForeground());

        // WALLPAPER Parte nel servizio

        // DETECTOR PARTE IN WALLPAPER DOPO LA LETTURA DELLE VARIABILI SALVATE PER CAPIRE
        // SE SI DEVE APRIRE (InizializzaMaschera)
    }

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

                    VariabiliStaticheServizio.getInstance().getTxtPath().setText(path);
                    VariabiliStaticheServizio.getInstance().setPercorsoIMMAGINI(path);

                    db_dati db = new db_dati(this);
                    db.ScriveImpostazioni();

                    ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(this);
                    bckLeggeImmaginiLocali.execute();

                    break;
                }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        Utility.getInstance().ScriveLog(this, NomeMaschera,
                "Tasto premuto: " + Integer.toString(keyCode));

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(true);

                return false;
        }

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