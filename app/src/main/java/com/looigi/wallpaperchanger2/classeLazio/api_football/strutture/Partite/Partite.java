package com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partite;

import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Paging;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.ParametersPartita;

import java.util.List;

public class Partite {
    public String get;
    public ParametersPartita parameters;
    public List<String> errors;
    public int results;
    public Paging paging;
    public List<FixtureData> response;
}
