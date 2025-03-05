package com.looigi.wallpaperchanger2.classeGps;

import android.content.Context;

import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
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

        PuliscePunti();
    }

    public void LeggePunti(String dataOdierna) {
        db_dati_gps db = new db_dati_gps(context);

        dataAttuale = new Date();

        listaGPS = db.RitornaPosizioni(dataOdierna);
        listaGPS = togliePuntiEccessivi(listaGPS);

        UtilityGPS.getInstance().ScriveLog(context, "Gestione_GPS",
                "Lettura punti: " + listaGPS.size()
        );

        calcolaDistanza();

        db.ChiudeDB();
    }

    public void ChiudeMaschera() {
        listaGPS = null;
    }

    private List<StrutturaGps> togliePuntiEccessivi(List<StrutturaGps> list) {
        List<StrutturaGps> listaGPS = new ArrayList<>();

        if (listaGPS.size() <= VariabiliStaticheGPS.getInstance().getQuantiPuntiSumappa()) {
            listaGPS = list;
        } else {
            int passo = (int) Math.round((float) list.size() /
                    VariabiliStaticheGPS.getInstance().getQuantiPuntiSumappa());
            UtilityGPS.getInstance().ScriveLog(context, "Gestione_GPS",
                    "Passo per toglie punti eccessivi: " + passo
                    );
            for (int i = 0; i < list.size(); i += passo) {
                listaGPS.add(list.get(i));
            }
            UtilityGPS.getInstance().ScriveLog(context, "Gestione_GPS",
                    "Punti totali tagliati: " + listaGPS.size()
            );
        }

        return listaGPS;
    }

    public void AggiungePosizione(StrutturaGps g) {
        listaGPS.add(g);

        listaGPS = togliePuntiEccessivi(listaGPS);

        long d = VariabiliStaticheGPS.getInstance().getDistanzaTotale();
        long dist = Math.round(g.getDistanza());
        VariabiliStaticheGPS.getInstance().setDistanzaTotale(d + dist);
        conta++;
        if (conta > 10) {
            conta = 0;
            UtilityGPS.getInstance().ScriveLog(context, "Gestione_GPS",
                    "Punti totali memorizzati su lista: " + listaGPS.size()
            );
            GestioneNotificheTasti.getInstance().AggiornaNotifica();
        }
    }

    public void PuliscePunti() {
        listaGPS = new ArrayList<>();
    }

    public int RitornaQuantiPunti() {
        int rit = 0;

        if (listaGPS != null) {
            rit = listaGPS.size();
        }

        return rit;
    }

    public List<StrutturaGps> RitornaPunti() {
        calcolaDistanza();

        return listaGPS;
    }

    private void calcolaDistanza() {
        long d = 0;
        for (StrutturaGps g : listaGPS) {
            d += (long) g.getDistanza();
        }
        VariabiliStaticheGPS.getInstance().setDistanzaTotale(d);
    }
}
