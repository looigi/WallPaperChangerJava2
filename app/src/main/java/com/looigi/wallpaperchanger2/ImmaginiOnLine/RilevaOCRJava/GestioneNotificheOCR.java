package com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.UtilitiesVarie.ActivityDiStart;
import com.looigi.wallpaperchanger2.Notifiche.NotificationDismissedReceiver;

public class GestioneNotificheOCR {
    private static final String nomeMaschera = "Gestione_Notifiche_OCR";
    private NotificationManager manager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private Context context;
    // private Notification notifica;

    private static final GestioneNotificheOCR ourInstance = new GestioneNotificheOCR();

    public static GestioneNotificheOCR getInstance() {
        return ourInstance;
    }

    private GestioneNotificheOCR() {
    }

    public Notification StartNotifica(Context ctxP) {
        try {
            context = ctxP;

            NotificationChannel chan = new NotificationChannel(
                    VariabiliStaticheRilevaOCRJava.NOTIFICATION_CHANNEL_STRING,
                    VariabiliStaticheRilevaOCRJava.channelName,
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

            contentView = new RemoteViews(context.getPackageName(), R.layout.barra_notifica_ocr);
            setListenersTasti(contentView, context);
            setListeners(contentView);

            contentView.setTextViewText(R.id.txtMessaggio, VariabiliStaticheRilevaOCRJava.getInstance().getMessaggioNotifica());

            notificationBuilder = new NotificationCompat.Builder(context, VariabiliStaticheRilevaOCRJava.NOTIFICATION_CHANNEL_STRING);

            Notification notifica = notificationBuilder
                .setContentTitle(VariabiliStaticheRilevaOCRJava.channelName)                            // required
                .setSmallIcon(R.drawable.eye)   // required android.R.drawable.ic_menu_slideshow
                .setContentText(VariabiliStaticheRilevaOCRJava.channelName) // required
                // .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(false)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setAutoCancel(false)
                .setOngoing(true)
                .setDeleteIntent(createOnDismissedIntent(context))
                .setCategory(Notification.CATEGORY_SERVICE)
                // .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                // .setGroup("LOO'S WEB PLAYER")
                .setGroupSummary(false)
                // .setDefaults(NotificationCompat.DEFAULT_ALL)
                // .setContentIntent(pendingIntent)
                .setTicker("")
                .setContent(contentView)
                .build();

            notifica.bigContentView = contentView;

            manager.notify(VariabiliStaticheRilevaOCRJava.NOTIFICATION_CHANNEL_ID, notifica);

            return notifica;
        } catch (Exception e) {
            return null;
        }
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("com.looigi.wallpaperchanger2.notificationId", 17);

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
            Intent apre = new Intent(ctx, ActivityDiStart.class);
            apre.addCategory(Intent.CATEGORY_LAUNCHER);
            apre.setAction(Intent.ACTION_MAIN );
            apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            apre.putExtra("DO", "ocr");
            PendingIntent pApre = PendingIntent.getActivity(ctx, 170, apre,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.txtMessaggio, pApre);
        }
    }

    public void AggiornaNotifica() {
        if (context != null) {
            try {
                Notification notification = StartNotifica(context);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(VariabiliStaticheRilevaOCRJava.NOTIFICATION_CHANNEL_ID, notification);
            } catch (Exception e) {
            }
        }
    }

    public void RimuoviNotifica() {
        // // Utility.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(VariabiliStaticheRilevaOCRJava.NOTIFICATION_CHANNEL_ID);
                // manager.cancelAll();
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

    public static class NotificationActionService extends Service {
        private Context context;

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            context = VariabiliStaticheRilevaOCRJava.getInstance().getContext();
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
                    case "apre":
                        break;
                }
            }

            return START_REDELIVER_INTENT ;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
