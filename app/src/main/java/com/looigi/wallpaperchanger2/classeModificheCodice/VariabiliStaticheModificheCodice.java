package com.looigi.wallpaperchanger2.classeModificheCodice;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Modifiche;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Moduli;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Progetti;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Sezioni;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Stati;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.StrutturaConteggi;
import com.looigi.wallpaperchanger2.classeModificheCodice.adapters.AdapterListenerConteggi;
import com.looigi.wallpaperchanger2.classeModificheCodice.adapters.AdapterListenerModificheCodice;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheModificheCodice {
    private static VariabiliStaticheModificheCodice instance = null;

    private VariabiliStaticheModificheCodice() {
    }

    public static VariabiliStaticheModificheCodice getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheModificheCodice();
        }

        return instance;
    }

    public static final String UrlWS = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaWallPaperChanger + "/"; // "http://www.wslazio.looigi.it/";
    private Activity act;
    private List<Progetti> listaProgetti;
    private List<Moduli> listaModuli;
    private List<Sezioni> listaSezioni;
    private List<Modifiche> listaModifiche;
    private List<Stati> listaStati;
    private int idProgetto;
    private int idModulo;
    private int idSezione;
    private int idModifica;
    private LinearLayout layTipologia;
    private String Tipologia = "";
    private String Operazione = "";
    private TextView txtTipologia;
    private EditText edtTipologia;
    private Spinner spnProgetto;
    private Spinner spnModulo;
    private Spinner spnSezione;
    private String ProgettoSelezionato;
    private String ModuloSelezionato;
    private String SezioneSelezionata;
    private String ModificaSelezionata;
    private ListView lstModifiche;
    private int idStato;
    private LinearLayout layStato;
    private String statoSelezionato;
    private Spinner spnStati;
    private EditText edtStato;
    private SwitchCompat swcSoloAperti;
    private ImageView imgModificaProgetto;
    private ImageView imgEliminaProgetto;
    private ImageView imgModificaModulo;
    private ImageView imgEliminaModulo;
    private ImageView imgModificaSezioni;
    private ImageView imgEliminaSezioni;
    private TextView txtQuante;
    private GifImageView imgCaricamento;
    private List<StrutturaConteggi> ListaConteggi = new ArrayList<>();
    private ListView lstConteggi;
    private List<String> listaAppoggioDaSalvare;
    private int qualeIdDaSalvare;
    private boolean staSalvandoTutto = false;
    private ImageView imgAggiungeModulo;
    private ImageView imgAggiungeSezione;
    private ImageView imgAggiungeModifica;
    private boolean eseguitaLetturaIniziale = false;
    private AdapterListenerModificheCodice adapterModifiche;

    public void Attende(boolean Attesa) {
        if (imgCaricamento != null) {
            if (!Attesa) {
                imgCaricamento.setVisibility(LinearLayout.GONE);
            } else {
                imgCaricamento.setVisibility(LinearLayout.VISIBLE);
            }
        }
    }

    public void ScriveConteggi(Context context) {
        db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
        db.RitornaConteggi();
        /* AdapterListenerConteggi adapterC = (new AdapterListenerConteggi(context, VariabiliStaticheModificheCodice.getInstance().getListaConteggi()));
        VariabiliStaticheModificheCodice.getInstance().getLstConteggi().setAdapter(adapterC); */
        db.ChiudeDB();
    }

    public AdapterListenerModificheCodice getAdapterModifiche() {
        return adapterModifiche;
    }

    public void setAdapterModifiche(AdapterListenerModificheCodice adapterModifiche) {
        this.adapterModifiche = adapterModifiche;
    }

    public boolean isEseguitaLetturaIniziale() {
        return eseguitaLetturaIniziale;
    }

    public void setEseguitaLetturaIniziale(boolean eseguitaLetturaIniziale) {
        this.eseguitaLetturaIniziale = eseguitaLetturaIniziale;
    }

    public boolean isStaSalvandoTutto() {
        return staSalvandoTutto;
    }

    public ImageView getImgAggiungeModifica() {
        return imgAggiungeModifica;
    }

    public void setImgAggiungeModifica(ImageView imgAggiungeModifica) {
        this.imgAggiungeModifica = imgAggiungeModifica;
    }

    public ImageView getImgAggiungeSezione() {
        return imgAggiungeSezione;
    }

    public void setImgAggiungeSezione(ImageView imgAggiungeSezione) {
        this.imgAggiungeSezione = imgAggiungeSezione;
    }

    public ImageView getImgAggiungeModulo() {
        return imgAggiungeModulo;
    }

    public void setImgAggiungeModulo(ImageView imgAggiungeModulo) {
        this.imgAggiungeModulo = imgAggiungeModulo;
    }

    public void setStaSalvandoTutto(boolean staSalvandoTutto) {
        this.staSalvandoTutto = staSalvandoTutto;
    }

    public List<String> getListaAppoggioDaSalvare() {
        return listaAppoggioDaSalvare;
    }

    public void setListaAppoggioDaSalvare(List<String> listaAppoggioDaSalvare) {
        this.listaAppoggioDaSalvare = listaAppoggioDaSalvare;
    }

    public int getQualeIdDaSalvare() {
        return qualeIdDaSalvare;
    }

    public void setQualeIdDaSalvare(int qualeIdDaSalvare) {
        this.qualeIdDaSalvare = qualeIdDaSalvare;
    }

    public ListView getLstConteggi() {
        return lstConteggi;
    }

    public void setLstConteggi(ListView lstConteggi) {
        this.lstConteggi = lstConteggi;
    }

    public List<StrutturaConteggi> getListaConteggi() {
        return ListaConteggi;
    }

    public void setListaConteggi(List<StrutturaConteggi> listaConteggi) {
        ListaConteggi = listaConteggi;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }

    public TextView getTxtQuante() {
        return txtQuante;
    }

    public void setTxtQuante(TextView txtQuante) {
        this.txtQuante = txtQuante;
    }

    public ImageView getImgEliminaProgetto() {
        return imgEliminaProgetto;
    }

    public void setImgEliminaProgetto(ImageView imgEliminaProgetto) {
        this.imgEliminaProgetto = imgEliminaProgetto;
    }

    public ImageView getImgModificaProgetto() {
        return imgModificaProgetto;
    }

    public void setImgModificaProgetto(ImageView imgModificaProgetto) {
        this.imgModificaProgetto = imgModificaProgetto;
    }

    public ImageView getImgEliminaSezioni() {
        return imgEliminaSezioni;
    }

    public void setImgEliminaSezioni(ImageView imgEliminaSezioni) {
        this.imgEliminaSezioni = imgEliminaSezioni;
    }

    public ImageView getImgModificaSezioni() {
        return imgModificaSezioni;
    }

    public void setImgModificaSezioni(ImageView imgModificaSezioni) {
        this.imgModificaSezioni = imgModificaSezioni;
    }

    public ImageView getImgEliminaModulo() {
        return imgEliminaModulo;
    }

    public void setImgEliminaModulo(ImageView imgEliminaModulo) {
        this.imgEliminaModulo = imgEliminaModulo;
    }

    public ImageView getImgModificaModulo() {
        return imgModificaModulo;
    }

    public void setImgModificaModulo(ImageView imgModificaModulo) {
        this.imgModificaModulo = imgModificaModulo;
    }

    public SwitchCompat getSwcSoloAperti() {
        return swcSoloAperti;
    }

    public void setSwcSoloAperti(SwitchCompat swcSoloAperti) {
        this.swcSoloAperti = swcSoloAperti;
    }

    public EditText getEdtStato() {
        return edtStato;
    }

    public void setEdtStato(EditText edtStato) {
        this.edtStato = edtStato;
    }

    public Spinner getSpnStati() {
        return spnStati;
    }

    public void setSpnStati(Spinner spnStati) {
        this.spnStati = spnStati;
    }

    public String getStatoSelezionato() {
        return statoSelezionato;
    }

    public void setStatoSelezionato(String statoSelezionato) {
        this.statoSelezionato = statoSelezionato;
    }

    public LinearLayout getLayStato() {
        return layStato;
    }

    public void setLayStato(LinearLayout layStato) {
        this.layStato = layStato;
    }

    public int getIdStato() {
        return idStato;
    }

    public void setIdStato(int idStato) {
        this.idStato = idStato;
    }

    public ListView getLstModifiche() {
        return lstModifiche;
    }

    public void setLstModifiche(ListView lstModifiche) {
        this.lstModifiche = lstModifiche;
    }

    public int getIdModifica() {
        return idModifica;
    }

    public void setIdModifica(int idModifica) {
        this.idModifica = idModifica;
    }

    public String getModificaSelezionata() {
        return ModificaSelezionata;
    }

    public void setModificaSelezionata(String modificaSelezionata) {
        ModificaSelezionata = modificaSelezionata;
    }

    public String getModuloSelezionato() {
        return ModuloSelezionato;
    }

    public void setModuloSelezionato(String moduloSelezionato) {
        ModuloSelezionato = moduloSelezionato;
    }

    public String getProgettoSelezionato() {
        return ProgettoSelezionato;
    }

    public void setProgettoSelezionato(String progettoSelezionato) {
        ProgettoSelezionato = progettoSelezionato;
    }

    public String getSezioneSelezionata() {
        return SezioneSelezionata;
    }

    public void setSezioneSelezionata(String sezioneSelezionata) {
        SezioneSelezionata = sezioneSelezionata;
    }

    public Spinner getSpnModulo() {
        return spnModulo;
    }

    public void setSpnModulo(Spinner spnModulo) {
        this.spnModulo = spnModulo;
    }

    public Spinner getSpnProgetto() {
        return spnProgetto;
    }

    public void setSpnProgetto(Spinner spnProgetto) {
        this.spnProgetto = spnProgetto;
    }

    public Spinner getSpnSezione() {
        return spnSezione;
    }

    public void setSpnSezione(Spinner spnSezione) {
        this.spnSezione = spnSezione;
    }

    public EditText getEdtTipologia() {
        return edtTipologia;
    }

    public void setEdtTipologia(EditText edtTipologia) {
        this.edtTipologia = edtTipologia;
    }

    public TextView getTxtTipologia() {
        return txtTipologia;
    }

    public void setTxtTipologia(TextView txtTipologia) {
        this.txtTipologia = txtTipologia;
    }

    public String getOperazione() {
        return Operazione;
    }

    public void setOperazione(String operazione) {
        Operazione = operazione;
    }

    public String getTipologia() {
        return Tipologia;
    }

    public void setTipologia(String tipologia) {
        Tipologia = tipologia;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public int getIdProgetto() {
        return idProgetto;
    }

    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }

    public int getIdSezione() {
        return idSezione;
    }

    public void setIdSezione(int idSezione) {
        this.idSezione = idSezione;
    }

    public LinearLayout getLayTipologia() {
        return layTipologia;
    }

    public void setLayTipologia(LinearLayout layTipologia) {
        this.layTipologia = layTipologia;
    }

    public List<Modifiche> getListaModifiche() {
        return listaModifiche;
    }

    public void setListaModifiche(List<Modifiche> listaModifiche) {
        this.listaModifiche = listaModifiche;
    }

    public List<Moduli> getListaModuli() {
        return listaModuli;
    }

    public void setListaModuli(List<Moduli> listaModuli) {
        this.listaModuli = listaModuli;
    }

    public List<Progetti> getListaProgetti() {
        return listaProgetti;
    }

    public void setListaProgetti(List<Progetti> listaProgetti) {
        this.listaProgetti = listaProgetti;
    }

    public List<Sezioni> getListaSezioni() {
        return listaSezioni;
    }

    public void setListaSezioni(List<Sezioni> listaSezioni) {
        this.listaSezioni = listaSezioni;
    }

    public List<Stati> getListaStati() {
        return listaStati;
    }

    public void setListaStati(List<Stati> listaStati) {
        this.listaStati = listaStati;
    }

    public String[] RitornaStringaStati(List<Stati> listaStati) {
        String[] sStati = new String[listaStati.size()];

        int i = 0;
        for (Stati p : listaStati) {
            sStati[i] = p.getStato();
            i++;
        }

        return sStati;
    }

    public String RitornaStringaStato(int idStato) {
        for (Stati p : listaStati) {
            if (p.getIdStato() == idStato) {
                return p.getStato();
            }
        }

        return "";
    }

    public String[] RitornaStringaProgetti(List<Progetti> listaProgetti) {
        String[] sProgetti = new String[listaProgetti.size()];

        int i = 0;
        for (Progetti p : listaProgetti) {
            if (p != null && p.getProgetto() != null) {
                sProgetti[i] = p.getProgetto();
                i++;
            }
        }

        return sProgetti;
    }

    public int TornaIdProgetto(List<Progetti> lista, String Progetto) {
        for (Progetti p : lista) {
            if (p.getProgetto().toUpperCase().trim().equals(Progetto.toUpperCase().trim())) {
                return p.getIdProgetto();
            }
        }

        return -1;
    }

    public String[] RitornaStringaModuli(List<Moduli> listaModuli) {
        String[] sModuli = new String[listaModuli.size()];

        int i = 0;
        for (Moduli p : listaModuli) {
            if (p != null && p.getModulo() != null) {
                sModuli[i] = p.getModulo();
                i++;
            }
        }

        return sModuli;
    }

    public int TornaIdModulo(List<Moduli> lista, String Modulo) {
        for (Moduli p : lista) {
            if (p.getModulo().toUpperCase().trim().equals(Modulo.toUpperCase().trim())) {
                return p.getIdModulo();
            }
        }

        return -1;
    }

    public String[] RitornaStringaSezioni(List<Sezioni> listaSezioni) {
        String[] sSezioni = new String[listaSezioni.size()];

        int i = 0;
        for (Sezioni p : listaSezioni) {
            if (p != null && p.getSezione() != null) {
                sSezioni[i] = p.getSezione();
                i++;
            }
        }

        return sSezioni;
    }

    public int TornaIdStato(List<Stati> lista, String Stato) {
        for (Stati p : lista) {
            if (p.getStato().toUpperCase().trim().equals(Stato.toUpperCase().trim())) {
                return p.getIdStato();
            }
        }

        return -1;
    }

    public int TornaIdSezione(List<Sezioni> lista, String Sezione) {
        for (Sezioni p : lista) {
            if (p.getSezione().equals(Sezione)) {
                return p.getIdSezione();
            }
        }

        return -1;
    }

    private ArrayAdapter<String> adapterProgetti;

    public void setAdapterProgetti(ArrayAdapter<String> adapterProgetti) {
        this.adapterProgetti = adapterProgetti;
    }

    public void setAdapterModuli(ArrayAdapter<String> adapterModuli) {
        this.adapterModuli = adapterModuli;
    }

    public void setAdapterSezioni(ArrayAdapter<String> adapterSezioni) {
        this.adapterSezioni = adapterSezioni;
    }

    public ArrayAdapter<String> getAdapterProgetti() {
        return adapterProgetti;
    }

    private void RicaricaProgetti(Context context, db_dati_modifiche_codice db) {
        db.RitornaProgetti();
    }

    private ArrayAdapter<String> adapterModuli;

    public ArrayAdapter<String> getAdapterModuli() {
        return adapterModuli;
    }

    public void RicaricaModuli(Context context, db_dati_modifiche_codice db) {
        db.RitornaModuli(idProgetto);
    }

    private ArrayAdapter<String> adapterSezioni;

    public ArrayAdapter<String> getAdapterSezioni() {
        return adapterSezioni;
    }

    public void RicaricaSezioni(Context context, db_dati_modifiche_codice db) {
        db.RitornaSezioni(idProgetto, idModulo);
    }

    private void RicaricaStati(Context context, db_dati_modifiche_codice db) {
        db.RitornaStati();

        /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                RitornaStringaStati(listaStati)
        ); */
    }

    public void RicaricaModifiche(Context context, db_dati_modifiche_codice db) {
        db.RitornaModifiche(idProgetto, idModulo, idSezione);

        // AdapterListenerModificheCodice customAdapterT = new AdapterListenerModificheCodice(context, VariabiliStaticheModificheCodice.getInstance().getListaModifiche());
        // lstModifiche.setAdapter(customAdapterT);
    }

    public void EffettuaSalvataggio(Context context) {
        db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);

        String Cosa = "";

        switch (VariabiliStaticheModificheCodice.getInstance().getTipologia()) {
            case "PROGETTO":
                Cosa = VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().getText().toString();

                switch (VariabiliStaticheModificheCodice.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovoProgetto(Cosa);
                        break;
                    case "UPDATE":
                        db.ModificaProgetto(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                Cosa
                        );
                        break;
                    case "DELETE":
                        db.EliminaProgetto(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto()
                        );
                        break;
                }

                RicaricaProgetti(context, db);
                break;
            case "MODULO":
                Cosa = VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().getText().toString();

                switch (VariabiliStaticheModificheCodice.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovoModulo(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                Cosa);
                        break;
                    case "UPDATE":
                        db.ModificaModulo(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                Cosa
                        );
                        break;
                    case "DELETE":
                        db.EliminaModulo(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModulo()
                        );
                        break;
                }

                RicaricaModuli(context, db);
                break;
            case "SEZIONE":
                Cosa = VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().getText().toString();

                switch (VariabiliStaticheModificheCodice.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovaSezione(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                Cosa);
                        break;
                    case "UPDATE":
                        db.ModificaSezione(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                VariabiliStaticheModificheCodice.getInstance().getIdSezione(),
                                Cosa);
                        break;
                    case "DELETE":
                        db.EliminaSezione(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                VariabiliStaticheModificheCodice.getInstance().getIdSezione()
                        );
                        break;
                }

                RicaricaSezioni(context, db);
                break;
            case "MODIFICA":
                Cosa = VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().getText().toString();

                switch (VariabiliStaticheModificheCodice.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovaModifica(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                VariabiliStaticheModificheCodice.getInstance().getIdSezione(),
                                Cosa);
                        break;
                    case "UPDATE":
                        db.ModificaModifica(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                VariabiliStaticheModificheCodice.getInstance().getIdSezione(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModifica(),
                                Cosa,
                                VariabiliStaticheModificheCodice.getInstance().getIdStato()
                        );
                        break;
                    case "DELETE":
                        db.EliminaModifica(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                VariabiliStaticheModificheCodice.getInstance().getIdSezione(),
                                VariabiliStaticheModificheCodice.getInstance().getIdModifica()
                        );
                        break;
                }

                RicaricaModifiche(context, db);
                break;
            case "STATI":
                Cosa = VariabiliStaticheModificheCodice.getInstance().getEdtStato().getText().toString();

                switch (VariabiliStaticheModificheCodice.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovoStato(
                                Cosa
                        );
                        break;
                    case "UPDATE":
                        db.ModificaStato(
                                VariabiliStaticheModificheCodice.getInstance().getIdStato(),
                                Cosa
                        );
                        break;
                    case "DELETE":
                        db.EliminaStati(
                                VariabiliStaticheModificheCodice.getInstance().getIdStato()
                        );
                        break;
                }

                RicaricaStati(context, db);
                break;
        }

        db.ChiudeDB();

        VariabiliStaticheModificheCodice.getInstance().ScriveConteggi(context);
    }

    public String PrendeNumeroModifiche(Context context) {
        db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);

        int modificheTotali = db.RitornaNumeroModificheTotali(
          VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
          VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
          VariabiliStaticheModificheCodice.getInstance().getIdSezione()
        );

        String Ritorno = "Modifiche: " +
                VariabiliStaticheModificheCodice.getInstance().getListaModifiche().size() +
                "/" + modificheTotali;

        db.ChiudeDB();

        return Ritorno;
    }
}
