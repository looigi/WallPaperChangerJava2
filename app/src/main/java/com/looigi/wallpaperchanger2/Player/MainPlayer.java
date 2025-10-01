package com.looigi.wallpaperchanger2.Player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Impostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaUtenti;
import com.looigi.wallpaperchanger2.Player.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.Player.impostazioniInterne.impostazioni_player_interne;
import com.looigi.wallpaperchanger2.Player.scan.ScanBraniNonPresentiSuDB;
import com.looigi.wallpaperchanger2.Player.scan.ScanBraniPerLimite;
import com.looigi.wallpaperchanger2.UtilitiesVarie.OnSwipeTouchListener;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainPlayer extends Activity {
    private final AudioManager mAudioManager = null;
    private final ComponentName mReceiverComponent = null;
    private Context context;
    private LinearLayout layImpostazioni;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        context = this;
        Activity act = this;

        // GESTIONE TASTI CUFFIE
        /* mButtonMediaReceiver = new GestioneTastiCuffie();
        IntentFilter mediaFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        mAudioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mReceiverComponent = new ComponentName(this, GestioneTastiCuffie.class);
        mediaFilter.setPriority(2139999999);
        registerReceiver(mButtonMediaReceiver, mediaFilter, Context.RECEIVER_NOT_EXPORTED); */

        VariabiliStatichePlayer.getInstance().setAct(this);
        VariabiliStatichePlayer.getInstance().setContext(this);

        // db_dati_player db = new db_dati_player(context);
        // db.CaricaImpostazioni();

        VariabiliStatichePlayer.getInstance().setMascheraNascosta(false);

        VariabiliStatichePlayer.getInstance().setImgAvanti(findViewById(R.id.imgAvantiPlayer));
        VariabiliStatichePlayer.getInstance().setImgIndietro(findViewById(R.id.imgIndietroPlayer));
        VariabiliStatichePlayer.getInstance().setImgPlayStop(findViewById(R.id.imgPlayStop));
        VariabiliStatichePlayer.getInstance().setImgBrano(findViewById(R.id.imgBrano));
        VariabiliStatichePlayer.getInstance().setLayCaricamento(findViewById(R.id.layCaricamentoInCorso));
        VariabiliStatichePlayer.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloPlayer));
        VariabiliStatichePlayer.getInstance().setTxtOperazione(findViewById(R.id.txtOperazionePlayer));
        VariabiliStatichePlayer.getInstance().setTxtInizio(findViewById(R.id.txtInizio));
        VariabiliStatichePlayer.getInstance().setTxtFine(findViewById(R.id.txtFine));
        VariabiliStatichePlayer.getInstance().setSeekBarBrano(findViewById(R.id.seekBarBrano));
        // VariabiliStatichePlayer.getInstance().setTxtInformazioniPlayer(findViewById(R.id.txtInformazioniPlayer));
        VariabiliStatichePlayer.getInstance().setTxtBranoPregresso(findViewById(R.id.txtBranoPregresso));
        VariabiliStatichePlayer.getInstance().setImgCambiaPregresso(findViewById(R.id.imgCambiaPregresso));
        VariabiliStatichePlayer.getInstance().setImgCuffie(findViewById(R.id.imgCuffie));

        int[] bell = { R.id.imgBellezza0, R.id.imgBellezza1, R.id.imgBellezza2, R.id.imgBellezza3,
                R.id.imgBellezza4, R.id.imgBellezza5, R.id.imgBellezza6, R.id.imgBellezza7,
                R.id.imgBellezza8, R.id.imgBellezza9, R.id.imgBellezza10 };
        List<ImageView> l = new ArrayList<>();
        int numero = 0;
        for (int b : bell) {
            l.add(findViewById(b));
            int finalNumero = numero;
            l.get(numero).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // UtilitiesGlobali.getInstance().ApreToast(context, "Click " + finalNumero);
                    StrutturaBrano s = VariabiliStatichePlayer.getInstance().getUltimoBrano();
                    int idCanzone = s.getIdBrano();

                    ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                    ws.SettaStelle(idCanzone, finalNumero);
                }
            });
            numero++;
        }
        VariabiliStatichePlayer.getInstance().setImgBellezza(l);

        layImpostazioni = findViewById(R.id.layImpostazioniPlayerInterne);
        layImpostazioni.setVisibility(LinearLayout.GONE);

        VariabiliStatichePlayer.getInstance().setImgSfondoSettings(act.findViewById(R.id.imgSfondoSettings));

        VariabiliStatichePlayer.getInstance().setTxtNumeroImmagine(act.findViewById(R.id.txtNumeroImmagine));
        VariabiliStatichePlayer.getInstance().setEdtNumeroImmagine(act.findViewById(R.id.edtNumeroImmagine));
        VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("");
        VariabiliStatichePlayer.getInstance().getEdtNumeroImmagine().setText("");
        VariabiliStatichePlayer.getInstance().setTxtNomeImmaginePerModifica(act.findViewById(R.id.txtNomeImmagine));
        VariabiliStatichePlayer.getInstance().getTxtNomeImmaginePerModifica().setText("");

        ImageView imgNumero = (ImageView) findViewById(R.id.imgImpostaNumero);
        imgNumero.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int n = Integer.parseInt(VariabiliStatichePlayer.getInstance().getEdtNumeroImmagine().getText().toString());
                if (n < 0 || n > VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1 ) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Valore non valido");
                    return;
                }
                VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(n);

                UtilityPlayer.getInstance().ImpostaImmagineInterna(context);
            }
        });

        VariabiliStatichePlayer.getInstance().setSettingsAperte(false);
        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettings);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        UtilityPlayer.getInstance().ScriveInfoImmagine();

                        VariabiliStatichePlayer.getInstance().setSettingsAperte(true);
                        layImpostazioni.setVisibility(LinearLayout.VISIBLE);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });
        ImageView imgChiudeSettings = (ImageView) findViewById(R.id.imgChiusuraSettings);
        imgChiudeSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        layImpostazioni.setVisibility(LinearLayout.GONE);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });

        ImageView imgSettingsG = (ImageView) findViewById(R.id.imgSettingsGlobali);
        imgSettingsG.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(VariabiliStatichePlayer.getInstance().getContext(), MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "PLAYER");
                                iP.putExtras(b);
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

        VariabiliStatichePlayer.getInstance().setImgRetePresente(findViewById(R.id.imgRete));
        if (VariabiliStaticheStart.getInstance().isSegnaleAttivo()) {
            VariabiliStatichePlayer.getInstance().getImgRetePresente().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStatichePlayer.getInstance().getImgRetePresente().setVisibility(LinearLayout.GONE);
        }
        VariabiliStatichePlayer.getInstance().setTxtPercentuale(findViewById(R.id.txtpercentuale));

        if (VariabiliStatichePlayer.getInstance().isCuffieInserite()) {
            VariabiliStatichePlayer.getInstance().getImgCuffie().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStatichePlayer.getInstance().getImgCuffie().setVisibility(LinearLayout.GONE);
        }

        if (!VariabiliStatichePlayer.getInstance().isGiaPartito()) {
            VariabiliStatichePlayer.getInstance().setGiaPartito(true);

            VariabiliStatichePlayer.getInstance().setClasseChiamata(null);
            VariabiliStatichePlayer.getInstance().setStaSuonando(false);

            VariabiliStatichePlayer.getInstance().getTxtTitolo().setText("");
            VariabiliStatichePlayer.getInstance().getTxtInizio().setText("");
            VariabiliStatichePlayer.getInstance().getTxtFine().setText("");
            // VariabiliStatichePlayer.getInstance().getTxtInformazioniPlayer().setText("");

            ScanBraniNonPresentiSuDB s = new ScanBraniNonPresentiSuDB();
            s.controllaCanzoniNonSalvateSuDB(context, true);

            ScanBraniPerLimite sl = new ScanBraniPerLimite();
            sl.controllaSpazioOccupato(context);

            StrutturaUtenti su = new StrutturaUtenti();
            su.setId(1);
            su.setAmministratore(true);
            su.setUtente("Looigi");
            su.setPassword("");
            VariabiliStatichePlayer.getInstance().setUtente(su);

            db_dati_player db = new db_dati_player(context);
            String idBrano = db.CaricaUltimoBranoAscoltato();
            StrutturaBrano sb = null;
            if (!idBrano.isEmpty()) {
                sb = db.CaricaBrano(idBrano);
            }
            if (sb != null) {
                // Brano già scaricato
                VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);

                UtilityPlayer.getInstance().CaricaBranoNelLettore(this);

                // UtilityPlayer.getInstance().ImpostaImmagine(context);
            } else {
                UtilityPlayer.getInstance().BranoAvanti(this, "", false, false);
            }
            db.ChiudeDB();

            // this.moveTaskToBack(true);
            VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());
            UtilityPlayer.getInstance().FaiPartireTimerChiusura(context);
        } else {
            // RIPRISTINO SCHERMATA
            if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
                VariabiliStatichePlayer.getInstance().getTxtTitolo().setText(
                        VariabiliStatichePlayer.getInstance().getUltimoBrano().getBrano()
                );
            } else {
                VariabiliStatichePlayer.getInstance().getTxtTitolo().setText(
                        ""
                );
            }
            VariabiliStatichePlayer.getInstance().getTxtInizio().setText(
                    VariabiliStatichePlayer.getInstance().getInizioMinuti()
            );
            VariabiliStatichePlayer.getInstance().getTxtFine().setText(
                    VariabiliStatichePlayer.getInstance().getFineMinuti()
            );

            VariabiliStatichePlayer.getInstance().getSeekBarBrano().setMax(
                VariabiliStatichePlayer.getInstance().getDurataBranoInSecondi()
            );

            UtilityPlayer.getInstance().ImpostaPosizioneBrano(
                    VariabiliStatichePlayer.getInstance().getSecondiPassati()
            );

            UtilityPlayer.getInstance().ScrivePerc();

            UtilityPlayer.getInstance().ImpostaBellezza();
        }

        String ultimaImmagine = context.getFilesDir() + "/Player/UltimaImmagine.txt";
        if (Files.getInstance().EsisteFile(ultimaImmagine)) {
            String imm = Files.getInstance().LeggeFileUnico(ultimaImmagine);
            imm = imm.replace("\n", "");
            VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(imm);
            if (imm.isEmpty()) {
                UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
            } else {
                if (VariabiliStatichePlayer.getInstance().getPathUltimaImmagine() != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(VariabiliStatichePlayer.getInstance().getPathUltimaImmagine());
                    VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(bitmap);
                }
            }
        }

        Bitmap bmpStart;
        if (!VariabiliStatichePlayer.getInstance().isStaSuonando()) {
            bmpStart = BitmapFactory.decodeResource(this.getResources(), R.drawable.icona_suona);
        } else {
            bmpStart = BitmapFactory.decodeResource(this.getResources(), R.drawable.icona_pausa);
        }
        VariabiliStatichePlayer.getInstance().getImgPlayStop().setImageBitmap(bmpStart);

        VariabiliStatichePlayer.getInstance().getImgAvanti().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                UtilityPlayer.getInstance().StoppaTimer();

                UtilityPlayer.getInstance().BranoAvanti(
                        VariabiliStatichePlayer.getInstance().getContext(), "",
                        false, true);
            }
        });

        VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                UtilityPlayer.getInstance().RicaricaPregresso();
            }
        });

        VariabiliStatichePlayer.getInstance().getImgIndietro().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                UtilityPlayer.getInstance().IndietroBrano(context);
            }
        });

        VariabiliStatichePlayer.getInstance().getImgPlayStop().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

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
                    VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                    UtilityPlayer.getInstance().ImpostaPosizioneBrano(progress);
                }
            }
        });

        ImageView imgScorri = findViewById(R.id.imgScorriPL);
        imgScorri.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                UtilityPlayer.getInstance().ImpostaImmagine(context, -1);
            }
            public void onSwipeLeft() {
                VariabiliStatichePlayer.getInstance().setUltimaOperazioneTS(new Date().getTime());

                UtilityPlayer.getInstance().ImpostaImmagine(context, -1);
            }
            public void onSwipeBottom() {
            }
        });

        impostazioni_player_interne i = new impostazioni_player_interne(act, context);
        i.impostaMaschera();

        VariabiliStatichePlayer.getInstance().setPlayerAttivo(true);
    }

    @Override
    protected void onDestroy() {
        VariabiliStatichePlayer.getInstance().setPlayerAttivo(false);

        if (VariabiliStatichePlayer.getInstance().getClasseChiamata() != null) {
            VariabiliStatichePlayer.getInstance().getClasseChiamata().StoppaEsecuzione();
            VariabiliStatichePlayer.getInstance().setClasseChiamata(null);
        }

        /* if (mButtonMediaReceiver != null) {
            context.unregisterReceiver(mButtonMediaReceiver);
        } */

        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        VariabiliStatichePlayer.getInstance().setMascheraNascosta(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        VariabiliStatichePlayer.getInstance().setMascheraNascosta(false);
    }

    @Override
    public void onBackPressed() {
        if (VariabiliStatichePlayer.getInstance().isSettingsAperte()) {
            if (layImpostazioni != null) {
                layImpostazioni.setVisibility(LinearLayout.GONE);
            }
            VariabiliStatichePlayer.getInstance().setSettingsAperte(false);
        } else {
            super.onBackPressed();

            UtilityPlayer.getInstance().ImpostaImmagine(this, -1);
            VariabiliStatichePlayer.getInstance().setMascheraNascosta(true);

            if (VariabiliStaticheStart.getInstance().getMainActivity() != null) {
                VariabiliStaticheStart.getInstance().getMainActivity().moveTaskToBack(true);
            }
        }
    }
}
