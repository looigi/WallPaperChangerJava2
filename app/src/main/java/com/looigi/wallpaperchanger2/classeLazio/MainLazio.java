package com.looigi.wallpaperchanger2.classeLazio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaFonti;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaRuoli;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaSquadre;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaStati;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerClassifica;
import com.looigi.wallpaperchanger2.classeLazio.api_football.MainApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classePlayer.Files;

import java.util.Calendar;
import java.util.Date;

public class MainLazio extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lazio);

        context = this;
        act = this;

        VariabiliStaticheLazio.getInstance().setPathLazio(context.getFilesDir() + "/Lazio");

        VariabiliStaticheLazio.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoLazio));
        VariabiliStaticheLazio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().setSpnAnni(findViewById(R.id.spnAnni));
        VariabiliStaticheLazio.getInstance().setSpnCompetizioni(findViewById(R.id.spnCompetizione));

        VariabiliStaticheLazio.getInstance().setImgNuovo(findViewById(R.id.imgNuovo));
        VariabiliStaticheLazio.getInstance().setLayCalendario(findViewById(R.id.layCalendario));
        VariabiliStaticheLazio.getInstance().setLstCalendario(findViewById(R.id.lstCalendario));
        VariabiliStaticheLazio.getInstance().setLayClassifica(findViewById(R.id.layClassifica));
        VariabiliStaticheLazio.getInstance().setLstClassifica(findViewById(R.id.lstClassifica));
        VariabiliStaticheLazio.getInstance().setLaySquadre(findViewById(R.id.laySquadre));
        VariabiliStaticheLazio.getInstance().setLstSquadre(findViewById(R.id.lstSquadre));
        VariabiliStaticheLazio.getInstance().setLayMercato(findViewById(R.id.layMercato));
        VariabiliStaticheLazio.getInstance().setLstMercato(findViewById(R.id.lstMercato));
        VariabiliStaticheLazio.getInstance().setLayFonti(findViewById(R.id.layFonti));
        VariabiliStaticheLazio.getInstance().setLstFonti(findViewById(R.id.lstFonti));
        VariabiliStaticheLazio.getInstance().setLayRuoli(findViewById(R.id.layRuoli));
        VariabiliStaticheLazio.getInstance().setLstRuoli(findViewById(R.id.lstRuoli));
        VariabiliStaticheLazio.getInstance().setLayAllenatori(findViewById(R.id.layAllenatori));
        VariabiliStaticheLazio.getInstance().setLstAllenatori(findViewById(R.id.lstAllenatori));
        VariabiliStaticheLazio.getInstance().setLayStati(findViewById(R.id.layStati));
        VariabiliStaticheLazio.getInstance().setLstStati(findViewById(R.id.lstStati));
        VariabiliStaticheLazio.getInstance().setModalitaClassifica(1);
        VariabiliStaticheLazio.getInstance().setMascheraSelezionata(1);
        VariabiliStaticheLazio.getInstance().setLayGiocatori(findViewById(R.id.layGiocatori));
        VariabiliStaticheLazio.getInstance().setLayRuolo(findViewById(R.id.layRuolo));
        VariabiliStaticheLazio.getInstance().setLayMarcatori(findViewById(R.id.layMarcatori));
        VariabiliStaticheLazio.getInstance().setLstMarcatori(findViewById(R.id.lstMarcatori));

        VariabiliStaticheLazio.getInstance().setEdtCognome(findViewById(R.id.edtCognomeGiocatore));
        VariabiliStaticheLazio.getInstance().setEdtNome(findViewById(R.id.edtNomeGiocatore));

        VariabiliStaticheLazio.getInstance().setSpnRuolo(findViewById(R.id.spnRuolo));
        final boolean[] primoIngressoR = {true};
        VariabiliStaticheLazio.getInstance().getSpnRuolo().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (primoIngressoR[0]) {
                    primoIngressoR[0] = false;
                    return;
                }

                String selected ="";

                try {
                    selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                    for (StrutturaRuoli s : VariabiliStaticheLazio.getInstance().getRuoli()) {
                        if (s.getRuolo().equals(selected)) {
                            VariabiliStaticheLazio.getInstance().setIdRuoloSelezionato(s.getIdRuolo());
                            break;
                        }
                    }
                } catch (Exception e) {
                    selected="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheLazio.getInstance().setLayModifica(findViewById(R.id.layModifica));
        VariabiliStaticheLazio.getInstance().setLayModificaSFS(findViewById(R.id.layModificaSFS));
        VariabiliStaticheLazio.getInstance().setLayModificaCalendario(findViewById(R.id.layModificaCalendario));
        VariabiliStaticheLazio.getInstance().setLayModificaMercato(findViewById(R.id.layModificaMercato));
        VariabiliStaticheLazio.getInstance().setLayModificaGiocatore(findViewById(R.id.layModificaGiocatore));
        VariabiliStaticheLazio.getInstance().setTxtModifica(findViewById(R.id.txtModifica));
        VariabiliStaticheLazio.getInstance().getLayModifica().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().setEdtValore1(findViewById(R.id.edtValore1));

        VariabiliStaticheLazio.getInstance().setSpnStati(findViewById(R.id.spnStato));
        final boolean[] primoIngressoST = {true};
        VariabiliStaticheLazio.getInstance().getSpnStati().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (primoIngressoST[0]) {
                    primoIngressoST[0] = false;
                    return;
                }

                String selected ="";

                try {
                    selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                    for (StrutturaStati s : VariabiliStaticheLazio.getInstance().getStati()) {
                        if (s.getStato().equals(selected)) {
                            VariabiliStaticheLazio.getInstance().setIdStato(s.getIdStato());
                            break;
                        }
                    }
                } catch (Exception e) {
                    selected="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheLazio.getInstance().setSpnFonti(findViewById(R.id.spnFonte));
        final boolean[] primoIngressoFo = {true};
        VariabiliStaticheLazio.getInstance().getSpnFonti().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (primoIngressoFo[0]) {
                    primoIngressoFo[0] = false;
                    return;
                }

                String selected ="";

                try {
                    selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                    for (StrutturaFonti s : VariabiliStaticheLazio.getInstance().getFonti()) {
                        if (s.getFonte().equals(selected)) {
                            VariabiliStaticheLazio.getInstance().setIdFonte(s.getIdFonte());
                            break;
                        }
                    }
                } catch (Exception e) {
                    selected="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheLazio.getInstance().setEdtData(findViewById(R.id.edtData));
        VariabiliStaticheLazio.getInstance().getEdtData().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                VariabiliStaticheLazio.getInstance().setValoreImpostato2(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        VariabiliStaticheLazio.getInstance().setEdtDataCal(findViewById(R.id.edtDataCal));
        VariabiliStaticheLazio.getInstance().getEdtDataCal().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                VariabiliStaticheLazio.getInstance().setValoreImpostato1(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        VariabiliStaticheLazio.getInstance().setEdtRisultato(findViewById(R.id.edtRisultato));
        VariabiliStaticheLazio.getInstance().getEdtRisultato().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                VariabiliStaticheLazio.getInstance().setValoreImpostato2(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        VariabiliStaticheLazio.getInstance().setSpnCasa(findViewById(R.id.spnCasa));
        final boolean[] primoIngressoC = {true};
        VariabiliStaticheLazio.getInstance().getSpnCasa().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (primoIngressoC[0]) {
                    primoIngressoC[0] = false;
                    return;
                }

                String selected ="";

                try {
                    selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                    for (StrutturaSquadre s : VariabiliStaticheLazio.getInstance().getSquadre()) {
                        if (s.getSquadra().equals(selected)) {
                            VariabiliStaticheLazio.getInstance().setIdCasa(s.getIdSquadra());
                            break;
                        }
                    }
                } catch (Exception e) {
                    selected="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        VariabiliStaticheLazio.getInstance().setSpnFuori(findViewById(R.id.spnFuori));

        final boolean[] primoIngressoF = {true};
        VariabiliStaticheLazio.getInstance().getSpnCasa().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (primoIngressoF[0]) {
                    primoIngressoF[0] = false;
                    return;
                }

                String selected ="";

                try {
                    selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                    for (StrutturaSquadre s : VariabiliStaticheLazio.getInstance().getSquadre()) {
                        if (s.getSquadra().equals(selected)) {
                            VariabiliStaticheLazio.getInstance().setIdFuori(s.getIdSquadra());
                            break;
                        }
                    }
                } catch (Exception e) {
                    selected="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheLazio.getInstance().setEdtNominativo(findViewById(R.id.edtNominativo));
        VariabiliStaticheLazio.getInstance().getEdtNominativo().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                VariabiliStaticheLazio.getInstance().setValoreImpostato1(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        VariabiliStaticheLazio.getInstance().getEdtValore1().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                VariabiliStaticheLazio.getInstance().setValoreImpostato1(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        VariabiliStaticheLazio.getInstance().setSpnSquadreGioc(findViewById(R.id.spnSquadreGioc));
        final boolean[] primoIngressoS = {true};
        VariabiliStaticheLazio.getInstance().getSpnSquadreGioc().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (primoIngressoS[0]) {
                    primoIngressoS[0] = false;
                    return;
                }

                String selected ="";

                try {
                    selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                    for (StrutturaSquadre s : VariabiliStaticheLazio.getInstance().getSquadre()) {
                        if (s.getSquadra().equals(selected)) {
                            VariabiliStaticheLazio.getInstance().setIdSquadraPerGioc(s.getIdSquadra());

                            ChiamateWSLazio ws = new ChiamateWSLazio(context);
                            ws.RitornaGiocatori();
                            break;
                        }
                    }
                } catch (Exception e) {
                    selected="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        VariabiliStaticheLazio.getInstance().setLstGiocatori(findViewById(R.id.lstGiocatori));

        VariabiliStaticheLazio.getInstance().setSpnSquadreAll(findViewById(R.id.spnSquadreAll));
        final boolean[] primoIngressoSA = {true};
        VariabiliStaticheLazio.getInstance().getSpnSquadreAll().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (primoIngressoSA[0]) {
                    primoIngressoSA[0] = false;
                    return;
                }

                String selected ="";

                try {
                    selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                    for (StrutturaSquadre s : VariabiliStaticheLazio.getInstance().getSquadre()) {
                        if (s.getSquadra().equals(selected)) {
                            VariabiliStaticheLazio.getInstance().setIdSquadraPerAll(s.getIdSquadra());

                            ChiamateWSLazio ws = new ChiamateWSLazio(context);
                            ws.RitornaAllenatori();
                            break;
                        }
                    }
                } catch (Exception e) {
                    selected="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ImageView imgSalva = findViewById(R.id.imgSalvaValore);
        imgSalva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().SalvaValori(context);
            }
        });
        ImageView imgAnnulla = findViewById(R.id.imgAnnullaValore);
        imgAnnulla.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().getLayModifica().setVisibility(LinearLayout.GONE);
            }
        });

        LinearLayout layGestioneAnno = findViewById(R.id.layGestioneAnno);
        layGestioneAnno.setVisibility(LinearLayout.GONE);

        ImageView imgSalvaAnno = findViewById(R.id.imgSalvaAnno);
        imgSalvaAnno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().SalvaValori(context);

                layGestioneAnno.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaAnno = findViewById(R.id.imgAnnullaAnno);
        imgAnnullaAnno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layGestioneAnno.setVisibility(LinearLayout.GONE);
            }
        });

        VariabiliStaticheLazio.getInstance().setEdtDescrizioneAnno(findViewById(R.id.edtDescrizioneAnno));
        VariabiliStaticheLazio.getInstance().setEdtPuntiPerVittoria(findViewById(R.id.edtPuntiVittoria));

        ImageView imgNuovoAnno = findViewById(R.id.imgNuovoAnno);
        imgNuovoAnno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().getEdtDescrizioneAnno().setText("");
                VariabiliStaticheLazio.getInstance().getEdtPuntiPerVittoria().setText("3");
                VariabiliStaticheLazio.getInstance().setIdAnnoPerModifica(-1);

                VariabiliStaticheLazio.getInstance().setCosaStoModificando("ANNO");
                VariabiliStaticheLazio.getInstance().setModalitaModifica("INSERT");

                layGestioneAnno.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgNuovaCompetizione = findViewById(R.id.imgNuovaCompetizione);
        imgNuovaCompetizione.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setCosaStoModificando("COMPETIZIONE");
                VariabiliStaticheLazio.getInstance().setModalitaModifica("INSERT");

                UtilityLazio.getInstance().SalvaValori(context);
            }
        });

        ImageView imgAF = findViewById(R.id.imgApiFootball);
        imgAF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iI = new Intent(context, MainApiFootball.class);
                iI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iI);
            }
        });

        VariabiliStaticheLazio.getInstance().setImgAggiorna(findViewById(R.id.imgAggiorna));
        VariabiliStaticheLazio.getInstance().getImgAggiorna().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);

                switch (VariabiliStaticheLazio.getInstance().getMascheraSelezionata()) {
                    case 1:
                        // Classifica
                        break;
                    case 2:
                        // Calendario
                        break;
                    case 3:
                        // Squadre
                        break;
                    case 4:
                        // Mercato
                        break;
                    case 5:
                        // Fonti
                        ws.RitornaFonti(true);
                        break;
                    case 6:
                        // Stati
                        ws.RitornaStati(true);
                        break;
                    case 7:
                        // Giocatori
                        break;
                    case 8:
                        // Ruoli
                        ws.RitornaRuoli(true);
                        break;
                    case 9:
                        // Allenatori
                        break;
                }
            }
        });

        VariabiliStaticheLazio.getInstance().getImgNuovo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (VariabiliStaticheLazio.getInstance().getMascheraSelezionata()) {
                    case 1:
                        break;
                    case 2:
                        // Calendario
                        UtilityLazio.getInstance().ApreModifica(context, "CALENDARIO", "NUOVO",
                                "Nuova partita", "");
                        break;
                    case 3:
                        // Squadre
                        UtilityLazio.getInstance().ApreModifica(context, "SQUADRE", "NUOVO",
                                "Nuova squadra", "");
                        break;
                    case 4:
                        // Mercato
                        UtilityLazio.getInstance().ApreModifica(context, "MERCATO", "NUOVO",
                                "Nuovo movimento di mercato", "");
                        break;
                    case 5:
                        // Fonti
                        UtilityLazio.getInstance().ApreModifica(context, "FONTI", "NUOVO",
                                "Nuova fonte", "");
                        break;
                    case 6:
                        // Stati
                        UtilityLazio.getInstance().ApreModifica(context, "STATI", "NUOVO",
                                "Nuovo stato", "");
                        break;
                    case 7:
                        // Giocatori
                        if (VariabiliStaticheLazio.getInstance().getIdSquadraPerGioc() > -1) {
                            UtilityLazio.getInstance().ApreModifica(context, "GIOCATORI", "NUOVO",
                                    "Nuovo giocatore", "");
                        }
                        break;
                    case 8:
                        // Ruoli
                        UtilityLazio.getInstance().ApreModifica(context, "RUOLI", "NUOVO",
                                "Nuovo ruolo", "");
                        break;
                    case 9:
                        // Allenatori
                        if (VariabiliStaticheLazio.getInstance().getIdSquadraPerAll() > -1) {
                            UtilityLazio.getInstance().ApreModifica(context, "ALLENATORI", "NUOVO",
                                    "Nuovo allenatore", "");
                        }
                        break;
                }
            }
        });

        VariabiliStaticheLazio.getInstance().setTxtGiornata(findViewById(R.id.txtGiornata));
        ImageView imgIndietroClassifica = findViewById(R.id.imgIndietroClassifica);
        imgIndietroClassifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int giornata = VariabiliStaticheLazio.getInstance().getGiornata();
                giornata--;
                if (giornata > 0) {
                    VariabiliStaticheLazio.getInstance().getTxtGiornata().setText("Giornata " + giornata);
                    VariabiliStaticheLazio.getInstance().setGiornata(giornata);

                    VariabiliStaticheLazio.getInstance().setNonRicaricareMercato(true);

                    ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
                    ws1.RitornaClassifica();
                }
            }
        });
        ImageView imgAvantiClassifica = findViewById(R.id.imgAvantiClassifica);
        imgAvantiClassifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int giornata = VariabiliStaticheLazio.getInstance().getGiornata();
                giornata++;
                if (giornata <= VariabiliStaticheLazio.getInstance().getMaxGiornate()) {
                    VariabiliStaticheLazio.getInstance().getTxtGiornata().setText("Giornata " + giornata);
                    VariabiliStaticheLazio.getInstance().setGiornata(giornata);

                    VariabiliStaticheLazio.getInstance().setNonRicaricareMercato(true);

                    ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
                    ws1.RitornaClassifica();
                }
            }
        });

        UtilityLazio.getInstance().VisualizzaMaschera();

        UtilityLazio.getInstance().ImpostaAttesa(false);

        ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
        ws1.RitornaCompetizioni(false);

        ChiamateWSLazio ws2 = new ChiamateWSLazio(context);
        ws2.RitornaAnni(false);

        ChiamateWSLazio ws3 = new ChiamateWSLazio(context);
        ws3.RitornaStati(false);

        ChiamateWSLazio ws4 = new ChiamateWSLazio(context);
        ws4.RitornaFonti(false);

        ChiamateWSLazio ws5 = new ChiamateWSLazio(context);
        ws5.RitornaRuoli(false);

        VariabiliStaticheLazio.getInstance().setAcquistiCessioni(1);
        RadioButton optAcquisti = findViewById(R.id.optAcquisti);
        optAcquisti.setChecked(true);
        optAcquisti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setAcquistiCessioni(1);

                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaMercato();
            }
        });

        RadioButton optCessioni = findViewById(R.id.optCessioni);
        optCessioni.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setAcquistiCessioni(2);

                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaMercato();
            }
        });

        RadioButton optEstivo = findViewById(R.id.optEstivo);
        optEstivo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setModalitaMercato(1);

                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaMercato();
            }
        });

        RadioButton optInvernale = findViewById(R.id.optInvernale);
        optInvernale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setModalitaMercato(2);

                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaMercato();
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH);
        if (month < 3 || month > 11) {
            optInvernale.setChecked(true);
            VariabiliStaticheLazio.getInstance().setModalitaMercato(2);
        } else {
            optEstivo.setChecked(true);
            VariabiliStaticheLazio.getInstance().setModalitaMercato(1);
        }

        RadioButton optTotale = findViewById(R.id.optTotale);
        optTotale.setChecked(true);
        optTotale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setModalitaClassifica(1);

                VariabiliStaticheLazio.getInstance().setCstmAdptClassifica(
                        new AdapterListenerClassifica(context, VariabiliStaticheLazio.getInstance().getClassifica())
                );
                VariabiliStaticheLazio.getInstance().getLstClassifica().setAdapter(
                        VariabiliStaticheLazio.getInstance().getCstmAdptClassifica()
                );
            }
        });

        RadioButton optCasa = findViewById(R.id.optCasa);
        optCasa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setModalitaClassifica(2);

                VariabiliStaticheLazio.getInstance().setCstmAdptClassifica(
                        new AdapterListenerClassifica(context, VariabiliStaticheLazio.getInstance().getClassifica())
                );
                VariabiliStaticheLazio.getInstance().getLstClassifica().setAdapter(
                        VariabiliStaticheLazio.getInstance().getCstmAdptClassifica()
                );
            }
        });

        RadioButton optFuori = findViewById(R.id.optFuori);
        optFuori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setModalitaClassifica(3);

                VariabiliStaticheLazio.getInstance().setCstmAdptClassifica(
                        new AdapterListenerClassifica(context, VariabiliStaticheLazio.getInstance().getClassifica())
                );
                VariabiliStaticheLazio.getInstance().getLstClassifica().setAdapter(
                        VariabiliStaticheLazio.getInstance().getCstmAdptClassifica()
                );
            }
        });

        ImageView imgRefreshFonti = findViewById(R.id.imgRefreshFonti);
        imgRefreshFonti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaFonti(true);
            }
        });

        ImageView imgRefreshStati = findViewById(R.id.imgRefreshStati);
        imgRefreshStati.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaStati(true);
            }
        });

        ImageView imgRefreshRuoli = findViewById(R.id.imgRefreshRuoli);
        imgRefreshRuoli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaRuoli(true);
            }
        });

        ImageView imgClassifica = findViewById(R.id.imgClassifica);
        imgClassifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(1);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgCalendario = findViewById(R.id.imgCalendario);
        imgCalendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(2);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgSquadre = findViewById(R.id.imgSquadre);
        imgSquadre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(3);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgMercato = findViewById(R.id.imgMercato);
        imgMercato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(4);
                UtilityLazio.getInstance().VisualizzaMaschera();

                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaMercato();
            }
        });

        ImageView imgFonti = findViewById(R.id.imgFonti);
        imgFonti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(5);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgStati = findViewById(R.id.imgStati);
        imgStati.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(6);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgGiocatori = findViewById(R.id.imgGiocatori);
        imgGiocatori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(7);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgRuoli = findViewById(R.id.imgRuoli);
        imgRuoli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(8);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgAllenatori = findViewById(R.id.imgAllenatori);
        imgAllenatori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(9);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgMarcatori = findViewById(R.id.imgMarcatori);
        imgMarcatori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(10);

                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaMarcatori();

                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgRefreshAnni = findViewById(R.id.imgRefreshAnno);
        imgRefreshAnni.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaAnni(true);
            }
        });

        ImageView imgRefreshComp = findViewById(R.id.imgRefreshCompetizione);
        imgRefreshComp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaCompetizioni(true);
            }
        });

        ImageView imgCambiaAnno = findViewById(R.id.imgCambiaAnno);
        imgCambiaAnno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String PathFileSel = VariabiliStaticheLazio.getInstance().getPathLazio();
                String NomeFileSel = "AnnoSelezionato.txt";

                if (Files.getInstance().EsisteFile(PathFileSel + "/" + NomeFileSel)) {
                    Files.getInstance().EliminaFileUnico(PathFileSel + "/" + NomeFileSel);
                }
                Files.getInstance().ScriveFile(PathFileSel, NomeFileSel,
                        VariabiliStaticheLazio.getInstance().getAnnoSelezionato() + ";" +
                        VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato()
                );

                UtilityLazio.getInstance().LeggeAnno(context);
            }
        });

        ImageView imgCambiaComp = findViewById(R.id.imgCambiaCompetizione);
        imgCambiaComp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().LeggeAnno(context);
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
