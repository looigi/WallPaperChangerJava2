package com.looigi.wallpaperchanger2.classeStandard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.classeWallpaper.ScannaDiscoPerImmaginiLocali;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.db_dati_wallpaper;

public class RichiestaPathImmaginiLocali extends Activity {
    private static String NomeMaschera = "Richiesta_Path_IL";

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

            db_dati_wallpaper db = new db_dati_wallpaper(this);
            db.ScriveImpostazioni();

            ScannaDiscoPerImmaginiLocali bckLeggeImmaginiLocali = new ScannaDiscoPerImmaginiLocali(this);
            bckLeggeImmaginiLocali.execute();
        }
        finish();
    }
}