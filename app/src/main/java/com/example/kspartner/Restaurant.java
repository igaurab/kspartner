package com.example.kspartner;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String r_id;
    private String r_name;
    private String r_address;
    private String r_contact;
    private String r_created_date;

    public Restaurant() {}

    public Restaurant(String r_id, String r_name, String r_address, String r_contact, String r_created_date) {
        this.r_id = r_id;
        this.r_name = r_name;
        this.r_address = r_address;
        this.r_contact = r_contact;
        this.r_created_date = r_created_date;
    }

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public void setR_name(String r_name) {
        this.r_name = r_name;
    }

    public void setR_address(String r_address) {
        this.r_address = r_address;
    }

    public void setR_contact(String r_contact) {
        this.r_contact = r_contact;
    }

    public void setR_created_date(String r_created_date) {
        this.r_created_date = r_created_date;
    }

    public String getR_name() {
        return r_name;
    }

    public String getR_address() {
        return r_address;
    }

    public String getR_contact() {
        return r_contact;
    }

    public String getR_created_date() {
        return r_created_date;
    }
}


