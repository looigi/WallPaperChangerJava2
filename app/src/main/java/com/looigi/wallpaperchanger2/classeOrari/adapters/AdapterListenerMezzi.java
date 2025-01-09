package com.looigi.wallpaperchanger2.classeOrari.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.UtilityOrari;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerMezzi extends BaseAdapter {
    private Context context;
    private List<StrutturaMezzi> listaMezzi;
    private List<StrutturaMezzi> listaMezziOrig;
    private LayoutInflater inflter;
    private boolean ModalitaPerNuovo;
    private boolean Andata;

    public AdapterListenerMezzi(Context applicationContext, List<StrutturaMezzi> Mezzi,
                                boolean ModalitaPerNuovo, boolean Andata) {
        this.context = applicationContext;
        if (ModalitaPerNuovo) {
            this.listaMezziOrig = Mezzi;
            this.listaMezzi = Mezzi;
        } else {
            this.listaMezziOrig = Mezzi;
            this.listaMezzi = Mezzi;
        }
        this.Andata = Andata;
        inflter = (LayoutInflater.from(applicationContext));
        this.ModalitaPerNuovo = ModalitaPerNuovo;
    }

    @Override
    public int getCount() {
        return listaMezzi.size();
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
        listaMezzi = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaMezziOrig.size(); i++) {
            String NomeImmagine = listaMezziOrig.get(i).getMezzo() + " " + listaMezziOrig.get(i).getDettaglio();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaMezzi.add(listaMezziOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (ModalitaPerNuovo) {
            view = inflter.inflate(R.layout.lista_mezzi_per_nuovo, null);
        } else {
            view = inflter.inflate(R.layout.lista_mezzi, null);
        }

        TextView txtSito = (TextView) view.findViewById(R.id.txtMezzo);
        txtSito.setText(listaMezzi.get(i).getMezzo() + " " +
                (listaMezzi.get(i).getDettaglio() == null ? "" : listaMezzi.get(i).getDettaglio())
        );

        if (!ModalitaPerNuovo) {
            ImageView imgSu = view.findViewById(R.id.imgSu);
            imgSu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (i > 0) {
                        StrutturaMezzi selezionata = listaMezzi.get(i);
                        StrutturaMezzi precedente = listaMezzi.get(i - 1);
                        listaMezzi.set(i, precedente);
                        listaMezzi.set(i - 1, selezionata);
                    }

                    if (Andata) {
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziAndata(listaMezzi);

                        UtilityOrari.getInstance().AggiornaListaMezziAndata(context, false, true);
                    } else {
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziRitorno(listaMezzi);

                        UtilityOrari.getInstance().AggiornaListaMezziRitorno(context, false, false);
                    }
                }
            });

            ImageView imgGiu = view.findViewById(R.id.imgGiu);
            imgGiu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (i < listaMezzi.size() - 1) {
                        StrutturaMezzi selezionata = listaMezzi.get(i);
                        StrutturaMezzi prossima = listaMezzi.get(i + 1);
                        listaMezzi.set(i, prossima);
                        listaMezzi.set(i + 1, selezionata);
                    }

                    if (Andata) {
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziAndata(listaMezzi);

                        UtilityOrari.getInstance().AggiornaListaMezziAndata(context, false, true);
                    } else {
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziRitorno(listaMezzi);

                        UtilityOrari.getInstance().AggiornaListaMezziRitorno(context, false, false);
                    }
                }
            });

            ImageView imgElimina = view.findViewById(R.id.imgElimina);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listaMezzi.remove(i);

                    if (Andata) {
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziAndata(listaMezzi);

                        UtilityOrari.getInstance().AggiornaListaMezziAndata(context, false, true);
                    } else {
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziRitorno(listaMezzi);

                        UtilityOrari.getInstance().AggiornaListaMezziRitorno(context, false, false);
                    }
                }
            });
        } else {
            ImageView imgAggiunge = view.findViewById(R.id.imgAggiunge);
            imgAggiunge.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StrutturaMezzi s = listaMezzi.get(i);

                    if (Andata) {
                        List<StrutturaMezzi> lista = VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziAndata();
                        lista.add(s);
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziAndata(lista);

                        UtilityOrari.getInstance().AggiornaListaMezziAndata(context, false, true);
                    } else {
                        List<StrutturaMezzi> lista = VariabiliStaticheOrari.getInstance().getDatiGiornata().getMezziRitorno();
                        lista.add(s);
                        VariabiliStaticheOrari.getInstance().getDatiGiornata().setMezziRitorno(lista);

                        UtilityOrari.getInstance().AggiornaListaMezziRitorno(context, false, false);
                    }

                    VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.GONE);
                    VariabiliStaticheOrari.getInstance().getLayNuovoDato().setVisibility(LinearLayout.GONE);
                }
            });
        }
        return view;
    }
}
