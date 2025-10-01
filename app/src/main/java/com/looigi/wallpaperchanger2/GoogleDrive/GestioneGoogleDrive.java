package com.looigi.wallpaperchanger2.GoogleDrive;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.looigi.wallpaperchanger2.Lazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class GestioneGoogleDrive {
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;
    private String Scrittura;
    private boolean staLeggendo;
    private List<String> Operazioni;
    private List<String> folderId;
    private int idOperazione;
    private boolean EliminaOrigine;
    private Activity Chiamante;

    public void GestioneFileSuGoogleDrive(Context context, String Operazione, String NomeFile, String Scrittura,
                                          boolean EliminaOrigine, Activity act) {
        this.Scrittura = Scrittura;
        this.EliminaOrigine = EliminaOrigine;
        this.Chiamante = act;

        if (Scrittura.equals("SCRITTURA")) {
            if (VariabiliStaticheGoogleDrive.getInstance().getFileDiOrigine().isEmpty()) {
                UtilitiesGlobali.getInstance().ApreToast(context, "File di origine non valorizzato");
                VariabiliStaticheGoogleDrive.getInstance().getAct().finish();
                return;
            } else {
                if (!Files.getInstance().EsisteFile(VariabiliStaticheGoogleDrive.getInstance().getFileDiOrigine())) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "File di origine non esistente");
                    VariabiliStaticheGoogleDrive.getInstance().getAct().finish();
                    return;
                }
            }
        }

        Operazioni = new ArrayList<>();
        folderId = new ArrayList<>();

        folderId.add(VariabiliStaticheGoogleDrive.wallpaperFolderID);

        if (Operazione.contains("/")) {
            String[] O = Operazione.split("/");
            for (String Oper : O) {
                Operazioni.add(Oper);
            }
        } else {
            Operazioni.add(Operazione);
        }

        idOperazione = 0;
        checkFolder(context, NomeFile);
    }

    private void checkFolder(Context context, String NomeFile) {
        String Operazione = Operazioni.get(idOperazione);
        VariabiliStaticheGoogleDrive.getInstance().setCheckFolder("");

        UtilityGoogleDrive.getInstance().EsisteFolder(
                folderId.get(idOperazione),
                Operazione
        );

        handlerThread = new HandlerThread("background-thread_CheckFolder_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getCheckFolder().isEmpty()) {
                    if (VariabiliStaticheGoogleDrive.getInstance().getCheckFolder().equals("TRUE")) {
                        UtilityGoogleDrive.getInstance().returnIdFolder(
                                folderId.get(idOperazione),
                                Operazione
                        );

                        handlerThread = new HandlerThread("background-thread_GetIdFolder_" +
                                VariabiliStaticheWallpaper.channelName);
                        handlerThread.start();

                        handler = new Handler(handlerThread.getLooper());
                        r = new Runnable() {
                            public void run() {
                                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                                    if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().contains("ERROR")) {
                                        folderId.add(VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato());

                                        idOperazione++;
                                        if (idOperazione < Operazioni.size()) {
                                            checkFolder(context, NomeFile);
                                        } else {
                                            switch(Scrittura) {
                                                case "ELIMINA":
                                                    EliminaFile(context, NomeFile);
                                                    break;
                                                case "LETTURA":
                                                    LeggeFile(context, NomeFile);
                                                    break;
                                                case "SCRITTURA":
                                                    ScriveFile(context, NomeFile);
                                                    break;
                                            }
                                        }
                                    } else {
                                        switch(Scrittura) {
                                            case "ELIMINA":
                                                VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(false);
                                                staLeggendo = false;
                                                Chiamante.finish();
                                                break;
                                            case "LETTURA":
                                                VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(false);
                                                staLeggendo = false;
                                                Chiamante.finish();
                                                break;
                                            case "SCRITTURA":
                                                CreaFolder(context, NomeFile);
                                                break;
                                        }
                                    }
                                } else {
                                    if (handler != null) {
                                        handler.postDelayed(this, 100);
                                    }
                                }
                            }
                        };
                        handler.postDelayed(r, 100);
                    } else {
                        switch(Scrittura) {
                            case "ELIMINA":
                                VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(false);
                                staLeggendo = false;
                                Chiamante.finish();
                                break;
                            case "LETTURA":
                                VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(false);
                                staLeggendo = false;
                                Chiamante.finish();
                                break;
                            case "SCRITTURA":
                                CreaFolder(context, NomeFile);
                                break;
                        }
                    }
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 100);
                    }
                }
            }
        };
        handler.postDelayed(r, 100);
    }

    private void CreaFolder(Context context, String NomeFile) {
        String Operazione = Operazioni.get(idOperazione);

        UtilityGoogleDrive.getInstance().createFolder(
                folderId.get(idOperazione),
                Operazione
        );

        handlerThread = new HandlerThread("background-thread_CreateFolder_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                    if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().contains("ERROR")) {
                        folderId.add(VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato());

                        idOperazione++;
                        if (idOperazione < Operazioni.size()) {
                            checkFolder(context, NomeFile);
                        } else {
                            switch(Scrittura) {
                                case "ELIMINA":
                                    EliminaFile(context, NomeFile);
                                    break;
                                case "LETTURA":
                                    LeggeFile(context, NomeFile);
                                    break;
                                case "SCRITTURA":
                                    ScriveFile(context, NomeFile);
                                    break;
                            }
                        }
                    } else {
                        // Errore nella creazione del folder... Gestire
                    }
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 100);
                    }
                }
            }
        };
        handler.postDelayed(r, 100);
    }

    private void LeggeFile(Context context, String NomeFile) {
        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("");

        UtilityGoogleDrive.getInstance().esisteFileInFolder(
                context,
                folderId.get(idOperazione),
                NomeFile
        );

        handlerThread = new HandlerThread("background-thread_LeggeFile_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                    if (VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().equals("TRUE")) {
                        String Path2 = "";
                        for (int i = 0; i < Operazioni.size(); i++) {
                            String Operazione = Operazioni.get(i);

                            Path2 += Operazione + "/";
                        }
                        if (!Path2.isEmpty()) {
                            Path2 = Path2.substring(0, Path2.length() - 1);
                        }

                        Files.getInstance().CreaCartelle(
                                context.getFilesDir() + "/" + Path2 + "/"
                        );

                        VariabiliStaticheGoogleDrive.getInstance().setStaScaricandoFile(true);

                        String Destinazione = context.getFilesDir() + "/" + Path2 + "/" + NomeFile;
                        UtilityGoogleDrive.getInstance().dowload(
                                folderId.get(idOperazione),
                                NomeFile,
                                Destinazione
                        );

                        handlerThread = new HandlerThread("background-thread_Download_" +
                                VariabiliStaticheWallpaper.channelName);
                        handlerThread.start();

                        handler = new Handler(handlerThread.getLooper());
                        r = new Runnable() {
                            public void run() {
                                if (!VariabiliStaticheGoogleDrive.getInstance().isStaScaricandoFile()) {
                                    VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(false);
                                    VariabiliStaticheGoogleDrive.getInstance().getAct().finish();
                                    staLeggendo = false;
                                    Chiamante.finish();
                                } else {
                                    if (handler != null) {
                                        handler.postDelayed(this, 100);
                                    }
                                }
                            }
                        };
                        handler.postDelayed(r, 100);
                    } else {
                        VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(false);
                        VariabiliStaticheGoogleDrive.getInstance().getAct().finish();
                        staLeggendo = false;
                        Chiamante.finish();
                    }
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 100);
                    }
                }
            }
        };
        handler.postDelayed(r, 100);
    }

    private void ScriveFile(Context context, String NomeFile) {
        String Nome = NomeFile.replace(" ", "_");

        String[] S = Nome.split("/");
        String SoloNome = S[S.length - 1];

        String idFileCaricato = UtilityGoogleDrive.getInstance().upload(
                context,
                VariabiliStaticheGoogleDrive.getInstance().getFileDiOrigine(),
                SoloNome,
                folderId.get(idOperazione)
        );

        VariabiliStaticheGoogleDrive.getInstance().setStaScaricandoFile(true);

        if (!idFileCaricato.isEmpty() && !idFileCaricato.contains("ERROR")){
            if (EliminaOrigine) {
                Files.getInstance().EliminaFileUnico(
                        VariabiliStaticheGoogleDrive.getInstance().getFileDiOrigine()
                );
            }
        }

        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
        VariabiliStaticheGoogleDrive.getInstance().getAct().finish();
    }

    private void EliminaFile(Context context, String NomeFile) {
        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("");

        UtilityGoogleDrive.getInstance().esisteFileInFolder(
                context,
                folderId.get(idOperazione),
                NomeFile
        );

        handlerThread = new HandlerThread("background-thread_EliminaFile_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                    if (VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().equals("TRUE")) {
                        VariabiliStaticheGoogleDrive.getInstance().setStaScaricandoFile(true);

                        UtilityGoogleDrive.getInstance().deleteFileOrFolder(
                                folderId.get(idOperazione),
                                NomeFile
                        );
                    } else {
                        VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(false);
                        VariabiliStaticheGoogleDrive.getInstance().getAct().finish();
                        staLeggendo = false;
                        Chiamante.finish();
                    }
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 100);
                    }
                }
            }
        };
        handler.postDelayed(r, 100);
    }
}
