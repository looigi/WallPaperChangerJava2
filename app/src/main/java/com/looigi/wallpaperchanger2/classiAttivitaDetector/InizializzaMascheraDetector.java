package com.looigi.wallpaperchanger2.classiAttivitaDetector;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.Receivers.Video;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.TestMemory.DatiMemoria;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.TestMemory.TestMemory;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.db_dati;
import com.looigi.wallpaperchanger2.utilities.Utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class InizializzaMascheraDetector {
    private static final String NomeMaschera = "INITMASCHERA";
    private Long datella1 = null;

    public void inizializzaMaschera(Context context, Activity act) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"onStartCommand Service");

        Utility.getInstance().ScriveLog(context, NomeMaschera, "On Create. Creazione Tabelle");

        /* Notification notifica = GestioneNotificheDetector.getInstance().StartNotifica(context);
        if (notifica != null) {
            Utility.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");
            GestioneNotificheDetector.getInstance().AggiornaNotifica();

            // Esecuzione e = new Esecuzione(context);
            // e.startServizio1();

            // Toast.makeText(context, VariabiliStaticheDetector.channelName + ": Foreground Partito",
            //         Toast.LENGTH_SHORT).show();
            Utility.getInstance().ApreToast(context, "Detector Partito");
        } else {
            Utility.getInstance().ScriveLog(context, NomeMaschera, "Notifica " +
                    VariabiliStaticheDetector.channelName + " nulla");
            Toast.makeText(context, "Notifica " + VariabiliStaticheDetector.channelName + " nulla",
                    Toast.LENGTH_SHORT).show();
        } */

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Instanziamento camera");

        // Camera2 c = new Camera2();
        // VariabiliStaticheDetector.getInstance().setCamera(c);
        // VariabiliStaticheDetector.getInstance().getCamera().Start(act, context, "MAIN");

        /* Button bEliminaLog = (Button) act.findViewById(R.id.btnEliminaLog);
        bEliminaLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PercorsoDIR = VariabiliStaticheDetector.getInstance().getPercorsoDIRLog();
                String nomeFile = VariabiliStaticheDetector.getInstance().getNomeFileDiLog();
                UtilityDetector.getInstance().EliminaFile(PercorsoDIR + "/" + nomeFile);

                nomeFile = VariabiliStaticheDetector.getInstance().getNomeLogServizio();
                UtilityDetector.getInstance().EliminaFile(PercorsoDIR + "/" + nomeFile);

                UtilityDetector.getInstance().VisualizzaToast(context, "Files di log eliminato", true);
            }
        });

        Switch sLog = (Switch) act.findViewById(R.id.sLog2); */
        Switch sVibrazione = (Switch) act.findViewById(R.id.sVibrazione);
        Switch sToast = (Switch) act.findViewById(R.id.sToast);

        // View fgmMappa = (View) act.findViewById(R.id.map);

        TextView txtCoords = (TextView) act.findViewById(R.id.txtCoords);
        VariabiliStaticheDetector.getInstance().setTxtCoords(txtCoords);

        PrendeModelloTelefono p = new PrendeModelloTelefono();
        String modello = p.getDeviceName();
        VariabiliStaticheDetector.getInstance().setModelloTelefono(modello);

        /* int cameraID = VariabiliStaticheDetector.getInstance().getFotocamera();
        if (VariabiliStaticheDetector.getInstance().getCamera() != null) {
            if (cameraID == 0) {
                VariabiliStaticheDetector.getInstance().getCamera().ImpostaFrontale();
            } else {
                VariabiliStaticheDetector.getInstance().getCamera().ImpostaRetro();
            }
        } */
        /* VariabiliStaticheDetector.getInstance().setLista((ListView) act.findViewById(R.id.lstRisoluzioni));
        if (VariabiliStaticheDetector.getInstance().getCamera() != null) {
            VariabiliStaticheDetector.getInstance().getCamera().RitornaRisoluzioni();
        }

        if (VariabiliStaticheDetector.getInstance().Dimensioni != null) {
            Impostazioni i = new Impostazioni();
            i.RiempieListaRisoluzioni(context, VariabiliStaticheDetector.getInstance().Dimensioni);
        } */

        // VariabiliStaticheDetector.getInstance().manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        CaricaSpinnerOrientamento(context, act);

        SistemaSchermata(act);

        // IMPOSTAZIONI
        /* sLog.setChecked(VariabiliStaticheDetector.getInstance().isFaiLog());
        sLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setFaiLog(sLog.isChecked());
            }
        }); */

        sVibrazione.setChecked(VariabiliStaticheDetector.getInstance().isVibrazione());
        sVibrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setVibrazione(sVibrazione.isChecked());

                Impostazioni i = new Impostazioni();
                i.ImpostaVibrazione(context, sVibrazione.isChecked());
            }
        });

        sToast.setChecked(VariabiliStaticheDetector.getInstance().isVisualizzaToast());
        sToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setVisualizzaToast(sToast.isChecked());

                Impostazioni i = new Impostazioni();
                i.ImpostaVisualizzaToast(context, sToast.isChecked());
            }
        });

        ImageView imgItaliano = (ImageView) act.findViewById(R.id.imgItaliano);
        imgItaliano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setLingua("ITALIANO");

                ImpostaCampiTestoPerLingua(act);
            }
        });

        Button cmdVideo = (Button) act.findViewById(R.id.cmdEsegueVideo);
        cmdVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Folder = new Intent(VariabiliStaticheDetector.getInstance().getMainActivity(), Video.class);
                VariabiliStaticheDetector.getInstance().getMainActivity().startActivity(Folder);
            }
        });

        ImageView imgInglese = (ImageView) act.findViewById(R.id.imgInglese);
        imgInglese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheDetector.getInstance().setLingua("INGLESE");

                ImpostaCampiTestoPerLingua(act);
            }
        });

        // AUTOSCATTO - IMMEDIATO
        RadioButton AS = (RadioButton) act.findViewById(R.id.optAutoScatto);
        AS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton IM = (RadioButton) act.findViewById(R.id.optImmediato);
                IM.setChecked(false);
                EditText et = (EditText) act.findViewById(R.id.txtSecondi);
                et.setEnabled(true);
                Button bi = (Button) act.findViewById(R.id.cmdImposta);
                bi.setEnabled(true);

                Impostazioni i = new Impostazioni();
                i.ImpostaAutoScatto(context, et);
            }
        });

        RadioButton IM = (RadioButton) act.findViewById(R.id.optImmediato);
        IM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton AS = (RadioButton) act.findViewById(R.id.optAutoScatto);
                AS.setChecked(false);
                EditText et = (EditText) act.findViewById(R.id.txtSecondi);
                et.setEnabled(false);
                Button bi = (Button) act.findViewById(R.id.cmdImposta);
                bi.setEnabled(false);

                Impostazioni i = new Impostazioni();
                i.ImpostaImmediato(context);
            }
        });

        EditText et = (EditText) act.findViewById(R.id.txtSecondi);
        et.setText(Integer.toString(VariabiliStaticheDetector.getInstance().getSecondi()));
        Button bi = (Button) act.findViewById(R.id.cmdImposta);
        bi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et = (EditText) act.findViewById(R.id.txtSecondi);

                Impostazioni i = new Impostazioni();
                i.ImpostaAutoScatto(context, et);

                UtilityDetector.getInstance().VisualizzaPOPUP("Saved", false, 0);
            }
        });

        EditText t = (EditText) act.findViewById(R.id.txtScatti);
        t.setText(Integer.toString(VariabiliStaticheDetector.getInstance().getNumeroScatti()));

        if (VariabiliStaticheDetector.getInstance().getTipologiaScatto() == 1) {
            AS.setChecked(false);
            IM.setChecked(true);
            et.setEnabled(false);
            bi.setEnabled(false);
        } else {
            AS.setChecked(true);
            IM.setChecked(false);
            et.setEnabled(true);
            bi.setEnabled(true);
        }

        // ORIGINALE - MASCHERATA
        RadioButton or = (RadioButton) act.findViewById(R.id.optOriginale);
        or.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton ma = (RadioButton) act.findViewById(R.id.optMascherata);
                ma.setChecked(false);

                Impostazioni i = new Impostazioni();
                i.ImpostaEstensioneOriginale(context);
            }
        });

        RadioButton ma = (RadioButton) act.findViewById(R.id.optMascherata);
        ma.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton or = (RadioButton) act.findViewById(R.id.optOriginale);
                or.setChecked(false);

                Impostazioni i = new Impostazioni();
                i.ImpostaEstensioneMascherata(context);
            }
        });

        if (VariabiliStaticheDetector.getInstance().getEstensione() == 1) {
            or.setChecked(true);
            ma.setChecked(false);
        } else {
            or.setChecked(false);
            ma.setChecked(true);
        }

        // ANTEPRIMA
        final CheckBox ca = (CheckBox) act.findViewById(R.id.chkAnteprima);
        ca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Impostazioni i = new Impostazioni();
                i.ImpostaAnteprima(context, ca);
            }
        });

        if (VariabiliStaticheDetector.getInstance().getAnteprima() == null) {
            VariabiliStaticheDetector.getInstance().setAnteprima("N");
        }
        if (VariabiliStaticheDetector.getInstance().getAnteprima().equals("S")) {
            ca.setChecked(true);
        } else {
            ca.setChecked(false);
        }

        // FRONTALE - RETRO
        RadioButton ff = (RadioButton) act.findViewById(R.id.optFrontale);
        ff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton fr = (RadioButton) act.findViewById(R.id.optRetro);
                fr.setChecked(false);

                // TextView tv = (TextView) view.findViewById(R.id.txtRisoluzione);
                /* VariabiliStaticheDetector.getInstance().getCamera().ImpostaFrontale();
                VariabiliStaticheDetector.getInstance().setFotocamera(0);

                VariabiliStaticheDetector.getInstance().getCamera().RitornaRisoluzioni();
                // Impostazioni i = new Impostazioni();
                // i.RiempieListaRisoluzioni(context, VariabiliStaticheDetector.getInstance().Dimensioni); */

                db_dati_detector db = new db_dati_detector(context);
                db.ScriveImpostazioni(context);
            }
        });

        RadioButton fr = (RadioButton) act.findViewById(R.id.optRetro);
        fr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton ff = (RadioButton) act.findViewById(R.id.optFrontale);
                ff.setChecked(false);

                // TextView tv = (TextView) view.findViewById(R.id.txtRisoluzione);
                /* VariabiliStaticheDetector.getInstance().getCamera().ImpostaRetro();
                VariabiliStaticheDetector.getInstance().setFotocamera(1);

                VariabiliStaticheDetector.getInstance().getCamera().RitornaRisoluzioni();
                // Impostazioni i = new Impostazioni();
                // i.RiempieListaRisoluzioni(context, VariabiliStaticheDetector.getInstance().Dimensioni); */

                db_dati_detector db = new db_dati_detector(context);
                db.ScriveImpostazioni(context);
            }
        });

        /* TextView tv = (TextView) act.findViewById(R.id.txtRisoluzione);
        tv.setText(VariabiliStaticheDetector.getInstance().getRisoluzione());

        if (VariabiliStaticheDetector.getInstance().getFotocamera() == 1) {
            ff.setChecked(true);
            fr.setChecked(true);
        } else {
            ff.setChecked(false);
            fr.setChecked(true);
        }

        ListView lvS = (ListView) act.findViewById(R.id.lstRisoluzioni);
        lvS.setClickable(true);
        lvS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String o = (String) VariabiliStaticheDetector.getInstance().Dimensioni.get(position);
                TextView tv = (TextView) act.findViewById(R.id.txtRisoluzione);
                tv.setText(o);

                Impostazioni i = new Impostazioni();
                i.ImpostaRisoluzione(context, o);
            }
        }); */

        Button bs = (Button) act.findViewById(R.id.cmdImpostaScatti);
        bs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText t = (EditText) act.findViewById(R.id.txtScatti);

                Impostazioni i = new Impostazioni();
                i.ImpostaNumScatti(context, t);

                UtilityDetector.getInstance().VisualizzaPOPUP("Saved", false, 0);
            }
        });

        ImpostaCampiTestoPerLingua(act);

        ScrollView lPr = (ScrollView) act.findViewById(R.id.scrollViewPrincipale);
        // VariabiliStaticheDetector.getInstance().setSvGenerale(lPr);
        lPr.setVisibility(LinearLayout.VISIBLE);

        TextView tTotale = (TextView) act.findViewById(R.id.txtMemoriaTotale);
        TextView tLibera = (TextView) act.findViewById(R.id.txtMemoriaLibera);
        TextView tUsata = (TextView) act.findViewById(R.id.txtMemoriaUsata);
        TextView tInfo = (TextView) act.findViewById(R.id.txtInfo);

        tLibera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                if (datella1 == null) {
                    Handler handlerTimer;
                    Runnable rTimer;

                    datella1 = System.currentTimeMillis();
                    // try {
                    UtilityDetector.getInstance().Vibra(context, 100);
                    // vibrator.vibrate(100);
                    // Thread.sleep(100);
                    // } catch (InterruptedException ignored) {

                    // }

                    handlerTimer = new Handler();
                    rTimer = new Runnable() {
                        public void run() {
                            datella1 = null;
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 2000);
                } else {
                    long diff = System.currentTimeMillis() - datella1;

                    if (diff < 1950) {
                        lPr.setVisibility(LinearLayout.GONE);
                    }
                }

                return false;
            }
        });

        TextView tTitEstensione = (TextView) act.findViewById(R.id.txtTitoloEstensione);
        // TextView txtKM = (TextView) view.findViewById(R.id.txtKM);
        // VariabiliStaticheDetector.getInstance().setTxtKM(txtKM);
        VariabiliStaticheDetector.getInstance().setTxtImm((TextView) act.findViewById(R.id.txtImmagini));
        VariabiliStaticheDetector.getInstance().setTxtNomeImm((TextView) act.findViewById(R.id.txtNomeImm));
        final LinearLayout lScatti = (LinearLayout) act.findViewById(R.id.layScatti);
        final LinearLayout lTasti = (LinearLayout) act.findViewById(R.id.layTasti);
        final LinearLayout lFrecce = (LinearLayout) act.findViewById(R.id.layFrecce);
        final LinearLayout lNomeImm = (LinearLayout) act.findViewById(R.id.layNomeImmagine);
        lScatti.setVisibility(LinearLayout.GONE);
        lTasti.setVisibility(LinearLayout.GONE);
        lFrecce.setVisibility(LinearLayout.GONE);
        lNomeImm.setVisibility(LinearLayout.GONE);

        VariabiliStaticheDetector.getInstance().getTxtImm().setVisibility(LinearLayout.GONE);
        VariabiliStaticheDetector.getInstance().getTxtNomeImm().setVisibility(LinearLayout.GONE);

        VariabiliStaticheDetector.getInstance().setImg((ImageView) act.findViewById(R.id.imgScatto));
        VariabiliStaticheDetector.getInstance().setAudio((ImageView) act.findViewById(R.id.imgPlayAudio));
        VariabiliStaticheDetector.getInstance().setvView((VideoView) act.findViewById(R.id.videoView1));

        VariabiliStaticheDetector.getInstance().getImg().setVisibility(LinearLayout.GONE);
        VariabiliStaticheDetector.getInstance().getAudio().setVisibility(LinearLayout.GONE);
        VariabiliStaticheDetector.getInstance().getvView().setVisibility(LinearLayout.GONE);

        VariabiliStaticheDetector.getInstance().getvView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliStaticheDetector.getInstance().StaVedendo) {
                    UtilityDetector.getInstance().PlayVideo(context);
                } else {
                    UtilityDetector.getInstance().StopVideo();
                }
            }
        });

        VariabiliStaticheDetector.getInstance().getAudio().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliStaticheDetector.getInstance().StaSuonando) {
                    UtilityDetector.getInstance().PlayAudio(context);
                } else {
                    UtilityDetector.getInstance().StopAudio();
                }
            }
        });

        final Button bDecripta = (Button) act.findViewById(R.id.cmdDecript);
        final Button bCripta = (Button) act.findViewById(R.id.cmdCript);
        final Button bSposta = (Button) act.findViewById(R.id.cmdSposta);

        bCripta.setVisibility(LinearLayout.GONE);
        bDecripta.setVisibility(LinearLayout.GONE);
        bSposta.setVisibility(LinearLayout.GONE);

        bDecripta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityDetector.getInstance().DeCriptaFiles(VariabiliStaticheDetector.getInstance().getContext());
            }
        });

        bCripta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityDetector.getInstance().CriptaFiles(VariabiliStaticheDetector.getInstance().getContext());
            }
        });

        bSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityDetector.getInstance().SpostaFile(VariabiliStaticheDetector.getInstance().getContext());
            }
        });

        tTitEstensione.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                if (datella1 == null) {
                    Handler handlerTimer;
                    Runnable rTimer;

                    datella1 = System.currentTimeMillis();
                    UtilityDetector.getInstance().Vibra(context, 100);

                    handlerTimer = new Handler();
                    rTimer = new Runnable() {
                        public void run() {
                            datella1 = null;
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 2000);
                } else {
                    long diff = System.currentTimeMillis() - datella1;

                    if (diff < 1950) {
                        lScatti.setVisibility(LinearLayout.VISIBLE);
                        lTasti.setVisibility(LinearLayout.VISIBLE);
                        lFrecce.setVisibility(LinearLayout.VISIBLE);
                        lNomeImm.setVisibility(LinearLayout.VISIBLE);
                        bCripta.setVisibility(LinearLayout.VISIBLE);
                        bDecripta.setVisibility(LinearLayout.VISIBLE);
                        bSposta.setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheDetector.getInstance().getTxtImm().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheDetector.getInstance().getTxtNomeImm().setVisibility(LinearLayout.VISIBLE);

                        UtilityDetector.getInstance().CaricaMultimedia(context);
                        UtilityDetector.getInstance().VisualizzaMultimedia(context);

                        Handler handlerTimer;
                        Runnable rTimer;

                        handlerTimer = new Handler(Looper.getMainLooper());
                        rTimer = new Runnable() {
                            public void run() {
                                VariabiliStaticheDetector.getInstance().MascheraImmaginiMostrata = true;
                            }
                        };
                        handlerTimer.postDelayed(rTimer, 2000);
                    }
                }

                return false;
            }
        });

        tTitEstensione.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheDetector.getInstance().MascheraImmaginiMostrata) {
                    lScatti.setVisibility(LinearLayout.GONE);
                    lTasti.setVisibility(LinearLayout.GONE);
                    lFrecce.setVisibility(LinearLayout.GONE);
                    lNomeImm.setVisibility(LinearLayout.GONE);

                    VariabiliStaticheDetector.getInstance().getTxtImm().setVisibility(LinearLayout.GONE);

                    VariabiliStaticheDetector.getInstance().MascheraImmaginiMostrata = false;
                }
            }
        });

        ImageView btnIndietro = (ImageView) act.findViewById(R.id.imgIndietro);
        ImageView btnAvanti = (ImageView) act.findViewById(R.id.imgAvanti);
        ImageView btnElimina = (ImageView) act.findViewById(R.id.imgElimina);
        ImageView btnRefresh = (ImageView) act.findViewById(R.id.imgRefresh);

        VariabiliStaticheDetector.getInstance().setBtnFlipX((ImageView) act.findViewById(R.id.imgFlipX));
        VariabiliStaticheDetector.getInstance().setBtnFlipY((ImageView) act.findViewById(R.id.imgFlipY));
        VariabiliStaticheDetector.getInstance().setBtnRuotaDes((ImageView) act.findViewById(R.id.imgRuotaDes));
        VariabiliStaticheDetector.getInstance().setBtnRuotaSin((ImageView) act.findViewById(R.id.imgRuotaSin));

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityDetector.getInstance().CaricaMultimedia(context);
                UtilityDetector.getInstance().VisualizzaMultimedia(context);
            }
        });

        VariabiliStaticheDetector.getInstance().getBtnFlipX().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GestioneImmagini u = new GestioneImmagini();
                u.FlipImmagine(context, true);

                int appo = VariabiliStaticheDetector.getInstance().numMultimedia;
                UtilityDetector.getInstance().CaricaMultimedia(context);
                VariabiliStaticheDetector.getInstance().numMultimedia = appo;
                UtilityDetector.getInstance().VisualizzaMultimedia(context);
            }
        });

        VariabiliStaticheDetector.getInstance().getBtnFlipY().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GestioneImmagini u = new GestioneImmagini();
                u.FlipImmagine(context, false);

                int appo = VariabiliStaticheDetector.getInstance().numMultimedia;
                UtilityDetector.getInstance().CaricaMultimedia(context);
                VariabiliStaticheDetector.getInstance().numMultimedia = appo;
                UtilityDetector.getInstance().VisualizzaMultimedia(context);
            }
        });

        btnIndietro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheDetector.getInstance().numMultimedia > 0) {
                    VariabiliStaticheDetector.getInstance().numMultimedia--;
                } else {
                    VariabiliStaticheDetector.getInstance().numMultimedia = VariabiliStaticheDetector.getInstance().totImmagini - 1;
                }

                UtilityDetector.getInstance().VisualizzaMultimedia(context);
            }
        });

        btnAvanti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheDetector.getInstance().numMultimedia < VariabiliStaticheDetector.getInstance().totImmagini - 1) {
                    VariabiliStaticheDetector.getInstance().numMultimedia++;
                } else {
                    VariabiliStaticheDetector.getInstance().numMultimedia = 0;
                }

                UtilityDetector.getInstance().VisualizzaMultimedia(context);
            }
        });

        VariabiliStaticheDetector.getInstance().getBtnRuotaSin().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GestioneImmagini u = new GestioneImmagini();
                u.RuotaImmagine(context,270);
            }
        });

        VariabiliStaticheDetector.getInstance().getBtnRuotaDes().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GestioneImmagini u = new GestioneImmagini();
                u.RuotaImmagine(context, 90);
            }
        });

        btnElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Origine = Environment.getExternalStorageDirectory().getAbsolutePath();
                String Cartella = UtilityDetector.getInstance().PrendePath(context);
                String NomeImmagine = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().numMultimedia);

                try {
                    File file = new File(Origine + Cartella + NomeImmagine);
                    boolean deleted = file.delete();
                } catch (Exception ignored) {

                }

                try {
                    File file = new File(Origine + Cartella + NomeImmagine + ".PV3");
                    boolean deleted = file.delete();
                } catch (Exception ignored) {

                }

                int appo = VariabiliStaticheDetector.getInstance().numMultimedia;
                UtilityDetector.getInstance().CaricaMultimedia(context);
                appo--;
                if (appo < 0) appo = 0;
                VariabiliStaticheDetector.getInstance().numMultimedia = appo;
                UtilityDetector.getInstance().VisualizzaMultimedia(context);
                UtilityDetector.getInstance().VisualizzaPOPUP("File multimediale eliminato", false, 0);
            }
        });

        TestMemory tm = new TestMemory();
        DatiMemoria d = new DatiMemoria();
        d = tm.LeggeValori();

        tTotale.setText(Float.toString(d.getMemoriaTotale()));
        tLibera.setText(Float.toString(d.getMemoriaLibera()));
        tUsata.setText(Float.toString(d.getMemoriaUsata()));

        tInfo.setText(getCPUDetails());

        act.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Maschera inizializzata");

        if (VariabiliStaticheDetector.getInstance().isChiudiActivity()) {
            VariabiliStaticheDetector.getInstance().setChiudiActivity(false);

            act.moveTaskToBack(true);
        }
    }

    private String getCPUDetails(){
        ProcessBuilder processBuilder;
        String cpuDetails = "";
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        InputStream is;
        Process process ;
        byte[] bArray ;
        bArray = new byte[1024];

        try{
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            is = process.getInputStream();

            while(is.read(bArray) != -1){
                cpuDetails = cpuDetails + new String(bArray);   //Stroing all the details in cpuDetails
            }
            is.close();

        } catch(IOException ex){
            ex.printStackTrace();
        }

        return cpuDetails;
    }

    private int qualeSchermata = 0;

    private void SistemaSchermata(Activity act) {
        Button btnHome = act.findViewById(R.id.btnMenuHome);
        Button btnEstensione = act.findViewById(R.id.btnMenuEstensione);
        Button btnImpostazioni = act.findViewById(R.id.btnMenuImpostazioni);
        Button btnVideo = act.findViewById(R.id.btnMenuVideo);
        Button btnTipoFoto = act.findViewById(R.id.btnMenuTipoFoto);
        Button btnTipoScatto = act.findViewById(R.id.btnMenuTipoScatto);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 0;
                VisualizzaSchermata(act);
            }
        });

        btnEstensione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 1;
                VisualizzaSchermata(act);
            }
        });

        btnImpostazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 2;
                VisualizzaSchermata(act);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 3;
                VisualizzaSchermata(act);
            }
        });

        btnTipoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 4;
                VisualizzaSchermata(act);
            }
        });

        btnTipoScatto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualeSchermata = 5;
                VisualizzaSchermata(act);
            }
        });

        VisualizzaSchermata(act);

        ImpostaInfo(act);
    }

    private void VisualizzaSchermata(Activity act) {
        LinearLayout layHome = act.findViewById(R.id.container_home);
        LinearLayout layEstensione = act.findViewById(R.id.container_tipo_estensione);
        LinearLayout layImpostazioni = act.findViewById(R.id.container_impostazioni);
        LinearLayout layVideo = act.findViewById(R.id.container_video);
        LinearLayout layTipoFotocamera = act.findViewById(R.id.container_tipo_fotocamera);
        LinearLayout layTipoScatto = act.findViewById(R.id.container_tipo_scatto);

        layHome.setVisibility(LinearLayout.GONE);
        layEstensione.setVisibility(LinearLayout.GONE);
        layImpostazioni.setVisibility(LinearLayout.GONE);
        layVideo.setVisibility(LinearLayout.GONE);
        layTipoFotocamera.setVisibility(LinearLayout.GONE);
        layTipoScatto.setVisibility(LinearLayout.GONE);

        switch (qualeSchermata) {
            case 0:
                layHome.setVisibility(LinearLayout.VISIBLE);
                break;
            case 1:
                layEstensione.setVisibility(LinearLayout.VISIBLE);
                break;
            case 2:
                layImpostazioni.setVisibility(LinearLayout.VISIBLE);
                break;
            case 3:
                layVideo.setVisibility(LinearLayout.VISIBLE);
                break;
            case 4:
                layTipoFotocamera.setVisibility(LinearLayout.VISIBLE);
                break;
            case 5:
                layTipoScatto.setVisibility(LinearLayout.VISIBLE);
                break;
        }
    }

    private void ImpostaCampiTestoPerLingua(Activity act) {

    }

    /*
    private void ImpostaCampiTestoPerLingua(Activity act) {
        RadioButton AS=(RadioButton) act.findViewById(R.id.optAutoScatto);
        RadioButton IM=(RadioButton) act.findViewById(R.id.optImmediato);
        RadioButton or=(RadioButton) act.findViewById(R.id.optOriginale);
        RadioButton ma=(RadioButton) act.findViewById(R.id.optMascherata);
        Button bi=(Button) act.findViewById(R.id.cmdImposta);
        RadioButton ff=(RadioButton) act.findViewById(R.id.optFrontale);
        RadioButton fr=(RadioButton) act.findViewById(R.id.optRetro);
        TextView tps=(TextView) act.findViewById(R.id.txtTitTipoScatto);
        // TextView t1=(TextView) view.findViewById(R.id.txtTitNick);
        // TextView t2=(TextView) view.findViewById(R.id.txtTitPass);
        TextView tpos=(TextView) act.findViewById(R.id.txtTitPos);
        TextView ts=(TextView) act.findViewById(R.id.textView22);
        TextView t4=(TextView) act.findViewById(R.id.txtTitoloEstensione);
        // TextView tr=(TextView) act.findViewById(R.id.TextView01);
        CheckBox c=(CheckBox) act.findViewById(R.id.chkVibrazione);
        CheckBox ca=(CheckBox) act.findViewById(R.id.chkAnteprima);
        TextView tns=(TextView) act.findViewById(R.id.textView3);
        // TextView ttu=(TextView) view.findViewById(R.id.txtTitUte);
        // Button cme=(Button) view.findViewById(R.id.cmdEsportaFiles);
        // Button cmr=(Button) view.findViewById(R.id.cmdRetrieve);
        //TextView tcaf=(TextView) view.findViewById(R.id.txtTitolo5);
        TextView to=(TextView) act.findViewById(R.id.txtTitOrient);
        if (VariabiliStaticheDetector.getInstance().getLingua().equals("INGLESE")) {
            // ttu.setText("Logged User:");
            tps.setText("Kind of shoot");
            // t1.setText("User");
            tpos.setText("Position of camera");
            // t2.setText("Password");
            t4.setText("File format");
            // tr.setText("Resolution");
            ts.setText("Seconds");
            AS.setText("Auto Shoot");
            IM.setText("Immediate");
            or.setText("JPG");
            ma.setText("Masked");
            bi.setText("Save");
            ff.setText("Frontal");
            fr.setText("Retro");
            c.setText("Vibration");
            ca.setText("Preview");
            tns.setText("Number of shots: ");
            // cme.setText("Export");
            // cmr.setText("Retrieve Images");
            // VariabiliStaticheDetector.getInstance().getTa().setText("Please login to export");
            // VariabiliStaticheDetector.getInstance().getTai().setText("Please login to import");
            //tcaf.setText("If you want, please, offer me a coffe. Spiator DONATE");
            to.setText("Orientation");
        } else {
            // ttu.setText("Utente loggato:");
            tps.setText("Tipologia di scatto");
            // t1.setText("Utente");
            tpos.setText("Fotocamera");
            // t2.setText("Password");
            t4.setText("Formato del file");
            // tr.setText("Risoluzione");
            ts.setText("Secondi");
            AS.setText("Auto scatto");
            IM.setText("Immediato");
            or.setText("JPG");
            ma.setText("Mascherata");
            bi.setText("Salva");
            ff.setText("Frontale");
            fr.setText("Retro");
            c.setText("Vibrazione");
            ca.setText("Anteprima");
            tns.setText("Numero scatti: ");
            // cme.setText("Esporta");
            // cmr.setText("Rileva Immagini");
            // VariabiliStaticheDetector.getInstance().getTa().setText("Effettua il login per esportare");
            // VariabiliStaticheDetector.getInstance().getTai().setText("Effettua il login per importare");
            //tcaf.setText("Se vuoi, offrimi un caff?. Spiator DONATE");
            to.setText("Orientamento");
        }

        ImpostaInfo(act);
    }

    private void SistemaSchermata(Activity act) {
        String[] Titoli;
        Titoli=new String[8];
        if (VariabiliStaticheDetector.getInstance().getLingua() == null) {
            VariabiliStaticheDetector.getInstance().setLingua("INGLESE");
        }
        if (VariabiliStaticheDetector.getInstance().getLingua().equals("INGLESE")) {
            Titoli[0]="Video";
            Titoli[1]="Shoot Type";
            Titoli[2]="Camera";
            Titoli[3]="Image Extension";
            // Titoli[4]="Photo Resolution";
            Titoli[4]="Settings";
            Titoli[5]="Info";
        } else {
            Titoli[0]="Video";
            Titoli[1]="Tipologia di scatto";
            Titoli[2]="Fotocamera";
            Titoli[3]="Estensione";
            // Titoli[4]="Risoluzione";
            Titoli[4]="Impostazioni";
            Titoli[5]="Info";
        }

        TabHost tabHost = (TabHost) act.findViewById(R.id.tabGenerale1);

        tabHost.setup();
        // tabHost.addTab(tabHost.newTabSpec("tabview6").setContent(R.id.tab6).setIndicator(Titoli[1]));
        tabHost.addTab(tabHost.newTabSpec("tabviewVideo").setContent(R.id.tabVideo).setIndicator(Titoli[0]));
        tabHost.addTab(tabHost.newTabSpec("tabviewTipoScatto").setContent(R.id.tabTipoScatto).setIndicator(Titoli[1]));
        tabHost.addTab(tabHost.newTabSpec("tabviewTipoFotocamera").setContent(R.id.tabTipoFotocamera).setIndicator(Titoli[2]));
        // tabHost.addTab(tabHost.newTabSpec("tabviewEstensione").setContent(R.id.tabEstensione).setIndicator(Titoli[3]));
        // tabHost.addTab(tabHost.newTabSpec("tabviewRisoluzione").setContent(R.id.tabRisoluzione).setIndicator(Titoli[4]));
        // tabHost.addTab(tabHost.newTabSpec("tabview9").setContent(R.id.tab9).setIndicator(Titoli[8]));
        tabHost.addTab(tabHost.newTabSpec("tabviewImpostazioni").setContent(R.id.tabImpostazioni).setIndicator(Titoli[5]));
        tabHost.addTab(tabHost.newTabSpec("tabviewHome").setContent(R.id.tabHome).setIndicator(Titoli[6]));
        // tabHost.addTab(tabHost.newTabSpec("tabview10").setContent(R.id.tab6).setIndicator(Titoli[6]));

        DisplayMetrics metrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        TabWidget tw = (TabWidget) tabHost.findViewById(android.R.id.tabs);

        for (int i = 0;i < 6; i++) {
            if (tw != null) {
                View tabView = tw.getChildTabViewAt(i);
                if (tabView != null) {
                    // tabView.getLayoutParams().width = 500; // LinearLayout.LayoutParams.WRAP_CONTENT;

                    TextView tv = (TextView) tabView.findViewById(android.R.id.title);
                    if (tv != null) {
                        tv.setText(Titoli[i]);
                        tv.setTextSize(15);
                    }
                }
            }
//	        tv.setWidth(200);
        }

        ImpostaInfo(act);
    }
    */

    private void ImpostaInfo(Activity act) {
        String Messaggio="";

        /* if (VariabiliStaticheDetector.getInstance().getLingua().equals("INGLESE")) {
            Messaggio+= "Detector: The app that will allow you to make photos directly from ";
            Messaggio+= "a dedicated widget to be placed on the home phone. You can, at any time and only by pressing on the ";
            Messaggio+= "relative, take a picture and trovarsela directly into the default directory ";
            Messaggio+= "which is /sdcard/LooigiSoft/Detector/DB without any other effort. \n \n";
            Messaggio+= "The procedure allows the configuration to use the front or back camera, for ";
            Messaggio+= "the resolution you want, ";
            Messaggio+= "to hide pictures to the gallery using an extension other than that of the ";
            Messaggio+= "known images, and even use a countdown. \n \n";
            Messaggio+= "For the sake hardware on some phone models the procedure fails to recognize the resolutions and ";
            Messaggio+= "will therefore not be possible to take pictures. However, we are trying to solve the problem. \n \n";
            Messaggio+="The new version will also allow the creation of video and the ability to save your images\n";
            Messaggio+="on a web space\n";
        } else { */
            Messaggio+="Detector: L'app che Vi permetterà di effettuare foto direttamente da ";
            Messaggio+="un widget dedicato da posizionare nella home del telefono. Sarà possibile, in qualsiasi momento e soltanto tramite pressione sull'icona ";
            Messaggio+="relativa, scattare una foto e trovarsela direttamente nella directory preimpostata ";
            Messaggio+="che è /sdcard/LooigiSoft/Detector/DB senza nessun altro sforzo.\n\n";
            Messaggio+="La procedura permette la configurazione per utilizzare la fotocamera anteriore o posteriore, per ";
            Messaggio+="impostare la risoluzione voluta, ";
            Messaggio+="per nascondere le immagini alla galleria utilizzando un'estensione diversa da quella delle";
            Messaggio+="immagini conosciute e, addirittura, utilizzare un countdown.\n\n";
            Messaggio+="Per motivi hardware su alcuni modelli di telefono la procedura non riesce a riconoscere le risoluzioni e";
            Messaggio+="non sarà quindi possibile scattare foto. Stiamo comunque tentando di risolvere il problema.\n\n";
            Messaggio+="La nuova versione permetterà inoltre la creazione di video e la possibilità di salvare le proprie immagini\n";
            Messaggio+="su uno spazio web\n";
        // }

        TextView tv=(TextView) act.findViewById(R.id.txtInfoHome);
        tv.setText(Messaggio);
    }

    private void CaricaSpinnerOrientamento(Context context, Activity act) {
        String[] Cartell=new String[5];
        Cartell[0]=Integer.toString(VariabiliStaticheDetector.getInstance().getOrientamento());
        Cartell[1]="0";
        Cartell[2]="90";
        Cartell[3]="180";
        Cartell[4]="270";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                Cartell
        );
        Spinner spinner=(Spinner) act.findViewById(R.id.spnOrient);
        spinner.setAdapter(adapter);
        spinner.setPrompt(Integer.toString(VariabiliStaticheDetector.getInstance().getOrientamento()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view,int pos, long id) {
                String selected ="";

                try {
                    selected = (String) adapter.getItemAtPosition(pos).toString().trim();
                    VariabiliStaticheDetector.getInstance().setOrientamento(Integer.parseInt(selected));
                } catch (Exception e) {
                    selected="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
