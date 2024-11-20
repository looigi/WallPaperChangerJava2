package com.looigi.wallpaperchanger2.classePlayer.preferiti_tags;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGps.db_dati_gps;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;

public class Main_Preferiti_Tags extends Activity {
    private Activity act;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_preferiti_tags);

        context = this;
        act = this;

        Intent intent = getIntent();
        String id = intent.getStringExtra("DO");

        VaribiliStatichePrefTags.getInstance().setTipoOperazione(id);

        boolean preferiti = false;

        switch (id) {
            case "Preferiti":
                VaribiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        VariabiliStatichePlayer.getInstance().getPreferiti()
                );
                preferiti = true;
                break;
            case "PreferitiElimina":
                VaribiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        VariabiliStatichePlayer.getInstance().getPreferitiElimina()
                );
                preferiti = true;
                break;
            case "Tags":
                VaribiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        VariabiliStatichePlayer.getInstance().getPreferitiTags()
                );
                break;
            case "TagsElimina":
                VaribiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        VariabiliStatichePlayer.getInstance().getPreferitiEliminaTags()
                );
                break;
        }

        VaribiliStatichePrefTags.getInstance().setLstArtisti(findViewById(R.id.lstArtisti));
        VaribiliStatichePrefTags.getInstance().setTxtQuanti(findViewById(R.id.txtQuantiArtisti));
        VaribiliStatichePrefTags.getInstance().setTxtSelezionati(findViewById(R.id.txtSelezionati));

        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
        if (preferiti) {
            ws.RitornaListaArtisti(false, true);

            if (!VariabiliStatichePlayer.getInstance().getListaArtisti().isEmpty()) {
                AdapterListenerPreferiti customAdapterA = new AdapterListenerPreferiti(context,
                        VariabiliStatichePlayer.getInstance().getListaArtisti());
                VaribiliStatichePrefTags.getInstance().getLstArtisti().setAdapter(customAdapterA);
                VaribiliStatichePrefTags.getInstance().setCustomAdapterPref(customAdapterA);
            }
        } else {
            ws.RitornaListaTags(false);
        }

        EditText edtFiltro = findViewById(R.id.edtFiltroArtisti);
        ImageView imgCerca = findViewById(R.id.imgFiltroArtisti);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Filtro = edtFiltro.getText().toString();

                if (VaribiliStatichePrefTags.getInstance().getCustomAdapterPref() != null) {
                    VaribiliStatichePrefTags.getInstance().getCustomAdapterPref().updateData(Filtro);
                }
            }
        });
        ImageView imgRefresh = findViewById(R.id.imgRefreshArtisti);
        boolean finalPreferiti = preferiti;
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (finalPreferiti) {
                    ws.RitornaListaArtisti(false, true);
                } else {
                    ws.RitornaListaTags(false);
                }
            }
        });

        SwitchCompat sSoloSel = act.findViewById(R.id.sSoloSelezionati);
        sSoloSel.setChecked(false);
        sSoloSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VaribiliStatichePrefTags.getInstance().setSoloSelezionati(sSoloSel.isChecked());

                String Filtro = edtFiltro.getText().toString();

                if (VaribiliStatichePrefTags.getInstance().getCustomAdapterPref() != null) {
                    VaribiliStatichePrefTags.getInstance().getCustomAdapterPref().updateData(Filtro);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        db_dati_gps db = new db_dati_gps(context);
        db.ScriveImpostazioni();
        db.ChiudeDB();

        UtilityPlayer.getInstance().ScrivePreferitiTags();
    }

}
