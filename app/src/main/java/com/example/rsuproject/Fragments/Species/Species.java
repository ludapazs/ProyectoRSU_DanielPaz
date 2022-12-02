package com.example.rsuproject.Fragments.Species;

public class Species {

    private int id;
    private String name;
    private String description;
    private int family_id;

    public Species(int id, String name, String description, int family_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.family_id = family_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getFamily_id() {
        return family_id;
    }
}
