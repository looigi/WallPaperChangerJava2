package com.looigi.wallpaperchanger2.classePlayer.impostazioniInterne;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerAlbum;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerBrani;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadImmagine;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classePlayer.scan.ScanBraniNonPresentiSuDB;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.List;

public class impostazioni_player_interne {
    private Activity act;
    private Context context;
    private String Filtro = "";

    public impostazioni_player_interne(Activity act, Context context) {
        this.act = act;
        this.context = context;
    }

    public void impostaMaschera() {
        Button imgRicerche = (Button) act.findViewById(R.id.btnSettingsPlayerRicerche);
        imgRicerche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(0);

                ImpostazioniMascheraRicerca();
            }
        });

        Button imgBranilocali = (Button) act.findViewById(R.id.btnSettingsBraniLocali);
        imgBranilocali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(1);

                imposta_brani_locali();
            }
        });

        Button imgBraniOnLine = (Button) act.findViewById(R.id.btnSettingsBraniRemoti);
        imgBraniOnLine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(4);

                impostazioniMascheraBraniOnLine();
            }
        });

        Button imgSfondo = (Button) act.findViewById(R.id.btnSettingsImmagine);
        imgSfondo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(2);

                impostazioniMascheraSfondo();
            }
        });

        Button imgBrano = (Button) act.findViewById(R.id.btnSettingsBrano);
        imgBrano.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(3);

                impostazioniMascheraBrano();
            }
        });

        visualizzaImpostazioniMaschera(0);

        ImpostazioniMascheraRicerca();
    }

    public void imposta_brani_locali() {
        db_dati_player db = new db_dati_player(context);
        List<StrutturaBrano> lista = db.CaricaTuttiIBraniLocali();

        long spazioOccupato = 0;
        for (StrutturaBrano l : lista) {
            spazioOccupato += (Files.getInstance().DimensioniFile(l.getPathBrano()) * 1024L);
        }
        VariabiliStatichePlayer.getInstance().setSpazioOccupato(spazioOccupato);
        float lim = VariabiliStatichePlayer.getInstance().getLimiteInGb();
        long limiteSpazio = (long) (lim * 1024 * 1024 * 1024);
        VariabiliStatichePlayer.getInstance().setSpazioMassimo(limiteSpazio);

        VariabiliStatichePlayer.getInstance().setTxtQuanteRicerca(act.findViewById(R.id.txtQuanteRicercaPL));

        ListView lstBrani = act.findViewById(R.id.lstBrani);
        AdapterListenerBrani customAdapterT = new AdapterListenerBrani(context, lista);
        lstBrani.setAdapter(customAdapterT);

        EditText edtFiltro = act.findViewById(R.id.edtFiltro);
        ImageView imgRicercaScelta = (ImageView) act.findViewById(R.id.imgRicercaSceltaPL);
        imgRicercaScelta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Filtro = edtFiltro.getText().toString();
                customAdapterT.updateData(Filtro);
            }
        });

        ImageView imgRefreshBrani = (ImageView) act.findViewById(R.id.imgRefreshBraniDB);
        imgRefreshBrani.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db_dati_player db = new db_dati_player(context);
                db.EliminaTutto();

                ScanBraniNonPresentiSuDB s = new ScanBraniNonPresentiSuDB();
                s.controllaCanzoniNonSalvateSuDB(context, true);
            }
        });
    }

    private void ImpostazioniMascheraRicerca() {
        SwitchCompat swcRandom = act.findViewById(R.id.sRandom);
        swcRandom.setChecked(VariabiliStatichePlayer.getInstance().isRandom());
        swcRandom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setRandom(swcRandom.isChecked());

                db_dati_player db = new db_dati_player(context);
                db.ScriveRicerca();
            }
        });
    }

    private void impostazioniMascheraBrano() {
        ImageView imgCondividi = act.findViewById(R.id.imgCondividiBrano);
        imgCondividi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Path = VariabiliStatichePlayer.getInstance().getUltimoBrano().getPathBrano();

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                File f = new File(Path);
                Uri uri = FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider",
                        f);

                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, Path);
                i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                i.putExtra(Intent.EXTRA_STREAM,uri);
                i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
                context.startActivity(Intent.createChooser(i,"Share immagine"));
            }
        });
    }

    private void impostazioniMascheraSfondo() {
        VariabiliStatichePlayer.getInstance().setImgImposta(act.findViewById(R.id.imgImpostaWP));
        VariabiliStatichePlayer.getInstance().getImgImposta().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePlayer.getInstance().getPathUltimaImmagine() != null) {
                    UtilityPlayer.getInstance().Attesa(true);

                    String Path = VariabiliStatichePlayer.getInstance().getPathUltimaImmagine();
                    String[] N = Path.split("/");
                    String Nome = N[N.length - 1];
                    String Data = Files.getInstance().DataFile(Path).toString();
                    long Dimensione = Files.getInstance().DimensioniFile(Path);

                    StrutturaImmagine src = new StrutturaImmagine();
                    src.setPathImmagine(Path);
                    src.setImmagine(Nome);
                    src.setDimensione(String.valueOf(Dimensione));
                    src.setDataImmagine(Data);

                    ChangeWallpaper c = new ChangeWallpaper(context);
                    c.setWallpaperLocale(context, src);

                    UtilityPlayer.getInstance().Attesa(false);
                }
            }
        });

        VariabiliStatichePlayer.getInstance().setImgIndietroSfondo(act.findViewById(R.id.imgIndietroSfondoPlayer));
        VariabiliStatichePlayer.getInstance().getImgIndietroSfondo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int n = VariabiliStatichePlayer.getInstance().getIdImmagineImpostata();
                if (n - 1 > 0) {
                    n--;
                } else {
                    n = VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1;
                }
                VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(n);
                ImpostaImmagine();
            }
        });

        VariabiliStatichePlayer.getInstance().setImgAvantiSfondo(act.findViewById(R.id.imgAvantiSfondoPlayer));
        VariabiliStatichePlayer.getInstance().getImgAvantiSfondo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int n = VariabiliStatichePlayer.getInstance().getIdImmagineImpostata();
                if (n + 1 <= VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1) {
                    n++;
                } else {
                    n = 0;
                }
                VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(n);
                ImpostaImmagine();
            }
        });

        LinearLayout layTempoCambio = act.findViewById(R.id.layTempoCambio);

        SwitchCompat swcCambiaImmagine = act.findViewById(R.id.sCambiaImmagine);
        swcCambiaImmagine.setChecked(VariabiliStatichePlayer.getInstance().isCambiaImmagine());
        if (VariabiliStatichePlayer.getInstance().isCambiaImmagine()) {
            layTempoCambio.setVisibility(LinearLayout.VISIBLE);
        } else {
            layTempoCambio.setVisibility(LinearLayout.GONE);
        }
        swcCambiaImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePlayer.getInstance().setCambiaImmagine(swcCambiaImmagine.isChecked());
                if (VariabiliStatichePlayer.getInstance().isCambiaImmagine()) {
                    layTempoCambio.setVisibility(LinearLayout.VISIBLE);
                } else {
                    layTempoCambio.setVisibility(LinearLayout.GONE);
                }

                db_dati_player db = new db_dati_player(context);
                db.ScriveImpostazioni();
            }
        });

        EditText edtTempoCambio = act.findViewById(R.id.edtTempoCambio);
        edtTempoCambio.setText(Integer.toString(VariabiliStatichePlayer.getInstance().getTempoCambioImmagine()));
        edtTempoCambio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    VariabiliStatichePlayer.getInstance().setTempoCambioImmagine(Integer.parseInt(edtTempoCambio.getText().toString()));

                    db_dati_player db = new db_dati_player(context);
                    db.ScriveImpostazioni();
                }
            }
        });

        VariabiliStatichePlayer.getInstance().setImgEliminaSfondo(act.findViewById(R.id.imgEliminaSfondo));
        VariabiliStatichePlayer.getInstance().getImgEliminaSfondo().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliStatichePlayer.getInstance().isCeImmaginePerModifica()) {
                    return;
                }

                // String path = VariabiliStatichePlayer.getInstance().getPathUltimaImmagine();
                if (VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica() != null) {
                    StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica();
                    String path = s.getPathImmagine();
                    if (path != null) {
                        if (s != null) {
                            db_dati_player db = new db_dati_player(context);
                            db.EliminaImmagineFisica(s.getArtista(), s.getNomeImmagine());
                        }

                        if (Files.getInstance().EsisteFile(path)) {
                            Files.getInstance().EliminaFileUnico(path);
                        }

                        if (s != null) {
                            ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
                            c.EliminaImmagine(s.getArtista(), s.getAlbum(), s.getNomeImmagine());
                        }
                    }
                }
            }
        });

        VariabiliStatichePlayer.getInstance().setImgRefreshImmagini(act.findViewById(R.id.imgRefreshImmagini));
        VariabiliStatichePlayer.getInstance().getImgRefreshImmagini().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Artista = VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista();

                ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
                c.RitornaImmaginiArtista(Artista);
            }
        });

        VariabiliStatichePlayer.getInstance().setImgCondividi(act.findViewById(R.id.imgCondividiSfondo));
        VariabiliStatichePlayer.getInstance().getImgCondividi().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!VariabiliStatichePlayer.getInstance().isCeImmaginePerModifica()) {
                    return;
                }

                if (VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica() != null) {
                    StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica();

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(s.getPathImmagine());
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, s.getNomeImmagine());
                    i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i,"Share immagine looWebPlayer"));
                }
            }
        });
    }

    private void impostazioniMascheraBraniOnLine() {
        VariabiliStatichePlayer.getInstance().setLstArtisti(act.findViewById(R.id.lstListaArtisti));
        VariabiliStatichePlayer.getInstance().setLstAlbum(act.findViewById(R.id.lstListaAlbum));
        VariabiliStatichePlayer.getInstance().setLstBrani(act.findViewById(R.id.lstListaBrani));

        EditText edtArtista = act.findViewById(R.id.edtFiltroArtisti);
        ImageView imgRicercaScelta = (ImageView) act.findViewById(R.id.imgFiltroArtisti);
        imgRicercaScelta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Artista = edtArtista.getText().toString();
                if (VariabiliStatichePlayer.getInstance().getCustomAdapterA() != null) {
                    VariabiliStatichePlayer.getInstance().getCustomAdapterA().updateData(Artista);
                }
            }
        });

        ImageView imgRefreshArtisti = (ImageView) act.findViewById(R.id.imgRefreshArtisti);
        imgRefreshArtisti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                ws.RitornaListaArtisti(true);

                VariabiliStatichePlayer.getInstance().getLstAlbum().setAdapter(null);
                VariabiliStatichePlayer.getInstance().getLstBrani().setAdapter(null);
            }
        });

        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
        ws.RitornaListaArtisti(false);
    }

    private void ImpostaImmagine() {
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() == null) {
            return;
        }

        Bitmap bitmapAttesa = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading);
        VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmapAttesa);

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                int n = VariabiliStatichePlayer.getInstance().getIdImmagineImpostata();
                if (n < VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size()) {
                    StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().get(n);
                    VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(s);
                    String path = s.getPathImmagine();
                    path = path.replace("//", "/");
                    Bitmap bitmap = null;
                    VariabiliStatichePlayer.getInstance().setCeImmaginePerModifica(false);
                    boolean visibile = true;
                    if (Files.getInstance().EsisteFile(path)) {
                        bitmap = BitmapFactory.decodeFile(path);
                        VariabiliStatichePlayer.getInstance().setCeImmaginePerModifica(true);
                    } else {
                        if (!UtilitiesGlobali.getInstance().isRetePresente()) {
                            // bitmap = null;
                            visibile = false;
                            VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(null);
                        } else {
                            DownloadImmagine d = new DownloadImmagine();
                            VariabiliStatichePlayer.getInstance().setDownImmagine(d);
                            d.EsegueDownload(
                                    context,
                                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings(),
                                    s.getUrlImmagine()
                            );
                        }
                    }
                /* if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                } */

                    if (visibile) {
                        VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmap);
                    } else {
                        VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setVisibility(LinearLayout.GONE);
                    }

                    VariabiliStatichePlayer.getInstance().getTxtNomeImmaginePerModifica().setText(
                            VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica().getNomeImmagine()
                    );
                    VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("Immagine " + n +
                            "/" + (VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1));
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 50);

    }

    private void visualizzaImpostazioniMaschera(int quale) {
        LinearLayout layBraniLocali = act.findViewById(R.id.layBraniLocali);
        LinearLayout layRicerche = act.findViewById(R.id.layRicerchePlayer);
        LinearLayout laySfondoPlayer = act.findViewById(R.id.laySfondoPlayer);
        LinearLayout layBraniOnLine = act.findViewById(R.id.layBraniOnLine);
        LinearLayout layBranoPlayer = act.findViewById(R.id.layBranoPlayer);

        layBraniLocali.setVisibility(LinearLayout.GONE);
        layRicerche.setVisibility(LinearLayout.GONE);
        laySfondoPlayer.setVisibility(LinearLayout.GONE);
        layBraniOnLine.setVisibility(LinearLayout.GONE);
        layBranoPlayer.setVisibility(LinearLayout.GONE);

        switch(quale) {
            case 0:
                layRicerche.setVisibility(LinearLayout.VISIBLE);
                break;
            case 1:
                layBraniLocali.setVisibility(LinearLayout.VISIBLE);
                break;
            case 2:
                laySfondoPlayer.setVisibility(LinearLayout.VISIBLE);
                break;
            case 3:
                layBranoPlayer.setVisibility(LinearLayout.VISIBLE);
                break;
            case 4:
                layBraniOnLine.setVisibility(LinearLayout.VISIBLE);
                break;
        }
    }
}
