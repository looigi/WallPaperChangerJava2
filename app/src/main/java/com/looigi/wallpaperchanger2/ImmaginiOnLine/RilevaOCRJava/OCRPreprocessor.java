package com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava;

import android.graphics.Bitmap;
import android.graphics.Color;

public class OCRPreprocessor {
    // Converti in scala di grigi
    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width = bmpOriginal.getWidth();
        int height = bmpOriginal.getHeight();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bmpOriginal.getPixel(x, y);
                int gray = (int)(0.299 * Color.red(pixel) + 0.587 * Color.green(pixel) + 0.114 * Color.blue(pixel));
                bmpGrayscale.setPixel(x, y, Color.rgb(gray, gray, gray));
            }
        }
        return bmpGrayscale;
    }

    // Aumenta il contrasto
    public Bitmap enhanceContrast(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bmpContrast = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float contrast = 1.5f; // 1.0 = nessun cambiamento
        float translate = (-0.5f * contrast + 0.5f) * 255f;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bmp.getPixel(x, y);
                int r = (int)(Color.red(pixel) * contrast + translate);
                int g = (int)(Color.green(pixel) * contrast + translate);
                int b = (int)(Color.blue(pixel) * contrast + translate);

                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));

                bmpContrast.setPixel(x, y, Color.rgb(r, g, b));
            }
        }
        return bmpContrast;
    }

    // Binarizzazione automatica (Otsu-like semplice)
    public Bitmap binarize(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap binarized = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int sum = 0;
        int total = width * height;

        // Calcolo media
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bmp.getPixel(x, y);
                int gray = Color.red(pixel);
                sum += gray;
            }
        }
        int threshold = sum / total;

        // Applica soglia
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bmp.getPixel(x, y);
                int gray = Color.red(pixel);
                int color = (gray > threshold) ? 255 : 0;
                binarized.setPixel(x, y, Color.rgb(color, color, color));
            }
        }
        return binarized;
    }

    public Bitmap denoise(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap filtered = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int sum = 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = bmp.getPixel(x + kx, y + ky);
                        sum += Color.red(pixel);
                    }
                }
                int avg = sum / 9;
                filtered.setPixel(x, y, Color.rgb(avg, avg, avg));
            }
        }
        // Copia bordi senza modifiche
        for (int x = 0; x < width; x++) {
            filtered.setPixel(x, 0, bmp.getPixel(x, 0));
            filtered.setPixel(x, height - 1, bmp.getPixel(x, height - 1));
        }
        for (int y = 0; y < height; y++) {
            filtered.setPixel(0, y, bmp.getPixel(0, y));
            filtered.setPixel(width - 1, y, bmp.getPixel(width - 1, y));
        }

        return filtered;
    }

    // Preprocessing completo
    public Bitmap preprocess(Bitmap original) {
        Bitmap gray = toGrayscale(original);
        // Bitmap contrast = enhanceContrast(gray);
        // Bitmap denoise = denoise(contrast);
        // Bitmap binarized = binarize(contrast);

        return gray;
    }
}
