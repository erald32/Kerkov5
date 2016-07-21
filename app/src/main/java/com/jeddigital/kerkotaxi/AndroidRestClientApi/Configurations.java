package com.jeddigital.kerkotaxi.AndroidRestClientApi;

/**
 * Created by Erald.Kerluku on 7/20/2016.
 */
public class Configurations {

    public static final String WEB_SERVICE_BASE_URL = "http://jeddigital.com/kerko_taxi/services/clients/";
    public static final String UPDATE_KLIENT_LOCATION_URL = WEB_SERVICE_BASE_URL + "updateClientLocation.php";
    public static final String GET_NEARBY_TAXIS_URL = WEB_SERVICE_BASE_URL + "getNearbyTaxis.php";
    public static final String REQUEST_TAXI_URL = WEB_SERVICE_BASE_URL + "requestTaxi.php";
    public static final String CANCEL_REQUEST_URL = WEB_SERVICE_BASE_URL + "cancelRequest.php";



    public static final String RESPONSE_ERROR = "error";
    public static final String RESPONSE_SUCCESS = "Success";
    public static final String BOOKING_NOT_VALID = "booking_not_valid";


}
