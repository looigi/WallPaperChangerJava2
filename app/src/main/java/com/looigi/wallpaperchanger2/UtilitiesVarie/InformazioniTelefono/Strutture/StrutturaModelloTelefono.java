package com.looigi.wallpaperchanger2.UtilitiesVarie.InformazioniTelefono.Strutture;

import android.os.Build;

public class StrutturaModelloTelefono {
    private String Type;
    private String Model;
    private String Device;
    private String Product;
    private String Manufacturer;
    private String ANDROID_ID;

    public String getANDROID_ID() {
        return ANDROID_ID;
    }

    public void setANDROID_ID(String ANDROID_ID) {
        this.ANDROID_ID = ANDROID_ID;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
