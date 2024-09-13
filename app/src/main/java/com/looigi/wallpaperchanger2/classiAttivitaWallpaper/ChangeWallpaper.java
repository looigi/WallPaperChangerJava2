package com.looigi.wallpaperchanger2.classiAttivitaWallpaper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;

import com.looigi.wallpaperchanger2.MainActivity;
import com.looigi.wallpaperchanger2.classiStandard.GestioneNotifiche;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;
import com.looigi.wallpaperchanger2.webservice.ChiamateWS;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeWallpaper {
	private static final String NomeMaschera = "CHANGEWALLPAPER";
	private final int BordoX = 10;
	private final int BordoY = 10;
	private int SchermoX;
	private int SchermoY;

	public ChangeWallpaper(Context context) {
		Utility.getInstance().Attesa(true);

		DisplayMetrics metrics = new DisplayMetrics();
		if (VariabiliStaticheServizio.getInstance().getMainActivity() != null) {
			VariabiliStaticheServizio.getInstance().getMainActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

			SchermoX = metrics.widthPixels; //  * 70 / 100;
			SchermoY = metrics.heightPixels;

			Utility.getInstance().ScriveLog(context, NomeMaschera, "Cambio immagine instanziato. Dimensioni schermo: " +
					SchermoX + "x" + SchermoY);
		} else {
			SchermoX = -1;
			SchermoY = -1;
			Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE su Cambio immagine: Act nulla. Riavvio applicazione");

			if (context != null) {
				Intent mStartActivity = new Intent(context, MainActivity.class);
				mStartActivity.putExtra("CAMBIAWALLPAPER", "SI");
				int mPendingIntentId = 123351;
				PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
				AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
				System.exit(0);
			} else {
				Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE su riavvio: Context nullo");
			}
		}

		Utility.getInstance().Attesa(false);
	}

	public Boolean setWallpaper(Context context, StrutturaImmagine src) {
		Utility.getInstance().Attesa(true);

		if (SchermoX == -1) {
			Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE su set wallpaper: dimensioni schermo non impostate");
			return false;
		} else {
			if (!VariabiliStaticheServizio.getInstance().isOffline()) {
				Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine online");

				ChiamateWS c = new ChiamateWS(context);
				c.TornaProssimaImmagine();
			} else {
				setWallpaperLocale(context, src);
			}
		}

		Utility.getInstance().Attesa(false);

		return true;
	}

	public Boolean setWallpaperLocale(Context context, StrutturaImmagine src) {
		boolean Ritorno = true;
		Utility.getInstance().Attesa(true);

		if (SchermoX == -1) {
			Utility.getInstance().Attesa(false);
			Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE su set wallpaper locale: dimensioni schermo non impostate");
			Ritorno = false;
		} else {
			Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Caricamento bitmap.");

			Bitmap setWallToDevice = PrendeImmagineReale(context, src);

			if (setWallToDevice != null) {
				Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Applicazione wallpaper.");

				WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
				try {
					Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Impostazione dimensioni " + setWallToDevice.getWidth() + "x" + setWallToDevice.getHeight());

					// HOME SCREEN
					if (VariabiliStaticheServizio.getInstance().isHome()) {
						wallpaperManager.setBitmap(
								setWallToDevice,
								null,
								true,
								WallpaperManager.FLAG_SYSTEM
						);
					}

					// LOCK SCREEN
					if (VariabiliStaticheServizio.getInstance().isLock()) {
						wallpaperManager.setBitmap(
								setWallToDevice,
								null,
								true,
								WallpaperManager.FLAG_LOCK);
					}

					// wallpaperManager.setWallpaperOffsetSteps(1, 1);
					// if (VariabiliGlobali.getInstance().getStretch().equals("S")) {
					// wallpaperManager.suggestDesiredDimensions(setWallToDevice.getWidth(), setWallToDevice.getHeight());
					// } else {
					// 	wallpaperManager.suggestDesiredDimensions(VariabiliGlobali.getInstance().getDimeWallWidthOriginale(), VariabiliGlobali.getInstance().getDimeWallHeightOriginale());
					// }

					Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Settata bitmap.");
				} catch (IOException e) {
					// Utility.getInstance().ScriveLog("Errore: " + u.PrendeErroreDaException(e));
					// e.printStackTrace();

					Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Errore " + Utility.getInstance().PrendeErroreDaException(e));

					// Toast.makeText(VariabiliGlobali.getInstance().getContext(),
					// 		u.PrendeErroreDaException(e),
					// 		Toast.LENGTH_LONG).show();

					Ritorno = false;
				}
			} else {
				Ritorno = false;
			}
		}
		Utility.getInstance().Attesa(false);

		return Ritorno;
	}

	private Bitmap PrendeImmagineReale(Context context, StrutturaImmagine si) {
		if (si == null) {
			return null;
		}

		Utility.getInstance().ScriveLog(context, NomeMaschera,"Prende immagine sistemata");

		// Bitmap myBitmap = null;

		boolean ok = true;
		String path = si.getPathImmagine();
		if (!Utility.getInstance().EsisteFile(path)) {
			path = context.getFilesDir() + "/Download/Appoggio.jpg";
			if (!Utility.getInstance().EsisteFile(path)) {
				ok = false;
			}
		}
		if (ok) {
			Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. File esistente: " + si.getPathImmagine());

			Bitmap myBitmap = null; // = BitmapFactory.decodeFile(si.getPathImmagine());
			try {
				// myBitmap = getPreview(si.getPathImmagine());

				myBitmap = BitmapFactory.decodeFile(path);
			} catch (Exception e) {
				Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. Errore preview");
			}

			if (myBitmap != null) {
				// if (VariabiliGlobali.getInstance().getStretch().equals("S")) {
					// Utility.getInstance().ScriveLog("Cambio immagine. Stretch = S");

					// if (VariabiliGlobali.getInstance().getModalitaVisua().equals("N")) {
			// 			Utility.getInstance().ScriveLog("Cambio immagine. Converte dimensioni");

				/* if (VariabiliGlobali.getInstance().isResize()) {
					myBitmap = ConverteDimensioni(myBitmap);
				} */

			 		// if (VariabiliGlobali.getInstance().isBlur()) {
						// if (myBitmap != null) {
							try {
								// Bitmap Immaginona = Bitmap.createBitmap(VariabiliGlobali.getInstance().getSchermoX(), VariabiliGlobali.getInstance().getSchermoY(), Bitmap.Config.ARGB_8888);
								// Canvas comboImage = new Canvas(Immaginona);
								// float Altezza=(((float) (VariabiliGlobali.getInstance().getSchermoY()))/2)-(myBitmap.getHeight()/2);
								// float Larghezza=(((float) (VariabiliGlobali.getInstance().getSchermoX()))/2)-(myBitmap.getWidth()/2);
								// comboImage.drawBitmap(myBitmap, Larghezza, Altezza, null);
								Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. Mette bordo a immagine");

								myBitmap = MetteBordoAImmagine(context, myBitmap, si);
							} catch (Exception e) {
								Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. Mette bordo a immagine. Errore: " + Utility.getInstance().PrendeErroreDaException(e));
								myBitmap = null;
							}
						// } else {
						// 	Utility.getInstance().ScriveLog("Cambio immagine. Converte dimensioni ha ritornato null");

						// 	myBitmap = null;
						// }
					// }
					/* } else {
						Utility.getInstance().ScriveLog("Cambio immagine. Create scaled bitmap");

						myBitmap = Bitmap.createScaledBitmap(myBitmap, (int) VariabiliGlobali.getInstance().getSchermoX() - BordoX,
								(int) VariabiliGlobali.getInstance().getSchermoY() - BordoY, true);
					} */
				// }
				// SalvaImmagine(myBitmap);
			} else {
				Utility.getInstance().ScriveLog(context, NomeMaschera,"Bitmap nulla");
			}

			Utility.getInstance().ScriveLog(context, NomeMaschera,"Aggiorno notifica");

			VariabiliStaticheServizio.getInstance().setUltimaImmagine(si);
			// Notifica.getInstance().setContext(VariabiliGlobali.getInstance().getContext());

			// Notifica.getInstance().setTitolo(si.getImmagine());
			// Notifica.getInstance().setImmagine(si.getPathImmagine());
			// GestioneNotifiche.getInstance().RimuoviNotifica();
			GestioneNotifiche.getInstance().AggiornaNotifica();

			Bitmap ultima = BitmapFactory.decodeFile(si.getPathImmagine());
			VariabiliStaticheServizio.getInstance().getImgImpostata().setImageBitmap(ultima);

			VariabiliStaticheServizio.getInstance().setSecondiPassati(0);

			int minuti = VariabiliStaticheServizio.getInstance().getMinutiAttesa();
			int quantiGiri = (minuti * 60) / VariabiliStaticheServizio.secondiDiAttesaContatore;

			VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
				public void run() {
					VariabiliStaticheServizio.getInstance().getTxtTempoAlCambio().setText(
							"Prossimo cambio: " +
							VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
							quantiGiri);
				}
			});

			// Notifica.getInstance().AggiornaNotifica();
			db_dati db = new db_dati(context);
			db.ScriveImpostazioni();

			// VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);

			return myBitmap;
		} else {
			Utility.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. Svuoto bitmap. File inesistente.");

			return null;
		}
	}
	
	private Bitmap getPreview(Context context, String uri) {
		Utility.getInstance().ScriveLog(context, NomeMaschera,"Get preview");

		try {
			File image = new File(uri);

			BitmapFactory.Options bounds = new BitmapFactory.Options();
			bounds.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(image.getPath(), bounds);
			if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
				return null;

			int originalSize = Math.max(bounds.outHeight, bounds.outWidth);

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = originalSize/(SchermoY+SchermoX);

			Utility.getInstance().ScriveLog(context, NomeMaschera,"Get preview fatto");

			return BitmapFactory.decodeFile(image.getPath(), opts);
		} catch (Exception ignored) {
			Utility.getInstance().ScriveLog(context, NomeMaschera,"Prende Preview errore ");
			return null;
		}
	}

	private Bitmap ConverteDimensioni(Context context, Bitmap b) {
		if (b!=null) {
			try {
				Utility.getInstance().ScriveLog(context, NomeMaschera,"Converte dimensioni 1");

				// Bitmap bb=b;
				int width = b.getWidth();
				int height = b.getHeight();

				float p1 = width;
				float p2 = height;
				// if (width > SchermoX || height > SchermoY) {
					p1 = (float) width / ((float) SchermoX);
					p2 = (float) height / ((float) SchermoY);
					float p;
					if (p1 > p2) {
						p = p1;
					} else {
						p = p2;
					}

					p1 = width / p;
					p2 = height / p;
				/* } else {
					p1 = width;
					p2 = height;
				} */
				Bitmap bb = Bitmap.createScaledBitmap(b, (int) p1, (int) p2, true);

				Utility.getInstance().ScriveLog(context, NomeMaschera,"Converte dimensioni 2");

				return bb;
			} catch (Exception ignored) {
				Utility.getInstance().ScriveLog(context, NomeMaschera,"Converte dimensioni errore: " + Utility.getInstance().PrendeErroreDaException(ignored));

				return null;
			}
		} else {
			Utility.getInstance().ScriveLog(context, NomeMaschera,"Converte dimensioni. Ritorno nullo");

			return null;
		}
	}

	private Bitmap MetteBordoAImmagine(Context context, Bitmap myBitmapPassata, StrutturaImmagine si) {
		Utility.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo a immagine");

		Bitmap myBitmap = ConverteDimensioni(context, myBitmapPassata);

		float dimeImmX = myBitmap.getWidth();
		float dimeImmY = myBitmap.getHeight();

		float posX = ((float)SchermoX / 2) - (dimeImmX / 2);
		float posY = ((float)SchermoY / 2) - (dimeImmY / 2);

		Utility.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo 1");

		// ImmagineX = SchermoX-ImmagineX;
		// ImmagineX = SchermoX;
		// ImmagineY = (SchermoY-ImmagineY)/2;

		// ImmagineX = 500;
		// ImmagineY = 100;

		Bitmap myOutputBitmap = myBitmap.copy(myBitmap.getConfig(), true);
		Bitmap immagineDiSfondo = null;
		try {
			Utility.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo 2");

			RenderScript renderScript = RenderScript.create(context);
			Allocation blurInput = Allocation.createFromBitmap(renderScript, myBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
			Allocation blurOutput = Allocation.createFromBitmap(renderScript, myOutputBitmap);
			ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
					Element.U8_4(renderScript));
			blur.setInput(blurInput);
			blur.setRadius(25); // radius must be 0 < r <= 25
			blur.forEach(blurOutput);
			blurOutput.copyTo(myOutputBitmap);
			renderScript.destroy();

			immagineDiSfondo = Bitmap.createScaledBitmap(myOutputBitmap, SchermoX, SchermoY, true);

			/* try (FileOutputStream out = new FileOutputStream(VariabiliGlobali.getInstance().getPercorsoDIR() + "/WFREGNA.png")) {
				immagineDiSfondo.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
				// PNG is a lossless format, the compression factor (100) is ignored
			} catch (IOException e) {
				e.printStackTrace();
			} */
		} catch (Exception e) {
			Utility.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo: " + Utility.getInstance().PrendeErroreDaException(e));
			int a = 0;
		}

		// int offset = 50;
		// int divisore = 2;

		Utility.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo 2");

		// Bitmap Immaginona = Bitmap.createBitmap((SharedObjects.getInstance().getSchermoX()) + offset * 2,
// 					SharedObjects.getInstance().getSchermoY() + offset * 2, Bitmap.Config.ARGB_8888);
		Bitmap Immaginona = Bitmap.createBitmap(SchermoX, SchermoY, Bitmap.Config.ARGB_8888);

		Utility.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo 3");

		Canvas canvas1 = new Canvas(Immaginona);
		/* if (posY > 0) {
			try {
				int posizY = (posY / divisore);
				Bitmap croppedSuperiore = Bitmap.createBitmap(myOutputBitmap, 0, 0, SchermoX, posizY);
				Bitmap resizedBitmap = Bitmap.createScaledBitmap(croppedSuperiore, SchermoX, posY, false);
				canvas1.drawBitmap(resizedBitmap, 0, 0, null);
			} catch (Exception ignored) {
				Utility.getInstance().ScriveLog("Mette bordo 3: Errore " + Utility.getInstance().PrendeErroreDaException(ignored));
			}
		}

		canvas1.drawBitmap(myBitmap, posX, posY, null);

		Utility.getInstance().ScriveLog("Mette bordo 4");

		if (posY > 0) {
			try {
				Bitmap croppedInferiore = Bitmap.createBitmap(myOutputBitmap, 0, myBitmap.getHeight() - (posY / divisore), SchermoX, posY / divisore);
				Bitmap resizedBitmap = Bitmap.createScaledBitmap(croppedInferiore, SchermoX, posY, false);
				canvas1.drawBitmap(resizedBitmap, 0, myBitmap.getHeight() + posY + 1, null);
			} catch (Exception ignored) {
				Utility.getInstance().ScriveLog("Mette bordo 4: Errore " + Utility.getInstance().PrendeErroreDaException(ignored));
			}
		}

		Utility.getInstance().ScriveLog("Mette bordo uscita"); */
		if(VariabiliStaticheServizio.getInstance().isBlur()) {
			canvas1.drawBitmap(immagineDiSfondo, 0, 0, null);
		}
		canvas1.drawBitmap(myBitmap, posX, posY, null);

		if (VariabiliStaticheServizio.getInstance().isScriveTestoSuImmagine()) {
			String Nome = si.getImmagine();
			String[] n = Nome.split("\\.");
			if (n.length > 0) {
				String estensione = n[n.length - 1];
				Nome = Nome.replace("." + estensione, "");
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String dateTime = "";
			try {
				Date date = new Date(si.getDataImmagine());
				dateTime = dateFormat.format(date);
			} catch (Exception e) {

			}

			int posizioneScrittaX = ((int) posX) + 85;
			int posizioneScrittaY = ((int) posY) - 95;
			if (posizioneScrittaY < 50) {
				posizioneScrittaY = 50;
			}
			int spostamento = 3;

			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setTextSize(35);
			canvas1.drawText(Nome, posizioneScrittaX + spostamento, posizioneScrittaY + spostamento, paint);
			int altezza = 35;
			if (dateTime != null && !dateTime.trim().isEmpty()) {
				canvas1.drawText("Data immagine: " + dateTime, posizioneScrittaX + spostamento, posizioneScrittaY + spostamento + altezza, paint);
				altezza = 70;
			}
			if (si.getDimensione() != null && !si.getDimensione().trim().isEmpty()) {
				canvas1.drawText("Dimensione immagine: " + si.getDimensione(), posizioneScrittaX + spostamento, posizioneScrittaY + spostamento + altezza, paint);
			}

			Paint paint2 = new Paint();
			paint2.setColor(Color.YELLOW);
			paint2.setTextSize(35);
			canvas1.drawText(Nome, posizioneScrittaX,  posizioneScrittaY, paint2);
			altezza = 35;
			if (dateTime != null && !dateTime.trim().isEmpty()) {
				canvas1.drawText("Data immagine: " + dateTime, posizioneScrittaX, posizioneScrittaY + altezza, paint2);
				altezza = 70;
			}
			if (si.getDimensione() != null && !si.getDimensione().trim().isEmpty()) {
				canvas1.drawText("Dimensione immagine: " + si.getDimensione(), posizioneScrittaX, posizioneScrittaY + altezza, paint2);
			}
		}

		return Immaginona;
	}
}
