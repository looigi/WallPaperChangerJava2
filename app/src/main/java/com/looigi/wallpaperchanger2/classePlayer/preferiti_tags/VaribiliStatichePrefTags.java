package com.looigi.wallpaperchanger2.classePlayer.preferiti_tags;

import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;

public class VaribiliStatichePrefTags {
    private static VaribiliStatichePrefTags instance = null;

    private VaribiliStatichePrefTags() {
    }

    public static VaribiliStatichePrefTags getInstance() {
        if (instance == null) {
            instance = new VaribiliStatichePrefTags();
        }

        return instance;
    }

    private ListView lstArtisti;
    private AdapterListenerPreferiti customAdapterPref;
    private AdapterListenerTags customAdapterTag;
    private boolean SoloSelezionati = false;
    private String StringaDiConfronto = "";
    private TextView txtSelezionati;
    private TextView txtQuanti;
    private String tipoOperazione;

    public void ImpostaStringa(String Cosa) {
        switch (tipoOperazione) {
            case "Preferiti":
                VariabiliStatichePlayer.getInstance().setPreferiti(
                        Cosa
                );
                break;
            case "PreferitiElimina":
                VariabiliStatichePlayer.getInstance().setPreferitiElimina(
                        Cosa
                );
                break;
            case "Tags":
                VariabiliStatichePlayer.getInstance().setPreferitiTags(
                        Cosa
                );
                break;
            case "TagsElimina":
                VariabiliStatichePlayer.getInstance().setPreferitiEliminaTags(
                        Cosa
                );
                break;
        }
    }

    public AdapterListenerTags getCustomAdapterTag() {
        return customAdapterTag;
    }

    public void setCustomAdapterTag(AdapterListenerTags customAdapterTag) {
        this.customAdapterTag = customAdapterTag;
    }

    public void setTipoOperazione(String tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

    public TextView getTxtQuanti() {
        return txtQuanti;
    }

    public void setTxtQuanti(TextView txtQuanti) {
        this.txtQuanti = txtQuanti;
    }

    public TextView getTxtSelezionati() {
        return txtSelezionati;
    }

    public void setTxtSelezionati(TextView txtSelezionati) {
        this.txtSelezionati = txtSelezionati;
    }

    public String getStringaDiConfronto() {
        return StringaDiConfronto;
    }

    public void setStringaDiConfronto(String stringaDiConfronto) {
        StringaDiConfronto = stringaDiConfronto;
    }

    public boolean isSoloSelezionati() {
        return SoloSelezionati;
    }

    public void setSoloSelezionati(boolean soloSelezionati) {
        SoloSelezionati = soloSelezionati;
    }

    public AdapterListenerPreferiti getCustomAdapterPref() {
        return customAdapterPref;
    }

    public void setCustomAdapterPref(AdapterListenerPreferiti customAdapterPref) {
        this.customAdapterPref = customAdapterPref;
    }

    public ListView getLstArtisti() {
        return lstArtisti;
    }

    public void setLstArtisti(ListView lstArtisti) {
        this.lstArtisti = lstArtisti;
    }
}
