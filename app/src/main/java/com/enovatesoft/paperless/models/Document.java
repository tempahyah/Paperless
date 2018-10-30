package com.enovatesoft.paperless.models;

public class Document {
    private int mImageDrawable;
    private String document_type;
    private String setting_id;

    public Document() {

    }

    public Document(int mImageDrawable, String document_type, String setting_id) {

        this.mImageDrawable = mImageDrawable;
        this.document_type = document_type;
        this.setting_id = setting_id;

    }

    public int getmImageDrawable() {
        return mImageDrawable;
    }

    public void setmImageDrawable(int mImageDrawable) {
        this.mImageDrawable = mImageDrawable;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getSetting_id() {
        return setting_id;
    }

    public void setSetting_id(String setting_id) {
        this.setting_id = setting_id;
    }
}
