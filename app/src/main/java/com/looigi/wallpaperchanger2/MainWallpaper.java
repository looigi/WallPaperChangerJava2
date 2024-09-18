package com.looigi.wallpaperchanger2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.KeyEvent;

import com.looigi.wallpaperchanger2.classiStandard.InizializzaMaschera;
import com.looigi.wallpaperchanger2.classiStandard.ServizioInterno;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

public class MainWallpaper extends Activity {
    private static String NomeMaschera = "MAINACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wallpaper);

        VariabiliStaticheWallpaper.getInstance().setMainActivity(this);
        VariabiliStaticheWallpaper.getInstance().setContext(this);

        startService();
    }

    public void startService() {
        if (!isMyServiceRunning(ServizioInterno.class)) {
            Utility.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
            Utility.getInstance().ScriveLog(this, NomeMaschera, "AVVIO");
            Utility.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

            InizializzaMaschera i = new InizializzaMaschera();
            i.inizializzaMaschera(this, this);

            /* if (!VariabiliStaticheServizio.getInstance().isLetteImpostazioni()) {
                Utility.getInstance().ScriveLog(this, NomeMaschera, "Uscita per mancanza di impostazioni 1");
                Utility.getInstance().ApreToast(this, "Uscita per mancanza di impostazioni 1");

                Utility.getInstance().stopService(this);
                finish();
                System.exit(-1);
            } */

            VariabiliStaticheWallpaper.getInstance().ChiudeActivity(false);
        } else {
            Utility.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
            Utility.getInstance().ScriveLog(this, NomeMaschera, "RIAPERTURA DA NOTIFICA");
            Utility.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

            // Operazioni che si devono eseguire in caso di ripartenza col servizio attivo
            InizializzaMaschera i = new InizializzaMaschera();
            i.inizializzaMaschera(this, this);

            /* if (!VariabiliStaticheServizio.getInstance().isLetteImpostazioni()) {
                Utility.getInstance().ScriveLog(this, NomeMaschera, "Uscita per mancanza di impostazioni 2");
                Utility.getInstance().ApreToast(this, "Uscita per mancanza di impostazioni 2");

                Utility.getInstance().stopService(this);
                finish();
                System.exit(-1);
            } */
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        Utility.getInstance().ScriveLog(this, NomeMaschera,
                "Tasto premuto: " + Integer.toString(keyCode));

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheWallpaper.getInstance().ChiudeActivity(false);

                return false;
        }

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        Utility.getInstance().ScriveLog(this, NomeMaschera, "OnStop");
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
    protected void onRestart() {
        super.onRestart();

        Utility.getInstance().ScriveLog(this, NomeMaschera, "OnRestart");

        Utility.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
        Utility.getInstance().ScriveLog(this, NomeMaschera, "RIAPERTURA DA ICONA");
        Utility.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

        VariabiliStaticheWallpaper.getInstance().setMainActivity(this);
        VariabiliStaticheWallpaper.getInstance().setContext(this);

        // if (!VariabiliStaticheServizio.getInstance().isLetteImpostazioni()) {
            InizializzaMaschera i = new InizializzaMaschera();
            i.inizializzaMaschera(this, this);
        // }

        /* if (!VariabiliStaticheServizio.getInstance().isLetteImpostazioni()) {
            Utility.getInstance().ScriveLog(this, NomeMaschera, "Uscita per mancanza di impostazioni 3");
            Utility.getInstance().ApreToast(this, "Uscita per mancanza di impostazioni 3");

            Utility.getInstance().stopService(this);
            finish();
            System.exit(-1);
        } */
    }

    @Override
    protected void onPause() {
        super.onPause();

        Utility.getInstance().ScriveLog(this, NomeMaschera, "OnPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Utility.getInstance().ScriveLog(this, NomeMaschera, "OnDestroy");

        // Utility.getInstance().ChiudeApplicazione(this);
    }

}