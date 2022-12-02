package com.example.rsuproject.Fragments.Trees.Procedures.Types;

public class ProceduresTypes {
    private int id;
    private String name;
    private String description;

    public ProceduresTypes(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
}
