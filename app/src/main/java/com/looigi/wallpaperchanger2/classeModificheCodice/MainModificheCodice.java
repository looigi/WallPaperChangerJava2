package com.looigi.wallpaperchanger2.classeModificheCodice;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Modifiche;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Moduli;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Sezioni;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainModificheCodice extends Activity {
    private Context context;
    // private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_modifiche);

        context = this;
        // act = this;

        db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
        db.CreazioneTabelle();
        VariabiliStaticheModificheCodice.getInstance().setListaStati(db.RitornaStati());
        VariabiliStaticheModificheCodice.getInstance().setListaProgetti(db.RitornaProgetti());

        VariabiliStaticheModificheCodice.getInstance().setLayTipologia(findViewById(R.id.layTipologia));
        VariabiliStaticheModificheCodice.getInstance().setTxtTipologia(findViewById(R.id.txtTipologia));
        VariabiliStaticheModificheCodice.getInstance().setEdtTipologia(findViewById(R.id.edtTipologia));
        VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModificheCodice.getInstance().setLayStato(findViewById(R.id.layStato));
        VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModificheCodice.getInstance().setSwcSoloAperti(findViewById(R.id.switchSoloAperte));
        VariabiliStaticheModificheCodice.getInstance().getSwcSoloAperti().setChecked(true);
        ImageView imgSalvaTipologia = findViewById(R.id.imgSalvaTipologia);
        ImageView imgAnnullaTipologia = findViewById(R.id.imgAnnullaTipologia);
        ImageView imgAggiungeProgetto = findViewById(R.id.imgAggiungeProgetto);
        VariabiliStaticheModificheCodice.getInstance().setImgModificaProgetto(findViewById(R.id.imgModificaProgetto));
        VariabiliStaticheModificheCodice.getInstance().setImgEliminaProgetto(findViewById(R.id.imgEliminaProgetto));
        ImageView imgAggiungeModulo = findViewById(R.id.imgAggiungeModulo);
        VariabiliStaticheModificheCodice.getInstance().setImgModificaModulo(findViewById(R.id.imgModificaModulo));
        VariabiliStaticheModificheCodice.getInstance().setImgEliminaModulo(findViewById(R.id.imgEliminaModulo));
        ImageView imgAggiungeSezione = findViewById(R.id.imgAggiungeSezione);
        VariabiliStaticheModificheCodice.getInstance().setImgModificaSezioni(findViewById(R.id.imgModificaSezione));
        VariabiliStaticheModificheCodice.getInstance().setImgEliminaSezioni(findViewById(R.id.imgEliminaSezione));
        ImageView imgAggiungeModifica = findViewById(R.id.imgAggiungeModifica);
        VariabiliStaticheModificheCodice.getInstance().setSpnProgetto(findViewById(R.id.spnProgetto));
        VariabiliStaticheModificheCodice.getInstance().setSpnModulo(findViewById(R.id.spnModulo));
        VariabiliStaticheModificheCodice.getInstance().setSpnSezione(findViewById(R.id.spnSezione));
        VariabiliStaticheModificheCodice.getInstance().setTxtQuante(findViewById(R.id.txtQuante));
        ImageView imgCreaTesto = findViewById(R.id.imgCreaTesto);
        VariabiliStaticheModificheCodice.getInstance().getTxtQuante().setText("");

        imgCreaTesto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);

                List<Modifiche> listaModificaIniziale = VariabiliStaticheModificheCodice.getInstance().getListaModifiche();
                boolean check = VariabiliStaticheModificheCodice.getInstance().getSwcSoloAperti().isChecked();
                VariabiliStaticheModificheCodice.getInstance().getSwcSoloAperti().setChecked(false);

                String Testo = "Modifiche '" + VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato().toUpperCase().trim() + "'";
                Testo += "\n----------------------------------------------------------------";
                for (Moduli m : VariabiliStaticheModificheCodice.getInstance().getListaModuli()) {
                    Testo += "\n     Modulo '" + m.getModulo().toUpperCase().trim() + "':\n";
                    int idModulo = VariabiliStaticheModificheCodice.getInstance().TornaIdModulo(
                            VariabiliStaticheModificheCodice.getInstance().getListaModuli(),
                            m.getModulo()
                    );
                    List<Sezioni> listaSezioni = db.RitornaSezioni(VariabiliStaticheModificheCodice.getInstance().getIdProgetto(), idModulo);
                    for (Sezioni s : listaSezioni) {
                        Testo += "\n         Sezione '" + s.getSezione().toUpperCase().trim() + "':";
                        int idSezione = VariabiliStaticheModificheCodice.getInstance().TornaIdSezione(
                                VariabiliStaticheModificheCodice.getInstance().getListaSezioni(),
                                s.getSezione()
                        );
                        List<Modifiche> listaModifiche = db.RitornaModifiche(
                                VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                idModulo,
                                idSezione
                        );
                        for (Modifiche modif : listaModifiche) {
                            int idStato = modif.getIdStato();
                            String Stato = VariabiliStaticheModificheCodice.getInstance().RitornaStringaStato(idStato);

                            Testo += "\n             " + modif.getModifica() + " (" + Stato + ")";
                        }
                    }
                }
                Testo += "\n----------------------------------------------------------------";

                String Path = context.getFilesDir() + "/Appoggio";
                Files.getInstance().CreaCartelle(Path);
                String NomeFile = "Modifiche_" + VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato() + ".txt";
                Files.getInstance().EliminaFileUnico(Path + "/" + NomeFile);
                Files.getInstance().ScriveFile(Path, NomeFile, Testo);

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                File f = new File(Path + "/" + NomeFile);
                Uri uri = FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider",
                        f);

                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, NomeFile);
                i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                i.putExtra(Intent.EXTRA_STREAM,uri);
                i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                context.startActivity(Intent.createChooser(i,"Share file modifiche"));

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Files.getInstance().EliminaFile(Path, NomeFile);
                    }
                }, 30000);

                VariabiliStaticheModificheCodice.getInstance().getSwcSoloAperti().setChecked(check);
                VariabiliStaticheModificheCodice.getInstance().setListaModifiche(listaModificaIniziale);
            }
        });

        VariabiliStaticheModificheCodice.getInstance().getSwcSoloAperti().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
                db.RitornaModifiche(
                        VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                        VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                        VariabiliStaticheModificheCodice.getInstance().getIdSezione()
                );

                VariabiliStaticheModificheCodice.getInstance().getTxtQuante().setText(
                        VariabiliStaticheModificheCodice.getInstance().PrendeNumeroModifiche(context)
                );
                db.ChiudeDB();

                AdapterListenerModificheCodice customAdapterT = new AdapterListenerModificheCodice(
                        context,
                        VariabiliStaticheModificheCodice.getInstance().getListaModifiche());
                VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setAdapter(customAdapterT);
            }
        });

        if (!VariabiliStaticheModificheCodice.getInstance().getListaProgetti().isEmpty()) {
            VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.GONE);
        }
        db.LeggeUltimeSelezioni();

        if (!VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato().isEmpty()) {
            VariabiliStaticheModificheCodice.getInstance().setIdProgetto(
                VariabiliStaticheModificheCodice.getInstance().TornaIdProgetto(
                        VariabiliStaticheModificheCodice.getInstance().getListaProgetti(),
                        VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato()
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
                VariabiliStaticheModificheCodice.getInstance().EffettuaSalvataggio(context);

                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.GONE);
            }
        });
        imgAnnullaTipologia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.GONE);
            }
        });

        imgAggiungeProgetto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModificheCodice.getInstance().setTipologia("PROGETTO");
                VariabiliStaticheModificheCodice.getInstance().setOperazione("INSERT");

                VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getTxtTipologia().setText("Aggiunta progetto");
                VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().setText("");
                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModificheCodice.getInstance().setTipologia("PROGETTO");
                VariabiliStaticheModificheCodice.getInstance().setOperazione("UPDATE");

                String Cosa = VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato();
                VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getTxtTipologia().setText("Modifica progetto");
                VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().setText(Cosa);
                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare il progetto '" +
                        VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato() + "' ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheModificheCodice.getInstance().setTipologia("PROGETTO");
                        VariabiliStaticheModificheCodice.getInstance().setOperazione("DELETE");

                        VariabiliStaticheModificheCodice.getInstance().EffettuaSalvataggio(context);
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
                VariabiliStaticheModificheCodice.getInstance().setTipologia("MODULO");
                VariabiliStaticheModificheCodice.getInstance().setOperazione("INSERT");

                VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getTxtTipologia().setText("Aggiunta modulo");
                VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().setText("");
                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModificheCodice.getInstance().setTipologia("MODULO");
                VariabiliStaticheModificheCodice.getInstance().setOperazione("UPDATE");

                String Cosa = VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato();
                VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getTxtTipologia().setText("Modifica modulo");
                VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().setText(Cosa);
                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare il modulo " +
                        VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato() + " ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheModificheCodice.getInstance().setTipologia("MODULO");
                        VariabiliStaticheModificheCodice.getInstance().setOperazione("DELETE");

                        VariabiliStaticheModificheCodice.getInstance().EffettuaSalvataggio(context);
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
                VariabiliStaticheModificheCodice.getInstance().setTipologia("SEZIONE");
                VariabiliStaticheModificheCodice.getInstance().setOperazione("INSERT");

                VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getTxtTipologia().setText("Aggiunta sezione");
                VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().setText("");
                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModificheCodice.getInstance().setTipologia("SEZIONE");
                VariabiliStaticheModificheCodice.getInstance().setOperazione("UPDATE");

                String Cosa = VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata();
                VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getTxtTipologia().setText("Modifica sezione");
                VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().setText(Cosa);
                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare la sezione '" +
                        VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata() + "' ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheModificheCodice.getInstance().setTipologia("SEZIONE");
                        VariabiliStaticheModificheCodice.getInstance().setOperazione("DELETE");

                        VariabiliStaticheModificheCodice.getInstance().EffettuaSalvataggio(context);
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
                VariabiliStaticheModificheCodice.getInstance().setTipologia("MODIFICA");
                VariabiliStaticheModificheCodice.getInstance().setOperazione("INSERT");

                VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getTxtTipologia().setText("Aggiunta modifica");
                VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().setText("");
                VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
            }
        });

        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

        imgAggiungeProgetto.setVisibility(LinearLayout.VISIBLE);
        VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.GONE);

        imgAggiungeModulo.setVisibility(LinearLayout.GONE);
        VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);

        imgAggiungeSezione.setVisibility(LinearLayout.GONE);
        VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
        VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

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

        VariabiliStaticheModificheCodice.getInstance().setSpnStati(findViewById(R.id.spnStato));
        ArrayAdapter<String> adapterS = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaStati(
                        VariabiliStaticheModificheCodice.getInstance().getListaStati()
                )
        );
        VariabiliStaticheModificheCodice.getInstance().getSpnStati().setAdapter(adapterS);
        VariabiliStaticheModificheCodice.getInstance().getSpnStati().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                VariabiliStaticheModificheCodice.getInstance().setStatoSelezionato(
                        (String) adapter.getItemAtPosition(pos).toString().trim()
                );

                /* VariabiliStaticheModifiche.getInstance().getEdtStato().setText(
                        VariabiliStaticheModifiche.getInstance().getStatoSelezionato()
                ); */

                VariabiliStaticheModificheCodice.getInstance().setIdStato(
                        VariabiliStaticheModificheCodice.getInstance().TornaIdStato(
                                VariabiliStaticheModificheCodice.getInstance().getListaStati(),
                                VariabiliStaticheModificheCodice.getInstance().getStatoSelezionato()
                        )
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheModificheCodice.getInstance().setLstModifiche(findViewById(R.id.lstModifiche));
        VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setVisibility(LinearLayout.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaProgetti(
                        VariabiliStaticheModificheCodice.getInstance().getListaProgetti()
                )
        );
        VariabiliStaticheModificheCodice.getInstance().getSpnProgetto().setAdapter(adapter);
        VariabiliStaticheModificheCodice.getInstance().getSpnProgetto().setPrompt(
                VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato()
        );
        VariabiliStaticheModificheCodice.getInstance().getSpnProgetto().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                try {
                    if (adapter.getItemAtPosition(pos) == null) {
                        return;
                    }

                    VariabiliStaticheModificheCodice.getInstance().setProgettoSelezionato(
                            (String) adapter.getItemAtPosition(pos).toString().trim()
                    );

                    if (!VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato().isEmpty()) {
                        VariabiliStaticheModificheCodice.getInstance().setModuloSelezionato("");
                        VariabiliStaticheModificheCodice.getInstance().setSezioneSelezionata("");

                        VariabiliStaticheModificheCodice.getInstance().getTxtQuante().setText("");

                        VariabiliStaticheModificheCodice.getInstance().setListaModifiche(new ArrayList<>());
                        AdapterListenerModificheCodice customAdapterT = new AdapterListenerModificheCodice(
                                context,
                                VariabiliStaticheModificheCodice.getInstance().getListaModifiche());
                        VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setAdapter(customAdapterT);


                        VariabiliStaticheModificheCodice.getInstance().setIdProgetto(
                                VariabiliStaticheModificheCodice.getInstance().TornaIdProgetto(
                                        VariabiliStaticheModificheCodice.getInstance().getListaProgetti(),
                                        VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato()
                                )
                        );

                        db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
                        List<Moduli> listaModuli = VariabiliStaticheModificheCodice.getInstance().RicaricaModuli(context, db);
                        VariabiliStaticheModificheCodice.getInstance().setListaModuli(
                                listaModuli
                        );
                        db.ModificaUltimeSelezioni();
                        db.ChiudeDB();

                        VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);

                        imgAggiungeModulo.setVisibility(LinearLayout.VISIBLE);

                        if (!VariabiliStaticheModificheCodice.getInstance().getListaModuli().isEmpty()) {
                            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.VISIBLE);
                            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.VISIBLE);
                        } else {
                            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
                            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);
                        }

                        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        imgAggiungeSezione.setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);
                    } else {
                        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.GONE);

                        imgAggiungeModulo.setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        imgAggiungeSezione.setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);
                    }
                } catch (Exception e) {
                    VariabiliStaticheModificheCodice.getInstance().setProgettoSelezionato("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setPrompt(
                VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato()
        );
        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (adapter.getItemAtPosition(pos) == null) {
                    return;
                }

                try {
                    VariabiliStaticheModificheCodice.getInstance().setModuloSelezionato(
                            (String) adapter.getItemAtPosition(pos).toString().trim()
                    );

                    if (!VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato().isEmpty()) {
                        VariabiliStaticheModificheCodice.getInstance().setSezioneSelezionata("");

                        VariabiliStaticheModificheCodice.getInstance().getTxtQuante().setText("");

                        VariabiliStaticheModificheCodice.getInstance().setListaModifiche(new ArrayList<>());
                        AdapterListenerModificheCodice customAdapterT = new AdapterListenerModificheCodice(
                                context,
                                VariabiliStaticheModificheCodice.getInstance().getListaModifiche());
                        VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setAdapter(customAdapterT);

                        VariabiliStaticheModificheCodice.getInstance().setIdModulo(
                                VariabiliStaticheModificheCodice.getInstance().TornaIdModulo(
                                        VariabiliStaticheModificheCodice.getInstance().getListaModuli(),
                                        VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato()
                                )
                        );

                        db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
                        List<Sezioni> listaSezioni = VariabiliStaticheModificheCodice.getInstance().RicaricaSezioni(context, db);
                        VariabiliStaticheModificheCodice.getInstance().setListaSezioni(
                                listaSezioni
                        );
                        db.ModificaUltimeSelezioni();
                        db.ChiudeDB();

                        VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.VISIBLE);

                        imgAggiungeSezione.setVisibility(LinearLayout.VISIBLE);

                        if (!VariabiliStaticheModificheCodice.getInstance().getListaSezioni().isEmpty()) {
                            VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.VISIBLE);
                            VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.VISIBLE);
                        }

                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);
                    } else {
                        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        imgAggiungeSezione.setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);
                    }
                } catch (Exception e) {
                    VariabiliStaticheModificheCodice.getInstance().setModuloSelezionato("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setPrompt(
                VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata()
        );
        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                try {
                    if (adapter.getItemAtPosition(pos) == null) {
                        return;
                    }

                    VariabiliStaticheModificheCodice.getInstance().setSezioneSelezionata(
                            (String) adapter.getItemAtPosition(pos).toString().trim()
                    );

                    if (!VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata().isEmpty()) {
                        VariabiliStaticheModificheCodice.getInstance().setIdSezione(
                                VariabiliStaticheModificheCodice.getInstance().TornaIdSezione(
                                        VariabiliStaticheModificheCodice.getInstance().getListaSezioni(),
                                        VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata()
                                )
                        );

                        db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
                        VariabiliStaticheModificheCodice.getInstance().setListaModifiche(
                                db.RitornaModifiche(
                                        VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                        VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                        VariabiliStaticheModificheCodice.getInstance().getIdSezione()
                                )
                        );

                        VariabiliStaticheModificheCodice.getInstance().getTxtQuante().setText(
                                VariabiliStaticheModificheCodice.getInstance().PrendeNumeroModifiche(context)
                        );
                        db.ChiudeDB();

                        AdapterListenerModificheCodice customAdapterT = new AdapterListenerModificheCodice(
                                context,
                                VariabiliStaticheModificheCodice.getInstance().getListaModifiche());
                        VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setAdapter(customAdapterT);

                        imgAggiungeModifica.setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setVisibility(LinearLayout.VISIBLE);
                    } else {
                        imgAggiungeModifica.setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setVisibility(LinearLayout.GONE);
                    }
                } catch (Exception e) {
                    VariabiliStaticheModificheCodice.getInstance().setSezioneSelezionata("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

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
