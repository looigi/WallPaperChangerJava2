package com.looigi.wallpaperchanger2.Password;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.looigi.wallpaperchanger2.Detector.UtilityDetector;
import com.looigi.wallpaperchanger2.Password.strutture.StrutturaPassword;
import com.looigi.wallpaperchanger2.Password.strutture.StrutturaUtente;
import com.looigi.wallpaperchanger2.Password.ws.ChiamateWSPwd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class db_dati_password {
    private static final String NomeMaschera = "DB_Password";
    private String PathDB;
    private final SQLiteDatabase myDB;
    private Context context;
    
    public db_dati_password(Context context) {
        this.context = context;
        PathDB = UtilityDetector.getInstance().PrendePathDB(context);

        File f = new File(PathDB);
        try {
            f.mkdirs();
        } catch (Exception ignored) {

        }
        myDB = ApreDB();
    }

    private SQLiteDatabase ApreDB() {
        SQLiteDatabase db = null;
        try {
            String nomeDB = "dati_password.db";
            db = context.openOrCreateDatabase(
                    PathDB + nomeDB, MODE_PRIVATE, null);
        } catch (Exception e) {
            String Messaggio = "ERRORE Nell'apertura del db: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, Messaggio);
            // UtilityPassword.getInstance().VisualizzaMessaggio(context, Messaggio);
        }
        return  db;
    }

    public void CreazioneTabelle() {
        try {
            if (myDB != null) {
                String sql = "CREATE TABLE IF NOT EXISTS "
                        + "Password "
                        + " (idUtente VARCHAR, idRiga VARCHAR, Sito VARCHAR, Utenza VARCHAR, Password VARCHAR, Note VARCHAR, " +
                        "Indirizzo VARCHAR);";
                myDB.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "
                        + "Utente "
                        + " (idUtente VARCHAR, Nick VARCHAR, Nome VARCHAR, Cognome VARCHAR, Password VARCHAR);";
                myDB.execSQL(sql);
            }
        } catch (Exception e) {
            String Messaggio = "ERRORE Nella creazione delle tabelle: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, Messaggio);
            // UtilityPassword.getInstance().VisualizzaMessaggio(context, Messaggio);
        }
    }

    public void SalvaUtente(StrutturaUtente s, boolean AggiungeOnLine) {
        try {
            String SQL = "SELECT * FROM Utente Where idUtente=" + s.getIdUtente();
            Cursor c = myDB.rawQuery(SQL, null);
            c.moveToFirst();
            if (c.getCount() > 0) {
                UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Salvataggio Utente. Skippo " + s.getIdUtente() + "-" + s.getNick() + " in quanto già esistente");
            } else {
                SQL = "Insert Into Utente Values(" +
                        "'" + s.getIdUtente() + "', " +
                        "'" + s.getNick().replace("'", "''") + "', " +
                        "'" + s.getNome().replace("'", "''") + "', " +
                        "'" + s.getCognome().replace("'", "''") + "', " +
                        "'" + s.getPassword().replace("'", "''") + "' " +
                        ")";
                myDB.execSQL(SQL);

                if (AggiungeOnLine) {
                    ChiamateWSPwd ws = new ChiamateWSPwd(context);
                    ws.SalvaNuovoUtente(s);
                }
            }
        } catch (Exception e) {
            String Messaggio = "ERRORE Nel salvataggio dell'utente: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e);
            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, Messaggio);
            // UtilityPassword.getInstance().VisualizzaMessaggio(context, Messaggio);
        }
    }

    public void EliminaPassword(String idRiga, boolean AggiungeOnLine) {
        try {
            String SQL = "Delete From Password Where idUtente=" + VariabiliStatichePWD.getInstance().getIdUtente() +
                    " And idRiga=" + idRiga;
            myDB.execSQL(SQL);

            if (AggiungeOnLine) {
                ChiamateWSPwd ws = new ChiamateWSPwd(context);
                ws.EliminaPassword(idRiga);
            }
        } catch (Exception e) {
            String Messaggio = "ERRORE Nell'eliminazione della password: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, Messaggio);
            UtilityPassword.getInstance().VisualizzaMessaggio(context, Messaggio);
        }
    }

    public void ModificaPassword(StrutturaPassword s, boolean AggiungeOnLine) {
        try {
            String SQL = "Delete From Password Where idUtente=" + VariabiliStatichePWD.getInstance().getIdUtente() +
                    " And idRiga=" + s.getIdRiga();
            myDB.execSQL(SQL);

            SQL = "Insert Into Password Values(" +
                    "'" + VariabiliStatichePWD.getInstance().getIdUtente() + "', " +
                    "'" + s.getIdRiga() + "', " +
                    "'" + s.getSito().replace("'", "''") + "', " +
                    "'" + s.getUtenza().replace("'", "''") + "', " +
                    "'" + s.getPassword().replace("'", "''") + "', " +
                    "'" + s.getNote().replace("'", "''") + "', " +
                    "'" + s.getIndirizzo().replace("'", "''") + "' " +
                    ")";
            myDB.execSQL(SQL);

            if (AggiungeOnLine) {
                ChiamateWSPwd ws = new ChiamateWSPwd(context);
                ws.ModificaPassword(s);
            }
        } catch (Exception e) {
            String Messaggio = "ERRORE Nella modifica della password: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, Messaggio);
            UtilityPassword.getInstance().VisualizzaMessaggio(context, Messaggio);
        }
    }

    public void SalvaPassword(StrutturaPassword s, boolean AggiungeOnLine) {
        try {
            int id = -1;

            if (!VariabiliStatichePWD.getInstance().isDeveAggiungereRigheAlDb()) {
                String SQL = "SELECT Max(idRiga) + 1 FROM Password";
                Cursor c = myDB.rawQuery(SQL, null);
                c.moveToFirst();
                if (c.getCount() > 0) {
                    id = c.getInt(0);
                } else {
                    String Messaggio = "ERRORE Nel rilevamento dell'id della password";
                    UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, Messaggio);
                    UtilityPassword.getInstance().VisualizzaMessaggio(context, Messaggio);
                    return;
                }
            } else {
                id = s.getIdRiga();
            }

            String SQL = "Insert Into Password Values(" +
                    "'" + VariabiliStatichePWD.getInstance().getIdUtente() + "', " +
                    "'" + id + "', " +
                    "'" + s.getSito().replace("'", "''") + "', " +
                    "'" + s.getUtenza().replace("'", "''") + "', " +
                    "'" + s.getPassword().replace("'", "''") + "', " +
                    "'" + s.getNote().replace("'", "''") + "', " +
                    "'" + s.getIndirizzo().replace("'", "''") + "' " +
                    ")";
            myDB.execSQL(SQL);

            if (AggiungeOnLine) {
                ChiamateWSPwd ws = new ChiamateWSPwd(context);
                ws.ScriveNuovaPassword(s);
            }
        } catch (Exception e) {
            String Messaggio = "ERRORE Nel salvataggio della password: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
            UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, Messaggio);
            UtilityPassword.getInstance().VisualizzaMessaggio(context, Messaggio);
        }
    }

    public boolean LeggeUtente() {
        String SQL = "SELECT * FROM Utente";
        Cursor c = myDB.rawQuery(SQL, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            StrutturaUtente s = new StrutturaUtente();
            s.setIdUtente(Integer.parseInt(c.getString(0)));
            s.setNick(c.getString(1));
            s.setNome(c.getString(2));
            s.setCognome(c.getString(3));
            s.setPassword(c.getString(4));
            VariabiliStatichePWD.getInstance().setUtenteAttuale(s);
            VariabiliStatichePWD.getInstance().setIdUtente(s.getIdUtente());

            return true;
        } else {
            return false;
        }
    }

    public boolean LeggePasswords() {
        boolean Ritorno = false;

        String Altro = "";
        if (!VariabiliStatichePWD.getInstance().getRicerca().isEmpty()) {
            String Ricerca = VariabiliStatichePWD.getInstance().getRicerca().replace("'", "''");
            Altro = "And (Sito Like '%" + Ricerca + "%' Or Note Like '%" + Ricerca + "%' Or Indirizzo Like '%" + Ricerca + "%')";
        }
        String SQL = "SELECT * FROM Password Where idUtente=" + VariabiliStatichePWD.getInstance().getIdUtente() + " " + Altro;
        UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Legge Passwords sul db: " + SQL);

        Cursor c = myDB.rawQuery(SQL, null);
        c.moveToFirst();
        List<StrutturaPassword> lista = new ArrayList<>();
        if (c.getCount() > 0) {
            do {
                StrutturaPassword s = new StrutturaPassword();
                s.setIdRiga(Integer.parseInt(c.getString(1)));
                s.setSito(c.getString(2));
                s.setUtenza(c.getString(3));
                s.setPassword(c.getString(4));
                s.setNote(c.getString(5));
                s.setIndirizzo(c.getString(6));
                lista.add(s);

                Ritorno = true;
            } while (c.moveToNext());
        }
        VariabiliStatichePWD.getInstance().setListaPassword(lista);

        return Ritorno;
    }
}
