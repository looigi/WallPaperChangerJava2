package com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

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
import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.strutture.StrutturaRilevaOCR;
import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.webService.ChiamateWSRilevaOCR;
import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.webService.DownloadImmagineRilevaOCR;

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
        UtilitiesRilevaOCRJava.getInstance().Attesa(true);
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
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void LeggeTestoSuImmagine(Context context) {
        TagsRilevati = "";

        InputImage image = InputImage.fromBitmap(bitmap, 0);
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
                        LeggeTestoSuImmagine2(context);
                    }
                }, 50);
            });
        } else {
            TagsRilevati = VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale().getTags();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    LeggeTestoSuImmagine2(context);
                }
            }, 50);
        }
    }

    private void LeggeTestoSuImmagine2(Context context) {
        if (VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale().getTestoJava().isEmpty()) {
            ScrittaRilevata = "";

            InputImage image = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        // Tutto il testo rilevato
                        // String fullText = visionText.getText();
                        // Log.d("OCR", "Testo trovato: " + fullText);

                        // Analizzare per blocchi/parole
                        for (Text.TextBlock block : visionText.getTextBlocks()) {
                            String blockText = block.getText();
                            // Log.d("OCR", "Blocco: " + blockText);

                            for (Text.Line line : block.getLines()) {
                                // Log.d("OCR", "Linea: " + line.getText());
                                String Nome2 = line.getText().toUpperCase().trim();
                                if (!Nome2.trim().replace("\n", "").isEmpty() && Nome2.length() > 3) {
                                    if (!ScrittaRilevata.contains(Nome2)) {
                                        ScrittaRilevata += Nome2 + ";";
                                    }
                                }
                            }
                        }

                        if (ScrittaRilevata.length() > 1000) {
                            ScrittaRilevata = ScrittaRilevata.substring(0, 995) + "...";
                        }

                        StrutturaRilevaOCR s = VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale();
                        VariabiliStaticheRilevaOCRJava.getInstance().getTxtAvanzamento().setText(
                                "Rimanenti: " + s.getQuante() + " - " + s.getNomeFile() + " (" + s.getCategoria() + ")\n" +
                                        ScrittaRilevata
                        );

                        if (!ScrittaRilevata.isEmpty()) {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                                    ws.AggiornaTestoOcrDaJava(ScrittaRilevata, TagsRilevati);
                                }
                            }, 10);
                        } else {
                            ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                            ws.AggiornaTestoOcrDaJava(";", TagsRilevati);
                        }
                    })
                    .addOnFailureListener(e -> {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                                ws.AggiornaTestoOcrDaJava(";", TagsRilevati);
                            }
                        }, 10);
                    });
        } else {
            StrutturaRilevaOCR s = VariabiliStaticheRilevaOCRJava.getInstance().getImmagineAttuale();
            VariabiliStaticheRilevaOCRJava.getInstance().getTxtAvanzamento().setText(
                    "Rimanenti: " + s.getQuante() + " - " + s.getNomeFile() + " (" + s.getCategoria() + ")\n" +
                            s.getTestoJava()
            );
            ScrittaRilevata = s.getTestoJava();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
                    ws.AggiornaTestoOcrDaJava(ScrittaRilevata, TagsRilevati);
                }
            }, 50);
        }
    }

    public void Attesa(boolean Acceso) {
        if (VariabiliStaticheRilevaOCRJava.getInstance().getImgCaricamento() == null) {
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Acceso) {
                    VariabiliStaticheRilevaOCRJava.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                } else {
                    VariabiliStaticheRilevaOCRJava.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
                }
            }
        }, 50);
    }
}
