package com.looigi.wallpaperchanger2.ModificheCodice.webService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.MainStart;
import com.looigi.wallpaperchanger2.Backup.UtilityBackup;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.adapters.AdapterListenerFilesRemoti;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.CalcoloVelocita;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.UtilityGPS;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.db_dati_gps;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.strutture.StrutturaNomeFileRemoti;
import com.looigi.wallpaperchanger2.ModificheCodice.GestioneSpinner;
import com.looigi.wallpaperchanger2.ModificheCodice.GestioneStati.adapters.AdapterListenerModificheStati;
import com.looigi.wallpaperchanger2.ModificheCodice.Strutture.Modifiche;
import com.looigi.wallpaperchanger2.ModificheCodice.Strutture.Moduli;
import com.looigi.wallpaperchanger2.ModificheCodice.Strutture.Progetti;
import com.looigi.wallpaperchanger2.ModificheCodice.Strutture.Sezioni;
import com.looigi.wallpaperchanger2.ModificheCodice.Strutture.Stati;
import com.looigi.wallpaperchanger2.ModificheCodice.Strutture.StrutturaConteggi;
import com.looigi.wallpaperchanger2.ModificheCodice.VariabiliStaticheModificheCodice;
import com.looigi.wallpaperchanger2.ModificheCodice.adapters.AdapterListenerConteggi;
import com.looigi.wallpaperchanger2.ModificheCodice.adapters.AdapterListenerModificheCodice;
import com.looigi.wallpaperchanger2.ModificheCodice.db_dati_modifiche_codice;
import com.looigi.wallpaperchanger2.Notifiche.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ChiamateWSModifiche implements TaskDelegateModifiche {
    private static final String NomeMaschera = "Chiamate_WS_MODIFICHE";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheModificheCodice.UrlWS;
    private final String ws = "modifiche.asmx/";
    private final String NS="http://wallpaperChangerWS_Modifiche.it/";
    private final String SA="http://wallpaperChangerWS_Modifiche.it/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private String TipoFile;

    public ChiamateWSModifiche(Context context) {
        this.context = context;
    }

    private boolean unzip(File zipFile, File targetDirectory) throws IOException {
        boolean Ritorno = true;

        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } catch(IOException ignored) {
                    Ritorno = false;
                } finally {
                    fout.close();
                }
            }
        } catch(IOException ignored) {
            Ritorno = false;
        } finally {
            zis.close();
        }

        return Ritorno;
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

    private String SistemaTestoPerInvio(String Cosa) {
        String Ritorno = null;
        try {
            URI uri = new URI(null, null, Cosa, null);
            Ritorno = uri.toASCIIString();

            Ritorno = Ritorno.replace(",", "*V*");
            Ritorno = Ritorno.replace("/", "*S*");
            Ritorno = Ritorno.replace("\\", "*BS*");
            Ritorno = Ritorno.replace("&", "*A*");
            Ritorno = Ritorno.replace(":", "*2P*");
        } catch (URISyntaxException e) {
            Ritorno = "";
        }

        return Ritorno;
    }

    private String SistemaTestoPerRitorno(String Cosa) {
        String Ritorno = Cosa;

        Ritorno = Ritorno.replace("*V*", ",").replace("*v*", ",");
        Ritorno = Ritorno.replace( "*S*", "/").replace( "*s*", "/");
        Ritorno = Ritorno.replace("*BS*", "\\").replace("*bs*", "\\");
        Ritorno = Ritorno.replace("*A*", "&").replace("*a*", "&");
        Ritorno = Ritorno.replace("*2P*", ":").replace("*2p*", ":");

        return Ritorno;
    }
    public void SalvaTuttiIDati() {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Riga = VariabiliStaticheModificheCodice.getInstance().getListaAppoggioDaSalvare().get(
                VariabiliStaticheModificheCodice.getInstance().getQualeIdDaSalvare()
        );
        String[] r = Riga.split(";", -1);
        String Tipo = r[0];
        String idProgetto = r[1];
        String idModulo = r[2];
        String idSezione = r[3];
        String idModifica = r[4];
        String Valore = SistemaTestoPerInvio(r[5]);
        String idStato = r[6];
        String Stato = r[7];

        String Urletto = "";

        switch (Tipo) {
            case "PROGETTO":
                Urletto = "InserisceModificaProgetto?idProgetto=" + idProgetto + "&Progetto=" + Valore;
                TipoOperazione = "InserisceModificaProgetto";
                break;
            case "MODULO":
                Urletto = "InserisceModificaModulo?idProgetto=" + idProgetto + "&idModulo=" + idModulo +
                        "&Modulo=" + Valore;
                TipoOperazione = "InserisceModificaModulo";
                break;
            case "SEZIONE":
                Urletto = "InserisceModificaSezione?idProgetto=" + idProgetto + "&idModulo=" + idModulo +
                        "&idSezione=" + idSezione + "&Sezione=" + Valore;
                TipoOperazione = "InserisceModificaSezione";
                break;
            case "MODIFICA":
                Urletto = "InserisceModificaModifica?idProgetto=" + idProgetto + "&idModulo=" + idModulo +
                        "&idSezione=" + idSezione + "&idModifica=" + idModifica + "&Modifica=" + Valore + "&idStato=" + idStato;
                TipoOperazione = "InserisceModificaModifica";
                break;
        }

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void RitornaNumeroModificheTotali(String idProgetto, String idModulo, String idSezione) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaNumeroModificheTotali?" +
                "idProgetto=" + idProgetto +
                "&idModulo=" + idModulo + "" +
                "&idSezione=" + idSezione;

        TipoOperazione = "RitornaNumeroModificheTotali";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void RitornaConteggi() {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaConteggi";

        TipoOperazione = "RitornaConteggi";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void InserisceModificaStato(String idStato, String Stato, boolean DaGestioneStati) {
        UtilityGPS.getInstance().ImpostaAttesa(true);
        this.DaGestioneStati = DaGestioneStati;

        String Urletto="InserisceModificaStato?" +
                "idStato=" + idStato +
                "&Stato=" + SistemaTestoPerInvio(UtilitiesGlobali.getInstance().MetteMaiuscoleDopoOgniPunto(Stato));

        TipoOperazione = "InserisceModificaStato";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void InserisceModificaProgetto(String idProgetto, String Valore) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="InserisceModificaProgetto?" +
                "idProgetto=" + idProgetto +
                "&Progetto=" + SistemaTestoPerInvio(Valore);

        TipoOperazione = "InserisceModificaProgetto";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void InserisceModificaModulo(String idProgetto, String idModulo, String Valore) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="InserisceModificaModulo?" +
                "idProgetto=" + idProgetto +
                "&idModulo=" + idModulo +
                "&Modulo=" + SistemaTestoPerInvio(Valore);

        TipoOperazione = "InserisceModificaModulo";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void InserisceModificaSezione(String idProgetto, String idModulo, String idSezione, String Valore) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="InserisceModificaSezione?" +
                "idProgetto=" + idProgetto +
                "&idModulo=" + idModulo +
                "&idSezione=" + idSezione +
                "&Sezione=" + SistemaTestoPerInvio(Valore);

        TipoOperazione = "InserisceModificaSezione";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void InserisceModificaModifica(String idProgetto, String idModulo, String idSezione, String idModifica, String Valore, String idStato) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="InserisceModificaModifica?" +
                "idProgetto=" + idProgetto +
                "&idModulo=" + idModulo +
                "&idSezione=" + idSezione +
                "&idModifica=" + idModifica +
                "&Modifica=" + SistemaTestoPerInvio(Valore) +
                "&idStato=" + idStato;

        TipoOperazione = "InserisceModificaModifica";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void EliminaStato(String idStato, boolean DaGestioneStati) {
        UtilityGPS.getInstance().ImpostaAttesa(true);
        this.DaGestioneStati = DaGestioneStati;

        String Urletto="EliminaStato?idStato=" + idStato;

        TipoOperazione = "EliminaStato";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void EliminaProgetto(String idProgetto) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="EliminaProgetto?idProgetto=" + idProgetto;

        TipoOperazione = "EliminaProgetto";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void EliminaModulo(String idProgetto, String idModulo) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="EliminaModulo?idProgetto=" + idProgetto + "&idModulo=" + idModulo;

        TipoOperazione = "EliminaModulo";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void EliminaSezione(String idProgetto, String idModulo, String idSezione) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="EliminaSezione?idProgetto=" + idProgetto + "&idModulo=" + idModulo + "&idSezione=" + idSezione;

        TipoOperazione = "EliminaSezione";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void EliminaModifica(String idProgetto, String idModulo, String idSezione, String idModifica) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="EliminaModifica?idProgetto=" + idProgetto + "&idModulo=" + idModulo + "&idSezione=" + idSezione + "&idModifica=" + idModifica;

        TipoOperazione = "EliminaModifica";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    private boolean DaGestioneStati = false;

    public void RitornaStati(boolean DaGestioneStati) {
        UtilityGPS.getInstance().ImpostaAttesa(true);
        this.DaGestioneStati = DaGestioneStati;

        String Urletto="RitornaStati";

        TipoOperazione = "RitornaStati";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void RitornaProgetti() {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaProgetti";

        TipoOperazione = "RitornaProgetti";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void RitornaModuli(String idProgetto) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaModuli?" +
                "idProgetto=" + idProgetto;

        TipoOperazione = "RitornaModuli";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void RitornaSezioni(String idProgetto, String idModulo) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaSezioni?" +
                "idProgetto=" + idProgetto +
                "&idModulo=" + idModulo;

        TipoOperazione = "RitornaSezioni";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void RitornaModifiche(String idProgetto, String idModulo, String idSezione) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaModifiche?" +
                "idProgetto=" + idProgetto +
                "&idModulo=" + idModulo +
                "&idSezione=" + idSezione;

        TipoOperazione = "RitornaModifiche";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void RitornaFilesRemoti() {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaFilesGPS";

        TipoOperazione = "RitornaFilesGPS";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void EliminaFileRemoto(String QualeFile, String NomeFile) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="EliminaFile?" +
                "&TipoFile=" + QualeFile +
                "&NomeFile=" + NomeFile;

        TipoOperazione = "EliminaFileRemoto";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    private boolean EseguePulizia = false;
    private String listaDate;

    public void Esporta(String QualeFile, String NomeFile, boolean eseguePulizia, String listaDate) {
        this.TipoFile = QualeFile;
        this.EseguePulizia = eseguePulizia;
        this.listaDate = listaDate;
        String PathModifiche = "";
        String b64 = "";
        String sNomeFile = "";

        switch (QualeFile) {
            case "MODIFICHE":
                VariabiliStaticheModificheCodice.getInstance().Attende(true);
                PathModifiche = context.getFilesDir() + "/DB/dati_modifiche.db";
                b64 = ConvertFileToBase64(PathModifiche);
                break;
            case "BACKUP":
                UtilityBackup.getInstance().Attende(true);
                PathModifiche = NomeFile;
                b64 = ConvertFileToBase64(PathModifiche);
                break;
            case "GPS":
                UtilityGPS.getInstance().ImpostaAttesa(false);
                PathModifiche = NomeFile;
                String[] n = NomeFile.split("/", -1);
                sNomeFile = n[n.length - 1];
                b64 = ConvertFileToBase64(PathModifiche);
                break;
        }

        String Urletto="Esporta?" +
                "Filetto=" + b64 +
                "&TipoFile=" + QualeFile +
                "&sNomeFile=" + sNomeFile;

        TipoOperazione = "Esporta";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                120000,
                ApriDialog);
    }

    public void Importa(String TipoFile, String NomeFile) {
        this.TipoFile = TipoFile;

        switch (TipoFile) {
            case "MODIFICHE":
                VariabiliStaticheModificheCodice.getInstance().Attende(true);
                break;
            case "BACKUP":
                UtilityBackup.getInstance().Attende(true);
                break;
            case "GPS":
                UtilityGPS.getInstance().ImpostaAttesa(true);
                break;
        }

        String Urletto="Importa?" +
                "TipoFile=" + TipoFile +
                "&sNomeFile=" + NomeFile;

        TipoOperazione = "Importa";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                20000,
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
                    case "RitornaConteggi":
                        fRitornaConteggi(result);
                        break;
                    case "RitornaFilesGPS":
                        fRitornaFilesGPS(result);
                        break;
                    case "EliminaFileRemoto":
                        fEliminaFileRemoto(result);
                        break;
                    case "InserisceModificaStato":
                        fInserisceModificaStato(result);
                        break;
                    case "InserisceModificaProgetto":
                        fInserisceModificaProgetto(result);
                        break;
                    case "InserisceModificaModulo":
                        fInserisceModificaModulo(result);
                        break;
                    case "InserisceModificaSezione":
                        fInserisceModificaSezione(result);
                        break;
                    case "InserisceModificaModifica":
                        fInserisceModificaModifica(result);
                        break;
                    case "EliminaStato":
                        fEliminaStato(result);
                        break;
                    case "EliminaProgetto":
                        fEliminaProgetto(result);
                        break;
                    case "EliminaModulo":
                        fEliminaModulo(result);
                        break;
                    case "EliminaSezione":
                        fEliminaSezione(result);
                        break;
                    case "EliminaModifica":
                        fEliminaModifica(result);
                        break;
                    case "RitornaStati":
                        fRitornaStati(result);
                        break;
                    case "RitornaProgetti":
                        fRitornaProgetti(result);
                        break;
                    case "RitornaModuli":
                        fRitornaModuli(result);
                        break;
                    case "RitornaSezioni":
                        fRitornaSezioni(result);
                        break;
                    case "RitornaModifiche":
                        fRitornaModifiche(result);
                        break;
                    case "RitornaNumeroModificheTotali":
                        fRitornaNumeroModificheTotali(result);
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

    private void ContinuaSalvataggio() {
        int q = VariabiliStaticheModificheCodice.getInstance().getQualeIdDaSalvare();
        VariabiliStaticheModificheCodice.getInstance().setQualeIdDaSalvare(q + 1);

        if (VariabiliStaticheModificheCodice.getInstance().getQualeIdDaSalvare() <
                VariabiliStaticheModificheCodice.getInstance().getListaAppoggioDaSalvare().size()) {
            ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
            ws.SalvaTuttiIDati();
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Operazione effettuata");
        }
    }

    public void fRitornaNumeroModificheTotali(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna numero modifiche", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        VariabiliStaticheModificheCodice.getInstance().setTotModifiche(Integer.parseInt(result));
        VariabiliStaticheModificheCodice.getInstance().contaModifiche();
    }

    private void fRitornaConteggi(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna conteggi", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        // {"idProgetto": 1,"idModulo": 2,"idSezione": 3,"Progetto": "Wallpaper%20changer%20ii","Modulo": "Applicazione","Sezione": "Detector","Quanti": 1}
        List<StrutturaConteggi> lista = new ArrayList<>();
        String resultSenzaQuadre = result.substring(1, result.length() - 1);
        if (!resultSenzaQuadre.isEmpty()) {
            String[] righe = resultSenzaQuadre.split("\\},\\{", -1);
            for (String obj : righe) {
                obj = obj.replace("{", "").replace("}", "");
                String[] fields = obj.split(",", -1);

                StrutturaConteggi p = new StrutturaConteggi();
                String[] keyValue = fields[0].split(":", -1);
                String value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdProgetto(Integer.parseInt(value));

                keyValue = fields[1].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdModulo(Integer.parseInt(value));

                keyValue = fields[2].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdSezione(Integer.parseInt(value));

                keyValue = fields[3].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                try {
                    String decoded = URLDecoder.decode(value, "UTF-8");
                    p.setProgetto(decoded);
                } catch (UnsupportedEncodingException e) {
                    p.setProgetto("ERROR");
                }

                keyValue = fields[4].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                try {
                    String decoded = URLDecoder.decode(value, "UTF-8");
                    p.setModulo(decoded);
                } catch (UnsupportedEncodingException e) {
                    p.setModulo("ERROR");
                }

                keyValue = fields[5].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                try {
                    String decoded = URLDecoder.decode(value, "UTF-8");
                    p.setSezione(decoded);
                } catch (UnsupportedEncodingException e) {
                    p.setSezione("ERROR");
                }

                keyValue = fields[6].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setQuante(Integer.parseInt(value));

                lista.add(p);
            }

            VariabiliStaticheModificheCodice.getInstance().setListaConteggi(lista);

            AdapterListenerConteggi adapterC = (new AdapterListenerConteggi(context,
                    VariabiliStaticheModificheCodice.getInstance().getListaConteggi()));
            VariabiliStaticheModificheCodice.getInstance().getLstConteggi().setAdapter(adapterC);
        }
    }

    private void fRitornaStati(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna stati", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        // [{"idStato": 0,"Stato": "Aperta"},{"idStato": 2,"Stato": "Chiusa"},{"idStato": 1,"Stato": "Da%20Controllare"},{"idStato": 3,"Stato": "In%20Dubbio"}]
        List<Stati> lista = new ArrayList<>();
        String resultSenzaQuadre = result.substring(1, result.length() - 1);
        if (!resultSenzaQuadre.isEmpty()) {
            String[] righe = resultSenzaQuadre.split("\\},\\{", -1);
            for (String obj : righe) {
                obj = obj.replace("{", "").replace("}", "");
                String[] fields = obj.split(",", -1);

                Stati p = new Stati();
                String[] keyValue = fields[0].split(":", -1);
                String value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdStato(Integer.parseInt(value));

                keyValue = fields[1].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();

                try {
                    String decoded = URLDecoder.decode(value, "UTF-8");
                    p.setStato(decoded);
                } catch (UnsupportedEncodingException e) {
                    p.setStato("ERROR");
                }

                lista.add(p);
            }

            if (DaGestioneStati) {
                VariabiliStaticheModificheCodice.getInstance().setListaStatiPerGestione(lista);

                AdapterListenerModificheStati customAdapterT = new AdapterListenerModificheStati(
                        context,
                        VariabiliStaticheModificheCodice.getInstance().getListaStatiPerGestione());
                VariabiliStaticheModificheCodice.getInstance().setAdapterModificheStati(customAdapterT);
                VariabiliStaticheModificheCodice.getInstance().getLstModificheStati().setAdapter(customAdapterT);
            } else {
                VariabiliStaticheModificheCodice.getInstance().setListaStati(lista);
                db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
                db.RitornaStatiAttivi();
                VariabiliStaticheModificheCodice.getInstance().DisegnaStati(context);

                GestioneSpinner.getInstance().GestioneSpinnerStati(context);
            }
        }
    }

    private void fRitornaProgetti(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna progetti", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        // [{"idProgetto": 1,"Progetto": "Wallpaper%20changer%20ii"}]
        List<Progetti> lista = new ArrayList<>();
        String resultSenzaQuadre = result.substring(1, result.length() - 1);
        if (!resultSenzaQuadre.isEmpty()) {
            String[] righe = resultSenzaQuadre.split("\\},\\{", -1);
            for (String obj : righe) {
                obj = obj.replace("{", "").replace("}", "");
                String[] fields = obj.split(",", -1);

                Progetti p = new Progetti();
                String[] keyValue = fields[0].split(":", -1);
                String value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdProgetto(Integer.parseInt(value));

                keyValue = fields[1].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();

                try {
                    String decoded = URLDecoder.decode(value, "UTF-8");
                    decoded = UtilitiesGlobali.getInstance().MetteMaiuscoleDopoOgniPunto(SistemaTestoPerRitorno(decoded));
                    p.setProgetto(decoded);
                } catch (UnsupportedEncodingException e) {
                    p.setProgetto("ERROR");
                }

                lista.add(p);
            }

            VariabiliStaticheModificheCodice.getInstance().setListaProgetti(lista);

            if (!lista.isEmpty()) {
                VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);
            } else {
                VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.GONE);
            }

            GestioneSpinner.getInstance().GestioneSpinnerProgetti(context);

            int idProgetto = -1;
            if (!VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato().isEmpty()) {
                idProgetto = VariabiliStaticheModificheCodice.getInstance().TornaIdProgetto(
                        VariabiliStaticheModificheCodice.getInstance().getListaProgetti(),
                        VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato()
                );
            }
            if (idProgetto == -1) {
                if (!lista.isEmpty()) {
                    // idProgetto = -1;
                // } else {
                    idProgetto = lista.get(0).getIdProgetto();
                    VariabiliStaticheModificheCodice.getInstance().setProgettoSelezionato(
                            (String) lista.get(0).getProgetto().trim()
                    );

                    VariabiliStaticheModificheCodice.getInstance().getSpnProgetto().setPrompt(
                            VariabiliStaticheModificheCodice.getInstance().getProgettoSelezionato()
                    );
                }
            } else {
                for (Progetti p: lista) {
                    if (idProgetto == p.getIdProgetto()) {
                        VariabiliStaticheModificheCodice.getInstance().setProgettoSelezionato(
                                p.getProgetto().trim()
                        );
                    }
                }
            }
            VariabiliStaticheModificheCodice.getInstance().setIdProgetto(idProgetto);

            db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
            db.ModificaUltimeSelezioni();
            VariabiliStaticheModificheCodice.getInstance().RicaricaModuli(context, db);

            VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModulo().setVisibility(LinearLayout.VISIBLE);

            if (VariabiliStaticheModificheCodice.getInstance().getListaModuli() != null && !VariabiliStaticheModificheCodice.getInstance().getListaModuli().isEmpty()) {
                VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.VISIBLE);
            } else {
                VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);
            }

            VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.VISIBLE);

            VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE);
        }
    }

    private void fRitornaModuli(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna moduli", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        // [{"idProgetto": 1,"idModulo": 2,"Modulo": "Applicazione"},{"idProgetto": 1,"idModulo": 1,"Modulo": "Web%20service"}]
        List<Moduli> lista = new ArrayList<>();
        String resultSenzaQuadre = result.substring(1, result.length() - 1);
        if (!resultSenzaQuadre.isEmpty()) {
            String[] righe = resultSenzaQuadre.split("\\},\\{", -1);
            for (String obj : righe) {
                obj = obj.replace("{", "").replace("}", "");
                String[] fields = obj.split(",", -1);

                Moduli p = new Moduli();
                String[] keyValue = fields[0].split(":", -1);
                String value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdProgetto(Integer.parseInt(value));

                keyValue = fields[1].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdModulo(Integer.parseInt(value));

                keyValue = fields[2].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                try {
                    String decoded = URLDecoder.decode(value, "UTF-8");
                    decoded = UtilitiesGlobali.getInstance().MetteMaiuscoleDopoOgniPunto(SistemaTestoPerRitorno(decoded));
                    p.setModulo(decoded);
                } catch (UnsupportedEncodingException e) {
                    p.setModulo("ERROR");
                }

                lista.add(p);
            }

            VariabiliStaticheModificheCodice.getInstance().setListaModuli(lista);

            GestioneSpinner.getInstance().GestioneSpinnerModuli(context);

            int idModulo = 1;
            if (!VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato().isEmpty()) {
                idModulo = VariabiliStaticheModificheCodice.getInstance().TornaIdModulo(
                        VariabiliStaticheModificheCodice.getInstance().getListaModuli(),
                        VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato()
                );
            }
            if (idModulo == -1) {
                if (!lista.isEmpty()) {
                    // idModulo = 1;
                // } else {
                    idModulo = lista.get(0).getIdModulo();
                    VariabiliStaticheModificheCodice.getInstance().setModuloSelezionato(
                            (String) lista.get(0).getModulo().trim()
                    );

                    VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setPrompt(
                            VariabiliStaticheModificheCodice.getInstance().getModuloSelezionato()
                    );
                }
            } else {
                for (Moduli p: lista) {
                    if (idModulo == p.getIdModulo()) {
                        VariabiliStaticheModificheCodice.getInstance().setModuloSelezionato(
                                p.getModulo().trim()
                        );
                    }
                }
            }
            VariabiliStaticheModificheCodice.getInstance().setIdModulo(idModulo);

            db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
            db.ModificaUltimeSelezioni();
            VariabiliStaticheModificheCodice.getInstance().RicaricaSezioni(context, db);

            // VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
            // VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModulo().setVisibility(LinearLayout.VISIBLE);

            if (!VariabiliStaticheModificheCodice.getInstance().getListaModuli().isEmpty()) {
                VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.VISIBLE);
            } else {
                VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);
            }

            VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.VISIBLE);

            VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE);
        } else {
            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModulo().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);

            VariabiliStaticheModificheCodice.getInstance().setListaModuli(new ArrayList<>());
            VariabiliStaticheModificheCodice.getInstance().getSpnModulo().setVisibility(LinearLayout.GONE);

            GestioneSpinner.getInstance().GestioneSpinnerModuli(context);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);

            VariabiliStaticheModificheCodice.getInstance().setListaSezioni(new ArrayList<>());
            VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

            GestioneSpinner.getInstance().GestioneSpinnerSezioni(context);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE);
        }
    }

    private void fRitornaSezioni(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna sezioni", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        // [{"idProgetto": 1,"idModulo": 2,"idSezione": 2,"Sezione": "Backup"},
        List<Sezioni> lista = new ArrayList<>();
        String resultSenzaQuadre = result.substring(1, result.length() - 1);
        if (!resultSenzaQuadre.isEmpty()) {
            String[] righe = resultSenzaQuadre.split("\\},\\{", -1);
            for (String obj : righe) {
                obj = obj.replace("{", "").replace("}", "");
                String[] fields = obj.split(",", -1);

                Sezioni p = new Sezioni();
                String[] keyValue = fields[0].split(":", -1);
                String value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdProgetto(Integer.parseInt(value));

                keyValue = fields[1].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdModulo(Integer.parseInt(value));

                keyValue = fields[2].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdSezione(Integer.parseInt(value));

                keyValue = fields[3].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                try {
                    String decoded = URLDecoder.decode(value, "UTF-8");
                    decoded = UtilitiesGlobali.getInstance().MetteMaiuscoleDopoOgniPunto(SistemaTestoPerRitorno(decoded));
                    p.setSezione(decoded);
                } catch (UnsupportedEncodingException e) {
                    p.setSezione("ERROR");
                }

                lista.add(p);
            }

            VariabiliStaticheModificheCodice.getInstance().setListaSezioni(lista);

            GestioneSpinner.getInstance().GestioneSpinnerSezioni(context);

            int idSezione = 1;
            if (!VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata().isEmpty()) {
                idSezione = VariabiliStaticheModificheCodice.getInstance().TornaIdSezione(
                        VariabiliStaticheModificheCodice.getInstance().getListaSezioni(),
                        VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata()
                );
            }
            if (idSezione == -1) {
                if (lista.isEmpty()) {
                    idSezione = 1;
                } else {
                    idSezione = lista.get(0).getIdModulo();
                    VariabiliStaticheModificheCodice.getInstance().setSezioneSelezionata(
                            (String) lista.get(0).getSezione().trim()
                    );

                    VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setPrompt(
                            VariabiliStaticheModificheCodice.getInstance().getSezioneSelezionata()
                    );
                }
            } else {
                for (Sezioni p: lista) {
                    if (idSezione == p.getIdSezione()) {
                        VariabiliStaticheModificheCodice.getInstance().setSezioneSelezionata(
                                p.getSezione().trim()
                        );
                    }
                }
            }
            VariabiliStaticheModificheCodice.getInstance().setIdSezione(idSezione);

            db_dati_modifiche_codice db = new db_dati_modifiche_codice(context);
            db.ModificaUltimeSelezioni();
            VariabiliStaticheModificheCodice.getInstance().RicaricaModifiche(context, db);

            /* VariabiliStaticheModificheCodice.getInstance().getImgModificaProgetto().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaProgetto().setVisibility(LinearLayout.VISIBLE); */

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.VISIBLE);

            if (!VariabiliStaticheModificheCodice.getInstance().getListaSezioni().isEmpty()) {
                VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.VISIBLE);
            } else {
                VariabiliStaticheModificheCodice.getInstance().getImgModificaSezioni().setVisibility(LinearLayout.GONE);
                VariabiliStaticheModificheCodice.getInstance().getImgEliminaSezioni().setVisibility(LinearLayout.GONE);
            }

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeSezione().setVisibility(LinearLayout.VISIBLE);
            VariabiliStaticheModificheCodice.getInstance().getImgModificaModulo().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getImgEliminaModulo().setVisibility(LinearLayout.GONE);

            VariabiliStaticheModificheCodice.getInstance().setListaSezioni(new ArrayList<>());
            VariabiliStaticheModificheCodice.getInstance().getSpnSezione().setVisibility(LinearLayout.GONE);

            GestioneSpinner.getInstance().GestioneSpinnerSezioni(context);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.GONE);
        }
    }

    private void fRitornaModifiche(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna modifiche", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        // String idStato = "";
        // if (VariabiliStaticheModificheCodice.getInstance().getSwcSoloAperti().isChecked()) {
        //     idStato = "0";
        // }

        List<Modifiche> lista = new ArrayList<>();
        String resultSenzaQuadre = result.substring(1, result.length() - 1);
        if (!resultSenzaQuadre.isEmpty()) {
            String[] righe = resultSenzaQuadre.split("\\},\\{", -1);
            for (String obj : righe) {
                obj = obj.replace("{", "").replace("}", "");
                String[] fields = obj.split(",", -1);

                Modifiche p = new Modifiche();
                String[] keyValue = fields[0].split(":", -1);
                String value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdProgetto(Integer.parseInt(value));

                keyValue = fields[1].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdModulo(Integer.parseInt(value));

                keyValue = fields[2].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdSezione(Integer.parseInt(value));

                keyValue = fields[3].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                p.setIdModifica(Integer.parseInt(value));

                keyValue = fields[4].split(":", -1);
                value = keyValue[1].replaceAll("\"", "").trim();
                try {
                    String decoded = URLDecoder.decode(value, "UTF-8");
                    decoded = UtilitiesGlobali.getInstance().MetteMaiuscoleDopoOgniPunto(SistemaTestoPerRitorno(decoded));
                    p.setModifica(decoded);
                } catch (UnsupportedEncodingException e) {
                    p.setModifica("ERROR");
                }

                keyValue = fields[5].split(":", -1);
                try {
                    value = keyValue[1].replaceAll("\"", "").trim();
                    p.setIdStato(Integer.parseInt(value));
                } catch (Exception e) {
                    p.setIdStato(-1);
                }

                lista.add(p);
            }

            VariabiliStaticheModificheCodice.getInstance().setListaModifiche(lista);

            // GestioneSpinner.getInstance().GestioneSpinnerModifiche(context);

            VariabiliStaticheModificheCodice.getInstance().PrendeNumeroModifiche(context);

            AdapterListenerModificheCodice customAdapterT = new AdapterListenerModificheCodice(
                    context,
                    VariabiliStaticheModificheCodice.getInstance().getListaModifiche());
            VariabiliStaticheModificheCodice.getInstance().setAdapterModifiche(customAdapterT);
            VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setAdapter(customAdapterT);

            VariabiliStaticheModificheCodice.getInstance().getImgAggiungeModifica().setVisibility(LinearLayout.VISIBLE);

            VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setVisibility(LinearLayout.VISIBLE);

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    VariabiliStaticheModificheCodice.getInstance().setEseguitaLetturaIniziale(true);
                }
            };
            handlerTimer.postDelayed(rTimer, 1000);
        } else {
            VariabiliStaticheModificheCodice.getInstance().getLstModifiche().setVisibility(LinearLayout.GONE);
        }
    }

    private void fEliminaStato(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Elimina stato", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaStati(DaGestioneStati);
            }
        };
        handlerTimer.postDelayed(rTimer, 100);

        UtilitiesGlobali.getInstance().ApreToast(context, "Stato eliminato");
    }

    private void fEliminaProgetto(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Elimina progetto", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaProgetti();
            }
        };
        handlerTimer.postDelayed(rTimer, 100);

        UtilitiesGlobali.getInstance().ApreToast(context, "Progetto eliminato");
    }

    private void fEliminaModulo(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Elimina modulo", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaModuli(
                        String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdProgetto())
                );
            }
        };
        handlerTimer.postDelayed(rTimer, 100);

        UtilitiesGlobali.getInstance().ApreToast(context, "Modulo eliminato");
    }

    private void fEliminaSezione(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Elimina sezione", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaSezioni(
                        String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdProgetto()),
                        String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdModulo())
                );
            }
        };
        handlerTimer.postDelayed(rTimer, 100);

        UtilitiesGlobali.getInstance().ApreToast(context, "Sezione eliminata");
    }

    private void fEliminaModifica(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Elimina modifica", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaModifiche(
                        String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdProgetto()),
                        String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdModulo()),
                        String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdSezione())
                );
            }
        };
        handlerTimer.postDelayed(rTimer, 100);

        UtilitiesGlobali.getInstance().ApreToast(context, "Modifica eliminata");
    }

    private void fInserisceModificaStato(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Inserisce modifica stato", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaStati(DaGestioneStati);
            }
        };
        handlerTimer.postDelayed(rTimer, 100);

        UtilitiesGlobali.getInstance().ApreToast(context, "Stato inserito/modificato");

        if (DaGestioneStati) {
            VariabiliStaticheModificheCodice.getInstance().getLayGestioneStato().setVisibility(LinearLayout.GONE);
            VariabiliStaticheModificheCodice.getInstance().getEdtGestioneStato().setText("");
            VariabiliStaticheModificheCodice.getInstance().setIdGestioneStato(-1);
        }
    }

    private void fInserisceModificaProgetto(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Inserisce modifica progetto", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (VariabiliStaticheModificheCodice.getInstance().isStaSalvandoTutto()) {
                    ContinuaSalvataggio();
                } else {
                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.RitornaProgetti();
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    private void fInserisceModificaModulo(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Inserisce modifica modulo", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (VariabiliStaticheModificheCodice.getInstance().isStaSalvandoTutto()) {
                    ContinuaSalvataggio();
                } else {
                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.RitornaModuli(
                            String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdProgetto())
                    );
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    private void fInserisceModificaSezione(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Inserisce modifica sezione", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (VariabiliStaticheModificheCodice.getInstance().isStaSalvandoTutto()) {
                    ContinuaSalvataggio();
                } else {
                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.RitornaSezioni(
                            String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdProgetto()),
                            String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdModulo())
                    );
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    private void fInserisceModificaModifica(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Inserisce modifica modifica", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (VariabiliStaticheModificheCodice.getInstance().isStaSalvandoTutto()) {
                    ContinuaSalvataggio();
                } else {
                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.RitornaModifiche(
                            String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdProgetto()),
                            String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdModulo()),
                            String.valueOf(VariabiliStaticheModificheCodice.getInstance().getIdSezione())
                    );
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    private void fEliminaFileRemoto(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Elimina file GPS", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaFilesRemoti();
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    private void fRitornaFilesGPS(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna files GPS", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        List<StrutturaNomeFileRemoti> files = new ArrayList<>();
        String[] filesGPS = result.split(";", -1);
        for (String f : filesGPS) {
            if (!f.isEmpty()) {
                String[] ff = f.split("\\\\", -1);
                String Nome = ff[ff.length - 1];

                StrutturaNomeFileRemoti s = new StrutturaNomeFileRemoti();
                s.setPath(f);
                s.setNomeFile(Nome);

                files.add(s);
            }
        }

        files.sort((t1, t2) -> t2.getNomeFile().compareTo(t1.getNomeFile()));

        AdapterListenerFilesRemoti customAdapterT = new AdapterListenerFilesRemoti(context,
                files);
        VariabiliStaticheGPS.getInstance().getLstFilesRemoti().setAdapter(customAdapterT);
    }

    private void fImporta(String result) {
        boolean ritorno = ControllaRitorno("Ritorno importa", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            byte[] decodedBytes;

            switch (this.TipoFile) {
                case "MODIFICHE":
                    String PathModifiche = context.getFilesDir() + "/DB/dati_modifiche2.db";
                    String PathModificheFinali = context.getFilesDir() + "/DB/dati_modifiche.db";
                    decodedBytes = Base64.getDecoder().decode(result);
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

                    VariabiliStaticheModificheCodice.getInstance().Attende(false);
                    break;
                case "BACKUP":
                    Files.getInstance().CreaCartelle(context.getFilesDir() + "/Appoggio/Backup");
                    String PathBackup = context.getFilesDir() + "/Appoggio/Backup/Backup.zip";
                    decodedBytes = Base64.getDecoder().decode(result);
                    try (FileOutputStream fos = new FileOutputStream(PathBackup)) {
                        fos.write(decodedBytes);
                        fos.close();

                        File fileZip = new File(PathBackup);
                        File fileUnzip = new File(context.getFilesDir() + "/Appoggio/Backup");
                        if (unzip(fileZip, fileUnzip)) {
                            Files.getInstance().EliminaFileUnico(PathBackup);

                            String PathOrigine = context.getFilesDir() + "/Appoggio/Backup";
                            String PathDestinazione = context.getFilesDir() + "/DB/";

                            File file = new File(PathOrigine);
                            File[] filetti = file.listFiles();
                            if (filetti != null) {
                                for (File f : filetti) {
                                    String FilettoOrigine = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                                    String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
                                    String FilettoDestinazione = PathDestinazione + Nome;

                                    Files.getInstance().EliminaFileUnico(FilettoDestinazione);
                                    Files.getInstance().CopiaFile(FilettoOrigine, FilettoDestinazione);
                                    if (Files.getInstance().EsisteFile(FilettoDestinazione)) {
                                        Files.getInstance().EliminaFileUnico(FilettoOrigine);
                                    }
                                }
                                UtilitiesGlobali.getInstance().ApreToast(context, "Import effettuato");

                                Intent mStartActivity = new Intent(context, MainStart.class);
                                int mPendingIntentId = 123654;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(
                                        context,
                                        mPendingIntentId,
                                        mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
                                );
                                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);
                            }
                        } else {
                            UtilitiesGlobali.getInstance().ApreToast(context, "ERROR: Unzip non riuscito");
                        }
                    } catch (IOException e) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "ERROR: " + e.getMessage());
                    }

                    UtilityBackup.getInstance().Attende(false);
                    break;
                case "GPS":
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Modifiche");
                    builder.setMessage("Si vogliono eliminare i dati presenti in archivio per le date caricate ?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EsegueCaricamento(result, true);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EsegueCaricamento(result, false);
                        }
                    });

                    builder.show();
                    break;
            }
        }
    }

    private void EsegueCaricamento(String result, boolean EliminaVecchiDati) {
        byte[] decodedBytes;

        Files.getInstance().CreaCartelle(context.getFilesDir() + "/Appoggio/BackupGPS");
        File dir = new File(context.getFilesDir() + "/Appoggio/BackupGPS");
        for(File file: Objects.requireNonNull(dir.listFiles()))
            if (!file.isDirectory())
                file.delete();

        String PathGPS = context.getFilesDir() + "/Appoggio/BackupGPS/BackupGPS.zip";
        decodedBytes = Base64.getDecoder().decode(result);
        try (FileOutputStream fos = new FileOutputStream(PathGPS)) {
            fos.write(decodedBytes);
            fos.close();

            File fileZip = new File(PathGPS);
            File fileUnzip = new File(context.getFilesDir() + "/Appoggio/BackupGPS");
            if (unzip(fileZip, fileUnzip)) {
                Files.getInstance().EliminaFileUnico(PathGPS);
            }

            int posizioniAggiunte = 0;
            db_dati_gps db = new db_dati_gps(context);
            String ultimaData = "";
            for(File file: Objects.requireNonNull(dir.listFiles())) {
                if (!file.isDirectory()) {
                    String Filetto = file.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                    String Nome = file.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
                    String AppoNome = Nome.replace("DatiGPS_", "").replace(".csv", "");
                    String[] d = AppoNome.split("-", -1);
                    String Data = d[2] + "/" + d[1] + "/" + d[0];
                    ultimaData = Data;
                    if (EliminaVecchiDati) {
                        db.EliminaPosizioni(Data);
                    }
                    String dati = Files.getInstance().LeggeFileUnico(Filetto);
                    String[] righe = dati.split("\n", -1);
                    for (String r : righe) {
                        String[] c = r.split(";", -1);

                        StrutturaGps s = new StrutturaGps();
                        s.setData(c[0]);
                        s.setOra(c[1]);
                        s.setLat(Double.parseDouble(c[2]));
                        s.setLon(Double.parseDouble(c[3]));
                        s.setSpeed(Float.parseFloat(c[4]));
                        s.setAltitude(Double.parseDouble(c[5]));
                        s.setAccuracy(Float.parseFloat(c[6]));
                        s.setDistanza(Float.parseFloat(c[7]));
                        s.setWifi(c[8].equals("S"));
                        s.setLivelloSegnale(Integer.parseInt(c[9]));
                        s.setTipoSegnale(c[10]);
                        s.setLevel(Integer.parseInt(c[11]));
                        s.setDirezione(Float.parseFloat(c[12]));

                        db.AggiungePosizione(s);
                        posizioniAggiunte++;
                    }

                    Files.getInstance().EliminaFileUnico(Filetto);
                }
            };
            if (!ultimaData.isEmpty()) {
                CalcoloVelocita cv = new CalcoloVelocita();
                UtilityGPS.getInstance().DisegnaPath(context, cv, ultimaData);
            }

            UtilitiesGlobali.getInstance().ApreToast(context, "Posizioni aggiunte: " + posizioniAggiunte);
        } catch (IOException e) {
            UtilitiesGlobali.getInstance().ApreToast(context, "ERROR: " + e.getMessage());
        }
        UtilityGPS.getInstance().ImpostaAttesa(false);
    }

    private void fEsporta(String result) {
        boolean ritorno = ControllaRitorno("Ritorno esporta", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        switch (this.TipoFile) {
            case "MODIFICHE":
                UtilitiesGlobali.getInstance().ApreToast(context, "DB esportato");
                VariabiliStaticheModificheCodice.getInstance().Attende(false);
                break;
            case "BACKUP":
                UtilitiesGlobali.getInstance().ApreToast(context, "DB esportato");
                UtilityBackup.getInstance().Attende(false);
                break;
            case "GPS":
                UtilitiesGlobali.getInstance().ApreToast(context, "Files GPS esportato");
                UtilityGPS.getInstance().ImpostaAttesa(false);
                break;
        }

        String Messaggio = "Dati Esportati";

        if (EseguePulizia) {
            String ultimaData = "";

            db_dati_gps db = new db_dati_gps(context);
            String[] date = listaDate.split(";", -1);
            for (String d: date) {
                if (!d.isEmpty()) {
                    db.EliminaPosizioni(d);
                    ultimaData = d;
                }
            }
            db.ChiudeDB();

            Messaggio += " ed eliminati";

            VariabiliStaticheGPS.getInstance().getMappa().PuliscePunti();
            CalcoloVelocita cv = new CalcoloVelocita();
            UtilityGPS.getInstance().DisegnaPath(context, cv, ultimaData);
            GestioneNotificheTasti.getInstance().AggiornaNotifica();
        }

        UtilitiesGlobali.getInstance().ApreToast(context, Messaggio);
    }
}
