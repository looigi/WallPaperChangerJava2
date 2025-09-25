package com.looigi.wallpaperchanger2.classePassword;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePassword.strutture.StrutturaPassword;
import com.looigi.wallpaperchanger2.classePassword.strutture.StrutturaUtente;
import com.looigi.wallpaperchanger2.classePassword.ws.ChiamateWSPwd;
import com.looigi.wallpaperchanger2.utilities.BiometricManagerSingleton;

public class MainPassword extends FragmentActivity {
    private static final String NomeMaschera = "MainPassword";
    private Context context;
    private FragmentActivity act;
    private Runnable rAgg;
    private Handler handlerAgg;
    private Runnable rAgg2;
    private Handler handlerAgg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_password);

        this.context = this;
        this.act = this;
        VariabiliStatichePWD.getInstance().setContext(this);
        
        Inizio();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }

    private BiometricManagerSingleton bioManager;
    private EditText edtUtente;

    private void Inizio() {
        // VariabiliStatichePWD.getInstance().setRetePresente(true);

        UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Apro db");
        db_dati_password db = new db_dati_password(context);
        UtilityPassword.getInstance().ScriveLog(context, NomeMaschera, "Creo tabelle");
        db.CreazioneTabelle();

        LinearLayout laySceltaPWD = (LinearLayout) findViewById(R.id.laySceltaPassword);
        laySceltaPWD.setVisibility(LinearLayout.GONE);
        VariabiliStatichePWD.getInstance().setLaySceltaPassword(laySceltaPWD);

        LinearLayout layNuovoUtente = (LinearLayout) findViewById(R.id.layNuovoUtente);
        layNuovoUtente.setVisibility(LinearLayout.GONE);
        VariabiliStatichePWD.getInstance().setLayNuovoUtente(layNuovoUtente);

        VariabiliStatichePWD.getInstance().setLayPassword(findViewById(R.id.layEditPassword));
        VariabiliStatichePWD.getInstance().getLayPassword().setVisibility(LinearLayout.GONE);

        ImageView layRicerca = (ImageView) findViewById(R.id.imgRicerca);
        layRicerca.setVisibility(LinearLayout.GONE);

        edtUtente = (EditText) findViewById(R.id.edtUtenteLogin);
        EditText edtPassword = (EditText) findViewById(R.id.edtPasswordLogin);

        boolean utenza = db.LeggeUtente();
        if (utenza) {
            edtUtente.setText(VariabiliStatichePWD.getInstance().getUtenteAttuale().getNick());
        } else {
            edtUtente.setText("");
        }
        utenza = false;

        if (!utenza) {
            // Non esiste l'utente sul db
            laySceltaPWD.setVisibility(LinearLayout.VISIBLE);

            ImageView imgLogin = (ImageView) findViewById(R.id.imgLogin);
            imgLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String NomeUtente = edtUtente.getText().toString();
                    String Password = edtPassword.getText().toString();

                    VariabiliStatichePWD.getInstance().setNomeUtenteAppoggio(NomeUtente);
                    VariabiliStatichePWD.getInstance().setPasswordAppoggio(Password);

                    ChiamateWSPwd ws = new ChiamateWSPwd(context);
                    ws.RitornaPasswordLogin(NomeUtente);
                }
            });

            ImageView imgNuovoUtente = (ImageView) findViewById(R.id.imgNuovoUtente);
            imgNuovoUtente.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStatichePWD.getInstance().getLayNuovoUtente().setVisibility(LinearLayout.VISIBLE);

                    ImageView imgAnnullaNuovo = (ImageView) findViewById(R.id.imgAnnullaNuovo);
                    imgAnnullaNuovo.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            VariabiliStatichePWD.getInstance().getLayNuovoUtente().setVisibility(LinearLayout.GONE);
                        }
                    });

                    ImageView imgSalvaNuovo = (ImageView) findViewById(R.id.imgSalvaNuovo);
                    imgSalvaNuovo.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            EditText edtNick = (EditText) findViewById(R.id.edtUtenteNuovo);
                            EditText edtNome = (EditText) findViewById(R.id.edtNomeNuovo);
                            EditText edtCognome = (EditText) findViewById(R.id.edtCognomeNuovo);
                            EditText edtPassword = (EditText) findViewById(R.id.edtPasswordNuovo);
                            String Nick = edtNick.getText().toString();
                            String Nome = edtNome.getText().toString();
                            String Cognome = edtCognome.getText().toString();
                            String Password = edtPassword.getText().toString();

                            StrutturaUtente s = new StrutturaUtente();
                            s.setNick(Nick);
                            s.setNome(Nome);
                            s.setCognome(Cognome);
                            s.setPassword(Password);

                            VariabiliStatichePWD.getInstance().setStrutturaUtenteAppoggio(s);

                            ChiamateWSPwd ws = new ChiamateWSPwd(context);
                            ws.SalvaNuovoUtente(s);
                        }
                    });
                }
            });

            ImageView imgAnnullaLogin = (ImageView) findViewById(R.id.imgAnnullaLogin);
            imgAnnullaLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                    System.exit(1);
                }
            });

            VariabiliStatichePWD.getInstance().setLoginEffettuata(false);
            handlerAgg2 = new Handler();
            rAgg2 = new Runnable() {
                public void run() {
                    if (!VariabiliStatichePWD.getInstance().isLoginEffettuata()) {
                        handlerAgg2.postDelayed(rAgg2, 100);
                    } else {
                        ContinuaEsecuzione();
                    }
                }
            };
            handlerAgg2.postDelayed(rAgg2, 100);
        } else {
            // Login eliminato
            ContinuaEsecuzione();
        }

        ImageView imgSplash = (ImageView) findViewById(R.id.imgSplash);
        imgSplash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgSplash.setVisibility(LinearLayout.GONE);
            }
        });
        handlerAgg = new Handler();
        rAgg = new Runnable() {
            public void run() {
                imgSplash.setVisibility(LinearLayout.GONE);

                if (!edtUtente.getText().toString().isEmpty()) {
                    // Impronta digitale
                    bioManager = BiometricManagerSingleton.getInstance(context);

                    int can = bioManager.canAuthenticate();
                    if (can == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS) {
                        bioManager.authenticate(act, "Accedi", "Autenticazione con impronta o volto", authCallback);
                    } else {
                        String msg;
                        switch (can) {
                            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                                msg = "Dispositivo senza hardware biometrico";
                                break;
                            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                                msg = "Hardware biometrico non disponibile";
                                break;
                            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                                msg = "Nessuna impronta/biometria registrata. Registra una nella impostazioni.";
                                break;
                            default:
                                msg = "Impossibile usare biometria";
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                    }
                    // Impronta digitale
                } else {
                    Toast.makeText(context, "Inserire l'utenza a mano, la prima volta", Toast.LENGTH_LONG).show();
                }
            }
        };
        handlerAgg.postDelayed(rAgg, 3000);
    }

    private final BiometricPrompt.AuthenticationCallback authCallback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            runOnUiThread(() -> {
                Toast.makeText(context, "Autenticazione OK", Toast.LENGTH_SHORT).show();

                String NomeUtente = edtUtente.getText().toString();
                VariabiliStatichePWD.getInstance().setNomeUtenteAppoggio(NomeUtente);

                ChiamateWSPwd ws = new ChiamateWSPwd(context);
                ws.RitornaIdUtente(VariabiliStatichePWD.getInstance().getNomeUtenteAppoggio());
            });
            // Procedi con operazione protetta
        }

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            runOnUiThread(() -> Toast.makeText(context, "Errore: " + errString, Toast.LENGTH_SHORT).show());
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            runOnUiThread(() -> Toast.makeText(context, "Autenticazione fallita", Toast.LENGTH_SHORT).show());
        }
    };

    private void ContinuaEsecuzione() {
        db_dati_password db = new db_dati_password(context);

        ImageView layRicerca = (ImageView) findViewById(R.id.imgRicerca);
        layRicerca.setVisibility(LinearLayout.VISIBLE);

        ListView lstPassword = (ListView) findViewById(R.id.lstPassword);
        VariabiliStatichePWD.getInstance().setLstPassword(lstPassword);

        VariabiliStatichePWD.getInstance().setTxtQuante(findViewById(R.id.txtQuante));

        VariabiliStatichePWD.getInstance().setTxtId(findViewById(R.id.txtIdEdit));
        VariabiliStatichePWD.getInstance().setEdtSito(findViewById(R.id.edtSito));
        VariabiliStatichePWD.getInstance().setEdtUtenza(findViewById(R.id.edtUtenza));
        VariabiliStatichePWD.getInstance().setEdtPassword(findViewById(R.id.edtPassword));
        VariabiliStatichePWD.getInstance().setEdtNote(findViewById(R.id.edtNote));
        VariabiliStatichePWD.getInstance().setEdtIndirizzo(findViewById(R.id.edtIndirizzo));

        ImageView imgSalvaPWD = (ImageView) findViewById(R.id.imgSalvaPassword);
        imgSalvaPWD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_password db = new db_dati_password(context);
                StrutturaPassword s = new StrutturaPassword();
                s.setSito(VariabiliStatichePWD.getInstance().getEdtSito().getText().toString());
                s.setUtenza(VariabiliStatichePWD.getInstance().getEdtUtenza().getText().toString());
                s.setPassword(VariabiliStatichePWD.getInstance().getEdtPassword().getText().toString());
                s.setNote(VariabiliStatichePWD.getInstance().getEdtNote().getText().toString());
                s.setIndirizzo(VariabiliStatichePWD.getInstance().getEdtIndirizzo().getText().toString());

                if (VariabiliStatichePWD.getInstance().getModalitaEdit().equals("INSERT")) {
                    db.SalvaPassword(s, true);
                } else {
                    s.setIdRiga(Integer.parseInt(VariabiliStatichePWD.getInstance().getTxtId().getText().toString()));
                    db.ModificaPassword(s, true);
                }

                db.LeggePasswords();

                UtilityPassword.getInstance().RiempieArrayLista(context);

                VariabiliStatichePWD.getInstance().setModalitaEdit("");
                VariabiliStatichePWD.getInstance().getLayPassword().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgChiudeEditPWD = (ImageView) findViewById(R.id.imgAnnullaEdit);
        imgChiudeEditPWD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePWD.getInstance().getLayPassword().setVisibility(LinearLayout.GONE);
            }
        });

        if (db.LeggePasswords()) {
            // Utility.getInstance().RiempieArrayLista();
        } else {
            VariabiliStatichePWD.getInstance().setRicerca("");
            VariabiliStatichePWD.getInstance().setDeveAggiungereRigheAlDb(true);

            ChiamateWSPwd ws = new ChiamateWSPwd(context);
            ws.CaricaPassword();
        }

        ImageView imgAggiunge = (ImageView) findViewById(R.id.imgAggiunge);
        imgAggiunge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePWD.getInstance().setModalitaEdit("INSERT");
                VariabiliStatichePWD.getInstance().getLayPassword().setVisibility(LinearLayout.VISIBLE);
                VariabiliStatichePWD.getInstance().getTxtId().setText("");
                VariabiliStatichePWD.getInstance().getEdtSito().setText("");
                VariabiliStatichePWD.getInstance().getEdtUtenza().setText("");
                VariabiliStatichePWD.getInstance().getEdtPassword().setText("");
                VariabiliStatichePWD.getInstance().getEdtNote().setText("");
                VariabiliStatichePWD.getInstance().getEdtIndirizzo().setText("");
            }
        });

        EditText edtRicerca = (EditText) findViewById(R.id.edtRicerca);
        ImageView imgRicerca = (ImageView) findViewById(R.id.imgRicerca);
        imgRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Ricerca = edtRicerca.getText().toString();
                VariabiliStatichePWD.getInstance().setRicerca(Ricerca);

                db_dati_password db = new db_dati_password(context);
                db.LeggePasswords();

                UtilityPassword.getInstance().RiempieArrayLista(context);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return true;
        }

        return false;
    }
}
