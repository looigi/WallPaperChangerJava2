package com.looigi.wallpaperchanger2.classePreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePreview.strutture.StrutturaVoltiRilevati;
import com.looigi.wallpaperchanger2.classePreview.webService.DownloadImmaginePreview;
import com.looigi.wallpaperchanger2.classeSpostamento.VariabiliStaticheSpostamento;
import com.looigi.wallpaperchanger2.classeSpostamento.strutture.StrutturaCategorieSpostamento;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class UtilitiesPreview {
    private static UtilitiesPreview instance = null;

    private UtilitiesPreview() {
    }

    public static UtilitiesPreview getInstance() {
        if (instance == null) {
            instance = new UtilitiesPreview();
        }

        return instance;
    }
    public void Attesa(boolean Acceso) {
        if (VariabiliStatichePreview.getInstance().getImgCaricamento() == null) {
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Acceso) {
                    VariabiliStatichePreview.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                } else {
                    VariabiliStatichePreview.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
                }
            }
        }, 50);
    }

    public void RitornoProssimaImmagine(Context context, StrutturaImmaginiLibrary si) {
        VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);

        DownloadImmaginePreview d = new DownloadImmaginePreview();
        d.EsegueChiamata(
                context,
                si.getNomeFile(),
                VariabiliStatichePreview.getInstance().getImgPreview(),
                si.getUrlImmagine()
        );
    }

    public void AggiornaCategorieSpostamento(Context context) {
        List<StrutturaImmaginiCategorie> l1 = new ArrayList<>();

        for (StrutturaImmaginiCategorie s : VariabiliStatichePreview.getInstance().getListaCategorie()) {
            if (s.getCategoria().toUpperCase().trim().contains(
                    VariabiliStatichePreview.getInstance().getFiltroCategoriaSpostamento().toUpperCase().trim())) {
                l1.add(s);
            }
        }

        String[] l = new String[l1.size()];
        int c = 0;
        for (StrutturaImmaginiCategorie s : l1) {
            l[c] = s.getCategoria();
            c++;
        }

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStatichePreview.getInstance().getSpnSpostaCategorie(),
                l,
                ""
        );
    }

    private static int NumeroCaratteri = 5;
    private List<String> CategorieMesse = new ArrayList<>();

    public void CercaCategoriaDaNome(Context context, String Nome) {
        VariabiliStatichePreview.getInstance().getLayTasti().setVisibility(LinearLayout.GONE);
        CategorieMesse = new ArrayList<>();

        String Nome2 = "";
        if (Nome.contains("/") && Nome.toUpperCase().contains("HTTP")) {
            String[] n = Nome.split("/");
            Nome2 = n[n.length - 1].toUpperCase().trim();
        } else {
            Nome2 = Nome.toUpperCase().trim();
        }

        boolean ok = false;
        for (StrutturaImmaginiCategorie s : VariabiliStatichePreview.getInstance().getListaCategorie()) {
            String Categoria = s.getCategoria();
            String Primo = "";
            String Secondo = "";

            if (Categoria.contains("_")) {
                String[] c = Categoria.split("_");
                Primo = c[0].toUpperCase().trim();
                if (Primo.length() > NumeroCaratteri) {
                    Primo = Primo.substring(0, NumeroCaratteri + 1);
                }
                Secondo = c[1].toUpperCase().trim();
                if (Secondo.length() > NumeroCaratteri) {
                    Secondo = Secondo.substring(0, NumeroCaratteri + 1);
                }
            } else {
                Primo = Categoria.toUpperCase().trim();
                if (Primo.length() > NumeroCaratteri) {
                    Primo = Primo.substring(0, NumeroCaratteri + 1);
                }
            }

            if (Nome2.contains(Primo) && Nome2.contains(Secondo)) {
                if (!Categoria.trim().replace("\n", "").isEmpty()) {
                    if (!CategorieMesse.contains(Categoria)) {
                        addDynamicButton(context, Categoria);
                        CategorieMesse.add(Categoria);
                        ok = true;
                    }
                }
            }
        }

        if (ok) {
            VariabiliStatichePreview.getInstance().getLayCategorieRilevate().setVisibility(LinearLayout.VISIBLE);
            VariabiliStatichePreview.getInstance().getLayTasti().setVisibility(LinearLayout.GONE);
        } else {
            VariabiliStatichePreview.getInstance().getLayCategorieRilevate().setVisibility(LinearLayout.GONE);
        }
    }

    public void LeggeTestoSuImmagine(Context context, Bitmap Immagine) {
        VariabiliStatichePreview.getInstance().getLayScritteRilevate().setVisibility(LinearLayout.GONE);

        InputImage image = InputImage.fromBitmap(Immagine, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    // Tutto il testo rilevato
                    String fullText = visionText.getText();
                    // Log.d("OCR", "Testo trovato: " + fullText);

                    // Analizzare per blocchi/parole
                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                        String blockText = block.getText();
                        // Log.d("OCR", "Blocco: " + blockText);

                        for (Text.Line line : block.getLines()) {
                            // Log.d("OCR", "Linea: " + line.getText());
                            String Nome2 = line.getText().toUpperCase().trim();
                            if (!Nome2.trim().replace("\n", "").isEmpty()) {
                                if (!CategorieMesse.contains(Nome2)) {
                                    addDynamicText(context, Nome2);
                                    VariabiliStatichePreview.getInstance().getLayScritteRilevate().setVisibility(LinearLayout.VISIBLE);
                                    VariabiliStatichePreview.getInstance().getLayTasti().setVisibility(LinearLayout.VISIBLE);
                                    CategorieMesse.add(Nome2);
                                }
                            }


                            for (StrutturaImmaginiCategorie s : VariabiliStatichePreview.getInstance().getListaCategorie()) {
                                String Categoria = s.getCategoria();
                                String Primo = "";
                                String Secondo = "";

                                if (Categoria.contains("_")) {
                                    String[] c = Categoria.split("_");
                                    Primo = c[0].toUpperCase().trim();
                                    if (Primo.length() > NumeroCaratteri) {
                                        Primo = Primo.substring(0, NumeroCaratteri + 1);
                                    }
                                    Secondo = c[1].toUpperCase().trim();
                                    if (Secondo.length() > NumeroCaratteri) {
                                        Secondo = Secondo.substring(0, NumeroCaratteri + 1);
                                    }
                                } else {
                                    Primo = Categoria.toUpperCase().trim();
                                    if (Primo.length() > NumeroCaratteri) {
                                        Primo = Primo.substring(0, NumeroCaratteri + 1);
                                    }
                                }

                                if (Nome2.contains(Primo) && Nome2.contains(Secondo)) {
                                    if (!Categoria.trim().replace("\n", "").isEmpty()) {
                                        if (!CategorieMesse.contains(Categoria)) {
                                            addDynamicButton(context, Categoria);
                                            VariabiliStatichePreview.getInstance().getLayCategorieRilevate().setVisibility(LinearLayout.VISIBLE);
                                            VariabiliStatichePreview.getInstance().getLayTasti().setVisibility(LinearLayout.VISIBLE);
                                            CategorieMesse.add(Categoria);
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Log.e("OCR", "Errore OCR", e);
                });
    }

    private void addDynamicText(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(14);
        textView.setPadding(3, 3, 3, 3);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(1, 1, 1, 1);

        textView.setLayoutParams(params);

        // Aggiungi la TextView al Flexbox
        VariabiliStatichePreview.getInstance().getLayScritteRilevate().addView(textView);
    }

    private void addDynamicButton(Context context, String text) {
        Button button = new Button(context);
        button.setText(text);
        button.setIncludeFontPadding(false);
        button.setTextSize(9);
        button.setTextColor(Color.BLACK);
        button.setMinHeight(0);
        button.setMinimumHeight(0);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(1, 1, 1, 1);
        button.setLayoutParams(params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImpostaIdCategoria(text);

                String idImmagine = String.valueOf(VariabiliStatichePreview.getInstance().getStrutturaImmagine().getIdImmagine());

                StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary(); // CREARE LA STRUTTURA PER LO SPOSTAMENTO
                s.setIdImmagine(Integer.parseInt(idImmagine));

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "PREVIEW");
            }
        });

        // Aggiungi al layout
        VariabiliStatichePreview.getInstance().getLayCategorieRilevate().addView(button);
    }

    public void ImpostaIdCategoria(String Categoria) {
        VariabiliStatichePreview.getInstance().setIdCategoriaSpostamento(-1);

        for (StrutturaImmaginiCategorie c: VariabiliStatichePreview.getInstance().getListaCategorie()) {
            if (c.getCategoria().equals(Categoria)) {
                VariabiliStatichePreview.getInstance().setIdCategoriaSpostamento(c.getIdCategoria());
                break;
            }
        }
    }
}
