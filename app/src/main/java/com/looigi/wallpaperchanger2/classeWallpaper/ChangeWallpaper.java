package com.looigi.wallpaperchanger2.classeWallpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeModificaImmagine.GestioneImmagini;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.ChiamateWsWP;
import com.looigi.wallpaperchanger2.utilities.RilevamentoVolti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChangeWallpaper {
	private static final String NomeMaschera = "Change_Wallpaper";
	private final int BordoX = 10;
	private final int BordoY = 10;
	private int SchermoX;
	private int SchermoY;

	public ChangeWallpaper(Context context, String daDove, StrutturaImmagine UltimaImmagine) {
		UtilityWallpaper.getInstance().Attesa(true);
		VariabiliStaticheWallpaper.getInstance().setImmagineImpostataDaChi(daDove);

		Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
		if (act != null) {
			PrendeDimensioniSchermo(context);
		} else {
			SchermoX = -1;
			SchermoY = -1;
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
					"ERRORE su Cambio immagine: Act nulla. Riavvio applicazione");

			/* if (context != null) {
				Intent mStartActivity = new Intent(context, MainWallpaper.class);
				mStartActivity.putExtra("CAMBIAWALLPAPER", "SI");
				int mPendingIntentId = 123351;
				PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
				AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
				System.exit(0);
			} else {
				Utility.getInstance().ScriveLog(context, NomeMaschera,"ERRORE su riavvio: Context nullo");
			} */
		}

		UtilityWallpaper.getInstance().Attesa(false);
	}

	private void PrendeDimensioniSchermo(Context context) {
		// DisplayMetrics metrics = new DisplayMetrics();
		Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
		if (act != null) {
			Display d = act.getDisplay();

			SchermoX = d.getWidth(); //  * 70 / 100;
			SchermoY = d.getHeight();

			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio immagine instanziato. Dimensioni schermo: " +
					SchermoX + "x" + SchermoY);
		} else {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio immagine instanziato. Dimensioni schermo non prese. Act nulla");
		}
	}

	public void setWallpaper(Context context, StrutturaImmagine src) {
		UtilityWallpaper.getInstance().Attesa(true);

		if (SchermoX == -1) {
			PrendeDimensioniSchermo(context);
		}

		if (SchermoX == -1) {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"ERRORE su set wallpaper: dimensioni schermo non impostate");
			// return false;
		} else {
			switch (VariabiliStaticheWallpaper.getInstance().getModoRicercaImmagine()) {
				case 0:
					UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine online");

					ChiamateWsWP c = new ChiamateWsWP(context);
					c.TornaProssimaImmagine();
					break;
				case 1:
					UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine offline");

					setWallpaperLocale(context, src);
					break;
				case 2:
					UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine da immagini");

					ChiamateWSMI ws = new ChiamateWSMI(context);
					ws.RitornaProssimaImmaginePerWP(
							VariabiliStaticheWallpaper.getInstance().getFiltro()
					);
					break;
			}
		}

		UtilityWallpaper.getInstance().Attesa(false);

		// return true;
	}

	private int exifToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
		return 0;
	}

	private Bitmap RuotaImmagine(Context context, String path, Bitmap bitmap) {
		File curFile = new File(path); // ... This is an image file from my device.
		Bitmap rotatedBitmap;

		try {
			ExifInterface exif = new ExifInterface(curFile.getPath());
			int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int rotationInDegrees = exifToDegrees(rotation);
			Matrix matrix = new Matrix();
			if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}
			rotatedBitmap = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			return rotatedBitmap;
		} catch(IOException ex){
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
					"Cambio immagine. Errore rotazione: " +
					UtilityWallpaper.getInstance().PrendeErroreDaException(ex));
			return bitmap;
		}
	}

	public void setWallpaperLocale(Context context, StrutturaImmagine src) {
		UtilityWallpaper.getInstance().Attesa(true);

		if (SchermoX == -1) {
			PrendeDimensioniSchermo(context);
		}

		if (SchermoX == -1) {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"ERRORE su set wallpaper locale: dimensioni schermo non impostate");

			UtilityWallpaper.getInstance().Attesa(false);
		} else {
			boolean ok = true;

			String path = src.getPathImmagine();
			if (!UtilityWallpaper.getInstance().EsisteFile(path)) {
				path = context.getFilesDir() + "/Download/Appoggio.jpg";
				if (!UtilityWallpaper.getInstance().EsisteFile(path)) {
					ok = false;
				}
			}
			if (ok) {
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				bitmap = RuotaImmagine(context, path, bitmap);
				if (bitmap != null) {
					boolean soloVolto = false;

					if (VariabiliStaticheWallpaper.getInstance().isEffetti()) {
						final int random1 = new Random().nextInt(3) + 1;

						if (random1 != 2) {
							soloVolto = true;
						}
					} else {
						soloVolto = VariabiliStaticheWallpaper.getInstance().isEspansa();
					}

					if (!soloVolto) {
						// Cambio immagine non espansa
						UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio immagine. Mette bordo a immagine");

						if (VariabiliStaticheWallpaper.getInstance().isEffetti()) {
							bitmap = applicaEffetti(bitmap);
						}

						bitmap = MetteBordoAImmagine(context, bitmap, src);

						setWallpaperLocaleEsegue(context, bitmap);

						faseFinale(context, src);
					} else {
						try {
							VariabiliStaticheWallpaper.getInstance().setStaPrendendoVolto(true);

							RilevamentoVolti rv = new RilevamentoVolti(context);
							rv.ElaboraImmagineDaPath(path);

							Handler handler1 = new Handler(Looper.getMainLooper());

							String finalPath = path;
							Bitmap finalBitmap = bitmap;

							Runnable r1 = new Runnable() {
								public void run() {
									if (!VariabiliStaticheWallpaper.getInstance().isStaPrendendoVolto()) {
										handler1.removeCallbacksAndMessages(this);
										handler1.removeCallbacks(this);

										List<Rect> r = VariabiliStaticheWallpaper.getInstance().getQuadratiFaccia();

										Bitmap bmpAppoggio = null;

										if (r != null) {
											Bitmap bitmap = BitmapFactory.decodeFile(finalPath);

											int larghezzaImmagine = bitmap.getWidth();
											int altezzaImmagine = bitmap.getHeight();

										/* if (r == null || (SchermoX > bitmap.getWidth() && SchermoY > bitmap.getHeight())) {
											bmpAppoggio = MetteBordoAImmagine(context, bitmap, src);
										} else { */

											int inizioVisoX = 9999;
											int inizioVisoY = 9999;
											int larghezzaViso = -9999;
											int altezzaViso = -9999;

											for (Rect r1 : r) {
												if (r1.left < inizioVisoX) {
													inizioVisoX = r1.left;
												}
												if (r1.top < inizioVisoY) {
													inizioVisoY = r1.top;
												}
												if (r1.right - r1.left > larghezzaViso) {
													larghezzaViso = r1.right - r1.left;
												}
												if (r1.bottom - r1.top > altezzaViso) {
													altezzaViso = r1.bottom - r1.top;
												}
											}

											/* Rect r1 = new Rect();
											r1.set(inizioVisoX, inizioVisoY, larghezzaViso, altezzaViso);
											bitmap = disegnaRettangolo(bitmap, r1, Color.WHITE); */

											boolean voltoPrimoPiano = false;
											if (VariabiliStaticheWallpaper.getInstance().isSoloVolti()) {
												voltoPrimoPiano = true;
											} else {
												final int random1 = new Random().nextInt(2) + 1;
												if (random1 == 1) {
													voltoPrimoPiano = true;
												}
											}

											if (voltoPrimoPiano) {
												inizioVisoY -= (int) (altezzaImmagine * VariabiliStaticheWallpaper.percAumentoY);
												if (inizioVisoY < 0) {
													inizioVisoY = 0;
												}
											} else {
												inizioVisoY = 0;
											}

											inizioVisoX -= (int) (larghezzaImmagine * VariabiliStaticheWallpaper.percAumentoX);
											if (inizioVisoX < 0) {
												inizioVisoX = 0;
											}

											larghezzaViso += (int) (larghezzaImmagine * (VariabiliStaticheWallpaper.percAumentoX * 2)) ;
											if (larghezzaViso + inizioVisoX > larghezzaImmagine) {
												larghezzaViso = larghezzaImmagine - inizioVisoX;
											}

											if (voltoPrimoPiano) {
												altezzaViso += (int) (altezzaImmagine * VariabiliStaticheWallpaper.percAumentoY);
												if (altezzaViso + inizioVisoY > altezzaImmagine) {
													altezzaViso = altezzaImmagine - inizioVisoY;
												}
											} else {
												altezzaViso = (altezzaImmagine + inizioVisoY) - 1;
											}

											/*Rect r2 = new Rect();
											r2.set(inizioVisoX, inizioVisoY, larghezzaViso, altezzaViso);
											bitmap = disegnaRettangolo(bitmap, r2, Color.RED); */

											try {
												bmpAppoggio = Bitmap.createBitmap(
														bitmap,
														inizioVisoX,
														inizioVisoY,
														larghezzaViso,
														altezzaViso
												);

												/* bmpAppoggio = Bitmap.createBitmap(
														bitmap,
														inizioVisoX,
														inizioVisoY,
														larghezzaImmagine - inizioVisoX,
														altezzaImmagine - inizioVisoY
												);

												bmpAppoggio = Bitmap.createScaledBitmap(
														bmpAppoggio,
														SchermoX,
														SchermoY,
														true
												); */
											} catch (Exception e) {
												UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
														"Cambio immagine. Errore conversione: " +
																UtilityDetector.getInstance().PrendeErroreDaException(e));

												bmpAppoggio = finalBitmap;
											}
										} else {
											bmpAppoggio = finalBitmap;
										}

										if (VariabiliStaticheWallpaper.getInstance().isEffetti()) {
											bmpAppoggio = applicaEffetti(bmpAppoggio);
										}

										bmpAppoggio = MetteBordoAImmagine(context, bmpAppoggio, src);

										setWallpaperLocaleEsegue(context, bmpAppoggio);

										faseFinale(context, src);
										// }
										UtilityWallpaper.getInstance().Attesa(false);
									} else {
										handler1.postDelayed(this, 1000);
									}
								}
							};
							handler1.postDelayed(r1, 1000);
						} catch (Exception e) {
							UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio immagine. Errore preview");
							UtilityWallpaper.getInstance().Attesa(false);
						}
					}
				} else {
					UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio immagine. Errore rotazione");
					UtilityWallpaper.getInstance().Attesa(false);
				}
			}
		}
	}

	private Bitmap disegnaRettangolo(Bitmap b, Rect r, int Colore) {
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setStyle(Paint.Style.STROKE);
		p.setColor(Colore);

		Bitmap tBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Bitmap.Config.RGB_565);
		Canvas tCanvas = new Canvas(tBitmap);
		tCanvas.drawBitmap(b, 0, 0, null);
		tCanvas.drawRoundRect(r.left, r.top, r.right, r.bottom,10 ,10 , p);

		return tBitmap;
	}

	private void faseFinale(Context context, StrutturaImmagine si) {
		UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Aggiorno notifica");

		VariabiliStaticheWallpaper.getInstance().setUltimaImmagine(si);
		// Notifica.getInstance().setContext(VariabiliGlobali.getInstance().getContext());

		// Notifica.getInstance().setTitolo(si.getImmagine());
		// Notifica.getInstance().setImmagine(si.getPathImmagine());
		// GestioneNotifiche.getInstance().RimuoviNotifica();
		GestioneNotificheWP.getInstance().AggiornaNotifica();

		Handler handlerTimer = new Handler(Looper.getMainLooper());
		Runnable rTimer = new Runnable() {
			public void run() {
				Bitmap ultima = BitmapFactory.decodeFile(si.getPathImmagine());
				if (VariabiliStaticheWallpaper.getInstance().getImgImpostata() != null) {
					VariabiliStaticheWallpaper.getInstance().getImgImpostata().setImageBitmap(ultima);
				}

				String path1 = context.getFilesDir() + "/Download/AppoggioImpostato.jpg";
				if (UtilityWallpaper.getInstance().EsisteFile(path1)) {
					Bitmap ultimaFinale = BitmapFactory.decodeFile(path1);
					VariabiliStaticheWallpaper.getInstance().getImgImpostataFinale().setImageBitmap(ultimaFinale);
				}

				VariabiliStaticheWallpaper.getInstance().setSecondiPassati(0);

				int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();
				int quantiGiri = (minuti * 60) / VariabiliStaticheWallpaper.secondiDiAttesaContatore;

				Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
				act.runOnUiThread(new Runnable() {
					public void run() {
						VariabiliStaticheWallpaper.getInstance().getTxtTempoAlCambio().setText(
								"Prossimo cambio: " +
										VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
										quantiGiri);
					}
				});

				// Notifica.getInstance().AggiornaNotifica();
				db_dati_wallpaper db = new db_dati_wallpaper(context);
				db.ScriveImpostazioni();
				db.ChiudeDB();
			}
		};
		handlerTimer.postDelayed(rTimer, 100);
	}

		private void setWallpaperLocaleEsegue(Context context, Bitmap bitmap) {
		// boolean Ritorno = true;
		UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Caricamento bitmap.");

		// Bitmap setWallToDevice = PrendeImmagineReale(context, Path);

		if (bitmap != null) {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Applicazione wallpaper.");

			GestioneImmagini g = new GestioneImmagini();
			String path1 = context.getFilesDir() + "/Download";
			UtilityWallpaper.getInstance().CreaCartelle(path1);
			UtilityWallpaper.getInstance().EliminaFileUnico(path1 + "/AppoggioImpostato.jpg");
			g.saveBitmap(bitmap, path1, "AppoggioImpostato.jpg");

			WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
			try {
				UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Impostazione dimensioni " + bitmap.getWidth() + "x" + bitmap.getHeight());

				// HOME SCREEN
				if (VariabiliStaticheWallpaper.getInstance().isHome()) {
					wallpaperManager.setBitmap(
							bitmap,
							null,
							false,
							WallpaperManager.FLAG_SYSTEM
					);
				}

				// LOCK SCREEN
				if (VariabiliStaticheWallpaper.getInstance().isLock()) {
					wallpaperManager.setBitmap(
							bitmap,
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

				UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Settata bitmap.");
			} catch (IOException e) {
				// Utility.getInstance().ScriveLog("Errore: " + u.PrendeErroreDaException(e));
				// e.printStackTrace();

				UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine: Errore " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));

				// Toast.makeText(VariabiliGlobali.getInstance().getContext(),
				// 		u.PrendeErroreDaException(e),
				// 		Toast.LENGTH_LONG).show();

				// Ritorno = false;
			}
		} else {
			// Ritorno = false;
		}

		UtilityWallpaper.getInstance().Attesa(false);

		// return Ritorno;
	}

	/* private int exifToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }

		return 0;
	}

	private Bitmap CheckRotazione(Bitmap bitmap, String path) {
		try {
			ExifInterface exif = new ExifInterface(path);
			int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			int rotationInDegrees = exifToDegrees(rotation);
			Matrix matrix = new Matrix();
			if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}

			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (IOException ex){
			return null;
		}
	} */

	private void wPrendeImmagineReale(Context context, Bitmap bitmap) {
		/* if (si == null) {
			return null;
		} * /

		// UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Prende immagine sistemata");

		// Bitmap myBitmap = null;

		/* boolean ok = true;
		String path = si.getPathImmagine();
		if (!UtilityWallpaper.getInstance().EsisteFile(path)) {
			path = context.getFilesDir() + "/Download/Appoggio.jpg";
			if (!UtilityWallpaper.getInstance().EsisteFile(path)) {
				ok = false;
			}
		}
		if (ok) {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. File esistente: " + si.getPathImmagine());

			Bitmap myBitmap = null; // = BitmapFactory.decodeFile(si.getPathImmagine());
			try {
				// myBitmap = getPreview(si.getPathImmagine());

				myBitmap = BitmapFactory.decodeFile(path);
			} catch (Exception e) {
				UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. Errore preview");
			}

			myBitmap = CheckRotazione(myBitmap);

			if (myBitmap != null) { * /
				// if (VariabiliGlobali.getInstance().getStretch().equals("S")) {
					// Utility.getInstance().ScriveLog("Cambio immagine. Stretch = S");

					// if (VariabiliGlobali.getInstance().getModalitaVisua().equals("N")) {
			// 			Utility.getInstance().ScriveLog("Cambio immagine. Converte dimensioni");

				/* if (VariabiliGlobali.getInstance().isResize()) {
					myBitmap = ConverteDimensioni(myBitmap);
				} * /

			 		// if (VariabiliGlobali.getInstance().isBlur()) {
						// if (myBitmap != null) {
							/* try {
								// Bitmap Immaginona = Bitmap.createBitmap(VariabiliGlobali.getInstance().getSchermoX(), VariabiliGlobali.getInstance().getSchermoY(), Bitmap.Config.ARGB_8888);
								// Canvas comboImage = new Canvas(Immaginona);
								// float Altezza=(((float) (VariabiliGlobali.getInstance().getSchermoY()))/2)-(myBitmap.getHeight()/2);
								// float Larghezza=(((float) (VariabiliGlobali.getInstance().getSchermoX()))/2)-(myBitmap.getWidth()/2);
								// comboImage.drawBitmap(myBitmap, Larghezza, Altezza, null);
								if (!VariabiliStaticheWallpaper.getInstance().isEspansa()) {
									UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. Mette bordo a immagine");

									myBitmap = MetteBordoAImmagine(context, myBitmap, si);
								} else {
									myBitmap = CentraImmagineTuttoSchermo(context, myBitmap);
								}
							} catch (Exception e) {
								UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. Mette bordo a immagine. Errore: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
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
					} * /
				// }
				// SalvaImmagine(myBitmap);
			/* } else {
				UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Bitmap nulla");
			} * /

			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Aggiorno notifica");

			VariabiliStaticheWallpaper.getInstance().setUltimaImmagine(si);
			// Notifica.getInstance().setContext(VariabiliGlobali.getInstance().getContext());

			// Notifica.getInstance().setTitolo(si.getImmagine());
			// Notifica.getInstance().setImmagine(si.getPathImmagine());
			// GestioneNotifiche.getInstance().RimuoviNotifica();
			GestioneNotifiche.getInstance().AggiornaNotifica();

			Bitmap ultima = BitmapFactory.decodeFile(si.getPathImmagine());
			VariabiliStaticheWallpaper.getInstance().getImgImpostata().setImageBitmap(ultima);

			VariabiliStaticheWallpaper.getInstance().setSecondiPassati(0);

			int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();
			int quantiGiri = (minuti * 60) / VariabiliStaticheWallpaper.secondiDiAttesaContatore;

			VariabiliStaticheWallpaper.getInstance().getMainActivity().runOnUiThread(new Runnable() {
				public void run() {
					VariabiliStaticheWallpaper.getInstance().getTxtTempoAlCambio().setText(
							"Prossimo cambio: " +
							VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
							quantiGiri);
				}
			});

			// Notifica.getInstance().AggiornaNotifica();
			db_dati_wallpaper db = new db_dati_wallpaper(context);
			db.ScriveImpostazioni();

			// VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);

			return myBitmap;
		} else {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Cambio immagine. Svuoto bitmap. File inesistente.");

			return null;
		} */
	}
	
	private Bitmap getPreview(Context context, String uri) {
		UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Get preview");

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

			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Get preview fatto");

			return BitmapFactory.decodeFile(image.getPath(), opts);
		} catch (Exception ignored) {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Prende Preview errore ");
			return null;
		}
	}

	private Bitmap ConverteDimensioni(Context context, Bitmap b) {
		if (b != null) {
			try {
				UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Converte dimensioni 1");

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

				UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Converte dimensioni 2");

				return bb;
			} catch (Exception ignored) {
				UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Converte dimensioni errore: " + UtilityWallpaper.getInstance().PrendeErroreDaException(ignored));

				return null;
			}
		} else {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Converte dimensioni. Ritorno nullo");

			return null;
		}
	}

	private Bitmap CentraImmagineTuttoSchermo(Context context, Bitmap bPassata) {
		UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Centra immagine per tutto schermo");

		try {
			float width = bPassata.getWidth();
			float height = bPassata.getHeight();

			float diffX = (width / SchermoX);
			float diffY = (height / SchermoY);
			if (diffX < diffY) {
				width /= diffX;
				height /= diffX;
			} else {
				width /= diffY;
				height /= diffY;
			}

			Bitmap bb = Bitmap.createScaledBitmap(bPassata, (int) width, (int) height, true);

			float fineX = 0;
			float fineY = 0;

			float inizioX;
			if (width > SchermoX) {
				inizioX = (width / 2) - (SchermoX / 2);
				fineX = inizioX + SchermoX;
			} else {
				inizioX = 0;
				fineX = width;
			}

			float inizioY;
			if (height > SchermoY) {
				inizioY = (height / 2) - (SchermoY / 2);
				fineY = inizioY + SchermoY;
			} else {
				inizioY = 0;
				fineY = height;
			}

			Bitmap croppedBitmap = Bitmap.createBitmap(
					bb,
					(int) inizioX,
					(int) inizioY,
					(int) fineX,
					(int) fineY);

			return croppedBitmap;
		} catch (Exception e) {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Centra immagine per tutto schermo. Errore: " +
					UtilityWallpaper.getInstance().PrendeErroreDaException(e));

			return null;
		}
	}

	private Bitmap MetteBordoAImmagine(Context context, Bitmap myBitmapPassata, StrutturaImmagine si) {
		UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo a immagine");

		Bitmap myBitmap = ConverteDimensioni(context, myBitmapPassata);

		float dimeImmX = myBitmap.getWidth();
		float dimeImmY = myBitmap.getHeight();

		float posX = ((float)SchermoX / 2) - (dimeImmX / 2);
		float posY = ((float)SchermoY / 2) - (dimeImmY / 2);

		UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo 1");

		// ImmagineX = SchermoX-ImmagineX;
		// ImmagineX = SchermoX;
		// ImmagineY = (SchermoY-ImmagineY)/2;

		// ImmagineX = 500;
		// ImmagineY = 100;

		Bitmap myOutputBitmap = myBitmap.copy(myBitmap.getConfig(), true);
		Bitmap immagineDiSfondo = null;
		try {
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo 2");

			RenderScript renderScript = RenderScript.create(context);
			Allocation blurInput = Allocation.createFromBitmap(
					renderScript,
					myBitmap,
					Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
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
			UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
		}

		// int offset = 50;
		// int divisore = 2;

		UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo 2");

		// Bitmap Immaginona = Bitmap.createBitmap((SharedObjects.getInstance().getSchermoX()) + offset * 2,
// 					SharedObjects.getInstance().getSchermoY() + offset * 2, Bitmap.Config.ARGB_8888);
		Bitmap Immaginona = Bitmap.createBitmap(SchermoX, SchermoY, Bitmap.Config.ARGB_8888);

		UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Mette bordo 3");

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
		if(VariabiliStaticheWallpaper.getInstance().isBlur()) {
            if (immagineDiSfondo != null) {
				canvas1.drawBitmap(immagineDiSfondo, 0, 0, null);
			}
		}
		canvas1.drawBitmap(myBitmap, posX, posY, null);

		if (VariabiliStaticheWallpaper.getInstance().isScriveTestoSuImmagine()) {
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

	public Bitmap applicaEffetti(Bitmap bitmap) {
		Bitmap b = bitmap;
		GestioneImmagini g = new GestioneImmagini();

		final int random1 = new Random().nextInt(15) + 1;

		// Bianco / nero
		switch (random1) {
			case 2:
				b = g.AddGlow(b);
				break;
			case 6:
				b = g.ConverteBN(b);
				break;
			case 9:
				b = g.ConvertSephia(b);
				break;
			case 12:
				b = g.ConvertReflection(b);
				break;
			case 13:
				b = g.ConvertSketch(b);
				break;
		}

		final int random2 = new Random().nextInt(5) + 1;

		if (random2 == 2) {
			b = g.FlipImmagine(b,true);
		}

		return b;
	}
}
