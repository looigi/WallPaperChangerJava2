package com.looigi.wallpaperchanger2.classeBackup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;

public class MainBackup extends Activity {
    private Activity act;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_backup);

        act = this;
        context = this;
    }
}
