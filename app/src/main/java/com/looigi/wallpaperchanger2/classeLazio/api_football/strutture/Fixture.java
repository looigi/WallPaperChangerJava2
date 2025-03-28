package com.looigi.wallpaperchanger2.classeLazio.api_football.strutture;

import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partite.VenuePartite;

public class Fixture {
    public int id;
    public String referee;
    public String timezone;
    public String date;
    public long timestamp;
    public Periods periods;
    public VenuePartite venue;
    public Status status;
}
