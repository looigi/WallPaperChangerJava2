package com.looigi.wallpaperchanger2.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RichiestaPath extends Activity {
    private static String NomeMaschera = "Richiesta_Path";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        Uri u = Uri.parse("/storage/emulated/0");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, u); */

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, "/storage/emulated/0");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera,"Spostamento Files");

        String SRC_PATH = UtilityDetector.getInstance().PrendePath(this);
        File directory = new File(SRC_PATH);
        File[] files = directory.listFiles();

        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera,"Path Origine: " + SRC_PATH);
        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera,"Files: " + files.length);

        Uri _uri = data.getData();

        try {
            getContentResolver().takePersistableUriPermission(
                    _uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            String filePath = _uri.toString();
            UtilityDetector.getInstance().ScriveLog(this, NomeMaschera,"Path URI: " + filePath);

            int c = filePath.indexOf("%3A");
            if (c > 0) {
                filePath = "/storage/emulated/0/" + filePath.substring(c, filePath.length()) + "/";
                filePath = filePath.replace("%3A", "").replace("%2F", "/") ;

                UtilityDetector.getInstance().ScriveLog(this, NomeMaschera,"Path Files: " + filePath);
                // String filePath = readText(_uri);

                UtilityDetector.getInstance().ControllaFileNoMedia(filePath);

                boolean criptati = false;
                for (int i = 0; i < files.length; i++) {
                    String strFileName = files[i].getName();
                    if (!strFileName.toUpperCase().contains("NOMEDIA")) {
                        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "Criptaggio file: " + strFileName);

                        if (strFileName.contains(".dbf")) {
                            criptati = true;
                            break;
                        }
                    }
                }
                if (criptati) {
                    UtilityDetector.getInstance().DeCriptaFiles(this);
                }

                File[] files2 = directory.listFiles();
                for (int i = 0; i < files2.length; i++) {
                    String strFileName = files2[i].getName();
                    String pathOrigine = files2[i].getParent();

                    if (!strFileName.toUpperCase().contains("NOMEDIA")) {
                        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "Spostamento " + i + ": " +
                                pathOrigine + strFileName
                                + " -> " + filePath);

                        boolean rit = moveFile(this, pathOrigine, strFileName, filePath);

                        UtilityDetector.getInstance().ScriveLog(this, NomeMaschera, "Ritorno spostamento: " + rit);
                    }
                }
            }

            UtilityDetector.getInstance().CaricaMultimedia(this);
            UtilityDetector.getInstance().VisualizzaMultimedia(this);
        } catch (SecurityException e) {
            // handle failure to get persistable permission though NOT DOCUMENTED
            UtilityDetector.getInstance().ScriveLog(this, NomeMaschera,"Sicurezza sulla cartella: " +
                    _uri + " -> " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));

            UtilitiesGlobali.getInstance().ApreToast(this,"Sicurezza sulla cartella: " +
                    _uri + " -> " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));
        }

        /* String dest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LooigiSoft/" +
                VariabiliStaticheServizio.channelName + "/Database/";
        CreaCartella(dest);
        for (int i = 0; i < files.length; i++) {
            String strFileName = files[i].getName();
            String pathOrigine = files[i].getParent();

            ScriveLog(context, NomeMaschera,"Spostamento file: " + strFileName);
            boolean rit = moveFile(context, pathOrigine, strFileName, dest);
        } */

        finish();
    }

    /* private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    } */

    private boolean moveFile(Context context, String inputPath, String inputFile, String outputPath) {
        InputStream in = null;
        OutputStream out = null;

        try {
            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            in = new FileInputStream(inputPath + "/" + inputFile);
            if (!UtilityWallpaper.getInstance().EsisteFile(outputPath + inputFile)) {
                File f = new File(outputPath + inputFile);
                f.createNewFile();
            }

            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + "/"  + inputFile).delete();
        }

        catch (FileNotFoundException fnfe1) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"File non trovato: " +
                    inputPath + "/" + inputFile + " -> " + outputPath +  inputFile);

            return false;
        }
        catch (Exception e) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore nello spostamento: " + UtilityWallpaper.getInstance().PrendeErroreDaException(e));

            return false;
        }

        return true;
    }

}