package com.looigi.wallpaperchanger2.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.looigi.wallpaperchanger2.classePreview.UtilitiesPreview;
import com.looigi.wallpaperchanger2.classePreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.classeSpostamento.VariabiliStaticheSpostamento;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

public class UtilitiesLetturaInfoImmagine {
    private static int NumeroCaratteri = 5;
    private List<String> CategorieMesse = new ArrayList<>();
    private LinearLayout layTasti;
    private FlexboxLayout layScritte;
    private FlexboxLayout layCategorie;
    private List<StrutturaImmaginiCategorie> listaCategorie;
    private String CategorieGiaMesse = "";
    private static final int soglia = 2;

    public UtilitiesLetturaInfoImmagine() {
    }

    public void Pulisce() {
        if (layScritte != null && layCategorie != null) {
            layScritte.removeAllViews();
            layCategorie.removeAllViews();
            layScritte.setVisibility(LinearLayout.GONE);
            layCategorie.setVisibility(LinearLayout.GONE);
        }
    }

    public void ImpostaCategorieGiaMesse(String categorieGiaMesse) {
        CategorieGiaMesse = categorieGiaMesse;
    }

    public void ImpostaListaCategorie(List<StrutturaImmaginiCategorie> lista) {
        this.listaCategorie = lista;
    }

    public void ImpostaLayTasti(LinearLayout layTasti) {
        this.layTasti = layTasti;
    }

    public void ImpostaLayCategorie(FlexboxLayout layCategorie) {
        this.layCategorie = layCategorie;
    }
    
    public void ImpostaLayScritte(FlexboxLayout layScritte) {
        this.layScritte = layScritte;    
    }

    public void CercaCategoriaDaTags(Context context, String Tags) {
        if (layTasti != null) {
            layTasti.setVisibility(LinearLayout.GONE);
        }
        CategorieMesse = new ArrayList<>();

        String Tags2 = "";
        if (Tags.contains(";")) {
            String[] n = Tags.split("/");
            Tags2 = n[n.length - 1].toUpperCase().trim();
        } else {
            Tags2 = Tags.toUpperCase().trim();
        }

        boolean ok = false;
        String[] n;

        if (Tags2.contains(" ")) {
            n = Tags2.split(" ");
        } else {
            n = new String[]{Tags2};
        }
        for (String nn: n) {
            if (!isNumeric(nn) && nn.length() > 3) {
                String nnn = nn;
                if (nnn.contains(".")) {
                    nnn = nnn.substring(0, nnn.indexOf("."));
                }
                addDynamicText(context, nnn);
                ok = true;

                String Categoria = RitornaDistanza(nnn, listaCategorie);
                if (!Categoria.trim().replace("\n", "").isEmpty()) {
                    String[] c;

                    if (Categoria.contains(";")) {
                        c = Categoria.split(";");
                    } else {
                        c = new String[]{Categoria};
                    }
                    for (String cc : c) {
                        if (!CategorieMesse.contains(cc) && !CategorieGiaMesse.contains(cc)) {
                            addDynamicButton(context, cc);
                            CategorieMesse.add(cc);
                        }
                    }
                }
            }
        }

        // if (layTasti != null && layCategorie != null) {
        if (ok) {
            layCategorie.setVisibility(LinearLayout.VISIBLE);
            layTasti.setVisibility(LinearLayout.VISIBLE);
            layScritte.setVisibility(LinearLayout.VISIBLE);
            // } else {
            //     layCategorie.setVisibility(LinearLayout.GONE);
            //     layTasti.setVisibility(LinearLayout.VISIBLE);
        }
        // }
    }

