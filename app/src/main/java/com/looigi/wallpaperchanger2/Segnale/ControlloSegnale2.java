package com.looigi.wallpaperchanger2.Segnale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.looigi.wallpaperchanger2.classeGps.GestioneGPS;
import com.looigi.wallpaperchanger2.classeGps.UtilityGPS;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;
import com.looigi.wallpaperchanger2.watchDog.VariabiliStaticheWatchdog;

import java.util.Calendar;
import java.util.List;

public class ControlloSegnale2 extends Service {
    private TelephonyManager mTelephonyManager;
    private PhoneStateListener mPhoneStateListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTelephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        mPhoneStateListener = new PhoneStateListener(){
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                CambiatoSegnale(signalStrength);
                super.onSignalStrengthsChanged(signalStrength);
            }

            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                CambiatoStatoServizio(serviceState);
                super.onServiceStateChanged(serviceState);
            }
        };
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Register the listener here.
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS|PhoneStateListener.LISTEN_SERVICE_STATE);
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean vecchioWifi;
    private String vecchioTipoConnessione;
    private int vecchioLivello;

    private void CambiatoSegnale(SignalStrength signalStrength){
        Context context = UtilitiesGlobali.getInstance().tornaContextValido();

        boolean wifi = UtilitiesGlobali.getInstance().checkWifiOnAndConnected();
        VariabiliStaticheStart.getInstance().setCeWifi(wifi);

        String NomeMaschera = "Gestione_GPS";
        // UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
        //         "Controllo segnale segnale wifi. Attivo: " + wifi);

            // if (VariabiliStaticheGPS.getInstance().isBloccoPerWifi()) {
                if (vecchioWifi != wifi) {
                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                            "Attivazione gps. Wifi: " + wifi);

                    UtilitiesGlobali.getInstance().ImpostaServizioGPS(context, "CONTROLLO_ATTIVAZIONE");
                }
            // }
        /* } else {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Classe GPS non istanziata...");

            VariabiliStaticheGPS.getInstance().setGestioneGPS(new GestioneGPS());
            VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaTimer(context);
            VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS("On Create Servizio GPS");

            if (VariabiliStaticheGPS.getInstance().isBloccoPerWifi()) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Controllo GPS Per cambio segnale wifi: " + wifi);

                VariabiliStaticheGPS.getInstance().getGestioneGPS().ControlloAccSpegn(context);
            }
        } */

        String tipoConnessione = "";
        int mLevel = 0;

        List<CellSignalStrength> segnali = signalStrength.getCellSignalStrengths();
        if (!segnali.isEmpty()) {
            int mSignalStrength = segnali.get(0).getDbm();
            mLevel = segnali.get(0).getLevel();

            // String livello = UtilitiesGlobali.getInstance().getLevelString(mLevel);

            // int mSignalStrength = signalStrength.getGsmSignalStrength();
            // mSignalStrength = (2 * mSignalStrength) - 113; // -> dBm

            VariabiliStaticheStart.getInstance().setLivelloSegnaleConnessione(mSignalStrength);
            VariabiliStaticheStart.getInstance().setLivello(mLevel);

            tipoConnessione = getNetworkClass();
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

            String livello = UtilitiesGlobali.getInstance().getLevelString(
                    VariabiliStaticheStart.getInstance().getLivello()
            );
            VariabiliStaticheWatchdog.getInstance().setSegnale((wifi ? "WiFi" : "Mobile") + ": " +
                     livello);

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

        if (wifi != vecchioWifi || !tipoConnessione.equals(vecchioTipoConnessione)) {
            // || mLevel != vecchioLivello) {
            String quando = UtilitiesGlobali.getInstance().RitornaOra();

            VariabiliStaticheStart.getInstance().setUltimoControlloRete(quando);
            GestioneNotificheTasti.getInstance().AggiornaNotifica();
        }

        vecchioWifi = wifi;
        vecchioLivello = mLevel;
        vecchioTipoConnessione = tipoConnessione;
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

    private void CambiatoStatoServizio(ServiceState serviceState){
        // Log.d(TAG, "Service State changed! New state = "+serviceState.getState());
    }

    @Override
    public void onDestroy() {
        // Log.d(TAG, "Shutting down the Service");
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }
}