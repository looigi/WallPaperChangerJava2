package com.looigi.wallpaperchanger2.classeOrari.impostazioni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.adapters.AdapterListenerMezzi;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerCommesseGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerLavoriGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerMezziGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerMezziStandardGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerPasticcheGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerPortateGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerTempoGestione;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Commesse.MainCommessa;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.webService.ChiamateWSImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezziStandard;
import com.looigi.wallpaperchanger2.classeOrari.webService.ChiamateWSOrari;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class MainImpostazioniOrari extends Activity {
    private Context context;
    private Activity act;

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

        VariabiliStaticheImpostazioniOrari.getInstance().setEdtRicercaTestoNuovo(findViewById(R.id.edtTestoRicerca));

        ImageView imgCercaTestoNuovo = findViewById(R.id.imgCercaTesto);
        imgCercaTestoNuovo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (VariabiliStaticheImpostazioniOrari.getInstance().getModalita()) {
                    case "PORTATE":
                        VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptPranzo().updateData(
                                VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());
                        break;
                    case "MEZZI":
                        VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptMezzi().updateData(
                                VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());
                        break;
                    case "MSA":
                        VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptMSA().updateData(
                                VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());
                        break;
                    case "MSR":
                        VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptMSA().updateData(
                                VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());
                        break;
                    case "LAVORI":
                        VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptLavori().updateData(
                                VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());
                        break;
                    case "COMMESSE":
                        VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptCommessa().updateData(
                                VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());
                        break;
                }
            }
        });

        ListView lstLista = findViewById(R.id.lstLista);

        //region PORTATE
        VariabiliStaticheImpostazioniOrari.getInstance().setLayPortata(findViewById(R.id.layPortata));
        VariabiliStaticheImpostazioniOrari.getInstance().getLayPortata().setVisibility(LinearLayout.GONE);
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtPortata(findViewById(R.id.edtPortata));
        ImageView imgSalvaPortata = findViewById(R.id.imgSalvaPortata);
        imgSalvaPortata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheImpostazioniOrari.getInstance().getEdtPortata().getText().toString().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire un valore per la portata");
                    return;
                }

                ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                if (VariabiliStaticheImpostazioniOrari.getInstance().getIdPortata() > -1) {
                    // Modifica
                    ws.SalvaPortata(String.valueOf(VariabiliStaticheImpostazioniOrari.getInstance().getIdPortata()),
                            VariabiliStaticheImpostazioniOrari.getInstance().getEdtPortata().getText().toString()
                    );
                } else {
                    // Nuovo Inserimento
                    ws.SalvaPortata("-1",
                            VariabiliStaticheImpostazioniOrari.getInstance().getEdtPortata().getText().toString()
                    );
                }

                VariabiliStaticheImpostazioniOrari.getInstance().setDatiModificati(true);

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

                VariabiliStaticheImpostazioniOrari.getInstance().setCstmAdptPranzo(new AdapterListenerPortateGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getPortate()));
                lstLista.setAdapter(VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptPranzo());
            }
        });
        // endregion

        // region MEZZI
        ImageView imgMezzi = findViewById(R.id.imgGestioneMezzi);
        imgMezzi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("MEZZI");
                txtTipoModifica.setText("Gestione mezzi");

                VariabiliStaticheImpostazioniOrari.getInstance().setCstmAdptMezzi(new AdapterListenerMezziGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()));
                lstLista.setAdapter(VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptMezzi());
            }
        });

        VariabiliStaticheImpostazioniOrari.getInstance().setLayMezzi(findViewById(R.id.layGestioneMezzi));
        VariabiliStaticheImpostazioniOrari.getInstance().getLayMezzi().setVisibility(LinearLayout.GONE);
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtMezzo(findViewById(R.id.edtMezzo));
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtMezzoDettaglio(findViewById(R.id.edtMezzoDettaglio));
        ImageView imgSalvaMezzo = findViewById(R.id.imgSalvaMezzo);
        imgSalvaMezzo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheImpostazioniOrari.getInstance().getEdtMezzo().getText().toString().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire un valore per il mezzo");
                    return;
                }

                ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                ws.SalvaMezzo(
                        String.valueOf(VariabiliStaticheImpostazioniOrari.getInstance().getIdMezzo()),
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtMezzo().getText().toString(),
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtMezzoDettaglio().getText().toString()
                );
                VariabiliStaticheImpostazioniOrari.getInstance().setDatiModificati(true);

                VariabiliStaticheImpostazioniOrari.getInstance().getLayMezzi().setVisibility(LinearLayout.GONE);
            }
        });
        ImageView imgAnnullaMezzo = findViewById(R.id.imgAnnullaMezzo);
        imgAnnullaMezzo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayMezzi().setVisibility(LinearLayout.GONE);
            }
        });
        // endregion

        // region GESTIONE MS
        LinearLayout layMezziStandard = findViewById(R.id.layGestioneMS);
        layMezziStandard.setVisibility(LinearLayout.GONE);
        ListView lstMezzi = findViewById(R.id.lstListaMezzi);
        VariabiliStaticheImpostazioniOrari.getInstance().setLstMezziStandard(findViewById(R.id.lstListaMezziStandard));

        ImageView imgSalvaMS = findViewById(R.id.imgSalvaMS);
        imgSalvaMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String idMezzi = "";

                List<StrutturaMezziStandard> lista = new ArrayList<>();
                List<StrutturaMezziStandard> listNum = new ArrayList<>();
                for (StrutturaMezzi s : VariabiliStaticheImpostazioniOrari.getInstance().getMezziStandard()) {
                    idMezzi += s.getIdMezzo() + ";";

                    StrutturaMezziStandard ss = new StrutturaMezziStandard();
                    ss.setIdMezzo(s.getIdMezzo());
                    lista.add(ss);

                    StrutturaMezziStandard sss = new StrutturaMezziStandard();
                    sss.setIdMezzo(s.getIdMezzo());
                    listNum.add(sss);
                }

                String Tipologia = "";
                if (VariabiliStaticheImpostazioniOrari.getInstance().getAndataRitorno() == 1) {
                    // Andata
                    Tipologia = "Andata";
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziStandardAndata(lista);
                    VariabiliStaticheOrari.getInstance().setListaMezziAndataStandard(listNum);
                } else {
                    // Ritorno
                    Tipologia = "Ritorno";
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziStandardRitorno(lista);
                    VariabiliStaticheOrari.getInstance().setListaMezziRitornoStandard(listNum);
                }

                ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                ws.SalvaMezziStandard(Tipologia, idMezzi);

                VariabiliStaticheImpostazioniOrari.getInstance().setDatiModificati(true);

                layMezziStandard.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaMS = findViewById(R.id.imgAnnullaMS);
        imgAnnullaMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layMezziStandard.setVisibility(LinearLayout.GONE);
            }
        });
        // endregion

        // region MEZZI STANDARD ANDATA
        ImageView imgMSA = findViewById(R.id.imgGestioneMSA);
        imgMSA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setAndataRitorno(1);

                AdapterListenerMezzi adapter = new AdapterListenerMezzi(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi(),
                        true,
                        true,
                        "IMPO1");
                lstMezzi.setAdapter(adapter);

                List<StrutturaMezzi> standard = new ArrayList<>();
                for (StrutturaMezziStandard s : VariabiliStaticheOrari.getInstance().getListaMezziAndataStandard()) {
                    for (StrutturaMezzi sm : VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()) {
                        if (sm.getIdMezzo() == s.getIdMezzo()) {
                            standard.add(sm);
                        }
                    }
                }
                VariabiliStaticheImpostazioniOrari.getInstance().setMezziStandard(standard);
                AdapterListenerMezzi adapterS = new AdapterListenerMezzi(context,
                        standard,
                        false,
                        true,
                        "IMPO2");
                VariabiliStaticheImpostazioniOrari.getInstance().getLstMezziStandard().setAdapter(adapterS);

                layMezziStandard.setVisibility(LinearLayout.VISIBLE);
            }
        });
        // endregion

        // region MEZZI STANDARD RITORNO
        ImageView imgMSR = findViewById(R.id.imgGestioneMSR);
        imgMSR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setAndataRitorno(2);

                AdapterListenerMezzi adapter = new AdapterListenerMezzi(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi(),
                        true,
                        false,
                        "IMPO1");
                lstMezzi.setAdapter(adapter);

                List<StrutturaMezzi> standard = new ArrayList<>();
                for (StrutturaMezziStandard s : VariabiliStaticheOrari.getInstance().getListaMezziRitornoStandard()) {
                    for (StrutturaMezzi sm : VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()) {
                        if (sm.getIdMezzo() == s.getIdMezzo()) {
                            standard.add(sm);
                        }
                    }
                }
                VariabiliStaticheImpostazioniOrari.getInstance().setMezziStandard(standard);
                AdapterListenerMezzi adapterS = new AdapterListenerMezzi(context,
                        standard,
                        false,
                        false,
                        "IMPO2");
                VariabiliStaticheImpostazioniOrari.getInstance().getLstMezziStandard().setAdapter(adapterS);

                layMezziStandard.setVisibility(LinearLayout.VISIBLE);
            }
        });
        // endregion

        // region LAVORI
        VariabiliStaticheImpostazioniOrari.getInstance().setLayLavoro(findViewById(R.id.layGestioneLavoro));
        VariabiliStaticheImpostazioniOrari.getInstance().getLayLavoro().setVisibility(LinearLayout.GONE);

        VariabiliStaticheImpostazioniOrari.getInstance().setEdtLavoro(findViewById(R.id.edtLavoro));
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtIndirizzo(findViewById(R.id.edtIndirizzo));
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtDataInizio(findViewById(R.id.edtDataInizio));
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtDataFine(findViewById(R.id.edtDataFine));
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtLatLng(findViewById(R.id.edtLatLng));

        ImageView imgLavori = findViewById(R.id.imgGestioneLavoro);
        imgLavori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("LAVORI");
                txtTipoModifica.setText("Gestione lavori");

                VariabiliStaticheImpostazioniOrari.getInstance().setCstmAdptLavori(new AdapterListenerLavoriGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori(),
                                false));
                lstLista.setAdapter(VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptLavori());
            }
        });

        ImageView imgPrendeLatLng = findViewById(R.id.imgPrendeLatLng);
        imgPrendeLatLng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Indirizzo = VariabiliStaticheImpostazioniOrari.getInstance().getEdtIndirizzo().getText().toString();
                if (Indirizzo.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire l'indirizzo");
                    return;
                }
                LatLng l = UtilityImpostazioniOrari.getInstance().getLocationFromAddress(context, Indirizzo);
                VariabiliStaticheImpostazioniOrari.getInstance().getEdtLatLng().setText(l.latitude + ";" + l.longitude);
            }
        });

        ImageView imgSalvaLavoro = findViewById(R.id.imgSalvaLavoro);
        imgSalvaLavoro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Lavoro = VariabiliStaticheImpostazioniOrari.getInstance().getEdtLavoro().getText().toString();
                if (Lavoro.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire il lavoro");
                    return;
                }
                String Indirizzo = VariabiliStaticheImpostazioniOrari.getInstance().getEdtIndirizzo().getText().toString();
                if (Indirizzo.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire l'indirizzo");
                    return;
                }
                String DataInizio = VariabiliStaticheImpostazioniOrari.getInstance().getEdtDataInizio().getText().toString();
                if (DataInizio.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire la data di inizio");
                    return;
                }
                String DataFine = VariabiliStaticheImpostazioniOrari.getInstance().getEdtDataFine().getText().toString();
                if (DataFine.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire la data di fine");
                    return;
                }
                String LatLng = VariabiliStaticheImpostazioniOrari.getInstance().getEdtLatLng().getText().toString();

                ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                ws.SalvaLavori(
                        String.valueOf(VariabiliStaticheImpostazioniOrari.getInstance().getIdLavoro()),
                        Lavoro,
                        Indirizzo,
                        DataInizio,
                        DataFine,
                        LatLng
                );
                VariabiliStaticheImpostazioniOrari.getInstance().setDatiModificati(true);

                VariabiliStaticheImpostazioniOrari.getInstance().getLayLavoro().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaLavoro = findViewById(R.id.imgAnnullaLavoro);
        imgAnnullaLavoro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayLavoro().setVisibility(LinearLayout.GONE);
            }
        });
        // endregion

        // region COMMESSE LAVORI
        ListView listaLavori = findViewById(R.id.lstListaLavori);
        VariabiliStaticheImpostazioniOrari.getInstance().setLstCommesse(findViewById(R.id.lstListaCommesse));
        VariabiliStaticheImpostazioniOrari.getInstance().setLayCommesse(findViewById(R.id.layGestioneCommesse));
        VariabiliStaticheImpostazioniOrari.getInstance().getLayCommesse().setVisibility(LinearLayout.GONE);

        ImageView imgCommesseLavori = findViewById(R.id.imgGestioneCommesseLavoro);
        imgCommesseLavori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setCstmAdptLavori(
                        new AdapterListenerLavoriGestione(
                                context,
                                VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori(),
                                true
                        )
                );
                listaLavori.setAdapter(VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptLavori());

                VariabiliStaticheImpostazioniOrari.getInstance().getLayCommesse().setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgChiudeCommesse = findViewById(R.id.imgChiudeCommesse);
        imgChiudeCommesse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayCommesse().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAggiungeCommessa = findViewById(R.id.imgAggiungeCommessa);
        imgAggiungeCommessa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setIdCommessa(-1);

                Intent iI = new Intent(context, MainCommessa.class);
                iI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iI);
            }
        });
        // endregion

        // region TEMPO
        VariabiliStaticheImpostazioniOrari.getInstance().setLayTempo(findViewById(R.id.layGestioneTempo));
        VariabiliStaticheImpostazioniOrari.getInstance().getLayTempo().setVisibility(LinearLayout.GONE);
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtTempo(findViewById(R.id.edtTempo));

        ImageView imgTempo = findViewById(R.id.imgGestioneTempo);
        imgTempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("TEMPO");
                txtTipoModifica.setText("Gestione tempo");

                VariabiliStaticheImpostazioniOrari.getInstance().setCstmAdptTempo(new AdapterListenerTempoGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi()));
                lstLista.setAdapter(VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptTempo());
            }
        });

        ImageView imgSalvaTempo = findViewById(R.id.imgSalvaTempo);
        imgSalvaTempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheImpostazioniOrari.getInstance().getEdtTempo().getText().toString().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire un valore per il tempo");
                    return;
                }

                ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                if (VariabiliStaticheImpostazioniOrari.getInstance().getIdTempo() > -1) {
                    // Modifica
                    ws.SalvaTempo(String.valueOf(VariabiliStaticheImpostazioniOrari.getInstance().getIdTempo()),
                            VariabiliStaticheImpostazioniOrari.getInstance().getEdtTempo().getText().toString()
                    );
                } else {
                    // Nuovo Inserimento
                    ws.SalvaTempo("-1",
                            VariabiliStaticheImpostazioniOrari.getInstance().getEdtTempo().getText().toString()
                    );
                }

                VariabiliStaticheImpostazioniOrari.getInstance().setDatiModificati(true);

                VariabiliStaticheImpostazioniOrari.getInstance().getLayTempo().setVisibility(LinearLayout.GONE);
            }
        });
        ImageView imgAnnullaTempo = findViewById(R.id.imgAnnullaTempo);
        imgAnnullaTempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayTempo().setVisibility(LinearLayout.GONE);
            }
        });
        // endregion

        // region PASTICCHE
        VariabiliStaticheImpostazioniOrari.getInstance().setLayPasticca(findViewById(R.id.layGestionePasticca));
        VariabiliStaticheImpostazioniOrari.getInstance().getLayPasticca().setVisibility(LinearLayout.GONE);
        VariabiliStaticheImpostazioniOrari.getInstance().setEdtPasticca(findViewById(R.id.edtPasticca));

        ImageView imgPasticche = findViewById(R.id.imgGestionePasticche);
        imgPasticche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayGestione().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheImpostazioniOrari.getInstance().setModalita("PASTICCHE");
                txtTipoModifica.setText("Gestione pasticche");

                VariabiliStaticheImpostazioniOrari.getInstance().setCstmAdptPasticche(new AdapterListenerPasticcheGestione(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getPasticche()));
                lstLista.setAdapter(VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptPasticche());
            }
        });

        ImageView imgSalvaPasticca = findViewById(R.id.imgSalvaPasticca);
        imgSalvaPasticca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheImpostazioniOrari.getInstance().getEdtPasticca().getText().toString().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire un valore per la pasticca");
                    return;
                }

                ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                if (VariabiliStaticheImpostazioniOrari.getInstance().getIdPasticca() > -1) {
                    // Modifica
                    ws.SalvaPasticca(String.valueOf(VariabiliStaticheImpostazioniOrari.getInstance().getIdPasticca()),
                            VariabiliStaticheImpostazioniOrari.getInstance().getEdtPasticca().getText().toString()
                    );
                } else {
                    // Nuovo Inserimento
                    ws.SalvaPasticca("-1",
                            VariabiliStaticheImpostazioniOrari.getInstance().getEdtPasticca().getText().toString()
                    );
                }

                VariabiliStaticheImpostazioniOrari.getInstance().setDatiModificati(true);

                VariabiliStaticheImpostazioniOrari.getInstance().getLayPasticca().setVisibility(LinearLayout.GONE);
            }
        });
        ImageView imgAnnullaPasticca = findViewById(R.id.imgAnnullaPasticca);
        imgAnnullaPasticca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().getLayPasticca().setVisibility(LinearLayout.GONE);
            }
        });
        // endregion

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
                        VariabiliStaticheImpostazioniOrari.getInstance().setIdMezzo(-1);
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtMezzo().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtMezzoDettaglio().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getLayMezzi().setVisibility(LinearLayout.VISIBLE);
                        break;
                    case "MSA":
                        break;
                    case "MSR":
                        break;
                    case "LAVORI":
                        VariabiliStaticheImpostazioniOrari.getInstance().setIdLavoro(-1);
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtLavoro().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtIndirizzo().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtDataInizio().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtDataFine().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtLatLng().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getLayLavoro().setVisibility(LinearLayout.VISIBLE);
                        break;
                    case "COMMESSE":
                        break;
                    case "TEMPO":
                        VariabiliStaticheImpostazioniOrari.getInstance().setIdTempo(-1);
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtTempo().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getLayTempo().setVisibility(LinearLayout.VISIBLE);
                        break;
                    case "PASTICCHE":
                        VariabiliStaticheImpostazioniOrari.getInstance().setIdPasticca(-1);
                        VariabiliStaticheImpostazioniOrari.getInstance().getEdtPasticca().setText("");
                        VariabiliStaticheImpostazioniOrari.getInstance().getLayPasticca().setVisibility(LinearLayout.VISIBLE);
                        break;
                }
            }
        });

        ImageView imgRefresh = findViewById(R.id.imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSOrari ws = new ChiamateWSOrari(context);
                ws.RitornaDatiPerModifica(true, false);
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
                if (VariabiliStaticheImpostazioniOrari.getInstance().isDatiModificati()) {
                    ChiamateWSOrari ws = new ChiamateWSOrari(context);
                    ws.RitornaDatiPerModifica(true, false);
                }
                this.finish();

                return false;
        }

        return false;
    }
}
