package com.looigi.wallpaperchanger2.classePlayer;

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
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.VariabiliStaticheTasti;
import com.looigi.wallpaperchanger2.notifiche.NotificationDismissedReceiver;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.Date;

public class GestioneNotifichePlayer {
    private NotificationManager manager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private Context context;
    private static final String nomeMaschera = "Gestione_Notifiche_Player";
    private String Brano = "";

    private static final GestioneNotifichePlayer ourInstance = new GestioneNotifichePlayer();

    public static GestioneNotifichePlayer getInstance() {
        return ourInstance;
    }

    private GestioneNotifichePlayer() {
    }

    public Notification StartNotifica(Context ctxP) {
        try {
            context = ctxP;

            NotificationChannel chan = new NotificationChannel(
                    VariabiliStatichePlayer.NOTIFICATION_CHANNEL_STRING,
                    VariabiliStatichePlayer.channelName,
                    NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.BLUE);
            chan.setShowBadge(false);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            chan.setSound(null, null);
            chan.setImportance(NotificationManager.IMPORTANCE_LOW);

            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            contentView = new RemoteViews(context.getPackageName(), R.layout.barra_notifica_player);
            setListenersTasti(contentView, context);
            setListeners(contentView);

            contentView.setTextViewText(R.id.txtDettaglio, Brano);

            Bitmap bmpStart;
            if (!VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                bmpStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_suona);
            } else {
                bmpStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_pausa);
            }
            contentView.setImageViewBitmap(R.id.imgPlayStopBarra, bmpStart);

            String pathImmagine = VariabiliStatichePlayer.getInstance().getPathUltimaImmagine();
            if (VariabiliStatichePlayer.getInstance().getPathUltimaImmagine() != null) {
                if (!pathImmagine.isEmpty()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(pathImmagine);
                    contentView.setImageViewBitmap(R.id.imgCopertina, bitmap);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                    contentView.setImageViewBitmap(R.id.imgCopertina, bitmap);
                }
            }

            notificationBuilder = new NotificationCompat.Builder(context, VariabiliStatichePlayer.NOTIFICATION_CHANNEL_STRING);

            Notification notifica = notificationBuilder
                    .setContentTitle(VariabiliStatichePlayer.channelName)                            // required
                    .setSmallIcon(R.drawable.player)   // required android.R.drawable.ic_menu_slideshow
                    .setContentText(VariabiliStatichePlayer.channelName) // required
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

            manager.notify(VariabiliStatichePlayer.NOTIFICATION_CHANNEL_ID, notifica);

            return notifica;
        } catch (Exception e) {
            UtilityPlayer.getInstance().ScriveLog(context, nomeMaschera, "Errore notifica: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e));

            return null;
        }
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("com.looigi.wallpaperchanger2.notificationId", 4);

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
            Intent apre = new Intent(ctx, MainPlayer.class);
            apre.addCategory(Intent.CATEGORY_LAUNCHER);
            apre.setAction(Intent.ACTION_MAIN );
            apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            apre.putExtra("DO", "player");
            PendingIntent pApre = PendingIntent.getActivity(ctx, 31, apre,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.layPlayer1, pApre);

            Intent indietro = new Intent(ctx, GestioneNotifichePlayer.NotificationActionServicePlayer.class);
            indietro.putExtra("DO", "Indietro");
            PendingIntent pIndietro = PendingIntent.getService(ctx, 151, indietro,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgIndietroPlayerBarra, pIndietro);

            Intent play = new Intent(ctx, GestioneNotifichePlayer.NotificationActionServicePlayer.class);
            play.putExtra("DO", "PlayStop");
            PendingIntent pPlay = PendingIntent.getService(ctx, 152, play,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgPlayStopBarra, pPlay);

            Intent avanti = new Intent(ctx, GestioneNotifichePlayer.NotificationActionServicePlayer.class);
            avanti.putExtra("DO", "Avanti");
            PendingIntent pAvanti = PendingIntent.getService(ctx, 153, avanti,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgAvantiPlayerBarra, pAvanti);

        }
    }

    public void AggiornaNotifica(String Brano) {
        this.Brano = Brano;

        if (context != null) {
            try {
                Notification notification = StartNotifica(context);
                manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(VariabiliStatichePlayer.NOTIFICATION_CHANNEL_ID, notification);
            } catch (Exception e) {
                UtilityPlayer.getInstance().ScriveLog(context, nomeMaschera, "Errore su aggiorna notifica: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public void RimuoviNotifica() {
        // // Log.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(VariabiliStatichePlayer.NOTIFICATION_CHANNEL_ID);
                // manager.cancelAll();
                manager = null;
                contentView = null;
                notificationBuilder = null;
                // notifica = null;
                // NOTIF_ID++;
            } catch (Exception e) {
                UtilitiesGlobali.getInstance().ApreToast(context, "Errore nella chiusura " +
                        "della notifica:\n" + UtilityDetector.getInstance().PrendeErroreDaException(e));
                // // Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public static class NotificationActionServicePlayer extends Service {
        private Context context;

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            context = VariabiliStatichePlayer.getInstance().getContext();
            String action = "";

            // Log.getInstance().ScriveLog("Notifica: onCreate PassaggioNotifica");

            try {
                action = (String) intent.getExtras().get("DO");
                // Log.getInstance().ScriveLog("Notifica: Action: " + action);
            } catch (Exception e) {
                // Log.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }

            if (action != null) {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                switch (action) {
                    case "Apre":
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (context == null) {
                                    context = VariabiliStaticheStart.getInstance().getContext();
                                }
                                if (context == null) {
                                    context = VariabiliStaticheWallpaper.getInstance().getContext();
                                }
                                if (context == null) {
                                    context = VariabiliStatichePlayer.getInstance().getContext();
                                }
                                if (context != null) {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(context, MainPlayer.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(i);
                                        }
                                    }, 1000);
                                }
                            }
                        }, 100);
                        break;
                    case "Indietro":
                        UtilityPlayer.getInstance().ResettaCampi(VariabiliStatichePlayer.getInstance().getContext(), false);

                        UtilityPlayer.getInstance().IndietroBrano(
                                VariabiliStatichePlayer.getInstance().getContext());
                        break;
                    case "PlayStop":
                        boolean acceso = VariabiliStatichePlayer.getInstance().isStaSuonando();

                        UtilityPlayer.getInstance().PressionePlay(this, !acceso);
                        break;
                    case "Avanti":
                        UtilityPlayer.getInstance().ResettaCampi(VariabiliStatichePlayer.getInstance().getContext(), false);

                        UtilityPlayer.getInstance().BranoAvanti(
                                VariabiliStatichePlayer.getInstance().getContext(), "", false, true);
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
