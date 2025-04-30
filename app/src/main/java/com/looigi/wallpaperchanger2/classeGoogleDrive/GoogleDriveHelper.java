package com.looigi.wallpaperchanger2.classeGoogleDrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.*;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GoogleDriveHelper {
    private static final int REQUEST_CODE_SIGN_IN = 400;
    private static final String TAG = "GoogleDriveHelper";

    private final Activity activity;
    private Drive driveService;
    private Context context;

    public GoogleDriveHelper(Activity activity, Context context) {
        this.activity = activity;
    }

    public void signIn(GoogleDrive gd) {
        if (VariabiliStaticheGoogleDrive.getInstance().getDriveService() != null) {
            driveService = VariabiliStaticheGoogleDrive.getInstance().getDriveService();

            VariabiliStaticheGoogleDrive.getInstance().setConnesso(true);
            VariabiliStaticheGoogleDrive.getInstance().setErroreConnessione("");

            gd.AttendeConnessione();

            UtilitiesGlobali.getInstance().ApreToast(context, "Login effettuato");
        } else {
            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .requestScopes(new Scope(DriveScopes.DRIVE)) // <-- per Drive completo
                            .build();

            GoogleSignInClient client = GoogleSignIn.getClient(activity, signInOptions);
            activity.startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
        }
    }

    public void handleSignInResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                initializeDriveService(account);
                // Log.d(TAG, "Login effettuato");
                VariabiliStaticheGoogleDrive.getInstance().setConnesso(true);
                VariabiliStaticheGoogleDrive.getInstance().setErroreConnessione("");
                UtilitiesGlobali.getInstance().ApreToast(context, "Login effettuato");
            } else {
                // Log.e(TAG, "Errore durante il login", task.getException());
                VariabiliStaticheGoogleDrive.getInstance().setConnesso(true);
                VariabiliStaticheGoogleDrive.getInstance().setErroreConnessione(Objects.requireNonNull(task.getException()).toString());
                // UtilitiesGlobali.getInstance().ApreToast(context, Objects.requireNonNull(task.getException()).toString());
            }
        }
    }

    private void initializeDriveService(GoogleSignInAccount account) {
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        activity, Collections.singleton(DriveScopes.DRIVE));
        credential.setSelectedAccount(account.getAccount());

        driveService = new Drive.Builder(
                new NetHttpTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("Wallpaper Changer II")
                .build();

        VariabiliStaticheGoogleDrive.getInstance().setDriveService(driveService);
    }

    public String uploadFile(Context context, String localFilePath, String fileName, String folderId) {
        if (driveService == null) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Servizio Drive non inizializzato");
            UtilityGoogleDrive.getInstance().ImpostaAttesa(false);
            return "";
        }

        File fileMetadata = new File();
        fileMetadata.setName(fileName);

        // Imposta la cartella di destinazione
        if (folderId != null && !folderId.isEmpty()) {
            fileMetadata.setParents(Collections.singletonList(folderId));
        }

        java.io.File filePath = new java.io.File(localFilePath);

        java.io.File f = new java.io.File(localFilePath);  // << CORRETTO: devi usare localFilePath, non fileName qui!
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider",
                f
        );

        String tipoMime = UtilitiesGlobali.getInstance().GetMimeType(context, uri);
        FileContent mediaContent = new FileContent(tipoMime, filePath);

        try {
            Drive.Files.Create create = driveService.files().create(fileMetadata, mediaContent);
            MediaHttpUploader uploader = create.getMediaHttpUploader();
            uploader.setProgressListener(uploader1 ->
                    Log.d(TAG, "Upload progress: " + uploader1.getProgress())
            );

            File uploadedFile = create.setFields("id").execute();
            return uploadedFile.getId();
            // Log.d(TAG, "File caricato con ID: " + uploadedFile.getId());
        } catch (IOException e) {
            // Log.e(TAG, "Errore nel caricamento del file", e);
            return "";
        }
    }

    public void downloadFile(String fileId, String destinationPath) {
        if (driveService == null) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Servizio Drive non inizializzato");
            UtilityGoogleDrive.getInstance().ImpostaAttesa(false);
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

    public void listRootFolders(RootFolderListCallback callback) {
        new Thread(() -> {
            try {
                File rootFolder = driveService.files().get("root")
                        .setFields("id")
                        .execute();
                VariabiliStaticheGoogleDrive.getInstance().setRootId(rootFolder.getId());

                FileList result = driveService.files().list()
                        .setQ("mimeType='application/vnd.google-apps.folder' and trashed=false and 'root' in parents")
                        .setOrderBy("name")
                        .setFields("files(id, name)")
                        .execute();

                List<File> folders = result.getFiles();

                if (callback != null) {
                    callback.onRootFolderListed(folders);
                }
            } catch (UserRecoverableAuthIOException e) {
                // Log.w(TAG, "Autorizzazione necessaria: " + e.getMessage());
                // activity.startActivityForResult(e.getIntent(), REQUEST_CODE_AUTHORIZATION);
            } catch (IOException e) {
                // Log.e(TAG, "Errore durante il recupero delle cartelle Root", e);
                if (callback != null) {
                    callback.onError(e);
                }
            }
        }).start();
    }

    // Callback per restituire la lista o gestire errori
    public interface RootFolderListCallback {
        void onRootFolderListed(List<File> folders);
        void onError(Exception e);
    }

    public void listFolders(FoldersListCallback callback, String folderId) {
        if (driveService == null) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Servizio Drive non inizializzato");
            UtilityGoogleDrive.getInstance().ImpostaAttesa(false);
            return;
        }

        new Thread(() -> {
            try {
                File file = driveService.files().get(folderId)
                        .setFields("parents")
                        .execute();
                List<String> parentIds = file.getParents();

                String query = "mimeType='application/vnd.google-apps.folder' and trashed=false and '" + folderId + "' in parents";

                Drive.Files.List request = driveService.files().list()
                        .setQ(query)
                        .setOrderBy("name")
                        .setFields("files(id, name)");

                FileList result = request.execute();
                List<File> folders = result.getFiles();

                // Aggiungere la voce "Torna indietro" se esiste un genitore
                if (parentIds != null && !parentIds.isEmpty()) {
                    File backItem = new File();
                    backItem.setName("...");
                    backItem.setId(parentIds.get(0));
                    backItem.setMimeType("application/vnd.google-apps.folder");
                    folders.add(0, backItem);
                }

                if (callback != null) {
                    callback.onFoldersListed(folders);
                }
            } catch (IOException e) {
                // Log.e(TAG, "Errore durante la ricerca delle cartelle", e);
                if (callback != null) {
                    callback.onError(e);
                }
            }
        }).start();
    }

    // Callback per restituire la lista o gestire errori
    public interface FoldersListCallback {
        void onFoldersListed(List<File> folders);
        void onError(Exception e);
    }

    public void listFilesInFolder(String folderId, FilesListCallback callback) {
        if (driveService == null) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Servizio Drive non inizializzato");
            UtilityGoogleDrive.getInstance().ImpostaAttesa(false);
            return;
        }

        new Thread(() -> {
            try {
                String query = "'" + folderId + "' in parents and trashed = false and mimeType != 'application/vnd.google-apps.folder'";

                Drive.Files.List request = driveService.files().list()
                        .setQ(query)
                        .setOrderBy("name")
                        .setFields("files(id, name, mimeType)");

                FileList result = request.execute();
                List<File> files = result.getFiles();

                if (callback != null) {
                    callback.onFilesListed(files);
                }
            } catch (IOException e) {
                Log.e(TAG, "Errore durante la ricerca dei file nella cartella", e);
                if (callback != null) {
                    callback.onError(e);
                }
            }
        }).start();
    }

    // Callback per restituire la lista o l'errore
    public interface FilesListCallback {
        void onFilesListed(List<File> files);
        void onError(Exception e);
    }

    /**
     * Crea una nuova cartella dentro una cartella specifica su Google Drive.
     * @param parentFolderId ID della cartella padre
     * @param folderName Nome della nuova cartella da creare
     * @param callback Callback per segnalare successo o errore
     */
    public void createFolder(String parentFolderId, String folderName, FolderCreationCallback callback) {
        if (driveService == null) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Servizio Drive non inizializzato");
            UtilityGoogleDrive.getInstance().ImpostaAttesa(false);
            return;
        }

        new Thread(() -> {
            try {
                // 1. Metadata della cartella
                File metadata = new File();
                metadata.setName(folderName);
                metadata.setMimeType("application/vnd.google-apps.folder");

                // 2. Setto il parent ID
                if (parentFolderId != null && !parentFolderId.isEmpty()) {
                    metadata.setParents(Collections.singletonList(parentFolderId));
                }

                // 3. Creo la cartella
                File folder = driveService.files().create(metadata)
                        .setFields("id")
                        .execute();

                Log.d(TAG, "Cartella creata con ID: " + folder.getId());

                // 4. Callback di successo
                if (callback != null) {
                    callback.onFolderCreated(folder.getId());
                }
            } catch (IOException e) {
                Log.e(TAG, "Errore nella creazione della cartella", e);
                if (callback != null) {
                    callback.onError(e);
                }
            }
        }).start();
    }

    // Callback per notificare il risultato
    public interface FolderCreationCallback {
        void onFolderCreated(String folderId);
        void onError(Exception e);
    }

    /**
     * Cerca una cartella per nome (e opzionalmente dentro una cartella padre) e restituisce il suo ID.
     * @param parentFolderId ID della cartella padre (può essere null per cercare ovunque)
     * @param folderName Nome esatto della cartella da cercare
     * @param callback Callback per ricevere l'ID trovato o errore
     */
    public void getFolderIdByName(String parentFolderId, String folderName, FolderIdCallback callback) {
        if (driveService == null) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Servizio Drive non inizializzato");
            UtilityGoogleDrive.getInstance().ImpostaAttesa(false);
            return;
        }

        new Thread(() -> {
            try {
                // Costruisce la query
                String query = "mimeType = 'application/vnd.google-apps.folder' and trashed = false and name = '" + folderName + "'";
                if (parentFolderId != null && !parentFolderId.isEmpty()) {
                    query += " and '" + parentFolderId + "' in parents";
                }

                FileList result = driveService.files().list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("files(id, name)")
                        .execute();

                List<File> folders = result.getFiles();
                if (folders != null && !folders.isEmpty()) {
                    String folderId = folders.get(0).getId(); // Prende il primo (se ci sono più cartelle con lo stesso nome)
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFolderIdFound(folderId));
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFolderNotFound());
                }
            } catch (IOException e) {
                Log.e(TAG, "Errore durante la ricerca della cartella", e);
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
            }
        }).start();
    }

    public interface FolderIdCallback {
        void onFolderIdFound(String folderId);
        void onFolderNotFound();
        void onError(Exception e);
    }

    public void deleteFileOrFolder(String fileId) {
        if (driveService == null) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Servizio Drive non inizializzato");
            UtilityGoogleDrive.getInstance().ImpostaAttesa(false);
            return;
        }

        new Thread(() -> {
            try {
                driveService.files().delete(fileId).execute();
                Log.d(TAG, "File o cartella eliminato con ID: " + fileId);
            } catch (IOException e) {
                Log.e(TAG, "Errore durante l'eliminazione", e);
            }
        }).start();
    }

    public void downloadFileByNameInFolder(String folderId, String fileName, String outputFile) {
        new Thread(() -> {
            try {
                // 1. Cerca il file
                String query = "name = '" + fileName + "' and trashed=false and '" + folderId + "' in parents";
                FileList result = driveService.files().list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("files(id, name)")
                        .execute();

                List<File> files = result.getFiles();

                if (files != null && !files.isEmpty()) {
                    File fileToDownload = files.get(0); // Prende il primo che trova (se più file con stesso nome)

                    // 2. Scarica il file
                    OutputStream outputStream = Files.newOutputStream(Paths.get(outputFile));
                    driveService.files().get(fileToDownload.getId())
                            .executeMediaAndDownloadTo(outputStream);

                    outputStream.flush();
                    outputStream.close();

                    // Log.d(TAG, "Download completato: " + outputFile.getAbsolutePath());
                } else {
                    // Log.d(TAG, "Nessun file trovato con il nome: " + fileName);
                }

            } catch (UserRecoverableAuthIOException e) {
                // Log.w(TAG, "Autorizzazione necessaria: " + e.getMessage());
                // activity.startActivityForResult(e.getIntent(), REQUEST_CODE_AUTHORIZATION);
            } catch (IOException e) {
                // Log.e(TAG, "Errore durante il download del file", e);
            }
        }).start();
    }
    /**
     * Controlla se una cartella con un nome specifico esiste dentro un'altra cartella.
     * @param parentFolderId ID della cartella padre
     * @param folderName Nome esatto della cartella da cercare
     * @return true se la cartella esiste, false altrimenti
     */
    public boolean checkIfFolderExists(String parentFolderId, String folderName) {
        try {
            String query = "mimeType = 'application/vnd.google-apps.folder' " +
                    "and trashed = false " +
                    "and name = '" + folderName + "' " +
                    "and '" + parentFolderId + "' in parents";

            FileList result = driveService.files().list()
                    .setQ(query)
                    .setSpaces("drive")
                    .setFields("files(id, name)")
                    .execute();

            List<File> folders = result.getFiles();

            return folders != null && !folders.isEmpty();
        } catch (IOException e) {
            Log.e(TAG, "Errore durante il controllo della cartella", e);
            return false;
        }
    }
}
