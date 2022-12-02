package com.example.rsuproject.Fragments.Responsibles;

public class Responsibles {

    private int id;
    private String names, dni;

    public Responsibles(int id, String names, String dni) {
        this.id = id;
        this.names = names;
        this.dni = dni;
    }

    public int getId() {
        return id;
    }

    public String getNames() {
        return names;
    }

    public String getDni() {
        return dni;
    }
}
