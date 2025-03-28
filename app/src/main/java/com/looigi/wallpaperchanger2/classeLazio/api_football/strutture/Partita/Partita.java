package com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partita;

import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Paging;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.ParametersPartita;

import java.util.List;

public class Partita {
    public String get;
    public ParametersPartita parameters;
    public List<Object> errors;
    public int results;
    public Paging paging;
    public List<ResponsePartita> response;
}
