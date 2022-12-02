package com.example.rsuproject.Fragments.Trees.Procedures;

public class Procedures {
    private int id;
    private String date;
    private String description;
    private int procedure_type_id;
    private int tree_id;
    private int responsible_id;
    private int user_id;
    private String description_type;

    public Procedures(int id, String date, String description, int procedure_type_id, int tree_id, int responsible_id, int user_id, String description_type) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.procedure_type_id = procedure_type_id;
        this.tree_id = tree_id;
        this.responsible_id = responsible_id;
        this.user_id = user_id;
        this.description_type = description_type;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getProcedure_type_id() {
        return procedure_type_id;
    }

    public int getTree_id() {
        return tree_id;
    }

    public int getResponsible_id() {
        return responsible_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDescription_type() {
        return description_type;
    }
}
