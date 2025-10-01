package com.looigi.wallpaperchanger2.GoogleDrive;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.google.api.services.drive.model.File;
import com.looigi.wallpaperchanger2.GoogleDrive.adapters.AdapterListenerFiles;
import com.looigi.wallpaperchanger2.GoogleDrive.adapters.AdapterListenerFolders;

import java.util.ArrayList;
import java.util.List;

public class UtilityGoogleDrive {
    private static UtilityGoogleDrive instance = null;
    private List<File> folders;

    private UtilityGoogleDrive() {
    }

    public static UtilityGoogleDrive getInstance() {
        if (instance == null) {
            instance = new UtilityGoogleDrive();
        }

        return instance;
    }

    public void ImpostaAttesa(boolean Come, boolean Dettaglio) {
        if (VariabiliStaticheGoogleDrive.getInstance().getImgAttesa() != null) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    if (Come) {
                        VariabiliStaticheGoogleDrive.getInstance().getImgAttesa().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStaticheGoogleDrive.getInstance().getTxtDettaglio().setText("");
                        if (Dettaglio) {
                            VariabiliStaticheGoogleDrive.getInstance().getTxtDettaglio().setVisibility(LinearLayout.VISIBLE);
                        } else {
                            VariabiliStaticheGoogleDrive.getInstance().getTxtDettaglio().setVisibility(LinearLayout.GONE);
                        }
                    } else {
                        VariabiliStaticheGoogleDrive.getInstance().getImgAttesa().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheGoogleDrive.getInstance().getTxtDettaglio().setVisibility(LinearLayout.GONE);
                        VariabiliStaticheGoogleDrive.getInstance().getTxtDettaglio().setText("");
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    public void ImpostaDettaglioGD(String Dettaglio) {
        if (VariabiliStaticheGoogleDrive.getInstance().getImgAttesa() != null) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    VariabiliStaticheGoogleDrive.getInstance().getTxtDettaglio().setText(Dettaglio);
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    public void esisteFileInFolder(Context context, String folderId, String NomeFile) {
        new Thread(() -> {
            boolean esiste = VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().fileExistsInFolder(
                    folderId,
                    NomeFile);
            if (esiste) {
                VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("TRUE");
            } else {
                VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("FALSE");
            }
        }).start();
    }

    public String upload(Context context, String path, String File, String folderId) {
        return VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().uploadFile(
                context,
                path,
                File,
                folderId);
    }

    public void dowload(String folderId, String fileName, String destinationPath) {
        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().downloadFileByNameInFolder(
                folderId,
                fileName,
                destinationPath
        );
    }

    public void listaRootFolder(Context context) {
        UtilityGoogleDrive.getInstance().ImpostaAttesa(true, false);

        folders = new ArrayList<>();

        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().listRootFolders(new GoogleDriveHelper.RootFolderListCallback() {
            @Override
            public void onRootFolderListed(List<File> folders) {
                UtilityGoogleDrive.getInstance().ImpostaAttesa(false, false);

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        AdapterListenerFolders adapter = new AdapterListenerFolders(context, folders);
                        VariabiliStaticheGoogleDrive.getInstance().getLstFolders().setAdapter(adapter);

                        UtilityGoogleDrive.getInstance().ImpostaAttesa(true, false);
                        UtilityGoogleDrive.getInstance().listaFilesInFolder(
                                context,
                                VariabiliStaticheGoogleDrive.getInstance().getRootId()
                        );
                    }
                };
                handlerTimer.postDelayed(rTimer, 100);
            }

            @Override
            public void onError(Exception e) {
                UtilityGoogleDrive.getInstance().ImpostaAttesa(false, false);

                // Log.e("FoldersTest", "Errore caricamento cartelle", e);
            }
        });
    }

    public void listaFolders(Context context, String id) {
        UtilityGoogleDrive.getInstance().ImpostaAttesa(true, false);

        folders = new ArrayList<>();

        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().listFolders(new GoogleDriveHelper.FoldersListCallback() {
            @Override
            public void onFoldersListed(List<File> folders) {
                UtilityGoogleDrive.getInstance().ImpostaAttesa(false, false);

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        AdapterListenerFolders adapter = new AdapterListenerFolders(context, folders);
                        VariabiliStaticheGoogleDrive.getInstance().getLstFolders().setAdapter(adapter);

                        UtilityGoogleDrive.getInstance().listaFilesInFolder(
                                context,
                                id
                        );
                    }
                };
                handlerTimer.postDelayed(rTimer, 100);
            }

            @Override
            public void onError(Exception e) {
                UtilityGoogleDrive.getInstance().ImpostaAttesa(false, false);

                // Log.e("FoldersTest", "Errore caricamento cartelle", e);
            }
        }, id);
    }

    public void listaFilesInFolder(Context context, String folderId) {
        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().listFilesInFolder(folderId, new GoogleDriveHelper.FilesListCallback() {
            @Override
            public void onFilesListed(List<com.google.api.services.drive.model.File> files) {
                UtilityGoogleDrive.getInstance().ImpostaAttesa(false, false);

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        AdapterListenerFiles adapter = new AdapterListenerFiles(context, files);
                        VariabiliStaticheGoogleDrive.getInstance().getLstFiles().setAdapter(adapter);
                    }
                };
                handlerTimer.postDelayed(rTimer, 100);
            }

            @Override
            public void onError(Exception e) {
                // Log.e("FilesTest", "Errore caricamento file", e);
            }
        });
    }

    public void createFolder(String folderId, String nomeFolder) {
        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("");

        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().createFolder(
                folderId,
                nomeFolder,
                new GoogleDriveHelper.FolderCreationCallback() {
                    @Override
                    public void onFolderCreated(String folderId) {
                        // Log.d("FolderTest", "Cartella creata con ID: " + folderId);
                        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato(folderId);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Log.e("FolderTest", "Errore creazione cartella", e);
                        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("ERROR");
                    }
                }
        );
    }

    public void returnIdFolder(String folderId, String nomeFolder) {
        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("");

        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().getFolderIdByName(
                folderId,
                nomeFolder,
                new GoogleDriveHelper.FolderIdCallback() {
                    @Override
                    public void onFolderIdFound(String folderId) {
                        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato(folderId);
                    }

                    @Override
                    public void onFolderNotFound() {
                        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("ERROR");
                    }

                    @Override
                    public void onError(Exception e) {
                        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("ERROR");
                    }
                }
        );
    }

    public void deleteFileOrFolder(String folderId, String FileName) {
        VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().deleteFileByNameInFolder(
                folderId,
                FileName
        );
    }

    public void EsisteFolder(String folderId, String folderName) {
        new Thread(() -> {
            boolean rit = VariabiliStaticheGoogleDrive.getInstance().getDriveHelper().checkIfFolderExists(
                    folderId,
                    folderName
            );

            VariabiliStaticheGoogleDrive.getInstance().setCheckFolder(rit ? "TRUE" : "FALSE");
        }).start();
    }
}
