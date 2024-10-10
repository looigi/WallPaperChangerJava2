package com.looigi.wallpaperchanger2.classeModificaImmagine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;

import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiWallpaper.RilevamentoVolti;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

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

		boolean OkEXIF = false;
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

	public Bitmap RuotaImmagine(Context context, Bitmap Immagine, int Quanto) {
		// String Origine = Environment.getExternalStorageDirectory().getAbsolutePath();
		// String Cartella = VariabiliStatiche.getInstance().PathApplicazione;
		// String Cartella = UtilityDetector.getInstance().PrendePath(context);
		/* String Path = UtilityDetector.getInstance().PrendePath(context);
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
		} */

		// Bitmap Immagine = BitmapFactory.decodeFile(Path+NomeImmagine); // getPreview(Origine+Cartella+NomeImmagine);
		Matrix matrix = new Matrix();
		matrix.postRotate(Quanto);

		Bitmap rit = Bitmap.createBitmap(
				Immagine,
				0,
				0,
				Immagine.getWidth(),
				Immagine.getHeight(),
				matrix,
				true);

		return rit;
		// saveBitmap(rotated, Path, NomeImmagine);

		/* if (OkEXIF) {
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
		} */

		// UtilityDetector.getInstance().VisualizzaMultimedia(context);
	}

	public void PrendeVoltoDaBitmap(Context context, Bitmap bitmap, modificaImmagine mI) {
		UtilityWallpaper.getInstance().Attesa(true);

		VariabiliStaticheWallpaper.getInstance().setStaPrendendoVolto(true);

		RilevamentoVolti rv = new RilevamentoVolti(context);
		rv.ElaboraImmagineDaBitmap(bitmap);

		Handler handler1 = new Handler(Looper.getMainLooper());

		Runnable r1 = new Runnable() {
			public void run() {
				if (!VariabiliStaticheWallpaper.getInstance().isStaPrendendoVolto()) {
					handler1.removeCallbacks(this);

					List<Rect> r = VariabiliStaticheWallpaper.getInstance().getQuadratiFaccia();

					Bitmap bmpAppoggio = null;

					if (r != null) {
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

							mI.ImpostaBitmap(bmpAppoggio);
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

	/*
	public void PrendeVoltoDaPath(Context context) {
		boolean Criptata = false;

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
		rv.ElaboraImmagineDaPath(Path + NomeImmagine);

		Handler handler1 = new Handler(Looper.getMainLooper());

		String finalPath = Path + NomeImmagine;
		String finalNomeDestinazione = NomeDestinazione;
		boolean finalCriptata = Criptata;

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
						} * /

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

							if (finalCriptata) {
								UtilityDetector.getInstance().addKeyToFile(Path, finalNomeDestinazione);
							}

							UtilityDetector.getInstance().CaricaMultimedia(context);
							UtilityDetector.getInstance().VisualizzaMultimedia(context);
						} catch (IOException e) {
							if (finalCriptata) {
								UtilityWallpaper.getInstance().EliminaFileUnico(Path + finalNomeDestinazione);
							}

							UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
									"Cambio immagine. IO Exception: " +
											UtilityDetector.getInstance().PrendeErroreDaException(e));
						} catch (Exception e) {
							if (finalCriptata) {
								UtilityWallpaper.getInstance().EliminaFileUnico(Path + finalNomeDestinazione);
							}

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
	*/

	public Bitmap FlipImmagine(Bitmap Immagine, boolean Orizzontale) {
		// String Origine= Environment.getExternalStorageDirectory().getAbsolutePath();
		// String Cartella=VariabiliStatiche.getInstance().PathApplicazione;
		// String Cartella = UtilityDetector.getInstance().PrendePath(context);
		/* String Path = UtilityDetector.getInstance().PrendePath(context);
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
		} */

		// Bitmap Immagine = BitmapFactory.decodeFile(Path + NomeImmagine); // getPreview(Origine+Cartella+NomeImmagine);
		Matrix matrix = new Matrix();
		int cx = Immagine.getWidth()/2;
		int cy = Immagine.getHeight()/2;
		if (Orizzontale) {
			matrix.postScale(-1, 1, cx, cy);
		} else {
			matrix.postScale(1, -1, cx, cy);
		}

		return Bitmap.createBitmap(Immagine, 0, 0, Immagine.getWidth(), Immagine.getHeight(),
				matrix, true);

		/* saveBitmap(rotated, Path, NomeImmagine);

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
		} */

		// UtilityDetector.getInstance().VisualizzaMultimedia(context);
	}

	public void saveBitmap(Bitmap bm, String Percorso, String Nome)  {
		if (bm!=null) {
			OutputStream fOut = null;
			Uri outputFileUri;
			try {
				File root = new File(Percorso);
				File sdImageMainDirectory = new File(root, Nome);
				sdImageMainDirectory.delete();
				outputFileUri = Uri.fromFile(sdImageMainDirectory);
				fOut = new FileOutputStream(sdImageMainDirectory);
				bm.compress(CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (Exception ignored) {
			}
		}
	}

	public Bitmap Resize(Bitmap imaged, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
			int width = imaged.getWidth();
			int height = imaged.getHeight();
			float ratioBitmap = (float) width / (float) height;
			float ratioMax = (float) maxWidth / (float) maxHeight;
			int finalWidth = maxWidth;
			int finalHeight = maxHeight;
			if (ratioMax > 1) {
				finalWidth = Math.round(((float) maxHeight * ratioBitmap));
			} else {
				finalHeight = Math.round(((float) maxWidth / ratioBitmap));
			}
			Bitmap b = Bitmap.createScaledBitmap(imaged, finalWidth, finalHeight, false);

			return b;
		} else {
			return imaged;
		}
	}

	public Bitmap CambiaContrastoLuminosita(Bitmap Immagine, float contrast, float brightness)
	{
		if (Immagine != null) {
			ColorMatrix cm = new ColorMatrix(new float[]
					{
							contrast, 0, 0, 0, brightness,
							0, contrast, 0, 0, brightness,
							0, 0, contrast, 0, brightness,
							0, 0, 0, 1, 0
					});
			Bitmap mEnhancedBitmap = Bitmap.createBitmap(
					Immagine.getWidth(),
					Immagine.getHeight(),
					Immagine.getConfig());
			Canvas canvas = new Canvas(mEnhancedBitmap);
			Paint paint = new Paint();
			paint.setColorFilter(new ColorMatrixColorFilter(cm));
			canvas.drawBitmap(Immagine, 0, 0, paint);

			return mEnhancedBitmap;
		} else {
			return null;
		}
	}

	public Bitmap ConverteBN(Bitmap bitmap) {
		return  BitmapBuilder.toGrayscale(
				bitmap,
				0.2F
		);
	}

	public Bitmap ConvertSephia(Bitmap bmpOriginal)
	{
		return BitmapBuilder.toSepia(bmpOriginal);
	}

	public Bitmap ConvertReflection(Bitmap bmpOriginal)
	{
		return BitmapBuilder.reflectionEffect(
				bmpOriginal,
				5
		);
	}

	public Bitmap ConvertSketch(Bitmap bmpOriginal)
	{
		Context ctx = VariabiliStaticheStart.getInstance().getContext();
		if (ctx != null) {
			return BitmapBuilder.sketchEffect(
					ctx,
					bmpOriginal
			);
		} else {
			return bmpOriginal;
		}
	}

	public Bitmap AddGlow(Bitmap src) {
		final int random2 = new Random().nextInt(5) + 1;
		int colore = 0;

		switch (random2) {
			case 1:
				colore = Color.BLUE;
				break;
			case 2:
				colore = Color.WHITE;
				break;
			case 3:
				colore = Color.GREEN;
				break;
			case 4:
				colore = Color.RED;
				break;
			case 5:
				colore = Color.YELLOW;
				break;
		}

		final int dimensione = new Random().nextInt(60) + 25;

		return BitmapBuilder.glowEffect(src, dimensione, colore);
	}

	public Bitmap doSharpen(Context context, Bitmap original) {
		float[] sharp = { -0.60f, -0.60f, -0.60f, -0.60f, 5.81f, -0.60f,
				-0.60f, -0.60f, -0.60f };

		Bitmap bitmap = Bitmap.createBitmap(
				original.getWidth(), original.getHeight(),
				Bitmap.Config.ARGB_8888);

		RenderScript rs = RenderScript.create(context);

		Allocation allocIn = Allocation.createFromBitmap(rs, original);
		Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

		ScriptIntrinsicConvolve3x3 convolution
				= ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
		convolution.setInput(allocIn);
		convolution.setCoefficients(sharp);
		convolution.forEach(allocOut);

		allocOut.copyTo(bitmap);
		rs.destroy();

		return bitmap;

	}
}
