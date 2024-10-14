package com.looigi.wallpaperchanger2.classiPlayer.impostazioniInterne;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiPlayer.AdapterListenerBrani;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classiPlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classiPlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classiPlayer.scan.ScanBraniNonPresentiSuDB;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;

import java.util.List;

public class brani_locali {
    private Activity act;
    private Context context;
    private String Filtro = "";

    public brani_locali(Activity act, Context context) {
        this.act = act;
        this.context = context;
    }

    public void impostaMaschera() {
        db_dati_player db = new db_dati_player(context);
        List<StrutturaBrano> lista = db.CaricaTuttiIBraniLocali();

        VariabiliStatichePlayer.getInstance().setTxtQuanteRicerca(act.findViewById(R.id.txtQuanteRicercaPL));

        ListView lstBrani = act.findViewById(R.id.lstBrani);
        AdapterListenerBrani customAdapterT = new AdapterListenerBrani(context, lista);
        lstBrani.setAdapter(customAdapterT);

        EditText edtFiltro = act.findViewById(R.id.edtFiltro);
        ImageView imgRicercaScelta = (ImageView) act.findViewById(R.id.imgRicercaSceltaPL);
        imgRicercaScelta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Filtro = edtFiltro.getText().toString();
                customAdapterT.updateData(Filtro);
            }
        });

        ImageView imgRefreshBrani = (ImageView) act.findViewById(R.id.imgRefreshBraniDB);
        imgRefreshBrani.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_player db = new db_dati_player(context);
                db.EliminaTutto();

                ScanBraniNonPresentiSuDB s = new ScanBraniNonPresentiSuDB();
                s.controllaCanzoniNonSalvateSuDB(context, true);
            }
        });

    }
}