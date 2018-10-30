package com.enovatesoft.paperless.models;

public class Section {

    private String name;
    private String uId;
    private String sID;
    private String image;

    public Section() {
    }

    public Section(String name, String uId, String sID, String image) {
        this.name = name;
        this.uId = uId;
        this.sID = sID;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
