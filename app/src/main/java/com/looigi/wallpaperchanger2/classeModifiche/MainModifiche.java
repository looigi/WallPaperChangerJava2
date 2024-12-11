package com.looigi.wallpaperchanger2.classeModifiche;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Moduli;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Sezioni;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePennetta.db_dati_pennetta;

import java.util.ArrayList;
import java.util.List;

public class MainModifiche extends Activity {
    private Context context;
    // private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_modifiche);

        context = this;
        // act = this;

        db_dati_modifiche db = new db_dati_modifiche(context);
        db.CreazioneTabelle();
        VariabiliStaticheModifiche.getInstance().setListaStati(db.RitornaStati());
        VariabiliStaticheModifiche.getInstance().setListaProgetti(db.RitornaProgetti());

        VariabiliStaticheModifiche.getInstance().setLayTipologia(findViewById(R.id.layTipologia));
        VariabiliStaticheModifiche.getInstance().setTxtTipologia(findViewById(R.id.txtTipologia));
        VariabiliStaticheModifiche.getInstance().setEdtTipologia(findViewById(R.id.edtTipologia));
        VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModifiche.getInstance().setLayStato(findViewById(R.id.layStato));
        VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModifiche.getInstance().setSwcSoloAperti(findViewById(R.id.switchSoloAperte));
        VariabiliStaticheModifiche.getInstance().getSwcSoloAperti().setChecked(true);
        ImageView imgSalvaTipologia = findViewById(R.id.imgSalvaTipologia);
        ImageView imgAnnullaTipologia = findViewById(R.id.imgAnnullaTipologia);
        ImageView imgAggiungeProgetto = findViewById(R.id.imgAggiungeProgetto);
        VariabiliStaticheModifiche.getInstance().setImgModificaProgetto(findViewById(R.id.imgModificaProgetto));
        VariabiliStaticheModifiche.getInstance().setImgEliminaProgetto(findViewById(R.id.imgEliminaProgetto));
        ImageView imgAggiungeModulo = findViewById(R.id.imgAggiungeModulo);
        VariabiliStaticheModifiche.getInstance().setImgModificaModulo(findViewById(R.id.imgModificaModulo));
        VariabiliStaticheModifiche.getInstance().setImgEliminaModulo(findViewById(R.id.imgEliminaModulo));
        ImageView imgAggiungeSezione = findViewById(R.id.imgAggiungeSezione);
        VariabiliStaticheModifiche.getInstance().setImgModificaSezioni(findViewById(R.id.imgModificaSezione));
        VariabiliStaticheModifiche.getInstance().setImgEliminaSezioni(findViewById(R.id.imgEliminaSezione));
        ImageView imgAggiungeModifica = findViewById(R.id.imgAggiungeModifica);
        VariabiliStaticheModifiche.getInstance().setSpnProgetto(findViewById(R.id.spnProgetto));
        VariabiliStaticheModifiche.getInstance().setSpnModulo(findViewById(R.id.spnModulo));
        VariabiliStaticheModifiche.getInstance().setSpnSezione(findViewById(R.id.spnSezione));
        VariabiliStaticheModifiche.getInstance().setTxtQuante(findViewById(R.id.txtQuante));
        ImageView imgCreaTesto = findViewById(R.id.imgCreaTesto);
        VariabiliStaticheModifiche.getInstance().getTxtQuante().setText("");

        imgCreaTesto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        VariabiliStaticheModifiche.getInstance().getSwcSoloAperti().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db_dati_modifiche db = new db_dati_modifiche(context);
                db.RitornaModifiche(
                        VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                        VariabiliStaticheModifiche.getInstance().getIdModulo(),
                        VariabiliStaticheModifiche.getInstance().getIdSezione()
                );

                VariabiliStaticheModifiche.getInstance().getTxtQuante().setText(
                        VariabiliStaticheModifiche.getInstance().PrendeNumeroModifiche(context)
                );
                db.ChiudeDB();

