package com.looigi.wallpaperchanger2.classeMostraImmagini;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiWallpaper.WebServices.ChiamateWs;
import com.looigi.wallpaperchanger2.classeMostraImmagini.webservice.DownloadImage;

import org.json.JSONException;
import org.json.JSONObject;

public class MostraImmaginiLibrary extends Activity {
    private Context context;
    private Activity act;

    public MostraImmaginiLibrary() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostra_immagini);

        context = this;
        act = this;

        VariabiliStaticheMostraImmagini.getInstance().setAct(act);

        VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(-999);
        VariabiliStaticheMostraImmagini.getInstance().setFiltro("");
        VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(1);
        VariabiliStaticheMostraImmagini.getInstance().setRandom("S");

        ChiamateWs ws = new ChiamateWs(context);
        ws.RitornaCategorie();

        VariabiliStaticheMostraImmagini.getInstance().setTxtInfo(findViewById(R.id.txtInfoImmagine));

        VariabiliStaticheMostraImmagini.getInstance().setImg(findViewById(R.id.imgLibrary));
        // ImageView imgIndietro = findViewById(R.id.imgIndietroLibrary);
        // ImageView imgAvanti = findViewById(R.id.imgAvantiLibrary);

        VariabiliStaticheMostraImmagini.getInstance().getImg().setOnTouchListener(new OnSwipeTouchListener(MostraImmaginiLibrary.this) {
            public void onSwipeTop() {
                UtilityWallpaper.getInstance().ApreToast(context, "Swipe Top");
            }
            public void onSwipeRight() {
                // UtilityWallpaper.getInstance().ApreToast(context, "Swipe Right");
                UtilityImmagini.getInstance().TornaIndietro(context);
            }
            public void onSwipeLeft() {
                // UtilityWallpaper.getInstance().ApreToast(context, "Swipe Left");
                UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
            }
            public void onSwipeBottom() {
                UtilityWallpaper.getInstance().ApreToast(context, "Swipe Bottom");
            }
        });

        EditText edtFiltro = findViewById(R.id.edtFiltroImmagimi);
        edtFiltro.setText(VariabiliStaticheMostraImmagini.getInstance().getFiltro());
        edtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    VariabiliStaticheMostraImmagini.getInstance().setFiltro(edtFiltro.getText().toString());

                    UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
                }
            }
        });

        boolean letto = false;
        String path1 = context.getFilesDir() + "/Immagini";
        UtilityWallpaper.getInstance().CreaCartelle(path1);
        String NomeFile = "/UltimaImmagine.txt";
        if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
            String u = UtilityDetector.getInstance().LeggeFileDiTesto(path1 + NomeFile);
            if (u != null) {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(u);
                    StrutturaImmaginiLibrary si = UtilityImmagini.getInstance().prendeStruttura(jObject);

                    VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(si.getIdCategoria());
                    VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(si.getIdImmagine());

                    VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(si);

                    VariabiliStaticheMostraImmagini.getInstance().ScriveInfoImmagine(si);

                    VariabiliStaticheMostraImmagini.getInstance().AggiungeCaricata();

                    new DownloadImage(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(si.getUrlImmagine());

                    letto = true;
                } catch (JSONException ignored) {
                }
            }
        }

        final boolean[] primoIngresso = {true};
        VariabiliStaticheMostraImmagini.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        VariabiliStaticheMostraImmagini.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
                    if (s.getCategoria().equals(Categoria)) {
                        VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(s.getIdCategoria());
                        UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
                        break;
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        if (!letto) {
            UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
        }

        /* imgAvanti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RitornaProssimaImmagine(ws);
            }
        }); */
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        act.finish();

        super.onKeyDown(keyCode, event);

        /* Utility.getInstance().ScriveLog(this, NomeMaschera,
                "Tasto premuto: " + Integer.toString(keyCode)); */

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheMostraImmagini.getInstance().getAct().finish();

                return false;
        }

        return false;
    }
}
