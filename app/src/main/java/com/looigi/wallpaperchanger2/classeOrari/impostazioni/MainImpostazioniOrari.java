package com.looigi.wallpaperchanger2.classeOrari.impostazioni;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerCommesseGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerLavoriGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerMezziGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerMezziStandardGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerPasticcheGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerPortateGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerTempoGestione;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezziStandard;

import java.util.ArrayList;
import java.util.List;

public class MainImpostazioniOrari extends Activity {
    private Context context;
    private Activity act;
    private AdapterListenerPortateGestione cstmAdptPranzo;
    private AdapterListenerMezziGestione cstmAdptMezzi = null;
    private AdapterListenerMezziStandardGestione cstmAdptMSA = null;
    private AdapterListenerLavoriGestione cstmAdptLavori = null;
    private AdapterListenerCommesseGestione cstmAdptCommessa = null;
    private AdapterListenerTempoGestione cstmAdptTempo = null;
    private AdapterListenerPasticcheGestione cstmAdptPasticche = null;

    public MainImpostazioniOrari() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_impostazioni_orari);

        context = this;
        act = this;

        VariabiliStaticheImpostazioniOrari.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoImpOrari));
        UtilityImpostazioniOrari.getInstance().ImpostaAttesa(false);

        VariabiliStaticheImpostazioniOrari.getInstance().setLayGestione(findViewById(R.id.layGestione));
        VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.GONE);
        VariabiliStaticheImpostazioniOrari.getInstance().setModalita("");

        TextView txtTipoModifica = findViewById(R.id.txtTipoModifica);
        txtTipoModifica.setText("");

        EditText edtRicercaTestoNuovo = findViewById(R.id.edtTestoRicerca);

        ImageView imgCercaTestoNuovo = findViewById(R.id.imgCercaTesto);
        imgCercaTestoNuovo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (VariabiliStaticheImpostazioniOrari.getInstance().getModalita()) {
                    case "PORTATE":
                        cstmAdptPranzo.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                    case "MEZZI":
                        cstmAdptMezzi.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                    case "MSA":
                        cstmAdptMSA.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                    case "MSR":
                        cstmAdptMSA.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                    case "LAVORI":
                        cstmAdptLavori.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                    case "COMMESSE":
                        cstmAdptCommessa.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                }
            }
        });

        ListView lstLista = findViewById(R.id.lstLista);

        // PORTATE
        VariabiliStaticheImpostazioniOrari.getInstance().setLayPortata(findViewById(R.id.layPortata));
        VariabiliStaticheImpostazioniOrari.getInstance().getLayPortata().setVisibility(LinearLayout.GONE);
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtPortata(findViewById(R.id.edtPortata));
        ImageView imgSalvaPortata = findViewById(R.id.imgSalvaPortata);
        imgSalvaPortata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheImpostazioniOrari.getInstance().getIdPortata() > -1) {
                    // Modifica
                } else {
                    // Nuovo Inserimento
                }
                VariabiliStaticheImpostazioniOrari.getInstance().getLayPortata().setVisibility(LinearLayout.GONE);
            }
        });
        ImageView imgAnnullaPortata = findViewById(R.id.imgAnnullaPortata);
        imgAnnullaPortata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayPortata().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgPortate = findViewById(R.id.imgGestionePortate);
        imgPortate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("PORTATE");
                txtTipoModifica.setText("Gestione portate");

                cstmAdptPranzo = new AdapterListenerPortateGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getPortate());
                lstLista.setAdapter(cstmAdptPranzo);
            }
        });
        // PORTATE

        ImageView imgMezzi = findViewById(R.id.imgGestioneMezzi);
        imgMezzi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("MEZZI");
                txtTipoModifica.setText("Gestione mezzi");

                cstmAdptMezzi = new AdapterListenerMezziGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi());
                lstLista.setAdapter(cstmAdptMezzi);
            }
        });

        ImageView imgMSA = findViewById(R.id.imgGestioneMSA);
        imgMSA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("MSA");
                txtTipoModifica.setText("Gestione mezzi standard andata");

                List<StrutturaMezzi> lista = new ArrayList<>();
                for (StrutturaMezziStandard s : VariabiliStaticheOrari.getInstance().getListaMezziAndataStandard()) {
                    for (StrutturaMezzi sm : VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()) {
                        if (sm.getIdMezzo() == s.getIdMezzo()) {
                            lista.add(sm);
                        }
                    }
                }
                cstmAdptMSA = new AdapterListenerMezziStandardGestione(context,
                        lista);
                lstLista.setAdapter(cstmAdptMSA);
            }
        });

        ImageView imgMSR = findViewById(R.id.imgGestioneMSR);
        imgMSR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("MSR");
                txtTipoModifica.setText("Gestione mezzi standard ritorno");

                List<StrutturaMezzi> lista = new ArrayList<>();
                for (StrutturaMezziStandard s : VariabiliStaticheOrari.getInstance().getListaMezziRitornoStandard()) {
                    for (StrutturaMezzi sm : VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()) {
                        if (sm.getIdMezzo() == s.getIdMezzo()) {
                            lista.add(sm);
                        }
                    }
                }
                cstmAdptMSA = new AdapterListenerMezziStandardGestione(context,
                        lista);
                lstLista.setAdapter(cstmAdptMSA);
            }
        });

        ImageView imgLavori = findViewById(R.id.imgGestioneLavoro);
        imgLavori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("LAVORI");
                txtTipoModifica.setText("Gestione lavori");

                cstmAdptLavori = new AdapterListenerLavoriGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori());
                lstLista.setAdapter(cstmAdptLavori);
            }
        });

        ImageView imgCommesseLavori = findViewById(R.id.imgGestioneCommesseLavoro);
        imgCommesseLavori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("COMMESSE");
                txtTipoModifica.setText("Gestione commesse lavoro");

                cstmAdptCommessa = new AdapterListenerCommesseGestione(context,
                        new ArrayList<>());
                lstLista.setAdapter(cstmAdptCommessa);
            }
        });

        ImageView imgTempo = findViewById(R.id.imgGestioneTempo);
        imgTempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("TEMPO");
                txtTipoModifica.setText("Gestione tempo");

                cstmAdptTempo = new AdapterListenerTempoGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi());
                lstLista.setAdapter(cstmAdptTempo);
            }
        });

        ImageView imgPasticche = findViewById(R.id.imgGestionePasticche);
        imgPasticche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("PASTICCHE");
                txtTipoModifica.setText("Gestione pasticche");

                cstmAdptPasticche = new AdapterListenerPasticcheGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getPasticche());
                lstLista.setAdapter(cstmAdptPasticche);
            }
        });

        ImageView imgNuovoValore = findViewById(R.id.imgNuovoValore);
        imgNuovoValore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (VariabiliStaticheImpostazioniOrari.getInstance().getModalita()) {
                    case "PORTATE":
                        VariabiliStaticheImpostazioniOrari.getInstance().setIdPortata(-1);
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtPortata().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getLayPortata().setVisibility(LinearLayout.VISIBLE);
                        break;
                    case "MEZZI":
                        break;
                    case "MSA":
                        break;
                    case "MSR":
                        break;
                    case "LAVORI":
                        break;
                    case "COMMESSE":
                        break;
                    case "TEMPO":
                        break;
                    case "PASTICCHE":
                        break;
                }
            }
        });

        ImageView imgChiudeGestione = findViewById(R.id.imgChiudeGestione);
        imgChiudeGestione.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.GONE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("");
                txtTipoModifica.setText("");
            }
        });
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
