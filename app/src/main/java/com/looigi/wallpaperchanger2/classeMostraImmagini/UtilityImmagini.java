package com.looigi.wallpaperchanger2.classeMostraImmagini;

import android.content.Context;

import com.looigi.wallpaperchanger2.classiWallpaper.WebServices.ChiamateWs;
import com.looigi.wallpaperchanger2.classeMostraImmagini.webservice.DownloadImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UtilityImmagini {
    private static UtilityImmagini instance = null;

    private UtilityImmagini() {
    }

    public static UtilityImmagini getInstance() {
        if (instance == null) {
            instance = new UtilityImmagini();
        }

        return instance;
    }

    public void RitornaProssimaImmagine(Context context) {
        ChiamateWs ws = new ChiamateWs(context);
        ws.RitornaProssimaImmagine(
                VariabiliStaticheMostraImmagini.getInstance().getIdCategoria(),
                VariabiliStaticheMostraImmagini.getInstance().getFiltro(),
                VariabiliStaticheMostraImmagini.getInstance().getIdImmagine(),
                VariabiliStaticheMostraImmagini.getInstance().getRandom()
        );
    }

    public void TornaIndietro(Context context) {
        if (!VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().isEmpty()) {
            int ultima = VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().size() - 1;
            StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().get(
                    ultima);

            VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(s);
            VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(s.getIdCategoria());
            VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(s.getIdImmagine());

            new DownloadImage(context, s.getUrlImmagine(),
                    VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(s.getUrlImmagine());

            List<StrutturaImmaginiLibrary> lista = new ArrayList<>();
            for (int i = 0; i < ultima; i++) {
                lista.add(VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().get(i));
            }
            VariabiliStaticheMostraImmagini.getInstance().setImmaginiCaricate(lista);

            if (VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().isEmpty()) {
                VariabiliStaticheMostraImmagini.getInstance().setImmaginiCaricate(new ArrayList<>());
            }
        }
    }

    public StrutturaImmaginiLibrary prendeStruttura(JSONObject j) {
        if (j != null) {
            // {"idImmagine": 522,"idCategoria": 1,"Categoria": "21_Sextury","Alias": ";^$$$$-$$$ ;",
            // "Tag": "","Cartella": "0004","NomeFile": "21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "DimensioneFile": 308264,"DataCreazione": "10/08/2024 12:14:26","DataModifica": "04/02/2024 04:56:29",
            // "DimensioniImmagine": "1280x834",
            // "UrlImmagine": "http://looigi.no-ip.biz:1085/Materiale/newPLibrary/21_Sextury/0004/21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "PathImmagine": "*S**S*192.168.0.33*S*Public*S*Materiale*S*newPLibrary*S*21_Sextury*S*0004*S*21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "EsisteImmagine": "True","ImmaginiCategoria": 758}
            StrutturaImmaginiLibrary si = new StrutturaImmaginiLibrary();
            try {
                si.setIdImmagine(j.getInt("idImmagine"));
                si.setIdCategoria(j.getInt("idCategoria"));
                si.setCategoria(j.getString("Categoria"));
                si.setAlias(j.getString("Alias"));
                si.setTag(j.getString("Tag"));
                si.setCartella(j.getString("Cartella"));
                si.setNomeFile(j.getString("NomeFile"));
                si.setDimensioneFile(j.getInt("DimensioneFile"));
                si.setDataCreazione(j.getString("DataCreazione"));
                si.setDataModifica(j.getString("DataModifica"));
                si.setDimensioniImmagine(j.getString("DimensioniImmagine"));
                si.setUrlImmagine(j.getString("UrlImmagine"));
                si.setPathImmagine(j.getString("PathImmagine"));
                si.setEsisteImmagine(j.getString("EsisteImmagine").equals("True"));
                si.setImmaginiCategoria(j.getInt("ImmaginiCategoria"));
                return si;
            } catch (JSONException e) {
                return null;
            }

        } else {
            return null;
        }
    }
}
