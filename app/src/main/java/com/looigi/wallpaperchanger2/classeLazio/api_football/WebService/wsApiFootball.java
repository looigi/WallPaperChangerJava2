package com.looigi.wallpaperchanger2.classeLazio.api_football.WebService;

import android.content.Context;
import android.content.Intent;

import com.looigi.wallpaperchanger2.classeGoogleDrive.GoogleDrive;
import com.looigi.wallpaperchanger2.classeGoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.classePlayer.Files;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class wsApiFootball {
    public void RitornaDati(Context context, String urlString, String NomeFile, String Operazione) {
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
                                        Operazione + "/" +
                                        NomeFile.replace(" ", "_")
                        );
                        Files.getInstance().ScriveFile(
                                VariabiliStaticheApiFootball.getInstance().getPathApiFootball(),
                                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                                        Operazione + "/" +
                                        NomeFile.replace(" ", "_"),
                                response.toString());

                        // GESTIONE GOOGLE DRIVE
                        VariabiliStaticheGoogleDrive.getInstance().setOperazioneApiFootball(Operazione);
                        VariabiliStaticheGoogleDrive.getInstance().setNomeFileApiFootball(NomeFile);

                        VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("ScriveFileApiFootball");
                        Intent apre = new Intent(context, GoogleDrive.class);
                        apre.addCategory(Intent.CATEGORY_LAUNCHER);
                        apre.setAction(Intent.ACTION_MAIN );
                        apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
                        context.startActivity(apre);

                        // VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
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
