package com.looigi.wallpaperchanger2.classePlayer.preferiti_tags;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaArtisti;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerPreferiti extends BaseAdapter {
    private Context context;
    private List<StrutturaArtisti> listaArtistiOrig;
    private List<StrutturaArtisti> listaArtisti;
    private LayoutInflater inflater;
    private String Filtro;
    private String Path;
    private String Confronto;

    public AdapterListenerPreferiti(Context applicationContext, List<StrutturaArtisti> Artisti) {
        this.context = applicationContext;
        this.listaArtistiOrig = Artisti;
        this.listaArtisti = new ArrayList<>();
        inflater = (LayoutInflater.from(applicationContext));
        this.Path = context.getFilesDir() + "/Player/ImmaginiMusica/";
        this.Confronto = VariabiliStatichePrefTags.getInstance().getStringaDiConfronto();

        ScriveQuantiSelezionati();
        VariabiliStatichePrefTags.getInstance().getTxtQuanti().setText("0/" +
                listaArtistiOrig.size());
    }

    @Override
    public int getCount() {
        return listaArtisti.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void updateData(String Filtro) {
        this.Filtro = Filtro;
        listaArtisti = new ArrayList<>();

        notifyDataSetChanged();

        if (VariabiliStatichePrefTags.getInstance().isSoloSelezionati()) {
            for (int i = 0; i < listaArtistiOrig.size(); i++) {
                String NomeArtista = listaArtistiOrig.get(i).getNomeArtista();

                if (Confronto.contains(NomeArtista + ";")) {
                    listaArtisti.add(listaArtistiOrig.get(i));
                }
            }
        } else {
            for (int i = 0; i < listaArtistiOrig.size(); i++) {
                String NomeArtista = listaArtistiOrig.get(i).getNomeArtista();
                boolean Ok = false;
                if (!Filtro.isEmpty()) {
                    if (NomeArtista.toUpperCase().contains(Filtro.toUpperCase())) {
                        Ok = true;
                    }
                } else {
                    Ok = true;
                }
                if (Ok) {
                    listaArtisti.add(listaArtistiOrig.get(i));
                }
            }
        }

        VariabiliStatichePrefTags.getInstance().getTxtQuanti().setText(listaArtisti.size() + "/" +
                listaArtistiOrig.size());

        notifyDataSetChanged();
    }

    private void ScriveQuantiSelezionati() {
        int quanti = 0;

        if (!Confronto.isEmpty()) {
            String[] s = Confronto.split(";");
            quanti = s.length;
        }

        VariabiliStatichePrefTags.getInstance().getTxtSelezionati().setText(Integer.toString(quanti));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_preferiti, null);

        if (i < listaArtisti.size()) {
            String NomeArtista = listaArtisti.get(i).getNomeArtista();
            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);

            Bitmap bitmap = UtilityPlayer.getInstance().PrendeImmagineArtistaACaso(
                    context, NomeArtista);
            imgImmagine.setImageBitmap(bitmap);

            CheckBox chkSelezionato = (CheckBox) view.findViewById(R.id.chkSelezionato);
            if (Confronto.contains(NomeArtista + ";")) {
                chkSelezionato.setChecked(true);
            } else {
                chkSelezionato.setChecked(false);
            }
            chkSelezionato.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSelezionato.isChecked()) {
                        Confronto += NomeArtista + ";";
                    } else {
                        Confronto = Confronto.replace(NomeArtista + ";", "");
                    }

                    ScriveQuantiSelezionati();

                    VariabiliStatichePrefTags.getInstance().ImpostaStringa(Confronto);
                }
            });

            TextView tArtista = (TextView) view.findViewById(R.id.txtArtista);
            tArtista.setText(NomeArtista);
        }

        return view;
    }
}
