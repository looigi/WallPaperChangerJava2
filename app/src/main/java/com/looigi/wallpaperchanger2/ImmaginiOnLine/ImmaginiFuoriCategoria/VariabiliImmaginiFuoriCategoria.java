package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.adapters.AdapterListenerImmaginiFuoricategoria;

import java.util.List;

public class VariabiliImmaginiFuoriCategoria {
    private static VariabiliImmaginiFuoriCategoria instance = null;

    private VariabiliImmaginiFuoriCategoria() {
    }

    public static VariabiliImmaginiFuoriCategoria getInstance() {
        if (instance == null) {
            instance = new VariabiliImmaginiFuoriCategoria();
        }

        return instance;
    }

    private String idCategoria = "";
    private String Categoria = "";
    private String Ricerca = "";
    /* private String Alias1 = "";
    private String Alias2 = "";
    private String Tag = ""; */
    private EditText QuantiCaratteri;
    private String AndOr = "Or";
    private boolean SoloSuAltro = true;
    private boolean CercaExif = false;
    private ImageView imgCaricamento;
    private ListView lstImmagini;
    // private LinearLayout laypreview;
    // private ImmagineZoomabile imgPreview;
    private TextView txtQuanteImmaginiRilevate;
    private List<StrutturaImmagineFuoriCategoria> listaImmagini;
    private int qualeImmagineStaSpostando = -1;
    private boolean staSpostandoTutte = false;
    private List<StrutturaImmagineFuoriCategoria> listaDaSpostare;
    private AdapterListenerImmaginiFuoricategoria adapter;
    private Spinner spnCategorie;
    private List<StrutturaImmaginiCategorie> listaCategorieIMM;
    private EditText edtAlias1;
    private EditText edtAlias2;
    private EditText edtTag;
    private String CategoriaInserita = "";
    private String TestoRicercato;

    public String getTestoRicercato() {
        return TestoRicercato;
    }

    public void setTestoRicercato(String testoRicercato) {
        TestoRicercato = testoRicercato;
    }

    public String getRicerca() {
        return Ricerca;
    }

    public void setRicerca(String ricerca) {
        Ricerca = ricerca;
    }

    public String getCategoriaInserita() {
        return CategoriaInserita;
    }

    public void setCategoriaInserita(String categoriaInserita) {
        CategoriaInserita = categoriaInserita;
    }

    public EditText getEdtAlias1() {
        return edtAlias1;
    }

    public void setEdtAlias1(EditText edtAlias1) {
        this.edtAlias1 = edtAlias1;
    }

    public EditText getEdtAlias2() {
        return edtAlias2;
    }

    public void setEdtAlias2(EditText edtAlias2) {
        this.edtAlias2 = edtAlias2;
    }

    public EditText getEdtTag() {
        return edtTag;
    }

    public void setEdtTag(EditText edtTag) {
        this.edtTag = edtTag;
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

    public AdapterListenerImmaginiFuoricategoria getAdapter() {
        return adapter;
    }

    public void setAdapter(AdapterListenerImmaginiFuoricategoria adapter) {
        this.adapter = adapter;
    }

    public List<StrutturaImmagineFuoriCategoria> getListaDaSpostare() {
        return listaDaSpostare;
    }

    public void setListaDaSpostare(List<StrutturaImmagineFuoriCategoria> listaDaSpostare) {
        this.listaDaSpostare = listaDaSpostare;
    }

    public boolean isStaSpostandoTutte() {
        return staSpostandoTutte;
    }

    public void setStaSpostandoTutte(boolean staSpostandoTutte) {
        this.staSpostandoTutte = staSpostandoTutte;
    }

    public int getQualeImmagineStaSpostando() {
        return qualeImmagineStaSpostando;
    }

    public void setQualeImmagineStaSpostando(int qualeImmagineStaSpostando) {
        this.qualeImmagineStaSpostando = qualeImmagineStaSpostando;
    }

    public List<StrutturaImmagineFuoriCategoria> getListaImmagini() {
        return listaImmagini;
    }

    public void setListaImmagini(List<StrutturaImmagineFuoriCategoria> listaImmagini) {
        this.listaImmagini = listaImmagini;
    }

    public TextView getTxtQuanteImmaginiRilevate() {
        return txtQuanteImmaginiRilevate;
    }

    public void setTxtQuanteImmaginiRilevate(TextView txtQuanteImmaginiRilevate) {
        this.txtQuanteImmaginiRilevate = txtQuanteImmaginiRilevate;
    }

    /*
    public ImmagineZoomabile getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(ImmagineZoomabile imgPreview) {
        this.imgPreview = imgPreview;
    }

    public LinearLayout getLaypreview() {
        return laypreview;
    }

    public void setLaypreview(LinearLayout laypreview) {
        this.laypreview = laypreview;
    }
    */

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    public ImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(ImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    /*
    public String getAlias1() {
        return Alias1;
    }

    public void setAlias1(String alias1) {
        Alias1 = alias1;
    }

    public String getAlias2() {
        return Alias2;
    }

    public void setAlias2(String alias2) {
        Alias2 = alias2;
    }
    */

    public String getAndOr() {
        return AndOr;
    }

    public void setAndOr(String andOr) {
        AndOr = andOr;
    }

    public boolean isCercaExif() {
        return CercaExif;
    }

    public void setCercaExif(boolean cercaExif) {
        CercaExif = cercaExif;
    }

    public EditText getQuantiCaratteri() {
        return QuantiCaratteri;
    }

    public void setQuantiCaratteri(EditText quantiCaratteri) {
        QuantiCaratteri = quantiCaratteri;
    }

    public boolean isSoloSuAltro() {
        return SoloSuAltro;
    }

    public void setSoloSuAltro(boolean soloSuAltro) {
        SoloSuAltro = soloSuAltro;
    }

    /*
    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
    */

    public void ScaricaProssimaImmagine(Context context, int quale) {
        StrutturaImmagineFuoriCategoria s = VariabiliImmaginiFuoriCategoria.getInstance().getListaDaSpostare().get(quale);
        StrutturaImmaginiLibrary Imm =  new StrutturaImmaginiLibrary();
        Imm.setAlias(s.getAlias());
        Imm.setCategoria(s.getCategoria());
        Imm.setCartella(s.getCartella());
        Imm.setIdCategoria(s.getIdCategoria());
        Imm.setTag(s.getTag());
        Imm.setDataCreazione(s.getDataCreazione());
        Imm.setDataModifica(s.getDataModifica());
        Imm.setDimensioneFile((int) s.getDimensioneFile());
        Imm.setIdImmagine(s.getIdImmagine());
        Imm.setDimensioniImmagine(s.getDimensioniImmagine());
        Imm.setNomeFile(s.getNomeFile());
        Imm.setPathImmagine(s.getPathImmagine());
        Imm.setUrlImmagine(s.getUrlImmagine());

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.SpostaImmagine(Imm, "FC");
    }
}
