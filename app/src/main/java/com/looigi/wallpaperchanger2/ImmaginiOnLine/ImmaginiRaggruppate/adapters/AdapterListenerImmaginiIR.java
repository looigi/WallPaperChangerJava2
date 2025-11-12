package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.adapters;

import android.app.AlertDialog;
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
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRicerca.VariabiliImmaginiFuoriCategoria;
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
            txtNomeFile.setText(UtilitiesGlobali.getInstance().EvidenziaTesto(Immagini.get(i).getNomeFile(), VariabiliImmaginiFuoriCategoria.getInstance().getTestoRicercato()));
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

    private SpannableString TornaDettaglio(StrutturaImmagineRaggruppata s) {
        String Testo = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Testo:", s.getTesto());
        String Luoghi = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Luoghi:", s.getLuoghi());
        String Oggetti = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Oggetti:", s.getOggetti());
        String Tags = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Tags:", s.getTags());
        String Volti = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Volti:", s.getVolti());
        String Desc = UtilitiesGlobali.getInstance().RitornaTestoDescrizioniSistemato("Descr.:", s.getDescrizione());

        return UtilitiesGlobali.getInstance().EvidenziaTesto(Testo + Luoghi + Oggetti + Tags + Volti + Desc, VariabiliImmaginiFuoriCategoria.getInstance().getTestoRicercato());
    }
}
