package com.looigi.wallpaperchanger2.classiAttivitaWallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.ArrayList;
import java.util.List;

public class RilevamentoVolti {
    private Context context;
    private final FaceDetectorOptions highAccuracyOpts;

    public RilevamentoVolti(Context context) {
        highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();
    }

    public void ElaboraImmagine(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);
        Task<List<Face>> result =
                detector.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Face>>() {
                                    @Override
                                    public void onSuccess(List<Face> faces) {
                                        List<Rect> rects = new ArrayList<>();
                                        for (Face face : faces) {
                                            rects.add(face.getBoundingBox());

                                            /* Paint paint=new Paint();
									paint.setColor(Color.RED);

									Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

									Canvas cnvs=new Canvas(mutableBitmap);
									cnvs.drawBitmap(BitmapFactory.decodeFile(finalPath), 0, 0, null);
									cnvs.drawRect(r , paint);

									String filename = context.getFilesDir() + "/Download/AppoggioCentrato.jpg";
									try (FileOutputStream out = new FileOutputStream(filename)) {
										mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
										// PNG is a lossless format, the compression factor (100) is ignored
									} catch (IOException e) {
										UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
												"Cambio immagine. Faccia centrata ERROR: " + UtilityDetector.getInstance().PrendeErroreDaException(e));
									} */


                                            /* float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                            // nose available):
                                            FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
                                            if (leftEar != null) {
                                                PointF leftEarPos = leftEar.getPosition();
                                            }

                                            // If contour detection was enabled:
                                            List<PointF> leftEyeContour =
                                                    face.getContour(FaceContour.LEFT_EYE).getPoints();
                                            List<PointF> upperLipBottomContour =
                                                    face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();

                                            // If classification was enabled:
                                            if (face.getSmilingProbability() != null) {
                                                float smileProb = face.getSmilingProbability();
                                            }
                                            if (face.getRightEyeOpenProbability() != null) {
                                                float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                            }

                                            // If face tracking was enabled:
                                            if (face.getTrackingId() != null) {
                                                int id = face.getTrackingId();
                                            } */
                                        }

                                        VariabiliStaticheWallpaper.getInstance().setQuadratiFaccia(rects);
                                        VariabiliStaticheWallpaper.getInstance().setStaPrendendoVolto(false);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        VariabiliStaticheWallpaper.getInstance().setQuadratiFaccia(null);
                                        VariabiliStaticheWallpaper.getInstance().setStaPrendendoVolto(false);
                                    }
                                });
    }
}
