package com.looigi.wallpaperchanger2.classeGoogleDrive;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.*;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class GoogleDriveHelper {
    private static final int REQUEST_CODE_SIGN_IN = 400;
    private static final String TAG = "GoogleDriveHelper";

    private final Activity activity;
    private Drive driveService;

    public GoogleDriveHelper(Activity activity) {
        this.activity = activity;
    }

    public void signIn() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(activity, signInOptions);
        activity.startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    public void handleSignInResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                initializeDriveService(account);
                Log.d(TAG, "Login effettuato");
            } else {
                Log.e(TAG, "Errore durante il login", task.getException());
            }
        }
    }

    private void initializeDriveService(GoogleSignInAccount account) {
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(activity, Collections.singleton(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccount(account.getAccount());

        driveService = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("Wallpaper Changer II")
                .build();
    }

    public void uploadFile(String localFilePath, String fileName) {
        if (driveService == null) {
            Log.e(TAG, "Servizio Drive non inizializzato.");
            return;
        }

        File fileMetadata = new File();
        fileMetadata.setName(fileName);

        java.io.File filePath = new java.io.File(localFilePath);
        FileContent mediaContent = new FileContent("text/plain", filePath);

        try {
            Drive.Files.Create create = driveService.files().create(fileMetadata, mediaContent);
            MediaHttpUploader uploader = create.getMediaHttpUploader();
            uploader.setProgressListener(uploader1 -> Log.d(TAG, "Upload progress: " + uploader1.getProgress()));
            File file = create.setFields("id").execute();
            Log.d(TAG, "File caricato con ID: " + file.getId());
        } catch (IOException e) {
            Log.e(TAG, "Errore nel caricamento del file", e);
        }
    }

    public void downloadFile(String fileId, String destinationPath) {
        if (driveService == null) {
            Log.e(TAG, "Servizio Drive non inizializzato.");
            return;
        }

        try (java.io.OutputStream outputStream = Files.newOutputStream(Paths.get(destinationPath))) {
            driveService.files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);
            Log.d(TAG, "File scaricato in: " + destinationPath);
        } catch (IOException e) {
            Log.e(TAG, "Errore durante il download del file", e);
        }
    }
}
