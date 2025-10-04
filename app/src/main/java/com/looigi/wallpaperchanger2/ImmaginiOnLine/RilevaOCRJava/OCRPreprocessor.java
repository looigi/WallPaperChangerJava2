package com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.util.Arrays;

public class OCRPreprocessor {
    // Converti in scala di grigi
    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        Bitmap resize = resizeIfNeeded(bmpOriginal);

        return toGrayscale2(resize);
    }

    private Bitmap resizeIfNeeded(Bitmap bmpOriginal) {
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();

        int maxWidth = 1024;
        int maxHeight = 768;

        if (width > maxWidth || height > maxHeight) {
            float ratioWidth = (float) maxWidth / width;
            float ratioHeight = (float) maxHeight / height;
            float ratio = Math.min(ratioWidth, ratioHeight);

            int newWidth = Math.round(width * ratio);
            int newHeight = Math.round(height * ratio);

            return Bitmap.createScaledBitmap(bmpOriginal, newWidth, newHeight, true);
        } else {
            return bmpOriginal; // già entro i limiti, restituisce l’originale
        }
    }

    private Bitmap toGrayscale2(Bitmap bmpOriginal) {
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // rimuove la saturazione → bianco e nero
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(filter);

        canvas.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static Bitmap enhanceContrastForOCR(Bitmap bmp, float contrast, float brightness) {
        // contrast: 1.0 = nessun cambiamento, >1 aumenta contrasto
        // brightness: 0 = nessun cambiamento, >0 più luminoso, <0 più scuro

        Bitmap bmpOut = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);

        float translateContrast = (-0.5f * contrast + 0.5f) * 255f;
        float translateBrightness = brightness * 255f;

        // Matrice combinata contrasto + luminosità
        ColorMatrix cm = new ColorMatrix(new float[]{
                contrast, 0, 0, 0, translateContrast + translateBrightness,
                0, contrast, 0, 0, translateContrast + translateBrightness,
                0, 0, contrast, 0, translateContrast + translateBrightness,
                0, 0, 0, 1, 0
        });

        Canvas canvas = new Canvas(bmpOut);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return bmpOut;
    }

    // Binarizzazione automatica (Otsu-like semplice)
    public static Bitmap binarize(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap binarized = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int total = width * height;
        int[] pixels = new int[total];
        int[] output = new int[total];

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        // Calcolo media
        long sum = 0; // usa long per evitare overflow
        for (int i = 0; i < total; i++) {
            int gray = (pixels[i] >> 16) & 0xFF; // canale rosso (RGB=grayscale)
            sum += gray;
        }
        int threshold = (int)(sum / total);

        // Applica soglia
        for (int i = 0; i < total; i++) {
            int gray = (pixels[i] >> 16) & 0xFF;
            int color = (gray > threshold) ? 255 : 0;
            output[i] = 0xFF000000 | (color << 16) | (color << 8) | color; // ARGB
        }

        binarized.setPixels(output, 0, width, 0, 0, width, height);
        return binarized;
    }

    public static Bitmap denoise(Bitmap bmp) {
        // LENTISSIMA !!!
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpOut = Bitmap.createBitmap(width, height, bmp.getConfig());

        int[] pixels = new int[width * height];
        int[] output = new int[width * height];

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        // Filtra con finestra 3x3
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int[] neighbors = new int[9];
                int k = 0;
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        neighbors[k++] = pixels[(y + j) * width + (x + i)];
                    }
                }
                Arrays.sort(neighbors);
                output[y * width + x] = neighbors[4]; // mediana
            }
        }

        // Copia bordi senza modifiche
        for (int x = 0; x < width; x++) {
            output[x] = pixels[x]; // top
            output[(height - 1) * width + x] = pixels[(height - 1) * width + x]; // bottom
        }
        for (int y = 0; y < height; y++) {
            output[y * width] = pixels[y * width]; // left
            output[y * width + (width - 1)] = pixels[y * width + (width - 1)]; // right
        }

        bmpOut.setPixels(output, 0, width, 0, 0, width, height);
        return bmpOut;
    }

    // Preprocessing completo
    public Bitmap preprocess(Bitmap original) {
        Bitmap gray = toGrayscale(original);
        Bitmap contrast = enhanceContrastForOCR(gray, 1, 0);
        // Bitmap denoise = denoise(contrast);
        // Bitmap binarized = binarize(contrast);

        return contrast;
    }
}
