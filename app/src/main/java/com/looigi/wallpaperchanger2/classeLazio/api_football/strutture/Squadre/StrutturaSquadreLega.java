package com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Squadre;

import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Paging;

import java.util.List;

public class StrutturaSquadreLega {
    public String get;
    public Parameters parameters;
    public List<String> errors;
    public int results;
    public Paging paging;
    public List<TeamResponse> response;
}