package com.looigi.wallpaperchanger2.ModificheCodice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ModificheCodice.Strutture.StrutturaConteggi;
import com.looigi.wallpaperchanger2.ModificheCodice.VariabiliStaticheModificheCodice;
import com.looigi.wallpaperchanger2.ModificheCodice.webService.ChiamateWSModifiche;

import java.util.List;

public class AdapterListenerConteggi extends BaseAdapter {
    private Context context;
    private List<StrutturaConteggi> listaConteggi;
    private LayoutInflater inflter;

    public AdapterListenerConteggi(Context applicationContext, List<StrutturaConteggi> Conteggi) {
        this.context = applicationContext;
        this.listaConteggi = Conteggi;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaConteggi.size();
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
        view = inflter.inflate(R.layout.lista_conteggi, null);

        String Sezione = listaConteggi.get(i).getSezione();
        int Quante = listaConteggi.get(i).getQuante();

        TextView txtSezione = view.findViewById(R.id.txtSezione);
        txtSezione.setText(Sezione);
        TextView txtQuante = view.findViewById(R.id.txtQuante);
        txtQuante.setText(Integer.toString(Quante));

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int idProgetto = listaConteggi.get(i).getIdProgetto();
                int idSezione = listaConteggi.get(i).getIdSezione();
                int idModulo = listaConteggi.get(i).getIdModulo();
                String Progetto = listaConteggi.get(i).getProgetto();
                String Modulo = listaConteggi.get(i).getModulo();
                String Sezione = listaConteggi.get(i).getSezione();

                int spinnerPositionP = VariabiliStaticheModificheCodice.getInstance().getAdapterProgetti().getPosition(Progetto);
                VariabiliStaticheModificheCodice.getInstance().getSpnProgetto().setSelection(spinnerPositionP);

                int spinnerPositionM = VariabiliStaticheModificheCodice.getInstance().getAdapterModuli().getPosition(Modulo);
                VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setSelection(spinnerPositionM);

                int spinnerPositionS = VariabiliStaticheModificheCodice.getInstance().getAdapterSezioni().getPosition(Sezione);
                VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setSelection(spinnerPositionS);

                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaModifiche(Integer.toString(idProgetto), Integer.toString(idModulo), Integer.toString(idSezione));
            }
        });


        return view;
    }
}
