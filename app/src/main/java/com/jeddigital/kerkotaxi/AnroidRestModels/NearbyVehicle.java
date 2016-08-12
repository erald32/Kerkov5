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

    @SerializedName("car_model")
    public String car_model;

    @SerializedName("conditioner")
    public boolean conditioner;

    @SerializedName("description")
    public String description;

    @SerializedName("driver")
    Driver driver;

    @SerializedName("distance_params")
    DistanceParam distance_params;

    public NearbyVehicle(){

    }
    public NearbyVehicle(int id, float lat, float lng, String photo_url, String plate_nr, String car_model, boolean conditioner, String description, Driver driver, DistanceParam distance_params){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.photo_url = photo_url;
        this.plate_nr = plate_nr;
        this.car_model = car_model;
        this.conditioner = conditioner;
        this.description = description;
        this.driver = driver;
        this.distance_params = distance_params;
    }

    public int getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public String getCar_model() {
        return car_model;
    }

    public boolean isConditioner() {
        return conditioner;
    }

    public String getDescription() {
        return description;
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

    public Driver getDriver() {
        return driver;
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

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public void setConditioner(boolean conditioner) {
        this.conditioner = conditioner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public void setPlate_nr(String plate_nr) {
        this.plate_nr = plate_nr;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setDistance_params(DistanceParam distance_params) {
        this.distance_params = distance_params;
    }
}
