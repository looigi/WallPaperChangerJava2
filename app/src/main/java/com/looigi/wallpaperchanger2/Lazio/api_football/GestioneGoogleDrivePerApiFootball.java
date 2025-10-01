/* package com.looigi.wallpaperchanger2.classeLazio.api_football;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classeGoogleDrive.UtilityGoogleDrive;
import com.looigi.wallpaperchanger2.classeGoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;

public class GestioneGoogleDrivePerApiFootball {
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;
    private String folderIdAnno;
    private String folderIdOperazione;
    private boolean Scrittura;
    private boolean staLeggendo;

    public void LeggeScriveFileSuGoogleDrive(Context context, String Operazione, String NomeFile, boolean Scrittura) {
        this.Scrittura = Scrittura;

        checkFolderAnno(context, Operazione, NomeFile);

        /* if (!Scrittura) {
            this.staLeggendo = true;

            handlerThread = new HandlerThread("background-thread_GDL_" +
                    VariabiliStaticheWallpaper.channelName);
            handlerThread.start();

            handler = new Handler(handlerThread.getLooper());
            r = new Runnable() {
                public void run() {
                    if (!staLeggendo) {

                    }
                }
            };
            handler.postDelayed(r, 100);
        } * /
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
                    if (VariabiliStaticheGoogleDrive.getInstance().getCheckFolder().equals("TRUE")) {
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
                                        if (Scrittura) {
                                            CreaFolderAnno(context, Operazione, NomeFile);
                                        } else {
                                            staLeggendo = false;
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
                        if (Scrittura) {
                            CreaFolderAnno(context, Operazione, NomeFile);
                        } else {
                            staLeggendo = false;
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

    private void CreaFolderAnno(Context context, String Operazione, String NomeFile) {
        UtilityGoogleDrive.getInstance().createFolder(
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
                    if (VariabiliStaticheGoogleDrive.getInstance().getCheckFolder().equals("TRUE")) {
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

                                        if (Scrittura) {
                                            ScriveFile(context, Operazione, NomeFile);
                                        } else {
                                            LeggeFile(context, Operazione, NomeFile);
                                        }
                                    } else {
                                        if (Scrittura) {
                                            CreaFolderOperazione(context, Operazione, NomeFile);
                                        } else {
                                            staLeggendo = false;
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

    private void LeggeFile(Context context, String Operazione, String NomeFile) {
        VariabiliStaticheGoogleDrive.getInstance().setIdFolderCreato("");

        UtilityGoogleDrive.getInstance().esisteFileInFolder(
                context,
                folderIdOperazione,
                NomeFile
        );

        handlerThread = new HandlerThread("background-thread_GDL2_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().isEmpty()) {
                    if (VariabiliStaticheGoogleDrive.getInstance().getIdFolderCreato().equals("TRUE")) {
                        UtilityApiFootball uf = new UtilityApiFootball();
                        String OperazioneRidotta = uf.RitornaOperazioneSistemata(Operazione);

                        Files.getInstance().CreaCartelle(
                                VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale() )+ "/" +
                                        OperazioneRidotta
                        );

                        VariabiliStaticheGoogleDrive.getInstance().setStaScaricandoFile(true);

                        String Destinazione = VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale() )+ "/" +
                                OperazioneRidotta + "/" + NomeFile;
                        UtilityGoogleDrive.getInstance().dowload(
                                folderIdOperazione,
                                NomeFile,
                                Destinazione
                        );

                        handlerThread = new HandlerThread("background-thread_GDLE_" +
                                VariabiliStaticheWallpaper.channelName);
                        handlerThread.start();

                        handler = new Handler(handlerThread.getLooper());
                        r = new Runnable() {
                            public void run() {
                                if (!VariabiliStaticheGoogleDrive.getInstance().isStaScaricandoFile()) {
                                    VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(false);
                                    VariabiliStaticheGoogleDrive.getInstance().getAct().finish();
                                    staLeggendo = false;
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

        String[] S = Nome.split("/");
        String SoloNome = S[S.length - 1];

        String idFileCaricato = UtilityGoogleDrive.getInstance().upload(
                context,
                Path + Nome,
                SoloNome,
                folderIdOperazione
        );

        VariabiliStaticheGoogleDrive.getInstance().setStaScaricandoFile(true);

        Files.getInstance().EliminaFileUnico(NomeFile);

        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
        VariabiliStaticheGoogleDrive.getInstance().getAct().finish();

        /* handlerThread = new HandlerThread("background-thread_IF_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().isStaScaricandoFile()) {
                    Files.getInstance().EliminaFileUnico(NomeFile);

                    VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(false);
                    VariabiliStaticheGoogleDrive.getInstance().getAct().finish();
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 100);
                    }
                }
            }
        };
        handler.postDelayed(r, 100); * /
    }
}
*/