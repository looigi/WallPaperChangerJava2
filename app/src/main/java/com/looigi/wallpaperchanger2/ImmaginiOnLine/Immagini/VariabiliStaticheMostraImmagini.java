package com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.InterrogazioneWSMI;
import com.looigi.wallpaperchanger2.Wallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.UtilitiesVarie.ImmagineZoomabile;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;

public class VariabiliStaticheMostraImmagini {
    private static VariabiliStaticheMostraImmagini instance = null;

    private VariabiliStaticheMostraImmagini() {
    }

    public static VariabiliStaticheMostraImmagini getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheMostraImmagini();
        }

        return instance;
    }

    private Activity act;
    private Context ctx;
    public static final String UrlWS = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaLooVF + "/"; // "http://looigi.no-ip.biz:1071/";
    // public static final String PercorsoImmagineSuURL = "http://www.sfondi.looigi.it";
    public static final int TimeoutImmagine = 25;
    private ImmagineZoomabile img;
    private StrutturaImmaginiLibrary ultimaImmagineCaricata;
    private int idCategoria;
    private String Categoria;
    private String Filtro = "";
    private int idImmagine;
    private String Random = "S";
    private List<StrutturaImmaginiCategorie> listaCategorie = new ArrayList<>();
    private Spinner spnCategorie;
    private Spinner spnSpostaCategorie;
    // private TextView txtId;
    // private TextView txtCate;
    // private TextView txtInfo;
    private List<StrutturaImmaginiLibrary> immaginiCaricate = new ArrayList<>();
    private ImageView imgCaricamento;
    private List<StrutturaImmagine> listaImmagini = new ArrayList<>();
    private boolean slideShowAttivo = false;
    private int secondiAttesa = 5000;
    private InterrogazioneWSMI ClasseChiamata;
    private boolean SettingsAperto = true;
    private String[] listaCategorieImm;
    private String filtroCategoria = "";
    private String categoriaAttuale = "";
    private String filtroCategoriaSpostamento = "";
    private String idCategoriaSpostamento;
    private boolean ricercaPerVisua = true;
    private boolean AggiornamentoCompleto = false;
    private StrutturaImmaginiLibrary immaginePerWP;
    private String OperatoreFiltro = "Or";
    private TextView txtInfoSotto;
    private StrutturaImmaginiLibrary StrutturaImmagineAttuale;
    private FlexboxLayout layCategorieRilevate;
    private FlexboxLayout layScritteRilevate;
    private LinearLayout layTasti;
    private boolean staCambiandocategoria = false;

    public boolean isStaCambiandocategoria() {
        return staCambiandocategoria;
    }

    public void setStaCambiandocategoria(boolean staCambiandocategoria) {
        this.staCambiandocategoria = staCambiandocategoria;
    }

    public FlexboxLayout getLayCategorieRilevate() {
        return layCategorieRilevate;
    }

    public void setLayCategorieRilevate(FlexboxLayout layCategorieRilevate) {
        this.layCategorieRilevate = layCategorieRilevate;
    }

    public FlexboxLayout getLayScritteRilevate() {
        return layScritteRilevate;
    }

    public void setLayScritteRilevate(FlexboxLayout layScritteRilevate) {
        this.layScritteRilevate = layScritteRilevate;
    }

    public LinearLayout getLayTasti() {
        return layTasti;
    }

    public void setLayTasti(LinearLayout layTasti) {
        this.layTasti = layTasti;
    }

    public StrutturaImmaginiLibrary getStrutturaImmagineAttuale() {
        return StrutturaImmagineAttuale;
    }

    public void setStrutturaImmagineAttuale(StrutturaImmaginiLibrary strutturaImmagineAttuale) {
        StrutturaImmagineAttuale = strutturaImmagineAttuale;
    }

    public StrutturaImmaginiLibrary getImmaginePerWP() {
        return immaginePerWP;
    }

    public TextView getTxtInfoSotto() {
        return txtInfoSotto;
    }

    public void setTxtInfoSotto(TextView txtInfoSotto) {
        this.txtInfoSotto = txtInfoSotto;
    }

    public String getOperatoreFiltro() {
        return OperatoreFiltro;
    }

    public void setOperatoreFiltro(String operatoreFiltro) {
        OperatoreFiltro = operatoreFiltro;
    }

    public void setImmaginePerWP(StrutturaImmaginiLibrary immaginePerWP) {
        this.immaginePerWP = immaginePerWP;
    }

    public boolean isAggiornamentoCompleto() {
        return AggiornamentoCompleto;
    }

    public void setAggiornamentoCompleto(boolean aggiornamentoCompleto) {
        AggiornamentoCompleto = aggiornamentoCompleto;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    /*
    public TextView getTxtCate() {
        return txtCate;
    }

    public void setTxtCate(TextView txtCate) {
        this.txtCate = txtCate;
    }

    public TextView getTxtId() {
        return txtId;
    }

    public void setTxtId(TextView txtId) {
        this.txtId = txtId;
    }
    */

    public boolean isRicercaPerVisua() {
        return ricercaPerVisua;
    }

    public void setRicercaPerVisua(boolean ricercaPerVisua) {
        this.ricercaPerVisua = ricercaPerVisua;
    }

    public String getIdCategoriaSpostamento() {
        return idCategoriaSpostamento;
    }

    public void setIdCategoriaSpostamento(String idCategoriaSpostamento) {
        this.idCategoriaSpostamento = idCategoriaSpostamento;
    }

    public String getFiltroCategoriaSpostamento() {
        return filtroCategoriaSpostamento;
    }

    public void setFiltroCategoriaSpostamento(String filtroCategoriaSpostamento) {
        this.filtroCategoriaSpostamento = filtroCategoriaSpostamento;
    }

    public Spinner getSpnSpostaCategorie() {
        return spnSpostaCategorie;
    }

    public void setSpnSpostaCategorie(Spinner spnSpostaCategorie) {
        this.spnSpostaCategorie = spnSpostaCategorie;
    }

    public String getCategoriaAttuale() {
        return categoriaAttuale;
    }

    public void setCategoriaAttuale(String categoriaAttuale) {
        this.categoriaAttuale = categoriaAttuale;
    }

    public String getFiltroCategoria() {
        return filtroCategoria;
    }

    public void setFiltroCategoria(String filtroCategoria) {
        this.filtroCategoria = filtroCategoria;
    }

    public String[] getListaCategorieImm() {
        return listaCategorieImm;
    }

    public void setListaCategorieImm(String[] listaCategorieImm) {
        this.listaCategorieImm = listaCategorieImm;
    }

    public boolean isSettingsAperto() {
        return SettingsAperto;
    }

    public void setSettingsAperto(boolean settingsAperto) {
        SettingsAperto = settingsAperto;
    }

    public InterrogazioneWSMI getClasseChiamata() {
        return ClasseChiamata;
    }

    public void setClasseChiamata(InterrogazioneWSMI classeChiamata) {
        ClasseChiamata = classeChiamata;
    }

    public int getSecondiAttesa() {
        return secondiAttesa;
    }

    public void setSecondiAttesa(int secondiAttesa) {
        this.secondiAttesa = secondiAttesa;
    }

    public boolean isSlideShowAttivo() {
        return slideShowAttivo;
    }

    public void setSlideShowAttivo(boolean slideShowAttivo) {
        this.slideShowAttivo = slideShowAttivo;
    }

    public List<StrutturaImmagine> getListaImmagini() {
        return listaImmagini;
    }

    public void setListaImmagini(List<StrutturaImmagine> listaImmagini) {
        this.listaImmagini = listaImmagini;
    }

    public ImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(ImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public void AggiungeCaricata() {
        this.immaginiCaricate.add(ultimaImmagineCaricata);
    }

    public void setImmaginiCaricate(List<StrutturaImmaginiLibrary> immaginiCaricate) {
        this.immaginiCaricate = immaginiCaricate;
    }

    public List<StrutturaImmaginiLibrary> getImmaginiCaricate() {
        return immaginiCaricate;
    }

    /*
    public TextView getTxtInfo() {
        return txtInfo;
    }

    public void setTxtInfo(TextView txtInfo) {
        this.txtInfo = txtInfo;
    }
    */

    public Spinner getSpnCategorie() {
        return spnCategorie;
    }

    public void setSpnCategorie(Spinner spnCategorie) {
        this.spnCategorie = spnCategorie;
    }

    public List<StrutturaImmaginiCategorie> getListaCategorie() {
        return listaCategorie;
    }

    public void setListaCategorie(List<StrutturaImmaginiCategorie> listaCategorie) {
        this.listaCategorie = listaCategorie;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getFiltro() {
        return Filtro;
    }

    public void setFiltro(String filtro) {
        Filtro = filtro;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getRandom() {
        return Random;
    }

    public void setRandom(String random) {
        Random = random;
    }

    public StrutturaImmaginiLibrary getUltimaImmagineCaricata() {
        return ultimaImmagineCaricata;
    }

    public void setUltimaImmagineCaricata(StrutturaImmaginiLibrary ultimaImmagineCaricata) {
        this.ultimaImmagineCaricata = ultimaImmagineCaricata;
    }

    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }

    public ImmagineZoomabile getImg() {
        return img;
    }

    public void setImg(ImmagineZoomabile img) {
        this.img = img;
    }

    public void ScriveInfoImmagine(StrutturaImmaginiLibrary si) {
        return;

        /* Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (si != null && VariabiliStaticheMostraImmagini.getInstance().getTxtInfo() != null) {
                    String NomeFile = si.getNomeFile();
                    String Categoria = si.getCategoria();

                    VariabiliStaticheMostraImmagini.getInstance().getTxtId().setText(
                            Integer.toString(VariabiliStaticheMostraImmagini.getInstance().getIdImmagine())
                    );
                    VariabiliStaticheMostraImmagini.getInstance().getTxtCate().setText(
                            Categoria
                    );
                    VariabiliStaticheMostraImmagini.getInstance().getTxtInfo().setText(
                            NomeFile
                    );
                } else {
                    if (VariabiliStaticheMostraImmagini.getInstance().getTxtId() != null) {
                        VariabiliStaticheMostraImmagini.getInstance().getTxtId().setText("");
                        VariabiliStaticheMostraImmagini.getInstance().getTxtCate().setText("");
                        VariabiliStaticheMostraImmagini.getInstance().getTxtInfo().setText("");
                    }
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100); */
    }
}
