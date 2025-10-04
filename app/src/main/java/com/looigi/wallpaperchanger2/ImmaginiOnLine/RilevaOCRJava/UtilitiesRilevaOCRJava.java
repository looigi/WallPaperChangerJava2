package com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.strutture.StrutturaRilevaOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.webService.ChiamateWSRilevaOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.webService.DownloadImmagineRilevaOCR;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UtilitiesRilevaOCRJava {
    private static UtilitiesRilevaOCRJava instance = null;

    private UtilitiesRilevaOCRJava() {
    }

    public static UtilitiesRilevaOCRJava getInstance() {
        if (instance == null) {
            instance = new UtilitiesRilevaOCRJava();
        }

        return instance;
    }

    public void DisegnaImmagine(Context context) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheRilevaOCRJava.getInstance().getImgCaricamento(),
                true
        );

        StrutturaRilevaOCR s = VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale();

        DownloadImmagineRilevaOCR dO = new DownloadImmagineRilevaOCR();
        dO.EsegueChiamata(
                context,
                "",
                VariabiliStaticheRilevaOCRJava.getInstance().getImgImmagine(),
                s.getUrl()
        );
    }

    private Task<List<String>> PrendeTags(InputImage image) {
        ImageLabeler labeler = ImageLabeling.getClient(
                new ImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.7f) // filtra etichette con confidenza < 0.7
                        .build()
        );

        return labeler.process(image)
                .continueWith(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        throw task.getException() != null
                                ? task.getException()
                                : new Exception("Errore nel riconoscimento delle immagini");
                    }

                    List<ImageLabel> labels = task.getResult();

                    // Ordina per confidenza decrescente
                    labels.sort(Comparator.comparing(ImageLabel::getConfidence).reversed());

                    List<String> labelsText = new ArrayList<>();
                    for (ImageLabel label : labels) {
                        labelsText.add(label.getText());
                    }

                    return labelsText;
                });
    }

    private String ScrittaRilevata = "";
    private String TagsRilevati = "";
    private Bitmap bitmap1;
    private Bitmap bitmapOrignale;
    private int giro;

    public void setBitmap(Bitmap bitmap1, Bitmap bitmapOriginale) {
        this.bitmap1 = bitmap1;
        this.bitmapOrignale = bitmapOriginale;
    }

    public void LeggeTestoSuImmagine(Context context) {
        giro = 1;
        LeggeTagsSuImmagine(context);
    }

    private void LeggeTagsSuImmagine(Context context) {
        TagsRilevati = "";

        InputImage image = InputImage.fromBitmap(bitmap1, 0);
        if (VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale().getTags().isEmpty()) {
            PrendeTags(image).addOnSuccessListener(labels -> {
                String Tags = "";
                // Qui ricevi tutte le etichette
                for (String label : labels) {
                    if (label.length() > 3) {
                        Tags += label + ";";
                    }
                }
                TagsRilevati = Tags;
                if (TagsRilevati.length() > 1000) {
                    TagsRilevati = TagsRilevati.substring(0, 996) + "...";
                }

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LeggeTestoSuImmagine2(context, bitmap1);
                    }
                }, 50);
            });
        } else {
            TagsRilevati = VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale().getTags();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    LeggeTestoSuImmagine2(context, bitmap1);
                }
            }, 50);
        }
    }

    private void LeggeTestoSuImmagine2(Context context, Bitmap bitmap) {
        leggeTestoSuImmagine2(context, bitmap, new OCRCallback() {
            @Override
            public void onOCRCompleted(String testoRilevato) {
                if (VariabiliStaticheRilevaOCRJava.getInstance().getScrittaTrovata().isEmpty()) {
                    if (giro == 1) {
                        giro = 2;
                        LeggeTestoSuImmagine2(context, bitmapOrignale);
                    } else {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Qui sei sicuro che l’OCR è completato
                                ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                                ws.aggiornaTestoOcrDaJava(";", TagsRilevati, "OCR");
                            }
                        }, 50);
                    }
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Qui sei sicuro che l’OCR è completato
                            ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                            ws.aggiornaTestoOcrDaJava(testoRilevato, TagsRilevati, "OCR");
                        }
                    }, 50);
                }
            }

            @Override
            public void onOCRFailed() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Qui sei sicuro che l’OCR è completato
                        ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                        ws.aggiornaTestoOcrDaJava(";", TagsRilevati, "OCR");
                    }
                }, 50);
            }
        });
    }

    public interface OCRCallback {
        void onOCRCompleted(String testoRilevato);
        void onOCRFailed();
    }

    public void leggeTestoSuImmagine2(Context context, Bitmap bitmap, OCRCallback callback) {
        // Se abbiamo già testo salvato, usiamolo subito
        StrutturaRilevaOCR s = VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale();
        if (!s.getTestoJava().isEmpty()) {
            String testo = s.getTestoJava();
            ScrittaRilevata = testo;

            // Aggiorna UI
            VariabiliStaticheRilevaOCRJava.getInstance().getTxtAvanzamento().setText(
                    "Rimanenti: " + s.getQuante() + " - " + s.getNomeFile() + " (" + s.getCategoria() + ")\n" +
                            testo
            );

            // Chiama callback subito
            if (callback != null) {
                callback.onOCRCompleted(testo);
            }
            return;
        }

        // Altrimenti esegui OCR
        ScrittaRilevata = "";
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    StringBuilder sb = new StringBuilder();

                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                        for (Text.Line line : block.getLines()) {
                            String Nome2 = line.getText().toUpperCase().trim();
                            if (!Nome2.replace("\n", "").isEmpty() && Nome2.length() > 3 && sb.indexOf(Nome2) == -1) {
                                sb.append(Nome2).append(";");
                            }
                        }
                    }

                    ScrittaRilevata = sb.toString();
                    if (ScrittaRilevata.length() > 1000) {
                        ScrittaRilevata = ScrittaRilevata.substring(0, 995) + "...";
                    }
                    VariabiliStaticheRilevaOCRJava.getInstance().setScrittaTrovata(ScrittaRilevata);

                    // Aggiorna UI
                    VariabiliStaticheRilevaOCRJava.getInstance().getTxtAvanzamento().setText(
                            "Fatte: " + VariabiliStaticheRilevaOCRJava.getInstance().getFatte() + " " +
                                    "Rim: " + s.getQuante() +
                                    "\n" + s.getNomeFile() + "(" + s.getCategoria() + ")" +
                                    "\nIn. " + s.getInizio() + " Imm. " + s.getIdImmagine() +
                                    "\nEm. " + s.getQualeEmulatore() + "/" + s.getEmulatori() +
                                    "\n" + (ScrittaRilevata.isEmpty() ? ";" : ScrittaRilevata)
                    );

                    // Chiamata WS
                    if (callback != null) {
                        callback.onOCRCompleted(ScrittaRilevata.isEmpty() ? ";" : ScrittaRilevata);
                    }

                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onOCRFailed();
                    }
                });
    }
}
