package com.looigi.wallpaperchanger2.classeAntifurto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class MainAntifurto extends Activity {
    private Context context;
    private Activity act;
    private Long controlloLongPress = null;

    public MainAntifurto() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_antifurto);

        context = this;
        act = this;

        /* if (VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
            act.finish();

            return;
        } */

        // VariabiliStaticheAntifurto.getInstance().setAllarmeAttivo(true);
        VariabiliStaticheAntifurto.getInstance().setTxtAllarme(findViewById(R.id.txtAllarme));
        VariabiliStaticheAntifurto.getInstance().getTxtAllarme().setText("");
        VariabiliStaticheAntifurto.getInstance().setTxtInfo1(findViewById(R.id.txtInfo1));
        VariabiliStaticheAntifurto.getInstance().getTxtInfo1().setText("");
        VariabiliStaticheAntifurto.getInstance().setTxtInfo2(findViewById(R.id.txtInfo2));
        VariabiliStaticheAntifurto.getInstance().getTxtInfo2().setText("");

        EditText edtGForce = findViewById(R.id.edtGForce);
        edtGForce.setText(Float.toString(VariabiliStaticheAntifurto.getInstance().getgForcePerAllarme()));
        ImageView imgImpostaGforce = findViewById(R.id.imgImpostaGForce);
        imgImpostaGforce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("START", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(
                        "gForcePerAllarme",
                        edtGForce.getText().toString());
                editor.apply();
                VariabiliStaticheAntifurto.getInstance().setgForcePerAllarme(Float.parseFloat(edtGForce.getText().toString()));
            }
        });

        EditText edtBT = findViewById(R.id.edtBT);
        edtBT.setText(VariabiliStaticheAntifurto.getInstance().getBtMonitorato());
        ImageView imgImpostaBT = findViewById(R.id.imgImpostaBT);
        imgImpostaBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("START", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(
                        "BTMonitorato",
                        edtBT.getText().toString());
                editor.apply();
                VariabiliStaticheAntifurto.getInstance().setBtMonitorato(edtBT.getText().toString());
            }
        });

        VariabiliStaticheAntifurto.getInstance().setActAllarme(act);
        LinearLayout layControlli = findViewById(R.id.layControlli);

        VolumePlayer vp = new VolumePlayer(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("DO");
        if (id != null) {
            if (id.contains("ALLARME")) {
                vp.playSoundAtMax(this, R.raw.bambino_che_piange);
                layControlli.setVisibility(LinearLayout.GONE);
            } else {
                layControlli.setVisibility(LinearLayout.VISIBLE);
            }
        }

        ImageView imgAllarme = findViewById(R.id.imgAllarme);
        // SwitchCompat swcOnOff = findViewById(R.id.sOnOff);
        // swcOnOff.setChecked(VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo());
        SwitchCompat swcBt = findViewById(R.id.sBT);
        SwitchCompat swcMovimento = findViewById(R.id.sSuMovimento);

        if (VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
            /* swcOnOff.setEnabled(false);
            imgAllarme.setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheAntifurto.getInstance().getTxtAllarme().setVisibility(LinearLayout.VISIBLE);
            layControlli.setVisibility(LinearLayout.GONE); */
        } else {
            /* swcOnOff.setEnabled(true);
            imgAllarme.setVisibility(LinearLayout.GONE);
            VariabiliStaticheAntifurto.getInstance().getTxtAllarme().setVisibility(LinearLayout.GONE);
            layControlli.setVisibility(LinearLayout.VISIBLE); */
        }

        imgAllarme.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (VariabiliStaticheAntifurto.getInstance().isAllarmeInCorso()) {
                    if (controlloLongPress == null) {
                        Handler handlerTimer;
                        Runnable rTimer;

                        controlloLongPress = System.currentTimeMillis();
                        UtilityWallpaper.getInstance().Vibra(context, 100);

                        handlerTimer = new Handler(Looper.getMainLooper());
                        rTimer = new Runnable() {
                            public void run() {
                                controlloLongPress = null;
                            }
                        };
                        handlerTimer.postDelayed(rTimer, 2000);
                    } else {
                        long diff = System.currentTimeMillis() - controlloLongPress;

                        if (diff < 1950) {
                            controlloLongPress = null;

                            vp.stop();

                            UtilityAntifurto.getInstance().AttivaAntifurto(context, false);

                            return true; // consumiamo l'evento
                        }
                    }
                }

                return false;
            }
        });

        SharedPreferences prefs = getSharedPreferences("START", MODE_PRIVATE);

        String BtAttivo = "";
        if (VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
            BtAttivo = prefs.getString(
                    "BTAttivo"
                    , "N");
        } else {
            BtAttivo = "N";
        }
        VariabiliStaticheAntifurto.getInstance().setAllarmeSuBT(BtAttivo.equals("S"));

        String SuMovimento = "";
        if (VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
            SuMovimento = prefs.getString(
                    "SuMovimento"
                    , "N");
        } else {
            SuMovimento = "N";
        }
        VariabiliStaticheAntifurto.getInstance().setAllarmeSuMovimento(SuMovimento.equals("S"));

        swcBt.setChecked(BtAttivo.equals("S"));
        swcMovimento.setChecked(SuMovimento.equals("S"));

        /* if (!VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
            // swcOnOff.setChecked(true);
            swcMovimento.setEnabled(true);
            swcBt.setEnabled(true);
        } else {
            // swcOnOff.setChecked(false);
            swcMovimento.setEnabled(false);
            swcBt.setEnabled(false);
        }

        swcOnOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
                    SharedPreferences prefs = getSharedPreferences("START", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    if (swcOnOff.isChecked()) {
                        UtilityAntifurto.getInstance().AttivaAntifurto(context, true);
                        editor.putString(
                                "AntifurtoAttivo",
                                "S");

                        swcMovimento.setEnabled(true);
                        swcBt.setEnabled(true);
                    } else {
                        UtilityAntifurto.getInstance().AttivaAntifurto(context, false);
                        editor.putString(
                                "AntifurtoAttivo",
                                "N");

                        swcMovimento.setEnabled(false);
                        swcBt.setEnabled(false);
                    }
                    editor.apply();
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Fermare l'allarme");
                }
            }
        }); */

        swcMovimento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("START", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(
                        "SuMovimento",
                        (swcMovimento.isChecked() ? "S" : "N"));
                editor.apply();
                VariabiliStaticheAntifurto.getInstance().setAllarmeSuMovimento(swcMovimento.isChecked());
            }
        });

        swcBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("START", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(
                        "BTAttivo",
                        (swcBt.isChecked() ? "S" : "N"));
                editor.apply();
                VariabiliStaticheAntifurto.getInstance().setAllarmeSuBT(swcBt.isChecked());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
            super.onBackPressed();

            act.finish();
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Allarme in corso");
        }
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
