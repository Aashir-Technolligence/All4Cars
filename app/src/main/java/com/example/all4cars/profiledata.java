package com.example.all4cars;

public class profiledata {
    String ID;
    private String NAME;
    private String EMAIL;
    private String IMAGEURL;

    public profiledata() {
    }

    public profiledata(String ID, String NAME, String EMAIL, String IMAGEURL) {
        this.ID = ID;
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.IMAGEURL = IMAGEURL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getIMAGEURL() {
        return IMAGEURL;
    }

    public void setIMAGEURL(String IMAGEURL) {
        this.IMAGEURL = IMAGEURL;
    }
}
