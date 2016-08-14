package com.jeddigital.kerkotaxi.AnroidRestModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by theodhori on 8/14/2016.
 */
public class BookingLatLngPosition {

    @SerializedName("booking_id")
    int booking_id;

    @SerializedName("lat_lng_booking_index")
    int lat_lng_booking_index;

    @SerializedName("lat")
    double lat;

    @SerializedName("lng")
    double lng;

    @SerializedName("lat_lng_time")
    String lat_lng_time;

    @SerializedName("lat_lng_booking_status")
    int lat_lng_booking_status;

    public int getBooking_id() {
        return booking_id;
    }

    public int getLat_lng_booking_index() {
        return lat_lng_booking_index;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getLat_lng_time() {
        return lat_lng_time;
    }

    public int getLat_lng_booking_status() {
        return lat_lng_booking_status;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public void setLat_lng_booking_index(int lat_lng_booking_index) {
        this.lat_lng_booking_index = lat_lng_booking_index;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat_lng_time(String lat_lng_time) {
        this.lat_lng_time = lat_lng_time;
    }

    public void setLat_lng_booking_status(int lat_lng_booking_status) {
        this.lat_lng_booking_status = lat_lng_booking_status;
    }
}
