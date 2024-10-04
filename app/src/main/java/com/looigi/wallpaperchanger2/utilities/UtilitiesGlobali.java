package com.looigi.wallpaperchanger2.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellSignalStrength;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.classeMostraImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classiDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiPlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classiPlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classiStandard.LogInterno;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;

public class UtilitiesGlobali {
    private static UtilitiesGlobali instance = null;

    private UtilitiesGlobali() {
    }

    public static UtilitiesGlobali getInstance() {
        if (instance == null) {
            instance = new UtilitiesGlobali();
        }

        return instance;
    }

    public Activity tornaActivityValida() {
        Activity act = VariabiliStaticheWallpaper.getInstance().getMainActivity();
        if (act == null) {
            act = VariabiliStaticheStart.getInstance().getMainActivity();
        }
        if (act == null) {
            act = VariabiliStaticheDetector.getInstance().getMainActivity();
        }
        if (act == null) {
            act = VariabiliStatichePlayer.getInstance().getAct();
        }
        if (act == null) {
            act = VariabiliStaticheMostraImmagini.getInstance().getAct();
        }

        return act;
    }

    public Context tornaContextValido() {
        Context ctx = VariabiliStaticheWallpaper.getInstance().getContext();
        if (ctx == null) {
            ctx = VariabiliStaticheStart.getInstance().getContext();
        }
        if (ctx == null) {
            ctx = VariabiliStaticheDetector.getInstance().getContext();
        }
        if (ctx == null) {
            ctx = VariabiliStatichePlayer.getInstance().getContext();
        }
        if (ctx == null) {
            ctx = VariabiliStaticheMostraImmagini.getInstance().getCtx();
        }

        return ctx;
    }

    public void ScriveLog(Context context, String Applicazione,  String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
            VariabiliStaticheStart.getInstance().getLog().ScriveLog(Applicazione, Maschera,  Log);
            // }
        } else {

        }
    }

    public void ApreToast(Context context, String messaggio) {
        if (VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
            if (context != null && act != null) {
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context,
                                VariabiliStaticheWallpaper.channelName + ": " + messaggio,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public String getLevelString(int level) {
        switch(level) {
            case CellSignalStrength.SIGNAL_STRENGTH_GOOD:
                return "GOOD";
            case CellSignalStrength.SIGNAL_STRENGTH_GREAT:
                return "GREAT";
            case CellSignalStrength.SIGNAL_STRENGTH_MODERATE:
                return "MODERATE";
            case CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN:
                return "UNKNOWN";
            case CellSignalStrength.SIGNAL_STRENGTH_POOR:
                return "POOR";
            default:
                throw new RuntimeException("Unsupported level " + level);
        }
    }

    public boolean checkWifiOnAndConnected() {
        Context context = UtilitiesGlobali.instance.tornaContextValido();
        if (context != null) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

                if (wifiInfo.getNetworkId() == -1) {
                    return false; // Not connected to an access point
                }

                return true; // Connected to an access point
            } else {
                return false; // Wi-Fi adapter is OFF
            }
        } else {
            return false; // Context nullo
        }
    }
}
