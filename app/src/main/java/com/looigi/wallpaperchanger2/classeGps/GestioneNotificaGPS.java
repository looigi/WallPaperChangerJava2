package com.looigi.wallpaperchanger2.classeGps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.notificaTasti.VariabiliStaticheTasti;
import com.looigi.wallpaperchanger2.notifiche.NotificationDismissedReceiver;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class GestioneNotificaGPS {
    private static final String nomeMaschera = "Gestione_Notifiche_GPS";
    private NotificationManager manager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private Context context;
    // private Notification notifica;

    private static final GestioneNotificaGPS ourInstance = new GestioneNotificaGPS();

    public static GestioneNotificaGPS getInstance() {
        return ourInstance;
    }

    private GestioneNotificaGPS() {
    }

    public Notification StartNotifica(Context ctxP) {
        try {
            context = ctxP;

            NotificationChannel chan = new NotificationChannel(
                    VariabiliStaticheGPS.NOTIFICATION_CHANNEL_STRING,
                    VariabiliStaticheGPS.channelName,
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setShowBadge(false);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            chan.setSound(null, null);
            chan.setImportance(NotificationManager.IMPORTANCE_LOW);

            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            // manager.areNotificationsEnabled();

            contentView = new RemoteViews(context.getPackageName(), R.layout.barra_notifica_gps);
            setListenersTasti(contentView, context);
            setListeners(contentView);

            Bitmap bmGps;
            if (!VariabiliStaticheGPS.getInstance().isGpsAttivo() ||
                    VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
                bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite_off);
            } else {
                bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite);
            }
            contentView.setImageViewBitmap(R.id.imgSwitchGPSTasti, bmGps);

            contentView.setViewVisibility(R.id.imgWifi, LinearLayout.GONE);
            contentView.setViewVisibility(R.id.imgStop, LinearLayout.GONE);
            contentView.setViewVisibility(R.id.imgStop2, LinearLayout.GONE);

            Bitmap bmGpsPS;
            if (!VariabiliStaticheGPS.getInstance().isNonScriverePunti() &&
                !VariabiliStaticheStart.getInstance().isCeWifi() &&
                VariabiliStaticheGPS.getInstance().isGpsAttivo() &&
                !VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
                bmGpsPS = BitmapFactory.decodeResource(context.getResources(), R.drawable.gps_on);
            } else {
                bmGpsPS = BitmapFactory.decodeResource(context.getResources(), R.drawable.gps_off);
                if (VariabiliStaticheStart.getInstance().isCeWifi()) {
                    contentView.setViewVisibility(R.id.imgWifi, LinearLayout.VISIBLE);
                }
                if (VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
                    contentView.setViewVisibility(R.id.imgStop, LinearLayout.VISIBLE);
                }
                if (VariabiliStaticheGPS.getInstance().isNonScriverePunti()) {
                    contentView.setViewVisibility(R.id.imgStop2, LinearLayout.VISIBLE);
                }
            }
            contentView.setImageViewBitmap(R.id.imgAreaPS, bmGpsPS);

            if (VariabiliStaticheGPS.getInstance().getMappa() != null &&
                    VariabiliStaticheGPS.getInstance().getMappa().RitornaPunti() != null) {
                long distanza = VariabiliStaticheGPS.getInstance().getDistanzaTotale();
                float dist = Math.round((distanza / 1000F) * 100) / 100F;
                contentView.setTextViewText(R.id.txtPunti,
                        "P.: " + Integer.toString(VariabiliStaticheGPS.getInstance().getMappa().RitornaPunti().size()) + "\n" +
                                "" + Float.toString(dist) + " Km.");
            } else {
                contentView.setTextViewText(R.id.txtPunti,
                        "");
            }

            contentView.setTextViewText(R.id.txtData,
                    "Last:\n" +
                    VariabiliStaticheGPS.getInstance().getUltimoDataPunto());

            notificationBuilder = new NotificationCompat.Builder(context, VariabiliStaticheGPS.NOTIFICATION_CHANNEL_STRING);

            Notification notifica = notificationBuilder
                    .setContentTitle(VariabiliStaticheGPS.channelName)                            // required
                    .setSmallIcon(R.drawable.satellite)   // required android.R.drawable.ic_menu_slideshow
                    .setContentText(VariabiliStaticheGPS.channelName) // required
                    // .setDefaults(Notification.DEFAULT_ALL)
                    .setOnlyAlertOnce(false)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    // .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                    // .setGroup("LOO'S WEB PLAYER")
                    .setDeleteIntent(createOnDismissedIntent(context))
                    .setGroupSummary(false)
                    // .setDefaults(NotificationCompat.DEFAULT_ALL)
                    // .setContentIntent(pendingIntent)
                    .setTicker("")
                    .setContent(contentView)
                    .build();

            notifica.bigContentView = contentView;

            manager.notify(VariabiliStaticheGPS.NOTIFICATION_CHANNEL_ID, notifica);

            return notifica;
        } catch (Exception e) {
            UtilityGPS.getInstance().ScriveLog(context, nomeMaschera, "Errore notifica: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e));

            return null;
        }
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("com.looigi.wallpaperchanger2.notificationId", 3);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        2, intent, PendingIntent.FLAG_IMMUTABLE);
        return pendingIntent;
    }

    private void setListeners(RemoteViews view) {
        if (view != null) {
            // // Utility.getInstance().ScriveLog("Set Listeners. View corretta");

            // String Traccia = "";

            /* if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
                AggiornaNotifica(VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine(),
                        VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine());
            } else {
                view.setTextViewText(R.id.txtTitoloImmagine, "---");
                view.setImageViewResource(R.id.imgCopertina, R.drawable.logo);
            } */
        } else {
            // // Utility.getInstance().ScriveLog("Set Listeners. View NON corretta");
        }
    }

    private void setListenersTasti(RemoteViews view, Context ctx) {
        if (view != null) {
            Intent gps = new Intent(ctx, NotificationActionServiceGPS.class);
            gps.putExtra("DO", "gps");
            PendingIntent pGps = PendingIntent.getService(ctx, 800, gps,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgSwitchGPSTasti, pGps);
        }
    }

    public void AggiornaNotifica() {
        if (context != null) {
            try {
                Notification notification = StartNotifica(context);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(VariabiliStaticheGPS.NOTIFICATION_CHANNEL_ID, notification);
            } catch (Exception e) {
                UtilityGPS.getInstance().ScriveLog(context, nomeMaschera, "Errore su aggiorna notifica: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public void RimuoviNotifica() {
        // // Utility.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(VariabiliStaticheGPS.NOTIFICATION_CHANNEL_ID);
                manager.cancelAll();
                manager = null;
                contentView = null;
                notificationBuilder = null;
                // notifica = null;
                // NOTIF_ID++;
            } catch (Exception e) {
                // // Utility.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public static class NotificationActionServiceGPS extends Service {
        private Context context;

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            context = VariabiliStaticheGPS.getInstance().getContext();
            String action="";

            // Utility.getInstance().ScriveLog("Notifica: onCreate PassaggioNotifica");

            try {
                action = (String) intent.getExtras().get("DO");
                // Utility.getInstance().ScriveLog("Notifica: Action: " + action);
            } catch (Exception e) {
                int a = 0;
                // Utility.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }

            if (action!=null) {
                switch (action) {
                    case "gps":
                        boolean attivo = VariabiliStaticheGPS.getInstance().isBloccatoDaTasto();
                        VariabiliStaticheGPS.getInstance().setBloccatoDaTasto(!attivo);
                        if (!attivo) {
                            VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("NOTIFICA");
                        } else {
                            VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS();
                        }
                        GestioneNotificaGPS.getInstance().AggiornaNotifica();
                        break;
                }
            }

            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
