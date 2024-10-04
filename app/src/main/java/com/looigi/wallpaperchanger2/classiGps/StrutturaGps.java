package com.looigi.wallpaperchanger2.classiGps;

public class StrutturaGps {
    private double lat;
    private double lon;
    private String data;
    private String ora;
    private double Altitude;
    private float Speed;
    private float Accuracy;
    private float distanza;
    private boolean wifi;
    private int livelloSegnale;
    private String tipoSegnale;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTipoSegnale() {
        return tipoSegnale;
    }

    public void setTipoSegnale(String tipoSegnale) {
        this.tipoSegnale = tipoSegnale;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public int getLivelloSegnale() {
        return livelloSegnale;
    }

    public void setLivelloSegnale(int livelloSegnale) {
        this.livelloSegnale = livelloSegnale;
    }

    public float getDistanza() {
        return distanza;
    }

    public void setDistanza(float distanza) {
        this.distanza = distanza;
    }

    public double getAltitude() {
        return Altitude;
    }

    public void setAltitude(double altitude) {
        Altitude = altitude;
    }

    public float getSpeed() {
        return Speed;
    }

    public void setSpeed(float speed) {
        Speed = speed;
    }

    public float getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(float accuracy) {
        Accuracy = accuracy;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
