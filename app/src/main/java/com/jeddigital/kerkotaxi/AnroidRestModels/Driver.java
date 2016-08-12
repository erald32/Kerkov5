package com.jeddigital.kerkotaxi.AnroidRestModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Theodhori on 7/21/2016.
 */
public class Driver {

    @SerializedName("id")
    int id;

    @SerializedName("first_name")
    String first_name;

    @SerializedName("last_name")
    String last_name;

    @SerializedName("photo_url")
    String photo_url;

    @SerializedName("personal_number")
    String driver_personal_number;

    public Driver(){

    }

    public Driver(int id, String first_name, String last_name, String photo_url, String driver_personal_number){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.photo_url = photo_url;
        this.driver_personal_number = driver_personal_number;
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getDriver_personal_number() {
        return driver_personal_number;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public void setDriver_personal_number(String driver_personal_number) {
        this.driver_personal_number = driver_personal_number;
    }
}
