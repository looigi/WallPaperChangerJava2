package com.looigi.wallpaperchanger2.gps;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.UtilityWallpaper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Mappa extends AppCompatActivity  implements OnMapReadyCallback {
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
    private boolean segue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa);

        this.context = this;
        this.act = this;
        primoPassaggio = true;
        segue = true;

        txtMappa = act.findViewById(R.id.txtMappa);

        dataAttuale = new Date();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
        dataOdierna = sdfD.format(calendar.getTime());

        SwitchCompat sSegue = act.findViewById(R.id.sSegue);
        sSegue.setChecked(true);
        segue = true;
        sSegue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segue = sSegue.isChecked();
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

                    DisegnaPath(mappa);
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

                    DisegnaPath(mappa);
                }
            }
        });

        ImageView imgE = act.findViewById(R.id.imgMappaElimina);
        imgE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_gps db = new db_dati_gps(context);
                db.EliminaPosizioni(dataOdierna);

                VariabiliStaticheGPS.getInstance().getMappa().PuliscePunti();

                UtilityWallpaper.getInstance().ApreToast(context,
                        "Eliminati dati gps per la data " + dataOdierna);
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private int ritornaColore(float speed) {
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

    private void AggiungePolyLine(GoogleMap googleMap, List<StrutturaGps> lista, int colore) {
        LatLng[] path = new LatLng[lista.size()];
        int c = 0;

        for (StrutturaGps l : lista) {
            if (c < lista.size()) {
                path[c] = new LatLng(l.getLat(), l.getLon());
                c++;
            }
        }

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(path)
                .width(12)
                .color(colore)
        );
    }

    private void AttivaTimer(GoogleMap googleMap) {
        handlerTimer = new Handler(Looper.getMainLooper());
        rTimer = new Runnable() {
            public void run() {
                DisegnaPath(googleMap);
                handlerTimer.postDelayed(rTimer, 10000);
            }
        };

        handlerTimer.postDelayed(rTimer, 10000);

        DisegnaPath(googleMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (handlerTimer == null) {
            mappa = googleMap;
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            AttivaTimer(googleMap);
        }
    }

    private void DisegnaPath(GoogleMap googleMap) {
        List<StrutturaGps> listaGPS = VariabiliStaticheGPS.getInstance().getMappa().RitornaPunti();

        txtMappa.setText("Data " + dataOdierna + ". Posizioni: " + listaGPS.size());

        if (listaGPS.isEmpty()) {
            googleMap.clear();
        }

        if (!listaGPS.isEmpty() && vecchiDati != listaGPS.size()) {
            googleMap.clear();

            vecchiDati = listaGPS.size();

            int vecchioColore = -1;
            List<StrutturaGps> lista = new ArrayList<>();
            LatLngBounds.Builder bc = new LatLngBounds.Builder();

            for (StrutturaGps s : listaGPS) {
                /* if (s.getLat() == -1 && s.getLon() == -1) {
                    AggiungePolyLine(googleMap, lista, Color.TRANSPARENT);

                    lista = new ArrayList<>();
                } else { */
                    LatLng ll = new LatLng(s.getLat(), s.getLon());
                    bc.include(ll);

                    float speed = Math.round(s.getSpeed()) * 3;
                    int colore = ritornaColore(speed);

                    lista.add(s);

                    if (vecchioColore != colore) {
                        if (vecchioColore != -1) {
                            AggiungePolyLine(googleMap, lista, colore);
                            lista = new ArrayList<>();
                        }

                        vecchioColore = colore;
                    }
                // }
            }

            if (!lista.isEmpty()) {
                AggiungePolyLine(googleMap, lista, vecchioColore);
            }

            AggiungeMarkers(googleMap);

            if (primoPassaggio || segue) {
                googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                    }
                });

                primoPassaggio = false;
            }
        }
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
                if (handlerTimer != null) {
                    handlerTimer.removeCallbacks(rTimer);
                    rTimer = null;
                    handlerTimer = null;
                }
                this.finish();

                return false;
        }

        super.onKeyDown(keyCode, event);

        return false;
    }
}