package com.looigi.wallpaperchanger2.Lazio.api_football.strutture.GiocatoreSquadra;

import java.util.List;
import java.util.Map;

public class PlayersTeamsResponse {
    private String get;
    private Map<String, String> parameters;
    private List<String> errors;
    private int results;
    private Paging paging;
    public List<TeamInfo> response;
}
