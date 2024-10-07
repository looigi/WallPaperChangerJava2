package com.looigi.wallpaperchanger2.classiPlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaUtenti;
import com.looigi.wallpaperchanger2.classiPlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class MainPlayer extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        VariabiliStatichePlayer.getInstance().setAct(this);
        VariabiliStatichePlayer.getInstance().setContext(this);

        VariabiliStatichePlayer.getInstance().setMascheraNascosta(false);

        VariabiliStatichePlayer.getInstance().setImgAvanti(findViewById(R.id.imgAvantiPlayer));
        VariabiliStatichePlayer.getInstance().setImgIndietro(findViewById(R.id.imgIndietroPlayer));
        VariabiliStatichePlayer.getInstance().setImgPlayStop(findViewById(R.id.imgPlayStop));
        VariabiliStatichePlayer.getInstance().setImgBrano(findViewById(R.id.imgBrano));
        VariabiliStatichePlayer.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoPlayer));
        VariabiliStatichePlayer.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloPlayer));
        VariabiliStatichePlayer.getInstance().setTxtOperazione(findViewById(R.id.txtOperazionePlayer));
        VariabiliStatichePlayer.getInstance().setTxtInizio(findViewById(R.id.txtInizio));
        VariabiliStatichePlayer.getInstance().setTxtFine(findViewById(R.id.txtFine));
        VariabiliStatichePlayer.getInstance().setSeekBarBrano(findViewById(R.id.seekBarBrano));
        VariabiliStatichePlayer.getInstance().setTxtInformazioniPlayer(findViewById(R.id.txtInformazioniPlayer));
        VariabiliStatichePlayer.getInstance().setTxtBranoPregresso(findViewById(R.id.txtBranoPregresso));
        VariabiliStatichePlayer.getInstance().setImgCambiaPregresso(findViewById(R.id.imgCambiaPregresso));

        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettings);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(VariabiliStatichePlayer.getInstance().getContext(), MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                VariabiliStatichePlayer.getInstance().getContext().startActivity(iP);
                            }
                        }, 500);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });

        UtilityPlayer.getInstance().Attesa(false);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
        if (VariabiliStatichePlayer.getInstance().getStrutturaBranoPregressoCaricata() != null) {
            UtilityPlayer.getInstance().ScriveBranoPregresso();
        } else {
            VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText("");
            VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.GONE);
        }

        if (!VariabiliStatichePlayer.getInstance().isGiaPartito()) {
            VariabiliStatichePlayer.getInstance().setGiaPartito(true);

            VariabiliStatichePlayer.getInstance().setStaSuonando(false);

            VariabiliStatichePlayer.getInstance().getTxtTitolo().setText("");
            VariabiliStatichePlayer.getInstance().getTxtInizio().setText("");
            VariabiliStatichePlayer.getInstance().getTxtFine().setText("");
            VariabiliStatichePlayer.getInstance().getTxtInformazioniPlayer().setText("");

            StrutturaUtenti su = new StrutturaUtenti();
            su.setId(1);
            su.setAmministratore(true);
            su.setUtente("Looigi");
            su.setPassword("");
            VariabiliStatichePlayer.getInstance().setUtente(su);

            db_dati_player db = new db_dati_player(this);
            String idBrano = db.CaricaUltimoBranoAscoltato();
            StrutturaBrano sb = null;
            if (!idBrano.isEmpty()) {
                sb = db.CaricaBrano(idBrano);
            }
            if (sb != null) {
                // Brano già scaricato
                VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);

                UtilityPlayer.getInstance().CaricaBranoNelLettore(this);
            } else {
                UtilityPlayer.getInstance().BranoAvanti(this, "", false);
            }

            // this.moveTaskToBack(true);
        } else {
            VariabiliStatichePlayer.getInstance().getTxtInizio().setText(
                    VariabiliStatichePlayer.getInstance().getInizioMinuti()
            );
            VariabiliStatichePlayer.getInstance().setFineMinuti(
                    VariabiliStatichePlayer.getInstance().getFineMinuti()
            );
            VariabiliStatichePlayer.getInstance().getSeekBarBrano().setProgress(
                    VariabiliStatichePlayer.getInstance().getSecondiPassati()
            );
        }

        Bitmap bmpStart;
        if (!VariabiliStatichePlayer.getInstance().isStaSuonando()) {
            bmpStart = BitmapFactory.decodeResource(this.getResources(), R.drawable.play);
        } else {
            bmpStart = BitmapFactory.decodeResource(this.getResources(), R.drawable.pausa);
        }
        VariabiliStatichePlayer.getInstance().getImgPlayStop().setImageBitmap(bmpStart);

        VariabiliStatichePlayer.getInstance().getImgAvanti().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityPlayer.getInstance().StoppaTimer();

                UtilityPlayer.getInstance().BranoAvanti(
                        VariabiliStatichePlayer.getInstance().getContext(), "",
                        false);
            }
        });

        VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityPlayer.getInstance().RicaricaPregresso();
            }
        });

        VariabiliStatichePlayer.getInstance().getImgIndietro().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        VariabiliStatichePlayer.getInstance().getImgPlayStop().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean acceso = VariabiliStatichePlayer.getInstance().isStaSuonando();
                acceso = !acceso;
                Context context = UtilitiesGlobali.getInstance().tornaContextValido();
                UtilityPlayer.getInstance().PressionePlay(context, acceso);
            }
        });

        VariabiliStatichePlayer.getInstance().getSeekBarBrano().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                if (fromUser) {
                    UtilityPlayer.getInstance().ImpostaPosizioneBrano(progress);
                }
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        VariabiliStatichePlayer.getInstance().setMascheraNascosta(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VariabiliStatichePlayer.getInstance().setMascheraNascosta(true);

        VariabiliStaticheStart.getInstance().getMainActivity().moveTaskToBack(true);
    }
}
