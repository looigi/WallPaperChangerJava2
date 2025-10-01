package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.strutture.StrutturaControlloImmagini;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheUtilityImmagini {
    private static VariabiliStaticheUtilityImmagini instance = null;

    private VariabiliStaticheUtilityImmagini() {
    }

    public static VariabiliStaticheUtilityImmagini getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheUtilityImmagini();
        }

        return instance;
    }

    private int idCategoria;
    private List<StrutturaImmaginiCategorie> listaCategorieIMM;
    private Spinner spnCategorie;
    private GifImageView imgCaricamento;
    private ListView lstImmagini;
    private List<StrutturaControlloImmagini> ControlloImmagini = new ArrayList<>();
    private AdapterListenerUI adapter;
    private boolean ControllaTutto = false;
    private int qualeStaControllando;
    private TextView txtQuale;
    private StrutturaControlloImmagini StrutturaAttuale;
    private String CategoriaAttuale;
    private boolean EsegueAncheRefresh = true;
    private boolean BloccaElaborazione = false;
    private String FiltroCategorie = "";
    private boolean chkControllo = false;
    private boolean chkUguali = false;
    private boolean chkFC = false;
    private boolean chkPoche = false;
    private boolean chkInvalide = false;
    private List<Integer> listaCategorieDiRicerca = new ArrayList<>();
    private int tipoCategoria = 3;

    private long downloadId = -1;
    private String fileDaEliminare = "";
    private WebView wvRicerca;
    private LinearLayout layWV;
    private EditText edtVW;
    private boolean vwInCorso = false;
    /* private int idCategoriaImpostataAdapter;
    private LinearLayout layPreview;
    private ImmagineZoomabile imgPreview;
    private int idImmagineInPreview;

    public int getIdImmagineInPreview() {
        return idImmagineInPreview;
    }

    public void setIdImmagineInPreview(int idImmagineInPreview) {
        this.idImmagineInPreview = idImmagineInPreview;
    } */

    public boolean isChkInvalide() {
        return chkInvalide;
    }

    public void setChkInvalide(boolean chkInvalide) {
        this.chkInvalide = chkInvalide;
    }

    /*
    public int getIdCategoriaImpostataAdapter() {
        return idCategoriaImpostataAdapter;
    }

    public void setIdCategoriaImpostataAdapter(int idCategoriaImpostataAdapter) {
        this.idCategoriaImpostataAdapter = idCategoriaImpostataAdapter;
    }

    public LinearLayout getLayPreview() {
        return layPreview;
    }

    public void setLayPreview(LinearLayout layPreview) {
        this.layPreview = layPreview;
    }

    public ImmagineZoomabile getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(ImmagineZoomabile imgPreview) {
        this.imgPreview = imgPreview;
    } */

    public boolean isVwInCorso() {
        return vwInCorso;
    }

    public void setVwInCorso(boolean vwInCorso) {
        this.vwInCorso = vwInCorso;
    }

    public EditText getEdtVW() {
        return edtVW;
    }

    public void setEdtVW(EditText edtVW) {
        this.edtVW = edtVW;
    }

    public LinearLayout getLayWV() {
        return layWV;
    }

    public void setLayWV(LinearLayout layWV) {
        this.layWV = layWV;
    }

    public WebView getWvRicerca() {
        return wvRicerca;
    }

    public void setWvRicerca(WebView wvRicerca) {
        this.wvRicerca = wvRicerca;
    }

    public String getFileDaEliminare() {
        return fileDaEliminare;
    }

    public void setFileDaEliminare(String fileDaEliminare) {
        this.fileDaEliminare = fileDaEliminare;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public int getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(int tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public List<Integer> getListaCategorieDiRicerca() {
        return listaCategorieDiRicerca;
    }

    public void setListaCategorieDiRicerca(List<Integer> listaCategorieDiRicerca) {
        this.listaCategorieDiRicerca = listaCategorieDiRicerca;
    }

    public boolean isChkPoche() {
        return chkPoche;
    }

    public void setChkPoche(boolean chkPoche) {
        this.chkPoche = chkPoche;
    }

    public boolean isChkControllo() {
        return chkControllo;
    }

    public void setChkControllo(boolean chkControllo) {
        this.chkControllo = chkControllo;
    }

    public boolean isChkUguali() {
        return chkUguali;
    }

    public void setChkUguali(boolean chkUguali) {
        this.chkUguali = chkUguali;
    }

    public boolean isChkFC() {
        return chkFC;
    }

    public void setChkFC(boolean chkFC) {
        this.chkFC = chkFC;
    }

    public String getFiltroCategorie() {
        return FiltroCategorie;
    }

    public void setFiltroCategorie(String filtroCategorie) {
        FiltroCategorie = filtroCategorie;
    }

    public boolean isBloccaElaborazione() {
        return BloccaElaborazione;
    }

    public void setBloccaElaborazione(boolean bloccaElaborazione) {
        BloccaElaborazione = bloccaElaborazione;
    }

    public boolean isEsegueAncheRefresh() {
        return EsegueAncheRefresh;
    }

    public void setEsegueAncheRefresh(boolean esegueAncheRefresh) {
        EsegueAncheRefresh = esegueAncheRefresh;
    }

    public String getCategoriaAttuale() {
        return CategoriaAttuale;
    }

    public void setCategoriaAttuale(String categoriaAttuale) {
        CategoriaAttuale = categoriaAttuale;
    }

    public StrutturaControlloImmagini getStrutturaAttuale() {
        return StrutturaAttuale;
    }

    public void setStrutturaAttuale(StrutturaControlloImmagini strutturaAttuale) {
        StrutturaAttuale = strutturaAttuale;
    }

    public TextView getTxtQuale() {
        return txtQuale;
    }

    public void setTxtQuale(TextView txtQuale) {
        this.txtQuale = txtQuale;
    }

    public int getQualeStaControllando() {
        return qualeStaControllando;
    }

    public void setQualeStaControllando(int qualeStaControllando) {
        this.qualeStaControllando = qualeStaControllando;
    }

    public boolean isControllaTutto() {
        return ControllaTutto;
    }

    public void setControllaTutto(boolean controllaTutto) {
        ControllaTutto = controllaTutto;
    }

    public AdapterListenerUI getAdapter() {
        return adapter;
    }

    public void setAdapter(AdapterListenerUI adapter) {
        this.adapter = adapter;
    }

    public List<StrutturaControlloImmagini> getControlloImmagini() {
        return ControlloImmagini;
    }

    public void setControlloImmagini(List<StrutturaControlloImmagini> controlloImmagini) {
        ControlloImmagini = controlloImmagini;
    }

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public List<StrutturaImmaginiCategorie> getListaCategorieIMM() {
        return listaCategorieIMM;
    }

    public void setListaCategorieIMM(List<StrutturaImmaginiCategorie> listaCategorieIMM) {
        this.listaCategorieIMM = listaCategorieIMM;
    }

    public Spinner getSpnCategorie() {
        return spnCategorie;
    }

    public void setSpnCategorie(Spinner spnCategorie) {
        this.spnCategorie = spnCategorie;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void Attesa(boolean bAttesa) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bAttesa) {
                    imgCaricamento.setVisibility(View.VISIBLE);
                } else {
                    imgCaricamento.setVisibility(View.GONE);
                }
            }
        }, 10);
    }
}
