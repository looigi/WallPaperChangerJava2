package com.looigi.wallpaperchanger2.classeUtilityImmagini;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.Objects;

public class UtilityUtilityImmagini {
    private static UtilityUtilityImmagini instance = null;

    private UtilityUtilityImmagini() {
    }

    public static UtilityUtilityImmagini getInstance() {
        if (instance == null) {
            instance = new UtilityUtilityImmagini();
        }

        return instance;
    }

    public void Prosegue(Context context) {
        StrutturaControlloImmagini s = VariabiliStaticheUtilityImmagini.getInstance().getStrutturaAttuale();

        VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");

        boolean ok = true;
        int i = 0;
        for (StrutturaControlloImmagini s2 : VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini()) {
            if (Objects.equals(s2.getCategoria(), s.getCategoria())) {
                ok = false;
                break;
            }
            i++;
        }
        if (ok) {
            VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini().add(s);
        } else {
            VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini().set(i, s);
        }
        VariabiliStaticheUtilityImmagini.getInstance().getAdapter().notifyDataSetChanged();

        db_dati_ui db = new db_dati_ui(context);
        db.ScriveDati(s);
        db.ChiudeDB();

        if (VariabiliStaticheUtilityImmagini.getInstance().isControllaTutto()) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    if (VariabiliStaticheUtilityImmagini.getInstance().isBloccaElaborazione()) {
                        VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
                        VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                        UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione bloccata");
                        return;
                    }
                    int quale = ControllaProssimoNumero(
                            VariabiliStaticheUtilityImmagini.getInstance().getQualeStaControllando()
                    );
                    if (quale > -1) {
                        VariabiliStaticheUtilityImmagini.getInstance().setQualeStaControllando(quale);

                        if (quale < VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().size()) {
                            int idCategoria2 = VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().get(quale).getIdCategoria();
                            String Categoria2 = VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().get(quale).getCategoria();
                            VariabiliStaticheUtilityImmagini.getInstance().setCategoriaAttuale(Categoria2);
                            VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("Elaborazione " + Categoria2 + " " +
                                    (quale + 1) + "/" + VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().size());

                            if (VariabiliStaticheUtilityImmagini.getInstance().isEsegueAncheRefresh()) {
                                ChiamateWSUI ws = new ChiamateWSUI(context);
                                ws.RefreshImmagini(String.valueOf(idCategoria2), false);
                            } else {
                                ChiamateWSUI ws = new ChiamateWSUI(context);
                                ws.ControlloImmagini(String.valueOf(idCategoria2), "N");
                            }
                        } else {
                            VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
                            VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                            UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione effettuata");

                            VariabiliStaticheUtilityImmagini.getInstance().setAdapter(new AdapterListenerUI(context,
                                    VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM()));
                            VariabiliStaticheUtilityImmagini.getInstance().getLstImmagini().setAdapter(VariabiliStaticheUtilityImmagini.getInstance().getAdapter());
                        }
                    } else {
                        VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
                        VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                        UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione effettuata");

                        VariabiliStaticheUtilityImmagini.getInstance().setAdapter(new AdapterListenerUI(context,
                                VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM()));
                        VariabiliStaticheUtilityImmagini.getInstance().getLstImmagini().setAdapter(VariabiliStaticheUtilityImmagini.getInstance().getAdapter());
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 500);
        } else {
            VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
        }
    }

    public int ControllaProssimoNumero(int quale) {
        int quale2 = quale;
        boolean ancora = true;

        while (ancora && quale2 < VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().size()) {
            StrutturaImmaginiCategorie c = VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().get(quale2);
            boolean ok = true;
            if (!c.getCategoria().toUpperCase().trim().equals("ALTRE")) {
                for (StrutturaControlloImmagini i : VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini()) {
                    if (c.getIdCategoria() == i.getIdCategoria()) {
                        ok = false;
                        break;
                    }
                }
            } else {
                ok = false;
            }
            if (ok) {
                ancora = false;
            } else {
                quale2++;
            }
        }

        return quale2;
    }
}
