package com.looigi.wallpaperchanger2.classePazzia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classePennetta.webservice.ChiamateWSPEN;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;

public class MainPazzia extends Activity {
    private Context context;
    private Activity act;

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

        VariabiliStatichePazzia.getInstance().setSlideShowAttivoIMM(true);
        VariabiliStatichePazzia.getInstance().setSlideShowAttivoPEN(true);

        db_dati_pazzia db = new db_dati_pazzia(context);
        db.CreazioneTabelle();
        db.LeggeImpostazioni();

        EditText edtFiltro = findViewById(R.id.edtFiltro);

        RelativeLayout laySettings = findViewById(R.id.laySettings);
        laySettings.setVisibility(LinearLayout.GONE);

        ImageView imgPlayPausePEN = findViewById(R.id.imgPlayPausePEN);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_pausa);
        imgPlayPausePEN.setImageBitmap(bitmap);
        imgPlayPausePEN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePazzia.getInstance().isSlideShowAttivoPEN()) {
                    UtilityPazzia.getInstance().bloccaTimerPEN();
                    VariabiliStatichePazzia.getInstance().setSlideShowAttivoPEN(false);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_suona);
                    imgPlayPausePEN.setImageBitmap(bitmap);
                } else {
                    UtilityPazzia.getInstance().CambiaImmaginePennetta(context);
                    VariabiliStatichePazzia.getInstance().setSlideShowAttivoPEN(true);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_pausa);
                    imgPlayPausePEN.setImageBitmap(bitmap);
                }
            }
        });

        ImageView imgPlayPauseIMM = findViewById(R.id.imgPlayPauseIMM);
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_pausa);
        imgPlayPauseIMM.setImageBitmap(bitmap2);
        imgPlayPauseIMM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePazzia.getInstance().isSlideShowAttivoIMM()) {
                    UtilityPazzia.getInstance().bloccaTimerIMM();
                    VariabiliStatichePazzia.getInstance().setSlideShowAttivoIMM(false);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_suona);
                    imgPlayPauseIMM.setImageBitmap(bitmap);
                } else {
                    UtilityPazzia.getInstance().CambiaImmagineImmagine(context);
                    VariabiliStatichePazzia.getInstance().setSlideShowAttivoIMM(true);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_pausa);
                    imgPlayPauseIMM.setImageBitmap(bitmap);
                }
            }
        });

        VariabiliStatichePazzia.getInstance().setSpnCategoria(findViewById(R.id.spnCategorie));

        TextView txtSettingsTitolo = findViewById(R.id.txtSettings);
        txtSettingsTitolo.setText("");

        ImageView imgChiudeSettings = findViewById(R.id.imgChiudeSettings);
        imgChiudeSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySettings.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgNextPEN = findViewById(R.id.imgNextPEN);
        imgNextPEN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityPazzia.getInstance().CambiaImmaginePennetta(context);
            }
        });

        ImageView imgNextIMM = findViewById(R.id.imgNextIMM);
        imgNextIMM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityPazzia.getInstance().CambiaImmagineImmagine(context);
            }
        });

        ChiamateWSPEN c1 = new ChiamateWSPEN(context);
        c1.RitornaCategorie(false, "PAZZIA");

        ChiamateWSMI c2 = new ChiamateWSMI(context);
        c2.RitornaCategorie(false, "PAZZIA");

        ChiamateWSV c3 = new ChiamateWSV(context);
        c3.RitornaCategorie(false, "PAZZIA");

        ImageView imgSettingsPEN = findViewById(R.id.imgSettingsPEN);
        imgSettingsPEN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtSettingsTitolo.setText("Settings Pennetta");
                VariabiliStatichePazzia.getInstance().setModalita("PENNETTA");
                edtFiltro.setText(VariabiliStatichePazzia.getInstance().getFiltroPEN());
                laySettings.setVisibility(LinearLayout.VISIBLE);

                String[] l2 = new String[VariabiliStatichePazzia.getInstance().getListaCategoriePEN().size()];
                int c = 0;
                for (StrutturaImmaginiCategorie s : VariabiliStatichePazzia.getInstance().getListaCategoriePEN()) {
                    l2[c] = s.getCategoria();
                    c++;
                }

                UtilityPazzia.getInstance().AggiornaCategorie(
                        context,
                        VariabiliStatichePazzia.getInstance().getSpnCategoria(),
                        l2,
                        "PENNETTA"
                );
            }
        });

        ImageView imgSettingsIMM = findViewById(R.id.imgSettingsIMM);
        imgSettingsIMM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtSettingsTitolo.setText("Settings Immagini");
                VariabiliStatichePazzia.getInstance().setModalita("IMMAGINI");
                laySettings.setVisibility(LinearLayout.VISIBLE);
                edtFiltro.setText(VariabiliStatichePazzia.getInstance().getFiltroIMM());

                String[] l2 = new String[VariabiliStatichePazzia.getInstance().getListaCategorieIMM().size()];
                int c = 0;
                for (com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie s : VariabiliStatichePazzia.getInstance().getListaCategorieIMM()) {
                    l2[c] = s.getCategoria();
                    c++;
                }

                UtilityPazzia.getInstance().AggiornaCategorie(
                        context,
                        VariabiliStatichePazzia.getInstance().getSpnCategoria(),
                        l2,
                        "IMMAGINI"
                );
            }
        });

        ImageView imgSettingsVID = findViewById(R.id.imgSettingsVID);
        imgSettingsVID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtSettingsTitolo.setText("Settings Video");
                laySettings.setVisibility(LinearLayout.VISIBLE);
                VariabiliStatichePazzia.getInstance().setModalita("VIDEO");
                edtFiltro.setText(VariabiliStatichePazzia.getInstance().getFiltroVID());

                String[] l2 = new String[VariabiliStatichePazzia.getInstance().getListaCategorieVID().size()];
                int c = 0;
                for (String s : VariabiliStatichePazzia.getInstance().getListaCategorieVID()) {
                    l2[c] = s;
                    c++;
                }

                UtilityPazzia.getInstance().AggiornaCategorie(
                        context,
                        VariabiliStatichePazzia.getInstance().getSpnCategoria(),
                        l2,
                        "VIDEO"
                );
            }
        });

        ImageView imgImpostaFiltro = findViewById(R.id.imgImpostaFiltro);
        imgImpostaFiltro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (VariabiliStatichePazzia.getInstance().getModalita()) {
                    case "PENNETTA":
                        VariabiliStatichePazzia.getInstance().setFiltroPEN(edtFiltro.getText().toString());
                        break;
                    case "IMMAGINI":
                        VariabiliStatichePazzia.getInstance().setFiltroIMM(edtFiltro.getText().toString());
                        break;
                    case "VIDEO":
                        VariabiliStatichePazzia.getInstance().setFiltroVID(edtFiltro.getText().toString());
                        break;
                }

                db_dati_pazzia db = new db_dati_pazzia(context);
                db.SalvaImpostazioni();
            }
        });

        if (VariabiliStatichePazzia.getInstance().isGiaPartito()) {

        } else {
            VariabiliStatichePazzia.getInstance().setGiaPartito(true);

            UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                    VariabiliStatichePazzia.getInstance().getImgCaricamentoPEN(),
                    false
            );
            UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                    VariabiliStatichePazzia.getInstance().getImgCaricamentoIMM(),
                    false
            );
            UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                    VariabiliStatichePazzia.getInstance().getImgCaricamentoVID(),
                    false
            );

            UtilityPazzia.getInstance().CambiaImmaginePennetta(context);
            UtilityPazzia.getInstance().CambiaImmagineImmagine(context);
            UtilityPazzia.getInstance().CambiaVideo(context);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilityPazzia.getInstance().bloccaTimerIMM();
        UtilityPazzia.getInstance().bloccaTimerPEN();

        if (VariabiliStatichePazzia.getInstance().getVideoView() != null) {
            VariabiliStatichePazzia.getInstance().getVideoView().stopPlayback();
            VariabiliStatichePazzia.getInstance().getVideoView().setMediaController(null);
            VariabiliStatichePazzia.getInstance().getVideoView().setVideoURI(null);
        }

        VariabiliStatichePazzia.getInstance().setGiaPartito(false);

        act.finish();
    }
}
