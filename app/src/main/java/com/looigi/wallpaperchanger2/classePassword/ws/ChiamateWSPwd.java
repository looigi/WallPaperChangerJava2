package com.looigi.wallpaperchanger2.classePassword.ws;

import android.content.Context;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classePassword.UtilityPassword;
import com.looigi.wallpaperchanger2.classePassword.VariabiliStatichePWD;
import com.looigi.wallpaperchanger2.classePassword.db_dati_password;
import com.looigi.wallpaperchanger2.classePassword.strutture.StrutturaPassword;
import com.looigi.wallpaperchanger2.classePassword.strutture.StrutturaUtente;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSPwd implements TaskDelegate {
    private LetturaWSAsincronaPwd bckAsyncTask;

    private final String RadiceWS = VariabiliStatichePWD.getInstance().getUrlWS() + "/";
    private String ws = "Service1.asmx/";
    private String NS="http://passWS.it/";
    private String SA="http://passWS.it/";
    private String TipoOperazione = "";
    private Context context;

    public ChiamateWSPwd(Context context) {
        this.context = context;
    }

    public void EliminaPassword(String idRiga) {
        String Urletto="EliminaPassword?" +
                "idUtente=" + VariabiliStatichePWD.getInstance().getIdUtente() + "&" +
                "idRiga=" + idRiga;
        boolean ApriDialog = true;
        TipoOperazione = "EliminaPassword";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                15000,
                ApriDialog);
    }

    public void ModificaPassword(StrutturaPassword s) {
        String Urletto="ModificaPassword?" +
                "idUtente=" + VariabiliStatichePWD.getInstance().getIdUtente() + "&" +
                "idRiga=" + s.getIdRiga() + "&" +
                "Sito=" + s.getSito() + "&" +
                "Utenza=" + s.getUtenza() + "&" +
                "Password=" + s.getPassword() + "&" +
                "Notelle=" + s.getNote() + "&" +
                "Indirizzo=" + s.getIndirizzo();
        boolean ApriDialog = true;
        TipoOperazione = "ModificaPassword";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                15000,
                ApriDialog);
    }

    public void ScriveNuovaPassword(StrutturaPassword s) {
        String Urletto="ScriveNuovaPassword?" +
                "idUtente=" + VariabiliStatichePWD.getInstance().getIdUtente() + "&" +
                "Sito=" + s.getSito() + "&" +
                "Utenza=" + s.getUtenza() + "&" +
                "Password=" + s.getPassword() + "&" +
                "Notelle=" + s.getNote() + "&" +
                "Indirizzo=" + s.getIndirizzo();
        boolean ApriDialog = true;
        TipoOperazione = "ScriveNuovaPassword";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                15000,
                ApriDialog);
    }

    public void RitornaPasswordLogin(String NickName) {
        String Urletto="RitornaPassword?" +
                "Nick=" + NickName;
        boolean ApriDialog = true;
        TipoOperazione = "RitornaPassword";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                15000,
                ApriDialog);
    }

    public void RitornaIdUtente(String NickName) {
        String Urletto="RitornaIDUtente?" +
                "Nick=" + NickName;
        boolean ApriDialog = true;
        TipoOperazione = "RitornaIDUtente";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                15000,
                ApriDialog);
    }

    public void SalvaNuovoUtente(StrutturaUtente s) {
        String Urletto="InserisceUtente?" +
                "Nick=" + s.getNick() + "&" +
                "Nome=" + s.getNome() + "&" +
                "Cognome=" + s.getCognome() + "&" +
                "Password=" + s.getPassword() + "&" +
                "Sesso=M&" +
                "Eta=0&" +
                "EMail=xx@xx.x&" +
                "Datella=1972-02-26&" +
                "Nazione=Italia";
        boolean ApriDialog = true;
        TipoOperazione = "InserisceUtente";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                15000,
                ApriDialog);
    }

    public void CaricaPassword() {
        String Urletto="RitornaStringaPassword?" +
                "idUtente=" + VariabiliStatichePWD.getInstance().getIdUtente() + "&" +
                "Stringa=" + VariabiliStatichePWD.getInstance().getRicerca();
        boolean ApriDialog = true;
        TipoOperazione = "RitornaStringaPassword";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                15000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        bckAsyncTask = new LetturaWSAsincronaPwd(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this);
        bckAsyncTask.execute(Urletto);
    }

    @Override
    public void TaskCompletionResult(String result) {
        // Log.getInstance().ScriveLog("Ritorno WS " + TipoOperazione + ". OK");

        switch (TipoOperazione) {
            case "EliminaPassword":
                fEliminaPassword(result);
                break;
            case "ModificaPassword":
                fModificaPassword(result);
                break;
            case "ScriveNuovaPassword":
                fScriveNuovaPassword(result);
                break;
            case "RitornaIDUtente":
                fRitornaIDUtente(result);
                break;
            case "RitornaPassword":
                fRitornaPassword(result);
                break;
            case "InserisceUtente":
                fInserisceUtente(result);
                break;
            case "RitornaStringaPassword":
                fRitornaStringaPassword(result);
                break;
        }
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }

    private void fEliminaPassword(String result) {
        if (result.contains("ERROR:")) {
            UtilityPassword.getInstance().VisualizzaMessaggio(context, result);
        } else {
        }
    }

    private void fModificaPassword(String result) {
        if (result.contains("ERROR:")) {
            UtilityPassword.getInstance().VisualizzaMessaggio(context, result);
        } else {
        }
    }

    private void fScriveNuovaPassword(String result) {
        if (result.contains("ERROR:")) {
            UtilityPassword.getInstance().VisualizzaMessaggio(context, result);
        } else {
        }
    }

    private void fRitornaPassword(String result) {
        if (result.contains("ERROR:")) {
            UtilityPassword.getInstance().VisualizzaMessaggio(context, result);
        } else {
            if (result.equals(VariabiliStatichePWD.getInstance().getPasswordAppoggio())) {
                ChiamateWSPwd ws = new ChiamateWSPwd(context);
                ws.RitornaIdUtente(VariabiliStatichePWD.getInstance().getNomeUtenteAppoggio());
            } else {
                UtilityPassword.getInstance().VisualizzaMessaggio(context, "Password non valida");
            }
        }
    }

    private void fRitornaIDUtente(String result) {
        if (result.contains("ERROR:")) {
            UtilityPassword.getInstance().VisualizzaMessaggio(context, result);
        } else {
            if (result.equals("-1")) {
                UtilityPassword.getInstance().VisualizzaMessaggio(context, "Utente non valido");
            } else {
                if (result.equals("-2")) {
                    UtilityPassword.getInstance().VisualizzaMessaggio(context, "Utente non presente");
                } else {
                    String[] campi = result.split(";");
                    StrutturaUtente s = new StrutturaUtente();
                    s.setIdUtente(Integer.parseInt(campi[0]));
                    s.setNick(campi[1]);
                    s.setNome(campi[2]);
                    s.setCognome(campi[3]);
                    s.setPassword(campi[4]);

                    VariabiliStatichePWD.getInstance().setIdUtente(s.getIdUtente());
                    db_dati_password db = new db_dati_password(context);
                    db.SalvaUtente(s, false);

                    VariabiliStatichePWD.getInstance().setNomeUtenteAppoggio("");
                    VariabiliStatichePWD.getInstance().setPasswordAppoggio("");

                    VariabiliStatichePWD.getInstance().getLaySceltaPassword().setVisibility(LinearLayout.GONE);

                    VariabiliStatichePWD.getInstance().setLoginEffettuata(true);
                }
            }
        }
    }

    private void fInserisceUtente(String result) {
        if (result.contains("ERROR:")) {
            UtilityPassword.getInstance().VisualizzaMessaggio(context, result);
        } else {
            VariabiliStatichePWD.getInstance().setIdUtente(Integer.parseInt(result));
            VariabiliStatichePWD.getInstance().setUtenteAttuale(VariabiliStatichePWD.getInstance().getStrutturaUtenteAppoggio());

            db_dati_password db = new db_dati_password(context);
            db.SalvaUtente(VariabiliStatichePWD.getInstance().getStrutturaUtenteAppoggio(), false);

            VariabiliStatichePWD.getInstance().getLayNuovoUtente().setVisibility(LinearLayout.GONE);
            VariabiliStatichePWD.getInstance().getLaySceltaPassword().setVisibility(LinearLayout.GONE);
            VariabiliStatichePWD.getInstance().setStrutturaUtenteAppoggio(null);
        }
    }

    private void fRitornaStringaPassword(String result) {
        if (result.contains("ERROR:")) {
            UtilityPassword.getInstance().VisualizzaMessaggio(context, result);
        } else {
            // Caricamento password
            db_dati_password db = new db_dati_password(context);
            List<StrutturaPassword> lista = new ArrayList<StrutturaPassword>();
            String[] righe = result.split("°");
            for (int q = 0; q < righe.length; q++) {
                // if (righe[q].isEmpty()) {
                try {
                    String[] campi = righe[q].split("§");
                    StrutturaPassword s = new StrutturaPassword();
                    s.setIdRiga(Integer.parseInt(campi[0]));
                    s.setSito(campi[1]);
                    s.setUtenza(campi[2]);
                    s.setPassword(campi[3]);
                    s.setNote(campi[4]);
                    s.setIndirizzo(campi[5]);

                    lista.add(s);

                    if (VariabiliStatichePWD.getInstance().isDeveAggiungereRigheAlDb()) {
                        db.SalvaPassword(s, false);
                    }
                } catch (Exception ignored) {

                }
                // }
            };
            VariabiliStatichePWD.getInstance().setListaPassword(lista);
            VariabiliStatichePWD.getInstance().setDeveAggiungereRigheAlDb(false);

            UtilityPassword.getInstance().RiempieArrayLista(context);
        }
    }
}
