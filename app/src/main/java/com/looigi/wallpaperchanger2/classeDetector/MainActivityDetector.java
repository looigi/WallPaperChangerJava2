package com.looigi.wallpaperchanger2.classeDetector;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.R;

import java.util.Objects;

public class MainActivityDetector extends Activity {
    private static String NomeMaschera = "Main_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detector);

        VariabiliStaticheDetector.getInstance().setMainActivity(this);
        VariabiliStaticheDetector.getInstance().setContext(this);

        // UtilityDetector.getInstance().generaPath(this);

        if (!VariabiliStaticheDetector.getInstance().isMascheraPartita()) {
            UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "\n----------------------------");
            UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "AVVIO");
            UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "----------------------------");

            InizializzaMascheraDetector i = new InizializzaMascheraDetector();
            i.inizializzaMaschera(this, this);

            /* if (!VariabiliStaticheDetector.getInstance().isLetteImpostazioni()) {
                Toast.makeText(this,
                        VariabiliStaticheDetector.channelName + ": Uscita per mancanza di impostazioni 1",
                        Toast.LENGTH_LONG).show();

                // finish();
                VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                // System.exit(-1);
            } */

            // VariabiliStaticheDetector.getInstance().ChiudeActivity(false);

            VariabiliStaticheDetector.getInstance().setMascheraPartita(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "Tasto premuto: " + Integer.toString(keyCode));

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheDetector.getInstance().ChiudeActivity(false);

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

        VariabiliStaticheDetector.getInstance().setMainActivity(this);
        VariabiliStaticheDetector.getInstance().setContext(this);

        // if (!VariabiliStaticheDetector.getInstance().isLetteImpostazioni()) {

        if (!VariabiliStaticheDetector.getInstance().isRiaperturaSenzaReimpostazione()) {
            InizializzaMascheraDetector i = new InizializzaMascheraDetector();
            i.inizializzaMaschera(this, this);

            VariabiliStaticheDetector.getInstance().setRiaperturaSenzaReimpostazione(false);
        }

        // }

        /* if (VariabiliStaticheDetector.getInstance().isChiudiActivity()) {
            Handler handlerTimer;
            Runnable rTimer;

            final int[] conta = {0};
            handlerTimer = new Handler(Looper.getMainLooper());
            rTimer = new Runnable() {
                public void run() {
                    if (VariabiliStaticheDetector.getInstance().isCameraImpostata()) {
                        VariabiliStaticheDetector.getInstance().setChiudiActivity(false);

                        moveTaskToBack(true);
                    } else {
                        conta[0]++;
                        if (conta[0] > 20) {
                            // CAMERA NON ATTIVATA IN TEMPO
                            handlerTimer.removeCallbacks(this);
                        } else {
                            handlerTimer.postDelayed(this, 150);
                        }
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 150);
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