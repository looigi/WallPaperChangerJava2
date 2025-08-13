package com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.VariabiliStaticheUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.VariabiliStaticheControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.adapters.AdapterListenerListaControllo;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.webService.ChiamateWSV;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.db_dati_ui;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class MainVolti extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_volti);

        context = this;
        act = this;

        VariabiliStaticheVolti.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoV));
        VariabiliStaticheVolti.getInstance().Attesa(false);

        VariabiliStaticheVolti.getInstance().setLstVolti(findViewById(R.id.lstVolti));
        VariabiliStaticheVolti.getInstance().setLayPreview(findViewById(R.id.layPreview));
        VariabiliStaticheVolti.getInstance().setImgPreview(findViewById(R.id.imgPreview));
        VariabiliStaticheVolti.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);

        ChiamateWSV ws = new ChiamateWSV(context);
        ws.RitornaVoltiRilevati();

        ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheVolti.getInstance().getImgPreview().setImageBitmap(null);
                VariabiliStaticheVolti.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return true;
        }

        return false;
    }
}
