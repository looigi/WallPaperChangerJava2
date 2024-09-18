package com.looigi.wallpaperchanger2.classiStandard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.ConverteNomeUri;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.ScannaDiscoPerImmaginiLocali;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.db_dati;
import com.looigi.wallpaperchanger2.utilities.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RichiestaPathImmaginiLocali extends Activity {
    private static String NomeMaschera = "RICHIESTAPATH";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        Uri u = Uri.parse("/storage/emulated/0");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, u);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();

        /* Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri,
                DocumentsContract.getTreeDocumentId(uri));
        ConverteNomeUri c = new ConverteNomeUri();
        String path = c.getPath(this, docUri); */

        String path = uri.toString();
        int c = path.indexOf("%3A");
        if (c > 0) {
            path = "/storage/emulated/0/" + path.substring(c, path.length()) + "/";
            path = path.replace("%3A", "").replace("%2F", "/");

            assert uri != null;
            getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);

            VariabiliStaticheWallpaper.getInstance().getTxtPath().setText(path);
            VariabiliStaticheWallpaper.getInstance().setPercorsoIMMAGINI(path);

            db_dati db = new db_dati(this);
            db.ScriveImpostazioni();

            ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(this);
            bckLeggeImmaginiLocali.execute();
        }
        finish();
    }
}