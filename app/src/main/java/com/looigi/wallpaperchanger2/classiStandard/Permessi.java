package com.looigi.wallpaperchanger2.classiStandard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;

import static android.content.Context.POWER_SERVICE;
import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

public class Permessi {
    private Context context;
    private Activity act;

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public boolean ControllaPermessi(Activity context) {
        int permissionRequestCode1 = 1193;
        this.context = context;
        this.act = context;

        String[] PERMISSIONS = new String[]{
                // Manifest.permission.WRITE_EXTERNAL_STORAGE,
                // Manifest.permission.READ_EXTERNAL_STORAGE,

                Manifest.permission.READ_MEDIA_IMAGES,

                // Manifest.permission.READ_EXTERNAL_STORAGE,
                // Manifest.permission.WRITE_EXTERNAL_STORAGE,

                Manifest.permission.FOREGROUND_SERVICE_CAMERA,
                Manifest.permission.FOREGROUND_SERVICE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.FOREGROUND_SERVICE,
                // android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                android.Manifest.permission.INTERNET,
                Manifest.permission.SET_WALLPAPER,
                // Manifest.permission.RECORD_AUDIO,
                Manifest.permission.VIBRATE,
                // Manifest.permission.REQUEST_INSTALL_PACKAGES,
                // Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                // Manifest.permission.WAKE_LOCK,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                // android.Manifest.permission.BLUETOOTH,
                // android.Manifest.permission.BLUETOOTH_ADMIN
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            // PERMESSO BATTERIA SENZA RESTRIZIONI
            Intent intent2 = new Intent();
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent2.setAction(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent2.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent2);
            }
            // PERMESSO BATTERIA SENZA RESTRIZIONI

            // PERMESSO FRONTE SCHERMO
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
            );
            context.startActivityForResult(intent, VariabiliStaticheWallpaper.channelIdIntentOverlay);
            // PERMESSO FRONTE SCHERMO

            // ALTRI PERMESSI
            ActivityCompat.requestPermissions(context,
                    PERMISSIONS, permissionRequestCode1);
            // ALTRI PERMESSI

            return false;
        } else {
            return true;
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        if (!Settings.canDrawOverlays(context)) {
            return false;
        }

        return true;
    }
}