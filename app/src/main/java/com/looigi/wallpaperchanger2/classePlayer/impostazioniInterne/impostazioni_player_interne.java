package com.looigi.wallpaperchanger2.classePlayer.impostazioniInterne;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classePlayer.AdapterListenerBrani;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classePlayer.scan.ScanBraniNonPresentiSuDB;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.io.IOException;
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
            }
        });

        Button imgBranilocali = (Button) act.findViewById(R.id.btnSettingsBraniLocali);
        imgBranilocali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                visualizzaImpostazioniMaschera(1);

                imposta_brani_locali();
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
        ImageView imgImposta = act.findViewById(R.id.imgImpostaWP);
        imgImposta.setOnClickListener(new View.OnClickListener() {
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

        ImageView imgElimina = act.findViewById(R.id.imgEliminaSfondo);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String path = VariabiliStatichePlayer.getInstance().getPathUltimaImmagine();
                if (path != null) {
                    StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getImmagineImpostata();

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

                    UtilityPlayer.getInstance().ImpostaImmagine(context);
                }
            }
        });

        ImageView imgRefreshImmagini = act.findViewById(R.id.imgRefreshImmagini);
        imgRefreshImmagini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Artista = VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista();

                ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
                c.RitornaImmaginiArtista(Artista);
            }
        });

        ImageView imgCondividi = act.findViewById(R.id.imgCondividiSfondo);
        imgCondividi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePlayer.getInstance().getPathUltimaImmagine() != null) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(VariabiliStatichePlayer.getInstance().getPathUltimaImmagine());
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT,VariabiliStatichePlayer.getInstance().getImmagineImpostata().getNomeImmagine());
                    i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i,"Share immagine looWebPlayer"));
                }
            }
        });
    }

    private void visualizzaImpostazioniMaschera(int quale) {
        LinearLayout layBraniLocali = act.findViewById(R.id.layBraniLocali);
        LinearLayout layRicerche = act.findViewById(R.id.layRicerchePlayer);
        LinearLayout laySfondoPlayer = act.findViewById(R.id.laySfondoPlayer);

        layBraniLocali.setVisibility(LinearLayout.GONE);
        layRicerche.setVisibility(LinearLayout.GONE);
        laySfondoPlayer.setVisibility(LinearLayout.GONE);

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
        }
    }
}
