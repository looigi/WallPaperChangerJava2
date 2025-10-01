package com.looigi.wallpaperchanger2.Lazio.DettaglioPartita;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Ammoniti;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Espulsi;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Formazione;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Marcatori;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.webService.ChiamateWSLazioDettaglio;
import com.looigi.wallpaperchanger2.Lazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;

import java.util.List;

public class MainDettaglioPartita extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lazio_dettaglio);

        context = this;
        act = this;

        VariabiliStaticheLazioDettaglio.getInstance().setPathLazio(context.getFilesDir() + "/Lazio");
        VariabiliStaticheLazioDettaglio.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoLazio));

        UtilityLazioDettaglio.getInstance().ImpostaAttesa(false);

        String Casa = VariabiliStaticheLazioDettaglio.getInstance().getCasa();
        String Fuori = VariabiliStaticheLazioDettaglio.getInstance().getFuori();
        String Risultato = VariabiliStaticheLazioDettaglio.getInstance().getRisultato();
        String Data = VariabiliStaticheLazioDettaglio.getInstance().getData();

        TextView txtCasa = findViewById(R.id.txtCasa);
        TextView txtFuori = findViewById(R.id.txtFuori);
        EditText edtRisultato = findViewById(R.id.edtRisultato);
        EditText edtData = findViewById(R.id.edtData);

        txtCasa.setText(Casa);
        txtFuori.setText(Fuori);
        edtRisultato.setText(Risultato);
        edtData.setText(Data);

        ImageView imgCasa = findViewById(R.id.imgCasa);
        String NomeSquadraCasa = Casa.toUpperCase().trim();
        String PathImmagini = VariabiliStaticheLazioDettaglio.getInstance().getPathLazio() + "/Stemmi";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadraCasa)) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadraCasa);
            imgCasa.setImageBitmap(bmp);
        } else {
            String url = VariabiliStaticheLazioDettaglio.UrlMedia + NomeSquadraCasa + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgCasa, url, NomeSquadraCasa + ".Jpg", "Stemmi");
        }

        ImageView imgFuori = findViewById(R.id.imgFuori);
        String NomeSquadraFuori = Fuori.toUpperCase().trim();
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadraFuori)) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadraFuori);
            imgFuori.setImageBitmap(bmp);
        } else {
            String url = VariabiliStaticheLazioDettaglio.UrlMedia + NomeSquadraFuori + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgFuori, url, NomeSquadraFuori + ".Jpg", "Stemmi");
        }

        VariabiliStaticheLazioDettaglio.getInstance().setLstAC(findViewById(R.id.lstAC));
        VariabiliStaticheLazioDettaglio.getInstance().setLstAF(findViewById(R.id.lstAF));
        VariabiliStaticheLazioDettaglio.getInstance().setLstEC(findViewById(R.id.lstEC));
        VariabiliStaticheLazioDettaglio.getInstance().setLstEF(findViewById(R.id.lstEF));
        VariabiliStaticheLazioDettaglio.getInstance().setLstMC(findViewById(R.id.lstMC));
        VariabiliStaticheLazioDettaglio.getInstance().setLstMF(findViewById(R.id.lstMF));
        VariabiliStaticheLazioDettaglio.getInstance().setLstFC(findViewById(R.id.lstFC));
        VariabiliStaticheLazioDettaglio.getInstance().setLstFF(findViewById(R.id.lstFF));
        VariabiliStaticheLazioDettaglio.getInstance().setEdtArbitro(findViewById(R.id.edtArbitro));
        VariabiliStaticheLazioDettaglio.getInstance().setEdtLocalita(findViewById(R.id.edtLocalita));
        VariabiliStaticheLazioDettaglio.getInstance().setEdtSpettatori(findViewById(R.id.edtSpettatori));
        VariabiliStaticheLazioDettaglio.getInstance().setEdtNote(findViewById(R.id.edtNote));

        ImageView imgSalvaDettaglio = findViewById(R.id.imgSalvaDettaglio);
        imgSalvaDettaglio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Risultato = edtRisultato.getText().toString();
                String Arbitro = VariabiliStaticheLazioDettaglio.getInstance().getEdtArbitro().getText().toString();
                String Localita = VariabiliStaticheLazioDettaglio.getInstance().getEdtLocalita().getText().toString();
                String Spettatori = VariabiliStaticheLazioDettaglio.getInstance().getEdtSpettatori().getText().toString();
                String Note =
                        Arbitro + ";" +
                        Localita + ";" +
                        Spettatori + ";" +
                        VariabiliStaticheLazioDettaglio.getInstance().getEdtNote().getText().toString().replace(";", "***PV***");
                List<Ammoniti> listaAc = VariabiliStaticheLazioDettaglio.getInstance().getDettaglioPartita().getAmmonitiCasa();
                String AmmonitiCasa = "";
                for (Ammoniti lista : listaAc) {
                    AmmonitiCasa += ";;§";
                }
                List<Ammoniti> listaAf = VariabiliStaticheLazioDettaglio.getInstance().getDettaglioPartita().getAmmonitiFuori();
                String AmmonitiFuori = "";
                for (Ammoniti lista : listaAf) {
                    AmmonitiFuori += ";;§";
                }
                List<Espulsi> listaEc = VariabiliStaticheLazioDettaglio.getInstance().getDettaglioPartita().getEspulsiCasa();
                String EspulsiCasa = "";
                for (Espulsi lista : listaEc) {
                    EspulsiCasa += ";;§";
                }
                List<Espulsi> listaEf = VariabiliStaticheLazioDettaglio.getInstance().getDettaglioPartita().getEspulsiFuori();
                String EspulsiFuori = "";
                for (Espulsi lista : listaEf) {
                    EspulsiFuori += ";;§";
                }
                List<Formazione> listaFc = VariabiliStaticheLazioDettaglio.getInstance().getDettaglioPartita().getFormazioneCasa();
                String FormazioneCasa = "";
                for (Formazione lista : listaFc) {
                    FormazioneCasa += ";;§";
                }
                List<Formazione> listaFf = VariabiliStaticheLazioDettaglio.getInstance().getDettaglioPartita().getFormazioneFuori();
                String FormazioneFuori = "";
                for (Formazione lista : listaFf) {
                    FormazioneFuori += ";;§";
                }
                List<Marcatori> listaMc = VariabiliStaticheLazioDettaglio.getInstance().getDettaglioPartita().getMarcatoriCasa();
                String MarcatoriCasa = "";
                for (Marcatori lista : listaMc) {
                    MarcatoriCasa += ";;§";
                }
                List<Marcatori> listaMf = VariabiliStaticheLazioDettaglio.getInstance().getDettaglioPartita().getMarcatoriFuori();
                String MarcatoriFuori = "";
                for (Marcatori lista : listaMf) {
                    MarcatoriFuori += ";;§";
                }

                ChiamateWSLazioDettaglio ws = new ChiamateWSLazioDettaglio(context);
                ws.SalvaDettaglio(
                    Integer.toString(VariabiliStaticheLazioDettaglio.getInstance().getIdPartita()),
                    Integer.toString(VariabiliStaticheLazioDettaglio.getInstance().getIdSquadraCasa()),
                    Integer.toString(VariabiliStaticheLazioDettaglio.getInstance().getIdSquadraFuori()),
                    Risultato,
                    Arbitro,
                    Localita,
                    Spettatori,
                    MarcatoriCasa,
                    MarcatoriFuori,
                    AmmonitiCasa,
                    AmmonitiFuori,
                    EspulsiCasa,
                    EspulsiFuori,
                    FormazioneCasa,
                    FormazioneFuori,
                    Note
                );
            }
        });

        ImageView imgAggAC = findViewById(R.id.imgAggAC);
        imgAggAC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgAggAF = findViewById(R.id.imgAggAF);
        imgAggAF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgAggEC = findViewById(R.id.imgAggEC);
        imgAggEC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgAggEF = findViewById(R.id.imgAggEF);
        imgAggEF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgAggFC = findViewById(R.id.imgAggFC);
        imgAggFC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgAggFF = findViewById(R.id.imgAggFF);
        imgAggFF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgAggMC = findViewById(R.id.imgAggMC);
        imgAggMC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgAggMF = findViewById(R.id.imgAggMF);
        imgAggMF.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ChiamateWSLazioDettaglio wsD = new ChiamateWSLazioDettaglio(context);
        wsD.RitornaDettaglio(
                Integer.toString(VariabiliStaticheLazioDettaglio.getInstance().getIdPartita()),
                Integer.toString(VariabiliStaticheLazioDettaglio.getInstance().getIdSquadraCasa()),
                Integer.toString(VariabiliStaticheLazioDettaglio.getInstance().getIdSquadraFuori())
        );

        ChiamateWSLazioDettaglio wsGC = new ChiamateWSLazioDettaglio(context);
        wsGC.RitornaGiocatori(Integer.toString(VariabiliStaticheLazioDettaglio.getInstance().getIdSquadraCasa()), true);

        ChiamateWSLazioDettaglio wsGF = new ChiamateWSLazioDettaglio(context);
        wsGF.RitornaGiocatori(Integer.toString(VariabiliStaticheLazioDettaglio.getInstance().getIdSquadraFuori()), false);
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
