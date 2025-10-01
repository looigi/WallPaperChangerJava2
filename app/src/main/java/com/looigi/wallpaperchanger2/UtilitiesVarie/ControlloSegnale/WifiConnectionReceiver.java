package com.looigi.wallpaperchanger2.UtilitiesVarie.ControlloSegnale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Network;
import android.os.Build;

import com.looigi.wallpaperchanger2.Detector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.GestioneGPS;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.UtilityGPS;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;
import com.looigi.wallpaperchanger2.WatchDog.VariabiliStaticheWatchdog;

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

            if (VariabiliStaticheGPS.getInstance().getGestioneGPS() == null) {
                if (VariabiliStaticheStart.getInstance().isDetector() &&
                        !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                        VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {
                    VariabiliStaticheGPS.getInstance().setGestioneGPS(new GestioneGPS());

                    UtilityGPS.getInstance().ScriveLog(context, "WIFI_WORKER",
                            "Controllo segnale segnale wifi. Intent gps nullo. Lo attivo");

                    VariabiliStaticheStart.getInstance().setIntentGPS(new Intent(context, GestioneGPS.class));
                    context.startForegroundService(VariabiliStaticheStart.getInstance().getIntentGPS());
                }
            }

            if (VariabiliStaticheGPS.getInstance().getGestioneGPS() != null) {
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
