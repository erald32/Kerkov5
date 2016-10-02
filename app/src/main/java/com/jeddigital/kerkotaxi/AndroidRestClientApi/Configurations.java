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
    public static final String CHECK_REQUEST_STATUS_URL = WEB_SERVICE_BASE_URL + "checkRequestStatus.php";

    public static  final int CHECK_REQUEST_STATUS_HOMEPAGE_SERVICE_INTERVAL = 5000;
    public static  final int CHECK_REQUEST_STATUS_BACKGROUND_SERVICE_INTERVAL = 5000;



}
