package com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.VariabiliImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.VariabiliStaticheControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.StrutturaVoltiRilevati;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.VariabiliStaticheVolti;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.List;

public class AdapterListenerListaVolti extends BaseAdapter {
    private Context context;
    private List<StrutturaVoltiRilevati> listaImmagini;
    private LayoutInflater inflter;

    public AdapterListenerListaVolti(Context applicationContext, List<StrutturaVoltiRilevati> Immagini) {
        this.context = applicationContext;
        this.listaImmagini = Immagini;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (listaImmagini != null) {
            return listaImmagini.size();
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
        view = inflter.inflate(R.layout.lista_volti, null);

        int idImmagine = listaImmagini.get(i).getIdImmagine();
        int idCategoriaVecchia = listaImmagini.get(i).getIdCategoriaVecchia();
        String CategoriaVecchia = listaImmagini.get(i).getCategoriaVecchia();
        int idCategoriaNuova = listaImmagini.get(i).getIdCategoriaNuova();
        String CategoriaNuova = listaImmagini.get(i).getCategoriaNuova();
        String Cartella = listaImmagini.get(i).getCartella();
        String NomeFile = listaImmagini.get(i).getNomeFile();
        int Confidenza = listaImmagini.get(i).getConfidenza();
        String urlImmagine = listaImmagini.get(i).getUrlImmagine();
        String urlImmagineNuova = listaImmagini.get(i).getUrlImmagineNuova();

        TextView txtIdImmagine = view.findViewById(R.id.txtIdImmagine);
        txtIdImmagine.setText(Integer.toString(idImmagine));

        TextView txtCategoriaVecchia = view.findViewById(R.id.txtCategoriaVecchia);
        txtCategoriaVecchia.setText(CategoriaVecchia);

        DownloadImmagineUguali d = new DownloadImmagineUguali();

        TextView txtCategoriaNuova = view.findViewById(R.id.txtCategoriaNuova);
        txtCategoriaNuova.setText(CategoriaNuova);

        TextView txtCartella = view.findViewById(R.id.txtCartella);
        txtCartella.setText(Cartella);

        TextView txtNomeFile = view.findViewById(R.id.txtNomeFile);
        txtNomeFile.setText(NomeFile);

        TextView txtConfidenza = view.findViewById(R.id.txtConfidenza);
        txtConfidenza.setText(Integer.toString(Confidenza));

        ImageView imgImmagine = view.findViewById(R.id.imgImmagine);
        ImageView imgImmagineOrigine = view.findViewById(R.id.imgImmagineOrigine);

        d.EsegueDownload(context, imgImmagine, urlImmagine);
        d.EsegueDownload(context, imgImmagineOrigine, urlImmagineNuova);

        imgImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheVolti.getInstance().getLayPreview().setVisibility(LinearLayout.VISIBLE);

                d.EsegueDownload(context, VariabiliStaticheVolti.getInstance().getImgPreview(), urlImmagine);
            }
        });

        imgImmagineOrigine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheVolti.getInstance().getLayPreview().setVisibility(LinearLayout.VISIBLE);

                d.EsegueDownload(context, VariabiliStaticheVolti.getInstance().getImgPreview(), urlImmagineNuova);
            }
        });

        ImageView imgSposta = view.findViewById(R.id.imgSposta);
        imgSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Si vuole spostare l'immagine selezionata alla categoria " +
                        CategoriaNuova + " ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM() == null) {
                            ChiamateWSMI c = new ChiamateWSMI(context);
                            c.RitornaCategorie(false, "VO");
                        }
                        String Nuova = CategoriaNuova.replace(" ", "_").trim().toUpperCase();
                        VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento("");
                        for (StrutturaImmaginiCategorie s : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                            if (s.getCategoria().toUpperCase().trim().equals(Nuova)) {
                                VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento(
                                        Integer.toString(s.getIdCategoria())
                                );
                                break;
                            }
                        }
                        if (VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento().isEmpty()) {
                            UtilitiesGlobali.getInstance().ApreToast(context, "Categoria non rilevata: " +
                                    VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaInserita());
                            return;
                        }

                        StrutturaImmaginiLibrary Imm =  new StrutturaImmaginiLibrary();
                        Imm.setCategoria(listaImmagini.get(i).getCategoriaVecchia());
                        Imm.setCartella(listaImmagini.get(i).getCartella());
                        Imm.setIdCategoria(listaImmagini.get(i).getIdCategoriaVecchia());
                        Imm.setNomeFile(listaImmagini.get(i).getNomeFile());
                        Imm.setIdImmagine(listaImmagini.get(i).getIdImmagine());

                        ChiamateWSMI ws = new ChiamateWSMI(context);
                        ws.SpostaImmagine(Imm, "VO");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return view;
    }
}
