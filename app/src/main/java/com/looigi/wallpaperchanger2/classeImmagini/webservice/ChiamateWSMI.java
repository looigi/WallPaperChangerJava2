package com.looigi.wallpaperchanger2.classeImmagini.webservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.webService.ChiamateWSIFC;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.VariabiliStaticheImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.strutture.StrutturaImmagineRaggruppata;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.webService.ChiamateWSIR;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePazzia.DownloadImmaginePAZZIA;
import com.looigi.wallpaperchanger2.classePazzia.UtilityPazzia;
import com.looigi.wallpaperchanger2.classePazzia.VariabiliStatichePazzia;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePreview.UtilitiesPreview;
import com.looigi.wallpaperchanger2.classePreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.classePreview.classeOCR.UtilitiesOCR;
import com.looigi.wallpaperchanger2.classePreview.classeOCR.VariabiliStaticheOCR;
import com.looigi.wallpaperchanger2.classePreview.classeOCR.webService.ChiamateWSOCR;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.adapters.AdapterListenerImmaginiDaScaricare;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.DownloadImmagineSI;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.MainScaricaImmagini;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.StrutturaImmagineDaScaricare;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeSpostamento.VariabiliStaticheSpostamento;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.VariabiliStaticheUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters.AdapterListenerUI;
// import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.VariabiliStaticheVolti;
// import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.webService.ChiamateWSV;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ChiamateWSMI implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Immagini";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmagini.UrlWS;
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private Context context;
    private boolean ApriDialog = false;
    private GifImageView imgAttesa;
    private boolean Sovrascrive = false;
    private String Categoria;
    private ImageView imgQuale;
    private String UrlImmagine;
    private String daDove;
    private String idImmagine;

    public ChiamateWSMI(Context context) {
        this.context = context;
    }

    public void ImpostaImgAttesa(GifImageView imgAttesa) {
        this.imgAttesa = imgAttesa;
    }

    public void RitornaProssimaImmaginePerWP(String Filtro) {
        if (VariabiliStaticheApiFootball.getInstance().isStaSalvandoTutteLePartite() ||
            VariabiliScaricaImmagini.getInstance().isMascheraAttiva()) {
            // Controllo che evita il cambio wp se sta salvando le partite apiFootball
            return;
        }

        String Urletto="ProssimaImmagine?" +
                "idCategoria=" +
                "&Filtro=" + Filtro +
                "&idImmagine=0" +
                "&Random=S" +
                "&OrdinaPerVisualizzato=N" +
                "&Operatore=" + VariabiliStaticheWallpaper.getInstance().getOperatoreFiltro() +
                "&Preview=N";

        TipoOperazione = "ProssimaImmagineWP";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void RitornaProssimaImmagine(int idCategoria, int idImmagine, String Random, String daDove) {
        if (idCategoria == -999) {
            return;
        }

        this.daDove = daDove;

        String Urletto = "";

        switch (daDove) {
            case "PAZZIA":
                Urletto="ProssimaImmagine?" +
                        "idCategoria=" + (idCategoria > 0 ? idCategoria : "") +
                        "&Filtro=" + VariabiliStatichePazzia.getInstance().getFiltroIMM() +
                        "&idImmagine=" + idImmagine +
                        "&Random=" + Random +
                        "&OrdinaPerVisualizzato=S" +
                        "&Operatore=Or" +
                        "&Preview=N";

                UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                        VariabiliStatichePazzia.getInstance().getImgCaricamentoIMM(),
                        true
                );
                break;
            case "IMMAGINI":
                Urletto = "ProssimaImmagine?" +
                        "idCategoria=" + (idCategoria > 0 ? idCategoria : "") +
                        "&Filtro=" + VariabiliStaticheMostraImmagini.getInstance().getFiltro() +
                        "&idImmagine=" + idImmagine +
                        "&Random=" + Random +
                        "&OrdinaPerVisualizzato=" + (VariabiliStaticheMostraImmagini.getInstance().isRicercaPerVisua() ? "S" : "N") +
                        "&Operatore=" + VariabiliStaticheMostraImmagini.getInstance().getOperatoreFiltro() +
                        "&Preview=N";
                break;
        }

        TipoOperazione = "ProssimaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void EliminaImmagine(String id) {
        String Urletto="EliminaImmagine?idImmagine=" + id;

        TipoOperazione = "EliminaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void RinominaCategoria(String Vecchia, String Nuova, String daDove) {
        this.daDove = daDove;

        String Urletto="RinominaCategoria?" +
                "VecchiaCategoria=" + Vecchia +
                "&NuovaCategoria=" + Nuova;

        TipoOperazione = "RinominaCategoria";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void CreaNuovaCategoria(String NuovaCategoria, String daDove) {
        String Urletto="CreaNuovaCategoria?Categoria=" + NuovaCategoria;
        this.daDove = daDove;

        switch(daDove) {
            case "UI":
                VariabiliStaticheUtilityImmagini.getInstance().Attesa(true);
                break;
            case "IR":
                VariabiliStaticheImmaginiRaggruppate.getInstance().Attesa(true);
                break;
            case "FC":
                VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                break;
            case "OCR":
                UtilitiesOCR.getInstance().Attesa(true);
                break;
        }

        TipoOperazione = "NuovaCategoria";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void ScaricaImmagini(String Categoria, String Ricerca, String Filtra, String daDove) {
        this.daDove = daDove;

        switch (daDove) {
            case "UI":
                VariabiliStaticheUtilityImmagini.getInstance().Attesa(true);
        }

        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "Scarica Immagini " + Categoria);

        this.Categoria = Categoria;

        String Urletto="ScaricaListaImmagini?" +
                "Categoria=" + Ricerca +
                "&Filtra=" + Filtra;

        TipoOperazione = "ScaricaListaImmagini";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                165000,
                ApriDialog);
    }

    public void UploadImmagine(String NomeFile, String base64, ImageView imgQuale, String UrlImmagine) {
        String sUrlImmagine = UrlImmagine;
        sUrlImmagine = sUrlImmagine.replace("/", "-SL-");
        sUrlImmagine = sUrlImmagine.replace("&", "-AN-");
        sUrlImmagine = sUrlImmagine.replace("?", "-PI-");
        sUrlImmagine = sUrlImmagine.replace(":", "-2P-");

        String Urletto="UploadImmagine?" +
                "Categoria=" + VariabiliStaticheMostraImmagini.getInstance().getCategoria().replace("\\", "§") +
                "&NomeFile=" + NomeFile +
                "&Base64=" + base64 +
                "&UrlImmagine=" + sUrlImmagine;

        TipoOperazione = "UploadImmagine";
        this.imgQuale = imgQuale;
        this.UrlImmagine = UrlImmagine;
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                600000,
                ApriDialog);
    }

    public void SpostaImmagine(StrutturaImmaginiLibrary s, String daDove) {
        this.daDove = daDove;
        String Urletto="";

        switch(daDove) {
            case "IR":
                VariabiliStaticheImmaginiRaggruppate.getInstance().Attesa(true);
                String idCategoria = "";
                for (StrutturaImmaginiCategorie s1: VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM()) {
                    if (s1.getCategoria().equals(VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata())) {
                        idCategoria = String.valueOf(s1.getIdCategoria());
                        break;
                    }
                }
                if (idCategoria.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "idCategoria non valida");
                    return;
                } else {
                    Urletto = "SpostaImmagineACategoria?" +
                            "idImmagine=" + s.getIdImmagine() +
                            "&idCategoriaNuova=" + idCategoria;
                }
                break;
            case "SPOSTAMENTO":
                VariabiliStaticheSpostamento.getInstance().Attesa(true);

                Urletto = "SpostaImmagineACategoria?" +
                        "idImmagine=" + s.getIdImmagine() +
                        "&idCategoriaNuova=" + VariabiliStaticheSpostamento.getInstance().getIdCategoriaSpostamento();
                break;
            case "PREVIEW":
                UtilitiesPreview.getInstance().Attesa(true);

                Urletto = "SpostaImmagineACategoria?" +
                        "idImmagine=" + s.getIdImmagine() +
                        "&idCategoriaNuova=" + VariabiliStatichePreview.getInstance().getIdCategoriaSpostamento();
                break;
            case "IMMAGINI":
            case "FC":
                if (VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento() != null) {
                    VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                }

                Urletto = "SpostaImmagineACategoria?" +
                        "idImmagine=" + s.getIdImmagine() +
                        "&idCategoriaNuova=" + VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento();
                break;
            case "VO":
                this.idImmagine = String.valueOf(s.getIdImmagine());

                Urletto = "SpostaImmagineACategoria?" +
                        "idImmagine=" + s.getIdImmagine() +
                        "&idCategoriaNuova=" + VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento();
                break;
            case "OCR":
                this.idImmagine = String.valueOf(s.getIdImmagine());

                Urletto = "SpostaImmagineACategoria?" +
                        "idImmagine=" + s.getIdImmagine() +
                        "&idCategoriaNuova=" + VariabiliStaticheSpostamento.getInstance().getIdCategoriaSpostamento();
                break;
        }

        TipoOperazione = "SpostaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void ModificaImmagine(StrutturaImmaginiLibrary s, String stringaBase64, boolean Sovrascrive) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(true);

        this.Sovrascrive = Sovrascrive;

        String Urletto="ModificaImmagine?" +
                "Categoria=" + s.getCategoria() +
                "&idImmagine=" + s.getIdImmagine() +
                "&StringaBase64=" + stringaBase64 +
                "&Sovrascrivi=" + (Sovrascrive ? "S" : "N");

        TipoOperazione = "ModificaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaCategorie(boolean forzaLettura, String daDove) {
        this.daDove = daDove;

        if (!forzaLettura) {
            db_dati_immagini db = new db_dati_immagini(context);
            List<StrutturaImmaginiCategorie> lista = db.LeggeCategorie();
            db.ChiudeDB();

            if (!lista.isEmpty()) {
                switch (daDove) {
                    case "IMMAGINI":
                        VariabiliStaticheMostraImmagini.getInstance().setListaCategorie(lista);
                        UtilityImmagini.getInstance().AggiornaCategorie(context);
                        UtilityImmagini.getInstance().AggiornaCategorieSpostamento(context);

                        for (StrutturaImmaginiCategorie c : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
                            if (c.getCategoria().equals(VariabiliStaticheMostraImmagini.getInstance().getCategoriaAttuale())) {
                                VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(c.getIdCategoria());
                                break;
                            }
                        }
                        break;
                    case "PAZZIA":
                        VariabiliStatichePazzia.getInstance().setListaCategorieIMM(lista);
                        break;
                    case "IR":
                        List<StrutturaImmaginiCategorie> lista2 = db.LeggeCategorie();
                        for (StrutturaImmaginiCategorie l: lista) {
                            if (!l.getCategoria().toUpperCase().trim().equals("TUTTE")) {
                                lista2.add(l);
                            }
                        }
                        VariabiliStaticheImmaginiRaggruppate.getInstance().setListaCategorieIMM(lista2);

                        String[] ll = new String[VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM().size() + 1];
                        int i = 0;
                        for (StrutturaImmaginiCategorie l: VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM()) {
                            if (l.getCategoria() != null) {
                                ll[i] = l.getCategoria();
                            }
                            i++;
                        }
                        i = 0;
                        for (String lll: ll) {
                            if (lll == null || lll.isEmpty()) {
                              ll[i] = "**NULL**";
                            }
                            i++;
                        }
                        if (VariabiliStaticheImmaginiRaggruppate.getInstance().getIdCategoria() != null &&
                                !VariabiliStaticheImmaginiRaggruppate.getInstance().getIdCategoria().isEmpty()) {
                            for (StrutturaImmaginiCategorie l: VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM()) {
                                if (l.getIdCategoria() == Integer.parseInt(VariabiliStaticheImmaginiRaggruppate.getInstance().getIdCategoria())) {
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().setCategoriaImpostata(l.getCategoria());
                                    break;
                                }
                            }
                        }
                        UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                VariabiliStaticheImmaginiRaggruppate.getInstance().getSpnCategorie(),
                                ll,
                                VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata()
                        );
                        break;
                    case "UI":
                        List<StrutturaImmaginiCategorie> lista22 = db.LeggeCategorie();
                        for (StrutturaImmaginiCategorie l: lista) {
                            if (!l.getCategoria().toUpperCase().trim().equals("TUTTE")) {
                                lista22.add(l);
                            }
                        }
                        VariabiliStaticheUtilityImmagini.getInstance().setListaCategorieIMM(lista22);

                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.RitornaCategorieDiRicerca();
                        break;
                    case "FC":
                    case "VO":
                        if (VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento() != null) {
                            VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
                        }

                        List<StrutturaImmaginiCategorie> lista222 = db.LeggeCategorie();
                        for (StrutturaImmaginiCategorie l: lista) {
                            if (!l.getCategoria().toUpperCase().trim().equals("TUTTE")) {
                                lista222.add(l);
                            }
                        }
                        VariabiliImmaginiFuoriCategoria.getInstance().setListaCategorieIMM(lista222);
                        String[] ll2 = new String[VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM().size() + 1];
                        int i2 = 0;
                        for (StrutturaImmaginiCategorie l: VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                            if (l.getCategoria() != null) {
                                ll2[i2] = l.getCategoria();
                            }
                            i2++;
                        }
                        i = 0;
                        for (String lll: ll2) {
                            if (lll == null || lll.isEmpty()) {
                                ll2[i] = "**NULL**";
                            }
                            i++;
                        }

                        if (daDove.equals("FC")) {
                            UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                    VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorie(),
                                    ll2,
                                    VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaInserita()
                            );
                        } else {
                            UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().getSpnCategorie(),
                                    ll2,
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata()
                            );
                        }
                        break;
                    case "PREVIEW":
                        VariabiliStatichePreview.getInstance().setListaCategorie(lista);
                        String[] ll22 = new String[lista.size() + 1];
                        i = 0;
                        for (StrutturaImmaginiCategorie lll: lista) {
                            if (lll.getCategoria() != null && !lll.getCategoria().isEmpty()) {
                                ll22[i] = lll.getCategoria();
                            }
                            i++;
                        }
                        UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                VariabiliStatichePreview.getInstance().getSpnSpostaCategorie(),
                                ll22,
                                ""
                        );
                        break;
                    case "OCR":
                        VariabiliStaticheOCR.getInstance().setListaCategorie(lista);
                        break;
                }

                return;
            }
        }

        switch (daDove) {
            case "IR":
                VariabiliStaticheImmaginiRaggruppate.getInstance().Attesa(true);
                break;
            case "UI":
                VariabiliStaticheUtilityImmagini.getInstance().Attesa(true);
                break;
            case "FC":
                VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                break;
            case "PREVIEW":
                UtilitiesPreview.getInstance().Attesa(true);
                break;
            case "OCR":
                UtilitiesOCR.getInstance().Attesa(true);
                break;
            // case "VO":
            //     VariabiliStaticheVolti.getInstance().Attesa(true);
        }

        String Urletto="RitornaCategorie";

        TipoOperazione = "RitornaCategorie";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000000,
                ApriDialog);
    }

    public void RefreshImmagini(String idCategoria) {
        String Urletto="RefreshImmagini?" +
                "idCategoria=" + idCategoria +
                "&Completo=" + (VariabiliStaticheMostraImmagini.getInstance().isAggiornamentoCompleto() ? "S" : "");

        TipoOperazione = "RefreshImmagini";
        // ControllaTempoEsecuzione = false;

        UtilitiesGlobali.getInstance().ApreToast(context, "Refresh immagini lanciato");

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                250000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Chiamata WS " + TipoOperazione + ". OK");
        UtilityImmagini.getInstance().Attesa(true);

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSMI i = new InterrogazioneWSMI();
        i.EsegueChiamata(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this
        );
        /* bckAsyncTask = new LetturaWSAsincrona(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this);
        bckAsyncTask.execute(Urletto); */
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

                UtilityImmagini.getInstance().Attesa(false);

                switch (TipoOperazione) {
                    case "RefreshImmagini":
                        fRefreshImmagini(result);
                        break;
                    case "ProssimaImmagine":
                        fProssimaImmagine(result);
                        break;
                    case "ProssimaImmagineWP":
                        fProssimaImmagineWP(result);
                        break;
                    case "ModificaImmagine":
                        fModificaImmagine(result);
                        break;
                    case "RitornaCategorie":
                        fRitornaCategorie(result);
                        break;
                    case "EliminaImmagine":
                        fEliminaImmagine(result);
                        break;
                    case "SpostaImmagine":
                        fSpostaImmagine(result);
                        break;
                    case "ScaricaListaImmagini":
                        fScaricaListaImmagini(result);
                        break;
                    case "UploadImmagine":
                        fUploadImmagine(result);
                        break;
                    case "NuovaCategoria":
                        fNuovaCategoria(result);
                        break;
                    case "RinominaCategoria":
                        fRinominaCategoria(result);
                        break;
                }


                if (VariabiliStatichePlayer.getInstance().getLayCaricamentoSI() != null) {
                    VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.GONE);
                }
                UtilitiesOCR.getInstance().Attesa(false);

                if (imgAttesa != null) {
                    imgAttesa.setVisibility(LinearLayout.GONE);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
        if (VariabiliStaticheMostraImmagini.getInstance().getClasseChiamata() != null) {
            VariabiliStaticheMostraImmagini.getInstance().getClasseChiamata().BloccaEsecuzione();
        }
    }

    private boolean ControllaRitorno(String Operazione, String result) {
        if (result.contains("ERROR:")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
            }

            return false;
        } else {
            return true;
        }
    }

    private void fRinominaCategoria(String result) {
        boolean ritorno = ControllaRitorno("RinominaCategoria", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Categoria rinominata");

            ChiamateWSMI c = new ChiamateWSMI(context);
            c.RitornaCategorie(true, daDove);
        }
    }

    private void fNuovaCategoria(String result) {
        boolean ritorno = ControllaRitorno("Crea nuova categoria", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch(daDove) {
                        case "IR":
                            VariabiliStaticheImmaginiRaggruppate.getInstance().Attesa(false);
                            break;
                        case "UI":
                            VariabiliStaticheUtilityImmagini.getInstance().Attesa(false);
                            break;
                        case "FC":
                            VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
                            break;
                        case "OCR":
                            UtilitiesOCR.getInstance().Attesa(false);
                            break;
                        /* case "VO":
                            VariabiliStaticheVolti.getInstance().Attesa(false);
                            break; */
                    }

                    ChiamateWSMI c = new ChiamateWSMI(context);
                    c.RitornaCategorie(true, daDove);
                }
            }, 500);

            UtilitiesGlobali.getInstance().ApreToast(context, "Nuova categoria creata");
        }
    }

    private void fScaricaListaImmagini(String result) {
        switch (daDove) {
            case "UI":
                VariabiliStaticheUtilityImmagini.getInstance().Attesa(false);
        }

        boolean ritorno = ControllaRitorno("Scarica lista immagini categoria " + Categoria, result);
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
                    si.putExtra("MODALITA", "IMMAGINI");
                    si.putExtra("FILTRO", Categoria);
                    context.startActivity(si);
                }
            }
        }
    }

    private void fUploadImmagine(String result) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(false);

        String Modalita = VariabiliScaricaImmagini.getInstance().getModalita();
        String Filtro = VariabiliScaricaImmagini.getInstance().getFiltro();

        boolean ritorno = ControllaRitorno("Upload Immagine", result);
        if (ritorno) {
            if (imgQuale != null) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.download);
                imgQuale.setImageBitmap(bitmap);
                imgQuale.setVisibility(LinearLayout.GONE);
            }

            if (VariabiliScaricaImmagini.getInstance().getImgScaricaDaDisabilitare() != null) {
                VariabiliScaricaImmagini.getInstance().getImgScaricaDaDisabilitare().setVisibility(LinearLayout.GONE);
                VariabiliScaricaImmagini.getInstance().setImgScaricaDaDisabilitare(null);
            }
            // VariabiliScaricaImmagini.getInstance().getChkSelezione().setChecked(false);

            /* List<String> l = new ArrayList<>();
            for (String s : VariabiliStatichePlayer.getInstance().getUrlImmaginiDaScaricare()) {
                if (!s.equals(UrlImmagine)) {
                    l.add(s);
                }
            }
            VariabiliStatichePlayer.getInstance().setUrlImmaginiDaScaricare(l); */

            if (!VariabiliStaticheUtilityImmagini.getInstance().getFileDaEliminare().isEmpty()) {
                Files.getInstance().EliminaFileUnico(VariabiliStaticheUtilityImmagini.getInstance().getFileDaEliminare());
                VariabiliStaticheUtilityImmagini.getInstance().setFileDaEliminare("");
            }

            if (!VariabiliScaricaImmagini.getInstance().isScaricaMultiplo()) {
                AggiornaImmagini(Modalita, Filtro);

                UtilitiesGlobali.getInstance().ApreToast(context, "Upload immagine completato");
            } else {
                ScaricaSuccessiva(Modalita, Filtro);
            }
        } else {
            VariabiliScaricaImmagini.getInstance().setScaricataBene(false);

            if (!VariabiliScaricaImmagini.getInstance().isScaricaMultiplo()) {
                UtilitiesGlobali.getInstance().ApreToast(context, result);
                VariabiliScaricaImmagini.getInstance().PulisceCartellaAppoggio(context);
            } else {
                ScaricaSuccessiva(Modalita, Filtro);
            }
        }

        UtilityPlayer.getInstance().AttesaSI(false);
    }

    public void ScaricaSuccessiva(String Modalita, String Filtro) {
        /* if (!VariabiliScaricaImmagini.getInstance().getListaDaScaricare().isEmpty()) {
            VariabiliScaricaImmagini.getInstance().getListaDaScaricare().remove(0);
        } */

        int quale = VariabiliScaricaImmagini.getInstance().getQualeImmagineStoScaricando();

        int riga = -1;
        int i = 0;
        for (String s: VariabiliStatichePlayer.getInstance().getUrlImmaginiDaScaricare()) {
            if (quale < VariabiliScaricaImmagini.getInstance().getListaDaScaricare().size()) {
                if (s.equals(VariabiliScaricaImmagini.getInstance().getListaDaScaricare().get(quale).getUrlImmagine())) {
                    riga = i;
                    break;
                }
            }
            i++;
        }

        if (riga == -1) {
            VariabiliScaricaImmagini.getInstance().getTxtSelezionate().setVisibility(LinearLayout.GONE);
            VariabiliScaricaImmagini.getInstance().getImgScaricaTutte().setVisibility(LinearLayout.GONE);

            AggiornaImmagini(Modalita, Filtro);
            VariabiliScaricaImmagini.getInstance().PulisceCartellaAppoggio(context);

            UtilitiesGlobali.getInstance().ApreToast(context, "Errore su upload immagini: indice sbragato 1");
            return;
        }

        if (riga < VariabiliScaricaImmagini.getInstance().getListaOriginaleDaScaricare().size()) {
            if (VariabiliScaricaImmagini.getInstance().isScaricataBene()) {
                VariabiliScaricaImmagini.getInstance().getListaOriginaleDaScaricare().set(riga, "OK");
            } else {
                VariabiliScaricaImmagini.getInstance().getListaOriginaleDaScaricare().set(riga, "ERRORE");
            }
        }

        VariabiliScaricaImmagini.getInstance().setQualeImmagineStoScaricando(
                quale + 1
        );

        VariabiliScaricaImmagini.getInstance().getTxtSelezionate().setText("Scarico: " +
                (VariabiliScaricaImmagini.getInstance().getQualeImmagineStoScaricando() + 1) + "/" +
                (VariabiliScaricaImmagini.getInstance().getListaDaScaricare().size()));

        if (VariabiliScaricaImmagini.getInstance().getQualeImmagineStoScaricando() <
            VariabiliScaricaImmagini.getInstance().getListaDaScaricare().size()) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (VariabiliScaricaImmagini.getInstance().getQualeImmagineStoScaricando() < VariabiliScaricaImmagini.getInstance().getListaDaScaricare().size()) {
                        StrutturaImmagineDaScaricare s = VariabiliScaricaImmagini.getInstance().getListaDaScaricare().get(
                                VariabiliScaricaImmagini.getInstance().getQualeImmagineStoScaricando()
                        );

                        VariabiliScaricaImmagini.getInstance().setImgScaricaDaDisabilitare(s.getImgImmagine());
                        VariabiliScaricaImmagini.getInstance().setChkSelezione(s.getChkSelezione());

                        VariabiliScaricaImmagini.getInstance().setScaricataBene(false);
                        DownloadImmagineSI d = new DownloadImmagineSI();
                        d.EsegueDownload(context, s.getImgImmagine(), s.getUrlImmagine(), Modalita,
                                Filtro, true, "SCARICA", 0, null);
                    } else {
                        VariabiliScaricaImmagini.getInstance().getTxtSelezionate().setVisibility(LinearLayout.GONE);
                        VariabiliScaricaImmagini.getInstance().getImgScaricaTutte().setVisibility(LinearLayout.GONE);

                        AggiornaImmagini(Modalita, Filtro);
                        VariabiliScaricaImmagini.getInstance().PulisceCartellaAppoggio(context);

                        UtilitiesGlobali.getInstance().ApreToast(context, "Errore su upload immagini: indice sbragato 2");
                    }
                }
            }, 500);
        } else {
            VariabiliScaricaImmagini.getInstance().getTxtSelezionate().setVisibility(LinearLayout.GONE);
            VariabiliScaricaImmagini.getInstance().getImgScaricaTutte().setVisibility(LinearLayout.GONE);
            VariabiliScaricaImmagini.getInstance().setScaricaMultiplo(false);

            AggiornaImmagini(Modalita, Filtro);
            VariabiliScaricaImmagini.getInstance().PulisceCartellaAppoggio(context);

            UtilitiesGlobali.getInstance().ApreToast(context, "Upload immagini completati");
        }
    }

    public void AggiornaImmagini(String Modalita, String Filtro) {
        if (VariabiliScaricaImmagini.getInstance().getLstImmagini() != null) {
            List<String> listaImmagini = VariabiliStatichePlayer.getInstance().getUrlImmaginiDaScaricare();

            AdapterListenerImmaginiDaScaricare customAdapterT = new AdapterListenerImmaginiDaScaricare(
                    context,
                    Modalita,
                    Filtro,
                    listaImmagini);
            VariabiliScaricaImmagini.getInstance().getLstImmagini().setAdapter(customAdapterT);
        }
    }

    private void fModificaImmagine(String result) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Modifica Immagine", result);
        if (ritorno) {
            String Path = context.getFilesDir() + "/Immagini/AppoggioMI.jpg";
            Bitmap bmp = BitmapFactory.decodeFile(Path);
            if (VariabiliStaticheMostraImmagini.getInstance().getImg() != null) {
                VariabiliStaticheMostraImmagini.getInstance().getImg().setImageBitmap(bmp);
            }
            if (VariabiliStatichePreview.getInstance().getImgPreview() != null) {
                VariabiliStatichePreview.getInstance().getImgPreview().setImageBitmap(bmp);
            }

            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine modificata");
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fSpostaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Sposta Immagine", result);
        if (daDove.equals("FC") || daDove.equals("IR")) {
            ContinuaSpostamento();
        } else {
            if (!ritorno) {
                UtilitiesGlobali.getInstance().ApreToast(context, result);
            } else {
                UtilitiesGlobali.getInstance().ApreToast(context, "Immagine spostata");

                if (daDove.equals("SPOSTAMENTO")) {
                    if (VariabiliStatichePreview.getInstance().getLayTasti() != null) {
                        VariabiliStatichePreview.getInstance().getLayTasti().setVisibility(LinearLayout.GONE);
                        VariabiliStatichePreview.getInstance().getImgPreview().setImageBitmap(null);
                        VariabiliStatichePreview.getInstance().getTxtDescrizione().setText("Eliminata");
                        VariabiliStaticheSpostamento.getInstance().Attesa(false);
                        if (VariabiliStaticheSpostamento.getInstance().getAct() != null) {
                            VariabiliStaticheSpostamento.getInstance().getAct().finish();
                        }

                        UtilitiesPreview.getInstance().ProssimaImmagine(context);
                    }
                } else {
                    if (daDove.equals("PREVIEW")) {
                        VariabiliStatichePreview.getInstance().getLayTasti().setVisibility(LinearLayout.GONE);
                        VariabiliStatichePreview.getInstance().getImgPreview().setImageBitmap(null);
                        VariabiliStatichePreview.getInstance().getTxtDescrizione().setText("Eliminata");
                        VariabiliStaticheSpostamento.getInstance().Attesa(false);

                        UtilitiesPreview.getInstance().ProssimaImmagine(context);
                    } else {
                        if (daDove.equals("OCR")) {
                            ChiamateWSOCR ws = new ChiamateWSOCR(context);
                            ws.ImpostaImmagineSpostata();
                        }
                    }
                }

                /* if (daDove.equals("VO")) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ChiamateWSV ws = new ChiamateWSV(context);
                            ws.AggiornaVoltoSpostato(idImmagine);
                        }
                    }, 500);
                } */
            }
        }
    }

    private void ContinuaSpostamento() {
        switch (daDove) {
            case "IR":
                VariabiliStaticheImmaginiRaggruppate.getInstance().Attesa(false);
                int Quale = VariabiliStaticheImmaginiRaggruppate.getInstance().CercaProssimoNumeroDaSpostare(
                        VariabiliStaticheImmaginiRaggruppate.getInstance().getIdImmagineDaSpostare()
                );
                if (Quale > -1) {
                    VariabiliStaticheImmaginiRaggruppate.getInstance().setIdImmagineDaSpostare(Quale);
                    VariabiliStaticheImmaginiRaggruppate.getInstance().SpostaTutteLeImmagini(context);
                } else {
                    int quante = 0;
                    for (StrutturaImmagineRaggruppata s: VariabiliStaticheImmaginiRaggruppate.getInstance().getListaImmagini()) {
                        if (s.isSelezionata()) {
                            quante++;
                        }
                    }
                    UtilitiesGlobali.getInstance().ApreToast(context, "Immagini spostate: " + quante);

                    VariabiliStaticheImmaginiRaggruppate.getInstance().setStaSpostandoImmagini(false);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ChiamateWSIR ws = new ChiamateWSIR(context);
                            ws.RitornaRaggruppatePerFiltro(
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().getIdCategoria(),
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().getFiltro()
                            );
                        }
                    }, 500);
                }
                break;
            case "FC":
                VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);

                if (VariabiliImmaginiFuoriCategoria.getInstance().isStaSpostandoTutte()) {
                    int quale = VariabiliImmaginiFuoriCategoria.getInstance().getQualeImmagineStaSpostando();
                    int qualeProssima = quale + 1;
                    if (qualeProssima < VariabiliImmaginiFuoriCategoria.getInstance().getListaDaSpostare().size()) {
                        VariabiliImmaginiFuoriCategoria.getInstance().setQualeImmagineStaSpostando(qualeProssima);

                        VariabiliImmaginiFuoriCategoria.getInstance().ScaricaProssimaImmagine(context, qualeProssima);
                    } else {
                        VariabiliImmaginiFuoriCategoria.getInstance().setStaSpostandoTutte(false);
                        UtilitiesGlobali.getInstance().ApreToast(context, "Immagini spostate: " +
                                (VariabiliImmaginiFuoriCategoria.getInstance().getListaDaSpostare().size()));

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ChiamateWSIFC ws = new ChiamateWSIFC(context);
                                ws.RitornaImmaginiFuoriCategoria("S");
                            }
                        }, 500);
                    }
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Immagine spostata");

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ChiamateWSIFC ws = new ChiamateWSIFC(context);
                            ws.RitornaImmaginiFuoriCategoria("S");
                        }
                    }, 500);
                }
                break;
            case "IMMAGINI":
                UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
                break;
        }
    }

    private void fEliminaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Elimina Immagine", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (VariabiliStatichePreview.getInstance().getImgPreview() != null) {
                VariabiliStatichePreview.getInstance().getImgPreview().setImageBitmap(null);
            }
            if (VariabiliStatichePreview.getInstance().getTxtDescrizione() != null) {
                VariabiliStatichePreview.getInstance().getTxtDescrizione().setText("Eliminata");
            }
            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine eliminata");
            UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
        }
    }

    private void fRefreshImmagini(String result) {
        boolean ritorno = ControllaRitorno("Refresh Immagini", result);
        // if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        // }
    }

    private void fRitornaCategorie(String result) {
        boolean ritorno = ControllaRitorno("Ritorna categorie", result);
        if (ritorno) {
            try {
                List<StrutturaImmaginiCategorie> listaCategorie = new ArrayList<>();
                StrutturaImmaginiCategorie sicT = new StrutturaImmaginiCategorie();
                sicT.setIdCategoria(-1);
                sicT.setCategoria("Tutte");
                sicT.setAlias("");
                sicT.setTag("");

                listaCategorie.add(sicT);

                JSONArray jObject = new JSONArray(result);

                for(int i = 0; i < jObject.length(); i++){
                    JSONObject obj = jObject.getJSONObject(i);

                    StrutturaImmaginiCategorie sic = new StrutturaImmaginiCategorie();
                    sic.setIdCategoria(obj.getInt("idCategoria"));
                    sic.setCategoria(obj.getString("Categoria"));
                    sic.setAlias(obj.getString("Alias"));
                    sic.setTag(obj.getString("Tag"));

                    listaCategorie.add(sic);
                }

                VariabiliStaticheMostraImmagini.getInstance().setListaCategorie(listaCategorie);

                int idCategoriaImpostata = -1;
                if (VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata() != null) {
                    idCategoriaImpostata = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata().getIdCategoria();
                }
                String CategoriaAttuale = "";

                String[] l = new String[listaCategorie.size()];
                int c = 0;
                for (StrutturaImmaginiCategorie s : listaCategorie) {
                    l[c] = s.getCategoria();
                    if (s.getIdCategoria() == idCategoriaImpostata) {
                        CategoriaAttuale = s.getCategoria();
                    }
                    c++;
                }

                db_dati_immagini db = new db_dati_immagini(context);
                db.EliminaCategorie();
                for (StrutturaImmaginiCategorie s : listaCategorie) {
                    db.ScriveCategoria(s);
                }
                db.ChiudeDB();

                VariabiliStaticheMostraImmagini.getInstance().setCategoriaAttuale(CategoriaAttuale);
                VariabiliStaticheMostraImmagini.getInstance().setListaCategorieImm(l);

                if (daDove == null) {
                    daDove = "";
                }

                switch(daDove) {
                    case "IMMAGINI":
                        for (StrutturaImmaginiCategorie ca : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
                            if (ca.getCategoria().equals(VariabiliStaticheMostraImmagini.getInstance().getCategoriaAttuale())) {
                                VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(ca.getIdCategoria());
                                break;
                            }
                        }

                        UtilityImmagini.getInstance().AggiornaCategorie(context);
                        UtilityImmagini.getInstance().AggiornaCategorieSpostamento(context);
                        break;
                    case "OCR":
                        VariabiliStaticheOCR.getInstance().setListaCategorie(listaCategorie);
                        break;
                    case "PAZZIA":
                        VariabiliStatichePazzia.getInstance().setListaCategorieIMM(listaCategorie);
                        break;
                    case "UI":
                        List<StrutturaImmaginiCategorie> lista2 = db.LeggeCategorie();
                        for (StrutturaImmaginiCategorie l2: listaCategorie) {
                            if (!l2.getCategoria().toUpperCase().trim().equals("TUTTE")) {
                                lista2.add(l2);
                            }
                        }
                        VariabiliStaticheUtilityImmagini.getInstance().Attesa(false);
                        VariabiliStaticheUtilityImmagini.getInstance().setListaCategorieIMM(lista2);

                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.RitornaCategorieDiRicerca();
                        break;
                    case "IR":
                        List<StrutturaImmaginiCategorie> lista22 = db.LeggeCategorie();
                        for (StrutturaImmaginiCategorie l2: listaCategorie) {
                            if (!l2.getCategoria().toUpperCase().trim().equals("TUTTE")) {
                                lista22.add(l2);
                            }
                        }
                        VariabiliStaticheImmaginiRaggruppate.getInstance().Attesa(false);
                        VariabiliStaticheImmaginiRaggruppate.getInstance().setListaCategorieIMM(lista22);

                        String[] ll = new String[VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM().size() + 1];
                        int i = 0;
                        for (StrutturaImmaginiCategorie l2: VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM()) {
                            if (l2.getCategoria() != null) {
                                ll[i] = l2.getCategoria();
                            }
                            i++;
                        }
                        i = 0;
                        for (String lll: ll) {
                            if (lll == null || lll.isEmpty()) {
                                ll[i] = "**NULL**";
                            }
                            i++;
                        }
                        if (VariabiliStaticheImmaginiRaggruppate.getInstance().getIdCategoria() != null &&
                                !VariabiliStaticheImmaginiRaggruppate.getInstance().getIdCategoria().isEmpty()) {
                            for (StrutturaImmaginiCategorie l3: VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM()) {
                                if (l3.getIdCategoria() == Integer.parseInt(VariabiliStaticheImmaginiRaggruppate.getInstance().getIdCategoria())) {
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().setCategoriaImpostata(l3.getCategoria());
                                    break;
                                }
                            }
                        }
                        UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                VariabiliStaticheImmaginiRaggruppate.getInstance().getSpnCategorie(),
                                ll,
                                VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata()
                        );
                        break;
                    case "FC":
                    case "VO":
                        if (daDove.equals("FC")) {
                            VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
                        // } else {
                        //     VariabiliStaticheVolti.getInstance().Attesa(false);
                        }

                        List<StrutturaImmaginiCategorie> lista222 = db.LeggeCategorie();
                        for (StrutturaImmaginiCategorie l2: listaCategorie) {
                            if (!l2.getCategoria().toUpperCase().trim().equals("TUTTE")) {
                                lista222.add(l2);
                            }
                        }
                        VariabiliImmaginiFuoriCategoria.getInstance().setListaCategorieIMM(lista222);

                        String[] ll2 = new String[VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM().size() + 1];
                        int i2 = 0;
                        for (StrutturaImmaginiCategorie l2: VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                            if (l2.getCategoria() != null) {
                                ll2[i2] = l2.getCategoria();
                            }
                            i2++;
                        }
                        i = 0;
                        for (String lll: ll2) {
                            if (lll == null || lll.isEmpty()) {
                                ll2[i] = "**NULL**";
                            }
                            i++;
                        }
                        UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorie(),
                                ll2,
                                VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaInserita()
                        );

                        for (StrutturaImmaginiCategorie s : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                            if (s.getCategoria().toUpperCase().trim().equals(VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaInserita())) {
                                VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento(
                                        Integer.toString(s.getIdCategoria())
                                );
                                break;
                            }
                        }
                        break;
                    case "PREVIEW":
                        VariabiliStatichePreview.getInstance().setListaCategorie(listaCategorie);
                        String[] ll22 = new String[listaCategorie.size() + 1];
                        i = 0;
                        for (StrutturaImmaginiCategorie lll: listaCategorie) {
                            if (lll.getCategoria() != null && !lll.getCategoria().isEmpty()) {
                                ll22[i] = lll.getCategoria();
                            }
                            i++;
                        }
                        UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                VariabiliStatichePreview.getInstance().getSpnSpostaCategorie(),
                                ll22,
                                ""
                        );
                        break;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            if (daDove.equals("UI")) {
                VariabiliStaticheUtilityImmagini.getInstance().Attesa(false);
            }
        }
    }

    private void fProssimaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);
        if (ritorno) {
            try {
                JSONObject jObject = new JSONObject(result);
                StrutturaImmaginiLibrary si = UtilityImmagini.getInstance().prendeStruttura(jObject);
                VariabiliStaticheMostraImmagini.getInstance().setStrutturaImmagineAttuale(si);

                if (si != null) {
                    // VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(si.getIdCategoria());
                    VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(si.getIdImmagine());

                    switch (daDove) {
                        case "IMMAGINI":
                            UtilityImmagini.getInstance().AggiungeImmagine(context, result, si);

                            UtilityImmagini.getInstance().ScriveInfoSotto(si);

                            DownloadImmagineMI d = new DownloadImmagineMI();
                            d.EsegueChiamata(
                                    context, si.getUrlImmagine(),
                                    VariabiliStaticheMostraImmagini.getInstance().getImg(),
                                    si.getUrlImmagine(),
                                    false,
                                    false
                            );
                            break;
                        case "PAZZIA":
                            VariabiliStatichePazzia.getInstance().setUltimaImmagine(si.getIdImmagine());

                            DownloadImmaginePAZZIA d2 = new DownloadImmaginePAZZIA();
                            d2.EsegueChiamata(
                                    context,
                                    VariabiliStatichePazzia.getInstance().getImgImmagini(),
                                    si.getUrlImmagine(),
                                    "IMMAGINI"
                            );
                            break;
                    }
                    // new DownloadImageMI(context, si.getUrlImmagine(),
                    //         VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(si.getUrlImmagine());
                }
            } catch (JSONException e) {
                UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                        VariabiliStatichePazzia.getInstance().getImgCaricamentoIMM(),
                        false
                );
            }
            // Utility.getInstance().VisualizzaMessaggio(result);
        // } else {
        //     UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (daDove.equals("PAZZIA")) {
                UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                        VariabiliStatichePazzia.getInstance().getImgCaricamentoIMM(),
                        false
                );
            }

            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fProssimaImmagineWP(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);
        if (ritorno) {
            try {
                JSONObject jObject = new JSONObject(result);
                StrutturaImmaginiLibrary si = UtilityImmagini.getInstance().prendeStruttura(jObject);
                if (si != null) {
                    VariabiliStaticheMostraImmagini.getInstance().setImmaginePerWP(si);
                    // VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(si.getIdCategoria());
                    // VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(si.getIdImmagine());

                    // UtilityImmagini.getInstance().AggiungeImmagine(context, result, si);

                    DownloadImmagineMI d = new DownloadImmagineMI();
                    d.EsegueChiamata(
                            context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmagini.getInstance().getImg(),
                            si.getUrlImmagine(),
                            false,
                            true
                    );
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Immagine scaricata non valida");
                }
            } catch (JSONException e) {
                UtilitiesGlobali.getInstance().ApreToast(context, "Errore su cambio immagine: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
            // Utility.getInstance().VisualizzaMessaggio(result);
            // } else {
            //     UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
