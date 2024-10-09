package com.looigi.wallpaperchanger2.Segnale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.looigi.wallpaperchanger2.classiGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.Calendar;
import java.util.List;

public class ControlloSegnale extends PhoneStateListener {
    // private TelephonyManager mTelephonyManager;
    // private ControlloSegnale mPhoneStatelistener;
    // private long ultimoControllo = -1;

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);

        /* long ora = new Date().getTime();
        if (ora - ultimoControllo < 10000) {
            return;
        }
        ultimoControllo = ora; */

        Context context = UtilitiesGlobali.getInstance().tornaContextValido();

        boolean wifi = UtilitiesGlobali.getInstance().checkWifiOnAndConnected();
        VariabiliStaticheStart.getInstance().setCeWifi(wifi);
        if (VariabiliStaticheGPS.getInstance().getGestioneGPS() != null) {
            VariabiliStaticheGPS.getInstance().getGestioneGPS().ControlloAccSpegn(context);
        }

        List<CellSignalStrength> segnali = signalStrength.getCellSignalStrengths();
        if (!segnali.isEmpty()) {
            int mSignalStrength = segnali.get(0).getDbm();
            int mLevel = segnali.get(0).getLevel();
            // String livello = UtilitiesGlobali.getInstance().getLevelString(mLevel);

            // int mSignalStrength = signalStrength.getGsmSignalStrength();
            // mSignalStrength = (2 * mSignalStrength) - 113; // -> dBm

            VariabiliStaticheStart.getInstance().setLivelloSegnaleConnessione(mSignalStrength);
            VariabiliStaticheStart.getInstance().setLivello(mLevel);

            String tipoConnessione = getNetworkClass();
            VariabiliStaticheStart.getInstance().setTipoConnessione(tipoConnessione);

            int downSpeed = 0;
            int upSpeed = 0;

            if (context != null) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm != null) {
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    //should check null because in airplane mode it will be null
                    if (netInfo != null) {
                        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                        if (nc != null) {
                            downSpeed = nc.getLinkDownstreamBandwidthKbps();
                            upSpeed = nc.getLinkUpstreamBandwidthKbps();

                            VariabiliStaticheStart.getInstance().setVelocitaDownload(downSpeed);
                            VariabiliStaticheStart.getInstance().setVelocitaUpload(upSpeed);
                        }
                    } else {
                        VariabiliStaticheStart.getInstance().setVelocitaDownload(0);
                        VariabiliStaticheStart.getInstance().setVelocitaUpload(0);
                    }
                }
            }

            /* if (VariabiliStatichePlayer.getInstance().getTxtInformazioniPlayer() != null) {
                String testo = (wifi ? "WiFi" : "Mobile");
                if (!wifi) {
                    testo += " " + tipoConnessione;
                }
                testo += " - Livello segnale: " + mSignalStrength + " (" + livello + ")";
                testo += " - DL: " + downSpeed + " - UL: " + upSpeed;

                VariabiliStatichePlayer.getInstance().getTxtInformazioniPlayer().setText(testo);
            } */
        } else {
            VariabiliStaticheStart.getInstance().setLivelloSegnaleConnessione(-999);
            VariabiliStaticheStart.getInstance().setLivello(-1);
            VariabiliStaticheStart.getInstance().setTipoConnessione("");
            VariabiliStaticheStart.getInstance().setVelocitaDownload(0);
            VariabiliStaticheStart.getInstance().setVelocitaUpload(0);
        }

        Calendar calendar = Calendar.getInstance();
        String h = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        if (h.length() == 1) { h = "0" + h; };
        String m = Integer.toString(calendar.get(Calendar.MINUTE));
        if (m.length() == 1) { m = "0" + m; };
        String s = Integer.toString(calendar.get(Calendar.SECOND));
        if (s.length() == 1) { s = "0" + s; };

        String quando = h + ":" + m + ":" + s;
        VariabiliStaticheStart.getInstance().setUltimoControlloRete(quando);
        GestioneNotificheTasti.getInstance().AggiornaNotifica();
    }

    private static String getNetworkClass() {
        Context context = UtilitiesGlobali.getInstance().tornaContextValido();
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null || !info.isConnected())
                return "-"; // not connected
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return "WIFI";
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                int networkType = info.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:     // api< 8: replace by 11
                    case TelephonyManager.NETWORK_TYPE_GSM:      // api<25: replace by 16
                        return "2G";
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:   // api< 9: replace by 12
                    case TelephonyManager.NETWORK_TYPE_EHRPD:    // api<11: replace by 14
                    case TelephonyManager.NETWORK_TYPE_HSPAP:    // api<13: replace by 15
                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA: // api<25: replace by 17
                        return "3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:      // api<11: replace by 13
                    case TelephonyManager.NETWORK_TYPE_IWLAN:    // api<25: replace by 18
                    case 19: // LTE_CA
                        return "4G";
                    case TelephonyManager.NETWORK_TYPE_NR:       // api<29: replace by 20
                        return "5G";
                    default:
                        return "?";
                }
            }
        }
        return "?";
    }
}