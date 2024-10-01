package com.looigi.wallpaperchanger2.classiAttivitaDetector;

/* import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class VolumePressed extends BroadcastReceiver {
    private static final String NomeMaschera = "VOLUMEPRESSED";

    public VolumePressed() {
        super();

        UtilityWallpaper.getInstance().ScriveLog(
                VariabiliStaticheStart.getInstance().getMainActivity(),
                NomeMaschera,
                "Instanziamento");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Azione: " + intentAction);

        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }

        KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }

        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "ACTION DOWN");

            Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
        }

        abortBroadcast();
    }
} */

/* public class VolumePressed extends ContentObserver {
    private int previousVolume;
    private Context context;
    private Long datella1 = null;
    private int lastVolume;
    private Activity act;

    public VolumePressed(Context c, Handler handler) {
        super(handler);

        context=c;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (previousVolume != volume) {
            previousVolume = volume;

            if (datella1 == null) {
                Handler handlerTimer;
                Runnable rTimer;

                datella1 = System.currentTimeMillis();
                UtilityDetector.getInstance().Vibra(context, 100);

                handlerTimer = new Handler();
                rTimer = new Runnable() {
                    public void run() {
                        datella1 = null;
                    }
                };
                handlerTimer.postDelayed(rTimer, 2000);
            } else {
                long diff = System.currentTimeMillis() - datella1;

                datella1 = null;

                if (diff < 1950) {
                    // audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume);
                    UtilityDetector.getInstance().Vibra(context, 1000);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent myIntent = new Intent(
                                    VariabiliStaticheStart.getInstance().getContext(),
                                    AndroidCameraApi.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            VariabiliStaticheStart.getInstance().getContext().startActivity(myIntent);
                        }
                    }, 1000);

                    VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                    VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
                }
            }
        }
    }
} */