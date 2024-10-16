package com.looigi.wallpaperchanger2.classeGps;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.CellSignalStrength;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaPuntiSpegnimento;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainMappa extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    private Handler handlerTimer;
    private Runnable rTimer;
    private String dataOdierna;
    private Context context;
    private db_dati_gps db;
    private Activity act;
    private TextView txtMappa;
    private GoogleMap mappa;
    private int vecchiDati = -1;
    private Date dataAttuale;
    private boolean primoPassaggio = true;
    private LatLngBounds.Builder bc;
    private int bcs = 0;
    private final int livelloZoomStandard = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa);

        this.context = this;
        this.act = this;
        primoPassaggio = true;

        // db_dati_gps db = new db_dati_gps(context);
        // db.CaricaImpostazioni();
        // db.CaricaPuntiDiSpegnimento();

        txtMappa = act.findViewById(R.id.txtMappa);

        dataAttuale = new Date();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
        dataOdierna = sdfD.format(calendar.getTime());

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
                if (mappa != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(dataAttuale);
                    c.add(Calendar.DATE, -1);

                    SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                    dataOdierna = sdfD.format(c.getTime());
                    dataAttuale = c.getTime();

                    VariabiliStaticheGPS.getInstance().getMappa().LeggePunti(dataOdierna);

                    DisegnaPath();

                    GestioneNotificheTasti.getInstance().AggiornaNotifica();
                }
            }
        });

        ImageView imgA = act.findViewById(R.id.imgAvantiMappa);
        imgA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mappa != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(dataAttuale);
                    c.add(Calendar.DATE, 1);

                    SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                    dataOdierna = sdfD.format(c.getTime());
                    dataAttuale = c.getTime();

                    VariabiliStaticheGPS.getInstance().getMappa().LeggePunti(dataOdierna);

                    DisegnaPath();

                    GestioneNotificheTasti.getInstance().AggiornaNotifica();
                }
            }
        });

        ImageView imgE = act.findViewById(R.id.imgMappaElimina);
        imgE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_gps db = new db_dati_gps(context);
                db.EliminaPosizioni(dataOdierna);

                VariabiliStaticheGPS.getInstance().getMappa().PuliscePunti();

                DisegnaPath();

                GestioneNotificheTasti.getInstance().AggiornaNotifica();

                UtilitiesGlobali.getInstance().ApreToast(context,
                        "Eliminati dati gps per la data " + dataOdierna);
            }
        });

        ImageView imgR = act.findViewById(R.id.imgMappaRefresh);
        imgR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaPath();
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
                                                targetLocation.getLongitude())).zoom(livelloZoomStandard).build();

                                mappa.animateCamera(CameraUpdateFactory
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

    private int ritornaColoreSegnale(StrutturaGps s) {
        int sp = (int) s.getLevel();

        switch(sp) {
            case CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN:
                return Color.TRANSPARENT;
            case CellSignalStrength.SIGNAL_STRENGTH_POOR:
                return Color.rgb( 200, 0, 0);
            case CellSignalStrength.SIGNAL_STRENGTH_MODERATE:
                return Color.rgb( 230, 115, 0);
            case CellSignalStrength.SIGNAL_STRENGTH_GOOD:
                return Color.rgb( 255, 255, 0);
            case CellSignalStrength.SIGNAL_STRENGTH_GREAT:
                return Color.rgb( 0, 255, 0);
            default:
                return Color.BLACK;
        }

    }

    private int ritornaColoreVelocita(float speed) {
        int sp = (int) speed;

        if (sp < 40) {
            return Color.BLACK;
        } else {
            if (sp > 39 && sp < 70) {
                return Color.GREEN;
            } else {
                if (sp > 69 && sp < 90) {
                    return Color.BLUE;
                } else {
                    if (sp > 89 && sp < 110) {
                        return Color.RED;
                    } else {
                        return Color.YELLOW;
                    }
                }
            }
        }
    }

    private void AggiungePolyLineVelocita(GoogleMap googleMap, List<StrutturaGps> lista, int colore) {
        LatLng[] path = new LatLng[lista.size()];
        int c = 0;

        for (StrutturaGps l : lista) {
            if (c < lista.size()) {
                path[c] = new LatLng(l.getLat(), l.getLon());
                c++;
            }
        }

        Polyline polylineVelocita = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(path)
                .width(10)
                .color(colore)
        );
    }

    private void AggiungePolyLineSegnale(List<StrutturaGps> lista, int colore) {
        LatLng[] path = new LatLng[lista.size()];
        int c = 0;

        for (StrutturaGps l : lista) {
            if (c < lista.size()) {
                path[c] = new LatLng(l.getLat(), l.getLon());
                c++;
            }
        }

        Polyline polylineSegnale = mappa.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(path)
                .width(20)
                .color(colore)
        );
    }

    private void AttivaTimer() {
        handlerTimer = new Handler(Looper.getMainLooper());
        rTimer = new Runnable() {
            public void run() {
                DisegnaPath();
                handlerTimer.postDelayed(rTimer, 10000);
            }
        };

        handlerTimer.postDelayed(rTimer, 10000);

        DisegnaPath();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // if (handlerTimer == null) {
            mappa = googleMap;

            mappa.setOnMapClickListener(new GoogleMap.OnMapClickListener()
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

                            disegnaMarkersPS();
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

            mappa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    String nome = marker.getTitle();

                    if (!nome.equals("Posizione Attuale")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Si vuole rimuovere il punto di spegnimento?");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db_dati_gps db = new db_dati_gps(context);
                                db.EliminaPuntoDiSpegnimento(nome);

                                disegnaMarkersPS();
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

                    return false;
                }
            });

            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            disegnaMarkersPS();

            AttivaTimer();
        // }
    }

    private void disegnaMarkersPS() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean aggiungePunti = false;
                if (bcs == 0) {
                    aggiungePunti = true;
                }
                for (StrutturaPuntiSpegnimento loc : VariabiliStaticheGPS.getInstance().getListaPuntiDiSpegnimento()) {
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.satellite_off);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 75, false);

                    if (aggiungePunti) {
                        LatLng ll = new LatLng(loc.getLoc().getLatitude(), loc.getLoc().getLongitude());
                        bc.include(ll);
                        bcs++;
                    }

                    mappa.addMarker(new MarkerOptions()
                            .position(new LatLng(loc.getLoc().getLatitude(), loc.getLoc().getLongitude()))
                            .title(loc.getNome())
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    );
                }
            }
        }, 100);
    }

    private void DisegnaPath() {
        if (VariabiliStaticheGPS.getInstance().getMappa() == null) {
            Context ctx = UtilitiesGlobali.getInstance().tornaContextValido();
            if (ctx != null) {
                GestioneMappa m = new GestioneMappa(ctx);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                String dataOdierna = sdfD.format(calendar.getTime());
                m.LeggePunti(dataOdierna);
                VariabiliStaticheGPS.getInstance().setMappa(m);

                if (VariabiliStaticheGPS.getInstance().getMappa() == null) {
                    return;
                }
            } else {
                return;
            }
        }

        List<StrutturaGps> listaGPS = VariabiliStaticheGPS.getInstance().getMappa().RitornaPunti();

        txtMappa.setText("Data " + dataOdierna + ". Posizioni: " + listaGPS.size());

        // if (listaGPS.isEmpty()) {
            mappa.clear();
        // }

        // if (!listaGPS.isEmpty() && vecchiDati != listaGPS.size()) {
            // mappa.clear();

            vecchiDati = listaGPS.size();

            int vecchioColore = -1;
            List<StrutturaGps> lista = new ArrayList<>();
            StrutturaGps ultimoPunto = null;

            if (VariabiliStaticheGPS.getInstance().isMostraSegnale()) {
                // Aggiunta path segnale
                for (int i = listaGPS.size() - 1; i >= 0; i--) {
                    StrutturaGps s = listaGPS.get(i);
                    int colore = ritornaColoreSegnale(s);

                    lista.add(s);

                    if (vecchioColore != colore) {
                        if (vecchioColore != -1) {
                            AggiungePolyLineSegnale(lista, colore);
                            lista = new ArrayList<>();
                        }

                        vecchioColore = colore;
                    }
                    // }
                }

                if (!lista.isEmpty()) {
                    AggiungePolyLineSegnale(lista, vecchioColore);
                }
            }

            /* if (!listaGPS.isEmpty()) {
                LatLng[] path = new LatLng[2];
                path[0] = new LatLng(listaGPS.get(listaGPS.size() - 1).getLat(), listaGPS.get(listaGPS.size() - 1).getLon());
                path[1] = new LatLng(listaGPS.get(0).getLat(), listaGPS.get(0).getLon());

                Polyline polylineBreak = googleMap.addPolyline(new PolylineOptions()
                                .clickable(false)
                                .add(path)
                                .visible(false)
                                .width(1)
                                .color(Color.TRANSPARENT)
                );
            } */

            bc = new LatLngBounds.Builder();
            vecchioColore = -1;
            bcs = 0;

            if (VariabiliStaticheGPS.getInstance().isMostraPercorso()) {
                // Aggiunta path velocità
                for (StrutturaGps s : listaGPS) {
                    ultimoPunto = s;

                /* if (s.getLat() == -1 && s.getLon() == -1) {
                    AggiungePolyLine(googleMap, lista, Color.TRANSPARENT);

                    lista = new ArrayList<>();
                } else { */
                    LatLng ll = new LatLng(s.getLat(), s.getLon());
                    bc.include(ll);
                    bcs++;

                    float speed = Math.round(s.getSpeed()) * 3.5F;
                    int colore = ritornaColoreVelocita(speed);

                    lista.add(s);

                    if (vecchioColore != colore) {
                        if (vecchioColore != -1) {
                            AggiungePolyLineVelocita(mappa, lista, colore);
                            lista = new ArrayList<>();
                        }

                        vecchioColore = colore;
                    }
                    // }
                }

                if (!lista.isEmpty()) {
                    AggiungePolyLineVelocita(mappa, lista, vecchioColore);
                }

                AggiungeMarkers(mappa);
            }

            disegnaMarkersPS();

            if (primoPassaggio) {
                int finalBcs = bcs;
                mappa.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (finalBcs > 0) {
                            mappa.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                        }
                    }
                });

                primoPassaggio = false;
            }

            if (VariabiliStaticheGPS.getInstance().isSegue()) {
                if (ultimoPunto != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(ultimoPunto.getLat(),
                                    ultimoPunto.getLon())).zoom(livelloZoomStandard).build();

                    mappa.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
            }
        // }
    }

    private void AggiungeMarkers(GoogleMap googleMap) {
        StrutturaGps s = VariabiliStaticheGPS.getInstance().getCoordinateAttuali();
        if (s != null) {
            int height = 120;
            int width = 120;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLat(), s.getLon()))
                    .title("Posizione Attuale")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            );
        }
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

        disegnaMarkersPS();
    }

    @Override
    protected void onResume() {
        super.onResume();

        disegnaMarkersPS();
    }
}