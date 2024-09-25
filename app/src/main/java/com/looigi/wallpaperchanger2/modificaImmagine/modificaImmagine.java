package com.looigi.wallpaperchanger2.modificaImmagine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.canhub.cropper.CropImageView;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class modificaImmagine extends Activity {
    private static String NomeMaschera = "MODIFICAIMMAGINE";
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
    private LinearLayout layColori;
    private ImageView btnCrop;
    private ImageView btnSalvaCrop;
    private ImageView btnSalva;
    private ImageView btnAnnullaCrop;
    private modificaImmagine mI;
    private GestioneImmagini g;
    private int modalita;
    private Bitmap vecchiaBitmap;
    private LinearLayout laySalvataggio;
    private List<Bitmap> undoBitmap;
    private ImageView imgUndo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifica_immagine);

        context = this;
        act = this;
        mI = this;
        modalitaCrop = false;
        undoBitmap = new ArrayList<>();
        g = new GestioneImmagini();

        ImpostaSchermata(this);

        Path = UtilityDetector.getInstance().PrendePath(context);
        String NomeImmagine = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().numMultimedia);
        if (NomeImmagine.toUpperCase().contains(".DBF")) {
            String[] c = NomeImmagine.split("\\.");
            if (c.length > 0) {
                NomeImmagine = c[0];
            }
            UtilityDetector.getInstance().removeKeyFromFile(Path, NomeImmagine + ".dbf", NomeImmagine + ".jpg");
            NomeImmagine += ".jpg";
        }
        NomeImmagineDaSalvare = NomeImmagine;

        bitmap = BitmapFactory.decodeFile(Path + NomeImmagineDaSalvare);
        crop.setImageBitmap(bitmap);
        img.setImageBitmap(bitmap);

        DisegnaUndo();
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

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(Path + NomeImmagineDaSalvare);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            RefreshImmagini();
            impostaCrop();

            UtilityDetector.getInstance().CriptaFiles(context);
        } catch (IOException e) {
        }
    }

    private void RefreshImmagini() {
        int appo = VariabiliStaticheDetector.getInstance().numMultimedia;
        UtilityDetector.getInstance().CaricaMultimedia(context);
        VariabiliStaticheDetector.getInstance().numMultimedia = appo;
        UtilityDetector.getInstance().VisualizzaMultimedia(context);
    }

    private void impostaContrasto() {
        modalita = 1;
        vecchiaBitmap = bitmap;

        btnSalvaCrop.setVisibility(LinearLayout.VISIBLE);
        btnAnnullaCrop.setVisibility(LinearLayout.VISIBLE);
        btnRuotaDes.setVisibility(LinearLayout.GONE);
        btnRuotaSin.setVisibility(LinearLayout.GONE);
        btnFlipX.setVisibility(LinearLayout.GONE);
        btnFlipY.setVisibility(LinearLayout.GONE);
        btnVolto.setVisibility(LinearLayout.GONE);
        btnCrop.setVisibility(LinearLayout.GONE);
        btnSalva.setVisibility(LinearLayout.GONE);
        btnChiude.setVisibility(LinearLayout.GONE);
        imgUndo.setVisibility(LinearLayout.GONE);
    }

    private void toglieContrasto() {
        modalita = 0;

        btnSalvaCrop.setVisibility(LinearLayout.GONE);
        btnAnnullaCrop.setVisibility(LinearLayout.GONE);
        btnRuotaDes.setVisibility(LinearLayout.VISIBLE);
        btnRuotaSin.setVisibility(LinearLayout.VISIBLE);
        btnFlipX.setVisibility(LinearLayout.VISIBLE);
        btnFlipY.setVisibility(LinearLayout.VISIBLE);
        btnVolto.setVisibility(LinearLayout.VISIBLE);
        btnCrop.setVisibility(LinearLayout.VISIBLE);
        btnSalva.setVisibility(LinearLayout.VISIBLE);
        btnChiude.setVisibility(LinearLayout.VISIBLE);
        if (undoBitmap.size() > 1) {
            imgUndo.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void impostaCrop() {
        if (modalitaCrop) {
            crop.setVisibility(LinearLayout.VISIBLE);
            img.setVisibility(LinearLayout.GONE);

            btnSalvaCrop.setVisibility(LinearLayout.VISIBLE);
            btnAnnullaCrop.setVisibility(LinearLayout.VISIBLE);
            btnRuotaDes.setVisibility(LinearLayout.GONE);
            btnRuotaSin.setVisibility(LinearLayout.GONE);
            btnFlipX.setVisibility(LinearLayout.GONE);
            btnFlipY.setVisibility(LinearLayout.GONE);
            btnVolto.setVisibility(LinearLayout.GONE);
            btnCrop.setVisibility(LinearLayout.GONE);
            layColori.setVisibility(LinearLayout.GONE);
            btnSalva.setVisibility(LinearLayout.GONE);
            btnChiude.setVisibility(LinearLayout.GONE);
            imgUndo.setVisibility(LinearLayout.GONE);

            crop.setImageBitmap(bitmap);
        } else {
            crop.setVisibility(LinearLayout.GONE);
            img.setVisibility(LinearLayout.VISIBLE);

            btnSalvaCrop.setVisibility(LinearLayout.GONE);
            btnAnnullaCrop.setVisibility(LinearLayout.GONE);
            btnRuotaDes.setVisibility(LinearLayout.VISIBLE);
            btnRuotaSin.setVisibility(LinearLayout.VISIBLE);
            btnFlipX.setVisibility(LinearLayout.VISIBLE);
            btnFlipY.setVisibility(LinearLayout.VISIBLE);
            btnVolto.setVisibility(LinearLayout.VISIBLE);
            btnCrop.setVisibility(LinearLayout.VISIBLE);
            layColori.setVisibility(LinearLayout.VISIBLE);
            btnSalva.setVisibility(LinearLayout.VISIBLE);
            btnChiude.setVisibility(LinearLayout.VISIBLE);
            if (undoBitmap.size() > 1) {
                imgUndo.setVisibility(LinearLayout.VISIBLE);
            }

            img.setImageBitmap(bitmap);
        }
    }

    private void AggiornaBitmap() {
        crop.setImageBitmap(bitmap);
        img.setImageBitmap(bitmap);

        DisegnaUndo();
    }

    public void ImpostaBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        AggiornaBitmap();
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
        layColori = act.findViewById(R.id.layColori);
        btnCrop = act.findViewById(R.id.imgCrop);
        btnSalvaCrop = (ImageView) act.findViewById(R.id.imgSalvaCrop);
        btnSalva = (ImageView) act.findViewById(R.id.imgSalva);
        btnChiude = (ImageView) act.findViewById(R.id.imgChiude);
        btnAnnullaCrop = (ImageView) act.findViewById(R.id.imgAnnullaCrop);
        laySalvataggio = (LinearLayout) act.findViewById(R.id.laySalvataggio);
        laySalvataggio.setVisibility(LinearLayout.GONE);
        imgUndo = act.findViewById(R.id.imgUndo);
        imgUndo.setVisibility(LinearLayout.GONE);
        Button cmdSovrascrive = (Button) act.findViewById(R.id.cmdSovrascrivi);
        Button cmdRinomina = (Button) act.findViewById(R.id.cmdRinomina);

        impostaCrop();

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

                    crop.setImageBitmap(bitmap);
                    img.setImageBitmap(bitmap);

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

                if (modalita != 1) {
                    bitmap = crop.getCroppedImage();
                    modalitaCrop = false;

                    impostaCrop();
                } else {
                    toglieContrasto();
                }
            }
        });

        btnRuotaSin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.RuotaImmagine(context,bitmap,270);

                AggiornaBitmap();
            }
        });

        btnRuotaDes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.RuotaImmagine(context, bitmap,90);

                AggiornaBitmap();
            }
        });

        btnAnnullaCrop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (modalita != 1) {
                    modalitaCrop = false;

                    impostaCrop();
                } else {
                    bitmap = vecchiaBitmap;
                    toglieContrasto();
                }
            }
        });

        btnFlipX.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.FlipImmagine(context, bitmap,true);

                AggiornaBitmap();
            }
        });

        btnFlipY.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DisegnaUndo();

                bitmap = g.FlipImmagine(context, bitmap,false);

                AggiornaBitmap();
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
                if (modalita != 1) {
                    impostaContrasto();
                }

                Contrasto = progress;

                bitmap = g.CambiaContrastoLuminosita(context, vecchiaBitmap, Contrasto, Luminosita);

                AggiornaBitmap();
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
                if (modalita != 1) {
                    impostaContrasto();
                }

                Luminosita = progress;

                bitmap = g.CambiaContrastoLuminosita(context, vecchiaBitmap, Contrasto, Luminosita);
                AggiornaBitmap();
            }
        });
    }
}
