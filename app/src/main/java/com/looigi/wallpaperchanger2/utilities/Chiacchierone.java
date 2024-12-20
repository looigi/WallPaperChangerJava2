package com.looigi.wallpaperchanger2.utilities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chiacchierone {
    private Context context;
    private TextToSpeech t1;
    private StrutturaBrano sb;

    public Chiacchierone(Context context, StrutturaBrano sb) {
        this.context = context;

        if (sb != null) {
            this.sb = sb;

            String Cosa = sb.getArtista() + " " + sb.getBrano() + ".";

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();
                    languageIdentifier.identifyLanguage(Cosa).addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                assert languageCode != null;

                                t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int status) {
                                        if (status != TextToSpeech.ERROR) {
                                            switch (languageCode) {
                                                case "it":
                                                    t1.setLanguage(Locale.ITALIAN);
                                                    break;
                                                case "de":
                                                    t1.setLanguage(Locale.GERMANY);
                                                    break;
                                                default:
                                                    t1.setLanguage(Locale.UK);
                                                    break;
                                            }

                                            Bundle params = new Bundle();
                                            params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 0.9f); // change the 0.5f to any value from 0f-1f (1f is default)

                                            t1.speak(Cosa, TextToSpeech.QUEUE_FLUSH, params,null);

                                            Termina();
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be loaded or other internal error.
                            }
                        });

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //UI Thread work here
                        }
                    });
                }
            });
        }
    }

    private void Termina() {
        if(t1 !=null) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    if (t1.isSpeaking()) {
                        handlerTimer.postDelayed(this, 500);
                    } else {
                        t1.stop();
                        t1.shutdown();

                        if (sb != null) {
                            Handler handlerTimer = new Handler(Looper.getMainLooper());
                            Runnable rTimer = new Runnable() {
                                public void run() {
                                    if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                                        UtilityPlayer.getInstance().FaiPartireTimer(context);
                                        VariabiliStatichePlayer.getInstance().getMp().start();
                                    } else {
                                        UtilityPlayer.getInstance().FermaTimer();
                                    }
                                }
                            };
                            handlerTimer.postDelayed(rTimer, 500);
                        }

                        handlerTimer.removeCallbacksAndMessages(this);
                        handlerTimer.removeCallbacks(this);
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 500);
        }
    }
}
