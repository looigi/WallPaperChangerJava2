package com.looigi.wallpaperchanger2.classePreview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.DownloadImmagineMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.MainImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classePreview.webService.DownloadImmaginePreview;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.UtilityUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.VariabiliStaticheUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.db_dati_ui;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class MainPreview extends Activity {
    private Context context;
    private Activity act;

    // pinimg;romantik;wheel;tata;nylon;fantasy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);

        context = this;
        act = this;

        Intent intent = getIntent();
        String Modalita = intent.getStringExtra("Modalita");
        VariabiliStatichePreview.getInstance().setModalita(Modalita);

        VariabiliStatichePreview.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoPreview));
        VariabiliStatichePreview.getInstance().Attesa(false);

        VariabiliStatichePreview.getInstance().setImgPreview(findViewById(R.id.imgPreview));

        LinearLayout layProssima = findViewById(R.id.layProssima);

        switch (Modalita) {
            case "Utility":
                layProssima.setVisibility(LinearLayout.VISIBLE);

                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.RitornaProssimaImmagine(
                        VariabiliStatichePreview.getInstance().getIdCategoria()
                );
                break;
            default:
                layProssima.setVisibility(LinearLayout.GONE);

                if (VariabiliStatichePreview.getInstance().getStrutturaImmagine() == null) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna struttura immagine impostata");
                    this.finish();
                    return;
                }

                DownloadImmaginePreview d = new DownloadImmaginePreview();
                d.EsegueChiamata(
                        context,
                        VariabiliStatichePreview.getInstance().getStrutturaImmagine().getNomeFile(),
                        VariabiliStatichePreview.getInstance().getImgPreview(),
                        VariabiliStatichePreview.getInstance().getStrutturaImmagine().getUrlImmagine()
                );
                break;
        }

        ImageView imgProssima = findViewById(R.id.imgProssima);
        imgProssima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.RitornaProssimaImmagine(
                        VariabiliStatichePreview.getInstance().getIdCategoria()
                );
            }
        });

        ImageView imgGestioneVolti = findViewById(R.id.imgGestioneVolti);
        imgGestioneVolti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.ControllaVolto(
                        String.valueOf(VariabiliStatichePreview.getInstance().getStrutturaImmagine().getIdImmagine())
                );
            }
        });

        ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePreview.getInstance().getImgPreview().setImageBitmap(null);
                act.finish();
            }
        });

        VariabiliStatichePreview.getInstance().setLayVolti(findViewById(R.id.layVoltiRilevati));
        VariabiliStatichePreview.getInstance().getLayVolti().setVisibility(LinearLayout.GONE);
        VariabiliStatichePreview.getInstance().setLstVolti(findViewById(R.id.lstListaVolti));

        ImageView imgChiudeListaVolti = findViewById(R.id.imgChiudeListaVolti);
        imgChiudeListaVolti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePreview.getInstance().getLayVolti().setVisibility(LinearLayout.GONE);
            }
        });

        // SPOSTAMENTO
        VariabiliStatichePreview.getInstance().setIdCategoriaDiSpostamento("");

        LinearLayout laySposta = findViewById(R.id.laySposta);
        laySposta.setVisibility(LinearLayout.GONE);

        ImageView imgSposta = findViewById(R.id.imgSpostaACategoria);
        imgSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySposta.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgSpostaImmagine = findViewById(R.id.imgSpostaImmagine);
        imgSpostaImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePreview.getInstance().getIdCategoriaDiSpostamento().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Selezionare una categoria");
                    return;
                }

                StrutturaImmaginiLibrary s = VariabiliStatichePreview.getInstance().getStrutturaImmagine();

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "PREVIEW");

                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaSposta = findViewById(R.id.imgAnnullaSposta);
        imgAnnullaSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        EditText edtFiltroSpostamento = findViewById(R.id.edtSpostaFiltroCategoria);
        edtFiltroSpostamento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliStatichePreview.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                VariabiliStatichePreview.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        VariabiliStatichePreview.getInstance().setSpnSpostaCategorie(findViewById(R.id.spnSpostaCategorie));
        final boolean[] primoIngresso = {true};

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaCategorie(false, "PREVIEW");

        VariabiliStatichePreview.getInstance().getSpnSpostaCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();

                VariabiliStatichePreview.getInstance().setIdCategoriaDiSpostamento("");
                for (StrutturaImmaginiCategorie c: VariabiliStatichePreview.getInstance().getListaCategorie()) {
                    if (c.getCategoria().equals(Categoria)) {
                        VariabiliStatichePreview.getInstance().setIdCategoriaDiSpostamento(String.valueOf(c.getIdCategoria()));
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
        // SPOSTAMENTO
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
