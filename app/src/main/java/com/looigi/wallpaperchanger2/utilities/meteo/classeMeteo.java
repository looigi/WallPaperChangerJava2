package com.looigi.wallpaperchanger2.utilities.meteo;

import android.content.Context;
import android.os.Handler;

import com.looigi.wallpaperchanger2.classeGps.db_dati_gps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;
import com.looigi.wallpaperchanger2.utilities.meteo.struttura.StrutturaMeteo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class classeMeteo {
    public void RitornaMeteo(Context context, String daDove) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdfD.format(calendar.getTime());

        db_dati_gps db = new db_dati_gps(context);
        StrutturaGps s = db.RitornaUltimaPosizione(currentDate);

        String latLon = "";
        if (s != null) {
            latLon = s.getLat() + "," + s.getLon();
        } else {
            latLon = "Rome";
        }
        String finalLatLon = latLon;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://api.weatherapi.com/v1/current.json?" +
                            "key=f6ecb475333f4b7181682929252703&" +
                            "q=" + finalLatLon + "&" +
                            "aqi=no");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        String ritorno = response.toString();
                        parsaJson(ritorno);
                    } else {
                        // Ritorno[0] = "Errore nella richiesta: " + responseCode;
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    // Ritorno[0] = "Errore nella richiesta: " + UtilityDetector.getInstance().PrendeErroreDaException(e);
                }
            }
        }).start();
    }

    private Handler handlerTimer;
    private Runnable rTimer;

    private void parsaJson(String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);

            // location
            JSONObject location = root.getJSONObject("location");
            String cityName = location.getString("name");
            String region = location.getString("region");
            String country = location.getString("country");
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");
            String localtime = location.getString("localtime");

            // current
            JSONObject current = root.getJSONObject("current");
            double tempC = current.getDouble("temp_c");
            int isDay = current.getInt("is_day");

            // condition
            JSONObject condition = current.getJSONObject("condition");
            String weatherText = condition.getString("text");
            String iconUrl = condition.getString("icon");

            StrutturaMeteo s = new StrutturaMeteo();
            s.setIcona(iconUrl);
            s.setTemperatura(tempC);
            s.setTesto(weatherText);

            VariabiliStaticheStart.getInstance().setMeteo(s);

            VariabiliStaticheStart.getInstance().setHaPresoMeteo(true);

            /* VariabiliStaticheStart.getInstance().setHaFattoTraduzione(false);
            traduzioneTesto tt = new traduzioneTesto();
            tt.TraduzioneTesto(weatherText);

            handlerTimer = new Handler(Looper.getMainLooper());
            rTimer = new Runnable() {
                public void run() {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (VariabiliStaticheStart.getInstance().isHaFattoTraduzione()) {
                                handlerTimer.removeCallbacksAndMessages(rTimer);

                                String Traduzione = tt.getTraduzione();

                                StrutturaMeteo s = new StrutturaMeteo();
                                s.setIcona(iconUrl);
                                s.setTemperatura(tempC);
                                s.setTesto(Traduzione);

                                VariabiliStaticheStart.getInstance().setMeteo(s);

                                VariabiliStaticheStart.getInstance().setHaPresoMeteo(true);
                            } else {
                                handlerTimer.postDelayed(rTimer, 500);
                            }
                        }
                    }, 500);
                }
            };
            handlerTimer.postDelayed(rTimer, 500); */
        } catch (JSONException e) {
            StrutturaMeteo s = new StrutturaMeteo();
            s.setIcona("");
            s.setTemperatura(0D);
            s.setTesto("");

            VariabiliStaticheStart.getInstance().setMeteo(s);

            VariabiliStaticheStart.getInstance().setHaPresoMeteo(true);
        }
    }
}
