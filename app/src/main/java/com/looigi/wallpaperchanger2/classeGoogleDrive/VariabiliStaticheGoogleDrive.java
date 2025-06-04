package com.looigi.wallpaperchanger2.classeGoogleDrive;

import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;

import com.google.api.services.drive.Drive;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheGoogleDrive {
    private static VariabiliStaticheGoogleDrive instance = null;
    // private db_dati_gps db;

    private VariabiliStaticheGoogleDrive() {
    }

    public static VariabiliStaticheGoogleDrive getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheGoogleDrive();
        }

        return instance;
    }

    private String rootId = "";
    public static String wallpaperFolderID = "1arfyRhdmLkyUYCFpsU5DNXICR2IuyNC1";
    public static String apiFootballID = "1bCHQ0iaKa_T70AnpzafPflqBhhFt0E03";
    public static String nuovaVersioneID = "1asjix2w7NKaJLJpyTXIHGu-nW4wybFyO";
    public static String nomeFileAPK = "Wallpaper Changer II.apk";

    private Activity act;
    private GoogleDriveHelper driveHelper;
    private GifImageView imgAttesa;
    private TextView txtDettaglio;
    private boolean Connesso = false;
    private String ErroreConnessione = "";
    private ListView lstFolders;
    private ListView lstFiles;
    private String idFolderCreato;
    private String PathOperazione;
    private String NomeFileApiFootball;
    private String checkFolder;
    private Drive driveService;
    private String OperazioneDaEffettuare;
    private boolean staCheckandoFile = false;
    private boolean staScaricandoFile = false;
    private String fileDiOrigine;
    private String VersioneScaricata;

    public String getVersioneScaricata() {
        return VersioneScaricata;
    }

    public void setVersioneScaricata(String versioneScaricata) {
        VersioneScaricata = versioneScaricata;
    }

    public TextView getTxtDettaglio() {
        return txtDettaglio;
    }

    public void setTxtDettaglio(TextView txtDettaglio) {
        this.txtDettaglio = txtDettaglio;
    }

    public String getFileDiOrigine() {
        return fileDiOrigine;
    }

    public void setFileDiOrigine(String fileDiOrigine) {
        this.fileDiOrigine = fileDiOrigine;
    }

    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }

    public boolean isStaScaricandoFile() {
        return staScaricandoFile;
    }

    public void setStaScaricandoFile(boolean staScaricandoFile) {
        this.staScaricandoFile = staScaricandoFile;
    }

    public boolean isStaCheckandoFile() {
        return staCheckandoFile;
    }

    public void setStaCheckandoFile(boolean staCheckandoFile) {
        this.staCheckandoFile = staCheckandoFile;
    }

    public String getOperazioneDaEffettuare() {
        return OperazioneDaEffettuare;
    }

    public void setOperazioneDaEffettuare(String operazioneDaEffettuare) {
        OperazioneDaEffettuare = operazioneDaEffettuare;
    }

    public Drive getDriveService() {
        return driveService;
    }

    public void setDriveService(Drive driveService) {
        this.driveService = driveService;
    }

    public String getCheckFolder() {
        return checkFolder;
    }

    public void setCheckFolder(String checkFolder) {
        this.checkFolder = checkFolder;
    }

    public String getPathOperazione() {
        return PathOperazione;
    }

    public void setPathOperazione(String pathOperazione) {
        PathOperazione = pathOperazione;
    }

    public String getNomeFileApiFootball() {
        return NomeFileApiFootball;
    }

    public void setNomeFileApiFootball(String nomeFileApiFootball) {
        NomeFileApiFootball = nomeFileApiFootball;
    }

    public String getIdFolderCreato() {
        return idFolderCreato;
    }

    public void setIdFolderCreato(String idFolderCreato) {
        this.idFolderCreato = idFolderCreato;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootID) {
        this.rootId = rootID;
    }

    public ListView getLstFiles() {
        return lstFiles;
    }

    public void setLstFiles(ListView lstFiles) {
        this.lstFiles = lstFiles;
    }

    public GoogleDriveHelper getDriveHelper() {
        return driveHelper;
    }

    public void setDriveHelper(GoogleDriveHelper driveHelper) {
        this.driveHelper = driveHelper;
    }

    public ListView getLstFolders() {
        return lstFolders;
    }

    public void setLstFolders(ListView lstFolders) {
        this.lstFolders = lstFolders;
    }

    public String getErroreConnessione() {
        return ErroreConnessione;
    }

    public void setErroreConnessione(String erroreConnessione) {
        ErroreConnessione = erroreConnessione;
    }

    public boolean isConnesso() {
        return Connesso;
    }

    public void setConnesso(boolean connesso) {
        Connesso = connesso;
    }

    public GifImageView getImgAttesa() {
        return imgAttesa;
    }

    public void setImgAttesa(GifImageView imgAttesa) {
        this.imgAttesa = imgAttesa;
    }
}
