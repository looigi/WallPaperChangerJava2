package com.looigi.wallpaperchanger2.classiPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public class GestioneTastiCuffie extends BroadcastReceiver {
    public GestioneTastiCuffie() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // DA TOGLIERE
        UtilityPlayer.getInstance().BranoAvanti(context, "", false);
        // DA TOGLIERE

        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            // do something
            Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
        }
        abortBroadcast();
    }
}