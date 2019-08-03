package com.example.kspartner;

public class Restaurant_owner_info {
    private String uid;
    private String emailId;
    private String rid;

    public Restaurant_owner_info() {
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Restaurant_owner_info(String uid, String emailId) {
        this.uid = uid;
        this.emailId = emailId;
    }

    public String getUid() {
        return uid;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
