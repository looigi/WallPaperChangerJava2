package com.looigi.wallpaperchanger2.classeVideo.webservice;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeFetekkie.VariabiliStaticheMostraImmaginiFetekkie;
import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classePazzia.UtilityPazzia;
import com.looigi.wallpaperchanger2.classePazzia.VariabiliStatichePazzia;
import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classeVideo.UtilityVideo;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classeVideo.db_dati_video;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ChiamateWSV implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Video";
    //private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheVideo.UrlWS + "/";
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private GifImageView imgAttesa;
    private String daDove;

    public ChiamateWSV(Context context) {
        this.context = context;
    }

    public void ImpostaImgAttesa(GifImageView imgAttesa) {
        this.imgAttesa = imgAttesa;
    }

    public void RitornaProssimoVideo(String daDove) {
        this.daDove = daDove;

        String Urletto = "";
        String Categoria = "";

        switch (daDove) {
            case "VIDEO":
                String Filtro = VariabiliStaticheVideo.getInstance().getFiltro();
                Categoria = VariabiliStaticheVideo.getInstance().getCategoria();

                Urletto = "RitornaProssimoVideo?" +
                        "Categoria=" + Categoria.replace("\\", "§") +
                        "&Filtro=" + Filtro +
                        "&Random=" + VariabiliStaticheVideo.getInstance().getRandom() +
                        "&pUltimoVideo=" + VariabiliStaticheVideo.getInstance().getIdUltimoVideo() +
                        "&OrdinaPerVisualizzato=" + (VariabiliStaticheVideo.getInstance().isRicercaPerVisua() ? "S" : "N");
                break;
            case "PAZZIA":
                UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                        VariabiliStatichePazzia.getInstance().getImgCaricamentoVID(),
                        true
                );

                Categoria = VariabiliStatichePazzia.getInstance().getCategoriaVideo();
                if (Categoria.toUpperCase().trim().equals("TUTTE")) {
                    Categoria = "";
                }

                Urletto="RitornaProssimoVideo?" +
                        "Categoria=" + Categoria.replace("\\", "§") +
                        "&Filtro=" +
                        "&Random=S" +
                        "&pUltimoVideo=" + VariabiliStatichePazzia.getInstance().getUltimoVideo() +
                        "&OrdinaPerVisualizzato=S";
                break;
        }

        TipoOperazione = "RitornaProssimoVideo";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                35000,
                ApriDialog);
    }

    public void RitornaCategorie(boolean forzaLettura, String daDove) {
        this.daDove = daDove;

        if (!forzaLettura) {
            db_dati_video db = new db_dati_video(context);
            List<String> lista = db.LeggeCategorie();
            db.ChiudeDB();

            if (!lista.isEmpty()) {
                switch (daDove) {
                    case "VIDEO":
                        VariabiliStaticheVideo.getInstance().setListaCategorie(lista);
                        UtilityVideo.getInstance().AggiornaCategorie(context);
                        UtilityVideo.getInstance().AggiornaCategorieSpostamento(context);
                        break;
                    case "PAZZIA":
                        VariabiliStatichePazzia.getInstance().setListaCategorieVID(lista);
                        break;
                }

                return;
            }
        }

        String Urletto="RitornaCategorieVideo";

        TipoOperazione = "RitornaCategorie";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                35000,
                ApriDialog);
    }

    public void EliminaVideo(String id) {
        String Urletto="EliminaVideo?" +
                "idVideo=" + id;

        TipoOperazione = "EliminaVideo";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void SpostaVideo() {
        String Urletto="SpostaVideo?" +
                "Categoria=" + VariabiliStaticheVideo.getInstance().getCategoria() +
                "&idVideo=" + VariabiliStaticheVideo.getInstance().getIdUltimoVideo() +
                "&NuovaCategoria=" + VariabiliStaticheVideo.getInstance().getIdCategoriaSpostamento();

        TipoOperazione = "SpostaVideo";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void RefreshVideo(String Categoria) {
        String Urletto="RefreshVideo?" +
                "Categoria=" + Categoria +
                "&Completo=" + (VariabiliStaticheVideo.getInstance().isAggiornamentoCompleto() ? "S" : "");

        TipoOperazione = "RefreshVideo";
        // ControllaTempoEsecuzione = false;

        UtilitiesGlobali.getInstance().ApreToast(context, "Refresh video lanciato");

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

        UtilityVideo.getInstance().ScriveLog(context, NomeMaschera, "Chiamata WS " + TipoOperazione + ". OK");
        if (VariabiliStaticheVideo.getInstance().getPbLoading() != null) {
            VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(LinearLayout.VISIBLE);
        }

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSV i = new InterrogazioneWSV();
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
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                UtilityVideo.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");
                if (VariabiliStaticheVideo.getInstance().getPbLoading() != null) {
                    VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(LinearLayout.GONE);
                }

                switch (TipoOperazione) {
                    case "RitornaProssimoVideo":
                        fRitornaProssimoVideo(result);
                        break;
                    case "RitornaCategorie":
                        fRitornaCategorie(result);
                        break;
                    case "RefreshVideo":
                        fRefreshVideo(result);
                        break;
                    case "SpostaVideo":
                        fSpostaVideo(result);
                        break;
                    case "EliminaVideo":
                        fEliminaVideo(result);
                        break;
                }

                if (imgAttesa != null) {
                    imgAttesa.setVisibility(LinearLayout.GONE);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }

    private void fEliminaVideo(String result) {
        boolean ritorno = ControllaRitorno("Elimina Video", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Video eliminato");

            ChiamateWSV ws = new ChiamateWSV(context);
            ws.RitornaProssimoVideo("VIDEO");
        }
    }

    private void fSpostaVideo(String result) {
        boolean ritorno = ControllaRitorno("Sposta Video", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Video spostato");
            UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
        }
    }

    private void fRefreshVideo(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Refresh Video", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
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

    private void fRitornaCategorie(String result) {
        boolean ritorno = ControllaRitorno("Ritorna categorie", result);
        if (ritorno) {
            String[] lista = result.split("§");
            List<String> l = new ArrayList<>();
            l.add("Tutte");
            l.addAll(Arrays.asList(lista));

            db_dati_video db = new db_dati_video(context);
            db.EliminaCategorie();
            for (String s : l) {
                db.ScriveCategoria(s);
            }
            db.ChiudeDB();

            VariabiliStaticheVideo.getInstance().setListaCategorie(l);

            switch(daDove) {
                case "VIDEO":
                    UtilityVideo.getInstance().AggiornaCategorie(context);
                    UtilityVideo.getInstance().AggiornaCategorieSpostamento(context);
                    break;
                case "PAZZIA":
                    VariabiliStatichePazzia.getInstance().setListaCategorieVID(l);
                    break;
            }
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fRitornaProssimoVideo(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossimo video", result);
        if (ritorno) {
            String url ="";
            int videoFiltrati = -1;
            int videoCategoria = -1;
            String Nome = "";

            int id = -1;
            if (result.contains("§")) {
                String[] p = result.split("§");
                url = VariabiliStaticheVideo.PathUrl + p[0];
                id = Integer.parseInt(p[1]);
                videoFiltrati = Integer.parseInt(p[2]);
                videoCategoria = Integer.parseInt(p[3]);
                // Nome = p[0];
            } else {
                url = VariabiliStaticheVideo.PathUrl + result;
            }

            VariabiliStaticheVideo.getInstance().setUltimoLink(url);
            VariabiliStaticheVideo.getInstance().setIdUltimoVideo(id);

            /* String[] u = result.split("/");
            String res = u[u.length -1];
            if (res.contains("§")) {
                String[] r = res.split("§");
                res = r[0];
            }
            res = VariabiliStaticheVideo.getInstance().getIdUltimoVideo() + ": " + res;
            VariabiliStaticheVideo.getInstance().getTxtTitolo().setText(res); */

            switch (daDove) {
                case "PAZZIA":
                    VariabiliStatichePazzia.getInstance().setUltimoVideo(id);

                    UtilityPazzia.getInstance().ImpostaVideo(context);
                    break;
                case "VIDEO":
                    if (videoFiltrati == videoCategoria) {
                        VariabiliStaticheVideo.getInstance().getTxtInfoSotto().setText(
                                "Video in Categoria " + videoFiltrati + ". " + Nome
                        );
                    } else {
                        VariabiliStaticheVideo.getInstance().getTxtInfoSotto().setText(
                                "Video Filtrati " + videoFiltrati + "/" + videoCategoria +  ". " + Nome
                        );
                    }

                    VariabiliStaticheVideo.getInstance().ScriveImmagini(url);

                    db_dati_video db = new db_dati_video(context);
                    db.ScriveUltimoVideo();
                    db.ChiudeDB();

                    UtilityVideo.getInstance().ImpostaVideo();
                    break;
            }
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
