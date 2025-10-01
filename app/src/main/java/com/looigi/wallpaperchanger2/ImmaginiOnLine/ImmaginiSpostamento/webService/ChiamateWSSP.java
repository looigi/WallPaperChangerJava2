package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.Pennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.VariabiliStaticheSpostamento;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.db_dati_spostamento;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.strutture.StrutturaCategorieSpostamento;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;


public class ChiamateWSSP implements TaskDelegateSP {
    private static final String NomeMaschera = "Chiamate_WS_Immagini_SP";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmaginiPennetta.UrlWS;
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;

    public ChiamateWSSP(Context context) {
        this.context = context;
    }

    public void TornaInformazioniImmagineDaID(String idImmagine) {
        VariabiliStaticheSpostamento.getInstance().Attesa(true);

        String Urletto="TornaInfoImmaginiDaId?idImmagine=" + idImmagine;

        TipoOperazione = "TornaInfoImmaginiDaId";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                250000,
                ApriDialog);
    }

    public void TornaCategoriePerImmaginiContenute(boolean Refresh) {
        VariabiliStaticheSpostamento.getInstance().Attesa(true);

        if (!Refresh) {
            db_dati_spostamento db = new db_dati_spostamento(context);
            db.LeggeCategorie();

            if (!VariabiliStaticheSpostamento.getInstance().getListaCategorie().isEmpty()) {
                VariabiliStaticheSpostamento.getInstance().AggiornaCategorieSpostamento(context);

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        ChiamateWSSP c = new ChiamateWSSP(context);
                        c.TornaInformazioniImmagineDaID(
                                VariabiliStaticheSpostamento.getInstance().getIdImmagine()
                        );
                    }
                };
                handlerTimer.postDelayed(rTimer, 100);

                return;
            }
        }
        String Urletto="TornaCategoriePerImmaginiContenute";

        TipoOperazione = "TornaCategoriePerImmaginiContenute";
        // ControllaTempoEsecuzione = false;

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
        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSSP i = new InterrogazioneWSSP();
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
                VariabiliStaticheSpostamento.getInstance().Attesa(false);

                switch (TipoOperazione) {
                    case "TornaCategoriePerImmaginiContenute":
                        fTornaCategoriePerImmaginiContenute(result);
                        break;
                    case "TornaInfoImmaginiDaId":
                        fTornaInfoImmaginiDaId(result);
                        break;
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }

    private boolean ControllaRitorno(String Operazione, String result) {
        if (result.contains("ERROR:")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
            }

            return false;
        } else {
            if (result.contains("anyType{}")) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void fTornaInfoImmaginiDaId(String result) {
        boolean ritorno = ControllaRitorno("Ritorna TornaInfoImmaginiDaId", result);

        if (VariabiliStaticheSpostamento.getInstance().getTxtSpostamento() != null) {
            VariabiliStaticheSpostamento.getInstance().getTxtSpostamento().setText("");
            VariabiliStaticheSpostamento.getInstance().getImgSpostamento().setImageBitmap(null);
        }

        if (ritorno) {
            // Rec("Categoria").Value & ";" & Rec("Cartella").Value & ";" & Rec("NomeFile").Value & ";" &
            //         Rec("idCategoria").Value & ";" & Rec("DimensioneFile").Value & ";" & Rec("DataCreazione").Value & ";" &
            //         Rec("DataModifica").Value & ";" & Rec("DimensioniImmagine").Value & ";" & Rec("Exif").Value

            String[] campi = result.split(";");
            String NomeImmagine = campi[2];
            String UrlImmagine = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaDiscoPublic +
                    "/Materiale/newPLibrary/" +
                    campi[0] + "/" + campi[1] + "/" + campi[2];

            String Testo = "idImmagine: " + VariabiliStaticheSpostamento.getInstance().getIdImmagine() +
                    " - Immagine: " + campi[2] + " - Categoria: " + campi[0] + " - Cartella " + campi[1] +
                    " - Dimensioni " + campi[7] + " - Bytes: " + campi[4];
            if (VariabiliStaticheSpostamento.getInstance().getTxtSpostamento() != null) {
                VariabiliStaticheSpostamento.getInstance().getTxtSpostamento().setText(Testo);
            }

            DownloadImmagineSpostamento d = new DownloadImmagineSpostamento();
            d.EsegueChiamata(
                    context,
                    NomeImmagine,
                    VariabiliStaticheSpostamento.getInstance().getImgSpostamento(),
                    UrlImmagine
            );
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fTornaCategoriePerImmaginiContenute(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Categorie per immagini contenute", result);
        if (ritorno) {
            // Rec("idCategoria").Value & ";" & Rec("Categoria").Value & ";" & Rec("quante").Value & "§"
            String[] righe = result.split("§");
            List<StrutturaCategorieSpostamento> lista = new ArrayList<>();

            for (String r: righe) {
                if (!r.isEmpty()) {
                    String[] campi = r.split(";");

                    StrutturaCategorieSpostamento s = new StrutturaCategorieSpostamento();
                    s.setIdCategoria(Integer.parseInt(campi[0]));
                    s.setCategoria(campi[1]);
                    s.setImmaginiContenute(Integer.parseInt(campi[2]));

                    lista.add(s);
                }
            }
            VariabiliStaticheSpostamento.getInstance().setListaCategorie(lista);
            VariabiliStaticheSpostamento.getInstance().AggiornaCategorieSpostamento(context);

            db_dati_spostamento db = new db_dati_spostamento(context);
            db.ScriveCategorie();

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    ChiamateWSSP c = new ChiamateWSSP(context);
                    c.TornaInformazioniImmagineDaID(
                            VariabiliStaticheSpostamento.getInstance().getIdImmagine()
                    );
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
