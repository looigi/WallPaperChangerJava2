package com.looigi.wallpaperchanger2.UtilitiesVarie.InformazioniTelefono;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.looigi.wallpaperchanger2.UtilitiesVarie.InformazioniTelefono.Strutture.StrutturaModelloTelefono;

public class EmulatorUtils {

    /**
     * Ritorna informazioni dettagliate sull'emulatore o device
     * @param context Context dell'app
     * @return stringa con tipo di device e ID univoco
     */
    public StrutturaModelloTelefono getDeviceIdentifier(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        boolean isEmulator = isEmulator();

        StringBuilder sb = new StringBuilder();
        sb.append("Type: ").append(isEmulator ? "Emulator" : "Physical Device").append("\n");
        sb.append("Model: ").append(Build.MODEL).append("\n");
        sb.append("Device: ").append(Build.DEVICE).append("\n");
        sb.append("Product: ").append(Build.PRODUCT).append("\n");
        sb.append("Manufacturer: ").append(Build.MANUFACTURER).append("\n");
        sb.append("ANDROID_ID: ").append(androidId);

        StrutturaModelloTelefono s = new StrutturaModelloTelefono();
        s.setType(isEmulator ? "Emulator" : "Physical Device");
        s.setModel(Build.MODEL);
        s.setDevice(Build.DEVICE);
        s.setProduct(Build.PRODUCT);
        s.setManufacturer(Build.MANUFACTURER);
        s.setANDROID_ID(androidId);

        return s;
    }

    /**
     * Controlla se il dispositivo è un emulatore
     * @return true se è un emulatore
     */
    private static boolean isEmulator() {
        String fingerprint = Build.FINGERPRINT;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String manufacturer = Build.MANUFACTURER;
        String brand = Build.BRAND;
        String device = Build.DEVICE;

        return fingerprint.startsWith("generic")
                || fingerprint.startsWith("unknown")
                || model.contains("google_sdk")
                || model.contains("Emulator")
                || model.contains("Android SDK built for x86")
                || manufacturer.contains("Genymotion")
                || brand.startsWith("generic") && device.startsWith("generic")
                || "google_sdk".equals(product);
    }
}
