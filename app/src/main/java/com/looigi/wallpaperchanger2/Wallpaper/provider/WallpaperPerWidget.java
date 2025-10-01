package com.looigi.wallpaperchanger2.Wallpaper.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Wallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.Wallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.Wallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;

import java.util.Date;

public class WallpaperPerWidget extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_attesa_wp);

        act = this;
        context = this; // VariabiliStatiche.getInstance().getContext();

        long ora = new Date().getTime();
        long diff = VariabiliStaticheWallpaper.getInstance().getUltimoCambio();
        if (ora - diff < 2000) {
            return;
        }
        VariabiliStaticheWallpaper.getInstance().setUltimoCambio(ora);

        Intent intent = getIntent();
        String id = intent.getStringExtra("DO");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (id) {
                    case "cambia":
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UtilityWallpaper.getInstance().CambiaImmagine(context);

                                GestioneNotificheWP.getInstance().AggiornaNotifica();

                                act.finish();
                            }
                        }, 100);
                        break;
                    case "refresh":
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ChangeWallpaper c = new ChangeWallpaper(context, "WALLPAPER",
                                        VariabiliStaticheWallpaper.getInstance().getUltimaImmagine());
                                c.setWallpaperLocale(context,
                                        VariabiliStaticheWallpaper.getInstance().getUltimaImmagine());
                                UtilityWallpaper.getInstance().Attesa(false);

                                act.finish();
                            }
                        }, 100);
                        break;
                    default:
                        act.finish();
                }
            }
        }, 100);

    }
}
