package com.looigi.wallpaperchanger2.classePlayer.impostazioniInterne;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeModificaImmagine.MainModificaImmagine;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerBrani;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaSalvataggi;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadImmagine;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classePlayer.preferiti_tags.Main_Preferiti_Tags;
import com.looigi.wallpaperchanger2.classePlayer.preferiti_tags.VariabiliStatichePrefTags;
import com.looigi.wallpaperchanger2.classePlayer.scan.ScanBraniNonPresentiSuDB;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class impostazioni_player_interne {
    private Activity act;
    private Context context;
    private String Filtro = "";
    private int tipoDataSelezionata = -1; // 0 - Inferiore / 1 - Superiore
    private boolean apertoDP = false;
    private ArrayAdapter<String> adapterSalvataggi;
    private final String StringaNessuno = "---Nessuno---";

    public impostazioni_player_interne(Activity act, Context context) {
        this.act = act;
        this.context = context;
    }

    public void impostaMaschera() {
        Button imgRicerche = (Button) act.findViewById(R.id.btnSettingsPlayerRicerche);
        imgRicerche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(0);

                ImpostazioniMascheraRicerca();
            }
        });

        Button imgBranilocali = (Button) act.findViewById(R.id.btnSettingsBraniLocali);
        imgBranilocali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(1);

                imposta_brani_locali();
            }
        });

        Button imgBraniOnLine = (Button) act.findViewById(R.id.btnSettingsBraniRemoti);
        imgBraniOnLine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(4);

                impostazioniMascheraBraniOnLine();
            }
        });

        Button imgRicercaBrano = (Button) act.findViewById(R.id.btnSettingsRicerca);
        imgRicercaBrano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(5);

                impostazioniMascheraRicercaBrano();
            }
        });

        Button imgSfondo = (Button) act.findViewById(R.id.btnSettingsImmagine);
        imgSfondo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(2);

                impostazioniMascheraSfondo();
            }
        });

        Button imgBrano = (Button) act.findViewById(R.id.btnSettingsBrano);
        imgBrano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(3);

                impostazioniMascheraBrano();
            }
        });

        visualizzaImpostazioniMaschera(0);

        ImpostazioniMascheraRicerca();
    }

    public void imposta_brani_locali() {
        db_dati_player db = new db_dati_player(context);
        List<StrutturaBrano> lista = db.CaricaTuttiIBraniLocali();
        db.ChiudeDB();

        long spazioOccupato = 0;
        for (StrutturaBrano l : lista) {
            spazioOccupato += (Files.getInstance().DimensioniFile(l.getPathBrano()) * 1024L);
        }
        VariabiliStatichePlayer.getInstance().setSpazioOccupato(spazioOccupato);
        float lim = VariabiliStatichePlayer.getInstance().getLimiteInGb();
        long limiteSpazio = (long) (lim * 1024 * 1024 * 1024);
        VariabiliStatichePlayer.getInstance().setSpazioMassimo(limiteSpazio);

        VariabiliStatichePlayer.getInstance().setTxtQuanteRicerca(act.findViewById(R.id.txtQuanteRicercaPL));

        ListView lstBrani = act.findViewById(R.id.lstBrani);
        AdapterListenerBrani customAdapterT = new AdapterListenerBrani(context, lista);
        lstBrani.setAdapter(customAdapterT);

        EditText edtFiltro = act.findViewById(R.id.edtFiltro);
        ImageView imgRicercaScelta = (ImageView) act.findViewById(R.id.imgRicercaSceltaPL);
        imgRicercaScelta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Filtro = edtFiltro.getText().toString();
                customAdapterT.updateData(Filtro);
            }
        });

        ImageView imgRefreshBrani = (ImageView) act.findViewById(R.id.imgRefreshBraniDB);
        imgRefreshBrani.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_player db = new db_dati_player(context);
                db.EliminaTutto();
                db.ChiudeDB();

                ScanBraniNonPresentiSuDB s = new ScanBraniNonPresentiSuDB();
                s.controllaCanzoniNonSalvateSuDB(context, true);
            }
        });
    }

    private void ImpostazioniMascheraRicerca() {
        final boolean[] primoIngresso = {true};
        Spinner spnSalvataggi = act.findViewById(R.id.spnSalvataggi);
        spnSalvataggi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Salvataggio = adapterView.getItemAtPosition(position).toString();
                VariabiliStatichePlayer.getInstance().setSalvataggioSelezionato(Salvataggio);

                if (Salvataggio.equals(StringaNessuno)) {
                    VariabiliStatichePlayer.getInstance().setRandom(true);
                    VariabiliStatichePlayer.getInstance().setRicercaStelle(true);
                    VariabiliStatichePlayer.getInstance().setStelleDaRicercare(8);
                    VariabiliStatichePlayer.getInstance().setStelleSuperiori(true);
                    VariabiliStatichePlayer.getInstance().setRicercaMaiAscoltata(false);
                    VariabiliStatichePlayer.getInstance().setRicercaTesto(false);
                    VariabiliStatichePlayer.getInstance().setTestoDaRicercare("");
                    VariabiliStatichePlayer.getInstance().setTestoDaNonRicercare("");
                    VariabiliStatichePlayer.getInstance().setRicercaPreferiti(false);
                    VariabiliStatichePlayer.getInstance().setPreferiti("");
                    VariabiliStatichePlayer.getInstance().setPreferitiElimina("");
                    VariabiliStatichePlayer.getInstance().setAndOrPref(true);
                    VariabiliStatichePlayer.getInstance().setRicercaTags(false);
                    VariabiliStatichePlayer.getInstance().setPreferitiTags("");
                    VariabiliStatichePlayer.getInstance().setPreferitiEliminaTags("");
                    VariabiliStatichePlayer.getInstance().setAndOrTags(true);
                    VariabiliStatichePlayer.getInstance().setDate(false);
                    VariabiliStatichePlayer.getInstance().setDataSuperiore(false);
                    VariabiliStatichePlayer.getInstance().setDataInferiore(false);
                    VariabiliStatichePlayer.getInstance().setsDataSuperiore("");
                    VariabiliStatichePlayer.getInstance().setsDataInferiore("");

                    db_dati_player db = new db_dati_player(context);
                    db.EliminaSalvataggioDefault();
                    db.ChiudeDB();

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ImpostazioniMascheraRicerca();
                        }
                    }, 500);
                    return;
                }

                for (StrutturaSalvataggi l : VariabiliStatichePlayer.getInstance().getListaSalvataggi()) {
                    if (l.getSalvataggio().equals(Salvataggio)) {
                        db_dati_player db = new db_dati_player(context);
                        db.CaricaSalvataggio(Integer.toString(l.getIdSalvataggio()));
                        db.SalvaSalvataggioDefault(Integer.toString(l.getIdSalvataggio()));
                        db.ChiudeDB();

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ImpostazioniMascheraRicerca();
                            }
                        }, 500);
                        break;
                    }
                };
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        CaricaSalvataggi(spnSalvataggi);

        final boolean[] primoIngresso2 = {true};
        Spinner spnStelle = act.findViewById(R.id.spnStelle);
        spnStelle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso2[0]) {
                    primoIngresso2[0] = false;
                    return;
                }

                String Stelle = adapterView.getItemAtPosition(position).toString();

                VariabiliStatichePlayer.getInstance().setStelleDaRicercare(Integer.parseInt(Stelle));

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        CaricaStelle(spnStelle);

        ImageView imgSalvaSalvataggio = act.findViewById(R.id.imgSalvaSalvataggio);
        imgSalvaSalvataggio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nome salvataggio");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Salvataggio = input.getText().toString();
                        if (Salvataggio.isEmpty()) {
                            UtilitiesGlobali.getInstance().ApreToast(context,
                                    "Immettere un nome salvataggio");
                        } else {
                            db_dati_player db = new db_dati_player(context);
                            db.ScriveSalvataggioDettaglio(Salvataggio);
                            db.ChiudeDB();

                            ImpostazioniMascheraRicerca();

                            VariabiliStatichePlayer.getInstance().setSalvataggioSelezionato(Salvataggio);

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int spinnerPosition = adapterSalvataggi.getPosition(Salvataggio);
                                    spnSalvataggi.setSelection(spinnerPosition);
                                }
                            }, 500);

                            UtilitiesGlobali.getInstance().ApreToast(context,
                                    "Salvataggio salvato");
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

        ImageView imgModificaSalvataggio = act.findViewById(R.id.imgModificaSalvataggio);
        imgModificaSalvataggio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePlayer.getInstance().getSalvataggioSelezionato().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context,
                            "Selezionare un salvataggio");
                } else {
                    if (VariabiliStatichePlayer.getInstance().getSalvataggioSelezionato().equals(StringaNessuno)) {
                        return;
                    }

                    for (StrutturaSalvataggi l : VariabiliStatichePlayer.getInstance().getListaSalvataggi()) {
                        if (l.getSalvataggio().equals(VariabiliStatichePlayer.getInstance().getSalvataggioSelezionato())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Nome salvataggio");

                            final EditText input = new EditText(context);
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            input.setText(l.getSalvataggio());
                            builder.setView(input);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String Salvataggio = input.getText().toString();
                                    if (Salvataggio.isEmpty()) {
                                        UtilitiesGlobali.getInstance().ApreToast(context,
                                                "Immettere un nome salvataggio");
                                    } else {
                                        db_dati_player db = new db_dati_player(context);
                                        db.ModificaSalvataggioDettaglio(Integer.toString(l.getIdSalvataggio()),
                                            Salvataggio);
                                        db.ChiudeDB();

                                        UtilitiesGlobali.getInstance().ApreToast(context,
                                                "Salvataggio modificato");
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

                            break;
                        }
                    };
                }
            }
        });

        ImageView imgEliminaSalvataggio = act.findViewById(R.id.imgEliminaSalvataggio);
        imgEliminaSalvataggio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePlayer.getInstance().getSalvataggioSelezionato().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context,
                            "Selezionare un salvataggio");
                } else {
                    if (VariabiliStatichePlayer.getInstance().getSalvataggioSelezionato().equals(StringaNessuno)) {
                        return;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("LooWebPlayer");
                    builder.setMessage("Vuoi eliminare il salvataggio selezionato ?");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (StrutturaSalvataggi l : VariabiliStatichePlayer.getInstance().getListaSalvataggi()) {
                                if (l.getSalvataggio().equals(VariabiliStatichePlayer.getInstance().getSalvataggioSelezionato())) {
                                    db_dati_player db = new db_dati_player(context);
                                    db.EliminaSalvataggio(Integer.toString(l.getIdSalvataggio()));
                                    db.ChiudeDB();

                                    ImpostazioniMascheraRicerca();

                                    UtilitiesGlobali.getInstance().ApreToast(context,
                                            "Salvataggio eliminato");
                                    break;
                                }
                            };
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
            }
        });

        SwitchCompat swcRandom = act.findViewById(R.id.sRandom);
        swcRandom.setChecked(VariabiliStatichePlayer.getInstance().isRandom());
        swcRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setRandom(swcRandom.isChecked());

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        LinearLayout layRicercaStelle = act.findViewById(R.id.layRicercaStelle);

        SwitchCompat swcStelle = act.findViewById(R.id.sStelle);
        swcStelle.setChecked(VariabiliStatichePlayer.getInstance().isRicercaStelle());
        if (swcStelle.isChecked()) {
            layRicercaStelle.setVisibility(LinearLayout.VISIBLE);
        } else {
            layRicercaStelle.setVisibility(LinearLayout.GONE);
        }
        swcStelle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setRicercaStelle(swcStelle.isChecked());
                if (swcStelle.isChecked()) {
                    layRicercaStelle.setVisibility(LinearLayout.VISIBLE);
                } else {
                    layRicercaStelle.setVisibility(LinearLayout.GONE);
                }

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        SwitchCompat swcStelleSuperiori = act.findViewById(R.id.sStelleSuperiori);
        swcStelleSuperiori.setChecked(VariabiliStatichePlayer.getInstance().isStelleSuperiori());
        swcStelleSuperiori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setStelleSuperiori(swcStelleSuperiori.isChecked());

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        SwitchCompat swcMaiAscoltate = act.findViewById(R.id.sMaiAscoltata);
        swcMaiAscoltate.setChecked(VariabiliStatichePlayer.getInstance().isRicercaMaiAscoltata());
        swcMaiAscoltate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setRicercaMaiAscoltata(swcMaiAscoltate.isChecked());

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        LinearLayout layRicercaTesto = act.findViewById(R.id.layRicercaTesto);
        EditText edtRicercaTesto = act.findViewById(R.id.edtRicercaTesto);
        EditText edtRicercaNonTesto = act.findViewById(R.id.edtRicercaNonTesto);

        SwitchCompat swcTesto = act.findViewById(R.id.sTesto);
        swcTesto.setChecked(VariabiliStatichePlayer.getInstance().isRicercaTesto());
        if (swcTesto.isChecked()) {
            layRicercaTesto.setVisibility(LinearLayout.VISIBLE);
        } else {
            layRicercaTesto.setVisibility(LinearLayout.GONE);
            edtRicercaTesto.setText("");
            edtRicercaNonTesto.setText("");
        }
        swcTesto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setRicercaTesto(swcTesto.isChecked());
                if (swcTesto.isChecked()) {
                    layRicercaTesto.setVisibility(LinearLayout.VISIBLE);
                } else {
                    layRicercaTesto.setVisibility(LinearLayout.GONE);
                }

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        edtRicercaTesto.setText(VariabiliStatichePlayer.getInstance().getTestoDaRicercare());
        edtRicercaTesto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    VariabiliStatichePlayer.getInstance().setTestoDaRicercare(edtRicercaTesto.getText().toString());

                    db_dati_player db = new db_dati_player(context);
                    db.ScriveImpostazioni();
                    db.ChiudeDB();
                }
            }
        });

        edtRicercaNonTesto.setText(VariabiliStatichePlayer.getInstance().getTestoDaNonRicercare());
        edtRicercaNonTesto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    VariabiliStatichePlayer.getInstance().setTestoDaNonRicercare(edtRicercaNonTesto.getText().toString());

                    db_dati_player db = new db_dati_player(context);
                    db.ScriveImpostazioni();
                    db.ChiudeDB();
                }
            }
        });

        // PREFERITI
        LinearLayout layRicercaPreferiti = act.findViewById(R.id.layRicercaPreferiti);

        VariabiliStatichePlayer.getInstance().setTxtPreferiti(act.findViewById(R.id.txtPreferiti));
        VariabiliStatichePlayer.getInstance().setTxtNonPreferiti(act.findViewById(R.id.txtNonPreferiti));

        SwitchCompat swcPreferiti = act.findViewById(R.id.sPreferiti);
        swcPreferiti.setChecked(VariabiliStatichePlayer.getInstance().isRicercaPreferiti());
        if (swcPreferiti.isChecked()) {
            layRicercaPreferiti.setVisibility(LinearLayout.VISIBLE);
        } else {
            layRicercaPreferiti.setVisibility(LinearLayout.GONE);
            VariabiliStatichePlayer.getInstance().getTxtPreferiti().setText("");
            VariabiliStatichePlayer.getInstance().getTxtNonPreferiti().setText("");
        }
        swcPreferiti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setRicercaPreferiti(swcPreferiti.isChecked());
                if (swcPreferiti.isChecked()) {
                    layRicercaPreferiti.setVisibility(LinearLayout.VISIBLE);
                } else {
                    layRicercaPreferiti.setVisibility(LinearLayout.GONE);
                }

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        Button btnPreferiti = act.findViewById(R.id.btnPreferiti);
        btnPreferiti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context, Main_Preferiti_Tags.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DO", "Preferiti");
                context.startActivity(i);
            }
        });

        Button btnPreferitiElimina = act.findViewById(R.id.btnPreferitiElimina);
        btnPreferitiElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context, Main_Preferiti_Tags.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DO", "PreferitiElimina");
                context.startActivity(i);
            }
        });

        RadioButton optAndPreferiti = act.findViewById(R.id.optAndPreferiti);
        RadioButton optOrPreferiti = act.findViewById(R.id.optOrPreferiti);

        if (VariabiliStatichePlayer.getInstance().isAndOrPref()) {
            optAndPreferiti.setChecked(true);
            optOrPreferiti.setChecked(false);
        } else {
            optAndPreferiti.setChecked(false);
            optOrPreferiti.setChecked(true);
        }
        optAndPreferiti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setAndOrPref(true);

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });
        optOrPreferiti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setAndOrPref(false);

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        // TAGS
        LinearLayout layRicercaTags = act.findViewById(R.id.layRicercaTags);

        VariabiliStatichePlayer.getInstance().setTxtTags(act.findViewById(R.id.txtTags));
        VariabiliStatichePlayer.getInstance().setTxtNonTags(act.findViewById(R.id.txtNonTags));

        SwitchCompat swcTags = act.findViewById(R.id.sTags);
        swcTags.setChecked(VariabiliStatichePlayer.getInstance().isRicercaTags());
        if (swcTags.isChecked()) {
            layRicercaTags.setVisibility(LinearLayout.VISIBLE);
        } else {
            layRicercaTags.setVisibility(LinearLayout.GONE);
            VariabiliStatichePlayer.getInstance().getTxtTags().setText("");
            VariabiliStatichePlayer.getInstance().getTxtNonTags().setText("");
        }
        swcTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setRicercaTags(swcTags.isChecked());
                if (swcTags.isChecked()) {
                    layRicercaTags.setVisibility(LinearLayout.VISIBLE);
                } else {
                    layRicercaTags.setVisibility(LinearLayout.GONE);
                }

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        Button btnTags = act.findViewById(R.id.btnTags);
        btnTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context, Main_Preferiti_Tags.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DO", "Tags");
                context.startActivity(i);
            }
        });

        Button btnTagsElimina = act.findViewById(R.id.btnTagsElimina);
        btnTagsElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context, Main_Preferiti_Tags.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DO", "TagsElimina");
                context.startActivity(i);
            }
        });

        RadioButton optAndTags = act.findViewById(R.id.optAndTags);
        RadioButton optOrTags = act.findViewById(R.id.optOrTags);

        if (VariabiliStatichePlayer.getInstance().isAndOrTags()) {
            optAndTags.setChecked(true);
            optOrTags.setChecked(false);
        } else {
            optAndTags.setChecked(false);
            optOrTags.setChecked(true);
        }
        optAndTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setAndOrTags(true);

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });
        optOrTags.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setAndOrTags(false);

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        // DATE
        LinearLayout layRicercaDate = act.findViewById(R.id.layRicercaDate);
        TextView txtDataSuperiore = act.findViewById(R.id.txtDataSuperiore);
        txtDataSuperiore.setText(VariabiliStatichePlayer.getInstance().getsDataSuperiore());
        TextView txtDataInferiore = act.findViewById(R.id.txtDataInferiore);
        txtDataInferiore.setText(VariabiliStatichePlayer.getInstance().getsDataInferiore());

        SwitchCompat swcDate = act.findViewById(R.id.sDate);
        swcDate.setChecked(VariabiliStatichePlayer.getInstance().isDate());
        if (swcDate.isChecked()) {
            layRicercaDate.setVisibility(LinearLayout.VISIBLE);
        } else {
            layRicercaDate.setVisibility(LinearLayout.GONE);
            txtDataSuperiore.setText("");
            txtDataInferiore.setText("");
        }
        swcDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setDate(swcDate.isChecked());
                if (swcDate.isChecked()) {
                    layRicercaDate.setVisibility(LinearLayout.VISIBLE);
                } else {
                    layRicercaDate.setVisibility(LinearLayout.GONE);
                }

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
                db.ChiudeDB();
            }
        });

        DatePicker dpSceltaData = act.findViewById(R.id.dpSceltaData);

        Calendar calendar = Calendar.getInstance();
        int annoInizio = calendar.get(Calendar.YEAR);
        int meseInizio = calendar.get(Calendar.MONTH) + 1;
        int giornoInizio = calendar.get(Calendar.DAY_OF_MONTH);
        dpSceltaData.updateDate(annoInizio, meseInizio, giornoInizio);

        dpSceltaData.setVisibility(LinearLayout.GONE);

        dpSceltaData.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (apertoDP) {
                    apertoDP = false;
                } else {
                    String dataFinale = year + ";" + (monthOfYear + 1) + ";" + dayOfMonth;

                    if (tipoDataSelezionata == 0) {
                        VariabiliStatichePlayer.getInstance().setsDataInferiore(dataFinale);

                        txtDataInferiore.setText(dataFinale);
                    } else {
                        VariabiliStatichePlayer.getInstance().setsDataSuperiore(dataFinale);

                        txtDataSuperiore.setText(dataFinale);
                    }

                    db_dati_player db = new db_dati_player(context);
                    db.ScriveRicerca();
                    db.ChiudeDB();

                    dpSceltaData.setVisibility(LinearLayout.GONE);
                }
            }
        });

        Button btnDataSuperiore = act.findViewById(R.id.btnDataSuperiore);
        btnDataSuperiore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tipoDataSelezionata = 1;
                apertoDP = true;

                dpSceltaData.setVisibility(LinearLayout.VISIBLE);

                String data = VariabiliStatichePlayer.getInstance().getsDataSuperiore();
                if (!data.isEmpty()) {
                    String[] d = data.split(";");
                    int anno = Integer.parseInt(d[0]);
                    int mese = Integer.parseInt(d[1]);
                    int giorno = Integer.parseInt(d[2]);

                    dpSceltaData.updateDate(anno, mese, giorno);
                } else {
                    dpSceltaData.updateDate(annoInizio, meseInizio, giornoInizio);
                }
            }
        });

        Button btnDataInferiore = act.findViewById(R.id.btnDataInferiore);
        btnDataInferiore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tipoDataSelezionata = 0;
                apertoDP = true;

                dpSceltaData.setVisibility(LinearLayout.VISIBLE);

                String data = VariabiliStatichePlayer.getInstance().getsDataInferiore();
                if (!data.isEmpty()) {
                    String[] d = data.split(";");
                    int anno = Integer.parseInt(d[0]);
                    int mese = Integer.parseInt(d[1]);
                    int giorno = Integer.parseInt(d[2]);

                    dpSceltaData.updateDate(anno, mese, giorno);
                } else {
                    dpSceltaData.updateDate(annoInizio, meseInizio, giornoInizio);
                }
            }
        });
    }

    private void impostazioniMascheraBrano() {
        ImageView imgCondividi = act.findViewById(R.id.imgCondividiBrano);
        imgCondividi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Path = VariabiliStatichePlayer.getInstance().getUltimoBrano().getPathBrano();

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                File f = new File(Path);
                Uri uri = FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider",
                        f);

                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, Path);
                // i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                i.putExtra(Intent.EXTRA_STREAM,uri);
                i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                context.startActivity(Intent.createChooser(i,"Share immagine"));
            }
        });

        TextView txtTagsBrano = act.findViewById(R.id.txtTagsBrano);
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
            txtTagsBrano.setText("Tags: " + VariabiliStatichePlayer.getInstance().getUltimoBrano().getTags());
        } else {
            txtTagsBrano.setText("");
        }
        VariabiliStatichePrefTags.getInstance().setTxtTagsPerBrano(txtTagsBrano);

        ImageView imgTagsBrano = act.findViewById(R.id.imgTagsBrano);
        imgTagsBrano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context, Main_Preferiti_Tags.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("DO", "TagsBrano");
                context.startActivity(i);
            }
        });
    }

    private void impostazioniMascheraSfondo() {
        VariabiliStatichePlayer.getInstance().setImgImposta(act.findViewById(R.id.imgImpostaWP));
        VariabiliStatichePlayer.getInstance().getImgImposta().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePlayer.getInstance().getPathUltimaImmagine() != null) {
                    UtilityPlayer.getInstance().Attesa(true);

                    String Path = VariabiliStatichePlayer.getInstance().getPathUltimaImmagine();
                    String[] N = Path.split("/");
                    String Nome = N[N.length - 1];
                    String Data = Files.getInstance().DataFile(Path).toString();
                    long Dimensione = Files.getInstance().DimensioniFile(Path);

                    StrutturaImmagine src = new StrutturaImmagine();
                    src.setPathImmagine(Path);
                    src.setImmagine(Nome);
                    src.setDimensione(String.valueOf(Dimensione));
                    src.setDataImmagine(Data);

                    ChangeWallpaper c = new ChangeWallpaper(context, "PLAYER", src);
                    c.setWallpaperLocale(context, src);

                    UtilityPlayer.getInstance().Attesa(false);
                }
            }
        });

        VariabiliStatichePlayer.getInstance().setImgIndietroSfondo(act.findViewById(R.id.imgIndietroSfondoPlayer));
        VariabiliStatichePlayer.getInstance().getImgIndietroSfondo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int n = VariabiliStatichePlayer.getInstance().getIdImmagineImpostata();
                if (n - 1 > 0) {
                    n--;
                } else {
                    n = VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1;
                }
                VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(n);
                UtilityPlayer.getInstance().ImpostaImmagineInterna(context);
            }
        });

        VariabiliStatichePlayer.getInstance().setImgAvantiSfondo(act.findViewById(R.id.imgAvantiSfondoPlayer));
        VariabiliStatichePlayer.getInstance().getImgAvantiSfondo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int n = VariabiliStatichePlayer.getInstance().getIdImmagineImpostata();
                if (n + 1 <= VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1) {
                    n++;
                } else {
                    n = 0;
                }
                VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(n);
                UtilityPlayer.getInstance().ImpostaImmagineInterna(context);
            }
        });

        LinearLayout layTempoCambio = act.findViewById(R.id.layTempoCambio);

        SwitchCompat swcCambiaImmagine = act.findViewById(R.id.sCambiaImmagine);
        swcCambiaImmagine.setChecked(VariabiliStatichePlayer.getInstance().isCambiaImmagine());
        if (VariabiliStatichePlayer.getInstance().isCambiaImmagine()) {
            layTempoCambio.setVisibility(LinearLayout.VISIBLE);
        } else {
            layTempoCambio.setVisibility(LinearLayout.GONE);
        }
        swcCambiaImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setCambiaImmagine(swcCambiaImmagine.isChecked());
                if (VariabiliStatichePlayer.getInstance().isCambiaImmagine()) {
                    layTempoCambio.setVisibility(LinearLayout.VISIBLE);
                } else {
                    layTempoCambio.setVisibility(LinearLayout.GONE);
                }

                db_dati_player db = new db_dati_player(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        EditText edtTempoCambio = act.findViewById(R.id.edtTempoCambio);
        edtTempoCambio.setText(Integer.toString(VariabiliStatichePlayer.getInstance().getTempoCambioImmagine()));
        edtTempoCambio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    VariabiliStatichePlayer.getInstance().setTempoCambioImmagine(Integer.parseInt(edtTempoCambio.getText().toString()));

                    db_dati_player db = new db_dati_player(context);
                    db.ScriveImpostazioni();
                    db.ChiudeDB();
                }
            }
        });

        VariabiliStatichePlayer.getInstance().setImgEliminaSfondo(act.findViewById(R.id.imgEliminaSfondo));
        VariabiliStatichePlayer.getInstance().getImgEliminaSfondo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliStatichePlayer.getInstance().isCeImmaginePerModifica()) {
                    return;
                }

                // String path = VariabiliStatichePlayer.getInstance().getPathUltimaImmagine();
                if (VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica() != null) {
                    StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica();
                    String path = s.getPathImmagine();
                    if (path != null) {
                        if (s != null) {
                            db_dati_player db = new db_dati_player(context);
                            db.EliminaImmagineFisica(s.getArtista(), s.getNomeImmagine());
                            db.ChiudeDB();
                        }

                        if (Files.getInstance().EsisteFile(path)) {
                            Files.getInstance().EliminaFileUnico(path);
                        }

                        if (s != null) {
                            ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
                            c.EliminaImmagine(s.getArtista(), s.getAlbum(), s.getNomeImmagine());
                        }
                    }
                }
            }
        });

        VariabiliStatichePlayer.getInstance().setImgRefreshImmagini(act.findViewById(R.id.imgRefreshImmagini));
        VariabiliStatichePlayer.getInstance().getImgRefreshImmagini().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Artista = VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista();

                ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
                c.RitornaImmaginiArtista(Artista);
            }
        });

        VariabiliStatichePlayer.getInstance().setImgCopiaSuSfondi(act.findViewById(R.id.imgCopiaSuSfondi));
        VariabiliStatichePlayer.getInstance().getImgCopiaSuSfondi().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica();
                String UrlImmagine = s.getUrlImmagine();

                if (UrlImmagine.contains("http://")) {
                    DownloadImmagine d = new DownloadImmagine();
                    d.EsegueDownload(context,
                            VariabiliStatichePlayer.getInstance().getImgSfondoSettings(),
                            UrlImmagine, true, true, s.getNomeImmagine());
                } else {
                    String result = UtilitiesGlobali.getInstance().convertBmpToBase64(UrlImmagine);

                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                    ws.ScriveImmagineSuSfondiLocale("DaPlayer/" + s.getNomeImmagine(), result);
                }
            }
        });

        VariabiliStatichePlayer.getInstance().setImgNuovaImmagine(act.findViewById(R.id.imgNuovoSfondo));
        VariabiliStatichePlayer.getInstance().getImgNuovaImmagine().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Artista = VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("LooWebPlayer");
                builder.setMessage("Nome Artista");

                final EditText input = new EditText(context);
                input.setText(Artista);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sArtista = input.getText().toString();

                        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                        ws.ScaricaImmaginiArtista(sArtista);
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

        VariabiliStatichePlayer.getInstance().setImgModificaSfondo(act.findViewById(R.id.imgModificaSfondo));
        VariabiliStatichePlayer.getInstance().getImgModificaSfondo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliStatichePlayer.getInstance().isCeImmaginePerModifica()) {
                    return;
                }

                if (VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica() != null) {
                    StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica();

                    String Path = s.getPathImmagine();

                    VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("PLAYER");
                    VariabiliStaticheModificaImmagine.getInstance().setNomeImmagine(
                            Path
                    );
                    Intent i = new Intent(context, MainModificaImmagine.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });

        VariabiliStatichePlayer.getInstance().setImgCondividi(act.findViewById(R.id.imgCondividiSfondo));
        VariabiliStatichePlayer.getInstance().getImgCondividi().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliStatichePlayer.getInstance().isCeImmaginePerModifica()) {
                    return;
                }

                if (VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica() != null) {
                    StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica();

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(s.getPathImmagine());
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, s.getNomeImmagine());
                    // i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i,"Share immagine looWebPlayer"));
                }
            }
        });
    }

    private void impostazioniMascheraBraniOnLine() {
        VariabiliStatichePlayer.getInstance().setLstArtisti(act.findViewById(R.id.lstListaArtisti));
        VariabiliStatichePlayer.getInstance().setLstAlbum(act.findViewById(R.id.lstListaAlbum));
        VariabiliStatichePlayer.getInstance().setLstBrani(act.findViewById(R.id.lstListaBrani));

        EditText edtArtista = act.findViewById(R.id.edtFiltroArtisti);
        ImageView imgRicercaScelta = (ImageView) act.findViewById(R.id.imgFiltroArtisti);
        imgRicercaScelta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Artista = edtArtista.getText().toString();
                if (VariabiliStatichePlayer.getInstance().getCustomAdapterA() != null) {
                    VariabiliStatichePlayer.getInstance().getCustomAdapterA().updateData(Artista);
                }
            }
        });

        ImageView imgRefreshArtisti = (ImageView) act.findViewById(R.id.imgRefreshArtisti);
        imgRefreshArtisti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                ws.RitornaListaArtisti(true, false);

                VariabiliStatichePlayer.getInstance().getLstAlbum().setAdapter(null);
                VariabiliStatichePlayer.getInstance().getLstBrani().setAdapter(null);
            }
        });

        UtilityPlayer.getInstance().ScrivePreferitiTags();

        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
        ws.RitornaListaArtisti(false, false);
    }

    private void impostazioniMascheraRicercaBrano() {
        VariabiliStatichePlayer.getInstance().setLstRicerca(act.findViewById(R.id.lstListaBraniRicercati));
        EditText edtFiltro = act.findViewById(R.id.edtRicercaBrano);
        ImageView imgCerca = act.findViewById(R.id.imgRicercaBrano);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Filtro = edtFiltro.getText().toString();

                ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                ws.RicercaBrano(Filtro);
            }
        });
    }

    private void visualizzaImpostazioniMaschera(int quale) {
        LinearLayout layBraniLocali = act.findViewById(R.id.layBraniLocali);
        RelativeLayout layRicerche = act.findViewById(R.id.layRicerchePlayer);
        LinearLayout laySfondoPlayer = act.findViewById(R.id.laySfondoPlayer);
        LinearLayout layBraniOnLine = act.findViewById(R.id.layBraniOnLine);
        LinearLayout layBranoPlayer = act.findViewById(R.id.layBranoPlayer);
        LinearLayout layRicercaBrano = act.findViewById(R.id.layRicercaBranoPlayer);

        layBraniLocali.setVisibility(LinearLayout.GONE);
        layRicerche.setVisibility(LinearLayout.GONE);
        laySfondoPlayer.setVisibility(LinearLayout.GONE);
        layBraniOnLine.setVisibility(LinearLayout.GONE);
        layBranoPlayer.setVisibility(LinearLayout.GONE);
        layRicercaBrano.setVisibility(LinearLayout.GONE);

        switch(quale) {
            case 0:
                layRicerche.setVisibility(LinearLayout.VISIBLE);
                break;
            case 1:
                layBraniLocali.setVisibility(LinearLayout.VISIBLE);
                break;
            case 2:
                laySfondoPlayer.setVisibility(LinearLayout.VISIBLE);
                break;
            case 3:
                layBranoPlayer.setVisibility(LinearLayout.VISIBLE);
                break;
            case 4:
                layBraniOnLine.setVisibility(LinearLayout.VISIBLE);
                break;
            case 5:
                layRicercaBrano.setVisibility(LinearLayout.VISIBLE);
                break;
        }
    }

    private void CaricaStelle(Spinner spinner) {
        String[] lista = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        String Stelle = Integer.toString(VariabiliStatichePlayer.getInstance().getStelleDaRicercare());

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spinner,
                lista,
                Stelle
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, lista);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                lista
        );
        spinner.setAdapter(adapter);

        if (!Stelle.isEmpty()) {
            int spinnerPosition = adapter.getPosition(Stelle);
            spinner.setSelection(spinnerPosition);
        } */
    }

    private void CaricaSalvataggi(Spinner spinner) {
        db_dati_player db = new db_dati_player(context);
        List<StrutturaSalvataggi> lista = db.RitornaSalvataggi();
        VariabiliStatichePlayer.getInstance().setListaSalvataggi(lista);

        List<String> lista2 = new ArrayList<>();
        lista2.add(StringaNessuno);

        for (StrutturaSalvataggi l : lista) {
            lista2.add(l.getSalvataggio());
        };
        String[] ll = lista2.toArray(new String[0]);

        String id = db.RitornaSalvataggioDefault();
        String salvataggioDefault = "";
        for (StrutturaSalvataggi l : lista) {
            if (Integer.toString(l.getIdSalvataggio()).equals(id)) {
                salvataggioDefault = l.getSalvataggio();
                break;
            }
        };

        adapterSalvataggi = UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spinner,
                ll,
                salvataggioDefault
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, lista2);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                ll
        );
        adapterSalvataggi = adapter;
        spinner.setAdapter(adapter); */

        if (!salvataggioDefault.isEmpty()) {
            VariabiliStatichePlayer.getInstance().setSalvataggioSelezionato(salvataggioDefault);

            // int spinnerPosition = adapter.getPosition(salvataggioDefault);
            // spinner.setSelection(spinnerPosition);

            db.CaricaSalvataggio(id);
        } else {
            VariabiliStatichePlayer.getInstance().setSalvataggioSelezionato(StringaNessuno);

            // int spinnerPosition = adapter.getPosition(salvataggioDefault);
            // spinner.setSelection(spinnerPosition);
        }

        db.ChiudeDB();
    }
}
