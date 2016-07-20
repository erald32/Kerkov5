package com.jeddigital.kerkotaxi.Models;

/**
 * Created by Theodhori on 7/20/2016.
 */
public class Taxi {
    private int id;
    private float lat;
    private float lng;
    private String photo_img_url;
    private String plate_nr;
    private int status_id;


    public Taxi(){

    }
    public Taxi(int id, float lat, float lng, String photo_img_url, String plate_nr, int status_id){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.photo_img_url = photo_img_url;
        this.plate_nr = plate_nr;
        this.status_id = status_id;
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

    public int getStatus_id() {
        return status_id;
    }

    public String getPhoto_img_url() {
        return photo_img_url;
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

    public void setPhoto_img_url(String photo_img_url) {
        this.photo_img_url = photo_img_url;
    }

    public void setPlate_nr(String plate_nr) {
        this.plate_nr = plate_nr;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }
}
