package com.looigi.wallpaperchanger2.classeLazio.api_football;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.looigi.wallpaperchanger2.classeGoogleDrive.UtilityGoogleDrive;
import com.looigi.wallpaperchanger2.classeGoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;

public class GestioneGoogleDrivePerApiFootball {
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;
    private String folderIdAnno;
    private String folderIdOperazione;

    public void ScriveFileSuGoogleDrive(Context context, String Operazione, String NomeFile) {
        checkFolderAnno(context, Operazione, NomeFile);
    }

    private void checkFolderAnno(Context context, String Operazione, String NomeFile) {
        VariabiliStaticheGoogleDrive.getInstance().setCheckFolder("");

        UtilityGoogleDrive.getInstance().EsisteFolder(
                VariabiliStaticheGoogleDrive.apiFootballID,
                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale())
        );

        handlerThread = new HandlerThread("background-thread_GD0_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getCheckFolder().isEmpty()) {
                    if (!VariabiliStaticheGoogleDrive.getInstance().getCheckFolder().equals("TRUE")) {
                        UtilityGoogleDrive.getInstance().returnIdFolder(
                                VariabiliStaticheGoogleDrive.apiFootballID,
                                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale())
                        );

                        handlerThread = new HandlerThread("background-thread_GD1_" +
                                VariabiliStaticheWallpaper.channelName);
                        handlerThread.start();

                        handler = new Handler(handlerThread.getLooper());
                        r = new Runnable() {
                            public void run() {
                                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                                    if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().contains("ERROR")) {
                                        folderIdAnno = VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato();

                                        checkFolderOperazione(context, Operazione, NomeFile);
                                    } else {
                                        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
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
                        CreaFolderAnno(context, Operazione, NomeFile);
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

    private void CreaFolderAnno(Context context, String Operazione, String NomeFile) {
        UtilityGoogleDrive.getInstance().createFolder(
                VariabiliStaticheGoogleDrive.nuovaVersioneID,
                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale())
        );

        handlerThread = new HandlerThread("background-thread_GD1_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                    if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().contains("ERROR")) {
                        folderIdAnno = VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato();
                        checkFolderOperazione(context, Operazione, NomeFile);
                    } else {
                        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
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

    private void checkFolderOperazione(Context context, String Operazione, String NomeFile) {
        VariabiliStaticheGoogleDrive.getInstance().setCheckFolder("");

        UtilityGoogleDrive.getInstance().EsisteFolder(
                folderIdAnno,
                Operazione
        );

        handlerThread = new HandlerThread("background-thread_GD2_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getCheckFolder().isEmpty()) {
                    if (!VariabiliStaticheGoogleDrive.getInstance().getCheckFolder().equals("TRUE")) {
                        UtilityGoogleDrive.getInstance().returnIdFolder(
                                folderIdAnno,
                                Operazione
                        );

                        handlerThread = new HandlerThread("background-thread_GD3_" +
                                VariabiliStaticheWallpaper.channelName);
                        handlerThread.start();

                        handler = new Handler(handlerThread.getLooper());
                        r = new Runnable() {
                            public void run() {
                                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                                    if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().contains("ERROR")) {
                                        folderIdOperazione = VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato();

                                        ScriveFile(context, Operazione, NomeFile);
                                    } else {
                                        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
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
                        CreaFolderOperazione(context, Operazione, NomeFile);
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

    private void CreaFolderOperazione(Context context, String Operazione, String NomeFile) {
        UtilityGoogleDrive.getInstance().createFolder(
                folderIdAnno,
                Operazione
        );

        handlerThread = new HandlerThread("background-thread_GD4_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                    if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().contains("ERROR")) {
                        folderIdOperazione = VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato();

                        ScriveFile(context, Operazione, NomeFile);
                    } else {
                        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
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

    private void ScriveFile(Context context, String Operazione, String NomeFile) {
        String Path = VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                Operazione + "/";
        String Nome = NomeFile.replace(" ", "_");
        String idFileCaricato = UtilityGoogleDrive.getInstance().upload(
                context,
                Path,
                Nome,
                folderIdOperazione
        );

        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
    }
}
