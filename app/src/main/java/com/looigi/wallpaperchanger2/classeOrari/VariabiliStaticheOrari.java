package com.looigi.wallpaperchanger2.classeOrari;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;

import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheOrari {
    private static VariabiliStaticheOrari instance = null;

    private VariabiliStaticheOrari() {
    }

    public static VariabiliStaticheOrari getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheOrari();
        }

        return instance;
    }

    public static final String UrlWS = "http://www.wsorari.looigi.it/"; // "http://looigi.no-ip.biz:1071/";
    private Date dataAttuale;
    private int idUtente = 1;
    private GifImageView imgCaricamento;
    private StrutturaDatiGiornata datiGiornata;

    private LinearLayout layContenitore;
    private TextView txtTipoLavoro;
    private EditText edtOreLavoro;
    private EditText edtEntrata;
    private TextView txtLavoro;
    private TextView txtCommessa;
    private LinearLayout layAggiunge;
    private LinearLayout layDettaglioGiornata;
    private TextView txtTempo;
    private EditText edtGradi;
    private TextView txtPasticca;
    private EditText edtNote;
    private ListView lstPranzo;
    private ListView lstMezziAndata;
    private ListView lstMezziRitorno;

    public ListView getLstMezziAndata() {
        return lstMezziAndata;
    }

    public void setLstMezziAndata(ListView lstMezziAndata) {
        this.lstMezziAndata = lstMezziAndata;
    }

    public ListView getLstMezziRitorno() {
        return lstMezziRitorno;
    }

    public void setLstMezziRitorno(ListView lstMezziRitorno) {
        this.lstMezziRitorno = lstMezziRitorno;
    }

    public ListView getLstPranzo() {
        return lstPranzo;
    }

    public void setLstPranzo(ListView lstPranzo) {
        this.lstPranzo = lstPranzo;
    }

    public EditText getEdtNote() {
        return edtNote;
    }

    public void setEdtNote(EditText edtNote) {
        this.edtNote = edtNote;
    }

    public TextView getTxtPasticca() {
        return txtPasticca;
    }

    public void setTxtPasticca(TextView txtPasticca) {
        this.txtPasticca = txtPasticca;
    }

    public EditText getEdtGradi() {
        return edtGradi;
    }

    public void setEdtGradi(EditText edtGradi) {
        this.edtGradi = edtGradi;
    }

    public TextView getTxtTempo() {
        return txtTempo;
    }

    public void setTxtTempo(TextView txtTempo) {
        this.txtTempo = txtTempo;
    }

    public LinearLayout getLayDettaglioGiornata() {
        return layDettaglioGiornata;
    }

    public void setLayDettaglioGiornata(LinearLayout layDettaglioGiornata) {
        this.layDettaglioGiornata = layDettaglioGiornata;
    }

    public LinearLayout getLayAggiunge() {
        return layAggiunge;
    }

    public void setLayAggiunge(LinearLayout layAggiunge) {
        this.layAggiunge = layAggiunge;
    }

    public LinearLayout getLayContenitore() {
        return layContenitore;
    }

    public void setLayContenitore(LinearLayout layContenitore) {
        this.layContenitore = layContenitore;
    }

    public TextView getTxtCommessa() {
        return txtCommessa;
    }

    public void setTxtCommessa(TextView txtCommessa) {
        this.txtCommessa = txtCommessa;
    }

    public TextView getTxtLavoro() {
        return txtLavoro;
    }

    public void setTxtLavoro(TextView txtLavoro) {
        this.txtLavoro = txtLavoro;
    }

    public EditText getEdtEntrata() {
        return edtEntrata;
    }

    public void setEdtEntrata(EditText edtEntrata) {
        this.edtEntrata = edtEntrata;
    }

    public EditText getEdtOreLavoro() {
        return edtOreLavoro;
    }

    public void setEdtOreLavoro(EditText edtOreLavoro) {
        this.edtOreLavoro = edtOreLavoro;
    }

    public TextView getTxtTipoLavoro() {
        return txtTipoLavoro;
    }

    public void setTxtTipoLavoro(TextView txtTipoLavoro) {
        this.txtTipoLavoro = txtTipoLavoro;
    }

    public StrutturaDatiGiornata getDatiGiornata() {
        return datiGiornata;
    }

    public void setDatiGiornata(StrutturaDatiGiornata datiGiornata) {
        this.datiGiornata = datiGiornata;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public Date getDataAttuale() {
        return dataAttuale;
    }

    public void setDataAttuale(Date dataAttuale) {
        this.dataAttuale = dataAttuale;
    }
}
