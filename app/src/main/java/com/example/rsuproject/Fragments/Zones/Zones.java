package com.example.rsuproject.Fragments.Zones;

public class Zones {

    private int id;
    private int area;
    private String description;
    private String name;

    public Zones(int id, int area, String description, String name) {
        this.id = id;
        this.area = area;
        this.description = description;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getArea() {
        return area;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
