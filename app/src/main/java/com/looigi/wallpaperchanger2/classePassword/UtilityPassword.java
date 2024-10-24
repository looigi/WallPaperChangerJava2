package com.looigi.wallpaperchanger2.classePassword;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.utilities.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class UtilityPassword {
    private static final String NomeMaschera = "Utility_Password";
    private static UtilityPassword instance = null;
    private ProgressDialog progressDialog;

    private UtilityPassword() {
    }

    public static UtilityPassword getInstance() {
        if (instance == null) {
            instance = new UtilityPassword();
        }

        return instance;
    }

    public void ChiudeDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception ignored) {
        }
    }

    public void ApriDialog(Context context, boolean ApriDialog, String tOperazione) {
        if (!ApriDialog) {
            // OggettiAVideo.getInstance().getImgRest().setVisibility(LinearLayout.VISIBLE);
        } else {
            try {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Attendere Prego...\n\n" + tOperazione);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            } catch (Exception e) {
                VisualizzaMessaggio(context, UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public void RiempieArrayLista(Context context) {
        AdapterListenerPassword customAdapterT = new AdapterListenerPassword(context,
                VariabiliStatichePWD.getInstance().getListaPassword());
        VariabiliStatichePWD.getInstance().getLstPassword().setAdapter(customAdapterT);

        VariabiliStatichePWD.getInstance().getTxtQuante().setText("Rilevate:\n" + Integer.toString(VariabiliStatichePWD.getInstance().getListaPassword().size()));
    }

    public void VisualizzaMessaggio(Context context, String Messaggio) {
        /* VariabiliGlobali.getInstance().getActivity().runOnUiThread(new Runnable() {
            public void run() { */
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Messaggio");
        alertDialog.setMessage(Messaggio);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        // }
        // });
    }

    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("PENNETTA", Maschera,  Log);
            // }
        } else {

        }
    }
}
