package com.looigi.wallpaperchanger2.classeScaricaImmagini;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;

import java.util.List;

public class MainScaricaImmagini extends Activity {
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scarica_immagini);

        context = this;
        Activity act = this;

        Intent intent = getIntent();
        String Modalita = intent.getStringExtra("MODALITA");
        String Filtro = intent.getStringExtra("FILTRO");

        VariabiliStatichePlayer.getInstance().setLayCaricamentoSI(findViewById(R.id.layCaricamentoInCorsoSI));
        VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.GONE);

        List<String> listaImmagini = VariabiliStatichePlayer.getInstance().getUrlImmaginiDaScaricare();
        ListView lstImms = act.findViewById(R.id.lstImmagini);
        AdapterListenerImmaginiDaScaricare customAdapterT = new AdapterListenerImmaginiDaScaricare(
                context,
                Modalita,
                Filtro,
                listaImmagini);
        lstImms.setAdapter(customAdapterT);

        UtilityPlayer.getInstance().AttesaSI(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
