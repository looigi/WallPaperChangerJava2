package com.looigi.wallpaperchanger2.Onomastici;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Onomastici.adapters.AdapterListenerCompleanni;
import com.looigi.wallpaperchanger2.Onomastici.strutture.DatiColori;
import com.looigi.wallpaperchanger2.Onomastici.strutture.StrutturaCompleanno;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;

public class VariabiliStaticheOnomastici {
    private static VariabiliStaticheOnomastici instance = null;

    private VariabiliStaticheOnomastici() {
    }

    public static VariabiliStaticheOnomastici getInstance() {
        if(instance == null) {
            instance = new VariabiliStaticheOnomastici();
        }

        return instance;
    }

    public static final String UrlImmagini = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaDiscoPublic + "/"; // "http://looigi.no-ip.biz:1085/";
    private ContentResolver Rubrica;
    private AssetManager Assets;
    private Context context;
    // private Context widgetContext;
    // private String NomeImmagineSantoPerWidget;
    public String BRIGHTNESS_PREFERENCE_KEY = "brightness";
    public String COLOR_PREFERENCE_KEY = "color";
    private DatiColori ColorWidget;
    // private String Lingua="";
    private ImageView imgView;
    private boolean partito = false;
    private List<StrutturaCompleanno> Compleanni;
    private LinearLayout layInsComp;
    private EditText edtNomeCompleanno;
    private EditText edtCognomeCompleanno;
    private EditText edtGiornoCompleanno;
    private EditText edtMeseCompleanno;
    private EditText edtAnnoCompleanno;
    private int idModifica = -1;
    private TextView txtNomeSceltoCompleanno;

    public void ScriveCompleanniDelGiorno(Activity act) {
        ListView lstCompleanni = act.findViewById(R.id.lstCompleanni);
        if (!VariabiliStaticheOnomastici.getInstance().getCompleanni().isEmpty()) {
            AdapterListenerCompleanni customAdapterA = new AdapterListenerCompleanni(context,
                    VariabiliStaticheOnomastici.getInstance().getCompleanni());
            lstCompleanni.setAdapter(customAdapterA);
        }
    }

    public void ScriveCompleanniRicerca(Activity act, List<StrutturaCompleanno> lista) {
        ListView lstCompleanni = act.findViewById(R.id.lstRicercaCompleanni);
        if (!lista.isEmpty()) {
            AdapterListenerCompleanni customAdapterA = new AdapterListenerCompleanni(context,
                    lista);
            lstCompleanni.setAdapter(customAdapterA);
        }
    }

    public TextView getTxtNomeSceltoCompleanno() {
        return txtNomeSceltoCompleanno;
    }

    public void setTxtNomeSceltoCompleanno(TextView txtNomeSceltoCompleanno) {
        this.txtNomeSceltoCompleanno = txtNomeSceltoCompleanno;
    }

    public EditText getEdtCognomeCompleanno() {
        return edtCognomeCompleanno;
    }

    public void setEdtCognomeCompleanno(EditText edtCognomeCompleanno) {
        this.edtCognomeCompleanno = edtCognomeCompleanno;
    }

    public int getIdModifica() {
        return idModifica;
    }

    public void setIdModifica(int idModifica) {
        this.idModifica = idModifica;
    }

    public EditText getEdtAnnoCompleanno() {
        return edtAnnoCompleanno;
    }

    public void setEdtAnnoCompleanno(EditText edtAnnoCompleanno) {
        this.edtAnnoCompleanno = edtAnnoCompleanno;
    }

    public EditText getEdtGiornoCompleanno() {
        return edtGiornoCompleanno;
    }

    public void setEdtGiornoCompleanno(EditText edtGiornoCompleanno) {
        this.edtGiornoCompleanno = edtGiornoCompleanno;
    }

    public EditText getEdtMeseCompleanno() {
        return edtMeseCompleanno;
    }

    public void setEdtMeseCompleanno(EditText edtMeseCompleanno) {
        this.edtMeseCompleanno = edtMeseCompleanno;
    }

    public EditText getEdtNomeCompleanno() {
        return edtNomeCompleanno;
    }

    public void setEdtNomeCompleanno(EditText edtNomeCompleanno) {
        this.edtNomeCompleanno = edtNomeCompleanno;
    }

    public LinearLayout getLayInsComp() {
        return layInsComp;
    }

    public void setLayInsComp(LinearLayout layInsComp) {
        this.layInsComp = layInsComp;
    }

    public List<StrutturaCompleanno> getCompleanni() {
        if (Compleanni == null) {
            Compleanni = new ArrayList<>();
        }
        return Compleanni;
    }

    public void setCompleanni(List<StrutturaCompleanno> compleanni) {
        Compleanni = compleanni;
    }

    public boolean isPartito() {
        return partito;
    }

    public void setPartito(boolean partito) {
        this.partito = partito;
    }
     /*private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    public int[] getAppWidgetIds() {
        return appWidgetIds;
    }

    public void setAppWidgetIds(int[] appWidgetIds) {
        this.appWidgetIds = appWidgetIds;
    }

    public AppWidgetManager getAppWidgetManager() {
        return appWidgetManager;
    }

    public void setAppWidgetManager(AppWidgetManager appWidgetManager) {
        this.appWidgetManager = appWidgetManager;
    }

    public String getNomeImmagineSantoPerWidget() {
        return NomeImmagineSantoPerWidget;
    }

    public void setNomeImmagineSantoPerWidget(String nomeImmagineSantoPerWidget) {
        NomeImmagineSantoPerWidget = nomeImmagineSantoPerWidget;
    }

    public Context getWidgetContext() {
        return widgetContext;
    }

    public void setWidgetContext(Context widgetContext) {
        this.widgetContext = widgetContext;
    }
    */

    public ImageView getImgView() {
        return imgView;
    }

    public void setImgView(ImageView imgView) {
        this.imgView = imgView;
    }

    public ContentResolver getRubrica() {
        return Rubrica;
    }

    public void setRubrica(ContentResolver rubrica) {
        Rubrica = rubrica;
    }

    public AssetManager getAssets() {
        return Assets;
    }

    public void setAssets(AssetManager assets) {
        Assets = assets;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public DatiColori getColorWidget() {
        return ColorWidget;
    }

    public void setColorWidget(DatiColori colorWidget) {
        ColorWidget = colorWidget;
    }

    /* public String getLingua() {
        return Lingua;
    }

    public void setLingua(String lingua) {
        Lingua = lingua;
    } */
}
