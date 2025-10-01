package com.looigi.wallpaperchanger2.Onomastici.web;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmagineSanto {
    private static final String NomeMaschera = "Download_Immagine_Santo";
    private DownloadPic d;
    private boolean ScaricaImmagine;
    private String Url;
    private String Directory;
    private String NomeFiletto;
    // private GestioneRubrica GestRubr;
    // private ContentResolver Rubrica;

    public void EsegueChiamata(Context context, DownloadPic d, boolean ScaricaImmagine, String Url,
                               String Directory, String NomeFiletto) {
        this.d = d;
        this.ScaricaImmagine = ScaricaImmagine;
        this.Url = Url;
        this.Directory = Directory;
        this.NomeFiletto = NomeFiletto;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Esecuzione(context);
                // BloccaTimer();
                // TermineEsecuzione();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });

    }

    private void Esecuzione(Context context) {
        if (ScaricaImmagine) {
            try {
                URL url = new URL(Url);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = Files.newOutputStream(Paths.get(Directory + "/" + NomeFiletto));

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception ignored) {
            }
        }

        d.updateWidget();
        // Scarica la lista dei nomi per farla leggere al widget
    }
}
