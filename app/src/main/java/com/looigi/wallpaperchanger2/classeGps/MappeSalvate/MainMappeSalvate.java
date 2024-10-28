package com.looigi.wallpaperchanger2.classeGps.MappeSalvate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainMappeSalvate extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappe_salvate);

        this.context = this;
        this.act = this;

        VariabiliStaticheGPS.getInstance().setImgMappa(findViewById(R.id.imgMappaSalvata));

        List<String> listaMappe = prendeMappe();
        ListView lstMappe = act.findViewById(R.id.lstMappe);
        AdapterListenerMappe customAdapterT = new AdapterListenerMappe(context, listaMappe);
        lstMappe.setAdapter(customAdapterT);
    }

    private List<String> prendeMappe() {
        List<String> lista = new ArrayList<>();

        String Path = context.getFilesDir() + "/Mappe/";
        File folder = new File(Path);
        File[] filesInFolder = folder.listFiles();
        assert filesInFolder != null;
        for (File file : filesInFolder) {
            if (!file.isDirectory()) {
                lista.add(file.getName());
            }
        }

        return lista;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VariabiliStaticheGPS.getInstance().setImgMappa(null);
        this.finish();
    }
}
