package com.looigi.wallpaperchanger2.classeOrari;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classeDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeGps.GestioneMappa;
import com.looigi.wallpaperchanger2.classeGps.GestioneNotificaGPS;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classeOnomastici.MainOnomastici;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni_orari.MainImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.statisticheOrari.MainStatisticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaCommesse;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaLavoro;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPasticca;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaTempo;
import com.looigi.wallpaperchanger2.classeOrari.webService.ChiamateWSOrari;
import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.db_dati_wallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainOrari extends Activity {
    private Context context;
    private Activity act;
    private String Modalita;
    private String ValoreImpostato;

    public MainOrari() {
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

        LinearLayout layBloccoSfondo = findViewById(R.id.layBloccoSfondo);
        layBloccoSfondo.setVisibility(LinearLayout.GONE);
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
                            } else {
                                sdg.setQuanteOre(8);
                            }
                        } else {
                            for (String l : listaTipiLavoro) {
                                if (l.equals(ValoreImpostato)) {
                                    sdg.setQuanteOre(valoriTipoLavoro[i]);
                                }
                                i++;
                            }
                        }
                        break;
                    case "ORELAVORO":
                        if (ValoreImpostato.isEmpty()) {
                            UtilitiesGlobali.getInstance().ApreToast(context, "Inserire un valore");
                            return;
                        }
                        if (Integer.parseInt(ValoreImpostato) < 1 || Integer.parseInt(ValoreImpostato) > 13) {
                            UtilitiesGlobali.getInstance().ApreToast(context, "Valore non valido ");
                            return;
                        }
                        sdg.setQuanteOre(Integer.parseInt(ValoreImpostato));
                        break;
                    case "ENTRATA":
                        if (UtilityOrari.getInstance().ControllaFormatodata(context, ValoreImpostato)) {
                            sdg.setEntrata(ValoreImpostato);
                        } else {
                            return;
                        }
                        break;
                    case "LAVORO":
                        sdg.setLavoro(ValoreImpostato);
                        sdg.setCommessa("");
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
                        break;
                    case "TEMPO":
                        sdg.setTempo(ValoreImpostato);
                        break;
                    case "GRADI":
                        sdg.setGradi(ValoreImpostato);
                        break;
                    case "PASTICCA":
                        sdg.getPasticca().get(0).setPasticca(ValoreImpostato);
                        break;
                }

                UtilityOrari.getInstance().ScriveDatiGiornata(context);

                layBloccoSfondo.setVisibility(LinearLayout.GONE);
                layGestione.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaValore = findViewById(R.id.imgAnnullaValori);
        imgAnnullaValore.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  layBloccoSfondo.setVisibility(LinearLayout.GONE);
                  layGestione.setVisibility(LinearLayout.GONE);
              }
          });

        ImageView imgCambiaTipoLavoro = findViewById(R.id.imgCambiaTipoLavoro);
        imgCambiaTipoLavoro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGestioneValoriTesto.setText("Tipo Lavoro");
                Modalita = "TIPOLAVORO";

                VariabiliStaticheOrari.getInstance().getSpnValori().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheOrari.getInstance().getEdtValori().setVisibility(LinearLayout.GONE);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item, listaTipiLavoro);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter);

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

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
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

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
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

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item, lista);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter);

                if (sdg == null || !sdg.isGiornoInserito()) {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(lavoroDefault);
                } else {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(sdg.getLavoro());
                }
                VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(qualeRiga);

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
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
                ws.RitornaCommesseLavoro(Integer.toString(idLavoro));

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
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

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
                layGestione.setVisibility(LinearLayout.VISIBLE);
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item, lista);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter);

                if (sdg == null || !sdg.isGiornoInserito()) {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(tempoDefault);
                } else {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(sdg.getTempo());
                }
                VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(qualeRiga);

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
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

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item, lista);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter);

                if (sdg == null || !sdg.isGiornoInserito()) {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt("");
                } else {
                    VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(sdg.getPasticca().get(0).getPasticca());
                }
                VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(qualeRiga);

                layBloccoSfondo.setVisibility(LinearLayout.VISIBLE);
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
            }
        });

        ChiamateWSOrari ws = new ChiamateWSOrari(context);
        ws.RitornaDatiPerModifica(false);

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
