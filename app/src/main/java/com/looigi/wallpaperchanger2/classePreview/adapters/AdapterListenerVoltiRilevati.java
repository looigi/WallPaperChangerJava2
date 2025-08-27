package com.looigi.wallpaperchanger2.classePreview.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePreview.MainPreview;
import com.looigi.wallpaperchanger2.classePreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.classePreview.strutture.StrutturaVoltiRilevati;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.DownloadImmagineSI;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.StrutturaImmagineDaScaricare;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterListenerVoltiRilevati extends BaseAdapter {
    private Context context;
    private List<StrutturaVoltiRilevati> Immagini;
    private LayoutInflater inflater;
    private String Filtro;
    private String Modalita;

    public AdapterListenerVoltiRilevati(Context applicationContext, List<StrutturaVoltiRilevati> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;
        this.Filtro = Filtro;
        this.Modalita = Modalita;

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (Immagini != null) {
            return Immagini.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_volti_rilevati, null);

        ImageView imgOrigine = view.findViewById(R.id.imgOrigine);
        ImageView imgDestinazione = view.findViewById(R.id.imgDestinazione);
        TextView txtCatOrigine = view.findViewById(R.id.txtOrigine);
        TextView txtCatDest = view.findViewById(R.id.txtDestinazione);

        StrutturaVoltiRilevati si = Immagini.get(i);
        txtCatOrigine.setText(si.getIdCategoriaOrigine() + "-" + si.getCategoriaOrigine());
        txtCatDest.setText(si.getIdCategoria() + "-" + si.getCategoria());

        return view;
    }
}