    public void CercaCategoriaDaNome(Context context, String Nome) {
        if (layTasti != null) {
            layTasti.setVisibility(LinearLayout.GONE);
        }
        CategorieMesse = new ArrayList<>();

        String Nome2 = "";
        if (Nome.contains("/") && Nome.toUpperCase().contains("HTTP")) {
            String[] n = Nome.split("/");
            Nome2 = n[n.length - 1].toUpperCase().trim();
        } else {
            Nome2 = Nome.toUpperCase().trim();
        }

        boolean ok = false;
        String[] n;

        if (Nome2.contains(" ")) {
            n = Nome2.split(" ");
        } else {
            n = new String[]{Nome2};
        }
        for (String nn: n) {
            if (!isNumeric(nn)) {
                String nnn = nn;
                if (nnn.contains(".")) {
                    nnn = nnn.substring(0, nnn.indexOf("."));
                }
                addDynamicText(context, nnn);
                ok = true;

                String Categoria = RitornaDistanza(nnn, listaCategorie);
                if (!Categoria.trim().replace("\n", "").isEmpty()) {
                    String[] c;

                    if (Categoria.contains(";")) {
                        c = Categoria.split(";");
                    } else {
                        c = new String[]{Categoria};
                    }
                    for (String cc : c) {
                        if (!CategorieMesse.contains(cc) && !CategorieGiaMesse.contains(cc)) {
                            addDynamicButton(context, cc);
                            CategorieMesse.add(cc);
                        }
                    }
                }
            }
        }

        // if (layTasti != null && layCategorie != null) {
            if (ok) {
                layCategorie.setVisibility(LinearLayout.VISIBLE);
                layTasti.setVisibility(LinearLayout.VISIBLE);
                layScritte.setVisibility(LinearLayout.VISIBLE);
            // } else {
            //     layCategorie.setVisibility(LinearLayout.GONE);
            //     layTasti.setVisibility(LinearLayout.VISIBLE);
            }
        // }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void CercaCategoriaSuExif(Context context, StrutturaImmaginiLibrary s) {
        boolean ok = false;

        if (s != null) {
            if (s.getExif() != null && !s.getExif().isEmpty() && s.getExif().length() > 3) {
                // 68.media.tumblr.com;3a919c1d2dc25de338973cb3388743fa;§picDROP§17/01/2017 20:44:51§
                String Exif = s.getExif();
                List<String> a1 = new ArrayList<>();
                String[] appo1 = Exif.split("§");
                for (String a12: appo1) {
                    String[] a13 = a12.split(";");
                    for (String a14: a13) {
                        if (a14.contains(".")) {
                            String[] aa = a14.split("\\.", -1);
                            for (String aaa: aa) {
                                if (aaa.length() > 3 && !isNumeric(aaa)) {
                                    a1.add(aaa);
                                }
                            }
                        } else {
                            a1.add(a14);
                        }
                    }
                }

                for (String ee: a1) {
                    String Categoria = RitornaDistanza(ee, listaCategorie);

                    addDynamicText(context, ee);

                    if (!Categoria.isEmpty() && !Categoria.trim().replace("\n", "").isEmpty()) {
                        String[] c;

                        if (Categoria.contains(";")) {
                            c = Categoria.split(";");
                        } else {
                            c = new String[]{Categoria};
                        }
                        for (String cc: c) {
                            if (!CategorieMesse.contains(cc)) {
                                addDynamicButton(context, cc);
                                layCategorie.setVisibility(LinearLayout.VISIBLE);
                                layTasti.setVisibility(LinearLayout.VISIBLE);
                                CategorieMesse.add(cc);
                                ok = true;
                            }
                        }
                    }
                }
            }
        }

        // if (layTasti != null && layCategorie != null) {
        if (ok) {
            layCategorie.setVisibility(LinearLayout.VISIBLE);
            layTasti.setVisibility(LinearLayout.VISIBLE);
            layScritte.setVisibility(LinearLayout.VISIBLE);
            // } else {
            //     layCategorie.setVisibility(LinearLayout.GONE);
            //     layTasti.setVisibility(LinearLayout.VISIBLE);
        }
        // }
    }

    public void LeggeTestoSuImmagine(Context context, Bitmap Immagine) {
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
                            if (!Nome2.trim().replace("\n", "").isEmpty() && Nome2.length() > 3) {
                                addDynamicText(context, Nome2);
                                if (layScritte != null && layTasti != null) {
                                    layScritte.setVisibility(LinearLayout.VISIBLE);
                                    layTasti.setVisibility(LinearLayout.VISIBLE);
                                }
                                if (!CategorieMesse.contains(Nome2) && !CategorieGiaMesse.contains(Nome2)) {
                                    CategorieMesse.add(Nome2);

                                    String Categoria = RitornaDistanza(Nome2, listaCategorie);
                                    if (!Categoria.isEmpty()) {
                                        String[] c;

                                        if (Categoria.contains(";")) {
                                            c = Categoria.split(";");
                                        } else {
                                            c = new String[]{Categoria};
                                        }
                                        for (String cc: c) {
                                            if (!CategorieMesse.contains(cc) && !CategorieGiaMesse.contains(cc) && cc.length() > 3) {
                                                addDynamicButton(context, cc);
                                                if (layScritte != null && layTasti != null) {
                                                    layCategorie.setVisibility(LinearLayout.VISIBLE);
                                                    layTasti.setVisibility(LinearLayout.VISIBLE);
                                                }
                                                CategorieMesse.add(cc);
                                            }
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

    private String RitornaDistanza(String Filtro, List<StrutturaImmaginiCategorie> nomi) {
        LevenshteinDistance distance = new LevenshteinDistance();

        String ritorno = "";

        String Query = Filtro.toLowerCase().trim();
        if (Query.contains(".")) {
            Query = Query.substring(0, Query.indexOf("."));
        }

        if (Query.length() > 3 && !Query.equals("www")) {
            for (StrutturaImmaginiCategorie nome : nomi) {
                int dist = distance.apply(Query, nome.getCategoria().toLowerCase().trim());
                if (dist <= soglia || nome.getCategoria().toLowerCase().trim().contains(Filtro.toLowerCase().trim())) {
                    ritorno += nome.getCategoria() + ";";
                }
            }
        }

        return ritorno;
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
        if (layScritte != null) {
            layScritte.addView(textView);
        }
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

                VariabiliStaticheSpostamento.getInstance().AggiungeSpostata(
                        context,
                        text
                );
                UtilitiesPreview.getInstance().DisegnaUltimiSpostamenti(context);

                StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary(); // CREARE LA STRUTTURA PER LO SPOSTAMENTO
                s.setIdImmagine(Integer.parseInt(idImmagine));

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "PREVIEW");
            }
        });

        // Aggiungi al layout
        layCategorie.addView(button);
    }

    public void ImpostaIdCategoria(String Categoria) {
        VariabiliStatichePreview.getInstance().setIdCategoriaSpostamento(-1);

        for (StrutturaImmaginiCategorie c: listaCategorie) {
            if (c.getCategoria().equals(Categoria)) {
                VariabiliStatichePreview.getInstance().setIdCategoriaSpostamento(c.getIdCategoria());
                break;
            }
        }
    }
}
