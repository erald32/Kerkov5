package com.jeddigital.kerkotaxi.AnroidRestModels;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Theodhori on 8/10/2016.
 */
public class CheckRequestResponse {

    @SerializedName("id")
    int id;

    @SerializedName("client_id")
    String client_id;

    @SerializedName("status_id")
    int status_id;

    @SerializedName("client_requested_lat")
    Double client_requested_lat;

    @SerializedName("client_requested_lng")
    Double client_requested_lng;

    @SerializedName("client_requested_client_lat")
    Double client_requested_client_lat;

    @SerializedName("client_requested_client_lng")
    Double client_requested_client_lng;

    @SerializedName("client_requested_time")
    String client_requested_time;

    @SerializedName("client_requested_vehicle_lat")
    Double client_requested_vehicle_lat;

    @SerializedName("client_requested_vehicle_lng")
    Double client_requested_vehicle_lng;

    @SerializedName("driver_accepted_vehicle_lat")
    Double driver_accepted_vehicle_lat;

    @SerializedName("driver_accepted_vehicle_lng")
    Double driver_accepted_vehicle_lng;

    @SerializedName("driver_accepted_time")
    String driver_accepted_time;

    @SerializedName("driver_accepted_client_lat")
    Double driver_accepted_client_lat;

    @SerializedName("driver_accepted_client_lng")
    Double driver_accepted_client_lng;

    @SerializedName("driver_refused_on_pending_vehicle_lat")
    Double driver_refused_on_pending_vehicle_lat;

    @SerializedName("driver_refused_on_pending_vehicle_lng")
    Double driver_refused_on_pending_vehicle_lng;

    @SerializedName("driver_refused_on_pending_time")
    String driver_refused_on_pending_time;

    @SerializedName("driver_refused_on_pending_client_lat")
    Double driver_refused_on_pending_client_lat;

    @SerializedName("driver_refused_on_pending_client_lng")
    Double driver_refused_on_pending_client_lng;

    @SerializedName("client_refused_on_pending_vehicle_lat")
    Double client_refused_on_pending_vehicle_lat;

    @SerializedName("client_refused_on_pending_vehicle_lng")
    Double client_refused_on_pending_vehicle_lng;

    @SerializedName("client_refused_on_pending_time")
    String client_refused_on_pending_time;

    @SerializedName("client_refused_on_pending_client_lat")
    Double client_refused_on_pending_client_lat;

    @SerializedName("client_refused_on_pending_client_lng")
    Double client_refused_on_pending_client_lng;

    @SerializedName("driver_refused_ongoing_to_client_vehicle_lat")
    Double driver_refused_ongoing_to_client_vehicle_lat;

    @SerializedName("driver_refused_ongoing_to_client_vehicle_lng")
    Double driver_refused_ongoing_to_client_vehicle_lng;

    @SerializedName("driver_refused_ongoing_to_client_time")
    String driver_refused_ongoing_to_client_time;

    @SerializedName("driver_refused_ongoing_to_client_client_lat")
    Double driver_refused_ongoing_to_client_client_lat;

    @SerializedName("driver_refused_ongoing_to_client_client_lng")
    Double driver_refused_ongoing_to_client_client_lng;

    @SerializedName("client_refused_ongoing_to_client_vehicle_lat")
    Double client_refused_ongoing_to_client_vehicle_lat;

    @SerializedName("client_refused_ongoing_to_client_vehicle_lng")
    Double client_refused_ongoing_to_client_vehicle_lng;

    @SerializedName("client_refused_ongoing_to_client_time")
    String client_refused_ongoing_to_client_time;

    @SerializedName("client_refused_ongoing_to_client_client_lat")
    Double client_refused_ongoing_to_client_client_lat;

    @SerializedName("client_refused_ongoing_to_client_client_lng")
    Double client_refused_ongoing_to_client_client_lng;

    @SerializedName("driver_onwaiting_vehicle_lat")
    Double driver_onwaiting_vehicle_lat;

    @SerializedName("driver_onwaiting_vehicle_lng")
    Double driver_onwaiting_vehicle_lng;

    @SerializedName("driver_onwaiting_time")
    String driver_onwaiting_time;

    @SerializedName("driver_onwaiting_client_lat")
    Double driver_onwaiting_client_lat;

