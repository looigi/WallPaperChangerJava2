package com.looigi.wallpaperchanger2.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.MainActivity;
import com.looigi.wallpaperchanger2.classiStandard.Log;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

public class Utility {
    private static final String NomeMaschera = "UTILITY";
    private ProgressDialog progressDialog;

    private static Utility instance = null;

    private Utility() {
    }

    public static Utility getInstance() {
        if (instance == null) {
            instance = new Utility();
        }

        return instance;
    }
    
    public void ScriveLog(Context context, String Maschera, String Log) {
        if (VariabiliStaticheServizio.getInstance().getPercorsoDIRLog().isEmpty() ||
                VariabiliStaticheServizio.getInstance().getNomeFileDiLog().isEmpty()) {
            generaPath();
        }

        if (context != null) {
            if (VariabiliStaticheServizio.getInstance().getLog() == null) {
                com.looigi.wallpaperchanger2.classiStandard.Log l = new Log(context);
                VariabiliStaticheServizio.getInstance().setLog(l);
            }

            if (!Utility.getInstance().EsisteFile(VariabiliStaticheServizio.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheServizio.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheServizio.getInstance().getLog().PulisceFileDiLog(false);
            }

            if (EsisteFile(VariabiliStaticheServizio.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheServizio.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheServizio.getInstance().getLog().ScriveLog(Maschera + ": " + Log);
            }
        } else {

        }
    }

    public String PrendeErroreDaException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return TransformError(errors.toString());
    }

    private String TransformError(String error) {
        String Return = error;

        if (Return.length() > 250) {
            Return = Return.substring(0, 247) + "...";
        }
        Return = Return.replace("\n", " ");

        return Return;
    }

    public boolean EsisteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void generaPath() {
        String pathLog = Environment.getExternalStorageDirectory() + "/" +
                Environment.DIRECTORY_DOWNLOADS + "/LooigiSoft/" + VariabiliStaticheServizio.channelName + "/Log";
        VariabiliStaticheServizio.getInstance().setPercorsoDIRLog(pathLog);
        String nomeFileLog = VariabiliStaticheServizio.channelName + ".txt";
        VariabiliStaticheServizio.getInstance().setNomeFileDiLog(nomeFileLog);
    }

    public boolean EliminaFileUnico(String fileName) {
        if (EsisteFile(fileName)) {
            File file = new File(fileName);
            return file.delete();
        } else {
            return false;
        }
    }

    public void VisualizzaMessaggio(String Messaggio) {
        VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(VariabiliStaticheServizio.getInstance().getMainActivity()).create();
                alertDialog.setTitle("Messaggio " + VariabiliStaticheServizio.channelName);
                alertDialog.setMessage(Messaggio);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                // alertDialog.show();
            }
        });
    }

    public void ChiudeDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception ignored) {
        }
    }

    public void ApriDialog(boolean ApriDialog, String tOperazione) {
        if (!ApriDialog) {
            // OggettiAVideo.getInstance().getImgRest().setVisibility(LinearLayout.VISIBLE);
        } else {
            try {
                progressDialog = new ProgressDialog(VariabiliStaticheServizio.getInstance().getMainActivity());
                progressDialog.setMessage("Attendere Prego...\n\n" + tOperazione);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            } catch (Exception ignored) {

            }
        }
    }

    public void CreaCartelle(String Path) {
        String[] Pezzetti = Path.split("/");
        String DaCreare = "";

        for (int i = 0; i < Pezzetti.length; i++) {
            if (!Pezzetti[i].isEmpty()) {
                DaCreare += "/" + Pezzetti[i];
                File newFolder = new File(DaCreare);
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
            }
        }
    }

    public void VisualizzaErrore(Context context, String Errore) {
        VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        ScriveLog(context, NomeMaschera, "Visualizzo messaggio di errore. Schermo acceso: " +
                VariabiliStaticheServizio.getInstance().isScreenOn());
        if (VariabiliStaticheServizio.getInstance().isScreenOn()) {
            VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(VariabiliStaticheServizio.getInstance().getMainActivity()).create();
                    alertDialog.setTitle("Messaggio " + VariabiliStaticheServizio.channelName);
                    alertDialog.setMessage(Errore);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    // alertDialog.show();
                }
            });
        } else {
            ScriveLog(context, NomeMaschera,"Schermo spento. Non visualizzo messaggio di errore: " + Errore);
        }
    }

    public int GeneraNumeroRandom(int NumeroMassimo) {
        if (NumeroMassimo > 0) {
            final int random = new Random().nextInt(NumeroMassimo);

            return random;
        } else {
            return -1;
        }
    }

    public void stopService(Context context) {
        Utility.getInstance().ScriveLog(context, NomeMaschera, "Stop Servizio");

        if (VariabiliStaticheServizio.getInstance().getServizioForeground() != null) {
            context.stopService(VariabiliStaticheServizio.getInstance().getServizioForeground());
            VariabiliStaticheServizio.getInstance().setServizioForeground(null);
        }
    }
}
