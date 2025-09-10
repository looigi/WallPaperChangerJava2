package com.looigi.wallpaperchanger2.classePreview.classeOCR;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classePreview.classeOCR.webService.ChiamateWSOCR;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class MainOCR extends Activity {
    private Context context;
    private Activity act = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_ocr);

        context = this;

        VariabiliStaticheOCR.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoOCR));
        UtilitiesOCR.getInstance().Attesa(false);

        VariabiliStaticheOCR.getInstance().setLstDestinazioni(findViewById(R.id.lstDestinazioni));
        VariabiliStaticheOCR.getInstance().setLstImmagini(findViewById(R.id.lstImmagini));

        ChiamateWSOCR ws = new ChiamateWSOCR(context);
        ws.RitornaDestinazioni();

        ChiamateWSMI ws2 = new ChiamateWSMI(context);
        ws2.RitornaCategorie(false, "OCR");

        SwitchCompat swcAncheVuote = findViewById(R.id.swtAncheVuote);
        swcAncheVuote.setChecked(VariabiliStaticheOCR.getInstance().isAncheDestinazioniVuote());
        swcAncheVuote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliStaticheOCR.getInstance().setAncheDestinazioniVuote(swcAncheVuote.isChecked());
                ws.RitornaDestinazioni();
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
}
