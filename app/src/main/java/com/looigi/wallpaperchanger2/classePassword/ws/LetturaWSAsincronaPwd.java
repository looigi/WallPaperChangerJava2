package com.looigi.wallpaperchanger2.classePassword.ws;

import android.content.Context;
import android.os.AsyncTask;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePassword.UtilityPassword;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class LetturaWSAsincronaPwd extends AsyncTask<String, Integer, String>  {
    private static final String NomeMaschera = "Lettura_Asincrona_PWD";
    private final String NAMESPACE;
    private String METHOD_NAME = "";
    private String[] Parametri;
    private final Integer Timeout;
    private String SOAP_ACTION;
    private Boolean Errore;
    private String result="";
    private String Urletto;
    private Integer QuantiTentativi;
    private Integer Tentativo;
    private String messErrore="";
    private final String tOperazione;
    private final String TimeStampAttuale;
    private TaskDelegate delegate;
    private boolean ApriDialog;
    private Context context;

    public LetturaWSAsincronaPwd(Context context, String NAMESPACE, int TimeOut,
                                 String SOAP_ACTION, String tOperazione,
                                 boolean ApriDialog, String Urletto,
                                 String TimeStampAttuale,
                                 TaskDelegate delegate) {
        this.context = context;
        this.NAMESPACE = NAMESPACE;
        this.Timeout = TimeOut;
        this.SOAP_ACTION = SOAP_ACTION;
        this.Urletto = Urletto;
        this.tOperazione = tOperazione;
        this.messErrore = "";
        this.ApriDialog = ApriDialog;
        this.TimeStampAttuale = TimeStampAttuale;
        this.QuantiTentativi = 3;
        this.Tentativo = 0;
        this.delegate = delegate;
    }

    private void SplittaCampiUrletto(String Cosa) {
        String Perc=Cosa;
        int pos;
        String Indirizzo="";
        String[] Variabili;
        String Funzione="";

        pos=Perc.indexOf("?");
        if (pos>-1) {
            Indirizzo=Perc.substring(0, pos);
            for (int i=Indirizzo.length()-1;i>0;i--) {
                if (Indirizzo.substring(i, i+1).equals("/")) {
                    Funzione=Indirizzo.substring(i+1, Indirizzo.length());
                    Indirizzo=Indirizzo.substring(0, i);
                    break;
                }
            }
            Urletto=Indirizzo;
            METHOD_NAME = Funzione;
            SOAP_ACTION = NAMESPACE + Funzione;
            Perc=Perc.substring(pos+1, Perc.length());
            pos=Perc.indexOf("&");
            if (pos>-1) {
                Variabili=Perc.split("&",-1);
            } else {
                Variabili=new String[1];
                Variabili[0]=Perc;
            }
            Parametri=Variabili;
        } else {
            Indirizzo=Perc;
            for (int i=Indirizzo.length()-1;i>0;i--) {
                if (Indirizzo.substring(i, i+1).equals("/")) {
                    Funzione=Indirizzo.substring(i+1, Indirizzo.length());
                    Indirizzo=Indirizzo.substring(0, i);
                    break;
                }
            }
            Urletto=Indirizzo;
            METHOD_NAME = Funzione;
            SOAP_ACTION = NAMESPACE + Funzione;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        UtilityPassword.getInstance().ApriDialog(context, ApriDialog, this.tOperazione);
        SplittaCampiUrletto(this.Urletto);
    }

    @Override
    protected void onPostExecute(String p) {
        super.onPostExecute(p);

        ControllaFineCiclo();
    }

    @Override
    protected String doInBackground(String... strings) {
        /* if (!VariabiliGlobali.getInstance().isRetePresente()) {
            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Operazione di rete " + tOperazione + " non possibile: Rete non presente");
            Errore = true;
            result = "ERROR: Rete non presente";
        } else { */
            Errore = false;
            result = "";

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);

            String Parametro = "";
            String Valore = "";

            if (Parametri != null) {
                for (int i = 0; i < Parametri.length; i++) {
                    if (Parametri[i] != null) {
                        int pos = Parametri[i].indexOf("=");
                        if (pos > -1) {
                            Parametro = Parametri[i].substring(0, pos);
                            Valore = Parametri[i].substring(pos + 1, Parametri[i].length());
                        }
                        Request.addProperty(Parametro, Valore);
                        UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Parametro " + Parametro + ": " + Valore);
                    }
                }
            }

            // Aggiunta Timestamp per riscontro chiamata
            /* Parametro = "TimeStampAttuale";
            Valore = this.TimeStampAttuale;
            Request.addProperty(Parametro, Valore); */
            // Aggiunta Timestamp per riscontro chiamata

            SoapSerializationEnvelope soapEnvelope = null;
            HttpTransportSE aht = null;
            messErrore = "";

            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Urletto: " + Urletto);

            try {
                soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                aht = new HttpTransportSE(Urletto, Timeout);
                aht.call(SOAP_ACTION, soapEnvelope);

                if (isCancelled()) {
                    messErrore = "ESCI";
                }
            } catch (SocketTimeoutException e) {
                if (tOperazione.equals("RitornaFiles")) {
                    Errore = false;
                } else {
                    Errore = true;
                    messErrore = UtilityDetector.getInstance().PrendeErroreDaException(e);
                    if (messErrore != null) {
                        messErrore = messErrore.toUpperCase().replace("LOOIGI.NO-IP.BIZ", "Web Service");
                    } else {
                        messErrore = "Unknown";
                    }
                    result = "ERROR: " + messErrore;
                    messErrore = result;

                    UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Errore di socket su ws per operazione " + tOperazione + ": " + messErrore);
                }
            } catch (IOException e) {
                Errore = true;
                messErrore = UtilityDetector.getInstance().PrendeErroreDaException(e);
                if (messErrore != null)
                    messErrore = messErrore.toUpperCase().replace("LOOIGI.NO-IP.BIZ", "Web Service");
                result = "ERROR: " + messErrore;
                messErrore = result;

                UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Errore di I/O su ws per operazione " + tOperazione + ": " + messErrore);
            } catch (XmlPullParserException e) {
                Errore = true;
                messErrore = UtilityDetector.getInstance().PrendeErroreDaException(e);
                if (messErrore != null) {
                    messErrore = messErrore.toUpperCase().replace("LOOIGI.NO-IP.BIZ", "Web Service");
                } else {
                    messErrore = "Unknown";
                }
                result = "ERRORE: " + messErrore;
                messErrore = result;

                UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Errore di parsing su ws per operazione " + tOperazione + ": " + messErrore);
            } catch (Exception e) {
                Errore = true;
                messErrore = UtilityDetector.getInstance().PrendeErroreDaException(e);
                if (messErrore != null)
                    messErrore = messErrore.toUpperCase().replace("LOOIGI.NO-IP.BIZ", "Web Service");
                result = "ERROR: " + messErrore;
                messErrore = result;

                UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Errore generico su ws per operazione " + tOperazione + ": " + messErrore);
            }
            if (!Errore && !isCancelled()) {
                try {
                    result = "" + soapEnvelope.getResponse();

                    UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Lettura ok su ws per operazione " + tOperazione);
                } catch (SoapFault e) {
                    Errore = true;
                    messErrore = UtilityDetector.getInstance().PrendeErroreDaException(e);
                    if (messErrore != null) {
                        messErrore = messErrore.toUpperCase().replace("LOOIGI.NO-IP.BIZ", "Web Service");
                    } else {
                        messErrore = "Unknown";
                    }
                    result = "ERROR: " + messErrore;
                    messErrore = result;

                    UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Errore SoapFault su ws per operazione " + tOperazione + ": " + messErrore);
                }
            } else {
                int a = 0;
            }
            if (aht != null) {
                aht = null;
            }
            if (soapEnvelope != null) {
                soapEnvelope = null;
            }
            if (isCancelled()) {
                messErrore = "ESCI";

                UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Richiesta uscita da ws su operazione " + tOperazione + ": " + messErrore);
            }
        // }

        return null;
    }

    private void ControllaFineCiclo() {
        UtilityPassword.getInstance().ChiudeDialog();

        if (!messErrore.equals("ESCI")) {
            String Ritorno = result;

            if (Ritorno.contains("ERROR:")) {
                messErrore = Ritorno;
                Errore = true;

                // if (VariabiliGlobali.getInstance().isRetePresente()) {
                    // OggettiAVideo.getInstance().getImgIndietro().setVisibility(LinearLayout.VISIBLE);
                    // OggettiAVideo.getInstance().getImgAvanti().setVisibility(LinearLayout.VISIBLE);
                    // OggettiAVideo.getInstance().getImgPlay().setVisibility(LinearLayout.VISIBLE);

                    UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "ERRORE Su Lettura Asincrona: " + messErrore);

                    /* if (VariabiliGlobali.getInstance().isScreenOn()) {
                        DialogMessaggio.getInstance().show(
                                VariabiliGlobali.getInstance().getContext(),
                                messErrore,
                                true,
                                VariabiliGlobali.getInstance().getNomeApplicazione(),
                                false,
                                "");
                    } */

                    delegate.TaskCompletionResult(result);
                // }
            } else {
                UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Avviso per termine chiamata su operazione " + tOperazione);

                delegate.TaskCompletionResult(result);
            }
        } else {
            // Uscito

            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Uscita da ws su operazione " + tOperazione);

            delegate.TaskCompletionResult(result);
        }
    }
}