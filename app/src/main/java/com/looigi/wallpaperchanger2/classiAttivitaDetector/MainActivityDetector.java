package com.looigi.wallpaperchanger2.classiAttivitaDetector;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import java.util.HashMap;
import java.util.Map;

public class MainActivityDetector extends Activity {
    private static String NomeMaschera = "MAINACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detector);

        VariabiliStaticheDetector.getInstance().setMainActivity(this);
        VariabiliStaticheDetector.getInstance().setContext(this);

        UtilityDetector.getInstance().generaPath(this);

        if (!VariabiliStaticheDetector.getInstance().isMascheraPartita()) {
            UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
            UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "AVVIO");
            UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

            InizializzaMascheraDetector i = new InizializzaMascheraDetector();
            i.inizializzaMaschera(this, this);

            if (!VariabiliStaticheDetector.getInstance().isLetteImpostazioni()) {
                Toast.makeText(this,
                        VariabiliStaticheDetector.channelName + ": Uscita per mancanza di impostazioni 1",
                        Toast.LENGTH_LONG).show();

                finish();
                // System.exit(-1);
            }

            this.moveTaskToBack(false);

            VariabiliStaticheDetector.getInstance().setMascheraPartita(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "Tasto premuto: " + Integer.toString(keyCode));

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.moveTaskToBack(true);

                return false;
        }

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "OnStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "OnRestart");

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "RIAPERTURA DA NOTIFICA");
        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

        /* if (!VariabiliStaticheDetector.getInstance().isLetteImpostazioni()) {
            InizializzaMascheraDetector i = new InizializzaMascheraDetector();
            i.inizializzaMaschera(this, this);
        } */
    }

    @Override
    protected void onPause() {
        super.onPause();

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "OnPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "OnDestroy");
    }
}