    @SerializedName("driver_onwaiting_client_lng")
    Double driver_onwaiting_client_lng;

    @SerializedName("driver_refused_onwaiting_vehicle_lat")
    Double driver_refused_onwaiting_vehicle_lat;

    @SerializedName("driver_refused_onwaiting_vehicle_lng")
    Double driver_refused_onwaiting_vehicle_lng;

    @SerializedName("driver_refused_onwaiting_time")
    String driver_refused_onwaiting_time;

    @SerializedName("driver_refused_onwaiting_client_lat")
    Double driver_refused_onwaiting_client_lat;

    @SerializedName("driver_refused_onwaiting_client_lng")
    Double driver_refused_onwaiting_client_lng;

    @SerializedName("client_refused_onwaiting_vehicle_lat")
    Double client_refused_onwaiting_vehicle_lat;

    @SerializedName("client_refused_onwaiting_vehicle_lng")
    Double client_refused_onwaiting_vehicle_lng;

    @SerializedName("client_refused_onwaiting_time")
    String client_refused_onwaiting_time;

    @SerializedName("client_refused_onwaiting_client_lat")
    Double client_refused_onwaiting_client_lat;

    @SerializedName("client_refused_onwaiting_client_lng")
    Double client_refused_onwaiting_client_lng;

    @SerializedName("client_in_client_lat")
    Double client_in_client_lat;

    @SerializedName("client_in_client_lng")
    Double client_in_client_lng;

    @SerializedName("client_in_client_time")
    String client_in_client_time;

    @SerializedName("client_in_vehicle_time")
    String client_in_vehicle_time;

    @SerializedName("client_in_vehicle_lat")
    Double client_in_vehicle_lat;

    @SerializedName("client_in_vehicle_lng")
    Double client_in_vehicle_lng;

    @SerializedName("driver_refused_with_client_in_lat")
    Double driver_refused_with_client_in_lat;

    @SerializedName("driver_refused_with_client_in_lng")
    Double driver_refused_with_client_in_lng;

    @SerializedName("driver_refused_with_client_in_time")
    String driver_refused_with_client_in_time;

    @SerializedName("client_out_client_lat")
    Double client_out_client_lat;

    @SerializedName("client_out_client_lng")
    Double client_out_client_lng;

    @SerializedName("client_out_client_time")
    String client_out_client_time;

    @SerializedName("client_out_vehicle_time")
    String client_out_vehicle_time;

    @SerializedName("client_out_vehicle_lat")
    Double client_out_vehicle_lat;

    @SerializedName("client_out_vehicle_lng")
    Double client_out_vehicle_lng;

    @SerializedName("vehicle")
    NearbyVehicle nearbyVehicle;

    @SerializedName("distance_params")
    DistanceParam distance_params;

    public CheckRequestResponse(){

    }

    public int getId() {
        return id;
    }

