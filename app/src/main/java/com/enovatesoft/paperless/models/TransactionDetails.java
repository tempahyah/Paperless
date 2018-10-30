package com.enovatesoft.paperless.models;

public class TransactionDetails {

    private String account_name;
    private String account_number;
    private String customerImage;

    private String transaction_idDiaog, transaction_dateDiaog, account_numberDiaog, transaction_typeDiaog,  amountDiaog, channelDiaog, branchDiaog;

    public TransactionDetails() {
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    //Dialog
    public String getTransaction_idDiaog() {
        return transaction_idDiaog;
    }

    public void setTransaction_idDiaog(String transaction_idDiaog) {
        this.transaction_idDiaog = transaction_idDiaog;
    }

    public String getTransaction_dateDiaog() {
        return transaction_dateDiaog;
    }

    public void setTransaction_dateDiaog(String transaction_dateDiaog) {
        this.transaction_dateDiaog = transaction_dateDiaog;
    }

    public String getAccount_numberDiaog() {
        return account_numberDiaog;
    }

    public void setAccount_numberDiaog(String account_numberDiaog) {
        this.account_numberDiaog = account_numberDiaog;
    }

    public String getTransaction_typeDiaog() {
        return transaction_typeDiaog;
    }

    public void setTransaction_typeDiaog(String transaction_typeDiaog) {
        this.transaction_typeDiaog = transaction_typeDiaog;
    }

    public String getAmountDiaog() {
        return amountDiaog;
    }

    public void setAmountDiaog(String amountDiaog) {
        this.amountDiaog = amountDiaog;
    }

    public String getChannelDiaog() {
        return channelDiaog;
    }

    public void setChannelDiaog(String channelDiaog) {
        this.channelDiaog = channelDiaog;
    }

    public String getBranchDiaog() {
        return branchDiaog;
    }

    public void setBranchDiaog(String branchDiaog) {
        this.branchDiaog = branchDiaog;
    }

}
