package com.looigi.wallpaperchanger2.classeUtilityImmagini;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.VariabiliStaticheImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;

public class MainUtilityImmagini extends Activity {
    private Context context;
    private Activity act;

    // pinimg;romantik;wheel;tata;nylon;fantasy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_utility_immagini);

        context = this;
        act = this;

        Intent intent = getIntent();
        String idCategoria = intent.getStringExtra("idCategoria");
        if (idCategoria == null) { idCategoria = "1"; }
        VariabiliStaticheUtilityImmagini.getInstance().setIdCategoria(Integer.parseInt(idCategoria));

        VariabiliStaticheUtilityImmagini.getInstance().setTxtQuale(findViewById(R.id.txtQualeStaFacendo));
        VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");

        db_dati_ui db = new db_dati_ui(context);
        db.CreazioneTabelle();
        // db.PulisceTutto();
        db.LeggeDati();
        db.ChiudeDB();

        VariabiliStaticheUtilityImmagini.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoUI));
        VariabiliStaticheUtilityImmagini.getInstance().Attesa(false);
        VariabiliStaticheUtilityImmagini.getInstance().setLstImmagini(findViewById(R.id.lstUtilityImmagini));

        ChiamateWSMI c = new ChiamateWSMI(context);
        c.RitornaCategorie(false, "UI");

        /* CheckBox chkControlli = findViewById(R.id.chkControlli);
        chkControlli.setChecked(true);
        chkControlli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setChkControllo(chkControlli.isChecked());
            }
        });
        CheckBox chkUguali = findViewById(R.id.chkUguali);
        chkUguali.setChecked(true);
        chkUguali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setChkUguali(chkUguali.isChecked());
            }
        }); */

        CheckBox chkFC = findViewById(R.id.chkFC);
        chkFC.setChecked(false);
        chkFC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setChkFC(chkFC.isChecked());

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }
        });

        CheckBox chkPoche = findViewById(R.id.chkPoche);
        chkPoche.setChecked(false);
        chkPoche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setChkPoche(chkPoche.isChecked());

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }
        });

        EditText edtFiltro = findViewById(R.id.edtFiltro);
        edtFiltro.setText("");
        VariabiliStaticheUtilityImmagini.getInstance().setFiltroCategorie("");

        ImageView imgFiltro = findViewById(R.id.imgFiltro);
        imgFiltro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Filtro = edtFiltro.getText().toString();
                VariabiliStaticheUtilityImmagini.getInstance().setFiltroCategorie(Filtro);

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }
        });

        CheckBox chkRefresh = findViewById(R.id.chkRefresh);
        VariabiliStaticheUtilityImmagini.getInstance().setEsegueAncheRefresh(false);
        chkRefresh.setChecked(false);
        chkRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean attivo = chkRefresh.isChecked();
                VariabiliStaticheUtilityImmagini.getInstance().setEsegueAncheRefresh(attivo);
            }
        });

        ImageView imgEliminaCache = findViewById(R.id.imgEliminaCache);
        imgEliminaCache.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vogliono eliminare tutti i dati della cache sul db online?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.PulisceCache();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        ImageView imgRefreshAltre = findViewById(R.id.imgRefreshAltre);
        imgRefreshAltre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vogliono aggiornare le categorie altre sul db?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheUtilityImmagini.getInstance().setPrimoGiroRefreshAltre(true);

                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.RefreshImmaginiAltre();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        LinearLayout layBlocca = findViewById(R.id.layBlocca);
        Button btnBlocca = findViewById(R.id.btnBlocca);
        layBlocca.setVisibility(LinearLayout.GONE);
        btnBlocca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setBloccaElaborazione(true);
                layBlocca.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgEliminaTutto = findViewById(R.id.imgEliminaTutto);
        imgEliminaTutto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vogliono eliminare tutti i dati ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db_dati_ui db = new db_dati_ui(context);
                        db.PulisceTutto();
                        db.ChiudeDB();

                        VariabiliStaticheUtilityImmagini.getInstance().setAdapter(new AdapterListenerUI(context, null));
                        VariabiliStaticheUtilityImmagini.getInstance().getLstImmagini().setAdapter(VariabiliStaticheUtilityImmagini.getInstance().getAdapter());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        ImageView imgTutto = findViewById(R.id.imgControllaTutto);
        imgTutto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layBlocca.setVisibility(LinearLayout.VISIBLE);

                VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(true);
                int quale = UtilityUtilityImmagini.getInstance().ControllaProssimoNumero(0);
                if (quale > -1) {
                    VariabiliStaticheUtilityImmagini.getInstance().setQualeStaControllando(quale);

                    int idCategoria = VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().get(quale).getIdCategoria();
                    String Categoria = VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().get(quale).getCategoria();
                    VariabiliStaticheUtilityImmagini.getInstance().setCategoriaAttuale(Categoria);
                    VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("Elaborazione " + Categoria);
                    VariabiliStaticheUtilityImmagini.getInstance().setBloccaElaborazione(false);

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            if (VariabiliStaticheUtilityImmagini.getInstance().isEsegueAncheRefresh()) {
                                ChiamateWSUI ws = new ChiamateWSUI(context);
                                ws.RefreshImmagini(String.valueOf(idCategoria), false);
                            } else {
                                ChiamateWSUI ws = new ChiamateWSUI(context);
                                ws.ControlloImmagini(String.valueOf(idCategoria), "N");
                            }
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 500);
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna categoria rilevata");
                }
            }
        });

        ImageView imgRicerca = findViewById(R.id.imgRicerca);
        imgRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VariabiliStaticheUtilityImmagini.getInstance().setBloccaElaborazione(true);
        act.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheUtilityImmagini.getInstance().setBloccaElaborazione(true);
                this.finish();

                return true;
        }

        return false;
    }
}
