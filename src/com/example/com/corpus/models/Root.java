package com.example.com.corpus.models;

public class Root {
    private int id;
    private String root;
    private boolean inQuran;

    public boolean isInQuran() {
        return inQuran;
    }

    public void setInQuran(boolean inQuran) {
        this.inQuran = inQuran;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}
