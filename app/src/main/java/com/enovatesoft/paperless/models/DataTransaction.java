package com.enovatesoft.paperless.models;

import java.util.List;
public class DataTransaction {

    private String title;
    private List<TransactionDetails> section;

    public DataTransaction() {
    }

    public DataTransaction(String title, List<TransactionDetails> section) {
        this.title = title;
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TransactionDetails> getSection() {
        return section;
    }

    public void setSection(List<TransactionDetails> section) {
        this.section = section;
    }

}
