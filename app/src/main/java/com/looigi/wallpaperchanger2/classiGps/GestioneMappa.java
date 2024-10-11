package com.looigi.wallpaperchanger2.classiGps;

import android.content.Context;

import com.looigi.wallpaperchanger2.classiGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestioneMappa {
    private List<StrutturaGps> listaGPS;
    private Context context;
    private Date dataAttuale;
    private int conta = 0;

    public GestioneMappa(Context context) {
        this.context = context;
    }

    public void LeggePunti(String dataOdierna) {
        db_dati_gps db = new db_dati_gps(context);

        dataAttuale = new Date();

        listaGPS = db.RitornaPosizioni(dataOdierna);
        listaGPS = togliePuntiEccessivi(listaGPS);

        calcolaDistanza();
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

        long d = VariabiliStaticheGPS.getInstance().getDistanzaTotale();
        long dist = Math.round(g.getDistanza());
        VariabiliStaticheGPS.getInstance().setDistanzaTotale(d + dist);
        conta++;
        if (conta > 10) {
            conta = 0;
            GestioneNotificheTasti.getInstance().AggiornaNotifica();
        }
    }

    public void PuliscePunti() {
        listaGPS = new ArrayList<>();
    }

    public List<StrutturaGps> RitornaPunti() {
        calcolaDistanza();

        return listaGPS;
    }

    private void calcolaDistanza() {
        long d = 0;
        for (StrutturaGps g : listaGPS) {
            d += g.getDistanza();
        }
        VariabiliStaticheGPS.getInstance().setDistanzaTotale(d);
    }
}
