package com.looigi.wallpaperchanger2.classeAntifurto;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class VolumePlayer {
    private AudioManager audioManager;
    private int originalVolume;
    private MediaPlayer mediaPlayer;
    private boolean stoppedByUser = false;

    public VolumePlayer(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void playSoundAtMax(Context context, int resId) {
        if (audioManager == null) return;

        stoppedByUser = false; // reset del flag

        // Salva il volume corrente
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // Imposta al massimo
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

        startPlayer(context, resId);
    }

    private void startPlayer(Context context, int resId) {
        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            if (!stoppedByUser) {
                // Ricomincia se non è stato fermato manualmente
                startPlayer(context, resId);
            } else {
                restoreVolume();
            }
        });
        mediaPlayer.start();
    }

    public void stop() {
        stoppedByUser = true;
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        restoreVolume();
    }

    private void restoreVolume() {
        if (audioManager != null) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        }
    }
}
