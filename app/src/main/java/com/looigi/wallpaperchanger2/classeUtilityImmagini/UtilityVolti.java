package com.looigi.wallpaperchanger2.classeUtilityImmagini;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.RilevamentoVolti;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UtilityVolti {
    private int countFilesInCurrentDirectory(File directory) {
        int count = 0;

        for (File file: directory.listFiles()) {
            if (file.isFile()) {
                count++;
            }
        }

        return count;
    }

    public void AggiungiVoltoAllAddestramento(Context context, ImageView img, int idCategoria, String Categoria) {
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();

        RilevamentoVolti rv = new RilevamentoVolti(context);
        rv.ElaboraImmagineDaBitmap(bitmap);

        Handler handler1 = new Handler(Looper.getMainLooper());
        Runnable r1 = new Runnable() {
            public void run() {
                if (!VariabiliStaticheWallpaper.getInstance().isStaPrendendoVolto()) {
                    handler1.removeCallbacks(this);

                    List<Rect> r = VariabiliStaticheWallpaper.getInstance().getQuadratiFaccia();

                    Bitmap bmpAppoggio = null;

                    if (r != null) {
                        int larghezzaImmagine = bitmap.getWidth();
                        int altezzaImmagine = bitmap.getHeight();

                        int inizioVisoX = 9999;
                        int inizioVisoY = 9999;
                        int larghezzaViso = -9999;
                        // int altezzaViso = -9999;

                        for (Rect r1 : r) {
                            if (r1.left < inizioVisoX) { inizioVisoX = r1.left; }
                            if (r1.top < inizioVisoY) { inizioVisoY = r1.top; }
                            if (r1.right > larghezzaViso) { larghezzaViso = r1.right; }
                        }

                        inizioVisoY -= (int) (altezzaImmagine * VariabiliStaticheWallpaper.percAumentoY);
                        if (inizioVisoY < 0) { inizioVisoY = 0; }
                        inizioVisoX -= (int) (larghezzaImmagine * VariabiliStaticheWallpaper.percAumentoX);
                        if (inizioVisoX < 0) { inizioVisoX = 0; }

                        try {
                            bmpAppoggio = Bitmap.createBitmap(
                                    bitmap,
                                    inizioVisoX,
                                    inizioVisoY,
                                    larghezzaViso,
                                    altezzaImmagine - inizioVisoY
                            );
                            bmpAppoggio = convertToBlackAndWhite(bmpAppoggio, 150, 150);

                            File dir = new File(context.getFilesDir(), "UtilityImmagini/Volti/" + idCategoria + "-" + Categoria);
                            if (!dir.exists()) dir.mkdirs();

                            int quantiFiles = countFilesInCurrentDirectory(dir);
                            quantiFiles++;

                            File file = new File(dir, quantiFiles + ".jpg");
                            FileOutputStream fos = new FileOutputStream(file);
                            bmpAppoggio.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                        } catch (Exception e) {
                        }
                    }
                } else {
                    handler1.postDelayed(this, 1000);
                }
            }
        };
        handler1.postDelayed(r1, 1000);
    }

    private Bitmap convertToBlackAndWhite(Bitmap original, int width, int height) {
        // 1. Ridimensiona l'immagine
        Bitmap resized = Bitmap.createScaledBitmap(original, width, height, true);

        // 2. Crea una bitmap vuota con le stesse dimensioni
        Bitmap bwBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 3. Cicla attraverso ogni pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = resized.getPixel(x, y);

                // Estrai i canali RGB
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                // Calcola la luminosità (media o luminanza)
                // int gray = (red + green + blue) / 3;
                int gray = (int)(0.3 * red + 0.59 * green + 0.11 * blue);

                // Soglia: scegli un valore (es. 128) per decidere se è bianco o nero
                if (gray < 128) {
                    bwBitmap.setPixel(x, y, Color.BLACK);
                } else {
                    bwBitmap.setPixel(x, y, Color.WHITE);
                }
            }
        }

        return bwBitmap;
    }
}
