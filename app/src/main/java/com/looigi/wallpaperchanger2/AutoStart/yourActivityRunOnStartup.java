package com.looigi.wallpaperchanger2.AutoStart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.looigi.wallpaperchanger2.MainStart;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class yourActivityRunOnStartup extends BroadcastReceiver {
    public yourActivityRunOnStartup() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            try {
                Intent i = new Intent(context, MainStart.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            } catch (Exception e) {
                UtilitiesGlobali.getInstance().ApreToast(context, "Service Started");

                // Toast.makeText(context, VariabiliStaticheServizio.channelName + ": Service Started", Toast.LENGTH_SHORT).show();
            }
        }
    }
}