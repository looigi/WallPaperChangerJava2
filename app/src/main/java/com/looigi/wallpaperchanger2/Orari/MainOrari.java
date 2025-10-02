package com.looigi.wallpaperchanger2.Orari;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.Pennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Orari.adapters.AdapterListenerMezzi;
import com.looigi.wallpaperchanger2.Orari.adapters.AdapterListenerPortate;
import com.looigi.wallpaperchanger2.Orari.impostazioni.MainImpostazioniOrari;
import com.looigi.wallpaperchanger2.Orari.statisticheOrari.MainStatisticheOrari;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaCommesse;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaDatiGiornata;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaLavoro;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaPasticca;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaTempo;
import com.looigi.wallpaperchanger2.Orari.webService.ChiamateWSOrari;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;
import com.looigi.wallpaperchanger2.UtilitiesVarie.meteo.classeMeteo;
import com.looigi.wallpaperchanger2.UtilitiesVarie.meteo.struttura.StrutturaMeteo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainOrari extends Activity {
    private Context context;
    private Activity act;
    private String Modalita;
    private String ValoreImpostato;
    private String ModalitaNuovoDato;
    private AdapterListenerPortate cstmAdptPranzo = null;
    private AdapterListenerMezzi cstmAdptMezziAndata = null;
    private AdapterListenerMezzi cstmAdptMezziRitorno = null;
    private boolean ApertoDP = true;
    private Handler handlerTimerMeteo;
    private Runnable rTimerMeteo;

    public MainOrari() {
        ApertoDP = true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_orari);

        context = this;
        act = this;

        VariabiliStaticheOrari.getInstance().setPathOrari(context.getFilesDir() + "/Orari");
        Files.getInstance().CreaCartelle(VariabiliStaticheOrari.getInstance().getPathOrari());

        ImageView imgIndietro = findViewById(R.id.imgIndietro);
        ImageView imgAvanti = findViewById(R.id.imgAvanti);
        TextView txtData = findViewById(R.id.txtData);
        TextView txtNomeGiorno = findViewById(R.id.txtNomeGiorno);
        VariabiliStaticheOrari.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoOrari));
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheOrari.getInstance().getImgCaricamento(),
                false
        );

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
        VariabiliStaticheOrari.getInstance().setTxtTipoGiorno(findViewById(R.id.txtTipoGiorno));
        VariabiliStaticheOrari.getInstance().setTxtNumeroGiorno(findViewById(R.id.txtNumeroGiorno));
        VariabiliStaticheOrari.getInstance().setEdtNote(findViewById(R.id.edtNote));
        VariabiliStaticheOrari.getInstance().setLstPranzo(findViewById(R.id.lstPranzo));
        VariabiliStaticheOrari.getInstance().setLstMezziAndata(findViewById(R.id.lstMezziAndata));
        VariabiliStaticheOrari.getInstance().setLstMezziRitorno(findViewById(R.id.lstMezziRitorno));

        VariabiliStaticheOrari.getInstance().getLayContenitore().setVisibility(LinearLayout.GONE);
        VariabiliStaticheOrari.getInstance().getLayAggiunge().setVisibility(LinearLayout.VISIBLE);

        VariabiliStaticheOrari.getInstance().setDataAttuale(new Date());

        VariabiliStaticheOrari.getInstance().setLayNote(findViewById(R.id.layNote));

        ImageView imgNuovoOrario = findViewById(R.id.imgNuovoOrario);
        imgNuovoOrario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, -5);
                SimpleDateFormat sdfO = new SimpleDateFormat("HH:mm:00");
                String currentHour = sdfO.format(calendar.getTime());

                VariabiliStaticheOrari.getInstance().getDatiGiornata().setGiornoInserito(true);
                VariabiliStaticheOrari.getInstance().getDatiGiornata().setEntrata(currentHour);
                VariabiliStaticheOrari.getInstance().getDatiGiornata().setQuanteOre(-6); // Smart Working default
                VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText("-6");
                UtilityOrari.getInstance().ScriveDatiGiornata(context);
            }
        });

        ImageView imgInserisceNote = findViewById(R.id.imgInserisceNote);
        imgInserisceNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheOrari.getInstance().getDatiGiornata().setSoloNote(true);
                UtilityOrari.getInstance().ScriveDatiGiornata(context);
            }
        });

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

        LinearLayout laySceltaData = findViewById(R.id.laySceltaData);
        laySceltaData.setVisibility(LinearLayout.GONE);

        DatePicker sceltaData = findViewById(R.id.datePicker1);

        txtData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Date datella = VariabiliStaticheOrari.getInstance().getDataAttuale();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(datella);

                int Giorno = calendar.get(Calendar.DAY_OF_MONTH);
                int Mese = calendar.get(Calendar.MONTH) + 1;
                int Anno = calendar.get(Calendar.YEAR);

                // sceltaData.init(Anno, Mese, Giorno, null);

                laySceltaData.setVisibility(LinearLayout.VISIBLE);
            }
        });

        sceltaData.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (ApertoDP) {
                    ApertoDP = false;
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    VariabiliStaticheOrari.getInstance().setDataAttuale(calendar.getTime());

                    ScriveData(context, txtData, txtNomeGiorno);

                    laySceltaData.setVisibility(LinearLayout.GONE);
                }
            }
        });

        ImageView imgOggi = findViewById(R.id.imgOggi);
        imgOggi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                VariabiliStaticheOrari.getInstance().setDataAttuale(c.getTime());

                ScriveData(context, txtData, txtNomeGiorno);
            }
        });

        ModalitaNuovoDato = "";
        VariabiliStaticheOrari.getInstance().setLayNuovoDato(findViewById(R.id.layNuovoDato));
        VariabiliStaticheOrari.getInstance().getLayNuovoDato().setVisibility(LinearLayout.GONE);

        TextView txtNuovoTesto = findViewById(R.id.txtNuovoTesto);
        ListView lstNuovoDato = findViewById(R.id.lstNuovoDato);

        EditText edtRicercaTestoNuovo = findViewById(R.id.edtTestoRicercaNuovo);

        ImageView imgCercaTestoNuovo = findViewById(R.id.imgCercaTestoNuovo);
        imgCercaTestoNuovo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (ModalitaNuovoDato) {
                    case "PORTATA":
                        cstmAdptPranzo.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                    case "MEZZOANDATA":
                        cstmAdptMezziAndata.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                    case "MEZZORITORNO":
                        cstmAdptMezziRitorno.updateData(
                                edtRicercaTestoNuovo.getText().toString());
                        break;
                }
            }
        });
        
        ImageView imgChiudeNuovo = findViewById(R.id.imgChiudeNuovo);
        imgChiudeNuovo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ModalitaNuovoDato = "";
                txtNuovoTesto.setText("");
                lstNuovoDato.setAdapter(null);
                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getLayNuovoDato().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgNuovaPortata = findViewById(R.id.imgNuovaPortata);
        imgNuovaPortata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtNuovoTesto.setText("Aggiungi portata");
                ModalitaNuovoDato = "PORTATA";

                cstmAdptPranzo = new AdapterListenerPortate(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getPortate(),
                        true);
                lstNuovoDato.setAdapter(cstmAdptPranzo);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getLayNuovoDato().setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgNuovoMezzoAndata = findViewById(R.id.imgNuovoMezzoAndata);
        imgNuovoMezzoAndata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtNuovoTesto.setText("Aggiungi Mezzo Andata");
                ModalitaNuovoDato = "MEZZOANDATA";

                cstmAdptMezziAndata = new AdapterListenerMezzi(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi(),
                        true,
                        true,
                        "");
                lstNuovoDato.setAdapter(cstmAdptMezziAndata);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getLayNuovoDato().setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgNuovoMezzoRitorno = findViewById(R.id.imgNuovoMezzoRitorno);
        imgNuovoMezzoRitorno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtNuovoTesto.setText("Aggiungi Mezzo Ritorno");
                ModalitaNuovoDato = "MEZZORITORNO";

                cstmAdptMezziRitorno = new AdapterListenerMezzi(context,
                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi(),
                        true,
                        false,
                        "");
                lstNuovoDato.setAdapter(cstmAdptMezziRitorno);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getLayNuovoDato().setVisibility(LinearLayout.VISIBLE);
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

        VariabiliStaticheOrari.getInstance().setLayBloccoSfondo(findViewById(R.id.layBloccoSfondo));
        VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.GONE);
        LinearLayout layGestione = findViewById(R.id.layGestioneValori);
        layGestione.setVisibility(LinearLayout.GONE);
        TextView txtGestioneValoriTesto = findViewById(R.id.txtGestioneTesto);
        txtGestioneValoriTesto.setText("");

        VariabiliStaticheOrari.getInstance().setSpnValori(findViewById(R.id.spnValori));
        VariabiliStaticheOrari.getInstance().setEdtValori(findViewById(R.id.edtValori));

        Modalita = "";
        ValoreImpostato = "";
        String[] listaTipiLavoro = {"Lavoro", "Ferie", "Permesso", "Malattia", "Altro", "Smart Working"};
        int[] valoriTipoLavoro = {0, -2, -3, -4, -5, -6};

        VariabiliStaticheOrari.getInstance().getEdtValori().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ValoreImpostato = s.toString();
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

        final boolean[] primoIngresso = {true};
        VariabiliStaticheOrari.getInstance().getSpnValori().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                ValoreImpostato = adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        ImageView imgSalvaValore = findViewById(R.id.imgSalvaValori);
        imgSalvaValore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();

                switch (Modalita) {
                    case "TIPOLAVORO":
                        int i = 0;
                        if (ValoreImpostato.equals("Lavoro")) {
                            if (sdg.getOreStandard() > 0) {
                                sdg.setQuanteOre(sdg.getOreStandard());
                                VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText(Integer.toString(sdg.getOreStandard()));
                            } else {
                                sdg.setQuanteOre(8);
                                VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText("8");
                            }

                            String LavoroDefault = "";
                            if (VariabiliStaticheOrari.getInstance().getStrutturaDati() != null) {
                                for (StrutturaLavoro s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori()) {
                                    if (s.getIdLavoro() == sdg.getLavoroDefault()) {
                                        LavoroDefault = s.getLavoro();
                                        sdg.setLavoro(LavoroDefault);
                                        sdg.setIdLavoro(s.getIdLavoro());
                                        break;
                                    }
                                }
                            }
                            VariabiliStaticheOrari.getInstance().getTxtLavoro().setText(
                                    LavoroDefault
                            );

                            if (VariabiliStaticheOrari.getInstance().getListaCommesse() == null) {
                                ChiamateWSOrari ws = new ChiamateWSOrari(context);
                                ws.RitornaCommesseLavoro(Integer.toString(sdg.getLavoroDefault()), false, true);
                            } else {
                                String CommessaDefault = "";
                                for (StrutturaCommesse s : VariabiliStaticheOrari.getInstance().getListaCommesse()) {
                                    if (s.getIdCommessa() == sdg.getCommessaDefault()) {
                                        CommessaDefault = s.getDescrizione();
                                        sdg.setCommessa(s.getDescrizione());
                                        sdg.setCodCommessa(s.getIdCommessa());
                                        break;
                                    }
                                }
                                VariabiliStaticheOrari.getInstance().getTxtCommessa().setText(
                                        CommessaDefault
                                );
                            }
                        } else {
                            for (String l : listaTipiLavoro) {
                                if (l.equals(ValoreImpostato)) {
                                    sdg.setQuanteOre(valoriTipoLavoro[i]);
                                    String vtl = String.valueOf(valoriTipoLavoro[i]);
                                    VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText(vtl);
                                }
                                i++;
                            }
                        }
                        // VariabiliStaticheOrari.getInstance().getTxtTipoLavoro().setText(ValoreImpostato);
                        // VariabiliStaticheOrari.getInstance().getTxtCommessa().setText("");
                        break;
                    case "ORELAVORO":
                        if (ValoreImpostato.isEmpty()) {
                            UtilitiesGlobali.getInstance().ApreToast(context, "Inserire un valore");
                            VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText("");
                            return;
                        }
                        if (Integer.parseInt(ValoreImpostato) < 1 || Integer.parseInt(ValoreImpostato) > 13) {
                            UtilitiesGlobali.getInstance().ApreToast(context, "Valore non valido ");
                            VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText("");
                            return;
                        }
                        sdg.setQuanteOre(Integer.parseInt(ValoreImpostato));
                        VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText(ValoreImpostato);
                        break;
                    case "ENTRATA":
                        if (UtilityOrari.getInstance().ControllaFormatodata(context, ValoreImpostato)) {
                            sdg.setEntrata(ValoreImpostato);
                            VariabiliStaticheOrari.getInstance().getEdtEntrata().setText(ValoreImpostato);
                        } else {
                            VariabiliStaticheOrari.getInstance().getEdtEntrata().setText("");
                            return;
                        }
                        break;
                    case "LAVORO":
                        sdg.setLavoro(ValoreImpostato);
                        sdg.setCommessa("");
                        VariabiliStaticheOrari.getInstance().getTxtLavoro().setText(ValoreImpostato);
                        break;
                    case "COMMESSA":
                        sdg.setCommessa(ValoreImpostato);
                        for (StrutturaCommesse s : VariabiliStaticheOrari.getInstance().getListaCommesse()) {
                            if (ValoreImpostato.equals(s.getDescrizione())) {
                                sdg.setCodCommessa(s.getIdCommessa());
                                break;
                            }
                        }
                        break;
                    case "NOTE":
                        sdg.setNote(ValoreImpostato);
                        VariabiliStaticheOrari.getInstance().getEdtNote().setText(ValoreImpostato);
                        break;
                    case "TEMPO":
                        sdg.setTempo(ValoreImpostato);
                        UtilityOrari.getInstance().disegnaIconaTempo(context, sdg.getTempo());

                        VariabiliStaticheOrari.getInstance().getTxtTempo().setText(ValoreImpostato);
                        break;
                    case "GRADI":
                        sdg.setGradi(ValoreImpostato);
                        VariabiliStaticheOrari.getInstance().getEdtGradi().setText(ValoreImpostato);
                        break;
                    case "PASTICCA":
                        sdg.getPasticca().get(0).setPasticca(ValoreImpostato);
                        VariabiliStaticheOrari.getInstance().getTxtPasticca().setText(ValoreImpostato);
                        break;
                }

                UtilityOrari.getInstance().ScriveDatiGiornata(context);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.GONE);
                layGestione.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaValore = findViewById(R.id.imgAnnullaValori);
        imgAnnullaValore.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.GONE);
                  layGestione.setVisibility(LinearLayout.GONE);
              }
        });

        ImageView imgElimina = findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Orari");
                builder.setMessage("Si vuole eliminare la giornata attuale?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setGiornoInserito(false);
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setSoloNote(false);

                        ChiamateWSOrari ws = new ChiamateWSOrari(context);
                        ws.EliminaOrario();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        ImageView imgCambiaTipoLavoro = findViewById(R.id.imgCambiaTipoLavoro);
        imgCambiaTipoLavoro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Tipo Lavoro");
                Modalita = "TIPOLAVORO";

                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.GONE);

                // ArrayAdapter<String> adapter = new ArrayAdapter<String>
                //         (context, android.R.layout.simple_spinner_item, listaTipiLavoro);
                // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                UtilitiesGlobali.getInstance().ImpostaSpinner(
                        context,
                        VariabiliStaticheOrari.getInstance().getSpnValori(),
                        listaTipiLavoro,
                        ""
                );

                /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                        context,
                        listaTipiLavoro
                );
                VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter); */

                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                if (sdg != null && sdg.isGiornoInserito()) {
                    String tipoLavoro = "Lavoro";
                    int i = 0;
                    int qualeLavoro = 0;
                    for (int vv : valoriTipoLavoro) {
                        if (vv == sdg.getQuanteOre()) {
                            tipoLavoro = listaTipiLavoro[i];
                            qualeLavoro = i;
                            break;
                        }
                        i++;
                    }
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(tipoLavoro);
                    VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(qualeLavoro);
                } else {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt("");
                    VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(0);
                }

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgCambiaOreLavoro = findViewById(R.id.imgCambiaOreLavoro);
        imgCambiaOreLavoro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Ore lavorative");
                Modalita = "ORELAVORO";

                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getEdtValori().setInputType(InputType.TYPE_CLASS_NUMBER);

                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                int ore = 0;
                if (sdg == null || !sdg.isGiornoInserito()) {
                    if (sdg != null) {
                        ore = sdg.getOreStandard();
                    } else {
                        ore = 8;
                    }
                } else {
                    ore = sdg.getQuanteOre();
                }
                VariabiliStaticheOrari.getInstance().getEdtValori().setText(Integer.toString(ore));

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgCambiaEntrata = findViewById(R.id.imgCambiaEntrata);
        imgCambiaEntrata.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Entrata");
                Modalita = "ENTRATA";

                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getEdtValori().setInputType(InputType.TYPE_CLASS_TEXT);

                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                String entrata = "";
                if (sdg == null || !sdg.isGiornoInserito()) {
                    entrata = "08:00:00";
                } else {
                    entrata = sdg.getEntrata();
                }
                VariabiliStaticheOrari.getInstance().getEdtValori().setText(entrata);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgCambiaLavoro = findViewById(R.id.imgCambiaLavoro);
        imgCambiaLavoro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Lavoro");
                Modalita = "LAVORO";

                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.VISIBLE);

                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                String[] lista = new String[VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori().size()];
                int i = 0;
                int qualeRiga = 0;
                String lavoroDefault = "";
                for (StrutturaLavoro s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori()) {
                    if (sdg != null && sdg.isGiornoInserito()) {
                        if (s.getLavoro().equals(sdg.getLavoro())) {
                            qualeRiga = i;
                        }
                    } else {
                        if (sdg.getLavoroDefault() == s.getIdLavoro()) {
                            lavoroDefault = s.getLavoro();
                            qualeRiga = i;
                        }
                    }
                    lista[i] = s.getLavoro();
                    i++;
                }

                UtilitiesGlobali.getInstance().ImpostaSpinner(
                        context,
                        VariabiliStaticheOrari.getInstance().getSpnValori(),
                        lista,
                        ""
                );

                // ArrayAdapter<String> adapter = new ArrayAdapter<String>
                //         (context, android.R.layout.simple_spinner_item, lista);
                // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                        context,
                        lista
                );
                VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter); */

                if (sdg == null || !sdg.isGiornoInserito()) {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(lavoroDefault);
                } else {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(sdg.getLavoro());
                }
                VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(qualeRiga);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgCambiaCommessa = findViewById(R.id.imgCambiaCommessa);
        imgCambiaCommessa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();

                txtGestioneValoriTesto.setText("Commessa " + sdg.getLavoro());
                Modalita = "COMMESSA";

                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.VISIBLE);

                int idLavoro = -1;
                for (StrutturaLavoro s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori()) {
                    if (sdg != null && sdg.isGiornoInserito()) {
                        if (s.getLavoro().equals(sdg.getLavoro())) {
                            idLavoro = s.getIdLavoro();
                        }
                    } else {
                        if (sdg.getLavoroDefault() == s.getIdLavoro()) {
                            idLavoro = s.getIdLavoro();
                        }
                    }
                }

                ChiamateWSOrari ws = new ChiamateWSOrari(context);
                ws.RitornaCommesseLavoro(Integer.toString(idLavoro), false, false);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgCambiaNote = findViewById(R.id.imgCambiaNote);
        imgCambiaNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Note");
                Modalita = "NOTE";

                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getEdtValori().setInputType(InputType.TYPE_CLASS_TEXT);

                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                VariabiliStaticheOrari.getInstance().getEdtValori().setText(sdg.getNote());

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheOrari.getInstance().setImgIconaTempo(findViewById(R.id.imgIconaTempo));
        VariabiliStaticheOrari.getInstance().getImgIconaTempo().setVisibility(LinearLayout.GONE);

        ImageView imgTrovaTempo = findViewById(R.id.imgTrovaTempo);
        imgTrovaTempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classeMeteo cl = new classeMeteo();
                cl.RitornaMeteo(context, "ORARI");

                VariabiliStaticheStart.getInstance().setHaPresoMeteo(false);

                handlerTimerMeteo = new Handler(Looper.getMainLooper());
                rTimerMeteo = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (VariabiliStaticheStart.getInstance().isHaPresoMeteo()) {
                                    handlerTimerMeteo.removeCallbacksAndMessages(rTimerMeteo);

                                    StrutturaMeteo s = VariabiliStaticheStart.getInstance().getMeteo();
                                    UtilityOrari.getInstance().disegnaIconaTempo(context, s.getTesto());

                                    VariabiliStaticheOrari.getInstance().getTxtTempo().setText(s.getTesto());
                                    VariabiliStaticheOrari.getInstance().getEdtGradi().setText(Double.toString(s.getTemperatura()));

                                    // Controlla se il meteo è già esistente oppure devo aggiungerlo
                                    boolean ok = false;
                                    int massimo = 0;
                                    for (StrutturaTempo st : VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi()) {
                                        if (s.getTesto().equals(st.getTempo())) {
                                            ok = true;
                                        }
                                        if (st.getIdTempo() > massimo) {
                                            massimo = st.getIdTempo();
                                        }
                                    }
                                    if (!ok) {
                                        StrutturaTempo st = new StrutturaTempo();
                                        st.setTempo(s.getTesto());
                                        st.setIdTempo(massimo + 1);
                                        st.setUrlIcona(s.getIcona());

                                        VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi().add(st);

                                        String PathFile = VariabiliStaticheOrari.getInstance().getPathOrari();
                                        String NomeFile = "Dati.txt";
                                        if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                                            Files.getInstance().EliminaFile(PathFile, NomeFile);
                                        }

                                        String[] lista = new String[VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi().size()];
                                        int i = 0;
                                        for (StrutturaTempo st2 : VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi()) {
                                            lista[i] = st2.getTempo();
                                        }

                                        UtilitiesGlobali.getInstance().ImpostaSpinner(
                                                context,
                                                VariabiliStaticheOrari.getInstance().getSpnValori(),
                                                lista,
                                                ""
                                        );

                                        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        //         (context, android.R.layout.simple_spinner_item, lista);
                                        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                                                context,
                                                lista
                                        );
                                        VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter); */

                                        ChiamateWSOrari c = new ChiamateWSOrari(context);
                                        c.AggiungeMeteo(String.valueOf(st.getIdTempo()), st.getTempo(), st.getUrlIcona());
                                    }
                                } else {
                                    handlerTimerMeteo.postDelayed(rTimerMeteo, 500);
                                }
                            }
                        }, 500);
                    }
                };
                handlerTimerMeteo.postDelayed(rTimerMeteo, 500);
            }
        });

        ImageView imgCambiaTempo = findViewById(R.id.imgCambiaTempo);
        imgCambiaTempo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Tempo");
                Modalita = "TEMPO";

                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.VISIBLE);

                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                String[] lista = new String[VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi().size()];
                int i = 0;
                int qualeRiga = 0;
                String tempoDefault = "";
                for (StrutturaTempo s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi()) {
                    if (sdg != null && sdg.isGiornoInserito()) {
                        if (s.getTempo().equals(sdg.getTempo())) {
                            qualeRiga = i;
                        }
                    }
                    lista[i] = s.getTempo();
                    i++;
                }

                UtilitiesGlobali.getInstance().ImpostaSpinner(
                        context,
                        VariabiliStaticheOrari.getInstance().getSpnValori(),
                        lista,
                        ""
                );

                // ArrayAdapter<String> adapter = new ArrayAdapter<String>
                //         (context, android.R.layout.simple_spinner_item, lista);
                // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                        context,
                        lista
                );
                VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter); */

                if (sdg == null || !sdg.isGiornoInserito()) {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(tempoDefault);
                } else {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(sdg.getTempo());
                }
                VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(qualeRiga);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgCambiaGradi = findViewById(R.id.imgCambiaGradi);
        imgCambiaGradi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Gradi");
                Modalita = "GRADI";

                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getEdtValori().setInputType(InputType.TYPE_CLASS_NUMBER);

                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                VariabiliStaticheOrari.getInstance().getEdtValori().setText(sdg.getGradi());

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgCambiaPasticca = findViewById(R.id.imgCambiaPasticca);
        imgCambiaPasticca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Pasticca");
                Modalita = "PASTICCA";

                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.VISIBLE);

                StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                String[] lista = new String[VariabiliStaticheOrari.getInstance().getStrutturaDati().getPasticche().size()];
                int i = 0;
                int qualeRiga = 0;
                for (StrutturaPasticca s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getPasticche()) {
                    if (sdg != null && sdg.isGiornoInserito()) {
                        if (s.getPasticca().equals(sdg.getPasticca().get(0).getPasticca())) {
                            qualeRiga = i;
                        }
                    }
                    lista[i] = s.getPasticca();
                    i++;
                }

                UtilitiesGlobali.getInstance().ImpostaSpinner(
                        context,
                        VariabiliStaticheOrari.getInstance().getSpnValori(),
                        lista,
                        ""
                );

                // ArrayAdapter<String> adapter = new ArrayAdapter<String>
                //         (context, android.R.layout.simple_spinner_item, lista);
                // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                        context,
                        lista
                );
                VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter); */

                if (sdg == null || !sdg.isGiornoInserito()) {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt("");
                } else {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(sdg.getPasticca().get(0).getPasticca());
                }
                VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(qualeRiga);

                VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgImpostazioni = findViewById(R.id.imgImpostazioni);
        imgImpostazioni.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iO = new Intent(context, MainImpostazioniOrari.class);
                iO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iO);
            }
        });

        ImageView imgStatistiche = findViewById(R.id.imgStatistiche);
        imgStatistiche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iO = new Intent(context, MainStatisticheOrari.class);
                iO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iO);
            }
        });

        ImageView imgSalva = findViewById(R.id.imgSalva);
        imgSalva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSOrari ws = new ChiamateWSOrari(context);
                ws.ScriveOrario();
            }
        });

        ChiamateWSOrari ws = new ChiamateWSOrari(context);
        ws.RitornaDatiPerModifica(false, false);

        ScriveData(context, txtData, txtNomeGiorno);
    }

    private void ScriveData(Context context, TextView txtData, TextView txtNomeGiorno) {
        String oggi = UtilityOrari.getInstance().RitornaData();
        String[] o = oggi.split(";");

        String tipoGiorno = "";
        int colore = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(VariabiliStaticheOrari.getInstance().getDataAttuale());

        int Giorno = calendar.get(Calendar.DAY_OF_MONTH);
        int Mese = calendar.get(Calendar.MONTH) + 1;
        int Anno = calendar.get(Calendar.YEAR);

        String[] Giorni = {"Domenica", "Lunedì", "Martedì", "Mercoledì",
                "Giovedì", "Venerdì", "Sabato" };
        int numeroGiorni = 0;
        int giorniMese = 30;

        if (Mese == 2) {
            giorniMese = 28;
        } else {
            if (Mese == 1 || Mese == 3 || Mese == 5 || Mese == 7 || Mese == 8 || Mese == 10 || Mese == 12) {
                giorniMese = 31;
            }
        }

        int giorniAnno = 365;
        if (Anno%400 == 0 || (Anno%4 == 0 && Anno%100 != 0)) {
            giorniAnno = 366;
            if (Mese == 2) {
                giorniMese = 29;
            }
        }

        for (int i = 1; i <= giorniMese; i++) {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.DAY_OF_MONTH, i);
            calendar2.set(Calendar.MONTH, Mese - 1);
            calendar2.set(Calendar.YEAR, Anno);

            int numeroGiorno = calendar2.get(Calendar.DAY_OF_WEEK) - 1;
            String NomeGiorno = Giorni[numeroGiorno];
            if (!NomeGiorno.equals("Sabato") && !NomeGiorno.equals("Domenica")) {
                numeroGiorni++;
            }
        }

        // Controllo festività
        switch(Mese) {
            case 1:
                numeroGiorni-=2;

                switch (Giorno) {
                    case 1:
                        colore = 1;
                        tipoGiorno = "Capodanno";
                        break;
                    case 6:
                        colore = 1;
                        tipoGiorno = "Befana";
                        break;
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                numeroGiorni-=1;

                switch (Giorno) {
                    case 25:
                        colore = 1;
                        tipoGiorno = "Liberazione";
                        break;
                }
                break;
            case 5:
                numeroGiorni-=1;

                switch (Giorno) {
                    case 1:
                        colore = 1;
                        tipoGiorno = "Festa del lavoro";
                        break;
                }
                break;
            case 6:
                numeroGiorni-=1;

                switch (Giorno) {
                    case 2:
                        colore = 1;
                        tipoGiorno = "Festa Repubblica";
                        break;
                }
                break;
            case 7:
                break;
            case 8:
                numeroGiorni-=1;

                switch (Giorno) {
                    case 15:
                        colore = 1;
                        tipoGiorno = "Ferragosto";
                        break;
                }
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                numeroGiorni-=1;

                switch (Giorno) {
                    case 1:
                        colore = 1;
                        tipoGiorno = "Ognissanti";
                        break;
                }
                break;
            case 12:
                numeroGiorni-=3;

                switch (Giorno) {
                    case 15:
                        colore = 1;
                        tipoGiorno = "Immacolata";
                        break;
                    case 25:
                        colore = 1;
                        tipoGiorno = "Natale";
                        break;
                    case 26:
                        colore = 1;
                        tipoGiorno = "Santo Stefano";
                        break;
                }
                break;
        }

        if (colore == 0) {
            String Pasqua = UtilityOrari.getInstance().RitornaPasqua(Anno);
            String[] psq = Pasqua.split(";");
            int giornoPasqua = Integer.parseInt(psq[0]);
            int mesePasqua = Integer.parseInt(psq[1]);

            if (giornoPasqua == Giorno && mesePasqua == Mese) {
                numeroGiorni-=2;

                colore = 1;
                tipoGiorno = "Pasqua";
            } else {
                int giornoPasquetta = giornoPasqua + 1;
                int mesePasquetta = mesePasqua;
                if (mesePasquetta == 3 || mesePasquetta == 5) {
                    if (giornoPasquetta > 31) {
                        giornoPasquetta = 1;
                        mesePasquetta++;
                    }
                } else {
                    if (giornoPasquetta > 30) {
                        giornoPasquetta = 1;
                        mesePasquetta++;
                    }
                }

                if (giornoPasquetta  == Giorno && mesePasquetta == Mese) {
                    numeroGiorni-=2;

                    colore = 1;
                    tipoGiorno = "Pasquetta";
                }
            }
        }

        if (colore == 0) {
            if (o[1].equals("Sabato")) {
                colore = 2;
                tipoGiorno = "Festivo";
            } else {
                if (o[1].equals("Domenica")) {
                    colore = 1;
                    tipoGiorno = "Festivo";
                }
            }
        }

        if (colore == 0) {
            tipoGiorno = "Feriale";
        }

        String sColore = "#000000";
        switch (colore) {
            case 1:
                sColore = "#FF0000";
                break;
            case 2:
                sColore = "#AA0000";
                break;
        }
        VariabiliStaticheOrari.getInstance().getTxtTipoGiorno().setTextColor(Color.parseColor(sColore));
        txtNomeGiorno.setTextColor(Color.parseColor(sColore));
        VariabiliStaticheOrari.getInstance().getTxtNumeroGiorno().setText(calendar.get(Calendar.DAY_OF_YEAR) + "/" + giorniAnno);
        VariabiliStaticheOrari.getInstance().getTxtNumeroGiorno().setTextColor(Color.parseColor(sColore));
        VariabiliStaticheOrari.getInstance().getTxtTipoGiorno().setText(tipoGiorno);

        txtData.setText(o[0] + " (Lav. " + numeroGiorni + "/" + giorniMese + ")");
        txtNomeGiorno.setText(o[1]);

        ChiamateWSOrari ws = new ChiamateWSOrari(context);
        ws.RitornaDatiGiorno();
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
