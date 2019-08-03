package com.example.kspartner;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String id;
    private String name;
    private String address;
    private String contact;
    private String created_date;
    private String latitude;
    private String longitude;
    private String menu_id;



    public Restaurant() {}

    public Restaurant(String id, String name, String address, String contact, String created_date, String latitude, String longitude, String menu_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.created_date = created_date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.menu_id = menu_id;
    }

    public String getid() {
        return id;
    }

    public void setR_id(String id) {
        this.id = id;
    }

    public void setR_name(String name) {
        this.name = name;
    }

    public void setR_address(String address) {
        this.address = address;
    }

    public void setR_contact(String contact) {
        this.contact = contact;
    }

    public void setR_created_date(String created_date) {
        this.created_date = created_date;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }


    public String getname() {
        return name;
    }

    public String getaddress() {
        return address;
    }

    public String getcontact() {
        return contact;
    }

    public String getcreated_date() {
        return created_date;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMenu_id() {
        return menu_id;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", created_date='" + created_date + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", menu_id='" + menu_id + '\'' +
                '}';
    }
}


