package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRicerca.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRicerca.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRicerca.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.MainPreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.List;

public class AdapterListenerImmaginiFuoricategoria extends BaseAdapter {
    private Context context;
    private List<StrutturaImmagineFuoriCategoria> Immagini;
    private LayoutInflater inflater;

    public AdapterListenerImmaginiFuoricategoria(Context applicationContext, List<StrutturaImmagineFuoriCategoria> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (Immagini != null) {
            return Immagini.size();
        } else {
            return 0;
        }
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

        view = inflater.inflate(R.layout.lista_immagini_fuori_categoria, null);

        if (i < Immagini.size()) {
            ImageView imgImmagine = view.findViewById(R.id.imgImmagine);
            ImageView imgSposta = view.findViewById(R.id.imgSpostaACategoria);
            TextView txtIdImmagine = view.findViewById(R.id.txtIdImmagine);
            TextView txtCartella = view.findViewById(R.id.txtCartella);
            TextView txtCategoria = view.findViewById(R.id.txtCategoria);
            TextView txtNomeFile = view.findViewById(R.id.txtNomeFile);
            TextView txtDimeFile = view.findViewById(R.id.txtDimensioneFile);
            TextView txtDimeImm = view.findViewById(R.id.txtDimensioneImmagine);
            TextView txtDettaglio = view.findViewById(R.id.txtDettaglio);

            txtIdImmagine.setText("Id Immagine: " + Immagini.get(i).getIdImmagine());
            txtCategoria.setText("Categoria: " + UtilitiesGlobali.getInstance().EvidenziaTesto(Immagini.get(i).getCategoria(), VariabiliImmaginiFuoriCategoria.getInstance().getTestoRicercato()));
            txtCartella.setText("Cartella: " + Immagini.get(i).getCartella());
            txtNomeFile.setText(UtilitiesGlobali.getInstance().EvidenziaTesto(Immagini.get(i).getNomeFile(), VariabiliImmaginiFuoriCategoria.getInstance().getTestoRicercato()));
            txtDimeFile.setText("Size: " + Immagini.get(i).getDimensioneFile());
            txtDimeImm.setText("Dim.: " + Immagini.get(i).getDimensioniImmagine());

            txtDettaglio.setText(TornaDettaglio(Immagini.get(i)));

            DownloadImmagineUguali d = new DownloadImmagineUguali();
            d.EsegueDownload(context, imgImmagine, Immagini.get(i).getUrlImmagine());

            CheckBox chkSeleziona = view.findViewById(R.id.chkScelta);
            chkSeleziona.setChecked(Immagini.get(i).isSelezionata());

            chkSeleziona.setVisibility(LinearLayout.VISIBLE);
            imgSposta.setVisibility(LinearLayout.VISIBLE);
            if (VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaRicerca() != null) {
                if (Immagini.get(i).getCategoria().toUpperCase().trim().contains(VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaRicerca().toUpperCase().trim())) {
                    chkSeleziona.setChecked(false);
                    chkSeleziona.setVisibility(LinearLayout.GONE);
                    imgSposta.setVisibility(LinearLayout.GONE);
                    int i2 = 0;
                    for (StrutturaImmagineFuoriCategoria s : VariabiliImmaginiFuoriCategoria.getInstance().getListaImmagini()) {
                        if (s.getIdImmagine() == Immagini.get(i).getIdImmagine()) {
                            StrutturaImmagineFuoriCategoria s2 = s;
                            s2.setSelezionata(false);
                            VariabiliImmaginiFuoriCategoria.getInstance().getListaImmagini().set(i2, s2);
                            Immagini.get(i).setSelezionata(false);
                            break;
                        }
                        i2++;
                    }
                }
            }

            chkSeleziona.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int i2 = 0;
                    for (StrutturaImmagineFuoriCategoria s : VariabiliImmaginiFuoriCategoria.getInstance().getListaImmagini()) {
                        if (s.getIdImmagine() == Immagini.get(i).getIdImmagine()) {
                            StrutturaImmagineFuoriCategoria s2 = s;
                            s2.setSelezionata(chkSeleziona.isChecked());
                            VariabiliImmaginiFuoriCategoria.getInstance().getListaImmagini().set(i2, s2);
                            Immagini.get(i).setSelezionata(chkSeleziona.isChecked());
                            break;
                        }
                        i2++;
                    }
                }
            });

            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // VariabiliImmaginiFuoriCategoria.getInstance().getLaypreview().setVisibility(LinearLayout.VISIBLE);
                    // d.EsegueDownload(context, VariabiliImmaginiFuoriCategoria.getInstance().getImgPreview(), Immagini.get(i).getUrlImmagine());

                    StrutturaImmaginiLibrary si = new StrutturaImmaginiLibrary();
                    si.setUrlImmagine(Immagini.get(i).getUrlImmagine());
                    si.setNomeFile(Immagini.get(i).getNomeFile());
                    si.setIdImmagine(Immagini.get(i).getIdImmagine());
                    VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);

                    Intent i = new Intent(context, MainPreview.class);
                    i.putExtra("Modalita", "ImmaginiFuoriCategoria");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            imgSposta.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String NuovaCategoria = VariabiliImmaginiFuoriCategoria.getInstance().getCategoria();

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setTitle("Immagini fuori categoria");
                    builder.setMessage("Si vuole spostare l'immagine selezionata alla categoria " +
                            NuovaCategoria + " ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM() == null) {
                                ChiamateWSMI c = new ChiamateWSMI(context);
                                c.RitornaCategorie(false, "FC");
                            }
                            String Nuova = VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaInserita().replace(" ", "_").trim().toUpperCase();
                            VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento("");
                            for (StrutturaImmaginiCategorie s : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                                if (s.getCategoria().toUpperCase().trim().equals(Nuova)) {
                                    VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento(
                                            Integer.toString(s.getIdCategoria())
                                    );
                                    break;
                                }
                            }
                            if (VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento().isEmpty()) {
                                UtilitiesGlobali.getInstance().ApreToast(context, "Categoria non rilevata: " +
                                        VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaInserita());
                                return;
                            }

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
                            ws.SpostaImmagine(Imm, "FC");
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

    private SpannableString TornaDettaglio(StrutturaImmagineFuoriCategoria s) {
        String Testo = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Testo:", s.getTesto());
        String Luoghi = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Luoghi:", s.getLuoghi());
        String Oggetti = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Oggetti:", s.getOggetti());
        String Tags = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Tags:", s.getTags());
        String Volti = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Volti:", s.getVolti());
        String Desc = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Descr.:", s.getDescrizione());

        return UtilitiesGlobali.getInstance().EvidenziaTesto(Testo + Luoghi + Oggetti + Tags + Volti + Desc, VariabiliImmaginiFuoriCategoria.getInstance().getTestoRicercato());
    }
}
