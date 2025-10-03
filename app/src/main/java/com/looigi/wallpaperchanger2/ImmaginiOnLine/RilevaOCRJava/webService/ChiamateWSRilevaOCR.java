package com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.GoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.Pennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.GestioneNotificheOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.UtilitiesRilevaOCRJava;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.VariabiliStaticheRilevaOCRJava;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.strutture.StrutturaRilevaOCR;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ChiamateWSRilevaOCR implements TaskDelegateRilevaOCR {
    private static final String NomeMaschera = "Chiamate_WS_RILEVA_OCR";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmaginiPennetta.UrlWS;
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private String daDove;

    public ChiamateWSRilevaOCR(Context context) {
        this.context = context;
    }

    public void RitornaProssimaImmagineDaLeggereInJava() {
        String Urletto="RitornaProssimaImmagineDaLeggereInJava?" +
                "ID=" + VariabiliStaticheStart.getInstance().getModelloTelefono().getANDROID_ID();

        TipoOperazione = "RitornaProssimaImmagineDaLeggereInJava";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                100000,
                ApriDialog);
    }

    private String DaDove;

    public void AggiornaTestoOcrDaJava(String CategorieMesse, String Tags, String daDove) {
        DaDove = daDove;

        if (CategorieMesse == null || CategorieMesse.isEmpty()) {
            CategorieMesse = ";";
        }
        if (Tags == null || Tags.isEmpty()) {
            Tags = ";";
        }

        String encoded = null;
        String encodedTags = null;

        try {
            if (CategorieMesse.equals(";") || CategorieMesse.length() < 3) {
                encoded = ";";
            } else {
                encoded = URLEncoder.encode(CategorieMesse, StandardCharsets.UTF_8.toString());
            }
            if (Tags.equals(";") || Tags.length() < 3) {
                encodedTags = ";";
            } else {
                encodedTags = URLEncoder.encode(Tags, StandardCharsets.UTF_8.toString());
            }

            String Urletto="AggiornaTestoOcrDaJava?" +
                    "idImmagine=" + VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale().getIdImmagine() +
                    "&Testo=" + encoded +
                    "&Tags=" + encodedTags;

            TipoOperazione = "AggiornaTestoOcrDaJava";
            // ControllaTempoEsecuzione = false;

            Esegue(
                    RadiceWS + ws + Urletto,
                    TipoOperazione,
                    NS,
                    SA,
                    100000,
                    ApriDialog);
        } catch (UnsupportedEncodingException e) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                    ws.AggiornaTestoOcrDaJava("ERRORE NELL'ENCODING;", ";", DaDove);
                }
            }, 10);
        }
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheRilevaOCRJava.getInstance().getImgCaricamento(),
                true
        );

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSRilevaOCR i = new InterrogazioneWSRilevaOCR();
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
                UtilitiesGlobali.getInstance().AttesaGif(
                        context,
                        VariabiliStaticheRilevaOCRJava.getInstance().getImgCaricamento(),
                        false
                );

                switch (TipoOperazione) {
                    case "RitornaProssimaImmagineDaLeggereInJava":
                        fRitornaProssimaImmagineDaLeggereInJava(result);
                        break;
                    case "AggiornaTestoOcrDaJava":
                        fAggiornaTestoOcrDaJava(result);
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

    private void fAggiornaTestoOcrDaJava(String result) {
        if (!DaDove.equals("OCR")) {
            return;
        }

        if (!VariabiliStaticheRilevaOCRJava.getInstance().isStaElaborando()) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione bloccata");
        } else {
            boolean ritorno = ControllaRitorno("Ritorna AggiornaTestoOcrDaJava", result);
            if (ritorno) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                        ws.RitornaProssimaImmagineDaLeggereInJava();
                    }
                }, 10);
            } else {
                UtilitiesGlobali.getInstance().ApreToast(context, result);
            }
        }
    }

    private void fRitornaProssimaImmagineDaLeggereInJava(String result) {
        if (!VariabiliStaticheRilevaOCRJava.getInstance().isStaElaborando()) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione bloccata");
        } else {
            boolean ritorno = ControllaRitorno("Ritorna Ritorna Prossima Immagine Da Leggere In Java", result);
            if (ritorno) {
                String[] c = result.split(";", -1);
                StrutturaRilevaOCR s = new StrutturaRilevaOCR();
                s.setIdImmagine(c[0]);

                /* if (VariabiliStaticheRilevaOCRJava.getInstance().getIdUltimaImmagine().trim().equals(c[0].trim())) {
                    VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(false);

                    UtilityWallpaper.getInstance().ApriDialog(
                            true,
                            "Stesso id della immagine precedente: " + c[0]
                    );

                    return;
                }
                VariabiliStaticheRilevaOCRJava.getInstance().setIdUltimaImmagine(c[0]); */

                s.setTesto(c[1].replace("*PV*", ";"));
                s.setQuante(c[2]);
                s.setCategoria(c[3]);
                s.setCartella(c[4]);
                s.setNomeFile(c[5]);
                String url = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaDiscoPublic + "/Materiale/newPLibrary/" +
                        c[3] + "/" + c[4] + "/" + c[5];
                s.setUrl(url);
                s.setTestoJava(c[6].replace("*PV*", ";"));
                s.setTags(c[7].replace("*PV*", ";"));
                s.setInizio(c[8]);
                s.setQualeEmulatore(c[9]);
                s.setEmulatori(c[10]);
                s.setID(c[11]);

                VariabiliStaticheRilevaOCRJava.getInstance().getTxtAvanzamento().setText(
                        "Rimanenti: " + c[2] + " - " + c[5] + " (" + c[3] + ")\nIn. " + c[8] + " Em. " + c[9] + "/" + c[10] + " (" + c[11] + ")"
                );

                VariabiliStaticheRilevaOCRJava.getInstance().setMessaggioNotifica(
                        "Rim.: " + c[2] + " In.  " + c[8] + " Em. " + c[9] + "/" + c[10] + " (" + c[11] + ")"
                );
                int conta = VariabiliStaticheRilevaOCRJava.getInstance().getContatore();
                conta++;
                if (conta > 9) {
                    GestioneNotificheOCR.getInstance().AggiornaNotifica();
                    conta = 0;
                }
                VariabiliStaticheRilevaOCRJava.getInstance().setContatore(conta);

                VariabiliStaticheRilevaOCRJava.getInstance().setImmagineAttuale(s);
                UtilitiesRilevaOCRJava.getInstance().DisegnaImmagine(context);
            } else {
                UtilitiesGlobali.getInstance().ApreToast(context, result);
            }
        }
    }
}
