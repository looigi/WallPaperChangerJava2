package com.looigi.wallpaperchanger2.Mappe.MappeEGps;

import android.content.Context;

import com.looigi.wallpaperchanger2.Mappe.MappeEGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.Notifiche.notificaTasti.GestioneNotificheTasti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestioneMappa {
    // private List<StrutturaGps> listaGPS;
    private List<StrutturaGps> listaGPSCompleta;
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

        listaGPSCompleta = db.RitornaPosizioni(dataOdierna);

        /* if (listaGPS.size() > VariabiliStaticheGPS.getInstance().getQuantiPuntiSumappa()) {
            listaGPS = togliePuntiEccessivi(listaGPS);
        } */

        UtilityGPS.getInstance().ScriveLog(context, "Gestione_GPS",
                "Lettura punti: " + listaGPSCompleta.size() //  + " (" + listaGPSC.size() + ")"
        );

        calcolaDistanza();

        db.ChiudeDB();
    }

    public void ChiudeMaschera() {
        listaGPSCompleta = null;
    }

    public List<StrutturaGps> togliePuntiEccessivi(List<StrutturaGps> list) {
        VariabiliStaticheGPS.getInstance().setPuntiTotali(list.size());

        int maxPunti = -1;

        if (VariabiliStaticheGPS.getInstance().isDisegnaPathComePolyline()) {
            maxPunti = 1000;
        } else {
            maxPunti = VariabiliStaticheGPS.getInstance().getQuantiPuntiSumappa();
        }

        if (list.size() > maxPunti) {
            // list = list;
        // } else {
            int passo = (int) Math.round((float) list.size() /
                    maxPunti);
            UtilityGPS.getInstance().ScriveLog(context, "Gestione_GPS",
                    "Passo per toglie punti eccessivi: " + passo
                    );
            List<StrutturaGps> listaGPSSezionata = new ArrayList<>();
            for (int i = 0; i < list.size(); i += passo) {
                listaGPSSezionata.add(list.get(i));
            }
            list = listaGPSSezionata;

            UtilityGPS.getInstance().ScriveLog(context, "Gestione_GPS",
                    "Punti totali tagliati: " + listaGPSSezionata.size()
            );
        }

        return list;
    }

    public void AggiungePosizione(StrutturaGps g) {
        // listaGPS.add(g);
        listaGPSCompleta.add(g);

        /* if (listaGPS.size() > VariabiliStaticheGPS.getInstance().getQuantiPuntiSumappa()) {
            listaGPS = togliePuntiEccessivi(listaGPS);
        } */

        long d = VariabiliStaticheGPS.getInstance().getDistanzaTotale();
        long dist = Math.round(g.getDistanza());
        VariabiliStaticheGPS.getInstance().setDistanzaTotale(d + dist);
        conta++;
        if (conta > 10) {
            conta = 0;
            UtilityGPS.getInstance().ScriveLog(context, "Gestione_GPS",
                    "Punti totali memorizzati su lista: " + listaGPSCompleta.size()
            );
            GestioneNotificheTasti.getInstance().AggiornaNotifica();
        }
    }

    public void PuliscePunti() {
        listaGPSCompleta = new ArrayList<>();
    }

    public int RitornaQuantiPunti() {
        int rit = 0;

        if (listaGPSCompleta != null) {
            rit = listaGPSCompleta.size();
        }

        return rit;
    }

    public List<StrutturaGps> RitornaPunti() {
        calcolaDistanza();

        return listaGPSCompleta;
    }

    private void calcolaDistanza() {
        long d = 0;

        for (StrutturaGps g : listaGPSCompleta) {
            d += (long) g.getDistanza();
        }

        VariabiliStaticheGPS.getInstance().setDistanzaTotale(d);
    }
}
