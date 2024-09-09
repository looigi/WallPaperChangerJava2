package com.looigi.wallpaperchanger2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.looigi.wallpaperchanger2.AutoStart.RunServiceOnBoot;
import com.looigi.wallpaperchanger2.classiStandard.InizializzaMaschera;
import com.looigi.wallpaperchanger2.classiStandard.Permessi;
import com.looigi.wallpaperchanger2.classiStandard.ServizioInterno;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static String NomeMaschera = "MAINACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VariabiliStaticheServizio.getInstance().setMainActivity(this);
        // VariabiliStaticheServizio.getInstance().setSecondiDiAttesaContatore(60L);

        Utility.getInstance().generaPath();

        if (!isMyServiceRunning(RunServiceOnBoot.class)) {
            Intent intent1 = new Intent(MainActivity.this, RunServiceOnBoot.class);
            startService(intent1);
        }

        Permessi pp = new Permessi();
        VariabiliStaticheServizio.getInstance().setCiSonoPermessi(pp.ControllaPermessi(this));

        if (VariabiliStaticheServizio.getInstance().isCiSonoPermessi()) {
            startService();
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
                startService();
            }
        };
        handlerTimer.postDelayed(rTimer, 3000);
    }

    public void startService() {
        if (!isMyServiceRunning(ServizioInterno.class)) {
            Utility.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
            Utility.getInstance().ScriveLog(this, NomeMaschera, "AVVIO");
            Utility.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

            VariabiliStaticheServizio.getInstance().setServizioForeground(new Intent(this, ServizioInterno.class));
            startService(VariabiliStaticheServizio.getInstance().getServizioForeground());

            InizializzaMaschera i = new InizializzaMaschera();
            i.inizializzaMaschera(this, this);

            moveTaskToBack(false);
        } else {
            Utility.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
            Utility.getInstance().ScriveLog(this, NomeMaschera, "RIAPERTURA DA NOTIFICA");
            Utility.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

            // Operazioni che si devono eseguire in caso di ripartenza col servizio attivo
            InizializzaMaschera i = new InizializzaMaschera();
            i.inizializzaMaschera(this, this);
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

        Utility.getInstance().ScriveLog(this, NomeMaschera, "OnRestart");

        Utility.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
        Utility.getInstance().ScriveLog(this, NomeMaschera, "RIAPERTURA DA ICONA");
        Utility.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

        // InizializzaMaschera i = new InizializzaMaschera();
        // i.inizializzaMaschera(this, this);
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

        Utility.getInstance().stopService(this);
    }

}