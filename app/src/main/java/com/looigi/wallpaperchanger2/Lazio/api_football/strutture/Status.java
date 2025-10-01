package com.looigi.wallpaperchanger2.Lazio.api_football.strutture;

import com.google.gson.annotations.SerializedName;

public class Status {
    public String longStatus;
    public String shortStatus;
    public Integer elapsed;
    public Object extra;

    @SerializedName("long")
    public void setLongStatus(String longStatus) {
        this.longStatus = longStatus;
    }

    @SerializedName("short")
    public void setShortStatus(String shortStatus) {
        this.shortStatus = shortStatus;
    }
}
