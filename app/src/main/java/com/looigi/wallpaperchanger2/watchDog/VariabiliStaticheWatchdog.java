package com.looigi.wallpaperchanger2.watchDog;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerArtisti;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaUtenti;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadCanzone;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadImmagine;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.InterrogazioneWSPlayer;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class VariabiliStaticheWatchdog {
    private static VariabiliStaticheWatchdog instance = null;

    private VariabiliStaticheWatchdog() {
    }

    public static VariabiliStaticheWatchdog getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheWatchdog();
        }

        return instance;
    }

    private Context context;
    private String Info1;
    private String Info2;
    private String Segnale;
    private String serv1;
    private String serv2;
    private String serv3;
    private String serv4;

    public void setContext(Context context) {
        this.context = context;
    }

    public String getServ1() {
        return serv1;
    }

    public String getServ2() {
        return serv2;
    }

    public String getServ3() {
        return serv3;
    }

    public String getServ4() {
        return serv4;
    }

    public void setSegnale(String segnale) {
        Segnale = segnale;
    }

    public String getInfo4() {
        String Ritorno = Segnale;

        if (context != null) {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            double availableMegs = Math.round((double) mi.availMem / 0x100000L);
            double percentAvail = mi.availMem / (double) mi.totalMem * 100.0;
            Ritorno += " - Free Mem. " + availableMegs + " (" + Double.toString(Math.round(percentAvail)) + "%)";
        }

        return Ritorno;
    }

    public void ControllaServizi() {
        isMyServiceRunning();
    }

    public String getInfo1() {
        return Info1;
    }

    public void setInfo1(String info1) {
        Info1 = info1;
    }

    public String getInfo2() {
        return Info2;
    }

    public void setInfo2(String info2) {
        Info2 = info2;
    }

    public String getInfo3() {
        String Ritorno = "";

        Ritorno = "GPS " + checkGPSStatus() + " (" + locationEnabled() + ") " +
            "Active: " + VariabiliStaticheGPS.getInstance().isGpsAttivo() + " - ";
        if (VariabiliStaticheGPS.getInstance().getMappa() != null) {
            Ritorno += "P.: " + VariabiliStaticheGPS.getInstance().getMappa().RitornaPunti().size() + " ";
        }
        Ritorno += "D.: " + VariabiliStaticheGPS.getInstance().getDistanzaTotale() + " ";
        Ritorno += "PS: " + VariabiliStaticheGPS.getInstance().isNonScriverePunti();

        return Ritorno;
    }

    private boolean checkGPSStatus() {
        if (context != null) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            return false;
        }
    }

    private String locationEnabled () {
        if (context != null) {
            LocationManager lm = (LocationManager)
                    context.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception e) {
                // e.printStackTrace() ;
            }
            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {
                // e.printStackTrace() ;
            }

            return (gps_enabled ? "Y" : "N") + (network_enabled ? "Y" : "N");
        } else {
            return "";
        }
    }

    private void isMyServiceRunning() {
        boolean Gps = false;
        boolean TastiCuffie = false;
        boolean Globale = false;
        boolean Segnale = false;

        if (context != null) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                String servizio = service.service.getClassName();
                String[] s = servizio.split("\\.");
                String Nome = s[s.length - 1];

                switch (Nome) {
                    case "GestioneTastiCuffieNuovo":
                        TastiCuffie = true;
                        break;
                    case "ControlloSegnale2":
                        Segnale = true;
                        break;
                    case "ServizioInterno":
                        Globale = true;
                        break;
                    case "GestioneGPS":
                        Gps = true;
                        break;
                }
            }
        }

        serv1 = (Globale ? "On" : "Off");
        serv2 = (TastiCuffie ? "On" : "Off");
        serv3 = (Segnale ? "On" : "Off");
        serv4 = (Gps ? "On" : "Off");
    }
}
