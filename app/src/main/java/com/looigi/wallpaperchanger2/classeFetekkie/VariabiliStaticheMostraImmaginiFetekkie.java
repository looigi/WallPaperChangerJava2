package com.looigi.wallpaperchanger2.classeFetekkie;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiCategorieFE;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiLibraryFE;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheMostraImmaginiFetekkie {
    private static VariabiliStaticheMostraImmaginiFetekkie instance = null;

    private VariabiliStaticheMostraImmaginiFetekkie() {
    }

    public static VariabiliStaticheMostraImmaginiFetekkie getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheMostraImmaginiFetekkie();
        }

        return instance;
    }

    private Activity act;
    private Context ctx;
    public static final String UrlWS = VariabiliStaticheStart.UrlWSGlobale + ":1071/"; // http://looigi.no-ip.biz:1071/";
    public static final String PathUrl = VariabiliStaticheStart.UrlWSGlobale + ":1085/Fetekkie/"; // "http://looigi.no-ip.biz:1085/Fetekkie/";
    public static final int TimeoutImmagine = 5;
    private ImmagineZoomabile img;
    private StrutturaImmaginiLibraryFE ultimaImmagineCaricata;
    private String Categoria;
    private String Filtro = "";
    private int idImmagine;
    private String Random = "S";
    private List<StrutturaImmaginiCategorieFE> listaCategorie = new ArrayList<>();
    private Spinner spnCategorie;
    private TextView txtId;
    private TextView txtCate;
    private TextView txtInfo;
    private List<StrutturaImmaginiLibraryFE> immaginiCaricate = new ArrayList<>();
    private GifImageView imgCaricamento;
    private List<StrutturaImmagine> listaImmagini = new ArrayList<>();
    private boolean slideShowAttivo = false;
    private int secondiAttesa = 5000;
    private boolean settingsAperto = true;
    private String[] listaCategorieFet;
    private String filtroCategoria = "";
    private String categoriAttuale = "";
    private Spinner spnSpostaCategorie;
    private String filtroCategoriaSpostamento = "";
    private String idCategoriaSpostamento;
    private boolean ricercaPerVisua = true;
    private boolean AggiornamentoCompleto = false;

    public boolean isAggiornamentoCompleto() {
        return AggiornamentoCompleto;
    }

    public void setAggiornamentoCompleto(boolean aggiornamentoCompleto) {
        AggiornamentoCompleto = aggiornamentoCompleto;
    }

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

    public String getCategoriAttuale() {
        return categoriAttuale;
    }

    public void setCategoriAttuale(String categoriAttuale) {
        this.categoriAttuale = categoriAttuale;
    }

    public String getFiltroCategoria() {
        return filtroCategoria;
    }

    public void setFiltroCategoria(String filtroCategoria) {
        this.filtroCategoria = filtroCategoria;
    }

    public String[] getListaCategorieFet() {
        return listaCategorieFet;
    }

    public void setListaCategorieFet(String[] listaCategorie) {
        this.listaCategorieFet = listaCategorie;
    }

    public boolean isSettingsAperto() {
        return settingsAperto;
    }

    public void setSettingsAperto(boolean settingsAperto) {
        this.settingsAperto = settingsAperto;
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

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
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

    public void setImmaginiCaricate(List<StrutturaImmaginiLibraryFE> immaginiCaricate) {
        this.immaginiCaricate = immaginiCaricate;
    }

    public List<StrutturaImmaginiLibraryFE> getImmaginiCaricate() {
        return immaginiCaricate;
    }

    public TextView getTxtInfo() {
        return txtInfo;
    }

    public void setTxtInfo(TextView txtInfo) {
        this.txtInfo = txtInfo;
    }

    public Spinner getSpnCategorie() {
        return spnCategorie;
    }

    public void setSpnCategorie(Spinner spnCategorie) {
        this.spnCategorie = spnCategorie;
    }

    public List<StrutturaImmaginiCategorieFE> getListaCategorie() {
        return listaCategorie;
    }

    public void setListaCategorie(List<StrutturaImmaginiCategorieFE> listaCategorie) {
        this.listaCategorie = listaCategorie;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
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

    public StrutturaImmaginiLibraryFE getUltimaImmagineCaricata() {
        return ultimaImmagineCaricata;
    }

    public void setUltimaImmagineCaricata(StrutturaImmaginiLibraryFE ultimaImmagineCaricata) {
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

    public void ScriveInfoImmagine(StrutturaImmaginiLibraryFE si) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (si != null && VariabiliStaticheMostraImmaginiFetekkie.getInstance().getTxtInfo() != null) {
                    String[] c = si.getNomeFile().split("/");
                    String NomeFile = c[c.length - 1];
                    String Categoria = si.getNomeFile().replace("/" + NomeFile, "");

                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getTxtId().setText(
                            Integer.toString(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getIdImmagine())
                    );
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getTxtCate().setText(
                            Categoria
                    );
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getTxtInfo().setText(
                            NomeFile
                    );
                } else {
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getTxtId().setText("");
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getTxtCate().setText("");
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getTxtInfo().setText("");
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }
}
