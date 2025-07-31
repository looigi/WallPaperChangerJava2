package com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.VariabiliStaticheImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerFonti;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.UtilityUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.VariabiliStaticheUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.adapters.AdapterListenerListaControllo;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.db_dati_ui;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class MainControlloImmagini extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_controllo_immagini);

        context = this;
        act = this;

        Intent intent = getIntent();
        String idCategoria = intent.getStringExtra("idCategoria");
        VariabiliStaticheControlloImmagini.getInstance().setIdCategoria(Integer.parseInt(idCategoria));
        String Categoria = "";
        for (StrutturaImmaginiCategorie s1 : VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM()) {
            String idCat = Integer.toString(s1.getIdCategoria()).trim();
            if (idCat.equals(idCategoria.trim())) {
                Categoria = s1.getCategoria();
                break;
            }
        }
        VariabiliStaticheControlloImmagini.getInstance().setCategoria(Categoria);

        VariabiliStaticheControlloImmagini.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoCI));
        VariabiliStaticheControlloImmagini.getInstance().Attesa(false);

        VariabiliStaticheControlloImmagini.getInstance().setLstLista(findViewById(R.id.lstLista));

        db_dati_ui db = new db_dati_ui(context);
        VariabiliStaticheControlloImmagini.getInstance().setStrutturaDati(db.LeggeDatiCategoria(idCategoria));
        db.ChiudeDB();

        Spinner spnTipo = findViewById(R.id.spnTipologie);
        String[] Tipi = { "Errate", "Piccole", "Inesistenti", "Grandi" };
        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spnTipo,
                Tipi,
                "Errate"
        );
        VariabiliStaticheControlloImmagini.getInstance().setTipologia("Errate");
        CaricaDati();

        final boolean[] primoIngresso = {true};
        spnTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Tipologia = adapterView.getItemAtPosition(position).toString();
                VariabiliStaticheControlloImmagini.getInstance().setTipologia(Tipologia);

                CaricaDati();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        VariabiliStaticheControlloImmagini.getInstance().setLayPreview(findViewById(R.id.layPreview));
        VariabiliStaticheControlloImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);
        VariabiliStaticheControlloImmagini.getInstance().setImgPreview(findViewById(R.id.imgPreview));

        ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheControlloImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);
            }
        });
    }

    private void CaricaDati() {
        List<String> lista = new ArrayList<>();

        switch (VariabiliStaticheControlloImmagini.getInstance().getTipologia()) {
            case "Errate":
                lista = VariabiliStaticheControlloImmagini.getInstance().getStrutturaDati().getListaErrate();
                break;
            case "Piccole":
                lista = VariabiliStaticheControlloImmagini.getInstance().getStrutturaDati().getListaPiccole();
                break;
            case "Inesistenti":
                lista = VariabiliStaticheControlloImmagini.getInstance().getStrutturaDati().getListaInesistenti();
                break;
            case "Grandi":
                lista = VariabiliStaticheControlloImmagini.getInstance().getStrutturaDati().getListaGrandi();
                break;
        }

        AdapterListenerListaControllo cstmAdptFonti = new AdapterListenerListaControllo(context, lista);
        VariabiliStaticheControlloImmagini.getInstance().getLstLista().setAdapter(cstmAdptFonti);
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
