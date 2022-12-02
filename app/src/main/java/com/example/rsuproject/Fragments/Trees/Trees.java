package com.example.rsuproject.Fragments.Trees;

public class Trees {

    private int id;
    private String name;
    private String birth_date;
    private String planting_date;
    private String description;
    private String latitude;
    private String longitude;

    private Integer specie_id;
    private Integer zone_id;
    private Integer user_id;
    private String family_name;
    private String species_name;

    public Trees(int id, String name, String birth_date, String planting_date, String description, String latitude, String longitude, Integer specie_id, Integer zone_id, Integer user_id, String family_name, String species_name) {
        this.id = id;
        this.name = name;
        this.birth_date = birth_date;
        this.planting_date = planting_date;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.specie_id = specie_id;
        this.zone_id = zone_id;
        this.user_id = user_id;
        this.family_name = family_name;
        this.species_name = species_name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getPlanting_date() {
        return planting_date;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Integer getSpecie_id() {
        return specie_id;
    }

    public Integer getZone_id() {
        return zone_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getSpecies_name() {
        return species_name;
    }
}
