package com.looigi.wallpaperchanger2.classiPlayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaUtenti;
import com.looigi.wallpaperchanger2.classiPlayer.WebServices.ChiamateWsPlayer;
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

        if (!VariabiliStatichePlayer.getInstance().isGiaPartito()) {
            VariabiliStatichePlayer.getInstance().setGiaPartito(true);

            VariabiliStatichePlayer.getInstance().setStaSuonando(false);

            VariabiliStatichePlayer.getInstance().getTxtTitolo().setText("");
            VariabiliStatichePlayer.getInstance().getTxtInizio().setText("");
            VariabiliStatichePlayer.getInstance().getTxtFine().setText("");

            UtilityPlayer.getInstance().Attesa(false);
            UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");

            StrutturaUtenti su = new StrutturaUtenti();
            su.setId(1);
            su.setAmministratore(true);
            su.setUtente("Looigi");
            su.setPassword("");
            VariabiliStatichePlayer.getInstance().setUtente(su);

            db_dati_player db = new db_dati_player(this);
            StrutturaBrano sb = db.CaricaUltimoBrano();
            if (sb != null) {
                // Brano già scaricato
                VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);

                UtilityPlayer.getInstance().CaricaBranoNelLettore(this);
            } else {
                ChiamateWsPlayer ws = new ChiamateWsPlayer(this);
                ws.RitornaBranoDaID("1");
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

                ChiamateWsPlayer ws = new ChiamateWsPlayer(VariabiliStatichePlayer.getInstance().getAct());
                ws.RitornaBranoDaID("");
            }
        });
        VariabiliStatichePlayer.getInstance().getImgIndietro().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        VariabiliStatichePlayer.getInstance().getImgPlayStop().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityPlayer.getInstance().PressionePlay(VariabiliStatichePlayer.getInstance().getContext());
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
