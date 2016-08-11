package com.jeddigital.kerkotaxi.AnroidRestModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Theodhori on 8/10/2016.
 */
public class Vehicle {
    @SerializedName("id")
    public int id;

    @SerializedName("lat")
    public float lat;

    @SerializedName("lng")
    public float lng;

    public Vehicle(){

    }

    public int getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}

