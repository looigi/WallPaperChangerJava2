package com.looigi.wallpaperchanger2.classeFilms;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;

public class MainMostraFilms extends Activity {
    private static String NomeMaschera = "Main_Mostra_Films";
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films);

        context = this;
        act = this;

        VariabiliStaticheFilms.getInstance().setContext(context);
        VariabiliStaticheFilms.getInstance().setAct(act);

        VariabiliStaticheFilms.getInstance().setFilmsView(findViewById(R.id.videoView));
        VariabiliStaticheFilms.getInstance().setPbLoading(findViewById(R.id.pbVideoLoading));
        VariabiliStaticheFilms.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloFilm));
        VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);
        VariabiliStaticheFilms.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        EditText txtFiltro = findViewById(R.id.edtFiltroFilms);
        ImageView imgCerca = findViewById(R.id.imgCerca);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filtro = txtFiltro.getText().toString();
                VariabiliStaticheFilms.getInstance().setFiltro(filtro);

                ChiamateWSF ws = new ChiamateWSF(context);
                ws.RitornaProssimoFilms();
            }
        });

        final boolean[] primoIngresso = {true};
        VariabiliStaticheFilms.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    VariabiliStaticheFilms.getInstance().setCategoria("");
                } else {
                    VariabiliStaticheFilms.getInstance().setCategoria(Categoria);
                }

                // ChiamateWSV ws = new ChiamateWSV(context);
                // ws.RitornaProssimoFilms();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        db_dati_films db = new db_dati_films(context);
        String url = db.CaricaFilms();

        if (!url.isEmpty()) {
            String[] u = url.split("/");
            String res = u[u.length - 1];
            res = VariabiliStaticheFilms.getInstance().getIdUltimoFilms() + ": " + res;
            VariabiliStaticheFilms.getInstance().getTxtTitolo().setText(res);
        }

        ChiamateWSF ws = new ChiamateWSF(context);
        ws.RitornaCategorie();

        if (!url.isEmpty()) {
            UtilityFilms.getInstance().ImpostaFilms();
        } else {
            ChiamateWSF ws2 = new ChiamateWSF(context);
            ws2.RitornaProssimoFilms();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }
}
