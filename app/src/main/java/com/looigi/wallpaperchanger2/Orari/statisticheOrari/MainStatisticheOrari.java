package com.looigi.wallpaperchanger2.Orari.statisticheOrari;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;

public class MainStatisticheOrari extends Activity {
    private Context context;
    private Activity act;

    public MainStatisticheOrari() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_statistiche_orari);

        context = this;
        act = this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return false;
        }

        return false;
    }
}
