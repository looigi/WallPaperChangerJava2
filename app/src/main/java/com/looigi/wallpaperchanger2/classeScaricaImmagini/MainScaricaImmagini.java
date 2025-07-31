package com.looigi.wallpaperchanger2.classeScaricaImmagini;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.adapters.AdapterListenerImmaginiDaScaricare;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class MainScaricaImmagini extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scarica_immagini);

        context = this;
        act = this;

        VariabiliScaricaImmagini.getInstance().setMascheraAttiva(true);

        Intent intent = getIntent();
        String Modalita = intent.getStringExtra("MODALITA");
        String Filtro = intent.getStringExtra("FILTRO");

        VariabiliScaricaImmagini.getInstance().setModalita(Modalita);
        VariabiliScaricaImmagini.getInstance().setFiltro(Filtro);

        VariabiliStatichePlayer.getInstance().setLayCaricamentoSI(findViewById(R.id.layCaricamentoInCorsoSI));
        VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.GONE);

        List<String> listaImmagini = VariabiliStatichePlayer.getInstance().getUrlImmaginiDaScaricare();

        VariabiliScaricaImmagini.getInstance().setTxtSelezionate(findViewById(R.id.txtSelezionate));
        VariabiliScaricaImmagini.getInstance().setImgScaricaTutte(findViewById(R.id.imgSalvaTutte));
        VariabiliScaricaImmagini.getInstance().getImgScaricaTutte().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliScaricaImmagini.getInstance().getListaDaScaricare().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna immagine selezionata");
                } else {
                    VariabiliScaricaImmagini.getInstance().setScaricaMultiplo(true);

                    List<String> listaFatte = new ArrayList<>();
                    for (String s: VariabiliStatichePlayer.getInstance().getUrlImmaginiDaScaricare()) {
                        listaFatte.add("");
                    }
                    VariabiliScaricaImmagini.getInstance().setListaOriginaleDaScaricare(
                            listaFatte
                    );

                    VariabiliScaricaImmagini.getInstance().setQualeImmagineStoScaricando(0);
                    StrutturaImmagineDaScaricare s = VariabiliScaricaImmagini.getInstance().getListaDaScaricare().get(
                            VariabiliScaricaImmagini.getInstance().getQualeImmagineStoScaricando()
                    );
                    VariabiliScaricaImmagini.getInstance().setImgScaricaDaDisabilitare(s.getImgImmagine());
                    VariabiliScaricaImmagini.getInstance().setChkSelezione(s.getChkSelezione());

                    VariabiliScaricaImmagini.getInstance().setScaricataBene(false);
                    DownloadImmagineSI d = new DownloadImmagineSI();
                    d.EsegueDownload(context, s.getImgImmagine(), s.getUrlImmagine(), Modalita,
                            Filtro, true, "SCARICA", 0, null);
                }
            }
        });

        VariabiliScaricaImmagini.getInstance().getTxtSelezionate().setVisibility(LinearLayout.GONE);
        VariabiliScaricaImmagini.getInstance().getImgScaricaTutte().setVisibility(LinearLayout.GONE);

        VariabiliScaricaImmagini.getInstance().setLstImmagini(act.findViewById(R.id.lstImmagini));
        AdapterListenerImmaginiDaScaricare customAdapterT = new AdapterListenerImmaginiDaScaricare(
                context,
                Modalita,
                Filtro,
                listaImmagini);
        VariabiliScaricaImmagini.getInstance().getLstImmagini().setAdapter(customAdapterT);

        VariabiliScaricaImmagini.getInstance().setLayPreview(findViewById(R.id.layPreview));
        VariabiliScaricaImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);

        VariabiliScaricaImmagini.getInstance().setImgPreview(findViewById(R.id.imgPreview));
        ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliScaricaImmagini.getInstance().getImgPreview().setImageBitmap(null);
                VariabiliScaricaImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);
            }
        });

        UtilityPlayer.getInstance().AttesaSI(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VariabiliScaricaImmagini.getInstance().setMascheraAttiva(false);
        act.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliScaricaImmagini.getInstance().setMascheraAttiva(false);
                this.finish();

                return false;
        }

        return false;
    }
}
