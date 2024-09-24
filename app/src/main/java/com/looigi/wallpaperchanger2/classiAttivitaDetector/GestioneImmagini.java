package com.looigi.wallpaperchanger2.classiAttivitaDetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.RilevamentoVolti;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

public class GestioneImmagini {
	private static final String NomeMaschera = "GESTIONEIMMAGINI";

	public void Ruotaimmagine(String NomeFile, int Angolo) {
		try {
			rotateBitmap(NomeFile, Angolo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean rotateBitmap(String NomeFile, int angle) throws IOException {
		File inFile = new File(NomeFile);

		boolean OkEXIF=false;
		String artista = "";
		String model = "";
		String lat = "";
		String latRef = "";
		String lon = "";
		String lonref = "";

		try {
			ExifInterface exif = new ExifInterface(NomeFile);
			artista = exif.getAttribute(ExifInterface.TAG_ARTIST);
			model = exif.getAttribute(ExifInterface.TAG_MODEL);
			lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			latRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
			lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			lonref = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

			OkEXIF = true;
		} catch (IOException ignored) {
			OkEXIF = false;
		}

		File outFile = new File(NomeFile+".PPP");

	    FileInputStream inStream = null;
	    FileOutputStream outStream = null;

	    BitmapFactory.Options options = new BitmapFactory.Options();

	    Matrix matrix = new Matrix();
	    matrix.postRotate(angle);

	    for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
	        try {
	            inStream = new FileInputStream(inFile);
	            Bitmap originalBitmap = BitmapFactory.decodeStream(inStream, null, options);

	            Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

	            outStream = new FileOutputStream(outFile);
	            rotatedBitmap.compress(CompressFormat.JPEG, 95, outStream);
	            outStream.close();

	            originalBitmap.recycle();
	            originalBitmap = null;
	            rotatedBitmap.recycle();
	            rotatedBitmap = null;

	            return true;
	        } catch (OutOfMemoryError ignored) {
	        } finally {
	            if (outStream != null) {
	                try {
	                    outStream.close();

						UtilityDetector.getInstance().EliminaFile(NomeFile);
	                    String Cartella = UtilityDetector.getInstance().PrendeNomeCartella(NomeFile);
	                    String VecchioNome = UtilityDetector.getInstance().PrendeNomeFile(NomeFile);
	                    VecchioNome+=".PPP";
	                    String NuovoNome = UtilityDetector.getInstance().PrendeNomeFile(NomeFile);

						UtilityDetector.getInstance().RinominaFile(Cartella, VecchioNome, NuovoNome);

	                    if (OkEXIF) {
							ExifInterface exif = new ExifInterface(NuovoNome);
							exif.setAttribute(ExifInterface.TAG_ARTIST, artista);
							exif.setAttribute(ExifInterface.TAG_MODEL, model);
							exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
							exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latRef);
							exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, lon);
							exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lonref);
							exif.saveAttributes();
						}
	                } catch (IOException ignored) {
	                }
	            }
	        }
	    }

