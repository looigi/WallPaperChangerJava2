package com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeFetekkie.UtilityFetekkie;
import com.looigi.wallpaperchanger2.classeFetekkie.VariabiliStaticheMostraImmaginiFetekkie;
import com.looigi.wallpaperchanger2.classeFetekkie.db_dati_fetekkie;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiCategorieFE;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.webService.ChiamateWSIFC;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.DownloadImmagineSI;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.StrutturaImmagineDaScaricare;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.db_dati_wallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class MainImmaginiFuoriCategoria extends Activity {
    private Context context;
    private Activity act;

    public MainImmaginiFuoriCategoria() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_immagini_fuori_categoria);

        context = this;
        act = this;

        Intent intent = getIntent();
        VariabiliImmaginiFuoriCategoria.getInstance().setIdCategoria(intent.getStringExtra("IDCATEGORIA"));
        VariabiliImmaginiFuoriCategoria.getInstance().setCategoria(intent.getStringExtra("CATEGORIA"));

        VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento(
                VariabiliImmaginiFuoriCategoria.getInstance().getIdCategoria()
        );

        if (VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().contains("_")) {
            String[] Aliases = VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().split("_");
            VariabiliImmaginiFuoriCategoria.getInstance().setAlias1(Aliases[0]);
            VariabiliImmaginiFuoriCategoria.getInstance().setAlias2(Aliases[1]);
        } else {
            VariabiliImmaginiFuoriCategoria.getInstance().setAlias1(VariabiliImmaginiFuoriCategoria.getInstance().getCategoria());
            VariabiliImmaginiFuoriCategoria.getInstance().setAlias2("");
        }

        EditText edtAlias1 = findViewById(R.id.edtAlias1);
        edtAlias1.setText(VariabiliImmaginiFuoriCategoria.getInstance().getAlias1());
        edtAlias1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliImmaginiFuoriCategoria.getInstance().setAlias1(edtAlias1.getText().toString());
            }
        });

        EditText edtAlias2 = findViewById(R.id.edtAlias2);
        edtAlias2.setText(VariabiliImmaginiFuoriCategoria.getInstance().getAlias2());
        edtAlias2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliImmaginiFuoriCategoria.getInstance().setAlias2(edtAlias2.getText().toString());
            }
        });

        EditText edtCaratteri = findViewById(R.id.edtCaratteri);
        edtCaratteri.setText(Integer.toString(VariabiliImmaginiFuoriCategoria.getInstance().getQuantiCaratteri()));
        edtCaratteri.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliImmaginiFuoriCategoria.getInstance().setQuantiCaratteri(Integer.parseInt(edtCaratteri.getText().toString()));
            }
        });

        EditText edtTag = findViewById(R.id.edtTag);
        edtTag.setText(VariabiliImmaginiFuoriCategoria.getInstance().getTag());
        edtTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliImmaginiFuoriCategoria.getInstance().setTag(edtTag.getText().toString());
            }
        });

        Spinner spnAndOr = findViewById(R.id.spnAndOr);
        String[] l = { "And", "Or" };
        VariabiliImmaginiFuoriCategoria.getInstance().setAndOr("And");

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spnAndOr,
                l,
                VariabiliImmaginiFuoriCategoria.getInstance().getAndOr()
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                l
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAndOr.setAdapter(adapter);
        spnAndOr.setPrompt(VariabiliImmaginiFuoriCategoria.getInstance().getAndOr()); */
        final boolean[] primoIngresso = {true};
        spnAndOr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String AndOr = adapterView.getItemAtPosition(position).toString();
                VariabiliImmaginiFuoriCategoria.getInstance().setAndOr(AndOr);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        SwitchCompat swcSoloAltro = findViewById(R.id.switchSoloSuAltro);
        swcSoloAltro.setChecked(VariabiliImmaginiFuoriCategoria.getInstance().isSoloSuAltro());
        swcSoloAltro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliImmaginiFuoriCategoria.getInstance().setSoloSuAltro(isChecked);
            }
        });

        SwitchCompat swcCercaExif = findViewById(R.id.switchCercaSuExif);
        swcCercaExif.setChecked(VariabiliImmaginiFuoriCategoria.getInstance().isCercaExif());
        swcCercaExif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliImmaginiFuoriCategoria.getInstance().setCercaExif(isChecked);
            }
        });

        ImageView imgCerca = findViewById(R.id.imgCerca);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSIFC ws = new ChiamateWSIFC(context);
                ws.RitornaImmaginiFuoriCategoria("N");
            }
        });

        VariabiliImmaginiFuoriCategoria.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoIFC));
        VariabiliImmaginiFuoriCategoria.getInstance().setLstImmagini(findViewById(R.id.lstImmagini));
        VariabiliImmaginiFuoriCategoria.getInstance().setLaypreview(findViewById(R.id.layPreview));
        VariabiliImmaginiFuoriCategoria.getInstance().setImgPreview(findViewById(R.id.imgPreview));
        VariabiliImmaginiFuoriCategoria.getInstance().getLaypreview().setVisibility(LinearLayout.GONE);
        VariabiliImmaginiFuoriCategoria.getInstance().setTxtQuanteImmaginiRilevate(findViewById(R.id.txtQuanti));
        VariabiliImmaginiFuoriCategoria.getInstance().getTxtQuanteImmaginiRilevate().setText("");

        ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliImmaginiFuoriCategoria.getInstance().getLaypreview().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgSpostaTutte = findViewById(R.id.imgSpostaTutte);
        imgSpostaTutte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<StrutturaImmagineFuoriCategoria> lista = new ArrayList<>();
                for (StrutturaImmagineFuoriCategoria s : VariabiliImmaginiFuoriCategoria.getInstance().getListaImmagini()) {
                    if (s.isSelezionata()) {
                        lista.add(s);
                    }
                }

                if (!lista.isEmpty()) {
                    VariabiliImmaginiFuoriCategoria.getInstance().setListaDaSpostare(lista);
                    VariabiliImmaginiFuoriCategoria.getInstance().setQualeImmagineStaSpostando(0);
                    int quale = VariabiliImmaginiFuoriCategoria.getInstance().getQualeImmagineStaSpostando();
                    VariabiliImmaginiFuoriCategoria.getInstance().setStaSpostandoTutte(true);
                    VariabiliImmaginiFuoriCategoria.getInstance().setQualeImmagineStaSpostando(quale);

                    VariabiliImmaginiFuoriCategoria.getInstance().ScaricaProssimaImmagine(context, 0);
                }
            }
        });

        ChiamateWSIFC ws = new ChiamateWSIFC(context);
        ws.RitornaImmaginiFuoriCategoria("N");
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
