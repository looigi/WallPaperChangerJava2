package com.looigi.wallpaperchanger2.classeGoogleDrive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;

public class GoogleDrive extends Activity {
    private GoogleDriveHelper driveHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_google_drive);

        driveHelper = new GoogleDriveHelper(this);
        driveHelper.signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        driveHelper.handleSignInResult(requestCode, resultCode, data);
    }

    public void upload(View view) {
        // Esempio: salva un file locale in /data/data/tuo.app/files/example.txt
        String path = getFilesDir() + "/example.txt";
        driveHelper.uploadFile(path, "EsempioDaApp.txt");
    }
}
