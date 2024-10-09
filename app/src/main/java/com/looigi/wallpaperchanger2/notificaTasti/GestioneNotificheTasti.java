package com.looigi.wallpaperchanger2.notificaTasti;

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
import android.os.IBinder;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeMostraImmagini.MainMostraImmagini;
import com.looigi.wallpaperchanger2.classeMostraVideo.MainMostraVideo;
import com.looigi.wallpaperchanger2.classiDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classiGps.MainMappa;
import com.looigi.wallpaperchanger2.classiGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classiPlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classiPlayer.MainPlayer;
import com.looigi.wallpaperchanger2.classiPlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classiWallpaper.MainWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;


public class GestioneNotificheTasti {
    private static final String nomeMaschera = "GESTIONENOTIFICHEWALLPAPER";
    private NotificationManager manager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews contentView;
    private Context context;
    // private Notification notifica;

    private static final GestioneNotificheTasti ourInstance = new GestioneNotificheTasti();

    public static GestioneNotificheTasti getInstance() {
        return ourInstance;
    }

    private GestioneNotificheTasti() {
    }

    public Notification StartNotifica(Context ctxP) {
        try {
            context = ctxP;

            NotificationChannel chan = new NotificationChannel(
                    VariabiliStaticheTasti.NOTIFICATION_CHANNEL_STRING,
                    VariabiliStaticheTasti.channelName,
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

            contentView = new RemoteViews(context.getPackageName(), R.layout.barra_notifica_tasti);
            setListenersTasti(contentView, context);
            setListeners(contentView);

            Bitmap bmPlayer;
            if (VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                bmPlayer = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_off);
            } else {
                bmPlayer = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
            }
            contentView.setImageViewBitmap(R.id.imgPlayerTasti, bmPlayer);

            if (VariabiliStaticheStart.getInstance().isDetector()) {
                // contentView.setViewVisibility(R.id.imgMap, LinearLayout.VISIBLE);
                contentView.setViewVisibility(R.id.imgSwitchGPSTasti, LinearLayout.VISIBLE);
                Bitmap bmGps;
                if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                    bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite);
                } else {
                    bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite_off);
                }
                contentView.setImageViewBitmap(R.id.imgSwitchGPSTasti, bmGps);

