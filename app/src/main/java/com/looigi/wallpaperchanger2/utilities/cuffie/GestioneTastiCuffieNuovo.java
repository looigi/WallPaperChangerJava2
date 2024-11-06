package com.looigi.wallpaperchanger2.utilities.cuffie;

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

import com.looigi.wallpaperchanger2.classeDetector.AndroidCameraApi;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

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
            UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto PLAY 1");

            UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                    "Premuto Play 1");

            super.onPlay();
        }

        @Override
        public void onPause() {
            UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto PAUSE 1");

            UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                    "Premuto Pause 1");

            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto NEXT 1");

            UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                    "Premuto NEXT 1");

            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto PREV 1");

            UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                    "Premuto Prev 1");

            super.onSkipToPrevious();
        }

        @Override
        public void onStop() {
            UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto STOP 1");

            UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                    "Premuto Stop");

            super.onStop();
        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
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

                    UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                            "Premuto Media Button. Evento: " + action);

                    long clickAttuale = new Date().getTime();
                    long diff = clickAttuale - ultimoClick;

                    // if (action == KeyEvent.ACTION_DOWN) {
                        switch (event.getKeyCode()) {
                            case KeyEvent.KEYCODE_MEDIA_PLAY:
                                UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                        "Premuto Play");

                                UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), true);

                                UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(),
                                        "Premuto PLAY");

                                come = true;
                            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                                UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                        "Premuto Pause");

                                UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), false);

                                UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(),
                                        "Premuto PAUSE");

                                come = true;
                            case KeyEvent.KEYCODE_MEDIA_NEXT:
                                if (diff < 2000) {
                                    UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                            "Premuto NEXT Button. Skip per troppo veloce");

                                    come = false;
                                } else {
                                    UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                            "Premuto Next");

                                    UtilityPlayer.getInstance().ResettaCampi(VariabiliStatichePlayer.getInstance().getContext(), false);

                                    UtilityPlayer.getInstance().BranoAvanti(getApplicationContext(), "", false, true);

                                    UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(),
                                            "Premuto NEXT");

                                    come = true;
                                }
                            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                                if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                                    UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                            "Premuto Pause");

                                    UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), false);

                                    UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(),
                                            "Premuto PAUSE");
                                } else {
                                    UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                            "Premuto Play");

                                    UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), true);

                                    UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(),
                                            "Premuto PLAY");
                                }

                                come = true;
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
                                        UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                                "Premuto NEXT Button. Skip per troppo veloce");

                                        come = false;
                                    } else {
                                        UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                                "Premuto Previous");

                                        UtilityPlayer.getInstance().ResettaCampi(VariabiliStatichePlayer.getInstance().getContext(), false);

                                        UtilityPlayer.getInstance().IndietroBrano(
                                                VariabiliStatichePlayer.getInstance().getContext());

                                        UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(),
                                                "Premuto PREVIOUS");

                                        come = true;
                                    }
                                }
                            case KeyEvent.KEYCODE_MEDIA_REWIND:
                                // code for rewind
                                come = true;
                            case KeyEvent.KEYCODE_MEDIA_STOP:
                                UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                        "Premuto Stop");

                                UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), false);

                                UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(),
                                        "Premuto STOP");

                                come = true;
                        }

                    /* } else {
                        if (action == KeyEvent.ACTION_UP) {
                            UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto ACTION UP");
                        } else {
                            if (action == KeyEvent.ACTION_DOWN) {
                                UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto ACTION DOWN");
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

    @Override
    public void onCreate() {
        // Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        // Log.e("SERVICE", "onCreate");

        UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                "onCreate");

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
        UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                "onDestroy");

        mediaSessionCompat.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                "onStart");

        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent);

        return super.onStartCommand(intent, flags, startId);
    }
}
