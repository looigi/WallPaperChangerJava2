package com.looigi.wallpaperchanger2.classeLazio;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;

public class MainLazio extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lazio);

        context = this;
        act = this;

        VariabiliStaticheLazio.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoLazio));
        VariabiliStaticheLazio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
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
