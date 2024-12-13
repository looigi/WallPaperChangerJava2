package com.looigi.wallpaperchanger2.classeOrari;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.webService.ChiamateWSOrari;

import java.util.Calendar;
import java.util.Date;

public class MainOrari extends Activity {
    private Context context;
    private Activity act;

    public MainOrari() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_orari);

        context = this;
        act = this;

        ImageView imgIndietro = findViewById(R.id.imgIndietro);
        ImageView imgAvanti = findViewById(R.id.imgAvanti);
        TextView txtData = findViewById(R.id.txtData);
        TextView txtNomeGiorno = findViewById(R.id.txtNomeGiorno);
        VariabiliStaticheOrari.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoOrari));
        VariabiliStaticheOrari.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);

        VariabiliStaticheOrari.getInstance().setLayContenitore(findViewById(R.id.layContenitore));
        VariabiliStaticheOrari.getInstance().setTxtTipoLavoro(findViewById(R.id.txtTipoLavoro));
        VariabiliStaticheOrari.getInstance().setEdtOreLavoro(findViewById(R.id.txtOreLavoro));
        VariabiliStaticheOrari.getInstance().setEdtEntrata(findViewById(R.id.txtEntrata));
        VariabiliStaticheOrari.getInstance().setTxtLavoro(findViewById(R.id.txtLavoro));
        VariabiliStaticheOrari.getInstance().setTxtCommessa(findViewById(R.id.txtCommessa));
        VariabiliStaticheOrari.getInstance().setLayAggiunge(findViewById(R.id.layAggiunge));
        VariabiliStaticheOrari.getInstance().setLayDettaglioGiornata(findViewById(R.id.layDettaglioGiornata));
        VariabiliStaticheOrari.getInstance().setTxtTempo(findViewById(R.id.txtTempo));
        VariabiliStaticheOrari.getInstance().setEdtGradi(findViewById(R.id.txtGradi));
        VariabiliStaticheOrari.getInstance().setTxtPasticca(findViewById(R.id.txtPasticca));
        VariabiliStaticheOrari.getInstance().setEdtNote(findViewById(R.id.edtNote));
        VariabiliStaticheOrari.getInstance().setLstPranzo(findViewById(R.id.lstPranzo));
        VariabiliStaticheOrari.getInstance().setLstMezziAndata(findViewById(R.id.lstMezziAndata));
        VariabiliStaticheOrari.getInstance().setLstMezziRitorno(findViewById(R.id.lstMezziRitorno));

        VariabiliStaticheOrari.getInstance().getLayContenitore().setVisibility(LinearLayout.GONE);
        VariabiliStaticheOrari.getInstance().getLayAggiunge().setVisibility(LinearLayout.VISIBLE);

        VariabiliStaticheOrari.getInstance().setDataAttuale(new Date());

        imgIndietro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Date d = VariabiliStaticheOrari.getInstance().getDataAttuale();
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DATE, -1);

                VariabiliStaticheOrari.getInstance().setDataAttuale(c.getTime());

                ScriveData(context, txtData, txtNomeGiorno);
            }
        });

        imgAvanti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Date d = VariabiliStaticheOrari.getInstance().getDataAttuale();
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DATE, 1);

                VariabiliStaticheOrari.getInstance().setDataAttuale(c.getTime());

                ScriveData(context, txtData, txtNomeGiorno);
            }
        });

        ScriveData(context, txtData, txtNomeGiorno);
    }

    private void ScriveData(Context context, TextView txtData, TextView txtNomeGiorno) {
        String oggi = UtilityOrari.getInstance().RitornaData();
        String[] o = oggi.split(";");

        txtData.setText(o[0]);
        txtNomeGiorno.setText(o[1]);

        ChiamateWSOrari ws = new ChiamateWSOrari(context);
        ws.RitornaDatiGiorno();
    }
}
