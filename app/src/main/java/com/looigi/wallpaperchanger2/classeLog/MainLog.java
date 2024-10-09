package com.looigi.wallpaperchanger2.classeLog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeMostraImmagini.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeMostraImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeMostraImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeMostraVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiPlayer.Files;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainLog extends Activity {
    private static String NomeMaschera = "MAINLOG";
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_log);

        context = this;
        act = this;

        TextView txtTesto = findViewById(R.id.txtTestoLog);
        txtTesto.setSingleLine(false);
        txtTesto.setMovementMethod(new ScrollingMovementMethod());

        Spinner spnFile = (findViewById(R.id.spnFile));
        final boolean[] primoIngresso = {true};
        spnFile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Filetto = adapterView.getItemAtPosition(position).toString();
                for (File f : VariabiliStaticheLog.getInstance().getListaFiles()) {
                    String nome = f.getName();
                    if (nome.equals(Filetto)) {
                        String path = f.getAbsolutePath();
                        String Cosa = LeggeFileLineaPerLinea(path);
                        txtTesto.setText(Cosa);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        List<String> l = new ArrayList<>();
        for (File f : VariabiliStaticheLog.getInstance().getListaFiles()) {
            String nome = f.getName();
            l.add(nome);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFile.setAdapter(adapter);

        if (!VariabiliStaticheLog.getInstance().getListaFiles().isEmpty()) {
            String path = VariabiliStaticheLog.getInstance().getListaFiles().get(0).getAbsolutePath();
            String Cosa = LeggeFileLineaPerLinea(path);
            txtTesto.setText(Cosa);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VariabiliStaticheLog.getInstance().setListaFiles(new ArrayList<>());
        act.finish();
    }

    private String LeggeFileLineaPerLinea(String path) {
        StringBuilder text = new StringBuilder();

        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line).append("\n");
                }
                br.close();
            } catch (IOException ignored) {

            }
        }

        return text.toString();
    }
}
