package com.enovatesoft.paperless.models;

import java.util.List;

public class DataRequirements {
    private String subTitle;
    private List<SubSection> subSection;

    public DataRequirements() {
    }

    public DataRequirements(String subTitle, List<SubSection> subSection) {
        this.subTitle = subTitle;
        this.subSection = subSection;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<SubSection> getSubSection() {
        return subSection;
    }

    public void setSubSection(List<SubSection> subSection) {
        this.subSection = subSection;
    }
}
