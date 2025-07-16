package com.looigi.wallpaperchanger2.classePazzia;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;

public class MainPazzia extends AppCompatActivity {
    private Context context;
    private Activity act;
    private Handler handlerPEN;
    private Runnable updateRunnablePEN;
    private Handler handlerIMM;
    private Runnable updateRunnableIMM;
    private int SecondiPEN = 10000;
    private int SecondiIMM = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pazzia);

        context = this;
        act = this;

        VariabiliStatichePazzia.getInstance().setImgPennetta(findViewById(R.id.imgPennetta));
        VariabiliStatichePazzia.getInstance().setImgImmagini(findViewById(R.id.imgImmagini));
        VariabiliStatichePazzia.getInstance().setVideoView(findViewById(R.id.videoView));

        VariabiliStatichePazzia.getInstance().setImgCaricamentoPEN(findViewById(R.id.imgCaricamentoPEN));
        VariabiliStatichePazzia.getInstance().setImgCaricamentoIMM(findViewById(R.id.imgCaricamentoIMM));
        VariabiliStatichePazzia.getInstance().setImgCaricamentoVID(findViewById(R.id.imgCaricamentoVID));

        UtilityPazzia.getInstance().CambiaImmaginePennetta(context);
        UtilityPazzia.getInstance().CambiaImmagineImmagine(context);
        UtilityPazzia.getInstance().CambiaVideo(context);

        AttivaTimerPEN();
        AttivaTimerIMM();
    }

    private void bloccaTimerPEN() {
        if (handlerPEN != null) {
            handlerPEN.removeCallbacks(updateRunnablePEN);
            updateRunnablePEN = null;
        }
    }

    private void bloccaTimerIMM() {
        if (handlerIMM != null) {
            handlerIMM.removeCallbacks(updateRunnableIMM);
            updateRunnableIMM = null;
        }
    }

    private void AttivaTimerPEN() {
        bloccaTimerPEN();

        handlerPEN = new Handler();
        updateRunnablePEN = new Runnable() {
            @Override
            public void run() {
                handlerPEN.postDelayed(this, SecondiPEN);
            }
        };
        handlerPEN.postDelayed(updateRunnablePEN, 0);
    }

    private void AttivaTimerIMM() {
        bloccaTimerIMM();

        handlerIMM = new Handler();
        updateRunnableIMM = new Runnable() {
            @Override
            public void run() {
                handlerIMM.postDelayed(this, SecondiIMM);
            }
        };
        handlerIMM.postDelayed(updateRunnableIMM, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                bloccaTimerPEN();
                bloccaTimerIMM();

                this.finish();

                return false;
        }

        return false;
    }
}
