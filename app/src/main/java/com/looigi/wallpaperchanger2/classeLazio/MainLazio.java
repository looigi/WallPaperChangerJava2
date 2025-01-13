package com.looigi.wallpaperchanger2.classeLazio;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerClassifica;
import com.looigi.wallpaperchanger2.classeLazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.classeOrari.UtilityOrari;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classePlayer.Files;

public class MainLazio extends Activity {
    private Context context;
    private Activity act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lazio);

        context = this;
        act = this;

        VariabiliStaticheLazio.getInstance().setPathLazio(context.getFilesDir() + "/Lazio");

        VariabiliStaticheLazio.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoLazio));
        VariabiliStaticheLazio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().setSpnAnni(findViewById(R.id.spnAnni));
        VariabiliStaticheLazio.getInstance().setSpnCompetizioni(findViewById(R.id.spnCompetizione));

        VariabiliStaticheLazio.getInstance().setLayCalendario(findViewById(R.id.layCalendario));
        VariabiliStaticheLazio.getInstance().setLstCalendario(findViewById(R.id.lstCalendario));
        VariabiliStaticheLazio.getInstance().setLayClassifica(findViewById(R.id.layClassifica));
        VariabiliStaticheLazio.getInstance().setLstClassifica(findViewById(R.id.lstClassifica));
        VariabiliStaticheLazio.getInstance().setLaySquadre(findViewById(R.id.laySquadre));
        VariabiliStaticheLazio.getInstance().setLstSquadre(findViewById(R.id.lstSquadre));
        VariabiliStaticheLazio.getInstance().setLayMercato(findViewById(R.id.layMercato));
        VariabiliStaticheLazio.getInstance().setLstMercato(findViewById(R.id.lstMercato));
        VariabiliStaticheLazio.getInstance().setModalitaClassifica(1);
        VariabiliStaticheLazio.getInstance().setMascheraSelezionata(1);
        UtilityLazio.getInstance().VisualizzaMaschera();

        VariabiliStaticheLazio.getInstance().setTxtGiornata(findViewById(R.id.txtGiornata));
        ImageView imgIndietroClassifica = findViewById(R.id.imgIndietroClassifica);
        imgIndietroClassifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int giornata = VariabiliStaticheLazio.getInstance().getGiornata();
                giornata--;
                if (giornata > 0) {
                    VariabiliStaticheLazio.getInstance().getTxtGiornata().setText("Giornata " + giornata);
                    VariabiliStaticheLazio.getInstance().setGiornata(giornata);

                    ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
                    ws1.RitornaClassifica();

                    ChiamateWSLazio ws2 = new ChiamateWSLazio(context);
                    ws2.RitornaCalendario();
                }
            }
        });
        ImageView imgAvantiClassifica = findViewById(R.id.imgAvantiClassifica);
        imgAvantiClassifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int giornata = VariabiliStaticheLazio.getInstance().getGiornata();
                giornata++;
                VariabiliStaticheLazio.getInstance().getTxtGiornata().setText("Giornata " + giornata);
                VariabiliStaticheLazio.getInstance().setGiornata(giornata);

                ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
                ws1.RitornaClassifica();

                ChiamateWSLazio ws2 = new ChiamateWSLazio(context);
                ws2.RitornaCalendario();
            }
        });

        UtilityLazio.getInstance().ImpostaAttesa(false);

        ChiamateWSLazio ws = new ChiamateWSLazio(context);
        ws.RitornaCompetizioni(false);
        ws.RitornaAnni(false);

        RadioButton optTotale = findViewById(R.id.optTotale);
        optTotale.setChecked(true);
        optTotale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setModalitaClassifica(1);

                VariabiliStaticheLazio.getInstance().setCstmAdptClassifica(
                        new AdapterListenerClassifica(context, VariabiliStaticheLazio.getInstance().getClassifica())
                );
                VariabiliStaticheLazio.getInstance().getLstClassifica().setAdapter(
                        VariabiliStaticheLazio.getInstance().getCstmAdptClassifica()
                );
            }
        });

        RadioButton optCasa = findViewById(R.id.optCasa);
        optCasa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setModalitaClassifica(2);

                VariabiliStaticheLazio.getInstance().setCstmAdptClassifica(
                        new AdapterListenerClassifica(context, VariabiliStaticheLazio.getInstance().getClassifica())
                );
                VariabiliStaticheLazio.getInstance().getLstClassifica().setAdapter(
                        VariabiliStaticheLazio.getInstance().getCstmAdptClassifica()
                );
            }
        });

        RadioButton optFuori = findViewById(R.id.optFuori);
        optFuori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setModalitaClassifica(3);

                VariabiliStaticheLazio.getInstance().setCstmAdptClassifica(
                        new AdapterListenerClassifica(context, VariabiliStaticheLazio.getInstance().getClassifica())
                );
                VariabiliStaticheLazio.getInstance().getLstClassifica().setAdapter(
                        VariabiliStaticheLazio.getInstance().getCstmAdptClassifica()
                );
            }
        });

        ImageView imgClassifica = findViewById(R.id.imgClassifica);
        imgClassifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(1);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgCalendario = findViewById(R.id.imgCalendario);
        imgCalendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(2);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgSquadre = findViewById(R.id.imgSquadre);
        imgSquadre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(3);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgMercato = findViewById(R.id.imgMercato);
        imgMercato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setMascheraSelezionata(4);
                UtilityLazio.getInstance().VisualizzaMaschera();
            }
        });

        ImageView imgRefreshAnni = findViewById(R.id.imgRefreshAnno);
        imgRefreshAnni.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaAnni(true);
            }
        });

        ImageView imgRefreshComp = findViewById(R.id.imgRefreshCompetizione);
        imgRefreshComp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.RitornaCompetizioni(true);
            }
        });

        ImageView imgCambiaAnno = findViewById(R.id.imgCambiaAnno);
        imgCambiaAnno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String PathFileSel = VariabiliStaticheLazio.getInstance().getPathLazio();
                String NomeFileSel = "AnnoSelezionato.txt";

                if (Files.getInstance().EsisteFile(PathFileSel + "/" + NomeFileSel)) {
                    Files.getInstance().EliminaFileUnico(PathFileSel + "/" + NomeFileSel);
                }
                Files.getInstance().ScriveFile(PathFileSel, NomeFileSel,
                        VariabiliStaticheLazio.getInstance().getAnnoSelezionato() + ";" +
                        VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato()
                );

                UtilityLazio.getInstance().LeggeAnno(context);
            }
        });

        ImageView imgCambiaComp = findViewById(R.id.imgCambiaCompetizione);
        imgCambiaComp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().LeggeAnno(context);
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
