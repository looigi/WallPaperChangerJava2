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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private Date dataAttuale;
    private Context context;
    private db_dati_gps db;
    private Activity act;
    private TextView txtMappa;
    private GoogleMap mappa;
    private int vecchiDati = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa);

        this.context = this;
        this.act = this;

        txtMappa = act.findViewById(R.id.txtMappa);

        dataAttuale = new Date();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
        dataOdierna = sdfD.format(calendar.getTime());

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

                    DisegnaPath(mappa);
                }
            }
        });

        ImageView imgE = act.findViewById(R.id.imgMappaElimina);
        imgE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_gps db = new db_dati_gps(context);
                db.EliminaPosizioni(dataOdierna);

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
                        return Color.YELLOW;
                    } else {
                        return Color.RED;
                    }
                }
            }
        }
    }

    private void AggiungePolyLine(GoogleMap googleMap, List<StrutturaGps> lista, float speed) {
        LatLng[] path = new LatLng[lista.size()];
        int c = 0;

        for (StrutturaGps l : lista) {
            path[c] = new LatLng(l.getLat(), l.getLon());
            c++;
        }

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(path)
                .width(12)
                .color(ritornaColore(speed))
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
        if (db == null) {
            db = new db_dati_gps(context);
        }

        List<StrutturaGps> listaGPS = db.RitornaPosizioni(dataOdierna);
        txtMappa.setText("Data " + dataOdierna + ". Posizioni: " + listaGPS.size());

        if (listaGPS.isEmpty()) {
            googleMap.clear();
        }

        if (!listaGPS.isEmpty() && vecchiDati != listaGPS.size()) {
            googleMap.clear();

            vecchiDati = listaGPS.size();

            float vecchiaSpeed = -1;
            List<StrutturaGps> lista = new ArrayList<>();
            LatLngBounds.Builder bc = new LatLngBounds.Builder();

            for (StrutturaGps s : listaGPS) {
                if (s.getLat() == -1 && s.getLon() == -1) {
                    AggiungePolyLine(googleMap, lista, vecchiaSpeed);

                    lista = new ArrayList<>();
                } else {
                    LatLng ll = new LatLng(s.getLat(), s.getLon());
                    bc.include(ll);

                    float speed = s.getSpeed();
                    if (vecchiaSpeed != speed) {
                        if (vecchiaSpeed != -1) {
                            AggiungePolyLine(googleMap, lista, vecchiaSpeed);
                            lista = new ArrayList<>();
                        }

                        vecchiaSpeed = speed;
                    }

                    lista.add(s);
                }
            }

            if (!lista.isEmpty()) {
                AggiungePolyLine(googleMap, lista, vecchiaSpeed);
            }

            AggiungeMarkers(googleMap);

            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 30));
                }
            });
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