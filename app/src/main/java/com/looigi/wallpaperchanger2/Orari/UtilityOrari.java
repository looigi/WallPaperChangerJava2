package com.looigi.wallpaperchanger2.Orari;

import android.content.Context;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.Orari.adapters.AdapterListenerMezzi;
import com.looigi.wallpaperchanger2.Orari.adapters.AdapterListenerPortate;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaDatiGiornata;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaMezziStandard;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaTempo;
import com.looigi.wallpaperchanger2.Orari.webService.ChiamateWSOrari;
import com.looigi.wallpaperchanger2.Orari.webService.DownloadImmagineOrari;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

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

    public String RitornaPasqua(int Anno) {
        int a = Anno % 19,
        b = Anno / 100,
        c = Anno % 100,
        d = b / 4,
        e = b % 4,
        g = (8 * b + 13) / 25,
        h = (19 * a + b - d - g + 15) % 30,
        j = c / 4,
        k = c % 4,
        m = (a + 11 * h) / 319,
        r = (2 * e + 2 * j - k - h + m + 32) % 7,
        month = (h - m + r + 90) / 25,
        day = (h - m + r + month + 19) % 32;

        return day + ";" + month;
    }

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

        if (sdg == null) {
            VariabiliStaticheOrari.getInstance().getLayContenitore().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getLayAggiunge().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheOrari.getInstance().getLayNote().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getLayDettaglioGiornata().setVisibility(LinearLayout.GONE);
            return;
        }

        if (sdg.isSoloNote()) {
            VariabiliStaticheOrari.getInstance().getLayContenitore().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getLayAggiunge().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getLayNote().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheOrari.getInstance().getLayDettaglioGiornata().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getDatiGiornata().setQuanteOre(-9999);
            VariabiliStaticheOrari.getInstance().getEdtNote().setText(sdg.getNote());
            return;
        }

        if (!sdg.isGiornoInserito()) {
            VariabiliStaticheOrari.getInstance().getLayContenitore().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getLayAggiunge().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheOrari.getInstance().getLayDettaglioGiornata().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getLayNote().setVisibility(LinearLayout.GONE);
        } else {
            VariabiliStaticheOrari.getInstance().getLayContenitore().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheOrari.getInstance().getLayAggiunge().setVisibility(LinearLayout.GONE);
            VariabiliStaticheOrari.getInstance().getLayNote().setVisibility(LinearLayout.VISIBLE);

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
                VariabiliStaticheOrari.getInstance().getLayNote().setVisibility(LinearLayout.VISIBLE);
            } else {
                VariabiliStaticheOrari.getInstance().getEdtOreLavoro().setText(Integer.toString(oreStandard));
                VariabiliStaticheOrari.getInstance().getLayDettaglioGiornata().setVisibility(LinearLayout.GONE);
                VariabiliStaticheOrari.getInstance().getLayNote().setVisibility(LinearLayout.VISIBLE);
            }
            VariabiliStaticheOrari.getInstance().getEdtEntrata().setText(sdg.getEntrata());
            VariabiliStaticheOrari.getInstance().getTxtLavoro().setText(sdg.getLavoro());
            VariabiliStaticheOrari.getInstance().getTxtCommessa().setText(sdg.getCommessa());
            VariabiliStaticheOrari.getInstance().getTxtTempo().setText(sdg.getTempo());

            if (sdg.getTempo() != null && !sdg.getTempo().isEmpty()) {
                disegnaIconaTempo(context, sdg.getTempo());
            } else {
                VariabiliStaticheOrari.getInstance().getImgIconaTempo().setVisibility(LinearLayout.GONE);
            }

            if (sdg.getGradi().equals("999")) {
                sdg.setGradi("");
            }
            VariabiliStaticheOrari.getInstance().getEdtGradi().setText(sdg.getGradi());

            // PASTICCA
            if (!giornoDiLavoro) {
                VariabiliStaticheOrari.getInstance().getDatiGiornata().setPasticca(new ArrayList<>());
            }
            if (!sdg.getPasticca().isEmpty()) {
                VariabiliStaticheOrari.getInstance().getTxtPasticca().setText(sdg.getPasticca().get(0).getPasticca());
            } else {
                VariabiliStaticheOrari.getInstance().getTxtPasticca().setText("");
            }

            // NOTE
            VariabiliStaticheOrari.getInstance().getEdtNote().setText(sdg.getNote().toString());

            // PORTATE
            if (!giornoDiLavoro) {
                VariabiliStaticheOrari.getInstance().getDatiGiornata().setPranzo(new ArrayList<>());
            }
            AggiornaListaPortate(context,false);

            // Controllo se esistono i dati standard
            if (giornoDiLavoro && (VariabiliStaticheOrari.getInstance().getStrutturaDati() == null ||
                    VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi() == null)) {
                ChiamateWSOrari ws = new ChiamateWSOrari(context);
                ws.RitornaDatiPerModifica(false, true);
            } else {
                // MEZZI
                RiempieMezzi(context);
            }
        }
    }

    public void disegnaIconaTempo(Context context, String Tempo) {
        boolean ok = false;

        if (VariabiliStaticheOrari.getInstance().getStrutturaDati() != null &&
                VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi() != null) {
            for (StrutturaTempo st : VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi()) {
                if (Tempo.equals(st.getTempo())) {
                    if (!st.getUrlIcona().isEmpty()) {
                        ok = true;

                        VariabiliStaticheOrari.getInstance().getImgIconaTempo().setVisibility(LinearLayout.VISIBLE);

                        DownloadImmagineOrari d = new DownloadImmagineOrari();
                        d.EsegueChiamata(context,
                                VariabiliStaticheOrari.getInstance().getImgIconaTempo(),
                                "http:" + st.getUrlIcona());
                    }
                }
            }
        }
        if (!ok) {
            VariabiliStaticheOrari.getInstance().getImgIconaTempo().setVisibility(LinearLayout.GONE);
        }
    }

    public void RiempieMezzi(Context context) {
        int oreStandard = VariabiliStaticheOrari.getInstance().getDatiGiornata().getOreStandard();

        if (VariabiliStaticheOrari.getInstance().getDatiGiornata().getQuanteOre() == oreStandard) {
            if (VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziAndata() == null ||
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziAndata().isEmpty()) {
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
            }

            if (VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziRitorno() == null ||
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziRitorno().isEmpty()) {
                List<StrutturaMezzi> listaRitorno = new ArrayList<>();
                for (StrutturaMezziStandard ss : VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziStandardRitorno()) {
                    if (VariabiliStaticheOrari.getInstance().getStrutturaDati() != null &&
                            VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi() != null) {
                        for (StrutturaMezzi m : VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()) {
                            if (m.getIdMezzo() == ss.getIdMezzo()) {
                                listaRitorno.add(m);
                            }
                        }
                    }
                }
                VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziRitorno(
                        listaRitorno
                );
            }

            AggiornaListaMezziAndata(context, false, true);
            AggiornaListaMezziRitorno(context, false, false);
        } else {
            VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziAndata(
                    new ArrayList<>()
            );
            VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziRitorno(
                    new ArrayList<>()
            );
        }
    }

    public void AggiornaListaPortate(Context context, boolean NuovoValore) {
        AdapterListenerPortate cstmAdptPranzo = new AdapterListenerPortate(context,
                VariabiliStaticheOrari.getInstance().getDatiGiornata().getPranzo(),
                NuovoValore);
        VariabiliStaticheOrari.getInstance().getLstPranzo().setAdapter(cstmAdptPranzo);
    }

    public void AggiornaListaMezziAndata(Context context, boolean NuovoValore, boolean Andata) {
        AdapterListenerMezzi cstmAdptMezziAndata = new AdapterListenerMezzi(context,
                VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziAndata(),
                NuovoValore,
                Andata,
                "");
        VariabiliStaticheOrari.getInstance().getLstMezziAndata().setAdapter(cstmAdptMezziAndata);
    }

    public void AggiornaListaMezziRitorno(Context context, boolean NuovoValore, boolean Andata) {
        AdapterListenerMezzi cstmAdptMezziRitorno = new AdapterListenerMezzi(context,
                VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziRitorno(),
                NuovoValore,
                Andata,
                "");
        VariabiliStaticheOrari.getInstance().getLstMezziRitorno().setAdapter(cstmAdptMezziRitorno);
    }
}
