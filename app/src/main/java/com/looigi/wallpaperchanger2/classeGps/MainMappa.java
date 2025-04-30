package com.looigi.wallpaperchanger2.classeGps;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.CellSignalStrength;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeGps.classeMappeSalvate.MainMappeSalvate;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaPuntiSpegnimento;
import com.looigi.wallpaperchanger2.classeModificheCodice.webService.ChiamateWSModifiche;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.ClasseZip;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainMappa extends AppCompatActivity implements OnMapReadyCallback {
    private static final String NomeMaschera = "Gestione_GPS";
    private SupportMapFragment mapFragment;
    private Handler handlerTimer;
    private Runnable rTimer;
    private String dataOdierna;
    private Context context;
    private db_dati_gps db;
    private Activity act;
    private Date dataAttuale;
    private CalcoloVelocita cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mappa);

        cv = new CalcoloVelocita();
        this.context = this;
        this.act = this;
        VariabiliStaticheGPS.getInstance().setPrimoPassaggio(true);

        VariabiliStaticheGPS.getInstance().setMappeAperte(true);

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Apertura mappa");

        VariabiliStaticheGPS.getInstance().setImgAttesa(findViewById(R.id.imgCaricamentoGPS));
        UtilityGPS.getInstance().ImpostaAttesa(false);

        LinearLayout layFilesRemoti = findViewById(R.id.layFilesRemoti);
        layFilesRemoti.setVisibility(LinearLayout.GONE);
        VariabiliStaticheGPS.getInstance().setLstFilesRemoti(findViewById(R.id.lstFilesRemoti));
        ImageView imgChiudeFilesRemoti = findViewById(R.id.imgChiudeFilesRemoti);
        imgChiudeFilesRemoti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layFilesRemoti.setVisibility(LinearLayout.GONE);
            }
        });

        // db_dati_gps db = new db_dati_gps(context);
        // db.CaricaImpostazioni();
        // db.CaricaPuntiDiSpegnimento();

        VariabiliStaticheGPS.getInstance().setTxtMappa(act.findViewById(R.id.txtMappa));

        dataAttuale = new Date();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
        dataOdierna = sdfD.format(calendar.getTime());

        ImageView imgMappeSalvate = (ImageView) act.findViewById(R.id.imgMappeSalvate);
        imgMappeSalvate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iO = new Intent(context, MainMappeSalvate.class);
                iO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iO);
           }
        });

        ImageView imgScattaFotoMappa = (ImageView) act.findViewById(R.id.imgScattaFotoMappa);
        imgScattaFotoMappa.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                   Bitmap bitmap;

                   @Override
                   public void onSnapshotReady(Bitmap snapshot) {
                       bitmap = snapshot;

                       Calendar calendar = Calendar.getInstance();
                       String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).trim();
                       String month = String.valueOf(calendar.get(Calendar.MONTH) + 1).trim();
                       String year = String.valueOf(calendar.get(Calendar.YEAR)).trim();
                       String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).trim();
                       String minuti = String.valueOf(calendar.get(Calendar.MINUTE)).trim();
                       String secondi = String.valueOf(calendar.get(Calendar.SECOND)).trim();
                       if (day.length() == 1) { day = "0" + day; }
                       if (month.length() == 1) { month = "0" + month; }
                       if (hour.length() == 1) { hour = "0" + hour; }
                       if (minuti.length() == 1) { minuti = "0" + minuti; }
                       if (secondi.length() == 1) { secondi = "0" + secondi; }

                       String Path = context.getFilesDir() + "/Mappe/";
                       String Nome = year + "_" + month + "_" + day + " " + hour + "_" + minuti + "_" + secondi + ".png";
                       Files.getInstance().CreaCartelle(Path);
                       Path += Nome;
                       try {
                           FileOutputStream out = new FileOutputStream(Path);
                           bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                           UtilitiesGlobali.getInstance().ApreToast(context, "Mappa salvata: " + Nome);
                       } catch (Exception e) {
                           UtilitiesGlobali.getInstance().ApreToast(context,
                                   "Errore salvataggio mappa: " +
                                           UtilityDetector.getInstance().PrendeErroreDaException(e));
                       }
                   }
               };

               VariabiliStaticheGPS.getInstance().getMappetta().snapshot(callback);
           }
       });

        ImageView imgSettings = (ImageView) act.findViewById(R.id.imgSettings);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(context, MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "MAPPA");
                                iP.putExtras(b);
                                context.startActivity(iP);
                            }
                        }, 500);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });

        ImageView imgI = act.findViewById(R.id.imgIndietroMappa);
        imgI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheGPS.getInstance().getMappetta() != null) {
                    UtilityGPS.getInstance().ImpostaAttesa(true);

                    VariabiliStaticheGPS.getInstance().setPrimoPassaggio(true);
                    VariabiliStaticheGPS.getInstance().setVecchiDati(-1);

                    Calendar c = Calendar.getInstance();
                    c.setTime(dataAttuale);
                    c.add(Calendar.DATE, -1);

                    SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                    dataOdierna = sdfD.format(c.getTime());
                    dataAttuale = c.getTime();

                    VariabiliStaticheGPS.getInstance().getMappa().LeggePunti(dataOdierna);

                    UtilityGPS.getInstance().DisegnaPath(context, cv, dataOdierna);
                    // disegnaMarkersPS();
                    // AggiungeMarkers(mappa);

                    GestioneNotificheTasti.getInstance().AggiornaNotifica();

                    UtilityGPS.getInstance().ImpostaAttesa(false);
                }
            }
        });

        ImageView imgA = act.findViewById(R.id.imgAvantiMappa);
        imgA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheGPS.getInstance().getMappetta() != null) {
                    UtilityGPS.getInstance().ImpostaAttesa(true);

                    VariabiliStaticheGPS.getInstance().setPrimoPassaggio(true);
                    VariabiliStaticheGPS.getInstance().setVecchiDati(-1);

                    Calendar c = Calendar.getInstance();
                    c.setTime(dataAttuale);
                    c.add(Calendar.DATE, 1);

                    SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                    dataOdierna = sdfD.format(c.getTime());
                    dataAttuale = c.getTime();

                    VariabiliStaticheGPS.getInstance().getMappa().LeggePunti(dataOdierna);

                    UtilityGPS.getInstance().DisegnaPath(context, cv, dataOdierna);
                    // disegnaMarkersPS();
                    // AggiungeMarkers(mappa);

                    GestioneNotificheTasti.getInstance().AggiornaNotifica();

                    UtilityGPS.getInstance().ImpostaAttesa(false);
                }
            }
        });

        ImageView imgArchivia = act.findViewById(R.id.imgMappaArchiviaTutto);
        imgArchivia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MandaDatiADBRemoto m = new MandaDatiADBRemoto();

                final AlertDialog.Builder[] builder = {new AlertDialog.Builder(context)};
                builder[0].setTitle("Data Attuale o tutto il db ?");
                builder[0].setPositiveButton("Data Attuale", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Si vogliono eliminare anche i dati presenti in archivio ?");
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m.inviaDatiPresentiSulDB(context, true, true, dataOdierna);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m.inviaDatiPresentiSulDB(context, false, true, dataOdierna);
                            }
                        });
                        builder.setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                });
                builder[0].setNegativeButton("Tutto il DB", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Si vogliono eliminare anche i dati presenti in archivio ?");
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m.inviaDatiPresentiSulDB(context, true, false, dataOdierna);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m.inviaDatiPresentiSulDB(context, false, false, dataOdierna);
                            }
                        });
                        builder.setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                });
                builder[0].setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder[0].show();
            }
        });

        ImageView imgRipristina = act.findViewById(R.id.imgMappaRirpistina);
        imgRipristina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layFilesRemoti.setVisibility(LinearLayout.VISIBLE);

                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaFilesRemoti();
            }
        });

        ImageView imgE = act.findViewById(R.id.imgMappaElimina);
        imgE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vogliono eliminare i dati della data visualizzata ?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db_dati_gps db = new db_dati_gps(context);
                        db.EliminaPosizioni(dataOdierna);
                        db.ChiudeDB();

                        VariabiliStaticheGPS.getInstance().getMappa().PuliscePunti();

                        UtilityGPS.getInstance().DisegnaPath(context, cv, dataOdierna);
                        // disegnaMarkersPS();
                        // AggiungeMarkers(mappa);

                        GestioneNotificheTasti.getInstance().AggiornaNotifica();

                        UtilitiesGlobali.getInstance().ApreToast(context,
                                "Eliminati dati gps per la data " + dataOdierna);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        ImageView imgEs = act.findViewById(R.id.imgMappaEstrai);
        imgEs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_gps db = new db_dati_gps(context);
                String Dati = db.EstraiPosizioni(dataOdierna, true);
                db.ChiudeDB();

                if (!Dati.isEmpty()) {
                    String Cartella = context.getFilesDir() + "/DatiGPS";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String sDataAttuale = sdf.format(dataAttuale);
                    String SoloNome = "DatiGPS_" + sDataAttuale;
                    Files.getInstance().CreaCartelle(context.getFilesDir() + "/DatiGPS");
                    Files.getInstance().EliminaFileUnico(Cartella + "/" + SoloNome);
                    Files.getInstance().ScriveFile(
                            Cartella,
                            SoloNome + ".csv",
                            Dati);

                    Files.getInstance().EliminaFileUnico(Cartella + "/" + SoloNome + ".zip");

                    List<String> lfz = new ArrayList<>();
                    lfz.add(Cartella + "/" + SoloNome + ".csv");
                    ClasseZip z = new ClasseZip();
                    z.ZippaFile(context, Cartella + "/", lfz, SoloNome);

                    Files.getInstance().EliminaFileUnico(Cartella + "/" + SoloNome + ".csv");

                    File f = new File(Cartella + "/" + SoloNome + ".zip");
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, SoloNome);
                    i.putExtra(Intent.EXTRA_TEXT, "Path GPS " + sDataAttuale);
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i, "Share Dati GPS"));

                    /* Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            Files.getInstance().EliminaFileUnico(Cartella + "/" + SoloNome);
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 10000); */
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessun dato presente");
                }
            }
        });

        ImageView imgET = act.findViewById(R.id.imgMappaEliminaTutto);
        imgET.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vogliono eliminare tutti i dati presenti in archivio ?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db_dati_gps db = new db_dati_gps(context);
                        db.EliminaTutto();
                        db.ChiudeDB();

                        VariabiliStaticheGPS.getInstance().getMappa().PuliscePunti();

                        UtilityGPS.getInstance().DisegnaPath(context, cv, dataOdierna);
                        // disegnaMarkersPS();
                        // AggiungeMarkers(mappa);

                        GestioneNotificheTasti.getInstance().AggiornaNotifica();

                        UtilitiesGlobali.getInstance().ApreToast(context,
                                "Eliminati tutti i dati gps");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        ImageView imgR = act.findViewById(R.id.imgMappaRefresh);
        imgR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityGPS.getInstance().DisegnaPath(context, cv, dataOdierna);
            }
        });

        SwitchCompat sSegue = act.findViewById(R.id.sSegue);
        sSegue.setChecked(VariabiliStaticheGPS.getInstance().isSegue());
        sSegue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheGPS.getInstance().setSegue(sSegue.isChecked());

                db_dati_gps db = new db_dati_gps(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        ImageView imgC = act.findViewById(R.id.imgMappaCerca);
        imgC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Indirizzo");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Indirizzo = input.getText().toString();

                        Geocoder gc = new Geocoder(context);
                        try {
                            List<Address> addresses = gc.getFromLocationName(
                                    Indirizzo,
                                    5);

                            assert addresses != null;
                            if (!addresses.isEmpty()) {
                                Location targetLocation = new Location("");
                                targetLocation.setLatitude(addresses.get(0).getLatitude());
                                targetLocation.setLongitude(addresses.get(0).getLongitude());

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(targetLocation.getLatitude(),
                                                targetLocation.getLongitude())).zoom(VariabiliStaticheGPS.getInstance().getLivelloZoomStandard()).build();

                                VariabiliStaticheGPS.getInstance().getMappetta().animateCamera(CameraUpdateFactory
                                        .newCameraPosition(cameraPosition));
                            } else {
                                UtilitiesGlobali.getInstance().ApreToast(context, "Nessun indirizzo rilevato");
                            }
                        } catch (IOException e) {
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (VariabiliStaticheGPS.getInstance().getCircolettiPS() != null) { VariabiliStaticheGPS.getInstance().getCircolettiPS().clear(); }
        if (VariabiliStaticheGPS.getInstance().getMarkersPS() != null) { VariabiliStaticheGPS.getInstance().getMarkersPS().clear(); }
        if (VariabiliStaticheGPS.getInstance().getMarkersPA() != null) { VariabiliStaticheGPS.getInstance().getMarkersPA().clear(); }

        if (VariabiliStaticheGPS.getInstance().getMappetta() != null) { VariabiliStaticheGPS.getInstance().getMappetta().clear(); }

        /* if (VariabiliStaticheGPS.getInstance().getPolylineSegnale() != null)
            VariabiliStaticheGPS.getInstance().getPolylineSegnale().remove();
        if (VariabiliStaticheGPS.getInstance().getPolylineVelocita() != null)
            VariabiliStaticheGPS.getInstance().getPolylineVelocita().remove(); */

        VariabiliStaticheGPS.getInstance().setMappeAperte(false);

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Chiusura mappa");
    }

    private void AttivaTimer() {
        handlerTimer = new Handler(Looper.getMainLooper());
        rTimer = new Runnable() {
            public void run() {
                UtilityGPS.getInstance().DisegnaPath(context, cv, dataOdierna);

                handlerTimer.postDelayed(rTimer, 10000);
            }
        };

        handlerTimer.postDelayed(rTimer, 10000);

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Timer disegno mappa attivato");

        UtilityGPS.getInstance().DisegnaPath(context, cv, dataOdierna);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // if (handlerTimer == null) {
        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Mappa pronta");

            VariabiliStaticheGPS.getInstance().setMappetta(googleMap);

            VariabiliStaticheGPS.getInstance().getMappetta().setOnMapClickListener(new GoogleMap.OnMapClickListener()
            {
                @Override
                public void onMapClick(LatLng arg0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Nome punto di spegnimento");

                    final EditText input = new EditText(context);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String m_Text = input.getText().toString();

                            Location targetLocation = new Location("");
                            targetLocation.setLatitude(arg0.latitude);
                            targetLocation.setLongitude(arg0.longitude);

                            db_dati_gps db = new db_dati_gps(context);
                            db.ScrivePuntoDiSpegnimento(m_Text, targetLocation);
                            db.ChiudeDB();

                            UtilityGPS.getInstance().disegnaMarkersPS(context);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });

            VariabiliStaticheGPS.getInstance().getMappetta().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    String nome = marker.getTitle();

                    if (nome.equals("Punto Path")) {
                        if (marker.getSnippet() != null) {  // Controlla se c'è uno snippet
                            marker.showInfoWindow();        // Mostra il popup con lo snippet
                        }
                    } else {
                        if (!nome.equals("Posizione Attuale")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Si vuole rimuovere il punto di spegnimento?");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db_dati_gps db = new db_dati_gps(context);
                                    db.EliminaPuntoDiSpegnimento(nome);
                                    db.ChiudeDB();

                                    UtilityGPS.getInstance().disegnaMarkersPS(context);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                        }
                    }

                    return false;
                }
            });

            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMaxZoomPreference(18.0f);

            UtilityGPS.getInstance().disegnaMarkersPS(context);

            AttivaTimer();
        // }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /* if (handlerTimer != null) {
                    handlerTimer.removeCallbacks(rTimer);
                    rTimer = null;
                    handlerTimer = null;
                } */
                this.finish();

                return false;
        }

        super.onKeyDown(keyCode, event);

        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Restart mappa");

        UtilityGPS.getInstance().disegnaMarkersPS(context);
    }

    @Override
    protected void onResume() {
        super.onResume();

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Resume mappa");

        UtilityGPS.getInstance().disegnaMarkersPS(context);
    }
}