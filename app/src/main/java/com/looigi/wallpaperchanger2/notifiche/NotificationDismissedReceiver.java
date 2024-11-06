package com.looigi.wallpaperchanger2.notifiche;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeGps.GestioneGPS;
import com.looigi.wallpaperchanger2.classeGps.GestioneNotificaGPS;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classePlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classeWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.notificaTasti.VariabiliStaticheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.Objects;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = Objects.requireNonNull(intent.getExtras()).getInt("com.looigi.wallpaperchanger2.notificationId");

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                // switch (notificationId) {
                //  case 1: // NOTIFICA TASTI
                GestioneNotificheTasti.getInstance().RimuoviNotifica();
                //      break;
                //  case 2:
                if (VariabiliStaticheStart.getInstance().isDetector()) {
                    GestioneNotificheDetector.getInstance().RimuoviNotifica();
                }
                //      break;
                //  case 3:
                GestioneNotificaGPS.getInstance().RimuoviNotifica();
                //      break;
                //  case 4:
                if (VariabiliStatichePlayer.getInstance().isPlayerAttivo()) {
                    GestioneNotifichePlayer.getInstance().RimuoviNotifica();
                }
                //      break;
                //  case 5:
                GestioneNotificheWP.getInstance().RimuoviNotifica();
                //      break;
                // }

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        /* Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();

                        switch (notificationId) {
                            case 1: */
                                Notification notificaTasti = GestioneNotificheTasti.getInstance().StartNotifica(context);
                                if (notificaTasti != null) {
                                    // startForeground(VariabiliStaticheTasti.NOTIFICATION_CHANNEL_ID, notificaTasti);
                                    // GestioneNotificheTasti.getInstance().AggiornaNotifica();
                                }
                                /* break;
                            case 2: */
                                if ( VariabiliStaticheStart.getInstance().isDetector()) {
                                    Notification notificaDetector = GestioneNotificheDetector.getInstance().StartNotifica(context);
                                    if (notificaDetector != null) {
                                        // GestioneNotificheDetector.getInstance().AggiornaNotifica("");
                                    }
                                }
                                /* break;
                            case 3: */
                                Notification notificaGPS = GestioneNotificaGPS.getInstance().StartNotifica(context);
                                if (notificaGPS != null) {
                                    // GestioneNotificaGPS.getInstance().AggiornaNotifica();
                                }
                                /* break;
                            case 4: */
                                if (VariabiliStatichePlayer.getInstance().isPlayerAttivo()) {
                                    Notification notificaPlayer = GestioneNotifichePlayer.getInstance().StartNotifica(context);
                                    if (notificaPlayer != null) {
                                        // GestioneNotifichePlayer.getInstance().AggiornaNotifica("");
                                    }
                                }
                                /* break;
                            case 5: */
                                Notification notificaWP = GestioneNotificheWP.getInstance().StartNotifica(context);
                                // VariabiliStatiche.getInstance().setNotifica(GestioneNotifiche.getInstance().StartNotifica(this));
                                if (notificaWP != null) {
                                    // startForeground(VariabiliStaticheWallpaper.NOTIFICATION_CHANNEL_ID, notifica);
                                    // GestioneNotificheWP.getInstance().AggiornaNotifica();
                                }
                                /* break;
                        } */

                        UtilitiesGlobali.getInstance().ApreToast(context,
                                "Chiusa e riaperta notifica " + notificationId);
                    }
                };
                handlerTimer.postDelayed(rTimer, 500);
            }
        };
        handlerTimer.postDelayed(rTimer, 500);
    }
}