package com.looigi.wallpaperchanger2.classePlayer.WebServices;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerAlbum;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerArtisti;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerBraniOnline;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerRicerca;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaRicerca;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaTags;
import com.looigi.wallpaperchanger2.classePlayer.preferiti_tags.AdapterListenerPreferiti;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaAlbum;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaFiltroBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaArtisti;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaUtenti;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classePlayer.preferiti_tags.AdapterListenerTags;
import com.looigi.wallpaperchanger2.classePlayer.preferiti_tags.VariabiliStatichePrefTags;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.MainScaricaImmagini;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChiamateWsPlayer implements TaskDelegatePlayer {
    private static final String NomeMaschera = "Chiamate_WS_Player";
    // private LetturaWSAsincronaPlayer bckAsyncTask;

    private final String RadiceWS = VariabiliStatichePlayer.UrlWS + "/";
    private final String ws = "wsMobile.asmx/";
    private final String ws2 = "wsLWP.asmx/";
    private final String NS="http://wsMobile2.org/";
    private final String SA="http://wsMobile2.org/";
    private final String NS2="http://csaricanuovai.org/";
    private final String SA2="http://csaricanuovai.org/";
    private String TipoOperazione = "";
    private Context context;
    private boolean Pregresso = false;
    private String Brano = "";
    private boolean Riprova = false;
    private String Artista;
    private String Immagine;
    private boolean PerActivityPrefTags = false;
    // private StrutturaChiamateWSPlayer chiamataDaFare;

    public ChiamateWsPlayer(Context context, boolean Riprova) {
        this.context = context;
        this.Riprova = Riprova;
    }

    public void SalvaTagsBrano(String idBrano, String idTags) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Salva Tags brano " + idBrano + ": " + idTags);

        String Urletto="SalvaTagsABrano?" +
                "idBrano=" + idBrano +
                "&idTags=" + idTags;

        TipoOperazione = "Salva TAGS Brano";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                5000,
                true,
                true,
                false,
                -1);
    }

    public void EliminaTag(String idTag) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Elimina Tag: " + idTag);

        String Urletto="EliminaTag?" +
                "idTag=" + idTag;

        TipoOperazione = "Elimina TAG";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                5000,
                true,
                true,
                false,
                -1);
    }

    public void ModificaTag(String idTag, String Tag) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Modifica Tag: " + Tag);

        String Urletto="ModificaTag?" +
                "idTag=" + idTag +
                "&Tag=" + Tag;

        TipoOperazione = "Modifica TAG";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                5000,
                true,
                true,
                false,
                -1);
    }

    public void SalvaTag(String Tag) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Salva Tag: " + Tag);

        String Urletto="SalvaTag?" +
                "Tag=" + Tag;

        TipoOperazione = "Salva TAG";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                5000,
                true,
                true,
                false,
                -1);
    }

    public void SalvaImmagineArtista(String Artista, String Immagine, String Base64) {
        UtilityPlayer.getInstance().AttesaSI(true);

        this.Artista = Artista;
        this.Immagine = Immagine;

        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Salva Immagine Artista " + Artista + ": " + Immagine);

        if (Immagine.contains("/")) {
            String[] i = Immagine.split("/");
            Immagine = i[i.length -1];
        }

        String Urletto="SalvaImmagineArtista?" +
                "Artista=" + Artista +
                "&Immagine=" + Immagine +
                "&Base64=" + Base64;

        TipoOperazione = "Salva Immagine Artista";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                600000,
                true,
                true,
                false,
                -1);
    }

    public void ScaricaImmaginiArtista(String Artista) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Scarica Immagini Artista " + Artista);

        this.Artista = Artista;

        String Urletto="ScaricaListaImmagini?" +
                "Artista=" + Artista;

        TipoOperazione = "Scarica Lista Immagini";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                160000,
                true,
                true,
                false,
                -1);
    }

    public void RicercaBrano(String Brano) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Ricerca brano " + Brano);

        String Urletto="RicercaBrano?" +
                "Filtro=" + Brano;

        TipoOperazione = "Ricerca Brano";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                true,
                true,
                false,
                -1);
    }

    public void EliminaImmagine(String Artista, String Album, String Immagine) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Elimina Immagine Artista " + Artista + ": " + Immagine);

        String Urletto="EliminaImmagine?" +
                "Artista=" + Artista +
                "&Album=" + Album +
                "&Immagine=" + Immagine;

        TipoOperazione = "Elimina Immagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                10000,
                true,
                true,
                false,
                -1);
    }

    public void ModificaImmagine(StrutturaImmagini s, String stringaBase64, boolean Sovrascrive) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(true);

        String Urletto="ModificaImmagine?" +
                "Artista=" + s.getArtista() +
                "&Album=" + s.getAlbum() +
                "&Cartella=" + s.getCartellaImmagine() +
                "&Immagine=" + s.getNomeImmagine() +
                "&StringaBase64=" + stringaBase64;

        TipoOperazione = "Modifica Immagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                10000,
                true,
                true,
                false,
                -1);
    }

    public void RitornaImmaginiArtista(String Artista) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorna Immagini Artista");

        String Urletto="RitornaImmaginiArtista?Artista=" + Artista;

        TipoOperazione = "Ritorna Immagini Artista";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                10000,
                true,
                true,
                false,
                -1);
    }

    public void RicercaBrani(String Ricerca) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ricerca Brani");

        String Urletto="RitornaBrani?Ricerca=" + Ricerca;

        TipoOperazione = "Ritorna Brani";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                true,
                true,
                false,
                -1);
    }

    public void RicaricaBraniHard() {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ricarica brani2. Creazione JSON");

        String Urletto="RefreshCanzoniHard?idUtente=" + VariabiliStatichePlayer.getInstance().getUtente().getId();

        TipoOperazione = "Refresh Canzoni Hard";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws2 + Urletto,
                TipoOperazione,
                NS2,
                SA2,
                500000,
                true,
                true,
                false,
                -1);
    }

    public void RitornaStelleBrano() {
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() == null) {
            VariabiliStatichePlayer.getInstance().setClasseChiamata(null);
            return;
        }

        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorna Stelle Brano " + VariabiliStatichePlayer.getInstance().getUltimoBrano().getIdBrano());

        String Urletto="RitornaStelleBranoDaBrano?" +
                "idUtente=" + VariabiliStatichePlayer.getInstance().getUtente().getId() +
                "&Artista=" + VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista() +
                "&Album=" + VariabiliStatichePlayer.getInstance().getUltimoBrano().getAlbum() +
                "&Brano=" + VariabiliStatichePlayer.getInstance().getUltimoBrano().getBrano();

        TipoOperazione = "Ritorna Stelle Brano";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                false,
                true,
                false,
                -1);

    }

    public void RitornaListaAlbum(String Artista, String Ricerca) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorna lista Album");

        Artista = UtilityPlayer.getInstance().ConverteNome(Artista);

        String Urletto="RitornaListaAlbumArtista?Artista=" + Artista + "&Ricerca=" + Ricerca;

        TipoOperazione = "Ritorna Lista Album Artista";
        // ControllaTempoEsecuzione = true;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                false,
                true,
                false,
                -1);
    }

    public void RitornaListaBrani(String Artista, String Album, String Ricerca) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorna lista brani per Artista " + Artista + " e album " + Album);

        Artista = UtilityPlayer.getInstance().ConverteNome(Artista);
        Album = UtilityPlayer.getInstance().ConverteNome(Album);

        String Urletto="RitornaListaBraniAlbumArtista?Artista=" + Artista + "&Album=" + Album + "&Ricerca=" + Ricerca;

        TipoOperazione = "Ritorna Lista Brani Album Artista";
        // ControllaTempoEsecuzione = true;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                false,
                true,
                false,
                -1);
    }

    public void RitornaListaTags(boolean EsegueRefresh) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorna lista Tags");

        if (!EsegueRefresh) {
            db_dati_player db = new db_dati_player(context);
            List<StrutturaTags> lista = db.RitornaTags();
            db.ChiudeDB();

            if (lista.isEmpty()) {
                EsegueRefresh = true;
            } else {
                VariabiliStatichePlayer.getInstance().setListaTags(lista);

                AdapterListenerTags customAdapterA = new AdapterListenerTags(context,
                        lista);
                VariabiliStatichePrefTags.getInstance().getLstArtisti().setAdapter(customAdapterA);
                VariabiliStatichePrefTags.getInstance().setCustomAdapterTag(customAdapterA);

                return;
            }
        }

        String Urletto = "RitornaListaTags";

        TipoOperazione = "Ritorna Lista Tags";
        // ControllaTempoEsecuzione = true;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                false,
                true,
                false,
                -1);
    }

    public void RitornaListaArtisti(boolean EsegueRefresh, boolean PerActivityPrefTags) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorna lista Artisti");

        this.PerActivityPrefTags = PerActivityPrefTags;

        boolean esegueQuery = true;

        if (!EsegueRefresh || PerActivityPrefTags) {
            db_dati_player db = new db_dati_player(context);
            List<StrutturaArtisti> lista = db.CaricaArtisti();
            db.ChiudeDB();

            if (!lista.isEmpty()) {
                if (!PerActivityPrefTags) {
                    AdapterListenerArtisti customAdapterA = new AdapterListenerArtisti(context, lista);
                    VariabiliStatichePlayer.getInstance().getLstArtisti().setAdapter(customAdapterA);
                    VariabiliStatichePlayer.getInstance().setCustomAdapterA(customAdapterA);
                } else {
                    VariabiliStatichePlayer.getInstance().setListaArtisti(lista);
                }

                esegueQuery = false;

                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                        "Ritorno artisti effettuato da DB");
            }
        }

        if (esegueQuery) {
            String Urletto = "RitornaArtisti";

            TipoOperazione = "Ritorna Artisti";
            // ControllaTempoEsecuzione = true;

            Esegue(
                    RadiceWS + ws + Urletto,
                    TipoOperazione,
                    NS,
                    SA,
                    150000,
                    true,
                    true,
                    false,
                    -1);
        }
    }

    private long lastCall = -1;

    public void RitornaBranoDaID(String idBrano, boolean Pregresso) {
        if (VariabiliStatichePlayer.getInstance().getUtente() == null) {
            StrutturaUtenti u = new StrutturaUtenti();
            u.setId(1);
            u.setAmministratore(true);
            u.setUtente("Looigi");
            u.setPassword("looigi227");

            VariabiliStatichePlayer.getInstance().setUtente(u);
        }

        this.Pregresso = Pregresso;

        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Caricamento prossimo brano. Pregresso: " + Pregresso);

        Brano = idBrano;

        long ora = new Date().getTime();
        if (ora - lastCall < 2000) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Caricamento prossimo brano. Esco per troppo veloce: " + (ora - lastCall));

            return;
        }
        lastCall = ora;

        StrutturaFiltroBrano s = UtilityPlayer.getInstance().CreaDatiFiltrobrani();

        String Urletto="RitornaProssimoBranoMobile?";
        Urletto += "idUtente=" + VariabiliStatichePlayer.getInstance().getUtente().getId();
        Urletto += "&Random=" + (VariabiliStatichePlayer.getInstance().isRandom() ? "S" : "N");
        Urletto += "&idBranoAttuale=" + s.getIdUltimoBrano();
        Urletto += "&Stelle=" + s.getStelle();
        Urletto += "&RicercaTesto=" + s.getTesto();
        Urletto += "&Tags=" + s.getTags();
        Urletto += "&Preferiti=" + s.getPreferiti();
        Urletto += "&TagsElimina=" + s.getTagsElimina();
        Urletto += "&PreferitiElimina=" + s.getPreferitiElimina();
        Urletto += "&BranoEsatto=" + idBrano;
        Urletto += "&DataSuperiore=" + s.getDataSuperiore();
        Urletto += "&DataInferiore=" + s.getDataInferiore();
        Urletto += "&StelleSuperiori=" + s.getStelleSuperiori();
        Urletto += "&AndOrTags=" + s.getAndOrTags();
        Urletto += "&AndOrPref=" + s.getAndOrPref();
        Urletto += "&RicercaNonTesto=" + s.getTestoNon();

        // ControllaTempoEsecuzione = true;
        TipoOperazione = "Ritorna Prossimo Brano Mobile";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                false,
                true,
                false,
                -1);
    }

    public void StoppaEsecuzione() {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Blocco elaborazione per cambio brano");
        VariabiliStatichePlayer.getInstance().setClasseChiamata(null);
        UtilityPlayer.getInstance().Attesa(false);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");

        if (VariabiliStatichePlayer.getInstance().getClasseInterrogazione() != null) {
            VariabiliStatichePlayer.getInstance().getClasseInterrogazione().BloccaEsecuzione();
        }
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog, boolean ChiamataDiretta,
                       boolean ControllaTempoEsecuzione, int Stelle) {

        UtilityPlayer.getInstance().Attesa(true);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso(tOperazione + (Pregresso ? "Pregresso" : ""));

        /* chiamataDaFare = new StrutturaChiamateWSPlayer();
        chiamataDaFare.setBrano(Brano);
        chiamataDaFare.setNS(NS);
        chiamataDaFare.setTimeout(Timeout);
        chiamataDaFare.setSOAP_ACTION(SOAP_ACTION);
        chiamataDaFare.settOperazione(tOperazione);
        chiamataDaFare.setApriDialog(ApriDialog);
        chiamataDaFare.setUrletto(Urletto);
        chiamataDaFare.setPregresso(Pregresso); */

        if (UtilitiesGlobali.getInstance().isRetePresente()) {
            InterrogazioneWSPlayer i = new InterrogazioneWSPlayer();
            VariabiliStatichePlayer.getInstance().setClasseInterrogazione(i);
            i.EsegueChiamata(
                    context,
                    NS,
                    Timeout,
                    SOAP_ACTION,
                    tOperazione,
                    ApriDialog,
                    Urletto,
                    "0", // TimeStampAttuale,
                    this,
                    Pregresso
            );
            /* bckAsyncTask = new LetturaWSAsincronaPlayer(
                    context,
                    NS,
                    Timeout,
                    SOAP_ACTION,
                    tOperazione,
                    ApriDialog,
                    Urletto,
                    "0", // TimeStampAttuale,
                    this,
                    Pregresso);
            bckAsyncTask.execute(Urletto); */
        // } else {
        //     aggiungeOperazione();
        } else {
            VariabiliStatichePlayer.getInstance().setClasseInterrogazione(null);
            VariabiliStatichePlayer.getInstance().setClasseChiamata(null);
            UtilityPlayer.getInstance().Attesa(false);
            UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
        }
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                VariabiliStatichePlayer.getInstance().setClasseInterrogazione(null);
                VariabiliStatichePlayer.getInstance().setClasseChiamata(null);
                UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
                UtilityPlayer.getInstance().Attesa(false);
               /* boolean Ok = true;

                if (ControllaTempoEsecuzione) {
                    if (differenza > 30) {
                        // Ci ha messo troppo tempo
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". Troppo tempo a rispondere: " + differenza);

                        UtilityPlayer.getInstance().ImpostaStatoReteOFF();
                        accesoStatoReteMancante = true;
                    } else {
                        if (accesoStatoReteMancante) {
                            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". Ripristino stato rete");
                            accesoStatoReteMancante = false;
                            UtilityPlayer.getInstance().ImpostaStatoReteON();
                        }
                    }
                } */

                        // boolean Scoda = true;

                        // if (Ok) {
                switch (TipoOperazione) {
                    case "Elimina Immagine":
                        fEliminaImmagine(result);
                        break;
                    case "Ritorna Stelle Brano":
                        fRitornaStelleBrano(result);
                        break;
                    case "Ritorna Artisti":
                        fRitornaArtisti(result);
                        break;
                    case "Ritorna Lista Brani Album Artista":
                        RitornaListaBraniAlbumArtista(result);
                        break;
                    case "Ritorna Lista Album Artista":
                        RitornaListaAlbumArtista(result);
                        break;
                    case "Ritorna Lista Tags":
                        fRitornaListaTags(result);
                        break;
                    case "Ritorna Immagini Artista":
                        fRitornaImmaginiArtista(result);
                        break;
                    case "Ritorna Prossimo Brano Mobile":
                        CaricaBrano(result);
                        break;
                    case "Scarica Testo":
                        fAggiornaTesto(result);
                        break;
                    case "Ritorna Brani":
                        fRitornaBrani(result);
                        break;
                    case "Refresh Canzoni Hard":
                        fRefreshCanzoni(result);
                        break;
                    case "Modifica Immagine":
                        fModificaImmagine(result);
                        break;
                    case "Salva Immagine Artista":
                        fSalvaImmagineArtista(result);
                        break;
                    case "Scarica Lista Immagini":
                        fScaricaListaImmagini(result);
                        break;
                    case "Ricerca Brano":
                        fRicercaBrano(result);
                        break;
                    case "Salva TAG":
                        fSalvaTag(result);
                        break;
                    case "Modifica TAG":
                        fModificaTag(result);
                        break;
                    case "Elimina TAG":
                        fEliminaTag(result);
                        break;
                    case "Salva TAGS Brano":
                        fSalvaTagsBrano(result);
                        break;
                }

                if (VariabiliStatichePlayer.getInstance().getLayCaricamentoSI() != null) {
                    VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.GONE);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    private void fRefreshCanzoni(String result) {
        UtilityPlayer.getInstance().AttesaSI(false);

        boolean ritorno = ControllaRitorno("Refresh brani", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Brani aggiornati");
        }
    }

    private void fSalvaTagsBrano(String result) {
        UtilityPlayer.getInstance().AttesaSI(false);

        boolean ritorno = ControllaRitorno("Salva Tags Brano", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Tags brano salvati");
        }
    }

    private void fEliminaTag(String result) {
        UtilityPlayer.getInstance().AttesaSI(false);

        boolean ritorno = ControllaRitorno("Elimina Tag", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                    ws.RitornaListaTags(true);

                    UtilitiesGlobali.getInstance().ApreToast(context, "Tag eliminato");
                }
            }, 100);
        }
    }

    private void fModificaTag(String result) {
        UtilityPlayer.getInstance().AttesaSI(false);

        boolean ritorno = ControllaRitorno("Modifica Tag", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            VariabiliStatichePrefTags.getInstance().getLayTag().setVisibility(LinearLayout.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                    ws.RitornaListaTags(true);

                    UtilitiesGlobali.getInstance().ApreToast(context, "Tag modificato");
                }
            }, 100);
        }
    }

    private void fSalvaTag(String result) {
        UtilityPlayer.getInstance().AttesaSI(false);

        boolean ritorno = ControllaRitorno("Salva Tag", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            VariabiliStatichePrefTags.getInstance().getLayTag().setVisibility(LinearLayout.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                    ws.RitornaListaTags(true);

                    UtilitiesGlobali.getInstance().ApreToast(context, "Tag salvato");
                }
            }, 100);
        }
    }

    private void fRicercaBrano(String result) {
        UtilityPlayer.getInstance().AttesaSI(false);

        boolean ritorno = ControllaRitorno("Ricerca Brano", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaRicerca> lista = new ArrayList<>();
            String[] trovati = result.split("§");
            for (String s : trovati) {
                if (!s.isEmpty() && s.contains(";")) {
                    String[] c = s.split(";");

                    StrutturaRicerca sr = new StrutturaRicerca();
                    sr.setId(Integer.parseInt(c[0]));
                    sr.setArtista(c[1]);
                    sr.setAlbum(c[2]);
                    sr.setBrano(c[3]);
                    sr.setBellezza(c[4]);
                    sr.setTraccia(c[5]);

                    lista.add(sr);
                }
            }

            AdapterListenerRicerca customAdapterA = new AdapterListenerRicerca(context,
                    lista);
            VariabiliStatichePlayer.getInstance().getLstRicerca().setAdapter(customAdapterA);
        }
    }

    private void fSalvaImmagineArtista(String result) {
        UtilityPlayer.getInstance().AttesaSI(false);

        boolean ritorno = ControllaRitorno("Salva Immagine Artista", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            String NomeFileAppoggio = context.getFilesDir() + "/Appoggio/ImmScaricata.jpg";
            String NomeInterno = context.getFilesDir() + "/Player/ImmaginiMusica/" + Artista +
                    "/ZZZ-ImmaginiArtista/" + Immagine;

            try {
                Files.getInstance().CopiaFile(NomeFileAppoggio, NomeInterno);

                StrutturaImmagini s = new StrutturaImmagini();
                s.setPathImmagine(context.getFilesDir() + "/Player/ImmaginiMusica/" + Artista +
                                "/ZZZ-ImmaginiArtista");
                s.setArtista(Artista);
                s.setNomeImmagine(Immagine);
                s.setAlbum("");
                s.setUrlImmagine("");

                db_dati_player db = new db_dati_player(context);
                db.ScriveImmagineBrano(Artista, s);

                VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().add(s);
            } catch (IOException e) {
            }

            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine salvata");
        }
    }

    private void fScaricaListaImmagini(String result) {
        boolean ritorno = ControllaRitorno("Scarica lista immagini artista", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (!result.isEmpty()) {
                if (!VariabiliScaricaImmagini.getInstance().isMascheraAttiva()) {
                    String[] urls = result.split("§");
                    List<String> urlDaScaricare = new ArrayList<>();
                    for (String url : urls) {
                        String link = url.replace("*CS*", "§");
                        urlDaScaricare.add(link);
                    }
                    VariabiliStatichePlayer.getInstance().setUrlImmaginiDaScaricare(urlDaScaricare);

                    VariabiliScaricaImmagini.getInstance().setListaOriginaleDaScaricare(new ArrayList<>());

                    Intent si = new Intent(context, MainScaricaImmagini.class);
                    si.addCategory(Intent.CATEGORY_LAUNCHER);
                    si.setAction(Intent.ACTION_MAIN);
                    si.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    si.putExtra("MODALITA", "PLAYER");
                    si.putExtra("FILTRO", Artista);
                    context.startActivity(si);
                }
            }
        }
    }
    
    private void fEliminaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Elimina Immagine", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            int n = VariabiliStatichePlayer.getInstance().getIdImmagineImpostata();
            if (n + 1 <= VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1) {
                n++;
            } else {
                n = 0;
            }
            VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(n);

            UtilityPlayer.getInstance().ImpostaImmagine(context, n);

            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine eliminata");
        }
    }

    private void fAggiornaTesto(String result) {
        boolean ritorno = ControllaRitorno("Aggiorna testo", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
        } else {
            String[] t = result.split("\\|", -1);
            String testo = t[0];
            testo = testo.replace("§", "\n");
            testo = testo.replace("%20", " ");
            String testo2 = testo.substring(0, 30);

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Aggiornamento testo brano eseguito: " + testo2);

            // Files.getInstance().ScriveFile(VariabiliGlobali.getInstance().getPathTesto(), VariabiliGlobali.getInstance().getNomeFileTesto(), testo);

            VariabiliStatichePlayer.getInstance().getUltimoBrano().setTesto(testo);

            // db_dati db = new db_dati();
            // db.aggiornaTestoBrano(VariabiliGlobali.getInstance().getIdTesto(), testo);
        }
    }

    private void fModificaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Modifica immagine artista", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine modificata");
        }
    }

    private void fRitornaImmaginiArtista(String result) {
        boolean ritorno = ControllaRitorno("Ritorna immagini artista", result);
        if (!ritorno) {
            // UtilityPlayer.getInstance().VisualizzaMessaggio(result);
        } else {
            db_dati_player db = new db_dati_player(context);
            // Sabaton;ZZZ-ImmaginiArtista;070629_Peace_And_Love_2007-480.jpg§Sabaton;ZZZ-ImmaginiArtista;11564-12192012123241.jpg
            String sArtista = "";
            if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
                sArtista = VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista();

                List<StrutturaImmagini> ListaImmagini = db.CaricaImmaginiBrano(sArtista);
                String[] lista = result.split("§");
                int quante = 0;
                for (String l : lista) {
                    String[] Campi = l.split(";");
                    String Artista = Campi[0];
                    String Album = Campi[1];
                    String Immagine = Campi[2];

                    boolean ok = true;

                    for (StrutturaImmagini li : ListaImmagini) {
                        if (li.getArtista().equals(Artista) &&
                                li.getAlbum().equals(Album) &&
                                li.getNomeImmagine().equals(Immagine)) {
                            ok = false;
                            break;
                        }
                    }

                    if (ok) {
                        StrutturaImmagini Imm = new StrutturaImmagini();
                        Imm.setArtista(Artista);
                        Imm.setAlbum(Album);
                        Imm.setNomeImmagine(Immagine);

                        String UrlImmagine = VariabiliStatichePlayer.PercorsoBranoMP3SuURL +
                                "/ImmaginiMusica/" + Artista + "/" + Album + "/" +
                                Immagine;
                        Imm.setUrlImmagine(UrlImmagine);

                        String PathImmagine = context.getFilesDir() + "/Player/" +
                                "/ImmaginiMusica/" + Artista + "/" + Album + "/" +
                                Immagine;
                        Imm.setPathImmagine(PathImmagine);

                        String CartellaImmagine = context.getFilesDir() + "/Player/" +
                                "/ImmaginiMusica/" + Artista + "/" + Album;
                        Imm.setCartellaImmagine(CartellaImmagine);

                        ListaImmagini.add(Imm);

                        db.ScriveImmagineBrano(Artista, Imm);

                        quante++;
                    }
                }

                VariabiliStatichePlayer.getInstance().getUltimoBrano().setImmagini(ListaImmagini);

                if (VariabiliStatichePlayer.getInstance().getIdImmagineImpostata() == -1 ||
                        VariabiliStatichePlayer.getInstance().getIdImmagineImpostata() == 0) {
                    UtilityPlayer.getInstance().ImpostaImmagine(context, -1);
                }

                UtilitiesGlobali.getInstance().ApreToast(context, "Immagini aggiunte: " + quante);
            }

            db.ChiudeDB();
        }
    }

    private void fRitornaBrani(String result) {
        boolean ritorno = ControllaRitorno("Ritorna brani", result);
        if (!ritorno) {
            // UtilityPlayer.getInstance().VisualizzaMessaggio(result);
        } else {
            String[] lista = result.split("§", -1);
            for (String l : lista) {
                if (!l.isEmpty()) {
                    String[] Campi = l.split(";", -1);

                    try {
                        StrutturaBrano s = new StrutturaBrano();
                        s.setIdBrano(Integer.parseInt(Campi[0]));
                        s.setAnno(Campi[1]);
                        s.setArtista(Campi[2]);
                        s.setAlbum(Campi[3]);
                        s.setBrano(Campi[4]);
                        s.setTraccia(Campi[5]);
                        if (Campi[6].isEmpty()) {
                            s.setAscoltata(0);
                        } else {
                            s.setAscoltata(Integer.parseInt(Campi[6]));
                        }
                        if (Campi[7].isEmpty()) {
                            s.setBellezza(0);
                        } else {
                            s.setBellezza(Integer.parseInt(Campi[7]));
                        }
                        s.setData(Campi[8]);
                        if (Campi[9].isEmpty()) {
                            s.setDimensione(0L);
                        } else {
                            s.setDimensione(Long.parseLong(Campi[9]));
                        }
                        s.setEstensione(Campi[10]);
                        try {
                            s.setTags(Campi[11]);
                        } catch (Exception ignored) {
                            s.setTags("");
                        }
                        s.setImmagini(new ArrayList<>());
                        s.setTipoBrano(0);

                        // VariabiliStatichePlayer.getInstance().getListaBraniRicercati().add(s);
                    } catch (Exception e) {
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Errore ritorno RitornaBrani: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));
                    }
                }
            }

        }
        // UtilityPlayer.getInstance().RiempieListaRicerche();
    }

    private void fRitornaStelleBrano(String result) {
        int Stelle;

        boolean ritorno = ControllaRitorno("Ritorna stelle brano", result);
        if (!ritorno) {
            Stelle = -1;
        } else {
            Stelle = Integer.parseInt(result);

            db_dati_player db = new db_dati_player(context);
            db.ScriveStelleBrano(String.valueOf(Stelle));
            db.ChiudeDB();
        }

        VariabiliStatichePlayer.getInstance().getUltimoBrano().setBellezza(Stelle);

        UtilityPlayer.getInstance().ImpostaBellezza();

        UtilityPlayer.getInstance().Attesa(false);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
    }

    private void fRitornaListaTags(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Artisti", result);
        if (!ritorno) {
        } else {
            String[] Globale = result.split("§", -1);
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorno Tags -> " + Globale.length);

            List<StrutturaTags> lista = new ArrayList<>();
            for (String g : Globale) {
                if (!g.isEmpty() && g.contains(";")) {
                    String[] c = g.split(";");
                    StrutturaTags s = new StrutturaTags();
                    s.setIdTag(Integer.parseInt(c[0]));
                    s.setTag(c[1]);
                    lista.add(s);
                }
            }

            VariabiliStatichePlayer.getInstance().setListaTags(lista);

            db_dati_player db = new db_dati_player(context);
            db.ScriveTags(lista);
            db.ChiudeDB();

            AdapterListenerTags customAdapterA = new AdapterListenerTags(context,
                    lista);
            VariabiliStatichePrefTags.getInstance().getLstArtisti().setAdapter(customAdapterA);
            VariabiliStatichePrefTags.getInstance().setCustomAdapterTag(customAdapterA);
        }
    }

    private void fRitornaArtisti(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Artisti", result);
        if (!ritorno) {
        } else {
            String[] Globale = result.split("§", -1);
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorno artisti -> " + Globale.length);

            List<StrutturaArtisti> lista = new ArrayList<>();
            for (int i = 0; i < Globale.length; i++) {
                if (!Globale[i].trim().replace("\n", "").isEmpty()) {
                    try {
                        String[] dati = Globale[i].split("\\|", -1);
                        String Artista = dati[0];
                        String Immagine = dati[1];

                        List<String> listaTags = new ArrayList<>();
                        if (!dati[2].isEmpty()) {
                            String[] Tags = dati[2].split("%", -1);
                            for (int k = 0; k < Tags.length; k++) {
                                if (!Tags[k].isEmpty()) {
                                    listaTags.add(Tags[k]);
                                }
                            }
                        }

                        StrutturaArtisti sa = new StrutturaArtisti();
                        sa.setNomeArtista(Artista);
                        sa.setTags(listaTags);
                        sa.setImmagine(Immagine);

                        lista.add(sa);
                    } catch (Exception ignored) {
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorno artisti. Errore su parse (" + Globale[i] + "): " +
                                UtilityDetector.getInstance().PrendeErroreDaException(ignored));
                    }
                }
            }

            db_dati_player db = new db_dati_player(context);
            db.ScriveArtisti(lista);
            db.ChiudeDB();

            if (!PerActivityPrefTags) {
                AdapterListenerArtisti customAdapterA = new AdapterListenerArtisti(context, lista);
                VariabiliStatichePlayer.getInstance().getLstArtisti().setAdapter(customAdapterA);
                VariabiliStatichePlayer.getInstance().setCustomAdapterA(customAdapterA);
            } else {
                VariabiliStatichePlayer.getInstance().setListaArtisti(lista);

                AdapterListenerPreferiti customAdapterA = new AdapterListenerPreferiti(context,
                        lista);
                VariabiliStatichePrefTags.getInstance().getLstArtisti().setAdapter(customAdapterA);
            }

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorno artisti effettuato");
        }
    }

    private void RitornaListaAlbumArtista(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Album Artista", result);
        if (!ritorno) {
            // UtilityPlayer.getInstance().VisualizzaMessaggio(result);
        } else {
            // Tarja Turunen;2005;One Angel's Dream§

            String[] Globale = result.split("§", -1);
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Ritorno album artista -> " + Globale.length);

            List<StrutturaAlbum> lista = new ArrayList<>();
            for (int i = 0; i < Globale.length; i++) {
                if (!Globale[i].trim().replace("\n", "").isEmpty()) {
                    try {
                        String[] dati = Globale[i].split(";", -1);
                        String Artista = dati[0];
                        String Anno = dati[1];
                        String Album = dati[2];

                        StrutturaAlbum sa = new StrutturaAlbum();
                        sa.setArtista(Artista);
                        sa.setAnno(Anno);
                        sa.setAlbum(Album);

                        lista.add(sa);
                        // VariabiliStatichePlayer.getInstance().AggiungeArtista(sa);
                    } catch (Exception ignored) {
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                "Ritorno album artista. Errore su parse (" + Globale[i] + "): " +
                                UtilityDetector.getInstance().PrendeErroreDaException(ignored));
                    }
                }
            }

            AdapterListenerAlbum customAdapterA = new AdapterListenerAlbum(context, lista);
            VariabiliStatichePlayer.getInstance().getLstAlbum().setAdapter(customAdapterA);

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Ritorno album artista effettuato");
        }
    }

    private void RitornaListaBraniAlbumArtista(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Brani Album Artista", result);
        if (!ritorno) {
            // UtilityPlayer.getInstance().VisualizzaMessaggio(result);
        } else {
            // Tarja Turunen;One Angel's Dream;0;Sadness In The Night (feat Tar;4628;2005

            String[] Globale = result.split("§", -1);
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Ritorno brani album artista -> " + Globale.length);

            List<StrutturaBrano> lista = new ArrayList<>();
            for (int i = 0; i < Globale.length; i++) {
                if (!Globale[i].trim().replace("\n", "").isEmpty()) {
                    try {
                        String[] dati = Globale[i].split(";", -1);
                        String Artista = dati[0];
                        String Album = dati[1];
                        String Traccia = dati[2];
                        String Brano = dati[3];
                        String idBrano = dati[4];
                        String Anno = dati[5];

                        StrutturaBrano sa = new StrutturaBrano();
                        sa.setAlbum(Album);
                        sa.setArtista(Artista);
                        sa.setAnno(Anno);
                        sa.setBrano(Brano);
                        sa.setTraccia(Traccia);
                        sa.setIdBrano(Integer.valueOf(idBrano));
                        sa.setAscoltata(0);
                        sa.setBellezza(-2);
                        sa.setCartellaBrano("");
                        sa.setData("");
                        sa.setDimensione(0L);
                        sa.setEsisteBranoSuDisco(false);
                        sa.setEstensione("");
                        sa.setPathBrano("");
                        sa.setImmagini(new ArrayList<>());
                        sa.setQuantiBrani(-1);
                        sa.setTags("");
                        sa.setTesto("");
                        sa.setTestoTradotto("");
                        sa.setTipoBrano(0);
                        sa.setUrlBrano("");

                        lista.add(sa);
                        // VariabiliStatichePlayer.getInstance().AggiungeArtista(sa);
                    } catch (Exception ignored) {
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                "Ritorno brano album artisti. Errore su parse (" + Globale[i] + "): " +
                                UtilityDetector.getInstance().PrendeErroreDaException(ignored));
                    }
                }
            }

            AdapterListenerBraniOnline customAdapterA = new AdapterListenerBraniOnline(context, lista);
            VariabiliStatichePlayer.getInstance().getLstBrani().setAdapter(customAdapterA);

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Ritorno brano album artisti effettuato");
        }
    }

    /* private void aggiungeOperazione() {
        if (!Riprova) {
            VariabiliStatichePlayer.getInstance().AggiungeChiamata(chiamataDaFare);

            RipristinoChiamate.getInstance().AttivaTimerChiamate(context);
        }
    } */

    private boolean ControllaRitorno(String Operazione, String result) {
        if (result.contains("ERROR:")) {
            UtilityPlayer.getInstance().Attesa(false);
            UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");

            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
                UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");

                if (Operazione.equals("Carica Brano")) {
                    UtilityPlayer.getInstance().PrendeBranoInLocaleNonEsatto(context, Pregresso);
                /* } else {
                    UtilitiesGlobali.getInstance().setRetePresente(false);
                    aggiungeOperazione();
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, Operazione + ": Rete non presente o timeout nella chiamata. Riprova: " + Riprova);
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, result); */
                }
            }

            return false;
        } else {
            /* if (Riprova) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Riprovo chiamata riuscito");

                RipristinoChiamate.getInstance().setTentativiEffettuati(0);

                VariabiliStatichePlayer.getInstance().RimuovePrimaChiamata();
                if (!VariabiliStatichePlayer.getInstance().getChiamate().isEmpty()) {
                    RipristinoChiamate.getInstance().AttivaTimerChiamate(context);
                } else {
                    RipristinoChiamate.getInstance().RimuoveTimer();
                }
            } */

            return true;
        }
    }

    public void CaricaBrano(String result) {
        boolean ritorno = ControllaRitorno("Carica Brano", result);
        if (!ritorno) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Carica brano: Esco per result non valido");

            return;
        }

        if (result.contains("|")) {
            String[] Globale = result.split("\\|", -1);
            String[] DatiBrano = Globale[0].split(";", -1);
            String[] Immagini = Globale[1].split("§", -1);
            String[] TestoEAltro = Globale[2].split(";", -1);
            if (Globale[2].equals(";;;;")) {
                TestoEAltro = new String[]{"", "", "", ""};
            }

            if (!Pregresso) {
                VariabiliStatichePlayer.getInstance().setBraniTotali(Integer.parseInt(DatiBrano[1]));
            }

            StrutturaBrano s = new StrutturaBrano();
            s.setIdBrano(Integer.parseInt(DatiBrano[0]));

            s.setQuantiBrani(Integer.parseInt(DatiBrano[1]));
            s.setArtista(DatiBrano[3]);

            s.setAlbum(DatiBrano[4]);

            s.setBrano(DatiBrano[5]);
            String Anno = DatiBrano[6];
            for (int i = Anno.length() + 1; i < 5; i++) {
                Anno = "0" + Anno;
            }
            s.setAnno(Anno);
            String Traccia = DatiBrano[7];
            if (Traccia.length() == 1) {
                Traccia = "0" + Traccia;
            }
            s.setTraccia(Traccia);
            s.setEstensione(DatiBrano[8]);
            s.setData(DatiBrano[9]);
            try {
                if (!DatiBrano[10].isEmpty()) {
                    s.setDimensione(Long.parseLong(DatiBrano[10]) / 1024L);
                } else {
                    s.setDimensione(0L);
                }
            } catch (Exception ignored) {
                s.setDimensione(0L);
            }
            try {
                String t = DatiBrano[11].replace("*PV*", ";");
                String[] tt = t.split(";", -1);
                String tags = "";
                for (int i = 0; i < tt.length; i++) {
                    if (!tt[i].isEmpty()) {
                        tags += tt[i] + ";";
                    }
                }
                if (tags.length() > 0) {
                    tags = tags.substring(0, tags.length() - 1);
                }
                s.setTags(tags);
            } catch (Exception ignored) {
                s.setTags("");
            }

            String UrlBrano = VariabiliStatichePlayer.PercorsoBranoMP3SuURL +
                    "/MP3/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4] + "/" +
                    Traccia + "-" + DatiBrano[5] + DatiBrano[8];
            s.setUrlBrano(UrlBrano);

            String PathBrano = context.getFilesDir() + "/Player/" +
                    "Brani/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4] + "/" +
                    Traccia + "-" + DatiBrano[5] + DatiBrano[8];
            s.setPathBrano(PathBrano);

            String CartellaBrano = context.getFilesDir() + "/Player/" +
                    "Brani/" + DatiBrano[3] + "/" + Anno + "-" + DatiBrano[4];
            s.setCartellaBrano(CartellaBrano);

            if (UtilityDetector.getInstance().EsisteFile(PathBrano)) {
                s.setEsisteBranoSuDisco(true);
            } else {
                s.setEsisteBranoSuDisco(false);
            }

            db_dati_player db = new db_dati_player(context);
            List<StrutturaImmagini> ListaImmagini = db.CaricaImmaginiBrano(DatiBrano[3]);
            if (ListaImmagini.isEmpty() && !Pregresso) {
                ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
                c.RitornaImmaginiArtista(s.getArtista());
            }
            String ImmagineDaImpostare = "";
            StrutturaImmagini StruttImmDaImpostare = new StrutturaImmagini();
            for (int i = 0; i < Immagini.length; i++) {
                if (!Immagini[i].isEmpty()) {
                    String[] Imm2 = Immagini[i].split(";", -1);

                    boolean ok = true;

                    for (StrutturaImmagini li : ListaImmagini) {
                        if (li.getArtista().equals(DatiBrano[3]) &&
                                li.getAlbum().equals(Imm2[1]) &&
                                li.getNomeImmagine().equals(Imm2[2])) {
                            ok = false;
                            break;
                        }
                    }

                    if (ok) {
                        StrutturaImmagini Imm = new StrutturaImmagini();
                        /* if (Imm2[2].toUpperCase().contains("COVER_")) {
                            Imm.setAlbum(Imm2[1]);
                        } else {
                        } */

                        Imm.setArtista(DatiBrano[3]);
                        Imm.setAlbum(Imm2[1]);
                        Imm.setNomeImmagine(Imm2[2]);

                        String UrlImmagine = VariabiliStatichePlayer.PercorsoBranoMP3SuURL +
                                "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1] + "/" +
                                Imm2[2];
                        Imm.setUrlImmagine(UrlImmagine);

                        String PathImmagine = context.getFilesDir() + "/Player/" +
                                "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1] + "/" +
                                Imm2[2];
                        Imm.setPathImmagine(PathImmagine);

                        String CartellaImmagine = context.getFilesDir() + "/Player/" +
                                "/ImmaginiMusica/" + DatiBrano[3] + "/" + Imm2[1];
                        Imm.setCartellaImmagine(CartellaImmagine);

                        ListaImmagini.add(Imm);

                        db.ScriveImmagineBrano(DatiBrano[3], Imm);
                    }
                }
            }
            int immagine = -1;
            for (int i = 0; i < ListaImmagini.size(); i++) {
                if (ListaImmagini.get(i).getAlbum().toUpperCase().contains(s.getAlbum().toUpperCase().trim())) {
                    immagine = i;
                    break;
                }
            }
            if (immagine == -1) {
                immagine = UtilityPlayer.getInstance().GeneraNumeroRandom(ListaImmagini.size());
            }
            if (immagine > -1) {
                StrutturaImmagini si = ListaImmagini.get(immagine);
                ImmagineDaImpostare = si.getUrlImmagine();
                StruttImmDaImpostare = si;
            } else {
                // UtilityPlayer.getInstance().ImpostaSfondoLogo();
            }

            s.setImmagini(ListaImmagini);

            if (TestoEAltro[0].isEmpty()) {
                s.setAscoltata(0);
            } else {
                s.setAscoltata(Integer.parseInt(TestoEAltro[0]));
            }
            if (TestoEAltro[1].isEmpty()) {
                s.setBellezza(0);
            } else {
                s.setBellezza(Integer.parseInt(TestoEAltro[1]));
            }
            String Testo = "";
            if (TestoEAltro.length > 2) {
                Testo = TestoEAltro[2];
            } else {
                Testo = "";
            }
            if (!Testo.isEmpty()) {
                Testo = Testo.replace("§", "\n");
                Testo = Testo.replace("%20", " ");

                /* String NomeFile = Traccia + "-" + DatiBrano[5] + ".txt";
                if (!Files.getInstance().EsisteFile(CartellaTesto + NomeFile)) {
                    Files.getInstance().CreaCartelle(CartellaTesto);
                    Files.getInstance().ScriveFile(CartellaTesto, NomeFile, Testo);
                }
                NomeFile = Traccia + "-" + DatiBrano[5] + ".2.txt";
                if (!Files.getInstance().EsisteFile(CartellaTesto + NomeFile)) {
                    String Cosa = s.getBellezza() + ";" + s.getAscoltata();
                    Files.getInstance().CreaCartelle(CartellaTesto);
                    Files.getInstance().ScriveFile(CartellaTesto, NomeFile, Cosa);
                }
                NomeFile = Traccia + "-" + DatiBrano[5] + ".TAGS.txt";
                if (!Files.getInstance().EsisteFile(CartellaTesto + NomeFile)) {
                    String Cosa = s.getTags();
                    Files.getInstance().CreaCartelle(CartellaTesto);
                    Files.getInstance().ScriveFile(CartellaTesto, NomeFile, Cosa);
                }
                NomeFile = Traccia + "-" + DatiBrano[5] + ".DATA.txt";
                if (!Files.getInstance().EsisteFile(CartellaTesto + NomeFile)) {
                    String Cosa = s.getData();
                    Files.getInstance().CreaCartelle(CartellaTesto);
                    Files.getInstance().ScriveFile(CartellaTesto, NomeFile, Cosa);
                } */
            }
            s.setTesto(Testo);

            if (TestoEAltro.length > 3) {
                s.setTestoTradotto(TestoEAltro[3]);
            } else {
                s.setTestoTradotto("");
            }

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Brano Pregresso: " + Pregresso);
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "idBrano: " + s.getIdBrano());
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Titolo Brano: " + s.getBrano());
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Album: " + s.getAlbum());
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Artista: " + s.getArtista());
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "File: " + s.getPathBrano());
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "File esistente: " + UtilityDetector.getInstance().EsisteFile(s.getPathBrano()));
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "URL: " + s.getUrlBrano());

            if (!UtilityDetector.getInstance().EsisteFile(s.getPathBrano())) {
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Scarico il brano in locale");
                DownloadCanzone d = new DownloadCanzone();
                VariabiliStatichePlayer.getInstance().setDownCanzone(d);
                d.EsegueDownload(context, s, Pregresso);
                // new DownloadBrano(context, s, Pregresso).execute(s.getUrlBrano());
            } else {
                if (!Pregresso) {
                    if (VariabiliStatichePlayer.getInstance().getUltimoBrano() == null) {
                        db.ScriveBrano(s);
                        db.ScriveUltimoBranoAscoltato(s);

                        VariabiliStatichePlayer.getInstance().setUltimoBrano(s);
                    }

                    UtilityPlayer.getInstance().CaricaBranoNelLettore(context);
                } else {
                    VariabiliStatichePlayer.getInstance().setStrutturaBranoPregressoCaricata(s);
                }
            }
            db.ChiudeDB();
        }
    }
}
