package com.jeddigital.kerkotaxi;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jeddigital.kerkotaxi.AndroidRestClientApi.Configurations;
import com.jeddigital.kerkotaxi.AnroidRestModels.NearbyVehicle;
import com.jeddigital.kerkotaxi.GSON.BooleanTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuHyreseActivity extends FragmentActivity implements LocationListener {

    private GoogleMap map;
    Location client_live_location;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(boolean.class, new BooleanTypeAdapter());
        gson = builder.create();


        if (!isGooglePlayServicesAvailable()) {
            finish();                                                                               // ketu mund ti nxjerrim nje warning qe duhet te update ose instaloje google play services
        }

        setContentView(R.layout.activity_menu_hyrese);

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = supportMapFragment.getMap();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        client_live_location = locationManager.getLastKnownLocation(bestProvider);
        if (client_live_location != null) {
            onLocationChanged(client_live_location);
        }

        locationManager.requestLocationUpdates(bestProvider, 10000, 0, this);


        //rest_update_client_location(client_live_location, "1");

        getNearbyVehicles(client_live_location, "1");
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        client_live_location = location;
        double client_latitude = client_live_location.getLatitude();
        double client_longitude = client_live_location.getLongitude();
        LatLng latLng = new LatLng(client_latitude, client_longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 14.0F));

        rest_update_client_location(client_live_location, "1");
        Log.d("Location changed","yes");
    }

    private void rest_update_client_location(Location location, final String client_id) {
        final String client_live_latitude = String.valueOf(location.getLatitude()).toString().trim();
        final String client_live_longitude = String.valueOf(location.getLongitude()).toString().trim();

        StringRequest postRequest = new StringRequest(Request.Method.POST, Configurations.UPDATE_KLIENT_LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int error_code = jsonResponse.getInt("error_code");
                            String error_code_desc = jsonResponse.getString("error_code_desc");
                            if (error_code == 0) {
                                Log.d("qqq", "koordinatat u derguan me sukses");
                            } else {
                                Log.d("qqq", error_code_desc);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("client_id", client_id);
                params.put("client_lat", client_live_latitude);
                params.put("client_lng", client_live_longitude);

                Log.d("qqq_liveLocation_params", client_live_latitude);
                Log.d("qqq_liveLocation_params", client_live_longitude);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

    private void getNearbyVehicles(Location location, final String client_id) {
        //  SharedPreferences Preferencat_Klient = getSharedPreferences(Configurations.SHARED_PREF_CLIENT, Context.MODE_PRIVATE);

        // final String getNearbyTaxis= "getNearbyTaxis";

        final String client_live_latitude = String.valueOf(location.getLatitude()).toString().trim();
        final String client_live_longitude = String.valueOf(location.getLongitude()).toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.GET_NEARBY_TAXIS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseJSONObject;
                        try {
                            responseJSONObject = new JSONObject(response);

                            int error_code = responseJSONObject.getInt("error_code");
                            String error_code_desc = responseJSONObject.getString("error_code_desc");
                            JSONArray nearbyVehiclesJSONArray = responseJSONObject.getJSONArray("nearby_vehicles");

                            List<NearbyVehicle> nearbyVehicles = gson.fromJson(responseJSONObject.getString("nearby_vehicles"), new TypeToken<List<NearbyVehicle>>(){}.getType());
                            nearbyVehicles.get(0);
                            Log.d("qqq", ""+ nearbyVehicles.get(0));
                            nearbyVehicles.size();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("qqq", "ErrorResponseOnLocationChanged: " + error.getMessage());
                if (error instanceof NoConnectionError) {
                    Log.d("qqq", "NoConnectionError: " + error.getMessage());
                } else if (error instanceof TimeoutError) {
                } else if (error instanceof AuthFailureError) {
                    Log.d("qqq", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.d("qqq", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.d("qqq", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.d("qqq", "ParseError: " + error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("getNearbyTaxis", getNearbyTaxis);
                params.put("client_id", client_id);
                params.put("client_lat", client_live_latitude);
                params.put("client_lng", client_live_longitude);

                Log.d("qqq send", client_live_latitude);
                Log.d("qqq send ", client_live_longitude);
       /*       SharedPreferences Preferencat_Klient = getSharedPreferences(Configurations.SHARED_PREF_CLIENT, Context.MODE_PRIVATE);
                SharedPreferences.Editor set_Prefs = Preferencat_Klient.edit();
                set_Prefs.putString(Configurations.CLIENT_LATITUDE_PREF, client_live_latitude);
                set_Prefs.putString(Configurations.CLIENT_LONGITUDE_PREF, client_live_longitude);
                set_Prefs.commit();
        */
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void rest_request_taxi(final int vehicle_id, final String client_id, final LatLng requested_location) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.REQUEST_TAXI_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseJSONObject;
                        try {
                            responseJSONObject = new JSONObject(response);

                            int error_code = responseJSONObject.getInt("error_code");
                            String error_code_desc = responseJSONObject.getString("error_code_desc");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("qqq", "ErrorResponseOnLocationChanged: " + error.getMessage());
                if (error instanceof NoConnectionError) {
                    Log.d("qqq", "NoConnectionError: " + error.getMessage());
                } else if (error instanceof TimeoutError) {
                } else if (error instanceof AuthFailureError) {
                    Log.d("qqq", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.d("qqq", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.d("qqq", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.d("qqq", "ParseError: " + error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("getNearbyTaxis", getNearbyTaxis);
                params.put("client_id", client_id);
                params.put("vehicle_id", ""+vehicle_id);
                params.put("requested_lat", ""+requested_location.latitude);
                params.put("requested_lat", ""+requested_location.longitude);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void rest_cancel_request(final int booking_id, final int vehicle_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configurations.CANCEL_REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject responseJSONObject;
                        try {
                            responseJSONObject = new JSONObject(response);

                            int error_code = responseJSONObject.getInt("error_code");
                            String error_code_desc = responseJSONObject.getString("error_code_desc");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("qqq", "ErrorResponseOnLocationChanged: " + error.getMessage());
                if (error instanceof NoConnectionError) {
                    Log.d("qqq", "NoConnectionError: " + error.getMessage());
                } else if (error instanceof TimeoutError) {
                } else if (error instanceof AuthFailureError) {
                    Log.d("qqq", "AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.d("qqq", "ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.d("qqq", "NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.d("qqq", "ParseError: " + error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("getNearbyTaxis", getNearbyTaxis);
                params.put("booking_id", ""+booking_id);
                params.put("vehicle_id", ""+vehicle_id);

                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}