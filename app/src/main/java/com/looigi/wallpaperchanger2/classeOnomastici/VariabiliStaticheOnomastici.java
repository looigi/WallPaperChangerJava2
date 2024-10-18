package com.looigi.wallpaperchanger2.classeOnomastici;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classeOnomastici.strutture.DatiColori;

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

    public static final String UrlImmagini = "http://looigi.no-ip.biz:1085/";
    private ContentResolver Rubrica;
    private AssetManager Assets;
    private Context context;
    // private Context widgetContext;
    // private String NomeImmagineSantoPerWidget;
    public String BRIGHTNESS_PREFERENCE_KEY = "brightness";
    public String COLOR_PREFERENCE_KEY = "color";
    private DatiColori ColorWidget;
    private String Lingua="";
     private ImageView imgView;
     private boolean partito = false;

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

    public String getLingua() {
        return Lingua;
    }

    public void setLingua(String lingua) {
        Lingua = lingua;
    }
}