                if (VariabiliStaticheGPS.getInstance().getMappa() != null &&
                        VariabiliStaticheGPS.getInstance().getMappa().RitornaPunti() != null) {
                    long distanza = VariabiliStaticheGPS.getInstance().getDistanzaTotale();
                    float dist = Math.round((distanza / 1000F) * 100) / 100F;
                    contentView.setTextViewText(R.id.txtPunti,
                            "Punti: " + Integer.toString(VariabiliStaticheGPS.getInstance().getMappa().RitornaPunti().size()) + "\n" +
                                    "Dist.: " + Float.toString(dist) + " Km.");
                } else {
                    contentView.setTextViewText(R.id.txtPunti,
                            "Punti: 0\nDist.: 0");
                }
            } else {
                // contentView.setViewVisibility(R.id.imgMap, LinearLayout.GONE);
                contentView.setViewVisibility(R.id.imgSwitchGPSTasti, LinearLayout.GONE);
                contentView.setViewVisibility(R.id.imgDetectorTasti, LinearLayout.GONE);
                contentView.setViewVisibility(R.id.imgMappaTasti, LinearLayout.GONE);
                contentView.setViewVisibility(R.id.txtPunti, LinearLayout.GONE);
            }

            if (VariabiliStaticheStart.getInstance().isVisibileImmagini()) {
                contentView.setViewVisibility(R.id.imgImmaginiTasti, LinearLayout.VISIBLE);
            } else {
                contentView.setViewVisibility(R.id.imgImmaginiTasti, LinearLayout.GONE);
            }

            if (VariabiliStaticheStart.getInstance().isVisibileVideo()) {
                contentView.setViewVisibility(R.id.imgImmaginiVideo, LinearLayout.VISIBLE);
            } else {
                contentView.setViewVisibility(R.id.imgImmaginiVideo, LinearLayout.GONE);
            }

            boolean wifi = VariabiliStaticheStart.getInstance().isCeWifi();
            String testo = (wifi ? "WiFi" : "Mobile");
            if (!wifi) {
                testo += " " + VariabiliStaticheStart.getInstance().getTipoConnessione();
            }
            String livello = UtilitiesGlobali.getInstance().getLevelString(
                    VariabiliStaticheStart.getInstance().getLivello()
            );
            testo += " - Liv. Segn.: " +
                    VariabiliStaticheStart.getInstance().getLivelloSegnaleConnessione() +
                    " (" + livello + ")";
            testo += "\nDl: " +
                    VariabiliStaticheStart.getInstance().getVelocitaDownload() +
                    " - Ul: " + VariabiliStaticheStart.getInstance().getVelocitaUpload() + " " +
                    VariabiliStaticheStart.getInstance().getUltimoControlloRete();
            contentView.setTextViewText(R.id.txtSegnale, testo);

            notificationBuilder = new NotificationCompat.Builder(context, VariabiliStaticheTasti.NOTIFICATION_CHANNEL_STRING);

            Notification notifica = notificationBuilder
                .setContentTitle(VariabiliStaticheTasti.channelName)                            // required
                .setSmallIcon(R.drawable.tasti)   // required android.R.drawable.ic_menu_slideshow
                .setContentText(VariabiliStaticheTasti.channelName) // required
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
            Intent settings = new Intent(ctx, NotificationActionServiceTasti.class);
            settings.putExtra("DO", "settings");
            PendingIntent pSettings = PendingIntent.getService(ctx, 201, settings,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgSettingsTasti, pSettings);

            Intent detector = new Intent(ctx, NotificationActionServiceTasti.class);
            detector.putExtra("DO", "detector");
            PendingIntent pDetector = PendingIntent.getService(ctx, 202, detector,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgDetectorTasti, pDetector);

            Intent wp = new Intent(ctx, NotificationActionServiceTasti.class);
            wp.putExtra("DO", "wallpaper");
            PendingIntent pWp = PendingIntent.getService(ctx, 203, wp,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgWallpaperTasti, pWp);

            Intent mappa = new Intent(ctx, NotificationActionServiceTasti.class);
            mappa.putExtra("DO", "mappa");
            PendingIntent pMappa = PendingIntent.getService(ctx, 204, mappa,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgMappaTasti, pMappa);

            Intent player = new Intent(ctx, NotificationActionServiceTasti.class);
            player.putExtra("DO", "player");
            PendingIntent pPlayer = PendingIntent.getService(ctx, 205, player,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgPlayerTasti, pPlayer);

            Intent imm = new Intent(ctx, NotificationActionServiceTasti.class);
            imm.putExtra("DO", "immagini");
            PendingIntent pImm = PendingIntent.getService(ctx, 206, imm,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgImmaginiTasti, pImm);

            Intent vid = new Intent(ctx, NotificationActionServiceTasti.class);
            vid.putExtra("DO", "video");
            PendingIntent pVid = PendingIntent.getService(ctx, 207, vid,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgImmaginiTasti, pVid);

            Intent uscita = new Intent(ctx, NotificationActionServiceTasti.class);
            uscita.putExtra("DO", "uscita");
            PendingIntent pUscita = PendingIntent.getService(ctx, 208, uscita,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgUscitaTasti, pUscita);
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
                mNotificationManager.notify(VariabiliStaticheTasti.NOTIFICATION_CHANNEL_ID, notification);
            } catch (Exception e) {
            }
        }
    }

    public void RimuoviNotifica() {
        // // Utility.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(VariabiliStaticheTasti.getInstance().getIdNotifica());
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

    public static class NotificationActionServiceTasti extends Service {
        private Context context;

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            context = UtilitiesGlobali.getInstance().tornaContextValido();
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
                    case "settings":
                        Intent iI = new Intent(context, MainImpostazioni.class);
                        iI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iI);
                        break;
                    case "detector":
                        Intent iD = new Intent(context, MainActivityDetector.class);
                        iD.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iD);
                        break;
                    case "wallpaper":
                        Intent iW = new Intent(context, MainWallpaper.class);
                        iW.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iW);
                        break;
                    case "mappa":
                        Intent iM = new Intent(context, MainMappa.class);
                        iM.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iM);
                        break;
                    case "player":
                        if (!VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                            VariabiliStaticheStart.getInstance().setPlayerAperto(true);

                            Intent iP = new Intent(context, MainPlayer.class);
                            iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iP);

                            Notification notificaPlayer = GestioneNotifichePlayer.getInstance().StartNotifica(context);
                            if (notificaPlayer != null) {
                                // startForeground(VariabiliStatichePlayer.NOTIFICATION_CHANNEL_ID, notificaPlayer);

                                GestioneNotifichePlayer.getInstance().AggiornaNotifica("Titolo Canzone");

                                UtilitiesGlobali.getInstance().ApreToast(context, "Player Partito");
                            }
                        } else {
                            VariabiliStaticheStart.getInstance().setPlayerAperto(false);

                            GestioneNotifichePlayer.getInstance().RimuoviNotifica();

                            UtilityPlayer.getInstance().PressionePlay(context, false);
                            UtilityPlayer.getInstance().ChiudeActivity(true);
                        }

                        GestioneNotificheTasti.getInstance().AggiornaNotifica();
                        break;
                    case "immagini":
                        Intent iIm = new Intent(context, MainMostraImmagini.class);
                        iIm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iIm);
                        break;
                    case "video":
                        Intent iVi = new Intent(context, MainMostraVideo.class);
                        iVi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(iVi);
                        break;
                    case "uscita":
                        UtilitiesGlobali.getInstance().ChiudeApplicazione(context);
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
