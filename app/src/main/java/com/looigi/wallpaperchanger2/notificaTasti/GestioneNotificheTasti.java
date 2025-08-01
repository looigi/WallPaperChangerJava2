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
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger2.MainStart;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeBackup.MainBackup;
import com.looigi.wallpaperchanger2.classeFilms.MainMostraFilms;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeImmagini.MainMostraImmagini;
import com.looigi.wallpaperchanger2.classeLazio.MainLazio;
import com.looigi.wallpaperchanger2.classePassword.MainPassword;
import com.looigi.wallpaperchanger2.classeVideo.MainMostraVideo;
import com.looigi.wallpaperchanger2.classeDetector.InizializzaMascheraDetector;
import com.looigi.wallpaperchanger2.classeDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeGps.MainMappa;
import com.looigi.wallpaperchanger2.classeOnomastici.MainOnomastici;
import com.looigi.wallpaperchanger2.classePennetta.MainMostraPennetta;
import com.looigi.wallpaperchanger2.classePlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.MainPlayer;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classeWallpaper.MainWallpaper;
import com.looigi.wallpaperchanger2.notifiche.NotificationDismissedReceiver;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class GestioneNotificheTasti {
    private static final String nomeMaschera = "Gestione_Notifiche_Wallpaper";
    private NotificationManager manager;
    // private NotificationCompat.Builder notificationBuilder;
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
                    NotificationManager.IMPORTANCE_HIGH);
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

                /* contentView.setViewVisibility(R.id.imgSwitchGPSTasti, LinearLayout.VISIBLE);
                Bitmap bmGps;
                if (!VariabiliStaticheGPS.getInstance().isGpsAttivo() ||
                    VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
                    bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite_off);
                } else {
                    bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite);
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
                } */
            } else {
                // contentView.setViewVisibility(R.id.imgMap, LinearLayout.GONE);
                // contentView.setViewVisibility(R.id.imgSwitchGPSTasti, LinearLayout.GONE);
                contentView.setViewVisibility(R.id.imgDetectorTasti, LinearLayout.GONE);
                contentView.setViewVisibility(R.id.imgMappaTasti, LinearLayout.GONE);
                contentView.setViewVisibility(R.id.imgControlloImmagini, LinearLayout.GONE);
                // contentView.setViewVisibility(R.id.txtPunti, LinearLayout.GONE);
            }

            /* if (VariabiliStaticheStart.getInstance().isDetector() &&
                    VariabiliStaticheStart.getInstance().isVisibileImmagini()) {
                contentView.setViewVisibility(R.id.imgImmaginiTasti, LinearLayout.VISIBLE);
            } else {
                contentView.setViewVisibility(R.id.imgImmaginiTasti, LinearLayout.GONE);
            }

            if (VariabiliStaticheStart.getInstance().isDetector() &&
                    VariabiliStaticheStart.getInstance().isVisibileVideo()) {
                contentView.setViewVisibility(R.id.imgImmaginiVideo, LinearLayout.VISIBLE);
            } else {
                contentView.setViewVisibility(R.id.imgImmaginiVideo, LinearLayout.GONE);
            }

            if (VariabiliStaticheStart.getInstance().isDetector() &&
                    VariabiliStaticheStart.getInstance().isVisibilePennetta()) {
                contentView.setViewVisibility(R.id.imgImmaginiPennetta, LinearLayout.VISIBLE);
            } else {
                contentView.setViewVisibility(R.id.imgImmaginiPennetta, LinearLayout.GONE);
            } */

            String testo = "";

            boolean wifi = VariabiliStaticheStart.getInstance().isCeWifi();
            testo += (wifi ? "WiFi" : "Mobile");
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
                    " - Ul: " + VariabiliStaticheStart.getInstance().getVelocitaUpload() +
                    " - Chiamate: " + VariabiliStaticheStart.getInstance().getChiamate();
            contentView.setTextViewText(R.id.txtInfoSopra, testo);

            String testo2 = "Accensioni: " + VariabiliStaticheStart.getInstance().getAccensioniDiSchermo() + " - ";
            if (VariabiliStaticheStart.getInstance().getOraEntrata() != null) {
                Date datella = VariabiliStaticheStart.getInstance().getOraEntrata();
                Calendar thatDay = Calendar.getInstance();
                thatDay.setTime(datella);

                Calendar today = Calendar.getInstance();

                long diff = (today.getTimeInMillis() - thatDay.getTimeInMillis()) / 1000;
                int minuti = 0;
                int ore = 0;

                while (diff > 60) {
                    minuti++;
                    if (minuti == 60) {
                        minuti = 0;
                        ore++;
                    }
                    diff -= 60;
                }

                String sOre = Integer.toString(ore).trim();
                if (sOre.length() == 1) {
                    sOre = "0" + sOre;
                }
                String sMinuti = Integer.toString(minuti).trim();
                if (sMinuti.length() == 1) {
                    sMinuti = "0" + sMinuti;
                }
                String sSecondi = Long.toString(diff).trim();
                if (sSecondi.length() == 1) {
                    sSecondi = "0" + sSecondi;
                }

                testo2 += "Tempo accensione: " + sOre + ":" + sMinuti + ":" + sSecondi;
            } else {
                testo2 += "Tempo accensione: 00:00:00";
            }
            contentView.setTextViewText(R.id.txtInfoSotto, testo2);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                    VariabiliStaticheTasti.NOTIFICATION_CHANNEL_STRING);

            Notification notifica = notificationBuilder
                .setContentTitle(VariabiliStaticheTasti.channelName)                            // required
                .setSmallIcon(R.drawable.tasti)   // required android.R.drawable.ic_menu_slideshow
                .setContentText(VariabiliStaticheTasti.channelName) // required
                // .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(false)
                .setStyle(new NotificationCompat.InboxStyle())
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setAutoCancel(false)
                .setOngoing(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                // .setGroupAlertBehavior(GROUP_ALERT_SUMMARY)
                // .setGroup("LOO'S WEB PLAYER")
                .setGroupSummary(false)
                .setDeleteIntent(createOnDismissedIntent(context))
                // .setDefaults(NotificationCompat.DEFAULT_ALL)
                // .setContentIntent(pendingIntent)
                .setTicker("")
                .setContent(contentView)
                .build();

            notifica.bigContentView = contentView;

            manager.notify(VariabiliStaticheTasti.NOTIFICATION_CHANNEL_ID, notifica);

            return notifica;
        } catch (Exception e) {
            return null;
        }
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("com.looigi.wallpaperchanger2.notificationId", 1);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        1, intent, PendingIntent.FLAG_IMMUTABLE);
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
            /* Intent gps = new Intent(ctx, NotificationActionServiceTasti.class);
            gps.putExtra("DO", "gps");
            PendingIntent pGps = PendingIntent.getService(ctx, 200, gps,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgSwitchGPSTasti, pGps); */

            Intent settings = new Intent(ctx, ActivityDiStart.class);
            settings.addCategory(Intent.CATEGORY_LAUNCHER);
            settings.setAction(Intent.ACTION_MAIN );
            settings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            settings.putExtra("DO", "settings");
            PendingIntent pSettings = PendingIntent.getActivity(ctx, 201, settings,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgSettingsTasti, pSettings);

            Intent detector = new Intent(ctx, ActivityDiStart.class);
            detector.addCategory(Intent.CATEGORY_LAUNCHER);
            detector.setAction(Intent.ACTION_MAIN );
            detector.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            detector.putExtra("DO", "detector");
            PendingIntent pDetector = PendingIntent.getActivity(ctx,
                    202,
                    detector,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgDetectorTasti, pDetector);

            Intent ci = new Intent(ctx, ActivityDiStart.class);
            ci.addCategory(Intent.CATEGORY_LAUNCHER);
            ci.setAction(Intent.ACTION_MAIN );
            ci.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            ci.putExtra("DO", "controllo_immagini");
            PendingIntent pCi = PendingIntent.getActivity(ctx,
                    212,
                    ci,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgControlloImmagini, pCi);

            Intent wp = new Intent(ctx, ActivityDiStart.class);
            wp.addCategory(Intent.CATEGORY_LAUNCHER);
            wp.setAction(Intent.ACTION_MAIN );
            wp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            wp.putExtra("DO", "wallpaper");
            PendingIntent pWp = PendingIntent.getActivity(ctx, 203, wp,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgWallpaperTasti, pWp);

            Intent mappa = new Intent(ctx, ActivityDiStart.class);
            mappa.addCategory(Intent.CATEGORY_LAUNCHER);
            mappa.setAction(Intent.ACTION_MAIN );
            mappa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            mappa.putExtra("DO", "mappa");
            PendingIntent pMappa = PendingIntent.getActivity(ctx, 204, mappa,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgMappaTasti, pMappa);

            Intent player = new Intent(ctx, ActivityDiStart.class);
            player.addCategory(Intent.CATEGORY_LAUNCHER);
            player.setAction(Intent.ACTION_MAIN );
            player.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            player.putExtra("DO", "player");
            PendingIntent pPlayer = PendingIntent.getActivity(ctx, 205, player,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgPlayerTasti, pPlayer);

            Intent ono = new Intent(ctx, ActivityDiStart.class);
            ono.addCategory(Intent.CATEGORY_LAUNCHER);
            ono.setAction(Intent.ACTION_MAIN );
            ono.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            ono.putExtra("DO", "onomastici");
            PendingIntent pOno = PendingIntent.getActivity(ctx, 207, ono,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgOnomasticiTasti, pOno);

            Intent film = new Intent(ctx, ActivityDiStart.class);
            film.addCategory(Intent.CATEGORY_LAUNCHER);
            film.setAction(Intent.ACTION_MAIN );
            film.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            film.putExtra("DO", "films");
            PendingIntent pFilm = PendingIntent.getActivity(ctx, 206, film,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgFilmsTasti, pFilm);

            Intent fetekkie = new Intent(ctx, ActivityDiStart.class);
            fetekkie.addCategory(Intent.CATEGORY_LAUNCHER);
            fetekkie.setAction(Intent.ACTION_MAIN );
            fetekkie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            fetekkie.putExtra("DO", "fetekkie");
            PendingIntent pFetekkie = PendingIntent.getActivity(ctx, 211, fetekkie,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgFetekkieTasti, pFetekkie);

            Intent lazio = new Intent(ctx, ActivityDiStart.class);
            lazio.addCategory(Intent.CATEGORY_LAUNCHER);
            lazio.setAction(Intent.ACTION_MAIN );
            lazio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            lazio.putExtra("DO", "lazio");
            PendingIntent pLazio = PendingIntent.getActivity(ctx, 214, lazio,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgLazioTasti, pLazio);

            Intent drive = new Intent(ctx, ActivityDiStart.class);
            drive.addCategory(Intent.CATEGORY_LAUNCHER);
            drive.setAction(Intent.ACTION_MAIN );
            drive.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            drive.putExtra("DO", "drive");
            PendingIntent pDrive = PendingIntent.getActivity(ctx, 215, drive,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgDriveTasti, pDrive);

            Intent update = new Intent(ctx, ActivityDiStart.class);
            update.addCategory(Intent.CATEGORY_LAUNCHER);
            update.setAction(Intent.ACTION_MAIN );
            update.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            update.putExtra("DO", "update");
            PendingIntent pUpdate = PendingIntent.getActivity(ctx, 217, update,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgUpdateTasti, pUpdate);

            Intent pwd = new Intent(ctx, ActivityDiStart.class);
            pwd.addCategory(Intent.CATEGORY_LAUNCHER);
            pwd.setAction(Intent.ACTION_MAIN );
            pwd.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            pwd.putExtra("DO", "password");
            PendingIntent pPwd = PendingIntent.getActivity(ctx, 208, pwd,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgPasswordTasti, pPwd);

            Intent bac = new Intent(ctx, ActivityDiStart.class);
            bac.addCategory(Intent.CATEGORY_LAUNCHER);
            bac.setAction(Intent.ACTION_MAIN );
            bac.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            bac.putExtra("DO", "backup");
            PendingIntent pBac = PendingIntent.getActivity(ctx, 209, bac,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgBackupTasti, pBac);

            /* Intent uscita = new Intent(ctx, ActivityDiStart.class);
            uscita.addCategory(Intent.CATEGORY_LAUNCHER);
            uscita.setAction(Intent.ACTION_MAIN );
            uscita.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            uscita.putExtra("DO", "uscita");
            PendingIntent pUscita = PendingIntent.getActivity(ctx, 210, uscita,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgUscitaTasti, pUscita); */

            Intent mod = new Intent(ctx, ActivityDiStart.class);
            mod.addCategory(Intent.CATEGORY_LAUNCHER);
            mod.setAction(Intent.ACTION_MAIN );
            mod.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            mod.putExtra("DO", "modifiche");
            PendingIntent pMod = PendingIntent.getActivity(ctx, 212, mod,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgModificheTasti, pMod);

            Intent orari = new Intent(ctx, ActivityDiStart.class);
            orari.addCategory(Intent.CATEGORY_LAUNCHER);
            orari.setAction(Intent.ACTION_MAIN );
            orari.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
            orari.putExtra("DO", "orari");
            PendingIntent pOrari = PendingIntent.getActivity(ctx, 213, orari,
                    PendingIntent.FLAG_IMMUTABLE);
            view.setOnClickPendingIntent(R.id.imgOrariTasti, pOrari);
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
            } catch (Exception ignored) {
            }
        }
    }

    public void RimuoviNotifica() {
        // // Utility.getInstance().ScriveLog("Rimuovi notifica");
        if (manager != null) {
            try {
                manager.cancel(VariabiliStaticheTasti.getInstance().getIdNotifica());
                // manager.cancelAll();
                manager = null;
                contentView = null;
                // notificationBuilder = null;
                // notifica = null;
                // NOTIF_ID++;
            } catch (Exception e) {
                // // Utility.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    /* public static class NotificationActionServiceTasti extends Service {
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
                action = (String) Objects.requireNonNull(intent.getExtras()).get("DO");
                // Utility.getInstance().ScriveLog("Notifica: Action: " + action);
            } catch (Exception e) {
                int a = 0;
                // Utility.getInstance().ScriveLog(Utility.getInstance().PrendeErroreDaException(e));
            }

            if (action!=null) {
                if (context == null) {
                    context = UtilitiesGlobali.getInstance().tornaContextValido();
                }

                if (context != null) {
                    switch (action) {
                        /* case "gps":
                            boolean attivo = VariabiliStaticheGPS.getInstance().isBloccatoDaTasto();
                            VariabiliStaticheGPS.getInstance().setBloccatoDaTasto(!attivo);
                            if (!attivo) {
                                VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("NOTIFICA");
                            } else {
                                VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS();
                            }
                            GestioneNotificheTasti.getInstance().AggiornaNotifica();
                            break; * /
                        case "settings":
                            Intent iI = new Intent(context, MainImpostazioni.class);
                            iI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iI);
                            break;
                        case "detector":
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent iD = new Intent(context, MainActivityDetector.class);
                                    iD.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(iD);

                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            InizializzaMascheraDetector i2 = new InizializzaMascheraDetector();
                                            i2.inizializzaMaschera(
                                                    context,
                                                    VariabiliStaticheDetector.getInstance().getMainActivity());
                                        }
                                    }, 100);
                                }
                            }, 500);
                            break;
                        case "onomastici":
                            Intent iO = new Intent(context, MainOnomastici.class);
                            iO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iO);
                            break;
                        case "wallpaper":
                            Intent iW = new Intent(context, MainWallpaper.class);
                            iW.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iW);
                            break;
                        case "backup":
                            Intent iB = new Intent(context, MainBackup.class);
                            iB.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iB);
                            break;
                        case "mappa":
                            Intent iM = new Intent(context, MainMappa.class);
                            iM.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iM);
                            break;
                        case "password":
                            Intent iPa = new Intent(context, MainPassword.class);
                            iPa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iPa);
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

                                Handler handlerTimer = new Handler(Looper.getMainLooper());
                                Runnable rTimer = new Runnable() {
                                    public void run() {
                                        UtilityPlayer.getInstance().PressionePlay(context, false);
                                        UtilityPlayer.getInstance().ChiudeActivity(true);

                                        GestioneNotificheTasti.getInstance().AggiornaNotifica();
                                    }
                                };
                                handlerTimer.postDelayed(rTimer, 500);
                            }
                            break;
                        case "immagini":
                            Intent iIm = new Intent(context, MainMostraImmagini.class);
                            iIm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iIm);
                            break;
                        case "pennetta":
                            Intent iPe = new Intent(context, MainMostraPennetta.class);
                            iPe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iPe);
                            break;
                        case "video":
                            Intent iVi = new Intent(context, MainMostraVideo.class);
                            iVi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iVi);
                            break;
                        case "films":
                            Intent iF = new Intent(context, MainMostraFilms.class);
                            iF.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(iF);
                            break;
                        case "uscita":
                            UtilitiesGlobali.getInstance().ChiudeApplicazione(context);
                            break;
                    }
                }
            }

            return START_REDELIVER_INTENT ;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    } */
}
