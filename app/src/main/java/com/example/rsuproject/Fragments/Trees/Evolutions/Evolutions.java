package com.example.rsuproject.Fragments.Trees.Evolutions;

public class Evolutions {
    private int id;
    private String date;
    private String height;
    private String width;
    private String description;
    private int tree_id;
    private int state_id;
    private int user_id;

    public Evolutions(int id, String date, String height, String width, String description, int tree_id, int state_id, int user_id) {
        this.id = id;
        this.date = date;
        this.height = height;
        this.width = width;
        this.description = description;
        this.tree_id = tree_id;
        this.state_id = state_id;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public String getDescription() {
        return description;
    }

    public int getTree_id() {
        return tree_id;
    }

    public int getState_id() {
        return state_id;
    }

    public int getUser_id() {
        return user_id;
    }
}
