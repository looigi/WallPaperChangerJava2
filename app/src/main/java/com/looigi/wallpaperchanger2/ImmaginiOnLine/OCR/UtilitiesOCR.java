package com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR;

import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class UtilitiesOCR {
    private static UtilitiesOCR instance = null;

    private UtilitiesOCR() {
    }

    public static UtilitiesOCR getInstance() {
        if (instance == null) {
            instance = new UtilitiesOCR();
        }

        return instance;
    }

    public void Attesa(boolean Acceso) {
        if (VariabiliStaticheOCR.getInstance().getImgCaricamento() == null) {
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Acceso) {
                    VariabiliStaticheOCR.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                } else {
                    VariabiliStaticheOCR.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
                }
            }
        }, 50);
    }
}
