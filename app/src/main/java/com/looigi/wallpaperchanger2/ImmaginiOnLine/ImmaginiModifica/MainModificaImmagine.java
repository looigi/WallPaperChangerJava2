package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiModifica;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.canhub.cropper.CropImageView;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Detector.UtilityDetector;
import com.looigi.wallpaperchanger2.Detector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.Fetekkie.UtilityFetekkie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.Pennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.Player.UtilityPlayer;
import com.looigi.wallpaperchanger2.Wallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainModificaImmagine extends Activity {
    private static String NomeMaschera = "Modifica_Immagine";
    private Context context;
    private Activity act;
    private CropImageView crop;
    private ImageView img;
    private String NomeImmagineDaSalvare;
    private String Path;
    private int Contrasto;
    private int Luminosita;
    private boolean modalitaCrop = false;
    private Bitmap bitmap;
    private ImageView btnRuotaSin;
    private ImageView btnRuotaDes;
    private ImageView btnFlipX;
    private ImageView btnFlipY;
    private ImageView btnChiude;
    private ImageView btnVolto;
    private ImageView btnSharpen;
    // private LinearLayout layColori;
    private ImageView btnCrop;
    private ImageView btnSalvaCrop;
    private ImageView btnSalva;
    private ImageView btnAnnullaCrop;
    private MainModificaImmagine mI;
    private GestioneImmagini g;
    // private int modalita;
    private Bitmap vecchiaBitmap;
    private LinearLayout laySalvataggio;
    private List<Bitmap> undoBitmap;
    private ImageView imgUndo;
    private int Resize = 100;
    private int Angolo = 0;
    private TextView txtDimensioni;
    private TextView txtInformazioni;
    private ImageView imgColori;
    private ImageView imgResize;
    private ImageView imgRuota;
    private LinearLayout layRuota;
    private LinearLayout layResize;
    private LinearLayout layColori;
    private SeekBar seekDimensioni;
    private SeekBar seekAngolo;
    private boolean nonFareRuota = false;
    private boolean nonResizare = false;
    private Button cmdRinomina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_modifica_immagine);

        context = this;
        act = this;
        mI = this;
        modalitaCrop = false;
        undoBitmap = new ArrayList<>();
        g = new GestioneImmagini();
        Resize = 100;
        Angolo = 0;
        nonFareRuota = false;

        ImpostaSchermata(this);

        cmdRinomina.setVisibility(LinearLayout.VISIBLE);
        switch(VariabiliStaticheModificaImmagine.getInstance().getMascheraApertura()) {
            case "DETECTOR":
                break;
            case "IMMAGINI":
                break;
            case "PENNETTA":
                break;
            case "FETEKKIE":
                break;
            case "PLAYER":
                cmdRinomina.setVisibility(LinearLayout.GONE);
                break;
            case "WALLPAPER1":
                cmdRinomina.setVisibility(LinearLayout.GONE);
                break;
            case "WALLPAPER2":
                cmdRinomina.setVisibility(LinearLayout.GONE);
                break;
        }

        Path = UtilityDetector.getInstance().PrendePath(context);
        String NomeImmagine = VariabiliStaticheModificaImmagine.getInstance().getNomeImmagine();
        if (NomeImmagine.toUpperCase().contains(".DBF")) {
            String[] c = NomeImmagine.split("\\.");
            if (c.length > 0) {
                NomeImmagine = c[0];
            }
            UtilityDetector.getInstance().removeKeyFromFile(Path, NomeImmagine + ".dbf", NomeImmagine + ".jpg");
            NomeImmagine += ".jpg";
        }
        NomeImmagineDaSalvare = NomeImmagine;

        String NomeFinale = "";
        if (VariabiliStaticheModificaImmagine.getInstance().getMascheraApertura().equals("DETECTOR")) {
            NomeFinale = Path + NomeImmagineDaSalvare;
        } else {
            NomeFinale = NomeImmagineDaSalvare;
        }
        bitmap = BitmapFactory.decodeFile(NomeFinale);

        AggiornaBitmap(bitmap);
    }

    public void EsegueSalvataggio(boolean Sovrascrive) {
        if (!Sovrascrive) {
            String[] c = NomeImmagineDaSalvare.split("\\.");
            if (c.length > 0) {
                Calendar calendar = Calendar.getInstance();
                int s = calendar.get(Calendar.SECOND);
                NomeImmagineDaSalvare = c[0] + "_" + s + "." + c[1];
            }
        }

        String NomeFinale = "";
        if(VariabiliStaticheModificaImmagine.getInstance().getMascheraApertura().equals("DETECTOR")) {
            NomeFinale = Path + NomeImmagineDaSalvare;
        } else {
            NomeFinale = NomeImmagineDaSalvare;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(NomeFinale);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            if (VariabiliStaticheModificaImmagine.getInstance().getMascheraApertura().equals("DETECTOR")) {
                RefreshImmagini();
                impostaCrop();

                UtilityDetector.getInstance().CriptaFiles(context);
            }

            switch(VariabiliStaticheModificaImmagine.getInstance().getMascheraApertura()) {
                case "DETECTOR":
                    UtilitiesGlobali.getInstance().ApreToast(context, "Immagine salvata");
                    break;
                case "IMMAGINI":
                    // Salvataggio immagine da maschera immagini
                    UtilityImmagini.getInstance().SalvataggioImmagine(context, Sovrascrive);
                    break;
                case "PREVIEW":
                    // Salvataggio immagine da maschera preview
                    UtilityImmagini.getInstance().SalvataggioImmagine(context, Sovrascrive);
                    break;
                case "PENNETTA":
                    // Salvataggio immagine da maschera immagini
                    UtilityPennetta.getInstance().SalvataggioImmagine(context, Sovrascrive);
                    break;
                case "FETEKKIE":
                    // Salvataggio immagine da maschera immagini
                    UtilityFetekkie.getInstance().SalvataggioImmagine(context, Sovrascrive);
                    break;
                case "PLAYER":
                    // Salvataggio immagine da maschera player
                    UtilityPlayer.getInstance().SalvataggioImmagine(context, Sovrascrive);
                    break;
                case "WALLPAPER":
                    UtilityWallpaper.getInstance().SalvataggioImmagine(context, Sovrascrive);
                    break;
            }
        } catch (IOException ignored) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Errore nel salvataggio");
        }
    }

    private void RefreshImmagini() {
        switch (VariabiliStaticheModificaImmagine.getInstance().getMascheraApertura()) {
            case "DETECTOR":
                int appo = VariabiliStaticheDetector.getInstance().getNumMultimedia();
                UtilityDetector.getInstance().CaricaMultimedia(context);
                VariabiliStaticheDetector.getInstance().setNumMultimedia(appo);
                UtilityDetector.getInstance().VisualizzaMultimedia(context);
                break;
            case "IMMAGINI":
                break;
            case "PENNETTA":
                break;
            case "PLAYER":
                break;
            case "WALLPAPER1":
                break;
            case "WALLPAPER2":
                break;
        }
    }

    private void impostaCrop() {
        Resize = 100;
        Angolo = 0;
        seekDimensioni.setProgress(Resize);
        seekAngolo.setProgress(Angolo);

        if (modalitaCrop) {
            crop.setImageBitmap(bitmap);

            crop.setVisibility(LinearLayout.VISIBLE);
            img.setVisibility(LinearLayout.GONE);

            btnSalvaCrop.setVisibility(LinearLayout.VISIBLE);
            btnAnnullaCrop.setVisibility(LinearLayout.VISIBLE);
            btnFlipX.setVisibility(LinearLayout.GONE);
            btnFlipY.setVisibility(LinearLayout.GONE);
            btnVolto.setVisibility(LinearLayout.GONE);
            btnCrop.setVisibility(LinearLayout.GONE);
            btnSharpen.setVisibility(LinearLayout.GONE);
            // layColori.setVisibility(LinearLayout.GONE);
            btnSalva.setVisibility(LinearLayout.GONE);
            btnChiude.setVisibility(LinearLayout.GONE);
            imgUndo.setVisibility(LinearLayout.GONE);
            imgColori.setVisibility(LinearLayout.GONE);
            imgResize.setVisibility(LinearLayout.GONE);
            imgRuota.setVisibility(LinearLayout.GONE);
            layColori.setVisibility(LinearLayout.GONE);
            layResize.setVisibility(LinearLayout.GONE);
            layRuota.setVisibility(LinearLayout.GONE);
            if (vecchiaBitmap != null) {
                bitmap = vecchiaBitmap;
                vecchiaBitmap = null;
            }
        } else {
            crop.setImageBitmap(null);

            crop.setVisibility(LinearLayout.GONE);
            img.setVisibility(LinearLayout.VISIBLE);

            btnSalvaCrop.setVisibility(LinearLayout.GONE);
            btnAnnullaCrop.setVisibility(LinearLayout.GONE);
            btnFlipX.setVisibility(LinearLayout.VISIBLE);
            btnFlipY.setVisibility(LinearLayout.VISIBLE);
            btnVolto.setVisibility(LinearLayout.VISIBLE);
            btnCrop.setVisibility(LinearLayout.VISIBLE);
            btnSharpen.setVisibility(LinearLayout.VISIBLE);
            // layColori.setVisibility(LinearLayout.VISIBLE);
            btnSalva.setVisibility(LinearLayout.VISIBLE);
            btnChiude.setVisibility(LinearLayout.VISIBLE);
            if (undoBitmap.size() > 1) {
                imgUndo.setVisibility(LinearLayout.VISIBLE);
            }
            imgColori.setVisibility(LinearLayout.VISIBLE);
            imgResize.setVisibility(LinearLayout.VISIBLE);
            imgRuota.setVisibility(LinearLayout.VISIBLE);
            layColori.setVisibility(LinearLayout.GONE);
            layResize.setVisibility(LinearLayout.GONE);
            layRuota.setVisibility(LinearLayout.GONE);
        }

        AggiornaBitmap(bitmap);
    }

    private void AggiornaBitmap(Bitmap bitmap) {
        // crop.setImageBitmap(bitmap);
        img.setImageBitmap(bitmap);

        if (bitmap != null) {
            txtDimensioni.setText(Resize + "%: " + bitmap.getWidth() + "x" + bitmap.getHeight());

            txtInformazioni.setText(
                    NomeImmagineDaSalvare + "\n" +
                    bitmap.getWidth() + "x" + bitmap.getHeight()
            );
        } else {
            txtDimensioni.setText("");
            txtInformazioni.setText("");
        }

        DisegnaUndo();
    }

    public void ImpostaBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        AggiornaBitmap(bitmap);
    }

    private void DisegnaUndo() {
        undoBitmap.add(bitmap);
        if (undoBitmap.size() > 1) {
            imgUndo.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void ImpostaSchermata(Activity act) {
        crop = act.findViewById(R.id.cropImgViewModifica);
        img = act.findViewById(R.id.imgViewModifica);

        btnRuotaSin = act.findViewById(R.id.imgRuotaSin);
        btnRuotaDes = act.findViewById(R.id.imgRuotaDes);
        btnFlipX = act.findViewById(R.id.imgFlipX);
        btnFlipY = act.findViewById(R.id.imgFlipY);
        btnVolto = act.findViewById(R.id.imgVolto);
        // layColori = act.findViewById(R.id.layColori);
        btnCrop = act.findViewById(R.id.imgCrop);
        btnSalvaCrop = (ImageView) act.findViewById(R.id.imgSalvaCrop);
        btnSalva = (ImageView) act.findViewById(R.id.imgSalva);
        btnChiude = (ImageView) act.findViewById(R.id.imgChiude);
        btnSharpen = (ImageView) act.findViewById(R.id.imgSharpen);
        btnAnnullaCrop = (ImageView) act.findViewById(R.id.imgAnnullaCrop);
        laySalvataggio = (LinearLayout) act.findViewById(R.id.laySalvataggio);
        laySalvataggio.setVisibility(LinearLayout.GONE);
        imgUndo = act.findViewById(R.id.imgUndo);
        imgUndo.setVisibility(LinearLayout.GONE);
        Button cmdSovrascrive = (Button) act.findViewById(R.id.cmdSovrascrivi);
        cmdRinomina = (Button) act.findViewById(R.id.cmdRinomina);
        layColori = act.findViewById(R.id.layColori);
        layColori.setVisibility(LinearLayout.GONE);
        ImageView imgAnnullaColori = act.findViewById(R.id.imgAnnullaColori);
        layResize = act.findViewById(R.id.layResize);
        layResize.setVisibility(LinearLayout.GONE);
        layRuota = act.findViewById(R.id.layRuota);
        layRuota.setVisibility(LinearLayout.GONE);
        imgResize = act.findViewById(R.id.imgResize);
        imgRuota = act.findViewById(R.id.imgRuota);
        ImageView imgAnnullaRuota = act.findViewById(R.id.imgAnnullaRuota);
        ImageView imgAnnullaResize = act.findViewById(R.id.imgAnnullaResize);
        ImageView imgColori2 = act.findViewById(R.id.imgColori2);
        ImageView imgResize2 = act.findViewById(R.id.imgResize2);
        ImageView imgRuota2 = act.findViewById(R.id.imgRuota2);
        ImageView imgEsegueRotazione = act.findViewById(R.id.imgEsegueRotazione);
        imgColori = act.findViewById(R.id.imgColori);
        seekDimensioni = act.findViewById(R.id.seekBarDimensioni);
        seekDimensioni.setMin(0);
        seekDimensioni.setMax(500);
        seekDimensioni.setProgress(Resize);
        txtDimensioni = act.findViewById(R.id.txtDimensioni);
        seekAngolo = act.findViewById(R.id.seekBarAngolo);
        seekAngolo.setMin(0);
        seekAngolo.setMax(360);
        seekAngolo.setProgress(Angolo);
        EditText txtAngolo = act.findViewById(R.id.txtAngolo);
        txtAngolo.setText(Integer.toString(Angolo));
        txtInformazioni = act.findViewById(R.id.txtImmagineDati);
        VariabiliStaticheModificaImmagine.getInstance().setImgAttendere(act.findViewById(R.id.imgAttendere));
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(false);

        impostaCrop();

        imgAnnullaColori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (vecchiaBitmap != null) {
                    bitmap = vecchiaBitmap;
                    vecchiaBitmap = null;
                }

                AggiornaBitmap(bitmap);

                layColori.setVisibility(LinearLayout.GONE);
            }
        });

        imgAnnullaResize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (vecchiaBitmap != null) {
                    bitmap = vecchiaBitmap;
                    vecchiaBitmap = null;
                }

                AggiornaBitmap(bitmap);

                layResize.setVisibility(LinearLayout.GONE);
            }
        });

        imgAnnullaRuota.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (vecchiaBitmap != null) {
                    bitmap = vecchiaBitmap;
                    vecchiaBitmap = null;
                }
                nonFareRuota = false;

                AggiornaBitmap(bitmap);

                layRuota.setVisibility(LinearLayout.GONE);
            }
        });

        imgColori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (vecchiaBitmap != null) {
                    bitmap = vecchiaBitmap;
                    vecchiaBitmap = null;

                    AggiornaBitmap(bitmap);
                }
                vecchiaBitmap = bitmap;

                layColori.setVisibility(LinearLayout.VISIBLE);
                layResize.setVisibility(LinearLayout.GONE);
                layRuota.setVisibility(LinearLayout.GONE);
            }
        });

        imgResize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (vecchiaBitmap != null) {
                    bitmap = vecchiaBitmap;
                    vecchiaBitmap = null;

                    AggiornaBitmap(bitmap);
                }
                vecchiaBitmap = bitmap;

                layResize.setVisibility(LinearLayout.VISIBLE);
                layColori.setVisibility(LinearLayout.GONE);
                layRuota.setVisibility(LinearLayout.GONE);
            }
        });

        imgRuota.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nonFareRuota = false;

                if (vecchiaBitmap != null) {
                    bitmap = vecchiaBitmap;
                    vecchiaBitmap = null;

                    AggiornaBitmap(bitmap);
                }
                vecchiaBitmap = bitmap;

                layRuota.setVisibility(LinearLayout.VISIBLE);
                layResize.setVisibility(LinearLayout.GONE);
                layColori.setVisibility(LinearLayout.GONE);
            }
        });

        imgColori2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vecchiaBitmap = null;

                AggiornaBitmap(bitmap);

                layColori.setVisibility(LinearLayout.GONE);
            }
        });

        imgResize2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nonResizare = true;
                Resize = 100;
                seekDimensioni.setProgress(Resize);
                vecchiaBitmap = null;
                nonResizare = false;

                AggiornaBitmap(bitmap);

                layResize.setVisibility(LinearLayout.GONE);
            }
        });

        imgRuota2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nonFareRuota = true;

                AggiornaBitmap(bitmap);

                Angolo = 0;
                seekAngolo.setProgress(Angolo);
                vecchiaBitmap = null;

                layRuota.setVisibility(LinearLayout.GONE);
            }
        });

        cmdSovrascrive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EsegueSalvataggio(true);

                laySalvataggio.setVisibility(LinearLayout.GONE);
            }
        });

        imgUndo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int quale = undoBitmap.size() - 2;
                if (quale >= 0) {
                    bitmap = undoBitmap.get(quale);

                    AggiornaBitmap(bitmap);

                    undoBitmap.remove(undoBitmap.size() - 1);
                    if (undoBitmap.size() <= 1) {
                        imgUndo.setVisibility(LinearLayout.GONE);
                    }
                } else {
                    imgUndo.setVisibility(LinearLayout.GONE);
                }
            }
        });

        cmdRinomina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EsegueSalvataggio(false);

                laySalvataggio.setVisibility(LinearLayout.GONE);
            }
        });

        btnSalva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySalvataggio.setVisibility(LinearLayout.VISIBLE);
            }
        });

        btnChiude.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (VariabiliStaticheModificaImmagine.getInstance().getMascheraApertura()) {
                    case "DETECTOR":
                        VariabiliStaticheDetector.getInstance().setRiaperturaSenzaReimpostazione(true);
                        break;
                    case "WALLPAPER1":
                        VariabiliStaticheWallpaper.getInstance().setApreRicerca(true);
                        break;
                    case "WALLPAPER2":
                        VariabiliStaticheWallpaper.getInstance().setApreRicerca(true);
                        break;
                }

                bitmap = null;
                crop = null;
                img = null;

                act.finish();
            }
        });

        btnCrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modalitaCrop = true;

                impostaCrop();
            }
        });

        btnVolto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                g.PrendeVoltoDaBitmap(context, bitmap, mI);
            }
        });

        btnSalvaCrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                // if (modalita != 1) {
                    bitmap = crop.getCroppedImage();
                    modalitaCrop = false;

                    impostaCrop();
                /* } else {
                    toglieContrasto();
                } */
            }
        });

        btnRuotaSin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.RuotaImmagine(context,bitmap,270);

                AggiornaBitmap(bitmap);
            }
        });

        btnRuotaDes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.RuotaImmagine(context, bitmap,90);

                AggiornaBitmap(bitmap);
            }
        });

        btnAnnullaCrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if (modalita != 1) {
                    modalitaCrop = false;

                    impostaCrop();
                /* } else {
                    bitmap = vecchiaBitmap;
                    toglieContrasto();
                } */
            }
        });

        btnFlipX.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.FlipImmagine(bitmap,true);

                AggiornaBitmap(bitmap);
            }
        });

        btnSharpen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.doSharpen(context, bitmap);

                AggiornaBitmap(bitmap);
            }
        });

        btnFlipY.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.FlipImmagine(bitmap,false);

                AggiornaBitmap(bitmap);
            }
        });

        Contrasto = 1;
        Luminosita = 0;

        SeekBar seekbarcontrasto=(SeekBar) act.findViewById(R.id.seekBarCont);
        seekbarcontrasto.setMin(0);
        seekbarcontrasto.setMax(10);
        seekbarcontrasto.setProgress(Contrasto);
        seekbarcontrasto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                /* if (modalita != 1) {
                    impostaContrasto();
                } */

                Contrasto = progress;

                Bitmap b = g.CambiaContrastoLuminosita(vecchiaBitmap, Contrasto, Luminosita);
                if (b != null) {
                    bitmap = b;

                    AggiornaBitmap(bitmap);
                }
            }
        });

        SeekBar seekbarluminosita=(SeekBar) act.findViewById(R.id.seekBarLum);
        seekbarluminosita.setMin(-255);
        seekbarluminosita.setMax(255);
        seekbarluminosita.setProgress(Luminosita);
        seekbarluminosita.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                /* if (modalita != 1) {
                    impostaContrasto();
                } */

                Luminosita = progress;

                bitmap = g.CambiaContrastoLuminosita(vecchiaBitmap, Contrasto, Luminosita);

                AggiornaBitmap(bitmap);
            }
        });

        seekDimensioni.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                if (!nonResizare) {
                    Resize = progress;

                    int vecchioX = vecchiaBitmap.getWidth();
                    int vecchioY = vecchiaBitmap.getHeight();
                    int nuovoX = Math.round((float) (vecchioX * Resize) / 100);
                    int nuovoY = Math.round((float) (vecchioY * Resize) / 100);

                    txtDimensioni.setText(Resize + "%: " + nuovoX + "x" + nuovoY);

                    bitmap = g.Resize(vecchiaBitmap, nuovoX, nuovoY);
                    // AggiornaBitmap(bitmap);
                }
            }
        });

        imgEsegueRotazione.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Angolo = Integer.parseInt(txtAngolo.getText().toString());

                txtAngolo.setText(Integer.toString(Angolo));

                bitmap = g.RuotaImmagine(context, vecchiaBitmap, Angolo);

                AggiornaBitmap(bitmap);
            }
        });

        seekAngolo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                if (!nonFareRuota) {
                    Angolo = progress;

                    txtAngolo.setText(Integer.toString(Angolo));

                    bitmap = g.RuotaImmagine(context, vecchiaBitmap, Angolo);

                    AggiornaBitmap(bitmap);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return false;
        }

        return false;
    }
}
