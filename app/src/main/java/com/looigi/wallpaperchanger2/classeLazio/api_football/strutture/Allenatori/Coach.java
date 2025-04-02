package com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Allenatori;

import java.util.List;

public class Coach {
    public int id;
    public String name;
    public String firstname;
    public String lastname;
    private int age;
    private Birth birth;
    private String nationality;
    private String height;
    private String weight;
    private String photo;
    private TeamAllenatori team;
    private List<Career> career;
}
