package com.looigi.wallpaperchanger2.classeGps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.CellSignalStrength;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaPuntiSpegnimento;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.log.LogInterno;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UtilityGPS {
    private static final String NomeMaschera = "Utility_GPS";

    private static UtilityGPS instance = null;

    private UtilityGPS() {
    }

    public static UtilityGPS getInstance() {
        if (instance == null) {
            instance = new UtilityGPS();
        }

        return instance;
    }

    public void ImpostaAttesa(boolean come) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (VariabiliStaticheGPS.getInstance().getImgAttesa() != null) {
                    if (come) {
                        VariabiliStaticheGPS.getInstance().getImgAttesa().setVisibility(LinearLayout.VISIBLE);
                    } else {
                        VariabiliStaticheGPS.getInstance().getImgAttesa().setVisibility(LinearLayout.GONE);
                    }
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Gps", Maschera,  Log);
            // }
        } else {

        }
    }

    private LatLngBounds.Builder bc;
    private int bcs = 0;

    public void DisegnaPath(Context context, String dataOdierna) {
        if (!VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            return;
        }

        if (dataOdierna.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
            dataOdierna = sdfD.format(calendar.getTime());
        }

        if (!VariabiliStaticheGPS.getInstance().isMappeAperte()) {
            VariabiliStaticheGPS.getInstance().getMappetta().clear();

            // if (polylineSegnale != null) polylineSegnale.remove();
            // if (polylineVelocita != null) polylineVelocita.remove();
            if (VariabiliStaticheGPS.getInstance().getMarkersPS() != null) VariabiliStaticheGPS.getInstance().getMarkersPS().clear();
            if (VariabiliStaticheGPS.getInstance().getMarkersPA() != null) VariabiliStaticheGPS.getInstance().getMarkersPA().clear();
            // if (VariabiliStaticheGPS.getInstance().getMarkersPATH() != null) VariabiliStaticheGPS.getInstance().getMarkersPATH().clear();

            return;
        }

        if (VariabiliStaticheGPS.getInstance().getMappa() == null) {
            Context ctx = UtilitiesGlobali.getInstance().tornaContextValido();
            if (ctx != null) {
                GestioneMappa m = new GestioneMappa(ctx);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                String sDataOdierna = sdfD.format(calendar.getTime());
                m.LeggePunti(sDataOdierna);
                VariabiliStaticheGPS.getInstance().setMappa(m);

                if (VariabiliStaticheGPS.getInstance().getMappa() == null) {
                    return;
                } else {
                    GestioneNotificaGPS.getInstance().AggiornaNotifica();
                }
            } else {
                return;
            }
        }

        List<StrutturaGps> listaGPS = VariabiliStaticheGPS.getInstance().getMappa().RitornaPunti();
        listaGPS = VariabiliStaticheGPS.getInstance().getMappa().togliePuntiEccessivi(listaGPS);

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Disegno punti. Quanti: " + listaGPS.size());

        if (listaGPS.isEmpty()) {
            VariabiliStaticheGPS.getInstance().getMappetta().clear();
        }

        if (!listaGPS.isEmpty() && VariabiliStaticheGPS.getInstance().getVecchiDati() != listaGPS.size()) {
            VariabiliStaticheGPS.getInstance().setVecchiDati(listaGPS.size());

            VariabiliStaticheGPS.getInstance().getMappetta().clear();

            VariabiliStaticheGPS.getInstance().getTxtMappa().setText(
                    "Data " + dataOdierna + "\nPosizioni: " + VariabiliStaticheGPS.getInstance().getPuntiTotali()
            );

            // int vecchioColore = -1;
            // List<StrutturaGps> lista = new ArrayList<>();
            StrutturaGps ultimoPunto = null;

            bc = new LatLngBounds.Builder();

            if (VariabiliStaticheGPS.getInstance().isDisegnaPathComePolyline()) {
                if (VariabiliStaticheGPS.getInstance().isMostraSegnale()) {
                    // if (VariabiliStaticheGPS.getInstance().getPolylineSegnale() != null)
                    //     VariabiliStaticheGPS.getInstance().getPolylineSegnale().remove();

                    int vecchioColore = -1;
                    List<StrutturaGps> lista = new ArrayList<>();

                    for (int i = listaGPS.size() - 1; i >= 0; i--) {
                        StrutturaGps s = listaGPS.get(i);
                        int colore = ritornaColoreSegnale(s);

                        LatLng punto = new LatLng(s.getLat(), s.getLon());
                        bc.include(punto);
                        bcs++;

                        if (vecchioColore != colore) {
                            if (vecchioColore != -1) {
                                AggiungePolyLineSegnale(lista, colore);
                                lista = new ArrayList<>();
                            }

                            vecchioColore = colore;
                        }

                        lista.add(s);
                    }

                    if (!lista.isEmpty()) {
                        AggiungePolyLineSegnale(lista, vecchioColore);
                    }
                }

                if (VariabiliStaticheGPS.getInstance().isMostraPercorso()) {
                    // if (VariabiliStaticheGPS.getInstance().getPolylineVelocita() != null)
                    //     VariabiliStaticheGPS.getInstance().getPolylineVelocita().remove();

                    int vecchioColore = -1;
                    int c = 0;
                    List<StrutturaGps> lista = new ArrayList<>();

                    for (int i = listaGPS.size() - 1; i >= 0; i--) {
                        StrutturaGps s = listaGPS.get(i);
                        // float speed = Math.round(s.getSpeed()) * 3.5F;
                        int colore = ritornaColoreVelocita(s.getSpeed());

                        LatLng punto = new LatLng(s.getLat(), s.getLon());
                        bc.include(punto);
                        bcs++;

                        if (vecchioColore != colore) {
                            if (vecchioColore != -1) {
                                AggiungePolyLineVelocita(lista, colore);
                                lista = new ArrayList<>();
                            }

                            vecchioColore = colore;
                        }

                        lista.add(s);

                        BitmapDescriptor icona;
                        c++;
                        if (c > VariabiliStaticheGPS.puntiPerFreccia) {
                            c = 0;
                            icona = creaFrecciaConBordo(colore, colore);

                            float speed = s.getSpeed();
                            float velocityInMps = speed;
                            speed = velocityInMps * 3.6F;

                            VariabiliStaticheGPS.getInstance().getMappetta().addMarker(new MarkerOptions()
                                    .position(punto)
                                    .icon(icona)
                                    .rotation(s.getDirezione())          // Angolo in gradi (0-360)
                                    .anchor(0.5f, 0.5f)           // Centra l'icona rispetto al punto
                                    .flat(true)
                                    .title("Punto Path")
                                    .snippet(s.getOra() + " Vel.: " + speed)
                            );
                        }

                    }

                    if (!lista.isEmpty()) {
                        AggiungePolyLineVelocita(lista, vecchioColore);
                    }
                }
            } else {
                if (VariabiliStaticheGPS.getInstance().getMarkersPS() != null)
                    VariabiliStaticheGPS.getInstance().getMarkersPS().clear();
                if (VariabiliStaticheGPS.getInstance().getMarkersPA() != null)
                    VariabiliStaticheGPS.getInstance().getMarkersPA().clear();
                // if (markersPATH != null) markersPATH.clear();

                int coloreSegnale;
                int coloreVelocita;
                int c = VariabiliStaticheGPS.puntiPerFreccia;

                for (int i = listaGPS.size() - 1; i >= 0; i--) {
                    StrutturaGps s = listaGPS.get(i);
                    coloreSegnale = ritornaColoreSegnale(s);

                    // float speed = Math.round(s.getSpeed()) * 3.5F;
                    coloreVelocita = ritornaColoreVelocita(s.getSpeed());

                    LatLng punto = new LatLng(s.getLat(), s.getLon());
                    ultimoPunto = s;

                    bc.include(punto);
                    bcs++;

                    BitmapDescriptor icona;
                    c++;
                    if (c > VariabiliStaticheGPS.puntiPerFreccia) {
                        c = 0;
                        icona = creaFrecciaConBordo(coloreSegnale, coloreVelocita);
                    } else {
                        icona = creaPallinoConBordo(coloreSegnale, coloreVelocita);
                    }

                    float speed = s.getSpeed();
                    float velocityInMps = speed;
                    speed = velocityInMps * 3.6F;

                    VariabiliStaticheGPS.getInstance().getMappetta().addMarker(new MarkerOptions()
                            .position(punto)
                            .icon(icona)
                            .rotation(s.getDirezione())          // Angolo in gradi (0-360)
                            .anchor(0.5f, 0.5f)           // Centra l'icona rispetto al punto
                            .flat(true)
                            .title("Punto Path")
                            .snippet(s.getOra() + " Vel.: " + speed)
                    );
                }
            }

            if (VariabiliStaticheGPS.getInstance().isPrimoPassaggio()) {
                int finalBcs = bcs;
                VariabiliStaticheGPS.getInstance().getMappetta().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (finalBcs > 0) {
                            VariabiliStaticheGPS.getInstance().getMappetta().moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                        }
                    }
                });

                VariabiliStaticheGPS.getInstance().setPrimoPassaggio(false);
            }

            if (VariabiliStaticheGPS.getInstance().isSegue()) {
                if (ultimoPunto != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(ultimoPunto.getLat(),
                                    ultimoPunto.getLon())).zoom(VariabiliStaticheGPS.getInstance().getLivelloZoomStandard()).build();

                    VariabiliStaticheGPS.getInstance().getMappetta().animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
            }
        } else {
            if (VariabiliStaticheGPS.getInstance().isPrimoPassaggio()) {
                VariabiliStaticheGPS.getInstance().getTxtMappa().setText("Data " + dataOdierna + "\nPosizioni: 0");

                StrutturaGps ultimoPunto = VariabiliStaticheGPS.getInstance().getCoordinateAttuali();
                if (ultimoPunto != null) {
                    LatLng ll = new LatLng(ultimoPunto.getLat(), ultimoPunto.getLon());
                    bc = new LatLngBounds.Builder();
                    bc.include(ll);
                }

                VariabiliStaticheGPS.getInstance().getMappetta().setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (ultimoPunto != null) {
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(ultimoPunto.getLat(),
                                            ultimoPunto.getLon())).zoom(VariabiliStaticheGPS.getInstance().getLivelloZoomStandard()).build();

                            VariabiliStaticheGPS.getInstance().getMappetta().animateCamera(CameraUpdateFactory
                                    .newCameraPosition(cameraPosition));
                        } else {
                            VariabiliStaticheGPS.getInstance().getMappetta().moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
                        }
                    }
                });

                VariabiliStaticheGPS.getInstance().setPrimoPassaggio(false);
            }
        }

        AggiungeMarkers(context);
        disegnaMarkersPS(context);
    }

    private void AggiungePolyLineVelocita(List<StrutturaGps> lista, int colore) {
        if (!VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            return;
        }

        try {
            LatLng[] path = new LatLng[lista.size()];
            int c = 0;

            for (StrutturaGps l : lista) {
                if (c < lista.size()) {
                    path[c] = new LatLng(l.getLat(), l.getLon());
                    c++;
                }
            }

            Polyline polylineVelocita = VariabiliStaticheGPS.getInstance().getMappetta().addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(path)
                    .width(10)
                    .color(colore)
            );
        } catch (Exception ignored) {

        }
    }

    private void AggiungePolyLineSegnale(List<StrutturaGps> lista, int colore) {
        if (!VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            return;
        }

        try {
            LatLng[] path = new LatLng[lista.size()];
            String[] ora = new String[lista.size()];
            float[] direzione = new float[lista.size()];

            int c = 0;

            for (StrutturaGps l : lista) {
                if (c < lista.size()) {
                    path[c] = new LatLng(l.getLat(), l.getLon());
                    ora[c] = l.getOra();
                    direzione[c] = l.getDirezione();

                    c++;
                }
            }

            Polyline polylineSegnale = VariabiliStaticheGPS.getInstance().getMappetta().addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(path)
                    .width(20)
                    .color(colore)
            );

            /* int i = 0;
            for (LatLng punto : path) {  // 'path' è l'elenco dei punti
                VariabiliStaticheGPS.getInstance().getMappetta().addMarker(new MarkerOptions()
                        .position(punto)
                        .icon(creaFrecciaConBordo(colore))
                        .rotation(direzione[i])          // Angolo in gradi (0-360)
                        .anchor(0.5f, 0.5f)           // Centra l'icona rispetto al punto
                        .flat(true)
                        .title("Punto Path")
                        .snippet(ora[i])
                );
                i++;
            } */
        } catch (Exception ignored) {

        }
    }

    public void disegnaMarkersPS(Context context) {
        if (!VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            return;
        }

        VariabiliStaticheGPS.getInstance().setCircolettiPS(new ArrayList<>());
        VariabiliStaticheGPS.getInstance().setMarkersPS(new ArrayList<>());

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Disegno markers PS");

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean aggiungePunti = (bcs == 0);

                for (StrutturaPuntiSpegnimento loc : VariabiliStaticheGPS.getInstance().getListaPuntiDiSpegnimento()) {
                    BitmapDrawable bitmapDraw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.satellite_off);
                    Bitmap b = bitmapDraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 75, false);

                    LatLng ll = new LatLng(loc.getLoc().getLatitude(), loc.getLoc().getLongitude());
                    if (aggiungePunti) {
                        if (bc == null) {
                            bc = new LatLngBounds.Builder();
                        }

                        bc.include(ll);
                        bcs++;
                    }

                    Circle circolo = VariabiliStaticheGPS.getInstance().getMappetta().addCircle(new CircleOptions()
                            .center(ll)
                            .radius(VariabiliStaticheGPS.getInstance().getDistanzaMetriPerPS())
                            .strokeWidth(0f)
                            .fillColor(0x440000FF));
                    VariabiliStaticheGPS.getInstance().getCircolettiPS().add(circolo);

                    Marker marcher = VariabiliStaticheGPS.getInstance().getMappetta().addMarker(new MarkerOptions()
                            .position(ll)
                            .title(loc.getNome())
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    );
                    VariabiliStaticheGPS.getInstance().getMarkersPS().add(marcher);
                }

                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Disegno markers. Quanti: " + VariabiliStaticheGPS.getInstance().getMarkersPS().size());
            }
        }, 100);
    }

    private void AggiungeMarkers(Context context) {
        if (!VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            return;
        }

        VariabiliStaticheGPS.getInstance().setMarkersPA(new ArrayList<>());

        StrutturaGps s = VariabiliStaticheGPS.getInstance().getCoordinateAttuali();
        if (s != null) {
            int height = 80;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable) context.getResources().getDrawable(R.drawable.marker);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            Marker m = VariabiliStaticheGPS.getInstance().getMappetta().addMarker(new MarkerOptions()
                    .position(new LatLng(s.getLat(), s.getLon()))
                    .title("Posizione Attuale")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            );
            VariabiliStaticheGPS.getInstance().getMarkersPA().add(m);
        }

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Disegno marker posizione attuale");
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

        if (sp < 0) {
            return Color.GRAY; // Velocità non valida
        } else if (sp < 10) {
            return Color.BLUE; // Molto bassa
        } else if (sp < 30) {
            return Color.GREEN; // Bassa
        } else if (sp < 50) {
            return 0xFF9ACD32; // Verde-Giallastro (YellowGreen)
        } else if (sp < 70) {
            return Color.YELLOW; // Media
        } else if (sp < 90) {
            return 0xFFFFA500; // Arancione
        } else if (sp < 110) {
            return 0xFFB22222; // Rosso scuro (Firebrick)
        } else {
            return Color.RED; // Estrema
        }
    }

    private BitmapDescriptor creaPallinoConBordo(int coloreInterno, int coloreBordo) {
        int raggio = 5;  // Raggio del pallino
        int diametro = raggio * 2;

        Bitmap bitmap = Bitmap.createBitmap(diametro + 8, diametro + 8, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Disegna il bordo del pallino
        Paint paintBordo = new Paint();
        paintBordo.setColor(coloreBordo);          // Colore del bordo
        paintBordo.setStyle(Paint.Style.FILL);
        canvas.drawCircle(diametro / 2f + 4, diametro / 2f + 4, raggio + 4, paintBordo);  // Cerchio con bordo

        // Disegna l'interno del pallino
        Paint paintInterno = new Paint();
        paintInterno.setColor(coloreInterno);       // Colore interno del pallino
        paintInterno.setStyle(Paint.Style.FILL);
        canvas.drawCircle(diametro / 2f + 4, diametro / 2f + 4, raggio, paintInterno);    // Cerchio interno

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor creaFrecciaConBordo(int coloreInterno, int coloreVelocita) {
        int larghezza = 35;  // Larghezza della freccia
        int altezza = 35;    // Altezza della freccia

        Bitmap bitmap = Bitmap.createBitmap(larghezza, altezza, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Disegna la freccia
        Paint paintBordo = new Paint();
        paintBordo.setColor(coloreInterno);          // Colore del bordo
        paintBordo.setStyle(Paint.Style.FILL_AND_STROKE);
        paintBordo.setStrokeWidth(4);              // Spessore del bordo

        Path path = new Path();
        path.moveTo(larghezza / 2f, 0);           // Punta della freccia
        path.lineTo(0, altezza);                  // Angolo sinistro in basso
        path.lineTo(larghezza / 2f, altezza * 0.7f);  // Centro in basso
        path.lineTo(larghezza, altezza);          // Angolo destro in basso
        path.close();
        canvas.drawPath(path, paintBordo);        // Disegna la freccia con il bordo

        // Disegna il riempimento della freccia
        Paint paintInterno = new Paint();
        paintInterno.setColor(coloreVelocita);     // Colore interno della freccia
        paintInterno.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paintInterno);      // Disegna il riempimento

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
