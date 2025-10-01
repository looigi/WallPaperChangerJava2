package com.looigi.wallpaperchanger2.Orari.impostazioni.Commesse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Orari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.Orari.webService.ChiamateWSOrari;

public class MainCommessa extends Activity {
    private Context context;
    private Activity act;

    public MainCommessa() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_commessa);

        context = this;
        act = this;

        ImageView imgSalvaCommessa = findViewById(R.id.imgSalvaCommessa);
        imgSalvaCommessa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (VariabiliStaticheImpostazioniOrari.getInstance().isDatiModificati()) {
                    ChiamateWSOrari ws = new ChiamateWSOrari(context);
                    ws.RitornaDatiPerModifica(true, false);
                }
                this.finish();

                return false;
        }

        return false;
    }
}
