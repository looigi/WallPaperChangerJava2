package com.looigi.wallpaperchanger2.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
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
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePreview.UtilitiesPreview;
import com.looigi.wallpaperchanger2.classePreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.VariabiliStaticheRilevaOCRJava;
import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.strutture.StrutturaRilevaOCR;
import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.webService.ChiamateWSRilevaOCR;
import com.looigi.wallpaperchanger2.classeSpostamento.VariabiliStaticheSpostamento;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class UtilitiesLetturaInfoImmagine {
    private List<String> CategorieMesse = new ArrayList<>();
    private LinearLayout layTasti;
    private FlexboxLayout layScritte;
    private FlexboxLayout layCategorie;
    private List<StrutturaImmaginiCategorie> listaCategorie;
    private String CategorieGiaMesse = "";
    private static final int soglia = 2;
    private StrutturaImmaginiLibrary immagine;
    private final Context context;
    private String Url;
    private boolean StaLeggendoTesto = false;
    private Bitmap bitmap;
    private String sCategorieMesse = "";
    private boolean StaLeggendoTags = false;
    private String TagsRilevati = "";
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    private Handler handler2 = new Handler(Looper.getMainLooper());
    private Runnable runnable2;

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

    public UtilitiesLetturaInfoImmagine(Context context) {
        this.context = context;
    }

    public void AvviaControllo() {
        StaLeggendoTesto = false;
        StaLeggendoTags = false;
        CategorieMesse = new ArrayList<>();
        sCategorieMesse = "";
        TagsRilevati = "";
        Url = "";

        if (layScritte != null && layCategorie != null) {
            layScritte.removeAllViews();
            layCategorie.removeAllViews();
            layScritte.setVisibility(LinearLayout.GONE);
            layCategorie.setVisibility(LinearLayout.GONE);
        }

        CercaCategoriaDaNome();
        CercaCategoriaSuExif();
        LeggeTestoSuImmagine();
        LeggeTagsSuImmagine();

        FaseFinale();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public void setImmagine(StrutturaImmaginiLibrary immagine) {
        this.immagine = immagine;
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

    public void CercaCategoriaDaTags(String Tags) {
        if (layTasti != null) {
            layTasti.setVisibility(LinearLayout.GONE);
        }
        boolean ok = false;

        // if (s.getTags().isEmpty()) {
            String Tags2 = "";
            if (Tags.contains(";")) {
                String[] n = Tags.split("/");
                Tags2 = n[n.length - 1].toUpperCase().trim();
            } else {
                Tags2 = Tags.toUpperCase().trim();
            }

            String[] n;

            if (Tags2.contains(" ")) {
                n = Tags2.split(" ");
            } else {
                n = new String[]{Tags2};
            }
            for (String nn : n) {
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
        /* } else {
            if (!s.getTags().equals(";")) {
                CategorieMesse.add(s.getTags());

                ok = true;
            } else {
                ok = false;
            }
        } */

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

    public void CercaCategoriaDaNome() {
        boolean ok = false;

        // if (s.getTestoJava().isEmpty()) {
            if (layTasti != null) {
                layTasti.setVisibility(LinearLayout.GONE);
            }

            String Nome2 = "";
            if (Url.contains("/") && Url.toUpperCase().contains("HTTP")) {
                String[] n = Url.split("/");
                Nome2 = n[n.length - 1].toUpperCase().trim();
            } else {
                Nome2 = Url.toUpperCase().trim();
            }

            String[] n;

            if (Nome2.contains(" ")) {
                n = Nome2.split(" ");
            } else {
                n = new String[]{Nome2};
            }
            for (String nn : n) {
                if (!isNumeric(nn)) {
                    String nnn = nn;
                    if (nnn.contains(".")) {
                        nnn = nnn.substring(0, nnn.indexOf("."));
                    }
                    addDynamicText(context, nnn);
                    ok = true;

                    CategorieMesse.add(nnn);
                    sCategorieMesse += nnn + ";";
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
        /* } else {
            if (!s.getTestoJava().equals(";")) {
                CategorieMesse.add(s.getTestoJava());
                ok = true;
            }
        } */

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

    public void FaseFinale() {
        runnable2 = new Runnable() {
            @Override
            public void run() {
                if (!StaLeggendoTags && !StaLeggendoTesto) {
                    handler2.removeCallbacks(runnable2);
                    runnable2 = null;
                    handler2 = null;

                    CercaCategoriaDaTags(TagsRilevati);

                    FaseFinale2();
                } else {
                    handler2.postDelayed(this, 100);
                }
            }
        };
        handler2.post(runnable2);
    }

    private void FaseFinale2() {
        if (sCategorieMesse.isEmpty()) {
            sCategorieMesse = ";";
        }
        if (sCategorieMesse.length() > 1000) {
            sCategorieMesse = sCategorieMesse.substring(0, 995) + "...";
        }
        if (TagsRilevati.isEmpty()) {
            TagsRilevati = ";";
        }
        if (TagsRilevati.length() > 1000) {
            TagsRilevati = TagsRilevati.substring(0, 995) + "...";
        }

        //  || !TagsRilevati.equals(s.getTags()) || !sCategorieMesse.equals(s.getTestoJava())
        if (immagine.getTags().isEmpty() || immagine.getTestoJava().isEmpty()) {
            StrutturaRilevaOCR s2 = new StrutturaRilevaOCR();
            s2.setIdImmagine(String.valueOf(immagine.getIdImmagine()));
            VariabiliStaticheRilevaOCRJava.getInstance().setImmagineAttuale(s2);
            if (VariabiliStatichePreview.getInstance().getStrutturaImmagine() != null) {
                VariabiliStatichePreview.getInstance().getStrutturaImmagine().setTestoJava(sCategorieMesse);
                VariabiliStatichePreview.getInstance().getStrutturaImmagine().setTags(TagsRilevati);
                VariabiliStatichePreview.getInstance().AggiornaImmagineVisualizzata(VariabiliStatichePreview.getInstance().getStrutturaImmagine());
            }

            ChiamateWSRilevaOCR ws = new ChiamateWSRilevaOCR(context);
            ws.AggiornaTestoOcrDaJava(sCategorieMesse, TagsRilevati, "IMMAGINI");
        }
    }

    public void LeggeTagsSuImmagine() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!StaLeggendoTesto) {
                    handler.removeCallbacks(runnable);
                    runnable = null;
                    handler = null;

                    LeggeTagsSuImmagine2();
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnable);
    }

    private void LeggeTagsSuImmagine2() {
        // TagsRilevati = "";

        if (!immagine.getTags().isEmpty()) {
            TagsRilevati = immagine.getTags();
            return;
        }

        // if (s.getTags().isEmpty()) {
            StaLeggendoTags = true;
            InputImage image = InputImage.fromBitmap(bitmap, 0);
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
                StaLeggendoTags = false;
            });
        /* } else {
            TagsRilevati = s.getTags();
        } */
    }

    public void CercaCategoriaSuExif() {
        boolean ok = false;

        // if (s != null) {
        //     if (s.getTestoJava().isEmpty()) {
                if (immagine.getExif() != null && !immagine.getExif().isEmpty() && immagine.getExif().length() > 3) {
                    // 68.media.tumblr.com;3a919c1d2dc25de338973cb3388743fa;§picDROP§17/01/2017 20:44:51§
                    String Exif = immagine.getExif();
                    List<String> a1 = new ArrayList<>();
                    String[] appo1 = Exif.split("§");
                    for (String a12 : appo1) {
                        String[] a13 = a12.split(";");
                        for (String a14 : a13) {
                            if (a14.contains(".")) {
                                String[] aa = a14.split("\\.", -1);
                                for (String aaa : aa) {
                                    if (aaa.length() > 3 && !isNumeric(aaa)) {
                                        a1.add(aaa);
                                    }
                                }
                            } else {
                                a1.add(a14);
                            }
                        }
                    }

                    for (String ee : a1) {
                        sCategorieMesse += ee + ";";
                        String Categoria = RitornaDistanza(ee, listaCategorie);

                        addDynamicText(context, ee);

                        if (!Categoria.isEmpty() && !Categoria.trim().replace("\n", "").isEmpty()) {
                            String[] c;

                            if (Categoria.contains(";")) {
                                c = Categoria.split(";");
                            } else {
                                c = new String[]{Categoria};
                            }
                            for (String cc : c) {
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
        /*    } else {
                if (!s.getTestoJava().equals(";")) {
                    CategorieMesse.add(s.getTestoJava());
                    ok = true;
                }
            } */
        // }

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

    public void LeggeTestoSuImmagine() {
        if (!immagine.getTestoJava().isEmpty() && !immagine.getTestoJava().equals(";")) {
            StaLeggendoTesto = true;
            String Cate = immagine.getTestoJava();
            String[] c = Cate.split(";", -1);
            for (String cc: c) {
                if (!cc.isEmpty() && !CategorieMesse.contains(cc)) {
                    addDynamicText(context, cc);
                    CategorieMesse.add(cc);

                    sCategorieMesse += cc + ";";
                    String Categoria = RitornaDistanza(cc, listaCategorie);
                    if (!Categoria.isEmpty()) {
                        String[] c2;

                        if (Categoria.contains(";")) {
                            c2 = Categoria.split(";");
                        } else {
                            c2 = new String[]{Categoria};
                        }
                        for (String cc2 : c2) {
                            if (!CategorieMesse.contains(cc2) && !CategorieGiaMesse.contains(cc2) && cc2.length() > 3) {
                                addDynamicButton(context, cc2);
                                if (layScritte != null && layTasti != null) {
                                    layCategorie.setVisibility(LinearLayout.VISIBLE);
                                    layTasti.setVisibility(LinearLayout.VISIBLE);
                                }
                                CategorieMesse.add(cc2);
                            }
                        }
                    }
                }
            }
            if (layScritte != null && layTasti != null) {
                layScritte.setVisibility(LinearLayout.VISIBLE);
                layTasti.setVisibility(LinearLayout.VISIBLE);
            }
            StaLeggendoTesto = false;
            return;
        }

        // if (s.getTestoJava().isEmpty()) {
            StaLeggendoTesto = true;
            InputImage image = InputImage.fromBitmap(bitmap, 0);
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

                                        sCategorieMesse += Nome2 + ";";
                                        String Categoria = RitornaDistanza(Nome2, listaCategorie);
                                        if (!Categoria.isEmpty()) {
                                            String[] c;

                                            if (Categoria.contains(";")) {
                                                c = Categoria.split(";");
                                            } else {
                                                c = new String[]{Categoria};
                                            }
                                            for (String cc : c) {
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

                        StaLeggendoTesto = false;
                    })
                    .addOnFailureListener(e -> {
                        // Log.e("OCR", "Errore OCR", e);
                        StaLeggendoTesto = false;
                    });
        /* } else {
            if (!s.getTestoJava().equals(";")) {
                CategorieMesse.add(s.getTestoJava());
            }
        } */
    }

    private List<String> SplittaCampiPerDistanza(String Query, String Carattere) {
        List<String> Ritorno = new ArrayList<>();
        if (Query.contains(Carattere)) {
            String[] d = Query.split(Carattere, -1);
            Ritorno.addAll(Arrays.asList(d));
        // } else {
        //     Ritorno.add(Query);
        }

        return Ritorno;
    }

    private String RitornaDistanza(String Filtro, List<StrutturaImmaginiCategorie> nomi) {
        LevenshteinDistance distance = new LevenshteinDistance();

        String ritorno = "";

        String Query = Filtro.toLowerCase().trim();
        if (Query.contains(".")) {
            Query = Query.substring(0, Query.indexOf("."));
        }
        List<String> DaCercare = new ArrayList<>();
        // DaCercare.addAll(SplittaCampiPerDistanza(Query, "-"));
        // DaCercare.addAll(SplittaCampiPerDistanza(Query, " "));
        // DaCercare.addAll(SplittaCampiPerDistanza(Query, ";"));
        // DaCercare.addAll(SplittaCampiPerDistanza(Query, "."));
        // if (DaCercare.isEmpty()) {
            DaCercare.add(Query);
        // }

        for (String NomeDaCercare: DaCercare) {
            if (NomeDaCercare.length() > 3) {
                for (StrutturaImmaginiCategorie nome : nomi) {
                    int dist = distance.apply(NomeDaCercare, nome.getCategoria().toLowerCase().trim());
                    if (dist <= soglia || nome.getCategoria().toLowerCase().trim().contains(Filtro.toLowerCase().trim())) {
                        ritorno += nome.getCategoria() + ";";
                    }
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
        button.setTextSize(11);
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
