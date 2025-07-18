package com.looigi.wallpaperchanger2.classeVideo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeFilms.UtilityFilms;
import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.classeFilms.db_dati_films;
import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;
import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;
import com.looigi.wallpaperchanger2.utilities.OnSwipeTouchListener;
import com.looigi.wallpaperchanger2.utilities.OnSwipeTouchListenerVideo;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class MainMostraVideo extends Activity {
    private static String NomeMaschera = "Main_Mostra_Video";
    private Context context;
    private Activity act;
    private boolean SettaggiAperti = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);

        context = this;
        act = this;

        VariabiliStaticheVideo.getInstance().setContext(context);
        VariabiliStaticheVideo.getInstance().setAct(act);

        SettaggiAperti = VariabiliStaticheVideo.getInstance().isSettingsAperto();
        ImageView imgLinguetta = findViewById(R.id.imgLinguettaVideo);
        LinearLayout laySettaggi = findViewById(R.id.laySettaggiVideo);
        if (SettaggiAperti) {
            laySettaggi.setLayoutParams(
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
        } else {
            laySettaggi.setLayoutParams(
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            1));
        }

        imgLinguetta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettaggiAperti = !SettaggiAperti;
                VariabiliStaticheVideo.getInstance().setSettingsAperto(SettaggiAperti);
                db_dati_video db = new db_dati_video(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();

                if (!SettaggiAperti) {
                    laySettaggi.setLayoutParams(
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    1));
                } else {
                    laySettaggi.setLayoutParams(
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        });

        VariabiliStaticheVideo.getInstance().setTxtAvanzamento(findViewById(R.id.txtAvanzamentoThumbs));
        VariabiliStaticheVideo.getInstance().getTxtAvanzamento().setVisibility(LinearLayout.GONE);

        ImageView imgRefreshCat = findViewById(R.id.imgRefreshCategorieVideo);
        imgRefreshCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSV c = new ChiamateWSV(context);
                c.RitornaCategorie(true, "VIDEO");
            }
        });

        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettingsVideo);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(VariabiliStaticheVideo.getInstance().getContext(), MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "VIDEO");
                                iP.putExtras(b);
                                VariabiliStaticheVideo.getInstance().getContext().startActivity(iP);
                            }
                        }, 500);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });

        VariabiliStaticheVideo.getInstance().setVideoView(findViewById(R.id.videoView));
        VariabiliStaticheVideo.getInstance().setPbLoading(findViewById(R.id.pbVideoLoading));
        VariabiliStaticheVideo.getInstance().setTxtId(findViewById(R.id.txtIdVideo));
        VariabiliStaticheVideo.getInstance().setTxtCate(findViewById(R.id.txtCategoriaVideo));
        VariabiliStaticheVideo.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloVideo));
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
        VariabiliStaticheVideo.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));

        EditText txtFiltro = findViewById(R.id.edtFiltroVideo);
        txtFiltro.setText(VariabiliStaticheVideo.getInstance().getFiltro());
        txtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    db_dati_video db = new db_dati_video(context);
                    db.ScriveImpostazioni();

                    VariabiliStaticheVideo.getInstance().setEntratoNelCampoDiTesto(false);
                } else {
                    VariabiliStaticheVideo.getInstance().setEntratoNelCampoDiTesto(true);
                }
            }
        });

        EditText txtFiltroCate = findViewById(R.id.edtFiltroCategoriaVideo);
        txtFiltroCate.setText(VariabiliStaticheVideo.getInstance().getFiltroCategoria());
        txtFiltroCate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (!hasFocus) {
                    VariabiliStaticheVideo.getInstance().setFiltroCategoria(txtFiltroCate.getText().toString());

                    db_dati_video db = new db_dati_video(context);
                    db.ScriveImpostazioni();

                    UtilityVideo.getInstance().AggiornaCategorie(context);
                // }
            }
        });

        ImageView imgCerca = findViewById(R.id.imgCerca);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filtro = txtFiltro.getText().toString();
                VariabiliStaticheVideo.getInstance().setFiltro(filtro);

                ChiamateWSV ws = new ChiamateWSV(context);
                ws.RitornaProssimoVideo("VIDEO");
            }
        });

        ImageView imgElimina = findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheVideo.getInstance().getIdUltimoVideo());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare il Video ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSV c = new ChiamateWSV(context);
                        c.EliminaVideo(id);
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

        ImageView imgScreenShot = findViewById(R.id.imgScreenshot);
        imgScreenShot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheVideo.getInstance().getIdUltimoVideo());

                db_dati_video db = new db_dati_video(context);
                boolean fatto = db.VedeSnapshotS(id);
                db.ChiudeDB();

                if (fatto) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Immagine già acquisita per questo video.\nSi vuole procedere di nuovo alla cattura ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilityVideo.getInstance().takeScreenshot(context, id);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else {
                    UtilityVideo.getInstance().takeScreenshot(context, id);
                }
            }
        });

        VariabiliStaticheVideo.getInstance().setTxtAvanzamento(findViewById(R.id.txtAvanzamento));
        VariabiliStaticheVideo.getInstance().setTxtMaxSeek(findViewById(R.id.txtMax));
        VariabiliStaticheVideo.getInstance().setTxtTitoloSeek(findViewById(R.id.txtTitolo));
        VariabiliStaticheVideo.getInstance().getTxtTitoloSeek().setText("");
        VariabiliStaticheVideo.getInstance().setLayBarraTasti(findViewById(R.id.layBarraTasti));
        VariabiliStaticheVideo.getInstance().getLayBarraTasti().setAlpha(1f);
        VariabiliStaticheVideo.getInstance().setSecondiAlpha(0);
        VariabiliStaticheVideo.getInstance().setBarraOscurata(false);
        VariabiliStaticheVideo.getInstance().getLayBarraTasti().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheVideo.getInstance().getLayBarraTasti().setAlpha(1f);
                VariabiliStaticheVideo.getInstance().setSecondiAlpha(0);
                VariabiliStaticheVideo.getInstance().setBarraOscurata(false);
            }
        });

        ImageView imgAvanti = findViewById(R.id.imgAvanti);
        imgAvanti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheVideo.getInstance().isBarraOscurata()) {
                    VariabiliStaticheVideo.getInstance().getLayBarraTasti().setAlpha(1f);
                    VariabiliStaticheVideo.getInstance().setSecondiAlpha(0);
                    VariabiliStaticheVideo.getInstance().setBarraOscurata(false);
                }

                String filtro = txtFiltro.getText().toString();
                VariabiliStaticheVideo.getInstance().setFiltro(filtro);

                ChiamateWSV ws = new ChiamateWSV(context);
                ws.RitornaProssimoVideo("VIDEO");
            }
        });

        VariabiliStaticheVideo.getInstance().setStaVedendo(true);
        VariabiliStaticheVideo.getInstance().setBarraOscurata(false);

        ImageView imgPlayPause = findViewById(R.id.imgPlayPause);
        if (VariabiliStaticheVideo.getInstance().isStaVedendo()) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_pausa);
            imgPlayPause.setImageBitmap(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_suona);
            imgPlayPause.setImageBitmap(bitmap);
        }
        imgPlayPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheVideo.getInstance().getVideoView() != null) {
                    VariabiliStaticheVideo.getInstance().getLayBarraTasti().setAlpha(1f);
                    VariabiliStaticheVideo.getInstance().setSecondiAlpha(0);
                    VariabiliStaticheVideo.getInstance().setBarraOscurata(false);

                    if (VariabiliStaticheVideo.getInstance().isStaVedendo()) {
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_suona);
                        imgPlayPause.setImageBitmap(bitmap);

                        VariabiliStaticheVideo.getInstance().setStaVedendo(false);
                        VariabiliStaticheVideo.getInstance().getVideoView().pause();
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_pausa);
                        imgPlayPause.setImageBitmap(bitmap);

                        VariabiliStaticheVideo.getInstance().setStaVedendo(true);
                        VariabiliStaticheVideo.getInstance().getVideoView().start();
                    }
                }
            }
        });

        /* ImageView imgRicarica = findViewById(R.id.imgRicarica);
        imgRicarica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        }); */

        VariabiliStaticheVideo.getInstance().setSeekScorri(findViewById(R.id.imgScorri));
        VariabiliStaticheVideo.getInstance().getSeekScorri().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean userTouch = false;

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTouch = true;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (userTouch) {
                    VariabiliStaticheVideo.getInstance().getVideoView().seekTo(progress);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userTouch = false;
                VariabiliStaticheVideo.getInstance().getVideoView().seekTo(seekBar.getProgress());
            }
        });
        VariabiliStaticheVideo.getInstance().setSeekScorri2(findViewById(R.id.imgScorri2));
        VariabiliStaticheVideo.getInstance().getSeekScorri2().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean userTouch = false;

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTouch = true;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (userTouch) {
                    if (VariabiliStaticheVideo.getInstance().isBarraOscurata()) {
                        VariabiliStaticheVideo.getInstance().getLayBarraTasti().setAlpha(1f);
                        VariabiliStaticheVideo.getInstance().setSecondiAlpha(0);
                        VariabiliStaticheVideo.getInstance().setBarraOscurata(false);
                    }

                    VariabiliStaticheVideo.getInstance().getVideoView().seekTo(progress);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userTouch = false;
                VariabiliStaticheVideo.getInstance().getVideoView().seekTo(seekBar.getProgress());
            }
        });

        ImageView imgScreenShotM = findViewById(R.id.imgScreenshotMultipliV);
        imgScreenShotM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheVideo.getInstance().getIdUltimoVideo());

                db_dati_video db = new db_dati_video(context);
                boolean fatto = db.VedeSnapshot(id);
                db.ChiudeDB();

                if (fatto) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Video già scansionato.\nSi vuole procedere di nuovo alla cattura ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilityVideo.getInstance().takeScreenShotMultipli(context, id);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else {
                    UtilityVideo.getInstance().takeScreenShotMultipli(context, id);
                }
            }
        });

        final boolean[] primoIngresso = {true};
        VariabiliStaticheVideo.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    VariabiliStaticheVideo.getInstance().setCategoria("");
                } else {
                    VariabiliStaticheVideo.getInstance().setCategoria(Categoria);
                }

                // ChiamateWSV ws = new ChiamateWSV(context);
                // ws.RitornaProssimoVideo();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        db_dati_video db = new db_dati_video(context);
        String url = db.CaricaVideo();
        db.ChiudeDB();

        if (!url.isEmpty()) {
            VariabiliStaticheVideo.getInstance().ScriveImmagini(url);
        }

        ImpostaSpostamento(act);

        ChiamateWSV ws = new ChiamateWSV(context);
        ws.RitornaCategorie(false, "VIDEO");

        if (!url.isEmpty()) {
            UtilityVideo.getInstance().ImpostaVideo();
        } else {
            ChiamateWSV ws2 = new ChiamateWSV(context);
            ws2.RitornaProssimoVideo("VIDEO");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (VariabiliStaticheVideo.getInstance().getVideoView() != null) {
            VariabiliStaticheVideo.getInstance().getVideoView().stopPlayback();
            VariabiliStaticheVideo.getInstance().getVideoView().clearAnimation();
            VariabiliStaticheVideo.getInstance().getVideoView().suspend(); // clears media player
            VariabiliStaticheVideo.getInstance().getVideoView().setVideoURI(null);
            VariabiliStaticheVideo.getInstance().setVideoView(null);
        }

        act.finish();
    }


    private void ImpostaSpostamento(Activity act) {
        LinearLayout laySposta = act.findViewById(R.id.laySposta);
        laySposta.setVisibility(LinearLayout.GONE);

        ImageView imgApre = act.findViewById(R.id.imgSpostaACategoria);
        imgApre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySposta.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgSpostaImmagine = act.findViewById(R.id.imgSpostaImmagine);
        imgSpostaImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSV c = new ChiamateWSV(context);
                c.SpostaVideo();

                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaSposta = act.findViewById(R.id.imgAnnullaSposta);
        imgAnnullaSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        EditText edtFiltroSpostamento = findViewById(R.id.edtSpostaFiltroCategoria);
        edtFiltroSpostamento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliStaticheVideo.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                UtilityVideo.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        VariabiliStaticheVideo.getInstance().setIdCategoriaSpostamento("");
        VariabiliStaticheVideo.getInstance().setSpnSpostaCategorie(findViewById(R.id.spnSpostaCategorie));
        final boolean[] primoIngresso = {true};
        VariabiliStaticheVideo.getInstance().getSpnSpostaCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    UtilitiesGlobali.getInstance().ApreToast(context,
                            "Impostare una categoria. Non Tutte");
                } else {
                    for (String s : VariabiliStaticheVideo.getInstance().getListaCategorie()) {
                        if (s.equals(Categoria)) {
                            VariabiliStaticheVideo.getInstance().setIdCategoriaSpostamento(s);
                            break;
                        }
                    }
                    if (VariabiliStaticheVideo.getInstance().getIdCategoriaSpostamento().isEmpty()) {
                        UtilitiesGlobali.getInstance().ApreToast(context,
                                "Categoria non valida: " + Categoria);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return true;
        }

        return false;
    }
}
