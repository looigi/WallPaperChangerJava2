/* package com.looigi.wallpaperchanger2.classiAttivitaDetector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.db_dati;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Camera2 {
    private static String NomeMaschera = "CAMERA2";
    private static final String TAG = "AndroidCameraApi";
    // private Button takePictureButton;
    // private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private Handler handlerTimerAttesaTexture;
    private Runnable rTimerAttesaTexture;
    private Handler handlerTimerAttesaCamera;
    private Runnable rTimerAttesaCamera;
    private Handler handlerTimerAttesaScatto;
    private Runnable rTimerAttesaScatto;
    private Handler handlerTimerPausa;
    private Runnable rTimerAttesaPausa;
    private Handler handlerTimerChiusura;
    private Runnable rTimerChiusura;
    private int cameraImpostata = 1;

    private ImageReader reader = null;
    private List<Surface> outputSurfaces = null;
    private CaptureRequest.Builder captureBuilder = null;
    private String sEstensione = "";
    private int Secondi = 3000;
    private int Estensione;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private boolean Scattato = false;
    private boolean ERRORE = false;
    private String textureViewImpostata = "";
    private String daDove;
    private Activity act;
    private Context context;
    private TextureView textureView;

    public void Start(Activity act, Context context, String daDove) {
        this.act = act;
        this.context = context;
        this.daDove = daDove;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String nome = "LogAcq_" + currentDateandTime + ".txt";
        if (daDove.equals("MAIN")) {
            nome = VariabiliStaticheDetector.getInstance().getNomeLogServizio();
        }

        UtilityDetector.getInstance().ScriveLog(act, NomeMaschera, "Start Camera 1. Provenienza " + daDove);

        ERRORE = false;
        creaTextureView(act);
    }

    private void creaTextureView(Activity act) {
        if (act != null) {
            // if (textureView != null) {
            // } else {
            textureViewImpostata = "";
            // if (this.act != null) {
                textureView = (TextureView) act.findViewById(R.id.textureView);
                if (textureView != null) {
                    // textureView.setVisibility(LinearLayout.VISIBLE);
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Crea Texture View");
                    // assert textureView != null;
                    /* if (textureView == null) {
                        nascondeTV();

                        UtilityDetector.getInstance().ScriveLog(ctx,  NomeMaschera, "Crea Texture View Error: TEXTURE VIEW NULL 2");
                        UtilityDetector.getInstance().VisualizzaToast(ctx, "Error: TEXTURE VIEW NULL 2", true);
                        UtilityDetector.getInstance().Vibra(ctx, 2000, l);
                        ERRORE = true;
                    } else { * /
                        ERRORE = false;
                        textureView.setSurfaceTextureListener(textureListener);

                        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "" +
                                "Crea Texture View ciclo: " + VariabiliStaticheDetector.getInstance().getRisoluzione());
                        if (VariabiliStaticheDetector.getInstance().getRisoluzione().isEmpty()) {
                            boolean Lette = UtilityDetector.getInstance().LeggeImpostazioni(act);
                            VariabiliStaticheDetector.getInstance().setLetteImpostazioni(Lette);
                            if (!Lette) {
                                UtilityDetector.getInstance().VisualizzaToast(this.act, "Impossibile aprire impostazioni", true);
                            }
                        }
                    // }
                } else {
                    nascondeTV(act);

                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Crea Texture View Error: TEXTURE VIEW NULL 1");
                    UtilityDetector.getInstance().VisualizzaToast(this.act, "Error: TEXTURE VIEW NULL 1", true);
                    UtilityDetector.getInstance().Vibra(this.act, 2000);
                    ERRORE = true;
                }
            /* } else {
                nascondeTV(act);

                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Crea Texture View Error: CTX NULL");
                ERRORE = true;
            } * /
        } else {
            nascondeTV(act);

            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Crea Texture View Error: ACT NULL");
            ERRORE = true;
        }
        // }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            textureViewImpostata = "OK";
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "TVSTL: 1");
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            textureViewImpostata = "CAMBIATA";
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "TVSTL: 2");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            textureViewImpostata = "DISTRUTTA";
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "TVSTL: 3");
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            textureViewImpostata = "UPDATATA";
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "TVSTL: 4");
        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "onOpened");
            
            cameraDevice = camera;
            createCameraPreview(act);
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "disconnected");

            if (cameraDevice != null) {
                cameraDevice.close();
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "onError " + error);

            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }
    };

    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            
            Toast.makeText(act, "Saved:" + file, Toast.LENGTH_SHORT).show();
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "Acquisita");

            createCameraPreview(act);
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();

        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ImpostaFrontale() {
        cameraImpostata = 0;
    }

    public void ImpostaRetro() {
        cameraImpostata = 1;
    }

    public void RitornaRisoluzioni() {
        List<String> Dimens=new ArrayList<String>();

        try {
            CameraManager cameraManager = (CameraManager) act.getSystemService(Context.CAMERA_SERVICE);
            String[] Camere = cameraManager.getCameraIdList();
            String cameraId = Camere[cameraImpostata];
            // for (String cameraId : Camere) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                Size[] sizes = streamConfigurationMap.getOutputSizes(ImageFormat.PRIVATE);
                // String size = getCameraMP(sizes);
                for (Size size : sizes) {
                    Dimens.add(size.getWidth() + "x" + size.getHeight());
                }
            // }
        } catch (CameraAccessException e) {
            UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "RitornaRisoluzioni Error: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
        }

        VariabiliStaticheDetector.getInstance().setDimensioni(Dimens);
    }

    private int Quale;
    private int Quante;
    private int Attesa;

    public void ScattaFoto(String NomeLog, Activity act, TextureView textureView) {
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Scatta foto: Impegnato -> " + VariabiliStaticheDetector.getInstance().isStoScattando());
        // VariabiliStatiche.getInstance().setControllaWatchDog(false);

        if (!VariabiliStaticheDetector.getInstance().isLetteImpostazioni()) {
            db_dati_detector db = new db_dati_detector(context);
            db.CreazioneTabelle();

            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
            boolean letto = UtilityDetector.getInstance().LeggeImpostazioni(context);
            VariabiliStaticheDetector.getInstance().setLetteImpostazioni(letto);
        }

        if (!VariabiliStaticheDetector.getInstance().isStoScattando()) {
            VariabiliStaticheDetector.getInstance().setStoScattando(true);

            openCamera(act);

            Quale = 0;
            this.Quante = VariabiliStaticheDetector.getInstance().getNumeroScatti();
            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Inizio scatti: " + Quante);

            ScattaFoto2(act, textureView);
        } else {
            nascondeTV(act);

            UtilityDetector.getInstance().VisualizzaToast(this.act, "Session busy", false);
        }
    }

    private void nascondeTV(Activity act) {
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Nascondo TextView Utility Camera2");

        if (act != null) {
            TextureView textureView = (TextureView) act.findViewById(R.id.textureView);
            if (textureView != null) {
                textureView.setVisibility(LinearLayout.GONE);
                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Nascondo TextView Utility Camera2: OK");
            } else {
                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Nascondo TextView Utility Camera2: TV non esistente");
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Nascondo TextView Utility Camera2: Act non esistente");
        }
    }

    private void ScattaFoto2(Activity act, TextureView textureView) {
        UtilityDetector.getInstance().VisualizzaToast(this.act, (Quale + 1) + "/" + Quante, false);
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Scatto: " + (Quale + 1) + "/" + Quante);

        Attesa = 0;
        Scattato = false;
        UtilityDetector.getInstance().Vibra(this.act, 200);

        // SETTA IMPOSTAZIONI
        Secondi = 3000;
        if (VariabiliStaticheDetector.getInstance().getSecondi() != 0) {
            Secondi = VariabiliStaticheDetector.getInstance().getSecondi() * 1000;
        }
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Secondi: " + Secondi);

        Estensione = VariabiliStaticheDetector.getInstance().getEstensione(); // Integer.parseInt(Ritorno.substring(pos+1,Ritorno.length()));
        // if (Estensione == 2) {
            sEstensione = "dbf";
        /* } else {
            sEstensione = "jpg";
        } * /
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Estensione: " + Estensione + " -> " + sEstensione);

        // int width = 4080;
        // int height = 3060;
        // if (VariabiliStaticheDetector.getInstance().getRisoluzione() == null) {
            // VariabiliStaticheDetector.getInstance().setRisoluzione("4080x3060");
        if (VariabiliStaticheDetector.getInstance().getDimensioni() == null) {
            RitornaRisoluzioni();
        }

        String RisolX = UtilityDetector.getInstance().RitornaRisoluzioneMassima(
                VariabiliStaticheDetector.getInstance().getDimensioni());
        // }
        /* String RisolX = VariabiliStaticheDetector.getInstance().getRisoluzione(); * /
        int pos = RisolX.indexOf("x");
        int width = Integer.parseInt(RisolX.substring(pos + 1));
        int height = Integer.parseInt(RisolX.substring(0, pos));
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera,
                "Dimensioni: " + width + "x" + height + " (" + RisolX + ")");

        reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);

        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Attesa per texture view");
        final int[] q = {0};
        handlerTimerAttesaTexture = new Handler();
        rTimerAttesaTexture = new Runnable() {
            public void run() {
                if (textureViewImpostata.isEmpty()) {
                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Attesa per texture view: " + q[0]);
                    if (q[0] > 10) {
                        nascondeTV(act);

                        UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Attesa per texture view. Esco... " + textureViewImpostata);
                        UtilityDetector.getInstance().VisualizzaToast(Camera2.this.act, "Attesa per texture view. Esco... " + textureViewImpostata, false);
                        VariabiliStaticheDetector.getInstance().setStoScattando(false);
                        handlerTimerAttesaTexture.removeCallbacks(rTimerAttesaTexture);
                        rTimerAttesaTexture = null;
                    // } else {
                    //     handlerTimerAttesaTexture.postDelayed(rTimerAttesaTexture, 1000);
                    }
                } else {
                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Attesa per texture view finita: " + textureViewImpostata);

                    if (textureViewImpostata.equals("OK") || textureViewImpostata.equals("UPDATATA")) {
                        final int[] q = {0};
                        handlerTimerAttesaCamera = new Handler();
                        rTimerAttesaCamera = new Runnable() {
                            public void run() {
                                if (cameraDevice == null) {
                                    q[0]++;
                                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Attesa per Camera device: " + q[0]);
                                    if (q[0] > 10) {
                                        nascondeTV(act);

                                        UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Attesa per Camera device. Esco... Troppo tempo...");
                                        UtilityDetector.getInstance().VisualizzaToast(Camera2.this.act, "Attesa per camera device. Esco per troppo tempo", false);
                                        VariabiliStaticheDetector.getInstance().setStoScattando(false);
                                        handlerTimerAttesaCamera.removeCallbacks(rTimerAttesaCamera);
                                        rTimerAttesaCamera = null;
                                    // } else {
                                    //     handlerTimerAttesaCamera.postDelayed(rTimerAttesaCamera, 1000);
                                    }
                                } else {
                                    prosegueScattaFoto(act, textureView);

                                    handlerTimerAttesaCamera.removeCallbacks(rTimerAttesaCamera);
                                    rTimerAttesaCamera = null;
                                }
                            }
                        };
                        handlerTimerAttesaCamera.postDelayed(rTimerAttesaCamera, 1000);
                    } else {
                        nascondeTV(act);

                        UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Attesa per texture view. Esco per textview non valida... " + textureViewImpostata);
                        UtilityDetector.getInstance().VisualizzaToast(Camera2.this.act, "Attesa per camera device. Esco per textview non valida... " + textureViewImpostata, false);
                        VariabiliStaticheDetector.getInstance().setStoScattando(false);
                    }
                    handlerTimerAttesaTexture.removeCallbacks(rTimerAttesaTexture);
                    rTimerAttesaTexture = null;
                }
            }
        };
        handlerTimerAttesaTexture.postDelayed(rTimerAttesaTexture, 1000);

        // SETTA IMPOSTAZIONI
    }

    private void prosegueScattaFoto(Activity act, TextureView textureView) {
        CameraManager manager = (CameraManager) this.act.getSystemService(Context.CAMERA_SERVICE);

        if (this.act == null) {
            nascondeTV(act);

            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "PSF. Error: CTX NULL");
            UtilityDetector.getInstance().VisualizzaToast(this.act, "Error: CTX NULL", true);
            UtilityDetector.getInstance().Vibra(this.act, 2000);
        } else {
            // if (textureView == null) {
            // creaTextureView(act);
            // }

            try {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
                Size[] jpegSizes = null;
                if (characteristics != null) {
                    jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
                }
                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Impostato jpegSizes");

                outputSurfaces = new ArrayList<Surface>(2);
                outputSurfaces.add(reader.getSurface());
                if (textureView != null && textureView.getSurfaceTexture() != null) {
                    outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Impostato outputSurfaces");

                    captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                    captureBuilder.addTarget(reader.getSurface());
                    captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Impostato captureBuilder");

                    // Orientation
                    int rotation = this.act.getWindowManager().getDefaultDisplay().getRotation();
                    captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Impostata rotazione");

                    handlerTimerPausa = new Handler();
                    rTimerAttesaPausa = new Runnable() {
                        public void run() {
                            handlerTimerPausa.removeCallbacks(rTimerAttesaPausa);

                            try {
                                takePicture(act);

                                handlerTimerAttesaScatto = new Handler();
                                rTimerAttesaScatto = new Runnable() {
                                    public void run() {
                                        if (Scattato) {
                                            handlerTimerAttesaScatto.removeCallbacks(rTimerAttesaScatto);
                                            rTimerAttesaScatto = null;

                                            Quale++;
                                            if (Quale < Quante) {
                                                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Procedo al prossimo scatto");
                                                ScattaFoto2(act, textureView);
                                            } else {
                                                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Terminati scatti");
                                                handlerTimerChiusura = new Handler();
                                                rTimerChiusura = new Runnable() {
                                                    public void run() {
                                                        if (Estensione == 2) {
                                                            UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Criptaggio jpeg");
                                                            UtilityDetector.getInstance().CriptaFiles(Camera2.this.act);
                                                        }

                                                        UtilityDetector.getInstance().VisualizzaToast(Camera2.this.act, "End of process", false);
                                                        UtilityDetector.getInstance().Vibra(Camera2.this.act, 500);
                                                        UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Finito. Chiudo camera");
                                                        VariabiliStaticheDetector.getInstance().setStoScattando(false);

                                                        closeCamera(act);

                                                        handlerTimerChiusura.removeCallbacks(rTimerChiusura);
                                                        rTimerChiusura = null;

                                                        // textureView.setVisibility(LinearLayout.GONE);
                                                    }
                                                };
                                                handlerTimerChiusura.postDelayed(rTimerChiusura, 1000);
                                            }
                                        } else {
                                            Attesa++;
                                            UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Attesa: " + Attesa);
                                            if (Attesa < 10) {
                                                handlerTimerAttesaScatto.postDelayed(rTimerAttesaScatto, 500);
                                            } else {
                                                handlerTimerAttesaScatto.removeCallbacks(rTimerAttesaScatto);
                                                rTimerAttesaScatto = null;

                                                nascondeTV(act);

                                                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Uscita da Attesa");
                                                UtilityDetector.getInstance().VisualizzaToast(Camera2.this.act, "Error on close", true);
                                            }
                                        }
                                    }
                                };
                                handlerTimerAttesaScatto.postDelayed(rTimerAttesaScatto, 500);
                            } catch (CameraAccessException e) {
                                nascondeTV(act);

                                UtilityDetector.getInstance().VisualizzaToast(Camera2.this.act, "Error in TP", false);
                                UtilityDetector.getInstance().Vibra(Camera2.this.act, 500);
                                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Errore. Take Picture " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                            }
                        }
                    };
                    handlerTimerPausa.postDelayed(rTimerAttesaPausa, Secondi);
                } else {
                    nascondeTV(act);

                    UtilityDetector.getInstance().VisualizzaToast(this.act, "Error in CM 1", false);
                    UtilityDetector.getInstance().Vibra(this.act, 500);
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Errore. Camera Manager 1");
                }
            } catch (CameraAccessException e) {
                nascondeTV(act);

                UtilityDetector.getInstance().VisualizzaToast(this.act, "Error in CM 2", false);
                UtilityDetector.getInstance().Vibra(this.act, 500);
                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Errore. Camera Manager 2: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    public void takePicture(Activity act) throws CameraAccessException {
        if (null == cameraDevice) {
            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "cameraDevice is null");
            return;
        }

        // String Origine=Environment.getExternalStorageDirectory().getAbsolutePath();
        // String Cartella= VariabiliStatiche.getInstance().PathApplicazione;
        String Cartella = UtilityDetector.getInstance().PrendePath(context);
        Utility.getInstance().CreaCartelle(Cartella);
        String fileName = Cartella + UtilityDetector.getInstance().PrendeNomeImmagine() + "." + sEstensione;
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Nome file: " + fileName);

        final File file = new File(fileName);
        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onImageAvailable");

                Image image = null;
                try {
                    image = reader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);
                    save(bytes);
                } catch (FileNotFoundException e) {
                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onImageAvailable Errore File Not Found: " + UtilityDetector.getInstance().PrendeErroreDaException(e));

                    Scattato = true;
                } catch (IOException e) {
                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onImageAvailable Errore IO: " + UtilityDetector.getInstance().PrendeErroreDaException(e));

                    Scattato = true;
                } finally {
                    if (image != null) {
                        image.close();
                    }
                }
            }

            private void save(byte[] bytes) throws IOException {
                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Save");

                OutputStream output = null;

                try {
                    output = new FileOutputStream(file);
                    output.write(bytes);
                } catch (Exception e) {
                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Save Error: " + UtilityDetector.getInstance().PrendeErroreDaException(e));

                    Scattato = true;
                } finally {
                    if (null != output) {
                        output.close();
                        UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "Save Ok");
                    }

                    Scattato = true;
                }
            }
        };

        reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
        final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);

                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onCaptureCompleted");

                createCameraPreview(act);
            }
        };

        cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onClosed(@NonNull CameraCaptureSession session) {
                super.onClosed(session);

                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onClosed");
            }

            @Override
            public void onConfigured(CameraCaptureSession session) {
                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onConfigured");

                try {
                    session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onConfigured OK");
                } catch (CameraAccessException e) {
                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onConfigured Errore: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
            }
        }, mBackgroundHandler);
    }

    protected void createCameraPreview(Activity act) {
        try {
            if (ERRORE) {
                nascondeTV(act);

                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Create Camera Preview: Esco per errore precedente");
            } else {
                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Create camera Preview");

                TextureView textureView = (TextureView) this.act.findViewById(R.id.textureView);

                if (textureView == null) {
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "onConfigureFailed Create camera preview TEXTURE VIEW NULL");
                } else {
                    SurfaceTexture texture = textureView.getSurfaceTexture();
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "CCP: 1");
                    // assert texture != null;
                    if (texture != null) {
                        texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
                        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "CCP: 2");
                        Surface surface = new Surface(texture);
                        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        captureRequestBuilder.addTarget(surface);
                        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "CCP: 3");
                        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onConfigured Create camera preview");

                                //The camera is already closed
                                if (null == cameraDevice) {
                                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "CCP: ESCO 1");
                                    return;
                                }
                                // When the session is ready, we start displaying the preview.
                                cameraCaptureSessions = cameraCaptureSession;
                                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "CCP: 4");
                                updatePreview(act);
                            }

                            @Override
                            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                                UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "onConfigureFailed Create camera preview");

                                // Toast.makeText(ctx, "Configuration change", Toast.LENGTH_SHORT).show();
                            }
                        }, null);
                    } else {
                        nascondeTV(act);

                        UtilityDetector.getInstance().VisualizzaToast(this.act, "Create camera preview: TEXTURE NULL", false);
                        UtilityDetector.getInstance().Vibra(this.act,500);
                        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Create camera preview: TEXTURE NULL");
                        ERRORE = true;
                    }
                }
            }
        } catch (CameraAccessException e) {
            nascondeTV(act);

            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Create camera preview error: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
        }
    }

    public void openCamera(Activity act) {
        if (ERRORE) {
            nascondeTV(act);

            UtilityDetector.getInstance().VisualizzaToast(this.act, "Open Camera. Previous Error", false);
            UtilityDetector.getInstance().Vibra(this.act,500);
            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Open Camera. Esco per Errore precedente");
        } else {
            CameraManager manager = (CameraManager) this.act.getSystemService(Context.CAMERA_SERVICE);
            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "is camera open");

            try {
                cameraId = manager.getCameraIdList()[cameraImpostata];
                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "OpenCamera: Camera id: " + cameraId);
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "OpenCamera: MAP");
                // assert map != null;
                if (map != null) {
                    imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "OpenCamera: Image Dimension");

                    // Add permission for camera and let user grant the permission
                    if (ActivityCompat.checkSelfPermission(this.act, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this.act, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                        return;
                    }

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        public void run() {
                            if (context != null) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                try {
                                    manager.openCamera(cameraId, stateCallback, null);
                                } catch (CameraAccessException e) {
                                    throw new RuntimeException(e);
                                }

                                UtilityDetector.getInstance().ScriveLog(act,  NomeMaschera, "openCamera X");
                            }
                        }
                    }, 100);

                } else {
                    nascondeTV(act);

                    UtilityDetector.getInstance().VisualizzaToast(this.act, "openCamera Map Null", false);
                    UtilityDetector.getInstance().Vibra(this.act, 500);
                    UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "openCamera: MAP NULL");
                    ERRORE = true;
                }
            } catch (CameraAccessException e) {
                nascondeTV(act);

                UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "isCameraOpen error: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    protected void updatePreview(Activity act) {
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "updatePreview");
        if (cameraDevice == null) {
            nascondeTV(act);

            UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "updatePreview error, return");
        }
        
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "UP: 1");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),
                            null, mBackgroundHandler);
                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "updatePreview OK");
                } catch (CameraAccessException e) {
                    nascondeTV(act);

                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "updatePreview camera access exception: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                } catch (IllegalStateException e) {
                    nascondeTV(act);

                    UtilityDetector.getInstance().ScriveLog(Camera2.this.act,  NomeMaschera, "updatePreview illegal state exception: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
                }
            }
        }, 500);
    }

    public void closeCamera(Activity act) {
        nascondeTV(act);

        UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Close camera");
        // VariabiliStatiche.getInstance().setControllaWatchDog(true);

        textureView = null;
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }
} */