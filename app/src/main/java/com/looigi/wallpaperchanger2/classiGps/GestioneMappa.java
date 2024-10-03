package com.looigi.wallpaperchanger2.classiGps;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestioneMappa {
    private List<StrutturaGps> listaGPS;
    private Context context;
    private Date dataAttuale;

    public GestioneMappa(Context context) {
        this.context = context;
    }

    public void LeggePunti(String dataOdierna) {
        db_dati_gps db = new db_dati_gps(context);

        dataAttuale = new Date();

        listaGPS = db.RitornaPosizioni(dataOdierna);
        listaGPS = togliePuntiEccessivi(listaGPS);
    }

    public void ChiudeMaschera() {
        listaGPS = null;
    }

    private List<StrutturaGps> togliePuntiEccessivi(List<StrutturaGps> list) {
        List<StrutturaGps> listaGPS = new ArrayList<>();

        if (listaGPS.size() <= VariabiliStaticheGPS.quantiPuntiSumappa) {
            listaGPS = list;
        } else {
            int passo = (int) Math.round((float) list.size() / VariabiliStaticheGPS.quantiPuntiSumappa);
            for (int i = 0; i < list.size(); i += passo) {
                listaGPS.add(list.get(i));
            }
        }

        return listaGPS;
    }

    public void AggiungePosizione(StrutturaGps g) {
        listaGPS.add(g);
    }

    public void PuliscePunti() {
        listaGPS = new ArrayList<>();
    }

    public List<StrutturaGps> RitornaPunti() {
        return listaGPS;
    }
}
