package com.looigi.wallpaperchanger2.classeModifiche;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.InterrogazioneWSMI;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Modifiche;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Moduli;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Progetti;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Sezioni;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Stati;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheModifiche {
    private static VariabiliStaticheModifiche instance = null;

    private VariabiliStaticheModifiche() {
    }

    public static VariabiliStaticheModifiche getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheModifiche();
        }

        return instance;
    }

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
            if (p.getProgetto().equals(Progetto)) {
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
            if (p.getModulo().equals(Modulo)) {
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
            if (p.getStato().equals(Stato)) {
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

    private void RicaricaProgetti(Context context, db_dati_modifiche db) {
        listaProgetti = db.RitornaProgetti();

        if (!listaProgetti.isEmpty()) {
            VariabiliStaticheModifiche.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModifiche.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheModifiche.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModifiche.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.GONE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                RitornaStringaProgetti(listaProgetti)
        );
        spnProgetto.setAdapter(adapter);
    }

    public List<Moduli> RicaricaModuli(Context context, db_dati_modifiche db) {
        listaModuli = db.RitornaModuli(idProgetto);

        if (!listaModuli.isEmpty()) {
            VariabiliStaticheModifiche.getInstance().getImgModificaModulo().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModifiche.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheModifiche.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModifiche.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                RitornaStringaModuli(listaModuli)
        );
        spnModulo.setAdapter(adapter);

        return listaModuli;
    }

    public List<Sezioni> RicaricaSezioni(Context context, db_dati_modifiche db) {
        listaSezioni = db.RitornaSezioni(idProgetto, idModulo);

        if (!listaSezioni.isEmpty()) {
            VariabiliStaticheModifiche.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModifiche.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheModifiche.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModifiche.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                RitornaStringaSezioni(listaSezioni)
        );
        spnSezione.setAdapter(adapter);

        return listaSezioni;
    }

    private void RicaricaStati(Context context, db_dati_modifiche db) {
        listaStati = db.RitornaStati();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                RitornaStringaStati(listaStati)
        );
        spnStati.setAdapter(adapter);
    }

    private void RicaricaModifiche(Context context, db_dati_modifiche db) {
        listaModifiche = db.RitornaModifiche(idProgetto, idModulo, idSezione);

        AdapterListenerModifiche customAdapterT = new AdapterListenerModifiche(context, VariabiliStaticheModifiche.getInstance().getListaModifiche());
        lstModifiche.setAdapter(customAdapterT);
    }

    public void EffettuaSalvataggio(Context context) {
        db_dati_modifiche db = new db_dati_modifiche(context);

        String Cosa = "";

        switch (VariabiliStaticheModifiche.getInstance().getTipologia()) {
            case "PROGETTO":
                Cosa = VariabiliStaticheModifiche.getInstance().getEdtTipologia().getText().toString();

                switch (VariabiliStaticheModifiche.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovoProgetto(Cosa);
                        break;
                    case "UPDATE":
                        db.ModificaProgetto(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                Cosa
                        );
                        break;
                    case "DELETE":
                        db.EliminaProgetto(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto()
                        );
                        break;
                }

                RicaricaProgetti(context, db);
                break;
            case "MODULO":
                Cosa = VariabiliStaticheModifiche.getInstance().getEdtTipologia().getText().toString();

                switch (VariabiliStaticheModifiche.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovoModulo(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                Cosa);
                        break;
                    case "UPDATE":
                        db.ModificaModulo(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                VariabiliStaticheModifiche.getInstance().getIdModulo(),
                                Cosa
                        );
                        break;
                    case "DELETE":
                        db.EliminaModulo(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                VariabiliStaticheModifiche.getInstance().getIdModulo()
                        );
                        break;
                }

                RicaricaModuli(context, db);
                break;
            case "SEZIONE":
                Cosa = VariabiliStaticheModifiche.getInstance().getEdtTipologia().getText().toString();

                switch (VariabiliStaticheModifiche.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovaSezione(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                VariabiliStaticheModifiche.getInstance().getIdModulo(),
                                Cosa);
                        break;
                    case "UPDATE":
                        db.ModificaSezione(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                VariabiliStaticheModifiche.getInstance().getIdModulo(),
                                VariabiliStaticheModifiche.getInstance().getIdSezione(),
                                Cosa);
                        break;
                    case "DELETE":
                        db.EliminaSezione(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                VariabiliStaticheModifiche.getInstance().getIdModulo(),
                                VariabiliStaticheModifiche.getInstance().getIdSezione()
                        );
                        break;
                }

                RicaricaSezioni(context, db);
                break;
            case "MODIFICA":
                Cosa = VariabiliStaticheModifiche.getInstance().getEdtTipologia().getText().toString();

                switch (VariabiliStaticheModifiche.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovaModifica(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                VariabiliStaticheModifiche.getInstance().getIdModulo(),
                                VariabiliStaticheModifiche.getInstance().getIdSezione(),
                                Cosa);
                        break;
                    case "UPDATE":
                        db.ModificaModifica(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                VariabiliStaticheModifiche.getInstance().getIdModulo(),
                                VariabiliStaticheModifiche.getInstance().getIdSezione(),
                                VariabiliStaticheModifiche.getInstance().getIdModifica(),
                                Cosa,
                                VariabiliStaticheModifiche.getInstance().getIdStato()
                        );
                        break;
                    case "DELETE":
                        db.EliminaModifica(
                                VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                VariabiliStaticheModifiche.getInstance().getIdModulo(),
                                VariabiliStaticheModifiche.getInstance().getIdSezione(),
                                VariabiliStaticheModifiche.getInstance().getIdModifica()
                        );
                        break;
                }

                RicaricaModifiche(context, db);
                break;
            case "STATI":
                Cosa = VariabiliStaticheModifiche.getInstance().getEdtStato().getText().toString();

                switch (VariabiliStaticheModifiche.getInstance().getOperazione()) {
                    case "INSERT":
                        db.InserisceNuovoStato(
                                Cosa
                        );
                        break;
                    case "UPDATE":
                        db.ModificaStato(
                                VariabiliStaticheModifiche.getInstance().getIdStato(),
                                Cosa
                        );
                        break;
                    case "DELETE":
                        db.EliminaStati(
                                VariabiliStaticheModifiche.getInstance().getIdStato()
                        );
                        break;
                }

                RicaricaStati(context, db);
                break;
        }

        db.ChiudeDB();
    }

    public String PrendeNumeroModifiche(Context context) {
        db_dati_modifiche db = new db_dati_modifiche(context);

        int modificheTotali = db.RitornaNumeroModificheTotali(
          VariabiliStaticheModifiche.getInstance().getIdProgetto(),
          VariabiliStaticheModifiche.getInstance().getIdModulo(),
          VariabiliStaticheModifiche.getInstance().getIdSezione()
        );

        String Ritorno = "Modifiche: " +
                VariabiliStaticheModifiche.getInstance().getListaModifiche().size() +
                "/" + modificheTotali;

        db.ChiudeDB();

        return Ritorno;
    }
}
