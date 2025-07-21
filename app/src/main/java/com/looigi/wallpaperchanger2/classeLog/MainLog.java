package com.looigi.wallpaperchanger2.classeLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeBackup.MainBackup;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainLog extends Activity {
    private static String NomeMaschera = "Main_Log";
    private Context context;
    private Activity act;
    private TextView txtTesto;
    private String Testo = "";
    private int numeroRigheMassime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_log);

        context = this;
        act = this;

        txtTesto = findViewById(R.id.txtTestoLog);
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
        String[] ll = l.toArray(new String[0]);

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spnFile,
                ll,
                ""
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                ll
        );
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFile.setAdapter(adapter); */

        EditText edtRighe = act.findViewById(R.id.edtRighe);
        edtRighe.setText(Integer.toString(numeroRigheMassime));
        ImageView imgImposta = findViewById(R.id.imgImposta);
        imgImposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numeroRigheMassime = Integer.parseInt(edtRighe.getText().toString());

                String Cosa = InverteTesto(Testo);
                txtTesto.setText(Cosa);
            }
        });

        ImageView imgInizio = findViewById(R.id.imgInizio);
        imgInizio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScrollaAllInizio();
            }
        });
        ImageView imgFine = findViewById(R.id.imgFine);
        imgFine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScrollaAllaFine();
            }
        });

        if (!VariabiliStaticheLog.getInstance().getListaFiles().isEmpty()) {
            String path = VariabiliStaticheLog.getInstance().getListaFiles().get(0).getAbsolutePath();
            Testo = LeggeFileLineaPerLinea(path);
            String Cosa = InverteTesto(Testo);
            txtTesto.setText(Cosa);
        }
    }

    private void ScrollaAllaFine() {
        int scrollAmount = txtTesto.getLayout().getLineTop(txtTesto.getLineCount()) - txtTesto.getHeight();
        // if there is no need to scroll, scrollAmount will be <=0
        if (scrollAmount > 0)
            txtTesto.scrollTo(0, scrollAmount);
        else
            txtTesto.scrollTo(0, 0);
        // txtTesto.setMovementMethod(new ScrollingMovementMethod());
    }

    private void ScrollaAllInizio() {
        txtTesto.scrollTo(0, 0);
    }

    private String InverteTesto(String Cosa) {
        String[] r = Cosa.split("\n");
        String Ritorno = "";
        int righe = 0;

        for (int i = r.length - 1; i >= 0; i--) {
            Ritorno += r[i] + "\n";
            righe++;
            if (righe > numeroRigheMassime) {
                break;
            }
        }

        return Ritorno;
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
