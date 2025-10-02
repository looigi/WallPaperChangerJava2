package com.looigi.wallpaperchanger2.Detector;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Detector.widgets.Audio;
import com.looigi.wallpaperchanger2.Detector.widgets.Video;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.ActivityDiStart;
import com.looigi.wallpaperchanger2.Notifiche.NotificationDismissedReceiver;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

public class GestioneNotificheDetector {
    private NotificationManager manager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private Context context;
    private static final String nomeMaschera = "Gestione_Notifiche_Detector";
    private String Messaggio = "";

    private static final GestioneNotificheDetector ourInstance = new GestioneNotificheDetector();

    public static GestioneNotificheDetector getInstance() {
        return ourInstance;
    }

    private GestioneNotificheDetector() {
    }

    public Notification StartNotifica(Context ctxP) {
        try {
            context = ctxP;

            NotificationChannel chan = new NotificationChannel(
                    VariabiliStaticheDetector.NOTIFICATION_CHANNEL_STRING,
                    VariabiliStaticheDetector.channelName,
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setShowBadge(false);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            chan.setSound(null, null);
            chan.setImportance(NotificationManager.IMPORTANCE_LOW);

            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            contentView = new RemoteViews(context.getPackageName(), R.layout.barra_notifica_detector);
            setListenersTasti(contentView, context);
            setListeners(contentView);

            // contentView.setTextViewText(R.id.txtTitolo, VariabiliStaticheDetector.channelName);
            contentView.setTextViewText(R.id.txtDettaglio, Messaggio);

            notificationBuilder = new NotificationCompat.Builder(context, VariabiliStaticheDetector.NOTIFICATION_CHANNEL_STRING);

            Notification notifica = notificationBuilder
                    .setContentTitle(VariabiliStaticheDetector.channelName)                            // required
                    .setSmallIcon(R.drawable.detector)   // required android.R.drawable.ic_menu_slideshow
                    .setContentText(VariabiliStaticheDetector.channelName) // required
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

            // notifica.bigContentView = contentView;

            manager.notify(VariabiliStaticheDetector.NOTIFICATION_CHANNEL_ID, notifica);

            return notifica;
        } catch (Exception e) {
            UtilityDetector.getInstance().ScriveLog(context, nomeMaschera, "Errore notifica: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e));

            return null;
        }
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("com.looigi.wallpaperchanger2.notificationId", 2);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        2, intent, PendingIntent.FLAG_IMMUTABLE);
        return pendingIntent;
    }

    private void setListeners(RemoteViews view) {
        if (view != null) {
            // // Log.getInstance().ScriveLog("Set Listeners. View corretta");

            // String Traccia = "";

            /* if (VariabiliGlobali.getInstance().getUltimaImmagine() != null) {
                AggiornaNotifica(VariabiliGlobali.getInstance().getUltimaImmagine().getImmagine(),
                        VariabiliGlobali.getInstance().getUltimaImmagine().getPathImmagine());
            } else {
                view.setTextViewText(R.id.txtTitoloImmagine, "---");
                view.setImageViewResource(R.id.imgCopertina, R.drawable.logo);
            } */
        } else {
            // // Log.getInstance().ScriveLog("Set Listeners. View NON corretta");
        }
    }

    private void setListenersTasti(RemoteViews view, Context ctx) {
        if (view != null) {
            Intent apre = new Intent(ctx, ActivityDiStart.class);
            apre.addCategory(Intent.CATEGORY_LAUNCHER);
            apre.setAction(Intent.ACTION_MAIN );
            apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            apre.putExtra("DO", "detector");
            PendingIntent pApre = PendingIntent.getActivity(ctx, 50, apre,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.txtDettaglio, pApre);

            Intent scatta = new Intent(ctx, NotificationActionServiceDetector.class);
            scatta.putExtra("DO", "Foto");
            PendingIntent pScatta = PendingIntent.getService(ctx, 51, scatta,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgPhoto, pScatta);

            Intent video = new Intent(ctx, NotificationActionServiceDetector.class);
            video.putExtra("DO", "Video");
            PendingIntent pVideo = PendingIntent.getService(ctx, 52, video,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgVideo, pVideo);

            Intent audio = new Intent(ctx, NotificationActionServiceDetector.class);
            audio.putExtra("DO", "Audio");
            PendingIntent pAudio = PendingIntent.getService(ctx, 53, audio,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgAudio, pAudio);
        }
    }

    public void AggiornaNotifica(String Messaggio) {
        this.Messaggio = Messaggio;

        if (context != null) {
            try {
                Notification notification = StartNotifica(context);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(VariabiliStaticheDetector.NOTIFICATION_CHANNEL_ID, notification);
            } catch (Exception e) {
                UtilityDetector.getInstance().ScriveLog(context, nomeMaschera, "Errore su aggiorna notifica: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public void RimuoviNotifica() {
        // // Log.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(VariabiliStaticheDetector.NOTIFICATION_CHANNEL_ID);
                // manager.cancelAll();
                manager = null;
                contentView = null;
                notificationBuilder = null;
                // notifica = null;
                // NOTIF_ID++;
            } catch (Exception e) {
                // // Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public static class NotificationActionServiceDetector extends Service {
        private Context context;

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            context = VariabiliStaticheDetector.getInstance().getContext();
            String action = "";

            // Log.getInstance().ScriveLog("Notifica: onCreate PassaggioNotifica");

            try {
                action = (String) intent.getExtras().get("DO");
                // Log.getInstance().ScriveLog("Notifica: Action: " + action);
            } catch (Exception e) {
                // Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }

            if (action != null) {
                Context ctx = UtilitiesGlobali.getInstance().tornaContextValido();

                switch (action) {
                    case "Apre":
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // VariabiliStaticheDetector.getInstance().setChiudiActivity(false);

                                VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
                                VariabiliStaticheStart.getInstance().ChiudeActivity(true);

                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(context, MainActivityDetector.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        ctx.startActivity(i);

                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                InizializzaMascheraDetector i2 = new InizializzaMascheraDetector();
                                                i2.inizializzaMaschera(
                                                        ctx,
                                                        VariabiliStaticheDetector.getInstance().getMainActivity());
                                            }
                                        }, 1000);
                                    }
                                }, 1000);
                            }
                        }, 100);
                        break;

                    case "Foto":
                        // VariabiliStaticheDetector.getInstance().setChiudiActivity(true);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(
                                        ctx,
                                        AndroidCameraApi.class);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(myIntent);

                                UtilityDetector.getInstance().SpegneSchermo(context);
                            }
                        }, 100);

                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);

                        break;

                    case "Video":
                        // VariabiliStaticheDetector.getInstance().setChiudiActivity(true);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(
                                        ctx,
                                        Video.class);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(myIntent);
                            }
                        }, 1000);

                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);

                        break;

                    case "Audio":
                        // VariabiliStaticheDetector.getInstance().setChiudiActivity(true);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent myIntent = new Intent(
                                        ctx,
                                        Audio.class);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(myIntent);
                            }
                        }, 1000);

                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);

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
