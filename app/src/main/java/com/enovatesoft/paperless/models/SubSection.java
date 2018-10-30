package com.enovatesoft.paperless.models;

public class SubSection {

    private String name;
    private String sid;
    private String image;

    public SubSection() {
    }

    public SubSection(String name,String sid, String image) {
        this.name = name;
        this.sid = sid;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
