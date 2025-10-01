package com.looigi.wallpaperchanger2.Player.WebServices;

public class StrutturaChiamateWSPlayer {
    private String Brano;
    private String NS;
    private int Timeout;
    private String SOAP_ACTION;
    private String tOperazione;
    private boolean ApriDialog;
    private String Urletto;
    private boolean Pregresso;

    public String getBrano() {
        return Brano;
    }

    public void setBrano(String brano) {
        Brano = brano;
    }

    public String getNS() {
        return NS;
    }

    public void setNS(String NS) {
        this.NS = NS;
    }

    public int getTimeout() {
        return Timeout;
    }

    public void setTimeout(int timeout) {
        Timeout = timeout;
    }

    public String getSOAP_ACTION() {
        return SOAP_ACTION;
    }

    public void setSOAP_ACTION(String SOAP_ACTION) {
        this.SOAP_ACTION = SOAP_ACTION;
    }

    public String gettOperazione() {
        return tOperazione;
    }

    public void settOperazione(String tOperazione) {
        this.tOperazione = tOperazione;
    }

    public boolean isApriDialog() {
        return ApriDialog;
    }

    public void setApriDialog(boolean apriDialog) {
        ApriDialog = apriDialog;
    }

    public String getUrletto() {
        return Urletto;
    }

    public void setUrletto(String urletto) {
        Urletto = urletto;
    }

    public boolean isPregresso() {
        return Pregresso;
    }

    public void setPregresso(boolean pregresso) {
        Pregresso = pregresso;
    }
}
