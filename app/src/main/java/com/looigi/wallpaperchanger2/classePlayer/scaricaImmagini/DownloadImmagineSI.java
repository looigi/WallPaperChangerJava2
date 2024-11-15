package com.looigi.wallpaperchanger2.classePlayer.scaricaImmagini;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeFetekkie.MainMostraFetekkie;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
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
                               String Artista, boolean Salva, String TipoOperazione) {
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

                            String finalUrldisplay = urldisplay;
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bmImage.setImageBitmap(finalMIcon1);

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

                                            String result = UtilitiesGlobali.getInstance().convertBmpToBase64(NomeFileAppoggio);

                                            String[] n = UrlImmagine.split("/");
                                            String NomeFile = n[n.length - 1];
                                            if (!NomeFile.toUpperCase().contains(".JPG")) {
                                                NomeFile += ".jpg";
                                            }

                                            NomeFile = UtilitiesGlobali.getInstance().TogliePercentualiDalNome(NomeFile);

                                            switch (TipoOperazione) {
                                                case "SCARICA":
                                                    ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                                                    ws.SalvaImmagineArtista(Artista, NomeFile, result);
                                                    break;
                                                case "COPIA":
                                                    UtilityPlayer.getInstance().AttesaSI(true);

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
                                                    i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                                                    i.putExtra(Intent.EXTRA_STREAM,uri);
                                                    i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
                                                    context.startActivity(Intent.createChooser(i,"Share immagine looWebPlayer"));
                                                    break;
                                            }
                                        } catch (IOException ignored) {

                                        }
                                    }
                                }
                            }, 100);
                        } else {
                            ImpostaLogo();
                        }
                    } else {
                        ImpostaLogo();
                    }
                } catch (Exception e) {
                    ImpostaLogo();

                    Errore = true;
                }

                // BloccaTimer();

                UtilityPlayer.getInstance().Attesa(false);

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

    private void ImpostaLogo() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                bmImage.setImageBitmap(bitmap);
            }
        }, 10);
    }
}
