package com.looigi.wallpaperchanger2.classeLazio.api_football;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.Files;

public class MainApiFootball extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_api_football);

        context = this;
        act = this;

        VariabiliStaticheApiFootball.getInstance().setPathApiFootball(context.getFilesDir() + "/ApiFootball");
        Files.getInstance().CreaCartelle(VariabiliStaticheApiFootball.getInstance().getPathApiFootball());
        VariabiliStaticheApiFootball.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoAF));
        UtilityApiFootball.getInstance().ImpostaAttesa(false);
        VariabiliStaticheApiFootball.getInstance().setLstSquadre(findViewById(R.id.lstSquadre));
        VariabiliStaticheApiFootball.getInstance().setLstPartite(findViewById(R.id.lstPartite));
        VariabiliStaticheApiFootball.getInstance().setLstGiocatoriCasa(findViewById(R.id.lstGiocatoriCasa));
        VariabiliStaticheApiFootball.getInstance().setLstGiocatoriFuori(findViewById(R.id.lstGiocatoriFuori));

        VariabiliStaticheApiFootball.getInstance().setAnnoScelto(2023);

        TextView txtAnno = findViewById(R.id.txtAnno);
        txtAnno.setText("Anno: " + Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoScelto()));

        // Prende Squadre anno scelto
        String urlString = "https://v3.football.api-sports.io/teams?" +
                "league=" + VariabiliStaticheApiFootball.idLegaSerieA + "&" +
                "season=" + VariabiliStaticheApiFootball.getInstance().getAnnoScelto();
        UtilityApiFootball.getInstance().EffettuaChiamata(
                context,
                urlString,
                "Squadre.json",
                false,
                "SQUADRE_LEGA"
        );
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
