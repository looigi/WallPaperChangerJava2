package com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.webService.ChiamateWSLazioDettaglio;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.classePlayer.Files;

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
            d.EsegueChiamata(context, imgCasa, url, NomeSquadraCasa + ".Jpg");
        }

        ImageView imgFuori = findViewById(R.id.imgFuori);
        String NomeSquadraFuori = Fuori.toUpperCase().trim();
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadraFuori)) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadraFuori);
            imgFuori.setImageBitmap(bmp);
        } else {
            String url = VariabiliStaticheLazioDettaglio.UrlMedia + NomeSquadraFuori + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgFuori, url, NomeSquadraFuori + ".Jpg");
        }

        ImageView imgSalvaDettaglio = findViewById(R.id.imgSalvaDettaglio);
        imgSalvaDettaglio.setOnClickListener(new View.OnClickListener() {
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
