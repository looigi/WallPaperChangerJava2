package com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori;

import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.ResponseGiocatori;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Paging;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Squadre.Parameters;

import java.util.List;

public class GiocatoriPartita {
    public ParametersGiocatori parameters;
    private int results;
    private Paging paging;
    public List<ResponseGiocatori> response;
}
