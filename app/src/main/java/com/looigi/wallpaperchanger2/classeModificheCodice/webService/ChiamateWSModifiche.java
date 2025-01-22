package com.looigi.wallpaperchanger2.classeModificheCodice.webService;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeModificheCodice.VariabiliStaticheModificheCodice;
import com.looigi.wallpaperchanger2.classeModificheCodice.db_dati_modifiche_codice;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ChiamateWSModifiche implements TaskDelegateModifiche {
    private static final String NomeMaschera = "Chiamate_WS_MODIFICHE";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheModificheCodice.UrlWS;
    private final String ws = "wsModifiche.asmx/";
    private final String NS="http://looModifiche.it/";
    private final String SA="http://looModifiche.it/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;

    public ChiamateWSModifiche(Context context) {
        this.context = context;
    }

    private String ConvertFileToBase64(String uri) {
        String base64File = "";
        File file = new File(uri);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a file from file system
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = Base64.getEncoder().encodeToString(fileData);
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException ioe) {
            return "";
        }
        return base64File;
    }

    public void Esporta() {
        String PathModifiche = context.getFilesDir() + "/DB/dati_modifiche.db";
        String b64 = ConvertFileToBase64(PathModifiche);

        String Urletto="Esporta?" +
                "Filetto=" + b64;

        TipoOperazione = "Esporta";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void Importa() {
        String Urletto="Importa";

        TipoOperazione = "Importa";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {
        // UtilityLazio.getInstance().ImpostaAttesa(true);

        long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = Long.toString(tsLong);

        InterrogazioneWSModifiche i = new InterrogazioneWSModifiche();
        i.EsegueChiamata(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this
        );
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                // UtilityLazio.getInstance().ImpostaAttesa(false);

                switch (TipoOperazione) {
                    case "Esporta":
                        fEsporta(result);
                        break;
                    case "Importa":
                        fImporta(result);
                        break;
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }

    private boolean ControllaRitorno(String Operazione, String result) {
        if (result.contains("ERROR:") || result.toUpperCase().contains("ANYTYPE")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
            }

            return false;
        } else {
            return true;
        }
    }

    private void fImporta(String result) {
        boolean ritorno = ControllaRitorno("Ritorno importa", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            String PathModifiche = context.getFilesDir() + "/DB/dati_modifiche2.db";
            String PathModificheFinali = context.getFilesDir() + "/DB/dati_modifiche.db";
            byte[] decodedBytes = Base64.getDecoder().decode(result);
            try (FileOutputStream fos = new FileOutputStream(PathModifiche)) {
                fos.write(decodedBytes);
                fos.close();

                Files.getInstance().EliminaFileUnico(PathModificheFinali);
                Files.getInstance().CopiaFile(PathModifiche, PathModificheFinali);
                Files.getInstance().EliminaFileUnico(PathModifiche);

                UtilitiesGlobali.getInstance().ApreToast(context, "DB Importato correttamente");

                Intent intent = VariabiliStaticheModificheCodice.getInstance().getAct().getIntent();
                VariabiliStaticheModificheCodice.getInstance().getAct().finish();
                VariabiliStaticheModificheCodice.getInstance().getAct().startActivity(intent);
            } catch (IOException e) {
                UtilitiesGlobali.getInstance().ApreToast(context, "ERROR: " + e.getMessage());
            }
        }
    }

    private void fEsporta(String result) {
        boolean ritorno = ControllaRitorno("Ritorno esporta", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "DB esportato");
        }
    }
}
