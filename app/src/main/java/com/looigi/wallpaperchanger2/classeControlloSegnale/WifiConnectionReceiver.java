package com.looigi.wallpaperchanger2.classeControlloSegnale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Network;
import android.os.Build;

import com.looigi.wallpaperchanger2.classeGps.UtilityGPS;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;
import com.looigi.wallpaperchanger2.watchDog.VariabiliStaticheWatchdog;

public class WifiConnectionReceiver extends BroadcastReceiver {
    private boolean vecchioWifi;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean wifi = isWifiConnected(context);
        VariabiliStaticheStart.getInstance().setCeWifi(wifi);
        VariabiliStaticheStart.getInstance().setPresoWiFi(true);

        if (wifi != vecchioWifi) {
            UtilityGPS.getInstance().ScriveLog(context, "WIFI_WORKER",
                    "Controllo segnale segnale wifi. E' attivo: " + wifi);

            if (!VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
                if (wifi) {
                    if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                        VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS(
                                "Controllo Acc Spegn. Da " + "Controllo Segnale");
                    }
                } else {
                    if (!VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                        VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS(
                                "Controllo Acc Spegn. Da " + "Controllo Segnale");
                    }
                }
            }

            vecchioWifi = wifi;
        }

        String livello = UtilitiesGlobali.getInstance().getLevelString(
                VariabiliStaticheStart.getInstance().getLivello()
        );
        VariabiliStaticheWatchdog.getInstance().setSegnale((wifi ? "WiFi" : "Mobile") + ": " +
                livello);
    }

    private boolean isWifiConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = cm.getActiveNetwork();
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                return capabilities != null &&
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            } else {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                return networkInfo != null &&
                        networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                        networkInfo.isConnected();
            }
        }
        return false;
    }
}
