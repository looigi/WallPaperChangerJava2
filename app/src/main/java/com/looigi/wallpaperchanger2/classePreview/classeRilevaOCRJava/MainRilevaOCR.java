package com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.MainImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.webService.ChiamateWSRilevaOCR;

public class MainRilevaOCR extends Activity {
    private Context context;
    private Activity act = this;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_rileva_ocr);

        context = this;
        act = this;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::CpuLock");
        wakeLock.acquire();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        VariabiliStaticheRilevaOCRJava.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoOCR));
        UtilitiesRilevaOCRJava.getInstance().Attesa(false);

        VariabiliStaticheRilevaOCRJava.getInstance().setImgImmagine(findViewById(R.id.imgImmagine));

        VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(true);

        ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
        Button btnFerma = (findViewById(R.id.btnFerma));
        btnFerma.setText("Ferma");
        btnFerma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheRilevaOCRJava.getInstance().isStaElaborando()) {
                    VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(false);
                    btnFerma.setText("Parti");
                } else {
                    VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(true);
                    btnFerma.setText("Ferma");

                    ws.RitornaProssimaImmagineDaLeggereInJava();
                }
            }
        });
        VariabiliStaticheRilevaOCRJava.getInstance().setTxtAvanzamento(findViewById(R.id.txtAvanzamento));

        ws.RitornaProssimaImmagineDaLeggereInJava();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(false);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        act.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(false);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                if (wakeLock != null && wakeLock.isHeld()) {
                    wakeLock.release();
                }
                this.finish();

                return true;
        }

        return false;
    }
}
