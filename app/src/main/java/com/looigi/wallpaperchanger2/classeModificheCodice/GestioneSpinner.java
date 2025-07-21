package com.looigi.wallpaperchanger2.classeModificheCodice;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Moduli;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Sezioni;
import com.looigi.wallpaperchanger2.classeModificheCodice.adapters.AdapterListenerModificheCodice;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class GestioneSpinner {
    private static GestioneSpinner instance = null;

    private GestioneSpinner() {
    }

    public static GestioneSpinner getInstance() {
        if (instance == null) {
            instance = new GestioneSpinner();
        }

        return instance;
    }

    public void GestioneSpinnerStati(Context context) {
        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheModificheCodice.getInstance().getSpnStati(),
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaStati(
                        VariabiliStaticheModificheCodice.getInstance().getListaStati()
                ),
                VariabiliStaticheModificheCodice.getInstance().getStatoSelezionato()
        );

        VariabiliStaticheModificheCodice.getInstance().getSpnStati().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (adapter.getItemAtPosition(pos) == null) {
                    return;
                }
                if (!VariabiliStaticheModificheCodice.getInstance().isEseguitaLetturaIniziale()) {
                    return;
                }

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
    }

    public void GestioneSpinnerProgetti(Context context) {
        VariabiliStaticheModificheCodice.getInstance().setAdapterProgetti(UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheModificheCodice.getInstance().getSpnProgetto(),
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaProgetti(
                        VariabiliStaticheModificheCodice.getInstance().getListaProgetti()
                ),
                VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato()
        ));

        /* VariabiliStaticheModificheCodice.getInstance().setAdapterProgetti(new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaProgetti(
                        VariabiliStaticheModificheCodice.getInstance().getListaProgetti()
                )
        ));
        VariabiliStaticheModificheCodice.getInstance().getSpnProgetto().setAdapter(
                VariabiliStaticheModificheCodice.getInstance().getAdapterProgetti()
        );
        if (VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato() != null) {
            VariabiliStaticheModificheCodice.getInstance().getSpnProgetto().setPrompt(
                    VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato()
            );
        } */
        VariabiliStaticheModificheCodice.getInstance().getSpnProgetto().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                try {
                    if (adapter.getItemAtPosition(pos) == null) {
                        return;
                    }
                    if (!VariabiliStaticheModificheCodice.getInstance().isEseguitaLetturaIniziale()) {
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
                        VariabiliStaticheModificheCodice.getInstance().RicaricaModuli(context, db);
                        /* VariabiliStaticheModificheCodice.getInstance().setListaModuli(
                                listaModuli
                        ); */
                        db.ModificaUltimeSelezioni();
                        db.ChiudeDB();

                        /* VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModulo().setVisibility(LinearLayout.VISIBLE);

                        if (!VariabiliStaticheModificheCodice.getInstance().getListaModuli().isEmpty()) {
                            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.VISIBLE);
                            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.VISIBLE);
                        } else {
                            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
                            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);
                        }

                        // VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.VISIBLE);

                        // VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE); */
                    } else {
                        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModulo().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);

                        // VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE);
                    }
                } catch (Exception e) {
                    VariabiliStaticheModificheCodice.getInstance().setProgettoSelezionato("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void GestioneSpinnerModuli(Context context) {
        if (!VariabiliStaticheModificheCodice.getInstance().getListaModuli().isEmpty()) {
            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);
        }

        VariabiliStaticheModificheCodice.getInstance().setAdapterModuli(UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheModificheCodice.getInstance().getSpnModulo(),
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaModuli(
                        VariabiliStaticheModificheCodice.getInstance().getListaModuli()
                ),
                VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato()
        ));

        /* VariabiliStaticheModificheCodice.getInstance().setAdapterModuli(new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaModuli(
                        VariabiliStaticheModificheCodice.getInstance().getListaModuli()
                )
        ));
        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setAdapter(
                VariabiliStaticheModificheCodice.getInstance().getAdapterModuli()
        );

        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setPrompt(
                VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato()
        ); */

        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.VISIBLE);

        VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                if (adapter.getItemAtPosition(pos) == null) {
                    return;
                }
                if (!VariabiliStaticheModificheCodice.getInstance().isEseguitaLetturaIniziale()) {
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
                        VariabiliStaticheModificheCodice.getInstance().RicaricaSezioni(context, db);
                        /* VariabiliStaticheModificheCodice.getInstance().setListaSezioni(
                                listaSezioni
                        ); */
                        db.ModificaUltimeSelezioni();
                        db.ChiudeDB();

                        // VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
                        // VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);

                        // VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.VISIBLE);

                        /* VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.VISIBLE);

                        if (!VariabiliStaticheModificheCodice.getInstance().getListaSezioni().isEmpty()) {
                            VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.VISIBLE);
                            VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.VISIBLE);
                        }

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE); */
                    } else {
                        // VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE);
                    }
                } catch (Exception e) {
                    VariabiliStaticheModificheCodice.getInstance().setModuloSelezionato("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void GestioneSpinnerSezioni(Context context) {
        if (!VariabiliStaticheModificheCodice.getInstance().getListaSezioni().isEmpty()) {
            VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);
        }

        VariabiliStaticheModificheCodice.getInstance().setAdapterSezioni(UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheModificheCodice.getInstance().getSpnSezione(),
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaSezioni(
                        VariabiliStaticheModificheCodice.getInstance().getListaSezioni()
                ),
                VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata()
        ));

        /* VariabiliStaticheModificheCodice.getInstance().setAdapterSezioni(new ArrayAdapter<String>(
                context,
                R.layout.spinner_text,
                VariabiliStaticheModificheCodice.getInstance().RitornaStringaSezioni(
                        VariabiliStaticheModificheCodice.getInstance().getListaSezioni()
                )
        ));
        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setAdapter(
                VariabiliStaticheModificheCodice.getInstance().getAdapterSezioni()
        );

        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setPrompt(
                VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata()
        ); */

        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.VISIBLE);
        VariabiliStaticheModificheCodice.getInstance().setEseguitaLetturaIniziale(true);

        VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                try {
                    if (adapter.getItemAtPosition(pos) == null) {
                        return;
                    }
                    if (!VariabiliStaticheModificheCodice.getInstance().isEseguitaLetturaIniziale()) {
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
                        // VariabiliStaticheModificheCodice.getInstance().setListaModifiche(
                                db.RitornaModifiche(
                                        VariabiliStaticheModificheCodice.getInstance().getIdProgetto(),
                                        VariabiliStaticheModificheCodice.getInstance().getIdModulo(),
                                        VariabiliStaticheModificheCodice.getInstance().getIdSezione()
                                );
                        // );

                        /* VariabiliStaticheModificheCodice.getInstance().getTxtQuante().setText(
                                VariabiliStaticheModificheCodice.getInstance().PrendeNumeroModifiche(context)
                        ); */
                        db.ChiudeDB();

                        /* AdapterListenerModificheCodice customAdapterT = new AdapterListenerModificheCodice(
                                context,
                                VariabiliStaticheModificheCodice.getInstance().getListaModifiche());
                        VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setAdapter(customAdapterT);

                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.VISIBLE);

                        VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setVisibility(LinearLayout.VISIBLE); */
                    } else {
                        VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE);

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
}
