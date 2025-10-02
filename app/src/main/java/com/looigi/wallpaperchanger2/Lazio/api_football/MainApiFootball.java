package com.looigi.wallpaperchanger2.Lazio.api_football;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.Lazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Lazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

public class MainApiFootball extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_api_football);

        context = this;
        act = this;

        VariabiliStaticheApiFootball.getInstance().setTxtAvanzamento(findViewById(R.id.txtAvanzamento));
        VariabiliStaticheApiFootball.getInstance().getTxtAvanzamento().setText("");

        Bundle b = getIntent().getExtras();
        int idAnnoScelto = -1;
        String annoScelto = "";
        if(b != null) {
            idAnnoScelto = b.getInt("idAnnoScelto");
            annoScelto = b.getString("AnnoScelto");
        }
        if (idAnnoScelto > 1) {
            String[] a = annoScelto.split("-");
            VariabiliStaticheApiFootball.getInstance().setAnnoIniziale(Integer.parseInt(a[0]));

            VariabiliStaticheApiFootball.getInstance().setIdAnnoScelto(idAnnoScelto);
            VariabiliStaticheApiFootball.getInstance().setAnnoScelto(annoScelto);

            VariabiliStaticheApiFootball.getInstance().setPathApiFootball(context.getFilesDir() + "/ApiFootball");
            Files.getInstance().CreaCartelle(VariabiliStaticheApiFootball.getInstance().getPathApiFootball());
            VariabiliStaticheApiFootball.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoAF));
            UtilitiesGlobali.getInstance().AttesaGif(
                    context,
                    VariabiliStaticheApiFootball.getInstance().getImgCaricamento(),
                    false
            );

            VariabiliStaticheApiFootball.getInstance().setLstSquadre(findViewById(R.id.lstSquadre));
            VariabiliStaticheApiFootball.getInstance().setLstPartite(findViewById(R.id.lstPartite));
            VariabiliStaticheApiFootball.getInstance().setLstGiocatoriCasa(findViewById(R.id.lstGiocatoriCasa));
            VariabiliStaticheApiFootball.getInstance().setLstGiocatoriFuori(findViewById(R.id.lstGiocatoriFuori));

            TextView txtAnno = findViewById(R.id.txtAnno);
            txtAnno.setText("Anno: " + VariabiliStaticheApiFootball.getInstance().getAnnoScelto());

            // Prende Squadre anno scelto
            String urlString = "https://v3.football.api-sports.io/teams?" +
                    "league=" + VariabiliStaticheApiFootball.idLegaSerieA + "&" +
                    "season=" + Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale());
            UtilityApiFootball u = new UtilityApiFootball();
            u.EffettuaChiamata(
                    context,
                    urlString,
                    "Squadre.json",
                    false,
                    "SQUADRE_LEGA"
            );
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "id Anno non valido");
        }

        ImageView imgSalvaTutteLeSquadre = findViewById(R.id.imgSalvaTutteLeSquadre);
        imgSalvaTutteLeSquadre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheApiFootball.getInstance().setIndiceSalvataggioTutteLeSquadre(0);
                VariabiliStaticheApiFootball.getInstance().setStaSalvandoTutteLeSquadre(true);
                VariabiliStaticheApiFootball.getInstance().setSquadreAggiunte(0);

                UtilityApiFootball u = new UtilityApiFootball();
                u.SalvaTutteLeSquadre(context);
            }
        });

        ImageView imgSalvaTutteLePartite = findViewById(R.id.imgSalvaTutteLePartite);
        imgSalvaTutteLePartite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheApiFootball.getInstance().setStaSalvandoTutteLePartite(true);
                VariabiliStaticheApiFootball.getInstance().setIndiceSalvataggioTutteLePartite(0);

                UtilityApiFootball u = new UtilityApiFootball();
                u.SalvaTutteLePartite(context);
            }
        });

        ImageView imgSalvaTuttiIGiocatoriCasa = findViewById(R.id.imgScriveRisultati);
        imgSalvaTuttiIGiocatoriCasa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.ScriveRisultati();
            }
        });

        /* ImageView imgSalvaTuttiIGiocatoriFuori = findViewById(R.id.imgSalvaTuttiIGiocatoriFuori);
        imgSalvaTuttiIGiocatoriFuori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        }); */
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
