package com.looigi.wallpaperchanger2.classeLazio.api_football.WebService;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;

import com.looigi.wallpaperchanger2.classeGoogleDrive.GoogleDrive;
import com.looigi.wallpaperchanger2.classeGoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.utilities.Files;

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

                        String Cartella = VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                                Operazione + "/";
                        Files.getInstance().CreaCartelle(Cartella);

                        Files.getInstance().EliminaFileUnico(
                                Cartella +
                                        NomeFile.replace(" ", "_")
                        );
                        Files.getInstance().ScriveFile(
                                Cartella,
                                        NomeFile.replace(" ", "_"),
                                response.toString());

                        // GESTIONE GOOGLE DRIVE - SALVATAGGIO FILE ON LINE
                        VariabiliStaticheGoogleDrive.getInstance().setPathOperazione(
                                "ApiFootball/" +
                                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                                        Operazione
                        );
                        VariabiliStaticheGoogleDrive.getInstance().setNomeFileApiFootball(NomeFile);
                        VariabiliStaticheGoogleDrive.getInstance().setFileDiOrigine(
                                Cartella +
                                        NomeFile.replace(" ", "_")
                        );

                        VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("ScriveFile");
                        Intent apre = new Intent(context, GoogleDrive.class);
                        apre.addCategory(Intent.CATEGORY_LAUNCHER);
                        apre.setAction(Intent.ACTION_MAIN );
                        apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
                        context.startActivity(apre);

                        /* handlerThread = new HandlerThread("background-thread_SF_" +
                                VariabiliStaticheWallpaper.channelName);
                        handlerThread.start();

                        handler = new Handler(handlerThread.getLooper());
                        r = new Runnable() {
                            public void run() {
                                if (!VariabiliStaticheApiFootball.getInstance().isStaLeggendoWS()) {

                                }
                            }
                        };
                        handler.postDelayed(r, 100); */
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
