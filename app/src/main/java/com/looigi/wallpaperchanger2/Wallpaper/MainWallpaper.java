package com.looigi.wallpaperchanger2.Wallpaper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Avvio.ServizioInterno;

public class MainWallpaper extends Activity {
    private static String NomeMaschera = "Main_Activity_Wallpaper";

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
            UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
            UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "AVVIO");
            UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

            InizializzaMascheraWallpaper i = new InizializzaMascheraWallpaper();
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
            UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
            UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "RIAPERTURA DA NOTIFICA");
            UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

            // Operazioni che si devono eseguire in caso di ripartenza col servizio attivo
            InizializzaMascheraWallpaper i = new InizializzaMascheraWallpaper();
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

        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera,
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

        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "OnStop");
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

        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "OnRestart");

        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "RIAPERTURA DA ICONA");
        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

        VariabiliStaticheWallpaper.getInstance().setMainActivity(this);
        VariabiliStaticheWallpaper.getInstance().setContext(this);

        if (VariabiliStaticheWallpaper.getInstance().isApreRicerca()) {
            VariabiliStaticheWallpaper.getInstance().setApreRicerca(false);
        } else {
           // if (!VariabiliStaticheServizio.getInstance().isLetteImpostazioni()) {
            InizializzaMascheraWallpaper i = new InizializzaMascheraWallpaper();
            i.inizializzaMaschera(this, this);
            // }
        }
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

        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "OnPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UtilityWallpaper.getInstance().ScriveLog(this, NomeMaschera, "OnDestroy");

        // Utility.getInstance().ChiudeApplicazione(this);
    }

}