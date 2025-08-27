package com.looigi.wallpaperchanger2.classePlayer.preferiti_tags;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGps.db_dati_gps;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaTags;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class Main_Preferiti_Tags extends Activity {
    private Activity act;
    private Context context;
    private String InserimentoModifica;
    private boolean tagsBrano;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_preferiti_tags);

        context = this;
        act = this;

        Intent intent = getIntent();
        String id = intent.getStringExtra("DO");

        VariabiliStatichePrefTags.getInstance().setTipoOperazione(id);

        boolean preferiti = false;
        tagsBrano = false;

        VariabiliStatichePrefTags.getInstance().setTxtTagSelezionato(findViewById(R.id.txtTagSelezionato));
        VariabiliStatichePrefTags.getInstance().getTxtTagSelezionato().setText("");

        switch (id) {
            case "Preferiti":
                VariabiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        VariabiliStatichePlayer.getInstance().getPreferiti()
                );
                preferiti = true;
                break;
            case "PreferitiElimina":
                VariabiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        VariabiliStatichePlayer.getInstance().getPreferitiElimina()
                );
                preferiti = true;
                break;
            case "Tags":
                VariabiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        VariabiliStatichePlayer.getInstance().getPreferitiTags()
                );
                break;
            case "TagsElimina":
                VariabiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        VariabiliStatichePlayer.getInstance().getPreferitiEliminaTags()
                );
                break;
            case "TagsBrano":
                tagsBrano = true;
                String Cosa = VariabiliStatichePlayer.getInstance().getUltimoBrano().getTags() + ";";
                Cosa = Cosa.replace(";;", ";");

                VariabiliStatichePrefTags.getInstance().setStringaDiConfronto(
                        Cosa
                );
                VariabiliStatichePrefTags.getInstance().setTagsBrano(
                        Cosa
                );
                break;
        }

        VariabiliStatichePrefTags.getInstance().setLstArtisti(findViewById(R.id.lstTagPreferiti));
        VariabiliStatichePrefTags.getInstance().setTxtQuanti(findViewById(R.id.txtQuantiArtisti));
        VariabiliStatichePrefTags.getInstance().setTxtSelezionati(findViewById(R.id.txtSelezionati));

        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
        if (preferiti) {
            ws.RitornaListaArtisti(false, true);

            if (!VariabiliStatichePlayer.getInstance().getListaArtisti().isEmpty()) {
                AdapterListenerPreferiti customAdapterA = new AdapterListenerPreferiti(context,
                        VariabiliStatichePlayer.getInstance().getListaArtisti());
                VariabiliStatichePrefTags.getInstance().getLstArtisti().setAdapter(customAdapterA);
                VariabiliStatichePrefTags.getInstance().setCustomAdapterPref(customAdapterA);
            }
        } else {
            ws.RitornaListaTags(false);
        }

        EditText edtFiltro = findViewById(R.id.edtFiltroArtisti);
        ImageView imgCerca = findViewById(R.id.imgFiltroArtisti);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Filtro = edtFiltro.getText().toString();

                if (VariabiliStatichePrefTags.getInstance().getCustomAdapterPref() != null) {
                    VariabiliStatichePrefTags.getInstance().getCustomAdapterPref().updateData(Filtro);
                }
            }
        });
        ImageView imgRefresh = findViewById(R.id.imgRefreshArtisti);
        boolean finalPreferiti = preferiti;
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (finalPreferiti) {
                    ws.RitornaListaArtisti(false, true);
                } else {
                    ws.RitornaListaTags(false);
                }
            }
        });

        SwitchCompat sSoloSel = act.findViewById(R.id.sSoloSelezionati);
        sSoloSel.setChecked(false);
        sSoloSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStatichePrefTags.getInstance().setSoloSelezionati(sSoloSel.isChecked());

                String Filtro = edtFiltro.getText().toString();

                if (finalPreferiti) {
                    if (VariabiliStatichePrefTags.getInstance().getCustomAdapterPref() != null) {
                        VariabiliStatichePrefTags.getInstance().getCustomAdapterPref().updateData(Filtro);
                    }
                } else {
                    if (VariabiliStatichePrefTags.getInstance().getCustomAdapterTag() != null) {
                        VariabiliStatichePrefTags.getInstance().getCustomAdapterTag().updateData(Filtro);
                    }
                }
            }
        });

        InserimentoModifica = "";

        VariabiliStatichePrefTags.getInstance().setLayTag(findViewById(R.id.layTag));
        VariabiliStatichePrefTags.getInstance().getLayTag().setVisibility(LinearLayout.GONE);
        TextView txtTitoloMaschera = findViewById(R.id.txtTitoloMaschera);
        txtTitoloMaschera.setText("");
        EditText edtTag = findViewById(R.id.edtTag);
        ImageView imgSalvaTag = findViewById(R.id.imgSalvaTag);
        imgSalvaTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cosa = edtTag.getText().toString();

                if (Cosa.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Inserire un valore");
                    return;
                }

                ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);

                if (InserimentoModifica.equals("INSERIMENTO")) {
                    ws.SalvaTag(Cosa);
                } else {
                    ws.ModificaTag(VariabiliStatichePrefTags.getInstance().getIdTagSelezionato(), Cosa);
                }
            }
        });

        ImageView imgAnnullaTag = findViewById(R.id.imgAnnullaTag);
        imgAnnullaTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStatichePrefTags.getInstance().getLayTag().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAggiungeTag = findViewById(R.id.imgAggiungeTag);
        imgAggiungeTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTitoloMaschera.setText("Inserimento nuovo Tag");
                edtTag.setText("");
                VariabiliStatichePrefTags.getInstance().getLayTag().setVisibility(LinearLayout.VISIBLE);
                InserimentoModifica = "INSERIMENTO";
            }
        });

        ImageView imgModificaTag = findViewById(R.id.imgModificaTag);
        imgModificaTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cosa = VariabiliStatichePrefTags.getInstance().getTxtTagSelezionato().getText().toString();
                if (Cosa.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Selezionare una voce dalla lista");
                    return;
                }

                txtTitoloMaschera.setText("Modifica Tag");
                edtTag.setText(Cosa);
                VariabiliStatichePrefTags.getInstance().getLayTag().setVisibility(LinearLayout.VISIBLE);
                InserimentoModifica = "MODIFICA";
            }
        });

        ImageView imgEliminaTag = findViewById(R.id.imgEliminaTag);
        imgEliminaTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cosa = VariabiliStatichePrefTags.getInstance().getTxtTagSelezionato().getText().toString();
                if (Cosa.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Selezionare una voce dalla lista");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("LooWebPlayer");
                builder.setTitle("Si vuole eliminare il tag '" + Cosa + "' ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                        ws.EliminaTag(VariabiliStatichePrefTags.getInstance().getIdTagSelezionato());
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

        if (preferiti || tagsBrano) {
            imgAggiungeTag.setVisibility(LinearLayout.GONE);
            imgModificaTag.setVisibility(LinearLayout.GONE);
            imgEliminaTag.setVisibility(LinearLayout.GONE);
        } else {
            imgAggiungeTag.setVisibility(LinearLayout.VISIBLE);
            imgModificaTag.setVisibility(LinearLayout.VISIBLE);
            imgEliminaTag.setVisibility(LinearLayout.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!tagsBrano) {
            db_dati_gps db = new db_dati_gps(context);
            db.ScriveImpostazioni();
            db.ChiudeDB();

            UtilityPlayer.getInstance().ScrivePreferitiTags();
        } else {
            VariabiliStatichePlayer.getInstance().getUltimoBrano().setTags(
                    VariabiliStatichePrefTags.getInstance().getTagsBrano()
            );
            VariabiliStatichePrefTags.getInstance().getTxtTagsPerBrano().setText(
                    VariabiliStatichePrefTags.getInstance().getTagsBrano()
            );

            String idTags = "";
            String[] Tags = VariabiliStatichePrefTags.getInstance().getTagsBrano().split(";");
            for (String t : Tags) {
                if (!t.isEmpty()) {
                    for (StrutturaTags st : VariabiliStatichePlayer.getInstance().getListaTags()) {
                        if (t.equals(st.getTag())) {
                            idTags += st.getIdTag() + ";";
                            break;
                        }
                    }
                }
            }

            ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
            ws.SalvaTagsBrano(Integer.toString(VariabiliStatichePlayer.getInstance().getUltimoBrano().getIdBrano()), idTags);
        }
    }
}
