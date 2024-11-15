package com.looigi.wallpaperchanger2.classePlayer.scaricaImmagini;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeMappeSalvate.AdapterListenerMappe;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;

import java.util.List;

public class scaricaImmagini extends Activity {
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scarica_immagini);

        context = this;
        Activity act = this;

        Intent intent = getIntent();
        String Artista = intent.getStringExtra("ARTISTA");

        VariabiliStatichePlayer.getInstance().setLayCaricamentoSI(findViewById(R.id.layCaricamentoInCorsoSI));
        UtilityPlayer.getInstance().AttesaSI(false);

        List<String> listaImmagini = VariabiliStatichePlayer.getInstance().getUrlImmaginiDaScaricare();
        ListView lstImms = act.findViewById(R.id.lstImmagini);
        AdapterListenerImmaginiDaScaricare customAdapterT = new AdapterListenerImmaginiDaScaricare(
                context,
                Artista,
                listaImmagini);
        lstImms.setAdapter(customAdapterT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
