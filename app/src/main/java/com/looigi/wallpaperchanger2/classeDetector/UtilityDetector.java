package com.looigi.wallpaperchanger2.classeDetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Size;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGoogleDrive.GoogleDrive;
import com.looigi.wallpaperchanger2.classeGoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.utilities.log.LogInterno;
import com.looigi.wallpaperchanger2.utilities.RichiestaPath;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class UtilityDetector {
    private static final String NomeMaschera = "Utility";

    private static UtilityDetector instance = null;

    private UtilityDetector() {
    }

    public static UtilityDetector getInstance() {
        if (instance == null) {
            instance = new UtilityDetector();
        }

        return instance;
    }

    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
                VariabiliStaticheStart.getInstance().getLog().ScriveLog("Detector", Maschera,  Log);
            // }
        } else {

        }
    }

    public String PrendeErroreDaException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return TransformError(errors.toString());
    }

    private String TransformError(String error) {
        String Return = error;

        if (Return.length() > 250) {
            Return = Return.substring(0, 247) + "...";
        }
        Return = Return.replace("\n", " ");

        return Return;
    }

    public boolean EsisteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public String PrendeNomeFile(String Percorso) {
        String Ritorno = Percorso;

        for (int i = Ritorno.length() - 1; i > 0; i--) {
            if (Ritorno.substring(i, i + 1).equals("/")) {
                Ritorno = Ritorno.substring(i + 1, Ritorno.length());
                break;
            }
        }

        return Ritorno;
    }

    /*
    public void ScattaFoto(Context context, String daDove) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String nome = "LogAcq_" + currentDateandTime + ".txt";

        // l.PulisceFileDiLog();
        ScriveLog(context, NomeMaschera, "onCreate Photo 1 " + daDove);
        if (VariabiliStaticheDetector.getInstance().getCamera() != null) {
            ScriveLog(context, NomeMaschera, "onCreate Photo 2 " + daDove);

            Activity act = VariabiliStaticheDetector.getInstance().getMainActivity();
            if (act != null) {
                TextureView textureView = (TextureView) act.findViewById(R.id.textureView);
                if (textureView != null) {
                    VariabiliStaticheDetector.getInstance().getCamera().ScattaFoto(
                            nome,
                            VariabiliStaticheDetector.getInstance().getMainActivity(),
                            textureView);
                } else {
                    nascondeTV(context);
                }
            } else {
                nascondeTV(context);
            }
        } else {
            nascondeTV(context);

            Utility.getInstance().ApreToast(context, "Camera not ready");

            ScriveLog(context, NomeMaschera, "onCreate Photo Error " + daDove + ": " + VariabiliStaticheDetector.getInstance().getCamera());

            VisualizzaToast(VariabiliStaticheDetector.getInstance().getMainActivity(), "Camera non attiva " + daDove, false);
        }
    }

    private void nascondeTV(Context context) {
        ScriveLog(context, NomeMaschera, "Nascondo TextView Utility");

        Activity act = VariabiliStaticheDetector.getInstance().getMainActivity();
        if (act != null) {
            TextureView textureView = (TextureView) act.findViewById(R.id.textureView);
            if (textureView != null) {
                textureView.setVisibility(LinearLayout.GONE);
                ScriveLog(context, NomeMaschera, "Nascondo TextView Utility: OK");
            } else {
                ScriveLog(context, NomeMaschera, "Nascondo TextView Utility: TV non esistente");
            }
        } else {
            ScriveLog(context, NomeMaschera, "Nascondo TextView Utility: Act non esistente");
        }
    } */

    public void ControllaFileNoMedia(String Cartella) {
        String NomeFile = ".nomedia";

        File file = new File(Cartella + "/" + NomeFile);
        if (!file.exists()) {
            File gpxfile = new File(Cartella + "/" + NomeFile);
            FileWriter writer;
            try {
                writer = new FileWriter(gpxfile);
                writer.append(".");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String PrendeNomeCartella(String Percorso) {
        String Ritorno = Percorso;

        for (int i = Ritorno.length() - 1; i > 0; i--) {
            if (Ritorno.substring(i, i + 1).equals("/")) {
                Ritorno = Ritorno.substring(0, i);
                break;
            }
        }

        return Ritorno;
    }

    public void RinominaFile(String Percorso, String VecchioNome, String NuovoNome) {
        try {
            File from = new File(Percorso, VecchioNome);
            File to = new File(Percorso, NuovoNome);
            from.renameTo(to);
        } catch (Exception ignored) {
        }
    }

    public String PrendeNomeImmagine() {
        Calendar Oggi = Calendar.getInstance();

        int Giorno = Oggi.get(Calendar.DAY_OF_MONTH);
        int Mese = Oggi.get(Calendar.MONTH) + 1;
        int Anno = Oggi.get(Calendar.YEAR);
        int Ora = Oggi.get(Calendar.HOUR_OF_DAY);
        int Minuti = Oggi.get(Calendar.MINUTE);
        int Secondi = Oggi.get(Calendar.SECOND);
        String Ritorno = "";

        Ritorno = Integer.toString(Anno);
        Ritorno += String.format("%02d", Mese); //Integer.toString(Mese);
        Ritorno += String.format("%02d", Giorno); //Integer.toString(Giorno);
        Ritorno += "_" + String.format("%02d", Ora); // Integer.toString(Ora);
        Ritorno += String.format("%02d", Minuti); //Integer.toString(Minuti);
        Ritorno += String.format("%02d", Secondi); // Integer.toString(Secondi);

        return Ritorno;
    }

    public void Vibra(Context context, long Quanto) {
        ScriveLog(context, NomeMaschera, "Vibrazione: " + VariabiliStaticheDetector.getInstance().isVibrazione());

        if (VariabiliStaticheDetector.getInstance().isVibrazione()) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    if (context != null) {
                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(VibrationEffect.createOneShot(Quanto, VibrationEffect.DEFAULT_AMPLITUDE));

                        ScriveLog(context, NomeMaschera, "Vibrazione: " + Quanto);
                    }
                }
            }, 100);
        }
    }

    public void VisualizzaPOPUP(Context context, String Messaggio, final boolean Tasti, final int QualeOperazione) {
        Context ctx = UtilitiesGlobali.getInstance().tornaContextValido();
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Wallpaper Changer II");
        builder.setMessage(Messaggio);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (QualeOperazione == -1) {
                    UtilitiesGlobali.getInstance().ChiudeApplicazione(context);
                }
            }
        });
        if (Tasti) {
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        }

        try {
            AlertDialog dialog = builder.show();
        } catch (Exception ignored) {

        }
    }

    public void VisualizzaToast(Context context, String Messaggio, boolean Lungo) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (VariabiliStaticheDetector.getInstance().isVisualizzaToast()) {
                    int come = -1;
                    if (Lungo) {
                        come = Toast.LENGTH_LONG;
                    } else {
                        come = Toast.LENGTH_SHORT;
                    }

                    Toast toast = Toast.makeText(context, Messaggio, come);
                    toast.show();
                }
            }
        }, 100);
    }

    public String RitornaRisoluzioneMassima(List<String> Risoluzioni) {
        int maxX = 0;
        int maxY = 0;
        String Ritorno = "";
        for (int i = 0; i < Risoluzioni.size(); i++) {
            String[] r = Risoluzioni.get(i).split("x");
            int x = Integer.parseInt(r[0]);
            int y = Integer.parseInt(r[1]);
            if (x > maxX || y > maxY) {
                maxX = x;
                maxY = y;
                Ritorno = Risoluzioni.get(i);
            }
        }

        return Ritorno;
    }

    /* public void generaPath(Context context) {
        if (context != null) {
            String pathLog = PrendePathLog(context);
            VariabiliStaticheStart.getInstance().setPercorsoDIRLog(pathLog);
            String nomeFileLog = VariabiliStaticheDetector.channelName + ".txt";
            VariabiliStaticheDetector.getInstance().setNomeFileDiLog(nomeFileLog);
        }
    } */

    public void EliminaFile(String NomeFile) {
        try {
            File file = new File(NomeFile);
            @SuppressWarnings("unused")
            boolean deleted = file.delete();
        } catch (Exception ignored) {

        }
    }

    public void RitornaRisoluzioni(Activity act, int cameraImpostata) {
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

    public List<String> RitornaTutteLeImmagini(Context context) {
        List<String> lista = new ArrayList<>();
        String Path = PrendePath(context);

        File directory = new File(Path);
        File[] files = directory.listFiles();
        for (File f : files) {
            String n = f.getName();
            if (n.toUpperCase().contains(".JPG")) {
                lista.add(Path + n);
            }
        }

        return lista;
    }

    public void DeCriptaFiles(Context context) {
        String Path = PrendePath(context);

        int cambiate = 0;

        File directory = new File(Path);
        File[] files = directory.listFiles();
        for (File f : files) {
            String n = f.getName();
            if (n.toUpperCase().contains(".DBF") && !n.toUpperCase().contains(".PV3")) {
                n = n.substring(0, n.indexOf("."));
                n += ".jpg";

                File to = new File(Path, n);
                f.renameTo(to);
                removeKeyFromFile(Path, n, n);

                cambiate++;
            }
            if (n.toUpperCase().contains(".DBV") && !n.toUpperCase().contains(".PV3")) {
                n = n.substring(0, n.indexOf("."));
                n += ".mp4";

                File to = new File(Path, n);
                f.renameTo(to);
                removeKeyFromFile(Path, n, n);

                cambiate++;
            }
            if (n.toUpperCase().contains(".DBA") && !n.toUpperCase().contains(".PV3")) {
                n = n.substring(0, n.indexOf("."));
                n += ".3gp";

                File to = new File(Path, n);
                f.renameTo(to);
                removeKeyFromFile(Path, n, n);

                cambiate++;
            }
        }

        CaricaMultimedia(context);
        VisualizzaMultimedia(context);
    }

    public void CriptaFiles(Context context) {
        String Path = PrendePath(context);
        int cambiate = 0;

        File directory = new File(Path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                String n = f.getName();
                if (!n.toUpperCase().contains("FRAMES_")) {
                    if (n.toUpperCase().contains(".JPG")) {
                        n = n.substring(0, n.indexOf("."));
                        n += ".dbf";

                        File to = new File(Path, n);
                        f.renameTo(to);
                        addKeyToFile(Path, n);

                        cambiate++;
                    }
                    if (n.toUpperCase().contains(".MP4")) {
                        n = n.substring(0, n.indexOf("."));
                        n += ".dbv";

                        File to = new File(Path, n);
                        f.renameTo(to);
                        addKeyToFile(Path, n);

                        cambiate++;
                    }
                    if (n.toUpperCase().contains(".3GP")) {
                        n = n.substring(0, n.indexOf("."));
                        n += ".dba";

                        File to = new File(Path, n);
                        f.renameTo(to);
                        addKeyToFile(Path, n);

                        cambiate++;
                    }
                }
            }
        }

        CaricaMultimedia(context);
        VisualizzaMultimedia(context);

        // VisualizzaPOPUP("Fatto. Immagini criptate: "+cambiate, false, 0);
    }

    public void removeKeyFromFile(String Path, String Filetto, String FilettoNuovo) {
        if (!Filetto.toUpperCase().contains(".PV3")) {
            String Filetto2 = Filetto;
            Filetto2 = Filetto2.replace(".jpg", ".dbf");

            String datiExif = "";

            if (!Filetto2.toUpperCase().contains(".3GP") && !Filetto2.toUpperCase().contains(".MP4") && !FilettoNuovo.toUpperCase().contains("APPOGGIO")) {
                datiExif = LeggeFileDiTesto(Path + Filetto2 + ".PV3");
            }

            byte[] bytes = {};

            try {
                File f = new File(Path, Filetto);
                FileInputStream fis = new FileInputStream(f);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];

                try {
                    for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                        bos.write(buf, 0, readNum);
                    }
                } catch (IOException ignored) {

                }

                byte[] altro = {68, 69, 84, 69, 67, 84, 79, 82};
                byte[] bytesApp = bos.toByteArray();
                if (bytesApp[0] == altro[0] && bytesApp[1] == altro[1] && bytesApp[2] == altro[2]) {
                    bytes = Arrays.copyOfRange(bytesApp, altro.length, bytesApp.length);
                } else {
                    bytes = bytesApp;
                }
            } catch (Exception ignored) {

            }

            File someFile = new File(Path, FilettoNuovo);
            try {
                someFile.delete();
            } catch (Exception ignored) {

            }

            try {
                FileOutputStream fos = new FileOutputStream(someFile);
                try {
                    fos.write(bytes);
                    fos.flush();
                    fos.close();

                    if (!datiExif.isEmpty()) {
                        String[] c = datiExif.split(";", -1);
                        ExifInterface exif = new ExifInterface(Path + FilettoNuovo);
                        exif.setAttribute(ExifInterface.TAG_ARTIST, c[0]);
                        exif.setAttribute(ExifInterface.TAG_MODEL, c[1]);
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, c[2]);
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, c[3]);
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, c[4]);
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, c[5]);
                        exif.saveAttributes();

                        File f = new File(Path + Filetto2 + ".PV3");
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                } catch (IOException ignored) {

                }
            } catch (FileNotFoundException ignored) {

            }
        }
    }

    public void addKeyToFile(String Path, String Filetto) {
        byte[] bytes = {};

        boolean OkEXIF = false;
        String artista = "";
        String model = "";
        String lat = "";
        String latRef = "";
        String lon = "";
        String lonref = "";

        if (Filetto.toUpperCase().contains(".DBF") &&
            !Filetto.toUpperCase().contains("FRAME_")) {
            try {
                ExifInterface exif = new ExifInterface(Path + Filetto);
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
        }

        try {
            File f = new File(Path, Filetto);
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            try {
                for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                    bos.write(buf, 0, readNum);
                }
            } catch (IOException ignored) {

            }

            byte[] altro = {68, 69, 84, 69, 67, 84, 79, 82};
            byte[] bytesApp = bos.toByteArray();
            bytes = new byte[bytesApp.length + altro.length];

            int i = 0;
            for (byte b : altro) {
                bytes[i] = b;
                i++;
            }
            for (byte b : bytesApp) {
                bytes[i] = b;
                i++;
            }
        } catch (FileNotFoundException ignored) {

        }

        File someFile = new File(Path, Filetto);
        try {
            someFile.delete();
        } catch (Exception ignored) {

        }

        try {
            FileOutputStream fos = new FileOutputStream(someFile);
            try {
                fos.write(bytes);
                fos.flush();
                fos.close();

                if (OkEXIF &&
                    !Filetto.toUpperCase().contains("FRAME_")) {
                    String datiExif = artista + ";" + model + ";" + lat + ";" + latRef + ";" + lon + ";" + lonref + ";";
                    CreaFileDiTesto(Path, Filetto + ".PV3", datiExif);
                }
            } catch (IOException ignored) {

            }
        } catch (FileNotFoundException ignored) {

        }
    }

    public String LeggeFileDiTesto(String path) {
        StringBuilder text = new StringBuilder();

        File file = new File(path);
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
            } catch (IOException ignored) {

            }
        }

        return text.toString();
    }

    public void CreaFileDiTesto(String Percorso, String sFileName, String sBody) {
        try {
            File gpxfile = new File(Percorso, sFileName);
            if (gpxfile.exists()) {
                gpxfile.delete();
            }
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public void EliminaPV3Inutili(Context context) {
        String Path = PrendePath(context);
        File directory = new File(Path);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File f : files) {
                String n = f.getName();
                if (n.toUpperCase().contains(".PV3")) {
                    String padre = n.replace(".PV3", "");
                    File p = new File(padre);
                    if (!p.exists()) {
                        f.delete();
                    }
                }
            }
        }
    }

    public void CaricaMultimedia(Context context) {
        String Path = PrendePath(context);
        File directory = new File(Path);
        File[] files = directory.listFiles();
        List<String> immagini = new ArrayList<>();
        int totImmagini = 0;

        if (files != null) {
            for (File f : files) {
                String n = f.getName();
                if (n.toUpperCase().contains(".JPG") || n.toUpperCase().contains(".DBF") ||
                        n.toUpperCase().contains(".MP4") || n.toUpperCase().contains(".DBV") ||
                        n.toUpperCase().contains(".3GP") || n.toUpperCase().contains(".DBA")) {
                    if (!n.toUpperCase().contains(".PV3") && !n.toUpperCase().contains("APPOGGIO")) {
                        immagini.add(n);

                        totImmagini++;
                    }
                }
            }
        }
        Collections.sort(immagini);

        VariabiliStaticheDetector.getInstance().setImmagini(immagini);
        VariabiliStaticheDetector.getInstance().setTotImmagini(totImmagini);

        // VariabiliStaticheDetector.getInstance().setNumMultimedia(VariabiliStaticheDetector.getInstance().getTotImmagini() - 1);

        /* if (VariabiliStaticheDetector.getInstance().getImg() != null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    VariabiliStaticheDetector.getInstance().getImg().setImageDrawable(null);
                    VariabiliStaticheDetector.getInstance().getImg().setImageResource(0);
                }
            }, 1000);
        } */
    }

    private void copyFile(File src, File dst) throws IOException {
        InputStream in = Files.newInputStream(src.toPath());
        try {
            OutputStream out = Files.newOutputStream(dst.toPath());
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public void VisualizzaMultimedia(Context context) {
        StopAudio();
        StopVideo();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (VariabiliStaticheDetector.getInstance().getNumMultimedia() < VariabiliStaticheDetector.getInstance().getImmagini().size()) {
                    // String Origine= Environment.getExternalStorageDirectory().getAbsolutePath();
                    // String Cartella=VariabiliStatiche.getInstance().PathApplicazione;
                    // String Cartella = Environment.getExternalStorageDirectory() + "/" +
                    //         Environment.DIRECTORY_DOWNLOADS + "/LooigiSoft/" + VariabiliStaticheDetector.channelName + "/DataBase";
                    String Path = PrendePath(context);
                    if (VariabiliStaticheDetector.getInstance().getNumMultimedia() > -1) {
                        String NomeMultimedia = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().getNumMultimedia());

                        if (NomeMultimedia.toUpperCase().contains(".JPG") || NomeMultimedia.toUpperCase().contains(".DBF")) {
                            File f = new File(Path, "Appoggio.jpg");
                            if (f.exists()) {
                                f.delete();
                            }

                            File o = new File(Path, NomeMultimedia);

                            try {
                                copyFile(o, f);
                            } catch (IOException ignored) {

                            }
                            removeKeyFromFile(Path, NomeMultimedia, "Appoggio.jpg");

                            if (VariabiliStaticheDetector.getInstance().getImg() != null) {
                                VariabiliStaticheDetector.getInstance().getImg().setImageBitmap(
                                        BitmapFactory.decodeFile(Path + "Appoggio.jpg")
                                );
                            }

                            // f.delete();

                            if (VariabiliStaticheDetector.getInstance().getImg() != null) {
                                PhotoViewAttacher photoAttacher;
                                photoAttacher = new PhotoViewAttacher(VariabiliStaticheDetector.getInstance().getImg());
                                photoAttacher.update();
                            }

                            if (VariabiliStaticheDetector.getInstance().getImg() != null) {
                                VariabiliStaticheDetector.getInstance().getImg().setVisibility(LinearLayout.VISIBLE);
                            }
                            if (VariabiliStaticheDetector.getInstance().getAudio() != null) {
                                VariabiliStaticheDetector.getInstance().getAudio().setVisibility(LinearLayout.GONE);
                            }
                            if (VariabiliStaticheDetector.getInstance().getvView() != null) {
                                VariabiliStaticheDetector.getInstance().getvView().setVisibility(LinearLayout.GONE);
                            }

                            if (VariabiliStaticheDetector.getInstance().getImgModificaImmagine() != null) {
                                VariabiliStaticheDetector.getInstance().getImgModificaImmagine().setVisibility(LinearLayout.VISIBLE);
                            }
                            if (VariabiliStaticheDetector.getInstance().getImgCondividiImmagine() != null) {
                                VariabiliStaticheDetector.getInstance().getImgCondividiImmagine().setVisibility(LinearLayout.VISIBLE);
                            }

                    /* if (NomeMultimedia.toUpperCase().contains(".DBF")) {
                        VariabiliStaticheDetector.getInstance().getBtnFlipX().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheDetector.getInstance().getBtnFlipY().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheDetector.getInstance().getBtnRuotaDes().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheDetector.getInstance().getBtnRuotaSin().setVisibility(LinearLayout.GONE);
                    } else {
                        VariabiliStaticheDetector.getInstance().getBtnFlipX().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheDetector.getInstance().getBtnFlipY().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheDetector.getInstance().getBtnRuotaDes().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheDetector.getInstance().getBtnRuotaSin().setVisibility(LinearLayout.VISIBLE);
                    } */

                            if (VariabiliStaticheDetector.getInstance().getTxtImm() != null) {
                                VariabiliStaticheDetector.getInstance().getTxtImm().setText("File immagine " + (VariabiliStaticheDetector.getInstance().getNumMultimedia() + 1) +
                                        "/" + VariabiliStaticheDetector.getInstance().getTotImmagini());
                            }
                        } else {
                            if (NomeMultimedia.toUpperCase().contains(".3GP") || NomeMultimedia.toUpperCase().contains(".DBA")) {
                                if (VariabiliStaticheDetector.getInstance().getImg() != null) {
                                    VariabiliStaticheDetector.getInstance().getImg().setVisibility(LinearLayout.GONE);
                                }
                                if (VariabiliStaticheDetector.getInstance().getAudio() != null) {
                                    VariabiliStaticheDetector.getInstance().getAudio().setVisibility(LinearLayout.VISIBLE);
                                }
                                if (VariabiliStaticheDetector.getInstance().getvView() != null) {
                                    VariabiliStaticheDetector.getInstance().getvView().setVisibility(LinearLayout.GONE);
                                }

                                if (VariabiliStaticheDetector.getInstance().getImgModificaImmagine() != null) {
                                    VariabiliStaticheDetector.getInstance().getImgModificaImmagine().setVisibility(LinearLayout.GONE);
                                }
                                if (VariabiliStaticheDetector.getInstance().getImgCondividiImmagine() != null) {
                                    VariabiliStaticheDetector.getInstance().getImgCondividiImmagine().setVisibility(LinearLayout.GONE);
                                }

                                if (VariabiliStaticheDetector.getInstance().getTxtImm() != null) {
                                    VariabiliStaticheDetector.getInstance().getTxtImm().setText("File audio " + (VariabiliStaticheDetector.getInstance().getNumMultimedia() + 1) +
                                            "/" + VariabiliStaticheDetector.getInstance().getTotImmagini());
                                }
                            } else {
                                if (NomeMultimedia.toUpperCase().contains(".MP4") || NomeMultimedia.toUpperCase().contains(".DBV")) {
                                    if (VariabiliStaticheDetector.getInstance().getImg() != null) {
                                        VariabiliStaticheDetector.getInstance().getImg().setVisibility(LinearLayout.GONE);
                                    }
                                    if (VariabiliStaticheDetector.getInstance().getAudio() != null) {
                                        VariabiliStaticheDetector.getInstance().getAudio().setVisibility(LinearLayout.GONE);
                                    }
                                    if (VariabiliStaticheDetector.getInstance().getvView() != null) {
                                        VariabiliStaticheDetector.getInstance().getvView().setVisibility(LinearLayout.VISIBLE);
                                    }

                                    if (VariabiliStaticheDetector.getInstance().getImgModificaImmagine() != null) {
                                        VariabiliStaticheDetector.getInstance().getImgModificaImmagine().setVisibility(LinearLayout.GONE);
                                    }

                                    if (VariabiliStaticheDetector.getInstance().getImgCondividiImmagine() != null) {
                                        VariabiliStaticheDetector.getInstance().getImgCondividiImmagine().setVisibility(LinearLayout.GONE);
                                    }

                                    if (VariabiliStaticheDetector.getInstance().getTxtImm() != null) {
                                        VariabiliStaticheDetector.getInstance().getTxtImm().setText("File video " + (VariabiliStaticheDetector.getInstance().getNumMultimedia() + 1) +
                                                "/" + VariabiliStaticheDetector.getInstance().getTotImmagini());
                                    }
                                }
                            }
                        }

                        if (VariabiliStaticheDetector.getInstance().getTxtNomeImm() != null) {
                            VariabiliStaticheDetector.getInstance().getTxtNomeImm().setText(NomeMultimedia);
                        }
                    } else {
                        if (VariabiliStaticheDetector.getInstance().getTxtNomeImm() != null) {
                            VariabiliStaticheDetector.getInstance().getTxtNomeImm().setText("");
                        }
                    }
                } else {
                    if (VariabiliStaticheDetector.getInstance().getTxtImm() != null) {
                        VariabiliStaticheDetector.getInstance().getTxtImm().setText("Nessuna immagine rilevata");
                    }
                    if (VariabiliStaticheDetector.getInstance().getTxtNomeImm() != null) {
                        VariabiliStaticheDetector.getInstance().getTxtNomeImm().setText("");
                    }
                }
            }
        }, 100);
    }

    public void SpegneSchermo(Context context) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                /* WindowManager.LayoutParams params = VariabiliStaticheStart.getInstance().getMainActivity().getWindow().getAttributes();

                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                params.screenBrightness = 0.1f;

                VariabiliStaticheStart.getInstance().getMainActivity().getWindow().setAttributes(params); */
            }
        }, 1000);
    }

    public void AccendeSchermo(Context context) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                /* WindowManager.LayoutParams params = VariabiliStaticheStart.getInstance().getMainActivity().getWindow().getAttributes();

                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                params.screenBrightness = 0.9f;

                VariabiliStaticheStart.getInstance().getMainActivity().getWindow().setAttributes(params); */
            }
        }, 1000);
    }

    public void PlayAudio(Context context) {
        // String Origine= Environment.getExternalStorageDirectory().getAbsolutePath();
        // String Cartella=VariabiliStatiche.getInstance().PathApplicazione;
        // String Cartella = Environment.getExternalStorageDirectory() + "/" +
        //         Environment.DIRECTORY_DOWNLOADS + "/LooigiSoft/" + VariabiliStaticheDetector.channelName + "/DataBase";
        String Path = PrendePath(context);
        String NomeMultimedia = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().getNumMultimedia());

        try {
            VariabiliStaticheDetector.getInstance().setMp(new MediaPlayer());
            VariabiliStaticheDetector.getInstance().getMp().setDataSource(Path + NomeMultimedia);
            VariabiliStaticheDetector.getInstance().getMp().prepare();
            VariabiliStaticheDetector.getInstance().getMp().start();
            VariabiliStaticheDetector.getInstance().getAudio().setImageResource(R.drawable.pausa);
            VariabiliStaticheDetector.getInstance().StaSuonando = true;
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public void StopAudio() {
        if (VariabiliStaticheDetector.getInstance().getMp() != null) {
            try {
                VariabiliStaticheDetector.getInstance().getMp().stop();
                VariabiliStaticheDetector.getInstance().getMp().release();
                VariabiliStaticheDetector.getInstance().setMp(null);
                VariabiliStaticheDetector.getInstance().StaSuonando = false;
                VariabiliStaticheDetector.getInstance().getAudio().setImageResource(R.drawable.play);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PlayVideo(Context context) {
        if (!VariabiliStaticheDetector.getInstance().StaVedendo) {
            // String Origine= Environment.getExternalStorageDirectory().getAbsolutePath();
            // String Cartella=VariabiliStatiche.getInstance().PathApplicazione;
            // String Cartella = Environment.getExternalStorageDirectory() + "/" +
            //         Environment.DIRECTORY_DOWNLOADS + "/LooigiSoft/" + VariabiliStaticheDetector.channelName + "/DataBase";
            String Path = PrendePath(context);
            String NomeMultimedia = VariabiliStaticheDetector.getInstance().getImmagini().get(VariabiliStaticheDetector.getInstance().getNumMultimedia());

            try {
                VariabiliStaticheDetector.getInstance().getvView().setVideoURI(Uri.parse(Path + NomeMultimedia));

                VariabiliStaticheDetector.getInstance().getvView().start();
                VariabiliStaticheDetector.getInstance().StaVedendo = true;
            } catch (Exception ignored) {

            }
        }
    }

    public void StopVideo() {
        if (VariabiliStaticheDetector.getInstance().StaVedendo) {
            VariabiliStaticheDetector.getInstance().getvView().pause();
            VariabiliStaticheDetector.getInstance().StaVedendo = false;
        }
    }

    /* public boolean LeggeImpostazioni(Context context, String daDove) {
        db_dati_detector db = new db_dati_detector(context);
        boolean rit = db.CaricaImpostazioni(context, daDove);
        ScriveLog(context, NomeMaschera, "Ritorno caricamento impostazioni: " + rit);

        return rit;
    } */

    public void CreaCartelle(String Origine, String Cartella) {
        for (int i = 1; i < Cartella.length(); i++) {
            if (Cartella.substring(i, i + 1).equals("/")) {
                CreaCartella(Origine + Cartella.substring(0, i));
            }
        }
    }

    public void SpostaFile(Context context) {
        Intent myIntent = new Intent(
                VariabiliStaticheWallpaper.getInstance().getMainActivity(),
                RichiestaPath.class);
        VariabiliStaticheWallpaper.getInstance().getMainActivity().startActivity(myIntent);
    }

    private int idImmagineDaSpostare;
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;

    public void SpostaFileSuDrive(Context context) {
        DeCriptaFiles(context);

        idImmagineDaSpostare = 0;
        SpostaImmagineSuDrive(context);
    }

    private void SpostaImmagineSuDrive(Context context) {
        String Cartella = UtilityDetector.getInstance().PrendePath(context);
        String Immagine = VariabiliStaticheDetector.getInstance().getImmagini().get(idImmagineDaSpostare);

        LocalDateTime currDate = LocalDateTime.now();
        int Anno = currDate.getYear();

        VariabiliStaticheGoogleDrive.getInstance().setPathOperazione(
                "Detector/" + Integer.toString(Anno)
        );
        String[] I = Immagine.split("/");
        String NomeImmagine = I[I.length - 1];
        VariabiliStaticheGoogleDrive.getInstance().setNomeFileApiFootball(NomeImmagine);
        VariabiliStaticheGoogleDrive.getInstance().setFileDiOrigine(
                Cartella + Immagine
        );

        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(true);

        handlerThread = new HandlerThread("background-thread_SpostaDetector_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheApiFootball.getInstance().isStaLeggendoWS()) {
                    idImmagineDaSpostare++;
                    if (idImmagineDaSpostare < VariabiliStaticheDetector.getInstance().getImmagini().size()) {
                        SpostaImmagineSuDrive(context);
                    } else {
                        UtilityDetector.getInstance().EliminaPV3Inutili(context);

                        Handler handlerTimer = new Handler(Looper.getMainLooper());
                        Runnable rTimer = new Runnable() {
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Detector");
                                builder.setMessage("Si vogliono eliminare dal telefono le immagini spostate?");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        List<String> daEliminare = RitornaTutteLeImmagini(context);

                                        for (String e: daEliminare) {
                                            if (com.looigi.wallpaperchanger2.utilities.Files.getInstance().EsisteFile(e)) {
                                                com.looigi.wallpaperchanger2.utilities.Files.getInstance().EliminaFileUnico(e);
                                            }
                                        }

                                        if (VariabiliStaticheDetector.getInstance().getImg() != null) {
                                            VariabiliStaticheDetector.getInstance().getImg().setImageBitmap(null);
                                        }

                                        prosegueSpostamento(context);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        prosegueSpostamento(context);

                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }
                        };
                        handlerTimer.postDelayed(rTimer, 100);
                    }
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 100);
                    }
                }
            }
        };
        handler.postDelayed(r, 100);

        VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("ScriveFile");
        Intent apre = new Intent(context, GoogleDrive.class);
        apre.addCategory(Intent.CATEGORY_LAUNCHER);
        apre.setAction(Intent.ACTION_MAIN );
        apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        context.startActivity(apre);
    }

    private void prosegueSpostamento(Context context) {
        // int appo = VariabiliStaticheDetector.getInstance().getNumMultimedia();
        UtilityDetector.getInstance().CaricaMultimedia(context);
                                /* appo--;
                                if (appo < 0) appo = 0;
                                VariabiliStaticheDetector.getInstance().setNumMultimedia(appo); */
        UtilityDetector.getInstance().VisualizzaMultimedia(context);
        // UtilityDetector.getInstance().VisualizzaPOPUP(context, "File multimediale eliminato", false, 0);

        UtilityDetector.getInstance().ContaFiles(context);

        UtilitiesGlobali.getInstance().ApreToast(context, "Immagini spostate su drive: " +
                (VariabiliStaticheDetector.getInstance().getImmagini().size()));
    }

    public void ContaFiles(Context context) {
        String Path = UtilityDetector.getInstance().PrendePath(context);
        File p = new File(Path);
        File[] list = p.listFiles();
        int q = 0;
        if (list != null) {
            for (File f : list) {
                if (!f.getName().toUpperCase().contains("NOMEDIA")) {
                    q++;
                }
            }
            if (list != null) {
                String Messaggio = String.valueOf(q);

                GestioneNotificheDetector.getInstance().AggiornaNotifica(Messaggio);
            } else {
                GestioneNotificheDetector.getInstance().AggiornaNotifica("Detector");
            }
        }
    }

    private void CreaCartella(String Percorso) {
        try {
            File dDirectory = new File(Percorso);
            dDirectory.mkdirs();
        } catch (Exception ignored) {

        }
    }

    /* public String PrendePathLog(Context context) {
        String Path = context.getFilesDir() + "/Log/";

        return Path;
    } */

    public String PrendePath(Context context) {
        Context c = context;
        if (c == null) {
            c = UtilitiesGlobali.getInstance().tornaContextValido();
        }
        String Path = c.getFilesDir() + "/DataBase/";

        return Path;
    }

    public String PrendePathDB(Context context) {
        Context c = context;
        if (c == null) {
            c = UtilitiesGlobali.getInstance().tornaContextValido();
        }
        String Path = c.getFilesDir() + "/DB/";

        return Path;
    }
}
