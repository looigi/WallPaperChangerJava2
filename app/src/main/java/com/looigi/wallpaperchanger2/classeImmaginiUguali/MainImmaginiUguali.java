package com.looigi.wallpaperchanger2.classeImmaginiUguali;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.ChiamateWSMIU;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class MainImmaginiUguali extends Activity {
    private Context context;
    private Activity act;

    public MainImmaginiUguali() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_immagini_uguali);

        context = this;
        act = this;

        Intent intent = getIntent();
        String Categoria = intent.getStringExtra("CATEGORIA");

        VariabiliImmaginiUguali.getInstance().setTipoImpostato("Hash");
        VariabiliImmaginiUguali.getInstance().setCategoria(Categoria);

        /* VariabiliImmaginiUguali.getInstance().setLayPreview(findViewById(R.id.layPreview));
        VariabiliImmaginiUguali.getInstance().setImgPreview(findViewById(R.id.imgPreview));
        VariabiliImmaginiUguali.getInstance().getLayPreview().setVisibility(LinearLayout.GONE); */

        ChiamateWSMIU c = new ChiamateWSMIU(context);

        /* ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliImmaginiUguali.getInstance().getImgPreview().setImageBitmap(null);
                VariabiliImmaginiUguali.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);
            }
        }); */

        ImageView imgRefresh = findViewById(R.id.imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                c.RitornaImmaginiUguali(Categoria);
            }
        });

        final boolean[] primoIngresso = {true};
        Spinner spnTipo = findViewById(R.id.spnTipologie);
        spnTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Tipologia = adapterView.getItemAtPosition(position).toString();
                VariabiliImmaginiUguali.getInstance().setTipoImpostato(Tipologia);

                VariabiliImmaginiUguali.getInstance().RiempieListaTipi(context);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        String[] tipi = {"Hash", "Hash2", "Exif", "Nome", "Dimensioni", "PiccoleByte", "PiccoleSize"};
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, tipi);

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spnTipo,
                tipi,
                ""
        );

        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                tipi
        );
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipo.setAdapter(adapter); */

        VariabiliImmaginiUguali.getInstance().setLstTipi(findViewById(R.id.lstTipi));
        VariabiliImmaginiUguali.getInstance().setLstImmagini(findViewById(R.id.lstImmagini));

        VariabiliImmaginiUguali.getInstance().setImgCaricamentoInCorso(findViewById(R.id.imgCaricamentoMIU));
        VariabiliImmaginiUguali.getInstance().getImgCaricamentoInCorso().setVisibility(LinearLayout.GONE);

        c.RitornaImmaginiUguali(Categoria);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return false;
        }

        return false;
    }
}
