package com.looigi.wallpaperchanger2.Player;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

public class AprePlayerDaWidget extends Activity {
    public AprePlayerDaWidget() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VariabiliStaticheStart.getInstance().setPlayerAperto(true);
        VariabiliStatichePlayer.getInstance().setNonMostrareToast(true);

        Intent iP = new Intent(this, MainPlayer.class);
        iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(iP);

        Notification notificaPlayer = GestioneNotifichePlayer.getInstance().StartNotifica(this);
        if (notificaPlayer != null) {
            GestioneNotifichePlayer.getInstance().AggiornaNotifica("Titolo Canzone");

            // UtilitiesGlobali.getInstance().ApreToast(this, "Player Partito");
        }

        this.finish();
    }
}