	    return false;
	}

	public void RuotaImmagine(Context context, int Quanto) {
		// String Origine = Environment.getExternalStorageDirectory().getAbsolutePath();
		// String Cartella = VariabiliStatiche.getInstance().PathApplicazione;
		// String Cartella = UtilityDetector.getInstance().PrendePath(context);
		String Path = UtilityDetector.getInstance().PrendePath(context);
		String NomeImmagine = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().numMultimedia);

		boolean OkEXIF=false;
		String artista = "";
		String model = "";
		String lat = "";
		String latRef = "";
		String lon = "";
		String lonref = "";

		try {
			ExifInterface exif = new ExifInterface(Path + NomeImmagine);
			artista = exif.getAttribute(ExifInterface.TAG_ARTIST);
			model = exif.getAttribute(ExifInterface.TAG_MODEL);
			lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			latRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
			lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			lonref = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

			OkEXIF = true;
		} catch (IOException ignored) {
			OkEXIF = false;
		}

		Bitmap Immagine = BitmapFactory.decodeFile(Path+NomeImmagine); // getPreview(Origine+Cartella+NomeImmagine);
		Matrix matrix = new Matrix();
		matrix.postRotate(Quanto);

		Bitmap rotated = Bitmap.createBitmap(
				Immagine,
				0,
				0,
				Immagine.getWidth(),
				Immagine.getHeight(),
				matrix,
				true);

		saveBitmap(rotated, Path, NomeImmagine);

		if (OkEXIF) {
			try {
				ExifInterface exif = new ExifInterface(Path + NomeImmagine);
				exif.setAttribute(ExifInterface.TAG_ARTIST, artista);
				exif.setAttribute(ExifInterface.TAG_MODEL, model);
				exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
				exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latRef);
				exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, lon);
				exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lonref);
				exif.saveAttributes();
			} catch (IOException ignored) {

			}
		}

		UtilityDetector.getInstance().VisualizzaMultimedia(context);
	}

	public void PrendeVolto(Context context) {
		UtilityWallpaper.getInstance().Attesa(true);

		String Path = UtilityDetector.getInstance().PrendePath(context);
		String NomeImmagine = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().numMultimedia);
		String NomeDestinazione = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().numMultimedia);
		String[] c = NomeDestinazione.split("\\.");
		if (c.length > 0) {
			Calendar calendar = Calendar.getInstance();
			int s = calendar.get(Calendar.SECOND);
			NomeDestinazione = c[0] + "_" + s + "." + c[1];
		}

		VariabiliStaticheWallpaper.getInstance().setStaPrendendoVolto(true);

		RilevamentoVolti rv = new RilevamentoVolti(context);
		rv.ElaboraImmagine(Path + NomeImmagine);

		Handler handler1 = new Handler(Looper.getMainLooper());

		String finalPath = Path + NomeImmagine;
		String finalNomeDestinazione = NomeDestinazione;

		Runnable r1 = new Runnable() {
			public void run() {
				if (!VariabiliStaticheWallpaper.getInstance().isStaPrendendoVolto()) {
					handler1.removeCallbacks(this);

					List<Rect> r = VariabiliStaticheWallpaper.getInstance().getQuadratiFaccia();

					Bitmap bmpAppoggio = null;

					if (r != null) {
						Bitmap bitmap = BitmapFactory.decodeFile(finalPath);
						int larghezzaImmagine = bitmap.getWidth();
						int altezzaImmagine = bitmap.getHeight();

						int inizioVisoX = 9999;
						int inizioVisoY = 9999;
						int larghezzaViso = -9999;
						// int altezzaViso = -9999;

						for (Rect r1 : r) {
							if (r1.left < inizioVisoX) { inizioVisoX = r1.left; }
							if (r1.top < inizioVisoY) { inizioVisoY = r1.top; }
							if (r1.right > larghezzaViso) { larghezzaViso = r1.right; }
							// if (r1.bottom > altezzaViso) { altezzaViso = r1.bottom; }
						}

						inizioVisoY -= (int) (altezzaImmagine * VariabiliStaticheWallpaper.percAumentoY);
						if (inizioVisoY < 0) { inizioVisoY = 0; }
						inizioVisoX -= (int) (larghezzaImmagine * VariabiliStaticheWallpaper.percAumentoX);
						if (inizioVisoX < 0) { inizioVisoX = 0; }

						/* larghezzaViso += (int) (larghezzaImmagine * VariabiliStaticheWallpaper.percAumentoX);
						if (larghezzaViso + inizioVisoX > larghezzaImmagine) {
							larghezzaViso = larghezzaImmagine - inizioVisoX;
						}
						altezzaViso += (int) (altezzaImmagine * VariabiliStaticheWallpaper.percAumentoY);
						if (altezzaViso + inizioVisoY > altezzaImmagine) {
							altezzaViso = altezzaImmagine - inizioVisoY;
						} */

						try {
							bmpAppoggio = Bitmap.createBitmap(
									bitmap,
									inizioVisoX,
									inizioVisoY,
									larghezzaViso,
									altezzaImmagine - inizioVisoY
							);

							FileOutputStream out = new FileOutputStream(Path + finalNomeDestinazione);
							bmpAppoggio.compress(CompressFormat.JPEG, 100, out);

							UtilityDetector.getInstance().CaricaMultimedia(context);
							UtilityDetector.getInstance().VisualizzaMultimedia(context);
						} catch (IOException e) {
							UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
									"Cambio immagine. IO Exception: " +
											UtilityDetector.getInstance().PrendeErroreDaException(e));
						} catch (Exception e) {
							UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
									"Cambio immagine. Errore conversione: " +
											UtilityDetector.getInstance().PrendeErroreDaException(e));
						}
					}

					UtilityWallpaper.getInstance().Attesa(false);
				} else {
					handler1.postDelayed(this, 1000);
				}
			}
		};
		handler1.postDelayed(r1, 1000);
	}

	public void FlipImmagine(Context context, boolean Orizzontale) {
		// String Origine= Environment.getExternalStorageDirectory().getAbsolutePath();
		// String Cartella=VariabiliStatiche.getInstance().PathApplicazione;
		// String Cartella = UtilityDetector.getInstance().PrendePath(context);
		String Path = UtilityDetector.getInstance().PrendePath(context);
		String NomeImmagine = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().numMultimedia);

		boolean OkEXIF = false;
		String artista = "";
		String model = "";
		String lat = "";
		String latRef = "";
		String lon = "";
		String lonref = "";

		try {
			ExifInterface exif = new ExifInterface(Path + NomeImmagine);
			artista = exif.getAttribute(ExifInterface.TAG_ARTIST);
			model = exif.getAttribute(ExifInterface.TAG_MODEL);
			lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			latRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
			lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			lonref = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

			OkEXIF = true;
		} catch (IOException ignored) {
			OkEXIF = false;
		}

		Bitmap Immagine = BitmapFactory.decodeFile(Path + NomeImmagine); // getPreview(Origine+Cartella+NomeImmagine);
		Matrix matrix = new Matrix();
		int cx = Immagine.getWidth()/2;
		int cy = Immagine.getHeight()/2;
		if (Orizzontale) {
			matrix.postScale(-1, 1, cx, cy);
		} else {
			matrix.postScale(1, -1, cx, cy);
		}

		Bitmap rotated = Bitmap.createBitmap(Immagine, 0, 0, Immagine.getWidth(), Immagine.getHeight(),
				matrix, true);

		saveBitmap(rotated, Path, NomeImmagine);

		if (OkEXIF) {
			try {
				ExifInterface exif = new ExifInterface(Path + NomeImmagine);
				exif.setAttribute(ExifInterface.TAG_ARTIST, artista);
				exif.setAttribute(ExifInterface.TAG_MODEL, model);
				exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lat);
				exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, latRef);
				exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, lon);
				exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lonref);
				exif.saveAttributes();
			} catch (IOException ignored) {

			}
		}

		UtilityDetector.getInstance().VisualizzaMultimedia(context);
	}

	private void saveBitmap(Bitmap bm, String Percorso, String Nome)  {
		if (bm!=null) {
			OutputStream fOut = null;
			Uri outputFileUri;
			try {
				File root = new File(Percorso);
				File sdImageMainDirectory = new File(root, Nome);
				sdImageMainDirectory.delete();
				outputFileUri = Uri.fromFile(sdImageMainDirectory);
				fOut = new FileOutputStream(sdImageMainDirectory);
				bm.compress(CompressFormat.JPEG, 90, fOut);
				fOut.flush();
				fOut.close();
			} catch (Exception ignored) {
			}
		}
	}

	public void CambiaContrastoLuminosita(Context context, float contrast, float brightness)
	{
		// String Origine= Environment.getExternalStorageDirectory().getAbsolutePath();
		// String Cartella=VariabiliStatiche.getInstance().PathApplicazione;
		// String Cartella = UtilityDetector.getInstance().PrendePath(context);
		String Path = UtilityDetector.getInstance().PrendePath(context);
		String NomeImmagine = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().numMultimedia);

		Bitmap Immagine = BitmapFactory.decodeFile(Path+ NomeImmagine);
		ColorMatrix cm = new ColorMatrix(new float[]
				{
						contrast, 0, 0, 0, brightness,
						0, contrast, 0, 0, brightness,
						0, 0, contrast, 0, brightness,
						0, 0, 0, 1, 0
				});

		Bitmap ret = Bitmap.createBitmap(Immagine.getWidth(), Immagine.getHeight(), Immagine.getConfig());

		Canvas canvas = new Canvas(ret);

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cm));
		canvas.drawBitmap(Immagine, 0, 0, paint);

		saveBitmap(ret, Path, NomeImmagine);

		UtilityDetector.getInstance().VisualizzaMultimedia(context);
	}
}
