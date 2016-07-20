package com.jeddigital.kerkotaxi.AnroidRestModels;


import com.google.gson.annotations.SerializedName;

/**
 * Created by theodhori on 7/20/2016.
 */
public class NearbyVehicle {

    @SerializedName("id")
    public int id;

    @SerializedName("lat")
    public float lat;

    @SerializedName("lng")
    public float lng;

    @SerializedName("photo_url")
    public String photo_url;

    @SerializedName("plate_number")
    public String plate_nr;


    public NearbyVehicle(){

    }
    public NearbyVehicle(int id, float lat, float lng, String photo_url, String plate_nr){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.photo_url = photo_url;
        this.plate_nr = plate_nr;
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

    public String getPhoto_url() {
        return photo_url;
    }

    public String getPlate_nr() {
        return plate_nr;
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

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public void setPlate_nr(String plate_nr) {
        this.plate_nr = plate_nr;
    }

}