                AdapterListenerModifiche customAdapterT = new AdapterListenerModifiche(
                        context,
                        VariabiliStaticheModifiche.getInstance().getListaModifiche());
                VariabiliStaticheModifiche.getInstance().getLstModifiche().setAdapter(customAdapterT);
            }
        });

        if (!VariabiliStaticheModifiche.getInstance().getListaProgetti().isEmpty()) {
            VariabiliStaticheModifiche.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModifiche.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheModifiche.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModifiche.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.GONE);
        }
        db.LeggeUltimeSelezioni();

        if (!VariabiliStaticheModifiche.getInstance().getProgettoSelezionato().isEmpty()) {
            VariabiliStaticheModifiche.getInstance().setIdProgetto(
                VariabiliStaticheModifiche.getInstance().TornaIdProgetto(
                        VariabiliStaticheModifiche.getInstance().getListaProgetti(),
                        VariabiliStaticheModifiche.getInstance().getProgettoSelezionato()
                )
            );

            /* List<Moduli> listaModuli = VariabiliStaticheModifiche.getInstance().RicaricaModuli(context, db);
            VariabiliStaticheModifiche.getInstance().setListaModuli(
                    listaModuli
            ); */
        }

        /* if (!VariabiliStaticheModifiche.getInstance().getModuloSelezionato().isEmpty()) {
            VariabiliStaticheModifiche.getInstance().setIdModulo(
                    VariabiliStaticheModifiche.getInstance().TornaIdModulo(
                            VariabiliStaticheModifiche.getInstance().getListaModuli(),
                            VariabiliStaticheModifiche.getInstance().getModuloSelezionato()
                    )
            );

            List<Sezioni> listaSezioni = VariabiliStaticheModifiche.getInstance().RicaricaSezioni(context, db);
            VariabiliStaticheModifiche.getInstance().setListaSezioni(
                    listaSezioni
            );
        }

        if (!VariabiliStaticheModifiche.getInstance().getSezioneSelezionata().isEmpty()) {
            VariabiliStaticheModifiche.getInstance().setIdSezione(
                    VariabiliStaticheModifiche.getInstance().TornaIdSezione(
                            VariabiliStaticheModifiche.getInstance().getListaSezioni(),
                            VariabiliStaticheModifiche.getInstance().getSezioneSelezionata()
                    )
            );

            /* List<Sezioni> listaSezioni = VariabiliStaticheModifiche.getInstance().RicaricaSezioni(context, db);
            VariabiliStaticheModifiche.getInstance().setListaSezioni(
                    listaSezioni
            ); * /
        } */

        db.ChiudeDB();

        imgSalvaTipologia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().EffettuaSalvataggio(context);

                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.GONE);
            }
        });
        imgAnnullaTipologia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.GONE);
            }
        });

        imgAggiungeProgetto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().setTipologia("PROGETTO");
                VariabiliStaticheModifiche.getInstance().setOperazione("INSERT");

                VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModifiche.getInstance().getTxtTipologia().setText("Aggiunta progetto");
                VariabiliStaticheModifiche.getInstance().getEdtTipologia().setText("");
                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModifiche.getInstance().getImgModificaProgetto().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().setTipologia("PROGETTO");
                VariabiliStaticheModifiche.getInstance().setOperazione("UPDATE");

                String Cosa = VariabiliStaticheModifiche.getInstance().getProgettoSelezionato();
                VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModifiche.getInstance().getTxtTipologia().setText("Modifica progetto");
                VariabiliStaticheModifiche.getInstance().getEdtTipologia().setText(Cosa);
                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModifiche.getInstance().getImgEliminaProgetto().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare il progetto '" +
                        VariabiliStaticheModifiche.getInstance().getProgettoSelezionato() + "' ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheModifiche.getInstance().setTipologia("PROGETTO");
                        VariabiliStaticheModifiche.getInstance().setOperazione("DELETE");

                        VariabiliStaticheModifiche.getInstance().EffettuaSalvataggio(context);
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

        imgAggiungeModulo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().setTipologia("MODULO");
                VariabiliStaticheModifiche.getInstance().setOperazione("INSERT");

                VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModifiche.getInstance().getTxtTipologia().setText("Aggiunta modulo");
                VariabiliStaticheModifiche.getInstance().getEdtTipologia().setText("");
                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModifiche.getInstance().getImgModificaModulo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().setTipologia("MODULO");
                VariabiliStaticheModifiche.getInstance().setOperazione("UPDATE");

                String Cosa = VariabiliStaticheModifiche.getInstance().getModuloSelezionato();
                VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModifiche.getInstance().getTxtTipologia().setText("Modifica modulo");
                VariabiliStaticheModifiche.getInstance().getEdtTipologia().setText(Cosa);
                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModifiche.getInstance().getImgEliminaModulo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare il modulo " +
                        VariabiliStaticheModifiche.getInstance().getModuloSelezionato() + " ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheModifiche.getInstance().setTipologia("MODULO");
                        VariabiliStaticheModifiche.getInstance().setOperazione("DELETE");

                        VariabiliStaticheModifiche.getInstance().EffettuaSalvataggio(context);
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

        imgAggiungeSezione.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().setTipologia("SEZIONE");
                VariabiliStaticheModifiche.getInstance().setOperazione("INSERT");

                VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModifiche.getInstance().getTxtTipologia().setText("Aggiunta sezione");
                VariabiliStaticheModifiche.getInstance().getEdtTipologia().setText("");
                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModifiche.getInstance().getImgModificaSezioni().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().setTipologia("SEZIONE");
                VariabiliStaticheModifiche.getInstance().setOperazione("UPDATE");

                String Cosa = VariabiliStaticheModifiche.getInstance().getSezioneSelezionata();
                VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModifiche.getInstance().getTxtTipologia().setText("Modifica sezione");
                VariabiliStaticheModifiche.getInstance().getEdtTipologia().setText(Cosa);
                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModifiche.getInstance().getImgEliminaSezioni().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare la sezione '" +
                        VariabiliStaticheModifiche.getInstance().getSezioneSelezionata() + "' ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheModifiche.getInstance().setTipologia("SEZIONE");
                        VariabiliStaticheModifiche.getInstance().setOperazione("DELETE");

                        VariabiliStaticheModifiche.getInstance().EffettuaSalvataggio(context);
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

        imgAggiungeModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().setTipologia("MODIFICA");
                VariabiliStaticheModifiche.getInstance().setOperazione("INSERT");

                VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModifiche.getInstance().getTxtTipologia().setText("Aggiunta modifica");
                VariabiliStaticheModifiche.getInstance().getEdtTipologia().setText("");
                VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModifiche.getInstance().getSpnModulo().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModifiche.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

        imgAggiungeProgetto.setVisibility(LinearLayout.VISIBLE);
        VariabiliStaticheModifiche.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModifiche.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.GONE);

        imgAggiungeModulo.setVisibility(LinearLayout.GONE);
        VariabiliStaticheModifiche.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModifiche.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);

        imgAggiungeSezione.setVisibility(LinearLayout.GONE);
        VariabiliStaticheModifiche.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModifiche.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

        imgAggiungeModifica.setVisibility(LinearLayout.GONE);

        /* TextView txtTipoStato = findViewById(R.id.txtStato);
        VariabiliStaticheModifiche.getInstance().setEdtStato(findViewById(R.id.edtStato));
        LinearLayout layStato = findViewById(R.id.layStati);
        layStato.setVisibility(LinearLayout.GONE);

        ImageView imgAggiungeStato = findViewById(R.id.imgNuovoStato);
        imgAggiungeStato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtTipoStato.setText("Inserimento stato");

                VariabiliStaticheModifiche.getInstance().setTipologia("STATI");
                VariabiliStaticheModifiche.getInstance().setOperazione("INSERT");

                layStato.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgModificaStato = findViewById(R.id.imgModificaStato);
        imgModificaStato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtTipoStato.setText("Modifica stato");

                VariabiliStaticheModifiche.getInstance().setTipologia("STATI");
                VariabiliStaticheModifiche.getInstance().setOperazione("UPDATE");

                layStato.setVisibility(LinearLayout.VISIBLE);
            }
        });
        ImageView imgEliminaStato = findViewById(R.id.imgEliminaStato);
        imgEliminaStato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare lo stato '" +
                        VariabiliStaticheModifiche.getInstance().getStatoSelezionato() + "' ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheModifiche.getInstance().setTipologia("STATI");
                        VariabiliStaticheModifiche.getInstance().setOperazione("DELETE");
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

        imgAggiungeStato.setVisibility(LinearLayout.VISIBLE);
        if (VariabiliStaticheModifiche.getInstance().getListaStati().isEmpty()) {
            imgModificaStato.setVisibility(LinearLayout.GONE);
            imgEliminaStato.setVisibility(LinearLayout.GONE);
        } else {
            imgModificaStato.setVisibility(LinearLayout.VISIBLE);
            imgEliminaStato.setVisibility(LinearLayout.VISIBLE);
        }

        ImageView imgSalvaStato = findViewById(R.id.imgSalvaStato);
        imgSalvaStato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModifiche.getInstance().EffettuaSalvataggio(context);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        context,
                        R.layout.spinner_text,
                        VariabiliStaticheModifiche.getInstance().RitornaStringaStati(VariabiliStaticheModifiche.getInstance().getListaStati())
                );
                VariabiliStaticheModifiche.getInstance().getSpnStati().setAdapter(adapter);

                layStato.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaStato = findViewById(R.id.imgAnnullaStato);
        imgAnnullaStato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layStato.setVisibility(LinearLayout.GONE);
            }
        }); */

        VariabiliStaticheModifiche.getInstance().setSpnStati(findViewById(R.id.spnStato));
        ArrayAdapter<String> adapterS = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                VariabiliStaticheModifiche.getInstance().RitornaStringaStati(
                        VariabiliStaticheModifiche.getInstance().getListaStati()
                )
        );
        VariabiliStaticheModifiche.getInstance().getSpnStati().setAdapter(adapterS);
        VariabiliStaticheModifiche.getInstance().getSpnStati().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                VariabiliStaticheModifiche.getInstance().setStatoSelezionato(
                        (String) adapter.getItemAtPosition(pos).toString().trim()
                );

                /* VariabiliStaticheModifiche.getInstance().getEdtStato().setText(
                        VariabiliStaticheModifiche.getInstance().getStatoSelezionato()
                ); */

                VariabiliStaticheModifiche.getInstance().setIdStato(
                        VariabiliStaticheModifiche.getInstance().TornaIdStato(
                                VariabiliStaticheModifiche.getInstance().getListaStati(),
                                VariabiliStaticheModifiche.getInstance().getStatoSelezionato()
                        )
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheModifiche.getInstance().setLstModifiche(findViewById(R.id.lstModifiche));
        VariabiliStaticheModifiche.getInstance().getLstModifiche().setVisibility(LinearLayout.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                VariabiliStaticheModifiche.getInstance().RitornaStringaProgetti(
                        VariabiliStaticheModifiche.getInstance().getListaProgetti()
                )
        );
        VariabiliStaticheModifiche.getInstance().getSpnProgetto().setAdapter(adapter);
        VariabiliStaticheModifiche.getInstance().getSpnProgetto().setPrompt(
                VariabiliStaticheModifiche.getInstance().getProgettoSelezionato()
        );
        VariabiliStaticheModifiche.getInstance().getSpnProgetto().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                try {
                    if (adapter.getItemAtPosition(pos) == null) {
                        return;
                    }

                    VariabiliStaticheModifiche.getInstance().setProgettoSelezionato(
                            (String) adapter.getItemAtPosition(pos).toString().trim()
                    );

                    if (!VariabiliStaticheModifiche.getInstance().getProgettoSelezionato().isEmpty()) {
                        VariabiliStaticheModifiche.getInstance().setModuloSelezionato("");
                        VariabiliStaticheModifiche.getInstance().setSezioneSelezionata("");

                        VariabiliStaticheModifiche.getInstance().getTxtQuante().setText("");

                        VariabiliStaticheModifiche.getInstance().setListaModifiche(new ArrayList<>());
                        AdapterListenerModifiche customAdapterT = new AdapterListenerModifiche(
                                context,
                                VariabiliStaticheModifiche.getInstance().getListaModifiche());
                        VariabiliStaticheModifiche.getInstance().getLstModifiche().setAdapter(customAdapterT);


                        VariabiliStaticheModifiche.getInstance().setIdProgetto(
                                VariabiliStaticheModifiche.getInstance().TornaIdProgetto(
                                        VariabiliStaticheModifiche.getInstance().getListaProgetti(),
                                        VariabiliStaticheModifiche.getInstance().getProgettoSelezionato()
                                )
                        );

                        db_dati_modifiche db = new db_dati_modifiche(context);
                        List<Moduli> listaModuli = VariabiliStaticheModifiche.getInstance().RicaricaModuli(context, db);
                        VariabiliStaticheModifiche.getInstance().setListaModuli(
                                listaModuli
                        );
                        db.ModificaUltimeSelezioni();
                        db.ChiudeDB();

                        VariabiliStaticheModifiche.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheModifiche.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);

                        imgAggiungeModulo.setVisibility(LinearLayout.VISIBLE);

                        if (!VariabiliStaticheModifiche.getInstance().getListaModuli().isEmpty()) {
                            VariabiliStaticheModifiche.getInstance().getImgModificaModulo().setVisibility(LinearLayout.VISIBLE);
                            VariabiliStaticheModifiche.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.VISIBLE);
                        } else {
                            VariabiliStaticheModifiche.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
                            VariabiliStaticheModifiche.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);
                        }

                        VariabiliStaticheModifiche.getInstance().getSpnModulo().setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheModifiche.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        imgAggiungeSezione.setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModifiche.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModifiche.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);
                    } else {
                        VariabiliStaticheModifiche.getInstance().getSpnModulo().setVisibility(LinearLayout.GONE);

                        imgAggiungeModulo.setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModifiche.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModifiche.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModifiche.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        imgAggiungeSezione.setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModifiche.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModifiche.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);
                    }
                } catch (Exception e) {
                    VariabiliStaticheModifiche.getInstance().setProgettoSelezionato("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheModifiche.getInstance().getSpnModulo().setPrompt(
                VariabiliStaticheModifiche.getInstance().getModuloSelezionato()
        );
        VariabiliStaticheModifiche.getInstance().getSpnModulo().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (adapter.getItemAtPosition(pos) == null) {
                    return;
                }

                try {
                    VariabiliStaticheModifiche.getInstance().setModuloSelezionato(
                            (String) adapter.getItemAtPosition(pos).toString().trim()
                    );

                    if (!VariabiliStaticheModifiche.getInstance().getModuloSelezionato().isEmpty()) {
                        VariabiliStaticheModifiche.getInstance().setSezioneSelezionata("");

                        VariabiliStaticheModifiche.getInstance().getTxtQuante().setText("");

                        VariabiliStaticheModifiche.getInstance().setListaModifiche(new ArrayList<>());
                        AdapterListenerModifiche customAdapterT = new AdapterListenerModifiche(
                                context,
                                VariabiliStaticheModifiche.getInstance().getListaModifiche());
                        VariabiliStaticheModifiche.getInstance().getLstModifiche().setAdapter(customAdapterT);

                        VariabiliStaticheModifiche.getInstance().setIdModulo(
                                VariabiliStaticheModifiche.getInstance().TornaIdModulo(
                                        VariabiliStaticheModifiche.getInstance().getListaModuli(),
                                        VariabiliStaticheModifiche.getInstance().getModuloSelezionato()
                                )
                        );

                        db_dati_modifiche db = new db_dati_modifiche(context);
                        List<Sezioni> listaSezioni = VariabiliStaticheModifiche.getInstance().RicaricaSezioni(context, db);
                        VariabiliStaticheModifiche.getInstance().setListaSezioni(
                                listaSezioni
                        );
                        db.ModificaUltimeSelezioni();
                        db.ChiudeDB();

                        VariabiliStaticheModifiche.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheModifiche.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheModifiche.getInstance().getSpnSezione().setVisibility(LinearLayout.VISIBLE);

                        imgAggiungeSezione.setVisibility(LinearLayout.VISIBLE);

                        if (!VariabiliStaticheModifiche.getInstance().getListaSezioni().isEmpty()) {
                            VariabiliStaticheModifiche.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.VISIBLE);
                            VariabiliStaticheModifiche.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.VISIBLE);
                        }

                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);
                    } else {
                        VariabiliStaticheModifiche.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        imgAggiungeSezione.setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModifiche.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModifiche.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);
                    }
                } catch (Exception e) {
                    VariabiliStaticheModifiche.getInstance().setModuloSelezionato("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheModifiche.getInstance().getSpnSezione().setPrompt(
                VariabiliStaticheModifiche.getInstance().getSezioneSelezionata()
        );
        VariabiliStaticheModifiche.getInstance().getSpnSezione().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                try {
                    if (adapter.getItemAtPosition(pos) == null) {
                        return;
                    }

                    VariabiliStaticheModifiche.getInstance().setSezioneSelezionata(
                            (String) adapter.getItemAtPosition(pos).toString().trim()
                    );

                    if (!VariabiliStaticheModifiche.getInstance().getSezioneSelezionata().isEmpty()) {
                        VariabiliStaticheModifiche.getInstance().setIdSezione(
                                VariabiliStaticheModifiche.getInstance().TornaIdSezione(
                                        VariabiliStaticheModifiche.getInstance().getListaSezioni(),
                                        VariabiliStaticheModifiche.getInstance().getSezioneSelezionata()
                                )
                        );

                        db_dati_modifiche db = new db_dati_modifiche(context);
                        VariabiliStaticheModifiche.getInstance().setListaModifiche(
                                db.RitornaModifiche(
                                        VariabiliStaticheModifiche.getInstance().getIdProgetto(),
                                        VariabiliStaticheModifiche.getInstance().getIdModulo(),
                                        VariabiliStaticheModifiche.getInstance().getIdSezione()
                                )
                        );

                        VariabiliStaticheModifiche.getInstance().getTxtQuante().setText(
                                VariabiliStaticheModifiche.getInstance().PrendeNumeroModifiche(context)
                        );
                        db.ChiudeDB();

                        AdapterListenerModifiche customAdapterT = new AdapterListenerModifiche(
                                context,
                                VariabiliStaticheModifiche.getInstance().getListaModifiche());
                        VariabiliStaticheModifiche.getInstance().getLstModifiche().setAdapter(customAdapterT);

                        imgAggiungeModifica.setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheModifiche.getInstance().getLstModifiche().setVisibility(LinearLayout.VISIBLE);
                    } else {
                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModifiche.getInstance().getLstModifiche().setVisibility(LinearLayout.GONE);
                    }
                } catch (Exception e) {
                    VariabiliStaticheModifiche.getInstance().setSezioneSelezionata("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }
}
