package com.looigi.wallpaperchanger2.classePazzia;

import android.widget.ImageView;
import android.widget.VideoView;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStatichePazzia {
    private static VariabiliStatichePazzia instance = null;

    private VariabiliStatichePazzia() {
    }

    public static VariabiliStatichePazzia getInstance() {
        if (instance == null) {
            instance = new VariabiliStatichePazzia();
        }

        return instance;
    }

    private ImageView imgPennetta;
    private ImageView imgImmagini;
    private VideoView videoView;
    private GifImageView imgCaricamentoPEN;
    private GifImageView imgCaricamentoIMM;
    private GifImageView imgCaricamentoVID;

    public GifImageView getImgCaricamentoPEN() {
        return imgCaricamentoPEN;
    }

    public void setImgCaricamentoPEN(GifImageView imgCaricamentoPEN) {
        this.imgCaricamentoPEN = imgCaricamentoPEN;
    }

    public GifImageView getImgCaricamentoIMM() {
        return imgCaricamentoIMM;
    }

    public void setImgCaricamentoIMM(GifImageView imgCaricamentoIMM) {
        this.imgCaricamentoIMM = imgCaricamentoIMM;
    }

    public GifImageView getImgCaricamentoVID() {
        return imgCaricamentoVID;
    }

    public void setImgCaricamentoVID(GifImageView imgCaricamentoVID) {
        this.imgCaricamentoVID = imgCaricamentoVID;
    }

    public ImageView getImgPennetta() {
        return imgPennetta;
    }

    public void setImgPennetta(ImageView imgPennetta) {
        this.imgPennetta = imgPennetta;
    }

    public ImageView getImgImmagini() {
        return imgImmagini;
    }

    public void setImgImmagini(ImageView imgImmagini) {
        this.imgImmagini = imgImmagini;
    }

    public VideoView getVideoView() {
        return videoView;
    }

    public void setVideoView(VideoView videoView) {
        this.videoView = videoView;
    }
}
