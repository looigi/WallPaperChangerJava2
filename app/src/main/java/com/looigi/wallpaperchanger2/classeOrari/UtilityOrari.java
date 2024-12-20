package com.looigi.wallpaperchanger2.classeOrari;

import android.content.Context;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeOrari.adapters.AdapterListenerMezzi;
import com.looigi.wallpaperchanger2.classeOrari.adapters.AdapterListenerPortate;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezziStandard;
import com.looigi.wallpaperchanger2.classePassword.AdapterListenerPassword;
import com.looigi.wallpaperchanger2.classePassword.VariabiliStatichePWD;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public boolean ControllaFormatodata(Context context, String ValoreImpostato) {
        if (ValoreImpostato.isEmpty()) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Immetter un valore");
            return false;
        } else {
            if (!ValoreImpostato.contains(":")) {
                UtilitiesGlobali.getInstance().ApreToast(context, "Valore non valido");
                return false;
            } else {
                String[] s = ValoreImpostato.split(":");
                if (s.length < 3) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Valore non valido");
                    return false;
                } else {
                    boolean ok = true;
                    for (String ss : s) {
                        if (!UtilityOrari.getInstance().isNumeric(ss)) {
                            ok = false;
                        }
                    }
                    if (!ok) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "Valore non valido");
                        return false;
                    } else {
                        int ore = Integer.parseInt(s[0]);
                        int minuti = Integer.parseInt(s[1]);
                        int secondi = Integer.parseInt(s[2]);

                        if (ore < 0 || ore > 23) {
                            UtilitiesGlobali.getInstance().ApreToast(context, "Ore non valide (0-23)");
                            return false;
                        } else {
                            if (minuti < 0 || minuti > 59) {
                                UtilitiesGlobali.getInstance().ApreToast(context, "Minuti non validi");
                                return false;
                            } else {
                                if (secondi < 0 || secondi > 59) {
                                    UtilitiesGlobali.getInstance().ApreToast(context, "Secondi non validi");
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
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
            boolean giornoDiLavoro = false;

            if (sdg.getQuanteOre() == oreStandard) {
                Lavoro = "Lavoro in sede / Cliente";
                giornoDiLavoro = true;
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
            if (sdg.getGradi().equals("999")) {
                sdg.setGradi("");
            }
            VariabiliStaticheOrari.getInstance().getEdtGradi().setText(sdg.getGradi());
            VariabiliStaticheOrari.getInstance().getTxtPasticca().setText(sdg.getPasticca().get(0).getPasticca());
            VariabiliStaticheOrari.getInstance().getEdtNote().setText(sdg.getNote().toString());

            AdapterListenerPortate cstmAdptPranzo = new AdapterListenerPortate(context,
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getPranzo());
            VariabiliStaticheOrari.getInstance().getLstPranzo().setAdapter(cstmAdptPranzo);

            if (giornoDiLavoro && (VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziAndata() == null ||
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziAndata().size() == 0)) {
                List<StrutturaMezzi> listaAndata = new ArrayList<>();
                for (StrutturaMezziStandard ss : VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziStandardAndata()) {
                    for (StrutturaMezzi m : VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()) {
                        if (m.getIdMezzo() == ss.getIdMezzo()) {
                            listaAndata.add(m);
                        }
                    }
                }
                VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziAndata(
                        listaAndata
                );

                List<StrutturaMezzi> listaRitorno = new ArrayList<>();
                for (StrutturaMezziStandard ss : VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziStandardRitorno()) {
                    for (StrutturaMezzi m : VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()) {
                        if (m.getIdMezzo() == ss.getIdMezzo()) {
                            listaRitorno.add(m);
                        }
                    }
                }
                VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziRitorno(
                        listaRitorno
                );
            }
            AdapterListenerMezzi cstmAdptMezziAndata = new AdapterListenerMezzi(context,
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziAndata());
            VariabiliStaticheOrari.getInstance().getLstMezziAndata().setAdapter(cstmAdptMezziAndata);

            AdapterListenerMezzi cstmAdptMezziRitorno = new AdapterListenerMezzi(context,
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziRitorno());
            VariabiliStaticheOrari.getInstance().getLstMezziRitorno().setAdapter(cstmAdptMezziRitorno);
        }
    }
}
