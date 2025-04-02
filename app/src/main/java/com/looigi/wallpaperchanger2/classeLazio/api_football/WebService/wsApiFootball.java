package com.looigi.wallpaperchanger2.classeLazio.api_football.WebService;

import android.content.Context;

import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.classePlayer.Files;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class wsApiFootball {
    public void RitornaDati(Context context, String urlString, String NomeFile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("x-rapidapi-key", "2d42c8b29dd3685687e5e6a6ce5f347b");
                    connection.setRequestProperty("x-rapidapi-host", "v3.football.api-sports.io");
                    connection.setInstanceFollowRedirects(true);

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        Files.getInstance().EliminaFileUnico(
                                VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                                NomeFile
                        );
                        Files.getInstance().ScriveFile(
                                VariabiliStaticheApiFootball.getInstance().getPathApiFootball(),
                                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" + NomeFile,
                                response.toString());

                        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
                    } else {
                        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
                }
            }
        }).start();
    }
}
