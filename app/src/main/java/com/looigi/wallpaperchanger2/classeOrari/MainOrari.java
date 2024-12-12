package com.looigi.wallpaperchanger2.classeOrari;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;

public class MainOrari extends Activity {
    private Context context;
    private Activity act;

    public MainOrari() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_orari);

        context = this;
        act = this;
    }
}
