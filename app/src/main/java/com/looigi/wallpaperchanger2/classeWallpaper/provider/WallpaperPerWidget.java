package com.looigi.wallpaperchanger2.classeWallpaper.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;

public class WallpaperPerWidget extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.camera);

        act = this;
        context = this; // VariabiliStatiche.getInstance().getContext();

        Intent intent = getIntent();
        String id = intent.getStringExtra("DO");

        switch (id) {
            case "cambia":
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UtilityWallpaper.getInstance().CambiaImmagine(context);

                        GestioneNotificheWP.getInstance().AggiornaNotifica();
                    }
                }, 100);
                break;
            case "refresh":
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChangeWallpaper c = new ChangeWallpaper(context);
                        c.setWallpaperLocale(context,
                                VariabiliStaticheWallpaper.getInstance().getUltimaImmagine());
                        UtilityWallpaper.getInstance().Attesa(false);
                    }
                }, 100);
                break;
        }

        act.finish();
    }
}
