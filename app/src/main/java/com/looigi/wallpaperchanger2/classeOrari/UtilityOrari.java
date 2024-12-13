package com.looigi.wallpaperchanger2.classeOrari;

import android.content.Context;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeOrari.adapters.AdapterListenerMezzi;
import com.looigi.wallpaperchanger2.classeOrari.adapters.AdapterListenerPortate;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;
import com.looigi.wallpaperchanger2.classePassword.AdapterListenerPassword;
import com.looigi.wallpaperchanger2.classePassword.VariabiliStatichePWD;

import java.util.Calendar;

public class UtilityOrari {
    private static UtilityOrari instance = null;

    private UtilityOrari() {
    }

    public static UtilityOrari getInstance() {
        if (instance == null) {
            instance = new UtilityOrari();
        }

        return instance;
    }

    private String[] Mese = { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
        "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
    private String[] Giorni = {"Domenica", "Lunedì", "Martedì", "Mercoledì",
        "Giovedì", "Venerdì", "Sabato" };

    public String RitornaData() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(VariabiliStaticheOrari.getInstance().getDataAttuale());
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).trim();

        if (day.length() == 1) { day = "0" + day; }
        int month = calendar.get(Calendar.MONTH);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).trim();

        int numeroGiorno = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        return day + " " + Mese[month] + " " + year + ";" + Giorni[numeroGiorno];
    }

    public void ScriveDatiGiornata(Context context) {
        StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();

        if (sdg == null || !sdg.isGiornoInserito()) {
            VariabiliStaticheOrari.getInstance().getLayContenitore().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getLayAggiunge().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheOrari.getInstance().getLayContenitore().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheOrari.getInstance().getLayAggiunge().setVisibility(LinearLayout.GONE);

            int oreStandard = sdg.getOreStandard();

            String Lavoro = "";
            boolean visualizzaOre = true;

            if (sdg.getQuanteOre() == oreStandard) {
                Lavoro = "Lavoro in sede / Cliente";
            } else {
                switch (sdg.getQuanteOre()) {
                    case -1:
                        Lavoro = "BOH!";
                        break;
                    case -2:
                        Lavoro = "Ferie";
                        visualizzaOre = false;
                        break;
                    case -3:
                        Lavoro = "Permesso";
                        visualizzaOre = false;
                        break;
                    case -4:
                        Lavoro = "Malattia";
                        visualizzaOre = false;
                        break;
                    case -5:
                        Lavoro = "Altro";
                        visualizzaOre = false;
                        break;
                    case -6:
                        Lavoro = "Smart Working";
                        visualizzaOre = false;
                        break;
                }
            }

            VariabiliStaticheOrari.getInstance().getTxtTipoLavoro().setText(Lavoro);
            if (visualizzaOre) {
                VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText(Integer.toString(sdg.getQuanteOre()));
                VariabiliStaticheOrari.getInstance().getLayDettaglioGiornata().setVisibility(LinearLayout.VISIBLE);
            } else {
                VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText(Integer.toString(oreStandard));
                VariabiliStaticheOrari.getInstance().getLayDettaglioGiornata().setVisibility(LinearLayout.GONE);
            }
            VariabiliStaticheOrari.getInstance().getEdtEntrata().setText(sdg.getEntrata());
            VariabiliStaticheOrari.getInstance().getTxtLavoro().setText(sdg.getLavoro());
            VariabiliStaticheOrari.getInstance().getTxtCommessa().setText(sdg.getCommessa());
            VariabiliStaticheOrari.getInstance().getTxtTempo().setText(sdg.getTempo());
            VariabiliStaticheOrari.getInstance().getEdtGradi().setText(sdg.getGradi());
            VariabiliStaticheOrari.getInstance().getTxtPasticca().setText(sdg.getPasticca().get(0).getPasticca());
            VariabiliStaticheOrari.getInstance().getEdtNote().setText(sdg.getNote().toString());

            AdapterListenerPortate cstmAdptPranzo = new AdapterListenerPortate(context,
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getPranzo());
            VariabiliStaticheOrari.getInstance().getLstPranzo().setAdapter(cstmAdptPranzo);

            AdapterListenerMezzi cstmAdptMezziAndata = new AdapterListenerMezzi(context,
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziAndata());
            VariabiliStaticheOrari.getInstance().getLstMezziAndata().setAdapter(cstmAdptMezziAndata);

            AdapterListenerMezzi cstmAdptMezziRitorno = new AdapterListenerMezzi(context,
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziRitorno());
            VariabiliStaticheOrari.getInstance().getLstMezziRitorno().setAdapter(cstmAdptMezziRitorno);
        }
    }
}
