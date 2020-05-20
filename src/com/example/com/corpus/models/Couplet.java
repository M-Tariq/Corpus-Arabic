package com.example.com.corpus.models;

public class Couplet {
    private int ID;
    private int poemID;
    private String line1;
    private String line2;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPoemID() {
        return poemID;
    }

    public void setPoemID(int poemID) {
        this.poemID = poemID;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    @Override
    public String toString() {
        return line1+"\t"+line2;
    }
}
