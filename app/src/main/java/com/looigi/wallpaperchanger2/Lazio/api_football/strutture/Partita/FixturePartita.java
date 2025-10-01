package com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Partita;

import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Periods;

public class FixturePartita {
    public int id;
    public String timezone;
    public String date;
    public long timestamp;
    public Periods periods;
    public VenuePartita venue;
    public StatusPartita status;
}
