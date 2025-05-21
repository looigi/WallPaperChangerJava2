package com.looigi.wallpaperchanger2.classeDetector;

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
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.CaricaSettaggi;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AndroidCameraApi extends Activity {
    private static String NomeMaschera = "Camera2";
    private static final String TAG = "AndroidCameraApi";
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraId;
    private CameraManager manager;
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
    private String daDove;
    // private Activity act;
    private Context context;
    private boolean ERRORE = false;
    private int Secondi;
    private int cameraFronteRetro;
    private String sEstensione = "";
    private int QuantiScatti = 3;
    private int Scatto = 0;
    private Activity act;
    private boolean CePocaLuce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        act = this;
        context = this;

        /* String rit = CaricaSettaggi.getInstance().CaricaImpostazioniGlobali(context, "AndroidCamera");
        if (!rit.equals("OK")) {
            UtilitiesGlobali.getInstance().ApreToast(this,
                    "Impossibile leggere le impostazioni");

            return;
        } */

        Attiva();
    }

    private void Attiva() {
        if (act == null) {
            UtilitiesGlobali.getInstance().ApreToast(this,
                    "ACT NULLA");

            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Attiva: Act NULLA");

            act.finish();
            return;
        }

        textureView = (TextureView) act.findViewById(R.id.textureViewCamera);
        if (textureView == null) {
            UtilitiesGlobali.getInstance().ApreToast(this,
                    "Texture View NULLA");

            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Attiva: Texture View NULLA");

            act.finish();
            return;
        }
        textureView.setSurfaceTextureListener(textureListener);

        sEstensione = "dbf";

        int cameraID = VariabiliStaticheDetector.getInstance().getFotocamera();
        if (cameraID == 0) {
            cameraFronteRetro = 0;
        } else {
            cameraFronteRetro = 1;
        }

        Secondi = 3000;
        if (VariabiliStaticheDetector.getInstance().getSecondi() != 0) {
            Secondi = VariabiliStaticheDetector.getInstance().getSecondi() * 1000;
        }

        this.context = this;
        this.daDove = "";
        QuantiScatti = VariabiliStaticheDetector.getInstance().getNumeroScatti();

        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Start Camera. Secondi: " + Secondi + " Camera: " + cameraFronteRetro);

        ERRORE = false;
        VariabiliStaticheDetector.getInstance().setCameraImpostata(false);
        Scatto = 0;

        UtilityDetector.getInstance().Vibra(context, 150);

        AttesaCamera();

        UtilitiesGlobali.getInstance().ApreToast(context, "Start C");
    }

    private void AttesaCamera() {
        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Attesa Camera");

        if (!VariabiliStaticheDetector.getInstance().isCameraImpostata()) {
            final int[] conta = {0};

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    if (VariabiliStaticheDetector.getInstance().isCameraImpostata()) {
                        UtilityDetector.getInstance().ScriveLog(
                                context,
                                NomeMaschera,
                                "Attesa Camera. OK");

                        takePicture();
                    } else {
                        conta[0]++;
                        if (conta[0] > 20) {
                            UtilityDetector.getInstance().ScriveLog(
                                    context,
                                    NomeMaschera,
                                    "Attesa Camera. Esco per mancata attivazione");
                            UtilitiesGlobali.getInstance().ApreToast(context, "Object C not activated");

                            // CAMERA NON ATTIVATA IN TEMPO
                            handlerTimer.removeCallbacks(this);

                            act.finish();
                        } else {
                            handlerTimer.postDelayed(this, 150);
                        }
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 150);
        } else {
            takePicture();
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                              int height) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "TextureView. Available");

            //open your camera here
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int
                width, int height) {
            // Transform you image captured size according to the surface width
            //  and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "TextureView. Destroyed");

            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "CameraDevice. Opened");

            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "CameraDevice: Disconnected");

            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "CameraDevice: OnError: " + error);

            cameraDevice.close();
            cameraDevice = null;
        }
    };

    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);

            // CePocaLuce = ControllaSeCePocaLuce(manager, result, "OnCaptureCompleted 1");

            // Toast.makeText(AndroidCameraApi.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "CameraCaptureSession: onCaptureCompleted");

            createCameraPreview();
        }
    };

    protected void startBackgroundThread() {
        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "StartBackground");

        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "StopBackground");

        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
            }
        }
    }

    protected void takePicture() {
        if (null == cameraDevice) {
            // Log.e(TAG, "cameraDevice is null");
            UtilitiesGlobali.getInstance().ApreToast(this, "Oggetto C nullo");
            act.finish();
            return;
        }

        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Scatto " + (Scatto + 1) + "/" + QuantiScatti);

        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = manager.getCameraIdList()[cameraFronteRetro];

            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "CameraID: " +  cameraId);

            if (VariabiliStaticheDetector.getInstance().getDimensioni() == null) {
                UtilityDetector.getInstance().RitornaRisoluzioni(this, cameraFronteRetro);
            }

            String RisolX = UtilityDetector.getInstance().RitornaRisoluzioneMassima(
                    VariabiliStaticheDetector.getInstance().getDimensioni());
            int pos = RisolX.indexOf("x");
            int width = Integer.parseInt(RisolX.substring(pos + 1));
            int height = Integer.parseInt(RisolX.substring(0, pos));

            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Risoluzione: " + RisolX);

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            captureBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, CameraMetadata.LENS_OPTICAL_STABILIZATION_MODE_ON);
            captureBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CameraMetadata.CONTROL_SCENE_MODE_HDR);

            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            String Cartella = UtilityDetector.getInstance().PrendePath(context);
            UtilityWallpaper.getInstance().CreaCartelle(Cartella);
            UtilityDetector.getInstance().ControllaFileNoMedia(Cartella);

            String fileName = Cartella + UtilityDetector.getInstance().PrendeNomeImmagine() +
                    "." + sEstensione;
            File file = new File(fileName);
            // UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Nome file: " + fileName);

            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Nome File: " + fileName);

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    UtilityDetector.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "onImageAvailable");

                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        UtilityDetector.getInstance().ScriveLog(
                                context,
                                NomeMaschera,
                                "onImageAvailable. File Not Found: " +
                                UtilityDetector.getInstance().PrendeErroreDaException(e));

                        // e.printStackTrace();
                    } catch (IOException e) {
                        UtilityDetector.getInstance().ScriveLog(
                                context,
                                NomeMaschera,
                                "onImageAvailable. IO Exception: " +
                                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                        // e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    UtilityDetector.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "Salvataggio");

                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();

                            AggiungeTag(file);
                        }
                    }

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            Scatto++;
                            if (Scatto < QuantiScatti) {
                                Handler handlerTimer = new Handler(Looper.getMainLooper());
                                Runnable rTimer = new Runnable() {
                                    public void run() {
                                        UtilityDetector.getInstance().Vibra(context, 250);
                                        UtilityDetector.getInstance().VisualizzaToast(context,
                                                (Scatto + 1) + "/" + QuantiScatti,
                                                true);

                                        takePicture();
                                    }
                                };
                                handlerTimer.postDelayed(rTimer, Secondi);
                            } else {
                                UtilityDetector.getInstance().ScriveLog(
                                        context,
                                        NomeMaschera,
                                        "Uscita");

                                UtilityDetector.getInstance().ContaFiles(context);

                                // VariabiliStaticheStart.getInstance().ChiudeActivity(false);
                                act.finish();

                                UtilityDetector.getInstance().Vibra(context, 1500);
                                UtilityDetector.getInstance().VisualizzaToast(context,
                                        "Completed",
                                        true);

                                stopBackgroundThread();

                                closeCamera();

                                FineElaborazione();
                            }
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 1000);
                }
            };

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);

                    // CePocaLuce = ControllaSeCePocaLuce(request);

                    // Toast.makeText(AndroidCameraApi.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Camera Access Exception: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
        }
    }

    private void AggiungeTag(File imageFile) throws IOException {
        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());

        if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() != null) {
            StrutturaGps s = VariabiliStaticheGPS.getInstance().getCoordinateAttuali();

            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, Double.toString(s.getLat()));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, Double.toString(s.getLon()));
            exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, Double.toString(s.getAltitude()));
            exif.setAttribute(ExifInterface.TAG_GPS_SPEED, Double.toString(s.getSpeed()));

            if (s.getLat() > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (s.getLon() > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }
        }

        exif.setAttribute(ExifInterface.TAG_ARTIST, "WallPaperChanger 2");
        exif.setAttribute(ExifInterface.TAG_MAKE, "Looigi");
        if (VariabiliStaticheDetector.getInstance().getModelloTelefono() != null) {
            exif.setAttribute(ExifInterface.TAG_MODEL, VariabiliStaticheDetector.getInstance().getModelloTelefono());
        }

        exif.saveAttributes();
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_LOCK, false);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            captureRequestBuilder.set(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE_ON);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, new Range<>(15, 30));
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            // Esposizione più breve per evitare mosso
            captureRequestBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, 4000000L); // 1/250s
            captureRequestBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, 800); // ISO più alto
            captureRequestBuilder.set(CaptureRequest.LENS_APERTURE, 1.8f);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_OFF);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    UtilityDetector.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "Create Camera Preview: onConfigured");

                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    UtilityDetector.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "Create Camera Preview: onConfiguredFailed");
                    // Toast.makeText(AndroidCameraApi.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Create Camera Preview: Camera Access Exception: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e));
        }
    }

    private void openCamera() {
        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Open Camera");

        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        // Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Open Camera. Camera Access Exception: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e));

        }
        // Log.e(TAG, "openCamera X");
    }

    protected void updatePreview() {
        if (null == cameraDevice || captureRequestBuilder == null) {
            // Log.e(TAG, "updatePreview error, return");
            return;
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(
                    captureRequestBuilder.build(),
                    null,
                    mBackgroundHandler);

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    UtilityDetector.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "Update Preview: Camera Impostata");

                    VariabiliStaticheDetector.getInstance().setCameraImpostata(true);
                }
            };
            handlerTimer.postDelayed(rTimer, 1000);
        } catch (CameraAccessException e) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Update Preview. Camera Access Exception: " +
                            UtilityDetector.getInstance().PrendeErroreDaException(e));
        }
    }

    private void closeCamera() {
        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Close Camera");

        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                // Toast.makeText(AndroidCameraApi.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                // finish();
                VariabiliStaticheStart.getInstance().ChiudeActivity(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Log.e(TAG, "onResume");
        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "onResume");

        // Attiva();

        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (act != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
            act.finish();
            // }
        }


        super.onKeyDown(keyCode, event);

        /* UtilityDetector.getInstance().ScriveLog(this, NomeMaschera,
                "Tasto premuto: " + Integer.toString(keyCode));

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheStart.getInstance().ChiudeActivity(false);

                return false;
        } */

        return false;
    }
    
    @Override
    protected void onPause() {
        super.onPause();

        // Log.e(TAG, "onPause");
        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "onPause");

        textureView.setVisibility(LinearLayout.GONE);

        closeCamera();
        stopBackgroundThread();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // e.printStackTrace();
        }
    }

    private void FineElaborazione() {
        if (sEstensione.equals("dbf")) {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Criptaggio Files");

            UtilityDetector.getInstance().CriptaFiles(this);
        }

        UtilityDetector.getInstance().AccendeSchermo(context);

        act.finish();
    }

    private boolean ControllaSeCePocaLuce(ImageReader reader) {
        /* CameraCharacteristics characteristics = null;
        boolean ritorno = false;

        UtilityDetector.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Controllo poca luce. Chiamato da " + daDove);

        if (manager != null) {
            try {
                characteristics = manager.getCameraCharacteristics(cameraId);
                Range<Integer> isoRange = characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);

                if (isoRange != null) {
                    int minISO = isoRange.getLower();
                    int maxISO = isoRange.getUpper();
                    int currentISO = captureResult.get(CaptureResult.SENSOR_SENSITIVITY);
                    UtilityDetector.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "Controllo poca luce. Iso Attuale: " + currentISO + " - Minimo: " + maxISO);

                    if (currentISO < maxISO / 2) {
                        UtilityDetector.getInstance().ScriveLog(
                                context,
                                NomeMaschera,
                                "Poca luce");

                        ritorno = true;
                    }
                }
            } catch (CameraAccessException e) {
                UtilityDetector.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Errore su controllo poca luce: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        } else {
            UtilityDetector.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Errore su controllo poca luce. Manager camera nullo");
        }

        return ritorno; */
        return false;
    }
}