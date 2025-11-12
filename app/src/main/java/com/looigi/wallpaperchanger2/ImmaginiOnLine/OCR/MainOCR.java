package com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.Detector.Impostazioni;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.VariabiliStaticheRilevaOCRJava;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.webService.ChiamateWSOCR;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

public class MainOCR extends Activity {
    private Context context;
    private Activity act = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_ocr);

        context = this;
        act = this;

        VariabiliStaticheOCR.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoOCR));
        // UtilitiesOCR.getInstance().Attesa(false);
        UtilitiesGlobali.getInstance().AttesaGif(context, VariabiliStaticheOCR.getInstance().getImgCaricamento(), false);

        VariabiliStaticheOCR.getInstance().setLstDestinazioni(findViewById(R.id.lstDestinazioni));
        VariabiliStaticheOCR.getInstance().setLstImmagini(findViewById(R.id.lstImmagini));

        ChiamateWSOCR ws = new ChiamateWSOCR(context);
        ws.RitornaDestinazioni();

        ChiamateWSMI ws2 = new ChiamateWSMI(context);
        ws2.RitornaCategorie(false, "OCR");

        RadioButton optDate = findViewById(R.id.optDate);
        RadioButton optNomi = findViewById(R.id.optNomi);
        RadioButton optTags = findViewById(R.id.optTags);
        SwitchCompat swcAncheVuote = findViewById(R.id.swtAncheVuote);

        if (VariabiliStaticheRilevaOCRJava.getInstance().getModalita() == 1) {
            optDate.setChecked(true);
            optTags.setChecked(false);
            optNomi.setChecked(false);
            swcAncheVuote.setVisibility(LinearLayout.GONE);
        } else {
            if (VariabiliStaticheRilevaOCRJava.getInstance().getModalita() == 2) {
                optDate.setChecked(false);
                optTags.setChecked(false);
                optNomi.setChecked(true);
                swcAncheVuote.setVisibility(LinearLayout.VISIBLE);
            } else {
                optDate.setChecked(false);
                optTags.setChecked(true);
                optNomi.setChecked(false);
                swcAncheVuote.setVisibility(LinearLayout.GONE);
            }
        }

        optDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheRilevaOCRJava.getInstance().setModalita(1);

                optDate.setChecked(true);
                optNomi.setChecked(false);
                optTags.setChecked(false);
                swcAncheVuote.setVisibility(LinearLayout.GONE);

                ws.RitornaDestinazioni();
            }
        });

        optNomi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheRilevaOCRJava.getInstance().setModalita(2);

                optDate.setChecked(false);
                optNomi.setChecked(true);
                optTags.setChecked(false);
                swcAncheVuote.setVisibility(LinearLayout.VISIBLE);

                ws.RitornaDestinazioni();
            }
        });

        optTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheRilevaOCRJava.getInstance().setModalita(3);

                optDate.setChecked(false);
                optNomi.setChecked(false);
                optTags.setChecked(true);
                swcAncheVuote.setVisibility(LinearLayout.GONE);

                ws.RitornaDestinazioni();
            }
        });

        swcAncheVuote.setChecked(VariabiliStaticheOCR.getInstance().isAncheDestinazioniVuote());
        swcAncheVuote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheOCR.getInstance().setAncheDestinazioniVuote(swcAncheVuote.isChecked());
                ws.RitornaDestinazioni();
            }
        });

        EditText edtFiltro = findViewById(R.id.edtFiltro);

        ImageView imgCerca = findViewById(R.id.imgCerca);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Filtro = edtFiltro.getText().toString();

                ws.RitornaImmaginiFiltro(Filtro);
            }
        });

        ImageView imgRefresh = findViewById(R.id.imgRefreshDest);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ws.RitornaDestinazioni();
            }
        });

        ImageView imgRefreshImm = findViewById(R.id.imgRefreshImm);
        imgRefreshImm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheOCR.getInstance().getFiltroPremuto() != null &&
                        !VariabiliStaticheOCR.getInstance().getFiltroPremuto().isEmpty()) {
                    ws.RitornaImmagini(VariabiliStaticheOCR.getInstance().getFiltroPremuto());
                }
            }
        });

        ImageView imgRefreshTutto = findViewById(R.id.imgRefreshTutto);
        imgRefreshTutto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ws.AggiornaCategorieOCR();
            }
        });

        ImageView imgNuovaCategoria = findViewById(R.id.imgNuovaCategoria);
        imgNuovaCategoria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Immagini OCR");
                builder.setMessage("Nome nuova categoria");

                // Crea l'EditText
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText("");
                builder.setView(input);

                // Bottone OK
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String valoreInserito = input.getText().toString().trim();
                        valoreInserito = valoreInserito.replace(" ", "_");

                        ChiamateWSMI c = new ChiamateWSMI(context);
                        c.CreaNuovaCategoria(valoreInserito, "OCR");
                    }
                });

                // Bottone Annulla
                builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // Chiude il dialog
                    }
                });

                // Mostra il dialog
                builder.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return true;
        }

        return false;
    }
}
