package com.looigi.wallpaperchanger2.classePlayer.cuffie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.media.session.MediaButtonReceiver;

import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class GestioneTastiCuffieNuovo extends Service {
    private static final String NomeMaschera = "Gestione_Tasti_Cuffie_Nuovo";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final MediaSessionCompat.Callback mediaSessionCompatCallBack = new MediaSessionCompat.Callback()
    {
        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onStop() {
            super.onStop();
            UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                    "Premuto Stop");

            UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), false);
        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                    "Premuto Media Button");

            String intentAction = mediaButtonEvent.getAction();

            if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction))
            {
                KeyEvent event = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

                if (event != null)
                {
                    int action = event.getAction();

                    UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                            "Premuto Media Button. Evento: " + action);

                    if (action == KeyEvent.ACTION_DOWN) {
                        switch (event.getKeyCode()) {
                            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                                // code for fast forward
                                return true;
                            case KeyEvent.KEYCODE_MEDIA_NEXT:
                                UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                        "Premuto Next");

                                UtilityPlayer.getInstance().BranoAvanti(getApplicationContext(), "", false);

                                UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto NEXT");
                                return true;
                            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                                if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                                    UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                            "Premuto Pause");

                                    UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), false);

                                    UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto PAUSE");
                                } else {
                                    UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                            "Premuto Play");

                                    UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), true);

                                    UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto PLAY");
                                }
                                return true;
                            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                                UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                        "Premuto Previous");

                                // UtilityPlayer.getInstance().(getApplicationContext(), "", false);

                                UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto PREVIOUS");
                                return true;
                            case KeyEvent.KEYCODE_MEDIA_REWIND:
                                // code for rewind
                                return true;
                            case KeyEvent.KEYCODE_MEDIA_STOP:
                                UtilityPlayer.getInstance().ScriveLog(getApplicationContext(), NomeMaschera,
                                        "Premuto Stop");

                                UtilityPlayer.getInstance().PressionePlay(getApplicationContext(), false);

                                UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto STOP");
                                return true;
                        }
                        return false;

                    }
                    if (action == KeyEvent.ACTION_UP) {
                        UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto ACTION UP");
                    }
                    if (action == KeyEvent.ACTION_DOWN) {
                        UtilitiesGlobali.getInstance().ApreToast(getApplicationContext(), "Premuto ACTION DOWN");
                    }
                }
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

        mediaSessionCompat = new MediaSessionCompat(this, "MEDIA");
        mediaSessionCompat.setCallback(mediaSessionCompatCallBack);
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        PlaybackStateCompat.Builder mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

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
