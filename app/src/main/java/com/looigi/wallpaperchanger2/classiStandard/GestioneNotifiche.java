package com.looigi.wallpaperchanger2.classiStandard;

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
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger2.MainActivity;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

public class GestioneNotifiche {
    private NotificationManager manager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private Context context;
    private static final String nomeMaschera = "GESTIONENOTIFICHE";
    // private Notification notifica;

    private static final GestioneNotifiche ourInstance = new GestioneNotifiche();

    public static GestioneNotifiche getInstance() {
        return ourInstance;
    }

    private GestioneNotifiche() {
    }

    public Notification StartNotifica(Context ctxP) {
        try {
            context = ctxP;

            NotificationChannel chan = new NotificationChannel(
                    VariabiliStaticheServizio.NOTIFICATION_CHANNEL_STRING,
                    VariabiliStaticheServizio.channelName,
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setShowBadge(false);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            chan.setSound(null, null);
            chan.setImportance(NotificationManager.IMPORTANCE_LOW);

            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            contentView = new RemoteViews(context.getPackageName(), R.layout.barra_notifica);
            setListenersTasti(contentView, context);
            setListeners(contentView);

            if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
                String path = VariabiliStaticheServizio.getInstance().getUltimaImmagine().getPathImmagine();
                Bitmap bmImg = BitmapFactory.decodeFile(path);
                contentView.setImageViewBitmap(R.id.imgCopertina, bmImg);

                contentView.setTextViewText(R.id.txtTitoloNotifica, VariabiliStaticheServizio.getInstance().getUltimaImmagine().getImmagine());
                contentView.setTextViewText(R.id.txtTitoloNotificaSfondo, VariabiliStaticheServizio.getInstance().getUltimaImmagine().getImmagine());
            } else {
                contentView.setImageViewBitmap(R.id.imgCopertina, null);

                contentView.setTextViewText(R.id.txtTitoloNotifica, "");
                contentView.setTextViewText(R.id.txtTitoloNotificaSfondo, "");
            }
            int minuti = VariabiliStaticheServizio.getInstance().getMinutiAttesa();
            int quantiGiri = (minuti * 60) / VariabiliStaticheServizio.secondiDiAttesaContatore;
            String prossimo = "Prossimo cambio: " +
                    VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                    quantiGiri;
            contentView.setTextViewText(R.id.txtDettaglio, prossimo);
            contentView.setTextViewText(R.id.txtDettaglioSfondo, prossimo);

            notificationBuilder = new NotificationCompat.Builder(context, VariabiliStaticheServizio.NOTIFICATION_CHANNEL_STRING);

            Notification notifica = notificationBuilder
                .setContentTitle(VariabiliStaticheServizio.channelName)                            // required
                .setSmallIcon(R.drawable.eye)   // required android.R.drawable.ic_menu_slideshow
                .setContentText(VariabiliStaticheServizio.channelName) // required
                // .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(false)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setAutoCancel(false)
                .setOngoing(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                // .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                // .setGroup("LOO'S WEB PLAYER")
                // .setGroupSummary(true)
                // .setDefaults(NotificationCompat.DEFAULT_ALL)
                // .setContentIntent(pendingIntent)
                .setTicker("")
                .setContent(contentView)
                .build();

            return notifica;
        } catch (Exception e) {
            Utility.getInstance().ScriveLog(context, nomeMaschera, "Errore notifica: " +
                    Utility.getInstance().PrendeErroreDaException(e));

            return null;
        }
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
            Intent apre = new Intent(ctx, NotificationActionService.class);
            apre.putExtra("DO", "apre");
            PendingIntent pApre = PendingIntent.getService(ctx, 91, apre,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.txtTitoloNotifica, pApre);
        // } else {
            // // Utility.getInstance().ScriveLog("Set Listeners tasti. View NON corretta" );
        }
    }

    public void AggiornaNotifica() {
        if (context != null) {
            /* if (manager == null || contentView == null || notifica == null) {
                RimuoviNotifica();

                StartNotifica(context);
            }

            if (contentView != null) {
                if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
                    String path = VariabiliStaticheServizio.getInstance().getUltimaImmagine().getPathImmagine();
                    Bitmap bmImg = BitmapFactory.decodeFile(path);
                    contentView.setImageViewBitmap(R.id.imgCopertina, bmImg);
                } else {
                    contentView.setImageViewBitmap(R.id.imgCopertina, null);
                }
                contentView.setTextViewText(R.id.txtTitoloImmagine, NomeImmagine);
                contentView.setTextViewText(R.id.txtTitoloImmagineSfondo, NomeImmagine);
                contentView.setTextViewText(R.id.txtAttesa, Attesa);
                contentView.setTextViewText(R.id.txtAttesaSfondo, Attesa);

                manager.notify(VariabiliStaticheServizio.NOTIFICATION_CHANNEL_ID, notifica);
            } else {
                Utility.getInstance().ScriveLog(context, nomeMaschera, "ContentView nullo nonostante il ricaricamento");
            } */

            Notification notification = StartNotifica(context);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(VariabiliStaticheServizio.NOTIFICATION_CHANNEL_ID, notification);
        }
    }

    public void RimuoviNotifica() {
        // // Utility.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(VariabiliStaticheServizio.getInstance().getIdNotifica());
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

            context = VariabiliStaticheServizio.getInstance().getContext();
            String action="";

            // Utility.getInstance().ScriveLog("Notifica: onCreate PassaggioNotifica");

            try {
                action = (String) intent.getExtras().get("DO");
                // Utility.getInstance().ScriveLog("Notifica: Action: " + action);
            } catch (Exception e) {
                // Utility.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }

            if (action!=null) {
                switch (action) {
                    case "apre":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(context, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                        }, 100);
                        break;
                }
            }

            return START_NOT_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
