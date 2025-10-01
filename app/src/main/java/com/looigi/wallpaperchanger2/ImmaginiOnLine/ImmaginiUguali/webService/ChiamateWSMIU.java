package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.InterrogazioneWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.TaskDelegate;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.adapters.AdapterListenerImmaginiUguali;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.StrutturaImmaginiUguali;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.StrutturaImmaginiUgualiRitornate;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.VariabiliImmaginiUguali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSMIU implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Immagini_Uguali";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmagini.UrlWS + "/";
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private Context context;
    private boolean ApriDialog = false;
    private String idImmagine;

    public ChiamateWSMIU(Context context) {
        this.context = context;
    }

    public void RitornaImmaginiUguali(String Categoria) {
        String Urletto="ImmaginiUgualiMobile?" +
                "Categoria=" + Categoria +
                "&ForzaRefresh=N";

        TipoOperazione = "ImmaginiUgualiMobile";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void EliminaImmagine(String idImmagine) {
        String Urletto="EliminaImmagine?" +
                "idImmagine=" + idImmagine;

        TipoOperazione = "EliminaImmagine";
        this.idImmagine = idImmagine;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void RitornaImmaginiUgualiFiltro(String Categoria, String Tipo, String Filtro) {
        String Urletto="ImmaginiUgualiMobileTorna?" +
                "Categoria=" + Categoria +
                "&Tipo=" + Tipo +
                "&Filtro=" + Filtro;

        TipoOperazione = "ImmaginiUgualiMobileTorna";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        VariabiliImmaginiUguali.getInstance().getImgCaricamentoInCorso().setVisibility(LinearLayout.VISIBLE);

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
                    case "ImmaginiUgualiMobile":
                        fImmaginiUgualiMobile(result);
                        break;
                    case "ImmaginiUgualiMobileTorna":
                        fImmaginiUgualiMobileTorna(result);
                        break;
                    case "EliminaImmagine":
                        fEliminaImmagine(result);
                        break;
                }

                VariabiliImmaginiUguali.getInstance().getImgCaricamentoInCorso().setVisibility(LinearLayout.GONE);
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

    private void fEliminaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Elimina immagini", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaImmaginiUgualiRitornate> lista = new ArrayList<>();

            for (StrutturaImmaginiUgualiRitornate s : VariabiliImmaginiUguali.getInstance().getLista2()) {
                if (s.getIdImmagine() != Integer.parseInt(idImmagine)) {
                    lista.add(s);
                }
            }
            VariabiliImmaginiUguali.getInstance().setLista2(lista);

            AdapterListenerImmaginiUguali customAdapterT = new AdapterListenerImmaginiUguali(
                    context,
                    lista
            );
            VariabiliImmaginiUguali.getInstance().getLstImmagini().setAdapter(customAdapterT);
        }
    }

    private void fImmaginiUgualiMobile(String result) {
        boolean ritorno = ControllaRitorno("Ritorna immagini uguali", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaImmaginiUguali> lista = new ArrayList<>();
            String[] r = result.split("§");
            for (String rr : r) {
                if (!rr.isEmpty()) {
                    try {
                        String[] rrr = rr.split(";");
                        StrutturaImmaginiUguali s = new StrutturaImmaginiUguali();
                        s.setTipo(rrr[0]);
                        s.setQuanti(Integer.parseInt(rrr[1]));
                        s.setFiltro(rrr[2]);
                        lista.add(s);
                    } catch (Exception ignored) {

                    }
                }
            }
            VariabiliImmaginiUguali.getInstance().setLista(lista);

            VariabiliImmaginiUguali.getInstance().RiempieListaTipi(context);
        }
    }

    private void fImmaginiUgualiMobileTorna(String result) {
        boolean ritorno = ControllaRitorno("Ritorna immagini uguali torna", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaImmaginiUgualiRitornate> lista = new ArrayList<>();
            String[] r = result.split("§");
            for (String rr : r) {
                if (!rr.isEmpty()) {
                    String[] rrr = rr.split(";");
                    StrutturaImmaginiUgualiRitornate s = new StrutturaImmaginiUgualiRitornate();
                    s.setIdImmagine(Integer.parseInt(rrr[0]));
                    s.setCartella(rrr[1]);
                    s.setNomeFile(rrr[2]);
                    s.setDimensioneFile(rrr[3]);
                    s.setDimensioneImmagine(rrr[4]);

                    lista.add(s);
                }
            }
            VariabiliImmaginiUguali.getInstance().setLista2(lista);

            AdapterListenerImmaginiUguali customAdapterT = new AdapterListenerImmaginiUguali(
                    context,
                    lista
            );
            VariabiliImmaginiUguali.getInstance().getLstImmagini().setAdapter(customAdapterT);
        }
    }
}
