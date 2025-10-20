package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.VariabiliStaticheImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.strutture.StrutturaImmagineRaggruppata;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.MainPreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.List;

public class AdapterListenerImmaginiIR extends BaseAdapter {
    private Context context;
    private List<StrutturaImmagineRaggruppata> Immagini;
    private LayoutInflater inflater;

    public AdapterListenerImmaginiIR(Context applicationContext, List<StrutturaImmagineRaggruppata> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Immagini.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // if (view != null) return view;

        view = inflater.inflate(R.layout.lista_immagini_ir, null);

        if (i < Immagini.size()) {
            ImageView imgImmagine = view.findViewById(R.id.imgImmagine);
            ImageView imgSposta = view.findViewById(R.id.imgSpostaACategoria);
            TextView txtIdImmagine = view.findViewById(R.id.txtIdImmagine);
            TextView txtCartella = view.findViewById(R.id.txtCartella);
            TextView txtNomeFile = view.findViewById(R.id.txtNomeFile);
            TextView txtDimeFile = view.findViewById(R.id.txtDimensioneFile);
            TextView txtDimeImm = view.findViewById(R.id.txtDimensioneImmagine);
            TextView txtDettaglio = view.findViewById(R.id.txtDettaglio);

            txtDettaglio.setText(TornaDettaglio(Immagini.get(i)));

            CheckBox chkScelta = view.findViewById(R.id.chkScelta);
            if (Immagini.get(i).isSelezionata()) {
                chkScelta.setChecked(true);
            } else {
                chkScelta.setChecked(false);
            }
            chkScelta.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Immagini.get(i).setSelezionata(chkScelta.isChecked());
                }
            });

            DownloadImmagineUguali d = new DownloadImmagineUguali();
            d.EsegueDownload(context, imgImmagine, Immagini.get(i).getUrlImmagine());

            txtIdImmagine.setText("Id Immagine: " + Immagini.get(i).getIdImmagine());
            txtCartella.setText("Cartella: " + Immagini.get(i).getCartella());
            txtNomeFile.setText(EvidenziaTesto(Immagini.get(i).getNomeFile()));
            txtDimeFile.setText("Size: " + Immagini.get(i).getDimensioneFile());
            txtDimeImm.setText("Dim.: " + Immagini.get(i).getDimensioniImmagine());

            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    /* VariabiliStaticheImmaginiRaggruppate.getInstance().getLaypreview().setVisibility(LinearLayout.VISIBLE);
                    d.EsegueDownload(
                            context,
                            VariabiliStaticheImmaginiRaggruppate.getInstance().getImgPreview(),
                            Immagini.get(i).getUrlImmagine()
                    ); */

                    StrutturaImmaginiLibrary si = new StrutturaImmaginiLibrary();
                    si.setUrlImmagine(Immagini.get(i).getUrlImmagine());
                    si.setNomeFile(Immagini.get(i).getNomeFile());
                    si.setIdImmagine(Immagini.get(i).getIdImmagine());
                    VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);

                    Intent i = new Intent(context, MainPreview.class);
                    i.putExtra("Modalita", "ImmaginiRaggruppate");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }
            });

            imgSposta.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String NuovaCategoria = VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata().toUpperCase().trim();

                    if (VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata() == null) {
                        ChiamateWSMI c = new ChiamateWSMI(context);
                        c.RitornaCategorie(false, "FC");
                    }

                    VariabiliStaticheImmaginiRaggruppate.getInstance().setCategoriaImpostata("");
                    // if (VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento() == null) {
                    for (StrutturaImmaginiCategorie s : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                        if (s.getCategoria().toUpperCase().trim().equals(NuovaCategoria)) {
                            VariabiliStaticheImmaginiRaggruppate.getInstance().setCategoriaImpostata(s.getCategoria());
                            break;
                        }
                    }
                    // }

                    if (VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata() == null ||
                            VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata().isEmpty()) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "Selezionare una categoria di destinazione");
                        return;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Immagini raggruppate");
                    builder.setMessage("Si vuole spostare l\'immagine selezionata alla categoria " +
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata() + " ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StrutturaImmaginiLibrary Imm =  new StrutturaImmaginiLibrary();
                            Imm.setAlias(Immagini.get(i).getAlias());
                            Imm.setCategoria(Immagini.get(i).getCategoria());
                            Imm.setCartella(Immagini.get(i).getCartella());
                            Imm.setIdCategoria(Immagini.get(i).getIdCategoria());
                            Imm.setTag(Immagini.get(i).getTag());
                            Imm.setDataCreazione(Immagini.get(i).getDataCreazione());
                            Imm.setDataModifica(Immagini.get(i).getDataModifica());
                            Imm.setDimensioneFile((int) Immagini.get(i).getDimensioneFile());
                            Imm.setIdImmagine(Immagini.get(i).getIdImmagine());
                            Imm.setDimensioniImmagine(Immagini.get(i).getDimensioniImmagine());
                            Imm.setNomeFile(Immagini.get(i).getNomeFile());
                            Imm.setPathImmagine(Immagini.get(i).getPathImmagine());
                            Imm.setUrlImmagine(Immagini.get(i).getUrlImmagine());

                            ChiamateWSMI ws = new ChiamateWSMI(context);
                            ws.SpostaImmagine(Imm, "IR");
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }

        return view;
    }

    private String ControllaValiditaTesto(String Testo, String Cosa) {
        String Testo2 = Testo.trim();

        if (!Testo2.equals(";") && !Testo2.isEmpty()) {
            if (Testo2.contains("(")) {
                Testo2 = Testo2.substring(0, Testo2.indexOf("(")).trim();
            }
            Testo2 = Testo2.replace(";", " ");
            Testo2 = formatCamelCase(Testo2);

            Testo2 = Cosa + Testo2.trim();
        } else {
            Testo2 = "";
        }

        return Testo2;
    }

    private String formatCamelCase(String input) {
        // 1️⃣ Inserisce uno spazio prima di una maiuscola seguita da una minuscola
        String step1 = input.replaceAll("(?<!^)(?=[A-Z][a-z])", " ");

        // 2️⃣ Gestisce le sequenze di maiuscole seguite da minuscole (es: "HTTPServer" → "HTTP Server")
        String step2 = step1.replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2");

        // 3️⃣ Mette solo la prima lettera maiuscola, il resto minuscolo, in ogni parola
        StringBuilder result = new StringBuilder();
        for (String word : step2.split(" ")) {
            if (!word.isEmpty()) {
                if (word.length() > 1 && word.equals(word.toUpperCase())) {
                    // parola tutta maiuscola → lascia com'è (es. "HTTP")
                    result.append(word);
                } else {
                    // parola normale → prima maiuscola, resto minuscolo
                    result.append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1).toLowerCase());
                }
                result.append(" ");
            }
        }

        return result.toString().trim();
    }

    private SpannableString EvidenziaTesto(String text) {
        String testoDaEvidenziare1 = "";
        String testoDaEvidenziare2 = "";
        String[] testo = VariabiliImmaginiFuoriCategoria.getInstance().getTestoRicercato().split(";", -1);
        testoDaEvidenziare1 = testo[0];
        testoDaEvidenziare2 = testo[1];

        // Evita errori se il testo da evidenziare è nullo o vuoto
        if (testoDaEvidenziare1 == null || testoDaEvidenziare1.isEmpty()) {
            return new SpannableString(text);
        }

        SpannableString spannable = new SpannableString(text);

        // Usa indexOf in un ciclo per trovare tutte le occorrenze
        int start = text.toLowerCase().indexOf(testoDaEvidenziare1.toLowerCase());
        while (start >= 0) {
            int end = start + testoDaEvidenziare1.length();

            // Applica l'evidenziazione
            spannable.setSpan(
                    new BackgroundColorSpan(Color.YELLOW),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            // Cerca la prossima occorrenza
            start = text.toLowerCase().indexOf(testoDaEvidenziare1.toLowerCase(), end);
        }

        return spannable;
    }

    private SpannableString TornaDettaglio(StrutturaImmagineRaggruppata s) {
        String Testo = ControllaValiditaTesto(s.getTesto(), "TESTO: ");
        if (!Testo.isEmpty()) { Testo += "\n"; }

        String Luoghi = ControllaValiditaTesto(s.getLuoghi(), "LUOGHI: ");
        if (!Luoghi.isEmpty()) { Luoghi += "\n"; }

        String Oggetti = ControllaValiditaTesto(s.getOggetti(), "OGGETTI: ");
        if (!Oggetti.isEmpty()) { Oggetti += "\n"; }

        String Tags = ControllaValiditaTesto(s.getTags(), "TAGS: ");
        if (!Tags.isEmpty()) { Tags += "\n"; }

        String Volti = ControllaValiditaTesto(s.getVolti(), "VOLTI: ");
        if (!Volti.isEmpty()) { Volti += "\n"; }

        String Desc = ControllaValiditaTesto(s.getDescrizione(), "DESCR.: ");
        if (!Desc.isEmpty()) { Desc += "\n"; }

        return EvidenziaTesto(Testo + Luoghi + Oggetti + Tags + Volti + Desc);
    }
}
