package com.looigi.wallpaperchanger2.utilities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricManager;

import java.util.concurrent.Executor;

public class BiometricManagerSingleton {
    // Volatile per sicurezza con double-checked locking
    private static volatile BiometricManagerSingleton instance;
    private final Context appContext;
    private final Executor executor;

    // Non tenere Activity/Context non-application per evitare leak.
    private BiometricManagerSingleton(Context context) {
        this.appContext = context.getApplicationContext();
        this.executor = ContextCompat.getMainExecutor(appContext);
    }

    public static BiometricManagerSingleton getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (BiometricManagerSingleton.class) {
                if (instance == null) {
                    instance = new BiometricManagerSingleton(context);
                }
            }
        }
        return instance;
    }

    /**
     * Controlla se il dispositivo supporta l'autenticazione biometrica e se è configurata.
     */
    public int canAuthenticate() {
        BiometricManager bm = BiometricManager.from(appContext);
        // Restituisce: BIOMETRIC_SUCCESS, BIOMETRIC_ERROR_NO_HARDWARE, BIOMETRIC_ERROR_HW_UNAVAILABLE, BIOMETRIC_ERROR_NONE_ENROLLED
        return bm.canAuthenticate();
    }

    /**
     * Mostra il prompt biometrico. L'activity passata serve come lifecycle owner.
     * callback: BiometricPrompt.AuthenticationCallback per gestire successo/errore.
     */
    public void authenticate(@NonNull FragmentActivity activity,
                             @NonNull String title,
                             @NonNull String subtitle,
                             @NonNull BiometricPrompt.AuthenticationCallback callback) {

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setNegativeButtonText("Usa PIN")
                .setConfirmationRequired(false)
                .build();

        BiometricPrompt prompt = new BiometricPrompt(activity, executor, callback);
        prompt.authenticate(promptInfo);
    }
}
