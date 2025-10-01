package com.looigi.wallpaperchanger2.Orari.impostazioni;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class UtilityImpostazioniOrari {
    private static UtilityImpostazioniOrari instance = null;

    private UtilityImpostazioniOrari() {
    }

    public static UtilityImpostazioniOrari getInstance() {
        if (instance == null) {
            instance = new UtilityImpostazioniOrari();
        }

        return instance;
    }

    public void ImpostaAttesa(boolean Come) {
        if (Come) {
            VariabiliStaticheImpostazioniOrari.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheImpostazioniOrari.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (IOException ex) {
        }

        return p1;
    }
}
