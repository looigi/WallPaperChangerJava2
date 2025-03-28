package com.looigi.wallpaperchanger2.classeLazio;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaAllenatori;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCalendario;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaGiocatori;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaMercato;
import com.looigi.wallpaperchanger2.classeLazio.webService.ChiamateWSLazio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UtilityLazio {
    private static UtilityLazio instance = null;

    private UtilityLazio() {
    }

    public static UtilityLazio getInstance() {
        if (instance == null) {
            instance = new UtilityLazio();
        }

        return instance;
    }

    public void ImpostaAttesa(boolean Come) {
        if (Come) {
            VariabiliStaticheLazio.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheLazio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        }
    }

    public void VisualizzaMaschera() {
        VariabiliStaticheLazio.getInstance().getLayCalendario().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayClassifica().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLaySquadre().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayMercato().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayFonti().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayStati().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayGiocatori().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayAllenatori().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayMarcatori().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayRuoli().setVisibility(LinearLayout.GONE);

        VariabiliStaticheLazio.getInstance().getImgAggiorna().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.GONE);

        switch (VariabiliStaticheLazio.getInstance().getMascheraSelezionata()) {
            case 1:
                VariabiliStaticheLazio.getInstance().getLayClassifica().setVisibility(LinearLayout.VISIBLE);
                break;
            case 2:
                VariabiliStaticheLazio.getInstance().getLayCalendario().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 3:
                VariabiliStaticheLazio.getInstance().getLaySquadre().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 4:
                VariabiliStaticheLazio.getInstance().getLayMercato().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 5:
                VariabiliStaticheLazio.getInstance().getLayFonti().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgAggiorna().setVisibility(LinearLayout.VISIBLE);
                break;
            case 6:
                VariabiliStaticheLazio.getInstance().getLayStati().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgAggiorna().setVisibility(LinearLayout.VISIBLE);
                break;
            case 7:
                VariabiliStaticheLazio.getInstance().getLayGiocatori().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 8:
                VariabiliStaticheLazio.getInstance().getLayRuoli().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgAggiorna().setVisibility(LinearLayout.VISIBLE);
                break;
            case 9:
                VariabiliStaticheLazio.getInstance().getLayAllenatori().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 10:
                VariabiliStaticheLazio.getInstance().getLayMarcatori().setVisibility(LinearLayout.VISIBLE);
                break;
        }
    }

    public void LeggeAnno(Context context) {
        ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
        ws1.RitornaSquadre();
    }

    public void ApreModifica(Context context, String Cosa, String Modalita, String Titolo, String Valore1) {
        VariabiliStaticheLazio.getInstance().setCosaStoModificando(Cosa);
        VariabiliStaticheLazio.getInstance().setModalitaModifica(Modalita);
        VariabiliStaticheLazio.getInstance().getTxtModifica().setText(Titolo);

        VariabiliStaticheLazio.getInstance().getLayModificaSFS().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayModificaMercato().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayModificaGiocatore().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayModificaCalendario().setVisibility(LinearLayout.GONE);

        switch (Cosa) {
            case "CALENDARIO":
                String[] righePerSpinner = new String[0];

                if (!Valore1.isEmpty()) {
                    righePerSpinner = VariabiliStaticheLazio.getInstance().getRighePerSquadre();
                    int riga = Integer.parseInt(Valore1);
                    StrutturaCalendario s = VariabiliStaticheLazio.getInstance().getCalendario().get(riga);
                    String ris = s.getRisultato1() + "-" + s.getRisultato2();
                    VariabiliStaticheLazio.getInstance().getEdtDataCal().setText(s.getDataPartita());
                    VariabiliStaticheLazio.getInstance().getEdtRisultato().setText(ris);
                } else {
                    List<String> sq = new ArrayList<>();
                    for (String squadra : VariabiliStaticheLazio.getInstance().getRighePerSquadre()) {
                        if (!squadra.isEmpty()) {
                            boolean ok = true;
                            for (StrutturaCalendario s : VariabiliStaticheLazio.getInstance().getCalendario()) {
                                if (s.getCasa().equals(squadra) || s.getFuori().equals(squadra)) {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok) {
                                sq.add(squadra);
                            }
                        }
                    }
                    if (!sq.isEmpty()) {
                        righePerSpinner = new String[sq.size() - 1];
                        int i = 0;
                        for (String s : sq) {
                            righePerSpinner[i] = s;
                            i++;
                        }
                    } else {
                        righePerSpinner = new String[0];
                    }
                    VariabiliStaticheLazio.getInstance().getEdtDataCal().setText("");
                    VariabiliStaticheLazio.getInstance().getEdtRisultato().setText("");
                    VariabiliStaticheLazio.getInstance().getSpnCasa().setPrompt("");
                    VariabiliStaticheLazio.getInstance().getSpnFuori().setPrompt("");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item, righePerSpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                VariabiliStaticheLazio.getInstance().getSpnCasa().setAdapter(adapter);
                VariabiliStaticheLazio.getInstance().getSpnFuori().setAdapter(adapter);

                if (!Valore1.isEmpty()) {
                    int riga = Integer.parseInt(Valore1);
                    StrutturaCalendario s = VariabiliStaticheLazio.getInstance().getCalendario().get(riga);

                    VariabiliStaticheLazio.getInstance().setIdCasa(s.getIdSquadraCasa());
                    int spinnerPosition = adapter.getPosition(s.getCasa());
                    VariabiliStaticheLazio.getInstance().getSpnCasa().setSelection(spinnerPosition);

                    VariabiliStaticheLazio.getInstance().setIdFuori(s.getIdSquadraFuori());
                    spinnerPosition = adapter.getPosition(s.getFuori());
                    VariabiliStaticheLazio.getInstance().getSpnFuori().setSelection(spinnerPosition);
                } else {
                    VariabiliStaticheLazio.getInstance().setIdCasa(0);
                    VariabiliStaticheLazio.getInstance().setIdFuori(0);
                }

                VariabiliStaticheLazio.getInstance().getLayModificaCalendario().setVisibility(LinearLayout.VISIBLE);
                break;
            case "GIOCATORI":
                VariabiliStaticheLazio.getInstance().getLayRuolo().setVisibility(LinearLayout.VISIBLE);
                if (!Valore1.isEmpty()) {
                    int riga = Integer.parseInt(Valore1);
                    StrutturaGiocatori s = VariabiliStaticheLazio.getInstance().getGiocatori().get(riga);
                    VariabiliStaticheLazio.getInstance().getEdtCognome().setText(s.getCognome());
                    VariabiliStaticheLazio.getInstance().getEdtNome().setText(s.getNome());
                    int spinnerPosition = VariabiliStaticheLazio.getInstance().getAdapterRuoli().getPosition(s.getRuolo());
                    VariabiliStaticheLazio.getInstance().getSpnRuolo().setSelection(spinnerPosition);
                } else {
                    VariabiliStaticheLazio.getInstance().getEdtCognome().setText("");
                    VariabiliStaticheLazio.getInstance().getEdtNome().setText("");
                }

                VariabiliStaticheLazio.getInstance().getLayModificaGiocatore().setVisibility(LinearLayout.VISIBLE);
                break;
            case "ALLENATORI":
                VariabiliStaticheLazio.getInstance().getLayRuolo().setVisibility(LinearLayout.GONE);
                if (!Valore1.isEmpty()) {
                    int riga = Integer.parseInt(Valore1);
                    StrutturaAllenatori s = VariabiliStaticheLazio.getInstance().getAllenatori().get(riga);
                    VariabiliStaticheLazio.getInstance().getEdtCognome().setText(s.getCognome());
                    VariabiliStaticheLazio.getInstance().getEdtNome().setText(s.getNome());
                } else {
                    VariabiliStaticheLazio.getInstance().getEdtCognome().setText("");
                    VariabiliStaticheLazio.getInstance().getEdtNome().setText("");
                }
                VariabiliStaticheLazio.getInstance().getLayModificaGiocatore().setVisibility(LinearLayout.VISIBLE);
                break;
            case "SQUADRE":
                VariabiliStaticheLazio.getInstance().getEdtValore1().setText(Valore1);
                VariabiliStaticheLazio.getInstance().setValoreImpostato1(Valore1);
                VariabiliStaticheLazio.getInstance().getLayModificaSFS().setVisibility(LinearLayout.VISIBLE);
                break;
            case "RUOLI":
                VariabiliStaticheLazio.getInstance().getEdtValore1().setText(Valore1);
                VariabiliStaticheLazio.getInstance().setValoreImpostato1(Valore1);
                VariabiliStaticheLazio.getInstance().getLayModificaSFS().setVisibility(LinearLayout.VISIBLE);
                break;
            case "MERCATO":
                if (!Valore1.isEmpty()) {
                    int riga = Integer.parseInt(Valore1);
                    StrutturaMercato s = VariabiliStaticheLazio.getInstance().getMercato().get(riga);
                    VariabiliStaticheLazio.getInstance().setIdPerOperazione(s.getProgressivo());
                    VariabiliStaticheLazio.getInstance().getEdtData().setText(s.getData());
                    VariabiliStaticheLazio.getInstance().getEdtNominativo().setText(s.getNominativo());

                    VariabiliStaticheLazio.getInstance().setIdStato(s.getIdStato());
                    int spinnerPosition = VariabiliStaticheLazio.getInstance().getAdapterStati().getPosition(s.getStato());
                    VariabiliStaticheLazio.getInstance().getSpnStati().setSelection(spinnerPosition);

                    VariabiliStaticheLazio.getInstance().setIdFonte(s.getIdFonte());
                    spinnerPosition = VariabiliStaticheLazio.getInstance().getAdapterFonti().getPosition(s.getFonte());
                    VariabiliStaticheLazio.getInstance().getSpnFonti().setSelection(spinnerPosition);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdfD = new SimpleDateFormat("dd-MM");
                    String dataOdierna = sdfD.format(calendar.getTime());
                    VariabiliStaticheLazio.getInstance().setIdPerOperazione(-1);
                    VariabiliStaticheLazio.getInstance().getEdtData().setText(dataOdierna);
                    VariabiliStaticheLazio.getInstance().getEdtNominativo().setText("");

                    VariabiliStaticheLazio.getInstance().setIdStato(1);
                    int spinnerPosition = VariabiliStaticheLazio.getInstance().getAdapterStati().getPosition("Voce");
                    VariabiliStaticheLazio.getInstance().getSpnStati().setSelection(spinnerPosition);
                    // VariabiliStaticheLazio.getInstance().getSpnStati().setPrompt("Voce");

                    VariabiliStaticheLazio.getInstance().setIdFonte(0);
                    int spinnerPositionF = VariabiliStaticheLazio.getInstance().getAdapterFonti().getPosition("");
                    VariabiliStaticheLazio.getInstance().getSpnFonti().setSelection(spinnerPositionF);
                    // VariabiliStaticheLazio.getInstance().getSpnFonti().setPrompt("");
                }

                VariabiliStaticheLazio.getInstance().getLayModificaMercato().setVisibility(LinearLayout.VISIBLE);
                break;
            case "FONTI":
                VariabiliStaticheLazio.getInstance().getEdtValore1().setText(Valore1);
                VariabiliStaticheLazio.getInstance().setValoreImpostato1(Valore1);
                VariabiliStaticheLazio.getInstance().getLayModificaSFS().setVisibility(LinearLayout.VISIBLE);
                break;
            case "STATI":
                VariabiliStaticheLazio.getInstance().getEdtValore1().setText(Valore1);
                VariabiliStaticheLazio.getInstance().setValoreImpostato1(Valore1);
                VariabiliStaticheLazio.getInstance().getLayModificaSFS().setVisibility(LinearLayout.VISIBLE);
                break;
        }

        VariabiliStaticheLazio.getInstance().getLayModifica().setVisibility(LinearLayout.VISIBLE);
    }

    public void SalvaValori(Context context) {
        ChiamateWSLazio ws = new ChiamateWSLazio(context);

        switch (VariabiliStaticheLazio.getInstance().getCosaStoModificando()) {
            case "ANNO":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        ws.GestioneAnno();
                        break;
                    case "UPDATE":
                        ws.GestioneAnno();
                        break;
                    case "DELETE":
                        break;
                }
                break;
            case "COMPETIZIONE":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        break;
                    case "UPDATE":
                        break;
                    case "DELETE":
                        break;
                }
                break;
            case "CALENDARIO":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        break;
                    case "UPDATE":
                        break;
                    case "DELETE":
                        break;
                }
                break;
            case "SQUADRE":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        break;
                    case "UPDATE":
                        break;
                    case "DELETE":
                        break;
                }
                break;
            case "MERCATO":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                    case "NUOVO":
                        ws.GestioneMercato();
                        break;
                    case "UPDATE":
                        ws.GestioneMercato();
                        break;
                }
                break;
            case "ALLENATORI":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        break;
                    case "UPDATE":
                        break;
                    case "DELETE":
                        break;
                }
                break;
            case "FONTI":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        break;
                    case "UPDATE":
                        break;
                    case "DELETE":
                        break;
                }
                break;
            case "STATI":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        break;
                    case "UPDATE":
                        break;
                    case "DELETE":
                        break;
                }
                break;
            case "GIOCATORI":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        break;
                    case "UPDATE":
                        break;
                    case "DELETE":
                        break;
                }
                break;
            case "RUOLI":
                switch (VariabiliStaticheLazio.getInstance().getModalitaModifica()) {
                    case "INSERT":
                        break;
                    case "UPDATE":
                        break;
                    case "DELETE":
                        break;
                }
                break;
        }
    }
}
