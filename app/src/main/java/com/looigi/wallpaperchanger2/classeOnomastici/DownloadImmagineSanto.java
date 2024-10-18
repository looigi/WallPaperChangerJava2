package com.looigi.wallpaperchanger2.classeOnomastici;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmagineSanto {
    private static final String NomeMaschera = "Download_Immagine_Santo";
    private DownloadPic d;
    private boolean ScaricaImmagine;
    private String Url;
    private String Directory;
    private String NomeFiletto;
    private GestioneRubrica GestRubr;
    private ContentResolver Rubrica;
    private GestioneDB GestDB;

    public void EsegueChiamata(DownloadPic d, boolean ScaricaImmagine, String Url,
                               String Directory, String NomeFiletto, GestioneRubrica GestRubr,
                               ContentResolver Rubrica, GestioneDB GestDB) {
        this.d = d;
        this.ScaricaImmagine = ScaricaImmagine;
        this.Url = Url;
        this.Directory = Directory;
        this.NomeFiletto = NomeFiletto;
        this.GestRubr = GestRubr;
        this.Rubrica = Rubrica;
        this.GestDB = GestDB;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Esecuzione();
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

    private void Esecuzione() {
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
        int Quanti=0;

        List<String> Lista=GestRubr.RitornaTuttiINomi(Rubrica);
        SQLiteDatabase myDB= GestDB.ApreDB();
        myDB.execSQL("Create Table If Not Exists Rubrica(Nome Varchar(100));");
        try {
            String Sql="SELECT Count(*) FROM Rubrica;";
            Cursor c = myDB.rawQuery(Sql , null);
            c.moveToFirst();
            Quanti=c.getInt(0);
            c.close();
            if (Quanti!=Lista.size()) {
                myDB.execSQL("Delete From Rubrica;");
                for (int i=0;i<Lista.size();i++) {
                    myDB.execSQL("Insert Into Rubrica Values ('"+Lista.get(i).replace("'", "''")+"');");
                }
            }
        } catch (Exception ignored) {

        }

        GestDB.ChiudeDB(myDB);
        // Scarica la lista dei nomi per farla leggere al widget
    }
}
