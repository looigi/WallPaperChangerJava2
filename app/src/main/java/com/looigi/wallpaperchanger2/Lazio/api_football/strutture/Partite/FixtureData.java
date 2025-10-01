package com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Partite;

import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Fixture;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Partita.Goals;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Partita.League;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Score;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Partita.Teams;

import java.util.List;

public class FixtureData {
    public Fixture fixture;
    public League league;
    public Teams teams;
    public Goals goals;
    public Score score;
    public List<Object> events;
    public List<Object> lineups;
    public List<Object> statistics;
    public List<Object> players;
}
