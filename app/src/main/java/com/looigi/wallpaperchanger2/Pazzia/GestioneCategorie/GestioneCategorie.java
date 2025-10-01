package com.looigi.wallpaperchanger2.Pazzia.GestioneCategorie;

import android.content.Context;
import android.text.TextUtils;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.Pazzia.VariabiliStatichePazzia;
import com.looigi.wallpaperchanger2.Video.db_dati_video;

import java.util.ArrayList;
import java.util.List;

public class GestioneCategorie {
    // Questa classe prende tutte le categorie che si chiamano uguali nelle immagini e nei video

    public void ScannaCategorie(Context context) {
        db_dati_immagini db = new db_dati_immagini(context);
        List<StrutturaImmaginiCategorie> listaCategorieImmagini = db.LeggeCategorie();
        db.ChiudeDB();

        db_dati_video db2 = new db_dati_video(context);
        List<String> listaCategorieVideo = db2.LeggeCategorie();
        db2.ChiudeDB();

        List<StrutturaCategorie> listaImm = new ArrayList<>();
        for (StrutturaImmaginiCategorie catImm: listaCategorieImmagini) {
            if (!catImm.getCategoria().toUpperCase().trim().equals("TUTTE") && !catImm.getCategoria().isEmpty()) {
                String NomeImm = SistemaCategoria(catImm.getCategoria());

                StrutturaCategorie s = new StrutturaCategorie();
                s.setCategoria(catImm.getCategoria());
                s.setCategoriaModificata(NomeImm);

                listaImm.add(s);
            }
        };
        List<StrutturaCategorie> listaVid = new ArrayList<>();
        for (String catVid : listaCategorieVideo) {
            if (!catVid.toUpperCase().trim().equals("TUTTE") && !catVid.isEmpty()) {
                String NomeVid = SistemaCategoria(catVid);

                StrutturaCategorie s = new StrutturaCategorie();
                s.setCategoria(catVid);
                s.setCategoriaModificata(NomeVid);

                listaVid.add(s);
            }
        };

        List<StrutturaCategorieFinali> listaFinale = new ArrayList<>();
        for (StrutturaCategorie catImm: listaImm) {
            for (StrutturaCategorie catVid : listaVid) {
                if (catVid.getCategoriaModificata().equalsIgnoreCase(catImm.getCategoriaModificata())) {
                    boolean ok = true;
                    for (StrutturaCategorieFinali f : listaFinale) {
                        if (f.getCategoriaImm().equals(catImm.getCategoriaModificata())) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        StrutturaCategorieFinali s = new StrutturaCategorieFinali();
                        s.setCategoriaImm(catImm.getCategoriaModificata());
                        s.setCategoriaVid(catVid.getCategoriaModificata());
                        s.setCategoriaOriginaleImm(catImm.getCategoria());
                        s.setCategoriaOriginaleVid(catVid.getCategoria());

                        listaFinale.add(s);
                    }
                }
            }
        }

        VariabiliStatichePazzia.getInstance().setListaCategoriePresentiImmVid(listaFinale);
    }

    private String SistemaCategoria(String Categoria) {
        String Ritorno = "";

        if (Categoria.contains("\\")) {
            String[] c = Categoria.split("\\\\");
            Ritorno = c[c.length - 1];
        } else {
            Ritorno = Categoria.trim();
        }

        Ritorno = Ritorno.replace("_", ";");
        Ritorno = Ritorno.replace("-", ";");
        Ritorno = Ritorno.replace(" ", ";");

        for (int i = 0; i < Ritorno.length(); i++) {
            String Carattere = Ritorno.substring(i, i + 1);
            if (!TextUtils.isDigitsOnly(Carattere) && Carattere.equals(Carattere.toUpperCase())) {
                if (i > 0) {
                    String Prima = Ritorno.substring(i - 1, i);
                    if (i + 2 < Ritorno.length()) {
                        String Dopo = Ritorno.substring(i + 1, i + 2);
                        if (!Prima.equals(";") && !Dopo.equals(Dopo.toUpperCase())) {
                            try {
                                Ritorno = Ritorno.substring(0, i) + ";" + Ritorno.substring(i);
                            } catch (Exception ignored) {

                            }
                            i++;
                        }
                    }
                }
            }
        }

        return Ritorno;
    }
}
