package com.example.rsuproject.Fragments.Trees.Procedures.Responsibles;

public class Responsibles {
    private int id;
    private String dni;
    private String name;
    private String lastname;

    public Responsibles(int id, String dni, String name, String lastname) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }
}
