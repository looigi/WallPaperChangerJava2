package com.looigi.wallpaperchanger2.classeVideo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;

public class MainMostraVideo extends Activity {
    private static String NomeMaschera = "Main_Mostra_Video";
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        context = this;
        act = this;

        VariabiliStaticheVideo.getInstance().setContext(context);
        VariabiliStaticheVideo.getInstance().setAct(act);

        VariabiliStaticheVideo.getInstance().setVideoView(findViewById(R.id.videoView));
        VariabiliStaticheVideo.getInstance().setPbLoading(findViewById(R.id.pbVideoLoading));
        VariabiliStaticheVideo.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloVideo));
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
        VariabiliStaticheVideo.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        EditText txtFiltro = findViewById(R.id.edtFiltroVideo);
        ImageView imgCerca = findViewById(R.id.imgCerca);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filtro = txtFiltro.getText().toString();
                VariabiliStaticheVideo.getInstance().setFiltro(filtro);

                ChiamateWSV ws = new ChiamateWSV(context);
                ws.RitornaProssimoVideo();
            }
        });

        final boolean[] primoIngresso = {true};
        VariabiliStaticheVideo.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    VariabiliStaticheVideo.getInstance().setCategoria("");
                } else {
                    VariabiliStaticheVideo.getInstance().setCategoria(Categoria);
                }

                // ChiamateWSV ws = new ChiamateWSV(context);
                // ws.RitornaProssimoVideo();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        db_dati_video db = new db_dati_video(context);
        String url = db.CaricaVideo();

        if (!url.isEmpty()) {
            String[] u = url.split("/");
            String res = u[u.length - 1];
            res = VariabiliStaticheVideo.getInstance().getIdUltimoVideo() + ": " + res;
            VariabiliStaticheVideo.getInstance().getTxtTitolo().setText(res);
        }

        ChiamateWSV ws = new ChiamateWSV(context);
        ws.RitornaCategorie();

        if (!url.isEmpty()) {
            UtilityVideo.getInstance().ImpostaVideo();
        } else {
            ChiamateWSV ws2 = new ChiamateWSV(context);
            ws2.RitornaProssimoVideo();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }
}
