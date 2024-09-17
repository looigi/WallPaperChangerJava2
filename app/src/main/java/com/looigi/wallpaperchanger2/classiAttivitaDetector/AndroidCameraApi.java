package com.looigi.wallpaperchanger2.classiAttivitaDetector;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AndroidCameraApi extends AppCompatActivity {
    private static String NomeMaschera = "CAMERA2";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        textureView = (TextureView) findViewById(R.id.textureView);
        assert textureView != null;
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
                        takePicture();
                    } else {
                        conta[0]++;
                        if (conta[0] > 20) {
                            // CAMERA NON ATTIVATA IN TEMPO
                            handlerTimer.removeCallbacks(this);
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
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            // Toast.makeText(AndroidCameraApi.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void takePicture() {
        if (null == cameraDevice) {
            // Log.e(TAG, "cameraDevice is null");
            Utility.getInstance().ApreToast(this, "Oggetto C nullo");
            return;
        }
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[cameraFronteRetro];

            if (VariabiliStaticheDetector.getInstance().getDimensioni() == null) {
                UtilityDetector.getInstance().RitornaRisoluzioni(this, cameraFronteRetro);
            }

            String RisolX = UtilityDetector.getInstance().RitornaRisoluzioneMassima(
                    VariabiliStaticheDetector.getInstance().getDimensioni());
            int pos = RisolX.indexOf("x");
            int width = Integer.parseInt(RisolX.substring(pos + 1));
            int height = Integer.parseInt(RisolX.substring(0, pos));

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            String Cartella = UtilityDetector.getInstance().PrendePath(context);
            Utility.getInstance().CreaCartelle(Cartella);
            String fileName = Cartella + UtilityDetector.getInstance().PrendeNomeImmagine() +
                    "." + sEstensione;
            File file = new File(fileName);
            // UtilityDetector.getInstance().ScriveLog(this.act,  NomeMaschera, "Nome file: " + fileName);

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
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
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    // Toast.makeText(AndroidCameraApi.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        // Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[cameraFronteRetro];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
            e.printStackTrace();
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
                    VariabiliStaticheDetector.getInstance().setCameraImpostata(true);
                }
            };
            handlerTimer.postDelayed(rTimer, 1000);
        } catch (CameraAccessException e) {
            // e.printStackTrace();
        }
    }

    private void closeCamera() {
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
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Log.e(TAG, "onPause");
        textureView.setVisibility(LinearLayout.GONE);

        closeCamera();
        stopBackgroundThread();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void FineElaborazione() {
        if (sEstensione.equals("dbf")) {
            UtilityDetector.getInstance().CriptaFiles(this);
        }

        finish();
    }
}