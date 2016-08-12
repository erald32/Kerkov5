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

    @SerializedName("distance_params")
    DistanceParam distance_params;


    public Vehicle() {

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

    public DistanceParam getDistance_params() {
        return distance_params;
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

    public void setDistance_params(DistanceParam distance_params) {
        this.distance_params = distance_params;
    }
}

