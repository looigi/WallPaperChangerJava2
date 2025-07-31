package com.looigi.wallpaperchanger2.classeScaricaImmagini.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.DownloadImmagineSI;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.StrutturaImmagineDaScaricare;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterListenerImmaginiDaScaricare extends BaseAdapter {
    private Context context;
    private List<String> Immagini;
    private LayoutInflater inflater;
    private String Filtro;
    private String Modalita;
    private List<Integer> controlloCheckBox;

    public AdapterListenerImmaginiDaScaricare(Context applicationContext, String Modalita,
                                              String Filtro, List<String> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;
        this.Filtro = Filtro;
        this.Modalita = Modalita;
        controlloCheckBox = new ArrayList<>();
        VariabiliScaricaImmagini.getInstance().PulisceCartellaAppoggio(context);

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
        // if (view == null) {
            view = inflater.inflate(R.layout.lista_immagini_da_scaricare, null);

            String Operazione = "";
            if (VariabiliScaricaImmagini.getInstance().getListaOriginaleDaScaricare() != null
                && !VariabiliScaricaImmagini.getInstance().getListaOriginaleDaScaricare().isEmpty()) {
                if (i < VariabiliScaricaImmagini.getInstance().getListaOriginaleDaScaricare().size()) {
                    Operazione = VariabiliScaricaImmagini.getInstance().getListaOriginaleDaScaricare().get(i);
                }
            }
            switch(Operazione) {
                case "OK":
                    view.setBackgroundColor(Color.rgb(150, 255, 150));
                    break;
                case "ERRORE":
                    view.setBackgroundColor(Color.rgb(255, 150, 150));
                    break;
            }

            String NomeFileAppoggio = context.getFilesDir() + "/AppoggioLW/Scarico_" + i + ".jpg";

            if (i < Immagini.size()) {
                DownloadImmagineSI d = new DownloadImmagineSI();
                String UrlImmagine = Immagini.get(i);

                TextView txtInfoImmagine = view.findViewById(R.id.txtInfoImmagine);

                ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);
                imgImmagine.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        VariabiliScaricaImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.VISIBLE);

                        if (Files.getInstance().EsisteFile(NomeFileAppoggio)) {
                            Bitmap bitmap = BitmapFactory.decodeFile(NomeFileAppoggio);
                            VariabiliScaricaImmagini.getInstance().getImgPreview().setImageBitmap(bitmap);
                        } else {
                            d.EsegueDownload(context, VariabiliScaricaImmagini.getInstance().getImgPreview(), UrlImmagine, Modalita,
                                    Filtro, false, "", i, txtInfoImmagine);
                        }
                    }
                });

                if (Files.getInstance().EsisteFile(NomeFileAppoggio)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(NomeFileAppoggio);
                    imgImmagine.setImageBitmap(bitmap);

                    txtInfoImmagine.setText(bitmap.getWidth() + "x" + bitmap.getHeight() + " Kb:" + Files.getInstance().DimensioniFile(NomeFileAppoggio));
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.download);
                    imgImmagine.setImageBitmap(bitmap);

                    txtInfoImmagine.setText("");

                    d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                            Filtro, false, "", i, txtInfoImmagine);
                }

                TextView tImmagine = (TextView) view.findViewById(R.id.txtImmagine);
                tImmagine.setText(UrlImmagine);

                CheckBox chkSelezionata = (CheckBox) view.findViewById(R.id.chkSelezionata);

                if (controlloCheckBox.contains((i))) {
                    chkSelezionata.setChecked(true);
                } else {
                    chkSelezionata.setChecked(false);
                }

                chkSelezionata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            StrutturaImmagineDaScaricare s = new StrutturaImmagineDaScaricare();
                            s.setUrlImmagine(Immagini.get(i));
                            s.setImgImmagine(imgImmagine);
                            s.setChkSelezione(chkSelezionata);

                            VariabiliScaricaImmagini.getInstance().getListaDaScaricare().add(s);

                            controlloCheckBox.add(i);
                        } else {
                            List<StrutturaImmagineDaScaricare> l = new ArrayList<>();

                            for (StrutturaImmagineDaScaricare s : VariabiliScaricaImmagini.getInstance().getListaDaScaricare()) {
                                if (!s.getUrlImmagine().equals(Immagini.get(i))) {
                                    l.add(s);
                                }
                            }

                            VariabiliScaricaImmagini.getInstance().setListaDaScaricare(l);

                            List<Integer> ll = new ArrayList<>();
                            for (Integer lll : controlloCheckBox) {
                                if (lll != i) {
                                    ll.add(lll);
                                }
                            }
                            controlloCheckBox = ll;
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

                        if (!Files.getInstance().EsisteFile(NomeFileAppoggio)) {
                            d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                                    Filtro, true, "SCARICA", i, txtInfoImmagine);
                        } else {
                            if (Modalita.equals("IMMAGINI")) {
                                VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.VISIBLE);

                                String result = UtilitiesGlobali.getInstance().convertBmpToBase64(NomeFileAppoggio);

                                String[] n = UrlImmagine.split("/");
                                String NomeFile = n[n.length - 1];
                                if (!NomeFile.toUpperCase().contains(".JPG")) {
                                    NomeFile += ".jpg";
                                }

                                NomeFile = UtilitiesGlobali.getInstance().TogliePercentualiDalNome(NomeFile);

                                ChiamateWSMI wsmi = new ChiamateWSMI(context);
                                wsmi.UploadImmagine(NomeFile, result, imgImmagine, UrlImmagine);
                            } else {
                                String result = UtilitiesGlobali.getInstance().convertBmpToBase64(NomeFileAppoggio);

                                ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                                ws.SalvaImmagineArtista(Filtro, NomeFileAppoggio, result);
                            }
                        }
                    }
                });

                ImageView imgCondividi = (ImageView) view.findViewById(R.id.imgCondividi);
                imgCondividi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Files.getInstance().EsisteFile(NomeFileAppoggio)) {
                            String[] n = UrlImmagine.split("/");
                            String NomeFile = n[n.length - 1];
                            if (!NomeFile.toUpperCase().contains(".JPG")) {
                                NomeFile += ".jpg";
                            }

                            NomeFile = UtilitiesGlobali.getInstance().TogliePercentualiDalNome(NomeFile);

                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());

                            File f = new File(NomeFileAppoggio);
                            Uri uri = FileProvider.getUriForFile(context,
                                    context.getApplicationContext().getPackageName() + ".provider",
                                    f);

                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                            i.putExtra(Intent.EXTRA_SUBJECT, NomeFile);
                            i.putExtra(Intent.EXTRA_TEXT, "Dettagli nel file allegato");
                            i.putExtra(Intent.EXTRA_STREAM, uri);
                            i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                            context.startActivity(Intent.createChooser(i, "Share immagine"));
                        } else {
                            d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                                    Filtro, true, "CONDIVIDI", i, txtInfoImmagine);
                        }
                    }
                });

                ImageView imgCopiaSuSfondi = (ImageView) view.findViewById(R.id.imgCopiaSuSfondi);
                imgCopiaSuSfondi.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (!Files.getInstance().EsisteFile(NomeFileAppoggio)) {
                            d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                                    Filtro, true, "COPIA", i, txtInfoImmagine);
                        } else {

                            String[] n = UrlImmagine.split("/");
                            String NomeFile = n[n.length - 1];
                            if (!NomeFile.toUpperCase().contains(".JPG")) {
                                NomeFile += ".jpg";
                            }

                            NomeFile = UtilitiesGlobali.getInstance().TogliePercentualiDalNome(NomeFile);

                            VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.VISIBLE);

                            String result = UtilitiesGlobali.getInstance().convertBmpToBase64(NomeFileAppoggio);
                            ChiamateWsWPRefresh ws2 = new ChiamateWsWPRefresh(context);
                            ws2.ScriveImmagineSuSfondiLocale("DaImmagini/" + NomeFile, result);
                        }
                    }
                });
            }
        // }

        return view;
    }
}
