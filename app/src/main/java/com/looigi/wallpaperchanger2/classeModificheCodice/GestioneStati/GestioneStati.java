package com.looigi.wallpaperchanger2.classeModificheCodice.GestioneStati;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeModificheCodice.VariabiliStaticheModificheCodice;
import com.looigi.wallpaperchanger2.classeModificheCodice.webService.ChiamateWSModifiche;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class GestioneStati extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gestionestati);

        context = this;
        act = this;

        VariabiliStaticheModificheCodice.getInstance().setLstModificheStati(findViewById(R.id.lstStati));
        VariabiliStaticheModificheCodice.getInstance().setLayGestioneStato(findViewById(R.id.layStato));
        VariabiliStaticheModificheCodice.getInstance().getLayGestioneStato().setVisibility(LinearLayout.GONE);

        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
        ws.RitornaStati(true);

        VariabiliStaticheModificheCodice.getInstance().setEdtGestioneStato(findViewById(R.id.edtStato));

        ImageView imgNuovo = findViewById(R.id.imgNuovoStato);
        imgNuovo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModificheCodice.getInstance().getLayGestioneStato().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheModificheCodice.getInstance().getEdtGestioneStato().setText("");
                VariabiliStaticheModificheCodice.getInstance().setIdGestioneStato(-1);
            }
        });

        ImageView imgSalva = findViewById(R.id.imgSalva);
        imgSalva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Stato = VariabiliStaticheModificheCodice.getInstance().getEdtGestioneStato().getText().toString();
                if (Stato.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire un valore per lo stato");
                    return;
                }
                Stato = UtilitiesGlobali.getInstance().MetteMaiuscole(Stato);
                int idS = VariabiliStaticheModificheCodice.getInstance().getIdGestioneStato();
                String idStato = "";
                if (idS == -1) {
                    idStato = "";
                } else {
                    idStato = String.valueOf(idS);
                }
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.InserisceModificaStato(idStato, Stato, true);
            }
        });

        ImageView imgAnnulla = findViewById(R.id.imgAnnulla);
        imgAnnulla.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModificheCodice.getInstance().getLayGestioneStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getEdtGestioneStato().setText("");
                VariabiliStaticheModificheCodice.getInstance().setIdGestioneStato(-1);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaStati(false);

                this.finish();

                return false;
        }

        return false;
    }
}