    public String getClient_id() {
        return client_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public Double getClient_requested_lat() {
        return client_requested_lat;
    }

    public Double getClient_requested_lng() {
        return client_requested_lng;
    }

    public Double getClient_requested_client_lat() {
        return client_requested_client_lat;
    }

    public Double getClient_requested_client_lng() {
        return client_requested_client_lng;
    }

    public String getClient_requested_time() {
        return client_requested_time;
    }

    public Double getClient_requested_vehicle_lat() {
        return client_requested_vehicle_lat;
    }

    public Double getClient_requested_vehicle_lng() {
        return client_requested_vehicle_lng;
    }

    public Double getDriver_accepted_vehicle_lat() {
        return driver_accepted_vehicle_lat;
    }

    public Double getDriver_accepted_vehicle_lng() {
        return driver_accepted_vehicle_lng;
    }

    public String getDriver_accepted_time() {
        return driver_accepted_time;
    }

    public Double getDriver_accepted_client_lat() {
        return driver_accepted_client_lat;
    }

    public Double getDriver_accepted_client_lng() {
        return driver_accepted_client_lng;
    }

    public Double getDriver_refused_on_pending_vehicle_lat() {
        return driver_refused_on_pending_vehicle_lat;
    }

    public Double getDriver_refused_on_pending_vehicle_lng() {
        return driver_refused_on_pending_vehicle_lng;
    }

    public String getDriver_refused_on_pending_time() {
        return driver_refused_on_pending_time;
    }

    public Double getDriver_refused_on_pending_client_lat() {
        return driver_refused_on_pending_client_lat;
    }

    public Double getDriver_refused_on_pending_client_lng() {
        return driver_refused_on_pending_client_lng;
    }

    public Double getClient_refused_on_pending_vehicle_lat() {
        return client_refused_on_pending_vehicle_lat;
    }

    public Double getClient_refused_on_pending_vehicle_lng() {
        return client_refused_on_pending_vehicle_lng;
    }

    public String getClient_refused_on_pending_time() {
        return client_refused_on_pending_time;
    }

    public Double getClient_refused_on_pending_client_lat() {
        return client_refused_on_pending_client_lat;
    }

    public Double getClient_refused_on_pending_client_lng() {
        return client_refused_on_pending_client_lng;
    }

    public Double getDriver_refused_ongoing_to_client_vehicle_lat() {
        return driver_refused_ongoing_to_client_vehicle_lat;
    }

    public Double getDriver_refused_ongoing_to_client_vehicle_lng() {
        return driver_refused_ongoing_to_client_vehicle_lng;
    }

    public String getDriver_refused_ongoing_to_client_time() {
        return driver_refused_ongoing_to_client_time;
    }

    public Double getDriver_refused_ongoing_to_client_client_lat() {
        return driver_refused_ongoing_to_client_client_lat;
    }

    public Double getDriver_refused_ongoing_to_client_client_lng() {
        return driver_refused_ongoing_to_client_client_lng;
    }

    public Double getClient_refused_ongoing_to_client_vehicle_lat() {
        return client_refused_ongoing_to_client_vehicle_lat;
    }

    public Double getClient_refused_ongoing_to_client_vehicle_lng() {
        return client_refused_ongoing_to_client_vehicle_lng;
    }

    public String getClient_refused_ongoing_to_client_time() {
        return client_refused_ongoing_to_client_time;
    }

    public Double getClient_refused_ongoing_to_client_client_lat() {
        return client_refused_ongoing_to_client_client_lat;
    }

    public Double getClient_refused_ongoing_to_client_client_lng() {
        return client_refused_ongoing_to_client_client_lng;
    }

    public Double getDriver_onwaiting_vehicle_lat() {
        return driver_onwaiting_vehicle_lat;
    }

    public Double getDriver_onwaiting_vehicle_lng() {
        return driver_onwaiting_vehicle_lng;
    }

    public String getDriver_onwaiting_time() {
        return driver_onwaiting_time;
    }

    public Double getDriver_onwaiting_client_lat() {
        return driver_onwaiting_client_lat;
    }

    public Double getDriver_onwaiting_client_lng() {
        return driver_onwaiting_client_lng;
    }

    public Double getDriver_refused_onwaiting_vehicle_lat() {
        return driver_refused_onwaiting_vehicle_lat;
    }

    public Double getDriver_refused_onwaiting_vehicle_lng() {
        return driver_refused_onwaiting_vehicle_lng;
    }

    public String getDriver_refused_onwaiting_time() {
        return driver_refused_onwaiting_time;
    }

    public Double getDriver_refused_onwaiting_client_lat() {
        return driver_refused_onwaiting_client_lat;
    }

    public Double getDriver_refused_onwaiting_client_lng() {
        return driver_refused_onwaiting_client_lng;
    }

    public Double getClient_refused_onwaiting_vehicle_lat() {
        return client_refused_onwaiting_vehicle_lat;
    }

    public Double getClient_refused_onwaiting_vehicle_lng() {
        return client_refused_onwaiting_vehicle_lng;
    }

    public String getClient_refused_onwaiting_time() {
        return client_refused_onwaiting_time;
    }

    public Double getClient_refused_onwaiting_client_lat() {
        return client_refused_onwaiting_client_lat;
    }

    public Double getClient_refused_onwaiting_client_lng() {
        return client_refused_onwaiting_client_lng;
    }

    public Double getClient_in_client_lat() {
        return client_in_client_lat;
    }

    public Double getClient_in_client_lng() {
        return client_in_client_lng;
    }

    public String getClient_in_client_time() {
        return client_in_client_time;
    }

    public String getClient_in_vehicle_time() {
        return client_in_vehicle_time;
    }

    public Double getClient_in_vehicle_lat() {
        return client_in_vehicle_lat;
    }

    public Double getClient_in_vehicle_lng() {
        return client_in_vehicle_lng;
    }

    public Double getDriver_refused_with_client_in_lat() {
        return driver_refused_with_client_in_lat;
    }

    public Double getDriver_refused_with_client_in_lng() {
        return driver_refused_with_client_in_lng;
    }

    public String getDriver_refused_with_client_in_time() {
        return driver_refused_with_client_in_time;
    }

    public Double getClient_out_client_lat() {
        return client_out_client_lat;
    }

    public Double getClient_out_client_lng() {
        return client_out_client_lng;
    }

    public String getClient_out_client_time() {
        return client_out_client_time;
    }

    public String getClient_out_vehicle_time() {
        return client_out_vehicle_time;
    }

    public Double getClient_out_vehicle_lat() {
        return client_out_vehicle_lat;
    }

    public Double getClient_out_vehicle_lng() {
        return client_out_vehicle_lng;
    }

    public NearbyVehicle getNearbyVehicle() {
        return nearbyVehicle;
    }

    public DistanceParam getDistance_params() {
        return distance_params;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public void setClient_requested_lat(Double client_requested_lat) {
        this.client_requested_lat = client_requested_lat;
    }

    public void setClient_requested_lng(Double client_requested_lng) {
        this.client_requested_lng = client_requested_lng;
    }

    public void setClient_requested_client_lat(Double client_requested_client_lat) {
        this.client_requested_client_lat = client_requested_client_lat;
    }

    public void setClient_requested_client_lng(Double client_requested_client_lng) {
        this.client_requested_client_lng = client_requested_client_lng;
    }

    public void setClient_requested_time(String client_requested_time) {
        this.client_requested_time = client_requested_time;
    }

    public void setClient_requested_vehicle_lat(Double client_requested_vehicle_lat) {
        this.client_requested_vehicle_lat = client_requested_vehicle_lat;
    }

    public void setClient_requested_vehicle_lng(Double client_requested_vehicle_lng) {
        this.client_requested_vehicle_lng = client_requested_vehicle_lng;
    }

    public void setDriver_accepted_vehicle_lat(Double driver_accepted_vehicle_lat) {
        this.driver_accepted_vehicle_lat = driver_accepted_vehicle_lat;
    }

    public void setDriver_accepted_vehicle_lng(Double driver_accepted_vehicle_lng) {
        this.driver_accepted_vehicle_lng = driver_accepted_vehicle_lng;
    }

    public void setDriver_accepted_time(String driver_accepted_time) {
        this.driver_accepted_time = driver_accepted_time;
    }

    public void setDriver_accepted_client_lat(Double driver_accepted_client_lat) {
        this.driver_accepted_client_lat = driver_accepted_client_lat;
    }

    public void setDriver_accepted_client_lng(Double driver_accepted_client_lng) {
        this.driver_accepted_client_lng = driver_accepted_client_lng;
    }

    public void setDriver_refused_on_pending_vehicle_lat(Double driver_refused_on_pending_vehicle_lat) {
        this.driver_refused_on_pending_vehicle_lat = driver_refused_on_pending_vehicle_lat;
    }

    public void setDriver_refused_on_pending_vehicle_lng(Double driver_refused_on_pending_vehicle_lng) {
        this.driver_refused_on_pending_vehicle_lng = driver_refused_on_pending_vehicle_lng;
    }

    public void setDriver_refused_on_pending_time(String driver_refused_on_pending_time) {
        this.driver_refused_on_pending_time = driver_refused_on_pending_time;
    }

    public void setDriver_refused_on_pending_client_lat(Double driver_refused_on_pending_client_lat) {
        this.driver_refused_on_pending_client_lat = driver_refused_on_pending_client_lat;
    }

    public void setDriver_refused_on_pending_client_lng(Double driver_refused_on_pending_client_lng) {
        this.driver_refused_on_pending_client_lng = driver_refused_on_pending_client_lng;
    }

    public void setClient_refused_on_pending_vehicle_lat(Double client_refused_on_pending_vehicle_lat) {
        this.client_refused_on_pending_vehicle_lat = client_refused_on_pending_vehicle_lat;
    }

    public void setClient_refused_on_pending_vehicle_lng(Double client_refused_on_pending_vehicle_lng) {
        this.client_refused_on_pending_vehicle_lng = client_refused_on_pending_vehicle_lng;
    }

    public void setClient_refused_on_pending_time(String client_refused_on_pending_time) {
        this.client_refused_on_pending_time = client_refused_on_pending_time;
    }

    public void setClient_refused_on_pending_client_lat(Double client_refused_on_pending_client_lat) {
        this.client_refused_on_pending_client_lat = client_refused_on_pending_client_lat;
    }

    public void setClient_refused_on_pending_client_lng(Double client_refused_on_pending_client_lng) {
        this.client_refused_on_pending_client_lng = client_refused_on_pending_client_lng;
    }

    public void setDriver_refused_ongoing_to_client_vehicle_lat(Double driver_refused_ongoing_to_client_vehicle_lat) {
        this.driver_refused_ongoing_to_client_vehicle_lat = driver_refused_ongoing_to_client_vehicle_lat;
    }

    public void setDriver_refused_ongoing_to_client_vehicle_lng(Double driver_refused_ongoing_to_client_vehicle_lng) {
        this.driver_refused_ongoing_to_client_vehicle_lng = driver_refused_ongoing_to_client_vehicle_lng;
    }

    public void setDriver_refused_ongoing_to_client_time(String driver_refused_ongoing_to_client_time) {
        this.driver_refused_ongoing_to_client_time = driver_refused_ongoing_to_client_time;
    }

    public void setDriver_refused_ongoing_to_client_client_lat(Double driver_refused_ongoing_to_client_client_lat) {
        this.driver_refused_ongoing_to_client_client_lat = driver_refused_ongoing_to_client_client_lat;
    }

    public void setDriver_refused_ongoing_to_client_client_lng(Double driver_refused_ongoing_to_client_client_lng) {
        this.driver_refused_ongoing_to_client_client_lng = driver_refused_ongoing_to_client_client_lng;
    }

    public void setClient_refused_ongoing_to_client_vehicle_lat(Double client_refused_ongoing_to_client_vehicle_lat) {
        this.client_refused_ongoing_to_client_vehicle_lat = client_refused_ongoing_to_client_vehicle_lat;
    }

    public void setClient_refused_ongoing_to_client_vehicle_lng(Double client_refused_ongoing_to_client_vehicle_lng) {
        this.client_refused_ongoing_to_client_vehicle_lng = client_refused_ongoing_to_client_vehicle_lng;
    }

    public void setClient_refused_ongoing_to_client_time(String client_refused_ongoing_to_client_time) {
        this.client_refused_ongoing_to_client_time = client_refused_ongoing_to_client_time;
    }

    public void setClient_refused_ongoing_to_client_client_lat(Double client_refused_ongoing_to_client_client_lat) {
        this.client_refused_ongoing_to_client_client_lat = client_refused_ongoing_to_client_client_lat;
    }

    public void setClient_refused_ongoing_to_client_client_lng(Double client_refused_ongoing_to_client_client_lng) {
        this.client_refused_ongoing_to_client_client_lng = client_refused_ongoing_to_client_client_lng;
    }

    public void setDriver_onwaiting_vehicle_lat(Double driver_onwaiting_vehicle_lat) {
        this.driver_onwaiting_vehicle_lat = driver_onwaiting_vehicle_lat;
    }

    public void setDriver_onwaiting_vehicle_lng(Double driver_onwaiting_vehicle_lng) {
        this.driver_onwaiting_vehicle_lng = driver_onwaiting_vehicle_lng;
    }

    public void setDriver_onwaiting_time(String driver_onwaiting_time) {
        this.driver_onwaiting_time = driver_onwaiting_time;
    }

    public void setDriver_onwaiting_client_lat(Double driver_onwaiting_client_lat) {
        this.driver_onwaiting_client_lat = driver_onwaiting_client_lat;
    }

    public void setDriver_onwaiting_client_lng(Double driver_onwaiting_client_lng) {
        this.driver_onwaiting_client_lng = driver_onwaiting_client_lng;
    }

    public void setDriver_refused_onwaiting_vehicle_lat(Double driver_refused_onwaiting_vehicle_lat) {
        this.driver_refused_onwaiting_vehicle_lat = driver_refused_onwaiting_vehicle_lat;
    }

    public void setDriver_refused_onwaiting_vehicle_lng(Double driver_refused_onwaiting_vehicle_lng) {
        this.driver_refused_onwaiting_vehicle_lng = driver_refused_onwaiting_vehicle_lng;
    }

    public void setDriver_refused_onwaiting_time(String driver_refused_onwaiting_time) {
        this.driver_refused_onwaiting_time = driver_refused_onwaiting_time;
    }

    public void setDriver_refused_onwaiting_client_lat(Double driver_refused_onwaiting_client_lat) {
        this.driver_refused_onwaiting_client_lat = driver_refused_onwaiting_client_lat;
    }

    public void setDriver_refused_onwaiting_client_lng(Double driver_refused_onwaiting_client_lng) {
        this.driver_refused_onwaiting_client_lng = driver_refused_onwaiting_client_lng;
    }

    public void setClient_refused_onwaiting_vehicle_lat(Double client_refused_onwaiting_vehicle_lat) {
        this.client_refused_onwaiting_vehicle_lat = client_refused_onwaiting_vehicle_lat;
    }

    public void setClient_refused_onwaiting_vehicle_lng(Double client_refused_onwaiting_vehicle_lng) {
        this.client_refused_onwaiting_vehicle_lng = client_refused_onwaiting_vehicle_lng;
    }

    public void setClient_refused_onwaiting_time(String client_refused_onwaiting_time) {
        this.client_refused_onwaiting_time = client_refused_onwaiting_time;
    }

    public void setClient_refused_onwaiting_client_lat(Double client_refused_onwaiting_client_lat) {
        this.client_refused_onwaiting_client_lat = client_refused_onwaiting_client_lat;
    }

    public void setClient_refused_onwaiting_client_lng(Double client_refused_onwaiting_client_lng) {
        this.client_refused_onwaiting_client_lng = client_refused_onwaiting_client_lng;
    }

    public void setClient_in_client_lat(Double client_in_client_lat) {
        this.client_in_client_lat = client_in_client_lat;
    }

    public void setClient_in_client_lng(Double client_in_client_lng) {
        this.client_in_client_lng = client_in_client_lng;
    }

    public void setClient_in_client_time(String client_in_client_time) {
        this.client_in_client_time = client_in_client_time;
    }

    public void setClient_in_vehicle_time(String client_in_vehicle_time) {
        this.client_in_vehicle_time = client_in_vehicle_time;
    }

    public void setClient_in_vehicle_lat(Double client_in_vehicle_lat) {
        this.client_in_vehicle_lat = client_in_vehicle_lat;
    }

    public void setClient_in_vehicle_lng(Double client_in_vehicle_lng) {
        this.client_in_vehicle_lng = client_in_vehicle_lng;
    }

    public void setDriver_refused_with_client_in_lat(Double driver_refused_with_client_in_lat) {
        this.driver_refused_with_client_in_lat = driver_refused_with_client_in_lat;
    }

    public void setDriver_refused_with_client_in_lng(Double driver_refused_with_client_in_lng) {
        this.driver_refused_with_client_in_lng = driver_refused_with_client_in_lng;
    }

    public void setDriver_refused_with_client_in_time(String driver_refused_with_client_in_time) {
        this.driver_refused_with_client_in_time = driver_refused_with_client_in_time;
    }

    public void setClient_out_client_lat(Double client_out_client_lat) {
        this.client_out_client_lat = client_out_client_lat;
    }

    public void setClient_out_client_lng(Double client_out_client_lng) {
        this.client_out_client_lng = client_out_client_lng;
    }

    public void setClient_out_client_time(String client_out_client_time) {
        this.client_out_client_time = client_out_client_time;
    }

    public void setClient_out_vehicle_time(String client_out_vehicle_time) {
        this.client_out_vehicle_time = client_out_vehicle_time;
    }

    public void setClient_out_vehicle_lat(Double client_out_vehicle_lat) {
        this.client_out_vehicle_lat = client_out_vehicle_lat;
    }

    public void setClient_out_vehicle_lng(Double client_out_vehicle_lng) {
        this.client_out_vehicle_lng = client_out_vehicle_lng;
    }

    public void setNearbyVehicle(NearbyVehicle nearbyVehicle) {
        this.nearbyVehicle = nearbyVehicle;
    }

    public void setDistance_params(DistanceParam distance_params) {
        this.distance_params = distance_params;
    }
}
