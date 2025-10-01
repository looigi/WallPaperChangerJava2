package com.looigi.wallpaperchanger2.Password;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.Password.strutture.StrutturaPassword;
import com.looigi.wallpaperchanger2.Password.strutture.StrutturaUtente;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import java.util.List;

public class VariabiliStatichePWD {
    private static VariabiliStatichePWD instance = null;

    private VariabiliStatichePWD() {
    }

    public static VariabiliStatichePWD getInstance() {
        if (instance == null) {
            instance = new VariabiliStatichePWD();
        }

        return instance;
    }

    private String UrlWS = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaPassword + "/"; // "http://www.wspassword.looigi.it";
    private int idUtente;
    private Context context;
    private String Ricerca = "";
    private String PasswordAppoggio;
    private String NomeUtenteAppoggio;
    private List<StrutturaPassword> listaPassword = null;
    private StrutturaUtente UtenteAttuale;
    private ListView lstPassword;
    private boolean deveAggiungereRigheAlDb = false;
    private StrutturaUtente StrutturaUtenteAppoggio;
    private LinearLayout laySceltaPassword;
    private LinearLayout layNuovoUtente;
    private TextView txtQuante;
    private LinearLayout layPassword;
    private TextView txtId;
    private EditText edtSito;
    private EditText edtUtenza;
    private EditText edtPassword;
    private EditText edtNote;
    private EditText edtIndirizzo;
    private String modalitaEdit = "";
    private boolean LoginEffettuata = false;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUrlWS() {
        return UrlWS;
    }

    public EditText getEdtIndirizzo() {
        return edtIndirizzo;
    }

    public void setEdtIndirizzo(EditText edtIndirizzo) {
        this.edtIndirizzo = edtIndirizzo;
    }

    public EditText getEdtNote() {
        return edtNote;
    }

    public void setEdtNote(EditText edtNote) {
        this.edtNote = edtNote;
    }

    public EditText getEdtPassword() {
        return edtPassword;
    }

    public void setEdtPassword(EditText edtPassword) {
        this.edtPassword = edtPassword;
    }

    public EditText getEdtSito() {
        return edtSito;
    }

    public void setEdtSito(EditText edtSito) {
        this.edtSito = edtSito;
    }

    public EditText getEdtUtenza() {
        return edtUtenza;
    }

    public void setEdtUtenza(EditText edtUtenza) {
        this.edtUtenza = edtUtenza;
    }

    public LinearLayout getLayPassword() {
        return layPassword;
    }

    public void setLayPassword(LinearLayout layPassword) {
        this.layPassword = layPassword;
    }

    public boolean isLoginEffettuata() {
        return LoginEffettuata;
    }

    public void setLoginEffettuata(boolean loginEffettuata) {
        LoginEffettuata = loginEffettuata;
    }

    public String getModalitaEdit() {
        return modalitaEdit;
    }

    public void setModalitaEdit(String modalitaEdit) {
        this.modalitaEdit = modalitaEdit;
    }

    public TextView getTxtId() {
        return txtId;
    }

    public void setTxtId(TextView txtId) {
        this.txtId = txtId;
    }

    public TextView getTxtQuante() {
        return txtQuante;
    }

    public void setTxtQuante(TextView txtQuante) {
        this.txtQuante = txtQuante;
    }

    public LinearLayout getLayNuovoUtente() {
        return layNuovoUtente;
    }

    public void setLayNuovoUtente(LinearLayout layNuovoUtente) {
        this.layNuovoUtente = layNuovoUtente;
    }

    public LinearLayout getLaySceltaPassword() {
        return laySceltaPassword;
    }

    public void setLaySceltaPassword(LinearLayout laySceltaPassword) {
        this.laySceltaPassword = laySceltaPassword;
    }

    public List<StrutturaPassword> getListaPassword() {
        return listaPassword;
    }

    public void setListaPassword(List<StrutturaPassword> listaPassword) {
        this.listaPassword = listaPassword;
    }

    public ListView getLstPassword() {
        return lstPassword;
    }

    public void setLstPassword(ListView lstPassword) {
        this.lstPassword = lstPassword;
    }

    public StrutturaUtente getStrutturaUtenteAppoggio() {
        return StrutturaUtenteAppoggio;
    }

    public void setStrutturaUtenteAppoggio(StrutturaUtente strutturaUtenteAppoggio) {
        StrutturaUtenteAppoggio = strutturaUtenteAppoggio;
    }

    public StrutturaUtente getUtenteAttuale() {
        return UtenteAttuale;
    }

    public void setUtenteAttuale(StrutturaUtente utenteAttuale) {
        UtenteAttuale = utenteAttuale;
    }

    public boolean isDeveAggiungereRigheAlDb() {
        return deveAggiungereRigheAlDb;
    }

    public void setDeveAggiungereRigheAlDb(boolean deveAggiungereRigheAlDb) {
        this.deveAggiungereRigheAlDb = deveAggiungereRigheAlDb;
    }

    public String getNomeUtenteAppoggio() {
        return NomeUtenteAppoggio;
    }

    public void setNomeUtenteAppoggio(String nomeUtenteAppoggio) {
        NomeUtenteAppoggio = nomeUtenteAppoggio;
    }

    public String getPasswordAppoggio() {
        return PasswordAppoggio;
    }

    public void setPasswordAppoggio(String passwordAppoggio) {
        PasswordAppoggio = passwordAppoggio;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getRicerca() {
        return Ricerca;
    }

    public void setRicerca(String ricerca) {
        Ricerca = ricerca;
    }
}
