package com.looigi.wallpaperchanger2.classeOnomastici;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.StrutturaCompleanno;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;

import java.util.Calendar;
import java.util.List;

public class GestioneCompleanni {
    private Context context;
    private Activity act;

    public GestioneCompleanni(Activity act, Context context) {
        this.act = act;
        this.context = context;

        db_dati_compleanni db = new db_dati_compleanni(context);
        db.CreazioneTabelle();

        Calendar calendar = Calendar.getInstance();
        int giorno = calendar.get(Calendar.DAY_OF_MONTH);
        int mese = calendar.get(Calendar.MONTH) + 1;
        VariabiliStaticheOnomastici.getInstance().setCompleanni(db.CaricaCompleannoDelGiorno(giorno, mese));

        Button btnRicerca = act.findViewById(R.id.btnRicercaCompleanni);
        btnRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                impostaSchermata(0);
            }
        });

        Button btnMessaggio = act.findViewById(R.id.btnInvioCompleanni);
        btnMessaggio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                impostaSchermata(1);
            }
        });

        // AGGIUNGE COMPLEANNO
        VariabiliStaticheOnomastici.getInstance().setLayInsComp(act.findViewById(R.id.layInserimentoCompleanno));
        VariabiliStaticheOnomastici.getInstance().getLayInsComp().setVisibility(LinearLayout.GONE);

        VariabiliStaticheOnomastici.getInstance().setEdtNomeCompleanno(act.findViewById(R.id.edtNomeCompleanno));
        VariabiliStaticheOnomastici.getInstance().setEdtGiornoCompleanno(act.findViewById(R.id.edtGiornoCompleanno));
        VariabiliStaticheOnomastici.getInstance().setEdtMeseCompleanno(act.findViewById(R.id.edtMeseCompleanno));
        VariabiliStaticheOnomastici.getInstance().setEdtAnnoCompleanno(act.findViewById(R.id.edtAnnoCompleanno));

        ImageView imgAggiungeCompleanno = act.findViewById(R.id.imgAggiungeCompleanno);
        imgAggiungeCompleanno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheOnomastici.getInstance().setIdModifica(-1);
                VariabiliStaticheOnomastici.getInstance().getEdtNomeCompleanno().setText("");
                VariabiliStaticheOnomastici.getInstance().getEdtGiornoCompleanno().setText("");
                VariabiliStaticheOnomastici.getInstance().getEdtMeseCompleanno().setText("");
                VariabiliStaticheOnomastici.getInstance().getEdtAnnoCompleanno().setText("");

                VariabiliStaticheOnomastici.getInstance().getLayInsComp().setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgSalvaCompleanno = act.findViewById(R.id.imgSalvaCompleanno);
        imgSalvaCompleanno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Nome = VariabiliStaticheOnomastici.getInstance().getEdtNomeCompleanno().getText().toString();
                String Giorno = VariabiliStaticheOnomastici.getInstance().getEdtGiornoCompleanno().getText().toString();
                String Mese = VariabiliStaticheOnomastici.getInstance().getEdtMeseCompleanno().getText().toString();
                String Anno = VariabiliStaticheOnomastici.getInstance().getEdtAnnoCompleanno().getText().toString();
                if (Nome.isEmpty()) {
                    UtilityWallpaper.getInstance().VisualizzaMessaggio("Inserire il nome");
                    return;
                }
                if (Giorno.isEmpty()) {
                    UtilityWallpaper.getInstance().VisualizzaMessaggio("Inserire il giorno");
                    return;
                } else {
                    if (Integer.parseInt(Giorno) < 1 || Integer.parseInt(Giorno) > 31) {
                        UtilityWallpaper.getInstance().VisualizzaMessaggio("Giorno non valido");
                        return;
                    }
                }
                if (Mese.isEmpty()) {
                    UtilityWallpaper.getInstance().VisualizzaMessaggio("Inserire il mese");
                    return;
                } else {
                    if (Integer.parseInt(Mese) < 1 || Integer.parseInt(Mese) > 12) {
                        UtilityWallpaper.getInstance().VisualizzaMessaggio("Mese non valido");
                        return;
                    }
                }
                if (Anno.isEmpty()) {
                    UtilityWallpaper.getInstance().VisualizzaMessaggio("Inserire l'anno");
                    return;
                }

                StrutturaCompleanno s = new StrutturaCompleanno();
                s.setNome(Nome);
                s.setGiorno(Integer.parseInt(Giorno));
                s.setMese(Integer.parseInt(Mese));
                s.setAnno(Integer.parseInt(Anno));

                db_dati_compleanni db = new db_dati_compleanni(context);
                if (VariabiliStaticheOnomastici.getInstance().getIdModifica() == -1) {
                    db.ScriveCompleanno(s);
                } else {
                    db.ModificaCompleanno(s);
                }

                VariabiliStaticheOnomastici.getInstance().setCompleanni(db.CaricaCompleannoDelGiorno(giorno, mese));
                VariabiliStaticheOnomastici.getInstance().ScriveCompleanni(act);

                VariabiliStaticheOnomastici.getInstance().getLayInsComp().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaCompleanno = act.findViewById(R.id.imgAnnullaCompleanno);
        imgAnnullaCompleanno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheOnomastici.getInstance().getLayInsComp().setVisibility(LinearLayout.GONE);
            }
        });
        // AGGIUNGE COMPLEANNO

        impostaSchermata(1);
    }

    private void impostaSchermata(int quale) {
        LinearLayout layRicerche = act.findViewById(R.id.layRicercheCompleanno);
        LinearLayout layMessaggi = act.findViewById(R.id.layMessaggiCompleanno);

        layRicerche.setVisibility(LinearLayout.GONE);
        layMessaggi.setVisibility(LinearLayout.GONE);

        switch(quale) {
            case 0:
                layRicerche.setVisibility(LinearLayout.VISIBLE);
                break;
            case 1:
                layMessaggi.setVisibility(LinearLayout.VISIBLE);
                break;
        }
    }
}
