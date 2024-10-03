package com.looigi.wallpaperchanger2.classiWallpaper;

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
import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiGps.Mappa;
import com.looigi.wallpaperchanger2.classiGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class GestioneNotifiche {
    private static final String nomeMaschera = "GESTIONENOTIFICHEWALLPAPER";
    private NotificationManager manager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private Context context;
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
                    VariabiliStaticheWallpaper.NOTIFICATION_CHANNEL_STRING,
                    VariabiliStaticheWallpaper.channelName,
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

            contentView = new RemoteViews(context.getPackageName(), R.layout.barra_notifica_wallpaper);
            setListenersTasti(contentView, context);
            setListeners(contentView);

            if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
                String path = VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getPathImmagine();
                Bitmap bmImg = BitmapFactory.decodeFile(path);
                if (contentView != null) {
                    contentView.setImageViewBitmap(R.id.imgCopertina, bmImg);

                    contentView.setTextViewText(R.id.txtTitoloNotifica, VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getImmagine());
                    contentView.setTextViewText(R.id.txtTitoloNotificaSfondo, VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getImmagine());
                }
            } else {
                if (contentView != null) {
                    contentView.setImageViewBitmap(R.id.imgCopertina, null);

                    contentView.setTextViewText(R.id.txtTitoloNotifica, "");
                    contentView.setTextViewText(R.id.txtTitoloNotificaSfondo, "");
                }
            }

            if (VariabiliStaticheStart.getInstance().isDetector()) {
                contentView.setViewVisibility(R.id.imgMap, LinearLayout.VISIBLE);
                contentView.setViewVisibility(R.id.imgSwitchGPS, LinearLayout.VISIBLE);
                Bitmap bmGps;
                if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                    bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite);
                } else {
                    bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite_off);
                }
                contentView.setImageViewBitmap(R.id.imgSwitchGPS, bmGps);
            } else {
                contentView.setViewVisibility(R.id.imgMap, LinearLayout.GONE);
                contentView.setViewVisibility(R.id.imgSwitchGPS, LinearLayout.GONE);
            }

            int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();
            int quantiGiri = (minuti * 60) / VariabiliStaticheWallpaper.secondiDiAttesaContatore;
            String prossimo = "Prossimo cambio: " +
                    VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
                    quantiGiri + " - Timeouts: " + VariabiliStaticheWallpaper.getInstance().getErrori();
            if (contentView != null) {
                contentView.setTextViewText(R.id.txtDettaglio, prossimo);

                /* if (VariabiliStaticheServizio.getInstance().isDetector()) {
                    contentView.setViewVisibility(R.id.layDetector, LinearLayout.VISIBLE);
                    contentView.setTextViewText(R.id.txtDetector, "Detector");
                } else {
                    contentView.setViewVisibility(R.id.layDetector, LinearLayout.GONE);
                } */
            }


            notificationBuilder = new NotificationCompat.Builder(context, VariabiliStaticheWallpaper.NOTIFICATION_CHANNEL_STRING);

            Notification notifica = notificationBuilder
                .setContentTitle(VariabiliStaticheWallpaper.channelName)                            // required
                .setSmallIcon(R.drawable.eye)   // required android.R.drawable.ic_menu_slideshow
                .setContentText(VariabiliStaticheWallpaper.channelName) // required
                // .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(false)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setAutoCancel(false)
                .setOngoing(true)
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

            return notifica;
        } catch (Exception e) {
            UtilityWallpaper.getInstance().ScriveLog(context, nomeMaschera, "Errore notifica: " +
                    UtilityWallpaper.getInstance().PrendeErroreDaException(e));

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

            Intent titoloApp = new Intent(ctx, NotificationActionService.class);
            titoloApp.putExtra("DO", "cambioWallpaper");
            PendingIntent pAvanti = PendingIntent.getService(ctx, 71, titoloApp,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgProssima, pAvanti);

            Intent mappa = new Intent(ctx, NotificationActionService.class);
            mappa.putExtra("DO", "mappa");
            PendingIntent pMappa = PendingIntent.getService(ctx, 72, mappa,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgMap, pMappa);

            Intent switchgps = new Intent(ctx, NotificationActionService.class);
            switchgps.putExtra("DO", "gps");
            PendingIntent pSwitchGPS = PendingIntent.getService(ctx, 73, switchgps,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgSwitchGPS, pSwitchGPS);

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

            try {
                Notification notification = StartNotifica(context);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(VariabiliStaticheWallpaper.NOTIFICATION_CHANNEL_ID, notification);
            } catch (Exception e) {
                UtilityWallpaper.getInstance().ScriveLog(context, nomeMaschera, "Errore su aggiorna notifica: " +
                        UtilityWallpaper.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public void RimuoviNotifica() {
        // // Utility.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(VariabiliStaticheWallpaper.getInstance().getIdNotifica());
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

    public static class NotificationActionService extends Service {
        private Context context;

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            context = VariabiliStaticheWallpaper.getInstance().getContext();
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
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(context, MainWallpaper.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);

                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        InizializzaMascheraWallpaper i = new InizializzaMascheraWallpaper();
                                        i.inizializzaMaschera(
                                                context,
                                                VariabiliStaticheWallpaper.getInstance().getMainActivity());
                                    }
                                }, 1000);
                            }
                        }, 1000);
                        break;

                    case "cambioWallpaper":
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UtilityWallpaper.getInstance().CambiaImmagine(context);

                                GestioneNotifiche.getInstance().AggiornaNotifica();
                            }
                        }, 100);
                        break;

                    case "mappa":
                        VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                        VariabiliStaticheStart.getInstance().ChiudeActivity(true);
                        VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(context, Mappa.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                        }, 100);
                        break;

                    case "gps":
                        if (VariabiliStaticheGPS.getInstance().getGestioneGPS() != null) {
                            boolean gps = VariabiliStaticheGPS.getInstance().isGpsAttivo();
                            gps = !gps;
                            VariabiliStaticheGPS.getInstance().setGpsAttivo(gps);
                            if (gps) {
                                VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS(context);
                            } else {
                                VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS();
                            }
                            GestioneNotifiche.getInstance().AggiornaNotifica();
                        } else {
                            UtilityWallpaper.getInstance().ApreToast(this, "GPS Non attivo");
                        }
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
