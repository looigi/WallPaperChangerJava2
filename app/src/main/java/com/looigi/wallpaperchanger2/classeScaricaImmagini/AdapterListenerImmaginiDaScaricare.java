package com.looigi.wallpaperchanger2.classeScaricaImmagini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerImmaginiDaScaricare extends BaseAdapter {
    private Context context;
    private List<String> Immagini;
    private LayoutInflater inflater;
    private String Filtro;
    private String Modalita;

    public AdapterListenerImmaginiDaScaricare(Context applicationContext, String Modalita,
                                              String Filtro, List<String> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;
        this.Filtro = Filtro;
        this.Modalita = Modalita;

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
        if (view != null) return view;

        view = inflater.inflate(R.layout.lista_immagini_da_scaricare, null);

        if (i < Immagini.size()) {
            DownloadImmagineSI d = new DownloadImmagineSI();
            String UrlImmagine = Immagini.get(i);

            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);
            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliScaricaImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.VISIBLE);

                    d.EsegueDownload(context, VariabiliScaricaImmagini.getInstance().getImgPreview(), UrlImmagine, Modalita,
                            Filtro, false, "");
                }
            });

            d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                    Filtro, false, "");

            TextView tImmagine = (TextView) view.findViewById(R.id.txtImmagine);
            tImmagine.setText(UrlImmagine);

            CheckBox chkSelezionata = (CheckBox) view.findViewById(R.id.chkSelezionata);
            chkSelezionata.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (chkSelezionata.isChecked()) {
                        StrutturaImmagineDaScaricare s = new StrutturaImmagineDaScaricare();
                        s.setUrlImmagine(Immagini.get(i));
                        s.setImgImmagine(imgImmagine);
                        s.setChkSelezione(chkSelezionata);

                        VariabiliScaricaImmagini.getInstance().getListaDaScaricare().add(s);
                    } else {
                        List<StrutturaImmagineDaScaricare> l = new ArrayList<>();

                        for (StrutturaImmagineDaScaricare s : VariabiliScaricaImmagini.getInstance().getListaDaScaricare()) {
                            if (!s.getUrlImmagine().equals(Immagini.get(i))) {
                                l.add(s);
                            }
                        }

                        VariabiliScaricaImmagini.getInstance().setListaDaScaricare(l);
                    }

                    if (VariabiliScaricaImmagini.getInstance().getListaDaScaricare().isEmpty()) {
                        VariabiliScaricaImmagini.getInstance().getTxtSelezionate().setVisibility(LinearLayout.GONE);
                        VariabiliScaricaImmagini.getInstance().getImgScaricaTutte().setVisibility(LinearLayout.GONE);
                    } else {
                        VariabiliScaricaImmagini.getInstance().getTxtSelezionate().setVisibility(LinearLayout.VISIBLE);
                        VariabiliScaricaImmagini.getInstance().getTxtSelezionate().setText("Selezionate: " +
                                (VariabiliScaricaImmagini.getInstance().getListaDaScaricare().size()));
                        VariabiliScaricaImmagini.getInstance().getImgScaricaTutte().setVisibility(LinearLayout.VISIBLE);
                    }
                }
            });

            ImageView imgScarica = (ImageView) view.findViewById(R.id.imgScarica);
            imgScarica.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliScaricaImmagini.getInstance().setScaricaMultiplo(false);
                    VariabiliScaricaImmagini.getInstance().setImgScaricaDaDisabilitare(imgScarica);
                    VariabiliScaricaImmagini.getInstance().setChkSelezione(chkSelezionata);

                    d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                            Filtro, true, "SCARICA");
                }
            });

            ImageView imgCondividi = (ImageView) view.findViewById(R.id.imgCondividi);
            imgCondividi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                            Filtro, true, "CONDIVIDI");
                }
            });

            ImageView imgCopiaSuSfondi = (ImageView) view.findViewById(R.id.imgCopiaSuSfondi);
            imgCopiaSuSfondi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                            Filtro, true, "COPIA");
                }
            });
        }

        return view;
    }
}
