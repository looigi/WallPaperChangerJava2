package com.looigi.wallpaperchanger2.notificaTasti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classiWallpaper.AdapterListenerImmagini;
import com.looigi.wallpaperchanger2.classiWallpaper.StrutturaImmagine;

import java.util.ArrayList;
import java.util.List;

public class VariabiliStaticheTasti {
    private static VariabiliStaticheTasti instance = null;

    private VariabiliStaticheTasti() {
    }

    public static VariabiliStaticheTasti getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheTasti();
        }

        return instance;
    }

    private int idNotifica = 111119;
    public static String channelName = "WallPaperChangerII_Tasti";
    public static String NOTIFICATION_CHANNEL_STRING = "com.looigi.wallpaperchanger2";
    public static int NOTIFICATION_CHANNEL_ID = 7;

    public int getIdNotifica() {
        return idNotifica;
    }
}