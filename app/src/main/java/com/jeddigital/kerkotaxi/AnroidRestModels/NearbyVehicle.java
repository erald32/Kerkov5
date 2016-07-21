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

    private class DistanceParam{

        @SerializedName("distance_in_meters")
        public int distance_in_meters;

        @SerializedName("time_in_seconds")
        public int time_in_seconds;

        @SerializedName("distance_readable")
        public String distance_readable;

        @SerializedName("time_readable")
        public String time_readable;

        public DistanceParam(int distance_in_meters, int time_in_seconds, String distance_readable, String time_readable){
            this.distance_in_meters = distance_in_meters;
            this.distance_readable = distance_readable;
            this.time_in_seconds = time_in_seconds;
            this.time_readable = time_readable;
        }

        public int getDistance_in_meters() {
            return distance_in_meters;
        }

        public int getTime_in_seconds() {
            return time_in_seconds;
        }

        public String getDistance_readable() {
            return distance_readable;
        }

        public String getTime_readable() {
            return time_readable;
        }

        public void setDistance_in_meters(int distance_in_meters) {
            this.distance_in_meters = distance_in_meters;
        }

        public void setDistance_readable(String distance_readable) {
            this.distance_readable = distance_readable;
        }

        public void setTime_in_seconds(int time_in_seconds) {
            this.time_in_seconds = time_in_seconds;
        }

        public void setTime_readable(String time_readable) {
            this.time_readable = time_readable;
        }
    }
}
