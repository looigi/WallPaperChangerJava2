package com.looigi.wallpaperchanger2.classeScaricaImmagini;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmagineSI {
    private static final String NomeMaschera = "Download_Immagine_Player_SI";
    private ImageView bmImage;
    private boolean Errore;
    private Context context;
    private boolean isCancelled;
    private InputStream in;

    public void EsegueDownload(Context context, ImageView bmImage, String UrlImmagine,
                               String Modalita, String Filtro, boolean Salva, String TipoOperazione, int numeroScarico,
                               TextView txtInfoImmagine) {
        this.context = context;
        this.bmImage = bmImage;

        // AttivaTimer();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Errore = false;

                String urldisplay = UrlImmagine;
                urldisplay = urldisplay.replace("\\", "/");
                Bitmap mIcon11 = null;
                try {
                    in = new java.net.URL(urldisplay).openStream();
                    if (in != null && !isCancelled) {
                        mIcon11 = BitmapFactory.decodeStream(in);

                        if (!isCancelled && mIcon11.getHeight() > 100 && mIcon11.getWidth() > 100) {
                            Bitmap finalMIcon1 = mIcon11;

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bmImage.setImageBitmap(finalMIcon1);

                                    String result = "";

                                    String[] n = UrlImmagine.split("/");
                                    String NomeFile = n[n.length - 1];
                                    if (!NomeFile.toUpperCase().contains(".JPG")) {
                                        NomeFile += ".jpg";
                                    }

                                    NomeFile = UtilitiesGlobali.getInstance().TogliePercentualiDalNome(NomeFile);

                                    if (Salva) {
                                        String NomeFileAppoggio = context.getFilesDir() + "/Appoggio/ImmScaricata.jpg";
                                        Files.getInstance().CreaCartelle(context.getFilesDir() + "/Appoggio");
                                        Files.getInstance().EliminaFileUnico(NomeFileAppoggio);

                                        FileOutputStream outStream;
                                        try {
                                            outStream = new FileOutputStream(NomeFileAppoggio);
                                            finalMIcon1.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

                                            outStream.flush();
                                            outStream.close();

                                            result = UtilitiesGlobali.getInstance().convertBmpToBase64(NomeFileAppoggio);
                                        } catch (IOException ignored) {

                                        }

                                        if (!result.isEmpty()) {
                                            switch (Modalita) {
                                                case "IMMAGINI":
                                                    switch (TipoOperazione) {
                                                        case "SCARICA":
                                                            VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.VISIBLE);

                                                            ChiamateWSMI wsmi = new ChiamateWSMI(context);
                                                            wsmi.UploadImmagine(NomeFile, result, bmImage, UrlImmagine);
                                                            break;
                                                        case "COPIA":
                                                            VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.VISIBLE);

                                                            ChiamateWsWPRefresh ws2 = new ChiamateWsWPRefresh(context);
                                                            ws2.ScriveImmagineSuSfondiLocale("DaImmagini/" + NomeFile, result);
                                                            break;
                                                        case "CONDIVIDI":
                                                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                                            StrictMode.setVmPolicy(builder.build());

                                                            File f = new File(NomeFileAppoggio);
                                                            Uri uri = FileProvider.getUriForFile(context,
                                                                    context.getApplicationContext().getPackageName() + ".provider",
                                                                    f);

                                                            Intent i = new Intent(Intent.ACTION_SEND);
                                                            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                                                            i.putExtra(Intent.EXTRA_SUBJECT, NomeFile);
                                                            i.putExtra(Intent.EXTRA_TEXT, "Dettagli nel file allegato");
                                                            i.putExtra(Intent.EXTRA_STREAM, uri);
                                                            i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                                                            context.startActivity(Intent.createChooser(i, "Share immagine"));
                                                            break;
                                                    }
                                                    break;
                                                case "PLAYER":
                                                    switch (TipoOperazione) {
                                                        case "SCARICA":
                                                            VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.VISIBLE);

                                                            ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                                                            ws.SalvaImmagineArtista(Filtro, NomeFile, result);
                                                            break;
                                                        case "COPIA":
                                                            VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.VISIBLE);

                                                            ChiamateWsWPRefresh ws2 = new ChiamateWsWPRefresh(context);
                                                            ws2.ScriveImmagineSuSfondiLocale("DaPlayer/" + NomeFile, result);
                                                            break;
                                                        case "CONDIVIDI":
                                                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                                            StrictMode.setVmPolicy(builder.build());

                                                            File f = new File(NomeFileAppoggio);
                                                            Uri uri = FileProvider.getUriForFile(context,
                                                                    context.getApplicationContext().getPackageName() + ".provider",
                                                                    f);

                                                            Intent i = new Intent(Intent.ACTION_SEND);
                                                            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                                                            i.putExtra(Intent.EXTRA_SUBJECT, NomeFile);
                                                            i.putExtra(Intent.EXTRA_TEXT, "Dettagli nel file allegato");
                                                            i.putExtra(Intent.EXTRA_STREAM, uri);
                                                            i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                                                            context.startActivity(Intent.createChooser(i, "Share immagine looWebPlayer"));
                                                            break;
                                                    }
                                                    break;
                                            }
                                        }
                                    } else {
                                        String NomeFileAppoggio = context.getFilesDir() + "/AppoggioLW/Scarico_" + numeroScarico + ".jpg";

                                        FileOutputStream outStream;
                                        try {
                                            outStream = new FileOutputStream(NomeFileAppoggio);
                                            finalMIcon1.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

                                            outStream.flush();
                                            outStream.close();

                                            if (txtInfoImmagine != null) {
                                                txtInfoImmagine.setText(finalMIcon1.getWidth() + "x" + finalMIcon1.getHeight() + " Kb:" + Files.getInstance().DimensioniFile(NomeFileAppoggio));
                                            }
                                        } catch (IOException ignored) {

                                        }
                                    }
                                }
                            }, 100);
                        } else {
                            ImpostaLogo(numeroScarico, txtInfoImmagine);
                        }
                    } else {
                        ImpostaLogo(numeroScarico, txtInfoImmagine);
                    }
                } catch (Exception e) {
                    ImpostaLogo(numeroScarico, txtInfoImmagine);

                    Errore = true;
                }

                // BloccaTimer();

                /* switch (Modalita) {
                    case "IMMAGINI":
                        UtilityImmagini.getInstance().Attesa(false);
                        break;
                    case "PLAYER":
                        UtilityPlayer.getInstance().Attesa(false);
                        break;
                } */

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }

    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;
    private int secondiPassati = 0;

    /* private void AttivaTimer() {
        secondiPassati = 0;

        handlerThread = new HandlerThread("background-thread_" +
                VariabiliStatichePlayer.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStatichePlayer.TimeoutImmagine) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                        in = null;
                    }

                    ImpostaLogo();

                    BloccaTimer();
                    BloccaEsecuzione();
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        };
        handler.postDelayed(r, 1000);
    }

    public void BloccaTimer() {
        if (handler != null && r != null && handlerThread != null) {
            handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    } */

    public void BloccaEsecuzione() {
        VariabiliStatichePlayer.getInstance().setDownImmagine(null);
        isCancelled = true;
    }

    private void ImpostaLogo(int numeroScarico, TextView txtInfoImmagine) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                bmImage.setImageBitmap(bitmap);

                String NomeFileAppoggio = context.getFilesDir() + "/AppoggioLW/Scarico_" + numeroScarico + ".jpg";

                FileOutputStream outStream;
                try {
                    outStream = new FileOutputStream(NomeFileAppoggio);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

                    outStream.flush();
                    outStream.close();

                    if (txtInfoImmagine != null) {
                        txtInfoImmagine.setText("");
                    }
                } catch (IOException ignored) {

                }

            }
        }, 10);
    }
}
