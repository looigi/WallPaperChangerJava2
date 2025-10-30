package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.Fetekkie.VariabiliStaticheMostraImmaginiFetekkie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.VariabiliStaticheSpostamento;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.MainImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.MainOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.adapters.AdapterListenerUI;
// import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.MainVolti;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

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
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                false
        );

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

        RadioButton chkTutte = findViewById(R.id.chkTutte);
        chkTutte.setChecked(true);
        RadioButton chkFC = findViewById(R.id.chkFC);
        chkFC.setChecked(false);
        RadioButton chkPoche = findViewById(R.id.chkPoche);
        chkPoche.setChecked(false);
        RadioButton chkInvalide = findViewById(R.id.chkInv);
        chkInvalide.setChecked(false);

        chkTutte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setChkFC(false);
                VariabiliStaticheUtilityImmagini.getInstance().setChkPoche(false);
                VariabiliStaticheUtilityImmagini.getInstance().setChkInvalide(false);

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }
        });

        chkFC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setChkFC(true);
                VariabiliStaticheUtilityImmagini.getInstance().setChkPoche(false);
                VariabiliStaticheUtilityImmagini.getInstance().setChkInvalide(false);

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }
        });

        chkPoche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setChkFC(false);
                VariabiliStaticheUtilityImmagini.getInstance().setChkPoche(true);
                VariabiliStaticheUtilityImmagini.getInstance().setChkInvalide(false);

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }
        });

        chkInvalide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setChkFC(false);
                VariabiliStaticheUtilityImmagini.getInstance().setChkPoche(false);
                VariabiliStaticheUtilityImmagini.getInstance().setChkInvalide(true);

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }
        });

        VariabiliStaticheUtilityImmagini.getInstance().setTipoCategoria(3);
        /* RadioButton chkTutteLeCat = findViewById(R.id.chkTutteLeCat);
        chkTutteLeCat.setChecked(false); */
        RadioButton chkSoloDiRicerca = findViewById(R.id.chkSoloDiRicerca);
        chkSoloDiRicerca.setChecked(false);
        RadioButton chkSoloNormali = findViewById(R.id.chkSoloNormali);
        chkSoloNormali.setChecked(true);

        /* chkTutteLeCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setTipoCategoria(1);
                if (VariabiliStaticheUtilityImmagini.getInstance().getAdapter() != null) {
                    VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
                }
            }
        }); */

        CheckBox chkRefresh = findViewById(R.id.chkRefresh);
        LinearLayout layControllaTutto = findViewById(R.id.layControllaTutto);
        LinearLayout layNuovaCategoria = findViewById(R.id.layNuovaCategoria);
        ImageView imgTutto = findViewById(R.id.imgControllaTutto);
        ImageView imgNuovaCategoria = findViewById(R.id.imgNuovaCategoriaMI);

        layControllaTutto.setVisibility(LinearLayout.VISIBLE);
        layNuovaCategoria.setVisibility(LinearLayout.VISIBLE);
        chkRefresh.setVisibility(LinearLayout.VISIBLE);

        chkSoloDiRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setTipoCategoria(2);
                layControllaTutto.setVisibility(LinearLayout.GONE);
                layNuovaCategoria.setVisibility(LinearLayout.GONE);
                chkRefresh.setVisibility(LinearLayout.GONE);

                if (VariabiliStaticheUtilityImmagini.getInstance().getAdapter() != null) {
                    VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
                }
            }
        });

        chkSoloNormali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setTipoCategoria(3);
                layControllaTutto.setVisibility(LinearLayout.VISIBLE);
                layNuovaCategoria.setVisibility(LinearLayout.VISIBLE);
                chkRefresh.setVisibility(LinearLayout.VISIBLE);

                if (VariabiliStaticheUtilityImmagini.getInstance().getAdapter() != null) {
                    VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
                }
            }
        });

        ImageView imgAggiornaHash = findViewById(R.id.imgAggiornaHash);
        imgAggiornaHash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.SistemaImmaginiSenzaHash();
            }
        });

        EditText edtFiltro = findViewById(R.id.edtFiltro);
        edtFiltro.setText("");
        edtFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Non serve implementare
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Filtro = edtFiltro.getText().toString();
                VariabiliStaticheUtilityImmagini.getInstance().setFiltroCategorie(Filtro);

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Non serve implementare
            }
        });
        VariabiliStaticheUtilityImmagini.getInstance().setFiltroCategorie("");

        /* ImageView imgFiltro = findViewById(R.id.imgFiltro);
        imgFiltro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Filtro = edtFiltro.getText().toString();
                VariabiliStaticheUtilityImmagini.getInstance().setFiltroCategorie(Filtro);

                VariabiliStaticheUtilityImmagini.getInstance().getAdapter().aggiornaListaConFiltro();
            }
        }); */

        VariabiliStaticheUtilityImmagini.getInstance().setEsegueAncheRefresh(false);
        chkRefresh.setChecked(false);
        chkRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean attivo = chkRefresh.isChecked();
                VariabiliStaticheUtilityImmagini.getInstance().setEsegueAncheRefresh(attivo);
            }
        });

        /* ImageView imgEliminaCache = findViewById(R.id.imgEliminaCache);
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
        }); */

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
                builder.setTitle("Immagini");
                builder.setMessage("Si vogliono eliminare tutti i dati ?");
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

        imgTutto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layBlocca.setVisibility(LinearLayout.VISIBLE);

                VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(true);
                int quale = UtilityUtilityImmagini.getInstance().ControllaProssimoNumero(0);
                if (quale > -1 && quale < VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().size()) {
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
                    layBlocca.setVisibility(LinearLayout.GONE);
                    VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna categoria rilevata");
                }
            }
        });

        ImageView imgRicerca = findViewById(R.id.imgRicerca);
        imgRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iP = new Intent(context, MainImmaginiFuoriCategoria.class);
                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("IDCATEGORIA", "-1");
                b.putString("CATEGORIA", "NESSUNA");
                iP.putExtras(b);
                context.startActivity(iP);
            }
        });

        /* ImageView imgVolti = findViewById(R.id.imgVolti);
        imgVolti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iP = new Intent(context, MainVolti.class);
                context.startActivity(iP);
            }
        }); */

        // WEBVIEW
        VariabiliStaticheUtilityImmagini.getInstance().setLayWV(findViewById(R.id.layWebView));
        VariabiliStaticheUtilityImmagini.getInstance().getLayWV().setVisibility(LinearLayout.GONE);
        VariabiliStaticheUtilityImmagini.getInstance().setVwInCorso(false);

        VariabiliStaticheUtilityImmagini.getInstance().setWvRicerca(findViewById(R.id.webView));
        VariabiliStaticheUtilityImmagini.getInstance().getWvRicerca().setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(VariabiliStaticheUtilityImmagini.getInstance().getWvRicerca().getContext(),
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public void onLongPress(MotionEvent e) {
                            if (VariabiliStaticheUtilityImmagini.getInstance().getDownloadId() == -1) {
                                WebView.HitTestResult result = VariabiliStaticheUtilityImmagini.getInstance().getWvRicerca().getHitTestResult();
                                if (result != null &&
                                        (result.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                                                result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {

                                    String imageUrl = result.getExtra();
                                    UtilityUtilityImmagini.getInstance().showDownloadDialog(context, imageUrl);
                                }
                            } else {
                                UtilitiesGlobali.getInstance().ApreToast(context, "Download in corso");
                            }
                        }
                    });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false; // restituisci false per non bloccare altri eventi
            }
        });

        VariabiliStaticheUtilityImmagini.getInstance().setEdtVW(findViewById(R.id.edtRicerca));
        /*
        assert Filtro != null;
        edtRicercaWV.setText(Filtro.replace("_", " "));

        ImageView imgApreWV = findViewById(R.id.imgApreWV);
        imgApreWV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().getLayWV().setVisibility(LinearLayout.VISIBLE);

                String Filtro2 = edtRicercaWV.getText().toString();
                UtilityUtilityImmagini.getInstance().esegueRicercaWV(context, Filtro2);
            }
        }); */

        ImageView imgCercaWV = findViewById(R.id.imgCercaWV);
        imgCercaWV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Filtro2 = VariabiliStaticheUtilityImmagini.getInstance().getEdtVW().getText().toString();
                UtilityUtilityImmagini.getInstance().esegueRicercaWV(context, Filtro2);
            }
        });

        ImageView imgChiudeWV = findViewById(R.id.imgChiudeWV);
        imgChiudeWV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().setVwInCorso(false);
                VariabiliStaticheUtilityImmagini.getInstance().getLayWV().setVisibility(LinearLayout.GONE);
            }
        });
        // WEBVIEW

        imgNuovaCategoria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nuova categoria");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText("");
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Salvataggio = input.getText().toString();

                        if (Salvataggio.isEmpty()) {
                            UtilitiesGlobali.getInstance().ApreToast(context,
                                    "Immettere un nome categoria");
                        } else {
                            ChiamateWSMI ws = new ChiamateWSMI(context);
                            ws.CreaNuovaCategoria(Salvataggio.replace(" ", "_"), "UI");
                        }
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

        ImageView imgRefreshCategorie = findViewById(R.id.imgRefresh);
        imgRefreshCategorie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSMI ws = new ChiamateWSMI(context);
                ws.RitornaCategorie(true, "UI");
            }
        });

        ImageView imgOCR = findViewById(R.id.imgOCR);
        imgOCR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iP = new Intent(context, MainOCR.class);
                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iP);
            }
        });

        /* VariabiliStaticheUtilityImmagini.getInstance().setImgPreview(findViewById(R.id.imgPreview));
        VariabiliStaticheUtilityImmagini.getInstance().setLayPreview(findViewById(R.id.layPreview));
        VariabiliStaticheUtilityImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);
        ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().getImgPreview().setImageBitmap(null);
                VariabiliStaticheUtilityImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.GONE);
            }
        });
        VariabiliStaticheUtilityImmagini.getInstance().getImgPreview().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheUtilityImmagini.getInstance().Attesa(true);
                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.RitornaProssimaImmagine(
                        VariabiliStaticheUtilityImmagini.getInstance().getIdCategoriaImpostataAdapter()
                );
            }
        }); */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VariabiliStaticheUtilityImmagini.getInstance().setBloccaElaborazione(true);
        act.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!VariabiliStaticheUtilityImmagini.getInstance().isVwInCorso()) {
            super.onKeyDown(keyCode, event);

            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    VariabiliStaticheUtilityImmagini.getInstance().setBloccaElaborazione(true);
                    this.finish();

                    return true;
            }

            return false;
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "vebView in corso");

            return false;
        }
    }
}
