package com.jeddigital.kerkotaxi.AnroidRestModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by theodhori on 8/11/2016.
 */
public class DistanceParam{

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