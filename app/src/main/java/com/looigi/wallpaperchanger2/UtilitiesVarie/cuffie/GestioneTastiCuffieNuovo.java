package com.looigi.wallpaperchanger2.UtilitiesVarie.cuffie;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.media.session.MediaButtonReceiver;

import com.looigi.wallpaperchanger2.Detector.AndroidCameraApi;
import com.looigi.wallpaperchanger2.Detector.UtilityDetector;
import com.looigi.wallpaperchanger2.Detector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.Player.UtilityPlayer;
import com.looigi.wallpaperchanger2.Player.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import java.util.Date;

public class GestioneTastiCuffieNuovo extends Service {
    private static final String NomeMaschera = "Gestione_Tasti_Cuffie_Nuovo";
    private long ultimoClick = -1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final MediaSessionCompat.Callback mediaSessionCompatCallBack = new MediaSessionCompat.Callback()
    {
        @Override
        public void onPlay() {
            UtilitiesGlobali.getInstance().ApreToast(context, "Premuto PLAY 1");

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Premuto Play 1");

            super.onPlay();
        }

        @Override
        public void onPause() {
            UtilitiesGlobali.getInstance().ApreToast(context, "Premuto PAUSE 1");

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Premuto Pause 1");

            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            UtilitiesGlobali.getInstance().ApreToast(context, "Premuto NEXT 1");

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Premuto NEXT 1");

            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            UtilitiesGlobali.getInstance().ApreToast(context, "Premuto PREV 1");

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Premuto Prev 1");

            super.onSkipToPrevious();
        }

        @Override
        public void onStop() {
            UtilitiesGlobali.getInstance().ApreToast(context, "Premuto STOP 1");

            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Premuto Stop");

            super.onStop();
        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Premuto Media Button");

            String intentAction = mediaButtonEvent.getAction();

            if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction))
            {
                KeyEvent event = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

                boolean come = false;

                if (event != null)
                {
                    int action = event.getAction();

                    // if (action == 1) {
                        // int codice = event.getKeyCode();

                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                            "Premuto Media Button. Evento: " + action);

                    long clickAttuale = new Date().getTime();
                    long diff = clickAttuale - ultimoClick;

                    // if (action == KeyEvent.ACTION_DOWN) {
                        switch (event.getKeyCode()) {
                            case KeyEvent.KEYCODE_MEDIA_PLAY:
                                if (VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                            "Premuto Play");

                                    UtilityPlayer.getInstance().PressionePlay(context, true);

                                    UtilitiesGlobali.getInstance().ApreToast(context,
                                            "Premuto PLAY");
                                }
                                come = true;
                                break;
                            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                                if (VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                            "Premuto Pause");

                                    UtilityPlayer.getInstance().PressionePlay(context, false);

                                    UtilitiesGlobali.getInstance().ApreToast(context,
                                            "Premuto PAUSE");
                                }
                                come = true;
                                break;
                            case KeyEvent.KEYCODE_MEDIA_NEXT:
                                if (diff < 2000) {
                                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                            "Premuto NEXT Button. Skip per troppo veloce");

                                } else {
                                    if (VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                "Premuto Next");

                                        UtilityPlayer.getInstance().ResettaCampi(context, false);

                                        UtilityPlayer.getInstance().BranoAvanti(context, "",
                                                false, true);

                                        UtilitiesGlobali.getInstance().ApreToast(context,
                                                "Premuto NEXT");
                                    }
                                    come = true;
                                }
                                break;
                            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                                if (VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                                    if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                "Premuto Pause");

                                        UtilityPlayer.getInstance().PressionePlay(context, false);

                                        UtilitiesGlobali.getInstance().ApreToast(context,
                                                "Premuto PAUSE");
                                    } else {
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                "Premuto Play");

                                        UtilityPlayer.getInstance().PressionePlay(context, true);

                                        UtilitiesGlobali.getInstance().ApreToast(context,
                                                "Premuto PLAY");
                                    }
                                }
                                come = true;
                                break;
                            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                                if (VariabiliStaticheDetector.getInstance().isFotoSuTriploTastoCuffie()) {
                                    Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
                                    Context context = UtilitiesGlobali.getInstance().tornaContextValido();

                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent myIntent = new Intent(
                                                    act,
                                                    AndroidCameraApi.class);
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            act.startActivity(myIntent);

                                            UtilityDetector.getInstance().SpegneSchermo(context);
                                        }
                                    }, 100);

                                    come = true;
                                } else {
                                    long diff2 = clickAttuale - ultimoClick;
                                    if (diff2 < 2000) {
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                "Premuto NEXT Button. Skip per troppo veloce");

                                        come = false;
                                    } else {
                                        if (VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                                            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                    "Premuto Previous");

                                            UtilityPlayer.getInstance().ResettaCampi(context, false);

                                            UtilityPlayer.getInstance().IndietroBrano(context);

                                            UtilitiesGlobali.getInstance().ApreToast(context,
                                                    "Premuto PREVIOUS");
                                        }
                                        come = true;
                                    }
                                }
                                break;
                            case KeyEvent.KEYCODE_MEDIA_REWIND:
                                // code for rewind
                                come = true;
                                break;
                            case KeyEvent.KEYCODE_MEDIA_STOP:
                                if (VariabiliStaticheStart.getInstance().isPlayerAperto()) {
                                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                            "Premuto Stop");

                                    UtilityPlayer.getInstance().PressionePlay(context, false);

                                    UtilitiesGlobali.getInstance().ApreToast(context,
                                            "Premuto STOP");
                                }
                                come = true;
                                break;
                        }

                    /* } else {
                        if (action == KeyEvent.ACTION_UP) {
                            UtilitiesGlobali.getInstance().ApreToast(context, "Premuto ACTION UP");
                        } else {
                            if (action == KeyEvent.ACTION_DOWN) {
                                UtilitiesGlobali.getInstance().ApreToast(context, "Premuto ACTION DOWN");
                            }
                        }
                    } */

                    ultimoClick = clickAttuale;
                }

                return come;
            }

            return super.onMediaButtonEvent(mediaButtonEvent);
        }
    };

    private MediaSessionCompat mediaSessionCompat;
    private Context context;
    
    @Override
    public void onCreate() {
        // Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        // Log.e("SERVICE", "onCreate");

        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "onCreate");

        this.context = this;
        
        mediaSessionCompat = new MediaSessionCompat(this, "MEDIABUTTONS");
        mediaSessionCompat.setCallback(mediaSessionCompatCallBack);
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                );

        mediaSessionCompat.setPlaybackState(mStateBuilder.build());
        mediaSessionCompat.setActive(true);
    }

    @Override
    public void onDestroy() {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "onDestroy");

        mediaSessionCompat.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                "onStart");

        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent);

        return super.onStartCommand(intent, flags, startId);
    }
}
