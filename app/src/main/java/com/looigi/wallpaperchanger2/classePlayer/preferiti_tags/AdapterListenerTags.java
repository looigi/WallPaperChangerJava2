package com.looigi.wallpaperchanger2.classePlayer.preferiti_tags;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaTags;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerTags extends BaseAdapter {
    private Context context;
    private List<StrutturaTags> listaTagsOrig;
    private List<StrutturaTags> listaTags;
    private LayoutInflater inflater;
    private String Filtro;
    private String Confronto;

    public AdapterListenerTags(Context applicationContext, List<StrutturaTags> Tags) {
        this.context = applicationContext;
        this.listaTagsOrig = Tags;
        this.listaTags = Tags; //  new ArrayList<>();
        inflater = (LayoutInflater.from(applicationContext));
        this.Confronto = VariabiliStatichePrefTags.getInstance().getStringaDiConfronto();

        ScriveQuantiSelezionati();

        VariabiliStatichePrefTags.getInstance().getTxtQuanti().setText("0/" +
                listaTagsOrig.size());
    }

    @Override
    public int getCount() {
        return listaTags.size();
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
        listaTags = new ArrayList<>();

        notifyDataSetChanged();

        if (VariabiliStatichePrefTags.getInstance().isSoloSelezionati()) {
            for (int i = 0; i < listaTagsOrig.size(); i++) {
                String NomeTag = listaTagsOrig.get(i).getTag();

                if (Confronto.contains(NomeTag + ";")) {
                    listaTags.add(listaTagsOrig.get(i));
                }
            }
        } else {
            for (int i = 0; i < listaTagsOrig.size(); i++) {
                String NomeArtista = listaTagsOrig.get(i).getTag();
                boolean Ok = false;
                if (!Filtro.isEmpty()) {
                    if (NomeArtista.toUpperCase().contains(Filtro.toUpperCase())) {
                        Ok = true;
                    }
                } else {
                    Ok = true;
                }
                if (Ok) {
                    listaTags.add(listaTagsOrig.get(i));
                }
            }
        }

        VariabiliStatichePrefTags.getInstance().getTxtQuanti().setText(listaTags.size() + "/" +
                listaTagsOrig.size());

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
        view = inflater.inflate(R.layout.lista_tags, null);

        if (i < listaTags.size()) {
            String NomeTag = listaTags.get(i).getTag();

            CheckBox chkSelezionato = (CheckBox) view.findViewById(R.id.chkSelezionato);
            if (Confronto.contains(NomeTag + ";")) {
                chkSelezionato.setChecked(true);
            } else {
                chkSelezionato.setChecked(false);
            }
            chkSelezionato.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSelezionato.isChecked()) {
                        Confronto += NomeTag + ";";
                    } else {
                        Confronto = Confronto.replace(NomeTag + ";", "");
                    }

                    ScriveQuantiSelezionati();

                    VariabiliStatichePrefTags.getInstance().ImpostaStringa(Confronto);
                }
            });

            TextView tTag = (TextView) view.findViewById(R.id.txtTag);
            tTag.setText(NomeTag);

            tTag.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStatichePrefTags.getInstance().setIdTagSelezionato(Integer.toString(listaTags.get(i).getIdTag()));
                    VariabiliStatichePrefTags.getInstance().getTxtTagSelezionato().setText(NomeTag);
                }
            });
        }

        return view;
    }
}
