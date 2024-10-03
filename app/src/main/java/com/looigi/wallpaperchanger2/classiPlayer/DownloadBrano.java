package com.looigi.wallpaperchanger2.classiPlayer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaBrano;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadBrano extends AsyncTask<String, String, String> {
    private static final String NomeMaschera = "DOWNLOADBRANOPLAYER";
    private StrutturaBrano sb;
    // private boolean bloccatoCiclo = false;
    // private boolean skippatoBrano = false;
    private boolean erroreDownload = false;
    private int tempoImpiegato;
    private int totTempoDaImpiegare = 45;
    private int vecchiSecondi = -1;
    private float p3Appo;
    private String sAppo;
    private String percAppo;
    private float Dimensione;
    private double ultimiBytes = 0;
    private double attualeBytes = 0;
    private int contaUguale = 0;
    private boolean PulisceBrani = false;
    private Context context;

    public DownloadBrano(Context context, StrutturaBrano s) {
        UtilityPlayer.getInstance().Attesa(true);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("Download brano: " + s.getBrano());

        this.context = context;
        sb = s;
        tempoImpiegato = 0;
        ultimiBytes = 0;
        PulisceBrani = false;
        Dimensione = Math.round((s.getDimensione() / 1024F) * 100F) / 100F;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        Files.getInstance().CreaCartelle(sb.getCartellaBrano());

        int count;

        try {
            URL url = new URL(f_url[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(
                    "Content-Type", "audio/mpeg" );
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // urlConnection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = urlConnection.getContentLength();
            // OggettiAVideo.getInstance().getProgressDownload().setMax(lenghtOfFile);

            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Lunghezza file: " + Long.toString(lenghtOfFile));

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),8192);

            // Output stream
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Creazione file output: " + sb.getPathBrano());
            OutputStream output = new FileOutputStream(sb.getPathBrano());

            byte[] data = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) (total));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
        } catch (Exception e) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Errore: " + e.getMessage());
            erroreDownload = true;
            Files.getInstance().EliminaFileUnico(sb.getPathBrano());
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        // dismissDialog(progress_bar_type);
        if (!erroreDownload) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    float DimensioneFile = Files.getInstance().DimensioniFile(sb.getPathBrano()) * 1024F;
                    float perc = Math.abs(Math.round(DimensioneFile / (Dimensione * 1024F * 1024F)) * 100F);
                    if (perc < 80 && Dimensione > 0F) {
                        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Elimino file " + sb.getPathBrano() + " in quanto più piccolo dell'80%");
                        Files.getInstance().EliminaFileUnico(sb.getPathBrano());
                    } else {
                        long dime = Files.getInstance().DimensioniFile(sb.getPathBrano());
                        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "File scaricato: " + sb.getPathBrano() + ". Dimensioni: " + dime);
                        if (dime > 1000) {
                            sb.setDimensione(dime * 1024L);

                            db_dati_player db = new db_dati_player(context);
                            db.ScriveUltimoBrano(sb);

                            VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);

                            UtilityPlayer.getInstance().CaricaBranoNelLettore(context);
                        } else {
                            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Elimino file. Dimensioni troppo piccole");
                            Files.getInstance().EliminaFileUnico(sb.getPathBrano());
                        }
                    }
                    // OggettiAVideo.getInstance().getProgressDownload().setVisibility(LinearLayout.GONE);
                }
            }, 100);
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Errore download brano");
        }

        UtilityPlayer.getInstance().Attesa(false);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
    }
}
